package com.hmd.activity;

import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.hmd.R;

public class NewsActivity extends ActivityGroup implements OnClickListener{
	private RelativeLayout mNewsMainLayout = null;
	private LayoutParams params = null;
	private Intent mIntent = null;
	// 设置新闻主题
	private View mNewsMain = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);      
        initeViews();
    }
    
    private void initeViews(){
    	
    	// 设置头条新闻主体
    	mIntent = new Intent(NewsActivity.this, TopicNews.class);
    	mNewsMain = getLocalActivityManager().startActivity(
    			"TopicNews", mIntent).getDecorView();
    	params = new LayoutParams(
    			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	mNewsMainLayout = (RelativeLayout) findViewById(R.id.layout_news_main);
    	mNewsMainLayout.addView(mNewsMain, params);
    	
    }
    
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}