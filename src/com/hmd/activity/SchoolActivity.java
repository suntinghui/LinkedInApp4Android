package com.hmd.activity;

import java.util.ArrayList;
import java.util.HashMap;

import weibo4j.Timeline;
import weibo4j.model.Paging;
import weibo4j.model.StatusWapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hmd.R;
import com.hmd.activity.component.SchoolCardRelativeLayout;
import com.hmd.activity.component.SchoolEventRelativeLayout;
import com.hmd.activity.component.SchoolInfoCardRelativeLayout;
import com.hmd.activity.component.SchoolNoticeRelativeLayout;
import com.hmd.activity.component.SchoolWeiboRelativeLayout;
import com.hmd.activity.component.TopbarRelativeLayout;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.model.AnnouncementModel;
import com.hmd.model.ProfileModel;
import com.hmd.model.SchoolModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.WeiboUtil;

public class SchoolActivity extends BaseActivity implements OnTouchListener {

	private TopbarRelativeLayout topbar 				= null; // 搜索栏
	private SchoolInfoCardRelativeLayout rlSchoolInfo 	= null; // 学校信息	
	private SchoolNoticeRelativeLayout rlSchoolNotice 	= null; // 官方公告
	private SchoolEventRelativeLayout rlSchoolEvent 	= null; // 官方活动
	private SchoolCardRelativeLayout rlSchoolCard		= null; // 校友卡
	private SchoolWeiboRelativeLayout rlSchoolWeibo 	= null; // 官方微博
	
	private SchoolModel schoolModel 					= null;
	private ProfileModel profileModel 					= null;
	
	private GestureDetector mDector 					= null;
	
	private long exitTimeMillis 						= 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_school);

		this.init();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return this.mDector.onTouchEvent(event);
	}
	
	private void init(){
		RelativeLayout rlMain = (RelativeLayout)this.findViewById(R.id.rl_main);
		LinearLayout llSchoolContainer = (LinearLayout)this.findViewById(R.id.ll_main_school_container);
		
		rlMain.setOnTouchListener(this);
		llSchoolContainer.setOnTouchListener(this);
		this.mDector = new GestureDetector(this, new GestureListener()); 
		
		// 搜索栏
		LinearLayout llTopbar = (LinearLayout)this.findViewById(R.id.ll_main_topbar);
		topbar = new TopbarRelativeLayout(this, this.onNavigation, R.drawable.img_btn_topbar_right_arrow);
		llTopbar.addView(topbar);

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
		
		// 校友卡
		rlSchoolCard = new SchoolCardRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolCard);
		
		// 官方微博
		rlSchoolWeibo = new SchoolWeiboRelativeLayout(this);
		llSchoolContainer.addView(rlSchoolWeibo);
		rlSchoolWeibo.setVisibility(WeiboUtil.hasAuth() ? View.GONE : View.VISIBLE);
		
		refreshData();
	}
	
	private void refreshData(){
		schoolModel = new SchoolModel();
		profileModel = new ProfileModel();
		
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getProfileRequest());
		queue.addHttpRequest(getCollegeInfo());
		queue.addHttpRequest(getLastestAnnouncement());
		queue.executeQueue("正在刷新数据...", new LKHttpRequestQueueDone());
		
		// 获取学校微博
		this.getSchoolWeibo();
	}
	
	// 查看个人基本信息
	private LKHttpRequest getProfileRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_PROFILE_BASIC, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				profileModel = (ProfileModel) obj;
			}
		}, "me");
		
		return request;
	}
	
	// 获取母校信息
	private LKHttpRequest getCollegeInfo(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_COLLEGE_INTRODUCT, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				schoolModel = (SchoolModel) obj;
				
				rlSchoolInfo.refresh(schoolModel);
			}
		});
		
		return request;
	}
	
	// 获取最新一条公告
	private LKHttpRequest getLastestAnnouncement(){
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("page", "1");
		paramMap.put("num", "1");
		paramMap.put("previewLen", "200"); //预览长度，即取正文内容前几个字符，范围[0,200]，0为关闭预览
		
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_BROADCAST_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, Object> map = (HashMap<String, Object>) obj;
				int total = (Integer) map.get("total");
				if (total == 0){
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
	
	private void getSchoolWeibo(){
		// 如果用户已经登录了新浪微博，则直接取得数据
		if (WeiboUtil.hasAuth()){
			new GetSchoolTask().execute();
		}
	}
	
	// 异步取得微博数据
	class GetSchoolTask extends AsyncTask<Object, Object, Object>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
		
		@Override
		protected Object doInBackground(Object... arg0) {
			try{
				Timeline timeline = new Timeline();
				Paging paging = new Paging();
				paging.count(3); // 在主页中只显示三条微博数据
				
				timeline.client.setToken(WeiboUtil.getToken());
				StatusWapper statusWapper = timeline.getUserTimelineByName(Constants.WEIBO_TIMELINE_SCREENNAME, paging, 0, 0);
				return statusWapper;
			} catch(Exception e){
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			rlSchoolWeibo.refresh(((StatusWapper)result).getStatuses());
		}

	}
	
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 认证微博成功
		if(requestCode == 100 && resultCode == RESULT_OK) {
			getSchoolWeibo();
		}
    }
	
	
	// 最上面的TopBar的左边的按纽的点击事件
	private OnClickListener onNavigation = new OnClickListener(){
		@Override
		public void onClick(View v) {
			TransformToProfileScreen();
		}
	};
	
	// 显示个人信息
	private void TransformToProfileScreen(){
		
		Intent intent = new Intent(SchoolActivity.this, ProfileActivity.class);  
		intent.putExtra("PROFILE", profileModel);
		startActivity(intent);  
	}
	
	// 程序退出
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - exitTimeMillis) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				exitTimeMillis = System.currentTimeMillis();
			} else {
				ArrayList<BaseActivity> list = BaseActivity.getAllActiveActivity();
				for (BaseActivity activity : list) {
					activity.finish();
				}

				System.exit(0);
			}
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	class GestureListener extends SimpleOnGestureListener  
    {  
		@Override
		public boolean onDown(MotionEvent e) {
			return true;
		}
	
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			if (e1.getX() - e2.getX() > 120) {    
				TransformToProfileScreen();
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
