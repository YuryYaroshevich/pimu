@echo off
cd c:\oraclexe\app\oracle\product\10.2.0\server\BIN
sqlldr system/1234@//localhost:1521/xe control=D:\workspace\pimu\sqlloader.ctl log=D:\workspace\pimu\sqllog.log rows=330
