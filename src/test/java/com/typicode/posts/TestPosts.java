package com.typicode.posts;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.annotations.Test;

import com.orasi.api.restServices.ResponseCodes;
import com.orasi.api.restServices.RestResponse;
import com.orasi.utils.Randomness;
import com.orasi.utils.TestReporter;
import com.typicode.Rest;
import com.typicode.posts.objects.PostRequest;
import com.typicode.posts.objects.PostCommentsResponse;
import com.typicode.posts.objects.PostsResponse;

public class TestPosts {

	@Test
	public void getAllPosts(){
		RestResponse restResponse = Rest.posts().getAllPosts();
		TestReporter.logAPI(restResponse.getStatusCode() == ResponseCodes.OK, "Validate successful repsonse", restResponse);
		PostsResponse[] posts= restResponse.mapJSONToObject(PostsResponse[].class);		
		TestReporter.assertGreaterThanZero(posts.length);
		
		for (PostsResponse post : posts){
			TestReporter.logStep("Validating info for post Id [ " + post.getId() + " ]");
			TestReporter.softAssertTrue(NumberUtils.isNumber(String.valueOf(post.getId())), "Validate Id is a number [ " + post.getId() + " ]");
			TestReporter.softAssertTrue(NumberUtils.isNumber(String.valueOf(post.getUserId())), "Validate User Id is a number [ " + post.getUserId() + " ]");
			TestReporter.softAssertTrue(StringUtils.isAlphaSpace(post.getTitle()), "Validate Title is a String [ " + post.getTitle() + " ]");
			TestReporter.softAssertTrue(StringUtils.isNotEmpty(post.getBody()), "Validate Body is not blank [ " + post.getBody() + " ]");
		}
		
		TestReporter.assertAll();
	}	

	@Test
	public void getPost(){
		RestResponse restResponse = Rest.posts().getPost(10);
		TestReporter.logAPI(restResponse.getStatusCode() == ResponseCodes.OK, "Validate successful repsonse", restResponse);
		PostsResponse post= restResponse.mapJSONToObject(PostsResponse.class);
		TestReporter.softAssertTrue(post.getId() == 10, "Validate Post Id 10 is returned");
		TestReporter.softAssertTrue(NumberUtils.isNumber(String.valueOf(post.getId())), "Validate Id is a number [ " + post.getId() + " ]");
		TestReporter.softAssertTrue(NumberUtils.isNumber(String.valueOf(post.getUserId())), "Validate User Id is a number [ " + post.getUserId() + " ]");
		TestReporter.softAssertTrue(StringUtils.isAlphaSpace(post.getTitle()), "Validate Title is a String [ " + post.getTitle() + " ]");
		TestReporter.softAssertTrue(StringUtils.isNotEmpty(post.getBody()), "Validate Body is not blank [ " + post.getBody() + " ]");

		TestReporter.assertAll();
	}
	
	@Test
	public void getPostsByQuery_UserId(){
		int userIdToValidate = 3;
		RestResponse restResponse = Rest.posts().getPostsByQuery("userId", userIdToValidate);
		TestReporter.logAPI(restResponse.getStatusCode() == ResponseCodes.OK, "Validate successful repsonse", restResponse);
		PostsResponse[] posts= restResponse.mapJSONToObject(PostsResponse[].class);		
		TestReporter.assertGreaterThanZero(posts.length);
		
		for (PostsResponse post : posts){
			TestReporter.softAssertTrue(post.getUserId() == userIdToValidate, "Validate Post Id [ " + post.getId() + " ] has User ID of [ " + userIdToValidate + " ] as expected. User ID returned [ " + post.getUserId() + " ]");
		}
		
		TestReporter.assertAll();
	}	

	@Test
	public void getPostsByQuery_Title(){
		String titleToValidate = "adipisci placeat illum aut reiciendis qui";
		RestResponse restResponse = Rest.posts().getPostsByQuery("title", titleToValidate);
		TestReporter.logAPI(restResponse.getStatusCode() == ResponseCodes.OK, "Validate successful repsonse", restResponse);
		PostsResponse[] posts= restResponse.mapJSONToObject(PostsResponse[].class);		
		TestReporter.assertGreaterThanZero(posts.length);
		
		for (PostsResponse post : posts){
			TestReporter.softAssertTrue(post.getTitle().equals(titleToValidate), "Validate Post Id [ " + post.getId() + " ] has Title of [ " + titleToValidate + " ] as expected. Title returned [ " + post.getTitle() + " ]");
		}
		
		TestReporter.assertAll();
	}	

