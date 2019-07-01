package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;

public class AddressController extends BaseController {

	private static final String QUERY_ADDRESS_GETADDRESS_BY_REFNO_BY_ADDRESS_TYPE_CODE = "SELECT        Addr.AddressID, Addr.RefNo, Addr.AddressTypeCode, Addr.AddressDetail, Addr.AddressDetail2, Addr.AddressDetail3, Addr.AddressDetail4,"
			+ "                         Addr.ProvinceCode, Addr.DistrictCode, Addr.SubDistrictCode, Addr.Zipcode, Addr.Latitude, Addr.Longitude, Addr.AddressInputMethod, Addr.TelHome,"
			+ "                         Addr.TelMobile, Addr.TelOffice, Addr.EMail, AddrT.AddressTypeName, P.ProvinceName, D.DistrictName, SD.SubDistrictName"
			+ " FROM            Address AS Addr INNER JOIN"
			+ "                         AddressType AS AddrT ON Addr.AddressTypeCode = AddrT.AddressTypeCode INNER JOIN"
			+ "                         Province AS P ON Addr.ProvinceCode = P.ProvinceCode INNER JOIN"
			+ "                         District AS D ON Addr.DistrictCode = D.DistrictCode INNER JOIN"
			+ "                         SubDistrict AS SD ON Addr.SubDistrictCode = SD.SubDistrictCode"
			+ " WHERE (Addr.RefNo = ?) AND (Addr.AddressTypeCode = ?)";

	public AddressInfo getAddressByRefNoByAddressTypeCode(String refNO, String addressTypeCode) {
		return executeQueryObject(QUERY_ADDRESS_GETADDRESS_BY_REFNO_BY_ADDRESS_TYPE_CODE, new String[] { refNO, addressTypeCode }, AddressInfo.class);
	}
	
	public List<AddressInfo> getAddressByRefNo(String refNo) {
		String sql = "select *from Address where RefNo=?";
		return executeQueryList(sql, new String[]{refNo}, AddressInfo.class);
	}

	public List<AddressInfo> getAddress() {
		return executeQueryList("select *from address", null, AddressInfo.class);
	}
	
	public List<AddressInfo> getAddress(String refNo) {
		final String QUERY_ADDRESS_GETADDRESS_BY_REFNO = "SELECT Address.*, Province.ProvinceName, District.DistrictName, SubDistrict.SubDistrictName, AddressType.AddressTypeName "
				+ "FROM Address INNER JOIN Province ON Address.ProvinceCode = Province.ProvinceCode "
				+ "INNER JOIN AddressType ON Address.AddressTypeCode = AddressType.AddressTypeCode "
				+ "INNER JOIN District ON Address.DistrictCode = District.DistrictCode "
				+ "INNER JOIN SubDistrict ON Address.SubDistrictCode = SubDistrict.SubDistrictCode " + "WHERE Address.RefNo = ?";
		return executeQueryList(QUERY_ADDRESS_GETADDRESS_BY_REFNO, new String[] { refNo }, AddressInfo.class);
	}

	public AddressInfo getAddress(String refNo, String type) {
        AddressType addressType = Enum.valueOf(AddressType.class, type);
        return getAddress(refNo, addressType);
	}

	public AddressInfo getAddress(String refNo, AddressType type) {
		List<AddressInfo> addresses = getAddress(refNo);
		return getAddress(addresses, type);
	}

	public AddressInfo getAddress(List<AddressInfo> address, AddressType type) {
		if (address != null) {
			String addressType = type.name();
			AddressInfo result = null;
			for (AddressInfo info : address) {
				if (info.AddressTypeCode.equals(addressType)) {
					result = info;
					break;
				}
			}

			return result;
		}

		return null;
	}

	public void addAddress(AddressInfo info) {
		String sql = "INSERT INTO Address (AddressID, RefNo, AddressTypeCode, AddressDetail, AddressDetail2, AddressDetail3, AddressDetail4, ProvinceCode, DistrictCode, SubDistrictCode, Zipcode, Latitude, Longitude, AddressInputMethod, TelHome, TelMobile, TelOffice, EMail)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		executeNonQuery(sql, new String[] { info.AddressID, info.RefNo, info.AddressTypeCode, info.AddressDetail, info.AddressDetail2, info.AddressDetail3,
				info.AddressDetail4, info.ProvinceCode, info.DistrictCode, info.SubDistrictCode, info.Zipcode, valueOf(info.Latitude), valueOf(info.Longitude),
				info.AddressInputMethod, info.TelHome, info.TelMobile, info.TelOffice, info.EMail });

	}

	public void updateAddress(AddressInfo info) {
		String sql = "update Address" + " set AddressDetail=?, AddressDetail2=?, AddressDetail3=?, AddressDetail4=?,"
				+ " ProvinceCode=?, DistrictCode=?, SubDistrictCode=?, Zipcode=?, Latitude=?, Longitude=?,"
				+ " AddressInputMethod=?, TelHome=?, TelMobile=?, TelOffice=?, EMail=?" + " where AddressID=?";
		executeNonQuery(sql, new String[] { info.AddressDetail, info.AddressDetail2, info.AddressDetail3, info.AddressDetail4, info.ProvinceCode,
				info.DistrictCode, info.SubDistrictCode, info.Zipcode, valueOf(info.Latitude), valueOf(info.Longitude), info.AddressInputMethod, info.TelHome,
				info.TelMobile, info.TelOffice, info.EMail, info.AddressID });
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////
	public void deleteAddressAll() {
		String sql = "DELETE FROM Address";
		executeNonQuery(sql, null);
	}
	public void deleteAddressByRefNo(String refNo) {
		String sql = "DELETE FROM Address WHERE (RefNo = ?)";
		executeNonQuery(sql, new String[]{refNo});
	}
	///////////////////////////////////////////////////////////////////////////////////////////////////////


}
