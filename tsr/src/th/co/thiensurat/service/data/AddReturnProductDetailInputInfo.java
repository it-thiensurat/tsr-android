package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ReturnProductDetailInfo;

public class AddReturnProductDetailInputInfo extends BHParcelable {
    public String ReturnProductID;
    public String OrganizationCode;
    public String ProductSerialNumber;
    public String ProductID;
    public String RefNo;
    public String TradeInBrandCode;
    public String TradeInProductModel;
    public Date SyncedDate;
    
    /** Fixed [BHPRJ00301-3728] **/
    public String SourceStatus;
    public String Status;
    public Date ReceivedDate;
    public String ReceivedBy;
    public String ReceivedRemark;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
	
	public static AddReturnProductDetailInputInfo from(ReturnProductDetailInfo input) {
		AddReturnProductDetailInputInfo info = new AddReturnProductDetailInputInfo();
	    info.ReturnProductID = input.ReturnProductID;
	    info.OrganizationCode = input.OrganizationCode;
	    info.ProductSerialNumber = input.ProductSerialNumber;
	    info.ProductID = input.ProductID;
	    info.RefNo = input.RefNo;
	    info.TradeInBrandCode = input.TradeInBrandCode;
	    info.TradeInProductModel = input.TradeInProductModel;
	    info.SyncedDate = input.SyncedDate;
	    
	    /** Fixed [BHPRJ00301-3728] **/
	    info.SourceStatus = input.SourceStatus;
	    info.Status = input.Status;
	    info.ReceivedDate = input.ReceivedDate;
	    info.ReceivedBy = input.ReceivedBy;
	    info.ReceivedRemark = input.ReceivedRemark;
	    info.CreateDate= input.CreateDate;
	    info.CreateBy = input.CreateBy;
	    info.LastUpdateDate = input.LastUpdateDate;
	    info.LastUpdateBy = input.LastUpdateBy;
		return info;
	}
}
