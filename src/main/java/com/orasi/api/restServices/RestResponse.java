package com.orasi.api.restServices;

import static com.orasi.api.restServices.ResponseCodes.NO_CONTENT;
import static com.orasi.utils.TestReporter.logInfo;
import static com.orasi.utils.TestReporter.logTrace;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.api.restServices.exceptions.RestException;

public class RestResponse {
    private ObjectMapper mapper = new ObjectMapper(); // .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private HttpUriRequest originalRequest = null;
    private String originalRequestBody = "";
    private String method = null;
    private HttpResponse response = null;
    private int statusCode = 0;
    private String responseFormat = "";
    private String responseAsString = "";
    private String url = "";
    private String executionTime = "";

    public RestResponse(HttpUriRequest request, HttpResponse httpResponse, String executionTime) {
        logTrace("Entering RestResponse#init");

        logTrace("Storing orignal request for later usage");
        this.originalRequest = request;
        this.executionTime = executionTime;
        if (request instanceof HttpEntityEnclosingRequestBase) {
            logTrace("Original request has a body entity, attempting to extract body");
            HttpEntity entity = ((HttpEntityEnclosingRequest) request).getEntity();

            if (entity != null) {
                try {
                    originalRequestBody = EntityUtils.toString(entity);
                    logTrace("Successfully retrieved body and stored");
                } catch (IOException throwAway) {
                    logTrace("Failed to retrieve original request body");
                }
            }
        }

        url = request.getURI().toString();
        logTrace("Successfully stored URI [ " + url + " ]");

        method = request.getMethod();
        logTrace("Successfully stored Request Method [ " + method + " ]");

        response = httpResponse;

        statusCode = response.getStatusLine().getStatusCode();
        logTrace("Successfully stored Response Status Code [ " + statusCode + " ]");

        responseFormat = ContentType.getOrDefault(response.getEntity()).getMimeType().replace("application/", "");
        logTrace("Successfully stored Response Format [ " + responseFormat + " ]");

        try {
            if (statusCode != NO_CONTENT || response.getEntity() != null) {
                responseAsString = EntityUtils.toString(response.getEntity());
                logInfo("Response Status returned [" + httpResponse.getStatusLine() + "]");
            }
            logInfo("Response returned: " + responseAsString);
        } catch (ParseException | IOException e) {
            throw new RestException(e.getMessage(), e);
        }
        logTrace("Exiting RestResponse#init");
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseFormat() {
        return responseFormat;
    }

    public String getResponse() {
        return responseAsString;
    }

    public String getMethod() {
        return method;
    }

    public HttpUriRequest getRequest() {
        return originalRequest;
    }

    public String getRequestBody() {
        return originalRequestBody;
    }

    public String getURL() {
        return url;
    }

    public Header[] getHeaders() {
        return response.getAllHeaders();
    }

    public String getHeader(String headerName) {
        for (Header header : getHeaders()) {
            if (header.getName().equalsIgnoreCase(headerName)) {
                return header.getValue();
            }
        }
        return null;
    }

    public String getExecutionTime() {
        return executionTime;
    }

    /**
     * Uses the class instance of the responeAsString to map to object
     *
     * @param clazz
     * @return
     * @throws IOException
     */
    public <T> T mapJSONToObject(Class<T> clazz) {
        return mapJSONToObject(responseAsString, clazz);

    }

    /**
     * Can pass in any json as a string and map to object
     *
     * @param clazz
     * @return
     * @throws IOException
     */
    public <T> T mapJSONToObject(String stringResponse, Class<T> clazz) {
        T map = null;
        try {
            map = mapper.readValue(stringResponse, clazz);
        } catch (JsonParseException e) {
            throw new RestException("Failed to parse JSON", e);
        } catch (IOException e) {
            throw new RestException("Failed to Map JSON", e);
        }
        return map;
    }

    /**
     * Can pass in any json as a string and maps to tree
     *
     * @param clazz
     * @return
     * @throws IOException
     */
    public JsonNode mapJSONToTree(String stringResponse) {

        try {
            return mapper.readTree(stringResponse);
        } catch (IOException e) {
            throw new RestException("Failed to read response:" + stringResponse, e);
        }
    }

    /**
     * Uses the class instance of the responeAsString to map to tree
     *
     * @param clazz
     * @return
     * @throws IOException
     */
    public JsonNode mapJSONToTree() {
        return mapJSONToTree(responseAsString);
    }

    public String getResponseAsXML() {
        Object json = null;

        try {
            json = new JSONObject(responseAsString);
        } catch (JSONException e) {
            try {
                json = new JSONArray(responseAsString);
            } catch (JSONException e1) {
                throw new RestException("Response is not in JSON format");
            }
        }
        String xml = "";
        try {
            xml = "<root>" + XML.toString(json, "element") + "</root>";
        } catch (JSONException e) {
            throw new RestException("Failed to transform JSON to XML");
        }
        return xml;
    }

}
