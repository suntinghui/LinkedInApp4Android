package com.hmd.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.hmd.R;
import com.hmd.model.TimelineModel;

public class ProfileTimelineLinearLayout extends LinearLayout {

	private LinearLayout llTimelineLayout = null;
	private Context mContext = null;
	
	public ProfileTimelineLinearLayout(Context context) {
		super(context);
		this.mContext = context;
		
        this.init();
	}
	
	public ProfileTimelineLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		
		this.init();
	}

	private void init(){
		LayoutInflater.from(this.mContext).inflate(R.layout.layout_profile_timeline, this, true);
		llTimelineLayout = (LinearLayout)this.findViewById(R.id.ll_profile_timeline);
	}
	
	public void refresh(ArrayList<TimelineModel> timelineList){
		this.setVisibility(View.VISIBLE);
		
		for(int i = 0; i < timelineList.size(); i ++){

			ProfileCardRelativeLayout profile = new ProfileCardRelativeLayout(this.mContext, timelineList.get(i));
			
			profile.setPadding(0, 0, 0, 0);
			
			llTimelineLayout.addView(profile,
					new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT, 
							LinearLayout.LayoutParams.WRAP_CONTENT));	

		}
	}
	
	public ArrayList<TimelineModel> getTestData(){
		ArrayList<TimelineModel> entries = new ArrayList<TimelineModel>();
		
		TimelineModel p1 = new TimelineModel();
		p1.setStartTime("2013");
		p1.setEndTime("今");
		p1.setCompany("高德软件公司");
		p1.setPosition("项目经理");
		p1.setDescription("负责苹果地图项目");
		
		entries.add(p1);
		
		TimelineModel p2 = new TimelineModel();
		p2.setStartTime("2010");
		p2.setEndTime("2013");
		p2.setCompany("北京四维图新科技股份有限公司");
		p2.setPosition("项目经理");
		p2.setDescription("四维图新二代数据制作平台项目");
		
		entries.add(p2);
		
		TimelineModel p3 = new TimelineModel();
		p3.setStartTime("2005");
		p3.setEndTime("2010");
		p3.setSchool("University of Otago");
		p3.setMajor("Computer Science");
		p3.setDescription("Top student, scholarship");
		
		entries.add(p3);
		
		return entries;
	}
}
