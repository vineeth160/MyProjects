package com.nab.wealth.service;

import org.alfresco.service.cmr.repository.NodeRef;
import com.nab.wealth.model.AcmeContract;

/**
 * A custom service for the Nab-Wealth content domain.
 *
 * 
 * @version 1.0
 */

public interface AcmeContentService {
    /**
     * Create a contract file under the /Company Home folder.
     * This will be done in a read-write transaction, retry until successful or 20 trials.
     * Joins an ongoing transaction if one exists.
     */
    NodeRef createContractFile(String filename, String contractTxt, AcmeContract contract);

    /**
     * Apply the acme:webPublished aspect to the content item with passed in node reference.
     * This will be done in a read-write transaction, retry until successful or 20 trials.
     * Joins an ongoing transaction if one exists.
     *
     * @param nodeRef the Alfresco Repo node reference to apply the aspect to
     */
    void applyWebPublishedAspect(NodeRef nodeRef);
}