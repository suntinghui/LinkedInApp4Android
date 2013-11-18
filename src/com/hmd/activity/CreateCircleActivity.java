package com.hmd.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hmd.R;

public class CreateCircleActivity extends AbsSubActivity implements OnClickListener {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_create_circle);

		Button btn_back = (Button) this.findViewById(R.id.profileButton);
		btn_back.setOnClickListener(this);
	}

	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.backButton:
			this.finish();
			
			break;
			
		case R.id.completedButton:
			
			break;
		}
	}


}
