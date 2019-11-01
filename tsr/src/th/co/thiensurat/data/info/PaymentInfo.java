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
















	public String getPaymentID() {
		return PaymentID;
	}

	public void setPaymentID(String paymentID) {
		PaymentID = paymentID;
	}

	public String getOrganizationCode() {
		return OrganizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		OrganizationCode = organizationCode;
	}

	public String getSendMoneyID() {
		return SendMoneyID;
	}

	public void setSendMoneyID(String sendMoneyID) {
		SendMoneyID = sendMoneyID;
	}

	public String getPaymentType() {
		return PaymentType;
	}

	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}

	public boolean isPayPartial() {
		return PayPartial;
	}

	public void setPayPartial(boolean payPartial) {
		PayPartial = payPartial;
	}

	public String getBankCode() {
		return BankCode;
	}

	public void setBankCode(String bankCode) {
		BankCode = bankCode;
	}

	public String getChequeNumber() {
		return ChequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		ChequeNumber = chequeNumber;
	}

	public String getChequeBankBranch() {
		return ChequeBankBranch;
	}

	public void setChequeBankBranch(String chequeBankBranch) {
		ChequeBankBranch = chequeBankBranch;
	}

	public String getChequeDate() {
		return ChequeDate;
	}

	public void setChequeDate(String chequeDate) {
		ChequeDate = chequeDate;
	}

	public String getCreditCardNumber() {
		return CreditCardNumber;
	}

	public void setCreditCardNumber(String creditCardNumber) {
		CreditCardNumber = creditCardNumber;
	}

	public String getCreditCardApproveCode() {
		return CreditCardApproveCode;
	}

	public void setCreditCardApproveCode(String creditCardApproveCode) {
		CreditCardApproveCode = creditCardApproveCode;
	}

	public String getCreditEmployeeLevelPath() {
		return CreditEmployeeLevelPath;
	}

	public void setCreditEmployeeLevelPath(String creditEmployeeLevelPath) {
		CreditEmployeeLevelPath = creditEmployeeLevelPath;
	}

	public String getTripID() {
		return TripID;
	}

	public void setTripID(String tripID) {
		TripID = tripID;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getRefNo() {
		return RefNo;
	}

	public void setRefNo(String refNo) {
		RefNo = refNo;
	}

	public String getPayPeriod() {
		return PayPeriod;
	}

	public void setPayPeriod(String payPeriod) {
		PayPeriod = payPeriod;
	}

	public Date getPayDate() {
		return PayDate;
	}

	public void setPayDate(Date payDate) {
		PayDate = payDate;
	}

	public float getPAYAMT() {
		return PAYAMT;
	}

	public void setPAYAMT(float PAYAMT) {
		this.PAYAMT = PAYAMT;
	}

	public String getCashCode() {
		return CashCode;
	}

	public void setCashCode(String cashCode) {
		CashCode = cashCode;
	}

	public String getEmpID() {
		return EmpID;
	}

	public void setEmpID(String empID) {
		EmpID = empID;
	}

	public String getTeamCode() {
		return TeamCode;
	}

	public void setTeamCode(String teamCode) {
		TeamCode = teamCode;
	}

	public String getReceiptkind() {
		return receiptkind;
	}

	public void setReceiptkind(String receiptkind) {
		this.receiptkind = receiptkind;
	}

	public String getKind() {
		return Kind;
	}

	public void setKind(String kind) {
		Kind = kind;
	}

	public String getBookNo() {
		return BookNo;
	}

	public void setBookNo(String bookNo) {
		BookNo = bookNo;
	}

	public String getReceiptNo() {
		return ReceiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		ReceiptNo = receiptNo;
	}

	public Date getCreateDate() {
		return CreateDate;
	}

	public void setCreateDate(Date createDate) {
		CreateDate = createDate;
	}

	public String getCreateBy() {
		return CreateBy;
	}

	public void setCreateBy(String createBy) {
		CreateBy = createBy;
	}

	public Date getLastUpdateDate() {
		return LastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		LastUpdateDate = lastUpdateDate;
	}

	public String getLastUpdateBy() {
		return LastUpdateBy;
	}

	public void setLastUpdateBy(String lastUpdateBy) {
		LastUpdateBy = lastUpdateBy;
	}

	public Date getSyncedDate() {
		return SyncedDate;
	}

	public void setSyncedDate(Date syncedDate) {
		SyncedDate = syncedDate;
	}

	public String getSendAmount() {
		return SendAmount;
	}

	public void setSendAmount(String sendAmount) {
		SendAmount = sendAmount;
	}

	public String getPaymentTypeName() {
		return PaymentTypeName;
	}

	public void setPaymentTypeName(String paymentTypeName) {
		PaymentTypeName = paymentTypeName;
	}

	public float getDiffSendAmount() {
		return DiffSendAmount;
	}

	public void setDiffSendAmount(float diffSendAmount) {
		DiffSendAmount = diffSendAmount;
	}

	public String getReceiptCode() {
		return ReceiptCode;
	}

	public void setReceiptCode(String receiptCode) {
		ReceiptCode = receiptCode;
	}

	public String getCONTNO() {
		return CONTNO;
	}

	public void setCONTNO(String CONTNO) {
		this.CONTNO = CONTNO;
	}

	public Date getEFFDATE() {
		return EFFDATE;
	}

	public void setEFFDATE(Date EFFDATE) {
		this.EFFDATE = EFFDATE;
	}

	public String getCustomerName() {
		return CustomerName;
	}

	public void setCustomerName(String customerName) {
		CustomerName = customerName;
	}

	public String getIDCard() {
		return IDCard;
	}

	public void setIDCard(String IDCard) {
		this.IDCard = IDCard;
	}

	public String getProductCode() {
		return ProductCode;
	}

	public void setProductCode(String productCode) {
		ProductCode = productCode;
	}

	public String getProductModel() {
		return ProductModel;
	}

	public void setProductModel(String productModel) {
		ProductModel = productModel;
	}

	public float getSumPayment() {
		return SumPayment;
	}

	public void setSumPayment(float sumPayment) {
		SumPayment = sumPayment;
	}

	public float getMoneyOnHand() {
		return MoneyOnHand;
	}

	public void setMoneyOnHand(float moneyOnHand) {
		MoneyOnHand = moneyOnHand;
	}

	public float getCloseAccountOutstandingAmount() {
		return CloseAccountOutstandingAmount;
	}

	public void setCloseAccountOutstandingAmount(float closeAccountOutstandingAmount) {
		CloseAccountOutstandingAmount = closeAccountOutstandingAmount;
	}

	public float getCloseAccountDiscountAmount() {
		return CloseAccountDiscountAmount;
	}

	public void setCloseAccountDiscountAmount(float closeAccountDiscountAmount) {
		CloseAccountDiscountAmount = closeAccountDiscountAmount;
	}

	public float getCloseAccountNetAmount() {
		return CloseAccountNetAmount;
	}

	public void setCloseAccountNetAmount(float closeAccountNetAmount) {
		CloseAccountNetAmount = closeAccountNetAmount;
	}

	public int getCloseAccountPaymentPeriodNumber() {
		return CloseAccountPaymentPeriodNumber;
	}

	public void setCloseAccountPaymentPeriodNumber(int closeAccountPaymentPeriodNumber) {
		CloseAccountPaymentPeriodNumber = closeAccountPaymentPeriodNumber;
	}

	public String getCloseAccountPaymentID() {
		return CloseAccountPaymentID;
	}

	public void setCloseAccountPaymentID(String closeAccountPaymentID) {
		CloseAccountPaymentID = closeAccountPaymentID;
	}

	public String getSalePaymentPeriodID() {
		return SalePaymentPeriodID;
	}

	public void setSalePaymentPeriodID(String salePaymentPeriodID) {
		SalePaymentPeriodID = salePaymentPeriodID;
	}

	public float getNetAmount() {
		return NetAmount;
	}

	public void setNetAmount(float netAmount) {
		NetAmount = netAmount;
	}

	public int getPaymentPeriodNumber() {
		return PaymentPeriodNumber;
	}

	public void setPaymentPeriodNumber(int paymentPeriodNumber) {
		PaymentPeriodNumber = paymentPeriodNumber;
	}

	public Date getPaymentAppointmentDate() {
		return PaymentAppointmentDate;
	}

	public void setPaymentAppointmentDate(Date paymentAppointmentDate) {
		PaymentAppointmentDate = paymentAppointmentDate;
	}

	public float getAmount() {
		return Amount;
	}

	public void setAmount(float amount) {
		Amount = amount;
	}

	public String getReceiptID() {
		return ReceiptID;
	}

	public void setReceiptID(String receiptID) {
		ReceiptID = receiptID;
	}

	public float getBalancesOfPeriod() {
		return BalancesOfPeriod;
	}

	public void setBalancesOfPeriod(float balancesOfPeriod) {
		BalancesOfPeriod = balancesOfPeriod;
	}

	public float getBalances() {
		return Balances;
	}

	public void setBalances(float balances) {
		Balances = balances;
	}

	public String getCustomerID() {
		return CustomerID;
	}

	public void setCustomerID(String customerID) {
		CustomerID = customerID;
	}

	public int getMODE() {
		return MODE;
	}

	public void setMODE(int MODE) {
		this.MODE = MODE;
	}

	public String getMODEL() {
		return MODEL;
	}

	public void setMODEL(String MODEL) {
		this.MODEL = MODEL;
	}

	public String getProductSerialNumber() {
		return ProductSerialNumber;
	}

	public void setProductSerialNumber(String productSerialNumber) {
		ProductSerialNumber = productSerialNumber;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getBankName() {
		return BankName;
	}

	public void setBankName(String bankName) {
		BankName = bankName;
	}

	public String getManualVolumeNo() {
		return ManualVolumeNo;
	}

	public void setManualVolumeNo(String manualVolumeNo) {
		ManualVolumeNo = manualVolumeNo;
	}

	public long getManualRunningNo() {
		return ManualRunningNo;
	}

	public void setManualRunningNo(long manualRunningNo) {
		ManualRunningNo = manualRunningNo;
	}

	public String getSaleEmployeeName() {
		return SaleEmployeeName;
	}

	public void setSaleEmployeeName(String saleEmployeeName) {
		SaleEmployeeName = saleEmployeeName;
	}

	public Boolean getCanVoid() {
		return CanVoid;
	}

	public void setCanVoid(Boolean canVoid) {
		CanVoid = canVoid;
	}

	public Boolean getVoidStatus() {
		return VoidStatus;
	}

	public void setVoidStatus(Boolean voidStatus) {
		VoidStatus = voidStatus;
	}
}
