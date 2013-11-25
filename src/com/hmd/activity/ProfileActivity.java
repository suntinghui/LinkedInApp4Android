package com.hmd.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.activity.component.NameCardMainRelativeLayout;
import com.hmd.activity.component.ProfileTimelineLinearLayout;
import com.hmd.activity.component.SwitchableScrollViewer;
import com.hmd.client.ApplicationEnvironment;
import com.hmd.client.HttpRequestType;
import com.hmd.model.ProfileModel;
import com.hmd.model.TimelineModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;

public class ProfileActivity extends AbsSubActivity {

	private NameCardMainRelativeLayout profileInfoLayout = null;
	private ProfileTimelineLinearLayout timelineLayout = null;
	private SwitchableScrollViewer friendLayout = null;
	private SwitchableScrollViewer fansLayout = null;

	private ProfileModel profileModel = null;
	private LinearLayout mLlContainer = null;
	private String mIdentity = "me";// 个人还是他人

	private Button backButton = null;
	private Button searchButton = null;

	private TextView titleView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_profile);

		Log.e("--", "------");

		this.init(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		Log.e("==", "======");

		this.init(intent);
	}

	private OnClickListener backListener = new OnClickListener() {
		@Override
		public void onClick(View arg0) {
			back();
		}
	};
	
	private OnClickListener searchListener = new OnClickListener(){
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(ProfileActivity.this, SearchPeopleActivity.class);
			ProfileActivity.this.startActivityForResult(intent, 100);
		}
	};

	private void init(Intent intent) {
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(backListener);

		searchButton = (Button) this.findViewById(R.id.btn_search);
		searchButton.setOnClickListener(searchListener);
		searchButton.setVisibility(View.VISIBLE);
		
		titleView = (TextView) this.findViewById(R.id.titleView);

		mIdentity = intent.getStringExtra("IDENTITY");
		if (null == mIdentity) {
			mIdentity = "me";
			backButton.setVisibility(View.GONE);
			titleView.setText("我的信息");
		} else {
			backButton.setVisibility(View.VISIBLE);
		}

		profileModel = (ProfileModel) intent.getSerializableExtra("PROFILE");
		if (profileModel != null) {
			titleView.setText(profileModel.getName() + "的信息");
		}

		this.mLlContainer = (LinearLayout) this.findViewById(R.id.ll_profile_container);

		profileInfoLayout = (NameCardMainRelativeLayout) this.findViewById(R.id.profileInfoLayout);
		profileInfoLayout.mIdentity = mIdentity;
		timelineLayout = (ProfileTimelineLinearLayout) this.findViewById(R.id.profileTimelineLayout);
		timelineLayout.mIdentity = mIdentity;
		friendLayout = (SwitchableScrollViewer) this.findViewById(R.id.profileFirendLayout);
		friendLayout.setTitle("个人关注");
		fansLayout = (SwitchableScrollViewer) this.findViewById(R.id.profileFansLayout);
		fansLayout.setTitle("关注我的人");
		if (!mIdentity.equals("me")) {
			friendLayout.setVisibility(View.GONE);
			fansLayout.setVisibility(View.GONE);
		}

		this.refreshData();
	}

	public void refreshData() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getProfileRequest());
		queue.addHttpRequest(getProfileTimelineRequest());
		if (mIdentity.equals("me")) {
			queue.addHttpRequest(getMyAttentionsRequest());
			queue.addHttpRequest(getFansRequest());
		}

		queue.executeQueue("正在请求数据...", new LKHttpRequestQueueDone());

	}

	// 查看个人基本信息
	private LKHttpRequest getProfileRequest() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_PROFILE_BASIC, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				profileModel = (ProfileModel) obj;
				profileInfoLayout.refresh(profileModel);
			}
		}, mIdentity.equals("me") ? "me" : profileModel.getId());

		return request;
	}

	// 查看个人履历
	private LKHttpRequest getProfileTimelineRequest() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_TIMELINE_LIST, null, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj) {
					timelineLayout.refresh((ArrayList<TimelineModel>) obj);
				}
			}
		}, mIdentity.equals("me") ? "me" : profileModel.getId());

		return request;
	}

	// 查看我关注的人
	private LKHttpRequest getMyAttentionsRequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", "5");
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_FRIENDS_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj) {
					ArrayList<ProfileModel> list = (ArrayList<ProfileModel>) (((HashMap<String, Object>) obj).get("list"));
					Integer total = Integer.valueOf((String) (((HashMap<String, Object>) obj).get("total")));
					if (total < 6) {
						friendLayout.hiddenMoreButton();
					}
					if (list == null || list.size() == 0) {
						friendLayout.setVisibility(View.GONE);
					} else {
						friendLayout.refresh(list);
					}
				} else {
					friendLayout.setVisibility(View.GONE);
				}

			}
		});

		return request;
	}

	// 查看关注我的人
	private LKHttpRequest getFansRequest() {
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", "5");
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_FRIENDS_FUNS_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj) {
					ArrayList<ProfileModel> list = (ArrayList<ProfileModel>) (((HashMap<String, Object>) obj).get("list"));
					Integer total = Integer.valueOf((String) (((HashMap<String, Object>) obj).get("total")));
					if (total < 6) {
						fansLayout.hiddenMoreButton();
					}
					if (list == null || list.size() == 0) {
						fansLayout.setVisibility(View.GONE);
					} else {
						fansLayout.refresh(list);
					}
				}

			}
		});

		return request;
	}

	public void backAction() {
		if (mIdentity.equals("me")) {
			ApplicationEnvironment.getInstance().exitApp();
		} else {
			back();
		}
	}

	private void back() {
		 goback();

		 /*
		Intent intent = new Intent(BaseActivity.getTopActivity(), ProfileActivity.class);
		BaseActivity.getTopActivity().startActivity(intent);
		*/
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 5) {
			refreshData();
		}
	}

}
