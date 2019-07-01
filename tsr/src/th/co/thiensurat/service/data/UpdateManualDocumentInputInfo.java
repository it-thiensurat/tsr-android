package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ManualDocumentInfo;

public class UpdateManualDocumentInputInfo extends BHParcelable {
	public String DocumentID;
    public String DocumentType;
    public String DocumentNumber;	// เลขที่สัญญา / เลขที่ใบเสร็จ
    public String ManualDocTypeID;
    public String ManualVolumeNo;	// เลขที่อ้างอิงของสัญญามือ / เล่มที่ของใบเสร็จมือ
    public long ManualRunningNo;
    public String Note;
    public String TeamCode;
    public String SubTeamCode;
    public String EmpID;
//    public String CreatedBy;
//    public Date CreatedDate;
    public String UpdateBy;
    public Date UpdateDate;
    public Date SyncedDate;
    public boolean isActive;
	
	public static  UpdateManualDocumentInputInfo from(ManualDocumentInfo localInfo) {
		UpdateManualDocumentInputInfo info = new UpdateManualDocumentInputInfo();
		info.DocumentID = localInfo.DocumentID;
		info.DocumentType = localInfo.DocumentType;
		info.DocumentNumber = localInfo.DocumentNumber;
		info.ManualDocTypeID = localInfo.ManualDocTypeID;
		info.ManualVolumeNo = localInfo.ManualVolumeNo;
		info.ManualRunningNo = localInfo.ManualRunningNo;
		info.Note = localInfo.Note;
		info.TeamCode = localInfo.TeamCode;
		info.SubTeamCode = localInfo.SubTeamCode;
		info.EmpID = localInfo.EmpID;
//		info.CreatedBy = localInfo.CreatedBy;
//		info.CreatedDate = localInfo.CreatedDate;
		info.UpdateBy = localInfo.UpdateBy;
		info.UpdateDate = localInfo.UpdateDate;
		info.SyncedDate = localInfo.SyncedDate;
		info.isActive = localInfo.isActive;
		return info;
	}
}
