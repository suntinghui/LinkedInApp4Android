package com.hmd.activity.component;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.activity.AnnouncementListActivity;
import com.hmd.activity.BaseActivity;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.model.AnnouncementModel;
import com.hmd.model.TimelineModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;

public class SchoolNoticeRelativeLayout extends RelativeLayout {
	
	private ImageButton noticeMoreButton = null;
	
	private TextView tvNoticeTitle = null;
	private TextView tvNoticePreview = null;
	private TextView tvNoticeTime = null;

	public SchoolNoticeRelativeLayout(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.layout_school_notice, this, true); 
        
        this.init();
	}

	private void init(){
		tvNoticeTitle = (TextView)this.findViewById(R.id.tv_notice_title);
		tvNoticePreview = (TextView) this.findViewById(R.id.tv_notice_preview);
		tvNoticeTime = (TextView) this.findViewById(R.id.tv_notice_time);
		
		noticeMoreButton = (ImageButton) this.findViewById(R.id.btn_school_notice_more);
		noticeMoreButton.setOnClickListener(new NoticeMoreListener());
	}
	
	public void refresh(AnnouncementModel model){
		this.setVisibility(View.VISIBLE);
		
		tvNoticeTitle.setText("    "+model.getTitle());
		tvNoticePreview.setText(model.getPreview());
		tvNoticeTime.setText(model.getTime());
	}
	
	class NoticeMoreListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			getAnnouncementList();
		}
		
	}
	
	private void getAnnouncementList(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getNoticeListRequest());
		queue.executeQueue("正在查询公告...", new LKHttpRequestQueueDone());
		
	}
	
	// 查询公告列表
	private LKHttpRequest getNoticeListRequest(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("page", "1");
		paramMap.put("num", Constants.PAGESIZE + "");
		paramMap.put("previewLen", "200"); //预览长度，即取正文内容前几个字符，范围[0,200]，0为关闭预览
		
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_COLLEGE_BROADCAST_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				HashMap<String, Object> map = (HashMap<String, Object>)obj;
				
				Intent intent = new Intent(BaseActivity.getTopActivity(), AnnouncementListActivity.class);
				intent.putExtra("TOTAL", (Integer)map.get("total"));
				intent.putExtra("LIST", (ArrayList<AnnouncementModel>)map.get("list"));
				BaseActivity.getTopActivity().startActivity(intent);
			}
		});
		
		return request;
	}

}
