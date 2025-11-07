package com.example.contactus.controller;


import com.example.contactus.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody Map<String, String> request){
        String to = request.get("to");
        String subject = request.get("subject");
        String body = request.get("body");
        return mailService.sendMail(to, subject, body);
    }
}
