package com.example.contactus.controller;


import com.example.contactus.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import java.io.File;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class MailController {

    @Autowired
    private MailService mailService;

    // Simple text mail (existing)
    @PostMapping("/sendMail")
    public String sendMail(@RequestBody Map<String,String> req) {
        return mailService.sendSimpleMail(req.get("to"), req.get("subject"), req.get("body"));
    }

    // HTML mail from raw HTML string
    @PostMapping("/sendHtmlMail")
    public String sendHtmlMail(@RequestBody Map<String,String> req) {
        return mailService.sendHtmlMail(req.get("to"), req.get("subject"), req.get("html"));
    }

    // Template mail (Thymeleaf)
    @PostMapping("/sendTemplateMail")
    public String sendTemplateMail(@RequestBody Map<String,String> req) {
        Context context = new Context();
        // set values used in the template: title, message etc.
        context.setVariable("title", req.getOrDefault("title", "Hello"));
        context.setVariable("message", req.getOrDefault("message", "This is a template email."));
        return mailService.sendTemplateMail(req.get("to"), req.get("subject"), "email-template", context);
    }

    // Attach a file from disk (for quick local testing)
    @PostMapping("/sendMailWithFile")
    public String sendMailWithFile(@RequestParam String to,
                                   @RequestParam String subject,
                                   @RequestParam String body,
                                   @RequestParam String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return "File not found: " + filePath;
        }
        return mailService.sendMailWithAttachment(to, subject, body, file);
    }

    // Upload file (multipart) and send as attachment
    @PostMapping("/sendMailWithUpload")
    public String sendMailWithUpload(@RequestParam String to,
                                     @RequestParam String subject,
                                     @RequestParam String body,
                                     @RequestPart("file") MultipartFile file) {
        return mailService.sendMailWithMultipartAttachment(to, subject, body, file);
    }
}
