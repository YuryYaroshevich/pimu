load data
  infile 'd:\bcpf.csv'
  append into table pimu_mapping1
  fields terminated by ","
  (organizationid, edcoid, commonname)