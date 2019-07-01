package th.co.bighead.utilities;

import java.util.Date;

import th.co.thiensurat.BuildConfig;

public abstract class BHGeneral {

    public enum SERVICE_TYPE{
        DEV, DEV_Migrate, UAT, UAT_Training, UAT_Migrate, PRODUCTION, UAT_Demo, CR_T, DEV_MCA
    }

	public final static boolean DEVELOPER_MODE = false;
    public final static boolean BARCODE_KEY_IN_MODE = true;

    //public final static SERVICE_TYPE SERVICE_MODE = SERVICE_TYPE.UAT;
    public final static SERVICE_TYPE SERVICE_MODE = GetServiceType();//SERVICE_TYPE.UAT;

    private final static SERVICE_TYPE GetServiceType() {
        try {
            return  Enum.valueOf(SERVICE_TYPE.class, BuildConfig.SERVICE_MODE);
            //return SERVICE_TYPE.DEV;
        } catch (Exception e){
            return SERVICE_TYPE.DEV;
        }
    }

    public final static String USER_ADMIN = "ADMIN";
    public final static String FOLDER_ADMIN = "_Admin"; // "EmpID" + "_Admin"

    public final static int TIME_OUT_LOGIN = 16; //กำหนดเวลา Login ในระบบ (ชั่วโมง)

    // SET null for disabled;
    // MONTH = REAL MONTH - 1;
    public final static Date AVAILABLE_DATE = null;//new GregorianCalendar(2015, 0, 1).getTime();

    //YIM switch for use change feature
    /** ระบบลายเซ็นหัวหน้าฝ่าย **/
    public final static boolean isOpenDepartmentSignature = true;
    /** เปลี่ยน Logic ในการ Gen Ref1 ของการส่งเงิน สำนักงานใหญ่ ให้เหมือนกับการส่งเงินที่ ธนาคาร (BHPROJ-0016-7262)**/
    public final static boolean isChangeSolutionGenerateReference = true;

    //ReCheckSqliteData
    public final static String BACKUP_TYPE_FILE = "backup"; //นามสกุลไฟล์
    public final static int BACKUP_MAXIMUM_FILE = 5;//จำนวนไฟล์สูงสุดที่เก็บได้
    public final static String BACKUP_DATE_FORMAT = "yyyy-MM-dd-HH-mm-ss";

    //id card
    //public final static boolean ID_CARD_MODE = false; //เปิด/ปิด เครื่องอ่านบัตรประชาชน
    public final static boolean ID_CARD_MODE = BuildConfig.ID_CARD_MODE; //เปิด/ปิด เครื่องอ่านบัตรประชาชน
}
