package th.co.thiensurat.service.data;

import th.co.bighead.utilities.BHParcelable;


/***
 * Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical
 ***/


public class AddUserDeviceLogInputInfo extends BHParcelable {
    //public String UserID;
    public String UserName;
    public String DeviceID;
    //public Date CreateDate;
    //public String CreateBy;
    public String ProcessType;     // ประเภทของรายการ (รายการที่ Login จากระบบ TSR Mobile App แบบปกติ = ANDROID_LOGIN, รายการที่ Logout จากระบบ TSR Mobile App แบบปกติ = ANDROID_LOGOUT, รายการที่ Logout จากการยิง GCM = GCM_LOGOUT)
    public int VersionCode;
    public int AndroidApiLevel;

    public enum UserDeviceLogProcessType {
        ANDROID_LOGIN,          //-- ประเภทของรายการ (รายการที่ Login จากระบบ TSR Mobile App แบบปกติ
        ANDROID_LOGOUT,         //-- รายการที่ Logout จากระบบ TSR Mobile App แบบปกต
        ANDROID_LOGOUT_TIMEOUTLOGIN,         //-- รายการที่ Logout จากระบบ TSR Mobile App แบบป Time Out Login
        GCM_LOGOUT,             //-- รายการที่ Logout จากการยิง GCM
        WEBADMIN_FORCELOGOUT    //-- รายการที่ FroceLogout จากระบบ Web-Admin
    }
}
