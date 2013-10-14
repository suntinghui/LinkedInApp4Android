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
import com.hmd.activity.MyAttentionsActivity;
import com.hmd.activity.ProfileActivity;
import com.hmd.activity.SchoolActivity;
import com.hmd.activity.SuggestPeopleActivity;
import com.hmd.client.Constants;
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
		TextView tvLocalPosition = (TextView)this.findViewById(R.id.tv_profile_localposition);
		
		tvStartDate.setText(this.data.getStartTime().equals("null")?"未知":this.data.getStartTime());
		tvEndDate.setText(this.data.getEndTime().equals("null")?"未知":this.data.getEndTime());
		String localPosition = (this.data.getProvince().equals("null") ? "未知":this.data.getProvince()) +"--"+ (this.data.getCity().equals("null") ? "未知":this.data.getCity());
		tvLocalPosition.setText(localPosition);
		
		TextView tvOrg = (TextView)this.findViewById(R.id.tv_profile_org);
		tvOrg.setText(this.data.getOrg().equals("null") ? "未知":this.data.getOrg());
		
		TextView tvDescription = (TextView)this.findViewById(R.id.tv_profile_description);
		tvDescription.setText(this.data.getDescription().equals("null") ? "未知":this.data.getDescription());
		
		TextView tvEndorsed = (TextView)this.findViewById(R.id.tv_profile_endorsed);
		if(this.data.getEndorsedAs() != null && this.data.getEndorsedAs() != ""){
			tvEndorsed.setVisibility(View.VISIBLE);
			tvEndorsed.setText(this.data.getEndorsedAs().equals("null") ? "未知":this.data.getEndorsedAs());
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
		paramMap.put("num", Constants.PAGESIZE+"");
		
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_SUGGESTPEOPLE_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				int count = Integer.valueOf((String)(((HashMap<String, Object>)obj).get("total")));
				ArrayList<ProfileModel> list = (ArrayList<ProfileModel>)(((HashMap<String, Object>)obj).get("list"));
				if(list == null || list.size() == 0){
					
				}else{
					Intent intent = new Intent(ProfileCardRelativeLayout.this.mContext, MyAttentionsActivity.class);  
					intent.putExtra("PROFILEMODELLIST", list);
					intent.putExtra("ID", data.getid());
					intent.putExtra("TITLE", "相关推荐");
					intent.putExtra("TOTAL", count);
					ProfileCardRelativeLayout.this.mContext.startActivity(intent);
				}
				
			}
		}, data.getid());
		
		return request;
	}
}