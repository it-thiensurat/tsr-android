package th.co.thiensurat.service.data;

import java.util.List;

import th.co.thiensurat.data.info.ContractInfo;

/*** Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/


public class GetContractStatusFinishForCreditBySearchOutputInfo extends GenericOutputInfo {
	public List<ContractInfo> Info;
}
