package com.biu.modulebase.binfenjiari.model;

public class UpdateVO extends BaseModel {

    private static final long serialVersionUID = 5119796519793033338L;

    /** 版本名称 1.0.1 **/
    private String version;

    /** 版本序列号 **/
    private int sequence;

    private String up_url;

    private String up_content;

    public String getVersion() {
	return version;
    }

    public void setVersion(String version) {
	this.version = version;
    }

    public String getUp_url() {
	return up_url;
    }

    public void setUp_url(String up_url) {
	this.up_url = up_url;
    }

    public int getSequence() {
	return sequence;
    }

    public void setSequence(int sequence) {
	this.sequence = sequence;
    }

    public String getUp_content() {
	return up_content;
    }

    public void setUp_content(String up_content) {
	this.up_content = up_content;
    }

}
