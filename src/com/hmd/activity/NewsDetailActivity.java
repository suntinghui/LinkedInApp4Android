package com.hmd.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.model.MediaModel;
import com.hmd.util.ImageUtil;

public class NewsDetailActivity extends AbsSubActivity implements OnClickListener {
	private LinearLayout layout_images = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_news_detail);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		
		layout_images = (LinearLayout)this.findViewById(R.id.layout_images);
		MediaModel model = new MediaModel();
		model = model.getTestMedia();
		
		TextView tv_title = (TextView)this.findViewById(R.id.tv_title);
		tv_title.setText(model.getTitle());
		TextView tv_time = (TextView)this.findViewById(R.id.tv_time);
		tv_time.setText(model.getTime());
		TextView tv_content = (TextView)this.findViewById(R.id.tv_content);
		tv_content.setText(model.getContent());
		
		String[] picUrl = model.getPics();
		for(int i = 0; i<picUrl.length; i++){
			ImageView imageView = new ImageView(this);
			LinearLayout.LayoutParams iv_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			iv_params.setMargins(0, 5, 0, 0);
			layout_images.addView(imageView, iv_params);
			ImageUtil.loadImage(R.drawable.image01, picUrl[i], imageView);
		}
		
	}

	
	@Override
	public void onClick(View view) {
		switch(view.getId()){
		case R.id.btn_back:
			this.goback();
			break;
			
		}
	}


}
