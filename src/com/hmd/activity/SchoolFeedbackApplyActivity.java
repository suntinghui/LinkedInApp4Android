package com.hmd.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hmd.R;
import com.hmd.util.PatternUtil;
import com.hmd.view.EditTextWithClearView;
import com.hmd.view.LKAlertDialog;

public class SchoolFeedbackApplyActivity extends AbsSubActivity implements OnClickListener {

	private EditTextWithClearView nameText;
	private EditTextWithClearView mailText;
	private EditTextWithClearView phoneText;

	private Button backButton;
	private Button okButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school_feedback_apply);

		nameText = (EditTextWithClearView) this.findViewById(R.id.nameText);
		mailText = (EditTextWithClearView) this.findViewById(R.id.mailText);
		phoneText = (EditTextWithClearView) this.findViewById(R.id.phoneText);

		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

		okButton = (Button) this.findViewById(R.id.okButton);
		okButton.setOnClickListener(this);
	}

	private boolean checkValue() {
		if ("".equals(nameText.getText().toString().trim())){
			Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
			return false;
			
		} else if ("".equals(mailText.getText().toString().trim())){
			Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
			return false;
			
		} else if (!PatternUtil.isValidEmail(mailText.getText().toString().trim())){
			Toast.makeText(this, "邮箱格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
			return false;
			
		} else if ("".equals(phoneText.getText().toString().trim())){
			Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
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
			if (checkValue()){
				LKAlertDialog dialog = new LKAlertDialog(this);
				dialog.setTitle("提示");
				dialog.setMessage("信息提交成功，管理员会与您联系，请耐心等待。");
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
			break;
		}
	}

}
