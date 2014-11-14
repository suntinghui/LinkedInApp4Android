package com.hmd.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hmd.R;
import com.hmd.view.ZoomImageView;

/**
 * 查看大图的Activity界面。
 * 
 * @author guolin
 */
public class ImageDetailsActivity extends Activity implements OnClickListener {
	/**
	 * 自定义的ImageView控制，可对图片进行多点触控缩放和拖动
	 */
	private ZoomImageView zoomImageView;

	/**
	 * 待展示的图片
	 */
	private static final String params = "http://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Hukou_Waterfall.jpg/800px-Hukou_Waterfall.jpg";
	private ProgressBar progress;
	private ProgressDialog dialog = null;
	private Bitmap bitmap = null;
	private ImageLoader imageLoader;
	private String imagePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_details);
		zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
		// 取出图片路径，并解析成Bitmap对象，然后在ZoomImageView中显示
		zoomImageView.content = this;
		imagePath = getIntent().getStringExtra("pic");

		TextView titleView = (TextView) findViewById(R.id.titleView);

		String titleTag = getIntent().getStringExtra("titleTag");
		if (titleTag != null) {
			titleView.setText(titleTag);
		}

		bitmap = getHttpBitmap(imagePath);
		zoomImageView.setImageBitmap(bitmap);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		Button btn_download = (Button) this.findViewById(R.id.btn_download);
		btn_download.setOnClickListener(this);

		progress = (ProgressBar) this.findViewById(R.id.progress);

	}

	/**
	 * 获取网落图片资源
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getHttpBitmap(String url) {
		URL myFileURL;
		Bitmap bitmap = null;
		try {
			myFileURL = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
			// 设置超时时间为6000毫秒，conn.setConnectionTiem(0);表示没有时间限制
			conn.setConnectTimeout(6000);
			// 连接设置获得数据流
			conn.setDoInput(true);
			// 不使用缓存
			conn.setUseCaches(false);
			// 这句可有可无，没有影响
			// conn.connect();
			// 得到数据流
			InputStream is = conn.getInputStream();
			// 解析得到图片
			bitmap = BitmapFactory.decodeStream(is);
			// 关闭数据流
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 记得将Bitmap对象回收掉
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

	public void backAction() {
		this.finish();
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_download:
			MediaStore.Images.Media.insertImage(ImageDetailsActivity.this.getContentResolver(), bitmap,
					 "", "");
//					saveImageToGallery(ImageDetailsActivity.this, bitmap);
					 sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
					 Uri.parse("file://"+
					 Environment.getExternalStorageDirectory())));

					Toast.makeText(ImageDetailsActivity.this, "图片已保存到相册", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}

	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// TODO Auto-generated method stub
//		MenuInflater inflater = getMenuInflater();
//		inflater.inflate(R.menu.menu0, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		// TODO Auto-generated method stub
//		int item_id = item.getItemId();
//
//		switch (item_id) {
//		case R.id.play:
//			 MediaStore.Images.Media.insertImage(ImageDetailsActivity.this.getContentResolver(), bitmap,
//			 "", "");
////			saveImageToGallery(ImageDetailsActivity.this, bitmap);
//			 sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
//			 Uri.parse("file://"+
//			 Environment.getExternalStorageDirectory())));
//
//			Toast.makeText(ImageDetailsActivity.this, "图片已保存到相册", Toast.LENGTH_SHORT).show();
//			break;
//		default:
//			return false;
//		}
//		return true;
//
//	}

	public void saveImageToGallery(Context context, Bitmap bmp) {
		// 首先保存图片
		File appDir = new File(Environment.getExternalStorageDirectory(), "首师图片保存");
		if (!appDir.exists()) {
			appDir.mkdir();
		}
		String fileName = System.currentTimeMillis() + ".jpg";
		File file = new File(appDir, fileName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bmp.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 其次把文件插入到系统图库
		try {
			MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		// 最后通知图库更新
		context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + Environment.getExternalStorageDirectory()))); // ACTION_MEDIA_MOUNTED
	}

	public boolean saveImgToGallery(Context context, String fileName) {
		boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (!sdCardExist)
			return false;

		try {
			// String url = MediaStore.Images.Media.insertImage(cr, bmp,
			// fileName,
			// "");
			// app.sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri
			// .parse("file://"
			// + Environment.getExternalStorageDirectory())));

			// debug
			ContentValues values = new ContentValues();
			values.put("datetaken", new Date().toString());
			values.put("mime_type", "image/png");
			values.put("_data", fileName);
			ContentResolver cr = context.getContentResolver();
			cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

}
