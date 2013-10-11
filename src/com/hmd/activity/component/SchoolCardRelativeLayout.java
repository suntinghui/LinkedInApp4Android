package com.hmd.activity.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.hmd.R;

public class SchoolCardRelativeLayout extends RelativeLayout {

	public SchoolCardRelativeLayout(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.layout_school_card, this, true); 
        
        this.init();
	}

	private void init(){
		
	}
}
