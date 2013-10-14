package com.hmd.client;

public interface HttpRequestType {
	
	// HTTP REQUEST TYPE
	public static final String HTTP_LOGIN 								= "LOGIN";
	public static final String HTTP_REGISTER							= "REGISTER";
	public static final String HTTP_LOGOUT								= "LOGOUT";
	
	public static final String HTTP_PROFILE_BASIC						= "PROFILE_BASIC";
	public static final String HTTP_PROFILE_ALL							= "PROFILE_ALL";
	public static final String HTTP_PROFILE_UPDATE						= "PROFILE_UPDATE";
	
	public static final String HTTP_TIMELINE_LIST						= "TIMELINE_LIST";
	public static final String HTTP_TIMELINE_NODE_CREATE				= "TIMELINE_NODE_CREATE";
	public static final String HTTP_TIMELINE_NODE_UPDATE				= "TIMELINE_NODE_UPDATE";
	public static final String HTTP_TIMELINE_NODE_DELETE				= "TIMELINE_NODE_DELETE";
	
	public static final String HTTP_COLLEGE_INTRODUCT					= "COLLEGE_INTRODUCT";
	public static final String HTTP_COLLEGE_BROADCAST_LIST				= "COLLEGE_BROADCAST_LIST";
	public static final String HTTP_COLLEGE_BROADCAST_DETAIL			= "COLLEGE_BROADCAST_DETAIL";
	
	public static final String HTTP_COLLEGE_EVENT_TYPE_LIST				= "COLLEGE_EVENT_TYPE_LIST";
	public static final String HTTP_COLLEGE_EVENT_LIST					= "COLLEGE_EVENT_LIST";
	public static final String HTTP_COLLEGE_EVENT_DETAIL				= "COLLEGE_EVENT_DETAIL";
	public static final String HTTP_COLLEGE_EVENT_PARTICIPANT_LIST		= "COLLEGE_EVENT_PARTICIPANT_LIST";
	public static final String HTTP_COLLEGE_EVENT_FOLLOW				= "COLLEGE_EVENT_FOLLOW";
	public static final String HTTP_COLLEGE_EVENT_UNFOLLOW				= "COLLEGE_EVENT_UNFOLLOW";
	public static final String HTTP_COLLEGE_EVENT_JOIN					= "COLLEGE_EVENT_JOIN";
	public static final String HTTP_COLLEGE_EVENT_QUIT					= "COLLEGE_EVENT_QUIT";
	
	public static final String HTTP_COLLEGE_FEEDBACK_LIST				= "COLLEGE_FEEDBACK_LIST";
	public static final String HTTP_COLLEGE_FEEDBACK_CREATE				= "COLLEGE_FEEDBACK_CREATE";
	public static final String HTTP_COLLEGE_ALUCARD_APPLY				= "COLLEGE_ALUCARD_APPLY";
	public static final String HTTP_COLLEGE_ALUCARD_STATUS				= "COLLEGE_ALUCARD_STATUS";
	public static final String HTTP_SUGGESTPEOPLE_LIST					= "SUGGESTPEOPLE_LIST";
	public static final String HTTP_MYATTENTIONS_LIST					= "MYATTENTIONS_LIST";
	public static final String HTTP_FANS_LIST							= "FANS_LIST";
	public static final String HTTP_ADDATTENTION						= "ADD_ATTENTION";
	public static final String HTTP_CANCELATTENTION						= "CANCELATTENTION";
	
	
}
