package th.co.thiensurat.data.info;

import android.graphics.Bitmap;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class AssignInfo extends BHParcelable {
	public String AssignID;
	public String TaskType;
	public String OrganizationCode;
	public String RefNo;
	public String AssigneeEmpID;
	public String AssigneeTeamCode;
	public int Order;
	public int OrderExpect;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public String ReferenceID;
	public Date SyncedDate;

	// EXTRA
	public String CustomerFullName;
	public int PaymentPeriodNumber;
	public int MinPaymentPeriodNumber;
	public float NetAmount;
	public String CONTNO;
	public float OutStandingPayment; // ค้างชำระเท่าไหร่ของงวดนั้นๆ
	public int HoldSalePaymentPeriod; // ค้างกี่เดือน
	public int CountCreditGroupByPaymentAppointmentDate;
	public Date PaymentAppointmentDate;
	public Date PaymentDueDate;

    //map
    public double Latitude;
    public double Longitude;
    public Bitmap profilePhoto;

	//check customer
	public boolean Selected;

	public String AddressID;
	public String AddressTypeCode;
	public String AddressDetail;
	public String AddressDetail2;
	public String AddressDetail3;
	public String AddressDetail4;
	public String ProvinceCode;
	public String DistrictCode;
	public String SubDistrictCode;
	public String Zipcode;
	public String AddressInputMethod;
	public String TelHome;
	public String TelMobile;
	public String TelOffice;
	public String EMail;
	public String ProvinceName;
	public String DistrictName;
	public String SubDistrictName;

	public int NewOrder;
	public boolean IsDuplicateValues;

	/*** [START] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/
	public int OldPaymentDueDay;
	public int NewPaymentDueDay;
	public String SalePaymentPeriodID;
	/*** [END] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/

	public AddressInfo address;
	public AddressInfo getAddress() {
		if(address == null){
			address = new AddressInfo();
			address.AddressID = AddressID;
			address.AddressTypeCode = AddressTypeCode;
			address.AddressDetail = AddressDetail;
			address.AddressDetail2 = AddressDetail2;
			address.AddressDetail3 = AddressDetail3;
			address.AddressDetail4 = AddressDetail4;
			address.ProvinceCode = ProvinceCode;
			address.DistrictCode = DistrictCode;
			address.SubDistrictCode = SubDistrictCode;
			address.Zipcode = Zipcode;
			address.AddressInputMethod = AddressInputMethod;
			address.TelHome = TelHome;
			address.TelMobile = TelMobile;
			address.TelOffice = TelOffice;
			address.EMail = EMail;
			address.ProvinceName = ProvinceName;
			address.DistrictName = DistrictName;
			address.SubDistrictName = SubDistrictName;
		}
		return address;
	}
}
