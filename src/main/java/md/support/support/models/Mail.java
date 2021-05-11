package md.support.support.models;

import md.support.support.repo.UserRepository;
import md.support.support.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {

    @Autowired
    private UserRepository userRepository;

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
            messages.setSubject(shop + " (Заявка с сайта!)", "UTF-8");

            // Now set the actual message
            messages.setText("Имя: " + name + "\n" + "Номер для связи: " + phone + "\n" + "Проблема: " + problem + "\n" + "Описание проблемы: " + message, "UTF-8");

            // Send message
            Transport.send(messages);
            System.out.println("Sent message successfully....");
        } catch (
                MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void sendMailToUser(String toUser, String name, String shop, String username, String password) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        try {
            // Create a default MimeMessage object.
            MimeMessage messages = new MimeMessage(session);

            // Set From: header field of the header.
            messages.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            messages.addRecipient(Message.RecipientType.TO, new InternetAddress(toUser));

            // Set Subject: header field
            messages.setSubject("Успешная регистрация!!!", "UTF-8");

            // Now set the actual message

            messages.setText("Поздравляем с успешной регистрацией " + name + ", теперь вы можете перейте по сылке " +
                    "http://support.web.md:8081/login и войти в программу для учета технических заявок." + "\n" + "\n" +
                    "Вы выбрали магазин : " + shop + " ." + "\n" +
                    "Ваши данные для входа в программу:" + "\n" + "Имя пользователя : " + username + "\n" + "Пароль : " + password + "\n" +
                    "\n" + "Благодарим за регистрацию !!!", "UTF-8");

            // Send message
            Transport.send(messages);
            // System.out.println("Sent message successfully....");
        } catch (
                MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public void sendEmailToConfirmationRequest(Request request, String userEmail) {
        Properties properties = System.getProperties();
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties);
        try {
            MimeMessage messages = new MimeMessage(session);
            messages.setFrom(new InternetAddress(from));
            messages.addRecipient(Message.RecipientType.TO, new InternetAddress(userEmail));
            messages.setSubject("Ваша завявка успешно выполнена !!!", "UTF-8");
            messages.setText("Ваша заявка под №: " + request.getId() + "\n"
                            + "Магазин: " + request.getShop() + "\n"
                            + "Проблема: " + request.getProblem() + "\n"
                            + "Описинаие проблемы: " + request.getMessage() + "\n" + "\n"
                            + "Перейдите по сылке http://support.web.md:8081/login и подтвердите выполнение технической заявки. "
                    , "UTF-8");
            Transport.send(messages);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }
}

