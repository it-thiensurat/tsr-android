package th.co.thiensurat.data.info;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;

/**
 * Created by bighead on 14/08/2018.
 */
public class PlatformVersionInfo  extends BHParcelable {
    
    public String Platform;
    public String VersionName;
    public String VersionCode;
    public boolean IsValidate;
    public String ButtonNameTemplate;
    public String TitleTemplate;
    public String MessageTemplate;
    public String LinkName;
    public Date CreateDate;
    public String CreateBy;
    public Date LastUpdateDate;
    public String LastUpdateBy;

    public String ButtonName;
    public String Title;
    public String Message;
    public boolean IsUpdate;

}
