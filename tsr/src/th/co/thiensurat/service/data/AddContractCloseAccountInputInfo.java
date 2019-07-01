package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ContractCloseAccountInfo;

public class AddContractCloseAccountInputInfo extends BHParcelable {

    public String ContractCloseAccountID;
    public String OrganizationCode;
    public String RefNo;
    public String PaymentID;
    public String SalePaymentPeriodID;
    public float OutstandingAmount;
    public float DiscountAmount;
    public float NetAmount;
    public Date EffectiveDate;
    public String EffectiveBy;
    public String EffectiveTeamCode;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;
    public Date SyncedDate;

    public static AddContractCloseAccountInputInfo from(ContractCloseAccountInfo contractCloseAccount) {
        AddContractCloseAccountInputInfo info = new AddContractCloseAccountInputInfo();

        info.ContractCloseAccountID = contractCloseAccount.ContractCloseAccountID;
        info.OrganizationCode = contractCloseAccount.OrganizationCode;
        info.RefNo = contractCloseAccount.RefNo;
        info.PaymentID = contractCloseAccount.PaymentID;
        info.SalePaymentPeriodID = contractCloseAccount.SalePaymentPeriodID;
        info.OutstandingAmount = contractCloseAccount.OutstandingAmount;
        info.DiscountAmount = contractCloseAccount.DiscountAmount;
        info.NetAmount = contractCloseAccount.NetAmount;
        info.EffectiveDate = contractCloseAccount.EffectiveDate;
        info.EffectiveBy = contractCloseAccount.EffectiveBy;
        info.EffectiveTeamCode = contractCloseAccount.EffectiveTeamCode;
        info.CreateDate = contractCloseAccount.CreateDate;
        info.CreateBy = contractCloseAccount.CreateBy;
        info.LastUpdateDate = contractCloseAccount.LastUpdateDate;
        info.LastUpdateBy = contractCloseAccount.LastUpdateBy;
        info.SyncedDate = contractCloseAccount.SyncedDate;

        return info;
    }
}
