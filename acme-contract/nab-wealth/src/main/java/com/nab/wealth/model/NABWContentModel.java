package com.nab.wealth.model;

import org.alfresco.service.namespace.QName;

public class NABWContentModel {
	
	 public final static String NAMESPACE_URI = "http://www.wealth.nab.com/model/bank/1.0";
	 
	    public final static String NAMESPACE_PREFIX = "nabw";
	   

	    public static QName nabw(final String qname) {
	        System.out.println("-------nabw()---------------"+QName.createQName(NAMESPACE_URI, qname));
	        return QName.createQName(NAMESPACE_URI, qname);
	    }
	    
	    
	    /**
	     * NABW FinancialStatement metadata
	     */
	    public static final class FinancialStatement {
	        public static final QName QNAME = nabw("financialStatement");

	        private FinancialStatement() {
	        }

	        public static final class Prop {
	            private Prop() {
	            }
	            
	            /*public static final QName FINANCIALSTATEMENT_NAME = nabw("name");
	            public static final QName FINANCIALSTATEMENT_DOB = nabw("dateOfBirth");
	            public static final QName FINANCIALSTATEMENT_RDCTABN = nabw("rdctAbn");
	            public static final QName FINANCIALSTATEMENT_FUNDNAME = nabw("fundName");
	            public static final QName FINANCIALSTATEMENT_TRUSTNAME = nabw("trustName");*/
	        }
	    }
	    
	    
	    /**
	     * NABW ProductConfirmation metadata
	     */
	    public static final class ProductConfirmation {
	        public static final QName QNAME = nabw("productConfirmation");

	        private ProductConfirmation() {
	        }

	        public static final class Prop {
	            private Prop() {
	            }
	            //public static final QName DOC_TYPE = nabw("financialStatement");
	            /*public static final QName PRODUCTCONFIRMATION_NAME = nabw("investorAccountName");
	            public static final QName PRODUCTCONFIRMATION_EMAIL = nabw("email");
	            public static final QName PRODUCTCONFIRMATION_SERVICE = nabw("superServiceUSI");
	            public static final QName PRODUCTCONFIRMATION_ACCOUNTTYPE = nabw("accountType");
	            public static final QName PRODUCTCONFIRMATION_TAXFILENO = nabw("taxFileNumber");
	            public static final QName PRODUCTCONFIRMATION_DATE = nabw("dateOfSubmission");
	            public static final QName PRODUCTCONFIRMATION_ACCNUMBER = nabw("investorAccountNumber");
	            public static final QName PRODUCTCONFIRMATION_TXNTYPE = nabw("transactionType");
	            public static final QName PRODUCTCONFIRMATION_VALUE = nabw("totalPortfolioValue");
	            public static final QName PRODUCTCONFIRMATION_ADVISORNAME = nabw("advisorName");
	            public static final QName PRODUCTCONFIRMATION_USI = nabw("pensionServiceUSI");
	            public static final QName PRODUCTCONFIRMATION_TXNRECEIPTNO = nabw("transactionReceiptNumber");
	            public static final QName PRODUCTCONFIRMATION_INVESTORADDRESS = nabw("investorAddress");
	            public static final QName PRODUCTCONFIRMATION_INVESTORNO = nabw("investorNumber");*/
	           
	            
	        }
	    }
	  
	    }