	@Test
	public void getPostComments(){
		RestResponse restResponse = Rest.posts().getPostComments(10);
		TestReporter.logAPI(restResponse.getStatusCode() == ResponseCodes.OK, "Validate successful repsonse", restResponse);
		PostCommentsResponse[] posts= restResponse.mapJSONToObject(PostCommentsResponse[].class);		
		TestReporter.assertGreaterThanZero(posts.length);
		
		for (PostCommentsResponse post : posts){
			TestReporter.logStep("Validating info for post Id [ " + post.getId() + " ]");
			TestReporter.softAssertTrue(NumberUtils.isNumber(String.valueOf(post.getId())), "Validate Id is a number [ " + post.getId() + " ]");
			TestReporter.softAssertTrue(NumberUtils.isNumber(String.valueOf(post.getPostId())), "Validate Post Id is a number [ " + post.getPostId() + " ]");
			TestReporter.softAssertTrue(StringUtils.isAlphaSpace(post.getName()), "Validate Name is a String [ " + post.getName() + " ]");
			TestReporter.softAssertTrue(StringUtils.isNotEmpty(post.getBody()), "Validate Body is not blank [ " + post.getBody() + " ]");
			TestReporter.softAssertTrue(StringUtils.isNotEmpty(post.getEmail()), "Validate Email is not blank [ " + post.getEmail() + " ]");
		}
		
		TestReporter.assertAll();	
	}	
	
	@Test
	public void createPost(){
		int userId = 1;
		String title = Randomness.randomString(10);
		String body = Randomness.randomString(50);
		
		PostRequest request = new PostRequest();
		request.setUserId(userId);
		request.setTitle(title);
		request.setBody(body);
		
		RestResponse restResponse = Rest.posts().createPost(request);
		TestReporter.logAPI(restResponse.getStatusCode() == ResponseCodes.CREATED, "Validate successful repsonse", restResponse);
		
		PostsResponse post = restResponse.mapJSONToObject(PostsResponse.class);
		TestReporter.softAssertTrue(NumberUtils.isNumber(String.valueOf(post.getId())), "Validate Id is a number [ " + post.getId() + " ]");
		TestReporter.softAssertTrue(post.getUserId() == request.getUserId(), "Validate User Id returned [ " + post.getUserId() + " ] matches User Id expected [ " + request.getUserId() + " ]");
		TestReporter.softAssertTrue(post.getTitle().equals(request.getTitle()), "Validate Title returned [ " + post.getTitle() + " ] matches Title expected [ " + request.getTitle() + " ]");
		TestReporter.softAssertTrue(post.getBody().equals(request.getBody()), "Validate Body returned [ " + post.getBody() + " ] matches Body expected [ " + request.getBody() + " ]");

		TestReporter.assertAll();		
	}
	
	@Test
	public void updatePost(){
		int id = 100;
		int userId = 1;
		String title = Randomness.randomString(10);
		String body = Randomness.randomString(50);
		
		PostRequest request = new PostRequest();
		request.setId(id);
		request.setUserId(userId);
		request.setTitle(title);
		request.setBody(body);
		
		RestResponse restResponse = Rest.posts().updatePost(id, request);
		TestReporter.logAPI(restResponse.getStatusCode() == ResponseCodes.OK, "Validate successful repsonse", restResponse);
		
		PostsResponse post = restResponse.mapJSONToObject(PostsResponse.class);
		TestReporter.softAssertTrue(post.getId() == request.getId(), "Validate Id returned [ " + post.getId() + " ] matches Id expected [ " + request.getId() + " ]");
		TestReporter.softAssertTrue(post.getUserId() == request.getUserId(), "Validate User Id returned [ " + post.getUserId() + " ] matches User Id expected [ " + request.getUserId() + " ]");
		TestReporter.softAssertTrue(post.getTitle().equals(request.getTitle()), "Validate Title returned [ " + post.getTitle() + " ] matches Title expected [ " + request.getTitle() + " ]");
		TestReporter.softAssertTrue(post.getBody().equals(request.getBody()), "Validate Body returned [ " + post.getBody() + " ] matches Body expected [ " + request.getBody() + " ]");

		TestReporter.assertAll();		
	}
	
	
	@Test
	public void patchPostTitle(){
		int id = 100;
		String title = Randomness.randomString(10);
		
		RestResponse restResponse = Rest.posts().patchPost(id, "title", title);
		TestReporter.logAPI(restResponse.getStatusCode() == ResponseCodes.OK, "Validate successful repsonse", restResponse);
		
		PostsResponse post = restResponse.mapJSONToObject(PostsResponse.class);
		TestReporter.softAssertTrue(post.getId() == id, "Validate Id returned [ " + post.getId() + " ] matches Id expected [ " + id + " ]");
		TestReporter.softAssertTrue(post.getTitle().equals(title), "Validate Title returned [ " + post.getTitle() + " ] matches Title expected [ " + title + " ]");
		
		TestReporter.assertAll();		
	}

	@Test
	public void deletePostTitle(){
		RestResponse restResponse = Rest.posts().deletePost(100);
		TestReporter.logAPI(restResponse.getStatusCode() == ResponseCodes.OK, "Validate successful repsonse", restResponse);		
	}
}
