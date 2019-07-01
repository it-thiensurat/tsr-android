package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class DocumentHistoryInfo extends BHParcelable {

	public String PrintHistoryID;
	public String OrganizationCode;
	public Date DatePrint;
	public String DocumentType;
	public String DocumentNumber;
	public String DocumentNo;
	public Date SyncedDate;
	public boolean Selected;
	public boolean Deleted;
	public String CreateBy;
	public Date CreateDate;
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
    public String CustomerFullName;
	public boolean isActive;
	
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
	public String TransactionNo;
	public String employeeFullName;

    //ManualDocument
    public String ManualDocumentID;
    public String ManualDocumentTypeID;
    public String ManualDocumentNumber;

    //ManualDocTypeID = 0
    public String con0CONTNO;
    public String dc0CustomerFullName;
	public boolean isActive0;

    //ManualDocTypeID = 1
    public String re1ReceiptCode;
    public String dc1CustomerFullName;
	public boolean isActive1;


	
}
