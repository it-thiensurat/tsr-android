package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class UpdateUserLoggedInInputInfo extends BHParcelable {
	public String UserName;
	public String Password;
    public String DeviceID;
	public Date LastLoginDate;		// Fixed - [BHPROJ-0020-1026] การบริหารจัดการผู้ใช้งาน วันที่ Login ล่าสุด แสดงไม่ถูกต้อง
}
