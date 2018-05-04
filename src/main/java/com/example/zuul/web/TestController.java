package com.example.zuul.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/showQrCodeTestPage")
    public String showQrCodeTestPage() {
        return "qrcode";
    }
}
