CREATE OR REPLACE PACKAGE RESYNC_ATTRIBUTES AS
  procedure process_all;
  procedure alter_session;
  procedure handle_classifications;
  procedure handle_departments;
  procedure handle_productTypes;
  procedure handle_attributes;
  procedure handle_attribute_values;
  procedure handle_chkbox_attribute_values;  
  procedure resynch_temp;
  procedure update_process_flag;
  procedure create_audit_trail;
END RESYNC_ATTRIBUTES;
/


CREATE OR REPLACE PACKAGE BODY RESYNC_ATTRIBUTES AS
  v_date varchar2(200);
  PROCEDURE process_all AS
    BEGIN
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> process_all..', 75)|| v_date);    
        alter_session;
        handle_classifications;
        handle_departments;
        handle_productTypes;
        handle_attributes;
        handle_attribute_values;
        handle_chkbox_attribute_values;
        resynch_temp;
        update_process_flag;
        create_audit_trail;
    END process_all;

/*
-----------------------------------------------------------------------------------------------
--// this Package does the Attribute Resynch by doing the following processes 
--// 1.Checks the Attribute_change_tracking table to get the list of attribute changes
     2.Inserts records into tmp_attribute_resync
     3.Deletes records in car_attributes tables
     4.Inserts records in car_attribute_tables
     5.Updates attribute values in car_attributes
     6.Deletes records in class_attribute,department_attribute,product_type_attribute,attribute
     7.Inserts records into attribute_resync_batch_audit
------------------------------------------------------------------------------------------------
*/
  PROCEDURE alter_session AS
    BEGIN
        execute immediate 'alter session enable parallel dml';
	
        delete from tmp_attribute_resync;
        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished alter_session..', 75)|| v_date);    
        
    END alter_session;
 
/*
-----------------------------------------------------------------------------------------------------

//this procedure handles the adding and deleted classifications, will insert records into temp table
  with action 'A' for added classifications and 'D' for deleted classifcations. 	
------------------------------------------------------------------------------------------------------
*/

  PROCEDURE handle_classifications AS
    BEGIN
        --handles deleted classification associations
        
        insert /*+ PARALLEL(a) */ into tmp_attribute_resync a
        select tmp_attribute_resync_seq.nextval, car.car_id,
        v_attribute_tracking.act_attr_id, attribute.blue_martini_attribute,car_attribute.car_attr_id,null, null, 'D',v_attribute_tracking.act_type,v_attribute_tracking.act_action, sysdate from
        car_attribute, car, vendor_style, v_attribute_tracking, attribute
        where act_processed = 'N'
        and lower(act_type) = 'classifications'
        and lower(act_action) = 'delete' 
        and car_attribute.car_id = car.car_id
        and car.vendor_style_id = vendor_style.vendor_style_id
        and car_attribute.attr_id = v_attribute_tracking.act_attr_id
	and v_attribute_tracking.act_attr_id = attribute.attr_id
        and vendor_style.class_id = v_attribute_tracking.act_new_value;
        
        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished handle_classifications deletes..', 75)|| v_date);    

        --handle added classification associations
       
        insert /*+ PARALLEL(a) */ into tmp_attribute_resync a
        select tmp_attribute_resync_seq.nextval, car.car_id,
        v_attribute_tracking.act_attr_id,attribute.blue_martini_attribute,null, null,null, 'A', v_attribute_tracking.act_type,v_attribute_tracking.act_action,sysdate from
        car, vendor_style , v_attribute_tracking, attribute
        where act_processed = 'N'
        and lower(act_type) = 'classifications'
        and lower(act_action) = 'add' 
	and v_attribute_tracking.act_attr_id = attribute.attr_id
        and car.vendor_style_id = vendor_style.vendor_style_id
        and vendor_style.class_id = v_attribute_tracking.act_new_value;
        
        commit;    
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished handle_classifications additions..', 75)|| v_date);    

    END handle_classifications;  

