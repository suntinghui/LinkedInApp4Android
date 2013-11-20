package com.hmd.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.model.SchoolModel;
import com.hmd.util.ImageUtil;
import com.hmd.util.WeiboUtil;

public class SchoolInfoActivity extends AbsSubActivity implements OnClickListener {

	private Button backButton = null;
	private TextView titleView = null;
	private ImageView imageView = null;
	private RelativeLayout weiboLayout = null;
	private TextView contentView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school_info);

		SchoolModel school = (SchoolModel) this.getIntent().getSerializableExtra("school");

		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

		titleView = (TextView) this.findViewById(R.id.titleTV);
		titleView.setText(school.getmName());

		imageView = (ImageView) this.findViewById(R.id.imageView);
		ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, school.getmLogoUrl(), imageView);

		weiboLayout = (RelativeLayout) this.findViewById(R.id.weiboLayout);
		// 如果没有登录微博则不显示此按纽。
		weiboLayout.setVisibility(WeiboUtil.hasAuth() ? View.VISIBLE : View.GONE);
		weiboLayout.setOnClickListener(this);

		contentView = (TextView) this.findViewById(R.id.content);
		contentView.setText(school.getmDesc());

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			this.goback();
			break;

		case R.id.weiboLayout:
			Intent intent = new Intent(this, WeiboListActivity.class);
			this.startActivity(intent);

			break;
		}
	}

	public void onBackPressed() {
		this.goback();
	}

}
