package com.hmd.activity.component;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.activity.AnnouncementListActivity;
import com.hmd.activity.BaseActivity;
import com.hmd.activity.LoginActivity;
import com.hmd.activity.PersonInfoModifyActivity;
import com.hmd.activity.SubmitProfileActivity;
import com.hmd.model.AnnouncementModel;
import com.hmd.model.ProfileModel;
import com.hmd.util.ImageUtil;

public class NameCardMainRelativeLayout extends RelativeLayout {

	private Context mContext = null;
	
	private ProfileModel data = null;
	private RelativeLayout topLayout = null;
	public String mIdentity = "he";// 个人还是他人
	
	public NameCardMainRelativeLayout(Context context){
		super(context);
		
		this.mContext = context;
		this.init();
	}
	
	public NameCardMainRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.mContext = context;
		this.init();
	}
	
	public NameCardMainRelativeLayout(Context context, ProfileModel d) {
		super(context);
		
		this.mContext = context;
		this.data = d;
		
        this.init();
	}

	private void init(){
		LayoutInflater.from(this.mContext).inflate(R.layout.layout_name_card_main, this, true);
		
		this.refresh(this.data);
	}
	
	public void refresh(ProfileModel profileModel){
		this.data = profileModel;
		
		if(this.data == null) 
			return;
		
		this.setVisibility(View.VISIBLE);
		
		topLayout = (RelativeLayout)this.findViewById(R.id.topLayout);
		topLayout.setOnClickListener(listener);
		TextView tvName = (TextView)this.findViewById(R.id.tv_name_card_main_name);
		TextView tvGender = (TextView)this.findViewById(R.id.tv_name_card_main_gender);
		TextView tvBrief1 = (TextView)this.findViewById(R.id.tv_name_card_main_brief1);
		TextView tvBrief2 = (TextView)this.findViewById(R.id.tv_name_card_main_brief2);
		ImageView photoImageView = (ImageView) this.findViewById(R.id.iv_name_card_main_photo);
		
		TextView tv_name_card_main_email = (TextView)this.findViewById(R.id.tv_name_card_main_email);
		TextView tv_name_card_main_qq = (TextView)this.findViewById(R.id.tv_name_card_main_qq);
		TextView tv_name_card_main_phone = (TextView)this.findViewById(R.id.tv_name_card_main_phone);
		
		RelativeLayout rl_name_card_main_brief  = (RelativeLayout)this.findViewById(R.id.rl_name_card_main_brief);
		
		RelativeLayout rl_name_card_main_email  = (RelativeLayout)this.findViewById(R.id.rl_name_card_main_email);
		RelativeLayout rl_name_card_main_qq  = (RelativeLayout)this.findViewById(R.id.rl_name_card_main_qq);
		RelativeLayout rl_name_card_main_phone  = (RelativeLayout)this.findViewById(R.id.rl_name_card_main_phone);
		
		tvName.setText(this.data.getName());
		tvGender.setText(this.data.getGender() == 1? "男":"女");
		
		String data_email = this.data.getEmail();
		String data_qq = this.data.getQq();
		String data_phone = this.data.getMobile();
		String data_brief1 = this.data.getOrg();
		String data_brief2 = this.data.getTitle();
		if(data_email != null && !data_email.equals("null")){
			tv_name_card_main_email.setText("邮箱: "+data_email);
			rl_name_card_main_email.setVisibility(View.VISIBLE);
		}else{
			rl_name_card_main_email.setVisibility(View.GONE);
		}
		
		if(data_qq != null && !data_qq.equals("null")){
			tv_name_card_main_qq.setText("QQ: "+data_qq);
			rl_name_card_main_qq.setVisibility(View.VISIBLE);
		}else{
			rl_name_card_main_qq.setVisibility(View.GONE);
		}
		
		if(data_phone != null && !data_phone.equals("null")){
			tv_name_card_main_phone.setText("电话: "+data_phone);
			rl_name_card_main_phone.setVisibility(View.VISIBLE);
		}else{
			rl_name_card_main_phone.setVisibility(View.GONE);
		}
		
		if((data_brief1 == null || data_brief1.equals("null")) && (data_brief2 == null || data_brief2.equals("null"))){
			rl_name_card_main_brief.setVisibility(View.GONE);
		}else{
			tvBrief1.setText(this.data.getOrg());
			tvBrief2.setText(this.data.getTitle());
			rl_name_card_main_brief.setVisibility(View.VISIBLE);
		}
		
		
		ImageUtil.loadImage(R.drawable.img_card_head_portrait, this.data.getPic(), photoImageView);
	}

	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			if(arg0.getId() == R.id.topLayout){
				if(mIdentity.equals("me")){
					Intent intent = new Intent(BaseActivity.getTopActivity(), PersonInfoModifyActivity.class);
					intent.putExtra("MODEL", data);
					((BaseActivity) NameCardMainRelativeLayout.this.mContext).startActivityForResult(intent,5);
				}
				
			}
			
		}
	};
}
