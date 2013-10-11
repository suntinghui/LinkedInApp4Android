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
		
		SwitchableScrollViewer svSuggestList = new SwitchableScrollViewer(this, this.entries, "相关推荐");
		
		LinearLayout llListContainer = (LinearLayout)this.findViewById(R.id.ll_suggest_people);
		llListContainer.removeAllViews();
		
		llListContainer.addView(svSuggestList);
	}
	
	private void prepareSampleData(){
		ArrayList<ProfileModel> friends = new ArrayList<ProfileModel>();
		
		ProfileModel nc = new ProfileModel();
		nc.setName("石清华");
		nc.setCity("北京");
		nc.setDistrict("海淀区");
		nc.setCompany("高德软件公司");
		nc.setPosition("项目总监");
		friends.add(nc);
		
		ProfileModel nc2 = new ProfileModel();
		nc2.setName("薛健");
		nc2.setCity("北京");
		nc2.setDistrict("朝阳区");
		nc2.setCompany("清远华程技术有限公司");
		nc2.setPosition("总裁");
		friends.add(nc2);
		
		ProfileModel nc3 = new ProfileModel();
		nc3.setName("张韵");
		nc3.setCity("北京");
		nc3.setDistrict("朝阳区");
		nc3.setCompany("高德软件公司");
		nc3.setPosition("总监");
		friends.add(nc3);
		
//		this.entries = friends;
	}
}
