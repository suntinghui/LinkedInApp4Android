package com.hmd.view;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.hmd.R;
import com.hmd.activity.SchoolMediaDetailActivity;
import com.hmd.model.MediaModel;
import com.hmd.util.ImageUtil;

public class SlideImageLayout {
	private Context mContext = null;
	// 圆点图片集合
	private ImageView[] mImageViews = null;
	private ImageView mImageView = null;
	
	private MediaModel model;
	private ArrayList<MediaModel> modelList;

	public SlideImageLayout(Context context) {
		this.mContext = context;
	}

	public View getSlideImageLayout(MediaModel model, ArrayList<MediaModel> modelList, int i) {
		this.model = model;
		this.modelList = modelList;
		// 包含TextView的LinearLayout
		LinearLayout imageLinerLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams imageLinerLayoutParames = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);

		ImageView iv = new ImageView(mContext);
		iv.setBackgroundResource(R.drawable.img_weibo_item_pic_loading);
		iv.setTag(1000+i);
		iv.setOnClickListener(new ImageOnClickListener());
		
		imageLinerLayout.addView(iv, imageLinerLayoutParames);
		ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, model.getPics().get(0), iv);

		return imageLinerLayout;
	}

	/**
	 * 获取LinearLayout
	 * 
	 * @param view
	 * @param width
	 * @param height
	 * @return
	 */
	public View getLinearLayout(View view, int width, int height) {
		LinearLayout linerLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams linerLayoutParames = new LinearLayout.LayoutParams(width, height, 1);
		// 这里最好也自定义设置，有兴趣的自己设置。
		linerLayout.setPadding(10, 0, 10, 0);
		linerLayout.addView(view, linerLayoutParames);

		return linerLayout;
	}

	/**
	 * 设置圆点个数
	 * 
	 * @param size
	 */
	public void setCircleImageLayout(int size) {
		mImageViews = new ImageView[size];
	}

	/**
	 * 生成圆点图片区域布局对象
	 * 
	 * @param index
	 * @return
	 */
	public ImageView getCircleImageLayout(int index) {
		mImageView = new ImageView(mContext);
		mImageView.setLayoutParams(new LayoutParams(10, 10));
		mImageView.setScaleType(ScaleType.FIT_XY);

		mImageViews[index] = mImageView;

		if (index == 0) {
			// 默认选中第一张图片
			mImageViews[index].setBackgroundResource(R.drawable.dot_selected);
		} else {
			mImageViews[index].setBackgroundResource(R.drawable.dot_none);
		}

		return mImageViews[index];
	}

	// 滑动页面点击事件监听器
	private class ImageOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			int index = (Integer) v.getTag() - 1000;
			Intent intent = new Intent(mContext, SchoolMediaDetailActivity.class);
			intent.putExtra("MODEL", modelList.get(index));
			mContext.startActivity(intent);
		}
	}
}
