package com.hmd.model;

public class MediaModel {

	public int type; // 类型：1动态；2公告；3活动；4捐赠信息
	public String title;
	public String time;
	public String content;
	public String[] pics;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String[] getPics() {
		return pics;
	}

	public void setPics(String[] pics) {
		this.pics = pics;
	}

	public String toString() {
		return this.title;
	}

}
