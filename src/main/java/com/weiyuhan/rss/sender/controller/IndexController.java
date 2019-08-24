package com.weiyuhan.rss.sender.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/")
@RestController
public class IndexController {

    @RequestMapping("/")
    public String index() {
        return "rss.sender.service";
    }
}
