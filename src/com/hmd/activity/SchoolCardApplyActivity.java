package com.hmd.activity;

import java.util.HashMap;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hmd.R;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.enums.LoginCode;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.PatternUtil;
import com.hmd.view.EditTextWithClearView;
import com.hmd.view.LKAlertDialog;

public class SchoolCardApplyActivity extends AbsSubActivity implements OnClickListener {

	private EditTextWithClearView nameText;
	private EditTextWithClearView mailText;
	private EditTextWithClearView phoneText;

	private Button backButton;
	private Button okButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_schoolcard_apply);

		nameText = (EditTextWithClearView) this.findViewById(R.id.nameText);
		mailText = (EditTextWithClearView) this.findViewById(R.id.mailText);
		phoneText = (EditTextWithClearView) this.findViewById(R.id.phoneText);

		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

		okButton = (Button) this.findViewById(R.id.okButton);
		okButton.setOnClickListener(this);
	}

	private boolean checkValue() {
		if ("".equals(nameText.getText().toString().trim())) {
			Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
			return false;

		} else if ("".equals(mailText.getText().toString().trim())) {
			Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
			return false;

		} else if (!PatternUtil.isValidEmail(mailText.getText().toString().trim())) {
			Toast.makeText(this, "邮箱格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
			return false;

		} else if ("".equals(phoneText.getText().toString().trim())) {
			Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
			return false;
		} else if (!PatternUtil.isPhoneNum(phoneText.getText().toString())) {
			Toast.makeText(this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			goback();
			break;

		case R.id.okButton:
			if (checkValue()) {
				this.doApply();
			}
			break;
		}
	}

	private void doApply() {
		if (!this.checkValue())
			return;

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", nameText.getText());
		paramMap.put("email", mailText.getText());
		paramMap.put("mobile", phoneText.getText());

		LKHttpRequest req1 = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_CARD_APPLY, paramMap, getApplyHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}

	private LKAsyncHttpResponseHandler getApplyHandler() {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				@SuppressWarnings("unchecked")
				HashMap<String, String> respMap = (HashMap<String, String>) obj;

				int returnCode = Integer.parseInt(respMap.get("rc"));
				String message = "";
				if (returnCode == 1) {
					message = "信息提交成功，管理员会与您联系，请耐心等待。";
				} else if (returnCode == 0) {
					message = "信息提交失败。";
				} else if (returnCode == -2) {
					message = "您已提交过校友龙卡申请，请耐心等待。";
				}

				LKAlertDialog dialog = new LKAlertDialog(SchoolCardApplyActivity.this);
				dialog.setTitle("提示");
				dialog.setMessage(message);
				dialog.setCancelable(false);
				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();

						goback();
					}
				});
				dialog.create().show();
			}

		};
	}

}
