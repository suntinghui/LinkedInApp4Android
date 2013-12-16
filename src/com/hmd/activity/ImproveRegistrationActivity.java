package com.hmd.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

import com.hmd.R;
import com.hmd.client.HttpRequestType;
import com.hmd.model.ProfileModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.DateUtil;
import com.hmd.util.ImageUtil;

public class ImproveRegistrationActivity extends BaseActivity implements OnClickListener {

	private final String IMAGE_TYPE = "image/*";
	private final int IMAGE_CODE = 0; // 这里的IMAGE_CODE是自己任意定义的
	private Bitmap bm = null;

	private Button btn_back = null;
	private Button btn_confirm = null;

	// 基本信息
	private ImageView image_head = null;
	private Button btn_pick = null;
	private EditText et_name = null;
	private RadioGroup radioGroup = null;
	private RadioButton radioMale = null;
	private RadioButton radioFemale = null;
	private Spinner idCardSpinner = null;

	// 院系
	private LinearLayout layout_stu = null;
	private Spinner deptSpinner = null;
	private Spinner majorSpinner = null;
	private EditText et_class = null;
	private Spinner adYearSpinner = null;

	// 可选信息
	private EditText et_mobile = null;
	private EditText et_email = null;
	private EditText et_qq = null;

	private int idCard_select = 0;
	private ArrayList<String> idCardList = new ArrayList<String>();
	private ArrayList<String> adYearKeyList = new ArrayList<String>();
	private ArrayList<String> adYearValueList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_improve_registration);

		this.init();
		refreshConfigDeptist();
	}

	private void init() {

		btn_back = (Button) this.findViewById(R.id.btn_back);
		btn_back.setOnClickListener(listener);
		btn_confirm = (Button) this.findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(listener);

		image_head = (ImageView) this.findViewById(R.id.image_head);
		btn_pick = (Button) this.findViewById(R.id.btn_pick);
		btn_pick.setOnClickListener(listener);
		et_name = (EditText) this.findViewById(R.id.et_name);

		radioGroup = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioMale = (RadioButton) this.findViewById(R.id.radioMale);
		radioFemale = (RadioButton) this.findViewById(R.id.radioFemale);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				// 获取变更后的选中项的ID
				int radioButtonId = arg0.getCheckedRadioButtonId();
				// 根据ID获取RadioButton的实例
				RadioButton rb = (RadioButton) ImproveRegistrationActivity.this.findViewById(radioButtonId);
				String text = rb.getText().toString();
				if (text.equals("男")) {
					// TODO
				} else {
					// TODO
				}
			}
		});

		idCardSpinner = (Spinner) this.findViewById(R.id.idCardTypeSpinner);
		adYearSpinner = (Spinner) this.findViewById(R.id.adYearSpinner);

		this.preparData();

		ArrayAdapter<String> idCardAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, idCardList);
		idCardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		idCardSpinner.setAdapter(idCardAdapter);
		idCardSpinner.setPrompt("身份类型");
		idCardSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				idCard_select = arg2;
				switch (arg2) {
				case 0:

					break;
				case 1:

					break;
				default:
					break;
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		ArrayAdapter<String> adYearAdapter = new ArrayAdapter<String>(this, R.layout.myspinner, adYearValueList);
		adYearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adYearSpinner.setAdapter(adYearAdapter);
		adYearSpinner.setPrompt("入学年份");
		adYearSpinner.setSelection(adYearValueList.size() - 1);

		// int currentYear = Integer.parseInt(DateUtil.getCurrentYear())+1;
		// int adYear =
		// adYearValueList.size()-(currentYear-Integer.valueOf(model.getAdYear()));
		// int gradYear = 1999;
		// idCardSpinner.setSelection(adYear);
	}

	private void preparData() {
		idCardList.add("学生校友");
		idCardList.add("教工校友");
		for (int i = 1950; i < Integer.parseInt(DateUtil.getCurrentYear()) + 1; i++) {
			adYearKeyList.add(i + "");
			adYearValueList.add(i + " 年");

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
			case R.id.btn_back:
				ImproveRegistrationActivity.this.finish();
				break;
			case R.id.btn_confirm:
				// doProfileUpdate();
				break;
			default:
				break;
			}

		}
	};

	private void refreshConfigDeptist() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getConfigDeptListRequest());
		queue.executeQueue("正在刷新数据...", new LKHttpRequestQueueDone());
	}

	// 获取院系专业双级列表
	private LKHttpRequest getConfigDeptListRequest() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_CONFIG_DEPT_LIST, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				Log.i("test", "te");
