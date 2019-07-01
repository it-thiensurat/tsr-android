package th.co.thiensurat.data.info;

import th.co.bighead.utilities.BHApplication;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;

public class ArrayInfo {
	public int id;
	public String text;

	public static ArrayInfo[] from(int arrayResID) {
		Context context = BHApplication.getContext();
		Resources res = context.getResources();
		TypedArray ar = res.obtainTypedArray(arrayResID);
		int len = ar.length();
		ArrayInfo[] result = new ArrayInfo[len];
		for (int ii = 0; ii < len; ii++) {
			int id = ar.getResourceId(ii, 0);

			ArrayInfo info = new ArrayInfo();
			info.id = id;
			info.text = res.getString(id);
			result[ii] = info;
		}

		ar.recycle();

		return result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return text;
	}
}
