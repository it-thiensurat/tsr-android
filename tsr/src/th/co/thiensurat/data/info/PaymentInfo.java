package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class PaymentInfo extends BHParcelable {

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
	public String CreditEmployeeLevelPath;	// โครงสร้างพนักงานเก็บเงิน (เก็บ TreeHistoryID ของ version ของพนักงานที่ทำให้เกิดรายการ)
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


	// -- for send money
	public String SendAmount;
	public String PaymentTypeName;
	public float DiffSendAmount;
	
	// -- for receipt
	public String ReceiptCode;
	public String CONTNO;
	public Date EFFDATE;
	public String CustomerName;
	public String IDCard;
	public String ProductCode;
	public String ProductModel;

	// sum
	public float SumPayment;
	public float MoneyOnHand;


	//ContractCloseAccount
	public float CloseAccountOutstandingAmount; //ยอดรวมทั้งหมดก่อนตัดสด
	public float CloseAccountDiscountAmount;//ส่วนลดตัดสด
	public float CloseAccountNetAmount; //ยอดคงเหลือหลังตัดสด
	public int CloseAccountPaymentPeriodNumber; //งวดที่ตัดสด
	public String CloseAccountPaymentID; //การจ่ายเงินที่มีการตัดสด

	//SalePaymentPeriod
	public String SalePaymentPeriodID;

	public float NetAmount;//ยอดเงินที่ต้องชำระ
	public int PaymentPeriodNumber;//งวดที่
	public Date PaymentAppointmentDate;

	//SalePaymentPeriodPayment
	public float Amount;//ยอดเงินที่ต้องชำระตามใบเสร็จ

	//Receipt
	public String ReceiptID;

	//For SaleReceiptPayment
	public float BalancesOfPeriod;//ยอดเงินคงเหลือของงวด
	public float Balances; //ยอดเงินคงเหลือของสัญญา

	//Contract
	public String CustomerID;
	public int MODE;
	public String MODEL;
	public String ProductSerialNumber;

	//Product
	public String ProductName;

	//Bank
	public String BankName;

	// ManualDocument
	public String ManualVolumeNo;	// เลขที่อ้างอิงของสัญญามือ / เล่มที่ของใบเสร็จมือ
	public long ManualRunningNo;

	//Employee
	public String SaleEmployeeName;

	public enum PaymentType1 {
		Cash, Credit, Cheque
	}

	public enum PaymentStatus { // Y = ต้องนำส่งเงิน, N = ไม่ต้องนำส่งเงิน
		Y, N
	}

	public  Boolean CanVoid; // สามารถยกเลิกรายการได้หรือไม่
	public Boolean VoidStatus;

}
