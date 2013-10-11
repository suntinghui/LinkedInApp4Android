package com.hmd.activity.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.model.ProfileModel;
import com.hmd.util.ImageUtil;

public class NameCardRelativeLayout extends RelativeLayout {

	private ProfileModel data = null;
	
	public NameCardRelativeLayout(Context context, ProfileModel d) {
		super(context);
		this.data = d;
		
        LayoutInflater.from(context).inflate(R.layout.layout_name_card, this, true); 
        
        this.init();
	}

	private void init(){
		if(this.data == null) return;
		
		TextView tvName = (TextView)this.findViewById(R.id.tv_name_card_name);
		TextView tvCity = (TextView)this.findViewById(R.id.tv_name_card_city);
		TextView tvDistrict = (TextView)this.findViewById(R.id.tv_name_card_district);
		TextView tvBrief1 = (TextView)this.findViewById(R.id.tv_name_card_brief1);
		TextView tvBrief2 = (TextView)this.findViewById(R.id.tv_name_card_brief2);
		ImageView photoImageView = (ImageView) this.findViewById(R.id.iv_name_card_photo);
		
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
		
		ImageUtil.loadImage(R.drawable.img_card_head_portrait_small, ImageUtil.getTestImageURL(), photoImageView);
	}
}
