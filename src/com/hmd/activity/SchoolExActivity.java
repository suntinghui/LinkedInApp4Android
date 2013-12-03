package com.hmd.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.hmd.R;
import com.hmd.activity.component.SchoolCardRelativeLayout;
import com.hmd.activity.component.SchoolInfoCardRelativeLayout;
import com.hmd.activity.component.SchoolMediaRelativeLayout;
import com.hmd.client.ApplicationEnvironment;
import com.hmd.client.HttpRequestType;
import com.hmd.model.MediaModel;
import com.hmd.model.SchoolModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;

public class SchoolExActivity extends AbsSubActivity {

	private SchoolMediaRelativeLayout rlSchoolDynamic = null; // 母校动态
	private SchoolMediaRelativeLayout rlSchoolNotice = null; // 通知公告
	private SchoolMediaRelativeLayout rlSchoolEvent = null; // 校友活动
	private SchoolCardRelativeLayout rlSchoolCard = null; // 校友卡
	private SchoolInfoCardRelativeLayout rlSchoolInfo = null; // 数据母校

	private SchoolModel schoolModel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school);

		this.init();
	}

	private void init() {
		LinearLayout llSchoolContainer = (LinearLayout) this.findViewById(R.id.ll_main_school_container);

		// 母校动态
		rlSchoolDynamic = new SchoolMediaRelativeLayout(this, "母校动态", new MoreListener(), getTestList());
		llSchoolContainer.addView(rlSchoolDynamic);

		// 通知公告
		rlSchoolNotice = new SchoolMediaRelativeLayout(this, "通知公告", new MoreListener(), getTestList());
		llSchoolContainer.addView(rlSchoolNotice);

		// 校友活动
		rlSchoolEvent = new SchoolMediaRelativeLayout(this, "校友活动", new MoreListener(), getTestList());
		llSchoolContainer.addView(rlSchoolEvent);

		// 校友卡
		rlSchoolCard = new SchoolCardRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolCard);

		// 学校信息
		rlSchoolInfo = new SchoolInfoCardRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolInfo);

		refreshData();
	}

	// 刷新数据
	private void refreshData() {
		schoolModel = new SchoolModel();

		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getCollegeInfo());
		queue.executeQueue("正在刷新数据...", new LKHttpRequestQueueDone());
	}

	// 获取母校信息
	private LKHttpRequest getCollegeInfo() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_INTRODUCT, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				schoolModel = (SchoolModel) obj;

				rlSchoolInfo.refresh(schoolModel);
			}
		});

		return request;
	}

	public void backAction() {
		ApplicationEnvironment.getInstance().exitApp();
	}

	public ArrayList<MediaModel> getTestList() {
		ArrayList<MediaModel> list = new ArrayList<MediaModel>();

		MediaModel model = new MediaModel();
		model.setType(1);
		model.setTitle("罗马尼亚基督教大学代表团来我校访问");
		model.setContent("    10月9日，罗马尼亚基督教大学校长、副校长一行4人来我校正式访问，这是我校第一次接待来自罗马尼亚的代表团。我校党委书记张雪、外国语学院院长封一函、教育学院党委书记方平、国际文化学院党总支书记兼副院长韩梅、国际文化学院副院长张静、政法学院副院长吴高臣、管理学院蒋景媛会见了来访客人。" + "\n    张雪书记和罗马尼亚基督教大学校长介绍了各自学校的概况，并畅谈了促进两校国际化发展的良好期望。中外双方院系负责人分别介绍了各自院系及专业情况。张雪书记指出，两校可以多种方式进行合作，如3+1、2+2、学生交换、对外汉语专业学生实习、教师交换、合作研究等等。罗马尼亚基督教大学3年前开设了中文专业，中文师资奇缺，希望我校每年能派中文教师及对外汉语教学实习生教授中文课程，并帮助其培养本校师资。" + "\n    这次访问为两校进一步合作与交流奠定了良好的基础，两校将尽快签署合作协议，启动合作项目。");
		model.setTime("2013-11-12 10:30");
		model.setPics(new String[] { "http://www.cnu.edu.cn/download.jsp?attachSeq=15161&filename=20131011171639480.jpg", "http://www.cnu.edu.cn/download.jsp?attachSeq=15157&filename=20131011170853141.jpg" });

		list.add(model);
		list.add(model);

		return list;
	}
	
	class MoreListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(SchoolExActivity.this, TopicNewsActivity.class);
			SchoolExActivity.this.startActivity(intent);
		}
	}
}