/*
-----------------------------------------------------------------------------------------------------

//this procedure handles the adding and deleted departments, will insert records into temp table
  with action 'A' for added departments and 'D' for deleted departments. 	
------------------------------------------------------------------------------------------------------
*/

  PROCEDURE handle_departments AS
    BEGIN
        --handle deleted department associations
       
        insert /*+ PARALLEL(a) */ into tmp_attribute_resync a
        select tmp_attribute_resync_seq.nextval, car.car_id,
        v_attribute_tracking.act_attr_id,attribute.blue_martini_attribute, car_attribute.car_attr_id, null, null, 'D',v_attribute_tracking.act_type,v_attribute_tracking.act_action, sysdate from
        car_attribute, car, v_attribute_tracking, attribute
        where act_processed = 'N'
        and lower(act_type) = 'departments'
        and lower(act_action) = 'delete' 
        and car_attribute.car_id = car.car_id
        and car_attribute.attr_id = v_attribute_tracking.act_attr_id
	and v_attribute_tracking.act_attr_id = attribute.attr_id
        and car.dept_id = v_attribute_tracking.act_new_value;
        
        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished handle_departments deletes..', 75)|| v_date);    

        --handle added classification associations
        
        insert /*+ PARALLEL(a) */ into tmp_attribute_resync a
        select tmp_attribute_resync_seq.nextval, car.car_id,
        v_attribute_tracking.act_attr_id,attribute.blue_martini_attribute, null, null, null, 'A', v_attribute_tracking.act_type,v_attribute_tracking.act_action,sysdate from
        car, v_attribute_tracking, attribute
        where act_processed = 'N'
        and lower(act_type) = 'departments'
        and lower(act_action) = 'add'
	and v_attribute_tracking.act_attr_id = attribute.attr_id
        and car.dept_id = v_attribute_tracking.act_new_value;
        
        commit;    
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished handle_departments additions..', 75)|| v_date);    

    END handle_departments;  

/*
-----------------------------------------------------------------------------------------------------

//this procedure handles the adding and deleted productTypes, will insert records into temp table
  with action 'A' for added productTypes and 'D' for deleted productTypes. 	
------------------------------------------------------------------------------------------------------
*/

    PROCEDURE handle_productTypes AS
    BEGIN
        --handle deleted productTypes associations
       
        insert /*+ PARALLEL(a) */ into tmp_attribute_resync a
        select tmp_attribute_resync_seq.nextval, car.car_id,
        v_attribute_tracking.act_attr_id,attribute.blue_martini_attribute, car_attribute.car_attr_id, null, null, 'D',v_attribute_tracking.act_type,v_attribute_tracking.act_action, sysdate from
        car_attribute, car, vendor_style, v_attribute_tracking, attribute
        where act_processed = 'N'
        and lower(act_type) = 'producttypes'
        and lower(act_action) = 'delete' 
        and car_attribute.car_id = car.car_id
        and car.vendor_style_id = vendor_style.vendor_style_id
        and car_attribute.attr_id = v_attribute_tracking.act_attr_id
	and v_attribute_tracking.act_attr_id = attribute.attr_id
        and vendor_style.product_type_id = v_attribute_tracking.act_new_value;
        
        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished handle_producttype deletes..', 75)|| v_date);    

        --handle added productTypes associations
       
        insert /*+ PARALLEL(a) */ into tmp_attribute_resync a
        select tmp_attribute_resync_seq.nextval, car.car_id,
        v_attribute_tracking.act_attr_id,attribute.blue_martini_attribute, null, null, null, 'A', v_attribute_tracking.act_type,v_attribute_tracking.act_action,sysdate from
        car, vendor_style, v_attribute_tracking, attribute
        where act_processed = 'N'
        and lower(act_type) = 'producttypes'
        and lower(act_action) = 'add' 
	and v_attribute_tracking.act_attr_id = attribute.attr_id
        and car.vendor_style_id = vendor_style.vendor_style_id
        and vendor_style.product_type_id = v_attribute_tracking.act_new_value;
        
        commit;    
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished handle_producttype additions..', 75)|| v_date);    

    END handle_productTypes;

