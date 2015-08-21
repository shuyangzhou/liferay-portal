use master
dump transaction master with no_log
go
drop database lportal
go
create database lportal on master = "250m"
go
exec sp_dboption 'lportal', 'allow nulls by default' , true
go

exec sp_dboption 'lportal', 'select into/bulkcopy/pllsort' , true
go
