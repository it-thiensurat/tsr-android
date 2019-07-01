package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.BankInfo;

public class BankController extends BaseController {


	public void addBank(BankInfo info) {
		final String QUERY_BANK_INSERT = "INSERT INTO Bank(BankCode, BankName) VALUES(?, ?)";
		executeNonQuery(QUERY_BANK_INSERT, new String[] { info.BankCode, info.BankName });
	}

	public List<BankInfo> getBank() {
		final String QUERY_BANK_GET_ALL = "SELECT * FROM Bank";
		return executeQueryList(QUERY_BANK_GET_ALL, null, BankInfo.class);
	}
	
	public BankInfo getBankInfo(String bankCode) {
		final String QUERY_BANK_GET_BY_CODE = "SELECT * FROM Bank WHERE BankCode = ?";
		return executeQueryObject(QUERY_BANK_GET_BY_CODE, new String[] { bankCode }, BankInfo.class);			
	}
	
	public void deleteBank() {
		executeNonQuery("delete from Bank", null);
	}
}
