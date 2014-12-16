package com.hmd.network;

import org.apache.http.client.HttpResponseException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.content.DialogInterface;
import android.util.Log;

import com.hmd.activity.BaseActivity;
import com.hmd.activity.LoginActivity;
import com.hmd.activity.SystemSettingActivity;
import com.hmd.client.ParseResponseData;
import com.hmd.enums.ErrorCode;
import com.hmd.exception.ServiceErrorException;
import com.hmd.view.LKAlertDialog;
import com.loopj.android.http.JsonHttpResponseHandler;

public abstract class LKAsyncHttpResponseHandler extends JsonHttpResponseHandler {

	private LKHttpRequest request;

	public void setRequest(LKHttpRequest request) {
		this.request = request;
	}

	public abstract void successAction(Object obj);

	public void failureAction(Throwable error, JSONObject content) {
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onSuccess(JSONObject response) {
		super.onSuccess(response);

		if (null == response) {
			BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, "系统异常，请重新操作。");
			return;
		}

		// TODO 如何处理异常？
		Object obj = null;
		try {
			obj = ParseResponseData.parse(request.getRequestId(), response);
			Log.e("success", "try to do success action... " + request.getRequestId());

			successAction(obj);

			try {
				// 如果不通过队列单独发起一个LKHttpRequest请求则会导致异常。
				if (null != this.request.getRequestQueue()) {
					// 当然也不需要去通知队列执行完成。
					this.request.getRequestQueue().updateCompletedTag(this.request.getTag());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (ServiceErrorException e) {
			e.printStackTrace();

			if (e.getErrorCode() == ErrorCode.SESSIONID_ERROR) {
				LKAlertDialog dialog = new LKAlertDialog(BaseActivity.getTopActivity());
				dialog.setTitle("提示");
				dialog.setMessage("系统超时，请您重新登录");
				dialog.setCancelable(false);
				dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						arg0.dismiss();

						try{
							while (!(BaseActivity.getTopActivity() instanceof LoginActivity)) {
								BaseActivity.getTopActivity().finish();
							}
							
						} catch(Exception e){
							e.printStackTrace();
						}

					}
				});
				dialog.createDialog().show();

			} else {
				BaseActivity.getTopActivity().hideDialog(BaseActivity.ALL_DIALOG);

				BaseActivity.getTopActivity().showDialog(BaseActivity.MODAL_DIALOG, e.getMessage());
			}

		}

	}

	@Override
	public void onFailure(final Throwable error, final JSONObject errorResponse) {
		super.onFailure(error, errorResponse);

		// TODO
		String content = "onFailure TODO";

		try {
			HttpResponseException exception = (HttpResponseException) error;
			Log.e("Status Code", "" + exception.getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Log.e("error content:", content);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Log.e("failure:", error.getCause().toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Log.e("failure message:", error.getCause().getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 当有请求失败的时候，终止所有的请求
		// LKHttpRequestQueue.sharedClient().cancelRequests(ApplicationEnvironment.getInstance().getApplication(),
		// true);
		// LKHttpRequestQueue.sharedClient().getHttpClient().getConnectionManager().shutdown();

		try {
			if (null != this.request.getRequestQueue()) {
				this.request.getRequestQueue().cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		BaseActivity.getTopActivity().hideDialog(BaseActivity.ALL_DIALOG);

		LKAlertDialog dialog = new LKAlertDialog(BaseActivity.getTopActivity());
		dialog.setTitle("提示");

		try {
			dialog.setMessage(getErrorMsg(error.toString()));
		} catch (Exception e) {
			dialog.setMessage(getErrorMsg(null));
		}

		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				dialog.dismiss();

				failureAction(error, errorResponse);
			}
		});
		dialog.createDialog().show();

		Log.e("error:", error.toString());
	}

	@Override
	public void onFinish() {
		super.onFinish();

		try {
			if (null != this.request.getRequestQueue()) {
				this.request.getRequestQueue().updataFinishedTag(this.request.getTag());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String getErrorMsg(String content) {
		if (null == content)
			return "服务器异常，请与管理员联系或稍候再试。";

		// org.apache.http.conn.ConnectTimeoutException: Connect to
		// /124.205.53.178:9596 timed out
		if (content.contains("ConnectTimeoutException") || content.contains("SocketTimeoutException")) {
			return "连接服务器超时，请检查您的网络情况或稍候再试。";
		} else if (content.contains("HttpHostConnectException") || content.contains("ConnectException")) {
			return "连接服务器超时，请检查您的网络情况或稍候再试。";
		} else if (content.contains("Bad Request")) { // org.apache.http.client.HttpResponseException:
														// Bad Request
			return "服务器地址发生更新，请与管理员联系或稍候再试。";
		} else if (content.contains("time out")) { // socket time out
			return "连接服务器超时，请重试。";
		} else if (content.contains("can't resolve host") || content.contains("400 Bad Request")) {
			return "连接服务器出错，请确定您的连接服务器地址是否正确。";
		} else if (content.contains("UnknownHostException")) { // java.net.UnknownHostException:
																// Unable to
																// resolve host
																// "oagd.crbcint.com":
																// No address
																// associated
																// with hostname
			return "网络异常，无法连接服务器。";
		}

		// return "处理请求出错[" + content +"]";
		return "服务器响应异常,请重新操作。";
	}

}
