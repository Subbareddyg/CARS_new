CREATE OR REPLACE PACKAGE STRING_FNC 
IS 
TYPE t_array IS TABLE OF VARCHAR2(4000) 
   INDEX BY BINARY_INTEGER; 
FUNCTION SPLIT (p_in_string VARCHAR2, p_delim VARCHAR2) RETURN t_array; 
FUNCTION to_string(nt_in IN varchar2_ntt, delimiter_in IN VARCHAR2 DEFAULT ',') RETURN VARCHAR2; 
END; 
/


CREATE OR REPLACE PACKAGE BODY STRING_FNC 
IS 
   FUNCTION SPLIT (p_in_string VARCHAR2, p_delim VARCHAR2) RETURN t_array  
   IS 
      i       number :=0; 
      pos     number :=0; 
      lv_str  varchar2(4000) := p_in_string; 
   strings t_array; 
   BEGIN 
      -- determine first chuck of string   
      pos := instr(lv_str,p_delim,1,1); 
      -- while there are chunks left, loop  
      WHILE ( pos != 0) LOOP 
         -- increment counter  
         i := i + 1; 
         -- create array element for chuck of string  
         strings(i) := trim(substr(lv_str,1, pos - 1)); 
         -- remove chunk from string  
         lv_str := substr(lv_str,pos+length(p_delim),length(lv_str)); 
         -- determine next chunk  
         pos := instr(lv_str,p_delim,1,1); 
         -- no last chunk, add to array  
         IF pos = 0 THEN 
            strings(i+1) := trim(lv_str); 
         END IF;
      END LOOP; 
      -- if thre is no delimiter in the string, ensure that we return the original string as-is
      IF strings.count = 0 THEN
        strings(1) := lv_str;
      END IF;
      -- return array  
      RETURN strings; 
   END SPLIT; 
   
  FUNCTION to_string (
                  nt_in        IN varchar2_ntt,
                  delimiter_in IN VARCHAR2 DEFAULT ','
                  ) RETURN VARCHAR2 IS
  
     v_idx PLS_INTEGER;
     v_str VARCHAR2(32767);
     v_dlm VARCHAR2(10);
  
  BEGIN
  
     v_idx := nt_in.FIRST;
     WHILE v_idx IS NOT NULL LOOP
        v_str := v_str || v_dlm || nt_in(v_idx);
        v_dlm := delimiter_in;
        v_idx := nt_in.NEXT(v_idx);
     END LOOP;
  
     RETURN v_str;
  
  END to_string;

END;
/

