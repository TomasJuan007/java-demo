package com.example.cache.common.cache;

import com.example.cache.menu.model.Menu;
import com.example.cache.common.annotation.MyEhCache;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MenuCache {

    @MyEhCache("menuListInfoEhcache:menuListInfo")
    @Cacheable(value = "menu#1680#60", key = "'all'")
    public List<Menu> get() {
        List<Menu> menuList = new ArrayList<>();
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setLevel(1);
        Menu childMenu = new Menu();
        childMenu.setId(2L);
        childMenu.setPid(1L);
        childMenu.setLevel(2);
        menuList.add(menu);
        menuList.add(childMenu);
        return menuList;
    }
}
