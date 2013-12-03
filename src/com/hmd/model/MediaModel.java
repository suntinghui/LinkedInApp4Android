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

	public MediaModel getTestMedia() {
		MediaModel model = new MediaModel();
		model.setType(1);
		model.setTitle("罗马尼亚基督教大学代表团来我校访问");
		model.setContent("    10月9日，罗马尼亚基督教大学校长、副校长一行4人来我校正式访问，这是我校第一次接待来自罗马尼亚的代表团。我校党委书记张雪、外国语学院院长封一函、教育学院党委书记方平、国际文化学院党总支书记兼副院长韩梅、国际文化学院副院长张静、政法学院副院长吴高臣、管理学院蒋景媛会见了来访客人。"
				+ "\n    张雪书记和罗马尼亚基督教大学校长介绍了各自学校的概况，并畅谈了促进两校国际化发展的良好期望。中外双方院系负责人分别介绍了各自院系及专业情况。张雪书记指出，两校可以多种方式进行合作，如3+1、2+2、学生交换、对外汉语专业学生实习、教师交换、合作研究等等。罗马尼亚基督教大学3年前开设了中文专业，中文师资奇缺，希望我校每年能派中文教师及对外汉语教学实习生教授中文课程，并帮助其培养本校师资。"
				+ "\n    这次访问为两校进一步合作与交流奠定了良好的基础，两校将尽快签署合作协议，启动合作项目。");
		model.setTime("2013-11-12 10:30");
		model.setPics(new String[]{"http://www.cnu.edu.cn/download.jsp?attachSeq=15161&filename=20131011171639480.jpg","http://www.cnu.edu.cn/download.jsp?attachSeq=15157&filename=20131011170853141.jpg"});
		return model;
	}

}
