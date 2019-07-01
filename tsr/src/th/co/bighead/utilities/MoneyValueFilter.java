package th.co.bighead.utilities;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.Log;

public class MoneyValueFilter extends DigitsKeyListener {
    public MoneyValueFilter() {
        super(true, true);
    }

    private int digits = 2;

    public void setDigits(int d) {
        digits = d;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        CharSequence out = super.filter(source, start, end, dest, dstart, dend);

        // if changed, replace the source
        if (out != null) {
            source = out;
            start = 0;
            end = out.length();
        }

        int len = end - start;

        // if deleting, source is empty
        // and deleting can't break anything
        if (len == 0) {
            return source;
        }

        int dlen = dest.length();

        // Find the position of the decimal .
        for (int i = 0; i < dstart; i++) {
            if (dest.charAt(i) == '.') {
                // being here means, that a number has
                // been inserted after the dot
                // check if the amount of digits is right
                return (dlen - (i + 1) + len > digits) ?
                        "" :
                        new SpannableStringBuilder(source, start, end);
            }
        }

        for (int i = start; i < end; ++i) {
            if (source.charAt(i) == '.') {
                // being here means, dot has been inserted
                // check if the amount of digits is right
                if ((dlen - dend) + (end - (i + 1)) > digits)
                    return "";
                else
                    break;  // return new SpannableStringBuilder(source, start, end);
            }
        }

        // if the dot is after the inserted part,
        // nothing can break
        if (dlen == 0) {
            String[] strAs = source.toString().split("\\.");
            if (strAs.length > 0) {
                String formatA = BHUtilities.DEFAULT_DOUBLE_FORMAT;
                if (strAs.length == 1 || (strAs.length > 1 && (strAs[1].toString().length() < 2))) {
                    if (strAs.length == 1) {
                        if (!source.toString().contains(".")) {
                            formatA = BHUtilities.DEFAULT_INTEGER_FORMAT;
                        } else {
                            formatA = null;
                        }
                    } else {
                        formatA = BHUtilities.DEFAULT_FLOAT_FORMAT;
                    }
                }
                if (formatA != null) {
                    source = BHUtilities.parseNumericFormat(source, formatA);
                    end = source.length();
                }
            }
            return new SpannableStringBuilder(source, start, end);
        } else {
            String[] strAs = source.toString().split("\\.");
            if (strAs.length > 0) {
                String formatA = BHUtilities.DEFAULT_DOUBLE_FORMAT;
                if (strAs.length == 1 || (strAs.length > 1 && (strAs[1].toString().length() < 2))) {
                    if (strAs.length == 1) {
                        if (!source.toString().contains(".")) {
                            formatA = BHUtilities.DEFAULT_INTEGER_FORMAT;
                        } else {
                            formatA = null;
                        }
                    } else {
                        formatA = BHUtilities.DEFAULT_FLOAT_FORMAT;
                    }
                }
                if (formatA != null) {
                    CharSequence A = BHUtilities.parseNumericFormat(source.toString().replace(",", ""), formatA);
                    CharSequence B = BHUtilities.parseNumericFormat(dest.toString().replace(",", ""), formatA);
                    if (A.toString().equals(B.toString())) {
                        source = BHUtilities.parseNumericFormat(source, formatA);
                        end = source.length();
                    } else {
                        String limit = new StringBuilder(dest).insert(dstart, source).toString().replace(",", "");
                        String[] limits = limit.split("\\.");
                        if (limits != null && limits.length > 0) {
                            if (limits[0].length() > 7) {
//                                Log.e("MoneyValueFilter", "Before edit dot :: " + limits[0]);
                                return "";
                            }
                        }
                    }
                }
            }
            return new SpannableStringBuilder(source, start, end);
        }
    }
}

