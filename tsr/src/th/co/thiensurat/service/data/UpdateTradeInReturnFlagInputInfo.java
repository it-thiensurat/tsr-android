package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ContractInfo;

public class UpdateTradeInReturnFlagInputInfo extends BHParcelable {
    public String RefNo;
    public Date LastUpdateDate;
    public String LastUpdateBy;

    public static UpdateTradeInReturnFlagInputInfo from(ContractInfo ct) {
        UpdateTradeInReturnFlagInputInfo info = new UpdateTradeInReturnFlagInputInfo();
        info.RefNo = ct.RefNo;
        info.LastUpdateDate = ct.LastUpdateDate;
        info.LastUpdateBy = ct.LastUpdateBy;
        return info;
    }
}
