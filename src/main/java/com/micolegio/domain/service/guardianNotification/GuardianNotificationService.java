package com.micolegio.domain.service.guardianNotification;

import com.micolegio.adapters.db.IGuardianNotificationRepository;
import com.micolegio.adapters.db.ISchoolSupplyRepository;
import com.micolegio.domain.service.cloudapi.SchoolSupplySearchService;
import com.micolegio.domain.service.dto.response.ComercioComparacion;
import com.micolegio.domain.service.dto.response.ComparacionResponse;
import com.micolegio.domain.service.dto.response.CourseSupplyResponse;
import com.micolegio.domain.service.dto.response.GuardianNotificationResponse;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuardianNotificationService implements IGuardianNotificationService {

    // tus repositorios, mailSender y searchService
    private final IGuardianNotificationRepository guardianNotificationRepository;
    private final JavaMailSender mailSender;
    private final SchoolSupplySearchService searchService;
    private final ISchoolSupplyRepository schoolSupplyRepository;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public GuardianNotificationService(IGuardianNotificationRepository guardianNotificationRepository,
                                       JavaMailSender mailSender,
                                       SchoolSupplySearchService searchService,
                                       ISchoolSupplyRepository schoolSupplyRepository) {
        this.guardianNotificationRepository = guardianNotificationRepository;
        this.mailSender = mailSender;
        this.searchService = searchService;
        this.schoolSupplyRepository = schoolSupplyRepository;
    }

    // --- Métodos para preview ---
    @Override
    public List<CourseSupplyResponse> getSuppliesForPreview(Long schoolId, Long courseId) {
        return schoolSupplyRepository.getSuppliesByCourseId(schoolId, courseId);
    }

    @Override
    public ComparacionResponse getComparacionForPreview(List<CourseSupplyResponse> supplies) {
        return searchService.buscarMejoresPrecios(supplies);
    }

    @Override
    public String buildHtmlEmailWithLogoCorner(List<CourseSupplyResponse> supplies,
                                               ComparacionResponse comparacion,
                                               GuardianNotificationResponse guardian) {
        StringBuilder html = new StringBuilder();

        html.append("<html><head>");
        html.append("<style>")
                .append("body { font-family: Arial, sans-serif; background-color: #f7f7f7; padding: 20px; }")
                .append("h2 { color: #2a7ae2; }")
                .append("table { width: 100%; border-collapse: collapse; margin-top: 15px; }")
                .append("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }")
                .append("th { background-color: #2a7ae2; color: white; }")
                .append("tr:nth-child(even) { background-color: #f2f2f2; }")
                .append(".logo { float: right; width: 150px; margin-left: 10px; }")
                .append(".header { overflow: hidden; margin-bottom: 20px; }")
                .append("</style>");
        html.append("</head><body>");

        // Header con logo
        html.append("<div class='header'>")
                .append("<h1 style='display:inline-block;'>Notificación de Útiles Escolares</h1>")
                .append("<img src='https://www.ejemplo.com/logo.png' alt='Logo Colegio' class='logo'>")
                .append("</div>");

        // Saludo personalizado
        html.append("<p>Estimado/a ").append(guardian.getGuardianName())
                .append(",<br><br>")
                .append("Le informamos que se ha generado la nueva lista de útiles escolares para su pupilo ")
                .append(guardian.getStudentName())
                .append(".<br>Hemos agregado una tabla de compras donde podrá encontrar recomendaciones de comercios con sus mejores ofertas sugeridas por una inteligencia artificial.<br><br>")
                ;

        // Lista de útiles
        html.append("<h2>Lista de Útiles</h2>");
        html.append("<ol>");
        for (CourseSupplyResponse s : supplies) {
            html.append("<li>")
                    .append(s.getName())
                    .append(" - ")
                    .append(s.getDescription())
                    .append(" (Cantidad: ").append(s.getQuantity()).append(")")
                    .append("</li>");
        }
        html.append("</ol>");

        // Tabla de comparaciones
        html.append("<h2>Mejores Opciones de Compra Generadas con Inteligencia Artificial</h2>");
        html.append("<table>");
        html.append("<tr>")
                .append("<th>Ranking</th>")
                .append("<th>Comercio</th>")
                .append("<th>URL</th>")
                .append("<th>Delivery</th>")
                .append("<th>Precio Total</th>")
                .append("<th>Costo Delivery</th>")
                //.append("<th>Observaciones</th>")
                .append("</tr>");

        for (ComercioComparacion c : comparacion.getComparacion()) {
            html.append("<tr>")
                    .append("<td>").append(c.getRanking()).append("</td>")
                    .append("<td>").append(c.getComercio()).append("</td>")
                    .append("<td><a href='").append(c.getUrl()).append("'>").append(c.getUrl()).append("</a></td>")
                    .append("<td>").append(c.getTieneDelivery() ? "Sí" : "No").append("</td>")
                    .append("<td>").append(c.getPrecioTotal()).append("</td>")
                    .append("<td>").append(c.getCostoDelivery()).append("</td>")
                    //.append("<td>").append(c.getObservaciones()).append("</td>")
                    .append("</tr>");
        }
        html.append("</table>");

        // Resumen destacado
        html.append("<h3>Resumen:</h3>");
        html.append("<div style='background-color:#e8f0fe;padding:10px;border-left:5px solid #2a7ae2;'>")
                .append(comparacion.getResumen())
                .append("</div><br>");
        html.append("<h3>Atentamente:</h3>");
        html.append("<h3>Equipo MiColegio:</h3>");
        html.append("</body></html>");

        return html.toString();
    }

    // --- Método de envío real ---
    @Override
    public void sendSupplyListNotification(Long courseId, Long schoolId) throws MessagingException {
        List<GuardianNotificationResponse> guardians = guardianNotificationRepository.findGuardiansByCourseId(courseId);
        List<CourseSupplyResponse> supplies = schoolSupplyRepository.getSuppliesByCourseId(schoolId, courseId);
        ComparacionResponse response = searchService.buscarMejoresPrecios(supplies);

        for (GuardianNotificationResponse guardian : guardians) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            // true = multipart para poder adjuntar imágenes
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            // Reemplaza el logo en el HTML por el CID
            String htmlBody = buildHtmlEmailWithLogoCorner(supplies, response, guardian)
                    .replace("https://www.ejemplo.com/logo.png", "cid:logo_cid");

            helper.setText(htmlBody, true); // true = HTML
            helper.setTo(guardian.getGuardianEmail());
            helper.setSubject("Lista de Útiles Escolares - " + guardian.getStudentName());
            helper.setFrom(senderEmail);

            // Adjuntar el logo desde resources
            helper.addInline("logo_cid", new ClassPathResource("static/logoMicolegio.png"));

            mailSender.send(mimeMessage);
        }
    }
}


