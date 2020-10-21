package th.co.bighead.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.thiensurat.data.controller.UserController;
import th.co.thiensurat.data.info.DeviceMenuInfo;
import th.co.thiensurat.data.info.UserInfo;
import th.co.thiensurat.service.data.GetCurrentFortnightOutputInfo;

public class BHPreference {

    private static String getTsrUrl(){
        String url = "";
        switch (BHGeneral.SERVICE_MODE){
            case DEV:
                url = "http://192.168.1.243/dev";
                break;
            case DEV_Migrate:
                url = "http://192.168.1.243/migrate";
                break;
            case UAT:
                //url = "http://203.150.54.171/mca";
                url = "http://appuat.thiensurat.co.th/mca";
                break;
            case UAT_Training:
                url = "http://203.150.54.171/mca_training";
                break;
            case UAT_Migrate:
                url = "http://203.150.54.171/mca_migrate";
                break;
            case PRODUCTION:
                //url = "http://203.150.54.248/mca";
                url = "http://bof.thiensurat.co.th/mca";
                break;
            case UAT_Demo:
                url = "http://203.150.54.171/reconciledemo";
                break;
            case CR_T:
                url = "http://203.150.54.248/mca_migrate";
                break;
            case DEV_MCA:
                url = "http://192.168.1.171/mca";
                break;
        }
        return url;
    }

    public static final String OVER_DUE_KEY = "21";

    public static final String TSR_URL = getTsrUrl();

	// DEV
    //public static final String TSR_URL = "http://192.168.1.243/dev";
//    public static final String TSR_URL = "http://192.168.1.138/tsr.web";

    // UAT-OLD
//    public static final String TSR_URL = "http://uat.tsr.bighead.co.th";

    // UAT-CLOUD
//    public static final String TSR_URL = "http://203.150.54.171/mca_training";        // App For Training
//    public static final String TSR_URL = "http://203.150.54.171/mca";                 // App ปกติ
//    public static final String TSR_URL = "http://203.150.54.171/mca_migrate";         // App ข้อมูล Migration
//    public static final String TSR_URL = "http://203.150.54.171/TestMigrate";

    // PRODUCTION-CLOUD
//    public static final String TSR_URL = "http://203.150.54.248/mca/";                 // App ข้อมูลปกติ + ข้อมูล Migration


    public static final String TSR_SERVICE_URL = String.format("%s/TSR.asmx", TSR_URL);
    public static final String TSR_REPORT_MOBILE = String.format("%s/Reports/Mobile/ReportMobile.aspx", TSR_URL);

    /*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/

    //public static final String TSR_IMAGE_URL = String.format("%s/Images/contractImage", TSR_URL);
    public static final String TSR_IMAGE_URL = String.format("%s/ContractImage?ImageID=", TSR_SERVICE_URL);

    /*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/

    public static final String TSR_DB_URL = String.format("%s/Database", TSR_URL);

    //public static final String TSR_SERVICE_URL = "http://192.168.1.243/dev/TSR.asmx";
    //public static final String TSR_REPORT_MOBILE = "http://192.168.1.243/dev/Reports/Mobile/ReportMobile.aspx";
    //public static final String TSR_SERVICE_URL = "http://192.168.1.133/TSR.Web/TSR.asmx";


	// SIT
//	public static final String TSR_SERVICE_URL = "http://sit.tsr.bighead.co.th/TSR.asmx";

    // UAT
	/*public static final String TSR_SERVICE_URL = "http://uat.tsr.bighead.co.th/TSR.asmx";
    public static final String TSR_REPORT_MOBILE = "http://uat.tsr.bighead.co.th/Reports/Mobile/ReportMobile.aspx";*/
//	public static final String TSR_SERVICE_URL = "http://203.144.154.17/TSR.asmx";

    // Production
//	public static final String TSR_SERVICE_URL = "http://110.170.185.76/bighead/TSR.asmx";

    public static final String TSR_SERVICE_ACCESS_KEY = "tsr@#123";

    public static final String GCM_SENDER_ID = "231483108984";

    private static final String PREFERENCE_NAME = "th.co.thiensurat.tsr.preferences";
    private static final String ORGANIZATION_CODE_KEY = "ORGANIZATION_CODE";
    private static final String SERVICE_MODE_KEY = "SERVICE_MODE";
    private static final String USER_ID_KEY = "USER_ID";
    private static final String EMPLOYEE_ID_KEY = "EMPLOYEE_ID";
    private static final String USER_FULL_NAME_KEY = "USER_FULL_NAME";

    private static final String DEPARTMENT_CODE_KEY = "DEPARTMENT_CODE";
    private static final String SUB_DEPARTMENT_CODE_KEY = "SUB_DEPARTMENT_CODE";
    private static final String SUPERVISOR_CODE_KEY = "SUPERVISOR_CODE";
    private static final String TEAM_CODE_KEY = "TEAM_CODE";
    private static final String SUBTEAM_CODE_KEY = "SUBTEAM_CODE";

