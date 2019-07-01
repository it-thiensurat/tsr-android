ALTER TABLE Contract ADD COLUMN PreSaleEmployeeName nvarchar(250) COLLATE NOCASE;

INSERT INTO "ImageType" VALUES('CUSTOMER','CUSTOMER',NULL);

ALTER TABLE Contract ADD COLUMN EmployeeHistoryID NVARCHAR(50);

CREATE TABLE [TradeInStock] (
	[TradeInStockID]	varchar(50) NOT NULL COLLATE NOCASE,
	[EFFDATE]	datetime,
	[SaleCode]	varchar(50) COLLATE NOCASE,
	[SaleName]	nvarchar(250) COLLATE NOCASE,
	[SubTeamCode]	varchar(50) COLLATE NOCASE,
	[SubTeamName]	nvarchar(250) COLLATE NOCASE,
	[TeamHeadCode]	varchar(50) COLLATE NOCASE,
	[TeamHeadName]	nvarchar(250) COLLATE NOCASE,
	[SupervisorCode]	varchar(50) COLLATE NOCASE,
	[SupervisorName]	nvarchar(250) COLLATE NOCASE,
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[OrganizationName]	nvarchar(250) COLLATE NOCASE,
	[CustomerName]	nvarchar(250) COLLATE NOCASE,
	[CusAddress]	nvarchar(250) COLLATE NOCASE,
	[CusTel]	varchar(50) COLLATE NOCASE,
	[ProductSerialNumber]	varchar(50) COLLATE NOCASE,
	[ProductPrice]	float,
	[Mode]	integer,
	[TradeInProductCode]	varchar(50) COLLATE NOCASE,
	[TradeInBrandCode]	varchar(50) COLLATE NOCASE,
	[TradeInProductModel]	nvarchar(250) COLLATE NOCASE,
	[TradeInDiscount]	float,
    PRIMARY KEY ([TradeInStockID])

);

ALTER TABLE TradeInBrand ADD COLUMN CreateDate DATETIME;
ALTER TABLE TradeInBrand ADD COLUMN  CreateBy NVARCHAR(250);
ALTER TABLE TradeInBrand ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE TradeInBrand ADD COLUMN LastUpdateBy NVARCHAR(250);


