package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class PartSpareInfo extends BHParcelable {

    public String PartSpareID;          // GUID รหัสชิ้นส่วนอะไหล่
    public String OrganizationCode;
    public String PartSpareCode;
    public String PartSpareName;
    public String PartSpareUnit;
    public String Type;                 // ชนิดสินค้า (จาก TSR-Stock)
    public boolean IsActive;            // สถานะการใช้งาน
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    //-- Extend Fields
    //RequestSpareDrawdownListFragment
    public int No;
    public int QTY;
    public boolean IsPartSpare;

}
