package com.hmd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.hmd.R;

public class MainActivity extends BaseActivity implements OnClickListener {
	
	private Button loginButton = null;
	private Button registrButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_main);

		this.init();
	}
	
	private void init(){
		loginButton = (Button) this.findViewById(R.id.loginButton);
		loginButton.setOnClickListener(this);
		
		registrButton = (Button) this.findViewById(R.id.registrButton);
		registrButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.loginButton:
			Intent loginIntent = new Intent(this, LoginActivity.class);
			this.startActivity(loginIntent);
			
			break;
			
		case R.id.registrButton:
			Intent registrIntent = new Intent(this, RegistrationActivity.class);
			this.startActivity(registrIntent);
			
			break;
		}
	}
}
