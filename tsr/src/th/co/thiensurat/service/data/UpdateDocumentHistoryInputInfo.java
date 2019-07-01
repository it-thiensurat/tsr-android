package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.DocumentHistoryInfo;

public class UpdateDocumentHistoryInputInfo extends BHParcelable {

	public String PrintHistoryID;
	public String OrganizationCode;
	public Date DatePrint;
	public String DocumentType;
	public String DocumentNumber;
	public String DocumentNo;
	public Date SyncedDate;
	public boolean Selected;
	public boolean Deleted;
	//public String CreateBy;
	//public Date CreateDate;
	public String LastUpdateBy;
	public Date LastUpdateDate;
    public int PrintOrder;
    public String Status;
    public Date SentDate;
    public String SentEmpID;
    public String SentSaleCode;
    public String SentSubTeamCode;
    public String SentTeamCode;
    public Date ReceivedDate;
    public String ReceivedEmpID;
	
	public String CustomerName;
	public String No;
	
	public String RefNo;
	
	//Contract
	public String CONTNO;
	
	//Receipt
	public String ReceiptCode;
	
	//ChangeProduct
	public String ChangeProductPaperID;
	
	//ImpoundProduct
	public String ImpoundProductPaperID;
	
	//ChangeContract
	public String ChangeContractPaperID;
	
	//SendMoney
	public String reference2;
	
	public static UpdateDocumentHistoryInputInfo from(DocumentHistoryInfo documentHistory) {
		UpdateDocumentHistoryInputInfo info = new UpdateDocumentHistoryInputInfo();
				
		info.PrintHistoryID = documentHistory.PrintHistoryID;
		info.OrganizationCode = documentHistory.OrganizationCode;
		info.DatePrint = documentHistory.DatePrint;
		info.DocumentType = documentHistory.DocumentType;
		info.DocumentNumber = documentHistory.DocumentNumber;
		info.DocumentNo = documentHistory.DocumentNo;
		info.SyncedDate = documentHistory.SyncedDate;
		info.Selected = documentHistory.Selected;
		info.Deleted = documentHistory.Deleted;
		info.CustomerName = documentHistory.CustomerName;
		info.No = documentHistory.No;
		info.RefNo = documentHistory.RefNo;
		info.CONTNO = documentHistory.CONTNO;
		info.ReceiptCode = documentHistory.ReceiptCode;
		info.ChangeProductPaperID = documentHistory.ChangeProductPaperID;
		info.ImpoundProductPaperID = documentHistory.ImpoundProductPaperID;
		info.ChangeContractPaperID = documentHistory.ChangeProductPaperID;
		info.reference2 = documentHistory.reference2;
		info.PrintOrder= documentHistory.PrintOrder;
		info.Status = documentHistory.Status;
		info.SentDate = documentHistory.SentDate;
		info.SentEmpID = documentHistory.SentEmpID;
		info.SentSaleCode = documentHistory.SentSaleCode;
		info.SentSubTeamCode= documentHistory.SentSubTeamCode;
		info.SentTeamCode = documentHistory.SentTeamCode;
		info.ReceivedDate = documentHistory.ReceivedDate;
		info.ReceivedEmpID = documentHistory.ReceivedEmpID;
		//info.CreateBy = documentHistory.CreateBy;
		//info.CreateDate = documentHistory.CreateDate;
		info.LastUpdateBy = documentHistory.LastUpdateBy;
		info.LastUpdateDate = documentHistory.LastUpdateDate;
		
		
		return info;
	}
}
