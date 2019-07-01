package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class SpareDrawdownDetailInfo extends BHParcelable {

    public String SpareDrawdownID;          // GUID ของการขอเบิกเครื่อง/อะไหล่
    public String PartSpareIDOrProductID;   // GUID รหัสชิ้นส่วนอะไหล่
    public boolean IsPartSpare;             // เป็นชิ้นส่วนอะไหล่หรือไม่? (สามารถเบิกได้ทั้ง เครื่องกรองน้ำ vs ชิ้นส่วนอะไหล่)
    public String RequestDetail;            // หมายเหตุที่ขอเบิกเครื่อง/อะไหล่
    public int RequestQTY;                  // จำนวนเครื่อง/อะไหล่ที่ขอเบิก
    public String ApproveDetail;            // หมายเหตุการขออนุมัติ
    public int ApproveQTY;                  // จำนวนเครื่อง/อะไหล่ที่ขออนุมัติ
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    //SpareDrawdown
    public String PartSpareName;
    public String PartSpareCode;
    public String PartSpareUnit;
}
