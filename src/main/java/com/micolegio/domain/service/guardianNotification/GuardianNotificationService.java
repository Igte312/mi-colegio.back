package com.micolegio.domain.service.guardianNotification;

import com.micolegio.adapters.db.IGuardianNotificationRepository;
import com.micolegio.domain.service.dto.response.GuardianNotificationResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GuardianNotificationService implements IGuardianNotificationService {

    private final IGuardianNotificationRepository guardianNotificationRepository;
    private final JavaMailSender mailSender;

    // Inyectamos el correo del remitente directamente desde las propiedades de Spring
    @Value("${spring.mail.username}")
    private String senderEmail;

    public GuardianNotificationService(IGuardianNotificationRepository guardianNotificationRepository, JavaMailSender mailSender) {
        this.guardianNotificationRepository = guardianNotificationRepository;
        this.mailSender = mailSender;
    }

    @Override
    public void sendSupplyListNotification(Long courseId) {
        System.out.println("DEBUG: Correo del remitente configurado: " + senderEmail);
        List<GuardianNotificationResponse> guardians = guardianNotificationRepository.findGuardiansByCourseId(courseId);

        // Eliminamos la lectura de variables de entorno, ya que Spring ya configuró mailSender
        // No es necesario verificar si el email o password existen aquí.

        for (GuardianNotificationResponse guardian : guardians) {
            SimpleMailMessage message = new SimpleMailMessage();

            // Usamos el email del remitente inyectado
            message.setFrom(senderEmail);
            message.setTo(guardian.getGuardianEmail());
            message.setSubject("Lista de Útiles Escolares - " + guardian.getStudentName());
            message.setText(String.format(
                    "Estimado %s,\n\n" +
                            "Le informamos que se ha actualizado la lista de útiles escolares para su pupilo %s.\n\n" +
                            "Por favor, revise el sistema para ver los detalles actualizados.\n\n" +
                            "Atentamente,\n" +
                            "Equipo del Colegio",
                    guardian.getGuardianName(),
                    guardian.getStudentName()
            ));

            // El JavaMailSender ahora usará la Contraseña de Aplicación para autenticarse
            mailSender.send(message);
        }
    }
}
