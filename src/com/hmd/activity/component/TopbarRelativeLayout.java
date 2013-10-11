package com.hmd.activity.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.hmd.R;

public class TopbarRelativeLayout extends RelativeLayout {

	public int btnNavRes = 0;
	
	public TopbarRelativeLayout(Context context, OnClickListener onNavigation, int btnNavRes) {
		super(context);

		this.onNavigation = onNavigation;
		this.btnNavRes = btnNavRes;
		
        LayoutInflater.from(context).inflate(R.layout.layout_topbar, this, true); 
		this.init();
	}
	
	private OnClickListener onNavigation = null;

	private void init(){
		ImageButton btnNavigation = (ImageButton)this.findViewById(R.id.btn_topbar_category);
		
		btnNavigation.setBackgroundResource(this.btnNavRes);
		
		if(this.onNavigation != null){
			btnNavigation.setOnClickListener(this.onNavigation);
		}
	}
}
