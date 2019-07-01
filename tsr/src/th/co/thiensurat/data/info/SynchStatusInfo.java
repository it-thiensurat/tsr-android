package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class SynchStatusInfo extends BHParcelable {
    public String EmpID;
    public String TeamCode;
    public String OrganizationCode;
    public int Progress;
    public String Status;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
}
