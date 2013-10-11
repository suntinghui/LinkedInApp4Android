package com.hmd.activity.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.hmd.R;

public class SchoolEventRelativeLayout extends RelativeLayout {

	public SchoolEventRelativeLayout(Context context) {
		super(context);
		
        LayoutInflater.from(context).inflate(R.layout.layout_school_event, this, true); 
        
        this.init();
	}

	private void init(){

	}
}
