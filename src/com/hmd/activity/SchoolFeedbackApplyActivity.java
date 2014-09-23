package com.hmd.activity;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.hmd.R;
import com.hmd.alipay.Keys;
import com.hmd.alipay.Result;
import com.hmd.alipay.Rsa;
import com.hmd.client.ApplicationEnvironment;
import com.hmd.client.Constants;
import com.hmd.client.HttpRequestType;
import com.hmd.model.ProfileModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.PatternUtil;
import com.hmd.view.EditTextWithClearView;
import com.hmd.view.LKAlertDialog;

public class SchoolFeedbackApplyActivity extends AbsSubActivity implements OnClickListener {

	private EditTextWithClearView nameText;
	private EditTextWithClearView mailText;
	private EditTextWithClearView phoneText;

	private Button backButton;
	private Button okButton;
	
	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school_feedback_apply);

		nameText = (EditTextWithClearView) this.findViewById(R.id.nameText);
		mailText = (EditTextWithClearView) this.findViewById(R.id.mailText);
		phoneText = (EditTextWithClearView) this.findViewById(R.id.phoneText);

		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

		okButton = (Button) this.findViewById(R.id.okButton);
		okButton.setOnClickListener(this);
		getProfile();
	}

	private boolean checkValue() {
		if ("".equals(nameText.getText().toString().trim())) {
			Toast.makeText(this, "请输入姓名", Toast.LENGTH_SHORT).show();
			return false;

		} else if ("".equals(mailText.getText().toString().trim())) {
			Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
			return false;

		} else if (!PatternUtil.isValidEmail(mailText.getText().toString().trim())) {
			Toast.makeText(this, "邮箱格式不正确，请重新输入", Toast.LENGTH_SHORT).show();
			return false;

		} else if ("".equals(phoneText.getText().toString().trim())) {
			Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
			return false;
		}

		return true;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			goback();
			break;

		case R.id.okButton:
//			if (checkValue()) {
//				this.doApply();
//			}
			pay();
			break;
		}
	}

	private void doApply() {
		if (!this.checkValue())
			return;

		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", nameText.getText());
		paramMap.put("email", mailText.getText());
		paramMap.put("mobile", phoneText.getText());

		LKHttpRequest req1 = new LKHttpRequest(HttpRequestType.HTTP_COLLEGE_FEEDBACK_APPLY, paramMap, getApplyHandler());

		new LKHttpRequestQueue().addHttpRequest(req1).executeQueue("正在提交请稍候...", new LKHttpRequestQueueDone() {

			@Override
			public void onComplete() {
				super.onComplete();
			}
		});
	}

	private LKAsyncHttpResponseHandler getApplyHandler() {
		return new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				int returnCode = (Integer) obj;
				String message = "";
				if (returnCode == 1) {
					message = "信息提交成功，管理员会尽快与您联系，请耐心等待。";
				} else if (returnCode == 0) {
					message = "信息提交失败。";
				} else if (returnCode == -2) {
					message = "您已提交过捐赠申请，请耐心等待。";
				}

				LKAlertDialog dialog = new LKAlertDialog(SchoolFeedbackApplyActivity.this);
				dialog.setTitle("提示");
				dialog.setMessage(message);
				dialog.setCancelable(false);
				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();

						goback();
					}
				});
				dialog.create().show();
			}

		};
	}

	private void getProfile() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		queue.addHttpRequest(getProfileRequest());
		queue.executeQueue("正在请求数据...", new LKHttpRequestQueueDone());
	}

	// 查看个人基本信息
	private LKHttpRequest getProfileRequest() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_PROFILE_DETAIL, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				ProfileModel detailModel = (ProfileModel) obj;
				nameText.setText(detailModel.getName().length() == 0 || detailModel.getName().equals("null") ? "" : detailModel.getName());
				mailText.setText(detailModel.getEmail().length() == 0 || detailModel.getEmail().equals("null") ? "" : detailModel.getEmail());
				phoneText.setText(detailModel.getMobile().length() == 0 || detailModel.getMobile().equals("null") ? "" : detailModel.getMobile());
			}
		}, "me");

		return request;
	}

	private void pay() {
		try {
			String info = getNewOrderInfo("0.05");
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			Log.i("ExternalPartner", "start pay");
			// start the pay.
			Log.i("ALIPAY", "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(SchoolFeedbackApplyActivity.this, mHandler);

					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);

					String result = alipay.pay(orderInfo);

					Log.i("ALIPAY", "result = " + result);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(SchoolFeedbackApplyActivity.this, "交易失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:
			case RQF_LOGIN: {
				Toast.makeText(SchoolFeedbackApplyActivity.this, result.getResult(), Toast.LENGTH_SHORT).show();

			}
				break;
			default:
				break;
			}
		};
	};

	private String getNewOrderInfo(String fee) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(getOutTradeNo());
		sb.append("\"&subject=\"");
		sb.append("校友会--捐赠(" + ApplicationEnvironment.getInstance().getPreferences().getString(Constants.kUSERNAME, "未名") + ")");
		sb.append("\"&body=\"");
		sb.append("首师大校友会捐赠");
		sb.append("\"&total_fee=\"");
		sb.append(fee);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://notify.java.jpxx.org/index.jsp"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(Keys.DEFAULT_SELLER);

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		Log.d("ALIPAY", "outTradeNo: " + key);
		return key;
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
}
