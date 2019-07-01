package th.co.thiensurat.service.data;

import th.co.bighead.utilities.BHParcelable;

public class GetNextSalePaymentPeriodForSearchInputInfo extends BHParcelable{
	
	public String OrganizationCode;
	public String SaleTeamCode;
	public String SearchText;
	public int FortnightYear;
	public int FortnightNumber;
	public String ProcessType;     //-- Fixed - [BHPROJ-0016-3231][Android-ตัดสัญญาค้าง-WriteOffNPL] แก้ไข Code เพื่อรองรับการเพิ่ม Field เพื่อแยกว่าเป็น ปักษ์การขาย (Fortnight) ของ ฝ่ายขาย (Sale) vs ฝ่ายธุรกิจต่อเนื่อง (CRD)

}