DROP TABLE IF EXISTS "OrgUnitType";
CREATE TABLE [OrgUnitType] (
	[OrgUnitTypeCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[OrgUnitTypeName]	nvarchar(150) NOT NULL COLLATE NOCASE,
	[DisplayOrder]	integer,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([OrgUnitTypeCode], [OrgUnitTypeName])

);
INSERT INTO "OrgUnitType" VALUES('DEPARTMENT','????',2,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "OrgUnitType" VALUES('DRIVER','????????????',8,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "OrgUnitType" VALUES('FULL-BRANCH','Full Branch',1,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "OrgUnitType" VALUES('HR','??????????????????????????',9,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "OrgUnitType" VALUES('SALE','??????????',7,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "OrgUnitType" VALUES('SALE-SUB-TEAM','????????',6,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "OrgUnitType" VALUES('SALETEAM','???',5,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "OrgUnitType" VALUES('SUB-DEPARTMENT','???',3,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "OrgUnitType" VALUES('SUP','???????????????',4,NULL,NULL,NULL,NULL,NULL);


DROP TABLE IF EXISTS "PositionNode";
CREATE TABLE [PositionNode] (
	[PositionNodeID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-','',
	[ParentPositionNodeID]	varchar(50) COLLATE NOCASE,
	[EmployeeCode]	varchar(50) COLLATE NOCASE,
	[PositionCode]	varchar(50) COLLATE NOCASE,
	[EmployeeTypeCode]	varchar(50) COLLATE NOCASE,
	[OrgUnitCode]	varchar(50) COLLATE NOCASE,
	[OrgUnitName]	nvarchar(250) COLLATE NOCASE,
	[OrgUnitTypeCode]	varchar(50) COLLATE NOCASE,
	[HistoryID]	varchar(50) COLLATE NOCASE,
	[DisplayOrder]	integer,
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([PositionNodeID])

);
INSERT INTO "PositionNode" VALUES('21DD6AEC518B42B7976289AA7D8934E8',NULL,'38EDAA3F7093487484DD20403BFC72DD','A00002','LineManager',NULL,'SUB-DEPARTMENT-A',NULL,'SUB-DEPARTMENT','E569A98F80EC4EC8B41B8711F61D28B7',4,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('23CF6746BADA4DFBADEC007A46FDEC8F',NULL,'D3766E56C050478E915E8D522981FAC4','P00005','SubTeamLeader',NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('25E37F4E09EA43149030BCD267A1572F',NULL,'380CA201DD5F47FE88E64AC6D713A701','P00015','Supervisor','    ','SUP-PAR',NULL,'SUP','799BA709511A42FAB7B304EAC40F4C1A',106,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('28D7FCF2AF99492FAE5D0BE0E093CBF7',NULL,'380CA201DD5F47FE88E64AC6D713A701','P00003','Supervisor',NULL,'SUP-PAK',NULL,'SUP','2241895543F143DC896D8B00189941B2',5,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('380CA201DD5F47FE88E64AC6D713A701',NULL,'38EDAA3F7093487484DD20403BFC72DD','P00002','LineManager',NULL,'SUB-DEPARTMENT-P',NULL,'SUB-DEPARTMENT','DB6D2829579C4C8B974980DEE255B65E',3,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('38EDAA3F7093487484DD20403BFC72DD',NULL,'TSR-HEAD','P00001','Manager',NULL,'DEPARTMENT-SALE',NULL,'DEPARTMENT','660B114683F44DEC836B32320AF1F51C',2,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('3F10AB513AE34F3887FAB1380B8096F5',NULL,'ECE7594F519549E7B0943E1BFD2BF629','P00009','Sale',NULL,'SALE-SUB-TEAM-PAK-2301',NULL,'SALE-SUB-TEAM','4180D77266B7408CB0A9544A989EA8BB',11,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('419032B844DE4B049726D9C96EAF2530',NULL,'ECE7594F519549E7B0943E1BFD2BF629','P00006','Driver',NULL,'SALE-SUB-TEAM-PAK-2301',NULL,'SALE-SUB-TEAM','CC057AC2461C450E8B4B523DEFF6DD88',8,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('44615B5A165F4DE49C4C7BB216F20C56',NULL,'9B256A3BDF3B4AD5919EEEFA1BA27A5B','P00011','Driver',NULL,'SALE-SUB-TEAM-PAK-2302',NULL,'SALE-SUB-TEAM','346DB713AEF94625BED14D2EEE64FB07',13,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('59F0473767AA40C791D2B8D500ED47CA',NULL,'380CA201DD5F47FE88E64AC6D713A701','P00031','Supervisor',NULL,'SUP-PAX',NULL,'SUP','1BAAE77737A24151B8E6B8A9383D39D6',107,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('90AABCE8745A40FE87F28F7ECA06203C',NULL,'380CA201DD5F47FE88E64AC6D713A701','P00065','Supervisor',NULL,'SUP-PAZ',NULL,'SUP','9861582DB0294FD5951CD66C097F8A7A',109,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('995636433FB0405691E745071E3AEF0D',NULL,'9B256A3BDF3B4AD5919EEEFA1BA27A5B','P00013','Sale',NULL,'SALE-SUB-TEAM-PAK-2302',NULL,'SALE-SUB-TEAM','306DD14718D94581A04F4EA104D03A25',15,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('9B256A3BDF3B4AD5919EEEFA1BA27A5B',NULL,'D3766E56C050478E915E8D522981FAC4','P00010','SubTeamLeader',NULL,'SALE-SUB-TEAM-PAK-2302',NULL,'SALE-SUB-TEAM','6322FE04528E4A02A3EA7D1D5E5443C6',12,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('A8B49D1577D8417E8B47C487B1C1F327',NULL,'ECE7594F519549E7B0943E1BFD2BF629','P00007','Sale',NULL,'SALE-SUB-TEAM-PAK-2301',NULL,'SALE-SUB-TEAM','46952B8E48474B4B82E95F72E9430CD4',9,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('BD161C7666E24AE2B4CCD48D6B6554D0',NULL,'380CA201DD5F47FE88E64AC6D713A701','P00043','Supervisor',NULL,'SUP-PAY',NULL,'SUP','92EF6EB5478547399E569399EDF4DCF3',108,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('D3766E56C050478E915E8D522981FAC4',NULL,'28D7FCF2AF99492FAE5D0BE0E093CBF7','P00004','SaleLeader',NULL,'SALETEAM-PAK-23',NULL,'SALETEAM','5CD644B290D6456BA33C8CE02948CF39',6,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('E5D4879547E544D78EA949F1D0BAF24A',NULL,'ECE7594F519549E7B0943E1BFD2BF629','P00008','Sale',NULL,'SALE-SUB-TEAM-PAK-2301',NULL,'SALE-SUB-TEAM','38DE2CCABCEB4191BA7F3C5F906B87AD',10,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('ECE7594F519549E7B0943E1BFD2BF629',NULL,'D3766E56C050478E915E8D522981FAC4','P00005','SubTeamLeader',NULL,'SALE-SUB-TEAM-PAK-2301',NULL,'SALE-SUB-TEAM','F2B8F4930DDE45B4ACCE3E6B5E25587D',7,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('ED155AD54E4F4A00B9D4B352F6420FDC',NULL,'9B256A3BDF3B4AD5919EEEFA1BA27A5B','P00014','Sale',NULL,'SALE-SUB-TEAM-PAK-2302',NULL,'SALE-SUB-TEAM','EBD7D77440824723A904AA37F0FB3362',16,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('FC8F14CC7F6F4DD191EC9D8E023BEDB0',NULL,'9B256A3BDF3B4AD5919EEEFA1BA27A5B','P00012','Sale',NULL,'SALE-SUB-TEAM-PAK-2302',NULL,'SALE-SUB-TEAM','BB5B4B4426C340E0B782589DA9ECBB1F',14,NULL,NULL,NULL,NULL,NULL,NULL);
INSERT INTO "PositionNode" VALUES('TSR-HEAD',NULL,NULL,NULL,NULL,NULL,'HEAD',NULL,'FULL-BRANCH','E4BE45A93334400C943F33F3EA534C43',1,NULL,NULL,NULL,NULL,NULL,NULL);




CREATE TABLE [PositionNodeHistory] (
	[PositionNodeHistoryID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-','',
	[HistoryID]	varchar(50) NOT NULL COLLATE NOCASE,
	[OwnerEmployeeCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[StartDate]	datetime NOT NULL,
	[EndDate]	datetime NOT NULL,
	[PositionNodeID]	varchar(50) NOT NULL COLLATE NOCASE,
	[ParentPositionNodeID]	varchar(50) COLLATE NOCASE,
	[EmployeeCode]	varchar(50) COLLATE NOCASE,
	[PositionCode]	varchar(50) COLLATE NOCASE,
	[EmployeeTypeCode]	varchar(50) COLLATE NOCASE,
	[OrgUnitCode]	varchar(50) COLLATE NOCASE,
	[OrgUnitTypeCode]	varchar(50) COLLATE NOCASE,
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([PositionNodeHistoryID])

);

ALTER TABLE Bank ADD COLUMN CreateDate DATETIME;
ALTER TABLE Bank ADD COLUMN  CreateBy NVARCHAR(250);
ALTER TABLE Bank ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE Bank ADD COLUMN LastUpdateBy NVARCHAR(250);

ALTER TABLE HabitatType ADD COLUMN CreateDate DATETIME;
ALTER TABLE HabitatType ADD COLUMN  CreateBy NVARCHAR(250);
ALTER TABLE HabitatType ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE HabitatType ADD COLUMN LastUpdateBy NVARCHAR(250);

ALTER TABLE Career ADD COLUMN CreateDate DATETIME;
ALTER TABLE Career ADD COLUMN  CreateBy NVARCHAR(250);
ALTER TABLE Career ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE Career ADD COLUMN LastUpdateBy NVARCHAR(250);

ALTER TABLE Hobby ADD COLUMN CreateDate DATETIME;
ALTER TABLE Hobby ADD COLUMN  CreateBy NVARCHAR(250);
ALTER TABLE Hobby ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE Hobby ADD COLUMN LastUpdateBy NVARCHAR(250);

ALTER TABLE Suggestion ADD COLUMN CreateDate DATETIME;
ALTER TABLE Suggestion ADD COLUMN  CreateBy NVARCHAR(250);
ALTER TABLE Suggestion ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE Suggestion ADD COLUMN LastUpdateBy NVARCHAR(250);

ALTER TABLE Employee ADD COLUMN Status NVARCHAR(10);

ALTER TABLE Package ADD COLUMN CreateDate DATETIME;
ALTER TABLE Package ADD COLUMN  CreateBy NVARCHAR(250);
ALTER TABLE Package ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE Package ADD COLUMN LastUpdateBy NVARCHAR(250);

ALTER TABLE PackagePeriodDetail ADD COLUMN CreateDate DATETIME;
ALTER TABLE PackagePeriodDetail ADD COLUMN  CreateBy NVARCHAR(250);
ALTER TABLE PackagePeriodDetail ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE PackagePeriodDetail ADD COLUMN LastUpdateBy NVARCHAR(250);

ALTER TABLE Fortnight ADD COLUMN CreateDate DATETIME;
ALTER TABLE Fortnight ADD COLUMN  CreateBy NVARCHAR(250);
ALTER TABLE Fortnight ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE Fortnight ADD COLUMN LastUpdateBy NVARCHAR(250);


ALTER TABLE DiscountLimit ADD COLUMN ProductID NVARCHAR(50);



ALTER TABLE ProductStock ADD COLUMN CreateDate DATETIME;
ALTER TABLE ProductStock ADD COLUMN  CreateBy NVARCHAR(250);
ALTER TABLE ProductStock ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE ProductStock ADD COLUMN LastUpdateBy NVARCHAR(250);


ALTER TABLE EmployeeDetail ADD COLUMN SaleCode VARCHAR(50);

ALTER TABLE Contract ADD COLUMN SaleSubTeamCode VARCHAR(50);



CREATE TABLE [Limit] (
	[LimitID]	varchar(50) NOT NULL COLLATE NOCASE,
	[LimitType]	nvarchar(250) COLLATE NOCASE,
	[EmpID]	varchar(50) COLLATE NOCASE,
	[LimitMax]	float,
    PRIMARY KEY ([LimitID])

);


ALTER TABLE Prefix ADD COLUMN CreateDate DATETIME;
ALTER TABLE Prefix ADD COLUMN  CreateBy VARCHAR(50);
ALTER TABLE Prefix ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE Prefix ADD COLUMN LastUpdateBy VARCHAR(50);



DROP TABLE IF EXISTS "ManualDocument";
CREATE TABLE [ManualDocument] (
	[DocumentID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[DocumentType]	varchar(50) COLLATE NOCASE,
	[DocumentNumber]	varchar(50) COLLATE NOCASE,
	[ManualDocTypeID]	varchar(50) COLLATE NOCASE,
	[ManualVolumeNo]	nvarchar(50) COLLATE NOCASE,
	[ManualRunningNo]	integer,
	[Note]	nvarchar COLLATE NOCASE,
	[TeamCode]	nvarchar(50) COLLATE NOCASE,
	[SubTeamCode]	nvarchar(50) COLLATE NOCASE,
	[EmpID]	nvarchar(50) COLLATE NOCASE,
	[CreatedBy]	nvarchar(50) COLLATE NOCASE,
	[CreatedDate]	datetime,
	[UpdateBy]	nvarchar(50) COLLATE NOCASE,
	[UpdateDate]	datetime,
	[SyncedDate]	datetime,
	[isActive]	bit,
    PRIMARY KEY ([DocumentID])

);
INSERT INTO "ManualDocument" VALUES('C7EBF764CC90458EAB39919041B48F8F','0','1A001845802003','01','MCont1',1,'123456','PAX-15','PAX-1500','A00184','A00184',NULL,'A00184',NULL,NULL,1);



ALTER TABLE Contract ADD COLUMN TradeInReturnFlag bit;

ALTER TABLE ReturnProductDetail ADD COLUMN TradeInBrandCode varchar(50);
ALTER TABLE ReturnProductDetail ADD COLUMN TradeInProductModel nvarchar
ALTER TABLE ReturnProductDetail ADD COLUMN RefNo varchar(50);


ALTER TABLE DocumentHistory ADD COLUMN CreateDate datetime;
ALTER TABLE DocumentHistory ADD COLUMN  CreateBy VARCHAR(50);
ALTER TABLE DocumentHistory ADD COLUMN LastUpdateDate datetime;
ALTER TABLE DocumentHistory ADD COLUMN LastUpdateBy VARCHAR(50);


DROP TABLE IF EXISTS "ReportInventory";
CREATE TABLE [ReportInventory] (
	[ReportInventoryID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-','',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[ReportDate]	datetime,
	[FortnightID]	varchar(50) COLLATE NOCASE,
	[Fortnight]	varchar(50) COLLATE NOCASE,
	[FortnightTime]	varchar(50) COLLATE NOCASE,
	[SubTeamCode]	varchar(50) COLLATE NOCASE,
	[SaleLevel01]	varchar(50) COLLATE NOCASE,
	[SaleLevel01_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel02]	varchar(50) COLLATE NOCASE,
	[SaleLevel02_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel03]	varchar(50) COLLATE NOCASE,
	[SaleLevel03_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel04]	varchar(50) COLLATE NOCASE,
	[SaleLevel04_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel05]	varchar(50) COLLATE NOCASE,
	[SaleLevel05_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel06]	varchar(50) COLLATE NOCASE,
	[SaleLevel06_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel07]	varchar(50) COLLATE NOCASE,
	[SaleLevel07_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel08]	varchar(50) COLLATE NOCASE,
	[SaleLevel08_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel09]	varchar(50) COLLATE NOCASE,
	[SaleLevel09_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel10]	varchar(50) COLLATE NOCASE,
	[SaleLevel10_Name]	nvarchar(250) COLLATE NOCASE,
	[ProductID]	varchar(50) COLLATE NOCASE,
	[ProductName]	varchar(250) COLLATE NOCASE,
	[ProductCode]	varchar(50) COLLATE NOCASE,
	[ProductModel]	varchar(250) COLLATE NOCASE,
	[RemainProduct]	integer,
	[PickProduct]	integer,
	[InstallProduct]	integer,
	[ChangeProduct]	integer,
	[ReturnProduct]	integer,
	[BalanceProduct]	integer,
    PRIMARY KEY ([ReportInventoryID])

);



DROP TABLE IF EXISTS "ReportInstallAndPayment";
CREATE TABLE [ReportInstallAndPayment] (
	[ReportInstallAndPaymentID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-','',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[ReportType]	varchar(50) COLLATE NOCASE,
	[ReportDate]	datetime,
	[FortnightID]	varchar(50) COLLATE NOCASE,
	[Fortnight]	varchar(50) COLLATE NOCASE,
	[FortnightTime]	varchar(50) COLLATE NOCASE,
	[SaleLevel01]	varchar(50) COLLATE NOCASE,
	[SaleLevel01_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel02]	varchar(50) COLLATE NOCASE,
	[SaleLevel02_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel03]	varchar(50) COLLATE NOCASE,
	[SaleLevel03_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel04]	varchar(50) COLLATE NOCASE,
	[SaleLevel04_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel05]	varchar(50) COLLATE NOCASE,
	[SaleLevel05_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel06]	varchar(50) COLLATE NOCASE,
	[SaleLevel06_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel07]	varchar(50) COLLATE NOCASE,
	[SaleLevel07_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel08]	varchar(50) COLLATE NOCASE,
	[SaleLevel08_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel09]	varchar(50) COLLATE NOCASE,
	[SaleLevel09_Name]	nvarchar(250) COLLATE NOCASE,
	[SaleLevel10]	varchar(50) COLLATE NOCASE,
	[SaleLevel10_Name]	nvarchar(250) COLLATE NOCASE,
	[Count_Hold_Cash]	integer,
	[Count_Hold_Credit]	integer,
	[Count_Install_Cash]	integer,
	[Count_Install_Credit]	integer,
    PRIMARY KEY ([ReportInstallAndPaymentID])

);


 
 DROP TABLE IF EXISTS "DocumentHistory";
CREATE TABLE [DocumentHistory] (
	[PrintHistoryID]	varchar(50) NOT NULL COLLATE NOCASE,
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[DatePrint]	datetime,
	[DocumentType]	varchar(50) COLLATE NOCASE,
	[DocumentNumber]	varchar(50) COLLATE NOCASE,
	[Selected]	bit,
	[Deleted]	bit,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
	[PrintOrder]	integer DEFAULT 0,
	[Status]	varchar(50) COLLATE NOCASE,
	[SentDate]	datetime,
	[SentEmpID]	varchar(50) COLLATE NOCASE,
	[SentSaleCode]	varchar(50) COLLATE NOCASE,
	[SentSubTeamCode]	varchar(50) COLLATE NOCASE,
	[SentTeamCode]	varchar(50) COLLATE NOCASE,
	[ReceivedDate]	datetime,
	[ReceivedEmpID]	varchar(50) COLLATE NOCASE,
    PRIMARY KEY ([PrintHistoryID])

);


DROP TABLE IF EXISTS "SendDocument";
CREATE TABLE [SendDocument] (
	[SendDocumentID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[SumDocument]	integer,
	[SyncedDate]	datetime,
	[SentSubTeamCode]	varchar(50) COLLATE NOCASE,
	[SentTeamCode]	varchar(50) COLLATE NOCASE,
    PRIMARY KEY ([SendDocumentID])

);




DROP TABLE IF EXISTS "SendDocumentDetail";
CREATE TABLE [SendDocumentDetail] (
	[SendDocumentID]	varchar(50) COLLATE NOCASE,
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[PrintHistoryID]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime

);


DROP TABLE IF EXISTS "ManualDocumentWithdrawal";
CREATE TABLE [ManualDocumentWithdrawal] (
	[ManualDocID]	nvarchar(50) NOT NULL COLLATE NOCASE,
	[TeamCode]	nvarchar(50) COLLATE NOCASE,
	[SubTeamCode]	nvarchar(50) COLLATE NOCASE,
	[EmpID]	nvarchar(50) COLLATE NOCASE,
	[ManualType]	nvarchar(50) COLLATE NOCASE,
	[VolumeNo]	nvarchar(50) COLLATE NOCASE,
	[StartNo]	integer,
	[EndNo]	integer,
	[CreatedBy]	nvarchar(250) COLLATE NOCASE,
	[CreatedDate]	datetime,
	[UpdateBy]	nvarchar(250) COLLATE NOCASE,
	[UpdateDate]	datetime,
	[WithdrawalDate]	datetime,
	[ReturnDate]	datetime,
	[ReturnBy]	nvarchar(250) COLLATE NOCASE,
	[ReceivedBy]	nvarchar(250) COLLATE NOCASE,
	[ReceivedDate]	datetime,
	[ReturnStartNo]	integer,
	[ReturnEndNo]	integer,
	[Test1]	integer,
	[Test2]	datetime,
	[ManualDocTypeID]	nvarchar(50) COLLATE NOCASE,
	[IsDelete]	bit,
	[Remark]	nvarchar COLLATE NOCASE,
	[ReturnRemark]	nvarchar COLLATE NOCASE,
    PRIMARY KEY ([ManualDocID])

);

alter table ManualDocument rename to ManualDocument_temp;
CREATE TABLE [ManualDocument] (
	[DocumentID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[DocumentType]	varchar(50) COLLATE NOCASE,
	[DocumentNumber]	varchar(50) COLLATE NOCASE,
	[ManualDocTypeID]	varchar(50) COLLATE NOCASE,
	[ManualVolumeNo]	nvarchar(50) COLLATE NOCASE,
	[ManualRunningNo]	bigint,
	[Note]	nvarchar COLLATE NOCASE,
	[TeamCode]	nvarchar(50) COLLATE NOCASE,
	[SubTeamCode]	nvarchar(50) COLLATE NOCASE,
	[EmpID]	nvarchar(50) COLLATE NOCASE,
	[CreatedBy]	nvarchar(50) COLLATE NOCASE,
	[CreatedDate]	datetime,
	[UpdateBy]	nvarchar(50) COLLATE NOCASE,
	[UpdateDate]	datetime,
	[SyncedDate]	datetime,
	[isActive]	bit,
    PRIMARY KEY ([DocumentID])

);
insert into ManualDocument select * from ManualDocument_temp;
drop table  ManualDocument_temp;


alter table ManualDocumentWithdrawal rename to ManualDocumentWithdrawal_temp;
CREATE TABLE [ManualDocumentWithdrawal] (
	[ManualDocID]	nvarchar(50) NOT NULL COLLATE NOCASE,
	[TeamCode]	nvarchar(50) COLLATE NOCASE,
	[SubTeamCode]	nvarchar(50) COLLATE NOCASE,
	[EmpID]	nvarchar(50) COLLATE NOCASE,
	[ManualType]	nvarchar(50) COLLATE NOCASE,
	[VolumeNo]	nvarchar(50) COLLATE NOCASE,
	[StartNo]	integer,
	[EndNo]	integer,
	[CreatedBy]	nvarchar(250) COLLATE NOCASE,
	[CreatedDate]	datetime,
	[UpdateBy]	nvarchar(250) COLLATE NOCASE,
	[UpdateDate]	datetime,
	[WithdrawalDate]	datetime,
	[ReturnDate]	datetime,
	[ReturnBy]	nvarchar(250) COLLATE NOCASE,
	[ReceivedBy]	nvarchar(250) COLLATE NOCASE,
	[ReceivedDate]	datetime,
	[ReturnStartNo]	integer,
	[ReturnEndNo]	integer,
	[Test1]	integer,
	[Test2]	datetime,
	[ManualDocTypeID]	nvarchar(50) COLLATE NOCASE,
	[IsDelete]	bit,
	[Remark]	nvarchar COLLATE NOCASE,
	[ReturnRemark]	nvarchar COLLATE NOCASE,
    PRIMARY KEY ([ManualDocID])

);
insert into ManualDocumentWithdrawal select * from ManualDocumentWithdrawal_temp;
drop table  ManualDocumentWithdrawal_temp;


DROP TABLE IF EXISTS "Limit";
CREATE TABLE [Limit] (
	[LimitID]	varchar(50) NOT NULL COLLATE NOCASE,
	[LimitType]	nvarchar(250) COLLATE NOCASE,
	[EmpID]	varchar(50) COLLATE NOCASE,
	[LimitMax]	integer,
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([LimitID])

);
INSERT INTO "Limit" VALUES('13e24faa-4bf3-42f8-85ad-df703e1d80e2','Moneyonhand','A23929',1000,'0','2015-06-25 12:08:53.34','0DBAE33963E949E7B3891BC4463C6A8A','2015-06-25 12:08:53.34','0DBAE33963E949E7B3891BC4463C6A8A','2015-06-25 12:08:53.33');
INSERT INTO "Limit" VALUES('2c286ca4-f1f5-45ad-b58c-e8d2e57b3f15','Productonhand','A23929',5,'0','2015-06-25 12:08:53.323','0DBAE33963E949E7B3891BC4463C6A8A','2015-06-25 12:08:53.323','0DBAE33963E949E7B3891BC4463C6A8A','2015-06-25 12:08:53.33');
INSERT INTO "Limit" VALUES('2f9d96de-c8ac-4a88-962c-9b5d0758612e','Moneyonhand','A31790',90000,'0',NULL,NULL,'2015-06-25 12:08:40.927','0DBAE33963E949E7B3891BC4463C6A8A',NULL);
INSERT INTO "Limit" VALUES('5ef08f94-ca84-497a-9f12-529b163b3b1a','Productonhand','A31790',15,'0',NULL,NULL,'2015-06-25 12:08:40.927','0DBAE33963E949E7B3891BC4463C6A8A',NULL);
INSERT INTO "Limit" VALUES('a30408bd-e733-4820-9dda-ff859dcc60a0','Productonhand',NULL,10,'0',NULL,NULL,'2015-06-25 12:08:24.42','0DBAE33963E949E7B3891BC4463C6A8A',NULL);
INSERT INTO "Limit" VALUES('c4703ab7-a34c-4d7e-97d6-169fbbe07d5a','Moneyonhand',NULL,70000,'0',NULL,NULL,'2015-06-25 12:08:24.45','0DBAE33963E949E7B3891BC4463C6A8A',NULL);


ALTER TABLE ReturnProductDetail ADD COLUMN SourceStatus VARCHAR(50);
ALTER TABLE ReturnProductDetail ADD COLUMN Status VARCHAR(50);
ALTER TABLE ReturnProductDetail ADD COLUMN ReceivedDate DATETIME;
ALTER TABLE ReturnProductDetail ADD COLUMN ReceivedBy VARCHAR(50);
ALTER TABLE ReturnProductDetail ADD COLUMN ReceivedRemark NVARCHAR(500);
ALTER TABLE ReturnProductDetail ADD COLUMN CreateDate DATETIME;
ALTER TABLE ReturnProductDetail ADD COLUMN CreateBy VARCHAR(50);
ALTER TABLE ReturnProductDetail ADD COLUMN LastUpdateDate DATETIME;
ALTER TABLE ReturnProductDetail ADD COLUMN LastUpdateBy VARCHAR(50);



ALTER TABLE Area ADD COLUMN SourceSystem VARCHAR(50);
ALTER TABLE Problem ADD COLUMN SourceSystem VARCHAR(50);
ALTER TABLE Position ADD COLUMN SourceSystem VARCHAR(50);


ALTER TABLE OrgUnitType ADD COLUMN SourceSystem VARCHAR(50);
ALTER TABLE EmployeeType ADD COLUMN SourceSystem VARCHAR(50);


ALTER TABLE EmployeeDetail ADD COLUMN SourceSystem VARCHAR(50);

ALTER TABLE SubTeam ADD COLUMN SourceSystem VARCHAR(50);
ALTER TABLE Team ADD COLUMN SourceSystem VARCHAR(50);



DROP TABLE IF EXISTS "AreaEmployee";
CREATE TABLE [AreaEmployee] (
	[EmployeeCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[SourceSystem]	varchar(50) NOT NULL COLLATE NOCASE,
	[AreaCode]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
    PRIMARY KEY ([EmployeeCode], [SourceSystem])

);



DROP TABLE IF EXISTS "SaleAudit";
CREATE TABLE [SaleAudit] (
	[SaleAuditID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[RefNo]	varchar(50) COLLATE NOCASE,
	[AppointmentAuditDate]	datetime,
	[AuditDate]	datetime,
	[AuditorSaleCode]	varchar(50) COLLATE NOCASE,
	[AuditorEmployeeID]	varchar(50) COLLATE NOCASE,
	[AuditorTeamCode]	varchar(50) COLLATE NOCASE,
	[IsPassFirstPayment]	bit,
	[IsPassInstall]	bit,
	[HasOtherOffer]	bit,
	[OtherOfferDetail]	nvarchar(250) COLLATE NOCASE,
	[ComplainProblemID]	varchar(50) COLLATE NOCASE,
	[ComplainDetail]	nvarchar(250) COLLATE NOCASE,
	[AuditEmployeeLevelPath]	varchar COLLATE NOCASE,
	[AppointmentPaymentDate]	datetime,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([SaleAuditID])

);



DROP TABLE IF EXISTS "Area";
CREATE TABLE [Area] (
	[AreaCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[AreaName]	nvarchar(250) COLLATE NOCASE,
	[TeamCode]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SourceSystem]	varchar(50) COLLATE NOCASE,
    PRIMARY KEY ([AreaCode])

);


DROP TABLE IF EXISTS "AreaEmployee";
CREATE TABLE [AreaEmployee] (
	[EmployeeCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[SourceSystem]	varchar(50) NOT NULL COLLATE NOCASE,
	[AreaCode]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
    PRIMARY KEY ([EmployeeCode], [SourceSystem])

);


DROP TABLE IF EXISTS "SubArea";
CREATE TABLE [SubArea] (
	[SubAreaCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[AreaCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[ProvinceCode]	varchar(50) COLLATE NOCASE,
	[DistrictCode]	varchar(50) COLLATE NOCASE,
	[SubDistrictCode]	varchar(50) COLLATE NOCASE,
	[Detail]	nvarchar(500) COLLATE NOCASE,
	[Zipcode]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
    PRIMARY KEY ([SubAreaCode])

);


CREATE TABLE [CloseAccountDiscount] (
	[CloseAccountDiscountID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[MinOutstandingAmount]	float,
	[MaxOutstandingAmount]	float,
	[DiscountAmount]	float,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([CloseAccountDiscountID])

);

CREATE TABLE [ContractCloseAccount] (
	[ContractCloseAccountID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[RefNo]	varchar(50) COLLATE NOCASE,
	[PaymentID]	varchar(50) COLLATE NOCASE,
	[SalePaymentPeriodID]	varchar(50) COLLATE NOCASE,
	[OutstandingAmount]	float,
	[DiscountAmount]	float,
	[NetAmount]	float,
	[EffectiveDate]	datetime,
	[EffectiveBy]	varchar(50) COLLATE NOCASE,
	[EffectiveTeamCode]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([ContractCloseAccountID])

);


CREATE TABLE [CutOffContract] (
	[CutOffContractID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[RefNo]	varchar(50) COLLATE NOCASE,
	[Status]	varchar(50) COLLATE NOCASE,
	[RequestProblemDetail]	nvarchar(250) COLLATE NOCASE,
	[RequestDate]	datetime,
	[RequestBy]	varchar(50) COLLATE NOCASE,
	[RequestTeamCode]	varchar(50) COLLATE NOCASE,
	[ApprovedDate]	datetime,
	[ApproveDetail]	nvarchar(250) COLLATE NOCASE,
	[ApprovedBy]	varchar(50) COLLATE NOCASE,
	[ResultProblemDetail]	nvarchar(250) COLLATE NOCASE,
	[EffectiveDate]	datetime,
	[EffectiveBy]	varchar(50) COLLATE NOCASE,
	[CutOffContractPaperID]	varchar(50) COLLATE NOCASE,
	[IsAvailableContract]	bit,
	[AvailableContractDetail]	nvarchar(250) COLLATE NOCASE,
	[AvailableContractDate]	datetime,
	[AvailableContractBy]	varchar(50) COLLATE NOCASE,
	[AvailableContractTeamCode]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([CutOffContractID])

);

ALTER TABLE [SendMoney] ADD COLUMN SaveTransactionNoDate DATETIME;


ALTER TABLE [SaleAudit] ADD COLUMN ComplainID VARCHAR(50);

ALTER TABLE [Complain] ADD COLUMN ReferenceID VARCHAR(50);

ALTER TABLE [Complain] ADD COLUMN TaskType VARCHAR(50);

ALTER TABLE [SaleAudit] ADD COLUMN IsPassSaleAudit bit;



ALTER TABLE [CutOffContract] ADD COLUMN LastAssigneeEmpID VARCHAR(50);
ALTER TABLE [CutOffContract] ADD COLUMN LastAssigneeSaleCode VARCHAR(50);
ALTER TABLE [CutOffContract] ADD COLUMN LastAssigneeTeamCode VARCHAR(50);
ALTER TABLE [CutOffContract] ADD COLUMN LastAssignTaskType VARCHAR(50);


CREATE TABLE [CutDivisorContract] (
	[CutDivisorContractID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[RefNo]	varchar(50) COLLATE NOCASE,
	[Status]	varchar(50) COLLATE NOCASE,
	[RequestProblemDetail]	nvarchar(250) COLLATE NOCASE,
	[RequestDate]	datetime,
	[RequestBy]	varchar(50) COLLATE NOCASE,
	[RequestTeamCode]	varchar(50) COLLATE NOCASE,
	[ApprovedDate]	datetime,
	[ApproveDetail]	nvarchar(250) COLLATE NOCASE,
	[ApprovedBy]	varchar(50) COLLATE NOCASE,
	[ResultProblemDetail]	nvarchar(250) COLLATE NOCASE,
	[EffectiveDate]	datetime,
	[EffectiveBy]	varchar(50) COLLATE NOCASE,
	[CutDivisorContractPaperID]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([CutDivisorContractID])

);

CREATE TABLE [RequestNextPayment] (
	[RequestNextPaymentID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-','',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[RefNo]	varchar(50) COLLATE NOCASE,
	[SalePaymentPeriodID]	varchar(50) COLLATE NOCASE,
	[Status]	varchar(50) COLLATE NOCASE,
	[RequestProblemDetail]	nvarchar(250) COLLATE NOCASE,
	[RequestDate]	datetime,
	[RequestBy]	varchar(50) COLLATE NOCASE,
	[RequestTeamCode]	varchar(50) COLLATE NOCASE,
	[ApproveDetail]	nvarchar(250) COLLATE NOCASE,
	[ApprovedDate]	datetime,
	[ApprovedBy]	varchar(50) COLLATE NOCASE,
	[ResultProblemDetail]	nvarchar(250) COLLATE NOCASE,
	[EffectiveDate]	datetime,
	[EffectiveBy]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([RequestNextPaymentID])

);

DROP TABLE [CloseAccountDiscount];

ALTER TABLE [PackagePeriodDetail] ADD COLUMN CloseDiscountAmount float;


DROP TABLE IF EXISTS "RequestNextPayment";
CREATE TABLE [RequestNextPayment] (
	[RequestNextPaymentID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-','',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[RefNo]	varchar(50) COLLATE NOCASE,
	[PaymentID]	varchar(50) COLLATE NOCASE,
	[Status]	varchar(50) COLLATE NOCASE,
	[RequestProblemDetail]	nvarchar(250) COLLATE NOCASE,
	[RequestDate]	datetime,
	[RequestBy]	varchar(50) COLLATE NOCASE,
	[RequestTeamCode]	varchar(50) COLLATE NOCASE,
	[ApproveDetail]	nvarchar(250) COLLATE NOCASE,
	[ApprovedDate]	datetime,
	[ApprovedBy]	varchar(50) COLLATE NOCASE,
	[ResultProblemDetail]	nvarchar(250) COLLATE NOCASE,
	[EffectiveDate]	datetime,
	[EffectiveBy]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([RequestNextPaymentID])

);


ALTER TABLE [SalePaymentPeriod] ADD COLUMN CloseAccountDiscountAmount float;

ALTER TABLE [ManualDocumentWithdrawal] ADD COLUMN IsReturnComplete bit;

ALTER TABLE [SalePaymentPeriodPayment] ADD COLUMN CloseAccountDiscountAmount float;


DROP TABLE IF EXISTS "ReturnProductDetail";
CREATE TABLE [ReturnProductDetail] ([ReturnProductID]	varchar(50) NOT NULL COLLATE NOCASE,	
[ProductSerialNumber]	varchar(50) COLLATE NOCASE,	
[OrganizationCode]	varchar(50) COLLATE NOCASE,
[ProductID]	varchar(50) COLLATE NOCASE,	
[SyncedDate]	datetime, 
TradeInBrandCode varchar(50), 
TradeInProductModel nvarchar(250),
RefNo varchar(50),
SourceStatus VARCHAR(50),
Status VARCHAR(50), 
ReceivedDate DATETIME, 
ReceivedBy VARCHAR(50),
ReceivedRemark NVARCHAR(500),
CreateDate DATETIME, 
CreateBy VARCHAR(50),
LastUpdateDate DATETIME,
LastUpdateBy VARCHAR(50));



CREATE TABLE [ChangePartSpare] (
	[ChangePartSpareID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[RefNo]	varchar(50) COLLATE NOCASE,
	[Status]	varchar(50) COLLATE NOCASE,
	[RequestDate]	datetime,
	[RequestBy]	varchar(50) COLLATE NOCASE,
	[RequestTeamCode]	varchar(50) COLLATE NOCASE,
	[RequestProblemDetail]	nvarchar(250) COLLATE NOCASE,
	[ApprovedDate]	datetime,
	[ApprovedBy]	varchar(50) COLLATE NOCASE,
	[ApproveDetail]	nvarchar(250) COLLATE NOCASE,
	[EffectiveDate]	datetime,
	[EffectiveBy]	varchar(50) COLLATE NOCASE,
	[EffectiveDetail]	nvarchar(250) COLLATE NOCASE,
	[ChangePartSparePaperID]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([ChangePartSpareID]));



CREATE TABLE [ChangePartSpareDetail] (
	[ChangePartSpareID]	varchar(50) NOT NULL COLLATE NOCASE,
	[PartSpareID]	varchar(50) NOT NULL COLLATE NOCASE,
	[QTYUsed]	integer,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([ChangePartSpareID], [PartSpareID]));


CREATE TABLE [PartSpare] (
	[PartSpareID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[PartSpareCode]	varchar(50) COLLATE NOCASE,
	[PartSpareName]	varchar(50) COLLATE NOCASE,
	[PartSpareUnit]	varchar(50) COLLATE NOCASE,
	[Type]	varchar(50) COLLATE NOCASE,
	[IsActive]	bit,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([PartSpareID])

);



CREATE TABLE [PartSpareStockOnHand] (
	[OrganizationCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[PartSpareID]	varchar(50) NOT NULL COLLATE NOCASE,
	[EmployeeCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[TeamCode]	varchar(50) COLLATE NOCASE,
	[QTYOnHand]	integer,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([OrganizationCode], [PartSpareID], [EmployeeCode])

);



CREATE TABLE [SpareDrawdown] (
	[SpareDrawdownID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[Status]	varchar(50) COLLATE NOCASE,
	[RequestDate]	datetime,
	[RequestBy]	varchar(50) COLLATE NOCASE,
	[RequestTeamCode]	varchar(50) COLLATE NOCASE,
	[RequestDetail]	nvarchar(250) COLLATE NOCASE,
	[ApprovedDate]	datetime,
	[ApprovedBy]	varchar(50) COLLATE NOCASE,
	[ApproveDetail]	nvarchar(250) COLLATE NOCASE,
	[EffectiveDate]	datetime,
	[EffectiveBy]	varchar(50) COLLATE NOCASE,
	[EffectiveDetail]	nvarchar(250) COLLATE NOCASE,
	[SpareDrawdownPaperID]	varchar(50) COLLATE NOCASE,
	[PrintCount]	integer,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([SpareDrawdownID])

);



CREATE TABLE [SpareDrawdownDetail] (
	[SpareDrawdownID]	varchar(50) NOT NULL COLLATE NOCASE,
	[PartSpareIDOrProductID]	varchar(50) NOT NULL COLLATE NOCASE,
	[IsPartSpare]	bit,
	[RequestDetail]	nvarchar(250) COLLATE NOCASE,
	[RequestQTY]	integer,
	[ApproveDetail]	nvarchar(250) COLLATE NOCASE,
	[ApproveQTY]	integer,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([SpareDrawdownID], [PartSpareIDOrProductID])

);


DROP TABLE IF EXISTS "AreaEmployee";
CREATE TABLE [AreaEmployee] (
	[EmployeeCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[SourceSystem]	varchar(50) NOT NULL COLLATE NOCASE,
	[AreaCode]	varchar(50) NOT NULL COLLATE NOCASE,
	[SyncedDate]	datetime,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
    PRIMARY KEY ([EmployeeCode], [SourceSystem], [AreaCode])

);
INSERT INTO "AreaEmployee" VALUES('A00018','Sale','0EB87FE404EA4F369B8D6D2D0D3E432B','2015-10-12 11:57:57.347','2015-10-12 11:57:57.347','792BC59AC6FA4AB09241AF5D6EA1A9F3','2015-10-12 11:57:57.347','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A01013','Sale','D7F9FA3E83874EAB9B220A12B5B628C0','2015-11-11 11:06:45.357','2015-11-11 11:06:45.357','792BC59AC6FA4AB09241AF5D6EA1A9F3','2015-11-11 11:06:45.357','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A03968','Audit','3A77891B55014AEFBFECB542F758C62E','2015-07-20 15:01:23.563','2015-07-20 15:01:23.563','A00094','2015-11-05 09:48:38.23','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A03968','Audit','3AD5A78C814744D18C32B2D0FC758909','2015-07-31 11:12:44.313','2015-07-31 11:12:44.313','0DBAE33963E949E7B3891BC4463C6A8A','2015-11-12 18:11:46.303','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A03968','Audit','4079E2DD9FE7462F9E786635EBC7F766','2015-08-14 15:03:25.32','2015-08-14 15:03:25.32','0DBAE33963E949E7B3891BC4463C6A8A','2015-11-05 09:50:27.41','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A03968','Audit','846FB9457AF24139A6209353D7BB6A91','2015-07-20 15:01:29.813','2015-07-20 15:01:29.813','A00094','2015-11-05 09:48:53.507','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A03968','Credit','55364CEEB2724E25B55C14056B70935E','2015-07-20 15:01:09.02','2015-07-20 15:01:09.02','A00094','2015-11-05 09:57:15.543','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A03968','Credit','6B428162A0A94BE09B0790629A9F96DB','2015-07-20 15:01:02.497','2015-07-20 15:01:02.497','A00094','2015-11-05 09:53:21.467','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A03968','Credit','ABEDFF3DCD5E41EEB6458097BEED9488','2015-07-31 11:15:08.383','2015-07-31 11:15:08.383','0DBAE33963E949E7B3891BC4463C6A8A','2015-11-05 09:53:47.9','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A06831','Sale','BE3C26391CB5429B9349416449306541','2015-07-20 14:37:49.907','2015-07-20 14:37:49.907','A00094','2015-07-20 14:37:49.907','A00094');
INSERT INTO "AreaEmployee" VALUES('A08938','Audit','11CE7E255A9644D2B24432B002C32D56','2015-07-24 11:51:29.64','2015-07-24 11:51:29.64','0DBAE33963E949E7B3891BC4463C6A8A','2015-11-05 09:49:43.6','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A08938','Credit','9938CADFFA7B458884A312B39CB5913F','2015-10-08 11:01:54.127','2015-10-08 11:01:54.127','792BC59AC6FA4AB09241AF5D6EA1A9F3','2015-11-05 09:53:09.28','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A09108','Audit','6FDF82A6CDED411BA366E0820837DE72','2015-07-24 09:49:08.913','2015-07-24 09:49:08.913','0DBAE33963E949E7B3891BC4463C6A8A','2015-11-05 09:50:00.513','792BC59AC6FA4AB09241AF5D6EA1A9F3');
INSERT INTO "AreaEmployee" VALUES('A10110','Credit','D8B2ACDBBFCE4BD19EAA58F0E7D33FC5','2015-07-24 12:01:05.957','2015-07-24 12:01:05.957','0DBAE33963E949E7B3891BC4463C6A8A','2015-07-24 12:01:05.957','0DBAE33963E949E7B3891BC4463C6A8A');
INSERT INTO "AreaEmployee" VALUES('A10186','Credit','402B406D2477401E8FC84B2C3730A0D4','2015-07-24 09:50:55.99','2015-07-24 09:50:55.99','0DBAE33963E949E7B3891BC4463C6A8A','2015-07-24 12:00:51.097','0DBAE33963E949E7B3891BC4463C6A8A');
INSERT INTO "AreaEmployee" VALUES('A31638','Sale','63968594432341C6B9DF47218BD86F8B','2015-07-20 14:37:37.907','2015-07-20 14:37:37.907','A00094','2015-10-13 12:05:18.27','792BC59AC6FA4AB09241AF5D6EA1A9F3');


ALTER TABLE [DebtorCustomer] ADD COLUMN ReferencePersonName NVARCHAR(250);
ALTER TABLE [DebtorCustomer] ADD COLUMN ReferencePersonTelephone NVARCHAR(250);

ALTER TABLE [Customer] ADD COLUMN ReferencePersonName NVARCHAR(250);
ALTER TABLE [Customer] ADD COLUMN ReferencePersonTelephone NVARCHAR(250);


ALTER TABLE Contract ADD COLUMN IsReadyForSaleAudit bit NOT NULL CONSTRAINT [DF_Contract_IsReadyForSaleAudit] DEFAULT (0);

ALTER TABLE Contract rename to Contract_temp;
CREATE TABLE [Contract] (
	[RefNo]	varchar(50) NOT NULL COLLATE NOCASE,
	[CONTNO]	varchar(50) COLLATE NOCASE,
	[CustomerID]	varchar(50) COLLATE NOCASE,
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[STATUS]	varchar(50) COLLATE NOCASE,
	[StatusCode]	varchar(50) COLLATE NOCASE,
	[SALES]	float,
	[TotalPrice]	float,
	[EFFDATE]	datetime,
	[HasTradeIn]	integer,
	[TradeInProductCode]	varchar(50) COLLATE NOCASE,
	[TradeInBrandCode]	varchar(50) COLLATE NOCASE,
	[TradeInProductModel]	nvarchar(250) COLLATE NOCASE,
	[TradeInDiscount]	float,
	[PreSaleSaleCode]	varchar(50) COLLATE NOCASE,
	[PreSaleEmployeeCode]	varchar(50) COLLATE NOCASE,
	[PreSaleTeamCode]	varchar(50) COLLATE NOCASE,
	[SaleCode]	varchar(50) COLLATE NOCASE,
	[SaleEmployeeCode]	varchar(50) COLLATE NOCASE,
	[SaleTeamCode]	varchar(50) COLLATE NOCASE,
	[InstallerSaleCode]	varchar(50) COLLATE NOCASE,
	[InstallerEmployeeCode]	varchar(50) COLLATE NOCASE,
	[InstallerTeamCode]	varchar(50) COLLATE NOCASE,
	[InstallDate]	datetime,
	[ProductSerialNumber]	varchar(50) COLLATE NOCASE,
	[ProductID]	varchar(50) COLLATE NOCASE,
	[SaleEmployeeLevelPath]	varchar(50) COLLATE NOCASE,
	[MODE]	integer,
	[FortnightID]	varchar(50) COLLATE NOCASE,
	[ProblemID]	varchar(50) COLLATE NOCASE,
	[svcontno]	varchar(50) COLLATE NOCASE,
	[isActive]	bit,
	[MODEL]	varchar(50) COLLATE NOCASE,
	[fromrefno]	varchar(50) COLLATE NOCASE,
	[fromcontno]	varchar(50) COLLATE NOCASE,
	[todate]	datetime,
	[tocontno]	varchar(50) COLLATE NOCASE,
	[torefno]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
	[splitxxxxxxxxxxxxxxx]	bit,
	[TYPE]	varchar(2) COLLATE NOCASE,
	[toacc]	nvarchar(1) COLLATE NOCASE,
	[toaccdate]	datetime,
	[toturn]	nvarchar(1) COLLATE NOCASE,
	[toturndate]	datetime,
	[errs1]	nvarchar(2) COLLATE NOCASE,
	[todate1]	datetime,
	[tonote]	nvarchar(50) COLLATE NOCASE,
	[tocode]	nvarchar(2) COLLATE NOCASE,
	[lock]	nvarchar(1) COLLATE NOCASE,
	[SCode]	varchar(8) COLLATE NOCASE,
	[premium2]	float,
	[discfirst]	float,
	[nextcredit]	float,
	[netcredit]	float,
	[vatpaid1]	float,
	[vatpaid2]	float,
	[vatrem]	varchar(50) COLLATE NOCASE,
	[vatstop]	varchar(1) COLLATE NOCASE,
	[vatcurp]	smallint,
	[vatnextp]	smallint,
	[vatfirst]	datetime,
	[vatnet]	float,
	[vatcur]	datetime,
	[vatnext]	datetime,
	[vatpaid]	float,
	[vatlast]	datetime,
	[vatbal]	float,
	[excdate]	datetime,
	[excstatus]	varchar(1) COLLATE NOCASE DEFAULT 'N',
	[docno]	varchar(12) COLLATE NOCASE,
	[visit]	varchar(1) COLLATE NOCASE,
	[call]	varchar(1) COLLATE NOCASE,
	[VatDate]	datetime,
	[GLFlag]	varchar(1) COLLATE NOCASE,
	[UInt]	float,
	[IsPost]	varchar(1) COLLATE NOCASE,
	[IsGroup]	varchar(1) COLLATE NOCASE,
	[IsType]	varchar(1) COLLATE NOCASE,
	[IsGL]	varchar(1) COLLATE NOCASE,
	[DrAmt]	float,
	[IncAmt]	float,
	[Vat]	float,
	[AccNo]	varchar(10) COLLATE NOCASE,
	[Book]	varchar(2) COLLATE NOCASE,
	[UV]	float,
	[UR]	float,
	[UCost]	float,
	[GlDate]	datetime,
	[ContNo2]	varchar(10) COLLATE NOCASE,
	[Paid]	varchar(1) COLLATE NOCASE,
	[PrnDate]	datetime,
	[PrnSt]	varchar(1) COLLATE NOCASE,
	[Errs]	varchar(2) COLLATE NOCASE,
	[Posted]	varchar(1) COLLATE NOCASE,
	[DocDisc]	varchar(1) COLLATE NOCASE,
	[SRun]	varchar(4) COLLATE NOCASE,
	[SYear]	varchar(2) COLLATE NOCASE,
	[SMth]	varchar(2) COLLATE NOCASE,
	[SModel]	varchar(2) COLLATE NOCASE,
	[Disc]	float,
	[Balance]	float,
	[FirstDate]	datetime,
	[CloseDate]	datetime,
	[OpenDate]	datetime,
	[SERVICE]	varchar(8) COLLATE NOCASE,
	[CREDIT]	float,
	[CHECKER]	varchar(5) COLLATE NOCASE,
	[MEETDATE]	datetime,
	[USERKEY]	varchar(10) COLLATE NOCASE,
	[CHK]	bit NOT NULL DEFAULT 0,
	[ENTERDATE]	datetime,
	[RESERVED]	varchar(1) COLLATE NOCASE,
	[TOTALPAY]	float,
	[PREMIUM]	float,
	[PAYTYPE]	varchar(2) COLLATE NOCASE,
	[PAYPERIOD]	varchar(2) COLLATE NOCASE,
	[BOOKNO]	varchar(6) COLLATE NOCASE,
	[RECEIPTNO]	varchar(6) COLLATE NOCASE,
	[SPECCODE]	varchar(2) COLLATE NOCASE,
	[SERIALNO]	varchar(12) COLLATE NOCASE,
	[COMPANY]	varchar(1) COLLATE NOCASE,
	[PAYDAY]	varchar(2) COLLATE NOCASE,
	[PAYDATE]	datetime,
	[NEXTPERIOD]	integer,
	[NEXTDUE]	datetime,
	[HOMETYPE]	varchar(2) COLLATE NOCASE,
	[STDATE]	datetime,
	[CASHCODE]	varchar(8) COLLATE NOCASE,
	[PreSaleEmployeeLevelPath]	varchar(50) COLLATE NOCASE,
	[InstallerEmployeeLevelPath]	varchar(50) COLLATE NOCASE,
	[PreSaleEmployeeName]	nvarchar(250) COLLATE NOCASE,
	[EmployeeHistoryID]	varchar(50) COLLATE NOCASE,
	[SaleSubTeamCode]	nvarchar(10) COLLATE NOCASE,
	[TradeInReturnFlag]	bit,
	[IsReadyForSaleAudit]	bit NOT NULL DEFAULT 0,
    PRIMARY KEY ([RefNo])

);
INSERT INTO Contract(RefNo,CONTNO,CustomerID,OrganizationCode,STATUS,StatusCode,SALES,
TotalPrice,EFFDATE,HasTradeIn,TradeInProductCode,TradeInBrandCode,
TradeInProductModel,TradeInDiscount,PreSaleSaleCode,PreSaleEmployeeCode,
PreSaleTeamCode,SaleCode,SaleEmployeeCode,SaleTeamCode,InstallerSaleCode,
InstallerEmployeeCode,InstallerTeamCode,InstallDate,ProductSerialNumber,
ProductID,SaleEmployeeLevelPath,MODE,FortnightID,ProblemID,svcontno,
isActive,MODEL,fromrefno,fromcontno,todate,tocontno,torefno,CreateDate,
CreateBy,LastUpdateDate,LastUpdateBy,SyncedDate,splitxxxxxxxxxxxxxxx,
TYPE,toacc,toaccdate,toturn,toturndate,errs1,todate1,tonote,tocode,
lock,SCode,premium2,discfirst,nextcredit,netcredit,vatpaid1,vatpaid2,
vatrem,vatstop,vatcurp,vatnextp,vatfirst,vatnet,vatcur,vatnext,vatpaid,
vatlast,vatbal,excdate,excstatus,docno,visit,call,VatDate,GLFlag,UInt,
IsPost,IsGroup,IsType,IsGL,DrAmt,IncAmt,Vat,AccNo,Book,UV,UR,UCost,GlDate,
ContNo2,Paid,PrnDate,PrnSt,Errs,Posted,DocDisc,SRun,SYear,SMth,SModel,
Disc,Balance,FirstDate,CloseDate,OpenDate,SERVICE,CREDIT,CHECKER,MEETDATE,
USERKEY,CHK,ENTERDATE,RESERVED,TOTALPAY,PREMIUM,PAYTYPE,PAYPERIOD,BOOKNO,RECEIPTNO,
SPECCODE,SERIALNO,COMPANY,PAYDAY,PAYDATE,NEXTPERIOD,NEXTDUE,HOMETYPE,STDATE,
CASHCODE,PreSaleEmployeeLevelPath,InstallerEmployeeLevelPath,PreSaleEmployeeName,
EmployeeHistoryID,SaleSubTeamCode,TradeInReturnFlag,IsReadyForSaleAudit) 
SELECT * FROM Contract_temp;
DROP TABLE Contract_temp;



ALTER TABLE Payment rename to Payment_temp;
CREATE TABLE [Payment] (
	[PaymentID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[SendMoneyID]	varchar(50) COLLATE NOCASE,
	[PaymentType]	varchar(50) COLLATE NOCASE,
	[PayPartial]	bit,
	[BankCode]	varchar(50) COLLATE NOCASE,
	[ChequeNumber]	varchar(50) COLLATE NOCASE,
	[ChequeBankBranch]	nvarchar(250) COLLATE NOCASE,
	[ChequeDate]	varchar(50) COLLATE NOCASE,
	[CreditCardNumber]	varchar(50) COLLATE NOCASE,
	[CreditCardApproveCode]	varchar(150) COLLATE NOCASE,
	[CreditEmployeeLevelPath]	varchar(50) COLLATE NOCASE,
	[TripID]	varchar(50) COLLATE NOCASE,
	[Status]	varchar(50) COLLATE NOCASE,
	[RefNo]	varchar(50) COLLATE NOCASE,
	[PayPeriod]	varchar(2) COLLATE NOCASE,
	[PayDate]	datetime,
	[PAYAMT]	float,
	[CashCode]	varchar(50) COLLATE NOCASE,
	[EmpID]	varchar(50) COLLATE NOCASE,
	[TeamCode]	varchar(50) COLLATE NOCASE,
	[receiptkind]	nvarchar(1) COLLATE NOCASE,
	[Kind]	varchar(1) COLLATE NOCASE,
	[BookNo]	varchar(6) COLLATE NOCASE,
	[ReceiptNo]	varchar(6) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
	[splitxxxxxxxxxxxxxxx]	bit,
	[Item]	integer,
	[Row]	smallint,
	[CashName]	varchar(50) COLLATE NOCASE,
	[Name]	varchar(80) COLLATE NOCASE,
	[Premium]	float,
	[Users]	varchar(15) COLLATE NOCASE,
	[Flag]	varchar(1) COLLATE NOCASE,
	[Team]	varchar(3) COLLATE NOCASE,
	[Cate]	varchar(1) COLLATE NOCASE,
	[CONTNO]	varchar(10) COLLATE NOCASE,
	[ContAmt]	float,
	[Checked]	varchar(1) COLLATE NOCASE,
	[Valid]	varchar(1) COLLATE NOCASE,
	[excstatus]	varchar(1) COLLATE NOCASE,
	[receiptdate]	datetime,
	[discfirst]	float,
	[model2]	nvarchar(10) COLLATE NOCASE,
	[receiptdate1]	datetime,
	[receiptby]	nvarchar(2) COLLATE NOCASE,
    PRIMARY KEY ([PaymentID])

);
INSERT INTO Payment(PaymentID,OrganizationCode,SendMoneyID,PaymentType,
PayPartial,BankCode,ChequeNumber,ChequeBankBranch,ChequeDate,CreditCardNumber,
CreditCardApproveCode,CreditEmployeeLevelPath,TripID,Status,RefNo,PayPeriod,
PayDate,PAYAMT,CashCode,EmpID,TeamCode,receiptkind,Kind,BookNo,ReceiptNo,
CreateDate,CreateBy,LastUpdateDate,LastUpdateBy,SyncedDate,splitxxxxxxxxxxxxxxx,
Item,Row,CashName,Name,Premium,Users,Flag,Team,Cate,CONTNO,ContAmt,Checked,
Valid,excstatus,receiptdate,discfirst,model2,receiptdate1,receiptby) 
SELECT * FROM Payment_temp;
DROP TABLE Payment_temp;


ALTER TABLE [SaleAudit] ADD COLUMN SaleAuditEmployeeLevelPath VARCHAR(50);
ALTER TABLE [ChangeContract] ADD COLUMN RequestEmployeeLevelPath VARCHAR(50);
ALTER TABLE [ChangeContract] ADD COLUMN EffectiveEmployeeLevelPath VARCHAR(50);
ALTER TABLE [ChangeProduct] ADD COLUMN RequestEmployeeLevelPath VARCHAR(50);
ALTER TABLE [ChangeProduct] ADD COLUMN EffectiveEmployeeLevelPath VARCHAR(50);
ALTER TABLE [ImpoundProduct] ADD COLUMN RequestEmployeeLevelPath VARCHAR(50);
ALTER TABLE [ImpoundProduct] ADD COLUMN EffectiveEmployeeLevelPath VARCHAR(50);
ALTER TABLE [Complain] ADD COLUMN RequestEmployeeLevelPath VARCHAR(50);
ALTER TABLE [Complain] ADD COLUMN EffectiveEmployeeLevelPath VARCHAR(50);
ALTER TABLE [RequestNextPayment] ADD COLUMN RequestEmployeeLevelPath VARCHAR(50);
ALTER TABLE [RequestNextPayment] ADD COLUMN EffectiveEmployeeLevelPath VARCHAR(50);
ALTER TABLE [CutOffContract] ADD COLUMN RequestEmployeeLevelPath VARCHAR(50);
ALTER TABLE [CutOffContract] ADD COLUMN EffectiveEmployeeLevelPath VARCHAR(50);
ALTER TABLE [CutDivisorContract] ADD COLUMN RequestEmployeeLevelPath VARCHAR(50);
ALTER TABLE [CutDivisorContract] ADD COLUMN EffectiveEmployeeLevelPath VARCHAR(50);
ALTER TABLE [EmployeeDetail] ADD COLUMN TreeHistoryID VARCHAR(50);


DROP TABLE EmployeeDetailHistory;
CREATE TABLE [EmployeeDetailHistory] (
	[EmployeeDetailHistoryID]	varchar(50) NOT NULL COLLATE NOCASE,
	[TreeHistoryID]	varchar(50) COLLATE NOCASE,
	[EmployeeDetailID]	varchar(50) COLLATE NOCASE,
	[EmployeeCode]	varchar(50) COLLATE NOCASE,
	[ParentEmployeeDetailID]	varchar(50) COLLATE NOCASE,
	[ParentEmployeeCode]	varchar(50) COLLATE NOCASE,
	[EmployeeTypeCode]	varchar(50) COLLATE NOCASE,
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[BranchCode]	varchar(50) COLLATE NOCASE,
	[DepartmentCode]	varchar(50) COLLATE NOCASE,
	[SubDepartmentCode]	varchar(50) COLLATE NOCASE,
	[TeamCode]	varchar(50) COLLATE NOCASE,
	[SubTeamCode]	varchar(50) COLLATE NOCASE,
	[PositionCode]	varchar(50) COLLATE NOCASE,
	[SupervisorCode]	varchar(50) COLLATE NOCASE,
	[SupervisorHeadCode]	varchar(50) COLLATE NOCASE,
	[SubTeamHeadCode]	varchar(50) COLLATE NOCASE,
	[TeamHeadCode]	varchar(50) COLLATE NOCASE,
	[SubDepartmentHeadCode]	varchar(50) COLLATE NOCASE,
	[DepartmentHeadCode]	varchar(50) COLLATE NOCASE,
	[OrganizationHeadCode]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
	[SaleCode]	varchar(50) COLLATE NOCASE,
	[SourceSystem]	varchar(50) COLLATE NOCASE,
    PRIMARY KEY ([EmployeeDetailHistoryID])

);


ALTER TABLE [WriteOffNPL] ADD COLUMN RequestEmployeeLevelPath VARCHAR(50) ;
ALTER TABLE [WriteOffNPL] ADD COLUMN EffectiveEmployeeLevelPath VARCHAR(50) ;


ALTER TABLE [SendMoney] ADD COLUMN SendMoneyEmployeeLevelPath VARCHAR(50);
ALTER TABLE [SendMoney] ADD COLUMN EmpID VARCHAR(50);
ALTER TABLE [SendMoney] ADD COLUMN TeamCode VARCHAR(50);

ALTER TABLE [Product] ADD COLUMN ItemID VARCHAR(50);

ALTER TABLE [Contract] ADD COLUMN ContractRefNo VARCHAR(50);


DROP TABLE IF EXISTS "Contract";
CREATE TABLE [Contract] (
	[RefNo]	varchar(50) NOT NULL COLLATE NOCASE,
	[CONTNO]	varchar(50) COLLATE NOCASE,
	[CustomerID]	varchar(50) COLLATE NOCASE,
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[STATUS]	varchar(50) COLLATE NOCASE,
	[StatusCode]	varchar(50) COLLATE NOCASE,
	[SALES]	float,
	[TotalPrice]	float,
	[EFFDATE]	datetime,
	[HasTradeIn]	integer,
	[TradeInProductCode]	varchar(50) COLLATE NOCASE,
	[TradeInBrandCode]	varchar(50) COLLATE NOCASE,
	[TradeInProductModel]	nvarchar(250) COLLATE NOCASE,
	[TradeInDiscount]	float,
	[PreSaleSaleCode]	varchar(50) COLLATE NOCASE,
	[PreSaleEmployeeCode]	varchar(50) COLLATE NOCASE,
	[PreSaleTeamCode]	varchar(50) COLLATE NOCASE,
	[SaleCode]	varchar(50) COLLATE NOCASE,
	[SaleEmployeeCode]	varchar(50) COLLATE NOCASE,
	[SaleTeamCode]	varchar(50) COLLATE NOCASE,
	[InstallerSaleCode]	varchar(50) COLLATE NOCASE,
	[InstallerEmployeeCode]	varchar(50) COLLATE NOCASE,
	[InstallerTeamCode]	varchar(50) COLLATE NOCASE,
	[InstallDate]	datetime,
	[ProductSerialNumber]	varchar(50) COLLATE NOCASE,
	[ProductID]	varchar(50) COLLATE NOCASE,
	[SaleEmployeeLevelPath]	varchar(50) COLLATE NOCASE,
	[MODE]	integer,
	[FortnightID]	varchar(50) COLLATE NOCASE,
	[ProblemID]	varchar(50) COLLATE NOCASE,
	[svcontno]	varchar(50) COLLATE NOCASE,
	[isActive]	bit,
	[MODEL]	varchar(50) COLLATE NOCASE,
	[fromrefno]	varchar(50) COLLATE NOCASE,
	[fromcontno]	varchar(50) COLLATE NOCASE,
	[todate]	datetime,
	[tocontno]	varchar(50) COLLATE NOCASE,
	[torefno]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[LastUpdateDate]	datetime,
	[LastUpdateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
	[splitxxxxxxxxxxxxxxx]	bit,
	[TYPE]	varchar(2) COLLATE NOCASE,
	[toacc]	nvarchar(1) COLLATE NOCASE,
	[toaccdate]	datetime,
	[toturn]	nvarchar(1) COLLATE NOCASE,
	[toturndate]	datetime,
	[errs1]	nvarchar(2) COLLATE NOCASE,
	[todate1]	datetime,
	[tonote]	nvarchar(50) COLLATE NOCASE,
	[tocode]	nvarchar(2) COLLATE NOCASE,
	[lock]	nvarchar(1) COLLATE NOCASE,
	[SCode]	varchar(8) COLLATE NOCASE,
	[premium2]	float,
	[discfirst]	float,
	[nextcredit]	float,
	[netcredit]	float,
	[vatpaid1]	float,
	[vatpaid2]	float,
	[vatrem]	varchar(50) COLLATE NOCASE,
	[vatstop]	varchar(1) COLLATE NOCASE,
	[vatcurp]	smallint,
	[vatnextp]	smallint,
	[vatfirst]	datetime,
	[vatnet]	float,
	[vatcur]	datetime,
	[vatnext]	datetime,
	[vatpaid]	float,
	[vatlast]	datetime,
	[vatbal]	float,
	[excdate]	datetime,
	[excstatus]	varchar(1) COLLATE NOCASE DEFAULT 'N',
	[docno]	varchar(12) COLLATE NOCASE,
	[visit]	varchar(1) COLLATE NOCASE,
	[call]	varchar(1) COLLATE NOCASE,
	[VatDate]	datetime,
	[GLFlag]	varchar(1) COLLATE NOCASE,
	[UInt]	float,
	[IsPost]	varchar(1) COLLATE NOCASE,
	[IsGroup]	varchar(1) COLLATE NOCASE,
	[IsType]	varchar(1) COLLATE NOCASE,
	[IsGL]	varchar(1) COLLATE NOCASE,
	[DrAmt]	float,
	[IncAmt]	float,
	[Vat]	float,
	[AccNo]	varchar(10) COLLATE NOCASE,
	[Book]	varchar(2) COLLATE NOCASE,
	[UV]	float,
	[UR]	float,
	[UCost]	float,
	[GlDate]	datetime,
	[ContNo2]	varchar(10) COLLATE NOCASE,
	[Paid]	varchar(1) COLLATE NOCASE,
	[PrnDate]	datetime,
	[PrnSt]	varchar(1) COLLATE NOCASE,
	[Errs]	varchar(2) COLLATE NOCASE,
	[Posted]	varchar(1) COLLATE NOCASE,
	[DocDisc]	varchar(1) COLLATE NOCASE,
	[SRun]	varchar(4) COLLATE NOCASE,
	[SYear]	varchar(2) COLLATE NOCASE,
	[SMth]	varchar(2) COLLATE NOCASE,
	[SModel]	varchar(2) COLLATE NOCASE,
	[Disc]	float,
	[Balance]	float,
	[FirstDate]	datetime,
	[CloseDate]	datetime,
	[OpenDate]	datetime,
	[SERVICE]	varchar(8) COLLATE NOCASE,
	[CREDIT]	float,
	[CHECKER]	varchar(5) COLLATE NOCASE,
	[MEETDATE]	datetime,
	[USERKEY]	varchar(10) COLLATE NOCASE,
	[CHK]	bit NOT NULL DEFAULT 0,
	[ENTERDATE]	datetime,
	[RESERVED]	varchar(1) COLLATE NOCASE,
	[TOTALPAY]	float,
	[PREMIUM]	float,
	[PAYTYPE]	varchar(2) COLLATE NOCASE,
	[PAYPERIOD]	varchar(2) COLLATE NOCASE,
	[BOOKNO]	varchar(6) COLLATE NOCASE,
	[RECEIPTNO]	varchar(6) COLLATE NOCASE,
	[SPECCODE]	varchar(2) COLLATE NOCASE,
	[SERIALNO]	varchar(12) COLLATE NOCASE,
	[COMPANY]	varchar(1) COLLATE NOCASE,
	[PAYDAY]	varchar(2) COLLATE NOCASE,
	[PAYDATE]	datetime,
	[NEXTPERIOD]	integer,
	[NEXTDUE]	datetime,
	[HOMETYPE]	varchar(2) COLLATE NOCASE,
	[STDATE]	datetime,
	[CASHCODE]	varchar(8) COLLATE NOCASE,
	[PreSaleEmployeeLevelPath]	varchar(50) COLLATE NOCASE,
	[InstallerEmployeeLevelPath]	varchar(50) COLLATE NOCASE,
	[PreSaleEmployeeName]	nvarchar(250) COLLATE NOCASE,
	[EmployeeHistoryID]	varchar(50) COLLATE NOCASE,
	[SaleSubTeamCode]	nvarchar(10) COLLATE NOCASE,
	[TradeInReturnFlag]	bit,
	[IsReadyForSaleAudit]	bit NOT NULL DEFAULT 0, 
	[ContractReferenceNo] VARCHAR(50),
    PRIMARY KEY ([RefNo])

);


ALTER TABLE [Contract] ADD COLUMN IsMigrate bit;

CREATE TABLE [LogScanProductSerial] (
	[LogScanProductSerialID]	varchar(50) NOT NULL COLLATE NOCASE DEFAULT '-',
	[OrganizationCode]	varchar(50) COLLATE NOCASE,
	[TaskType]	varchar(50) COLLATE NOCASE,
	[RequestID]	varchar(50) COLLATE NOCASE,
	[IsScanProductSerial]	bit,
	[RefNo]	varchar(50) COLLATE NOCASE,
	[ProductSerialNumber]	varchar(50) COLLATE NOCASE,
	[Status]	varchar(50) COLLATE NOCASE,
	[CreateDate]	datetime,
	[CreateBy]	varchar(50) COLLATE NOCASE,
	[SyncedDate]	datetime,
    PRIMARY KEY ([LogScanProductSerialID])
);