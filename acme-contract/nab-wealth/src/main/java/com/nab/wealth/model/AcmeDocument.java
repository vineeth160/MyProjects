package com.nab.wealth.model;


/**
 * FinancialStatement metadata
 *
 * 
 */
public class AcmeDocument {

    protected String documentId;
    protected AcmeContentModel.SecurityClassificationLevel securityClassificationLevel;

    public AcmeDocument(String documentId, AcmeContentModel.SecurityClassificationLevel securityClassificationLevel) {
        this.documentId = documentId;
        this.securityClassificationLevel = securityClassificationLevel;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public AcmeContentModel.SecurityClassificationLevel getSecurityClassificationLevel() {
        return securityClassificationLevel;
    }

    public void setSecurityClassificationLevel(AcmeContentModel.SecurityClassificationLevel securityClassificationLevel) {
        this.securityClassificationLevel = securityClassificationLevel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AcmeDocument)) return false;

        AcmeDocument that = (AcmeDocument) o;

        if (documentId != null ? !documentId.equals(that.documentId) : that.documentId != null) return false;
        return securityClassificationLevel == that.securityClassificationLevel;

    }

    @Override
    public int hashCode() {
        int result = documentId != null ? documentId.hashCode() : 0;
        result = 31 * result + (securityClassificationLevel != null ? securityClassificationLevel.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AcmeDocument{" +
                "documentId='" + documentId + '\'' +
                ", securityClassificationLevel=" + securityClassificationLevel +
                '}';
    }
}