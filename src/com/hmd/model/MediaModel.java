package com.hmd.model;

import java.io.Serializable;
import java.util.ArrayList;

public class MediaModel implements Serializable {

	private static final long serialVersionUID = 614234352745638089L;

	public String id = "";
	public int type = 1; // 类型：1动态；2公告；3活动；4捐赠信息
	public String title = "";
	public String time = "";
	public String preview = "";
	public String content = "";
	public ArrayList<String> pics = new ArrayList<String>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<String> getPics() {
		return pics;
	}

	public void setPics(ArrayList<String> pics) {
		this.pics = pics;
	}

	public void setPics(String[] pic) {
		for (int i = 0; i < pic.length; i++) {
			this.pics.add(pic[i]);
		}
	}

	public String toString() {
		return this.title;
	}

}
