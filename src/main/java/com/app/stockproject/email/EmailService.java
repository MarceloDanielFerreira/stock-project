package com.app.stockproject.email;

import com.app.stockproject.cron.ScheduledEmailSender;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class EmailService {
    private final Logger logger = LoggerFactory.getLogger(ScheduledEmailSender.class);


    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(List<String> to, String subject, String text, byte[] attachment, String attachmentName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        // Convierte la lista de direcciones de correo electrónico a un array de Strings
        String[] toAddresses = to.toArray(new String[0]);

        helper.setTo(toAddresses);
        helper.setSubject(subject);
        helper.setText(text, true);

        // Agrega el adjunto al correo electrónico si está presente
        if (attachment != null && attachment.length > 0) {
            helper.addAttachment(attachmentName, new ByteArrayResource(attachment));
        }

        javaMailSender.send(message);
        logger.info("Se envio el correo");

    }
}
