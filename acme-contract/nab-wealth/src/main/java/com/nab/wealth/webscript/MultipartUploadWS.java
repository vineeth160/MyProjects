package com.nab.wealth.webscript;

import java.io.IOException;
import java.io.InputStream;

import java.io.Serializable;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;
import java.util.Formatter.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.nab.wealth.model.NABWContentModel;
import org.alfresco.repo.nodelocator.CompanyHomeNodeLocator;
import org.alfresco.repo.nodelocator.NodeLocator;
import org.alfresco.service.cmr.model.FileExistsException;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.namespace.QName;
import org.alfresco.model.ContentModel;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.repository.ContentWriter;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
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

    

	private final String UPLOAD_DESTINATION = "workspace://SpacesStore/4d5d684c-da4f-4009-a264-849b5a0fb5dc";
	private FileFolderService fileFolderService;
	
     
	
	Map<String, Object> model = new HashMap<String, Object>(5);
	String siteName = null;

	@Override
	protected Map<String, Object> executeImpl(WebScriptRequest request, Status status, Cache cache) {
    

		Logger logger =Logger.getLogger("logs");
		FileHandler fh;
		
		try {
			fh = new FileHandler("C:\\Users\\USHA ALEKHYA\\Desktop\\vineeth\\bkp\\logs\\MultipartLogs.log");
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			
		} catch (SecurityException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
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
							logger.info("filedList"+fieldList);
						} else {
							model.put("code", "400");
							logger.info("content status... "+model);
							model.put("message", "file is null/empty in the request");
							logger.info("content status message... "+model);
						}
						break;
					
					  case "docType": 
						 
					  docType=field.getValue().replaceAll(".+:","");
					  System.out.println("doctype filedlst::"+docType); 
					  logger.info("doctype filedlst:: "+docType);
					  
					  
					 if(StringUtils.isEmpty(field.getFilename())) {
					  
					  model.put("code", "400"); model.put("message",
					  "metadata is null/empty in the request");
					  logger.info("metadata is null/empty in the request:: "+model);
					 }
					 break;

					case "metaData":
						
						metadata=field.getValue();						
						System.out.println("metadata filedlst::"+metadata);
						
						
						if(StringUtils.isEmpty(metadata)) {
						    System.out.println();
							model.put("code", "400");
							logger.info("metadata status:: "+model);
							model.put("message", "metadata is null/empty in the request");
							logger.info("metadata is null/empty in the request:: "+model);
						}
						break;
					}

				}
        
			    String folderName = "demo";
				NodeRef descFolderNodeRef = getPathNodeRef(folderName );
				//NodeRef descFolderNodeRef = getCompanyHome();
				/*
				 * NodeRef parentNoderef = getCompanyHome(); // NodeRef descFolderNodeRef = new
				 * NodeRef(UPLOAD_DESTINATION); //create new folder FileInfo newFolder= null;
				 * String newfolder="newFolder";
				 * 
				 * 
				 * NodeRef descFolderNodeRef = newFolder.getNodeRef();
				 * newFolder=createFolder(newfolder);
				 */
				
				//NodeRef newFolderName = parentNoderef.
				 
                 
                 
                		 
                		 
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
                     //if(descFolderNodeRef==null) ) 
                    	//if(newFolder!=null) {
					 docNodeRef = serviceRegistry.getNodeService().createNode(descFolderNodeRef,
					 ContentModel.ASSOC_CONTAINS,
					 QName.createQName(NamespaceService.CONTENT_MODEL_1_0_URI,
					 field.getFilename()), NABWContentModel.nabw(docType), props) .getChildRef();
						 
					 
					 boolean successFlag = writeContentStreamToCustomDocument(field.getInputStream(), docNodeRef,
							field.getMimetype());

					
					if (successFlag) {
						model.put("code", "200");
						logger.info("metadata update success:: "+model);
						model.put("message", "Metadata Updated Successfully");
						logger.info("metadata updte successfully:: "+model);
					} else {
						model.put("code", "400");
						logger.info("metadata fail status:: "+model);
						model.put("message", "Metadata Updated is Failed ");
						logger.info("metadata update is failed..:: "+model);
					}
				}
			}
			//}
		}
		
				catch (DuplicateChildNodeNameException e) {
			model.put("code", "400");
			logger.info("DuplicateChildNodeNameException... "+model);
			model.put("message", "DuplicateChildNodeNameException Occured " + e);
			logger.info("DuplicateChildNodeNameException occurred... "+model);
			
		} catch (Exception e) {
			model.put("code", "400");
			logger.info("status.. "+model);
			model.put("message", e);
			logger.info("status..erorr "+model);
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

		 params.put("query", "/app:company_home/cm:" + alfrescoFolderName);
		 
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
	
	private FileInfo createFolder(String folderName) throws FileExistsException{
		
		NodeRef parentFolderNodeRef = serviceRegistry.getNodeLocatorService().getNode(CompanyHomeNodeLocator.NAME, null, null);
		// String folderName="test1";
		// NodeRef existingFolderRef = null;
		 FileInfo folderInfo=null;
		 //if(folderName!="test1") {
		 //String folderName ="test";
	//   if(folderName!="test") {
		// if(folderInfo)
          folderInfo= serviceRegistry.getFileFolderService().create(parentFolderNodeRef,folderName,ContentModel.TYPE_FOLDER);
         
         NodeRef descFolderNodeRef=  folderInfo.getNodeRef();
         //existingFolderRef = fileFolderService.searchSimple(descFolderNodeRef, folderName);
		return folderInfo;
		
	}
	public ServiceRegistry getServiceRegistry() {
		return serviceRegistry;
	}

	public void setServiceRegistry(ServiceRegistry serviceRegistry) {
		this.serviceRegistry = serviceRegistry;
	}
}
	
	