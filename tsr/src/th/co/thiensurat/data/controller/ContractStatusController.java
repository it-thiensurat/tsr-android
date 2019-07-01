package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.ContractStatusInfo;

public class ContractStatusController extends BaseController {

	public void addContractStatus(ContractStatusInfo info) {
		String sql = "insert into ContractStatus(StatusCode, StatusName)"
				+" values (?,?)";
		executeNonQuery(sql, new String[] {info.StatusCode, info.StatusName});
	}
	
	public void deleteContractStatus() {
		executeNonQuery("delete from ContractStatus", null);
	}
}
