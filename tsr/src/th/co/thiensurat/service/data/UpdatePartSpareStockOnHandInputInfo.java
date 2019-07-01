package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.PartSpareStockOnHandInfo;

public class UpdatePartSpareStockOnHandInputInfo extends BHParcelable {

    public String OrganizationCode;
    public String PartSpareID;          // GUID รหัสชิ้นส่วนอะไหล่
    public String EmployeeCode;
    public String TeamCode;
    public int QTYOnHand;               // จำนวนชิ้นส่วนอะไหล่ที่สามารถใช้งานได้ ณ ขณะนั้น
//    public Date CreateDate;
//    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    public static UpdatePartSpareStockOnHandInputInfo from(PartSpareStockOnHandInfo info) {
        UpdatePartSpareStockOnHandInputInfo wsInfo = new UpdatePartSpareStockOnHandInputInfo();
        wsInfo.OrganizationCode = info.OrganizationCode;
        wsInfo.PartSpareID = info.PartSpareID;
        wsInfo.EmployeeCode = info.EmployeeCode;
        wsInfo.TeamCode = info.TeamCode;
        wsInfo.QTYOnHand = info.QTYOnHand;
//        wsInfo.CreateDate = info.CreateDate;
//        wsInfo.CreateBy = info.CreateBy;
        wsInfo.LastUpdateDate = info.LastUpdateDate;
        wsInfo.LastUpdateBy = info.LastUpdateBy;
        wsInfo.SyncedDate = info.SyncedDate;
        return wsInfo;
    }

}
