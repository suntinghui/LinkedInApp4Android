package com.hmd.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

import com.hmd.enums.ErrorCode;
import com.hmd.exception.ServiceErrorException;
import com.hmd.model.ActiveModel;
import com.hmd.model.AnnouncementModel;
import com.hmd.model.CommentModel;
import com.hmd.model.DeptModel;
import com.hmd.model.GroupModel;
import com.hmd.model.ImageModel;
import com.hmd.model.IndustryModel;
import com.hmd.model.MajorModel;
import com.hmd.model.MediaModel;
import com.hmd.model.OrgOneModel;
import com.hmd.model.OrgTwoModel;
import com.hmd.model.ProfileModel;
import com.hmd.model.SchoolModel;
import com.hmd.model.TimelineModel;
import com.hmd.util.ImageUtil;

public class ParseResponseData {

	public static Object parse(String type, JSONObject jsonObject) throws ServiceErrorException {
		int errorCode = jsonObject.optInt("ec", ErrorCode.UNKNOWN);

		if (errorCode != ErrorCode.SUCCESS) {
			throw new ServiceErrorException(errorCode, getErrorMsg(errorCode) + "[ERROR_CODE:" + errorCode + "]");
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

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_DETAIL)) {
			return getProfileDetail(jsonObject);

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

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_CARD_APPLY)) {
			return collegeCardApply(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_CARD_STATUS)) {
			return collegeCardStatus(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_FEEDBACK_APPLY)) {
			return collegeFeedbackApply(jsonObject);

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

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FRIENDS_SEARCH)) {
			return getSearchList(jsonObject);

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

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_MEDIA_TOPLIST)) {
			return getMediaTopList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_MEDIA_LIST)) {
			return getMediaList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_MEDIA_DETAIL)) {
			return getMediaDetail(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_CONFIG_DEPT_LIST)) {
			return getConfigDeptList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_CONFIG_ORG_LIST)) {
			return getConfigOrgList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_ME_CREATE)) {
			return getProfileMeCreateList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_CONFIG_INDUSTRY_LIST)) {
			return getConfigIndustryList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GALARY_LIST)) {
			return getGalaryList(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GALARY_DETAIL)) {
			return getGalaryDetail(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_ME_CONFIG)) {
			return getProfileMeConfig(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_ME_UPDATE)) {
			return getProfileMeUpdate(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_GOURPKICK)) {
			return getDeleteMember(jsonObject);

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
	 * {"idCardNo":null,"adYear":2009,"pic":"http://115.47.56.228:8080/alumni/pic/391/983/67952e3bda626f2f37a766b8d8fedcd7","type":1,"rc":1,"ec":1,"majorId":null, "email":null,"name":"王之稚","gender":0,"deptId":null,"qq":null,"mobile":null}
	 */
	private static Object getProfileDetail(JSONObject jsonObject) {
		try {
			if (null != jsonObject) {
				ProfileModel model = new ProfileModel();
				model.setIdCardNo(jsonObject.optString("idCardNo", ""));
				model.setName(jsonObject.optString("name", ""));
				model.setGender(jsonObject.optInt("gender"));
				model.setMajorId(jsonObject.optString("majorId", ""));
				model.setAdYear(jsonObject.optString("adYear", ""));
				model.setPic(jsonObject.optString("pic", ""));
				model.setType(jsonObject.optInt("type", 1));
				model.setClassName(jsonObject.optString("clazz", ""));
				model.setDeptId(jsonObject.optString("deptId", ""));
				model.setMajorId(jsonObject.optString("majorId", ""));

				model.setOrg1Id(jsonObject.optString("org2Id"));
				model.setOrg2Id(jsonObject.optString("org2Id", ""));
				model.setEmpNo(jsonObject.optString("empNo", ""));
				model.setEmail(jsonObject.optString("email", ""));
				model.setDeptId(jsonObject.optString("deptId", ""));
				model.setQq(jsonObject.optString("qq", ""));
				model.setMobile(jsonObject.optString("mobile", ""));

				return model;
			}

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
		model.setPosterImage(jsonObject.optString("pic", ""));

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
				model.setmId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 0));
				model.setProvince(obj.optString("province", ""));
				model.setCity(obj.optString("city", ""));
				model.setOrg(obj.optString("org", ""));
				model.setTitle(obj.optString("title", ""));
				model.setPic(obj.optString("pic", ""));

				modelList.add(model);
			}
		}

		map.put("list", modelList);

		return map;
	}

	private static Object collegeCardApply(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", 0);
		return errorCode;
	}

	private static Object collegeCardStatus(JSONObject jsonObject) {
		int status = jsonObject.optInt("status", -1);
		return status;
	}

	private static Object collegeFeedbackApply(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", 0);
		return errorCode;
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
				model.setmFlag("2");
				model.setmId(obj.optString("id", ""));
				model.setCity(obj.optString("city"));
				model.setProvince(obj.optString("province"));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
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
				model.setmFlag("1");
				model.setmId(obj.optString("id", ""));
				model.setCity(obj.optString("city"));
				model.setProvince(obj.optString("province"));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
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
				model.setmFlag("0");
				model.setmId(obj.optString("id", ""));
				model.setCity(obj.optString("city"));
				model.setProvince(obj.optString("province"));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
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

	// 获取圈子发言列表
	private static Object getCommentList(JSONObject jsonObject) {
		ArrayList<CommentModel> modelList = new ArrayList<CommentModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
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

	// 找同学
	private static Object getSearchList(JSONObject jsonObject) {
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
				model.setmFlag("2");
				model.setmId(obj.optString("id", ""));
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

	// 查看圈子成员
	private static Object getParticipantList(JSONObject jsonObject) {
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
				model.setmFlag("2");
				model.setmId(obj.optString("id", ""));
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
	private static int getPublishComment(JSONObject jsonObject) {
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);

		return errorCode;
	}

	private static Object getTimelineList(JSONObject jsonObject) {
		ArrayList<TimelineModel> modelList = new ArrayList<TimelineModel>();

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				TimelineModel timeline = new TimelineModel();

				JSONObject tiemlineObj = (JSONObject) jsonArray.opt(i);
				timeline.setId(tiemlineObj.optString("id", ""));
				timeline.setTitle(tiemlineObj.optString("title", ""));
				timeline.setDescription(tiemlineObj.optString("desc", ""));
				timeline.setIndustry(tiemlineObj.optString("industryId", ""));
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
		
		// 本来需要后台传递过来
		//school.setmDesc(jsonObject.optString("introduct", ""));
		school.setmDesc("         首都师范大学创建于1954年，是一所包括文、理、工、管、法、教育、外语、艺术等专业的综合性师范大学，是教育部、北京市“省部共建大学”，是北京市重点投入建设，进入“211工程”的北京市属重点大学。建校60年来，共培养各类高级专门人才十余万名，是为北京市基础教育输送合格师资和培养其他现代化建设所需人才的重要基地。\n\n"
				+ "         学校现有博士学位授权一级学科17个，博士点97个，博士后流动站16个，硕士学位授权一级学科26个，硕士点147个。国家重点学科4个，国家重点培育学科1个，北京市一级重点学科8个，北京市二级重点学科6个，北京市一级重点建设学科1个，北京市二级重点建设学科11个，北京市一级重点培育学科4个，交叉学科北京市重点学科2个；1个省部共建国家重点实验室培育基地，2个教育部重点实验室，1个教育部省属高校人文社会科学重点研究基地，1个教育部工程研究中心，1个民政部重点实验室，1个国家级实验教学示范中心，2个国家人才培养和科学研究基地，1个国家国际科技合作基地，4个省、部级 设置的研究（院、所、中心）、实验室，8个北京市重点实验室，2个北京市高等学校工程研究中心，1个北京市工程技术研究中心，1个北京市工程实验室，2个北京市人才培养和科学研究基地，3个北京高等学校市级校外人才培养基地，6个北京市实验教学示范中心。\n\n"
				+ "         学校下设文学院、历史学院、政法学院、教育学院、外国语学院、管理学院、音乐学院、美术学院、数学科学学院、物理系、化学系、生命科学学院、资源环境与旅游学院、信息工程学院、教育技术系、初等教育学院、学前教育学院、燕都学院、良乡校区基础学部、继续教育学院、国际文化学院、京疆学院、中国书法文化研究院、文化研究院、马克思主义教育学院等25个院系以及大学英语教研部、体育教研部。共有专科专业1个，本科专业55个。各类学生总数31,086人。其中，全日制专科生467人，本科生10,763人，硕士研究生5,934人，博士研究生577人，成人教育学生11,993人，外国留学生1352人，已形成从专科生到本科生、硕士生、博士生及博士后，从全日制到成人教育、留学生教育全方位、多层次的办学格局和教育体系。\n\n"
				+ "         学校现有教职工2,606人，在1,515名专任教师中正高职称人数299人，副高职称人数553人，博士854人，硕士502人，拥有硕士及以上学位教师占专任教师总数的89.5%。目前学校已拥有一批在国内外有一定影响的专家、学者。在校工作的中国科学院、工程院院士6人，俄罗斯工程院院士1人，俄罗斯自然科学院院士1人，国务院学位委员会学科评议组成员3人，国家教育部学科教学指导委员会委员13人，入选国家“千人计划”项目4人，万人计划第一批百千万工程领军人才1人，教育部长江学者计划特聘教授6人，教育部长江学者计划讲座教授1人，国家杰出青年基金资助者8人，国家级百千万人才9人，入选北京市海外人才聚集工程11人，北京市特聘教授40人，北京市高层次人才资助计划10人，北京市拔尖创新人才32人，北京市创新团队建设计划31个，北京市科技新星34人，市级青年骨干教师176人，青年拔尖人才培育计划入选人员56人，长城学者培养计划入选人员7人，教育部创新团队3个。学校另有60名教师荣获曾宪梓高师教师奖、霍英东青年教师教学奖和科研奖。 \n\n"
				+ "         学校在2003年接受教育部本科教学工作水平评估中，被评为“本科教学工作优秀学校”。截至目前，学校共有国家级特色专业7个，北京市级特色专业10个；国家级教学团队6个，北京市级教学团队8个；国家级人才培养模式创新实验区3个；市级人才培养模式创新试验区1个；国家级精品课程12门、北京市级精品课程29门；国家级双语教学示范课程 1门；教育部“十一五”规划教材20部、国家级精品教材5部、北京高等教育经典教材1部、北京高等教育精品教材57部，“十二五”普通高等教育本科国家级规划教材7部；国家精品视频公开课1门，国家级精品资源共享课6门，教师教育国家级精品资源共享课8门；学校现有国家级教学名师2人、北京市级教学名师18人；2008年我校成为“国家大学生创新性实验计划”校；在优秀教学成果评选中，学校获得国家级优秀教学成果一等奖2项、二等奖13项（含合作项目），北京市优秀教学成果奖96项。学生在挑战杯、数学建模与计算机应用竞赛、电子竞赛、英语演讲等国家级及北京市级比赛中均获得了多项奖励。高层次人才培养效果显著，获得全国优秀博士学位论文奖2项，全国优秀博士学位论文提名奖8项，北京市优秀博士学位论文奖9项。\n\n"
				+ "         学校设有90余个研究所（中心），建有北京市大学科技园。“十一五”以来，获批“科技支撑计划“课题及子课题15项，获批省部级以上科研项目1269项，其中国家自然科学基金项目364项，国家社科基金项目143项，国家社科基金重大项目9项，“863”计划项目及子课题22项，“973”计划合作项目15项。获国家科技进步奖一等奖1项（第二单位）、北京市科学进步奖13项，省部级以上奖励91项。2013至2014学年度科研项目总经费7,655万元。编辑出版有《首都师范大学学报》（社会科学版和自然科学版）、《语文导报》、《中学语文教学》、《教育艺术》等。\n\n"
				+ "         学校占地约88万平方米，建筑总面积约77万平方米。学校教学、科研条件优良，教学科研用仪器设备总资产95,700余万元。校图书馆收藏各类图书文献561万册（件），馆藏基础雄厚，是全国文献资料骨干馆之一。学校建有数字校园建设中心，稳定、完善、高效的校园网络已全面开通。此外，还建有国家级标准塑胶运动场、体育馆、羽毛球馆、游泳池等体育运动场地。\n\n"
				+ "         学校积极开展国际文化交流活动。目前已同32个国家和地区的194所大学建立了校际交流合作关系，同俄罗斯圣彼得堡大学、美国明尼苏达大学、意大利威尼斯大学、秘鲁皮乌拉大学、纽约州立布法罗大学和德国不来梅科技大学等6所国外院校合作开办了孔子学院。同澳大利亚福林德斯大学联合培养了477名教育硕士。学校是国家教育部批准的可以接受外国留学生和港、澳、台学生的院校之一，是市属高校中唯一一个教育部授予的来华留学示范基地，也是教育部“中国政府奖学金本科来华留学生预科教育基地”。\n\n"
				+ "         首都师大附中、首都师大附属育新学校等附属学校是学校教育教学改革实验基地，首都师大附中是北京市首批市级重点中学和示范性高中校。\n\n"
				+ "         学校历来重视党的建设和思想政治工作，近年来，先后被中组部、中宣部、教育部和北京市委、市政府评为全国和北京市“党的建设和思想政治工作先进高等学校”，并多次获“首都精神文明建设先进单位”、“北京市思想政治工作先进集体”等荣誉称号。\n\n"
				+ "         未来，首都师范大学将锐意创新，开拓进取，为把学校建设成为有特色、高水平的师范大学而努力奋斗。\n\n");
		
		school.setmWeiboScreenName(jsonObject.optString("sinaWeibo", ""));

		return school;
	}

	private static Object getMediaTopList(JSONObject jsonObject) {
		ArrayList<MediaModel> mediaList = new ArrayList<MediaModel>();

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				MediaModel media = new MediaModel();

				JSONObject mediaObj = (JSONObject) jsonArray.opt(i);
				media.setId(mediaObj.optString("id", ""));
				media.setTitle(mediaObj.optString("title", ""));
				media.setTime(mediaObj.optString("time", ""));
				// media.setPic(ImageUtil.getTestImageURL1());
				media.setPic(mediaObj.optString("pic", ""));

				mediaList.add(media);
			}

		}

		return mediaList;
	}

	private static Object getMediaList(JSONObject jsonObject) {
		ArrayList<MediaModel> mediaList = new ArrayList<MediaModel>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		String total = jsonObject.optString("total", "0");
		map.put("total", total);
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				MediaModel media = new MediaModel();

				JSONObject mediaObj = (JSONObject) jsonArray.opt(i);
				media.setId(mediaObj.optString("id", ""));
				media.setTitle(mediaObj.optString("title", ""));
				media.setTime(mediaObj.optString("time", ""));
				media.setPreview(mediaObj.optString("preview", ""));
				// media.setPic(ImageUtil.getTestImageURL1());
				media.setPic(mediaObj.optString("pic", ""));

				// 如果没有图片，则忽略掉该消息。
				mediaList.add(media);

			}

		}

		map.put("list", mediaList);

		return map;
	}

	private static Object getGalaryDetail(JSONObject jsonObject) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("time", jsonObject.optString("time"));
		map.put("pic", jsonObject.optString("pic"));
		map.put("thumbnail", jsonObject.optString("thumbnail"));

		return map;
	}

	private static Object getProfileMeConfig(JSONObject jsonObject) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("email", jsonObject.optString("email"));
		map.put("qq", jsonObject.optString("qq"));
		map.put("mobile", jsonObject.optString("mobile"));

		return map;
	}

	private static Object getProfileMeUpdate(JSONObject jsonObject) {

		return jsonObject.optString("rc");
	}

	private static Object getDeleteMember(JSONObject jsonObject) {

		return jsonObject.optString("rc");
	}

	private static Object getMediaDetail(JSONObject jsonObject) {
		MediaModel media = new MediaModel();
		media.setType(jsonObject.optInt("type", 1));
		media.setTitle(jsonObject.optString("title", ""));
		media.setTime(jsonObject.optString("time", ""));
		media.setContent(jsonObject.optString("content", ""));

		ArrayList<String> picList = new ArrayList<String>();
		JSONArray jsonArray = jsonObject.optJSONArray("pics");
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject picsObj = (JSONObject) jsonArray.opt(i);
				picList.add(picsObj.optString("url", ""));
			}
			media.setPics(picList);
		}

		// media.setPics(ImageUtil.getTestImageURL2());

		return media;
	}

	private static Object getConfigOrgList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		ArrayList<OrgOneModel> orgOneList = new ArrayList<OrgOneModel>();
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				OrgOneModel orgOneModel = new OrgOneModel();

				JSONObject deptObj = (JSONObject) jsonArray.opt(i);
				orgOneModel.setCode(Integer.valueOf(deptObj.optString("id", "")));
				orgOneModel.setName(deptObj.optString("display", ""));

				JSONArray majorArray = deptObj.optJSONArray("list");
				ArrayList<OrgTwoModel> majorList = new ArrayList<OrgTwoModel>();
				for (int j = 0; j < majorArray.length(); j++) {
					OrgTwoModel majorModel = new OrgTwoModel();
					JSONObject majorObj = (JSONObject) majorArray.opt(j);
					majorModel.setCode(Integer.valueOf(majorObj.optString("id", "")));
					majorModel.setName(majorObj.optString("display", ""));
					majorList.add(majorModel);
				}
				orgOneModel.setOrgTwoModels(majorList);
				orgOneList.add(orgOneModel);

			}

		}

		map.put("list", orgOneList);

		return map;
	}

	private static int getProfileMeCreateList(JSONObject jsonObject) {
		return jsonObject.optInt("rc", ErrorCode.UNKNOWN);
	}

	private static Object getConfigIndustryList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		ArrayList<IndustryModel> list = new ArrayList<IndustryModel>();
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				IndustryModel model = new IndustryModel();

				JSONObject deptObj = (JSONObject) jsonArray.opt(i);
				model.setId(deptObj.optString("id", ""));
				model.setDisplay(deptObj.optString("display", ""));

				list.add(model);

			}

		}

		map.put("list", list);

		return map;
	}

	private static Object getGalaryList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));
		map.put("total", jsonObject.optString("total", "0"));

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		ArrayList<ImageModel> list = new ArrayList<ImageModel>();
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				ImageModel model = new ImageModel();

				JSONObject deptObj = (JSONObject) jsonArray.opt(i);
				model.setId(deptObj.optString("id", ""));
				model.setTitle(deptObj.optString("title", ""));
				model.setTime(deptObj.optString("time", ""));
				model.setAuthor(deptObj.optString("author", ""));
				model.setThumbnail(deptObj.optString("thumbnail", ""));

				list.add(model);

			}

		}

		map.put("list", list);

		return map;
	}

	private static Object getConfigDeptList(JSONObject jsonObject) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("rc", jsonObject.optInt("rc", ErrorCode.UNKNOWN));

		JSONArray jsonArray = jsonObject.optJSONArray("list");
		ArrayList<DeptModel> deptList = new ArrayList<DeptModel>();
		if (jsonArray != null && jsonArray.length() > 0) {
			for (int i = 0; i < jsonArray.length(); i++) {
				DeptModel deptModel = new DeptModel();

				JSONObject deptObj = (JSONObject) jsonArray.opt(i);
				deptModel.setCode(Integer.valueOf(deptObj.optString("id", "")));
				deptModel.setName(deptObj.optString("display", ""));

				JSONArray majorArray = deptObj.optJSONArray("list");
				ArrayList<MajorModel> majorList = new ArrayList<MajorModel>();
				for (int j = 0; j < majorArray.length(); j++) {
					MajorModel majorModel = new MajorModel();
					JSONObject majorObj = (JSONObject) majorArray.opt(j);
					majorModel.setCode(Integer.valueOf(majorObj.optString("id", "")));
					majorModel.setName(majorObj.optString("display", ""));
					majorList.add(majorModel);
				}
				deptModel.setMajors(majorList);
				deptList.add(deptModel);

			}

		}

		map.put("list", deptList);

		return map;
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
