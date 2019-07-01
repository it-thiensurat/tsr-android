package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ProductStockInfo extends BHParcelable {

	// DB-Field
	public String ProductSerialNumber;
	public String OrganizationCode;
	public String ProductID;
	public String Type;
	public String TeamCode;
	public String Status;
	public Date ImportDate;
	public Date ScanDate;
	public Date SyncedDate;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;


	// Product
	public String ProductName;
	public String ProductCode;
	public String ProductModel;
	public float ProductPrice;
	public float CashDiscount;
	public float CreditDiscount;
	public String ProductDescription;
	public String TeamName;
	public String ScanByTeam;

    // contract
    public String TradeInBrandCode;
    public String TradeInProductModel;
    public String RefNo;


	//ใช้สำหรับ CRD
	public String OldOwnerEmpID;   // พนักงานเจ้าของสินค้า (คนเก่าก่อนการยืม)
	public String NewOwnerEmpID;  // พนักงานที่ขอยืมสินค้ามาขาย
}
