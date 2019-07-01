package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHUtilities;

public class DebtorCustomerInfo extends BHParcelable {

	public String CustomerID;
	public String OrganizationCode;
	public String PrefixCode;
	public String PrefixName;
	public String CustomerName;
	public String CustomerType;
	public String IDCardType;
	public String IDCard;
	public String CompanyName;
	public String AuthorizedName;
	public String AuthorizedIDCard;
	public Date Brithday;
	public String Sex;
	public int DebtStatus;		// 1 = ลูกหนี้ (ยังชำระเงินไม่เสร็จสิ้น) / 2 = ลูกค้า (ไม่มีหนี้คงค้างกับบริษัท)
	public String HabitatTypeCode;
	public String HabitatDetail;
	public String OccupyType;
	public String CareerCode;
	public String CareerDetail;
	public String HobbyCode;
	public String HobbyDetail;
	public boolean IsUsedProduct;
	public String UsedProductModelID;
	public String SuggestionCode;
	public String SuggestionDetail;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;

	public String ProductSerialNumber;
	public Date EFFDATE;
	public String STATUS;

	public String CustomerTypeName;
	public String IDCardTypeName;
    public String RefNo;

	/*** [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/
	public String ReferencePersonName;
	public String ReferencePersonTelephone;
	/*** [END] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/

	//ForSale
	public int logYear;
	public int logMonth;
	public int logDay;


	// Extend Fields
	public String CustomerFullName() {
		if (CustomerType.equals("0") || CustomerType.equals("2")) {
			return BHUtilities.trim(PrefixName) + BHUtilities.trim(CustomerName);
		} else {
			return BHUtilities.trim(PrefixName) + " " + BHUtilities.trim(CompanyName);
		}
	}

}
