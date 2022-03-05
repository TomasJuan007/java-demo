package com.example.cache.menu.model;

import lombok.Data;

import java.util.List;

@Data
public class Menu {
    private Long id;
    private Long pid;
    private Integer level;
    private List<Menu> childMenuList;
}
