package com.hmd.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

/** 继承该类即可实现子Activity的功能 */
public abstract class AbsSubActivity extends BaseActivity {

	private AbsSubActivity requestSubActivity;
	
	public AbsSubActivity getRequestSubActivity() {
		return requestSubActivity;
	}

	public void setRequestSubActivity(AbsSubActivity requestSubActivity) {
		this.requestSubActivity = requestSubActivity;
	}

	private Class<?> getTargetClass(Intent intent) {
		Class<?> clazz = null;
		try {
			if (intent.getComponent() != null)
				clazz = Class.forName(intent.getComponent().getClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return clazz;
	}

	// 重写了startActivity()方法，
	// 这样调用该方法时会根据目标Activity是否是子Activity而调用不同的方法
	@Override
	public void startActivity(Intent intent) {
		if (getTargetClass(intent) != null && AbsSubActivity.class.isAssignableFrom(getTargetClass(intent))) {
			if (this.getParent() instanceof AbsActivityGroup) {
				intent.putExtra("fromSubActivity", getClass().getName());
				((AbsActivityGroup) this.getParent()).launchNewActivity(intent);
			}
		} else {
			super.startActivity(intent);
		}
	}

	// 重写了startActivityForResult()方法，
	// 这样调用该方法时会根据目标Activity是否是子Activity而调用不同的方法
	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		if (getTargetClass(intent) != null && AbsSubActivity.class.isAssignableFrom(getTargetClass(intent))) {
			if (this.getParent() instanceof AbsActivityGroup) {
				intent.putExtra("fromSubActivity", getClass().getName());
				((AbsActivityGroup) this.getParent()).launchNewActivityForResult(this, intent, requestCode);
			}
		} else {
			super.startActivityForResult(intent, requestCode);
		}
	}

	/** 调用此方法来返回上一个界面 */
	// TODO 跟finish的区别？有销毁吗？
	public void goback() {
		Log.i("goback ", "进入");
		Class<?> clazz = null;
		try {
			clazz = Class.forName(getIntent().getStringExtra("fromSubActivity"));
			Log.i("goback ", "出来");
		} catch (ClassNotFoundException e) {
			Log.i("goback ", "抛异常");
			e.printStackTrace();
		}
		Intent intent = new Intent(this, clazz);
		((AbsActivityGroup) this.getParent()).launchActivity(intent);

		BaseActivity.popActivity();
	}

	/** 调用此方法来返回上一个界面并返回数据 */
	public void gobackWithResult(int resultCode, Intent data) {
		Class<?> clazz = null;
		try {
			clazz = Class.forName(getIntent().getStringExtra("fromSubActivity"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		data.setClass(this, clazz);
		if (requestSubActivity != null) {
			requestSubActivity.onActivityResult(data.getIntExtra("requestCode", 0), resultCode, data);
		}
		((AbsActivityGroup) this.getParent()).launchActivity(data);

		BaseActivity.popActivity();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			this.setResult(Activity.RESULT_OK);
			this.gobackWithResult(resultCode, data);
		}
	}

}
