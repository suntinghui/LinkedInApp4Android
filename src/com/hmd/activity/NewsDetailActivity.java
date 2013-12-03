package com.hmd.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.model.GroupModel;
import com.hmd.model.MediaModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.ImageUtil;

public class NewsDetailActivity extends AbsSubActivity implements OnClickListener {
	private LinearLayout layout_images = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_news_detail);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		layout_images = (LinearLayout)this.findViewById(R.id.layout_images);
		MediaModel model = new MediaModel();
		model = model.getTestMedia();
		
		TextView tv_title = (TextView)this.findViewById(R.id.tv_title);
		tv_title.setText(model.getTitle());
		TextView tv_time = (TextView)this.findViewById(R.id.tv_time);
		tv_time.setText(model.getTime());
		TextView tv_content = (TextView)this.findViewById(R.id.tv_content);
		tv_content.setText(model.getContent());
		
		String[] picUrl = model.getPics();
		for(int i = 0; i<picUrl.length; i++){
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			layout_images.addView(imageView, iv_params);
			ImageUtil.loadImage(R.drawable.image01, picUrl[i], imageView);
		}
		
	}

	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.gobackWithResult(1, this.getIntent());
			break;
			
		}
	}

//
//	private void createCircle(){
//		LKHttpRequestQueue queue = new LKHttpRequestQueue();
//		queue.addHttpRequest(getCreateRequest());
//		queue.executeQueue("正在请求数据...", new LKHttpRequestQueueDone());
//	}
//	
//	private Boolean checkValue(){
//		if(et_circle_name.length() == 0){
//			this.showToast("圈子名称不能为空！");
//			return false;
//		}else if(et_circle_content.length() == 0){
//			this.showToast("圈子内容不能为空！");
//			return false;
//		}
//		return true;
//	}
//	
//	// 查看所有圈子列表
//	private LKHttpRequest getCreateRequest() {
//		HashMap<String, Object> paramMap = new HashMap<String, Object>();
//		paramMap.put("name", et_circle_name.getText().toString());
//		paramMap.put("desc", et_circle_content.getText().toString());
//		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_GROUP_CREATE, paramMap, new LKAsyncHttpResponseHandler() {
//			@SuppressWarnings("unchecked")
//			@Override
//			public void successAction(Object obj) {
//				if (null != obj) {
//					if(((Integer)obj) == 1){
//						NewsDetailActivity.this.showToast("圈子创建成功！");
//						NewsDetailActivity.this.gobackWithResult(1, NewsDetailActivity.this.getIntent());
//					}else{
//						NewsDetailActivity.this.showToast("圈子创建失败！");
//					}
//					
//				} else {
//					
//				}
//
//			}
//		});
//
//		return request;
//	}
}