    private static final String CASH_CODE_KEY = "CASH_CODE";
    private static final String SALE_CODE_KEY = "SALE_CODE";

    private static final String SOURCE_SYSTEM_CODE_KEY = "EMPLOYEE_SOURCE_SYSTEM_CODE";
    private static final String SOURCE_SYSTEM_NAME_KEY = "EMPLOYEE_SOURCE_SYSTEM_NAME";

    // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
    private static final String EMP_PROCESSTYPE_KEY = "EMPLOYEE_PROCESS_TYPE";

    // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
    private static final String CURRENT_TREEHISTORYID_KEY = "CURRENT_TREEHISTORYID";

    private static final String POSITION_CODE_KEY = "POSITION_CODE";
    private static final String POSITION_NAME_KEY = "POSITION_NAME";

    private static final String LAST_LOGIN_ID_KEY = "LASTLOGIN_ID";

    private static final String REFNO_KEY = "REFNO";

    private static final String ASSIGNEE_EMPID_KEY = "ASSIGNEE_EMPID";
    private static final String APPOINTMENT_DATE_KEY = "APPOINTMENT_DATE";

    private static final String DATA_STORAGE_KEY = "DATA_STORAGE";

    private static final String FORTNIGHT_PER_YEAR_KEY = "FORTNIGHT_PER_YEAR";
    private static final String FORTNIGHT_YEAR_KEY = "FORTNIGHT_YEAR";
    private static final String FORTNIGHT_NUMBER_KEY = "FORTNIGHT_NUMBER";
    private static final String PROCESS_TYPE_KEY = "PROCESS_TYPE";

    private static final String PRODUCT_SERIAL_NUMBER_KEY = "PRODUCT_SERIAL_NUMBER";
    private static final String PRODUCT_SERIAL_NUMBER_KEY2 = "PRODUCT_SERIAL_NUMBER2";

    private static final String CUSTOMER_ID_KEY = "CUSTOMER_TD";

    private static final String USER_DEVICE_ID = "USER_DEVICE_ID";
    private static final String USER_DEVICE_ID_VERSION = "USER_DEVICE_ID_VERSION";
    private static final String USER_DEVICE_MENUS = "USER_DEVICE_MENUS";
    private static final String USER_PRINTER_ADDRESS = "USER_PRINTER_ADDRESS";

    private static final String LOG_LOGIN = "LOG_LOGIN";
    private static final String USER_NOT_ALLOW_LOGIN = "USER_NOT_ALLOW_LOGIN";

    private static final String IS_ADMIN = "IS_ADMIN";
    private static final String Time_Out_Login = "Time_Out_Login";

    private static final String SUSPEND_SERVICE_NOTICE = "Suspend Service Notice";
    private static final String NOTICE_TIME = "Notice_Time";

    /*** [START] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ ***/
    private static final String DATE_FORMAT_GENERATE_DOCUMENT_KEY = "DATE_FORMAT_GENERATE_DOCUMENT";
    private static final String RUNNING_NUMBER_RECEIPT_KEY = "RUNNING_NUMBER_RECEIPT";
    private static final String RUNNING_NUMBER_CHANGECONTRACT_KEY = "RUNNING_NUMBER_CHANGECONTRACT";
    private static final String RUNNING_NUMBER_RETURNPRODUCT_KEY = "RUNNING_NUMBER_RETURNPRODUCT";
    private static final String RUNNING_NUMBER_IMPOUNDPRODUCT_KEY = "RUNNING_NUMBER_IMPOUNDPRODUCT";
    private static final String RUNNING_NUMBER_CHANGEPRODUCT_KEY = "RUNNING_NUMBER_CHANGEPRODUCT";
    private static final String RUNNING_NUMBER_COMPLAIN_KEY = "RUNNING_NUMBER_COMPLAIN";
    /*** [END] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ   ***/

    /*** [START] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ ***/
    private static final String SEND_MONEY_YEAR_FORMAT_GENERATE_KEY = "SEND_MONEY_YEAR_FORMAT_GENERATE";
    private static final String SEND_MONEY_REFERENCE1_FORMAT_GENERATE_KEY = "SEND_MONEY_REFERENCE1_FORMAT_GENERATE";
    private static final String RUNNING_NUMBER_REFERENCE1_KEY = "RUNNING_NUMBER_REFERENCE1";
    private static final String RUNNING_NUMBER_REFERENCE2_KEY = "RUNNING_NUMBER_REFERENCE2";
    /*** [END] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ   ***/

    private static final String CURRENT_SERVER_DATE_KEY = "CURRENT_SERVER_DATE";

    private static SharedPreferences pref = BHApplication.getContext().getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
    private static Editor editor = pref.edit();

