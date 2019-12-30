package com.example.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author: pengwei
 * @date: 2019/12/30
 */
@Controller
@RequestMapping("/test")
public class LoginController {

    @RequestMapping("/index")
    public String  index() {
        return "/index";
    }
}
