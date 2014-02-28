select count(*) from pimu_mapping1;
select count(*) from pimu_mapping2;
delete from pimu_mapping1;
delete from pimu_mapping2;

 CREATE TABLE "SYSTEM"."PIMU_STATUS" 
   (	"EDCOIDTABLE" VARCHAR2(30 BYTE) NOT NULL ENABLE, 
	"STATUS" VARCHAR2(1 BYTE) NOT NULL ENABLE, 
	"REFRESH_TIME" DATE
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "SYSTEM" ;
  
  
  CREATE TABLE "SYSTEM"."PIMU_MAPPING1" 
   (	"EDCOID" VARCHAR2(30 BYTE) NOT NULL ENABLE, 
	"ORGANIZATIONID" NUMBER NOT NULL ENABLE, 
	"COMMONNAME" VARCHAR2(200 BYTE) NOT NULL ENABLE
   ) PCTFREE 10 PCTUSED 40 INITRANS 1 MAXTRANS 255 NOCOMPRESS LOGGING
  STORAGE(INITIAL 65536 NEXT 1048576 MINEXTENTS 1 MAXEXTENTS 2147483645
  PCTINCREASE 0 FREELISTS 1 FREELIST GROUPS 1 BUFFER_POOL DEFAULT)
  TABLESPACE "SYSTEM" ;