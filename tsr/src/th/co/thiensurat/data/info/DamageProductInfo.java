package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

/**
 * Created by Tanawut on 6/3/2558.
 */
public class DamageProductInfo extends BHParcelable {

    public String DamageProductID;
    public String OrganizationCode;
    public String EmpID;
    public String TeamCode;
    public String SubTeamCode;
    public Date ReturnDate;
    public String ReturnType;
    public String ReturnCause;
    public String ProductSerialNumber;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

}
