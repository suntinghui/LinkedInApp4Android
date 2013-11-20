package com.hmd.activity;

import java.util.ArrayList;
import java.util.List;

import weibo4j.Timeline;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.hmd.R;
import com.hmd.client.Constants;
import com.hmd.model.WeiboItemModel;
import com.hmd.util.ActivityUtil;
import com.hmd.util.ListViewUtil;
import com.hmd.util.WeiboUtil;
import com.hmd.view.TimelineAdapter;

public class WeiboListActivity extends AbsSubActivity implements OnItemClickListener, OnClickListener {
	
	private static final int BASEAPP = 0;
	private static final int FEATURE = 0;
	private static final int pageSize = 10;
	
	private long totalCount = 0L;
	private long totalPage = 0L;
	private int currentPage = 1;
	
	private Button backButton = null;
	private Button moreButton = null;
	
	private ListView timelineListView = null;
	private List<Status> timelineList = null;
	private TimelineAdapter timelineAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_weibo_list);

		this.init();
	}
	
	private void init(){
		backButton = (Button) this.findViewById(R.id.btn_back);
		backButton.setOnClickListener(this);
		
		moreButton = (Button) this.findViewById(R.id.moreButton);
		moreButton.setOnClickListener(this);
		
		timelineListView = (ListView) this.findViewById(R.id.lv_timeline);
		// 设置空页面
		ActivityUtil.setEmptyView(timelineListView);
		timelineListView.setOnItemClickListener(this);
		
		timelineList = new ArrayList<Status>();
		timelineAdapter = new TimelineAdapter(timelineList);
		timelineListView.setAdapter(timelineAdapter);
		
		new LoadTimelineTask().execute();
	}
	
	class LoadTimelineTask extends AsyncTask<Object, Object, Object>{
		
		@Override
		protected void onPreExecute() {
			WeiboListActivity.this.showDialog(BaseActivity.PROGRESS_HUD, "正在加载微博...");
			
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... arg0) {
			doLoadTimeline();
			
			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			if(totalPage > 1){
				moreButton.setVisibility(View.VISIBLE);
			}
			
			timelineAdapter.notifyDataSetChanged();
			
			ListViewUtil.setListViewHeightBasedOnChildren(timelineListView);
			
			WeiboListActivity.this.hideDialog(BaseActivity.PROGRESS_HUD);
		}

	}
	
	private void doLoadTimeline() {
		try{
			Timeline timeline = new Timeline();
			Paging paging = new Paging();
			paging.count(pageSize).setPage(currentPage++);
			
			timeline.client.setToken(WeiboUtil.getToken());

			StatusWapper statusWapper = timeline.getUserTimelineByName(Constants.WEIBO_TIMELINE_SCREENNAME, paging, BASEAPP, FEATURE);
			totalCount = statusWapper.getTotalNumber();
			totalPage = (totalCount + pageSize - 1) / pageSize;
			
			timelineList.addAll(statusWapper.getStatuses());
			
		} catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Status status = this.timelineList.get(arg2);
		
		Intent intent = new Intent(this, WeiboDetailActivity.class);
		intent.putExtra("status", new WeiboItemModel(status));
		this.startActivity(intent);
		
	}
	
	@Override
	public void onBackPressed() {
		this.goback();
		super.onBackPressed();
	}
	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.btn_back:
			WeiboListActivity.this.goback();
			break;
			
		case R.id.moreButton:
			if (currentPage < totalPage){
				moreButton.setVisibility(View.VISIBLE);
			} else {
				moreButton.setVisibility(View.GONE);
			}
			
			new LoadTimelineTask().execute();
			
			break;
		}
		
	}

}
