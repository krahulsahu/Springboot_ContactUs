package com.example.contactus.service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.IContext;
import org.thymeleaf.spring6.ISpringTemplateEngine;

import org.thymeleaf.context.Context;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ISpringTemplateEngine templateEngine;


    public String sendSimpleMail(String to, String subject, String body){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("rahulkumarfast2002@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            return "Mail sent successfully";
        }catch (Exception e){
            e.printStackTrace();
            return "Error while sending mail: " + e.getMessage();
        }
    }
    public String sendHtmlMail(String to, String subject, String htmlBody) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, "utf-8");
            helper.setFrom("yourgmail@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = isHtml
            mailSender.send(mime);
            return "Mail sent (html)";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Failed: " + e.getMessage();
        }
    }

    public String sendTemplateMail(String to, String subject, String templateName, Context thymeleafContext) {
        try {
            String htmlContent = templateEngine.process(templateName, (IContext) thymeleafContext);
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, "utf-8");
            helper.setFrom("yourgmail@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(mime);
            return "Mail sent (template)";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Failed: " + e.getMessage();
        }
    }
    // 4. Mail with attachment (file from disk)
    public String sendMailWithAttachment(String to, String subject, String body, File attachment) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true);
            helper.setFrom("yourgmail@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);
            FileSystemResource resource = new FileSystemResource(attachment);
            helper.addAttachment(attachment.getName(), resource);
            mailSender.send(mime);
            return "Mail sent (attachment)";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Failed: " + e.getMessage();
        }
    }

    // 5. Mail with attachment where attachment is uploaded (MultipartFile)
    public String sendMailWithMultipartAttachment(String to, String subject, String body, MultipartFile file) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            // true = multipart
            MimeMessageHelper helper = new MimeMessageHelper(mime, true);
            helper.setFrom("yourgmail@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, false);

            // Convert MultipartFile -> InputStreamSource and attach
            InputStreamSource source = new InputStreamSource() {
                @Override
                public InputStream getInputStream() throws IOException {
                    return file.getInputStream();
                }
            };
            helper.addAttachment(file.getOriginalFilename(), source, file.getContentType());
            mailSender.send(mime);
            return "Mail sent (multipart attachment)";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Failed: " + e.getMessage();
        }
    }


}
