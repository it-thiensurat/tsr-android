package th.co.thiensurat.data.info;

import java.util.Date;

public class ProductStockHistoryInfo {
    public String ProductStockHisID;
    public String OrganizationCode;
    public String ProductSerialNumber;
    public String ProductID;
    public String Type;
    public String TeamCode;
    public String Status;
    public Date ImportDate;
    public String NewTeamCode;
    public String NewStatus;
    public String Result;
    public Date SyncedDate;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;

    /*** [START] :: Fixed-[BHPROJ-1036-7663] ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย - แก้ไขให้ CRD สามารถยืมเครื่องได้ ***/
    public String OldOwnerEmpID;   // พนักงานเจ้าของสินค้า (คนเก่าก่อนการยืม)
    public String NewOwnerEmpID;   // พนักงานที่ขอยืมสินค้ามาขาย
    public boolean IsMoveByEmp;       // บอกว่าเป็นการโอนย้าย/โอนยิมสินค้ารายบคุลล (สำหรับฝ่าย Sale จะเป็นการโอนย้ายรายทีม แต่ CRD เป็นการโอนย้ายรายบุคคล)
    /*** [END] :: Fixed-[BHPROJ-1036-7663] ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย - แก้ไขให้ CRD สามารถยืมเครื่องได้ ***/
}

