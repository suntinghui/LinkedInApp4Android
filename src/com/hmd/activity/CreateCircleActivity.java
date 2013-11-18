package com.hmd.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hmd.R;

public class CreateCircleActivity extends AbsSubActivity implements OnClickListener {
	private EditText et_circle_name = null;
	private EditText et_circle_content = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create_circle);

		Button btn_back = (Button) this.findViewById(R.id.backButton);
		btn_back.setOnClickListener(this);
		
		Button btn_create = (Button)this.findViewById(R.id.btn_create);
		btn_create.setOnClickListener(this);
		et_circle_name = (EditText)this.findViewById(R.id.et_circle_name);
		et_circle_content = (EditText)this.findViewById(R.id.et_circle_content);
	}

	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.finish();
			break;
			
		case R.id.btn_create:
			if(checkValue()){
				createCircle();				
			}
			break;
		}
	}


	private void createCircle(){
		
	}
	
	private Boolean checkValue(){
		if(et_circle_name.length() == 0){
			this.showToast("圈子名称不能为空！");
			return false;
		}else if(et_circle_content.length() == 0){
			this.showToast("圈子内容不能为空！");
			return false;
		}
		return true;
	}
}
