package com.hmd.activity;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.LinearLayout;

import com.hmd.R;
import com.hmd.activity.component.SwitchableScrollViewer;
import com.hmd.model.ProfileModel;

public class SuggestPeopleActivity extends BaseActivity{
	
	private ArrayList<ProfileModel> entries = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_suggest_people);

		this.init();
		
	}

	@SuppressWarnings("unchecked")
	private void init(){
		
		Intent intent = this.getIntent();
		entries = (ArrayList<ProfileModel>) intent.getSerializableExtra("PROFILEMODELLIST");
		this.prepareSampleData();
		
		SwitchableScrollViewer svSuggestList = new SwitchableScrollViewer(this, this.entries, "相关推荐", false, true);
		
		LinearLayout llListContainer = (LinearLayout)this.findViewById(R.id.ll_suggest_people);
		llListContainer.removeAllViews();
		
		llListContainer.addView(svSuggestList);
	}
	
	private void prepareSampleData(){

	}
}
