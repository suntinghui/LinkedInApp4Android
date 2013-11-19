package com.hmd.view;

import com.hmd.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.view.View.OnClickListener;

public class EditTextWithClearView extends RelativeLayout implements OnClickListener {
	
	private EditText editText = null;
	private ImageView clearImage = null;
	private ImageView typeImage = null;

	public EditTextWithClearView(Context context) {
		super(context);
	}
	
	public EditTextWithClearView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.layout_edittext_clear, this, true);
		editText = (EditText) layout.findViewById(R.id.editText);
		clearImage = (ImageView) layout.findViewById(R.id.clearImage);
		clearImage.setOnClickListener(this);
		typeImage = (ImageView) layout.findViewById(R.id.typeImage);
		
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditTextWithClearView);  
		CharSequence hint = typedArray.getText(R.styleable.EditTextWithClearView_hint);
		if (null != hint){
			editText.setHint(hint);
		}
		
		int inputType = typedArray.getInteger(R.styleable.EditTextWithClearView_inputType, 0);
		if (inputType == 0){
			editText.setInputType(InputType.TYPE_CLASS_TEXT);
		} else if (inputType == 1){
			editText.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
		}
		
		int imageType = typedArray.getInteger(R.styleable.EditTextWithClearView_leftImage, 0);
		if (imageType == 0){
			typeImage.setVisibility(View.GONE);
		} else if (imageType == 1) {
			typeImage.setVisibility(View.VISIBLE);
			typeImage.setImageResource(R.drawable.img_edittext_user);
		} else if (imageType == 1){
			typeImage.setVisibility(View.VISIBLE);
			typeImage.setImageResource(R.drawable.img_edittext_pwd);
		} else {
			typeImage.setVisibility(View.GONE);
		}
		
		typedArray.recycle();
	}
	
	public void setText(String str){
		this.editText.setText(null == str ? "" : str);
	}
	
	public String getText(){
		return this.editText.getText().toString();
	}
	
	public void setHint(String hint){
		this.editText.setHint(hint);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.clearImage:
			editText.setText("");
			break;
		}
		
	}

	

}
