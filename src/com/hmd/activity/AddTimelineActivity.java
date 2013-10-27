package com.hmd.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.enums.RegistrationCode;
import com.hmd.model.ProfileModel;
import com.hmd.model.TimelineModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.DateUtil;
import com.hmd.util.ImageUtil;
import com.hmd.util.PatternUtil;
import com.hmd.view.LKAlertDialog;

public class AddTimelineActivity extends BaseActivity {

	private String afterId = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0;   //这里的IMAGE_CODE是自己任意定义的
	private Bitmap bm = null;
	
	private Button btn_back = null;
	private Button btn_confirm = null;
	
	//基本信息
	private ImageView image_head = null;
	private Button btn_pick = null;
	private EditText et_title = null;
	private EditText et_desc = null;
	private EditText et_org = null;
	private EditText et_province = null;
	private EditText et_city = null;
	private TextView tv_stime = null;
	private TextView tv_etime = null;
	
	private TimelineModel data = null;
	private Boolean isModify = false;
	
	private Boolean btn_pressed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_addtimeline);

		this.init();
	}

	private void init() {
		Intent intent = this.getIntent();
		data = (TimelineModel) intent.getSerializableExtra("DATA");
		isModify = intent.getBooleanExtra("ISMODIFY", false);
		afterId = (String) intent.getStringExtra("AFTERID");

		btn_back = (Button)this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(listener);
		btn_confirm = (Button)this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(listener);
		
		image_head = (ImageView)this.findViewById(R.id.image_head);
		btn_pick = (Button)this.findViewById(R.id.btn_pick);
		btn_pick.setOnClickListener(listener);
		et_title = (EditText)this.findViewById(R.id.et_title);
		et_desc = (EditText)this.findViewById(R.id.et_desc);
		et_org = (EditText)this.findViewById(R.id.et_org);
		et_province = (EditText)this.findViewById(R.id.et_province);
		et_city = (EditText)this.findViewById(R.id.et_city);
		tv_stime = (TextView)this.findViewById(R.id.tv_stime);
		tv_stime.setText(DateUtil.getCurrentYearMonthDay());
		tv_stime.clearFocus();
		tv_stime.setOnClickListener(listener);
		tv_etime = (TextView)this.findViewById(R.id.tv_etime);
		tv_etime.setText(DateUtil.getCurrentYearMonthDay());
		tv_etime.setOnClickListener(listener);
		
		if(isModify){
			if(data.getImgUrl() != null){
				ImageUtil.loadImage(R.drawable.img_card_head_portrait, data.getImgUrl(), image_head);	
			}
			et_title.setText(data.getTitle());
			et_desc.setText(data.getDescription());
			et_org.setText(data.getOrg());
			et_province.setText(data.getProvince());
			et_city.setText(data.getCity());
			tv_stime.setText(data.getStartTime()+"-09");
			tv_etime.setText(data.getEndTime()+"-06");
		}
		
	}

	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_pick:
				 Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
				 getAlbum.setType(IMAGE_TYPE);
				 startActivityForResult(getAlbum, IMAGE_CODE);
				break;
			case R.id.tv_stime:
				//用于显示日期对话框,他会调用onCreateDialog()
				btn_pressed = true;
				showDialog(1);
				break;
			case R.id.tv_etime:
				//用于显示日期对话框,他会调用onCreateDialog()
				btn_pressed = true;
				showDialog(2);
				break;
			case R.id.btn_back:
				AddTimelineActivity.this.finish();
				break;
			case R.id.btn_confirm:
				if(checkValue()){
					if(isModify){
						doTimeLineModify();
					}else{
						doTimeLineAdd();	
					}
					
				};
				
				break;
			default:
				break;
			}
			
		}
	};
	
	private void refreshPersonInfoAll(){
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getProfileRequest());
		queue.executeQueue("正在刷新数据...", new LKHttpRequestQueueDone());
	}
	// 查看个人基本信息
	private LKHttpRequest getProfileRequest(){
		LKHttpRequest request = new LKHttpRequest( HttpRequestType.HTTP_PROFILE_ALL, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
			}
		}, "me");
		
		return request;
	}
	
	//修改时间轴
	private void doTimeLineModify(){
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("title", et_title.getText().toString());
		paramMap.put("desc", et_desc.getText().toString());
		paramMap.put("org", et_org.getText().toString());
		paramMap.put("province", et_province.getText().toString());
		paramMap.put("city", et_city.getText().toString());
		paramMap.put("stime", tv_stime.getText().toString().substring(0, 7));
		paramMap.put("etime", tv_etime.getText().toString().substring(0, 7));
		paramMap.put("id", data.getid());
		if(bm != null){
			paramMap.put("pic",this.bitmaptoString(bm));				
		}
		
		LKHttpRequest req1 = new LKHttpRequest( HttpRequestType.HTTP_TIMELINE_NODE_UPDATE, paramMap, getModifyHandler());
		
		new LKHttpRequestQueue().addHttpRequest(req1)
		.executeQueue("正在提交数据，请稍候...", new LKHttpRequestQueueDone(){

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});	
	}
	 //	添加时间轴结点
	 private void doTimeLineAdd(){
			
			HashMap<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("title", et_title.getText().toString());
			paramMap.put("desc", et_desc.getText().toString());
			paramMap.put("org", et_org.getText().toString());
			paramMap.put("province", et_province.getText().toString());
			paramMap.put("city", et_city.getText().toString());
			paramMap.put("stime", tv_stime.getText().toString().substring(0, 7));
			paramMap.put("etime", tv_etime.getText().toString().substring(0, 7));
			paramMap.put("afterID", afterId);
			if(bm != null){
				paramMap.put("pic",this.bitmaptoString(bm));				
			}
			
			LKHttpRequest req1 = new LKHttpRequest( HttpRequestType.HTTP_TIMELINE_NODE_CREATE, paramMap, getAddHandler());
			
			new LKHttpRequestQueue().addHttpRequest(req1)
			.executeQueue("正在提交数据，请稍候...", new LKHttpRequestQueueDone(){

				@Override
				public void onComplete() {
					super.onComplete();
				}
			});	
			
		}
		
		private LKAsyncHttpResponseHandler getAddHandler(){
			 return new LKAsyncHttpResponseHandler(){
				@Override
				public void successAction(Object obj) {
					if((Integer)obj == 1){
						new AlertDialog.Builder(AddTimelineActivity.this)    
						                .setTitle("标题")  
						                .setMessage("新增履历成功！")  
						                .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
			                                   public void onClick(DialogInterface dialog, int whichButton) {  
			                                	   Intent it = new Intent();  
			                                       setResult(5, it);  
			                                       finish();  
			                                	   
			  
			                                   }  
			                       })  
						                .show(); 
					}
					
				}
				 
			 };
		}
		
		private LKAsyncHttpResponseHandler getModifyHandler(){
			 return new LKAsyncHttpResponseHandler(){
				@Override
				public void successAction(Object obj) {
					if((Integer)obj == 1){
						new AlertDialog.Builder(AddTimelineActivity.this)    
						                .setTitle("标题")  
						                .setMessage("修改履历成功！")  
						                .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
			                                   public void onClick(DialogInterface dialog, int whichButton) {  
			                                	   Intent it = new Intent();  
			                                       setResult(5, it);  
			                                       finish();  
			                                	   
			  
			                                   }  
			                       })  
						                .show(); 
					}
					
				}
				 
			 };
		}
		
		@Override
		 protected Dialog onCreateDialog(int id) {
			if(!btn_pressed){
				return null;
			}
			switch (id) {
			  case 1:
				  btn_pressed = false;
				  String tmpStr1 = tv_stime.getText().toString();
				  int year = Integer.valueOf(tmpStr1.substring(0, 4));
				  int month = Integer.valueOf(tmpStr1.substring(5, 7));
				  int day = Integer.valueOf(tmpStr1.substring(8, 10));
				  DatePickerDialog dialog1 = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
					  
						@Override
						public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
							tv_stime.setText(arg1+"-"+(arg2+1)+"-"+arg3);
							
							}
						 },year,month-1,day);
				  
				  return dialog1;
				  
			  case 2:
				  btn_pressed = false;
				  String tmpStr2 = tv_etime.getText().toString();
				  int year2 = Integer.valueOf(tmpStr2.substring(0, 4));
				  int month2 = Integer.valueOf(tmpStr2.substring(5, 7));
				  int day2 = Integer.valueOf(tmpStr2.substring(8, 10));
				  DatePickerDialog dialog2 = new DatePickerDialog(this,new DatePickerDialog.OnDateSetListener() {
					  
						@Override
						public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
							tv_etime.setText(arg1+"-"+(arg2+1)+"-"+arg3);
							
							}
						 }, year2,month2-1,day2);
				  return dialog2;
			  }
			  return null;
			 }
		 
		 
	private boolean checkValue(){
		if(et_title.getText().toString().trim().equals("")){
			this.showToast("身份不能为空！");
			return false;
		}else if(et_desc.getText().toString().trim().equals("")){
			this.showToast("描述不能为空！");
			return false;
		}
		else if(et_city.getText().toString().trim().equals("")){
			this.showToast("城市不能为空！");
			return false;
		}else if(et_org.getText().toString().trim().equals("")){
			this.showToast("组织不能为空！");
			return false;
		}else if(et_province.getText().toString().trim().equals("")){
			this.showToast("省份不能为空！");
			return false;
		}
		
		return true;
	}
	
	 @Override

	 protected void onActivityResult(int requestCode, int resultCode, Intent data){

	     if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量


	         return;

	     }

	     bm = null;
	     //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
	     ContentResolver resolver = getContentResolver();
	     //此处的用于判断接收的Activity是不是你想要的那个
	     if (requestCode == IMAGE_CODE) {
	         try {
	             Uri originalUri = data.getData();        //获得图片的uri 
	             bm = MediaStore.Images.Media.getBitmap(resolver, originalUri);        //显得到bitmap图片这里开始的第二部分，获取图片的路径：
	             String[] proj = {MediaStore.Images.Media.DATA};
	             //好像是android多媒体数据库的封装接口，具体的看Android文档
	             Cursor cursor = managedQuery(originalUri, proj, null, null, null); 
	             //按我个人理解 这个是获得用户选择的图片的索引值
	             if(bm != null){
	            	 image_head.setImageBitmap(bm);
	             }
	             
//	             int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//	             //将光标移至开头 ，这个很重要，不小心很容易引起越界
//	             cursor.moveToFirst();
//	             //最后根据索引值获取图片路径
//	             String path = cursor.getString(column_index);
	         }catch (IOException e) {


	         }

	     }

	 }
	 public Bitmap stringtoBitmap(String string){
		//将字符串转换成Bitmap类型
		 Bitmap bitmap=null;
		    try {
		    byte[]bitmapArray;
		    bitmapArray=Base64.decode(string, Base64.DEFAULT);
		    bitmap=BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
			} catch (Exception e) {
				e.printStackTrace();
			}
		   
		    return bitmap;
	}
		    
		     
	 public String bitmaptoString(Bitmap bitmap){

		//将Bitmap转换成字符串
		    String string=null;
		    ByteArrayOutputStream bStream=new ByteArrayOutputStream();
		    bitmap.compress(CompressFormat.PNG,100,bStream);
		    byte[]bytes=bStream.toByteArray();
		    string=Base64.encodeToString(bytes,Base64.DEFAULT);
		    return string;
		    
	}
}
