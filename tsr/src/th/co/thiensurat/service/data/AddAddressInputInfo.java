package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.AddressInfo;

public class AddAddressInputInfo extends BHParcelable {
	public String AddressID;
	public String RefNo;
	public String AddressTypeCode;
	public String AddressDetail;
	public String AddressDetail2;
	public String AddressDetail3;
	public String AddressDetail4;
	public String ProvinceCode;
	public String DistrictCode;
	public String SubDistrictCode;
	public String Zipcode;
	public double Latitude;
	public double Longitude;
	public String AddressInputMethod;
	public String TelHome;
	public String TelMobile;
	public String TelOffice;
	public String EMail;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;
	
	public static  AddAddressInputInfo from(AddressInfo address) {
		AddAddressInputInfo info = new AddAddressInputInfo();
		info.AddressID = address.AddressID;
		info.RefNo = address.RefNo;
		info.AddressTypeCode = address.AddressTypeCode;
		info.AddressDetail = address.AddressDetail;
		info.AddressDetail2 = address.AddressDetail2;
		info.AddressDetail3 = address.AddressDetail3;
		info.AddressDetail4 = address.AddressDetail4;
		info.ProvinceCode = address.ProvinceCode;
		info.DistrictCode = address.DistrictCode;
		info.SubDistrictCode = address.SubDistrictCode;
		info.Zipcode = address.Zipcode;
		info.Latitude = address.Latitude;
		info.Longitude = address.Longitude;
		info.AddressInputMethod = address.AddressInputMethod;
		info.TelHome = address.TelHome;
		info.TelMobile = address.TelMobile;
		info.TelOffice = address.TelOffice;
		info.EMail = address.EMail;
		info.CreateDate = address.CreateDate;
		info.CreateBy = address.CreateBy;
		info.LastUpdateDate = address.LastUpdateDate;
		info.LastUpdateBy = address.LastUpdateBy;
		info.SyncedDate = address.SyncedDate;
		return info;
	}
}
