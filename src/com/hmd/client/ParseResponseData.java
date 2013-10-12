package com.hmd.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.hmd.enums.ErrorCode;
import com.hmd.exception.ServiceErrorException;
import com.hmd.model.AnnouncementModel;
import com.hmd.model.ProfileModel;
import com.hmd.model.SchoolModel;
import com.hmd.model.TimelineModel;
import com.hmd.util.ImageUtil;

public class ParseResponseData {
	
	public static Object parse(String type, JSONObject jsonObject) throws ServiceErrorException {
		int errorCode = jsonObject.optInt("ec", ErrorCode.UNKNOWN);
		
		if (errorCode != ErrorCode.SUCCESS){
			throw new ServiceErrorException(getErrorMsg(errorCode)+"[ERROR_CODE:"+errorCode+"]");
		}
		
		if (type.equalsIgnoreCase(HttpRequestType.HTTP_LOGIN)){
			return login(jsonObject);
			
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_LOGOUT)) {
			return logout(jsonObject);
			
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_BASIC)) {
			return getProfileBasic(jsonObject);
			
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_ALL)) {
			return getProfileAll(jsonObject);

		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_PROFILE_UPDATE)) {
			return profileUpdate(jsonObject);
			
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_BROADCAST_LIST)) {
			return getBroadcastList(jsonObject);
			
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_COLLEGE_BROADCAST_DETAIL)) {
			return getBroadcastDetail(jsonObject);
					
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
			
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_SUGGESTPEOPLE_LIST)) {
			return getSuggestPeopleList(jsonObject);
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_MYATTENTIONS_LIST)) {
			return getMyAttentionsList(jsonObject);
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_FANS_LIST)) {
			return getFansList(jsonObject);
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_ADDATTENTION)) {
			return getAddAttention(jsonObject);
		} else if (type.equalsIgnoreCase(HttpRequestType.HTTP_CANCELATTENTION)) {
			return getCancelAttention(jsonObject);
		}
		
		return null;
	}
	
	/*
	 * 登录
	 */
	private static Object login(JSONObject jsonObject){
		
		return JSONObject2Map(jsonObject);
	}
	
	/*
	 * 登出
	 */
	private static Object logout(JSONObject jsonObject){
		
		return JSONObject2Map(jsonObject);
	}
		
	/*
	 * 查看个人基本信息
	 * 
	 * {"basic":{"adYear":2000,"colg":"首都师范大学","name":"刘斌","gender":0,"dept":"未知","gradYear":2004,"major":"小学教育"},"rc":1,"ec":1}
	 */
	private static Object getProfileBasic(JSONObject jsonObject){
		try{
			JSONTokener parse = new JSONTokener(jsonObject.optString("basic"));
			JSONObject basicObj = (JSONObject) parse.nextValue();
			
			if (null != basicObj){
				ProfileModel model = new ProfileModel();
				model.setName(basicObj.optString("name", ""));
				model.setGender(basicObj.optInt("gender"));
				model.setSchool(basicObj.optString("colg", ""));
				model.setDept(basicObj.optString("dept", ""));
				model.setMajor(basicObj.optString("major", ""));
				model.setAdYear(basicObj.optString("adyear", ""));
				model.setGradYear(basicObj.optString("gradYear", ""));
				
				return model;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return null;
	}
	
	private static Object getProfileAll(JSONObject jsonObject){
		try{
			JSONTokener parse = new JSONTokener(jsonObject.optString("basic"));
			JSONObject basicObj = (JSONObject) parse.nextValue();
			
			if (null != basicObj){
				// TODO 因为返回的数据不全，有些字段不能够多确定，待完善
				ProfileModel model = new ProfileModel();
				model.setName(basicObj.optString("name", null));
				model.setGender(basicObj.optInt("gender"));
				model.setSchool(basicObj.optString("colg", null));
				model.setDept(basicObj.optString("dept", null));
				model.setMajor(basicObj.optString("major", null));
				model.setAdYear(basicObj.optString("adyear", null));
				model.setGradYear(basicObj.optString("gradYear", null));

				model.setBirthday(basicObj.optString("birthday", null));
				model.setBirthplace(basicObj.optString("birthplace", null));
				model.setNation(basicObj.optString("nation", null));
				model.setDesc(basicObj.optString("desc", null));
				
				return model;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	private static Object profileUpdate(JSONObject jsonObject){
		
		return null;
	}
	
	// 取得公告列表
	private static Object getBroadcastList(JSONObject jsonObject){
		HashMap<String, Object> map = new HashMap<String, Object>();
		ArrayList<AnnouncementModel> modelList = new ArrayList<AnnouncementModel>();
		
		int total = jsonObject.optInt("total", 0);
		map.put("total", total);
		
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0){
			for (int i=0; i<jsonArray.length(); i++){
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
	
	public static Object getBroadcastDetail(JSONObject jsonObject){
		AnnouncementModel model = new AnnouncementModel();
		model.setTitle(jsonObject.optString("title", ""));
		model.setContent(jsonObject.optString("content", ""));
		model.setTime(jsonObject.optString("time", ""));
		
		return model;
	}
	
	//根据选中履历推荐好友列表
	private static Object getSuggestPeopleList(JSONObject jsonObject){
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();
		
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0){
			for (int i=0; i<jsonArray.length(); i++){
				JSONObject obj = (JSONObject) jsonArray.opt(i);
				
				ProfileModel model = new ProfileModel();
				model.setId(obj.optString("id", ""));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
				
				modelList.add(model);
			}
			
			return modelList;
			
		}
		
		return null;
	}
	
	//我关注的好友列表
	private static Object getMyAttentionsList(JSONObject jsonObject){
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();
		
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0){
			for (int i=0; i<jsonArray.length(); i++){
				ProfileModel model = new ProfileModel();
				
				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setFlag("1");
				model.setId(obj.optString("id", ""));
				model.setTime(obj.optString("time", ""));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
				
				modelList.add(model);
			}
			
			return modelList;
			
		}
		
		return null;
	}
	//关注我人列表
	private static Object getFansList(JSONObject jsonObject){
		ArrayList<ProfileModel> modelList = new ArrayList<ProfileModel>();
		
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0){
			for (int i=0; i<jsonArray.length(); i++){
				ProfileModel model = new ProfileModel();
				
				JSONObject obj = (JSONObject) jsonArray.opt(i);
				model.setFlag("0");
				model.setId(obj.optString("id", ""));
				model.setTime(obj.optString("time", ""));
				model.setName(obj.optString("name", ""));
				model.setGender(obj.optInt("gender", 1));
				
				modelList.add(model);
			}
			
			return modelList;
			
		}
		
		return null;
	}
	
	// 加关注
	private static int getAddAttention(JSONObject jsonObject){
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);
		
		return errorCode;
	}
	
	// 取消关注
	private static int getCancelAttention(JSONObject jsonObject){
		int errorCode = jsonObject.optInt("rc", ErrorCode.UNKNOWN);
		
		return errorCode;
	}
	
	private static Object getTimelineList(JSONObject jsonObject){
		ArrayList<TimelineModel> modelList = new ArrayList<TimelineModel>();
		
		JSONArray jsonArray = jsonObject.optJSONArray("list");
		if (jsonArray != null && jsonArray.length() > 0){
			for (int i=0; i<jsonArray.length(); i++){
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
				timeline.setSchool(tiemlineObj.optString("org", ""));
				timeline.setImgUrl(ImageUtil.getTestImageURL());
				
				modelList.add(timeline);
			}
			
			return modelList;
			
		}
		
		return null;
	}
	
	private static Object timelineNodeCreate(JSONObject jsonObject){
		
		return null;
	}
	
	private static Object timelineNodeUpdate(JSONObject jsonObject){
		
		return null;
	}
	
	private static Object timelineNodeDelete(JSONObject jsonObject){
		
		return null;
	}
	
	// 获取母校信息
	private static Object getCollegeInfo(JSONObject jsonObject){
		SchoolModel school = new SchoolModel();
		school.setmId(jsonObject.optString("id", ""));
		school.setmLogoUrl(jsonObject.optString("logoURL", ""));
		school.setmName(jsonObject.optString("name", ""));
		school.setmDesc(jsonObject.optString("introduct", ""));
		
		return school;
	}
	
	//////////////////////////////////////////////////////////////////////////////
	
	private static HashMap<String, String> JSONObject2Map(JSONObject jsonObject){
		HashMap<String, String> responseMap = new HashMap<String, String>();
		
		try{
			@SuppressWarnings("unchecked")
			Iterator<String> keys = jsonObject.keys();
			while (keys.hasNext()){
				String key = (String)keys.next();
				responseMap.put(key, jsonObject.getString(key));
			}
		} catch(Exception e){
			e.printStackTrace();
		}
		
		return responseMap;
	}
	
	private static String getErrorMsg(int errorCode){
		if (errorCode == ErrorCode.SYSTEM_MAINTENANCE){
			return "服务器维护中，请稍候再试。";
		} else if(errorCode == ErrorCode.PARAM_ERROR){
			return "参数异常，请重试。";
		} else if(errorCode == ErrorCode.PERMISSION_ERROR){
			return "权限不足，请与管理员联系。";
		} else if(errorCode == ErrorCode.SESSIONID_ERROR) {
			return "会话超时，请重新登录。";
		} else if(errorCode == ErrorCode.CLIENTID_ERROR) {
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
