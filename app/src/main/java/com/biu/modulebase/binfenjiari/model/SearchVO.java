package com.biu.modulebase.binfenjiari.model;

/**
 * 搜索bean
 * 
 * @author Administrator
 *
 */
public class SearchVO extends BaseModel {

	private static final long serialVersionUID = -210008082749663634L;

	/** 搜索关键词 **/
	private String name;
	/** 搜索时间 秒数 **/
	private String time;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}


}
