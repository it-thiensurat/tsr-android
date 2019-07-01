package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.SpareDrawdownDetailInfo;

public class AddSpareDrawdownDetailInputInfo extends BHParcelable {

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

    public static AddSpareDrawdownDetailInputInfo from(SpareDrawdownDetailInfo spareDrawdownDetailInfo) {
        AddSpareDrawdownDetailInputInfo info = new AddSpareDrawdownDetailInputInfo();

        info.SpareDrawdownID = spareDrawdownDetailInfo.SpareDrawdownID;
        info.PartSpareIDOrProductID = spareDrawdownDetailInfo.PartSpareIDOrProductID;
        info.IsPartSpare = spareDrawdownDetailInfo.IsPartSpare;
        info.RequestDetail = spareDrawdownDetailInfo.RequestDetail;
        info.RequestQTY = spareDrawdownDetailInfo.RequestQTY;
        info.ApproveDetail = spareDrawdownDetailInfo.ApproveDetail;
        info.ApproveQTY = spareDrawdownDetailInfo.ApproveQTY;
        info.CreateDate = spareDrawdownDetailInfo.CreateDate;
        info.CreateBy = spareDrawdownDetailInfo.CreateBy;
        info.LastUpdateDate = spareDrawdownDetailInfo.LastUpdateDate;
        info.LastUpdateBy = spareDrawdownDetailInfo.LastUpdateBy;

        return info;
    }
}
