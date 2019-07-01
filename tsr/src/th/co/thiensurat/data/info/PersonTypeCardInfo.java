package th.co.thiensurat.data.info;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.controller.DebtorCustomerController;

public class PersonTypeCardInfo extends BHParcelable {
	
	public enum PersonTypeCardEnum {
		PASSPORT, //หนังสือเดินทาง
		OUTLANDER, //บัตรต่างด้าว
		DUMMY, //""
        IDCARD, //บัตรประชาชน
        DRIVINGLICENSE, //ใบขับขี่
        OFFICIALCARD
		//, //บัตรข้าราชการ
		//IDCARD_device

	}

	/*** OLDEST ***/
	public PersonTypeCardEnum PersonTypeCard;
	public String PersonTypeCardCode;
	public String PersonTypeCardName;

	public PersonTypeCardInfo(String personTypeCardCode, String personTypeCardName) {
		PersonTypeCardCode = personTypeCardCode;
		PersonTypeCardName = personTypeCardName;
	}
	
	private PersonTypeCardInfo(PersonTypeCardEnum personTypeCard, String personTypeCardName) {
		this.PersonTypeCard = personTypeCard;
		this.PersonTypeCardName = personTypeCardName;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return PersonTypeCardName;
	}
	
	public String personTypeCode() {
		return PersonTypeCard != PersonTypeCardEnum.DUMMY ? PersonTypeCard.name() : null;
	}

	public static List<PersonTypeCardInfo> gets() {
		return gets(false);
	}
	
	public static List<PersonTypeCardInfo> gets(boolean dummy) {
		List<PersonTypeCardInfo> list = new ArrayList<PersonTypeCardInfo>();
		if (dummy) {
			list.add(new PersonTypeCardInfo(PersonTypeCardEnum.DUMMY, ""));
		}
		list.add(new PersonTypeCardInfo(PersonTypeCardEnum.PASSPORT, "หนังสือเดินทาง"));
		list.add(new PersonTypeCardInfo(PersonTypeCardEnum.OUTLANDER, "บัตรต่างด้าว"));
		list.add(new PersonTypeCardInfo(PersonTypeCardEnum.IDCARD, "บัตรประชาชน"));
		list.add(new PersonTypeCardInfo(PersonTypeCardEnum.DRIVINGLICENSE, "ใบขับขี่"));
		list.add(new PersonTypeCardInfo(PersonTypeCardEnum.OFFICIALCARD, "บัตรข้าราชการ"));
		//list.add(new PersonTypeCardInfo(PersonTypeCardEnum.IDCARD_device, "เครื่องอ่านบัตร"));

		return list;
	}

	public static List<PersonTypeCardInfo> gets(boolean dummy, PersonTypeInfo.PersonTypeEnum  personType) {
		List<PersonTypeCardInfo> list = new ArrayList<PersonTypeCardInfo>();
		if (dummy) {
			list.add(new PersonTypeCardInfo(PersonTypeCardEnum.DUMMY, ""));
		}

		switch (personType){
			case PERSON:
				list.add(new PersonTypeCardInfo(PersonTypeCardEnum.IDCARD, "บัตรประชาชน"));

				list.add(new PersonTypeCardInfo(PersonTypeCardEnum.DRIVINGLICENSE, "ใบขับขี่"));
				list.add(new PersonTypeCardInfo(PersonTypeCardEnum.OFFICIALCARD, "บัตรข้าราชการ"));
				//list.add(new PersonTypeCardInfo(PersonTypeCardEnum.IDCARD_device, "เครื่องอ่านบัตร"));
				break;
			case FOREIGNER:
				list.add(new PersonTypeCardInfo(PersonTypeCardEnum.PASSPORT, "หนังสือเดินทาง"));
				list.add(new PersonTypeCardInfo(PersonTypeCardEnum.OUTLANDER, "บัตรต่างด้าว"));
				break;
			default:
				break;
		}

		return list;
	}
}
