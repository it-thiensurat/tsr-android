package th.co.thiensurat.data.info;

import th.co.bighead.utilities.BHParcelable;

public class BankInfo extends BHParcelable {
	public BankInfo() {

	}

	public BankInfo(String bankCode, String bankName) {
		// TODO Auto-generated constructor stub
		BankCode = bankCode;
		BankName = bankName;
	}

	public String BankCode;
	public String BankName;

	public String toString() {
		return BankName;
	}
}
