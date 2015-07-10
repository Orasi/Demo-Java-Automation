package com.orasi.api.restServices.blueSource.titles;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.orasi.utils.Randomness;

public class Title {
    private int id;
    private String name;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    
    public List<NameValuePair> generateURIParameters(String authenticity_token){ 
  	return generateURIParameters(authenticity_token, null);
      }
    
      public List<NameValuePair> generateURIParameters(String authenticity_token, String method){ 
	  List<NameValuePair> params = new ArrayList<NameValuePair>();
	  params.add(new BasicNameValuePair("authenticity_token", authenticity_token));
	  params.add(new BasicNameValuePair("_method", method));
	  params.add(new BasicNameValuePair("title[name]", name));
	  return params;
      }
      
      public void generateData(){
	  this.name = "Random title " + Randomness.randomNumber(6);
      }
}
