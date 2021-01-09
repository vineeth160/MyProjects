package com.nab.wealth.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONString;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	String metadata ="[{\"va1\":\"v1\",\"val2\":23}]";
			
		
		
		List<HashMap<String,String>> response=new ArrayList<>();
		JSONArray jsonarray=null;
		try {
			jsonarray=new JSONArray(metadata);
			System.out.println("jsonarray??"+jsonarray);
			for(int i=0;i<jsonarray.length();i++) {
				JSONObject jsonObject= jsonarray.getJSONObject(i);
				Iterator<?>iterator=jsonObject.keys();
				HashMap<String,String>map=new HashMap<>();
				while (iterator.hasNext()) {
					
					Object key =iterator.next();
					System.out.println("key??"+key);
					System.out.println("keystr??"+key.toString());
					Object value= jsonObject.get(key.toString());
					System.out.println("value??"+value);
					map.put(key.toString(),value.toString());
					System.out.println("map??"+map);
				}
				response.add(map);
				System.out.println("response??"+response);
			}
		}catch(JSONException e) {
			
			e.printStackTrace();
				
				
			}

	}

}
