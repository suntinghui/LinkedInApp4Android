package com.hmd.util;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.widget.ImageView;

public class ImageUtil {
	
	private static String imageURL[] = new String[]{"http://www.news.cn/ziliao/leaders/xijinping_201302040101.jpg",
		"http://www.news.cn/ziliao/leaders/likeqiang_201302060114.jpg",
		"http://www.news.cn/ziliao/leaders/zhanggaoli_201302060350.jpg",
		"http://www.news.cn/ziliao/leaders/wangqishan_201302071038.jpg",
		"http://www.news.cn/ziliao/leaders/liuyunshan_201302071028.jpg",
		"http://www.news.cn/ziliao/leaders/zhangdejiang_201302060418.jpg",
		"http://news.xinhuanet.com/ziliao/2012-11/05/123913396_41n.jpg",
		"http://t0.gstatic.com/images?q=tbn:ANd9GcSAQCknJ8zdzkCmKFglp_stiG9L9qXBK2h5gembMbeIDsMK8MHBhQ",
		"http://t2.gstatic.com/images?q=tbn:ANd9GcSRXWN-PDegy1odMgt0ZvRi4p_1xhYNKVbTvV3g18XRplXAzt3m",
		"http://t3.gstatic.com/images?q=tbn:ANd9GcQcOxMY1I4u4QVizg3V1WlygnY2kA_ZVTgTu_4mbTbvNDQFe6EfOw",
		"http://t3.gstatic.com/images?q=tbn:ANd9GcQp_3pM2_hRkWXqRSvIVqJ1YdK26DVlRjnMARlpW8X3GAj_LUruaQ"};
	
	// 取测试数据
	public static String getTestImageURL(){
		int random = (int)(Math.random() * imageURL.length);
		return imageURL[random];
	}
	
	// http://my.oschina.net/u/143926/blog/137637
	public static void loadImage(int resid, String url, ImageView imageView){
		DisplayImageOptions options = new DisplayImageOptions.Builder()
		.showImageOnLoading(resid)
		.showImageForEmptyUri(resid)
		.showImageOnFail(resid)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.ARGB_8888)
		.build();
		
		ImageLoader.getInstance().displayImage(url, imageView, options);
	}
	
	/**
	 * 对指定的图片进行圆角处理
	 * 
	 * @param bitmap
	 *            要圆角处理的图片的bitmap信息
	 * @return 圆角之后的图片的bitmap信息
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		try {
			Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
			Canvas canvas = new Canvas(output);
			final Paint paint = new Paint();
			final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
			final float roundPx = 4;
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(Color.BLACK);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
			canvas.drawBitmap(bitmap, src, rect, paint);
			return output;
		} catch (Exception e) {
			return bitmap;
		}
	}

	/**
	 * 对指定的图片进行旋转处理
	 * 
	 * @param bitmap
	 *            要旋转的图片的bitmap信息
	 * @return 旋转之后的图片的bitmap信息
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix mtx = new Matrix();
		mtx.postRotate(90);// Setting post rotate to 90
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, mtx, true);
	}

}
