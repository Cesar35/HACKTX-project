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

public class ebaylistings {

	private Resources resources;
	private static String appID;
	private static String ebayURL;
	int i;
	
	public ebaylistings(Context context){
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
	
	private String getRequestURL(int pageNum, int categoryNum){
		CharSequence requestURL=TextUtils.expandTemplate(this.resources.getString(R.string.ebay_request_find_items_by_category),ebayURL,appID);
		return(requestURL.toString());
	}
	
	public String getListings(int pageNum, int categoryNum) throws Exception{
		String result=null;
		HttpClient httpClient=new DefaultHttpClient();  
		HttpGet httpGet=new HttpGet(this.getRequestURL(pageNum, categoryNum));  
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
