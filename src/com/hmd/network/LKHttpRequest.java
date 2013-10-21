package com.hmd.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.hmd.R;
import com.hmd.activity.BaseActivity;
import com.hmd.client.ApplicationEnvironment;
import com.hmd.client.Constants;
import com.hmd.model.HttpRequestModel;
import com.loopj.android.http.AsyncHttpClient;

public class LKHttpRequest {
	
	private int tag;
	private String requestId;
	private String[] httpRequestParms;
	private HashMap<String, String> requestDataMap;
	private LKAsyncHttpResponseHandler responseHandler;
	private AsyncHttpClient client;
	private LKHttpRequestQueue queue;
	private HttpRequestModel httpRequestModel;
	
	private static ArrayList<HttpRequestModel> httpRequestTypeList = null;
	
	public LKHttpRequest(String requestId, HashMap<String, String> requestDataMap, LKAsyncHttpResponseHandler handler,String... params){
		this.requestId = requestId;
		this.httpRequestParms = params;
		this.requestDataMap = (requestDataMap==null?new HashMap<String, String>():requestDataMap);
		this.responseHandler = handler;
		this.httpRequestModel = getHttpRequestModel(requestId);
		client = new AsyncHttpClient();
		this.responseHandler.setRequest(this);
	}
	
	public String getRequestURL(){
		String hostTemp = Constants.HOSTNAME + this.httpRequestModel.getUrl();
		for (String tempStr : this.httpRequestParms) {
			hostTemp = hostTemp.replaceFirst("\\$\\{\\w*\\}", tempStr);
		}
		// 添加公共参数
		StringBuffer hostStringBuffer = new StringBuffer(hostTemp);
		hostStringBuffer.append("?");
		hostStringBuffer.append("v=").append(Constants.VERSION);
		hostStringBuffer.append("&").append("cid=").append(Constants.CLIENT_ID);
		hostStringBuffer.append("&").append("sid=").append(Constants.SESSION_ID);
		
		return hostStringBuffer.toString();
		
	}
	
	public int getTag(){
		return tag;
	}
	
	public void setTag(int tag){
		this.tag = tag;
	}
	
	public String getRequestId(){
		return requestId;
	}
	
	public void setRequestId(String requestId){
		this.requestId = requestId;
	}
	
	public LKHttpRequestQueue getRequestQueue(){
		return this.queue;
	}
	
	public void setRequestQueue(LKHttpRequestQueue queue){
		this.queue = queue;
	}
	
	public HashMap<String, String> getRequestDataMap() {
		return requestDataMap;
	}
	
	public LKAsyncHttpResponseHandler getResponseHandler() {
		return responseHandler;
	}

	public AsyncHttpClient getClient() {
		return client;
	}
	
	public void send(){
		if (this.httpRequestModel.getMethod().equalsIgnoreCase("POST")){ // POST
			this.client.post(ApplicationEnvironment.getInstance().getApplication(), this.getRequestURL(), this.getHttpPostEntity(this.requestDataMap), this.httpRequestModel.getContentType(), this.responseHandler);
			
		} else { // GET
			this.client.get(ApplicationEnvironment.getInstance().getApplication(), this.getHttpGetEntity(this.requestDataMap), this.responseHandler);
		}
	}
	
	private String getHttpGetEntity(HashMap<String, String> map){
		StringBuffer requestURL = new StringBuffer(this.getRequestURL());
		requestURL.append("&");
		for (String key : map.keySet()){
			requestURL.append(key).append("=").append(map.get(key)).append("&");
		}
		requestURL.deleteCharAt(requestURL.length()-1);
		
		Log.e("request body:", requestURL.toString());
		
		return requestURL.toString();
	}
	
	private HttpEntity getHttpPostEntity(HashMap<String, String> map){
		try{
			JSONObject jsonObject = new JSONObject();
			for (String key : map.keySet()) {
				jsonObject.put(key, map.get(key));
			}
			
			Log.e("request body:", jsonObject.toString());
			
			return new StringEntity(jsonObject.toString());
			
		} catch(JSONException e){
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	private HttpRequestModel getHttpRequestModel(String requstId){
		if (null == httpRequestTypeList) {
			httpRequestTypeList = this.parseHttpRequestXML();
		}
		
		for (HttpRequestModel model : httpRequestTypeList){
			if (model.toString().equalsIgnoreCase(requstId)){
				return model;
			}
		}
		
		Log.e("LKHTTPREQUEST", "http_request_param.xml 有误！请检查配置文件");
		
		return null;
	}
	
	private ArrayList<HttpRequestModel> parseHttpRequestXML(){
		httpRequestTypeList = new ArrayList<HttpRequestModel>();
		
		HttpRequestModel model = null;
		
		try{
	        InputStream inputStream = BaseActivity.getTopActivity().getResources().openRawResource(R.raw.http_request_param); 
	        XmlPullParser parser = Xml.newPullParser();  
	        parser.setInput(inputStream, "UTF-8");  
	          
	        int event = parser.getEventType();  
	        while(event!=XmlPullParser.END_DOCUMENT){  
	            switch(event){  
	            case XmlPullParser.START_TAG:  
	                if("request".equals(parser.getName())){
	                    model = new HttpRequestModel();  
	                }  
	                if(model!=null){
	                	if("requestId".equals(parser.getName())){  
	                        model.setRequestId(parser.nextText());  
	                    }else if("url".equals(parser.getName())){  
	                        model.setUrl(parser.nextText());  
	                    }else if("method".equals(parser.getName())){  
	                        model.setMethod(parser.nextText());  
	                    } 
	                }  
	                break;
	                
	            case XmlPullParser.END_TAG:  
	                if("request".equals(parser.getName())){
	                	httpRequestTypeList.add(model);
	                }  
	                break;  
	            }  
	            event = parser.next();  
	        }
	        
		}catch(IOException e){
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		}
		
		return httpRequestTypeList;
	}
	
}
