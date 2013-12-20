package com.hmd.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

import com.hmd.R;
import com.hmd.activity.component.SchoolCardRelativeLayout;
import com.hmd.activity.component.SchoolMediaRelativeLayout;
import com.hmd.client.ApplicationEnvironment;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.model.MediaModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;

public class FriendActivity extends AbsSubActivity {

	private SchoolMediaRelativeLayout rlSchoolDynamic = null; // 校友动态
	private SchoolMediaRelativeLayout rlSchoolNotice = null; // 通知公告
	private SchoolCardRelativeLayout rlSchoolCard = null; // 校友龙卡

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school);

		this.init();
	}

	private void init() {
		LinearLayout llSchoolContainer = (LinearLayout) this.findViewById(R.id.ll_main_school_container);

		// 校友动态
		rlSchoolDynamic = new SchoolMediaRelativeLayout(this, "校友动态", new MoreMediaListener(1));
		llSchoolContainer.addView(rlSchoolDynamic);
		rlSchoolDynamic.setVisibility(View.GONE);

		// 通知公告
		rlSchoolNotice = new SchoolMediaRelativeLayout(this, "通知公告", new MoreMediaListener(2));
		llSchoolContainer.addView(rlSchoolNotice);
		rlSchoolNotice.setVisibility(View.GONE);

		// 校友龙卡
		rlSchoolCard = new SchoolCardRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolCard);

		refreshData();
	}

	// 刷新数据
	private void refreshData() {
		// type	类型：1校友动态；2通知公告；3母校动态；4回馈母校
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getSchoolMediaRequest(1)); // 校友动态
		queue.addHttpRequest(getSchoolMediaRequest(2)); // 通知公告
		queue.executeQueue("正在刷新数据...", new LKHttpRequestQueueDone());
	}

	private LKHttpRequest getSchoolMediaRequest(final int type) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("type", 3); // TODO type
		map.put("page", "1");
		map.put("num", "3");
		map.put("previewLen", "200");

		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_MEDIA_LIST, map, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				int total = Integer.parseInt((String) map.get("total"));
				if (total == 0) {
					if (type == 1) {
						rlSchoolDynamic.setVisibility(View.GONE);
					} else if (type == 2) {
						rlSchoolNotice.setVisibility(View.GONE);
					}

				} else {
					if (type == 1) {
						rlSchoolDynamic.setVisibility(View.VISIBLE);
						rlSchoolDynamic.refresh((ArrayList<MediaModel>) map.get("list"));
					} else if (type == 2) {
						rlSchoolNotice.setVisibility(View.VISIBLE);
						rlSchoolNotice.refresh((ArrayList<MediaModel>) map.get("list"));
					}
				}

			}
		});

		return request;
	}

	public void backAction() {
		ApplicationEnvironment.getInstance().exitApp();
	}

	class MoreMediaListener implements OnClickListener {

		private int type;
		
		private ArrayList<MediaModel> topList = null;
		private ArrayList<MediaModel> list = null;
		private int total = 0;

		public MoreMediaListener(int type) {
			this.type = type;
		}

		@Override
		public void onClick(View v) {
			LKHttpRequestQueue queue = new LKHttpRequestQueue();
			queue.addHttpRequest(this.getMediaTopListRequest());
			queue.addHttpRequest(this.getMediaListRequest());
			queue.executeQueue("正在查询请稍候...", new LKHttpRequestQueueDone() {
				public void onComplete() {
					Intent intent = new Intent(FriendActivity.this, SchoolMediaListActivity.class);
					intent.putExtra("TOPLIST", topList);
					intent.putExtra("LIST", list);
					intent.putExtra("TOTAL", total);
					FriendActivity.this.startActivity(intent);
				}
			});
		}

		private LKHttpRequest getMediaTopListRequest() {
			HashMap<String, Object> map = new HashMap<String, Object>();
			//map.put("type", this.type);
			map.put("type", 3); // TODO

			return new LKHttpRequest(HttpRequestType.HTTP_MEDIA_TOPLIST, map, new LKAsyncHttpResponseHandler() {

				@SuppressWarnings("unchecked")
				@Override
				public void successAction(Object obj) {
					topList = (ArrayList<MediaModel>) obj;
				}
			});

		}

		private LKHttpRequest getMediaListRequest() {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("type", 3);// TODO this.type
			map.put("page", "1");
			map.put("num", Constants.PAGESIZE);
			map.put("previewLen", "200");

			return new LKHttpRequest(HttpRequestType.HTTP_MEDIA_LIST, map, new LKAsyncHttpResponseHandler() {

				@SuppressWarnings("unchecked")
				@Override
				public void successAction(Object obj) {
					HashMap<String, Object> map = (HashMap<String, Object>) obj;
					list = (ArrayList<MediaModel>) map.get("list");
					total = Integer.parseInt((String) map.get("total"));
				}
			});
		}
	}
}
