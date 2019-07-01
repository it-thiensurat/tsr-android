package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ReturnProductInfo extends BHParcelable {

    // "DB-Field"
    public String ReturnProductID;
    public String OrganizationCode;
    public String EmpID;
    public String TeamCode;
    public String SubTeamCode;
    public Date ReturnDate;
    public String FortnightID;
    public Date RecevieDate;
    public String Remark;
    public String Status;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    // "Extra-Field"
    public String EmployeeName;

}
