package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class GCMProdStkAndContractInfo extends BHParcelable {

    public String GCMProdStkAndContractID;
    public String ProductSerialNumber;
    public String OrganizationCode;
    public String ProductID;
    public String Type;
    public String OldSubTeamCode;
    public String OldTeamCode;
    public String NewSubTeamCode;
    public String NewTeamCode;
    public String ProdStkStatus;  // สถานะของสินค้า (SOLD=ขายไปแล้ว/OVER=สินค้าเกิน/WAIT=สินค้ารอตรวจสอบ/CHECKED=สินค้าผ่านการตรวจสอบแล้ว)
    public Date ImportDate;
    public String ScanByTeam;
    public Date ScanDate;
    public String ProdStkCreateBy;
    public Date ProdStkCreateDate;
    public String ProdStkUserID;
    public String ProdStkUserName;
    public String ProdStkDeviceID;
    public String RefNo;
    public String ContractUserID;
    public String ContractUserName;
    public String ContractDeviceID;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;
}
