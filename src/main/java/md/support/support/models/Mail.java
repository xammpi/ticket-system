package md.support.support.models;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {

    String to = "support-it@nr1.md";
    String from = "site@nr1.md";
    String host = "mail";

    public void sendMail(String shop, String message, String problem, String phone, String name) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage messages = new MimeMessage(session);

            // Set From: header field of the header.
            messages.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            messages.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            messages.setSubject(shop + " (Заявка с сайта!)");

            // Now set the actual message
            messages.setText("Имя: " + name + "\n" + "Номер для связи: " + phone + "\n" + "Проблема: " + problem + "\n" + "Описание проблемы: " + message);

            // Send message
            Transport.send(messages);
            System.out.println("Sent message successfully....");
        } catch (
                MessagingException mex) {
            mex.printStackTrace();
        }
    }
}

