package com.orasi.api.restServices.blueSource.titles;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.protocol.HttpContext;

import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.api.restServices.blueSource.BlueSource;
import com.orasi.api.restServices.blueSource.Paths;

public class Titles extends BlueSource{
    
    public Titles(HttpContext httpContext){
   	setSessionCookie(httpContext);
       }
    
    public int getNumberOfTitles(){
  	String url = Paths.titles + ".json";
     	JsonNode allTitles = mapJSONTree(sendGet(url));
     	return allTitles.size();
      }
    
    public List<Title> getAllTitles(){
   	String url = Paths.titles + ".json";
   	List<Title> titleCollection = new ArrayList<Title>();
   		
   	JsonNode allTitles = null;
   	String response = sendGet(url);
   	 allTitles = mapJSONTree(response);
   	
   	
   	Iterator<JsonNode> nodeIterator = allTitles.iterator();
   	Title title = null;
	while (nodeIterator.hasNext()) {

	   JsonNode data = nodeIterator.next();
	   title = mapJSON(data.toString(), Title.class);
	   titleCollection.add(title);
	}
	
	return titleCollection;
     }
    
    public Title createTitle(Title title){
	 String url = Paths.titles + ".json";
	 List<NameValuePair> params = title.generateURIParameters(authenticity_token);
	 sendPost(url,  params);	
	 List<Title>  titles = getAllTitles();
	 Title tempTitle = null;
	 Iterator<Title> titleIterator = titles.iterator();
	 while (titleIterator.hasNext()) {
	     tempTitle = titleIterator.next();
	     if(tempTitle.getName().equals(title.getName()))  title.setId(tempTitle.getId());
	 }
	
	 return title;
    }
    
    public Title getTitle(int id){
	String url = Paths.title.replace(":id", String.valueOf(id)) + ".json";  	
 	return mapJSON(sendGet(url), Title.class);
   }
    
    public void updateTitle(Title title){
	 String url = Paths.title.replace(":id", String.valueOf(title.getId())) + ".json";
	 List<NameValuePair> params = title.generateURIParameters(authenticity_token);
	 sendPatch(url, params);
    }
    
    public void deleteTitle(Title title){
	 String url = Paths.title.replace(":id", String.valueOf(title.getId()));
	 List<NameValuePair> params = title.generateURIParameters(authenticity_token);
	 sendDelete(url, params);
   }
}
