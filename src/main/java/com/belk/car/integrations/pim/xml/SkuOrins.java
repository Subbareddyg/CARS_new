package com.belk.car.integrations.pim.xml;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "skuOrins", propOrder = {
		"skuOrin"
})
public class SkuOrins {
    
    @XmlElement(name = "sku_orin", required = true)
    protected List<String> skuOrin;

    public List<String> getSkuOrin() {
        return skuOrin;
    }

    public void setSkuOrin(List<String> skuOrin) {
        this.skuOrin = skuOrin;
    }
    
    
}
