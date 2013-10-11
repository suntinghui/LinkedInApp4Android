package com.hmd.activity.component;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.activity.SuggestPeopleActivity;
import com.hmd.model.TimelineModel;
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
		Intent intent = new Intent(this.mContext, SuggestPeopleActivity.class);  
		this.mContext.startActivity(intent);  
	}
}
