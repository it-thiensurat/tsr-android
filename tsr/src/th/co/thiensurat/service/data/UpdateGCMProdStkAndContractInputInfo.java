package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.GCMProdStkAndContractInfo;

public class UpdateGCMProdStkAndContractInputInfo extends BHParcelable {

    public String GCMProdStkAndContractID;
    public String RefNo;
    public String ProdStkStatus;
    public Date LastUpdateDate;
    public String LastUpdateBy;

    public static UpdateGCMProdStkAndContractInputInfo from(GCMProdStkAndContractInfo gcm) {
        UpdateGCMProdStkAndContractInputInfo info = new UpdateGCMProdStkAndContractInputInfo();
        info.GCMProdStkAndContractID = gcm.GCMProdStkAndContractID;
        info.RefNo = gcm.RefNo;
        info.ProdStkStatus = gcm.ProdStkStatus;
        info.LastUpdateDate = gcm.LastUpdateDate;
    	info.LastUpdateBy = gcm.LastUpdateBy;
        return info;
    }

}