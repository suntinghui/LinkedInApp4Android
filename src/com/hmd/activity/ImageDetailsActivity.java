package com.hmd.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.hmd.R;
import com.hmd.client.DownloadFileRequest;
import com.hmd.view.ZoomImageView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image_details);
		zoomImageView = (ZoomImageView) findViewById(R.id.zoom_image_view);
		// 取出图片路径，并解析成Bitmap对象，然后在ZoomImageView中显示
		zoomImageView.content = this;
		String imagePath = getIntent().getStringExtra("image_path");
		bitmap = BitmapFactory.decodeFile(imagePath);
		zoomImageView.setImageBitmap(bitmap);

		Button btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);

		Button btn_download = (Button) this.findViewById(R.id.btn_download);
		btn_download.setOnClickListener(this);

		progress = (ProgressBar) this.findViewById(R.id.progress);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 记得将Bitmap对象回收掉
		if (bitmap != null) {
			bitmap.recycle();
		}
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_back:
			this.finish();
			break;
		case R.id.btn_download:
			dialog = ProgressDialog.show(this, "", "下载数据，请稍等 …", true, true);
			// 启动一个后台线程
			handler.post(new Runnable() {
				@Override
				public void run() {
					// 这里下载数据
					try {
						URL url = new URL(params);
						HttpURLConnection conn = (HttpURLConnection) url.openConnection();
						conn.setDoInput(true);
						conn.connect();
						InputStream inputStream = conn.getInputStream();
						bitmap = BitmapFactory.decodeStream(inputStream);
						Message msg = new Message();
						msg.what = 1;
						handler.sendMessage(msg);

					} catch (MalformedURLException e1) {
						e1.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
			break;

		default:
			break;
		}

	}

	/** 这里重写handleMessage方法，接受到子线程数据后更新UI **/
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				// 关闭
				try {
					ImageDetailsActivity.this.saveMyBitmap("bitmap");
				} catch (IOException e) {
					e.printStackTrace();
				}
				dialog.dismiss();
				break;
			}
		}
	};

	// 附件及所有文件都存放在本目录下
	public static String getDownloadPath() {
		// 其它程序无法访问
		// String path =
		// ApplicationEnvironment.getInstance().getApplication().getFilesDir().getPath()+"/download/";
		String path = Environment.getExternalStorageDirectory() + "/LinkedApp/Images/";
		File file = new File(path);
		if (!file.exists()) {
			// file.mkdir();
			// creating missing parent directories if necessary
			file.mkdirs();
		}

		return path;
	}

	public void saveMyBitmap(String bitName) throws IOException {

		 try {

			 ContentResolver cr = this.getContentResolver();

			 String    url  = MediaStore.Images.Media.insertImage(cr, bitName, "filnale", "");

			 Toast.makeText(this, "保存成功!", Toast.LENGTH_SHORT).show();

			 }catch(Exception e){

			 e.printStackTrace();

			 }
		 
//		
//		File f = new File(Environment.getExternalStorageDirectory()+ "/LinkedApp/Images/" + bitName + ".png");
//		if (!f.exists()) {
//			// file.mkdir();
//			// creating missing parent directories if necessary
//			f.mkdirs();
//		}
////		f.createNewFile();
//		FileOutputStream fOut = null;
//		try {
//			fOut = new FileOutputStream(f);
//		} catch (FileNotFoundException e) {
//
//			e.printStackTrace();
//		}
//
//		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
//
//		try {
//			fOut.flush();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}
//
//		try {
//			fOut.close();
//
//		} catch (IOException e) {
//
//			e.printStackTrace();
//		}

	}

}
