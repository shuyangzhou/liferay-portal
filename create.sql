use master
dump transaction master with no_log
go
if exists (select * from master.dbo.sysdatabases where name = "lportal")
begin
drop database lportal
end
go
create database lportal on master = "250m"
go
exec sp_dboption 'lportal', 'allow nulls by default' , true
go

exec sp_dboption 'lportal', 'select into/bulkcopy/pllsort' , true
go

use lportal
go
