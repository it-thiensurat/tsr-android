package th.co.thiensurat.data.info;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHParcelable;

public class PersonTypeInfo extends BHParcelable {
	
	public enum PersonTypeEnum {
		PERSON,
		CORPORATION,
		FOREIGNER,
		DUMMY
	}
	
	/*** OLDEST ***/
	public PersonTypeEnum PersonType;
	public String PersonTypeCode;
	public String PersonTypeName;

	public PersonTypeInfo(String personTypeCode, String personTypeName) {
		PersonTypeCode = personTypeCode;
		PersonTypeName = personTypeName;
	}

	private PersonTypeInfo(PersonTypeEnum type, String typeCode, String name) {
		PersonType = type;
		PersonTypeCode = typeCode;
		PersonTypeName = name;
	}
	
	public static List<PersonTypeInfo> gets() {
		return gets(false);
	}
	
	public static List<PersonTypeInfo> gets(boolean dummy) {
		List<PersonTypeInfo> list = new ArrayList<PersonTypeInfo>();
		if (dummy) {
			list.add(new PersonTypeInfo(PersonTypeEnum.DUMMY, "", ""));
		}
		
		list.add(new PersonTypeInfo(PersonTypeEnum.PERSON, "0","บุคคลธรรมดา"));
		list.add(new PersonTypeInfo(PersonTypeEnum.CORPORATION,"1", "นิติบุคคล"));
		list.add(new PersonTypeInfo(PersonTypeEnum.FOREIGNER, "2", "บุคคลต่างชาติ"));
		
		return list;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return PersonTypeName;
	}
	
	public String personTypeCode() {
		return PersonType != PersonTypeEnum.DUMMY ? Integer.toString(PersonType.ordinal()) : null;
	}
}