    /** ServiceMode**/
    public static void setServiceMode(String serveiceMode) {
        synchronized (editor) {
            editor.putString(SERVICE_MODE_KEY, serveiceMode).commit();
        }
    }
    public static String serviceMode() {
        return pref.getString(SERVICE_MODE_KEY, null);
    }

    /** UserID **/
    public static void setUserID(String userID) {
        synchronized (editor) {
            editor.putString(USER_ID_KEY, userID).commit();
        }
    }
    public static String userID() {
        return pref.getString(USER_ID_KEY, null);
    }

    /** UserFullName **/
    public static void setUserFullName(String userFullName) {
        synchronized (editor) {
            editor.putString(USER_FULL_NAME_KEY, userFullName).commit();
        }
    }
    public static String userFullName() {
        return pref.getString(USER_FULL_NAME_KEY, null);
    }

    /** OrganizationCode **/
    public static void setOrganizationCode(String organizationCode) {
        synchronized (editor) {
            editor.putString(ORGANIZATION_CODE_KEY, organizationCode);
            editor.commit();
        }
    }
    public static String organizationCode() {
        return pref.getString(ORGANIZATION_CODE_KEY, null);
    }

    /** EmployeeID **/
    public static void setEmployeeID(String employeeID) {
        synchronized (editor) {
            editor.putString(EMPLOYEE_ID_KEY, employeeID);
            editor.commit();
        }
    }
    public static String employeeID() {
        return pref.getString(EMPLOYEE_ID_KEY, null);
    }


    /*** [START] :: [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย ***/
    /** ProcessType **/
    public static void setProcessTypeOfEmployee(String processType) {
        synchronized (editor) {
            editor.putString(EMP_PROCESSTYPE_KEY, processType);
            editor.commit();
        }
    }
    public static String processTypeOfEmployee() {
        return pref.getString(EMP_PROCESSTYPE_KEY, null);
    }
    /*** [END] :: [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย ***/

    /** SourceSystem **/
    public static void setSourceSystem(String sourceSystem) {
        synchronized (editor) {
            editor.putString(SOURCE_SYSTEM_CODE_KEY, sourceSystem);
            editor.commit();
        }
    }
    public static String sourceSystem() {
        return pref.getString(SOURCE_SYSTEM_CODE_KEY, null);
    }

    /** SourceSystemName **/
    public static void setSourceSystemName(String sourceSystemName) {
        synchronized (editor) {
            editor.putString(SOURCE_SYSTEM_NAME_KEY, sourceSystemName);
            editor.commit();
        }
    }
    public static String sourceSystemName() {
        return pref.getString(SOURCE_SYSTEM_NAME_KEY, null);
    }

    /** CashCode **/
    public static void setCashCode(String cashCode) {
        synchronized (editor) {
            editor.putString(CASH_CODE_KEY, cashCode);
            editor.commit();
        }
    }
    public static String cashCode() {
        return pref.getString(CASH_CODE_KEY, null);
    }

    /** SaleCode **/
    public static void setSaleCode(String saleTeamCode) {
        synchronized (editor) {
            editor.putString(SALE_CODE_KEY, saleTeamCode);
            editor.commit();
        }
    }
	public static String saleCode() {
		return pref.getString(SALE_CODE_KEY, null);
	}

    /** Current-TreeHistoryID **/
    public static void setCurrentTreeHistoryID(String treeHistoryID) {
        synchronized (editor) {
            editor.putString(CURRENT_TREEHISTORYID_KEY, treeHistoryID);
            editor.commit();
        }
    }
    public static String currentTreeHistoryID() { return pref.getString(CURRENT_TREEHISTORYID_KEY, null);}


    /** AssigneeEmpID **/
	public static void setAssigneeEmpID(String assigneeEmpID) {
        synchronized (editor) {
            editor.putString(ASSIGNEE_EMPID_KEY, assigneeEmpID);
            editor.commit();
        }
    }
    public static String AssigneeEmpID() {
        return pref.getString(ASSIGNEE_EMPID_KEY, null);
    }

    /** AppointmentDate **/
    public static void setAppointmentDate(String appointmentDate) {
        synchronized (editor) {
            editor.putString(APPOINTMENT_DATE_KEY, appointmentDate);
            editor.commit();
        }
    }
    public static String appointmentDate() {
        return pref.getString(APPOINTMENT_DATE_KEY, null);
    }

    /** RefNo **/
    public static void setRefNo(String refNO) {
        synchronized (editor) {
            editor.putString(REFNO_KEY, refNO);
            editor.commit();
        }
    }
    public static String RefNo() {
        return pref.getString(REFNO_KEY, null);
    }

    /** LastloginID **/
    public static void setLastloginID(String EmployeeID) {
        synchronized (editor) {
            editor.putString(LAST_LOGIN_ID_KEY, EmployeeID);
            editor.commit();
        }
    }
    public static String LastLoginId() {
        return pref.getString(LAST_LOGIN_ID_KEY, null);
    }

