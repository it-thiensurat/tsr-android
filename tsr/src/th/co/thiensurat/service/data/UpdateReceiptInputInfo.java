package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ReceiptInfo;

public class UpdateReceiptInputInfo extends BHParcelable {

    public String ReceiptID;
    public String OrganizationCode;
    public String ReceiptCode;
    public String PaymentID;
    public String RefNo;
    public Date DatePayment;
    public float TotalPayment;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    public static UpdateReceiptInputInfo from(ReceiptInfo receiptInfo) {
        UpdateReceiptInputInfo info = new UpdateReceiptInputInfo();
        info.ReceiptID = receiptInfo.ReceiptID;
        info.OrganizationCode = receiptInfo.OrganizationCode;
        info.ReceiptCode = receiptInfo.ReceiptCode;
        info.OrganizationCode = receiptInfo.OrganizationCode;
        info.PaymentID = receiptInfo.PaymentID;
        info.RefNo = receiptInfo.RefNo;
        info.DatePayment = receiptInfo.DatePayment;
        info.TotalPayment = receiptInfo.TotalPayment;
        info.CreateDate = receiptInfo.CreateDate;
        info.CreateBy = receiptInfo.CreateBy;
        info.LastUpdateDate = receiptInfo.LastUpdateDate;
        info.LastUpdateBy = receiptInfo.LastUpdateBy;
        info.SyncedDate = receiptInfo.SyncedDate;

        return info;
    }

}
