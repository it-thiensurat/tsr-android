package th.co.bighead.utilities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.zxing.common.StringUtils;
import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import th.co.thiensurat.R;
import th.co.thiensurat.data.controller.DatabaseManager;

import static th.co.bighead.utilities.BHFragment.showMessage;

public class BHUtilities {
    public static final Locale LOCALE_THAI = new Locale("th");
    public static final Locale LOCALE_EN = new Locale("en_US");
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String DEFAULT_DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DEFAULT_INTEGER_FORMAT = "#,##0";
    public static final String DEFAULT_DOUBLE_FORMAT = "#,##0.00";
    public static final String DEFAULT_FLOAT_FORMAT = "#,##0.0";
    public static final Calendar CALENDAR_THAI = Calendar.getInstance(LOCALE_THAI);
    public static final String TAG = "BHUtilities";

    public static String trim(String text) {
        return trim(text, "");
    }

    public static String trim(String text, String replaceText) {
        if (text == null || text.isEmpty()) {
            return replaceText;
        }

        String result = text.trim();
        if (result.equals("")) {
            return replaceText;
        }

        return result;
    }

    public static String dateFormat(Date date) {
        return dateFormat(date, LOCALE_THAI);
    }

    public static String dateFormat(Date date, String format) {
        return dateFormat(date, format, LOCALE_THAI);
    }

    public static String dateFormat(Date date, Locale locale) {
        return dateFormat(date, DEFAULT_DATE_FORMAT, locale);
    }

    public static String dateTimeFormat(Date date) {
        return dateFormat(date, DEFAULT_DATE_TIME_FORMAT);
    }

    public static String dateTimeFormat(Date date, Locale locale) {
        return dateFormat(date, DEFAULT_DATE_TIME_FORMAT, locale);
    }

