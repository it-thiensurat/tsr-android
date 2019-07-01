package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ProblemInfo extends BHParcelable {
	// <ProblemID>string</ProblemID>
	// <OrganizationCode>string</OrganizationCode>
	// <ProblemName>string</ProblemName>
	// <ProblemType>string</ProblemType>

	public String ProblemID;
	public String OrganizationCode;
	public String ProblemName;
	public String ProblemType;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public String SourceSystem;
	public String SourceSystemName;

	public enum ProblemType {
		ChangeProduct, ImpoundProduct, ChangeContract, WriteOffNPL
	}
}
