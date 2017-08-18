package com.biu.modulebase.binfenjiari.model;

import java.util.List;

/**
 * Created by jhj_Plus on 2016/6/17.
 */
public class Collection {
    private static final String TAG = "Collection";

    private List<CollectionItem> list;

    private int allPageNumber;

    public List<CollectionItem> getList() {
        return list;
    }

    public void setList(List<CollectionItem> list) {
        this.list = list;
    }

    public int getAllPageNumber() {
        return allPageNumber;
    }

    public void setAllPageNumber(int allPageNumber) {
        this.allPageNumber = allPageNumber;
    }

    public static class CollectionItem extends BaseModel{
        /**
         * 图片url
         */
        private String banner_pic;

        /**
         * 标题
         */
        private String name;

        /**
         * 开始时间（type=1）
         */
        private long open_time;

        /**
         * 结束时间（type=1）
         */
        private long end_time;

        /**
         * 是否结束 1未结束 2已结束（type=1）
         */
        private int isopen;

        /**
         * 活动数量（type=2）
         */
        private String activity_number;

        /**
         * 视听类别 1视频 3语音 2图文（type=3）
         */
        private int vedio_type;

        /**
         * 视听链接（type=3&&vedio_type=1、3）
         */
        private String url;

        /**
         * 时长（type=3&&vedio_type=1、3）
         */
        private String vedio_time;


        public String getBanner_pic() {
            return banner_pic;
        }

        public void setBanner_pic(String banner_pic) {
            this.banner_pic = banner_pic;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getOpen_time() {
            return open_time;
        }

        public void setOpen_time(long open_time) {
            this.open_time = open_time;
        }

        public long getEnd_time() {
            return end_time;
        }

        public void setEnd_time(long end_time) {
            this.end_time = end_time;
        }

        public int getIsopen() {
            return isopen;
        }

        public void setIsopen(int isopen) {
            this.isopen = isopen;
        }

        public String getActivity_number() {
            return activity_number;
        }

        public void setActivity_number(String activity_number) {
            this.activity_number = activity_number;
        }

        public int getVedio_type() {
            return vedio_type;
        }

        public void setVedio_type(int vedio_type) {
            this.vedio_type = vedio_type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVedio_time() {
            return vedio_time;
        }

        public void setVedio_time(String vedio_time) {
            this.vedio_time = vedio_time;
        }
    }




}
