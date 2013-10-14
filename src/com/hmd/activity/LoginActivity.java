package com.hmd.activity;

import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hmd.R;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.enums.LoginCode;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.view.EditTextWithClearView;

public class LoginActivity extends BaseActivity implements OnClickListener {
	
	private Button backButton = null;
	private Button completedButton = null;
	private EditTextWithClearView nameView = null;
	private EditTextWithClearView passwordView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_login);

		this.init();
	}

	private void init(){
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		completedButton = (Button) this.findViewById(R.id.completedButton);
		completedButton.setOnClickListener(this);
		
		nameView = (EditTextWithClearView) this.findViewById(R.id.nameText);
		passwordView = (EditTextWithClearView) this.findViewById(R.id.passwordText);
		
		nameView.setText("20131014001@qq.com");
		passwordView.setText("123");
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.finish();
			
			break;
			
		case R.id.completedButton:
			this.doLogin();
			
			break;
		}
	}

	private void doLogin(){
		if(!this.checkValue()) return;
		
		HashMap<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("name", nameView.getText());
		paramMap.put("password", passwordView.getText());
		
		LKHttpRequest req1 = new LKHttpRequest( HttpRequestType.HTTP_LOGIN, paramMap, getLoginHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在登录请稍候...", new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});	
	}
	
	private LKAsyncHttpResponseHandler getLoginHandler(){
		 return new LKAsyncHttpResponseHandler(){
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> respMap = (HashMap<String, String>) obj;
				int returnCode = Integer.parseInt(respMap.get("rc"));
				if (returnCode == LoginCode.SUCCESS){
					Constants.SESSION_ID = respMap.get("sid");
					
					Intent intent = new Intent(LoginActivity.this, SchoolActivity.class);
					LoginActivity.this.startActivity(intent);

				} else if (returnCode == LoginCode.WRONG_PASSWORD){
					LoginActivity.this.showDialog(BaseActivity.MODAL_DIALOG, "密码错误");
				} else if (returnCode == LoginCode.USER_NOT_EXIST) {
					LoginActivity.this.showDialog(BaseActivity.MODAL_DIALOG, "用户名不存在");
				} else if (returnCode == LoginCode.UNKNOWN_FAILURE){
					LoginActivity.this.showDialog(BaseActivity.MODAL_DIALOG, "登录失败，未知原因");
				}
			}
			 
		 };
	}
	
	private boolean checkValue(){
		if (nameView.getText().trim().equals("")){
			this.showToast("请输入用户名\\手机号\\邮箱");
			return false;
		} else if (passwordView.getText().trim().equals("")) {
			this.showToast("请输入密码");
			return false;
		}
		
		return true;
	}

}
