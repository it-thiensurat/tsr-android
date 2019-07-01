package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class SalePaymentPeriodInfo extends BHParcelable {

	public String SalePaymentPeriodID;
	public String RefNo;
	public int PaymentPeriodNumber;
	public float PaymentAmount;
	public float Discount;
	public float NetAmount;
	public boolean PaymentComplete;
	public Date PaymentDueDate;
	public Date PaymentAppointmentDate;
	public String TripID;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;
	public float CloseAccountDiscountAmount;

	// Extends Feilds
	public float OutstandingAmount;
	public float CurrentPaymentAmount;
	public String ReceiptID;
	public String ReceiptCode;
	public Date ReceiptDate;

	public String CONTNO;
	public String OrganizationCode;
	public Date InstallDate;
	public String SaleTeamCode;
	public String CustomerID;
	public String CustomerFullName;
	public String CompanyName;
	public String AssignID;
	public String AssigneeEmpID;
	public String AssigneeTeamCode;
	public String AssigneeEmpName;
	public float SummaryPaymentAmount;
	public String ProductSerialNumber;
	public String ProductName;
	public int CountPostpone;

	public int PaymentAppointmentDateCount;
	public int AssigneeCount;
	
	public float Amount;
	public float NextPaymentAmount;

	/* Trip */
	public int TripNumber;
	public int TripYear;
	/**/
	public int FortnightNumber;
	public int FortnightYear;
	
	public int NextFortnightNumber;
	public int NextFortnightYear;
	
	public float SummaryNetAmount;
	public Date SendDate;
	public String IDCard;

    public int index;

	public boolean OverDue;

    public boolean isLastChild;

    public float FirstPaymentAmount;
	public Date PayDate;
	public int CountRefNo;
	public float SumFirstPaymentAmount;

	// ContractCloseAccount
	public float CloseDiscountAmount;
	public int MinPaymentPeriodNumberForContractCloseAccount;
	public String MinSalePaymentPeriodIDForContractCloseAccount;
	public float TotalOutstandingAmount;


	// Contract
	public int MODE;
}
