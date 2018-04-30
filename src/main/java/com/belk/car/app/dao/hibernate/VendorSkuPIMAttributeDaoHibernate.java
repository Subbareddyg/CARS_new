/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.belk.car.app.dao.hibernate;

import com.belk.car.app.dao.VendorSkuPIMAttributeDao;
import com.belk.car.app.model.Car;
import com.belk.car.app.model.VendorSku;
import com.belk.car.app.model.VendorSkuPIMAttribute;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.appfuse.dao.hibernate.UniversalDaoHibernate;

/**
 *
 * @author afupxs5
 */
public class VendorSkuPIMAttributeDaoHibernate extends UniversalDaoHibernate implements VendorSkuPIMAttributeDao {

    public Map<Long, Set<VendorSkuPIMAttribute>> getPIMAttributesForVendorSku(Car car) {
        if (car == null) {
            throw new IllegalArgumentException("Car object cannot be null for query");
        }
        Map<Long, Set<VendorSkuPIMAttribute>> pimAttributesForSku 
                = new HashMap<Long, Set<VendorSkuPIMAttribute>>();
        Set<VendorSku> vendorSkusForCar = car.getVendorSkus();
        if (vendorSkusForCar != null && !vendorSkusForCar.isEmpty()) {
            for (VendorSku vendorSku :  vendorSkusForCar) {
                pimAttributesForSku.put(vendorSku.getCarSkuId(), vendorSku.getSkuPIMAttributes());
            }
        }
        return pimAttributesForSku;
    }

    public Set<VendorSkuPIMAttribute> getPIMAttributesForVendorSku(Long vendorSkuId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List getAll(Class type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Object get(Class type, Serializable srlzbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Object save(VendorSkuPIMAttribute skuPIMAttribute) {
        if (skuPIMAttribute != null) {
            getHibernateTemplate().saveOrUpdate(skuPIMAttribute);
            getHibernateTemplate().flush();
        }
        return skuPIMAttribute;
    }
    
    public void save(Set<VendorSkuPIMAttribute> vendorSkuPIMAttributes) {
        if (vendorSkuPIMAttributes != null) {
            getHibernateTemplate().saveOrUpdateAll(vendorSkuPIMAttributes);
            getHibernateTemplate().flush();
        }
    }

    public void remove(Class type, Serializable srlzbl) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
