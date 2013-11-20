package com.hmd.activity.component;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.activity.BaseActivity;
import com.hmd.activity.ProfileActivity;
import com.hmd.client.HttpRequestType;
import com.hmd.model.ProfileModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.ImageUtil;

public class NameCardRelativeLayout extends RelativeLayout {

	private ProfileModel data = null;
	private Button	 btnAttention = null;
	private Context context;
	private ProfileModel detailModel = null;
	
	public NameCardRelativeLayout(Context context, ProfileModel d) {
		super(context);
		this.context = context;
		this.data = d;
		
        LayoutInflater.from(context).inflate(R.layout.layout_name_card, this, true); 
        
        this.init();
	}

	private void init(){
		if(this.data == null) return;
		
		TextView tvName = (TextView)this.findViewById(R.id.tv_name_card_name);
		TextView tvGender = (TextView)this.findViewById(R.id.tv_name_card_gender);
		TextView tvProvince = (TextView)this.findViewById(R.id.tv_name_card_province);
		btnAttention = (Button)this.findViewById(R.id.btn_name_card_attention);
		btnAttention.setOnClickListener(listener);
		ImageView photoImageView = (ImageView) this.findViewById(R.id.iv_name_card_photo);
		photoImageView.setOnClickListener(listener);
		tvName.setText(this.data.getName()!=null ? this.data.getName():"name");
		tvGender.setText(this.data.getGender()==0 ? "女":"男");
		if(this.data.getFlag().equals("2")){
			btnAttention.setVisibility(View.GONE);
		}else{
			btnAttention.setText(this.data.getFlag().equals("0") ? "关注":"取消关注");
		}
		
		String province = this.data.getProvince() != null && this.data.getProvince().length() != 0 && !this.data.getProvince().equalsIgnoreCase("null") ? this.data.getProvince():"未知";
		String city = this.data.getCity() != null && this.data.getCity().length() != 0 && !this.data.getCity().equalsIgnoreCase("null") ? this.data.getCity():"未知";
		tvProvince.setText(province + "--" + city);
		
		ImageUtil.loadImage(R.drawable.img_card_head_portrait_small, this.data.getPic(), photoImageView);
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			switch (arg0.getId()) {
			case R.id.iv_name_card_photo:
				LKHttpRequestQueue queue = new LKHttpRequestQueue();
				queue.addHttpRequest(getProfileRequest());
				queue.executeQueue("正在请求数据...", new LKHttpRequestQueueDone());
				break;
			case R.id.btn_name_card_attention:
				if(NameCardRelativeLayout.this.data.getFlag().equals("0")){
					//加关注
					refreshAddAttentionData();
				}else{
					//取消关注
					refreshCancelAttentionData();
				}
				break;
			default:
				break;
			}
			
		}
	};
	
	// 查看个人基本信息
	private LKHttpRequest getProfileRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_PROFILE_BASIC, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				detailModel = (ProfileModel) obj;
				detailModel.setId(data.getId());
				TransformToProfileScreen();
			}
		}, data.getId());
		
		return request;
	}
	
	// 显示个人信息
	private void TransformToProfileScreen(){
		
		Intent intent = new Intent(this.context, ProfileActivity.class);  
		intent.putExtra("PROFILE", detailModel);
		intent.putExtra("IDENTITY", "he");
		this.context.startActivity(intent);  
	}
		
	//加关注
	private void refreshAddAttentionData(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getAddAttentionRequest());
		queue.executeQueue("正在关注...", new LKHttpRequestQueueDone());
		
	}
	
	
	private LKHttpRequest getAddAttentionRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_FRIENDS_FOLLOW, null, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				int obj2 = (Integer) obj;
				if(obj2 == 1){
					((ProfileActivity)(BaseActivity.getTopActivity())).refreshData();
					NameCardRelativeLayout.this.data.setFlag("1");
					btnAttention.setText("取消关注");
				}else{
					//失败
				}
				
			}
		}, NameCardRelativeLayout.this.data.getId());
		
		return request;
	}
	//取消关注
	private void refreshCancelAttentionData(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getCancelAttentionRequest());
		queue.executeQueue("正在取消关注...", new LKHttpRequestQueueDone());
		
	}
	
	
	private LKHttpRequest getCancelAttentionRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_FRIENDS_UNFOLLOW, null, new LKAsyncHttpResponseHandler() {
			@SuppressWarnings("unchecked")
			@Override
			public void successAction(Object obj) {
				int obj2 = (Integer) obj;
				if(obj2 == 1){
					((ProfileActivity)(BaseActivity.getTopActivity())).refreshData();
//					NameCardRelativeLayout.this.data.setFlag("0");
//					btnAttention.setText("关注");
				}else{
					//失败
				}
				
			}
		}, NameCardRelativeLayout.this.data.getId());
		
		return request;
	}
}
