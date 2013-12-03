package com.hmd.activity;

import java.util.HashMap;

import android.app.ActivityGroup;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout.LayoutParams;

import com.hmd.R;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.enums.LoginCode;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.ImageAnimation;
import com.hmd.util.PatternUtil;
import com.hmd.view.EditTextWithClearView;

public class NewsActivity extends ActivityGroup implements OnClickListener{
	private RelativeLayout mNewsMainLayout = null;
	private LayoutParams params = null;
	//������ʾ
	private TextView mNetEaseTop = null;
	// ���ŷ�����ÿ������Ŀ��
	private int mItemWidth = 0;
	// ��Ŀ�����ƶ���ʼλ��
	private int startX = 0;
	private Intent mIntent = null;
	// ������������
	private View mNewsMain = null;
	//�ײ�ѡ�б�־λ��ImageView
	private ImageView mImageView;
	//�ײ�Layout
	int startLeft;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);      
        // ��ʼ���ؼ�
        initeViews();
    }
    
    /**
     * ��ʼ���ؼ�
     */
    private void initeViews(){
    	
//    	// ����ѡ����Ŀ����
//    	mSelectedItem = new TextView(this);
//    	mSelectedItem.setText(R.string.title_news_category_tops);
//    	mSelectedItem.setTextColor(Color.WHITE);
//    	mSelectedItem.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
//    	mSelectedItem.setGravity(Gravity.CENTER);
//    	mSelectedItem.setWidth((getScreenWidth() - DimensionUtility.dip2px(this, 20)) / 6);
//    	mSelectedItem.setBackgroundResource(R.drawable.slidebar);
//    	RelativeLayout.LayoutParams param = new RelativeLayout.LayoutParams(
//    			LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//    	param.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE);
//    	
//    	mHeader = (RelativeLayout) findViewById(R.id.layout_title_bar);
//    	mNetEaseTop = (TextView) findViewById(R.id.tv_netease_top);
//    	
//    	mHeader.addView(mSelectedItem, param);
    	
    	// ����ͷ����������
    	mIntent = new Intent(NewsActivity.this, TopicNews.class);
    	mNewsMain = getLocalActivityManager().startActivity(
    			"TopicNews", mIntent).getDecorView();
    	params = new LayoutParams(
    			LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    	mNewsMainLayout = (RelativeLayout) findViewById(R.id.layout_news_main);
    	mNewsMainLayout.addView(mNewsMain, params);
    	
    }
    
    /**
     * ��ȡ��Ļ�Ŀ��
     * @return
     */
    private int getScreenWidth(){
    	WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
//		Point point = new Point();
//		display.getSize(point);
//		int screenWidth = point.x; 
		int screenWidth = display.getWidth();
		return screenWidth;
    }
    
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}