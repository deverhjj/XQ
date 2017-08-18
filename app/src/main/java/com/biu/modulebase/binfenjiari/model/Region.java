package com.biu.modulebase.binfenjiari.model;

/**
 * Created by jhj_Plus on 2016/6/16.
 */
public class Region {
    private static final String TAG = "Region";

    private RegionItem[] result;

    public RegionItem[] getResult() {
        return result;
    }

    public void setResult(RegionItem[] result) {
        this.result = result;
    }

    public static class RegionItem extends BaseModel {
        /**
         * 区域名称
         **/
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
