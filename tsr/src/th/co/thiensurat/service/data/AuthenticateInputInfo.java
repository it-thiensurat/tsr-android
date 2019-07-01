package th.co.thiensurat.service.data;

import th.co.bighead.utilities.BHParcelable;

public class AuthenticateInputInfo extends BHParcelable {
	public String UserName;
	public String Password;
    public String DeviceID;
	public int VersionCode;
	public String AndroidDeviceID;
	public String VersionName;
	public int AndroidApiLevel;
}
