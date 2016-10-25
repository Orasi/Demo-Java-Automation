package com.orasi.utils.debugging;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MustardResult {
    @JsonProperty("result")

    private Result result;

    public MustardResult(){
	this.result = new Result();
    }
    /**
     * 
     * @return
     * The result
     */
    public Result getResult() {
	return result;
    }

    /**
     * 
     * @param result
     * The result
     */
    public void setResult(Result result) {
	this.result = result;
    }


    public class Result{
	@JsonProperty("project_id")
	private String projectId;

	@JsonProperty("result_type")
	private String resultType;

	@JsonProperty("environment_id")
	private String environmentId;

	@JsonProperty("testcase_id")
	private String testcaseId;

	@JsonProperty("status")
	private String status;

	@JsonProperty("comment")
	private String comment;

	@JsonProperty("screenshot")
	private String screenshot;

	@JsonProperty("stacktrace")
	private String stacktrace;
	@JsonProperty("display_name")
	private String displayName;

	@JsonProperty("link")
	private String link;

	/**
	 * 
	 * @return
	 * The projectId
	 */
	public String getProjectId() {
	    return projectId;
	}

	/**
	 * 
	 * @param projectId
	 * The project_id
	 */
	public void setProjectId(String projectId) {
	    this.projectId = projectId;
	}

	/**
	 * 
	 * @return
	 * The resultType
	 */
	public String getResultType() {
	    return resultType;
	}

	/**
	 * 
	 * @param resultType
	 * The result_type
	 */
	public void setResultType(String resultType) {
	    this.resultType = resultType;
	}

	/**
	 * 
	 * @return
	 * The environmentId
	 */
	public String getEnvironmentId() {
	    return environmentId;
	}

	/**
	 * 
	 * @param environmentId
	 * The environment_id
	 */
	public void setEnvironmentId(String environmentId) {
	    this.environmentId = environmentId;
	}

	/**
	 * 
	 * @return
	 * The testcaseId
	 */
	public String getTestcaseId() {
	    return testcaseId;
	}

	/**
	 * 
	 * @param testcaseId
	 * The testcase_id
	 */
	public void setTestcaseId(String testcaseId) {
	    this.testcaseId = testcaseId;
	}

	/**
	 * 
	 * @return
	 * The status
	 */
	public String getStatus() {
	    return status;
	}

	/**
	 * 
	 * @param status
	 * The status
	 */
	public void setStatus(String status) {
	    this.status = status;
	}

	/**
	 * 
	 * @return
	 * The comment
	 */
	public String getComment() {
	    return comment;
	}

	/**
	 * 
	 * @param comment
	 * The comment
	 */
	public void setComment(String comment) {
	    this.comment = comment;
	}

	/**
	 * 
	 * @return
	 * The screenshot
	 */
	public String getScreenshot() {
	    return screenshot;
	}

	/**
	 * 
	 * @param screenshot
	 * The screenshot
	 */
	public void setScreenshot(String screenshot) {
	    this.screenshot = screenshot;
	}

	/**
	 * 
	 * @return
	 * The stacktrace
	 */
	public String getStacktrace() {
	    return stacktrace;
	}

	/**
	 * 
	 * @param stacktrace
	 * The stacktrace
	 */
	public void setStacktrace(String stacktrace) {
	    this.stacktrace = stacktrace;
	}

	/**
	 * 
	 * @return
	 * The displayName
	 */
	public String getDisplayName() {
	    return displayName;
	}

	/**
	 * 
	 * @param displayName
	 * The display_name
	 */
	public void setDisplayName(String displayName) {
	    this.displayName = displayName;
	}

	/**
	 * 
	 * @return
	 * The link
	 */
	public String getLink() {
	    return link;
	}

	/**
	 * 
	 * @param link
	 * The link
	 */
	public void setLink(String link) {
	    this.link = link;
	}
    }
}
