package th.co.thiensurat.service.data;


import th.co.bighead.utilities.BHParcelable;

public class GetCurrentFortnightInputInfo extends BHParcelable {
    public String OrganizationCode;
    public String ProcessType;     // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
}
