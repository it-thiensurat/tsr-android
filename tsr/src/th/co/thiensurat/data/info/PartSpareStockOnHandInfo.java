package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class PartSpareStockOnHandInfo extends BHParcelable {
    public String OrganizationCode;
    public String PartSpareID;          // GUID รหัสชิ้นส่วนอะไหล่
    public String EmployeeCode;
    public String TeamCode;
    public int QTYOnHand;               // จำนวนชิ้นส่วนอะไหล่ที่สามารถใช้งานได้ ณ ขณะนั้น
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;
}
