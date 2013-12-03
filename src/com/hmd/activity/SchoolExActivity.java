package com.hmd.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.hmd.R;
import com.hmd.activity.component.SchoolCardRelativeLayout;
import com.hmd.activity.component.SchoolEventRelativeLayout;
import com.hmd.activity.component.SchoolInfoCardRelativeLayout;
import com.hmd.activity.component.SchoolNoticeRelativeLayout;
import com.hmd.client.ApplicationEnvironment;
import com.hmd.client.HttpRequestType;
import com.hmd.model.ActiveModel;
import com.hmd.model.AnnouncementModel;
import com.hmd.model.SchoolModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;

public class SchoolExActivity extends AbsSubActivity {

	private SchoolInfoCardRelativeLayout rlSchoolInfo = null; // 学校信息
	private SchoolNoticeRelativeLayout rlSchoolNotice = null; // 官方公告
	private SchoolEventRelativeLayout rlSchoolEvent = null; // 官方活动
	private SchoolCardRelativeLayout rlSchoolCard = null; // 校友卡

	private SchoolModel schoolModel = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school);

		this.init();
	}

	private void init() {
		// RelativeLayout rlMain = (RelativeLayout)
		// this.findViewById(R.id.rl_main);
		LinearLayout llSchoolContainer = (LinearLayout) this.findViewById(R.id.ll_main_school_container);

		// 学校信息
		rlSchoolInfo = new SchoolInfoCardRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolInfo);

		// 官方公告
		rlSchoolNotice = new SchoolNoticeRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolNotice);
		rlSchoolNotice.setVisibility(View.GONE);

		// 官方活动
		rlSchoolEvent = new SchoolEventRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolEvent);
		rlSchoolEvent.setVisibility(View.GONE);

		// 校友卡
		rlSchoolCard = new SchoolCardRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolCard);

		refreshData();
	}

	// 刷新数据
	private void refreshData() {
		schoolModel = new SchoolModel();

		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getCollegeInfo());
		queue.addHttpRequest(getLastestAnnouncement());
		queue.addHttpRequest(getActiveTypeList());
		queue.addHttpRequest(getActiveList());
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

	// 获取最新一条公告
	private LKHttpRequest getLastestAnnouncement() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", "1");
		paramMap.put("previewLen", "200"); // 预览长度，即取正文内容前几个字符，范围[0,200]，0为关闭预览

		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_BROADCAST_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				int total = (Integer) map.get("total");
				if (total == 0) {
					rlSchoolNotice.setVisibility(View.GONE);
				} else {
					// 只取一条数据
					@SuppressWarnings("unchecked")
					ArrayList<AnnouncementModel> list = (ArrayList<AnnouncementModel>) map.get("list");
					AnnouncementModel model = list.get(0);
					rlSchoolNotice.refresh(model);
				}
			}
		});

		return request;
	}

	// 取得活动类型列表
	private LKHttpRequest getActiveTypeList() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_EVENT_TYPE_LIST, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> map = (HashMap<String, String>) obj;
				ActiveModel.setActiveTypeMap(map);
			}
		});

		return request;
	}

	// 取得活动列表
	private LKHttpRequest getActiveList() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", "2");
		paramMap.put("typeID", "0"); // 类型ID，用于返回特定类型的活动，0表示不限类型
		paramMap.put("previewLen", "200"); // 预览长度，即取正文内容前几个字符，范围[0,200]，0为关闭预览

		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_EVENT_LIST, paramMap, new LKAsyncHttpResponseHandler() {

			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				int total = (Integer) map.get("total");
				if (total > 0) {
					rlSchoolEvent.setVisibility(View.VISIBLE);
					rlSchoolEvent.refresh((ArrayList<ActiveModel>) map.get("list"));

				} else {
					rlSchoolEvent.setVisibility(View.GONE);
				}
			}
		});

		return request;
	}

	public void backAction() {
		ApplicationEnvironment.getInstance().exitApp();
	}

}
