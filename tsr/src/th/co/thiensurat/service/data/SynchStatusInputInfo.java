package th.co.thiensurat.service.data;

import th.co.bighead.utilities.BHParcelable;

public class SynchStatusInputInfo extends BHParcelable {
    public String OrganizationCode;
    public String TeamCode;
    public String EmpID;
    public String CallStatus; // สถานะที่ส่งมาจาก mibile เพื่อบอกว่าเป็นการเรียก ws เพื่อสร้าง (CREATE) หรือ ตรวจสอบ (CHECKE)
    public boolean IsAdmin;
}
