package com.hmd.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.hmd.R;
import com.hmd.util.PatternUtil;
import com.hmd.view.EditTextWithClearView;

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

			break;
		}
	}

}
