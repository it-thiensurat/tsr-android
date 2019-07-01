package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class LimitInfo extends BHParcelable {
    public String LimitID;
    public String LimitType;
    public String EmpID;
    public float LimitMax;
    public String OrganizationCode;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;
}
