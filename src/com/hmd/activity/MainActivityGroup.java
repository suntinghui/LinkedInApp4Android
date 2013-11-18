package com.hmd.activity;

import java.lang.reflect.Method;

import com.hmd.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.HorizontalScrollView;

public class MainActivityGroup extends AbsActivityGroup {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	// 第一个需要实现的方法，直接返回ActivityGroup实现类的layou布局即可
	// 注意该布局一定要有个id为activity_group_container的布局用来放子Activity的布局
	@Override
	protected int getLayoutResourceId() {
		// 横向排列选项卡
		return R.layout.activity_maingroup;
	}

	// 第二个需要实现的方法，返回layout布局下选项卡对应的radioButton的id
	@Override
	protected int[] getImageWithTextViewIds() {
		return new int[] { R.id.item1, R.id.item2, R.id.item3, R.id.item4 };
	}

	// 第三个需要实现的方法，上面一个方法中的radioButton对应的图标，注意图标的尺寸要自己调整到合适大小
	@Override
	protected int[] getImageWithTextViewImageIds() {
		return new int[] { R.drawable.img_group_school, R.drawable.img_group_profile, R.drawable.img_group_group, R.drawable.img_group_setting };
	}

	// 第五个需要实现的方法，返回每个选项卡对应的第一个子Activity（注意要继承自AbsSubActivity）
	@SuppressWarnings("unchecked")
	@Override
	public Class<? extends Activity>[] getClasses() {
		Class<? extends Activity>[] classes = new Class[] { SchoolActivity.class, ProfileActivity.class, CircleActivity.class, SchoolActivity.class };
		return classes;
	}

	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

			Class<?> subActivityClass = BaseActivity.getTopActivity().getClass();

			try {
				Method backMethod = subActivityClass.getMethod("backAction", null);
				backMethod.invoke(BaseActivity.getTopActivity(), null);

			} catch (Exception e) {
				e.printStackTrace();
			}

			return true;

		}

		return super.dispatchKeyEvent(event);
	}

}
