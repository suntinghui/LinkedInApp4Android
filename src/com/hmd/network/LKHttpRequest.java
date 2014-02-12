package com.hmd.network;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
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
import com.hmd.util.UnicodeUtil;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class LKHttpRequest {
	
	private int tag;
	private String requestId;
	private String[] httpRequestParms;
	private HashMap<String, Object> requestDataMap;
	private LKAsyncHttpResponseHandler responseHandler;
	private AsyncHttpClient client;
	private LKHttpRequestQueue queue;
	private HttpRequestModel httpRequestModel;
	
	private static ArrayList<HttpRequestModel> httpRequestTypeList = null;
	
	public LKHttpRequest(String requestId, HashMap<String, Object> requestDataMap, LKAsyncHttpResponseHandler handler,String... params){
		this.requestId = requestId;
		this.httpRequestParms = params;
		this.requestDataMap = (requestDataMap==null?new HashMap<String, Object>():requestDataMap);
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
		hostStringBuffer.append("v=").append(Constants.VERSION_ID);
		hostStringBuffer.append("&").append("cid=").append(Constants.CLIENT_ID);
		hostStringBuffer.append("&").append("sid=").append(Constants.SESSION_ID);
		Log.i("url:", hostStringBuffer.toString());
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
	
	public HashMap<String, Object> getRequestDataMap() {
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
			//this.client.post(ApplicationEnvironment.getInstance().getApplication(), this.getRequestURL(), getRequestParams(this.requestDataMap).getEntity(), this.httpRequestModel.getContentType(), this.responseHandler);
			
			//this.client.addHeader("Content-Type", this.httpRequestModel.getContentType());
			//this.client.post(this.getRequestURL(), getRequestParams(this.requestDataMap), this.responseHandler);
		} else { // GET
			this.client.get(ApplicationEnvironment.getInstance().getApplication(), this.getHttpGetEntity(this.requestDataMap), this.responseHandler);
		}
	}
	
	private String getHttpGetEntity(HashMap<String, Object> map){
		StringBuffer requestURL = new StringBuffer(this.getRequestURL());
		requestURL.append("&");
		for (String key : map.keySet()){
			requestURL.append(key).append("=").append(map.get(key)).append("&");
		}
		requestURL.deleteCharAt(requestURL.length()-1);
		
		Log.d("request body:", requestURL.toString());
		
		return requestURL.toString();
	}
	
	private HttpEntity getHttpPostEntity(HashMap<String, Object> map){
		try{
			JSONObject jsonObject = new JSONObject();
			for (String key : map.keySet()) {
				jsonObject.put(key, map.get(key));
			}
			
			Log.d("request body:", jsonObject.toString());
			return new StringEntity(jsonObject.toString(),HTTP.UTF_8);
			
		} catch(JSONException e){
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private RequestParams getRequestParams(HashMap<String, Object> map){
		RequestParams params = new RequestParams();
		try{
			for (String key : map.keySet()){
				Object obj = map.get(key);
				if (obj instanceof String){
					params.put(key, (String)obj);
					
				} else if (obj instanceof File){
					params.put(key, (File)obj);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//Log.d("request body:", params.getEntity().toString());
		
		return params;
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
	                    }else if("contentType".equals(parser.getName())){
	                    	model.setContentType(parser.nextText());
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
