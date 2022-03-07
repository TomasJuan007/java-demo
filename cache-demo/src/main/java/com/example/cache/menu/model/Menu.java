package com.example.cache.menu.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

//@Data
public class Menu implements Serializable {
    private Long id;
    private Long pid;
    private Integer level;
    private List<Menu> childMenuList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public List<Menu> getChildMenuList() {
        return childMenuList;
    }

    public void setChildMenuList(List<Menu> childMenuList) {
        this.childMenuList = childMenuList;
    }
}
