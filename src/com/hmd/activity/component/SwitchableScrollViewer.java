package com.hmd.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.model.ProfileModel;

public class SwitchableScrollViewer extends ScrollView {

	private TextView tvTitle = null;
	
	private ArrayList<ProfileModel> entries = null;
	private Context mContext = null;
	private boolean isList = true;
	private ImageButton btnList = null;
	private String mTitle = null;
	
	public SwitchableScrollViewer(Context context){
		super(context);
		this.mContext = context;
	}
	
	public SwitchableScrollViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		
		this.init();
	}
	
	public SwitchableScrollViewer(Context context, ArrayList<ProfileModel> e, String title) {
		super(context);

		this.mContext = context;
		this.entries = e;
		this.mTitle = title;
		
		this.init();
	}

	private void init(){
		LayoutInflater.from(this.mContext).inflate(R.layout.layout_switchable_scrollview, this, true);
		
		tvTitle = (TextView)this.findViewById(R.id.tv_friend_title);
		if(this.mTitle != null){
			tvTitle.setText(this.mTitle);
		}
		
		this.btnList = (ImageButton)this.findViewById(R.id.btn_layout_switch);
		
		btnList.setOnClickListener(this.onSwitchView);
		
		if(this.entries == null){
			if(this.isList){
				this.btnList.setBackgroundResource(R.drawable.img_card_list_two);
			}else{
				this.btnList.setBackgroundResource(R.drawable.img_card_list_one);
			}
			
		}else{
			
			this.refreshContent();
		}
	}
	
	private void refreshContent(){
		if(this.mTitle != null){
			tvTitle.setText(this.mTitle);
		}
		
		if(this.entries == null) return;
		
		ScrollView svContent = (ScrollView)this.findViewById(R.id.sv_container);
		
		svContent.removeAllViews();
		
		if(this.isList){
			NameCardListLinearLayout glNameCards = new NameCardListLinearLayout(this.mContext, this.entries);
			svContent.addView(glNameCards);
			this.btnList.setBackgroundResource(R.drawable.img_card_list_two);

		}else{
			NameCardListTableLayout glNameCards = new NameCardListTableLayout(this.mContext, this.entries);
			svContent.addView(glNameCards);
			this.btnList.setBackgroundResource(R.drawable.img_card_list_one);
		}
	}
	
	public void setTitle(String title){
		this.mTitle = title;
	}
	
	private OnClickListener onSwitchView = new OnClickListener(){

		@Override
		public void onClick(View v) {
			isList = !isList;
			refreshContent();
		}
	};
	
	public void refresh(ArrayList<ProfileModel> entries) {
		this.setVisibility(View.VISIBLE);
		
		this.entries = entries;
		
		this.refreshContent();
	}
	
	public ArrayList<ProfileModel> getTestData(){
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
		
		return friends;
	}
		
}
