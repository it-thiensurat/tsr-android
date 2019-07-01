package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

public class UserDeviceInfo extends BHParcelable {
    public String Username;
    public String DeviceID;
    public String LastLoginDeviceID;
    public Date CurrentServerDate; //Fixed - [BHPROJ-1036-9259] - พบการเปลี่ยนวันที่บนmobileทำให้สามารถ​แก้ไขสัญญาได้
}