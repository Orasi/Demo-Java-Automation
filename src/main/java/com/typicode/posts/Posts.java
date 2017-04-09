package com.typicode.posts;

import com.orasi.api.restServices.Headers.HeaderType;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.orasi.api.restServices.RestResponse;
import com.orasi.api.restServices.RestService;
import com.typicode.Rest;
import com.typicode.posts.objects.PostRequest;

public class Posts {
	private RestService restService = new RestService();
	private String path = Rest.baseUrl + "posts";
	
	public RestResponse getAllPosts(){
		return restService.sendGetRequest(path, HeaderType.JSON);
	}
	
	public RestResponse getPost(int postId){
		return restService.sendGetRequest(path + "/" + postId, HeaderType.JSON);
	}	

	public RestResponse getPostsByQuery(String name, int value){
		return getPostsByQuery(name, String.valueOf(value));
	}

	public RestResponse getPostsByQuery(String name, String value){
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(name, value));
		return restService.sendGetRequest(path, HeaderType.JSON, params);
	}
	
	public RestResponse getPostComments(int postId){
		return restService.sendGetRequest(path + "/" + postId + "/comments", HeaderType.JSON);
	}	
	
	public RestResponse createPost(PostRequest request){
		return restService.sendPostRequest(path, HeaderType.JSON, RestService.getJsonFromObject(request));
	}

	public RestResponse updatePost(int postId, PostRequest request){
		return restService.sendPutRequest(path + "/" + postId, HeaderType.JSON, RestService.getJsonFromObject(request));
	}

	public RestResponse patchPost(int postId, String field, int value){
		String json = "{ \"" + field + "\":" + value + "}";		
		return restService.sendPatchRequest(path + "/" + postId, HeaderType.JSON, json);
	}
	
	public RestResponse patchPost(int postId, String field, String value){
		String json = "{ \"" + field + "\":\"" + value + "\"}";
		return restService.sendPatchRequest(path + "/" + postId, HeaderType.JSON, json);
	}
	
	public RestResponse deletePost(int postId){
		return restService.sendDeleteRequest(path + "/" + postId, HeaderType.JSON);
	}
}
