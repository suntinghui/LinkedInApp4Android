package com.hmd.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hmd.R;
import com.hmd.model.ProfileModel;

public class PersonInfoModifyActivity extends BaseActivity {

	private ProfileModel model = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_personinfomodify);

		this.init();
	}

	private void init() {
		Intent intent = this.getIntent();
		model = (ProfileModel) intent.getSerializableExtra("MODEL");
		
	}

}
