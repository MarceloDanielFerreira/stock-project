package com.app.stockproject.cron;

import com.app.stockproject.dto.LibroDto;
import com.app.stockproject.dto.UserDto;
import com.app.stockproject.email.EmailService;
import com.app.stockproject.service.LibroService;
import com.app.stockproject.service.ReportService;
import com.app.stockproject.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduledEmailSender {

    private final Logger logger = LoggerFactory.getLogger(ScheduledEmailSender.class);

    @Autowired
    private LibroService libroService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserService userService;

    @Scheduled(cron = "0 50 0 * * ?") // Ejecutar todos los días a las 00:15
    public void sendLowStockReport() {
        try {
            List<LibroDto> librosConBajoStock = libroService.getAllLibrosConBajoStock();

            // Obtener correos de todos los administradores
            List<String> adminEmails = userService.getAllAdminUsers().stream()
                    .map(UserDto::getEmail)
                    .toList();

            // Enviar el informe por correo electrónico si hay libros con bajo stock y administradores
            if (!librosConBajoStock.isEmpty() && !adminEmails.isEmpty()) {
                // Generar el informe como un archivo byte[]
                byte[] pdfReport = reportService.generateLowStockReport(librosConBajoStock);

                // Construir el texto del correo
                String subject = "Informe de Libros con Bajo Stock";
                String text = "Adjunto encontrarás el informe de libros con bajo stock.";

                // Envía el correo electrónico con el informe adjunto a todos los administradores
                emailService.sendEmail(adminEmails, subject, text, pdfReport, "Informe_Libros_Bajo_Stock.pdf");
            }
        } catch (Exception e) {
            // Maneja las excepciones según tus necesidades
            logger.error("Error en el cron para enviar el informe por correo electrónico", e);
        }
    }
}
