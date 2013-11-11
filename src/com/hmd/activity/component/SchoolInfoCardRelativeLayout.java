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
import com.hmd.activity.BaseActivity;
import com.hmd.activity.SchoolInfoActivity;
import com.hmd.activity.ShowWebViewActivity;
import com.hmd.client.Constants;
import com.hmd.model.SchoolModel;
import com.hmd.util.ImageUtil;

public class SchoolInfoCardRelativeLayout extends RelativeLayout {
	
	private ImageView schoolLogoImageView = null;
	private TextView schoolNameTextView = null;
	private TextView schoolDescTextView = null;
	private ImageButton infoMoreButton = null;
	
	private SchoolModel school = null;

	public SchoolInfoCardRelativeLayout(Context context) {
		super(context);
		
        LayoutInflater.from(context).inflate(R.layout.layout_school_info_card, this, true); 
        
        this.init();
	}

	private void init(){
		schoolLogoImageView = (ImageView) this.findViewById(R.id.img_school_logo);
		
		schoolNameTextView = (TextView) this.findViewById(R.id.tv_school_info_name);
		schoolDescTextView = (TextView) this.findViewById(R.id.tv_school_info_desc);
		
		infoMoreButton = (ImageButton) this.findViewById(R.id.btn_school_info_more);
		infoMoreButton.setOnClickListener(new InfoMoreListener());
		
	}
	
	public void refresh(SchoolModel school){
		if (null != school){
			this.school = school;
			
			ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, school.getmLogoUrl(), schoolLogoImageView);
			
			schoolNameTextView.setText(school.getmName());
			schoolDescTextView.setText(school.getmDesc());
		}
	}
	
	class InfoMoreListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			/*
			Intent intent = new Intent(BaseActivity.getTopActivity(), ShowWebViewActivity.class);
			intent.putExtra("TITLE", "首都师范大学");
			intent.putExtra("URL", Constants.URL_CNU_EDU);
			BaseActivity.getTopActivity().startActivity(intent);
			*/
			
			Intent intent = new Intent(BaseActivity.getTopActivity(), SchoolInfoActivity.class);
			intent.putExtra("school", school);
			BaseActivity.getTopActivity().startActivity(intent);
		}
		
	}
}