    /** PrinterAddress **/
    public static void setPrinterAddress(String printerAddress) {
        synchronized (editor) {
            editor.putString(USER_PRINTER_ADDRESS, printerAddress);
            editor.commit();
        }
    }
    public static String printerAddress() {
        return pref.getString(USER_PRINTER_ADDRESS, null);
    }

    public static String selectTeamCodeOrSubTeamCode() {
        return SubTeamCode() == null ? teamCode() + "00" : SubTeamCode();
    }

    /** DepartmentCode **/
    public static void setDepartmentCode(String departmentCode) {
        synchronized (editor) {
            editor.putString(DEPARTMENT_CODE_KEY, departmentCode);
            editor.commit();
        }
    }
    public static String departmentCode() {
        return pref.getString(DEPARTMENT_CODE_KEY, null);
    }

    /** SubDepartmentCode **/
    public static void setSubDepartmentCode(String subDepartmentCode) {
        synchronized (editor) {
            editor.putString(SUB_DEPARTMENT_CODE_KEY, subDepartmentCode);
            editor.commit();
        }
    }
    public static String subDepartmentCode() {
        return pref.getString(SUB_DEPARTMENT_CODE_KEY, null);
    }

    /** SupervisorCode **/
    public static void setSupervisorCode(String supervisorCode) {
        synchronized (editor) {
            editor.putString(SUPERVISOR_CODE_KEY, supervisorCode);
            editor.commit();
        }
    }
    public static String supervisorCode() {
        return pref.getString(SUPERVISOR_CODE_KEY, null);
    }

    /** TeamCode **/
    public static void setTeamCode(String teamCode) {
        synchronized (editor) {
            editor.putString(TEAM_CODE_KEY, teamCode);
            editor.commit();
        }
    }
    public static String teamCode() {
        return pref.getString(TEAM_CODE_KEY, null);
    }

    /** SubTeamCode **/
    public static void setSubTeamCode(String subTeamCode) {
        synchronized (editor) {
            editor.putString(SUBTEAM_CODE_KEY, subTeamCode);
            editor.commit();
        }
    }
    public static String SubTeamCode() {
    	return pref.getString(SUBTEAM_CODE_KEY, null);
    	/*
    	String tmpSubTeam = pref.getString(SUBTEAM_CODE_KEY, null);

    	if (tmpSubTeam != null)
    	{
    		if (tmpSubTeam.equals(teamCode() + "00"))
        		return null;
        	else
        		return tmpSubTeam;
    	}
    	else {
    		return tmpSubTeam;
    	}
    	*/
    }

    public static String dataStorage() {
        try {
            return pref.getString(DATA_STORAGE_KEY, null);
        }catch (Exception e){
            return null;
        }
    }

    public static void setDataStorage(String device) {
        synchronized (editor) {
            editor.putString(DATA_STORAGE_KEY, device);
            editor.commit();
        }
    }

    public static String fortnightPerYear() {
        return pref.getString(FORTNIGHT_PER_YEAR_KEY, null);
    }

    public static void setFortnightYear(String fortnightPerYear) {
        synchronized (editor) {
            editor.putString(FORTNIGHT_PER_YEAR_KEY, fortnightPerYear);
            editor.commit();
        }
    }

    public static int fortnightYear() {
        return pref.getInt(FORTNIGHT_YEAR_KEY, 0);
    }

    public static void setFortnightYear(int fortnightYear) {
        synchronized (editor) {
            editor.putInt(FORTNIGHT_YEAR_KEY, fortnightYear);
            editor.commit();
        }
    }

    public static int fortnightNumber() {
        return pref.getInt(FORTNIGHT_NUMBER_KEY, 0);
    }

    public static void setFortnightNumber(int fortnightNumber) {
        synchronized (editor) {
            editor.putInt(FORTNIGHT_NUMBER_KEY, fortnightNumber);
            editor.commit();
        }
    }

    public static String ProcessType() {
        return pref.getString(PROCESS_TYPE_KEY, null);
    }

    public static void setProcessType(String processType) {
        synchronized (editor) {
            editor.putString(PROCESS_TYPE_KEY, processType);
            editor.commit();
        }
    }

    public static void setProductSerialNumber(String activeProductSerialNumber) {
        synchronized (editor) {
            editor.putString(PRODUCT_SERIAL_NUMBER_KEY, activeProductSerialNumber);

            editor.commit();
        }
    }

    public static String ProductSerialNumber() {
        return pref.getString(PRODUCT_SERIAL_NUMBER_KEY, null);

    }



   public static void setProductSerialNumber2(String activeProductSerialNumber) {
        synchronized (editor) {
            editor.putString(PRODUCT_SERIAL_NUMBER_KEY2, activeProductSerialNumber);
            editor.commit();
        }
    }

