package com.hmd.activity.component;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hmd.R;
import com.hmd.activity.BaseActivity;
import com.hmd.activity.PhotoWallActivity;
import com.hmd.activity.SchoolCardApplyActivity;
import com.hmd.activity.SchoolFeedbackApplyActivity;
import com.hmd.activity.SchoolFeedbackIntroductionActivity;

public class SchoolPhotoWallRelativeLayout extends RelativeLayout {

	private Button applyButton;
	private LinearLayout imageLayout;
	private BaseActivity context;

	public SchoolPhotoWallRelativeLayout(Context context) {
		super(context);
		this.context = (BaseActivity) context;

		LayoutInflater.from(context).inflate(R.layout.layout_school_photowall, this, true);

		imageLayout = (LinearLayout) this.findViewById(R.id.rl_school_card_content);
		imageLayout.setOnClickListener(new Listener());

		applyButton = (Button) this.findViewById(R.id.btn_schoolcard_apply);
		applyButton.setOnClickListener(new Listener());
	}

	class Listener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_schoolcard_apply){
				Intent intent = new Intent(SchoolPhotoWallRelativeLayout.this.context, PhotoWallActivity.class);
				SchoolPhotoWallRelativeLayout.this.context.startActivityForResult(intent, 100);
			} 
			
			
		}

	}

}
