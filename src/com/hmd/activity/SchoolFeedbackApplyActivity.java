package com.hmd.activity;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.android.app.sdk.AliPay;
import com.hmd.R;
import com.hmd.alipay.Keys;
import com.hmd.alipay.Result;
import com.hmd.alipay.Rsa;
import com.hmd.client.ApplicationEnvironment;
import com.hmd.client.Constants;

public class SchoolFeedbackApplyActivity extends AbsSubActivity implements OnClickListener {

	private Button backButton;
	private Button okButton;
	private EditText moneyText;

	private static final int RQF_PAY = 1;
	private static final int RQF_LOGIN = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_school_feedback_apply);

		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

		okButton = (Button) this.findViewById(R.id.okButton);
		okButton.setOnClickListener(this);
		
		moneyText = (EditText) this.findViewById(R.id.moneyText);
	}
	
	private boolean checkValue() {
		String m = moneyText.getText().toString().trim();
		if (m.equals("") || m.equals("0")){
			Toast.makeText(this, "请输入有效金额", Toast.LENGTH_SHORT).show();
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
			if (checkValue()) {
				this.pay(moneyText.getText().toString().trim());
			}
			break;
		}
	}

	private void pay(String fee) {
		try {
			String info = getNewOrderInfo(fee);
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

					Log.i("ALIPAY", "result---" + result);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(SchoolFeedbackApplyActivity.this, "无法启动支付宝", Toast.LENGTH_SHORT).show();
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Result result = new Result((String) msg.obj);

			switch (msg.what) {
			case RQF_PAY:
			case RQF_LOGIN: {
				Toast.makeText(SchoolFeedbackApplyActivity.this, result.getResultMsg(), Toast.LENGTH_SHORT).show();

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
