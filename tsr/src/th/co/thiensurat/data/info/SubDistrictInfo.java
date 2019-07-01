package th.co.thiensurat.data.info;

import th.co.bighead.utilities.BHParcelable;

public class SubDistrictInfo extends BHParcelable {
	
	public SubDistrictInfo() {
		
	}

	public SubDistrictInfo(String subDistrictCode, String subDistrictName, String districtCode, String postcode) {
		SubDistrictCode = subDistrictCode;
		SubDistrictName = subDistrictName;
		DistrictCode = districtCode;
		Postcode = postcode;
	}
	
	public String SubDistrictCode;
	public String SubDistrictName;
	public String DistrictCode;
	
	public String Postcode;

	public String toString() {
		return SubDistrictName;
	}
}
