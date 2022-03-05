package com.example.cache.menu.service.impl;

import com.example.cache.common.cache.MenuCache;
import com.example.cache.menu.model.Menu;
import com.example.cache.menu.service.MenuService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@Service
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuCache menuCache;

    @Override
    public List<Menu> getMenuList() {
        List<Menu> allMenuList = menuCache.get();
        Map<Integer, List<Menu>> collectByLevel = allMenuList.stream()
                .collect(Collectors.groupingBy(Menu::getLevel));
        List<Menu> oneLevelList = collectByLevel.get(1);
        List<Menu> twoLevelList = collectByLevel.get(2);
        if (CollectionUtils.isNotEmpty(twoLevelList)) {
            Map<Long, List<Menu>> twoLevelMenuMap = twoLevelList.stream()
                    .collect(Collectors.groupingBy(Menu::getPid));
            for (Menu oneLevelMenu : oneLevelList) {
                Long id = oneLevelMenu.getId();
                oneLevelMenu.setChildMenuList(twoLevelMenuMap.get(id));
            }
        }
        System.out.println(oneLevelList);
        new Thread(() -> {
            try {
                sleep(6000L);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            System.out.println(oneLevelList);
        }).start();
        return oneLevelList;
    }
}
