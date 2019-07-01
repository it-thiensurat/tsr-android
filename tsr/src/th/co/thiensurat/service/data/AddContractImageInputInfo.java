package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ContractImageInfo;

public class AddContractImageInputInfo extends BHParcelable {

	public String ImageID;
	public String RefNo;
	public String ImageName;
	public String ImageTypeCode;
	public Date SyncedDate;
	public String ImageData;

	public static AddContractImageInputInfo from(ContractImageInfo img) {
		AddContractImageInputInfo info = new AddContractImageInputInfo();
		info.ImageID = img.ImageID;
		info.RefNo = img.RefNo;
		info.ImageName = img.ImageName;
		info.ImageTypeCode = img.ImageTypeCode;
		info.SyncedDate = img.SyncedDate;
		return info;
	}
}
