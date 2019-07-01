package th.co.thiensurat.data.info;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import th.co.bighead.utilities.BHParcelable;

/**
 * Created by Annop on 2/3/2558.
 */
public class DeviceMenuInfo extends BHParcelable {
    public String MenuID;
    public String ParentID;
    public String MenuName;
    public String MenuControl;
    public int MenuIndex;
    public String CreatedBy;
    public Date CreatedDate;

    public List<DeviceMenuInfo> SubMenus;
    public boolean IsExpanded;

    private static DeviceMenuInfo getParent(List<DeviceMenuInfo> menus, String parentID) {
        for (DeviceMenuInfo m : menus) {
            if (parentID.equals(m.MenuID)) {
                return m;
            }
        }

        return null;
    }

    public static List<DeviceMenuInfo> prepare(List<DeviceMenuInfo> menus) {
        Iterator<DeviceMenuInfo> it = menus.iterator();
        while (it.hasNext()) {
            DeviceMenuInfo m = it.next();
            if (!TextUtils.isEmpty(m.ParentID)) {
                DeviceMenuInfo parent = getParent(menus, m.ParentID);
                if (parent != null) {
                    if (parent.SubMenus == null) {
                        parent.SubMenus = new ArrayList<DeviceMenuInfo>();
                    }

                    parent.SubMenus.add(m);
                    it.remove();
                }
            }
        }

        return menus;
    }
}