/*
-----------------------------------------------------------------------------------------------------

//this procedure handles the deleted attributes, will insert records into temp table
  for deleted attributes. 	
------------------------------------------------------------------------------------------------------
*/
    
    PROCEDURE handle_attributes AS
    BEGIN
        
	insert /*+ PARALLEL(a) */ into tmp_attribute_resync a
        select tmp_attribute_resync_seq.nextval, car.car_id,
        v_attribute_tracking.act_attr_id,attribute.blue_martini_attribute, car_attribute.car_attr_id,null, null, 'D',v_attribute_tracking.act_type,v_attribute_tracking.act_action, sysdate from
        car_attribute, car,v_attribute_tracking, attribute
        where act_processed = 'N'
        and lower(act_type) = 'attributes'
        and lower(act_action) = 'delete' 
	and v_attribute_tracking.act_attr_id = attribute.attr_id
        and car_attribute.car_id = car.car_id
        and car_attribute.attr_id = v_attribute_tracking.act_attr_id;
        
        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished handle_attributes deletes..', 75)|| v_date);    
    
    -- adding following query to consider those removed attribute that are not associated with car using Resynch-attribute process. These attributes should also be deleted.
    insert /*+ PARALLEL(a) */ into tmp_attribute_resync a
    select tmp_attribute_resync_seq.nextval,null,
        v_attribute_tracking.act_attr_id,attribute.blue_martini_attribute, null,null, null, 'D',v_attribute_tracking.act_type,v_attribute_tracking.act_action, sysdate from
        v_attribute_tracking, attribute 
        where act_processed = 'N'
        and lower(act_type) = 'attributes'
        and lower(act_action) = 'delete' 
        and v_attribute_tracking.act_attr_id = attribute.attr_id ;

      commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished handle_attribute deletes 2 ..', 75)|| v_date);    

      	
    END handle_attributes;

/*
-----------------------------------------------------------------------------------------------------

//this procedure handles the edited and deleted attributeValues, will insert records into temp table
  with action 'U' for edit and update. 	
------------------------------------------------------------------------------------------------------
*/

   PROCEDURE handle_attribute_values AS
    BEGIN
        --handle edited attribute value
      
        insert /*+ PARALLEL(a) */ into tmp_attribute_resync a
        select tmp_attribute_resync_seq.nextval, car.car_id,
        v_attribute_tracking.act_attr_id,attribute.blue_martini_attribute, car_attribute.car_attr_id, v_attribute_tracking.act_old_value,v_attribute_tracking.act_new_value, 'U',v_attribute_tracking.act_type,v_attribute_tracking.act_action, sysdate from
        car_attribute, car,v_attribute_tracking,attribute, attribute_config
        where act_processed = 'N'
        and lower(act_type) = 'attributevalues'
        and lower(act_action) = 'edit' 
        and attribute_config.HTML_DISPLAY_TYPE_CD <> 'CHECKBOX'
        and car_attribute.car_id = car.car_id
	and v_attribute_tracking.act_attr_id = attribute.attr_id
        and car_attribute.attr_value = v_attribute_tracking.act_old_value
        and car_attribute.attr_id = v_attribute_tracking.act_attr_id
        and attribute.name = attribute_config.attribute_name;
        
        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished handle_attributevalues edits ..', 75)|| v_date);    

        --handle deleted attribute value
        
        insert /*+ PARALLEL(a) */ into tmp_attribute_resync a
        select tmp_attribute_resync_seq.nextval, car.car_id,
        v_attribute_tracking.act_attr_id,attribute.blue_martini_attribute, car_attribute.car_attr_id, v_attribute_tracking.act_new_value,null, 'U',v_attribute_tracking.act_type,v_attribute_tracking.act_action, sysdate from
        car_attribute, car,v_attribute_tracking,attribute, attribute_config
        where act_processed = 'N'
        and lower(act_type) = 'attributevalues'
        and lower(act_action) = 'delete' 
        and attribute_config.HTML_DISPLAY_TYPE_CD <> 'CHECKBOX'
        and car_attribute.car_id = car.car_id
	and v_attribute_tracking.act_attr_id = attribute.attr_id
        and car_attribute.attr_value = v_attribute_tracking.act_new_value
        and car_attribute.attr_id = v_attribute_tracking.act_attr_id
        and attribute.name = attribute_config.attribute_name;
        
        commit;  
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished handle_attributevalues deletes..', 75)|| v_date);    

    END handle_attribute_values; 

