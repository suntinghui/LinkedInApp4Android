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
import com.hmd.model.CommentModel;
import com.hmd.model.GroupModel;
import com.hmd.util.ImageUtil;

public class GroupDetailActivity extends BaseActivity implements OnClickListener {

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
	
	private ArrayList<CommentModel> commentList = new ArrayList<CommentModel>();
	private CommentAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_group_detail);
		
		groupModel = (GroupModel) this.getIntent().getSerializableExtra("model");

		backButton = (Button) this.findViewById(R.id.backButton);
		backButton.setOnClickListener(this);

		joinOrQuitButton = (Button) this.findViewById(R.id.btn_jornOrQuit);
		joinOrQuitButton.setOnClickListener(this);

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
			this.finish();
			break;

		case R.id.btn_jornOrQuit: // 加入或退出圈子

			break;

		case R.id.sendButton:// 发送评论

			break;

		case R.id.queryParticipantBtn: // 查询成员

			break;

		}
	}
	
	public final class CommentHolder{
		public TextView authorName;
		public TextView content;
		public TextView time;
		public ImageView authorImg;
		public Button replyButton;
	}
	
	public class CommentAdapter extends BaseAdapter{
		
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
			if (null == convertView){
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
			holder.replyButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View view) {
					// TODO 评论
					
				}
				
			});
			
			return convertView;
		}
		
	}
	

}
