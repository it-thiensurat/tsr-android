package th.co.thiensurat.data.controller;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.SqlFileName;

public class BaseController {

    private static final String dateFormat = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String dateFormatWithoutTime = "yyyy-MM-dd";

    private SQLiteDatabase database = null;

    protected String valueOf(int value) {
        return String.valueOf(value);
    }

    protected String valueOf(long value) {
        return String.valueOf(value);
    }

    protected String valueOf(double value) {
        return String.valueOf(value);
    }

    protected String valueOf(float value) {
        return String.valueOf(value);
    }

    protected String valueOf(boolean value) {
        return value ? String.valueOf(1) : String.valueOf(0);
    }

    protected String valueOf(Date value) {
        if (value == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(value);
    }

    protected String valueOf(Date value, String dateFormat) {
        if (value == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
        return formatter.format(value);
    }

    public void removeDatabase() {
        closeDatabase(true);
        File file = new File(DatabaseManager.getInstance().getDatabasePath());
        if (file.exists()) {
            file.delete();
        }
    }

    public boolean checkExistingDatabase(){
        File file = new File(DatabaseManager.getInstance().getDatabasePath());
        return file.exists();
    }

    private void openDatabase() {
        database = DatabaseManager.getInstance().openDatabase();
    }

    private void closeDatabase() {
        closeDatabase(false);
    }

    private void closeDatabase(boolean force) {
        if(force)
            DatabaseManager.getInstance().forceCloseDatabase();
        else
            DatabaseManager.getInstance().closeDatabase();
    }

    private String getQueryFromAsset(SqlFileName name) {
        String fileName = String.format("sqls/%s.sql", name.name());
        try {
            InputStream is = BHApplication.getContext().getAssets().open(fileName);
            Scanner scanner = new Scanner(is);
            String sql = scanner.useDelimiter("\\A").next();
            scanner.close();

            return sql;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected void executeNonQuery(SqlFileName name, String[] args) {
        String sql = getQueryFromAsset(name);
        executeNonQuery(sql, args);
    }

    protected void executeNonQuery(String sql, String[] args) {
        openDatabase();
        try {
            if (args == null) {
                database.execSQL(sql);
            } else {
                database.execSQL(sql, args);
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            closeDatabase();
        }
    }

    protected <T extends BHParcelable> List<T> executeQueryList(SqlFileName name, String[] args, Class<T> cls) {
        String sql = getQueryFromAsset(name);
        return executeQuery(sql, args, cls, false);
    }

    protected <T extends BHParcelable> T executeQueryObject(SqlFileName name, String[] args, Class<T> cls) {
        String sql = getQueryFromAsset(name);
        List<T> result = executeQuery(sql, args, cls, true);
        if (result != null) {
            return result.get(0);
        }

        return null;
    }
    protected <T extends BHParcelable> List<T> executeQueryList(String sql, String[] args, Class<T> cls) {
        return executeQuery(sql, args, cls, false);
    }

    protected <T extends BHParcelable> T executeQueryObject(String sql, String[] args, Class<T> cls) {
        try {
            List<T> result = executeQuery(sql, args, cls, true);
            if (result != null) {
                return result.get(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private <T extends BHParcelable> List<T> executeQuery(String sql, String[] args, Class<T> cls, boolean onlyFirst) {


        //Query Timer
        /*Log.d("SQLite", " ");
        Log.d("SQLite", " ");
        Log.d("SQLite", " ");
        if (args != null) {
            String newStringSQL = sql;
            for (String s : args) {
                if (sql != null) {
                    newStringSQL = newStringSQL.replaceFirst("\\?", "'" + s + "'");
                }
            }
            //Log.d("SQLite", newStringSQL);
            int maxLogSize = 4000;
            for(int i = 0; i <= newStringSQL.length() / maxLogSize; i++) {
                int start = i * maxLogSize;
                int end = (i+1) * maxLogSize;
                end = end > newStringSQL.length() ? newStringSQL.length() : end;
                Log.d("SQLite", newStringSQL.substring(start, end));
            }
        } else {
            Log.d("SQLite", sql);
        }

        Log.d("SQLite", " ");
        Log.d("SQLite", " ");
        Log.d("SQLite", " ");*/

        //Calendar startTime = Calendar.getInstance();


        openDatabase();
        //database.beginTransaction();
        Cursor cursor = null;
        List<T> result = null;
        try {
            cursor = database.rawQuery(sql, args);
            if (cursor.moveToFirst()) {
                result = new ArrayList<T>();
                while (!cursor.isAfterLast()) {
                    T obj = objectFrom(cursor, cls);
                    result.add(obj);

                    if (onlyFirst) {
                        break;
                    }

                    cursor.moveToNext();
                }
            }
            //database.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            //database.endTransaction();
            if (cursor != null) {
                cursor.close();
            }
            closeDatabase();
        }

        /*//Query Timer
        Calendar endTime = Calendar.getInstance();
        long difference = endTime.getTimeInMillis() - startTime.getTimeInMillis();

        String hms = String.format("%02d:%02d:%02d.%03d", TimeUnit.MILLISECONDS.toHours(difference),
                TimeUnit.MILLISECONDS.toMinutes(difference) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(difference)),
                TimeUnit.MILLISECONDS.toSeconds(difference) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(difference)),
                difference - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(difference)));
        Log.d("SQLite", hms);*/


        return result;
    }

    private <T extends BHParcelable> T objectFrom(Cursor cursor, Class<T> cls) {
        Field[] fields = cls.getDeclaredFields();
        T obj = null;
        try {
            obj = cls.newInstance();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return (T) null;
        }

        for (Field field : fields) {
            int columnIndex = cursor.getColumnIndex(field.getName());
            if (columnIndex < 0) {
                continue;
            }

            Class<?> type = field.getType();
            try {
                if (!cursor.isNull(columnIndex)) {
                    if (type.equals(int.class)) {
                        field.setInt(obj, cursor.getInt(columnIndex));
                    } else if (type.equals(long.class)) {
                        field.setLong(obj, cursor.getLong(columnIndex));
                    } else if (type.equals(double.class)) {
                        field.setDouble(obj, cursor.getDouble(columnIndex));
                    } else if (type.equals(float.class)) {
                        field.setFloat(obj, cursor.getFloat(columnIndex));
                    } else if (type.equals(boolean.class)) {
                        field.setBoolean(obj, cursor.getInt(columnIndex) == 1 ? true : false);
                    } else if (type.equals(String.class)) {
                        field.set(obj, cursor.getString(columnIndex));
                    } else if (type.equals(Date.class)) {
                        field.set(obj, dateFromString(cursor.getString(columnIndex)));
                    }
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return obj;
    }

    private Date dateFromString(String dateString) {
        try {
            if (!TextUtils.isEmpty(dateString) && dateString.trim().length() > 0) {
                SimpleDateFormat formatter = new SimpleDateFormat(dateFormat.substring(0, dateString.length() - 1));
                return formatter.parse(dateString);
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
