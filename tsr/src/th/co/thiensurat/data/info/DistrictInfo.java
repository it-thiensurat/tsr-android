package th.co.thiensurat.data.info;

import th.co.bighead.utilities.BHParcelable;

public class DistrictInfo extends BHParcelable {
	
	public DistrictInfo() {
		
	}
	
	public DistrictInfo(String districtCode, String districtName, String provinceCode) {
		DistrictCode = districtCode;
		DistrictName = districtName;
		ProvinceCode = provinceCode;
	}

	public String DistrictCode;
	public String DistrictName;
	public String ProvinceCode;
	
	public String toString() {
		return DistrictName;
	}

}
