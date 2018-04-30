package com.belk.car.integrations.rrd.xml.vendorImage;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "vendorImageHistories"
})
@XmlRootElement(name = "carsmessage")
public class VendorImageHistoryMessageXML {

    @XmlElement(name = "VendorImageHistories", required = true)
    protected VendorImageHistoryMessageXML.VendorImageHistories vendorImageHistories;
    @XmlAttribute
    protected String from;
    @XmlAttribute
    protected String to;
    @XmlAttribute
    protected String type;
  
    public VendorImageHistoryMessageXML.VendorImageHistories getVendorImageHistories() {
        return vendorImageHistories;
    }   
    public void setVendorImageHistories(VendorImageHistoryMessageXML.VendorImageHistories value) {
        this.vendorImageHistories = value;
    }
    
    public String getFrom() {
        return from;
    }  
    public void setFrom(String value) {
        this.from = value;
    }
   
    public String getTo() {
        return to;
    }  
    public void setTo(String value) {
        this.to = value;
    }
    
    public String getType() {
        return type;
    }   
    public void setType(String value) {
        this.type = value;
    }


    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "vendorImageXML"
    })
    public static class VendorImageHistories {
        @XmlElement(name = "VendorImage", required = true)
        protected List<VendorImageHistoryMessageXML.VendorImageHistories.VendorImageXML> vendorImageXML;
        
        public List<VendorImageHistoryMessageXML.VendorImageHistories.VendorImageXML> getVendorImageXML() {
            if (vendorImageXML == null) {
                vendorImageXML = new ArrayList<VendorImageHistoryMessageXML.VendorImageHistories.VendorImageXML>();
            }
            return this.vendorImageXML;
        }
      
        @XmlAccessorType(XmlAccessType.FIELD)
        @XmlType(name = "", propOrder = {
            "name"
        })
        public static class VendorImageXML {

            @XmlElement(required = true)
            protected String name;
            @XmlAttribute
            protected Integer id;
           
            public String getName() {
                return name;
            }           
            public void setName(String value) {
                this.name = value;
            }
           
            public Integer getId() {
                return id;
            }            
            public void setId(Integer value) {
                this.id = value;
            }

        }

    }

}

