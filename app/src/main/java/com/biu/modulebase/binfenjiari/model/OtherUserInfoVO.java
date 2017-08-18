package com.biu.modulebase.binfenjiari.model;


public class OtherUserInfoVO  {

	/** 用户名 **/
	private String username;
	/** 头像 **/
	private String user_pic;
	/**性别1：男，2：女，null、0：未设置**/
	private int gender;
	/**积分**/
	private String rank;
	/** 签名 **/
	private String signature;
	/** 学校 **/
	private String schoolName;
	/**年级班级**/
	private String className;
	/**爱好**/
	private String hobby;
	/**是否是好友 1有  2不是**/
	private String is_friend;

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getHobby() {
		return hobby;
	}

	public void setHobby(String hobby) {
		this.hobby = hobby;
	}

	public String getIs_friend() {
		return is_friend;
	}

	public void setIs_friend(String is_friend) {
		this.is_friend = is_friend;
	}
}
