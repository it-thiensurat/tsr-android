package th.co.thiensurat.data.info;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHParcelable;

public class GenderInfo extends BHParcelable {
	public enum GenderTypeEnum {
		MALE,
		FEMALE,
		DUMMY
	}
	
	/*** OLDEST ***/
	public GenderTypeEnum GenderType;
	public String GenderCode;
	public String GenderName;

	public GenderInfo(String genderCode, String genderName) {
		GenderCode = genderCode;
		GenderName = genderName;
	}
    /*** OLDEST ***/


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return GenderName;
	}
	
	public String genderCode() {
		return GenderType != GenderType.DUMMY ? GenderType.name() : null;
	}
	
	private GenderInfo(GenderTypeEnum genderType, String genderName) {
		this.GenderType = genderType;
		this.GenderName = genderName;
	}
	
	public static List<GenderInfo> gets() {
		return gets(false);
	}
	
	public static List<GenderInfo> gets(boolean dummy) {
		List<GenderInfo> list = new ArrayList<GenderInfo>();
		if (dummy) {
			list.add(new GenderInfo(GenderTypeEnum.DUMMY, ""));
		}
		list.add(new GenderInfo(GenderTypeEnum.MALE, "ชาย"));
		list.add(new GenderInfo(GenderTypeEnum.FEMALE, "หญิง"));
		
		return list;
	}
	
	
}
