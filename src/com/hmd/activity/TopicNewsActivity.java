package com.hmd.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.model.MediaModel;
import com.hmd.util.ListViewUtil;
import com.hmd.view.NewsXmlParser;
import com.hmd.view.SlideImageLayout;

public class TopicNewsActivity extends AbsSubActivity implements OnClickListener {
	// 滑动图片的集合
	private ArrayList<View> mImagePageViewList = null;
	private ViewGroup mMainView = null;
	private ViewPager mViewPager = null;
	// 当前ViewPager索引
	// private int pageIndex = 0;

	// 包含圆点图片的View
	private ViewGroup mImageCircleView = null;
	private ImageView[] mImageCircleViews = null;

	// 滑动标题
	private TextView mSlideTitle = null;

	// 布局设置类
	private SlideImageLayout mSlideLayout = null;

	// 数据解析类
	private NewsXmlParser mParser = null;

	private ListView lv_news = null;
	private NewsAdapter adapter_news = null;
	private ArrayList<MediaModel> list = new ArrayList<MediaModel>();
	private int totalPage;
	private int currentPage;

	private TextView titleView = null;
	private Button backButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initeViews();
	}

	private void initeViews() {

		// 滑动图片区域
		mImagePageViewList = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		mMainView = (ViewGroup) inflater.inflate(R.layout.page_topic_news, null);
		mViewPager = (ViewPager) mMainView.findViewById(R.id.image_slide_page);

		titleView = (TextView) mMainView.findViewById(R.id.titleView);

		backButton = (Button) mMainView.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

		// 圆点图片区域
		mParser = new NewsXmlParser();
		int length = mParser.getSlideImages().length;
		mImageCircleViews = new ImageView[length];
		mImageCircleView = (ViewGroup) mMainView.findViewById(R.id.layout_circle_images);
		mSlideLayout = new SlideImageLayout(TopicNewsActivity.this);
		mSlideLayout.setCircleImageLayout(length);

		for (int i = 0; i < length; i++) {
			mImagePageViewList.add(mSlideLayout.getSlideImageLayout(mParser.getSlideImages()[i]));
			mImageCircleViews[i] = mSlideLayout.getCircleImageLayout(i);
			mImageCircleView.addView(mSlideLayout.getLinearLayout(mImageCircleViews[i], 10, 10));
			mSlideLayout.getSlideImageLayout(mParser.getSlideImages()[i]).setTag(1000 + i);
			mSlideLayout.getSlideImageLayout(mParser.getSlideImages()[i]).setOnClickListener(this);
		}

		// 设置默认的滑动标题
		mSlideTitle = (TextView) mMainView.findViewById(R.id.tvSlideTitle);
		mSlideTitle.setText(mParser.getSlideTitles()[0]);

		setContentView(mMainView);

		// 设置ViewPager
		mViewPager.setAdapter(new SlideImageAdapter());
		mViewPager.setOnPageChangeListener(new ImagePageChangeListener());

		list = getTestList();
		lv_news = (ListView) this.findViewById(R.id.lv_news);
		adapter_news = new NewsAdapter(this);
		lv_news.setAdapter(adapter_news);
		ListViewUtil.setListViewHeightBasedOnChildren(lv_news);
		lv_news.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

			}

		});
	}

	// 滑动图片数据适配器
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

	// 滑动页面更改事件监听器
	private class ImagePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			// pageIndex = index;
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

	public final class NewsViewHolder {
		public RelativeLayout contentLayout;
		public RelativeLayout moreLayout;
		
		public TextView titleView;
		public TextView contentView;
		public TextView tv_time;
		public ImageView imageView;
		public Button moreButton;
	}

	public class NewsAdapter extends BaseAdapter {
		private LayoutInflater mInflater;

		public NewsAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		public int getCount() {
			if (currentPage + 1 < totalPage) {
				return list.size() + 1;
			} else {
				return list.size();
			}
		}

		public Object getItem(int arg0) {
			return list.get(arg0);
		}

		public long getItemId(int arg0) {
			return arg0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			NewsViewHolder holder = null;
			if (null == convertView) {
				holder = new NewsViewHolder();

				convertView = mInflater.inflate(R.layout.listview_item_news_ex, null);

				holder.contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);
				holder.moreLayout = (RelativeLayout) convertView.findViewById(R.id.moreLayout);

				holder.titleView = (TextView) convertView.findViewById(R.id.titleView);
				holder.contentView = (TextView) convertView.findViewById(R.id.contentView);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.moreButton = (Button) convertView.findViewById(R.id.moreButton);
				holder.moreButton.setOnClickListener(TopicNewsActivity.this);

				convertView.setTag(holder);
			} else {
				holder = (NewsViewHolder) convertView.getTag();
			}

			if (currentPage + 1 < totalPage) {
				if (position == list.size()) {
					holder.contentLayout.setVisibility(View.GONE);
					holder.moreLayout.setVisibility(View.VISIBLE);
				} else {
					holder.contentLayout.setVisibility(View.VISIBLE);
					holder.moreLayout.setVisibility(View.GONE);

					holder.titleView.setText(list.get(position).getTitle());
					holder.contentView.setText(list.get(position).getContent());
					holder.tv_time.setText(list.get(position).getTime());
				}
			} else {
				holder.contentLayout.setVisibility(View.VISIBLE);
				holder.moreLayout.setVisibility(View.GONE);

				holder.titleView.setText(list.get(position).getTitle());
				holder.contentView.setText(list.get(position).getContent());
				holder.tv_time.setText(list.get(position).getTime());
			}

			return convertView;
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.backButton:
			this.goback();
			break;
		}
	}
	
	public ArrayList<MediaModel> getTestList() {
		ArrayList<MediaModel> list = new ArrayList<MediaModel>();

		MediaModel model = new MediaModel();
		model.setType(1);
		model.setTitle("罗马尼亚基督教大学代表团来我校访问");
		model.setContent("    10月9日，罗马尼亚基督教大学校长、副校长一行4人来我校正式访问，这是我校第一次接待来自罗马尼亚的代表团。我校党委书记张雪、外国语学院院长封一函、教育学院党委书记方平、国际文化学院党总支书记兼副院长韩梅、国际文化学院副院长张静、政法学院副院长吴高臣、管理学院蒋景媛会见了来访客人。" + "\n    张雪书记和罗马尼亚基督教大学校长介绍了各自学校的概况，并畅谈了促进两校国际化发展的良好期望。中外双方院系负责人分别介绍了各自院系及专业情况。张雪书记指出，两校可以多种方式进行合作，如3+1、2+2、学生交换、对外汉语专业学生实习、教师交换、合作研究等等。罗马尼亚基督教大学3年前开设了中文专业，中文师资奇缺，希望我校每年能派中文教师及对外汉语教学实习生教授中文课程，并帮助其培养本校师资。" + "\n    这次访问为两校进一步合作与交流奠定了良好的基础，两校将尽快签署合作协议，启动合作项目。");
		model.setTime("2013-11-12 10:30");
		model.setPics(new String[] { "http://www.cnu.edu.cn/download.jsp?attachSeq=15161&filename=20131011171639480.jpg", "http://www.cnu.edu.cn/download.jsp?attachSeq=15157&filename=20131011170853141.jpg" });

		list.add(model);
		list.add(model);
		list.add(model);
		list.add(model);
		list.add(model);
		list.add(model);
		list.add(model);
		list.add(model);

		return list;
	}
}
