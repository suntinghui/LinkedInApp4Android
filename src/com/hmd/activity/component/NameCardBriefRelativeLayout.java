package com.hmd.activity.component;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.activity.ProfileActivity;
import com.hmd.model.ProfileModel;
import com.hmd.util.ImageUtil;

public class NameCardBriefRelativeLayout extends RelativeLayout {

	private ProfileModel data = null;
	
	public NameCardBriefRelativeLayout(final Context context, ProfileModel d) {
		super(context);
		
		this.data = d;
		
        LayoutInflater.from(context).inflate(R.layout.layout_name_card_brief, this, true); 
        
        this.init();
        
//        this.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				
//				Intent intent = new Intent(context, ProfileActivity.class);
//				intent.putExtra("IDENTITY", "he");
//				intent.putExtra("PROFILE", data);
//				context.startActivity(intent);
//			}
//		});
	}

	private void init(){
		if(this.data == null) return;
		
		TextView tvName = (TextView)this.findViewById(R.id.tv_name_card_brief_name);
		ImageView photoImagView = (ImageView) this.findViewById(R.id.iv_name_card_brief_photo);
		
		tvName.setText(this.data.getName());
		ImageUtil.loadImage(R.drawable.img_card_head_portrait_small, this.data.getPic(), photoImagView);
	}
}
