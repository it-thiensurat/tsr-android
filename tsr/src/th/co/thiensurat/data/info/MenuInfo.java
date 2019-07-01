package th.co.thiensurat.data.info;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHApplication;
import android.content.Context;
import android.content.res.TypedArray;

public class MenuInfo {
	public int titleID;
	public int iconID;
	public MenuInfo[] subMenus;
	public boolean canCollapse;
	public boolean isCollapse;
	public int[] data;
	
	public MenuInfo() {
	}
	
	public MenuInfo(int titleResID, int iconResID) {
		this(titleResID, iconResID, null, true, false);
	}
	
	public MenuInfo(int titleResID, int iconResID, MenuInfo[] subMenus) {
		this(titleResID, iconResID, subMenus, true, false);
	}
	
	public MenuInfo(int titleResID, int iconResID, MenuInfo[] subMenus, boolean canCollapse) {
		this(titleResID, iconResID, subMenus, canCollapse, false);
	}
	
	public MenuInfo(int titleResID, int iconResID, MenuInfo[] subMenus, boolean canCollapse, boolean isCollapse) {
		this.titleID = titleResID;
		this.iconID = iconResID;
		this.subMenus = subMenus;
		this.canCollapse = canCollapse;
		this.isCollapse = isCollapse;
	}

	public static MenuInfo[] from(int titleResID) {
		return from(titleResID, -1, null);
	}

	public static MenuInfo[] from(int titleResID, int iconResID) {
		return from(titleResID, iconResID, null);
	}

	public static MenuInfo[] from(int titleResID, int iconResID, int... datas) {
		Context context = BHApplication.getContext();
		TypedArray ar = context.getResources().obtainTypedArray(titleResID);
		int len = ar.length();
		int[] titleIDs = new int[len];
		for (int ii = 0; ii < len; ii++) {
			titleIDs[ii] = ar.getResourceId(ii, 0);
		}
		ar.recycle();

		int[] iconIDs = null;
		if (iconResID > -1) {
			ar = context.getResources().obtainTypedArray(iconResID);
			len = ar.length();
			iconIDs = new int[len];
			for (int ii = 0; ii < len; ii++) {
				iconIDs[ii] = ar.getResourceId(ii, 0);
			}
			ar.recycle();
		}

		List<int[]> extras = new ArrayList<int[]>();
		if (datas != null) {
			for (int ii = 0; ii < datas.length; ii++) {
				ar = context.getResources().obtainTypedArray(datas[ii]);
				len = ar.length();
				int[] extra = new int[len];
				for (int jj = 0; jj < len; jj++) {
					extra[jj] = ar.getResourceId(jj, 0);
				}

				ar.recycle();
				extras.add(extra);
			}
		}

		MenuInfo[] result = new MenuInfo[titleIDs.length];
		for (int ii = 0; ii < titleIDs.length; ii++) {
			MenuInfo info = new MenuInfo();
			info.titleID = titleIDs[ii];

			if (iconIDs != null && ii < iconIDs.length) {
				info.iconID = iconIDs[ii];
			} else {
				info.iconID = -1;
			}

			if (datas != null) {
				int size = extras.size();
				info.data = new int[size];
				for (int jj = 0; jj < size; jj++) {
					int[] extra = extras.get(jj);
					if (ii < extra.length) {
						info.data[jj] = extra[ii];
					} else {
						info.data[jj] = -1;
					}
				}
			}

			result[ii] = info;
		}

		return result;
	}
}