//				model = (ProfileModel) obj;
//				et_name.setText(model.getName());
//				et_major.setText(model.getMajor());
//				et_birthplace.setText(model.getBirthplace());
//				et_desc.setText(model.getDesc());
//				tv_birthday.setText(model.getBirthday());
//				radioMale.setChecked(model.getGender() == 1 ? true : false);
//				radioFemale.setChecked(model.getGender() == 0 ? true : false);
//				int currentYear = Integer.parseInt(DateUtil.getCurrentYear()) + 1;
//				int adYear = adYearValueList.size() - (currentYear - Integer.valueOf(model.getAdYear()));
//				int gradYear = 0;
//				if (model.getGradYear().equals("未知")) {
//					gradYear = currentYear - 1949;
//				} else {
//					gradYear = gradYearValueList.size() - (currentYear - Integer.valueOf(model.getGradYear())) - 1;
//				}
//				idCardSpinner.setSelection(adYear);
//				gradYearSpinner.setSelection(gradYear);
			}
		}, "me");

		return request;
	}

	// //更新个人信息
	// private void doProfileUpdate(){
	//
	// HashMap<String, Object> paramMap = new HashMap<String, Object>();
	// paramMap.put("name", et_name.getText().toString());
	// paramMap.put("gender", model.getGender()+"");
	// paramMap.put("major", et_major.getText().toString());
	// paramMap.put("adYear",
	// adYearKeyList.get(idCardSpinner.getSelectedItemPosition()));
	// paramMap.put("gradYear",
	// gradYearKeyList.get(gradYearSpinner.getSelectedItemPosition()));
	// paramMap.put("pic", bm == null ? "null":this.bitmaptoString(bm));
	//
	// LKHttpRequest req1 = new LKHttpRequest(
	// HttpRequestType.HTTP_PROFILE_UPDATE, paramMap,
	// getUpdateProfileHandler());
	//
	// new LKHttpRequestQueue().addHttpRequest(req1)
	// .executeQueue("正在提交数据，请稍候...", new LKHttpRequestQueueDone(){
	//
	// @Override
	// public void onComplete() {
	// super.onComplete();
	// }
	// });
	//
	// }
	//
	// private LKAsyncHttpResponseHandler getUpdateProfileHandler(){
	// return new LKAsyncHttpResponseHandler(){
	// @Override
	// public void successAction(Object obj) {
	// @SuppressWarnings("unchecked")
	// HashMap<String, String> respMap = (HashMap<String, String>) obj;
	// int returnCode = Integer.parseInt(respMap.get("rc"));
	// if(returnCode == 1){
	// // //修改成功
	// // new AlertDialog.Builder(ImproveRegistrationActivity.this)
	// // .setTitle("提示")
	// // .setMessage("信息更新成功！")
	// // .setPositiveButton("确定", new DialogInterface.OnClickListener() {
	// // public void onClick(DialogInterface dialog, int whichButton) {
	// // Intent it = new Intent();
	// // ImproveRegistrationActivity.this.gobackWithResult(5, it);
	// // }
	// // })
	// // .show();
	// }
	//
	// }
	//
	// };
	// }
	// // private boolean checkValue(){
	// // if(et_name.getText().toString().trim().equals("")){
	// // this.showToast("姓名不能为空！");
	// // return false;
	// // }else if(et_major.getText().toString().trim().equals("")){
	// // this.showToast("专业不能为空！");
	// // return false;
	// // }
	// //
	// // return true;
	// // }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (resultCode != RESULT_OK) { // 此处的 RESULT_OK 是系统自定义得一个常量

			return;

		}

		bm = null;
		// 外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
		ContentResolver resolver = getContentResolver();
		// 此处的用于判断接收的Activity是不是你想要的那个
		if (requestCode == IMAGE_CODE) {
			try {
				Uri originalUri = data.getData(); // 获得图片的uri
				bm = MediaStore.Images.Media.getBitmap(resolver, originalUri); // 显得到bitmap图片这里开始的第二部分，获取图片的路径：
				String[] proj = { MediaStore.Images.Media.DATA };
				// 好像是android多媒体数据库的封装接口，具体的看Android文档
				Cursor cursor = managedQuery(originalUri, proj, null, null, null);
				// 按我个人理解 这个是获得用户选择的图片的索引值
				if (bm != null) {
					image_head.setImageBitmap(bm);
				}

				// int column_index =
				// cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				// //将光标移至开头 ，这个很重要，不小心很容易引起越界
				// cursor.moveToFirst();
				// //最后根据索引值获取图片路径
				// String path = cursor.getString(column_index);
			} catch (IOException e) {

			}

		}

	}

	public Bitmap stringtoBitmap(String string) {
		// 将字符串转换成Bitmap类型
		Bitmap bitmap = null;
		try {
			byte[] bitmapArray;
			bitmapArray = Base64.decode(string, Base64.DEFAULT);
			bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public String bitmaptoString(Bitmap bitmap) {

		// 将Bitmap转换成字符串
		String string = null;
		ByteArrayOutputStream bStream = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.PNG, 100, bStream);
		byte[] bytes = bStream.toByteArray();
		string = Base64.encodeToString(bytes, Base64.DEFAULT);
		return string;

	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}
}