/*
-----------------------------------------------------------------------------------------------------

// this procedure handles checkbox attributeValues. these should be handled separately beause
// they are stored as comma-separated values and cannot be easily compared with the old value
// stored in the tracking table.
------------------------------------------------------------------------------------------------------
*/
  PROCEDURE handle_chkbox_attribute_values AS

  carAttrValueArray string_fnc.t_array;
  trackingAttrValueArray string_fnc.t_array;
  tmpStrArray string_fnc.t_array;
  
  car_id number(12);
  act_attr_id number(12);
  bm_attr_name varchar2(200);
  car_attr_id number(12);
  attr_value varchar2(2000);
  act_type varchar2(24);
  act_concat_value varchar2(4000);
  attr_old_value varchar2(2000);
  attr_new_value varchar2(2000);
  tmp_str varchar2(2000);
  
  attr_final_value varchar2(4000);
  
  cursor car_attribute_records is
  select a.car_id, a.attr_value, b.act_attr_id, b.blue_martini_attribute, 
  a.car_attr_id, b.act_concat_value, b.act_type
  from car_attribute a, 
  (
    select a.act_attr_id, b.blue_martini_attribute, 
    -- an attribute for a car might have multiple records - one deleting a value, one editing a value etc.
    -- example below shows two deleted values and one edited value for the same attribute 3469.
    -- 3469	attributeValues	Delete		Gift Sets	Y	Y	N
    -- 3469	attributeValues	Delete		Rollerballs	Y	Y	N
    -- 3469	attributeValues	Edit	After Shave	After Shave Revitalized	Y	Y	N    
    -- now the idea here is to join them into one record so that processing becomes easier.
    -- the result is a 'collapsed' record that looks like this:
    -- 3469	Facet_Type	^d-Gift Sets,^d-Rollerballs,^e-After Shave-~-After Shave Revitalized	attributeValues
    -- we use the following convention to determine the action: ^d indicates a value to be deleted
    -- ^e indicates an edited value. the old and new values of edited value are separted by '-~-'
    string_fnc.to_string(cast(collect(
      case 
      when lower(act_action) = 'delete' then '^d-'||a.act_new_value
      when lower(act_action) = 'edit' then '^e-'||a.act_old_value||'-~-'||a.act_new_value
      end
    ) as varchar2_ntt)) as act_concat_value, a.act_type
    from v_attribute_tracking a, attribute b, attribute_config c
    where act_processed = 'N'
    and lower(act_type) = 'attributevalues'
    and lower(act_action) in ('delete', 'edit')
    and c.HTML_DISPLAY_TYPE_CD = 'CHECKBOX'
    and a.act_attr_id = b.attr_id
    and b.name = c.attribute_name
    group by a.act_attr_id, b.blue_martini_attribute,a.act_type
  ) b
  where a.attr_id = b.act_attr_id
  order by car_id;
  begin
    for rec in car_attribute_records loop
      car_id := rec.car_id;
      act_attr_id := rec.act_attr_id;
      bm_attr_name := rec.blue_martini_attribute;
      car_attr_id := rec.car_attr_id;
      attr_value := rec.attr_value;
      act_type := rec.act_type;
      act_concat_value := rec.act_concat_value;
      attr_final_value := '';
      
      -- for each record returned by the query above, 
      -- 1. split the comma-separated car attribute table attr_value
      -- 2. split the concatenated tracking table old/new values
      -- 3. compare and determine if the car attribute attr_value 
      --    should be updated (some tokens might be deleted or updated).

      carAttrValueArray := string_fnc.split(attr_value,',');
      --example concat value: ^d-Gift Sets,^d-Rollerballs,^e-After Shave-~-After Shave Revitalized
      trackingAttrValueArray := string_fnc.split(act_concat_value,',');
      
      for i in 1..carAttrValueArray.count loop  
        attr_new_value := carAttrValueArray(i);
        for j in 1..trackingAttrValueArray.count loop  
         tmp_str := trackingAttrValueArray(j);
         IF instr(tmp_str, '^e-') > 0 THEN
          --expected format: starts with '^e', old and new values delimited by '-~-'
          tmpStrArray := string_fnc.split(tmp_str,'^e-');
          tmpStrArray := string_fnc.split(tmpStrArray(2),'-~-');
           IF attr_new_value = tmpStrArray(1)  THEN
            attr_new_value := tmpStrArray(2);
           END IF;
         -- else handle deleted values
         ELSE
          tmpStrArray := string_fnc.split(tmp_str,'^d-');
          IF attr_new_value = tmpStrArray(2)  THEN
            --delete the value
            attr_new_value := '';
          END IF;
          
         END IF;
        end loop;
        
        IF attr_final_value is null THEN
          --this condition ensures that there are no leading or trailing commas
          attr_final_value := attr_new_value;
        ELSIF attr_new_value is not null THEN
          --append the attribute value from each iteration
          attr_final_value := attr_final_value || ',' || attr_new_value;
        ELSE
          attr_final_value := attr_final_value ;
        END IF;
      end loop;
      IF (nvl(attr_value, 'a') <> nvl(attr_final_value, 'a')) THEN
        IF attr_final_value is null THEN
          insert into tmp_attribute_resync values (tmp_attribute_resync_seq.nextval, car_id, act_attr_id, bm_attr_name,
            car_attr_id, attr_value, attr_final_value, 'U', 'attributeValues', 'Delete', SYSDATE);
          commit;
        ELSE
          insert into tmp_attribute_resync values (tmp_attribute_resync_seq.nextval, car_id, act_attr_id, bm_attr_name,
            car_attr_id, attr_value, attr_final_value, 'U', 'attributeValues', 'Edit', SYSDATE);
          commit;
        END IF;
      END IF;
      --dbms_output.put_line(car_id || ': ' || attr_value || '->' || attr_final_value);       
    end loop;
    select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
    Dbms_Output.put_line(rpad('>>>> finished handle_chkxbox_attribute_values ..', 75)|| v_date);    
    
  end handle_chkbox_attribute_values;

