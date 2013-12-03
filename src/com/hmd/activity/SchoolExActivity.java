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
import com.hmd.client.TestMedia;
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

		ArrayList<MediaModel> allList = TestMedia.getList();

		// 母校动态
		ArrayList<MediaModel> list1 = new ArrayList<MediaModel>();
		list1.add(allList.get(0));
		list1.add(allList.get(1));
		rlSchoolDynamic = new SchoolMediaRelativeLayout(this, "母校动态", new MoreListener(), list1);
		llSchoolContainer.addView(rlSchoolDynamic);

		// 通知公告
		ArrayList<MediaModel> list2 = new ArrayList<MediaModel>();
		list2.add(allList.get(2));
		list2.add(allList.get(3));
		rlSchoolNotice = new SchoolMediaRelativeLayout(this, "通知公告", new MoreListener(), list2);
		llSchoolContainer.addView(rlSchoolNotice);

		// 校友活动
		ArrayList<MediaModel> list3 = new ArrayList<MediaModel>();
		list3.add(allList.get(4));
		list3.add(allList.get(6));
		rlSchoolEvent = new SchoolMediaRelativeLayout(this, "校友活动", new MoreListener(), list3);
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

	class MoreListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(SchoolExActivity.this, TopicNewsActivity.class);
			SchoolExActivity.this.startActivity(intent);
		}
	}
}
