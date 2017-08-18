package com.biu.modulebase.binfenjiari.model;

/**
 * @author Lee
 * @Title: {举报类型bean}
 * @Description:{描述}
 * @date 2016/6/2
 */
public class ReportVO extends BaseModel {

    /**举报类型描述**/
    private String content;

/*    private String status;

    private String create_time;*/

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
