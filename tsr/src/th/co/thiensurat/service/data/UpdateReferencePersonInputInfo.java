package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.DebtorCustomerInfo;

public class UpdateReferencePersonInputInfo extends BHParcelable{
	public String CustomerID;
	public String OrganizationCode;
//	public String PrefixCode;
//	public String PrefixName;
//	public String CustomerName;
//	public String CustomerType;
//	public String IDCardType;
//	public String IDCard;
//	public String CompanyName;
//	public String AuthorizedName;
//	public String AuthorizedIDCard;
//	public Date Brithday;
//	public String Sex;
//	public int DebtStatus;
//	public String HabitatTypeCode;
//	public String HabitatDetail;
//	public String OccupyType;
//	public String CareerCode;
//	public String CareerDetail;
//	public String HobbyCode;
//	public String HobbyDetail;
//	public boolean IsUsedProduct;
//	public String UsedProductModelID;
//	public String SuggestionCode;
//	public String SuggestionDetail;
//	public Date CreateDate;
//	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
//	public Date SyncedDate;

	/*** [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/
	public String ReferencePersonName;
	public String ReferencePersonTelephone;
	/*** [END] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/

	public static UpdateReferencePersonInputInfo from(DebtorCustomerInfo dc) {
		UpdateReferencePersonInputInfo info = new UpdateReferencePersonInputInfo();
		info.CustomerID = dc.CustomerID;
		info.OrganizationCode = dc.OrganizationCode;
//		info.PrefixCode = dc.PrefixCode;
//		info.PrefixName = dc.PrefixName;
//		info.CustomerName = dc.CustomerName;
//		info.CustomerType = dc.CustomerType;
//		info.IDCardType = dc.IDCardType;
//		info.IDCard = dc.IDCard;
//		info.CompanyName = dc.CompanyName;
//		info.AuthorizedName = dc.AuthorizedName;
//		info.AuthorizedIDCard = dc.AuthorizedIDCard;
//		info.Brithday = dc.Brithday;
//		info.Sex = dc.Sex;
//		info.DebtStatus = dc.DebtStatus;
//		info.HabitatTypeCode = dc.HabitatTypeCode;
//		info.HabitatDetail = dc.HabitatDetail;
//		info.OccupyType = dc.OccupyType;
//		info.CareerCode = dc.CareerCode;
//		info.CareerDetail = dc.CareerDetail;
//		info.HobbyCode = dc.HobbyCode;
//		info.HobbyDetail = dc.HobbyDetail;
//		info.IsUsedProduct = dc.IsUsedProduct;
//		info.UsedProductModelID = dc.UsedProductModelID;
//		info.SuggestionCode = dc.SuggestionCode;
//		info.SuggestionDetail = dc.SuggestionDetail;
//		info.CreateDate = dc.CreateDate;
//		info.CreateBy = dc.CreateBy;
		info.LastUpdateDate = dc.LastUpdateDate;
		info.LastUpdateBy = dc.LastUpdateBy;
//		info.SyncedDate = dc.SyncedDate;
		/*** [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/
		info.ReferencePersonName = dc.ReferencePersonName;
		info.ReferencePersonTelephone = dc.ReferencePersonTelephone;
		/*** [END] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/
		return info;
	}
}
