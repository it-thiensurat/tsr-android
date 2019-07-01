package th.co.bighead.utilities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BHValidator {
	private static String CITIZEN_ID_EXPRESSION = "^[0-8]\\d{12}$";
	private static String Email_EXPRESSION = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";

	public static boolean isValidCitizenID(String citizenID) {
		boolean isValid = Pattern.matches(CITIZEN_ID_EXPRESSION, citizenID);

		if (isValid) {
			int len = citizenID.length();
			int checksum = 0;
			int position = len;
			for (int ii = 0; ii < len - 1; ii++) {
				checksum += Integer.parseInt(String.valueOf(citizenID.charAt(ii))) * position--;
			}

			int lastDigit = Integer.parseInt(String.valueOf(citizenID.charAt(len - 1)));
			checksum = (11 - (checksum % 11)) % 10;
			isValid = lastDigit == checksum;
		}

		return isValid;
	}

	public static boolean isEmailValid(String email) {
		boolean isValid = false;
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(Email_EXPRESSION, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (!matcher.matches()) {
			return isValid;
		}
		isValid = true;
		return isValid;
	}
}
