package com.example.cache.menu.controller;

import com.example.cache.menu.model.Menu;
import com.example.cache.menu.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MenuController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MenuController.class);

    @Autowired
    private MenuService menuService;

    @RequestMapping("/test")
    public List<Menu> test(boolean skipInject) {
        LOGGER.info("testing begin...");
        return menuService.getMenuList(skipInject);
    }
}
