package com.hmd.activity.component;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.hmd.R;
import com.hmd.activity.BaseActivity;
import com.hmd.activity.SchoolCardApplyActivity;

public class SchoolCardRelativeLayout extends RelativeLayout {
	
	private ImageButton applyButton;
	private BaseActivity context;

	public SchoolCardRelativeLayout(Context context) {
		super(context);
		this.context = (BaseActivity) context;

		LayoutInflater.from(context).inflate(R.layout.layout_school_card, this, true); 
        
        applyButton = (ImageButton) this.findViewById(R.id.btn_school_card_apply);
        applyButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				Intent intent = new Intent(SchoolCardRelativeLayout.this.context, SchoolCardApplyActivity.class);
				SchoolCardRelativeLayout.this.context.startActivityForResult(intent, 100);
			}
		});
	}

}