    public static String ProductSerialNumber2() {
        return pref.getString(PRODUCT_SERIAL_NUMBER_KEY2, null);
    }




    public static void setCustomerID(String customerID) {
        synchronized (editor) {
            editor.putString(CUSTOMER_ID_KEY, customerID);
            editor.commit();
        }
    }

    public static String CustomerID() {
        return pref.getString(CUSTOMER_ID_KEY, null);
    }

    public static void setPositionCode(String positionCode) {
        synchronized (editor) {
            editor.putString(POSITION_CODE_KEY, positionCode);
            editor.commit();
        }
    }

    public static String PositionCode() {
        return pref.getString(POSITION_CODE_KEY, null);
    }

    public static void setPositionName(String positionName) {
        synchronized (editor) {
            editor.putString(POSITION_NAME_KEY, positionName);
            editor.commit();
        }
    }

    public static String PositionName() {
        return pref.getString(POSITION_NAME_KEY, null);
    }

    public static String userDeviceId() {
        /*** [START] :: Fixed - [LINE@08/09/2016][Android] เกิดปัญหาข้อมูลที่ลูกค้า Test ถูกบันทึกลงตาราง TransactionLogSkip เยอะมาก ***/
        /*int version = pref.getInt(USER_DEVICE_ID_VERSION, Integer.MIN_VALUE);
        if (version == appVersion()) {
            return pref.getString(USER_DEVICE_ID, "");
        }

        return "";*/
        return pref.getString(USER_DEVICE_ID, "");
        /*** [END] :: Fixed - [LINE@08/09/2016][Android] เกิดปัญหาข้อมูลที่ลูกค้า Test ถูกบันทึกลงตาราง TransactionLogSkip เยอะมาก  ***/
    }

    public static void setUserDeviceID(String userDeviceID) {
        synchronized (editor) {
            editor.putString(USER_DEVICE_ID, userDeviceID);
            editor.putInt(USER_DEVICE_ID_VERSION, appVersionCode());
            editor.commit();
        }
    }

