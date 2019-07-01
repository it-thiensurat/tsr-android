package th.co.thiensurat.data.info;

import th.co.bighead.utilities.BHParcelable;

public class ProvinceInfo extends BHParcelable {
	
	public ProvinceInfo() {
	}
	
	public ProvinceInfo(String provinceCode, String provinceName) {
		ProvinceCode = provinceCode;
		ProvinceName = provinceName;
	}

	public String ProvinceCode;
	public String ProvinceName;
	
	public String toString() {
		return ProvinceName;
	}

}
