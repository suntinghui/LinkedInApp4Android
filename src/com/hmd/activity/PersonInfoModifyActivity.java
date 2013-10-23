package com.hmd.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.DateUtil;
import com.hmd.util.PatternUtil;
import com.hmd.view.LKAlertDialog;

public class PersonInfoModifyActivity extends BaseActivity {

	private ProfileModel model = null;
	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0;   //这里的IMAGE_CODE是自己任意定义的
	//基本信息
	private ImageView image_head = null;
	private Button btn_pick = null;
	private EditText et_name = null;
	private RadioGroup radioGroup = null;
	private RadioButton radioMale = null;
	private RadioButton radioFemale = null;
	private EditText et_major = null;
	private Spinner adYearSpinner = null;
	private Spinner gradYearSpinner = null;
	
	//扩展信息
	private TextView tv_birthday = null;
	private EditText et_birthplace = null;
	private EditText et_nation = null;
	private EditText et_desc = null;
	
	private ArrayList<String> adYearKeyList = new ArrayList<String>();
	private ArrayList<String> adYearValueList = new ArrayList<String>();
	
	private ArrayList<String> gradYearKeyList = new ArrayList<String>();
	private ArrayList<String> gradYearValueList = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_personinfomodify);

		this.init();
		refreshPersonInfoAll();
	}

	private void init() {
		
		image_head = (ImageView)this.findViewById(R.id.image_head);
		btn_pick = (Button)this.findViewById(R.id.btn_pick);
		btn_pick.setOnClickListener(listener);
		et_name = (EditText)this.findViewById(R.id.et_name);
		
		et_major = (EditText)this.findViewById(R.id.et_major);
		
		tv_birthday = (TextView)this.findViewById(R.id.tv_birthday);
		tv_birthday.setOnClickListener(listener);
		et_birthplace = (EditText)this.findViewById(R.id.et_birthplace); 
		
		et_nation = (EditText)this.findViewById(R.id.et_nation);
		et_desc = (EditText)this.findViewById(R.id.et_desc);
		
		radioGroup = (RadioGroup)this.findViewById(R.id.radioGroup);
		radioMale = (RadioButton)this.findViewById(R.id.radioMale);
		radioFemale = (RadioButton)this.findViewById(R.id.radioFemale);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				 //获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				//根据ID获取RadioButton的实例
			 	RadioButton rb = (RadioButton)PersonInfoModifyActivity.this.findViewById(radioButtonId);
				String text = rb.getText().toString();
				if(text.equals("男")){
					model.setGender(1);
				}else{
					model.setGender(0);
				}
			}
		});
		
		adYearSpinner = (Spinner) this.findViewById(R.id.adYearSpinner);
		gradYearSpinner = (Spinner) this.findViewById(R.id.gradYearSpinner);
		
		this.preparData();
		
		ArrayAdapter<String> adYearAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,  adYearValueList);
		adYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adYearSpinner.setAdapter(adYearAdapter);
		adYearSpinner.setPrompt("入学年份");
		adYearSpinner.setSelection(adYearValueList.size() - 1);
		
		ArrayAdapter<String> gradYearAdapter = new ArrayAdapter<String>(this, R.layout.myspinner,  gradYearValueList);
		gradYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		gradYearSpinner.setAdapter(gradYearAdapter);
		gradYearSpinner.setPrompt("毕业年份");
		gradYearSpinner.setSelection(gradYearValueList.size() - 1);
	}

	private void preparData(){
		
		for (int i=1950; i<Integer.parseInt(DateUtil.getCurrentYear())+1; i++){
			adYearKeyList.add(i+"");
			adYearValueList.add(i+" 年");
			
			gradYearKeyList.add(i+"");
			gradYearValueList.add(i+" 年");
		}
		
		gradYearKeyList.add(gradYearKeyList.size(), "null");
		gradYearValueList.add(gradYearValueList.size(), "尚未毕业");
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
			case R.id.tv_birthday:
				//用于显示日期对话框,他会调用onCreateDialog()
				showDialog(1);
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
				model = (ProfileModel) obj;
				et_name.setText(model.getName());
				et_major.setText(model.getMajor());
				et_birthplace.setText(model.getBirthplace());
				et_desc.setText(model.getDesc());
				tv_birthday.setText(model.getBirthday());
				radioMale.setChecked(model.getGender()==1 ? true:false);
				radioFemale.setChecked(model.getGender() == 0 ? true:false);
				int currentYear = Integer.parseInt(DateUtil.getCurrentYear())+1;
				int adYear = adYearValueList.size()-(currentYear-Integer.valueOf(model.getAdYear()));
				int gradYear = 0;
				if(model.getGradYear().equals("未知")){
					gradYear = currentYear - 1949;
				}else{
					gradYear = gradYearValueList.size()-(currentYear-Integer.valueOf(model.getGradYear())) - 1;
				}
				adYearSpinner.setSelection(adYear);
				gradYearSpinner.setSelection(gradYear);
			}
		}, "me");
		
		return request;
	}
	
	@Override
	 protected Dialog onCreateDialog(int id) {
	  // TODO Auto-generated method stub
	  switch (id) {
	  case 1:
		  String tmpStr = tv_birthday.getText().toString();
		  int year = Integer.valueOf(tmpStr.substring(0, 4));
		  int month = Integer.valueOf(tmpStr.substring(5, 7));
		  int day = Integer.valueOf(tmpStr.substring(8, 10));
		  
	   return new DatePickerDialog(this,onDateSetListener,year,month-1,day);
	  }
	  return null;
	 }
	 
	 //设置时间之后点击SET就会将时间改为你刚刚设置的时间
	 DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
	  
	@Override
	public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
		tv_birthday.setText(arg1+"-"+(arg2+1)+"-"+arg3);
		
	}
	 };    
	 
	 private void doRegistration(){
			
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("name", et_name.getText().toString());
			paramMap.put("gender", model.getGender()+"");
			paramMap.put("major", et_major.getText().toString());
			paramMap.put("adYear", adYearKeyList.get(adYearSpinner.getSelectedItemPosition()));
			paramMap.put("gradYear", gradYearKeyList.get(gradYearSpinner.getSelectedItemPosition()));
			
			LKHttpRequest req1 = new LKHttpRequest( HttpRequestType.HTTP_PROFILE_UPDATE, paramMap, getRegisterHandler());
			
			new LKHttpRequestQueue().addHttpRequest(req1)
			.executeQueue("正在提交数据，请稍候...", new LKHttpRequestQueueDone(){

				@Override
				public void onComplete() {
					super.onComplete();
				}
			});	
			
		}
		
		private LKAsyncHttpResponseHandler getRegisterHandler(){
			 return new LKAsyncHttpResponseHandler(){
				@Override
				public void successAction(Object obj) {
					@SuppressWarnings("unchecked")
					HashMap<String, String> respMap = (HashMap<String, String>) obj;
					int returnCode = Integer.parseInt(respMap.get("rc"));
					
				}
				 
			 };
		}
	private boolean checkValue(){
		if(et_name.getText().toString().trim().equals("")){
			this.showToast("姓名不能为空！");
			return false;
		}else if(et_major.getText().toString().trim().equals("")){
			this.showToast("专业不能为空！");
			return false;
		}
		
		return true;
	}
	
	 @Override

	 protected void onActivityResult(int requestCode, int resultCode, Intent data){

	     if (resultCode != RESULT_OK) {        //此处的 RESULT_OK 是系统自定义得一个常量


	         return;

	     }

	     Bitmap bm = null;
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
