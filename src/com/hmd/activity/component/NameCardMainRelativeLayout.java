package com.hmd.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.activity.AnnouncementListActivity;
import com.hmd.activity.BaseActivity;
import com.hmd.activity.LoginActivity;
import com.hmd.activity.PersonInfoModifyActivity;
import com.hmd.activity.SubmitProfileActivity;
import com.hmd.model.AnnouncementModel;
import com.hmd.model.ProfileModel;
import com.hmd.util.ImageUtil;

public class NameCardMainRelativeLayout extends RelativeLayout {

	private Context mContext = null;
	
	private ProfileModel data = null;
	private RelativeLayout topLayout = null;
	
	public NameCardMainRelativeLayout(Context context){
		super(context);
		
		this.mContext = context;
		this.init();
	}
	
	public NameCardMainRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.mContext = context;
		this.init();
	}
	
	public NameCardMainRelativeLayout(Context context, ProfileModel d) {
		super(context);
		
		this.mContext = context;
		this.data = d;
		
        this.init();
	}

	private void init(){
		LayoutInflater.from(this.mContext).inflate(R.layout.layout_name_card_main, this, true);
		
		this.refresh(this.data);
	}
	
	public void refresh(ProfileModel profileModel){
		this.data = profileModel;
		
		if(this.data == null) 
			return;
		
		this.setVisibility(View.VISIBLE);
		
		topLayout = (RelativeLayout)this.findViewById(R.id.topLayout);
		topLayout.setOnClickListener(listener);
		TextView tvName = (TextView)this.findViewById(R.id.tv_name_card_main_name);
		TextView tvCity = (TextView)this.findViewById(R.id.tv_name_card_main_city);
		TextView tvDistrict = (TextView)this.findViewById(R.id.tv_name_card_main_district);
		TextView tvBrief1 = (TextView)this.findViewById(R.id.tv_name_card_main_brief1);
		TextView tvBrief2 = (TextView)this.findViewById(R.id.tv_name_card_main_brief2);
		ImageView photoImageView = (ImageView) this.findViewById(R.id.iv_name_card_main_photo);
		
		tvName.setText(this.data.getName());
		tvCity.setText(this.data.getCity());
		tvDistrict.setText(this.data.getDistrict());
		
		if(this.data.isSchool()){
			tvBrief1.setText(this.data.getSchool());
			tvBrief2.setText(this.data.getMajor());
		}else{
			tvBrief1.setText(this.data.getCompany());
			tvBrief2.setText(this.data.getPosition());
		}
		
		ImageUtil.loadImage(R.drawable.img_card_head_portrait, ImageUtil.getTestImageURL(), photoImageView);
	}

	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			if(arg0.getId() == R.id.topLayout){
				Intent intent = new Intent(BaseActivity.getTopActivity(), PersonInfoModifyActivity.class);
				intent.putExtra("MODEL", data);
				BaseActivity.getTopActivity().startActivity(intent);
			}
			
		}
	};
}
