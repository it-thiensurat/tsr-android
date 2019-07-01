package th.co.thiensurat.data.info;

import java.util.List;

import th.co.bighead.utilities.BHParcelable;

public class SubTeamInfo extends BHParcelable {

	public String SubTeamCode;
	public String TeamCode;
	public String SubTeamName;
    public String SourceSystem;
    
    public String SourceSystemName;

	public List<EmployeeDetailInfo> Member;
}
