package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ProductStockInfo;

public class UpdateProductStockInputInfo extends BHParcelable {
	//public ProductStockInfo Info;
	
	public String ProductSerialNumber;
	public String OrganizationCode;
	public String ProductID;
	public String Type;
	public String TeamCode;
	public String Status;
	public Date ImportDate;
	public Date ScanDate;
	public String ScanByTeam;
	//public Date CreateDate;
	//public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	
	public static UpdateProductStockInputInfo from(ProductStockInfo ps) {
		UpdateProductStockInputInfo info = new UpdateProductStockInputInfo();
		info.ProductSerialNumber = ps.ProductSerialNumber;
		info.OrganizationCode = ps.OrganizationCode;
		info.ProductID = ps.ProductID;
		info.Type = ps.Type;
		info.TeamCode = ps.TeamCode;
		info.Status = ps.Status;
		info.ImportDate = ps.ImportDate;
		info.ScanDate = ps.ScanDate;
		info.ScanByTeam = ps.ScanByTeam;
		//info.CreateDate = ps.CreateDate;
		//info.CreateBy = ps.CreateBy;
		info.LastUpdateDate = ps.LastUpdateDate;
		info.LastUpdateBy = ps.LastUpdateBy;
		return info;
	}
	
}
