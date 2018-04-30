
/**
 * FaultMsg.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.4  Built on : Oct 21, 2016 (10:47:34 BST)
 */

package com.belkinc.services.product;

public class FaultMsg extends java.lang.Exception{

    private static final long serialVersionUID = 1485227267825L;
    
    private com.belkinc.services.product.BelkProductServiceStub.Fault faultMessage;

    
        public FaultMsg() {
            super("FaultMsg");
        }

        public FaultMsg(java.lang.String s) {
           super(s);
        }

        public FaultMsg(java.lang.String s, java.lang.Throwable ex) {
          super(s, ex);
        }

        public FaultMsg(java.lang.Throwable cause) {
            super(cause);
        }
    

    public void setFaultMessage(com.belkinc.services.product.BelkProductServiceStub.Fault msg){
       faultMessage = msg;
    }
    
    public com.belkinc.services.product.BelkProductServiceStub.Fault getFaultMessage(){
       return faultMessage;
    }
}
    