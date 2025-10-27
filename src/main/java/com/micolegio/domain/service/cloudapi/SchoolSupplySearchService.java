package com.micolegio.domain.service.cloudapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.micolegio.config.ClaudeConfig;
import com.micolegio.domain.service.dto.request.ClaudeRequest;
import com.micolegio.domain.service.dto.response.ClaudeResponse;
import com.micolegio.domain.service.dto.response.ComparacionResponse;
import com.micolegio.domain.service.dto.response.CourseSupplyResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchoolSupplySearchService {

    private final ClaudeConfig claudeConfig;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public ComparacionResponse buscarMejoresPrecios(List<CourseSupplyResponse> supplies) {
        try {
            log.info("=== INICIANDO BÚSQUEDA DE PRECIOS ===");
            log.info("Útiles a buscar: {}", supplies.size());

            String prompt = construirPrompt(supplies);

            ClaudeRequest request = ClaudeRequest.builder()
                    .model(claudeConfig.getModel())
                    .maxTokens(claudeConfig.getMaxTokens())
                    .messages(List.of(
                            ClaudeRequest.Message.builder()
                                    .role("user")
                                    .content(prompt)
                                    .build()
                    ))
                    .build();

            ClaudeResponse response = llamarClaudeAPI(request);

            if (response == null || response.getContent() == null || response.getContent().isEmpty()) {
                throw new RuntimeException("Respuesta vacía de Claude");
            }

            String responseText = response.getContent().get(0).getText();

            log.info("=== RESPUESTA RECIBIDA ===");
            log.info("Longitud: {}", responseText.length());
            log.info("Primeros 200 caracteres: {}", responseText.substring(0, Math.min(200, responseText.length())));
            log.info("Últimos 200 caracteres: {}", responseText.substring(Math.max(0, responseText.length() - 200)));

            return parsearRespuesta(responseText);

        } catch (HttpClientErrorException e) {
            log.error("Error HTTP al llamar a Claude: Status: {}, Body: {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException("Error en la comunicación con Claude API: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Error al buscar precios: ", e);
            throw new RuntimeException("Error al procesar búsqueda de útiles escolares", e);
        }
    }

    private String construirPrompt(List<CourseSupplyResponse> supplies) throws JsonProcessingException {
        String suppliesJson = objectMapper.writeValueAsString(supplies);

        return """
            Tengo la siguiente lista de útiles escolares en formato JSON:
            
            %s
            
            INSTRUCCIONES:
            1. Extrae el "name" y "quantity" de cada útil de la lista.
            2. Busca en comercios chilenos (tiendas físicas y online) los precios de estos útiles.
            3. Compara AL MENOS 3 comercios diferentes.
            4. Calcula el precio total aproximado de toda la lista en cada comercio.
            5. Verifica si tienen servicio de delivery y su costo.
            
            COMERCIOS SUGERIDOS A REVISAR:
            - Librería Nacional (https://librerianacional.cl)
            - Lapiz Lopez (https://www.lapizlopez.cl)
            - Paris (https://www.paris.cl)
            - Falabella (https://www.falabella.com/falabella-cl)
            - Jumbo (https://www.jumbo.cl)
            
            IMPORTANTE:
            - Considera las cantidades indicadas en "quantity"
            - Usa precios actuales y realistas para Chile (año 2024)
            - Si un comercio no tiene un producto, considera alternativas similares
            - Los precios deben estar en pesos chilenos con el formato "$XX.XXX"
            
            RESPONDE ÚNICAMENTE CON EL SIGUIENTE JSON (sin texto adicional antes o después, sin markdown):
            
            {
              "comparacion": [
                {
                  "ranking": 1,
                  "comercio": "Nombre del comercio más barato",
                  "url": "https://url-del-comercio.cl",
                  "tieneDelivery": true,
                  "precioTotal": "$45.000",
                  "costoDelivery": "$3.000",
                  "observaciones": "Detalles relevantes, promociones, etc."
                },
                {
                  "ranking": 2,
                  "comercio": "Segundo comercio",
                  "url": "https://url-del-comercio.cl",
                  "tieneDelivery": false,
                  "precioTotal": "$48.000",
                  "costoDelivery": "No disponible",
                  "observaciones": "Detalles"
                },
                {
                  "ranking": 3,
                  "comercio": "Tercer comercio",
                  "url": "https://url-del-comercio.cl",
                  "tieneDelivery": true,
                  "precioTotal": "$52.000",
                  "costoDelivery": "$5.000",
                  "observaciones": "Detalles"
                }
              ],
              "resumen": "Análisis breve de la mejor opción y por qué es la más conveniente"
            }
            """.formatted(suppliesJson);
    }

    private ClaudeResponse llamarClaudeAPI(ClaudeRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", claudeConfig.getKey());
        headers.set("anthropic-version", claudeConfig.getVersion());

        HttpEntity<ClaudeRequest> entity = new HttpEntity<>(request, headers);

        try {
            // Log para debug (remover en producción)
            log.info("Llamando a Claude API: {}", claudeConfig.getUrl());
            log.info("Headers: {}", headers);

            return restTemplate.postForObject(
                    claudeConfig.getUrl(),
                    entity,
                    ClaudeResponse.class
            );
        } catch (Exception e) {
            log.error("Error en llamada a Claude API: ", e);
            throw new RuntimeException("Error al comunicarse con Claude API", e);
        }
    }

    private ComparacionResponse parsearRespuesta(String responseText) throws JsonProcessingException {
        log.info("=== RESPUESTA DE CLAUDE ===");
        log.info("Texto completo: {}", responseText);
        log.info("Longitud: {}", responseText.length());

        String jsonText = responseText.trim();

        // Intentar extraer JSON si viene con markdown
        if (jsonText.contains("```json")) {
            int startIndex = jsonText.indexOf("```json") + 7;
            int endIndex = jsonText.lastIndexOf("```");

            if (startIndex > 7 && endIndex > startIndex) {
                jsonText = jsonText.substring(startIndex, endIndex).trim();
            }
        } else if (jsonText.contains("```")) {
            int startIndex = jsonText.indexOf("```") + 3;
            int endIndex = jsonText.lastIndexOf("```");

            if (startIndex > 3 && endIndex > startIndex) {
                jsonText = jsonText.substring(startIndex, endIndex).trim();
            }
        }

        // Si empieza con { ya es JSON válido
        if (jsonText.startsWith("{")) {
            log.info("JSON extraído: {}", jsonText);
            return objectMapper.readValue(jsonText, ComparacionResponse.class);
        }

        // Buscar el primer { y último }
        int firstBrace = jsonText.indexOf("{");
        int lastBrace = jsonText.lastIndexOf("}");

        if (firstBrace >= 0 && lastBrace > firstBrace) {
            jsonText = jsonText.substring(firstBrace, lastBrace + 1);
            log.info("JSON extraído por llaves: {}", jsonText);
            return objectMapper.readValue(jsonText, ComparacionResponse.class);
        }

        log.error("No se pudo extraer JSON válido de la respuesta");
        throw new RuntimeException("No se pudo parsear la respuesta de Claude: " + responseText);
    }
}
