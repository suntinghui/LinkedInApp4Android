package com.hmd.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hmd.R;
import com.hmd.model.ActiveModel;

public class SchoolEventRelativeLayout extends RelativeLayout {

	private LinearLayout bodyLayout;
	private Context context;

	public SchoolEventRelativeLayout(Context context) {
		super(context);
		this.context = context;

		this.init();
	}

	private void init() {
		LayoutInflater.from(context).inflate(R.layout.layout_school_event, this, true);
		bodyLayout = (LinearLayout) this.findViewById(R.id.bodyLayout);
	}

	public void refresh(ArrayList<ActiveModel> list) {
		this.setVisibility(View.VISIBLE);

		for (int i = 0; i < list.size(); i++) {

			ActiveItemLinearLayout layout = new ActiveItemLinearLayout(this.context, list.get(i));

			// 最后一项隐藏分隔线
			if (i == list.size() - 1) {
				layout.hideDivider();
			}

			layout.setPadding(0, 0, 0, 0);

			bodyLayout.addView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		}
	}
}
