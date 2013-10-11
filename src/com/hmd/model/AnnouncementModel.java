package com.hmd.model;

import java.io.Serializable;

public class AnnouncementModel implements Serializable {

	private static final long serialVersionUID = -8307674336517627131L;

	private String mId = null;
	private String mTitle = null;
	private String mTime = null;
	private String mContent = null;

	public AnnouncementModel() {

	}

	public AnnouncementModel(String id, String title, String time, String content) {
		this.mId = id;
		this.mTitle = title;
		this.mTime = time;
		this.mContent = content;
	}

	public String getId() {
		return mId;
	}

	public void setId(String id) {
		this.mId = id;
	}

	public String getTitle() {
		return this.mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getTime() {
		return this.mTime;
	}

	public void setTime(String time) {
		this.mTime = time;
	}

	public String getContent() {
		return this.mContent;
	}

	public void setContent(String content) {
		this.mContent = content;
	}

	
	public String toString(){
		return this.mTitle;
	}
}
