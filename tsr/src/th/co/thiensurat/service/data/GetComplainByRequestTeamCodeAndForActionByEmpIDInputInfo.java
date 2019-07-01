package th.co.thiensurat.service.data;

import th.co.bighead.utilities.BHParcelable;

public class GetComplainByRequestTeamCodeAndForActionByEmpIDInputInfo extends BHParcelable {
	public String OrganizationCode;
	public String RequestTeamCode;
	public String AssigneeTeamCode;
	public String TaskType;
	public String RequestBy;   // Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง
}
