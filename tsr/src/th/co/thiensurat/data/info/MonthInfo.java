package th.co.thiensurat.data.info;

import java.text.DateFormatSymbols;
import java.util.Locale;

public class MonthInfo {
	public int month;
	public String monthName;

	public MonthInfo() {

	}

	public MonthInfo(int monthcode, String monthname) {
		month = monthcode;
		monthName = monthname;
	}

	public static MonthInfo[] gets() {
		return gets(new Locale("th"), false);
	}

	public static MonthInfo[] gets(boolean dummy) {
		return gets(new Locale("th"), dummy);
	}

	public static MonthInfo[] gets(Locale locale) {
		return gets(locale, false);
	}

	public static MonthInfo[] gets(Locale locale, boolean dummy) {
		String[] months = new DateFormatSymbols(locale).getMonths();
		int extra = dummy ? 1 : 0;
		int len = months.length + extra;
		MonthInfo[] result = new MonthInfo[len];
		if (dummy) {
			result[0] = new MonthInfo();
		}
		
		for (int ii = 0; ii < months.length; ii++) {
			MonthInfo info = new MonthInfo();
			info.month = ii + extra + 1;
			info.monthName = months[ii];
			result[ii + extra] = info;
		}

		return result;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return monthName;
	}
}