    public static Date parseDate(Date date, String format, Locale locale) {
        if (date == null) {
            return new Date();
        }
        try {
            if (locale.equals(LOCALE_THAI)) {
                BuddhistDateFormat formatter = new BuddhistDateFormat(format);
                return formatter.parse(formatter.format(date));
            }
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.parse(formatter.format(date));
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String dateFormat(Date date, String format, Locale locale) {
        if (date == null) {
            return "";
        }

        if (locale.equals(LOCALE_THAI)) {
            /*** [START] :: Fixed - [BHPROJ-1036-8614] - ปัญหาใบส่งเงินเป็น เดือน ต.ค. แทนที่จะเป็น ตัวเลข 10 ***/

            /*BuddhistDateFormat formatter = new BuddhistDateFormat(format);
            return formatter.format(date);*/

            BuddhistDateFormat formatter = new BuddhistDateFormat(format);
            String strFormatter = formatter.format(date);


            //ตรวจสอบอีกครั้งว่าได้ค่าถูกต้องหรือไม่
            if (format.matches( "(.*)[^M]M{2}[^M](.*)")) {

                BuddhistDateFormat newFormatter = new BuddhistDateFormat("MM");
                String newStrFormatter = newFormatter.format(date);

                try {
                    Integer.parseInt(newStrFormatter);
                } catch(NumberFormatException nfe) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(date);

                    int month = c.get(Calendar.MONTH) + 1;
                    String strMpnth = String.format("%02d", month);

                    strFormatter = strFormatter.replace(newStrFormatter, strMpnth);
                }

                return strFormatter;
            }
            /*** [END] :: Fixed - [BHPROJ-1036-8614] - ปัญหาใบส่งเงินเป็น เดือน ต.ค. แทนที่จะเป็น ตัวเลข 10  ***/
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String numericFormat(long value) {
        return numericFormat(value, DEFAULT_INTEGER_FORMAT);
    }

    public static String numericFormat(long value, String format) {
        DecimalFormat formatter = new DecimalFormat(format);
        return formatter.format(value);
    }

    public static CharSequence parseNumericFormat(CharSequence value, String format) {
        if (!value.toString().equals("")) {
            DecimalFormat formatter = new DecimalFormat(format);
            try {
                return formatter.format(formatter.parse(value.toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return "";
        }
        return "";
    }

    public static String numericFormat(double value) {
        return numericFormat(value, DEFAULT_DOUBLE_FORMAT);
    }

    public static String numericFormat(double value, String format) {
        DecimalFormat formatter = new DecimalFormat(format);
        return formatter.format(value);
    }

    public static Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }

    public static Date addMonth(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, i);
        return cal.getTime();
    }

    public static Date addYear(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.YEAR, i);
        return cal.getTime();
    }

    public static String getMonthTH(String m) {
        String ret = "";

        switch (m) {
            case "01":
                ret = "มกราคม";
                break;
            case "02":
                ret = "กุมภาพันธ์";
                break;
            case "03":
                ret = "มีนาคม";
                break;
            case "04":
                ret = "เมษายน";
                break;
            case "05":
                ret = "พฤษภาคม";
                break;
            case "06":
                ret = "มิถุนายน";
                break;
            case "07":
                ret = "กรกฎาคม";
                break;
            case "08":
                ret = "สิงหาคม";
                break;
            case "09":
                ret = "กันยายน";
                break;
            case "10":
                ret = "ตุลาคม";
                break;
            case "11":
                ret = "พฤศจิกายน";
                break;
            case "12":
                ret = "ธันวาคม";
                break;

            default:
                break;
        }
        return ret;
    }

    public static String makePlaceholders(String args) {
        String[] splits = args.split(",");
        if (splits.length < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(splits.length * 2 - 1);
            sb.append("?");
            for (int i = 1; i < splits.length; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    public static String makePlaceholders(String[] args) {
        String format = "";
        for(int i = 0; i < args.length ; i++){
            format += ("?" + (i == (args.length - 1) ? "" : ", "));
        }
        return format;
    }

    public static String[] makeStringArray(String[] args) {
        List<String> list = new ArrayList<>();
        for (String arg : args) {
            String[] splits = arg.split(",");
            for (String s : splits) {
                list.add(s);
            }
        }
        String[] value = new String[list.size()];
        value = list.toArray(value);
        return value;
    }

    public static AlertDialog.Builder alertDialog(Activity activity, String title, String msg) {
        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
    }

    public static AlertDialog.Builder alertDialog(Activity activity, String title, String msg, String msgPositiveButton) {
        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setNegativeButton(msgPositiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
    }

    public static AlertDialog.Builder builderDialog(Activity activity, String title, String msg) {
        return new AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false);
    }

    public static String ThaiBaht(String strNumber) {
        //ตัดสิ่งที่ไม่ต้องการออก
        for (int i = 0; i < strNumber.length(); i++) {
            strNumber = strNumber.replace(",", ""); //ไม่ต้องการเครื่องหมายคอมมาร์
            strNumber = strNumber.replace(" ", ""); //ไม่ต้องการช่องว่าง
            strNumber = strNumber.replace("บาท", ""); //ไม่ต้องการตัวหนังสือ บาท
            strNumber = strNumber.replace("฿", ""); //ไม่ต้องการสัญลักษณ์สกุลเงินบาท
        }
        //สร้างอะเรย์เก็บค่าที่ต้องการใช้เอาไว้
        String TxtNumArr[] = {"ศูยน์", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า", "สิบ"};
        String TxtDigitArr[] = {"", "สิบ", "ร้อย", "พัน", "หมื่น", "แสน", "ล้าน"};
        String BahtText = "";
        //ตรวจสอบดูซะหน่อยว่าใช่ตัวเลขที่ถูกต้องหรือเปล่า ด้วย isNaN == true ถ้าเป็นข้อความ == false ถ้าเป็นตัวเลข

        if (Float.isNaN(Float.parseFloat(strNumber.toString()))) {
            showMessage("ข้อมูลนำเข้าไม่ถูกต้อง");
            //return "ข้อมูลนำเข้าไม่ถูกต้อง";
        } else {
            //ตรวสอบอีกสักครั้งว่าตัวเลขมากเกินความต้องการหรือเปล่า
            if ((Float.parseFloat(strNumber.toString()) - 0) > 9999999.9999) {
                showMessage("ข้อมูลนำเข้าเกินขอบเขตที่ตั้งไว้");
                //return "ข้อมูลนำเข้าเกินขอบเขตที่ตั้งไว้";
            } else {
                //พรากทศนิยม กับจำนวนเต็มออกจากกัน
                String[] splitNumber = strNumber.split("\\.");
                String integerNumber = splitNumber[0];
                String decimalNumber = splitNumber[1];

                //ขั้นตอนการประมวลผล
                int integerNumberSize = integerNumber.length();
                for (int i = 0; i < integerNumberSize; i++) {
                    String tmp = integerNumber.substring(i, i + 1);
                    if (!tmp.equals("0")) {
                        if ((i == (integerNumberSize - 1)) && (tmp.equals("1")) && (i == 0 ? false : (!(integerNumber.substring(i - 1, i)).equals("0")))) {
                            BahtText += "เอ็ด";
                        } else if ((i == (integerNumberSize - 2)) && (tmp.equals("2"))) {
                            BahtText += "ยี่";
                        } else if ((tmp.equals("0")) || (i == (integerNumberSize - 2)) && (tmp.equals("1"))) {
                            BahtText += "";
                        } else {
                            BahtText += TxtNumArr[Integer.parseInt(tmp.toString())];
                        }
                        BahtText += TxtDigitArr[integerNumberSize - i - 1];
                    } else if (i == 0 && tmp.equals("0")) {
                        BahtText += TxtNumArr[Integer.parseInt(tmp.toString())];
                    }
                }
                BahtText += "บาท";

                if ((decimalNumber.equals("0")) || (decimalNumber.equals("00"))) {
                    BahtText += "ถ้วน";
                } else {
                    BahtText += "\n";
                    int decimalNumberSize = decimalNumber.length();
                    for (int i = 0; i < decimalNumberSize; i++) {
                        String tmp = decimalNumber.substring(i, i + 1);
                        if (!tmp.equals("0")) {
                            if ((i == (decimalNumberSize - 1)) && (tmp.equals("1")) && (!(decimalNumber.substring(i - 1, i)).equals("0"))) {
                                BahtText += "เอ็ด";
                            } else if ((i == (decimalNumberSize - 2)) && (tmp.equals("2"))) {
                                BahtText += "ยี่";
                            } else if ((tmp.equals("0")) || (i == (decimalNumberSize - 2)) && (tmp.equals("1"))) {
                                BahtText += "";
                            } else {
                                BahtText += TxtNumArr[Integer.parseInt(tmp.toString())];
                            }
                            BahtText += TxtDigitArr[decimalNumberSize - i - 1];
                        }
                    }
                    BahtText += "สตางค์";
                }
            }
        }
        return BahtText;
    }

    public static void saveDatabase() {
        /**Create Zip*/
        File newFile = new File(DatabaseManager.getInstance().getDatabasePath() + ".zip");
        try {
            // is = file zip :: extracting
            InputStream is = new FileInputStream(newFile);
            ZipInputStream zis = getFileFromZip(is);
            if (zis == null) {
                throw new SQLiteAssetHelper.SQLiteAssetException("Archive is missing a SQLite database file");
            }

            File oldFileDB = new File(DatabaseManager.getInstance().getDatabasePath());
            if (oldFileDB.exists()) {
                oldFileDB.delete();
            }

            writeExtractedFileToDisk(zis, new FileOutputStream(DatabaseManager.getInstance().getDatabasePath()));

            Log.w(TAG, "database copy complete");
        } catch (IOException e) {
            SQLiteAssetHelper.SQLiteAssetException se = new SQLiteAssetHelper.SQLiteAssetException("Unable to write " + DatabaseManager.getInstance().getDatabasePath() + " to data directory");
            se.setStackTrace(e.getStackTrace());
            throw se;
        }
    }

    public static ZipInputStream getFileFromZip(InputStream zipFileStream) throws IOException {
        ZipInputStream zis = new ZipInputStream(zipFileStream);
        ZipEntry ze;
        while ((ze = zis.getNextEntry()) != null) {
            Log.w(TAG, "extracting file: '" + ze.getName() + "'...");
            return zis;
        }
        return null;
    }

    public static void writeExtractedFileToDisk(InputStream in, OutputStream outs) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = in.read(buffer)) > 0) {
            outs.write(buffer, 0, length);
        }
        outs.flush();
        outs.close();
        in.close();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView)
    {
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null) return;
        if(listAdapter.getCount() <= 0) return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        int totalHeight = 0;
        View view = null;
        for(int i = 0; i < listAdapter.getCount(); i++)
        {
            view = listAdapter.getView(i, view, listView);
            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static String numericFormat(int value) {
        return numericFormat(value, DEFAULT_INTEGER_FORMAT);
    }

    private static double SPACE_KB = 1024;
    private static double SPACE_MB = 1024 * SPACE_KB;
    private static double SPACE_GB = 1024 * SPACE_MB;
    private static double SPACE_TB = 1024 * SPACE_GB;

    public String Bytes2String(long sizeInBytes) {

        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);

        try {
            if ( sizeInBytes < SPACE_KB ) {
                return nf.format(sizeInBytes) + " Byte(s)";
            } else if ( sizeInBytes < SPACE_MB ) {
                return nf.format(sizeInBytes/SPACE_KB) + " KB";
            } else if ( sizeInBytes < SPACE_GB ) {
                return nf.format(sizeInBytes/SPACE_MB) + " MB";
            } else if ( sizeInBytes < SPACE_TB ) {
                return nf.format(sizeInBytes/SPACE_GB) + " GB";
            } else {
                return nf.format(sizeInBytes/SPACE_TB) + " TB";
            }
        } catch (Exception e) {
            return sizeInBytes + " Byte(s)";
        }
    }

    public static Date StringToDate(String strDate, String dateFormat,Locale locale) {

        try {

            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, locale);
            return sdf.parse(strDate);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
