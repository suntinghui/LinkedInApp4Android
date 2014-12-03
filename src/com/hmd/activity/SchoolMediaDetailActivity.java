package com.hmd.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.client.TestMedia;
import com.hmd.model.MediaModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.util.ImageUtil;

public class SchoolMediaDetailActivity extends AbsSubActivity implements OnClickListener {
	
	private LinearLayout layout_images = null;
	private ArrayList<String> picUrlList;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_news_detail);
		
		MediaModel model = (MediaModel) this.getIntent().getSerializableExtra("MODEL");

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		layout_images = (LinearLayout) this.findViewById(R.id.layout_images);

		TextView tv_title = (TextView) this.findViewById(R.id.tv_title);
		tv_title.setText(model.getTitle());
		
		TextView tv_time = (TextView) this.findViewById(R.id.tv_time);
		tv_time.setText(model.getTime());
		
		TextView tv_content = (TextView) this.findViewById(R.id.tv_content);
		tv_content.setText(model.getContent());

		
		
		picUrlList = model.getPics();
		for (int i = 0; i < picUrlList.size(); i++) {
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			iv_params.setMargins(0, 10, 0, 0);
			layout_images.addView(imageView, iv_params);
			imageView.setTag(i+1000);
			ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, picUrlList.get(i), imageView);
			imageView.setOnClickListener(this);
		}

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			this.goback();
			break;

		}
		if(view.getId() == R.id.btn_back){
			this.goback();
		}else{
			Intent intent = new Intent(SchoolMediaDetailActivity.this, ImageDetailsActivity.class);
			int tag = ((Integer) view .getTag()).intValue()-1000;
			intent.putExtra("pic", picUrlList.get(tag));
			intent.putExtra("titleTag", "图片");
			startActivity(intent);
		}
	}

	
}
