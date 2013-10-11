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
import com.hmd.client.HttpRequestType;
import com.hmd.model.AnnouncementModel;
import com.hmd.model.TimelineModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;

public class SchoolNoticeRelativeLayout extends RelativeLayout {
	
	private ImageButton noticeMoreButton = null;

	public SchoolNoticeRelativeLayout(Context context) {
		super(context);

		LayoutInflater.from(context).inflate(R.layout.layout_school_notice, this, true); 
        
        this.init();
	}

	private void init(){
		TextView tvNotice = (TextView)this.findViewById(R.id.tv_school_notice);
		String content = "各院（系）、单位：\r\n根据北京市政府办公厅精神，经过校长办公室讨论决定，现将2013年“十一”放假安排通知如下\r\n";
		content += "2013年国庆节放假9天（9月29日-10月7日）。其中10月1日-10月3日为国庆节法定节假日。";
		content += "9月28日（星期六）、10月12日（星期六）上星期一的课。";
		tvNotice.setText(content);
		
		noticeMoreButton = (ImageButton) this.findViewById(R.id.btn_school_notice_more);
		noticeMoreButton.setOnClickListener(new NoticeMoreListener());
	}
	
	class NoticeMoreListener implements OnClickListener{

		@Override
		public void onClick(View arg0) {
			refreshData();
		}
		
	}
	
	private void refreshData(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getNoticeListRequest());
		queue.executeQueue("正在查询公告...", new LKHttpRequestQueueDone());
		
	}
	
	// 查看个人履历
	private LKHttpRequest getNoticeListRequest(){
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("page", "1");
		paramMap.put("num", "20");
		
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_COLLEGE_BROADCAST_LIST, paramMap, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
//				Intent intent = new Intent(BaseActivity.getTopActivity(), AnnouncementListActivity.class);
//				intent.putExtra("LIST", (ArrayList<AnnouncementModel>)obj);
//				BaseActivity.getTopActivity().startActivity(intent);
			}
		});
		
		return request;
	}

}
