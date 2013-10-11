package com.hmd.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;

import com.hmd.R;
import com.hmd.activity.component.NameCardMainRelativeLayout;
import com.hmd.activity.component.ProfileTimelineLinearLayout;
import com.hmd.activity.component.SwitchableScrollViewer;
import com.hmd.activity.component.TopbarRelativeLayout;
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
	
	private GestureDetector mDector = null;
	private ProfileModel profileModel = null;
	private LinearLayout mLlContainer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_profile);
		
		Intent intent = this.getIntent();
		profileModel = (ProfileModel) intent.getSerializableExtra("PROFILE");

		this.init();
	}
	
	private void init(){

		this.mLlContainer = (LinearLayout)this.findViewById(R.id.ll_profile_container);
		this.mLlContainer.setOnTouchListener(this);
		
		this.mDector = new GestureDetector(this, new GestureListener()); 
	
		LinearLayout llTopbar = (LinearLayout)this.findViewById(R.id.ll_profile_topbar);
		TopbarRelativeLayout topbar = new TopbarRelativeLayout(this, this.onNavigation, R.drawable.img_btn_topbar_left_arrow);
		llTopbar.addView(topbar);
		
		profileInfoLayout = (NameCardMainRelativeLayout) this.findViewById(R.id.profileInfoLayout);
		timelineLayout = (ProfileTimelineLinearLayout) this.findViewById(R.id.profileTimelineLayout);
		friendLayout = (SwitchableScrollViewer) this.findViewById(R.id.profileFirendLayout);
		friendLayout.setTitle("个人关注");
		
		this.refreshData();
	}
	
	private void refreshData(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getProfileTimelineRequest());
		queue.executeQueue("正在查询履历...", new LKHttpRequestQueueDone());
		
		profileInfoLayout.refresh(profileModel);
	}
	
	// 查看个人履历
	private LKHttpRequest getProfileTimelineRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_TIMELINE_LIST, null, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				timelineLayout.refresh((ArrayList<TimelineModel>) obj);
				friendLayout.refresh(friendLayout.getTestData());
			}
		}, "me");
		
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
}
