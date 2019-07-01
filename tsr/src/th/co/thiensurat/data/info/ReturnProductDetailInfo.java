package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ReturnProductDetailInfo extends BHParcelable {

    // "DB-Field"
    public String ReturnProductID;
    public String OrganizationCode;
    public String ProductSerialNumber;	// กรณีเป็นเครื่องเทิร์นใส่ค่า TradeInProductCode 
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

    // "Extra-Field"
    public String EmpID;
    public String TeamCode;
    public String SubTeamCode;
    public Date ReturnDate;
    public Date RecevieDate;
    public String Remark;
//    public String Status;
//    public Date CreateDate;
//    public String CreateBy;
//    public Date LastUpdateDate;
//    public String LastUpdateBy;
    public String EmployeeName;
    public String TradeInBrandName;

}
