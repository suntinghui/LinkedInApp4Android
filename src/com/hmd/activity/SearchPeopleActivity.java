package com.hmd.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hmd.R;
import com.hmd.view.EditTextWithClearView;

public class SearchPeopleActivity extends AbsSubActivity implements OnClickListener {
	
	private Button backButton = null;
	private Button searchButton = null;
	private ListView listView = null;
	private EditTextWithClearView keyEditView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_search_people);
		
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		searchButton = (Button) this.findViewById(R.id.btn_search);
		searchButton.setOnClickListener(this);
		
		keyEditView = (EditTextWithClearView) this.findViewById(R.id.keyText);
		
		listView = (ListView) this.findViewById(R.id.listView);
		
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.backButton:
			goback();
			break;
		
		case R.id.btn_search:
			
			break;
		}
	}

}
