package th.co.thiensurat.service.data;

import th.co.bighead.utilities.BHParcelable;


/*** Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/


public class GetContractStatusFinishForCreditBySearchInputInfo extends BHParcelable {
	public String OrganizationCode;
	public String StatusName;
	public String SearchText;
}
