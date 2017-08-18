package com.biu.modulebase.binfenjiari.model;


public class UserInfoBean extends BaseModel {

//	private static final long serialVersionUID = 638121770002340964L;
	/** 用户名 **/
	private String username;
	/** 账号（手机号） **/
	private String telephone;
	/** 头像 **/
	private String user_pic;
	/**性别1：男，2：女，null、0：未设置**/
	private int gender;
	/**积分**/
	private String rank;
	/** 签名 **/
	private String signature;
	/**学校id**/
	private String school_id;
	/** 学校 **/
	private String schoolName;
	/**年级班级**/
	private String className;
	/**爱好**/
	private String hobby;
	/**是否有消息 1有  2没有**/
	private String hasmessage="2";

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getUser_pic() {
		return user_pic;
	}

	public void setUser_pic(String user_pic) {
		this.user_pic = user_pic;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
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

	public String getSchool_id() {
		return school_id;
	}

	public void setSchool_id(String school_id) {
		this.school_id = school_id;
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

	public String getHasMessage() {
		return hasmessage;
	}

	public void setHasMessage(String hasmessage) {
		this.hasmessage = hasmessage;
	}

}
