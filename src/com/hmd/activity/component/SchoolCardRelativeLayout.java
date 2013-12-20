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
import com.hmd.activity.SchoolCardApplyActivity;
import com.hmd.activity.SchoolCardIntroductionActivity;

public class SchoolCardRelativeLayout extends RelativeLayout {
	
	private Button applyButton;
	private BaseActivity context;
	private LinearLayout imageLayout;

	public SchoolCardRelativeLayout(Context context) {
		super(context);
		this.context = (BaseActivity) context;

		LayoutInflater.from(context).inflate(R.layout.layout_school_card, this, true); 
		
		imageLayout = (LinearLayout) this.findViewById(R.id.rl_school_card_content);
		imageLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SchoolCardRelativeLayout.this.context, SchoolCardIntroductionActivity.class);
				SchoolCardRelativeLayout.this.context.startActivityForResult(intent, 100);
			}
		});
        
		applyButton = (Button) this.findViewById(R.id.btn_schoolcard_apply);
		applyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SchoolCardRelativeLayout.this.context, SchoolCardApplyActivity.class);
				SchoolCardRelativeLayout.this.context.startActivityForResult(intent, 100);
			}
		});
	}

}
