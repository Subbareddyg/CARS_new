-- product info 
select car.car_id as "carID", 
	vendor.name as "vendorName",  
	vendor.vendor_number as "vendorNumber",  
	vendor_style.vendor_style_number as "styleNumber",  
	vendor_style.vendor_style_name as "productName",  
	product_type.name as "productType",  
	brand as "brand",
	department.dept_id as "departmentID",  
	department.name as "departmentName",   
	classification.class_id as "classID",  
	classification.name as "className",
    sample_notes.photo_instructions as "photoInstructions" 
from  
	car  
		left outer join 
		(select car_id, attr_value as brand from car_attribute where car_attribute.attr_id = 2232 ) ca on ca.car_id = car.car_id
        left outer join
        (select car_id, note_text as photo_instructions from car_note where note_type_cd = 'SAMPLE_NOTES') sample_notes on sample_notes.car_id = car.car_id
	,vendor_style  
		left outer join product_type on vendor_style.product_type_id = product_type.product_type_id  
	,vendor  
	,classification  
	,department  
where  
	--car.car_id in ( :carIDs )
    --car.car_id = 425 -- for testing   
	--and  
	car.vendor_style_id = vendor_style.vendor_style_id  
	and  
	vendor_style.vendor_id = vendor.vendor_id  
	and  
	vendor_style.class_id = classification.class_id  
	and  
	classification.dept_id = department.dept_id




-- sample info 
select  
	s.car_id as "carID",  		
	s.sample_id as "sampleID",  
	s.sample_type_cd as "type",  
	s.swatch_color as "colorCode",  
	'?' as "colorName",  
	s.is_returnable as "returnRequested",  
	shipping_type.name as "carrier",  
	s.shipper_account_number as "shippingAccountNumber",  
    return_notes.return_instructions as "returnInstructions"
from  
	(  
		select  
			car_sample.car_id,  
			sample.sample_id,  
			sample.sample_type_cd,  
			sample.swatch_color,  
			sample.is_returnable,
            sample.shipping_type_cd,
            sample.shipper_account_number
		from    
			sample, car_sample  
		where  
			sample.sample_id = car_sample.sample_id  
			and  
			sample.SAMPLE_TRACKING_STATUS_CD = 'REQUESTED'  
			and  
			sample.STATUS_CD = 'ACTIVE'  
	)  
	s  
	left outer join  
		shipping_type on shipping_type.SHIPPING_TYPE_CD = s.SHIPPING_TYPE_CD -- = ship_info.SHIPPING_TYPE_CD
    left outer join
        (select car_id, note_text as return_instructions from car_note where note_type_cd = 'RETURN_NOTES') return_notes on return_notes.car_id = s.car_id
    


    left outer join
        (select car_id, note_text as return_instructions from car_note where note_type_cd = 'RETURN_NOTES') return_notes on return_notes.car_id = s.car_id
    left outer join
        (select car_id, note_text as photo_instructions from car_note where note_type_cd = 'SAMPLE_NOTES') sample_notes on sample_notes.car_id = s.car_id
