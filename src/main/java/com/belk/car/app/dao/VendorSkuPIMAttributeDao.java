/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belk.car.app.dao;

import com.belk.car.app.model.Car;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author afupxs5
 */
public interface VendorSkuPIMAttributeDao extends CachedQueryDao {
    
    Map<Long, Set<VendorSkuPIMAttribute>> getPIMAttributesForVendorSku(Car car);
    
    Set<VendorSkuPIMAttribute> getPIMAttributesForVendorSku(Long vendorSkuId);
    
    void save(Set<VendorSkuPIMAttribute> vendorSkuPIMAttributes);
}
