package com.hmd.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hmd.R;
import com.hmd.model.AnnouncementModel;

public class AnnouncementListActivity extends BaseActivity implements OnClickListener, OnItemClickListener {
	
	private Button backButton = null;
	private ListView listView = null;
	
	private ArrayList<AnnouncementModel> modelList = new ArrayList<AnnouncementModel>();
	private ArrayList<HashMap<String, String>> mapList = new ArrayList<HashMap<String, String>>(); 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setContentView(R.layout.activity_announcement_list);
		
		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		
		listView = (ListView) this.findViewById(R.id.transList);
		//生成适配器的Item和动态数组对应的元素  
        SimpleAdapter listItemAdapter = new SimpleAdapter(this, mapList,
            R.layout.listview_item_announcement, 
            new String[] {"title","content", "date"},   
            //ImageItem的XML文件里面的一个ImageView,两个TextView ID  
            new int[] {R.id.annTitle, R.id.annContent, R.id.annDate}  
        );             
       
        listView.setAdapter(listItemAdapter); 
        listView.setOnItemClickListener(this);
        
        // 加载数据
        
	}
	

	@Override
	public void onClick(View arg0) {
		this.finish();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		try{
			Intent intent = new Intent(this, AnnouncementDetailActivity.class);
			//intent.putExtra("announcement", modelList.get(position));
			startActivityForResult(intent, 0);
		} catch(Exception e){
			
		}
		
	}
	
}
