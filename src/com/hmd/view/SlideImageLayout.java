package com.hmd.view;

import java.util.ArrayList;

import com.hmd.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ImageView.ScaleType;

/**
 * ��ɻ���ͼƬ���򲼾�
 * @Description: ��ɻ���ͼƬ���򲼾�

 * @File: SlideImageLayout.java

 * @Package com.image.indicator.layout

 * @Author Hanyonglu

 * @Date 2012-6-18 ����09:04:14

 * @Version V1.0
 */
public class SlideImageLayout {
	// ��ͼƬ��ArrayList
	private ArrayList<ImageView> mImageList = null;
	private Context mContext = null;
	// Բ��ͼƬ����
	private ImageView[] mImageViews = null; 
	private ImageView mImageView = null;
	private NewsXmlParser mParser = null;
	// ��ʾ��ǰ����ͼƬ������
	private int pageIndex = 0;
	
	public SlideImageLayout(Context context) {
		this.mContext = context;
		mImageList = new ArrayList<ImageView>();
		mParser = new NewsXmlParser();
	}
	
	/**
	 * ��ɻ���ͼƬ���򲼾�
	 * @param id
	 * @return
	 */
	public View getSlideImageLayout(int id){
		// ��TextView��LinearLayout
		LinearLayout imageLinerLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams imageLinerLayoutParames = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT,
				1);
		
		ImageView iv = new ImageView(mContext);
		iv.setBackgroundResource(id);
		iv.setOnClickListener(new ImageOnClickListener());
		imageLinerLayout.addView(iv,imageLinerLayoutParames);
		mImageList.add(iv);
		
		return imageLinerLayout;
	}
	
	/**
	 * ��ȡLinearLayout
	 * @param view
	 * @param width
	 * @param height
	 * @return
	 */
	public View getLinearLayout(View view,int width,int height){
		LinearLayout linerLayout = new LinearLayout(mContext);
		LinearLayout.LayoutParams linerLayoutParames = new LinearLayout.LayoutParams(
				width, 
				height,
				1);
		// �������Ҳ�Զ������ã�����Ȥ���Լ����á�
		linerLayout.setPadding(10, 0, 10, 0);
		linerLayout.addView(view, linerLayoutParames);
		
		return linerLayout;
	}
	
	/**
	 * ����Բ�����
	 * @param size
	 */
	public void setCircleImageLayout(int size){
		mImageViews = new ImageView[size];
	}
	
	/**
	 * ���Բ��ͼƬ���򲼾ֶ���
	 * @param index
	 * @return
	 */
	public ImageView getCircleImageLayout(int index){
		mImageView = new ImageView(mContext);  
		mImageView.setLayoutParams(new LayoutParams(10,10));
        mImageView.setScaleType(ScaleType.FIT_XY);
        
        mImageViews[index] = mImageView;
         
        if (index == 0) {  
            //Ĭ��ѡ�е�һ��ͼƬ
            mImageViews[index].setBackgroundResource(R.drawable.dot_selected);  
        } else {  
            mImageViews[index].setBackgroundResource(R.drawable.dot_none);  
        }  
         
        return mImageViews[index];
	}
	
	/**
	 * ���õ�ǰ����ͼƬ������
	 * @param index
	 */
	public void setPageIndex(int index){
		pageIndex = index;
	}
	
	// ����ҳ�����¼�������
    private class ImageOnClickListener implements OnClickListener{
    	@Override
    	public void onClick(View v) {
    		Toast.makeText(mContext, mParser.getSlideTitles()[pageIndex], Toast.LENGTH_SHORT).show();
    		Toast.makeText(mContext, mParser.getSlideUrls()[pageIndex], Toast.LENGTH_SHORT).show();
    	}
    }
}
