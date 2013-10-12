package com.hmd.activity.component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.activity.ProfileActivity;
import com.hmd.activity.SchoolActivity;
import com.hmd.activity.SuggestPeopleActivity;
import com.hmd.client.HttpRequestType;
import com.hmd.model.ProfileModel;
import com.hmd.model.TimelineModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.ImageUtil;


/*
 * 自定义控件 - 卡片式履历
 */

public class ProfileCardRelativeLayout extends RelativeLayout {

	private TimelineModel data = null;
	private Context mContext = null;
	
	public ProfileCardRelativeLayout(Context context, TimelineModel e) {
		super(context);
		this.mContext = context;
		this.data = e;
		
        LayoutInflater.from(context).inflate(R.layout.layout_profile_card, this, true); 
        
        this.init();
	}
	
	private void init(){
		TextView tvStartDate = (TextView)this.findViewById(R.id.tv_profile_start_date);
		TextView tvEndDate = (TextView)this.findViewById(R.id.tv_profile_end_date);
		
		tvStartDate.setText(this.data.getStartTime());
		tvEndDate.setText(this.data.getEndTime());
		
		RelativeLayout rlSchool = (RelativeLayout)this.findViewById(R.id.rl_profile_school);
		RelativeLayout rlCompany = (RelativeLayout)this.findViewById(R.id.rl_profile_company);
		
		if(this.data.isSchool()){
			rlSchool.setVisibility(View.VISIBLE);
			rlCompany.setVisibility(View.GONE);
			
			TextView tvSchool = (TextView)this.findViewById(R.id.tv_profile_school);
			TextView tvMajor = (TextView)this.findViewById(R.id.tv_profile_major);
			
			tvSchool.setText(this.data.getSchool());
			tvMajor.setText(this.data.getMajor());
		}else{
			rlSchool.setVisibility(View.GONE);
			rlCompany.setVisibility(View.VISIBLE);
			
			TextView tvCompany = (TextView)this.findViewById(R.id.tv_profile_company);
			TextView tvPosition = (TextView)this.findViewById(R.id.tv_profile_position);
			
			tvCompany.setText(this.data.getCompany());
			tvPosition.setText(this.data.getTitle());
		}
		
		TextView tvDescription = (TextView)this.findViewById(R.id.tv_profile_description);
		tvDescription.setText(this.data.getDescription());
		
		TextView tvEndorsed = (TextView)this.findViewById(R.id.tv_profile_endorsed);
		if(this.data.getEndorsedAs() != null && this.data.getEndorsedAs() != ""){
			tvEndorsed.setVisibility(View.VISIBLE);
			tvEndorsed.setText(this.data.getEndorsedAs());
		}else{
			tvEndorsed.setVisibility(View.GONE);
		}
		
		ImageView photoImageView = (ImageView) this.findViewById(R.id.iv_profile_photo);
		ImageUtil.loadImage(R.drawable.img_school_head_portrait, this.data.getImgUrl(), photoImageView);
		
		ImageButton btnFind = (ImageButton)this.findViewById(R.id.btn_profile_find);
		btnFind.setOnClickListener(this.onFindClicked);
	}
	
	private OnClickListener onFindClicked=new OnClickListener(){

		@Override
		public void onClick(View v) {
			findRelatedProfile();
		}
	};
	
	private void findRelatedProfile(){
		getSuggestPeopleList();  
	}
	
	private void getSuggestPeopleList(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getSuggestPeopleRequest());
		queue.executeQueue("正在查询推荐好友...", new LKHttpRequestQueueDone());
		
	}
	
	// 查看推荐好友
	private LKHttpRequest getSuggestPeopleRequest(){
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("page", "1");
		paramMap.put("num", "20");
		
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_SUGGESTPEOPLE_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				ArrayList<ProfileModel> list = (ArrayList<ProfileModel>)obj;
				if(list == null || list.size() == 0){
					
				}else{
					Intent intent = new Intent(ProfileCardRelativeLayout.this.mContext, SuggestPeopleActivity.class);  
					intent.putExtra("PROFILEMODELLIST", (Serializable)obj);
					ProfileCardRelativeLayout.this.mContext.startActivity(intent);
				}
				
			}
		}, data.getid());
		
		return request;
	}
}