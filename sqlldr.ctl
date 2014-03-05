load data
 infile 'd:/bcp.csv'
 append into table pimu_mapping2
 fields terminated by "," 
 (organizationid, edcoid, commonname)