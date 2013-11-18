package com.hmd.activity;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.hmd.R;
import com.hmd.client.HttpRequestType;
import com.hmd.model.CommentModel;
import com.hmd.model.GroupModel;
import com.hmd.network.LKAsyncHttpResponseHandler;
import com.hmd.network.LKHttpRequest;
import com.hmd.network.LKHttpRequestQueue;
import com.hmd.network.LKHttpRequestQueueDone;
import com.hmd.util.ImageUtil;

public class GroupDetailActivity extends AbsSubActivity implements OnClickListener {

	private Button backButton;
	private Button joinOrQuitButton;

	private Button queryParticipantButton;

	private TextView groupNameText;
	private TextView groupContentText;
	private TextView groupTimeText;

	private ListView commentListView;

	private EditText commentEdit;

	private Button sendButton;

	private GroupModel groupModel;
	public int currentGroup = 0; // 0：删除 1：退出 2：加入

	private ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
	private CommentAdapter adapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_group_detail);

		groupModel = (GroupModel) this.getIntent().getSerializableExtra("model");
		currentGroup = this.getIntent().getIntExtra("tag", 0);
		
		backButton = (Button) this.findViewById(R.id.btn_back);
		backButton.setOnClickListener(this);

		joinOrQuitButton = (Button) this.findViewById(R.id.btn_jornOrQuit);
		joinOrQuitButton.setOnClickListener(this);
		switch (currentGroup) {
		case 0:
			joinOrQuitButton.setText("删除圈子");
			break;
		case 1:
			joinOrQuitButton.setText("退出圈子");
			break;
		case 2:
			joinOrQuitButton.setText("加入圈子");
			break;

		default:
			break;
		}

		queryParticipantButton = (Button) this.findViewById(R.id.queryParticipantBtn);
		queryParticipantButton.setOnClickListener(this);

		groupNameText = (TextView) this.findViewById(R.id.groupNameText);
		groupNameText.setText(groupModel.getName());

		groupContentText = (TextView) this.findViewById(R.id.groupContentText);
		groupContentText.setText(groupModel.getDesc());

		groupTimeText = (TextView) this.findViewById(R.id.groupTimeText);
		groupTimeText.setText(groupModel.getCreateTime());

		commentListView = (ListView) this.findViewById(R.id.commentListView);

		commentEdit = (EditText) this.findViewById(R.id.commentEdit);

		sendButton = (Button) this.findViewById(R.id.sendButton);
		sendButton.setOnClickListener(this);

		commentListView = (ListView) this.findViewById(R.id.commentListView);
		adapter = new CommentAdapter(this);
		commentListView.setAdapter(adapter);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back: // 返回
			this.goback();
			break;

		case R.id.btn_jornOrQuit: // 加入或退出圈子
			groupAction();
			break;

		case R.id.sendButton:// 发送评论

			break;

		case R.id.queryParticipantBtn: // 查询成员

			break;

		}
	}

	public final class CommentHolder {
		public TextView authorName;
		public TextView content;
		public TextView time;
		public ImageView authorImg;
		public Button replyButton;
	}

	public class CommentAdapter extends BaseAdapter {

		private LayoutInflater mInflater;

		public CommentAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return commentList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return commentList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			CommentHolder holder = null;
			if (null == convertView) {
				convertView = this.mInflater.inflate(R.layout.listview_item_group_comment, null);
				holder = new CommentHolder();

				holder.authorName = (TextView) convertView.findViewById(R.id.authorNameText);
				holder.content = (TextView) convertView.findViewById(R.id.contentText);
				holder.time = (TextView) convertView.findViewById(R.id.timeText);
				holder.authorImg = (ImageView) convertView.findViewById(R.id.authorLogoImage);
				holder.replyButton = (Button) convertView.findViewById(R.id.replyButton);

				convertView.setTag(holder);
			} else {
				holder = (CommentHolder) convertView.getTag();
			}

			CommentModel comment = commentList.get(position);
			holder.authorName.setText(comment.getAuthorName());
			holder.content.setText(comment.getContent());
			holder.time.setText(comment.getTime());
			ImageUtil.loadImage(R.drawable.img_weibo_item_pic_loading, comment.getAuthorPic(), holder.authorImg);
			holder.replyButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO 评论

				}

			});

			return convertView;
		}

	}

	private void groupAction() {
		LKHttpRequestQueue queue = new LKHttpRequestQueue();
		String tmp_tip = "";
		switch (currentGroup) {
		case 0:
			tmp_tip = "正在删除圈子...";
			queue.addHttpRequest(getDeleteGroupRequest());
			this.goback();
			break;
		case 1:
			queue.addHttpRequest(getQuiteGroupRequest());
			tmp_tip = "正在退出圈子...";
			this.goback();
			break;
		case 2:
			queue.addHttpRequest(getJoinGroupRequest());
			tmp_tip = "正在加入圈子...";
			this.goback();
			break;

		default:
			break;
		}

		queue.executeQueue(tmp_tip, new LKHttpRequestQueueDone());
	}

	// 加入圈子列表
	private LKHttpRequest getJoinGroupRequest() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_GROUP_JOIN, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				if ((Integer) obj == 1) {
					GroupDetailActivity.this.showToast("成功加入圈子！");
				} else if ((Integer) obj == -2) {
					GroupDetailActivity.this.showToast("已经加入过该圈子！");
				}
			}
		}, groupModel.getId());
		return request;
	}
	// 删除圈子列表
	private LKHttpRequest getDeleteGroupRequest() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_GROUP_JOIN, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				if ((Integer) obj == 1) {
					GroupDetailActivity.this.showToast("成功删除圈子！");
				} else if ((Integer) obj == -2) {
					GroupDetailActivity.this.showToast("操作失败！");
				}
			}
		}, groupModel.getId());
		return request;
	}

	// 退出圈子列表
	private LKHttpRequest getQuiteGroupRequest() {
		LKHttpRequest request = new LKHttpRequest(HttpRequestType.HTTP_GROUP_QUIT, null, new LKAsyncHttpResponseHandler() {
			@Override
			public void successAction(Object obj) {
				if ((Integer) obj == 1) {
					GroupDetailActivity.this.showToast("成功退出圈子！");

				} else if ((Integer) obj == -2) {
					GroupDetailActivity.this.showToast("没有加入过该圈子！");
				} else {
					GroupDetailActivity.this.showToast("退出失败！");
				}
			}
		}, groupModel.getId());
		return request;
	}

}
