package com.example.cache.menu.service.impl;

import com.example.cache.menu.cache.MenuCache;
import com.example.cache.menu.model.Menu;
import com.example.cache.menu.property.EhCacheProperties;
import com.example.cache.menu.service.MenuService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;

@Service
public class MenuServiceImpl implements MenuService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MenuServiceImpl.class);

    @Autowired
    private MenuCache menuCache;
    @Autowired
    private EhCacheProperties ehCacheProperties;

    @Override
    public List<Menu> getMenuList(boolean skipInject) {
        List<Menu> allMenuList = menuCache.queryAllMenu();
        //如果ehcache切面克隆开关已经打开，不需要再克隆
        List<Menu> filteredMenuList;
        if (ehCacheProperties.isEhcacheCloneSwitch()) {
            filteredMenuList = allMenuList.stream()
                    .filter(e -> e.getLevel()>0)
                    //获取到的菜单列表直接使用了缓存的对象，修改补充对列表克隆
                    .map(SerializationUtils::clone)
                    .collect(Collectors.toList());
            LOGGER.info("MenuServiceImpl#getMenuList cloned.");
        } else {
            filteredMenuList = allMenuList.stream()
                    .filter(e -> e.getLevel()>0)
                    .collect(Collectors.toList());
        }
        Map<Integer, List<Menu>> collectByLevel = filteredMenuList.stream()
                .collect(Collectors.groupingBy(Menu::getLevel));
        List<Menu> oneLevelList = collectByLevel.get(1);
        List<Menu> twoLevelList = collectByLevel.get(2);
        if (CollectionUtils.isNotEmpty(twoLevelList) && !skipInject) {
            Map<Long, List<Menu>> twoLevelMenuMap = twoLevelList.stream()
                    .collect(Collectors.groupingBy(Menu::getPid));
            for (Menu oneLevelMenu : oneLevelList) {
                Long id = oneLevelMenu.getId();
                //线程不安全操作
                oneLevelMenu.setChildMenuList(twoLevelMenuMap.get(id));
            }
        }
        LOGGER.info("MenuServiceImpl#getMenuList oneLevelList:{}",oneLevelList);

        //模拟获取置顶菜单和用户常用菜单
        try {
            LOGGER.info("sleeping, wait for it...");
            sleep(3000L);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }
        LOGGER.info("MenuServiceImpl#getMenuList oneLevelList:{}",oneLevelList);
        return oneLevelList;
    }
}
