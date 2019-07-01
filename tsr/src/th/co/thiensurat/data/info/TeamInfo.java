package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class TeamInfo extends BHParcelable {

	public String Code;
	public String OrganizationCode;
	public String SupervisorCode;
	public String Name;
	public boolean NoPrint;
	public String TeamType;
	public Date SyncedDate;
    public String SourceSystem;

    public String TeamHeadCode;
    public String SourceSystemName;

	public String SubDepartmentCode;
}
