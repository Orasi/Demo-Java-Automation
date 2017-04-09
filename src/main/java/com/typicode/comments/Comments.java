package com.typicode.comments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.orasi.api.restServices.RestResponse;
import com.orasi.api.restServices.RestService;
import com.orasi.api.restServices.Headers.HeaderType;
import com.typicode.Rest;

public class Comments {
	private RestService restService = new RestService();
	private String path = Rest.baseUrl + "comments";
	
	public RestResponse getCommentsByPostId(int postId){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("postId", String.valueOf(postId)));
		return restService.sendGetRequest(path, HeaderType.JSON, params);
	}
}
