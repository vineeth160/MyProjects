package com.nab.wealth.webscript;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.namespace.NamespaceException;
import org.alfresco.service.namespace.QName;
//import org.apache.commons.lang.exception.ExceptionUtils;
//import org.apache.commons.logging.Log;
//import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;


public class SearchServiceWS extends DeclarativeWebScript { 
	

	//private static Log logger = LogFactory.getLog(VCMDocumentSearch.class);
	private Map<String, Object> model = new HashMap<String, Object>(5);
	
	@Autowired
	private ServiceRegistry serviceRegistry;

	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest request, Status status, Cache cache) {
		try {
			//int skipCount = 0;
			JSONObject jsonObject = new JSONObject(request.getContent().getContent());
            String docType = jsonObject.has("contentType") == true ? jsonObject.getString("contentType") : "cm:content";
           // String docCreated = jsonObject.has("contentCreate") == true ? jsonObject.getString("contentCreate") : "cm:created";
            JSONObject propValues = jsonObject.has("docMetadata") == true ? jsonObject.getJSONObject("docMetadata") : null;
            StringBuffer queryString = new StringBuffer();
            queryString.append("TYPE:'" + docType + "'");
           // if(skipCount==0) {
            if (null != propValues) {
       
                   Iterator<String> keysToCopyIterator = propValues.keys();
                   System.out.println(propValues.toString());
                   while (keysToCopyIterator.hasNext()) {
                         String key = keysToCopyIterator.next();
                         System.out.println("key:"+key);
                         //String query = "TYPE:'CM:PERSON' AND ASPECT:'sd:sponsorUserDetailsAspect'"; 
                         //TYPE:"cm:content"  AND  PROPERTY:"sd:name"='mercury'
							/*
							 * if(key.equalsIgnoreCase("fileName")){ queryString =
							 * queryString.append(" AND cm:name:'"+propValues.getString(key)+"'"); }
							 */
                         if(key.equalsIgnoreCase("createdDate")){
                                queryString = queryString.append(" AND cm:created:'"+propValues.getString(key)+"'");
                         }
							/*
							 * if(key.equalsIgnoreCase("description")){ queryString =
							 * queryString.append(" AND cm:description:'"+propValues.getString(key)+"'"); }
							 * if(key.equalsIgnoreCase("title")){ queryString =
							 * queryString.append(" AND cm:title:'"+propValues.getString(key)+"'"); }
							 * if(key.equalsIgnoreCase("nodeId")){ queryString =
							 * queryString.append(" AND sys\\:node\\-uuid:'"+propValues.getString(key)+"'");
							 * }
							 */
                        
         				
                   }
                   System.out.println("Query:::"+queryString);
                  // queryString.
            }
           // }
            
            

	        
			List<NodeRef> nodeList = (serviceRegistry.getSearchService().query(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, SearchService.LANGUAGE_FTS_ALFRESCO, queryString.toString())).getNodeRefs();
			System.out.println(nodeList.size());
			
			List<JSONObject> respList = new ArrayList<JSONObject>();
			JSONObject respObj = null;
			if (nodeList != null && nodeList.size() > 0) {
				for(NodeRef node : nodeList) {
					respObj = new JSONObject();
					System.out.println(node);
					System.out.println(node.getId());
					respObj.put("nodeId", node.getId());
					//respObj.put("fileName",serviceRegistry.getNodeService().getProperty(node, ContentModel.PROP_NAME).toString());
				//	respObj.put("title",serviceRegistry.getNodeService().getProperty(node, ContentModel.PROP_TITLE).toString());
					//respObj.put("description", serviceRegistry.getNodeService().getProperty(node, ContentModel.PROP_DESCRIPTION).toString());
					//respObj.put("author",serviceRegistry.getNodeService().getProperty(node, ContentModel.PROP_AUTHOR).toString());
					respObj.put("createdDate", serviceRegistry.getNodeService().getProperty(node, ContentModel.PROP_CREATED).toString());
					//respObj.put("creator", serviceRegistry.getNodeService().getProperty(node, ContentModel.PROP_CREATOR).toString());
					//String mimetype = serviceRegistry.getMimetypeService().guessMimetype(serviceRegistry.getNodeService().getProperty(node, ContentModel.PROP_NAME).toString());
					//respObj.put("contentType", mimetype);
					
					respList.add(respObj);
					System.out.println("respObj>>>"+respObj);
				}
			}
			System.out.println(respList);
			System.out.println("respList size???"+respList.size());
		
			
			
			//model.put("code", "");
			//model.put("message", "Set");
			//model.put("data", respList.toString());
		
		} catch (Exception e) {
			
			e.printStackTrace();
			/*
			 * model.put("code", ""); model.put("message", e);
			 * 
			 * model.put("data", "");
			 */
			//logger.error("Error occured while creating node :" + e);
		}
		return model;
	}

}

