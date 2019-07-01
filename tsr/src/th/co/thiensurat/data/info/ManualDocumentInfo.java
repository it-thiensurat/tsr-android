package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class ManualDocumentInfo extends BHParcelable {

	/*
	public String DocumentID;
	public String DocumentNo;
	public String DocumentType;
	public String ManualBookNo;
	public String ManualRunningNo;
	public String Note;
	*/
	
	public String DocumentID;
    public String DocumentType;		// ประเภทของเอกสาร
    public String DocumentNumber;	// เลขที่สัญญา / เลขที่ใบเสร็จ
    public String ManualDocTypeID;	// ประเภทของเอกสารมือ
    public String ManualVolumeNo;	// เล่มที่ของใบเสร็จมือ
    public long ManualRunningNo;    // เลขที่ของเอกสารอ้างอิง
    public String Note;
    public String TeamCode;
    public String SubTeamCode;
    public String EmpID;
    public String CreatedBy;
    public Date CreatedDate;
    public String UpdateBy;
    public Date UpdateDate;
    public Date SyncedDate;
    public boolean isActive;

}
