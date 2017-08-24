package com.binfenjiari.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <p>Author: Huajian Jiang
 * <br>Date: 2017/8/22
 * <br>Email: developer.huajianjiang@gmail.com
 */
public class ReporterHome {
    public List<?> topic;
    public List<?> repoterList;
    @SerializedName("recommendWorkList")
    public List<WorksItem> worksItems;
    public List<?> reportList;

    public static class WorksItem {
        public String id;
        public String themeId;
        public int isCommend;
        public String commendTime;
        public String bannerPic;
        public int type;
        public int userId;
        public String title;
        public int commentNumber;
        public int likeNumber;
        public int collectNumber;
        public String index;
        public String createTime;
        public String modifyTime;
        public int status;
        public String homepage;
        public String description;
        public String img;
    }

}
