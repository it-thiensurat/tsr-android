package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class AreaEmployeeInfo extends BHParcelable {
    public String EmployeeCode;
    public String SourceSystem;
    public String AreaCode;
    public Date SyncedDate;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
}
