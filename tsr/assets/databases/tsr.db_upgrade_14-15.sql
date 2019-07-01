alter table Hobby rename to Hobby_temp;
CREATE TABLE [Hobby] (
	[HobbyCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[HobbyName]	nvarchar(250) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([HobbyCode])

);
insert into Hobby(HobbyCode,HobbyName,SyncedDate) select HobbyCode, HobbyName,SyncedDate from Hobby_temp;
drop table  Hobby_temp;



