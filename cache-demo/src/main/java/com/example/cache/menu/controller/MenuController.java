package com.example.cache.menu.controller;

import com.example.cache.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuController {

    @Autowired
    private MenuService menuService;

    @RequestMapping("/test")
    public String test(String a) {
        menuService.getMenuList(a);
        return "ok";
    }
}