/*
-----------------------------------------------------------------------------------------------------

//this procedure removes redundant values from the tmp_attribute_resync table where old and new values
//are the same. this is easier than handling this situation in all the previous procedures.
//COMMENTED OUT BECAUSE WITHOUT ADDITIONAL FILTERS THIS PROCEDURE CREATES ISSUES
//TODO: ADD ADDITIONAL FILTERS
//THIS SHOULD BE CALLED AFTER handle_chkbox_attribute_values PROCEDURE.
------------------------------------------------------------------------------------------------------
*/
/*  
    PROCEDURE remove_redundant_tmp_records AS
    BEGIN
         delete from tmp_attribute_resync a
         where nvl(attr_old_value, 'a') = nvl(attr_value, 'a');
	  
         commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished remove_redundant_tmp_records ..', 75)|| v_date);    

    END remove_redundant_tmp_records;
*/
  
/*
-----------------------------------------------------------------------------------------------------

//this procedure resycn the car_attribute, class_attribute,department_attribute,product_type_attribute
attribute tables.
------------------------------------------------------------------------------------------------------
*/
    PROCEDURE resynch_temp AS
    BEGIN
        
        
      --handle 'D' delete
         delete /*+PARALLEL(a) */ from car_attribute a
         where a.car_attr_id in (select car_attr_id from tmp_attribute_resync b where b.action='D');
	  
         commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp delete car_attributes ..', 75)|| v_date);    

	
         delete /*+PARALLEL(a) */ from class_attribute a
         where a.attr_id in (select attr_id from tmp_attribute_resync b where 
			   b.action='D'
			   and lower(b.act_type) = 'attributes'
			   and lower(b.act_action) = 'delete');

        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp delete class_attributes ..', 75)|| v_date);    

         delete /*+PARALLEL(a) */ from department_attribute a
         where a.attr_id in (select attr_id from tmp_attribute_resync b where 
			   b.action='D'
			   and lower(b.act_type) = 'attributes'
			   and lower(b.act_action) = 'delete');

         commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp delete department_attributes..', 75)|| v_date);    

         delete /*+PARALLEL(a) */ from product_type_attribute a
         where a.attr_id in (select attr_id from tmp_attribute_resync b where 
			   b.action='D'
			   and lower(b.act_type) = 'attributes'
			   and lower(b.act_action) = 'delete');
         commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp delete product_type_attributes ..', 75)|| v_date);    

         delete /*+PARALLEL(a) */ from attribute_lookup_value a
         where a.attr_id in (select attr_id from tmp_attribute_resync b where 
			   b.action='D'
			   and lower(b.act_type) = 'attributes'
			   and lower(b.act_action) = 'delete');

         commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp delete attribute_lookup_values ..', 75)|| v_date);    

        delete /*+PARALLEL(a) */ from car_sku_attribute a
        where a.attr_id in (select attr_id from tmp_attribute_resync b where 
			   b.action='D'
			   and lower(b.act_type) = 'attributes'
			   and lower(b.act_action) = 'delete');

        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp delete car_sku_attributes ..', 75)|| v_date);    

        delete /*+PARALLEL(a) */ from outfit_car_attribute a
        where a.attr_id in (select attr_id from tmp_attribute_resync b where 
			   b.action='D'
			   and lower(b.act_type) = 'attributes'
			   and lower(b.act_action) = 'delete');

        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp delete outfit_car_attributes ..', 75)|| v_date);    

      	delete /*+PARALLEL(a) */ from promo_car_attribute a
      	where a.attr_id in (select attr_id from tmp_attribute_resync b where 
			   b.action='D'
			   and lower(b.act_type) = 'attributes'
			   and lower(b.act_action) = 'delete');

      	commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp delete promo_car_attributes ..', 75)|| v_date);    
	
      
	--handle'U' delete value

        merge /*+ PARALLEL(a) */ into car_attribute a
        using tmp_attribute_resync b
        on (a.car_attr_id = b.car_attr_id
        and b.action='U'
        and b.attr_value is null)
        when matched then
        update set a.attr_value = REPLACE(a.attr_value, b.attr_old_value, ''),
        a.UPDATED_DATE = sysdate;	

        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp merge car_attribute deletes ..', 75)|| v_date);    

      --handle'U' edit/update value

        merge /*+ PARALLEL(a) */ into car_attribute a
        using tmp_attribute_resync b
        on (a.car_attr_id = b.car_attr_id
        and b.action='U'
        and b.attr_value is not null)
        when matched then
        update set a.attr_value = REPLACE(a.attr_value, b.attr_old_value, b.attr_value),
        a.UPDATED_DATE = sysdate;	

        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp merge car_attribute updates..', 75)|| v_date);    


      --handle'A' insert
	
        merge /*+ PARALLEL(a) */ into car_attribute a
        using (select distinct attr_id,car_id, attr_value from tmp_attribute_resync where  action = 'A' and attr_id not in
        (
          select act_attr_id from attribute_change_tracking
          where act_type = 'attributes' and lower(act_action) = 'delete'
          and act_processed = 'N'
        )
        ) b
        on ( a.attr_id = b.attr_id and a.car_id = b.car_id)
        when not matched then
        insert (a.CAR_ID, a.ATTR_ID, a.CAR_ATTR_ID, a.ATTR_VALUE, a.HAS_CHANGED, a.IS_CHANGE_REQUIRED, a.STATUS_CD, a.DISPLAY_SEQ, a.CREATED_BY, a.UPDATED_BY, a.CREATED_DATE, a.UPDATED_DATE, a.ATTR_VALUE_PROCESS_STATUS_CD)
        values (b.car_id, b.attr_id, CAR_ATTRIBUTE_SEQ.nextval, b.attr_value, 'N', 'Y', 'ACTIVE', 0, 'carsadmin@belk.com','carsadmin@belk.com',sysdate, sysdate, 'NO_CHECK_REQUIRED');	

      	commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp merge car_attribute inserts..', 75)|| v_date);    

        delete /*+PARALLEL(a) */ from attribute a
        where a.attr_id in (select attr_id from tmp_attribute_resync b where 
			   b.action='D'
			   and lower(b.act_type) = 'attributes'
			   and lower(b.act_action) = 'delete');

        commit;
        
        delete from attribute_config a
        where a.attribute_name in (select attr_name from tmp_attribute_resync b where 
			   b.action='D'
			   and lower(b.act_type) = 'attributes'
			   and lower(b.act_action) = 'delete');
        
        commit;
        
        delete from attribute_lookup_value a
        where a.attr_id in (select attr_id from tmp_attribute_resync b where 
			   b.action='D'
			   and lower(b.act_type) = 'attributes'
			   and lower(b.act_action) = 'delete');
        
        commit;
        
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp hard delete attributes ..', 75)|| v_date);    

        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished resync_temp procedure all ..', 75)|| v_date);    
	
   END resynch_temp; 

