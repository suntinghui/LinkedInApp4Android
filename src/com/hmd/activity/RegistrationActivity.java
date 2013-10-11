package com.hmd.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hmd.R;
import com.hmd.view.EditTextWithClearView;
import com.hmd.view.LKAlertDialog;

public class RegistrationActivity extends BaseActivity implements OnClickListener {
	
	private Button backButton = null;
	private Button completedButton = null;
	private EditTextWithClearView nameView = null;
	private EditTextWithClearView passwordView = null;
	private EditTextWithClearView passwordConfirmView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_registration);

		this.init();
	}

	private void init(){
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		completedButton = (Button) this.findViewById(R.id.completedButton);
		completedButton.setOnClickListener(this);
		
		nameView = (EditTextWithClearView) this.findViewById(R.id.nameText);
		passwordView = (EditTextWithClearView) this.findViewById(R.id.passwordText);
		passwordConfirmView = (EditTextWithClearView) this.findViewById(R.id.passwordConfirmText);
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.finish();
			
			break;
			
		case R.id.completedButton:
			this.doRegistration();
			
			break;
		}
	}

	private void doRegistration(){
		if(!this.checkValue()) return;
		
		LKAlertDialog dialog = new LKAlertDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("注册成功,请登录。");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				
				Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
				RegistrationActivity.this.startActivity(intent);
				
				RegistrationActivity.this.finish();
			}
		});
		
		dialog.create().show();
	}
	
	private boolean checkValue(){
		if (nameView.getText().trim().equals("")){
			this.showToast("请输入用户名\\手机号\\邮箱");
			return false;
		} else if (passwordView.getText().trim().equals("")) {
			this.showToast("请输入密码");
			return false;
		} else if (passwordConfirmView.getText().trim().equals("")) {
			this.showToast("请输入确认密码");
			return false;
		} else if (!passwordView.getText().trim().equals(passwordConfirmView.getText().trim())) {
			this.showToast("两次密码输入不一致");
			return false;
		} 
		
		return true;
	}
	
}
