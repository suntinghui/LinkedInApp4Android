package com.hmd.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.hmd.enums.ErrorCode;
import com.hmd.exception.ServiceErrorException;
import com.hmd.model.ActiveModel;
import com.hmd.model.AnnouncementModel;
import com.hmd.model.CommentModel;
import com.hmd.model.GroupModel;
import com.hmd.model.ProfileModel;
import com.hmd.model.SchoolModel;
import com.hmd.model.TimelineModel;

public class ParseResponseData {

	public static Object parse(String type, JSONObject jsonObject) throws ServiceErrorException {
		int errorCode = jsonObject.optInt("ec", ErrorCode.UNKNOWN);

		if (errorCode != ErrorCode.SUCCESS) {
			throw new ServiceErrorException(getErrorMsg(errorCode) + "[ERROR_CODE:" + errorCode + "]");
		}

		// Parse
		if (type.equalsIgnoreCase(HttpRequestType.HTTP_LOGIN)) {
			return login(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_LOGOUT)) {
			return logout(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_UPDATEPWD)) {
			return updatePwd(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_REGISTER)) {
			return register(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_BASIC)) {
			return getProfileBasic(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_ALL)) {
			return getProfileAll(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_MATCH)) {
			return profileMatch(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_UPDATE)) {
			return profileUpdate(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_BROADCAST_LIST)) {
			return getBroadcastList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_BROADCAST_DETAIL)) {
			return getBroadcastDetail(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_EVENT_TYPE_LIST)) {
			return getEventTypeList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_EVENT_LIST)) {
			return getEventList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_EVENT_DETAIL)) {
			return getEventDetail(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_EVENT_PARTICIPANT_LIST)) {
			return getEventParticipantList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_TIMELINE_LIST)) {
			return getTimelineList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_TIMELINE_NODE_CREATE)) {
			return timelineNodeCreate(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_TIMELINE_NODE_UPDATE)) {
			return timelineNodeUpdate(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_TIMELINE_NODE_DELETE)) {
			return timelineNodeDelete(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_INTRODUCT)) {
			return getCollegeInfo(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_TIMELINE_NODE_NEWFRIENDS_LIST)) {
			return getSuggestPeopleList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FRIENDS_LIST)) {
			return getMyAttentionsList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FRIENDS_FUNS_LIST)) {
			return getFansList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FRIENDS_FOLLOW)) {
			return getAddAttention(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FRIENDS_UNFOLLOW)) {
			return getCancelAttention(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_ME_LIST)) {
			return getGroupMeList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_MY_LIST)) {
			return getGroupMyList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_PARTICIPANT_LIST)) {
			return getParticipantList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_LIST)) {
			return getAllGroupList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_JOIN)) {
			return getGroupJoin(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_DELETE)) {
			return getGroupDelete(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_QUIT)) {
			return getGroupQuit(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_CREATE)) {
			return getCreateCircle(jsonObject);
			
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GROUP_COMMENT_LIST)) {
			return getCommentList(jsonObject);
			
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PUBLISH_COMMENT)) {
			return getPublishComment(jsonObject);
		}

		return null;
	}

	/*
	 * 登录
	 */
	private static Object login(JSONObject jsonObject) {

		return JSONObject2Map(jsonObject);
	}

	/*
	 * 登出
	 */
	private static Object logout(JSONObject jsonObject) {

		return JSONObject2Map(jsonObject);
	}

	/*
	 * 修改密码
	 */
	private static Object updatePwd(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	/*
	 * 注册
	 */
	private static Object register(JSONObject jsonObject) {

		return JSONObject2Map(jsonObject);
	}

	/*
	 * 查看个人基本信息
	 * 
	 * {"basic":{"adYear":2000,"colg":"首都师范大学","name":"刘斌","gender":0,"dept":"未知"
	 * ,"gradYear":2004,"major":"小学教育"},"rc":1,"ec":1}
	 */
	private static Object getProfileBasic(JSONObject jsonObject) {
		try {
			JSONTokener parse = new JSONTokener(jsonObject.optString("basic"));
			JSONObject basicObj = (JSONObject) parse.nextValue();

			if (null != basicObj) {
				ProfileModel model = new ProfileModel();
				model.setName(basicObj.optString("name", ""));
				model.setGender(basicObj.optInt("gender"));
				model.setSchool(basicObj.optString("colg", ""));
				model.setMajor(basicObj.optString("major", ""));
				model.setAdYear(basicObj.optString("adYear", ""));
				model.setGradYear(basicObj.optString("gradYear", ""));
				model.setPic(basicObj.optString("pic", ""));

				return model;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	private static Object getProfileAll(JSONObject jsonObject) {
		try {
			JSONTokener parse = new JSONTokener(jsonObject.optString("basic"));
			JSONObject basicObj = (JSONObject) parse.nextValue();
			JSONTokener parse2 = new JSONTokener(jsonObject.optString("ext"));
			JSONObject extObj = (JSONObject) parse2.nextValue();
			ProfileModel model = new ProfileModel();
			if (null != basicObj) {
				// TODO 因为返回的数据不全，有些字段不能够多确定，待完善

				model.setName(basicObj.optString("name", ""));
				model.setGender(basicObj.optInt("gender"));
				model.setSchool(basicObj.optString("colg", ""));
				model.setDept(basicObj.optString("dept", ""));
				model.setMajor(basicObj.optString("major", ""));
				model.setAdYear(basicObj.optString("adYear", ""));
				model.setGradYear(basicObj.optString("gradYear", ""));

			}
			if (null != extObj) {
				model.setBirthday(extObj.optString("birthday", ""));
				model.setBirthplace(extObj.optString("birthplace", ""));
				model.setNation(extObj.optString("nation", ""));
				model.setDesc(extObj.optString("desc", ""));
			}
			return model;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static Object profileMatch(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 更新个人信息
	private static Object profileUpdate(JSONObject jsonObject) {

		return JSONObject2Map(jsonObject);
	}

	// 取得公告列表
	private static Object getBroadcastList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<AnnouncementModel> modelList = new ArrayList<AnnouncementModel>();

		int total = jsonObject.optInt("total", 0);
		map.put("total", total);

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = (JSONObject) jsonArray.opt(i);

				AnnouncementModel model = new AnnouncementModel();
				model.setId(obj.optString("id", ""));
				model.setTitle(obj.optString("title", ""));
				model.setTime(obj.optString("time", ""));
				model.setPreview(obj.optString("preview", ""));

				modelList.add(model);
			}
		}

		map.put("list", modelList);

		return map;
	}

	public static Object getBroadcastDetail(JSONObject jsonObject) {
		AnnouncementModel model = new AnnouncementModel();
		model.setTitle(jsonObject.optString("title", ""));
		model.setContent(jsonObject.optString("content", ""));
		model.setTime(jsonObject.optString("time", ""));

		return model;
	}

	// 获取活动类型列表
	public static Object getEventTypeList(JSONObject jsonObject) {
		HashMap<String, String> map = new HashMap<String, String>();

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (null != jsonArray) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = (JSONObject) jsonArray.opt(i);
				map.put(obj.optString("id"), obj.optString("display"));
			}
		}

		return map;
	}

	// 获取活动列表
	public static Object getEventList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();

		int total = jsonObject.optInt("total", 0); // 活动总数
		map.put("total", total);

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (null != jsonArray) {
			ArrayList<ActiveModel> list = new ArrayList<ActiveModel>();

			for (int i = 0; i < jsonArray.length(); i++) {
				ActiveModel model = new ActiveModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setId(obj.optString("id", ""));
				model.setStime(obj.optString("stime", ""));
				model.setEtime(obj.optString("etime", ""));
				model.setAddress(obj.optString("address", ""));
				model.setTitle(obj.optString("title", ""));
				model.setTypeID(obj.optString("typeID", ""));
				model.setCharge(obj.optInt("charge", 0));
				model.setSponsor(obj.optString("sponsor", ""));
				model.setPreview(obj.optString("preview", ""));
				model.setPosterImage(obj.optString("pic", ""));

				list.add(model);
			}

			map.put("list", list);
		}

		return map;
	}

	// 获取活动详情
	public static Object getEventDetail(JSONObject jsonObject) {
		ActiveModel model = new ActiveModel();

		model.setId(jsonObject.optString("id", ""));
		model.setStime(jsonObject.optString("stime", ""));
		model.setEtime(jsonObject.optString("etime", ""));
		model.setAddress(jsonObject.optString("address", ""));
		model.setTitle(jsonObject.optString("title", ""));
		model.setTypeID(jsonObject.optString("typeID", ""));
		model.setCharge(jsonObject.optInt("charge", 0));
		model.setSponsor(jsonObject.optString("sponsor", ""));
		model.setPreview(jsonObject.optString("preview", ""));
		model.setContent(jsonObject.optString("content", ""));

		return model;
	}

	public static Object getEventParticipantList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		int total = jsonObject.optInt("total", 0);
		map.put("total", total);

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = (JSONObject) jsonArray.opt(i);

				ProfileModel model = new ProfileModel();
				model.setId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));

				modelList.add(model);
			}
		}

		map.put("list", modelList);

		return map;
	}

	// 根据选中履历推荐好友列表
	private static Object getSuggestPeopleList(JSONObject jsonObject) {
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ProfileModel model = new ProfileModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setFlag("2");
				model.setId(obj.optString("id", ""));
				model.setCity(obj.optString("city"));
				model.setProvince(obj.optString("province"));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 我关注的好友列表
	private static Object getMyAttentionsList(JSONObject jsonObject) {
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ProfileModel model = new ProfileModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setFlag("1");
				model.setId(obj.optString("id", ""));
				model.setCity(obj.optString("city"));
				model.setProvince(obj.optString("province"));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 关注我的人列表
	private static Object getFansList(JSONObject jsonObject) {
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ProfileModel model = new ProfileModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setFlag("0");
				model.setId(obj.optString("id", ""));
				model.setCity(obj.optString("city"));
				model.setProvince(obj.optString("province"));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 加关注
	private static int getAddAttention(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 取消关注
	private static int getCancelAttention(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 查看我加入的圈子列表
	private static Object getGroupMeList(JSONObject jsonObject) {
		ArrayList<GroupModel> modelList = new ArrayList<GroupModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				GroupModel model = new GroupModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setDesc(obj.optString("desc", ""));
				model.setCreateTime(obj.optString("createTime", ""));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 查看我创建的圈子列表
	private static Object getGroupMyList(JSONObject jsonObject) {
		ArrayList<GroupModel> modelList = new ArrayList<GroupModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				GroupModel model = new GroupModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setDesc(obj.optString("desc", ""));
				model.setCreateTime(obj.optString("createTime", ""));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 所有圈子列表
	private static Object getAllGroupList(JSONObject jsonObject) {
		ArrayList<GroupModel> modelList = new ArrayList<GroupModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				GroupModel model = new GroupModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setDesc(obj.optString("desc", ""));
				model.setCreateTime(obj.optString("createTime", ""));

				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}
	
	//获取圈子发言列表
	private static Object getCommentList(JSONObject jsonObject){
		ArrayList<CommentModel> modelList = new ArrayList<CommentModel>();
		
		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0){
			for (int i=0; i<jsonArray.length(); i++){
				CommentModel model = new CommentModel();
				
				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setId(obj.optString("id", ""));
				model.setContent(obj.optString("content", ""));
				model.setAuthorId(obj.optString("authorId", ""));
				model.setAuthorPic(obj.optString("authorPic", ""));
				model.setAuthorName(obj.optString("authorName", ""));
				model.setTime(obj.optString("time", ""));
				
				modelList.add(model);
			}
			map.put("list", modelList);
			return map;
			
		}
		
		return null;
	}
	
	//查看圈子成员
	
	private static Object getParticipantList(JSONObject jsonObject){
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ProfileModel model = new ProfileModel();

				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setFlag("2");
				model.setId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
				model.setProvince(obj.optString("province"));
				model.setCity(obj.optString("city"));
				model.setOrg(obj.optString("org", ""));
				model.setTitle(obj.optString("title", ""));
				model.setPic(obj.optString("pic", ""));
				modelList.add(model);
			}
			map.put("list", modelList);
			return map;

		}

		return null;
	}

	// 加入圈子
	private static int getGroupJoin(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 删除圈子
	private static int getGroupDelete(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 退出圈子
	private static int getGroupQuit(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	// 退出圈子
	private static int getCreateCircle(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}
	
	// 退出圈子
	private static int getPublishComment(JSONObject jsonObject){
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);
		
		return errorCode;
	}
	
	private static Object getTimelineList(JSONObject jsonObject){
		ArrayList<TimelineModel> modelList = new ArrayList<TimelineModel>();

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				TimelineModel timeline = new TimelineModel();

				JSONObject tiemlineObj = (JSONObject) jsonArray.opt(i);
				timeline.setId(tiemlineObj.optString("id", ""));
				timeline.setTitle(tiemlineObj.optString("title", ""));
				timeline.setDescription(tiemlineObj.optString("desc", ""));
				timeline.setStartTime(tiemlineObj.optString("stime", ""));
				timeline.setEndTime(tiemlineObj.optString("etime", ""));
				timeline.setProvince(tiemlineObj.optString("province", ""));
				timeline.setCity(tiemlineObj.optString("city", ""));
				timeline.setDistrict(tiemlineObj.optString("district", ""));
				timeline.setOrg(tiemlineObj.optString("org", ""));
				timeline.setImgUrl(tiemlineObj.optString("pic", ""));

				modelList.add(timeline);
			}

			return modelList;

		}

		return null;
	}

	// 添加时间轴结点
	private static Object timelineNodeCreate(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);
		return errorCode;
	}

	private static Object timelineNodeUpdate(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);
		return errorCode;
	}

	private static Object timelineNodeDelete(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);
		return errorCode;
	}

	// 获取母校信息
	private static Object getCollegeInfo(JSONObject jsonObject) {
		SchoolModel school = new SchoolModel();
		school.setmId(jsonObject.optString("id", ""));
		school.setmLogoUrl(jsonObject.optString("logoURL", ""));
		school.setmName(jsonObject.optString("name", ""));
		school.setmDesc(jsonObject.optString("introduct", ""));
		school.setmWeiboScreenName(jsonObject.optString("sinaWeibo", ""));

		return school;
	}

	// ////////////////////////////////////////////////////////////////////////////

	private static HashMap<String, String> JSONObject2Map(JSONObject jsonObject) {
		HashMap<String, String> responseMap = new HashMap<String, String>();

		try {
			@SuppressWarnings("unchecked")
			Iterator<String> keys = jsonObject.keys();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				responseMap.put(key, jsonObject.getString(key));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return responseMap;
	}

	private static String getErrorMsg(int errorCode) {
		if (errorCode == ErrorCode.SYSTEM_MAINTENANCE) {
			return "服务器维护中，请稍候再试。";
		} else if (errorCode == ErrorCode.PARAM_ERROR) {
			return "参数异常，请重试。";
		} else if (errorCode == ErrorCode.PERMISSION_ERROR) {
			return "权限不足，请与管理员联系。";
		} else if (errorCode == ErrorCode.SESSIONID_ERROR) {
			return "会话超时，请重新登录。";
		} else if (errorCode == ErrorCode.CLIENTID_ERROR) {
			return "无效的终端类型。";
		} else if (errorCode == ErrorCode.VERSION_ERROR) {
			return "协议版本异常，请重试。";
		} else if (errorCode == ErrorCode.CACHE_ERROR) {
			return "服务器缓存异常，请重试。";
		} else if (errorCode == ErrorCode.DATABASE_ERROR) {
			return "服务器数据库异常，请重试。";
		} else if (errorCode == ErrorCode.UNKNOWN) {
			return "未知错误。";
		} else {
			return "未定义错误";
		}
	}

}
