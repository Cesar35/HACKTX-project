package com.huguesjohnson.ebayDemo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.text.TextUtils;

public class ebaycategories {

	private Resources resources;
	private static String appID;
	private static String ebayURL;
	
	public ebaycategories(Context context){
		this.resources=context.getResources();
		if(Build.PRODUCT.toLowerCase().indexOf("sdk")>-1){
			/* 
			the sandbox URLs are pretty useless as they only return a success code but no results
			if you really want to use them then swap out the next two lines
			appID=this.resources.getString(R.string.ebay_appid_sandbox);
			ebayURL=this.resources.getString(R.string.ebay_wsurl_sandbox);
			*/
			appID=this.resources.getString(R.string.ebay_appid_production);
			ebayURL=this.resources.getString(R.string.ebay_open_api);
		}else{
			appID=this.resources.getString(R.string.ebay_appid_production);
			ebayURL=this.resources.getString(R.string.ebay_open_api);
		}
	}
	
	private String getRequestURL(){
		CharSequence requestURL=TextUtils.expandTemplate(this.resources.getString(R.string.ebay_request_categories),ebayURL,appID);
		return(requestURL.toString());
	}
	
	public String getCategories() throws Exception{
		String result=null;
		HttpClient httpClient=new DefaultHttpClient();  
		HttpGet httpGet=new HttpGet(this.getRequestURL());  
		HttpResponse response=httpClient.execute(httpGet);  
		HttpEntity httpEntity=response.getEntity();  
		if(httpEntity!=null){  
			InputStream in=httpEntity.getContent();  
	        BufferedReader reader=new BufferedReader(new InputStreamReader(in));
	        StringBuffer temp=new StringBuffer();
	        String currentLine=null;
	        while((currentLine=reader.readLine())!=null){
	           	temp.append(currentLine);
	        }
	        result=temp.toString();
			in.close();
		}
		return(result);
	}	
}