/*
-----------------------------------------------------------------------------------------------------

//this procedure update the attribute_change_tracking table coumn act_processed flag from 'N' to 'Y'. 	
------------------------------------------------------------------------------------------------------
*/
   
   PROCEDURE update_process_flag AS
    BEGIN
      update attribute_change_tracking set act_processed='Y' where act_processed='N';

      commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished update_process_flag ..', 75)|| v_date);    
	
    END update_process_flag; 

/*
-----------------------------------------------------------------------------------------------------

//this procedure inserts records into audit table from the temp table created for attribute resync.
------------------------------------------------------------------------------------------------------
*/
    PROCEDURE create_audit_trail AS 
     batch_id integer;
     BEGIN
        select ATTRIBUTE_RESYNC_AUDIT_SEQ.nextval into batch_id from dual;
        execute immediate 'alter session enable parallel dml';
        insert /*+ PARALLEL(a) */ into ATTRIBUTE_RESYNC_BATCH_AUDIT a
        select batch_id, a.* from tmp_attribute_resync a;

        commit;
        select TO_CHAR(sysdate, 'YYYY-MM-DD HH24:MI:SS') into v_date from dual;
        Dbms_Output.put_line(rpad('>>>> finished create_audit_trail ..', 75)|| v_date);    
        
     END create_audit_trail;

END RESYNC_ATTRIBUTES;
/

