package com.nab.wealth.webscript;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nab.wealth.model.NABWContentModel;
import org.alfresco.repo.nodelocator.CompanyHomeNodeLocator;
import org.alfresco.repo.nodelocator.NodeLocator;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.namespace.QName;
import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.repository.DuplicateChildNodeNameException;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.NamespacePrefixResolver;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.service.cmr.search.ResultSet;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.util.ISO9075;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import org.springframework.extensions.webscripts.Cache;
import org.springframework.extensions.webscripts.DeclarativeWebScript;
import org.springframework.extensions.webscripts.Status;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.servlet.FormData;
import org.springframework.extensions.webscripts.servlet.FormData.FormField;

import com.nab.wealth.model.NABWContentModel.FinancialStatement;
import com.nab.wealth.model.NABWContentModel.ProductConfirmation;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


public class MultipartUploadWS extends DeclarativeWebScript {

	private ServiceRegistry serviceRegistry;

    

	private final String UPLOAD_DESTINATION = "workspace://SpacesStore/95b1729c-087e-4b6a-ac7d-6625346b96fc";
     
	
	Map<String, Object> model = new HashMap<String, Object>(5);
	String siteName = null;

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest request, Status status, Cache cache) {

			
		List<FormField> fieldList = new ArrayList<FormField>();
		Map<QName, Serializable> props = new HashMap<>();
		NodeRef docNodeRef = null;
		//String folderName = null;
		String metadata = null;
		String docType = null;

		try {
			
			if (request.getContentType().equalsIgnoreCase("multipart/form-data")) {
				FormData formData = (FormData) request.parseContent();
				FormData.FormField[] fields = formData.getFields();
				for (FormData.FormField field : fields) {
					switch (field.getName()) {
					case "fileData":
						if (field.getIsFile()) {
							fieldList.add(field);
						} else {
							model.put("code", "400");
							model.put("message", "file is null/empty in the request");
						}
						break;
					
					  case "docType": 
						 
					  docType=field.getValue().replaceAll(".+:","");
					  System.out.println("doctype filedlst::"+docType); 
					  
					  
					 if(StringUtils.isEmpty(field.getFilename())) {
					  
					  model.put("code", "400"); model.put("message",
					  "metadata is null/empty in the request");
					 }
					 break;

					case "metaData":
						
						metadata=field.getValue();						
						System.out.println("metadata filedlst::"+metadata);
						
						
						if(StringUtils.isEmpty(metadata)) {
						    System.out.println();
							model.put("code", "400");
							model.put("message", "metadata is null/empty in the request");
						}
						break;
					}

				}

			    //NodeRef descFolderNodeRef = getPathNodeRef(folderName);
				//NodeRef descFolderNodeRef = getCompanyHome();
				 NodeRef descFolderNodeRef = new NodeRef(UPLOAD_DESTINATION);

				for (FormField field : fieldList) {
					

                    props.put(ContentModel.PROP_NAME, field.getFilename());
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonnode = objectMapper.readTree(metadata);

                    if(jsonnode.isArray()) {
                        ArrayNode arrayNode = (ArrayNode) jsonnode;
                        for (int i = 0; i < arrayNode.size(); i++) {

                            JsonNode docJsonNode = arrayNode.get(i);

                            JsonNode docPropertiesJsonNode = docJsonNode.get("metaData");

                            Iterator<String> fieldNames=docPropertiesJsonNode.fieldNames();

                            while(fieldNames.hasNext()) {
                                String paramName = fieldNames.next();
                                String paramValue = docPropertiesJsonNode.get(paramName).toString();

                                if(docType.equals("financialStatement")) {
                                    System.out.println("------param Name----------"+paramName.substring(paramName.indexOf(":"),paramName.length()-1));
                                    props.put(NABWContentModel.nabw(paramName.substring(paramName.indexOf(":"),paramName.length()-1)),paramValue);
                                }
                                else if(docType.equals("productConfirmation")){
                                    props.put(NABWContentModel.nabw(paramName.substring(paramName.indexOf(":"),paramName.length()-1)),paramValue);
                                }
                            }
                        }
                    }

					 docNodeRef = serviceRegistry.getNodeService() .createNode(descFolderNodeRef,
					 ContentModel.ASSOC_CONTAINS,
					 QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,
					 field.getFilename()), NABWContentModel.nabw(docType), props) .getChildRef();
						 
                    
					 boolean successFlag = writeContentStreamToCustomDocument(field.getInputStream(), docNodeRef,
							field.getMimetype());

					
					if (successFlag) {
						model.put("code", "200");
						model.put("message", "Metadata Updated Successfully");
					} else {
						model.put("code", "400");
						model.put("message", "Metadata Updated is Failed ");
					}
				}
			}
		}
				catch (DuplicateChildNodeNameException e) {
			model.put("code", "400");
			model.put("message", "DuplicateChildNodeNameException Occured " + e);
		} catch (Exception e) {
			model.put("code", "400");
			model.put("message", e);
			model.put("message", e.getMessage());
		}

		return model;
	}

	private NodeRef getPathNodeRef(String foldername) {

		String alfrescoFolderName = ISO9075.encode(foldername);
		Map<String, Serializable> params = new HashMap<>();
		/*
		 * params.put("query", "./app:company_home/st:sites/cm:" + siteName +
		 * "/cm:documentLibrary/cm:" + alfrescoFolderName);
		 */

		  params.put("query", "./app:company_home/cm:" + alfrescoFolderName);
		 
		return serviceRegistry.getNodeLocatorService().getNode("xpath", null, params);

	}
	

    //If you want to test with CompanyHome first use this method instead of the NodeRef
    @SuppressWarnings("unused")
    private NodeRef getCompanyHome() {
        StoreRef storeRef = new StoreRef(StoreRef.PROTOCOL_WORKSPACE, "SpacesStore");
        serviceRegistry.getSearchService();
        ResultSet rs = serviceRegistry.getSearchService().query(storeRef, SearchService.LANGUAGE_XPATH, "/app:company_home");
        NodeRef descFolderNodeRef = null;
        try {
            if (rs.length() == 0) {
                throw new AlfrescoRuntimeException("Didn't find Company Home");
            }
            descFolderNodeRef = rs.getNodeRef(0);
        } finally {
            rs.close();
        }
        return descFolderNodeRef;
    }

	public boolean writeContentStreamToCustomDocument(InputStream contentInputStream, NodeRef docNodeRef,
			String mimeType) {
		boolean successFlag = false;
		try {
			ContentWriter writer = serviceRegistry.getContentService().getWriter(docNodeRef, ContentModel.PROP_CONTENT,
					true);
			writer.setMimetype(mimeType);
			writer.putContent(contentInputStream);
			successFlag = true;
		} catch (Exception e) {
			model.put("code", "400");
			model.put("message", "Exception Occurred " + e);
			model.put("message", "Exception Occurred " + e.getMessage());
		}
		return successFlag;
	}
	
	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
}
	
	