package com.biu.modulebase.binfenjiari.model;

import com.biu.modulebase.binfenjiari.widget.expandablerecyclerview.model.ParentListItem;

import java.io.Serializable;

/**
 * RecyclerView Header model 基类 , 保存共性属性
 * Created by jhj_Plus on 2016/7/1.
 */
public abstract class BaseHeaderParentItem extends ParentListItem implements Serializable {

    private static final String TAG = "BaseHeaderParentItem";

    public String getComment_number() {
        return "0";
    }

    public void setComment_number(String comment_number) {

    }



}