    public static int appVersionCode() {
        try {
            Context context = BHApplication.getContext();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String appVersionName() {
        try {
            Context context = BHApplication.getContext();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String appPackageName() {
        try {
            Context context = BHApplication.getContext();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.packageName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static int androidApiLevel() {
        try {
            return Build.VERSION.SDK_INT;
        } catch (Exception e) {
            return 0;
        }
    }

    public static void setUserMenus(List<DeviceMenuInfo> menus)
    {
        synchronized (editor) {
            editor.putString(USER_DEVICE_MENUS, new Gson().toJson(DeviceMenuInfo.prepare(menus)));
            editor.commit();
        }
    }

    public static List<DeviceMenuInfo> getUserMenus()
    {
        try {
            return new Gson().fromJson(pref.getString(USER_DEVICE_MENUS, null), new TypeToken<ArrayList<DeviceMenuInfo>>(){}.getType());
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public static void setLogLogin(String LogLogin) {
        synchronized (editor) {
            editor.putString(LOG_LOGIN, LogLogin);
            editor.commit();
        }
    }

    public static String LogLogin() {
        String LogLogin = pref.getString(LOG_LOGIN, null);
        return LogLogin;
    }


    public static  void setLogNoticeTime(String LogNoticeTime)
    {
        synchronized (editor) {
            editor.putString(NOTICE_TIME,LogNoticeTime);
            editor.commit();
        }
    }

    public static  String getLogNoticeTime(){
        return pref.getString(NOTICE_TIME, null);
    }

    /** Not allow user inactive and force login = false **/
    public static void setUserNotAllowLogin(String UserNotAllowLogin) {
        synchronized (editor) {
            editor.putString(USER_NOT_ALLOW_LOGIN, UserNotAllowLogin).commit();
        }
    }

    public static String getUserNotAllowLogin() {
        return pref.getString(USER_NOT_ALLOW_LOGIN, UserController.LoginType.ALLOW.toString());
    }


    public static void setIsAdmin(boolean isAdmin) {
        synchronized (editor) {
            editor.putBoolean(IS_ADMIN, isAdmin);
            editor.commit();
        }
    }

    public static boolean IsAdmin() {
        return pref.getBoolean(IS_ADMIN, false);
    }

    public static void setTimeOutLogin(boolean timeOutLogin) {
        synchronized (editor) {
            editor.putBoolean(Time_Out_Login, timeOutLogin);
            editor.commit();
        }
    }

    public static boolean TimeOutLogin() {
        return pref.getBoolean(Time_Out_Login, false);
    }


    public static void setSuspendServiceNotice (boolean input) {
        synchronized (editor) {
            editor.putBoolean(SUSPEND_SERVICE_NOTICE, input);
            editor.commit();
        }
    }

    public static  boolean isSuspendServiceNotice(){
        return pref.getBoolean(SUSPEND_SERVICE_NOTICE,false);
    }

    /*** [START] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ ***/
    public static void setDateFormatGenerateDocument(String dateFormatGenerateDocument) {
        synchronized (editor) {
            editor.putString(DATE_FORMAT_GENERATE_DOCUMENT_KEY, dateFormatGenerateDocument);
            editor.commit();
        }
    }

    public static String getDateFormatGenerateDocument() {
        return pref.getString(DATE_FORMAT_GENERATE_DOCUMENT_KEY, "");
    }

    /** Receipt : ใบเสร็จ **/
    public static void setRunningNumberReceipt(int runningNumberReceipt) {
        synchronized (editor) {
            editor.putInt(RUNNING_NUMBER_RECEIPT_KEY, runningNumberReceipt);
            editor.commit();
        }
    }

    public static int getRunningNumberReceipt() {
        return pref.getInt(RUNNING_NUMBER_RECEIPT_KEY, 0);
    }

    /** ChangeContract : เปลี่ยนสัญญา **/
    public static void setRunningNumberChangeContract(int runningNumberChangeContract) {
        synchronized (editor) {
            editor.putInt(RUNNING_NUMBER_CHANGECONTRACT_KEY, runningNumberChangeContract);
            editor.commit();
        }
    }

    public static int getRunningNumberChangeContract() {
        return pref.getInt(RUNNING_NUMBER_CHANGECONTRACT_KEY, 0);
    }

    /** ReturnProduct : ส่งคืนสินค้าเข้าระบบ **/
    public static void setRunningNumberReturnProduct(int runningNumberReturnProduct) {
        synchronized (editor) {
            editor.putInt(RUNNING_NUMBER_RETURNPRODUCT_KEY, runningNumberReturnProduct);
            editor.commit();
        }
    }

    public static int getRunningNumberReturnProduct() {
        return pref.getInt(RUNNING_NUMBER_RETURNPRODUCT_KEY, 0);
    }

    /** ImpoundProduct : ถอดเครื่อง **/
    public static void setRunningNumberImpoundProduct(int runningNumberImpoundProduct) {
        synchronized (editor) {
            editor.putInt(RUNNING_NUMBER_IMPOUNDPRODUCT_KEY, runningNumberImpoundProduct);
            editor.commit();
        }
    }

    public static int getRunningNumberImpoundProduct() {
        return pref.getInt(RUNNING_NUMBER_IMPOUNDPRODUCT_KEY, 0);
    }

    /** ChangeProduct : เปลี่ยนเครื่อง **/
    public static void setRunningNumberChangeProduct(int runningNumberChangeProduct) {
        synchronized (editor) {
            editor.putInt(RUNNING_NUMBER_CHANGEPRODUCT_KEY, runningNumberChangeProduct);
            editor.commit();
        }
    }

    public static int getRunningNumberChangeProduct() {
        return pref.getInt(RUNNING_NUMBER_CHANGEPRODUCT_KEY, 0);
    }

    /** Complain : แจ้งปัญหา **/
    public static void setRunningNumberComplain(int runningNumberComplain) {
        synchronized (editor) {
            editor.putInt(RUNNING_NUMBER_COMPLAIN_KEY, runningNumberComplain);
            editor.commit();
        }
    }

    public static int getRunningNumberComplain() {
        return pref.getInt(RUNNING_NUMBER_COMPLAIN_KEY, 0);
    }



    /*** [START] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ ***/
    public static void setSendMoneyYearFormatGenerate(String sendMoneyYearFormatGenerate) {
        synchronized (editor) {
            editor.putString(SEND_MONEY_YEAR_FORMAT_GENERATE_KEY, sendMoneyYearFormatGenerate);
            editor.commit();
        }
    }
    public static String getSendMoneyYearFormatGenerate() {
        return pref.getString(SEND_MONEY_YEAR_FORMAT_GENERATE_KEY, "");
    }

    public static void setSendMoneyReference1FormatGenerate(String sendMoneyReference1FormatGenerate) {
        synchronized (editor) {
            editor.putString(SEND_MONEY_REFERENCE1_FORMAT_GENERATE_KEY, sendMoneyReference1FormatGenerate);
            editor.commit();
        }
    }
    public static String getSendMoneyReference1FormatGenerate() {
        return pref.getString(SEND_MONEY_REFERENCE1_FORMAT_GENERATE_KEY, "");
    }


    public static void setRunningNumberReference1(int runningNumberReference1) {
        synchronized (editor) {
            editor.putInt(RUNNING_NUMBER_REFERENCE1_KEY, runningNumberReference1);
            editor.commit();
        }
    }
    public static int getRunningNumberReference1() {
        return pref.getInt(RUNNING_NUMBER_REFERENCE1_KEY, 0);
    }

    public static void setRunningNumberReference2(int runningNumberReference2) {
        synchronized (editor) {
            editor.putInt(RUNNING_NUMBER_REFERENCE2_KEY, runningNumberReference2);
            editor.commit();
        }
    }
    public static int getRunningNumberReference2() {
        return pref.getInt(RUNNING_NUMBER_REFERENCE2_KEY, 0);
    }
    /*** [END] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ   ***/

    /*** [START] :: Fixed - [BHPROJ-1036-9259] - พบการเปลี่ยนวันที่บนmobileทำให้สามารถ​แก้ไขสัญญาได้ ***/
    public static void setCurrentServerDate(Date currentServerDate) {
        synchronized (editor) {
            if (currentServerDate != null) {
                long longDateTime = currentServerDate.getTime();
                editor.putLong(CURRENT_SERVER_DATE_KEY, longDateTime);
                editor.commit();
            }
        }
    }
    public static Date getCurrentServerDate() {

        long longDateTime = pref.getLong(CURRENT_SERVER_DATE_KEY, 0);
        return new Date(longDateTime);
    }
    /*** [END] :: Fixed - [BHPROJ-1036-9259] - พบการเปลี่ยนวันที่บนmobileทำให้สามารถ​แก้ไขสัญญาได้ ***/


    // YIM initAdminPreference
    public static void initAdminPreference(DeviceMenuInfo deviceMenuInfo){

        BHPreference.setIsAdmin(true);
        BHPreference.setTimeOutLogin(false);
        BHPreference.setFortnightYear(null);
        BHPreference.setFortnightNumber(-1);

        List<DeviceMenuInfo> menuInfoList = new ArrayList<DeviceMenuInfo>();
        menuInfoList.add(deviceMenuInfo);
        BHPreference.setUserMenus(menuInfoList);

        BHPreference.setServiceMode(BHGeneral.SERVICE_MODE.toString());
        BHPreference.setUserNotAllowLogin(UserController.LoginType.ALLOW.toString());
        BHPreference.setUserID(null);
        BHPreference.setUserFullName(BHGeneral.USER_ADMIN);
        BHPreference.setOrganizationCode(null);
        BHPreference.setTeamCode(null);
        BHPreference.setSubTeamCode(null);
        BHPreference.setEmployeeID(BHGeneral.USER_ADMIN);
        BHPreference.setDepartmentCode(null);
        BHPreference.setSubDepartmentCode(null);
        BHPreference.setSupervisorCode(null);

        BHPreference.setSourceSystem(null);
        BHPreference.setSourceSystemName(null);
        BHPreference.setProcessTypeOfEmployee(null);         // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
        BHPreference.setPositionCode(null);
        BHPreference.setPositionName(BHGeneral.USER_ADMIN);
        BHPreference.setSaleCode(null);
        BHPreference.setCashCode(null);

        /*** [START] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ ***/
        BHPreference.setDateFormatGenerateDocument(null);
        BHPreference.setRunningNumberReceipt(0);
        BHPreference.setRunningNumberChangeContract(0);
        BHPreference.setRunningNumberReturnProduct(0);
        BHPreference.setRunningNumberImpoundProduct(0);
        BHPreference.setRunningNumberChangeProduct(0);
        BHPreference.setRunningNumberComplain(0);
        /*** [END] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ   ***/


        /*** [START] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ ***/
        BHPreference.setSendMoneyYearFormatGenerate(null);
        BHPreference.setSendMoneyReference1FormatGenerate(null);
        BHPreference.setRunningNumberReference1(0);
        BHPreference.setRunningNumberReference2(0);
        /*** [END] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ   ***/


        /*** [START] :: Fixed - [BHPROJ-1036-9259] - พบการเปลี่ยนวันที่บนmobileทำให้สามารถ​แก้ไขสัญญาได้ ***/
        BHPreference.setCurrentServerDate(new Date());
        /*** [END] :: Fixed - [BHPROJ-1036-9259] - พบการเปลี่ยนวันที่บนmobileทำให้สามารถ​แก้ไขสัญญาได้ ***/
    }

    // YIM initAdminPreference
    public static void initPreference(UserInfo info, GetCurrentFortnightOutputInfo fortnightOutput){
        if ((fortnightOutput != null) && (fortnightOutput.ResultCode == 0)) {   // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย
            BHPreference.setFortnightYear(fortnightOutput.Info.Year);
            BHPreference.setFortnightNumber(fortnightOutput.Info.FortnightNumber);
        }

        BHPreference.setIsAdmin(IsAdmin());
        BHPreference.setTimeOutLogin(false);
        BHPreference.setServiceMode(BHGeneral.SERVICE_MODE.toString());
        BHPreference.setUserNotAllowLogin(UserController.LoginType.ALLOW.toString());
        BHPreference.setUserID(info.UserName);
        BHPreference.setUserFullName(info.UserFullName);
        BHPreference.setOrganizationCode(info.OrganizationCode);
        BHPreference.setTeamCode(info.TeamCode);
        BHPreference.setSubTeamCode(info.SubTeamCode);
        BHPreference.setEmployeeID(info.EmpID);
        BHPreference.setDepartmentCode(info.DepartmentCode);
        BHPreference.setSubDepartmentCode(info.SubDepartmentCode);
        BHPreference.setSupervisorCode(info.SupervisorCode);

        String strSourceSystem = info.SourceSystem;
        BHPreference.setSourceSystem(strSourceSystem);
        BHPreference.setSourceSystemName(info.SourceSystemName);
        BHPreference.setProcessTypeOfEmployee(info.ProcessType);         // [BHPROJ-0016-3225] :: [Android+Web-Admin] แก้ไข Code เรื่องการเพิ่ม Field เพื่อระบุ Department สำหรับ ตารางเก็บปักษ์การขาย

        List<UserInfo> userPositionList = info.UserPosition;
        if (userPositionList != null) {
            String strPositionCode = "";
            String strPositionName = "";
            String strSaleCode = "";
            String strCashCode = "";

            for (UserInfo userInfo : userPositionList) {
                strPositionCode += userInfo.PositionCode;
                strPositionCode += ",";

                strPositionName += userInfo.PositionName;
                strPositionName += ",";

                if (userInfo.SaleCode != null) {
                    strSaleCode = userInfo.SaleCode;
                }
                if ((userInfo.SaleCode != null) && (strSourceSystem.equals("Credit"))) {
                    strCashCode = userInfo.SaleCode;
                }
            }
            strPositionCode = strPositionCode.substring(0, strPositionCode.length() - 1);
            BHPreference.setPositionCode(strPositionCode);

            strPositionName = strPositionName.substring(0, strPositionName.length() - 1);
            BHPreference.setPositionName(strPositionName);

            BHPreference.setSaleCode(strSaleCode);
            BHPreference.setCashCode(strCashCode);
        }

        /*** [START] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ ***/
        BHPreference.setDateFormatGenerateDocument(info.DateFormatGenerateDocument);
        BHPreference.setRunningNumberReceipt(info.RunningNumberReceipt);
        BHPreference.setRunningNumberChangeContract(info.RunningNumberChangeContract);
        BHPreference.setRunningNumberReturnProduct(info.RunningNumberReturnProduct);
        BHPreference.setRunningNumberImpoundProduct(info.RunningNumberImpoundProduct);
        BHPreference.setRunningNumberChangeProduct(info.RunningNumberChangeProduct);
        BHPreference.setRunningNumberComplain(info.RunningNumberComplain);
        /*** [END] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ   ***/


        /*** [START] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ ***/
        BHPreference.setSendMoneyYearFormatGenerate(info.SendMoneyYearFormatGenerate);
        BHPreference.setSendMoneyReference1FormatGenerate(info.SendMoneyReference1FormatGenerate);
        BHPreference.setRunningNumberReference1(info.RunningNumberReference1);
        BHPreference.setRunningNumberReference2(info.RunningNumberReference2);
        /*** [END] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ   ***/


        /*** [START] :: Fixed - [BHPROJ-1036-9259] - พบการเปลี่ยนวันที่บนmobileทำให้สามารถ​แก้ไขสัญญาได้ ***/
        BHPreference.setCurrentServerDate(info.CurrentServerDate);
        /*** [END] :: Fixed - [BHPROJ-1036-9259] - พบการเปลี่ยนวันที่บนmobileทำให้สามารถ​แก้ไขสัญญาได้ ***/
    }

    // YIM resource for department signature image
    public final static String departmentSignaturePath = String.format("%s/%s/", BHStorage.getFolder(BHStorage.FolderType.Picture), "signature");
    public final static String departmentSignatureName = "signature.png";



    public static boolean hasDepartmentSignatureImage(){
        Bitmap signatureImage = BitmapFactory.decodeFile(BHPreference.departmentSignaturePath + BHPreference.departmentSignatureName);
        return (signatureImage != null);
    }

    public static boolean IsSaleForCRD() {
        //ทำงานเหมือนพนักงานเก็บ แต่สามารถขายได้ด้วย การขายจะต้องเห็นเฉพาะของตัวเอง
        String processTypeOfEmployee = BHPreference.processTypeOfEmployee();

        if (processTypeOfEmployee != null) {
            if (processTypeOfEmployee.equals("CRD")) {
                return  true;
            }
        }
        return false;
    }


    public static boolean IsSaleForTS() {
        //ทำงานเหมือนพนักงานเก็บ แต่สามารถขายได้ด้วย การขายจะต้องเห็นเฉพาะของตัวเอง
        String processTypeOfEmployee = BHPreference.processTypeOfEmployee();

        if (processTypeOfEmployee != null) {
            if (processTypeOfEmployee.equals("TS")) {
                return  true;
            }
        }
        return false;
    }

}
