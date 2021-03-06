package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ChangePartSpareDetailInfo;

public class AddChangePartSpareDetailInputInfo extends BHParcelable {

    public String ChangePartSpareID;       // GUID ของการเปลี่ยนอะไหล่
    public String PartSpareID;             // GUID รหัสชิ้นส่วนอะไหล่
    public int QTYUsed;                    // จำนวนชิ้นส่วนอะไหล่ที่ใช้เปลี่ยน
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    public static AddChangePartSpareDetailInputInfo from(ChangePartSpareDetailInfo info) {
        AddChangePartSpareDetailInputInfo wsInfo = new AddChangePartSpareDetailInputInfo();
        wsInfo.ChangePartSpareID = info.ChangePartSpareID;
        wsInfo.PartSpareID = info.PartSpareID;
        wsInfo.QTYUsed = info.QTYUsed;
        wsInfo.CreateDate = info.CreateDate;
        wsInfo.CreateBy = info.CreateBy;
        wsInfo.LastUpdateDate = info.LastUpdateDate;
        wsInfo.LastUpdateBy = info.LastUpdateBy;
        wsInfo.SyncedDate = info.SyncedDate;
        return wsInfo;
    }

}
