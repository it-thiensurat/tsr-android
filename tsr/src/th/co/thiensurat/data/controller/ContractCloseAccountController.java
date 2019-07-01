package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ContractCloseAccountInfo;

public class ContractCloseAccountController extends BaseController {

	public List<ContractCloseAccountInfo> getAllCloseAccountDiscount() {
		return executeQueryList("SELECT * FROM ContractCloseAccount", null, ContractCloseAccountInfo.class);
	}

    public ContractCloseAccountInfo getContractCloseAccountByRefNo(String OrganizationCode, String RefNo) {
    	ContractCloseAccountInfo ret = null;
        String sql = "SELECT * FROM ContractCloseAccount "
        			+ " WHERE (OrganizationCode = ?) AND (RefNo = ?)";
        ret = executeQueryObject(sql, new String[]{ OrganizationCode, RefNo}, ContractCloseAccountInfo.class);
        return ret;
    }

    public void addContractCloseAccount(ContractCloseAccountInfo info) {
        String sql = "INSERT INTO ContractCloseAccount(OrganizationCode, ContractCloseAccountID, RefNo, PaymentID, SalePaymentPeriodID, OutstandingAmount, DiscountAmount, NetAmount, EffectiveDate, EffectiveBy, EffectiveTeamCode, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, SyncedDate)"
                +" VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        executeNonQuery(sql, new String[] {info.OrganizationCode, info.ContractCloseAccountID, info.RefNo, info.PaymentID, info.SalePaymentPeriodID, valueOf(info.OutstandingAmount), valueOf(info.DiscountAmount), valueOf(info.NetAmount), valueOf(info.EffectiveDate), info.EffectiveBy, info.EffectiveTeamCode, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, valueOf(info.SyncedDate)});
    }

    public void updateContractCloseAccount(ContractCloseAccountInfo info) {
        String sql = "UPDATE ContractCloseAccount " +
        		" SET  	RefNo = ?, " +
        		"		PaymentID = ?, " +
        		"		SalePaymentPeriodID = ?, " +
        		"		OutstandingAmount = ?, " +
        		"		DiscountAmount = ?, " +
        		"		NetAmount = ?, " +
        		"		EffectiveDate = ?, " +
        		"		EffectiveBy = ?, " +
        		"		EffectiveTeamCode = ?, " +
        		"		LastUpdateDate = ?, " +
        		"		LastUpdateBy = ?" +
        		" WHERE (OrganizationCode = ?) AND (ContractCloseAccountID = ?)";
        executeNonQuery(sql, new String[] {info.RefNo, info.PaymentID, info.SalePaymentPeriodID, valueOf(info.OutstandingAmount), valueOf(info.DiscountAmount), valueOf(info.NetAmount), valueOf(info.EffectiveDate), info.EffectiveBy, info.EffectiveTeamCode, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.OrganizationCode, info.ContractCloseAccountID });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteContractCloseAccountAll() {
        executeNonQuery("DELETE FROM ContractCloseAccount", null);
    }
    public void deleteContractCloseAccountByRefNo(String refNo) {
        String sql = "DELETE FROM ContractCloseAccount WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[]{refNo});
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////


}
