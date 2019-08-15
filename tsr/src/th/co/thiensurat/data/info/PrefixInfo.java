package th.co.thiensurat.data.info;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHParcelable;

public class PrefixInfo extends BHParcelable {

	public String PrefixCode;
	public String PrefixName;
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return PrefixName;
	}
	
	public PrefixInfo() {
		
	}

	/*** OLDEST ***/
	public PrefixInfo(String prefixCode, String prefixName) {
		PrefixCode= prefixCode;
		PrefixName = prefixName;
	}
	/*** OLDEST ***/

	public static List<PrefixInfo> getsCorporation() {
		return getsCorporation(false);
	}

	public static List<PrefixInfo> getsCorporation(boolean dummy) {
		List<PrefixInfo> list = new ArrayList<PrefixInfo>();
		if (dummy) {
			list.add(new PrefixInfo("", ""));
		}

		list.add(new PrefixInfo("company", "บริษัท"));
		list.add(new PrefixInfo("corporate", "ห้างหุ้นส่วน"));
		list.add(new PrefixInfo("legal_entity", "นิติบุคคล"));
		return list;
	}
}
