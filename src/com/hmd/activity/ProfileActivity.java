package com.hmd.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.hmd.R;
import com.hmd.activity.component.NameCardMainRelativeLayout;
import com.hmd.activity.component.ProfileTimelineLinearLayout;
import com.hmd.activity.component.SwitchableScrollViewer;
import com.hmd.activity.component.TopbarRelativeLayout;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.model.ProfileModel;
import com.hmd.model.TimelineModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;

public class ProfileActivity extends BaseActivity implements OnTouchListener{

	private NameCardMainRelativeLayout profileInfoLayout = null;
	private ProfileTimelineLinearLayout timelineLayout = null;
	private SwitchableScrollViewer friendLayout = null;
	private SwitchableScrollViewer fansLayout = null;
	
	private GestureDetector mDector = null;
	private ProfileModel profileModel = null;
	private LinearLayout mLlContainer = null;
	private String mIdentity = "me";// 个人还是他人
	
	private Button profileButton = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
		
		Intent intent = this.getIntent();
		mIdentity = intent.getStringExtra("IDENTITY");
		profileButton = (Button)this.findViewById(R.id.profileButton);
		profileButton.setOnClickListener(listener);
		this.init();
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			finish();
			
		}
	};
	private void init(){

		this.mLlContainer = (LinearLayout)this.findViewById(R.id.ll_profile_container);
		this.mLlContainer.setOnTouchListener(this);
		
		this.mDector = new GestureDetector(this, new GestureListener()); 
	
//		LinearLayout llTopbar = (LinearLayout)this.findViewById(R.id.ll_profile_topbar);
//		TopbarRelativeLayout topbar = new TopbarRelativeLayout(this, this.onNavigation, R.drawable.img_btn_topbar_left_arrow);
//		llTopbar.addView(topbar);
		
		profileInfoLayout = (NameCardMainRelativeLayout) this.findViewById(R.id.profileInfoLayout);
		timelineLayout = (ProfileTimelineLinearLayout) this.findViewById(R.id.profileTimelineLayout);
		timelineLayout.mIdentity = mIdentity;
		friendLayout = (SwitchableScrollViewer) this.findViewById(R.id.profileFirendLayout);
		friendLayout.setTitle("个人关注");
		fansLayout = (SwitchableScrollViewer) this.findViewById(R.id.profileFansLayout);
		fansLayout.setTitle("关注我的人");
		if(!mIdentity.equals("me")){
			friendLayout.setVisibility(View.GONE);
			fansLayout.setVisibility(View.GONE);
		}
		
		this.refreshData();
	}
	
	public void refreshData(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getProfileRequest());
		queue.addHttpRequest(getProfileTimelineRequest());
		if(mIdentity.equals("me")){
			queue.addHttpRequest(getMyAttentionsRequest());
			queue.addHttpRequest(getFansRequest());
		}
		
		queue.executeQueue("正在查询履历...", new LKHttpRequestQueueDone());
		
	}
	
	// 查看个人基本信息
	private LKHttpRequest getProfileRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_PROFILE_BASIC, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				profileModel = (ProfileModel) obj;
				profileInfoLayout.refresh(profileModel);
			}
		}, "me");
		
		return request;
	}
	
	// 查看个人履历
	private LKHttpRequest getProfileTimelineRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_TIMELINE_LIST, null, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj){
					timelineLayout.refresh((ArrayList<TimelineModel>) obj);
				}
			}
		}, mIdentity.equals("me") ? "me":profileModel.getId());
		
		return request;
	}
	
	// 查看我关注的人
	private LKHttpRequest getMyAttentionsRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", "5");
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_FRIENDS_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj){
					ArrayList<ProfileModel> list = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
					Integer total = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
					if(total < Constants.PAGESIZE+1){
						friendLayout.hiddenMoreButton();
					}
					if(list == null || list.size() == 0){
						friendLayout.setVisibility(View.GONE);
					}else{
						friendLayout.refresh(list);
					}
				}else{
					friendLayout.setVisibility(View.GONE);
				}
				
			}
		});
		
		return request;
	}
	
	// 查看关注我的人
	private LKHttpRequest getFansRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", "5");
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_FRIENDS_FUNS_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				if (null != obj){
					ArrayList<ProfileModel> list = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
					Integer total = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
					if(total < 5){
						fansLayout.hiddenMoreButton();
					}
					if(list == null || list.size() == 0){
						fansLayout.setVisibility(View.GONE);
					}else{
						fansLayout.refresh(list);
					}
				}
				
			}
		});
		
		return request;
	}
	
	
	private void TransformToMainScreen(){
		this.finish();
	}
	
	@Override
	public void onBackPressed(){
		this.TransformToMainScreen();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return this.mDector.onTouchEvent(event);
	}
	
	private OnClickListener onNavigation = new OnClickListener(){

		@Override
		public void onClick(View v) {
			TransformToMainScreen();
		}
		
	};
	
	class GestureListener extends SimpleOnGestureListener  
    {  
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
	
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			
			if (e1.getX() - e2.getX() < -120) {    
				TransformToMainScreen();
	            return true;    
	        }  
			
			return false;
		}
	
		@Override
		public void onLongPress(MotionEvent e) {
		}
	
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			return false;
		}
	
		@Override
		public void onShowPress(MotionEvent e) {
		}
	
		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			return false;
		}
    }
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data){
		 super.onActivityResult(requestCode, resultCode, data);
		 if(resultCode == 5){
			 refreshData();
		 }
	}
}
