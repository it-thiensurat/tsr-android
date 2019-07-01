package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.PaymentInfo;

public class AddPaymentInputInfo extends BHParcelable {

	public String PaymentID;
	public String OrganizationCode;
	public String SendMoneyID;
	public String PaymentType;
	public boolean PayPartial;
	public String BankCode;
	public String ChequeNumber;
	public String ChequeBankBranch;
	public String ChequeDate;
	public String CreditCardNumber;
	public String CreditCardApproveCode;
	public String CreditEmployeeLevelPath;
	public String TripID;
	public String Status;
	public String RefNo;
	public String PayPeriod;
	public Date PayDate;
	public float PAYAMT;
	public String CashCode;
	public String EmpID;
	public String TeamCode;
	public String receiptkind;
	public String Kind;
	public String BookNo;
	public String ReceiptNo;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;
    
    public static AddPaymentInputInfo from(PaymentInfo p) {
    	AddPaymentInputInfo info = new AddPaymentInputInfo();
    	info.PaymentID = p.PaymentID;
    	info.OrganizationCode = p.OrganizationCode;
    	//info.SendMoneyID = p.PaymentType;
    	info.PaymentType = p.PaymentType;
    	info.PayPeriod = p.PayPeriod;
    	info.BankCode = p.BankCode;
    	info.ChequeNumber = p.ChequeNumber;
    	info.ChequeBankBranch = p.ChequeBankBranch;
    	info.ChequeDate = p.ChequeDate;
    	info.CreditCardNumber=p.CreditCardNumber;
    	info.CreditCardApproveCode = p.CreditCardApproveCode;
    	info.CreditEmployeeLevelPath = p.CreditEmployeeLevelPath;
    	info.TripID = p.TripID;
    	info.Status = p.Status;
    	info.RefNo=p.RefNo;    	    			
    	info.PayPeriod=p.PayPeriod;
    	info.PayDate=p.PayDate;
    	info.PAYAMT=p.PAYAMT;
    	info.CashCode=p.CashCode;
		info.EmpID=p.EmpID;
    	info.TeamCode=p.TeamCode;
    	info.receiptkind=p.receiptkind;
    	info.Kind=p.Kind;
    	info.BookNo=p.BookNo;    	
    	info.ReceiptNo=p.ReceiptNo;
    	info.CreateDate=p.CreateDate;
    	info.CreateBy=p.CreateBy;
    	info.LastUpdateDate=p.LastUpdateDate;
    	info.LastUpdateBy=p.LastUpdateBy;
    	info.SyncedDate = p.SyncedDate;
    	return info;
    }
    
}
