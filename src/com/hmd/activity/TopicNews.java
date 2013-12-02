package com.hmd.activity;

import java.util.ArrayList;
import java.util.List;

import weibo4j.model.Status;

import com.hmd.R;
import com.hmd.view.NewsXmlParser;
import com.hmd.view.SlideImageLayout;
import com.hmd.view.TimelineAdapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * ͷ������Activity
 * @Description: ͷ������Activity

 * @File: TopicNews.java

 * @Package com.image.indicator.activity

 * @Author Hanyonglu

 * @Date 2012-6-18 ����08:24:36

 * @Version V1.0
 */
public class TopicNews extends Activity implements OnClickListener{
	// ����ͼƬ�ļ���
	private ArrayList<View> mImagePageViewList = null;
	private ViewGroup mMainView = null;
	private ViewPager mViewPager = null;
	// ��ǰViewPager����
//	private int pageIndex = 0; 
	
	// ��Բ��ͼƬ��View
	private ViewGroup mImageCircleView = null;
	private ImageView[] mImageCircleViews = null; 
	
	// ��������
	private TextView mSlideTitle = null;
	
	// ����������
	private SlideImageLayout mSlideLayout = null;
	// ��ݽ�����
	private NewsXmlParser mParser = null; 
	
	private ListView lv_news = null;
	private NewsAdapter adapter_news= null;
	private ArrayList<String> list = new ArrayList<String>();
	private int totalPage;
	private int currentPage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(android.R.style.Theme_Translucent_NoTitleBar);		
		// ��ʼ��
		initeViews();
	}
	
	/**
	 * ��ʼ��
	 */
	private void initeViews(){
		// ����ͼƬ����
		mImagePageViewList = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();  
		mMainView = (ViewGroup)inflater.inflate(R.layout.page_topic_news, null);
		mViewPager = (ViewPager) mMainView.findViewById(R.id.image_slide_page);  
		
		// Բ��ͼƬ����
		mParser = new NewsXmlParser();
		int length = mParser.getSlideImages().length;
		mImageCircleViews = new ImageView[length];
		mImageCircleView = (ViewGroup) mMainView.findViewById(R.id.layout_circle_images);
		mSlideLayout = new SlideImageLayout(TopicNews.this);
		mSlideLayout.setCircleImageLayout(length);
		
		for(int i = 0; i < length; i++){
			mImagePageViewList.add(mSlideLayout.getSlideImageLayout(mParser.getSlideImages()[i]));
			mImageCircleViews[i] = mSlideLayout.getCircleImageLayout(i);
			mImageCircleView.addView(mSlideLayout.getLinearLayout(mImageCircleViews[i], 10, 10));
		}
		
		// ����Ĭ�ϵĻ�������
		mSlideTitle = (TextView) mMainView.findViewById(R.id.tvSlideTitle);
		mSlideTitle.setText(mParser.getSlideTitles()[0]);
		
		setContentView(mMainView);
		
		// ����ViewPager
        mViewPager.setAdapter(new SlideImageAdapter());  
        mViewPager.setOnPageChangeListener(new ImagePageChangeListener());
        
        
        
        list.add("one");
        list.add("two");
        list.add("three");
        list.add("four");
        list.add("one");
        list.add("two");
        list.add("three");
        list.add("four");
        lv_news = (ListView)this.findViewById(R.id.lv_news);
		adapter_news = new NewsAdapter(this);
		lv_news.setAdapter(adapter_news);
		lv_news.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			}
			
		});
	}
	
	// ����ͼƬ���������
    private class SlideImageAdapter extends PagerAdapter {  
        @Override  
        public int getCount() { 
            return mImagePageViewList.size();  
        }  
  
        @Override  
        public boolean isViewFromObject(View view, Object object) {  
            return view == object;  
        }  
  
        @Override  
        public int getItemPosition(Object object) {  
            return super.getItemPosition(object);  
        }  
  
        @Override  
        public void destroyItem(View view, int arg1, Object arg2) {  
            ((ViewPager) view).removeView(mImagePageViewList.get(arg1));  
        }  
  
        @Override  
        public Object instantiateItem(View view, int position) {  
        	((ViewPager) view).addView(mImagePageViewList.get(position));
            
            return mImagePageViewList.get(position);  
        }  
  
        @Override  
        public void restoreState(Parcelable arg0, ClassLoader arg1) {  
  
        }  
  
        @Override  
        public Parcelable saveState() {  
            return null;  
        }  
  
        @Override  
        public void startUpdate(View arg0) {  
        }  
  
        @Override  
        public void finishUpdate(View arg0) {  
        }  
    }
    
    // ����ҳ�����¼�������
    private class ImagePageChangeListener implements OnPageChangeListener {
        @Override  
        public void onPageScrollStateChanged(int arg0) {  
        }  
  
        @Override  
        public void onPageScrolled(int arg0, float arg1, int arg2) {  
        }  
  
        @Override  
        public void onPageSelected(int index) {  
//        	pageIndex = index;
        	mSlideLayout.setPageIndex(index);
        	mSlideTitle.setText(mParser.getSlideTitles()[index]);
        	
            for (int i = 0; i < mImageCircleViews.length; i++) {  
            	mImageCircleViews[index].setBackgroundResource(R.drawable.dot_selected);
                
                if (index != i) {  
                	mImageCircleViews[i].setBackgroundResource(R.drawable.dot_none);  
                }  
            }
        }  
    }
    
    
    public final class NewsViewHolder{
		public RelativeLayout contentLayout;
		public RelativeLayout moreLayout;
		
		public TextView tv_name;
		
		public Button moreButton;
	}
	
	public class NewsAdapter extends BaseAdapter{
		private LayoutInflater mInflater;
		public NewsAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}
		
		public int getCount(){
			if (currentPage+1 < totalPage){
				return list.size() + 1;
			} else {
				return list.size();
			}
		}
		
		public Object getItem(int arg0){
			return list.get(arg0);
		}
		
		public long getItemId(int arg0){
			return arg0;
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			NewsViewHolder holder = null;
			if (null == convertView){
				holder = new NewsViewHolder();
				
				convertView = mInflater.inflate(R.layout.listview_item_news, null);
				
				holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
				holder.moreLayout = (RelativeLayout) convertView.findViewById(R.id.moreLayout);
				
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.moreButton = (Button) convertView.findViewById(R.id.moreButton);
				holder.moreButton.setOnClickListener(TopicNews.this);
				
				convertView.setTag(holder);
			} else {
				holder = (NewsViewHolder) convertView.getTag();
			}
			
			if (currentPage+1 < totalPage) {
				if (position == list.size()){
					holder.contentLayout.setVisibility(View.GONE);
					holder.moreLayout.setVisibility(View.VISIBLE);
				} else {
					holder.contentLayout.setVisibility(View.VISIBLE);
					holder.moreLayout.setVisibility(View.GONE);
					
					holder.tv_name.setText(list.get(position));
				}
			} else {
				holder.contentLayout.setVisibility(View.VISIBLE);
				holder.moreLayout.setVisibility(View.GONE);
				
				holder.tv_name.setText(list.get(position));
			}
			
			return convertView;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}