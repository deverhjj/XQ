package com.biu.modulebase.binfenjiari.model;

import java.util.List;

public class CityVO extends BaseModel {

    private static final long serialVersionUID = -818298150422450699L;

    private String name;

    private List<SchoolVO> classList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SchoolVO> getClassList() {
        return classList;
    }

    public void setClassList(List<SchoolVO> classList) {
        this.classList = classList;
    }
}
