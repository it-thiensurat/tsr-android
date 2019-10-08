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
		list.add(new PrefixInfo("partnership", "ห้างหุ้นส่วนจํากัด"));
		list.add(new PrefixInfo("office", "สำนักงาน"));
		list.add(new PrefixInfo("Bureau", "สำนักการ"));
		list.add(new PrefixInfo("school", "โรงเรียน"));
		list.add(new PrefixInfo("College", "วิทยาลัย"));
		list.add(new PrefixInfo("University", "มหาวิทยาลัย"));
		list.add(new PrefixInfo("hotel", "โรงแรม"));
		list.add(new PrefixInfo("foundation", "มูลนิธิ"));
		list.add(new PrefixInfo("municipality", "เทศบาล"));

		return list;
	}
}
