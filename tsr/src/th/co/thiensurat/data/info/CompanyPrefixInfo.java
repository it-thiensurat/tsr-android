package th.co.thiensurat.data.info;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHParcelable;

public class CompanyPrefixInfo extends BHParcelable {
	
	public enum PrefixCodeType {
		COMPANY,
		CORPORATE,
		DUMMY
	}

	public PrefixCodeType prefixCodeType;
	public String prefixName;
	
	private CompanyPrefixInfo(PrefixCodeType prefixCode, String prefixName) {
		this.prefixCodeType= prefixCode;
		this.prefixName = prefixName;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return prefixName;
	}
	
	public String prefixCode() {
		return prefixCodeType != PrefixCodeType.DUMMY ? prefixCodeType.name() : null;
	}
	
	public static List<CompanyPrefixInfo> gets() {
		return gets(false);
	}
	
	public static List<CompanyPrefixInfo> gets(boolean dummy) {
		List<CompanyPrefixInfo> list = new ArrayList<CompanyPrefixInfo>();
		if (dummy) {
			list.add(new CompanyPrefixInfo(PrefixCodeType.DUMMY, ""));
		}
		list.add(new CompanyPrefixInfo(PrefixCodeType.COMPANY, "บริษัท"));
		list.add(new CompanyPrefixInfo(PrefixCodeType.CORPORATE, "ห้างหุ้นส่วน"));
		
		return list;
	}
	
}
