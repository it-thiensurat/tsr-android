package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ChangePartSpareDetailInfo extends BHParcelable {
	public String ChangePartSpareID;       // GUID ของการเปลี่ยนอะไหล่
	public String PartSpareID;             // GUID รหัสชิ้นส่วนอะไหล่
	public int QTYUsed;                    // จำนวนชิ้นส่วนอะไหล่ที่ใช้เปลี่ยน
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;
}
