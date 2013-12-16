package com.hmd.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.hmd.R;
import com.hmd.activity.BaseActivity;
import com.hmd.activity.PhotoWallActivity;
import com.hmd.activity.SchoolExActivity;
import com.hmd.client.ApplicationEnvironment;
import com.hmd.util.BitmapUtil;

public class SchoolPhotoWallRelativeLayout extends RelativeLayout {

	private Button applyButton;
	private BaseActivity context;

	private Gallery mGallery = null;

	private ArrayList<BitmapDrawable> mBitmaps = new ArrayList<BitmapDrawable>();

	public SchoolPhotoWallRelativeLayout(Context context) {
		super(context);
		this.context = (BaseActivity) context;

		LayoutInflater.from(context).inflate(R.layout.layout_school_photowall, this, true);

		applyButton = (Button) this.findViewById(R.id.btn_schoolcard_apply);
		applyButton.setOnClickListener(new ClickListener());

		// 印象首师图片
		generateBitmaps();

		mGallery = (Gallery) this.findViewById(R.id.gallery);
		mGallery.setBackgroundColor(Color.GRAY);
		mGallery.setSpacing(10);
		mGallery.setFadingEdgeLength(0);
		mGallery.setGravity(Gravity.CENTER_VERTICAL);
		mGallery.setAdapter(new GalleryAdapter());
		mGallery.setSelection(Integer.MAX_VALUE / 2);
		mGallery.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(SchoolPhotoWallRelativeLayout.this.context, PhotoWallActivity.class);
				SchoolPhotoWallRelativeLayout.this.context.startActivityForResult(intent, 100);
			}
		});
	}

	private void generateBitmaps() {
		int[] ids = { R.drawable.img_splash_1, R.drawable.img_splash_2, R.drawable.img_splash_3, R.drawable.img_splash_4, R.drawable.img_splash_5 };

		/**
		 * for (int id : ids) { Bitmap bitmap = createReflectedBitmapById(id);
		 * if (null != bitmap) { BitmapDrawable drawable = new
		 * BitmapDrawable(bitmap); drawable.setAntiAlias(true);
		 * mBitmaps.add(drawable); } }
		 **/

		// 无倒影
		for (int id : ids) {
			mBitmaps.add((BitmapDrawable) getResources().getDrawable(id));
		}

	}

	private Bitmap createReflectedBitmapById(int resId) {
		Drawable drawable = getResources().getDrawable(resId);
		if (drawable instanceof BitmapDrawable) {
			Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
			Bitmap reflectedBitmap = BitmapUtil.createReflectedBitmap(bitmap);

			return reflectedBitmap;
		}

		return null;
	}

	private class GalleryAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (null == convertView) {
				convertView = new MyImageView(SchoolPhotoWallRelativeLayout.this.context);
				int width = ApplicationEnvironment.getInstance().getPixels().widthPixels;
				convertView.setLayoutParams(new Gallery.LayoutParams(width / 3, width / 2));
			}

			ImageView imageView = (ImageView) convertView;
			imageView.setImageDrawable(mBitmaps.get(position % mBitmaps.size()));
			imageView.setScaleType(ScaleType.FIT_XY);

			return imageView;
		}
	}

	private class MyImageView extends ImageView {
		public MyImageView(Context context) {
			this(context, null);
		}

		public MyImageView(Context context, AttributeSet attrs) {
			super(context, attrs, 0);
		}

		public MyImageView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);
		}
	}

	class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.btn_schoolcard_apply) {
				Intent intent = new Intent(SchoolPhotoWallRelativeLayout.this.context, PhotoWallActivity.class);
				SchoolPhotoWallRelativeLayout.this.context.startActivityForResult(intent, 100);
			}

		}

	}

}
