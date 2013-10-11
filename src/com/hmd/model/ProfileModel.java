package com.hmd.model;

import java.io.Serializable;

/*
 * 个人信息
 */
public class ProfileModel implements Serializable{

	private static final long serialVersionUID = -4122236788754937349L;

	/* 个人基本信息 */
	private String mId = null; // id
	
	private String mName = null;
	private int mGender = 0; // 性别，1男，0女
	private String mImgUrl = null;
	
	private String mCity = null;
	private String mDistrict = null;

	private String mSchool = null; // 大学名称
	private String mDept = null; // 院系名称
	private String mMajor = null; // 专业名称
	private String mAdYear = null; // 入学年份
	private String mGradYear = null; // 毕业年份，如果尚未毕业则为null
	
	private String mCompany = null;
	private String mPosition = null;
	
	/* 个人扩展信息 */
	private String mBirthday = null; // 生日，yyyy-mm-dd格式
	private String mBirthplace = null; // 籍贯
	private String mNation = null; // 民族
	private String mDesc = null; // 个人描述
	
	public String getId(){
		return this.mId;
	}
	
	public String getName() {
		return this.mName;
	}
	
	public String getImgUrl() {
		return this.mImgUrl;
	}
	
	public int getGender(){
		return this.mGender;
	}

	public String getCity() {
		return this.mCity;
	}

	public String getDistrict() {
		return this.mDistrict;
	}

	public String getSchool() {
		return this.mSchool;
	}
	
	public String getDept(){
		return this.mDept;
	}

	public String getMajor() {
		return this.mMajor;
	}
	
	public String getAdYear(){
		return this.mAdYear;
	}
	
	public String getGradYear(){
		return this.mGradYear;
	}

	public String getCompany() {
		return this.mCompany;
	}

	public String getPosition() {
		return this.mPosition;
	}


	public String getBirthday(){
		return this.mBirthday;
	}
	
	public String getBirthplace(){
		return this.mBirthplace;
	}
	
	public String getNation(){
		return this.mNation;
	}
	
	public String getDesc() {
		return this.mDesc;
	}
	
	public void setId(String str){
		this.mId = str;
	}
	
	public void setName(String s) {
		this.mName = s;
	}
	
	public void setGender(int gender){
		this.mGender = gender;
	}
	
	public void setImgUrl(String s) {
		this.mImgUrl = s;
	}

	public void setCity(String s) {
		if (!s.equals("null"))
			this.mCity = s;
	}

	public void setDistrict(String s) {
		if (!s.equals("null"))
			this.mDistrict = s;
	}

	public void setSchool(String s) {
		if (!s.equals("null"))
			this.mSchool = s;
	}
	
	public void setDept(String s){
		if (!s.equals("null")){
			this.mDept = s;
		}
	}
	
	public void setMajor(String s) {
		if (!s.equals("null"))
			this.mMajor = s;
	}
	
	public void setAdYear(String s){
		if (!s.equals("null")){
			this.mAdYear = s;
		}
	}
	
	public void setGradYear(String s){
		if (!s.equals("null")){
			this.mGradYear = s;
		}
	}

	public void setCompany(String s) {
		if (!s.equals("null"))
			this.mCompany = s;
	}

	public void setPosition(String s) {
		if (!s.equals("null"))
			this.mPosition = s;
	}

	public boolean isSchool() {
		return this.mSchool != null;
	}
	
	public void setBirthday(String s){
		this.mBirthday = s;
	}
	
	public void setBirthplace(String s){
		this.mBirthplace = s;
	}
	
	public void setNation(String s){
		this.mNation = s;
	}
	
	public void setDesc(String s){
		this.mDesc = s;
	}
	
	public String toString(){
		return this.mName;
	}
	
}
