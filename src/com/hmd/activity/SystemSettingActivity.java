package com.hmd.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.view.LKAlertDialog;

public class SystemSettingActivity extends AbsSubActivity {
	private ListView listView;
	private BusinessAdapter adapter;

	private Integer[] icons = { R.drawable.img_sys_modify_pwd, R.drawable.img_sys_about, R.drawable.img_sys_exit };
	private String[] title = { "修改密码", "关于", "退出" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_system_setting);

		listView = (ListView) this.findViewById(R.id.listview);

		adapter = new BusinessAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				switch (arg2) {
				case 0:// 修改密码
				{
					Intent intent = new Intent(SystemSettingActivity.this, ModifyPwdActivity.class);
					SystemSettingActivity.this.startActivity(intent);
				}	
					break;

				case 1:// 关于
				{
					Intent intent = new Intent(SystemSettingActivity.this, AboutActivity.class);
					SystemSettingActivity.this.startActivity(intent);
				}
					break;

				case 2:// 退出
					logout();
					break;

				default:
					break;
				}
			}

		});

	}
	
	public void logout(){
		LKAlertDialog dialog = new LKAlertDialog(this);
		dialog.setTitle("提示");
		dialog.setMessage("您确定要注销登录吗？");
		dialog.setCancelable(false);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
				
				while (!(BaseActivity.getTopActivity() instanceof LoginActivity)){
					BaseActivity.getTopActivity().finish();
				}
				
			}
		});
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.create().show();
	}

	public final class BusinessHolder {
		private ImageView iv_icon_left;
		private TextView tv_title;
	}

	public class BusinessAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public BusinessAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			return title.length;
		}

		public Object getItem(int arg0) {
			return arg0;
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			BusinessHolder holder = null;
			if (null == convertView) {
				holder = new BusinessHolder();

				convertView = mInflater.inflate(R.layout.listview_item_sys_setting, null);
				convertView.setTag(holder);
			} else {
				holder = (BusinessHolder) convertView.getTag();
			}
			holder.tv_title = (TextView) convertView.findViewById(R.id.titleView);
			holder.tv_title.setText(title[position]);
			holder.iv_icon_left = (ImageView) convertView.findViewById(R.id.imageView);
			holder.iv_icon_left.setImageResource(icons[position]);
			return convertView;
		}
	}

}
