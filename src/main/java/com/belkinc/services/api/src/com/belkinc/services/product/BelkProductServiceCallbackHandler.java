
/**
 * BelkProductServiceCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.4  Built on : Oct 21, 2016 (10:47:34 BST)
 */

    package com.belkinc.services.product;

    /**
     *  BelkProductServiceCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class BelkProductServiceCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public BelkProductServiceCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public BelkProductServiceCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for getPack method
            * override this method for handling normal response from getPack operation
            */
           public void receiveResultgetPack(
                    com.belkinc.services.product.BelkProductServiceStub.GetPackResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getPack operation
           */
            public void receiveErrorgetPack(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getItem method
            * override this method for handling normal response from getItem operation
            */
           public void receiveResultgetItem(
                    com.belkinc.services.product.BelkProductServiceStub.GetItemResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getItem operation
           */
            public void receiveErrorgetItem(java.lang.Exception e) {
            }
                


    }
    