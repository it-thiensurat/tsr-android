package th.co.thiensurat.service.data;

/*** Fixed-[BHPROJ-0016-1061] :: [Android-Logout-GCM] กรณีเป็นการ Logout จากการยิง GCM ให้ตรวจสอบก่อนว่า ณ เวลานั้นมีใคร Login ด้วย UserID ของเราด้วย DeviceID อื่นหรือเปล่า ถ้ามีให้ข้ามการทำ TRansactionLogService ของ Android เลย ***/

public class AddTransactionLogSkipOutputInfo extends GenericOutputInfo {

}
