package th.co.thiensurat.data.info;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHParcelable;

/**
 * Created by Tanawut on 5/3/2558.
 */
public class DamageInfo extends BHParcelable {
    public enum DamageType {
        DAMAGE,
        TEAM_DESTROY,
        DUMMY
    }

    /*** OLDEST ***/
    public String DamageCode;
    public String DamageName;

    public DamageInfo(String damageCode, String damageName) {
        DamageCode = damageCode;
        DamageName = damageName;
    }
    /*** OLDEST ***/

    public DamageType damageType;
    public String damageName;

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return damageName;
    }

    public String damageCode() {
        return damageType != DamageType.DUMMY ? damageType.name() : null;
    }

    public DamageInfo(DamageType damageType, String damageName) {
        this.damageType = damageType;
        this.damageName = damageName;
    }

    public static List<DamageInfo> gets() {
        return gets(false);
    }

    public static List<DamageInfo> gets(boolean dummy) {
        List<DamageInfo> list = new ArrayList<DamageInfo>();
        if (dummy) {
            list.add(new DamageInfo(DamageType.DUMMY, ""));
        }
        list.add(new DamageInfo(DamageType.DAMAGE, "เครื่องชำรุด"));
        list.add(new DamageInfo(DamageType.TEAM_DESTROY, "ยุบทีมขาย"));

        return list;
    }

}
