package com.typicode.comments;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.testng.annotations.Test;

import com.orasi.api.restServices.ResponseCodes;
import com.orasi.api.restServices.RestResponse;
import com.orasi.utils.TestReporter;
import com.typicode.Rest;
import com.typicode.comments.objects.GetCommentsResponse;

public class TestComments {

	@Test
	public void getCommentsByPostId(){
		RestResponse restResponse = Rest.comments().getCommentsByPostId(3);
		TestReporter.logAPI(restResponse.getStatusCode() == ResponseCodes.OK, "Validate successful repsonse", restResponse);
		GetCommentsResponse[] comments = restResponse.mapJSONToObject(GetCommentsResponse[].class);		
		TestReporter.assertGreaterThanZero(comments.length);
		
		for (GetCommentsResponse comment : comments){
			TestReporter.logStep("Validating info for Comment Id [ " + comment.getId() + " ]");
			TestReporter.softAssertTrue(NumberUtils.isNumber(String.valueOf(comment.getId())), "Validate Id is a number [ " + comment.getId() + " ]");
			TestReporter.softAssertTrue(NumberUtils.isNumber(String.valueOf(comment.getPostId())), "Validate Post Id is a number [ " + comment.getPostId() + " ]");
			TestReporter.softAssertTrue(StringUtils.isAlphaSpace(comment.getName()), "Validate Name is a String [ " + comment.getName() + " ]");
			TestReporter.softAssertTrue(StringUtils.isNotEmpty(comment.getBody()), "Validate Body is not blank [ " + comment.getBody() + " ]");
			TestReporter.softAssertTrue(StringUtils.isNotEmpty(comment.getEmail()), "Validate Email is not blank [ " + comment.getEmail() + " ]");
		}
		
		TestReporter.assertAll();
	}	

	
}
