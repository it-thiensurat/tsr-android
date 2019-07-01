package th.co.thiensurat.service.data;

import java.util.Date;
import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ProductStockHistoryInfo;

public class AddProductStockHistoryInputInfo extends BHParcelable {

	public String ProductStockHisID;	// GUID
	public String OrganizationCode;
	public String ProductSerialNumber;
	public String ProductID;
	public String Type;					// ชนิดสินค้า (จากระบบเดิม)
	public String TeamCode;				// รหัสทีม/รหัสหน่วยก่อนการโอนย้ายสินค้า
	public String Status;
	public Date ImportDate;				// วันที่ทำการ Import มาจากระบบ Stock ของ TSR
	public String NewTeamCode;			// รหัสทีม/รหัสหน่วยหลังการโอนย้ายสินค้า
	public String NewStatus;			// สถานะของสินค้าหลังการโอนย้ายสินค้า
	public String Result;				// หมายเหตุ
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;				// วันที่ทำการ Synchronize รายการลง Database Server
	public String OldOwnerEmpID;		// พนักงานเจ้าของสินค้า (คนเก่าก่อนการยืม)
	public String NewOwnerEmpID;		// พนักงานที่ขอยืมสินค้ามาขาย
	public boolean IsMoveByEmp;			// บอกว่าเป็นการโอนย้าย/โอนยิมสินค้ารายบคุลล (สำหรับฝ่าย Sale จะเป็นการโอนย้ายรายทีม แต่ CRD เป็นการโอนย้ายรายบุคคล)

	public static AddProductStockHistoryInputInfo from(ProductStockHistoryInfo r) {
		AddProductStockHistoryInputInfo info = new AddProductStockHistoryInputInfo();
		info.ProductStockHisID = r.ProductStockHisID;
		info.OrganizationCode = r.OrganizationCode;
		info.ProductSerialNumber = r.ProductSerialNumber;
		info.ProductID = r.ProductID;
		info.Type = r.Type;
		info.TeamCode = r.TeamCode;
		info.Status = r.Status;
		info.ImportDate = r.ImportDate;
		info.NewTeamCode = r.NewTeamCode;
		info.NewStatus = r.NewStatus;
		info.Result = r.Result;
		info.CreateDate = r.CreateDate;
		info.CreateBy = r.CreateBy;
		info.LastUpdateDate = r.LastUpdateDate;
		info.LastUpdateBy = r.LastUpdateBy;
		info.OldOwnerEmpID = r.OldOwnerEmpID;
		info.NewOwnerEmpID = r.NewOwnerEmpID;
		info.IsMoveByEmp = r.IsMoveByEmp;
		return info;
	}
}
