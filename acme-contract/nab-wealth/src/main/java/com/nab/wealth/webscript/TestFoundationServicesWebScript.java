package com.nab.wealth.webscript;

import org.alfresco.service.cmr.repository.NodeRef;
import com.nab.wealth.model.AcmeContract;
import com.nab.wealth.service.AcmeContentService;
import org.springframework.extensions.webscripts.AbstractWebScript;
import org.springframework.extensions.webscripts.WebScriptRequest;
import org.springframework.extensions.webscripts.WebScriptResponse;

import java.io.IOException;

import static com.nab.wealth.model.AcmeContentModel.*;

/**
 * A Web Script that is only used to test the ACME Content Service interface
 *
 * @author martin.bergljung@alfresco.com
 * @version 1.0
 */
public class TestFoundationServicesWebScript extends AbstractWebScript {
    private AcmeContentService acmeContentService;

    public void setAcmeContentService(AcmeContentService acmeContentService) {
        this.acmeContentService = acmeContentService;
    }

    @Override
    public void execute(WebScriptRequest webScriptRequest, WebScriptResponse webScriptResponse) throws IOException {
        AcmeContract contractMetadata = new AcmeContract("DOC001", SecurityClassificationLevel.COMPANY_CONFIDENTIAL,
                "C001", "Contract A");
        NodeRef contractNodeRef = acmeContentService.createContractFile(
                "ContractA.txt", "This is my first contract...", contractMetadata);

        acmeContentService.applyWebPublishedAspect(contractNodeRef);

        webScriptResponse.getWriter().write("Done with the ACME Content Service tests!");
    }
}