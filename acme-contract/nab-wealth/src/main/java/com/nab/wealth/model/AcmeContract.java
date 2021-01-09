package com.nab.wealth.model;

/**
*
* ProductConfirmationmetadata
*
*
*/
public class AcmeContract extends AcmeDocument {
   protected String contractId;
   protected String contractName;

   public AcmeContract(String documentId, AcmeContentModel.SecurityClassificationLevel securityClassificationLevel,
                       String contractId, String contractName) {
       super(documentId, securityClassificationLevel);

       this.contractId = contractId;
       this.contractName = contractName;
   }

   public String getContractName() {
       return contractName;
   }

   public void setContractName(String contractName) {
       this.contractName = contractName;
   }

   public String getContractId() {
       return contractId;
   }

   public void setContractId(String contractId) {
       this.contractId = contractId;
   }

   @Override
   public boolean equals(Object o) {
       if (this == o) return true;
       if (!(o instanceof AcmeContract)) return false;
       if (!super.equals(o)) return false;

       AcmeContract that = (AcmeContract) o;

       if (contractId != null ? !contractId.equals(that.contractId) : that.contractId != null) return false;
       return !(contractName != null ? !contractName.equals(that.contractName) : that.contractName != null);

   }

   @Override
   public int hashCode() {
       int result = super.hashCode();
       result = 31 * result + (contractId != null ? contractId.hashCode() : 0);
       result = 31 * result + (contractName != null ? contractName.hashCode() : 0);
       return result;
   }

   @Override
   public String toString() {
       return "ProductConfirmation{" +
               "documentId='" + documentId + '\'' +
               ", securityClassificationLevel=" + securityClassificationLevel +
               ", contractId='" + contractId + '\'' +
               ", contractName='" + contractName + '\'' +
               '}';
   }
}