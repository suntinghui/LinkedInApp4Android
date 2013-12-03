package com.hmd.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.activity.BaseActivity;
import com.hmd.activity.NewsDetailActivity;
import com.hmd.model.MediaModel;
import com.hmd.util.ImageUtil;

public class SchoolMediaRelativeLayout extends RelativeLayout {

	private TextView titleView;
	private Button moreButton;

	private LinearLayout bodyLayout;

	private BaseActivity context;
	private String title;
	private OnClickListener moreListener;
	private ArrayList<MediaModel> mediaList;

	public SchoolMediaRelativeLayout(BaseActivity context, String title, OnClickListener moreListener, ArrayList<MediaModel> list) {
		super(context);
		this.context = context;
		this.title = title;
		this.moreListener = moreListener;
		this.mediaList = list;

		this.init();
	}

	private void init() {
		LayoutInflater.from(context).inflate(R.layout.layout_school_media, this, true);
		bodyLayout = (LinearLayout) this.findViewById(R.id.bodyLayout);

		titleView = (TextView) this.findViewById(R.id.titleView);
		titleView.setText(title);

		moreButton = (Button) this.findViewById(R.id.moreButton);
		moreButton.setOnClickListener(moreListener);

		this.refresh();
	}

	public void refresh() {
		this.setVisibility(View.VISIBLE);

		for (int i = 0; i < this.mediaList.size(); i++) {

			MediaLayout layout = new MediaLayout(this.context, this.mediaList.get(i));

			layout.setPadding(0, 0, 0, 0);

			bodyLayout.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		}
	}

}

class MediaLayout extends LinearLayout implements View.OnClickListener {

	public BaseActivity context;

	private LinearLayout rootLayout;
	private ImageView imageView;
	private TextView titleView;
	private TextView contentView;

	private MediaModel media;

	public MediaLayout(BaseActivity context) {
		super(context);
		this.context = context;

		this.init();
	}

	public MediaLayout(BaseActivity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;

		this.init();
	}

	public MediaLayout(BaseActivity context, MediaModel media) {
		super(context);
		this.context = context;
		this.media = media;

		this.init();
	}

	private void init() {
		LayoutInflater.from(this.context).inflate(R.layout.layout_media_card, this, true);

		rootLayout = (LinearLayout) this.findViewById(R.id.rootLayout);
		rootLayout.setOnClickListener(this);

		imageView = (ImageView) this.findViewById(R.id.imageView);
		ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, media.getPics()[0], imageView);

		titleView = (TextView) this.findViewById(R.id.titleView);
		titleView.setText(media.getTitle());

		contentView = (TextView) this.findViewById(R.id.contentView);
		contentView.setText(media.getContent());

	}

	@Override
	public void onClick(View v) {
		 Intent intent = new Intent(this.context, NewsDetailActivity.class);
		 this.context.startActivityForResult(intent, 100);
		 
	}
}
