package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ProductStockInfo;

public class UpdateProductStockStatusInputInfo extends BHParcelable {

//	public String OrganizationCode;
//	public String SerialNo;
//	public String Status;
//	public Date ScanDate;
//	public String ScanByTeam;
	
//	public ProductStockInfo ProductStockInfo;
	
	
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
	//public Date CreateDate;
	//public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;

	// Product
//	public String ProductName;
//	public String ProductCode;
//	public String ProductModel;
//	public float ProductPrice;
//	public float CashDiscount;
//	public float CreditDiscount;
//	public String ProductDescription;
//	public String TeamName;
	public String ScanByTeam;
	
	public static UpdateProductStockStatusInputInfo from(ProductStockInfo productStock) {
		UpdateProductStockStatusInputInfo info = new  UpdateProductStockStatusInputInfo();
		
		info.ProductSerialNumber = productStock.ProductSerialNumber;
		info.OrganizationCode = productStock.OrganizationCode;
		info.ProductID = productStock.ProductID;
		info.Type = productStock.Type;
		info.TeamCode = productStock.TeamCode; 
		info.Status = productStock.Status;
		info.ImportDate = productStock.ImportDate;
		info.ScanDate = productStock.ScanDate;
		info.SyncedDate = productStock.SyncedDate;
		//info.CreateDate = productStock.CreateDate;
		//info.CreateBy = productStock.CreateBy;
		info.LastUpdateDate = productStock.LastUpdateDate;
		info.LastUpdateBy = productStock.LastUpdateBy;
		
		// Product
//		info.ProductName = productStock.ProductName;
//		info.ProductCode = productStock.ProductCode;
//		info.ProductModel = productStock.ProductModel;
//		info.ProductPrice = productStock.ProductPrice;
//		info.CashDiscount = productStock.CashDiscount;
//		info.CreditDiscount = productStock.CreditDiscount;
//		info.ProductDescription = productStock.ProductDescription;
//		info.TeamName = productStock.TeamName;
		info.ScanByTeam = productStock.ScanByTeam;
		
		
		return info;
	}
	
}
