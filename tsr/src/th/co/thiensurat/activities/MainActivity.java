package th.co.thiensurat.activities;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.zj.btsdk.BluetoothService;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

//import th.co.bighead.utilities.BHActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHActivity;
import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHBluetoothPrinter.BHBluetoothPrinter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.printer.PrintPic;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.BaseController;
import th.co.thiensurat.data.controller.DatabaseManager;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.ThemalPrintController;
import th.co.thiensurat.data.controller.TransactionLogController;
import th.co.thiensurat.data.controller.UserController;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DeviceMenuInfo;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.PrintTextInfo;
import th.co.thiensurat.data.info.ReceiptInfo;
import th.co.thiensurat.data.info.TransactionLogInfo;
import th.co.thiensurat.fragments.admin.AdminMainFragment;
import th.co.thiensurat.fragments.complain.ComplainMainFragment;
import th.co.thiensurat.fragments.contracts.change.ChangeContractMainFragment;
import th.co.thiensurat.fragments.credit.AssignEmployeeFragment;
import th.co.thiensurat.fragments.credit.Audit.CheckCustomersMainFragment;
import th.co.thiensurat.fragments.credit.ContractDetails;
import th.co.thiensurat.fragments.credit.Import.ImportAuditListFragment;
import th.co.thiensurat.fragments.credit.Import.ImportCreditSelectDateFragment;
import th.co.thiensurat.fragments.credit.SortOrderDefault.SortOrderDefaultForCreditMainFragment;
import th.co.thiensurat.fragments.credit.SortOrderDefault.SortOrderDefaultMainFragment;
import th.co.thiensurat.fragments.credit.credit.CreditListFragment;
import th.co.thiensurat.fragments.cutdivisor.contract.CutDivisorContractListFragment;
import th.co.thiensurat.fragments.cutoff.contract.CutOffContractMainFragment;
import th.co.thiensurat.fragments.document.DocumentHistoryMainFragment;
import th.co.thiensurat.fragments.employee.EmployeeTeamFragment;
import th.co.thiensurat.fragments.employee.credit.EmployeeDetailFragment;
import th.co.thiensurat.fragments.impound.ImpoundProductMainFragment;
import th.co.thiensurat.fragments.loss.LossMainFragment;
import th.co.thiensurat.fragments.payment.first.FirstPaymentMainMenuFragment;
import th.co.thiensurat.fragments.payment.next.NextPaymentListFragment;
import th.co.thiensurat.fragments.products.change.ChangeProductCustomerListFragment;
import th.co.thiensurat.fragments.products.returns.ReturnProductListFragment;
import th.co.thiensurat.fragments.products.stock.NewCheckStockMainFragment;
import th.co.thiensurat.fragments.report.ReportDailySummaryOnlineFragment;
import th.co.thiensurat.fragments.report.ReportDailySummarySaleByProductOnlineFragment;
import th.co.thiensurat.fragments.report.ReportDashboardSummaryMainFragment;
import th.co.thiensurat.fragments.report.ReportRePrintDocumentFragment;
import th.co.thiensurat.fragments.report.ReportSummaryApprovedFragment;
import th.co.thiensurat.fragments.report.ReportSummaryChangeContractFragment;
import th.co.thiensurat.fragments.report.ReportSummaryChangeProductFragment;
import th.co.thiensurat.fragments.report.ReportSummaryComplainFragment;
import th.co.thiensurat.fragments.report.ReportSummaryCreditContractCloseAccountFragment;
import th.co.thiensurat.fragments.report.ReportSummaryCreditNextPaymentFragment;
import th.co.thiensurat.fragments.report.ReportSummaryCreditSaleAuditFragment;
import th.co.thiensurat.fragments.report.ReportSummaryCreditSendMoneyFragment;
import th.co.thiensurat.fragments.report.ReportSummaryEmployeeTeamFragment;
import th.co.thiensurat.fragments.report.ReportSummaryImpoundProductFragment;
import th.co.thiensurat.fragments.report.ReportSummaryInstallAndPaymentFragment;
import th.co.thiensurat.fragments.report.ReportSummaryPaymentAndSendMoneyFragment;
import th.co.thiensurat.fragments.report.ReportSummaryPaymentAppointmentFragment;
import th.co.thiensurat.fragments.report.ReportSummaryProductFragment;
import th.co.thiensurat.fragments.report.ReportSummarySaleByAreaFragment;
import th.co.thiensurat.fragments.report.ReportSummaryTradeProductMainFragment;
import th.co.thiensurat.fragments.report.ReportSummaryWriteOffNPLMainFragment;
import th.co.thiensurat.fragments.sales.EditContractsMainFragment;
import th.co.thiensurat.fragments.sales.SaleMainFragment;
import th.co.thiensurat.fragments.sendmoney.SendMoneySummaryMainFragment;
import th.co.thiensurat.fragments.synchronize.SynchronizeMainFragment;
//import th.co.thiensurat.retrofit.api.Service;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.TimeOutLoginService;
import th.co.thiensurat.service.TransactionService;
import th.co.thiensurat.service.data.AddContractInputInfo;
import th.co.thiensurat.service.data.AddPaymentInputInfo;
import th.co.thiensurat.service.data.AddReceiptInputInfo;
import th.co.thiensurat.service.data.AddTransactionLogSkipInputInfo;
import th.co.thiensurat.service.data.AddUserDeviceLogInputInfo;
import th.co.thiensurat.service.data.AuthenticateInputInfo;
import th.co.thiensurat.service.data.AuthenticateOutputInfo;
import th.co.thiensurat.service.data.CheckSoapOutputInfo;
import th.co.thiensurat.service.data.DeleteContractInputInfo;
import th.co.thiensurat.service.data.GetDepartmentSignatureImageInputInfo;
import th.co.thiensurat.service.data.GetDepartmentSignatureImageOutputInfo;

//import static th.co.thiensurat.retrofit.api.client.BASE_URL;

//import com.zj.btsdk.PrintPic;

public class MainActivity extends BHActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    public static final Integer[] RequireSaleCode = new Integer[]{
            R.string.main_menu_remove, R.string.main_menu_change,
            R.string.main_menu_document,
            R.string.main_menu_complain,
            R.string.main_menu_return_product
    };
    private static final String FRAGMENT_ROOT_TAG = "th.co.thiensurat.fragment.root.tag";
    //variable for print version 2
    public static final int REQUEST_ENABLE_BT = 2;
    public static final int REQUEST_CONNECT_DEVICE = 1;
    //********************************************************************************//
    //*************************** BLUE TOOTH CONNECTION ******************************//
    //********************************************************************************//
    // Member variables
    private static final String LOG_TAG = "PrinterProcess";
    private static final int DEFAULT_NETWORK_PORT = 9100;
    public static boolean checkLogin = false;
    //CheckConnecting
    public static ProgressDialog pdia;
    public static int timeOut = 20000;//20s
    public static MainActivity activity;
    private static int selectedMenu = -1;
    private static LinearLayout show;

    public SlidingMenu menu;
    public String menuName;
    public boolean checkLogout = false;
    public List<DeviceMenuInfo> menusInfo;
    public MainMenuAdapter mainMenuAdapter;
    public List<Button> listProcessButtons = new ArrayList<>();
    private TextView txtVersion;
    private RelativeLayout vwProcess;
    private TextView txtTitle;
    private TextView txtDate;
    //end variable for print version 2
    private ImageView imgMenu;
    private ListView lvMainMenu;
    private String rootViewTag;
    private View lastView = null;
    private boolean isMenuForce = false;
    private boolean isFragmentBackStack = false;
    private BHParcelable fragmentResultData = null;
    private MyBroadcastReceiver receiver;


    /**********************************************************************************************/
    private Runnable mJob = null;
    private BHBluetoothPrinter bhBluetoothPrinter;
    BluetoothService mService = null;
    BluetoothDevice con_dev = null;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BluetoothService.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case BluetoothService.STATE_CONNECTED:   //������
                            Toast.makeText(getApplicationContext(), "Connect successful",
                                    Toast.LENGTH_SHORT).show();
                            Log.d(LOG_TAG, "STATE_CONNECTED");
                            if (mService.isAvailable()) {
                                if (mJob != null) doJob(mJob, R.string.bluetooth_printing);
                            }
                            break;
                        case BluetoothService.STATE_CONNECTING:
                            Log.d(LOG_TAG, "STATE_CONNECTING");
                            break;
                        case BluetoothService.STATE_LISTEN:
                        case BluetoothService.STATE_NONE:
                            Log.d(LOG_TAG, "STATE_LISTEN &&  STATE_NONE");
                            break;
                    }
                    break;
                case BluetoothService.MESSAGE_CONNECTION_LOST:
                    Log.d(LOG_TAG, "MESSAGE_CONNECTION_LOST");
                    break;
                case BluetoothService.MESSAGE_UNABLE_CONNECT:     //�޷������豸
                    Toast.makeText(getApplicationContext(), "Unable to connect device",
                            Toast.LENGTH_SHORT).show();
                    Log.d(LOG_TAG, "MESSAGE_UNABLE_CONNECT");
                    mJob = null;
                    break;
            }
        }

    };
    /**********************************************************************************************/



    public static void checkTransactionLog() {

        try {
            show.post(new Runnable() {
                @Override
                public void run() {
                    TransactionLogController controller = new TransactionLogController();
                    List<TransactionLogInfo> trans = controller.getUnSyncTransactionLogs();
                    if (trans != null && trans.size() > 0) {
                        show.setBackgroundResource(R.color.bg_sub_title_red);
                    } else {
                        show.setBackgroundResource(R.color.bg_sub_title);
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    private void bindControls() {
        ActionBar actionBar = getActionBar();
        actionBar.setCustomView(R.layout.actionbar_title);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);



        menu = new SlidingMenu(this);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.menu_shadow);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);

        LinearLayout llMainMenu = (LinearLayout) getLayoutInflater().inflate(R.layout.fragment_main_menu, null);
        lvMainMenu = (ListView) llMainMenu.findViewById(R.id.lvMainMenu);

        TextView tvUserDetail = (TextView) llMainMenu.findViewById(R.id.tvUserDetail);

        /*** [START] :: offset statusBar ***/
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;

        tvUserDetail.setPadding(tvUserDetail.getPaddingLeft(), statusBarHeight + tvUserDetail.getPaddingTop(),tvUserDetail.getPaddingRight(),tvUserDetail.getPaddingBottom());
        /*** [END] :: offset statusBar ***/

        String userProfileString = String.format("รหัสพนักงาน: %s \nชื่อ: %s \nตำแหน่ง: %s", BHPreference.employeeID(), BHPreference.userFullName(), BHPreference.PositionName());

        userProfileString = String.format("%s\nRun on : %s", userProfileString, BHGeneral.SERVICE_MODE.toString());
        tvUserDetail.setText(userProfileString);

        menusInfo = BHPreference.getUserMenus();
        mainMenuAdapter = new MainMenuAdapter(MainActivity.this, R.layout.list_main_menu, menusInfo);
        lvMainMenu.setAdapter(mainMenuAdapter);
        lvMainMenu.setEnabled(false);

        lvMainMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainMenuInfo info = (MainMenuInfo) parent.getItemAtPosition(position);

                if (!info.menu.MenuID.equals(getString(R.string.main_menu_admin))) {
                    if (!checkDatabase() && !info.menu.MenuID.equals(getString(R.string.main_menu_logout))) {
                        Builder setupAlert;
                        setupAlert = new AlertDialog.Builder(MainActivity.this)
                                .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
                                .setMessage("กรุณาปรับปรุงข้อมูลก่อนใช้ระบบ")
                                .setCancelable(false)
                                .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        showView(BHFragment.newInstance(SynchronizeMainFragment.class));
                                        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
                                        menu.showContent();

                                        dialog.cancel();
                                    }
                                });
                        setupAlert.show();

                        return;
                    } else if (!info.menu.MenuID.equals(getString(R.string.main_menu_logout))) {
                        if (BHPreference.TimeOutLogin()) {
                            Builder setupAlert;
                            setupAlert = new AlertDialog.Builder(MainActivity.this)
                                    .setTitle("แจ้งเตือน การ Login")
                                    .setMessage("ระบบทำงานครบ " + String.valueOf(BHGeneral.TIME_OUT_LOGIN) + " ชั่วโมงแล้ว กรุณาออกจากระบบ")
                                    .setCancelable(false)
                                    .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            dialog.cancel();
                                        }
                                    });
                            setupAlert.show();
                            return;
                        }
                    }
                }

                if (!info.hasSubMenu) {
                    if (lastView != null) {
                        lastView.setBackgroundResource(R.drawable.selector_main_menu_sub);
                    }
                    selectedMenu = position;
                    view.setSelected(true);
                    lastView = view;
                    menuName = info.menu.MenuName;
                    int resID = getMenuResourceID(info.menu.MenuID);
                    selectItem(position, resID);
                } else {
                    info.menu.IsExpanded = !info.menu.IsExpanded;
                    mainMenuAdapter.refresh();
                    mainMenuAdapter.notifyDataSetChanged();
                }
            }
        });

        menu.setMenu(llMainMenu);

        txtDate = (TextView) findViewById(R.id.txtEffDate);
        Datenow();
        vwProcess = (RelativeLayout) findViewById(R.id.vwProcess);
        txtTitle = (TextView) actionBar.getCustomView().findViewById(R.id.txtTitle);
        imgMenu = (ImageView) actionBar.getCustomView().findViewById(R.id.imgMenu);

        imgMenu.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // fmMainMenu.openMenu();
                if (BHPreference.getUserNotAllowLogin().equals(UserController.LoginType.NOT_ALLOW.toString())) {
                    showMenu();
                } else if (!checkLogin) {
                    Builder setupAlert;
                    setupAlert = new AlertDialog.Builder(MainActivity.this)
                            .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
                            .setMessage("กรุณาปรับปรุงข้อมูลก่อนใช้ระบบ")
                            .setCancelable(false)
                            .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });
                    setupAlert.show();
                } else {
                    showMenu();
                }
            }
        });

        if (BHGeneral.DEVELOPER_MODE || BHPreference.IsAdmin()) {
            checkLogin = true;
        }

        if (!checkLogin) {
            if (!BHPreference.getUserNotAllowLogin().equals(UserController.LoginType.NOT_ALLOW.toString())) {
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String strLoginDate = BHPreference.LogLogin();//"2015-06-26 07:30:00";
                String strCurrentDate = formatDate.format(new Date());//"2015-06-26 07:25:00";
                try {
                    Date loginDate = formatDate.parse(strLoginDate);
                    Date currentDate = formatDate.parse(strCurrentDate);

                    Calendar logoutDate = Calendar.getInstance();
                    logoutDate.setTime(loginDate);
                    logoutDate.add(Calendar.HOUR, BHGeneral.TIME_OUT_LOGIN);
                    //test
                    //logoutDate.add(Calendar.MINUTE, BHGeneral.TIME_OUT_LOGIN);

                    Log.i("TimeOutLogin", "Logout on :" + logoutDate.toString());

                    Calendar NoticeCalendar = Calendar.getInstance();

                    NoticeCalendar.setTime(loginDate);
                    //NoticeCalendar.add(Calendar.MINUTE, 2);
                    //Log.i("SuspendServiceNotice","Notice on " + NoticeCalendar.getTime().toString());

                    NoticeCalendar.set(Calendar.HOUR_OF_DAY, 23);
                    NoticeCalendar.set(Calendar.MINUTE, 50);
                    NoticeCalendar.set(Calendar.SECOND, 0);


                    Calendar NoticeFourceLogoutCalendar = Calendar.getInstance();
                    NoticeFourceLogoutCalendar.setTime(NoticeCalendar.getTime());
                    NoticeFourceLogoutCalendar.add(Calendar.MINUTE, 5);
                    BHPreference.setLogNoticeTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(NoticeFourceLogoutCalendar.getTime()));

                    Log.i("SuspendServiceNotice", "Notice on " + NoticeCalendar.getTime().toString());
                    Log.i("SuspendServiceNotice", "Fource Logout on " + NoticeFourceLogoutCalendar.getTime().toString());

                    if ((loginDate.before(currentDate) || loginDate.compareTo(currentDate) == 0) && currentDate.before(logoutDate.getTime())) {
                        long diff = logoutDate.getTime().getTime() - currentDate.getTime();

                        long milliseconds = 1000;
                        long seconds = milliseconds;
                        long minutes = seconds * 60;
                        long hours = minutes * 60;

                        long hourOfDay = diff / hours;
                        diff = diff % hours;

                        long minuteOfHour = diff / minutes;
                        diff = diff % minutes;

                        long secondOfMinute = diff / seconds;

                       /* Intent intent = new Intent(MainActivity.MyBroadcastReceiver.ACTION_NAME);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                this.getApplicationContext(), 234324243, intent, 0);
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP, logoutDate.getTimeInMillis(), pendingIntent);*/

                        TimeOutLoginService.startAlarmTimeOutLogin(activity.getApplicationContext(), logoutDate.getTimeInMillis());

                        String strMessage = "ระบบจะทำการ Logout อีก " + (hourOfDay > 0 ? hourOfDay + " ชั่วโมง " : "") + (minuteOfHour > 0 ? minuteOfHour + " นาที " : "")
                                + (secondOfMinute > 0 ? secondOfMinute + " วินาที " : "");
                        showMessage(strMessage);

                        BHPreference.setSuspendServiceNotice(false);
                        TimeOutLoginService.startAlarmSuspendServiceNotice(activity.getApplicationContext(), NoticeCalendar.getTimeInMillis());

//                        Intent i = getApplicationContext().getPackageManager().getLaunchIntentForPackage("com.gps_tracking");
//                        if (i != null) {
//                            Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("gis://empid/" + BHPreference.employeeID() + "/BH"));
////                      i.putExtra("empid", BHPreference.employeeID());
//                            getApplicationContext().startActivity(in);
//                        }

                    } else {
                        BHPreference.setTimeOutLogin(true);
                        TimeOutLoginService.startAlarmTimeOutLogin(activity.getApplicationContext(), logoutDate.getTimeInMillis());
                        TimeOutLoginService.startAlarmSuspendServiceNotice(activity.getApplicationContext(), NoticeCalendar.getTimeInMillis());
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if (BHPreference.getUserNotAllowLogin().equals(UserController.LoginType.NOT_ALLOW.toString())) {
                showMessage("ระบบกำลังทำการ Logout");
                checkLogout = true;
                startSyncLogout();
            } else {
                /*** [START] :: Fixed - [BHPROJ-0016-1064] :: [Android-Auto-Full-Synch] เปิดให้ Auto Full-Synch แค่กรณีหลังจาก Login เสร็จแล้วเท่านั้น (ส่วน Full-Synch ตอนที่เปิด App. ให้ตัดทิ้งไปเลย)  ***/
                //startSyncLogin();


                if (checkDatabase()) {
                    BHPreference.setLastloginID(BHPreference.userID());
                    checkLogin = true;
                } else {
                    startSyncLogin();
                }
                /*** [END] :: Fixed - [BHPROJ-0016-1064] :: [Android-Auto-Full-Synch] เปิดให้ Auto Full-Synch แค่กรณีหลังจาก Login เสร็จแล้วเท่านั้น (ส่วน Full-Synch ตอนที่เปิด App. ให้ตัดทิ้งไปเลย) ***/





                    /*Builder setupAlert;
                    setupAlert = new AlertDialog.Builder(this)
                            .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
                            .setMessage("กรุณาปรับปรุงข้อมูลก่อนใช้ระบบ")
                            .setCancelable(false)
                            .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    startSyncLogin();
                                }
                            });
                    setupAlert.show();*/
            }
            //checkLogin();
        }
        lvMainMenu.setEnabled(true);
    }

    private void Datenow() {
        // TODO Auto-generated method stub
        String date = BHUtilities.dateFormat(new Date());
        txtDate.setText("ณ   วันที่   " + date);
    }

    public void setupTitle(final int titleResourceID) {
        String title = titleResourceID > 0 ? getResources().getString(titleResourceID) : menuName;
        txtTitle.setText(title);

        int size = (int) getResources().getDimension(R.dimen.title_bar_font_size);
        txtTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getTitleSize(title, txtTitle, size));
    }

    public float getTitleSize (String text, TextView textView, float size)
    {
        /*Rect bounds = new Rect();
        TextPaint textPaint = textView.getPaint();
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        //int height = bounds.height();
        int width = bounds.width();

        return width;*/

        int textWidth = textView.getWidth();
        Rect bounds = new Rect();
        Paint p = new Paint();
        p.setTypeface(textView.getTypeface());
        p.setTextSize(size);

        p.getTextBounds(text, 0, text.length(), bounds);
        int width = bounds.width();

        if (textWidth >= width) {
            return size;
        } else {
            return  getTitleSize(text, textView, size - 0.1f);
        }
    }

    public void forceButtonClick(Integer ButtonID) {
        for (Button btn : listProcessButtons) {
                if (btn.getId() == ButtonID) {
                    btn.performClick();
                    break;
                }
        }
    }

    public void setViewProcessButtons(List<Integer> listId, int view) {
        vwProcess.setVisibility(View.GONE);
        int count = 0;
        List<Integer> listCount = new ArrayList<Integer>();

        for (Button btn : listProcessButtons) {
            for (int id : listId) {
                if (btn.getId() == id) {
                    btn.setVisibility(view);
                }
            }

            if (btn.getVisibility() == View.VISIBLE) {
                listCount.add(count);
            }
            count++;

        }

        setLayoutProcessButtons(listCount, 0);

        if (listCount.size() != 0) {
            vwProcess.setVisibility(View.VISIBLE);
        }
    }

    public void setLayoutProcessButtons(List<Integer> listCount, int maxWidth) {


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        //int height = size.y;

        if (listCount.size() * maxWidth > width) {
            maxWidth = (width / listCount.size()) - (listCount.size() * 10);
        }

        int maxHeight = 0;

        for (int ii = 0; ii < listCount.size(); ii++) {
            Button btn = listProcessButtons.get(listCount.get(ii));

            if (maxWidth != 0) {
                btn.setWidth(maxWidth);
            }

            if ((ii == 0 && listCount.size() == 1) || (ii == 1 && listCount.size() == 3)) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                btn.setLayoutParams(params);
            } else if (ii == 0 && listCount.size() > 1) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                btn.setLayoutParams(params);
            } else if (ii > 0 && ii + 1 == listCount.size()) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                btn.setLayoutParams(params);
            }

            btn.measure(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
            int height = btn.getMeasuredHeight();
            maxHeight = Math.max(height, maxHeight);
        }

        for(Button btn : listProcessButtons) {
            if (maxHeight != 0) {
                btn.setHeight(maxHeight);
            }
        }
    }

    public void setupProcessButtons(final BHFragment fragment, int[] btnIDs) {
        vwProcess.removeAllViews();
        listProcessButtons = new ArrayList<>();
        List<Integer> listCount = new ArrayList<Integer>();

        int maxWidth = 0;
        if (btnIDs != null) {
            final int max = 3;
            for (int ii = 0; ii < btnIDs.length && ii < max; ii++) {
                final int id = btnIDs[ii];

                Button btn = new Button(getApplicationContext());

                btn.setId(id);
                btn.setBackgroundResource(R.drawable.button_style_seagull);
                btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.0f);
                btn.setText(id);
                btn.setTextColor(getResources().getColor(R.color.font_color_white));

                btn.measure(android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
                int width = btn.getMeasuredWidth();
                maxWidth = Math.max(width, maxWidth);
                // btn.setWidth(width);

                btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        final Button b = (Button) v;

                        b.setEnabled(false);
                        //Log.d("BHPROJ-1036-7758", "Enabled = false");

                        fragment.onProcessButtonClicked(id);

                        b.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                b.setEnabled(true);
                                //Log.d("BHPROJ-1036-7758", "Enabled = true");
                            }
                        }, 500);
                    }
                });

                vwProcess.addView(btn);

                listProcessButtons.add(btn);
                listCount.add(ii);
            }

            setLayoutProcessButtons(listCount, maxWidth);

            vwProcess.setVisibility(View.VISIBLE);
        } else {
            vwProcess.setVisibility(View.GONE);
        }
    }

    public boolean showNextView(BHFragment fragment) {
        return showView(fragment, false);
    }

    public boolean showView(BHFragment fragment) {
        return showView(fragment, true);
    }

    private boolean showView(BHFragment fragment, boolean clearBackStack) {

        if (BHGeneral.AVAILABLE_DATE != null && new Date().after(BHGeneral.AVAILABLE_DATE)) {
            showMessage("Application can not be use at this time, Please contact Administrator");
            return false;
        }

        isFragmentBackStack = false;
        String backStackName = fragment.fragmentTag();
        if (clearBackStack) {
            getFragmentManager().popBackStackImmediate(rootViewTag, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            if (backStackName == null) {
                backStackName = MainActivity.FRAGMENT_ROOT_TAG;
            }

            rootViewTag = backStackName;
        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        transaction.setCustomAnimations(R.animator.fragment_slide_left_enter, R.animator.fragment_slide_left_exit, R.animator.fragment_slide_right_enter,
                R.animator.fragment_slide_right_exit);
        transaction.replace(R.id.vwContainer, fragment);
        transaction.addToBackStack(backStackName);

        transaction.commit();

        setRequestedOrientation(fragment.enableLandscape() ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
        //setRequestedOrientation(fragment.enableLandscape() ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        return true;
    }

    public void showLastView(String backStackName, BHParcelable data) {
        isFragmentBackStack = true;
        fragmentResultData = data;

        if (!getFragmentManager().popBackStackImmediate(backStackName, 0)) {
            getFragmentManager().popBackStackImmediate();
            if (getFragmentManager().getBackStackEntryCount() > 1) {
                BHFragment fragment = (BHFragment) getFragmentManager().findFragmentById(R.id.vwContainer);
                setRequestedOrientation(fragment.enableLandscape() ? ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
                //setRequestedOrientation(fragment.enableLandscape() ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    public boolean isFragmentBackStack() {
        boolean result = isFragmentBackStack;
        isFragmentBackStack = false;
        return result;
    }

    public BHParcelable getFragmentResult() {
        return fragmentResultData;
    }

    public void setFragmentResult(BHParcelable data) {
        fragmentResultData = data;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;
        BHFragment.setActivity(MainActivity.this);

        if (savedInstanceState != null) {
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();

            return;
        }

        bhBluetoothPrinter = new BHBluetoothPrinter(this);
        (new BackgroundProcess(this) {
            @Override
            protected void calling() {

            }

            @Override
            protected void after() {
                /*** [START] :: Fixed - [BHPROJ-0024-3216] :: [LINE-25/07/2016][Android-MainActivity] หากในเครื่องมีข้อมูลค้างอยู่ ที่ไม่สามารถ sync ข้อมูลไปที่ server ได้ ให้ระบบเปลี่ยนแถบ   ***/
                show = (LinearLayout) findViewById(R.id.show);
                checkTransactionLog();
                /*** [END] :: Fixed - [BHPROJ-0024-3216] :: [LINE-25/07/2016][Android-MainActivity] หากในเครื่องมีข้อมูลค้างอยู่ ที่ไม่สามารถ sync ข้อมูลไปที่ server ได้ ให้ระบบเปลี่ยนแถบ  ***/

                /*activity = MainActivity.this;
                BHFragment.setActivity(MainActivity.this);*/

                txtVersion = (TextView) findViewById(R.id.txtVersion);
                txtVersion.setText(String.format("%s (%s)", getResources().getString(R.string.tsr_app_version), BHGeneral.SERVICE_MODE.toString()));

                mService = new BluetoothService(MainActivity.this, mHandler);

                bindControls();
                if (savedInstanceState == null) {
                    forceShowMenu();
                }

                receiver = new MyBroadcastReceiver(MainActivity.this);
                registerReceiver(receiver, new IntentFilter(MyBroadcastReceiver.ACTION_NAME));
            }
        }).start();




    }

    @Override
    protected void onPause() {
        super.onPause();
        BHLoading.close();
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(receiver);
        } catch (Exception ex){}

        super.onDestroy();

        if (mService != null)   //close bluetooth
            mService.stop();
        mService = null;
        con_dev = null;

        ThemalPrintController.delTempPrint(this); //clear tmp print

    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        if (!checkLogin) {
            Builder setupAlert;
            setupAlert = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
                    .setMessage("กรุณาปรับปรุงข้อมูลก่อนใช้ระบบ")
                    .setCancelable(false)
                    .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.cancel();
                        }
                    });
            setupAlert.show();
        } else {
            if (menu.isMenuShowing()) {
                if (!isMenuForce) {
                    menu.showContent();
                }
            } else if (getFragmentManager().getBackStackEntryCount() > 1) {
                BHFragment fragment = (BHFragment) getFragmentManager().findFragmentById(R.id.vwContainer);
                fragment.onProcessButtonClicked(-1);
            } else {
                showMenu();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        bhBluetoothPrinter.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        new BHPermissions().onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    private boolean checkDatabase() {
        return new BaseController().checkExistingDatabase();
    }

    private int getMenuResourceID(String menuID) {
        int[] menuIDs = new int[]{

                /*** [START] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
//                R.string.main_menu_default,
                R.string.main_menu_admin,
                R.string.main_menu_report_daily_summary,
                R.string.main_menu_report_emp_team,
                R.string.main_menu_report_product,
                R.string.main_menu_report_install_payment,
                R.string.main_menu_report_change_product,
                R.string.main_menu_report_impound_product,
                R.string.main_menu_report_change_contract,
                R.string.main_menu_report_payment_appointment,
                R.string.main_menu_report_writeoff_npl,
                R.string.main_menu_report_approved,
                R.string.main_menu_report_complain,
                R.string.main_menu_report_trade_product,
                R.string.main_menu_report_payment_sendmoney,
                R.string.main_menu_report_zone,
                R.string.main_menu_report_re_print,
                R.string.main_menu_report_credit_saleaudit,
                R.string.main_menu_report_credit_next_payment,
                R.string.main_menu_report_credit_sendmoney,
                R.string.main_menu_report_credit_contract_close_account,
                /*** [END] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/

                R.string.main_menu_checkstock,
                R.string.main_menu_sales,
                R.string.main_menu_first,
                R.string.main_menu_next_period,
                R.string.main_menu_money,
                R.string.main_menu_remove,
                R.string.main_menu_change,
                R.string.main_menu_document,
                R.string.main_menu_loss,
                R.string.main_menu_total,
                R.string.main_menu_report_daily_summary_sale_by_product_online,
                R.string.main_menu_team,
                R.string.main_menu_brochure,
                R.string.main_menu_refresh,
                R.string.main_menu_complain,
                R.string.main_menu_return_product,
                R.string.main_menu_edit_contracts,
                R.string.main_menu_logout,
                //credit
                R.string.main_menu_credit_employee_detail,
                R.string.main_menu_credit_import_credit_data,
                R.string.main_menu_credit_audit_check_customers,
                R.string.main_menu_credit_salepaymentperiod_credit_customers,
                R.string.main_menu_credit_cut_off_contract,
                R.string.main_menu_credit_cut_divisor_contract,
                R.string.main_menu_credit_contract_details,
                R.string.main_menu_credit_import_audit_data,
                R.string.main_menu_credit_sort_order_default_audit,
                R.string.main_menu_credit_sort_order_default_credit,
                R.string.main_menu_credit_assign_push_pull,
                R.string.main_menu_goto_tssm

        };

        for (int id : menuIDs) {
            if (menuID.equals(getString(id))) {
                return id;
            }
        }

        return -1;
    }

    private void selectItem(int position, int titleResourceID) {
        String currentUserId = BHPreference.userID() == null ? "" : BHPreference.userID();
        String lastUserId = BHPreference.LastLoginId() == null ? "" : BHPreference.LastLoginId();
        boolean isPassRequireSaleCode = !(Arrays.asList(RequireSaleCode).contains(Integer.valueOf(titleResourceID)) && TextUtils.isEmpty(BHPreference.saleCode()));
        boolean success = false;
        if ((titleResourceID == R.string.main_menu_logout
                || (checkDatabase() && currentUserId.equals(lastUserId) && BHPreference.getUserNotAllowLogin().equals(UserController.LoginType.ALLOW.toString())))
                && isPassRequireSaleCode || BHPreference.IsAdmin()) {

            BHFragment bhFragment = null;

            switch (titleResourceID) {

                /*** [START] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/

                /*
                case R.string.main_menu_default: // ข้อมูลหลัก        // not require SaleCode
//                    BHFragment bhFragment = BHFragment.newInstance(ReportSummaryMainMenuFragment.class);
                    BHFragment bhFragment = null;
                    if (BHPreference.sourceSystem().toUpperCase().equals("SALE")) {
                        bhFragment = BHFragment.newInstance(ReportSummaryReportMainMenuForSaleFragment.class);
                    } else if (BHPreference.sourceSystem().toUpperCase().equals("CREDIT")) {
                        bhFragment = BHFragment.newInstance(ReportSummaryReportMainMenuForCreditFragment.class);
                    }
                    //bhFragment.bOrentation=true;
                    success = showView(bhFragment);
                    break;
                */

                case R.string.main_menu_admin:
                    success = showView(BHFragment.newInstance(AdminMainFragment.class));
                    break;

                case R.string.main_menu_report_daily_summary:            // รายงานสรุปยอดขาย - ReportDashboard (SALE)
                    bhFragment = BHFragment.newInstance(ReportDashboardSummaryMainFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_emp_team:             // รายงานโครงสร้างพนักงาน - ReportSaleAndDriver (SALE + CREDIT)
                    bhFragment = BHFragment.newInstance(ReportSummaryEmployeeTeamFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_product:          // รายงานยอดสินค้าคงเหลือ - ReportInventory (SALE)
                    bhFragment = BHFragment.newInstance(ReportSummaryProductFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_install_payment:            // รายงานยอดติดตั้ง/ขายสินค้า - ReportInstallAndPayment (SALE)
                    bhFragment = BHFragment.newInstance(ReportSummaryInstallAndPaymentFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_change_product:   // รายงานเปลี่ยนเครื่อง - ReportChangeProduct (SALE + CREDIT)
                    bhFragment = BHFragment.newInstance(ReportSummaryChangeProductFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_impound_product:                  // รายงานถอดเครื่อง - ReportImpoundProduct (SALE + CREDIT)
                    bhFragment = BHFragment.newInstance(ReportSummaryImpoundProductFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_change_contract:                  // รายงานเปลี่ยนสัญญา - ReportChangeContract (SALE + CREDIT)
                    bhFragment = BHFragment.newInstance(ReportSummaryChangeContractFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_payment_appointment:              // รายงานนัดเก็บเงินงวดแรก - ReportPaymentAppointment (SALE)
                    bhFragment = BHFragment.newInstance(ReportSummaryPaymentAppointmentFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_writeoff_npl:             // รายงานตัดสัญญาค้าง - ReportWriteOffNPL (SALE)
                    bhFragment = BHFragment.newInstance(ReportSummaryWriteOffNPLMainFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_approved:          // รายงานขออนุมัติ - ReportApproved  (SALE + CREDIT)
                    bhFragment = BHFragment.newInstance(ReportSummaryApprovedFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_complain:             // รายงานสรุปการแจ้งปัญหา - ReportComplain (SALE + CREDIT)
                    bhFragment = BHFragment.newInstance(ReportSummaryComplainFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_trade_product:                    // รายงานเครื่องเทิร์น - ReportTradeInProduct (SALE)
                    bhFragment = BHFragment.newInstance(ReportSummaryTradeProductMainFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_payment_sendmoney:    // รายงานเก็บเงินงวดแรก/ถัดไป - ReportPaymentAndSendMoney (SALE)
                    bhFragment = BHFragment.newInstance(ReportSummaryPaymentAndSendMoneyFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_zone:             // รายงานพื้นที่ขาย - ReportSaleArea (SALE)
                    bhFragment = BHFragment.newInstance(ReportSummarySaleByAreaFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_re_print: // รายงานการพิมพ์เอกสารที่มีการ Re-Print - ReportRePrintDocument (SALE + CREDIT)
                    bhFragment = BHFragment.newInstance(ReportRePrintDocumentFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_credit_saleaudit:     // รายงานการตรวจสอบการ์ดงวดแรก - ReportSaleAudit (CREDIT)
                    bhFragment = BHFragment.newInstance(ReportSummaryCreditSaleAuditFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_credit_next_payment:  // รายงานการเก็บเงินค่างวด - ReportNextPayment (CREDIT)
                    bhFragment = BHFragment.newInstance(ReportSummaryCreditNextPaymentFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_credit_sendmoney:    // รายงานสรุปการส่งเงิน - ReportSendMoney (CREDIT)
                    bhFragment = BHFragment.newInstance(ReportSummaryCreditSendMoneyFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;

                case R.string.main_menu_report_credit_contract_close_account: // รายงานตัดสด - ReportContractCloseAccount (CREDIT)
                    bhFragment = BHFragment.newInstance(ReportSummaryCreditContractCloseAccountFragment.class);
                    bhFragment.bOrentation = true;
                    success = showView(bhFragment);
                    break;


                /*** [END] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/


                case R.string.main_menu_checkstock: // not require SaleCode
                    //success = showView(BHFragment.newInstance(NewCheckStockMainFragment.class));
                    pdia = new ProgressDialog(this);
                    pdia.setIndeterminate(false);
                    pdia.setCancelable(false);
                    pdia.setTitle("Connecting To Internet");
                    pdia.setMessage("Checking...");
                    pdia.show();

                    if (isConnectingToInternet()) {
                        pdia.setTitle("Connecting To Server");
                        (new AsyncTask<String, Void, Boolean>() {

                            @Override
                            protected Boolean doInBackground(String... urls) {

                                boolean result = false;
                                try {
                                    HttpGet httpGet = new HttpGet(urls[0]);
                                    HttpClient client = new DefaultHttpClient();
                                    //TimeOut 20s
                                    HttpParams params = client.getParams();
                                    HttpConnectionParams.setConnectionTimeout(params, timeOut);
                                    HttpConnectionParams.setSoTimeout(params, timeOut);

                                    HttpResponse response = client.execute(httpGet);

                                    int statusCode = response.getStatusLine().getStatusCode();

                                    if (statusCode == 200) {
                                        result = true;
                                    }

                                } catch (ClientProtocolException e) {

                                } catch (IOException e) {

                                }

                                return result;
                            }

                            protected void onPostExecute(Boolean result) {
                                pdia.dismiss();
                                if (!result) {
                                    showWarningDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้");
                                } else {
                                    menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
                                    menu.showContent();
                                    showView(BHFragment.newInstance(NewCheckStockMainFragment.class));
                                }
                            }
                        }).execute(BHPreference.TSR_SERVICE_URL);
                    } else {
                        pdia.dismiss();
                        showWarningDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
                    }

                    break;

                case R.string.main_menu_sales: // not require SaleCode
                    success = showView(BHFragment.newInstance(SaleMainFragment.class));
                    break;

                case R.string.main_menu_first: // not require SaleCode
                    success = showView(BHFragment.newInstance(FirstPaymentMainMenuFragment.class));
                    break;

                case R.string.main_menu_next_period: // not require SaleCode
                    success = showView(BHFragment.newInstance(NextPaymentListFragment.class));
                    break;

                case R.string.main_menu_money: // not require SaleCode
                    success = showView(BHFragment.newInstance(SendMoneySummaryMainFragment.class));
                    break;

                case R.string.main_menu_remove: // require SaleCode
                    success = showView(BHFragment.newInstance(ImpoundProductMainFragment.class));
                    break;

                case R.string.main_menu_change: //ระบบเปลี่ยนเครื่อง // require SaleCode
                    success = showView(BHFragment.newInstance(ChangeProductCustomerListFragment.class));
                    break;

                case R.string.main_menu_document: // require SaleCode
                    success = showView(BHFragment.newInstance(ChangeContractMainFragment.class));
                    break;

                case R.string.main_menu_loss: // not require SaleCode
                    success = showView(BHFragment.newInstance(LossMainFragment.class));
                    break;

                case R.string.main_menu_total: //สรุปยอดขาย // not require SaleCode
                    /*** [START] :: Fixed - [BHPROJ-0026-977] :: [Meeting@TSR@07/03/2559] [Report-รายการสรุปประจำวันของเก็บเงิน] เพิ่มเติม Report Dashboard ของระบบงานเก็บเงิน (หน้าตาจะคล้าย ๆ DailySummary) ***/

//                    BHFragment reportDashBoard = BHFragment.newInstance(ReportDashboardSummaryMainFragment.class);
//                    reportDashBoard.bOrentation = true;
//                    success = showView(reportDashBoard);

                    success = showView(BHFragment.newInstance(ReportDailySummaryOnlineFragment.class));
                    /*** [END] :: Fixed - [BHPROJ-0026-977] :: [Meeting@TSR@07/03/2559] [Report-รายการสรุปประจำวันของเก็บเงิน] เพิ่มเติม Report Dashboard ของระบบงานเก็บเงิน (หน้าตาจะคล้าย ๆ DailySummary) ***/
                    break;

                case R.string.main_menu_report_daily_summary_sale_by_product_online: //สรุปรายการประจำวัน(แยกตามสินค้า)
                    /*** [START] :: Fixed - [BHPROJ-0018-3098] :: [Report] จัดทำรายงานการขายสินค้า break ตามชื่อสินค้า โดยดูเป็นของพนักงานขายแต่ละคน ***/
                    success = showView(BHFragment.newInstance(ReportDailySummarySaleByProductOnlineFragment.class));
                    /*** [END] :: Fixed - [BHPROJ-0018-3098] :: [Report] จัดทำรายงานการขายสินค้า break ตามชื่อสินค้า โดยดูเป็นของพนักงานขายแต่ละคน ***/
                    break;

                case R.string.main_menu_team: //พนักงานขาย // not require SaleCode
                    success = showView(BHFragment.newInstance(EmployeeTeamFragment.class));
                    break;

                case R.string.main_menu_credit_employee_detail: //พนักงานเก็บเงิน credit // not require SaleCode
                    success = showView(BHFragment.newInstance(EmployeeDetailFragment.class));
                    break;

                case R.string.main_menu_detail: // not require SaleCode
                    break;

                case R.string.main_menu_brochure: // not require SaleCode
                    success = showView(BHFragment.newInstance(DocumentHistoryMainFragment.class));
                    break;

                case R.string.main_menu_refresh: //ระบบปรับปรุงฐานข้อมูล // not require SaleCode
                    success = showView(BHFragment.newInstance(SynchronizeMainFragment.class));
                    break;

                case R.string.main_menu_complain: // require SaleCode
                    success = showView(BHFragment.newInstance(ComplainMainFragment.class));
                    break;

                case R.string.main_menu_return_product: // require SaleCode
                    success = showView(BHFragment.newInstance(ReturnProductListFragment.class));
                    break;

                case R.string.main_menu_edit_contracts: //แก้ไขสัญญาซื้อขาย // not require SaleCode
                    success = showView(BHFragment.newInstance(EditContractsMainFragment.class));
                    break;

                case R.string.main_menu_credit_import_credit_data: //พนักงานเก็บเงิน credit // not require SaleCode
                    success = showView(BHFragment.newInstance(ImportCreditSelectDateFragment.class));
                    break;

                case R.string.main_menu_credit_import_audit_data: //พนักงานเก็บเงิน audit // not require SaleCode
                    success = showView(BHFragment.newInstance(ImportAuditListFragment.class));
                    break;

                case R.string.main_menu_credit_audit_check_customers: //ระบบตรวจสอบลูกค้า credit // not require SaleCode
                    success = showView(BHFragment.newInstance(CheckCustomersMainFragment.class));
                    break;

                case R.string.main_menu_credit_salepaymentperiod_credit_customers: // not require SaleCode
                    ///success = showView(BHFragment.newInstance(CreditMainFragment.class));

                    CreditListFragment.Data input = new CreditListFragment.Data();
                    input.selectedDate =  Calendar.getInstance().getTime();
                    CreditListFragment fragment = BHFragment
                            .newInstance(CreditListFragment.class, input);
                    success = showNextView(fragment);

                    break;
                case R.string.main_menu_credit_cut_off_contract://ระบบตัดสัญญาออกจากฟอร์ม // not require SaleCode
                    success = showView(BHFragment.newInstance(CutOffContractMainFragment.class));
                    break;
                case R.string.main_menu_credit_cut_divisor_contract://ระบบตัดตัวหาร // not require SaleCode
                    success = showView(BHFragment.newInstance(CutDivisorContractListFragment.class));
                    break;
                case R.string.main_menu_credit_contract_details://รายละเอียดสัญญา ของ CREDIT // not require SaleCode
                    success = showView(BHFragment.newInstance(ContractDetails.class));
                    break;
                case R.string.main_menu_credit_sort_order_default_audit://ระบบจัดลำดับค่าเริ่มต้นสำหรับตรวจสอบลูกค้า
                    SortOrderDefaultMainFragment.Data dataAudit = new SortOrderDefaultMainFragment.Data();
                    dataAudit.sortType = SortOrderDefaultMainFragment.SortType.Audit;
                    SortOrderDefaultMainFragment fragmentAudit = BHFragment.newInstance(SortOrderDefaultMainFragment.class, dataAudit);
                    success = showView(fragmentAudit);
                    break;
                case R.string.main_menu_credit_sort_order_default_credit://ระบบจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน
                    /*** [START] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/
                    /*
                    SortOrderDefaultMainFragment.Data dataCredit = new SortOrderDefaultMainFragment.Data();
                    dataCredit.sortType = SortOrderDefaultMainFragment.SortType.Credit;
                    SortOrderDefaultMainFragment fragmentCredit = BHFragment.newInstance(SortOrderDefaultMainFragment.class, dataCredit);
                    */
                    SortOrderDefaultForCreditMainFragment.Data dataCredit = new SortOrderDefaultForCreditMainFragment.Data();
                    dataCredit.sortType = SortOrderDefaultForCreditMainFragment.SortType.Credit;
                    SortOrderDefaultForCreditMainFragment fragmentCredit = BHFragment.newInstance(SortOrderDefaultForCreditMainFragment.class, dataCredit);
                    /*** [END] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/
                    success = showView(fragmentCredit);
                    break;
                case R.string.main_menu_credit_assign_push_pull://ปรับปรุงข้อมูลการมอบหมายให้ไปเก็บเงิน ของ CREDIT // not require SaleCode
                    //-- Fixed - [BHPROJ-0026-3265] [Backend+Frontend] เพิ่มเมนูสำหรับทำการ ย้ายเข้า/ย้ายออก ข้อมูล Assign การเก็บเงิน
                    success = showView(BHFragment.newInstance(AssignEmployeeFragment.class));
                    break;
                case R.string.main_menu_goto_tssm://ระบบ TSSM // not require SaleCode
                    /*** [START] :: Fixed - [BHPROJ-0026-3266][LINE@20/09/2016][Android-เพิ่มเมนูใหม่] คุณหนุ๋ยแจ้งขอเพิ่มเมนูระบบ TSSM เพื่อ Link ไปยัง URL ข้างนอกโดยส่งค่า EmpID ออกไปให้ ***/
                    /*** [START] :: Fixed - [BHPROJ-1036-8613] - แก้ไข Link เก่าให้เป็น Link ใหม่ ของระบบ TSSM จากใน App ***/
                    //String url = "http://app.thiensurat.co.th/tssm/index.php?empid=" + BHPreference.employeeID();
                    String url = "http://tssm.thiensurat.co.th/login2.php?empid=" + BHPreference.employeeID();
                    /*** [END] :: Fixed - [BHPROJ-1036-8613] - แก้ไข Link เก่าให้เป็น Link ใหม่ ของระบบ TSSM จากใน App  ***/

                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                    break;
                    /*** [END] :: Fixed - [BHPROJ-0026-3266][LINE@20/09/2016][Android-เพิ่มเมนูใหม่] คุณหนุ๋ยแจ้งขอเพิ่มเมนูระบบ TSSM เพื่อ Link ไปยัง URL ข้างนอกโดยส่งค่า EmpID ออกไปให้ ***/
                case R.string.main_menu_logout: //ออกจากระบบ // not require SaleCode
                    List<TransactionLogInfo> trInfo = null;
                    trInfo = TSRController.getTransactionLogBySyncStatus(false);
                    if (trInfo == null) {

                        if (isConnectingToInternet()) {
                            (new AsyncTask<String, Void, Boolean>() {
                                @Override
                                protected void onPreExecute() {
                                    BHLoading.show(activity);
                                }

                                @Override
                                protected Boolean doInBackground(String... urls) {

                                    boolean result = false;
                                    try {
                                        HttpGet httpGet = new HttpGet(urls[0]);
                                        HttpClient client = new DefaultHttpClient();
                                        //TimeOut 20s
                                        HttpParams params = client.getParams();
                                        HttpConnectionParams.setConnectionTimeout(params, timeOut);
                                        HttpConnectionParams.setSoTimeout(params, timeOut);

                                        HttpResponse response = client.execute(httpGet);

                                        int statusCode = response.getStatusLine().getStatusCode();

                                        if (statusCode == 200) {
                                            result = true;
                                        }

                                    } catch (ClientProtocolException e) {

                                    } catch (IOException e) {

                                    }

                                    return result;
                                }

                                protected void onPostExecute(Boolean result) {
                                    if (!result) {
                                        showWarningDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้");
                                        BHLoading.close();
                                    } else {
                                        logout(BHPreference.userID(), BHPreference.userDeviceId(), AddUserDeviceLogInputInfo.UserDeviceLogProcessType.ANDROID_LOGOUT.toString());
                                    }
                                }
                            }).execute(BHPreference.TSR_SERVICE_URL);
                        } else {

                            showWarningDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
                        }


                    } else {
                        Builder setupAlert;
                        //success = showView(BHFragment.newInstance(SynchronizeMainFragment.class));
                        setupAlert = new AlertDialog.Builder(this)
                                .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
                                .setMessage("กรุณาปรับปรุงข้อมูลก่อนออกจากระบบ")
                                .setCancelable(false)
                                .setPositiveButton("ปรับปรุง", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        checkLogout = true;
                                        startSyncLogout();
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        dialog.cancel();
                                    }
                                });
                        setupAlert.show();
                        break;
                    }
                    //logout();
                    return;

                default:
                    break;
            }
        } else {
            if (BHPreference.getUserNotAllowLogin().equals(UserController.LoginType.NOT_ALLOW.toString())) {
                Builder setupAlert;
                setupAlert = new AlertDialog.Builder(this)
                        .setTitle("แจ้งเตือน")
                        .setMessage(
                                "ไม่สามารถใช้งานระบบได้ \nเนื่องจากชื่อผู้ใช้นี้ไม่ได้รับสิทธิ์ในการเข้าใช้งานระบบ \nกรุณาออกจากระบบ หรือติดต่อเจ้าหน้าที่")
                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                setupAlert.show();
            } else if (!isPassRequireSaleCode) {
                Builder setupAlert;
                setupAlert = new AlertDialog.Builder(this)
                        .setTitle("แจ้งเตือน")
                        .setMessage(
                                "ไม่สามารถใช้งานเมนูนี้ได้ \nเนื่องจากชื่อผู้ใช้นี้ไม่มี SaleCode กรุณาติดต่อเจ้าหน้าที่")
                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                setupAlert.show();
            } else {
                success = showView(BHFragment.newInstance(SynchronizeMainFragment.class));
                Builder setupAlert;
                setupAlert = new AlertDialog.Builder(this)
                        .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
                        .setMessage(
                                "ระบบไม่พบข้อมูลที่ถูกต้อง หรือมีการเปลี่ยนแปลงผู้ใช้งาน \nหรือมีข้อมูลบางส่วนที่ยังไม่ได้มีการส่งข้อมูลไปยัง Server \nให้ดำเนินการปรับปรุงข้อมูลก่อนเริ่มใช้งานต่อไป")
                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                setupAlert.show();
            }
        }

        if (success) {
            menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
            menu.showContent();
        }
    }

    private void forceShowMenu() {
        isMenuForce = true;

        menu.setBehindOffset(0);
        menu.showMenu();
    }

    private void showMenu() {
        isMenuForce = false;
        menu.showMenu();
    }

    public void ServiceSuspendNotice(){
        (new BackgroundProcess(MainActivity.this){
            @Override
            protected void before() {

            }

            @Override
            protected void calling() {

            }
            @Override
            protected void after() {
                try {
                    Builder setupAlert = new Builder(MainActivity.this)
                            .setTitle("แจ้งเตือน")
                            .setMessage("ระบบจะปิดการใช้งานในเวลา 23.55 น. กรุณาเชื่อมต่อ Internet และ Logout ออกจากระบบก่อนเวลาที่กำหนด")
                            .setCancelable(false)
                            .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    dialog.cancel();
                                }
                            });

                    setupAlert.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start(true, true);
    }

    public void SuspendServiceLogout(){
        (new BackgroundProcess(MainActivity.this){
            @Override
            protected void before() {

            }

            @Override
            protected void calling() {

            }
            @Override
            protected void after() {
                try {
                    Builder setupAlert = new Builder(MainActivity.this)
                        .setTitle("แจ้งเตือน")
                        .setMessage("ระบบจะทำการ Logout กรุณาตรวจสอบการเชื่อมต่อ Internet และกด ตกลง")
                        .setCancelable(false)
                        .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (TimeOutLoginService.isInternetConnected(MainActivity.this)) {
                                    dialog.cancel();
                                    Calendar logoutDate = Calendar.getInstance();
                                    TimeOutLoginService.cancelAlarm(activity.getApplicationContext());
                                    TimeOutLoginService.cancelAlarmSuspendServiceNotice(activity.getApplicationContext());
                                    TimeOutLoginService.startAlarmTimeOutLogin(activity.getApplicationContext(), logoutDate.getTimeInMillis());
                                } else {
                                    TimeOutLoginService.cancelAlarmSuspendServiceNotice(activity.getApplicationContext());
                                    BHPreference.setSuspendServiceNotice(true);
                                    TimeOutLoginService.startAlarmSuspendServiceNotice(activity.getApplicationContext(), Calendar.getInstance().getTimeInMillis());

                                }
                            }
                        });

                    setupAlert.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start(true, true);
    }
    
    public void logout(final String userName, final String deviceID, final String userDeviceLogProcessType) {
        //final BHLoading dialog = BHLoading.show(MainActivity.this);
        final Handler handler = new Handler();

        if (BHPreference.IsAdmin()) {
            (new BackgroundProcess(MainActivity.this) {

                @Override
                protected void calling() {
                    // TODO Auto-generated method stub]
                }

                @Override
                protected void after() {
                    // TODO Auto-generated method stub

                    BHPreference.setIsAdmin(false);
                    BHPreference.setLastloginID(null);
                    BHPreference.setServiceMode(null);
                    BHPreference.setUserID(null);
                    BHPreference.setEmployeeID(null);
                    BHPreference.setUserNotAllowLogin(UserController.LoginType.ALLOW.toString());

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    //dialog.dismiss();
                    checkLogin = false;
                    finish();
                }

            }).start(true, true);
        } else {
            (new BackgroundProcess(MainActivity.this) {
                CheckSoapOutputInfo checkSoapOutput;

                AuthenticateInputInfo input;
                AuthenticateOutputInfo output;

                /*** [START] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical  ***/
                AddUserDeviceLogInputInfo inputAddUserDeviceLog;

                /*** [END] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***/

                @Override
                protected void before() {
                    // TODO Auto-generated method stub
                    input = new AuthenticateInputInfo();
//                input.UserName = BHPreference.userID();
//                input.DeviceID = BHPreference.userDeviceId();
                    input.UserName = userName;
                    input.DeviceID = deviceID;
                    // input.UserName = "A10687";//Fix TEst


                    /*** [START] :: Fixed - [BHPROJ-0026-3271] [Back-End] เพิ่ม Field VersionCode ใน ตาราง [UserDevice] เพื่อเก็บว่า ณ ขณะนั้นใช้งาน TSR Mobile App version อะไรอยู่ (เป็น VersionCode ที่อยู่ใน AndroidManifest.xml) ***/
                    /*int VersionCode = 0;
                    try {
                        PackageManager pm = getPackageManager();
                        PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
                        VersionCode = packageInfo.versionCode;
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }*/
                    /*** [END] :: Fixed - [BHPROJ-0026-3271] [Back-End] เพิ่ม Field VersionCode ใน ตาราง [UserDevice] เพื่อเก็บว่า ณ ขณะนั้นใช้งาน TSR Mobile App version อะไรอยู่ (เป็น VersionCode ที่อยู่ใน AndroidManifest.xml)   ***/

                    /*** [START] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical  ***/
                    inputAddUserDeviceLog = new AddUserDeviceLogInputInfo();
                    inputAddUserDeviceLog.UserName = userName;
                    inputAddUserDeviceLog.DeviceID = deviceID;
                    inputAddUserDeviceLog.ProcessType = userDeviceLogProcessType;

                    //inputAddUserDeviceLog.VersionCode = VersionCode;
                    inputAddUserDeviceLog.VersionCode = BHPreference.appVersionCode();
                    inputAddUserDeviceLog.AndroidApiLevel = BHPreference.androidApiLevel();
                    /*** [END] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***/
                }

                @Override
                protected void calling() {
                    // TODO Auto-generated method stub]
                    try {
                        checkSoapOutput = TSRController.checkSoap();
                        if (checkSoapOutput != null && checkSoapOutput.ResultCode == 0) {

                            output = TSRController.logout(input);

                            /*** [START] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical  ***/
                            if (output != null) {
                                if (output.ResultCode == 0) {
                                    TSRController.addUserDeviceLog(inputAddUserDeviceLog);
                                }
                            }
                            /*** [END] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***/
                        }

                    } catch (Exception e) {
                        Log.i("Logout", "Error");
                    }
                }

                @Override
                protected void after() {
                    // TODO Auto-generated method stub
                    if (output != null) {
                        if (output.ResultCode == 0) {
                            BHPreference.setLastloginID(BHPreference.userID());
                            BHPreference.setServiceMode(null);
                            BHPreference.setUserID(null);
                            BHPreference.setEmployeeID(null);
                            BHPreference.setUserNotAllowLogin(UserController.LoginType.ALLOW.toString());
                            BHPreference.setTimeOutLogin(false);

                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            //dialog.dismiss();
                            checkLogin = false;
                            finish();

                            TimeOutLoginService.cancelAlarm(activity);
                            TimeOutLoginService.cancelAlarmSuspendServiceNotice(activity);
                        } else {
                            //showDialogBox("Logout", "Logout ไม่สำเร็จ กรุณาลองอีกครั้ง");
//                        showMessage("Logout ไม่สำเร็จ กรุณาลองอีกครั้ง");
                            //dialog.dismiss();

                            if (BHPreference.TimeOutLogin()) {
                                TimeOutLoginService.startAlarm(activity);
                            }

                            showDialogBox("Logout", "Logout ไม่สำเร็จ กรุณาลองอีกครั้ง");
                        }
                    } else {
                        //showDialogBox("Logout", "Logout ไม่สำเร็จ กรุณาลองอีกครั้ง");
//                    showMessage("Logout ไม่สำเร็จ กรุณาลองอีกครั้ง");
                        //dialog.dismiss();
                        if (BHPreference.TimeOutLogin()) {
                            TimeOutLoginService.startAlarm(activity);
                        }

                        showDialogBox("Logout", "Logout ไม่สำเร็จ กรุณาลองอีกครั้ง");
                    }
                }

            }).start(true, true);
        }
    }

    public void showDialogBox(String title, String message) {
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        setupAlert.show();
    }

    private void startSyncLogin() {
        TransactionService.stopService(MainActivity.this);
        SynchronizeReceiver synchronizeReceiver = new SynchronizeReceiver();
        synchronizeReceiver.getInstance().start();

        //new BaseController().removeDatabase();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
        request.master.syncFullRelated = true;
//        request.master.syncTeamRelated = true;
//        request.master.syncProductRelated = true;
//        request.master.syncCustomerRelated = true;
//        request.master.syncPaymentRelated = true;
//        request.master.syncContractRelated = true;
//        request.master.syncEditContractRelated = true;
//        request.master.syncSendMoneyRelated = true;
//        request.master.syncMasterDataRelated = true;

        Intent i = new Intent(MainActivity.this, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        MainActivity.this.startService(i);
    }

    public void startSyncLogout() {
        TransactionService.stopService(MainActivity.this);
        SynchronizeReceiver synchronizeReceiver = new SynchronizeReceiver();
        synchronizeReceiver.getInstance().start();

        //new BaseController().removeDatabase();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
        /*request.master.syncTeamRelated = true;
        request.master.syncProductRelated = true;
        request.master.syncCustomerRelated = true;
        request.master.syncPaymentRelated = true;
        request.master.syncContractRelated = true;
        request.master.syncEditContractRelated = true;
        request.master.syncSendMoneyRelated = true;
        request.master.syncMasterDataRelated = true;*/

        Intent i = new Intent(MainActivity.this, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        MainActivity.this.startService(i);
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public void toast(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!isFinishing()) {
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    

    /**********************************************************************************************/
    public void doJob(final Runnable job, final int resId) {
        // Start the job from main thread
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Progress dialog available due job execution
                final ProgressDialog dialog = new ProgressDialog(MainActivity.this);
                dialog.setTitle(getString(R.string.bluetooth_please_wait));
                dialog.setMessage(getString(resId));
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            job.run();
                        } finally {
                            dialog.dismiss();
                        }
                    }
                });
                t.start();
            }
        });
    }

    public void printImage(Bitmap image, PrintHandler handler) {
        printImage(image, 1, handler);
    }


    public void printImage(Bitmap image, int count, PrintHandler handler) {
        Bitmap[] images = new Bitmap[count];
        for (int ii = 0; ii < count; ii++) {
            images[ii] = image;
        }

        printImage(images, handler);
    }



    public synchronized void printText(final List<List<PrintTextInfo>> detailPrint, final PrintHandler handler) {
        if (bhBluetoothPrinter != null) {
            bhBluetoothPrinter.SetPrint(detailPrint, handler);
        }
    }

    /***
     * [START] :: Fixed - [BHPROJ-0026-3275] :: [Android-พิมพ์ใบเสร็จ] การพิมพ์ใบเสร็จกรณีเก็บเงินพร้อมกันหลายงวด
     ***/
    public synchronized void printTextWithInterrupt(final List<List<PrintTextInfo>> detailPrint, final PrintHandler handler) {
        if (bhBluetoothPrinter != null) {
            bhBluetoothPrinter.SetPrintWithInterrupt(detailPrint, handler);
        }
    }

    /***
     * [END] :: Fixed - [Android-พิมพ์ใบเสร็จ] การพิมพ์ใบเสร็จกรณีเก็บเงินพร้อมกันหลายงวด
     ***/


    public synchronized void printImage(final Bitmap[] images, final PrintHandler handler) {

        if (mJob != null) return;

        //ตรวจสอบ Bluetooth
        if (mService.isBTopen() == false) {
            Log.d(LOG_TAG, "Bluetooth Disabled");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            Log.d(LOG_TAG, "Bluetooth Enabled");

            if (mService.getState() != 3) {
                Log.d(LOG_TAG, "Waiting For Connection Printer...");
                Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity2.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }
        }

        mJob = new Runnable() {
            @Override
            public void run() {
                try {
                    ThemalPrintController.SaveImage(MainActivity.this, images);
                    printImage();
                    for (int i = 0; i < images.length; i++) {
                        handler.onBackgroundPrinting(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(e.getLocalizedMessage());
                } finally {
                    mJob = null;

                    if (mService != null)   //close bluetooth
                        mService.stop();
                        /*mService = null;
                        con_dev = null;*/
                }
            }
        };
    }

    public synchronized void newPrintImage(final Bitmap[] images, final PrintHandler handler) {

        if (mJob != null) return;

        //ตรวจสอบ Bluetooth
        if (mService.isBTopen() == false) {
            Log.d(LOG_TAG, "Bluetooth Disabled");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            Log.d(LOG_TAG, "Bluetooth Enabled");

            if (mService.getState() != 3) {
                Log.d(LOG_TAG, "Waiting For Connection Printer...");
                Intent serverIntent = new Intent(MainActivity.this, DeviceListActivity2.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
            }
        }

        mJob = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < images.length; i++) {
                        newPrintImage(images[i]);
                        handler.onBackgroundPrinting(i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(e.getLocalizedMessage());
                } finally {
                    mJob = null;

                    if (mService != null)   //close bluetooth
                        mService.stop();
                        /*mService = null;
                        con_dev = null;*/
                }
            }
        };
    }

    private void printImage() {
//        try {
//
//            String tmpDir = getResources().getString(R.string.folder_picture_tsr);
//
//            File file = new File(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/header_00.png");
//            File fileTxt = new File(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/listTxt_00.txt");
//            int stepFile = 0;
//            NumberFormat numberFormat = new DecimalFormat("##00");
//            String format = numberFormat.format(stepFile);
//
//            while (file.exists() || fileTxt.exists()) {
//
//                if (file.exists()) {
//
//                    String currentPath = Environment.getExternalStorageDirectory() + "/" + tmpDir + "/header_" + format + ".png";
//                    byte[] sendData = null;
//                    PrintPic pg = new PrintPic();
//                    pg.initCanvas(576);
//                    pg.initPaint();
//                    pg.drawImage(0, 0, currentPath);
//                    sendData = pg.printDraw();
//                    mService.write(sendData);
//
//
//                    Thread.sleep(2000);
//
//                    file = new File(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/receiptDetail1_" + format + ".txt");
//                    //ถ้า มีไฟล์ txt ถึงจะทำการปริ้น แบบ แบ่งส่วนเป็น หัว ลำตัว และท้าย
//                    if (file.exists()) {
//                        mService.sendMessage(ThemalPrintController.readText(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/receiptDetail1_" + format + ".txt"), "TIS-620");
//
//                        Thread.sleep(2000);
//
//                        currentPath = Environment.getExternalStorageDirectory() + "/" + tmpDir + "/detail_" + format + ".png";
//                        sendData = null;
//                        pg = new PrintPic();
//                        pg.initCanvas(576);
//                        pg.initPaint();
//                        pg.drawImage(0, 0, currentPath);
//                        sendData = pg.printDraw();
//                        mService.write(sendData);
//
//                        Thread.sleep(2000);
//
//                        mService.sendMessage(ThemalPrintController.readText(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/receiptDetail2_" + format + ".txt"), "TIS-620");
//
//
//                        Thread.sleep(2000);
//
//
//                        currentPath = Environment.getExternalStorageDirectory() + "/" + tmpDir + "/tailer_" + format + ".png";
//                        sendData = null;
//                        pg = new PrintPic();
//                        pg.initCanvas(576);
//                        pg.initPaint();
//                        pg.drawImage(0, 0, currentPath);
//                        sendData = pg.printDraw();
//                        mService.write(sendData);
//                    }
//                } else if (fileTxt.exists()) {
//                    if (mService != null) {
//                        List<String> _ListReadText = ThemalPrintController.readTextList(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/listTxt_" + format + ".txt");
//                        ThemalPrintController themalPrintController = new ThemalPrintController(mService);
//
//
//                        for (int i = 2; i < _ListReadText.size(); i++) {
//                            if (i == 2) {
//                                themalPrintController.setFontBold();
//                                themalPrintController.sendThaiMessage(_ListReadText.get(0));
//                                themalPrintController.sendThaiMessage(_ListReadText.get(1));
//                                themalPrintController.sendThaiMessage(_ListReadText.get(2));
//                                themalPrintController.setFontNormal();
//                            } else {
//                                themalPrintController.sendThaiMessage(_ListReadText.get(i));
//                            }
//                        }
//                        themalPrintController.sendThaiMessage("");
//
//                        Thread.sleep(2000);
//
//                    } else {
//                        Toast.makeText(getApplicationContext(), "ไม่พบเครื่องปริ้น", Toast.LENGTH_LONG).show();
//                        break;
//                    }
//                }
//                //update variable zone
//                stepFile++;
//                format = numberFormat.format(stepFile);
//                file = new File(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/header_" + format + ".png");
//                fileTxt = new File(Environment.getExternalStorageDirectory() + "/" + tmpDir + "/listTxt_" + format + ".txt");
//
//            }
//
//            ThemalPrintController.delTempPrint(getApplicationContext());  //remove pic on sd card
//
//            MainActivity.this.runOnUiThread(new Runnable() {
//                public void run() {
//                    Toast.makeText(getApplicationContext(), "Print Complete",
//                            Toast.LENGTH_SHORT).show();
//                }
//            });
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    private void newPrintImage(Bitmap bitmap) {
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(576);
        pg.initPaint();
        pg.drawImage(0, 0, bitmap);
        sendData = pg.printDraw();
        mService.write(sendData);
    }

    /**********************************************************************************************/



    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {

                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public interface IApiAccessResponse {
        void postResult(String asyncresult);
    }

    public static class MyBroadcastReceiver extends BroadcastReceiver {
        public static String ACTION_NAME = "th.co.thiensurat.mybroadcastreceiver";
        private MainActivity activity;

        public MyBroadcastReceiver() {

        }

        public MyBroadcastReceiver(MainActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            activity.showMessage("ระบบกำลังทำการ Logout");
//            Toast.makeText(context, "ระบบกำลังทำการ Logout",
//                    Toast.LENGTH_LONG).show();
            // Vibrate the mobile phone
            Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(2000);
            //activity.logout();
            activity.checkLogout = true;
            activity.startSyncLogout();
        }
    }

    private static class MainMenuInfo {
        public DeviceMenuInfo menu;
        public boolean hasSubMenu;
        public int iconID;

        private static List<MainMenuInfo> from(List<DeviceMenuInfo> menus, Map<String, Integer> menuIcons) {
            List<MainMenuInfo> result = new ArrayList<MainMenuInfo>();
            for (DeviceMenuInfo m : menus) {
                MainMenuInfo menu = new MainMenuInfo();
                menu.menu = m;
                menu.hasSubMenu = m.SubMenus != null;

                int icon = R.drawable.ic_tsr_logo;
                if (menuIcons.containsKey(m.MenuID)) {
                    icon = menuIcons.get(m.MenuID);
                }
                menu.iconID = icon;
                result.add(menu);
                if (menu.hasSubMenu && m.IsExpanded) {
                    result.addAll(from(m.SubMenus, menuIcons));
                }
            }

            return result;
        }

//		public static List<MainMenuInfo> from(MenuInfo[] menus) {
//			List<MainMenuInfo> result = new ArrayList<MainMenuInfo>();
//			from(menus, result, 0);
//			return result;
//		}
//
//		private static void from(MenuInfo[] menus, List<MainMenuInfo> mainMenus, int parentID) {
//			for (MenuInfo menu : menus) {
//				MainMenuInfo mainMenu = new MainMenuInfo();
//				mainMenu.menu = menu;
//				mainMenu.hasSubMenu = menu.subMenus != null;
//				mainMenu.parentTitleID = parentID;
//
//				mainMenus.add(mainMenu);
//				if (mainMenu.hasSubMenu && !mainMenu.menu.isCollapse) {
//					from(menu.subMenus, mainMenus, menu.titleID);
//				}
//			}
//		}
    }

    public static class MainMenuAdapter extends BHArrayAdapter<MainMenuInfo> {
        private List<DeviceMenuInfo> originalMenu;
        private Map<String, Integer> icons;

        public MainMenuAdapter(Context context, int resource, List<DeviceMenuInfo> objects) {
            super(context, resource, (List<MainMenuInfo>) null);
            // TODO Auto-generated constructor stub
            icons = getIcons(context);
            originalMenu = objects;
            originalList = MainMenuInfo.from(objects, icons);
            filterList = originalList;
        }

        private Map<String, Integer> getIcons(Context context) {
            final Resources r = context.getResources();
            Map<String, Integer> menuIcons = new HashMap<String, Integer>() {
                {
                    /*** [START] :: Fixed - [BHPROJ-0024-2038 Save] :: [Android-MenuIcon-Report] เปลี่ยนรูป Menu icon ของรายงานต่าง ๆ ***/
                    put(r.getString(R.string.main_menu_default), R.drawable.ic_menu_report);
                    put(r.getString(R.string.main_menu_report_daily_summary), R.drawable.ic_menu_report);                   // รายงานสรุปยอดขาย - ReportDashboard (SALE)
                    put(r.getString(R.string.main_menu_report_zone), R.drawable.ic_menu_report);                            // รายงานพื้นที่ขาย - ReportSaleArea (SALE)
                    put(r.getString(R.string.main_menu_report_writeoff_npl), R.drawable.ic_menu_report);                    // รายงานตัดสัญญาค้าง - ReportWriteOffNPL (SALE)
                    put(r.getString(R.string.main_menu_report_trade_product), R.drawable.ic_menu_report);                   // รายงานเครื่องเทิร์น - ReportTradeInProduct (SALE)
                    put(r.getString(R.string.main_menu_report_re_print), R.drawable.ic_menu_report);                        // รายงานการพิมพ์เอกสารที่มีการ Re-Print - ReportRePrintDocument (SALE + CREDIT)
                    put(r.getString(R.string.main_menu_report_product), R.drawable.ic_menu_report);                         // รายงานยอดสินค้าคงเหลือ - ReportInventory (SALE)
                    put(r.getString(R.string.main_menu_report_payment_sendmoney), R.drawable.ic_menu_report);               // รายงานเก็บเงินงวดแรก/ถัดไป - ReportPaymentAndSendMoney (SALE)
                    put(r.getString(R.string.main_menu_report_payment_appointment), R.drawable.ic_menu_report);             // รายงานนัดเก็บเงินงวดแรก - ReportPaymentAppointment (SALE)
                    put(r.getString(R.string.main_menu_report_install_payment), R.drawable.ic_menu_report);                 // รายงานยอดติดตั้ง/ขายสินค้า - ReportInstallAndPayment (SALE)
                    put(r.getString(R.string.main_menu_report_impound_product), R.drawable.ic_menu_report);                 // รายงานถอดเครื่อง - ReportImpoundProduct (SALE + CREDIT)
                    put(r.getString(R.string.main_menu_report_emp_team), R.drawable.ic_menu_report);                        // รายงานโครงสร้างพนักงาน - ReportSaleAndDriver (SALE + CREDIT)
                    put(r.getString(R.string.main_menu_report_credit_sendmoney), R.drawable.ic_menu_report);                // รายงานสรุปการส่งเงิน - ReportSendMoney (CREDIT)
                    put(r.getString(R.string.main_menu_report_credit_saleaudit), R.drawable.ic_menu_report);                // รายงานการตรวจสอบการ์ดงวดแรก - ReportSaleAudit (CREDIT)
                    put(r.getString(R.string.main_menu_report_credit_next_payment), R.drawable.ic_menu_report);             // รายงานการเก็บเงินค่างวด - ReportNextPayment (CREDIT)
                    put(r.getString(R.string.main_menu_report_credit_contract_close_account), R.drawable.ic_menu_report);   // รายงานตัดสด - ReportContractCloseAccount (CREDIT)
                    put(r.getString(R.string.main_menu_report_complain), R.drawable.ic_menu_report);                        // รายงานสรุปการแจ้งปัญหา - ReportComplain (SALE + CREDIT)
                    put(r.getString(R.string.main_menu_report_change_product), R.drawable.ic_menu_report);                  // รายงานเปลี่ยนเครื่อง - ReportChangeProduct (SALE + CREDIT)
                    put(r.getString(R.string.main_menu_report_change_contract), R.drawable.ic_menu_report);                 // รายงานเปลี่ยนสัญญา - ReportChangeContract (SALE + CREDIT)
                    put(r.getString(R.string.main_menu_report_approved), R.drawable.ic_menu_report);                        // รายงานขออนุมัติ - ReportApproved  (SALE + CREDIT)
                    /*** [END] :: Fixed - [BHPROJ-0024-2038 Save] :: [Android-MenuIcon-Report] เปลี่ยนรูป Menu icon ของรายงานต่าง ๆ ***/

                    put(r.getString(R.string.main_menu_team), R.drawable.ic_menu_team);
                    put(r.getString(R.string.main_menu_checkstock), R.drawable.ic_menu_checkstock);
                    put(r.getString(R.string.main_menu_sales), R.drawable.ic_menu_sales);
                    put(r.getString(R.string.main_menu_money), R.drawable.ic_menu_money);
                    put(r.getString(R.string.main_menu_first), R.drawable.ic_menu_first);
                    put(r.getString(R.string.main_menu_other), R.drawable.ic_menu_fix_request);     //-- Fixed - [BHPROJ-0026-3184] :: Change ic_tsr_logo to be new menu icon
                    put(r.getString(R.string.main_menu_logout), R.drawable.ic_menu_logout);

                    put(r.getString(R.string.main_menu_remove), R.drawable.ic_menu_remove);
                    put(r.getString(R.string.main_menu_change), R.drawable.ic_menu_change);
                    put(r.getString(R.string.main_menu_document), R.drawable.ic_menu_document);
                    put(r.getString(R.string.main_menu_loss), R.drawable.ic_menu_loss);
                    put(r.getString(R.string.main_menu_next_period), R.drawable.ic_menu_next_period);
                    put(r.getString(R.string.main_menu_total), R.drawable.ic_menu_total);
                    put(r.getString(R.string.main_menu_report_daily_summary_sale_by_product_online), R.drawable.ic_menu_total);
                    put(r.getString(R.string.main_menu_brochure), R.drawable.ic_menu_brochure);
                    put(r.getString(R.string.main_menu_complain), R.drawable.ic_menu_complain);
                    put(r.getString(R.string.main_menu_return_product), R.drawable.ic_menu_checkstock);
                    put(r.getString(R.string.main_menu_refresh), R.drawable.ic_menu_refresh);
                    put(r.getString(R.string.main_menu_edit_contracts), R.drawable.ic_menu_edit_contracts);

                    //credit
                    put(r.getString(R.string.main_menu_credit_employee_detail), R.drawable.ic_menu_credit_team);
                    put(r.getString(R.string.main_menu_credit_import_credit_data), R.drawable.ic_menu_import_credit);
                    put(r.getString(R.string.main_menu_credit_import_audit_data), R.drawable.ic_menu_import_saleaudit);             //-- Fixed - [BHPROJ-0026-3184] :: Change ic_tsr_logo to be new menu icon
                    put(r.getString(R.string.main_menu_credit_audit_check_customers), R.drawable.ic_menu_sale_audit);
                    put(r.getString(R.string.main_menu_credit_salepaymentperiod_credit_customers), R.drawable.ic_menu_next_period);
                    put(r.getString(R.string.main_menu_credit_cut_off_contract), R.drawable.ic_menu_cutoff_contract);
                    put(r.getString(R.string.main_menu_credit_cut_divisor_contract), R.drawable.ic_menu_cutdivisor_contract);
                    put(r.getString(R.string.main_menu_credit_contract_details), R.drawable.ic_menu_contract_detail);       //-- Fixed - [BHPROJ-0026-771]
                    put(r.getString(R.string.main_menu_credit_sort_order_default_audit), R.drawable.ic_menu_orderexp_saleaudit);    //-- Fixed - [BHPROJ-0026-3184] :: Change ic_tsr_logo to be new menu icon
                    put(r.getString(R.string.main_menu_credit_sort_order_default_credit), R.drawable.ic_menu_orderexp_credit);      //-- Fixed - [BHPROJ-0026-3184] :: Change ic_tsr_logo to be new menu icon
                }
            };

            return menuIcons;
        }

        public void refresh() {
            filterList = MainMenuInfo.from(originalMenu, icons);
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, MainMenuInfo info) {
            // TODO Auto-generated method stub
            ViewHolder vh = (ViewHolder) holder;
            vh.imgToggle.setVisibility(View.GONE);
            vh.imgIcon.setImageResource(info.iconID);
            vh.txtTitle.setText(info.menu.MenuName);
            if (info.hasSubMenu) {
                view.setBackgroundResource(R.drawable.selector_main_menu);
                vh.imgToggle.setVisibility(View.VISIBLE);
                if (!info.menu.IsExpanded) {
                    vh.imgToggle.setImageResource(R.drawable.ic_toggle_on);
                } else {
                    vh.imgToggle.setImageResource(R.drawable.ic_toggle_off);
                }
            } else {
                view.setBackgroundResource(R.drawable.selector_main_menu_sub);
            }

            if (MainActivity.selectedMenu == position) {
                view.setBackgroundResource(R.drawable.selector_main_menu);
            }
        }

        class ViewHolder {
            public ImageView imgIcon;
            public TextView txtTitle;
            public ImageView imgToggle;
        }
    }

    public static class PrintHandler {
        public void onBackgroundPrinting(int index) {
        }

        public void onPrinting(int index) {
        }

        public void onPrintCompleted() {

        }
    }
    //endregion

    //region LoadImage
    public static class LoadImageToImageView extends AsyncTask<String, String, Bitmap> {
        Bitmap bitmap;
        ProgressDialog pDialog;

        ContractImageInfo contractImageInfo;
        ImageView imageView;

        public LoadImageToImageView(ContractImageInfo contractImageInfo, ImageView imageView) {
            this.contractImageInfo = contractImageInfo;
            this.imageView = imageView;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.activity);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.setTitle("Connecting To Server");
            pDialog.setMessage("Loading Image ....");
            pDialog.show();

        }

        protected Bitmap doInBackground(String... args) {

            try {
                int timeOutImage = 2000;//TimeOut 2s
                HttpGet httpGet = new HttpGet(args[0]);
                HttpClient client = new DefaultHttpClient();

                HttpParams params = client.getParams();
                HttpConnectionParams.setConnectionTimeout(params, timeOutImage);
                HttpConnectionParams.setSoTimeout(params, timeOutImage);

                HttpResponse response = client.execute(httpGet);

                int statusCode = response.getStatusLine().getStatusCode();

                if (statusCode == 200) {
                    bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @SuppressLint("WrongThread")
        protected void onPostExecute(Bitmap image) {

            if (image != null) {

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] b = stream.toByteArray();
                String temp = Base64.encodeToString(b, Base64.DEFAULT);

                contractImageInfo.ImageData = temp;


                TSRController.saveContractImage(contractImageInfo);
                imageView.setImageBitmap(image);
                pDialog.dismiss();

            } else {
                imageView.setImageResource(R.drawable.no_image);
                pDialog.dismiss();

                Builder setupAlert;
                setupAlert = new AlertDialog.Builder(activity)
                        .setTitle("Connecting To Server")
                        .setMessage("เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้")
                        .setCancelable(false)
                        .setNegativeButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.cancel();
                            }
                        });
                setupAlert.show();
            }
        }
    }

    public static class DownloadProgressUpdateInfo {
        String message;
        int progress;
        int progressMax;
        String progressNumberFormat;
        boolean indeterminate;
    }

    public static class DownloadResultInfo {
        int ResultCode;
        String ResultDescription;
        String url;
    }

    // Load DB.Zip
    public static class DownloadTask extends AsyncTask<String, DownloadProgressUpdateInfo, DownloadResultInfo> {

        public ProgressDialog mProgressDialog;
        public IApiAccessResponse delegate = null;
        private Context context;
        private PowerManager.WakeLock mWakeLock;

        public DownloadTask(Context context) {
            this.context = context;
            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Download ฐานข้อมูล");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setProgressNumberFormat("");
        }

        private boolean DownloadFileFromServer(URL url, String pathFile, int reConnect, DownloadProgressUpdateInfo progressUpdateInfo) {
           return DownloadFileFromServer(url, pathFile, reConnect, 5000, progressUpdateInfo);
        }

        private boolean DownloadFileFromServer(final URL url, final String pathFile, final int reConnect , final int timeout, final DownloadProgressUpdateInfo progressUpdateInfo) {
            HttpURLConnection connection = null;
            boolean downloadSuccess = false;

            try {
                long downloadFile = 0;

                File file = new File(pathFile);
                if(file.exists()){
                    downloadFile = file.length();
                }
                connection = (HttpURLConnection) url.openConnection();
                connection.setConnectTimeout(timeout);
                connection.setReadTimeout(timeout);
                connection.setRequestProperty("Range", "bytes=" + downloadFile + "-");
                connection.connect();

                // download the file
                final int fileLength = connection.getContentLength();


                try (OutputStream output = (downloadFile == 0) ? new FileOutputStream(pathFile) : new FileOutputStream(pathFile, true);
                      BufferedInputStream in = new BufferedInputStream(connection.getInputStream());
                      BufferedOutputStream out = new BufferedOutputStream(output)){

                    byte[] data = new byte[1024 * 1024 * 4];
                    int count;

                    //long startTimeMS = System.currentTimeMillis();
                    while ((count = in.read(data)) != -1) {
                        downloadFile += count;

                        int percent = (int) (downloadFile * 100 / fileLength);

                        progressUpdateInfo.progress = percent > 100 ? 100 : percent;
                        progressUpdateInfo.progressNumberFormat = (new BHUtilities().Bytes2String(downloadFile)) + "/" + (new BHUtilities().Bytes2String(fileLength));
                        publishProgress(progressUpdateInfo);

                        out.write(data, 0, count);

                        /*long endTimeMS = System.currentTimeMillis();
                        //long offsetTime = endTimeMS - startTimeMS;

                        double timeTakenMills = Math.floor(endTimeMS - startTimeMS);  // time taken in milliseconds
                        double timeTakenSecs = timeTakenMills / 1000;  // divide by 1000 to get time in seconds
                        int kilobytePerSec = (int) Math.round(data.length / timeTakenSecs);
                        data = new byte[kilobytePerSec];
                        startTimeMS = endTimeMS;

                        //Log.d("endTimeMS - startTimeMS", String.valueOf(offsetTime));
                        //Log.d("buffer", String.valueOf(data.length));*/

                    }
                    //Log.d("555", "DownloadFileFromServer");
                    downloadSuccess = true;
                }

                /*try ( InputStream input = connection.getInputStream();
                      OutputStream output = (downloadFile == 0) ? new FileOutputStream(pathFile) : new FileOutputStream(pathFile, true)) {
                    byte data[] = new byte[1024];
                    int count;
                    while ((count = input.read(data)) != -1) {
                        downloadFile += count;

                        int percent = (int) (downloadFile * 100 / fileLength);
                    *//*if (fileLength > 0) // only if total length is known
                        publishProgress(percent > 99 ? 99 : percent);*//*

                        progressUpdateInfo.progress = percent > 100 ? 100 : percent;
                        progressUpdateInfo.progressNumberFormat = (BHUtilities.Bytes2String(downloadFile)) + "/" + (BHUtilities.Bytes2String(fileLength));
                        publishProgress(progressUpdateInfo);

                        output.write(data, 0, count);
                    }

                    downloadSuccess = true;
                }*/
            }catch (Exception e){

            } finally {
                if (connection != null)
                    connection.disconnect();
            }

            if (downloadSuccess) {
                return  true;
            } else {
                if (reConnect == 3) {
                    return false;
                } else {
                    return DownloadFileFromServer(url, pathFile, reConnect + 1, timeout, progressUpdateInfo);
                }
            }
        }

        private String unzip(String zipFile, String location, DownloadProgressUpdateInfo progressUpdateInfo) throws IOException {
            try {
                File f = new File(location);
                if(!f.isDirectory()) {
                    f.mkdirs();
                }
                //ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile));
                try (ZipInputStream zin = new ZipInputStream(new FileInputStream(zipFile)))
                {
                    long fileLength = 0;
                    long saveFile = 0;

                    ZipEntry ze = null;
                    while ((ze = zin.getNextEntry()) != null) {
                        String newFileName = ze.getName() + ".temp";
                        String path = location + "/" + newFileName;

                        if (ze.isDirectory()) {
                            File unzipFile = new File(path);
                            if(!unzipFile.isDirectory()) {
                                unzipFile.mkdirs();
                            }
                        }
                        else {
                            fileLength = ze.getSize();
                            /*FileOutputStream fout = new FileOutputStream(path, false);

                            BufferedInputStream in = new BufferedInputStream(zin);
                            BufferedOutputStream out = new BufferedOutputStream(fout);*/


                            try ( FileOutputStream fout = new FileOutputStream(path, false);
                                  //BufferedInputStream in = new BufferedInputStream(zin);
                                  BufferedOutputStream out = new BufferedOutputStream(fout))
                            {
                                BufferedInputStream in = new BufferedInputStream(zin);

                                /*for (int c = zin.read(); c != -1; c = zin.read()) {

                                    saveFile += 1;
                                    int percent = (int) (saveFile * 100 / fileLength);
                                    progressUpdateInfo.progress = percent > 100 ? 100 : percent;
                                    progressUpdateInfo.progressNumberFormat = (bytes2String(saveFile)) + "/" + (bytes2String(fileLength));
                                    publishProgress(progressUpdateInfo);

                                    fout.write(c);
                                }*/

                                //final byte[] buffer = new byte[1024 * 1024 * 4];
                                byte[] buffer = new byte[1024];
                                int length;

                                /*while ((length = zin.read(buffer)) > 0) {
                                    saveFile += length;

                                    int percent = (int) (saveFile * 100 / fileLength);

                                    progressUpdateInfo.progress = percent > 100 ? 100 : percent;
                                    progressUpdateInfo.progressNumberFormat = (BHUtilities.Bytes2String(saveFile)) + "/" + (BHUtilities.Bytes2String(fileLength));
                                    publishProgress(progressUpdateInfo);


                                    fout.write(buffer, 0, length);
                                }*/

                                long startTimeMS = System.currentTimeMillis();

                                while ((length = in.read(buffer)) != -1) {
                                    saveFile += length;

                                    int percent = (int) (saveFile * 100 / fileLength);

                                    progressUpdateInfo.progress = percent > 100 ? 100 : percent;
                                    progressUpdateInfo.progressNumberFormat = (new BHUtilities().Bytes2String(saveFile)) + "/" + (new BHUtilities().Bytes2String(fileLength));
                                    publishProgress(progressUpdateInfo);


                                    out.write(buffer, 0, length);

                                    long endTimeMS = System.currentTimeMillis();
                                    //long offsetTime = endTimeMS - startTimeMS;

                                    //buffer = new byte[(int)((length / offsetTime) * 500)];
                                    double timeTakenMills = Math.floor(endTimeMS - startTimeMS);  // time taken in milliseconds
                                    double timeTakenSecs =  timeTakenMills / 1000; // divide by 1000 to get time in seconds
                                    if (timeTakenSecs <= 0) {
                                        timeTakenSecs = 1 / 1000;
                                    }
                                    int kilobytePerSec = (int) Math.round(length / timeTakenSecs);
                                    if (kilobytePerSec <= 0) {
                                        kilobytePerSec = 1024;
                                    }
                                    buffer = new byte[kilobytePerSec];
                                    startTimeMS = endTimeMS;

                                    //Log.d("kilobytePerSec", new BHUtilities().Bytes2String(kilobytePerSec));
                                    //Log.d("endTimeMS - startTimeMS", String.valueOf(offsetTime));
                                    //Log.d("buffer", String.valueOf(buffer.length));
                                }

                                /*File oldfile =new File(path);
                                File newfile =new File(path.replace(".temp", ""));

                                *//*if (newfile.exists()) {
                                    newfile.delete();
                                }*//*
                                new BaseController().removeDatabase();

                                if(oldfile.renameTo(newfile)){
                                    System.out.println("Rename succesful");
                                }else{
                                    System.out.println("Rename failed");
                                }*/

                                zin.closeEntry();

                                return  newFileName;
                            }
                            /*finally {
                                fout.close();

                                in.close();
                                out.close();
                            }*/
                        }
                    }
                }
                /*finally {
                    zin.close();
                }*/
            }
            catch (IOException  e) {
                //e.printStackTrace();
                throw e;
            }

            return null;
        }

        private void ReCheckSqliteData(String strFile) {

            String backupDBName = null;
            Map<String, String> tempLog = null;
            boolean isCheckSqliteStep3 = false;
            Date backupDate = new Date();

            List<TransactionLogInfo> transactionLogList = TSRController.getAllTransactionLog();
            if (transactionLogList != null && transactionLogList.size() > 0) {

                //Step 1 ตรวจสอบข้อมูล TransactionLog ถ้ายังมี SyncStatus = False อยู่แสดงว่ามีการส่งข้อมูลไปยัง server ไม่สำเร็จ
                boolean step1 = CheckSqliteStep1(transactionLogList);
                if (!step1) {
                    //SyncStatus = False
                    backupDBName = BackUpSqlite(isCheckSqliteStep3, backupDate);

                    //Back Up Log การทำงานของ DB ก้อนเก่าทั้งหมดขึ้น Server
                    TSRController.addTransactionLogBackup(transactionLogList, BHPreference.userDeviceId(), backupDate, BHPreference.employeeID());
                } else {
                    //SyncStatus = True
                    //Step 2 ทำการค้นหา ServiceName ที่จะนำมาใช้ตรวจสอบ พร้อม Id
                    tempLog = CheckSqliteStep2(transactionLogList);
                    if (tempLog != null && tempLog.size() > 0) {
                        isCheckSqliteStep3 = true;
                        backupDBName = BackUpSqlite(isCheckSqliteStep3, backupDate);

                        //ต้องทำการตรวจสอบใน Step 3 ต่อ หลังจากบันทึก Sqlite ก้อนใหม่แล้ว
                    }
                }
            }

            RenameAndRemoveOldSqlite(strFile);

            if (isCheckSqliteStep3) {
                //Step 3 ตรวจสอบกับข้อมูลใหม่ว่ามี Id นี้อยู่หรือไหม
                boolean step3 = CheckSqliteStep3(tempLog);
                if (!step3) {
                    ClearBackUpSqlite(isCheckSqliteStep3);

                    //Back Up Log การทำงานของ DB ก้อนเก่าทั้งหมดขึ้น Server
                    TSRController.addTransactionLogBackup(transactionLogList,  BHPreference.userDeviceId(), backupDate, BHPreference.employeeID());
                } else {
                    if (backupDBName != null && !backupDBName.isEmpty()) {
                        //Step 3.1 ตรวจสอบข้อมูลสำเร็จ ไม่พบปัญหาทำการการลบข้อมูล DB Backup
                        String strPathBackup = BHStorage.getFolder(BHStorage.FolderType.Database) + "/" + backupDBName;
                        File fileBackup = new File(strPathBackup);
                        if (fileBackup.exists()) {
                            fileBackup.delete();
                        }
                    }
                }
            }
        }

        private boolean CheckSqliteStep1(List<TransactionLogInfo> transactionLogList) {
            if (transactionLogList != null) {
                for (TransactionLogInfo info : transactionLogList) {
                    if (!info.SyncStatus) {
                        return false;
                    }
                }
            }

            return true;
        }

        private Map<String, String> CheckSqliteStep2(List<TransactionLogInfo> transactionLogList) {
            if (transactionLogList != null) {
                Gson gson = new Gson();
                Map<String, String> oldTransactionLog = new LinkedHashMap<>();

                for (TransactionLogInfo info : transactionLogList) {
                    if (info.ServiceName != null && info.ServiceInputData != null && info.ServiceInputType != null) {
                        switch (info.ServiceName) {
                            case "AddContract":
                                AddContractInputInfo addContractInputInfo = gson.fromJson(info.ServiceInputData, AddContractInputInfo.class);

                                String addContractInputInfo_RefNo = addContractInputInfo.RefNo;
                                if (addContractInputInfo_RefNo != null && !oldTransactionLog.containsKey(addContractInputInfo_RefNo)) {
                                    oldTransactionLog.put(addContractInputInfo_RefNo, "AddContract");
                                }
                                break;

                            case "AddReceipt":
                                AddReceiptInputInfo addReceiptInputInfo = gson.fromJson(info.ServiceInputData, AddReceiptInputInfo.class);

                                String addReceiptInputInfo_ReceiptID = addReceiptInputInfo.ReceiptID;
                                if (addReceiptInputInfo_ReceiptID != null  && !oldTransactionLog.containsKey(addReceiptInputInfo_ReceiptID)) {
                                    oldTransactionLog.put(addReceiptInputInfo_ReceiptID, "AddReceipt");
                                }
                                break;

                            case "AddPayment":
                                AddPaymentInputInfo addPaymentInputInfo = gson.fromJson(info.ServiceInputData, AddPaymentInputInfo.class);

                                String addPaymentInputInfo_PaymentID = addPaymentInputInfo.PaymentID;
                                if (addPaymentInputInfo_PaymentID != null && !oldTransactionLog.containsKey(addPaymentInputInfo_PaymentID)) {
                                    oldTransactionLog.put(addPaymentInputInfo_PaymentID, "AddPayment");
                                }
                                break;

                            case "DeleteContract":
                                DeleteContractInputInfo deleteContractInputInfo = gson.fromJson(info.ServiceInputData, DeleteContractInputInfo.class);

                                String deleteContractInputInfo_RefNo = deleteContractInputInfo.RefNo;
                                if (deleteContractInputInfo_RefNo != null && oldTransactionLog.containsKey(deleteContractInputInfo_RefNo)) {
                                    oldTransactionLog.remove(deleteContractInputInfo_RefNo);
                                }
                                break;

                            case "AddTransactionLogSkip":
                                AddTransactionLogSkipInputInfo addTransactionLogSkipInputInfo = gson.fromJson(info.ServiceInputData, AddTransactionLogSkipInputInfo.class);

                                if (addTransactionLogSkipInputInfo != null
                                        && addTransactionLogSkipInputInfo.ServiceName != null
                                        && addTransactionLogSkipInputInfo.ServiceInputData != null) {
                                    switch (addTransactionLogSkipInputInfo.ServiceName) {
                                        case "AddContract":
                                            AddContractInputInfo skip_addContractInputInfo = gson.fromJson(addTransactionLogSkipInputInfo.ServiceInputData, AddContractInputInfo.class);

                                            String skip_addContractInputInfo_RefNo = skip_addContractInputInfo.RefNo;
                                            if (skip_addContractInputInfo_RefNo != null && oldTransactionLog.containsKey(skip_addContractInputInfo_RefNo)) {
                                                oldTransactionLog.remove(skip_addContractInputInfo_RefNo);
                                            }
                                            break;

                                        case "AddReceipt":
                                            AddReceiptInputInfo skip_addReceiptInputInfo = gson.fromJson(addTransactionLogSkipInputInfo.ServiceInputData, AddReceiptInputInfo.class);

                                            String skip_addReceiptInputInfo_ReceiptID = skip_addReceiptInputInfo.ReceiptID;
                                            if (skip_addReceiptInputInfo_ReceiptID != null  && oldTransactionLog.containsKey(skip_addReceiptInputInfo_ReceiptID)) {
                                                oldTransactionLog.remove(skip_addReceiptInputInfo_ReceiptID);
                                            }
                                            break;

                                        case "AddPayment":
                                            AddPaymentInputInfo skip_addPaymentInputInfo = gson.fromJson(addTransactionLogSkipInputInfo.ServiceInputData, AddPaymentInputInfo.class);

                                            String skip_addPaymentInputInfo_PaymentID = skip_addPaymentInputInfo.PaymentID;
                                            if (skip_addPaymentInputInfo_PaymentID != null && oldTransactionLog.containsKey(skip_addPaymentInputInfo_PaymentID)) {
                                                oldTransactionLog.remove(skip_addPaymentInputInfo_PaymentID);
                                            }
                                            break;

                                        default:
                                            break;

                                    }
                                }
                                break;

                            default:
                                break;

                        }
                    }
                }

                return oldTransactionLog;
            }

            return null;
        }

        private boolean CheckSqliteStep3(Map<String, String> tempLog) {

            if (tempLog != null && tempLog.size() > 0) {
                Iterator<String> oldTransactionLogIterator = tempLog.keySet().iterator();

                while (oldTransactionLogIterator.hasNext()) {
                    String id = (oldTransactionLogIterator.next());  // Key
                    String serviceName = tempLog.get(id); // Value

                    if (id != null && serviceName != null) {
                        switch (serviceName) {
                            case "AddContract":
                                ContractInfo contractInfo = TSRController.getContractByRefNo(id);

                                if (contractInfo == null) {
                                    return false;
                                }
                                break;

                            case "AddReceipt":
                                ReceiptInfo receiptInfo = TSRController.getReceiptByReceiptID(id);

                                if (receiptInfo == null) {
                                    return false;
                                }
                                break;

                            case "AddPayment":
                                PaymentInfo paymentInfo = TSRController.getPaymentByPaymentID(id);

                                if (paymentInfo == null) {
                                    return false;
                                }
                                break;

                            default:
                                break;

                        }
                    }
                }
            }

            return true;
        }

        private String BackUpSqlite(boolean isCheckSqliteStep3, Date backupDate) {

            String dbName = DatabaseManager.getInstance().getDatabaseName();
            String backupDBName = String.format("%s_%s.%s", BHPreference.employeeID()
                                                            , BHUtilities.dateFormat(backupDate, BHGeneral.BACKUP_DATE_FORMAT, BHUtilities.LOCALE_EN)
                                                            , BHGeneral.BACKUP_TYPE_FILE); //employeeID_yyyy-MM-dd-HH-mm-ss.backup
            String pathDB = BHStorage.getFolder(BHStorage.FolderType.Database);

            String fromPath = pathDB + "/" + dbName;
            String toPath = pathDB + "/" + backupDBName;

            if (!isCheckSqliteStep3) {
                ClearBackUpSqlite(isCheckSqliteStep3);
            }
            RenameSqlite(fromPath, toPath);

            return backupDBName;
        }

        private void ClearBackUpSqlite(boolean isCheckSqliteStep3) {
            int offset = isCheckSqliteStep3 ? 1 : 0;
            String pathDB = BHStorage.getFolder(BHStorage.FolderType.Database);

            File dir = new File(pathDB);

            File[] fileList = dir.listFiles();
            List<File> newFileList = new ArrayList<>();

            //ดึงข้อมูล .backup
            if (fileList != null && fileList.length > 0) {
                for (File f : fileList) {
                    if(f.getName().endsWith(String.format(".%s", BHGeneral.BACKUP_TYPE_FILE))) {

                        //ตรวจสอบไฟล์ว่าสามารถดึงวันที่ได้
                        if (GetDateByFileName(f) != null) {
                            newFileList.add(f);
                        }
                    }
                }
            }


            if (newFileList != null
                    && newFileList.size() > 0
                    && newFileList.size() >= BHGeneral.BACKUP_MAXIMUM_FILE + offset) {

                //เรียงลำดับวันที่ ตามชื่อไฟล์
                Collections.sort(newFileList, new Comparator<File>() {
                    public int compare(File f1, File f2) {

                        Date d1 = GetDateByFileName(f1);
                        Date d2 = GetDateByFileName(f2);

                        if (d1 == null || d2 == null) {
                            return 0;
                        }

                        //DESC
                        return d2.compareTo(d1);
                    }
                });

                //ลบไฟล์ที่เกิน Maximum ออก
                for (int i = newFileList.size(); i >= BHGeneral.BACKUP_MAXIMUM_FILE + offset; i--){
                    File f = newFileList.get(i - 1);
                    if (f.exists()) {
                        f.delete();
                    }
                }
            }
        }

        private Date GetDateByFileName(File f) {
            String fullName = f.getName();
            if (fullName != null && !fullName.isEmpty()) {

                String[] splitFullName = fullName.split("\\.");
                if (splitFullName != null && splitFullName.length == 2) {

                    String fileName = splitFullName[0];
                    if (fileName != null && !fileName.isEmpty()) {

                        String[] splitFileName = fileName.split("_");
                        if (splitFileName != null && splitFileName.length == 2) {

                            String strDate = splitFileName[1];
                            if (strDate != null && !strDate.isEmpty()) {

                                Date date = BHUtilities.StringToDate(strDate, BHGeneral.BACKUP_DATE_FORMAT, BHUtilities.LOCALE_EN);

                                return date;
                            }
                        }
                    }
                }
            }

            return null;
        }

        private void RenameAndRemoveOldSqlite(String strFile) {
            String fromPath = BHStorage.getFolder(BHStorage.FolderType.Database) + "/" + strFile;
            String toPath = DatabaseManager.getInstance().getDatabasePath();
            new BaseController().removeDatabase();
            RenameSqlite(fromPath, toPath);
        }

        private void RenameSqlite(String fromPath, String toPath) {
            File toFile = new File(toPath);
            File fromFile = new File(fromPath);

            if (toFile.exists()) {
                toFile.delete();
            }

            if (fromFile.exists()) {
                if(fromFile.renameTo(toFile)){
                    System.out.println("Rename succesful");
                }else{
                    System.out.println("Rename failed");
                }
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // take CPU lock to prevent CPU from going off if the user
            // presses the power button during download
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    getClass().getName());
            mWakeLock.acquire();
            mProgressDialog.show();
        }

        @Override
        protected DownloadResultInfo doInBackground(String... sUrl) {
            /*InputStream input = null;
            OutputStream output = null;
            HttpURLConnection connection = null;*/
            int step = 1;
            final int stepMax = BHGeneral.isOpenDepartmentSignature ? 3 : 2;
            DownloadResultInfo resultInfo = new DownloadResultInfo();
            try {
                /*URL url = new URL(sUrl[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    return "Server returned HTTP " + connection.getResponseCode()
                            + " " + connection.getResponseMessage();
                }

                // download the file
                int fileLength = connection.getContentLength();
                input = connection.getInputStream();
                output = new FileOutputStream(DatabaseManager.getInstance().getDatabasePath() + ".zip");

                byte data[] = new byte[4096];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    int percent = (int) (total * 100 / fileLength);
                    if (fileLength > 0) // only if total length is known
                        publishProgress(percent > 99 ? 99 : percent);
                    output.write(data, 0, count);
                }

                BHUtilities.saveDatabase();
                publishProgress(100);


                *//**Delete Folder Picture*//*
                File delFolder = new File(BHStorage.getFolder(BHStorage.FolderType.Picture));
                TSRController.deleteDir(delFolder);


                // YIM Download Signature Image By DepartmentCode
                if(BHGeneral.isOpenDepartmentSignature){
                    EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());

                    GetDepartmentSignatureImageInputInfo departmentSignatureInput = new GetDepartmentSignatureImageInputInfo();
                    departmentSignatureInput.DepartmentCode = saleLeader.DepartmentCode;
                    GetDepartmentSignatureImageOutputInfo departmentSignatureImage = TSRService.GetDepartmentSignatureImage(departmentSignatureInput, false);
                    if (departmentSignatureImage.Info != null) {
                        byte[] b = Base64.decode(departmentSignatureImage.Info.ImageData, Base64.DEFAULT);
                        String temp = Base64.encodeToString(b, Base64.DEFAULT);
                        TSRController.saveDepartmentSignatureImage(temp);
                    }
                    *//* use when return only image
                    EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());
                    if (sUrl[1] != null&&saleLeader != null) {
                        url = new URL(sUrl[1]);
                        connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                        connection.setDoOutput(true);

                        //Send request
                        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
                        wr.writeBytes ("DepartmentCode="+saleLeader.DepartmentCode);
                        wr.flush ();
                        wr.close ();

                        connection.connect();

                        Bitmap image = BitmapFactory.decodeStream(connection.getInputStream());
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        byte[] b = stream.toByteArray();
                        String temp = Base64.encodeToString(b, Base64.DEFAULT);
                        TSRController.saveDepartmentSignatureImage(temp);
                    }
                    */
                resultInfo.url = sUrl[0];

                final URL url = new URL(sUrl[0]);
                final String pathFileDBZip = DatabaseManager.getInstance().getDatabasePath() + ".zip";

                File fileDBZip = new File(pathFileDBZip);
                if (fileDBZip.exists()) {
                    fileDBZip.delete();
                }

                final DownloadProgressUpdateInfo progressUpdateInfo = new DownloadProgressUpdateInfo();
                progressUpdateInfo.message = String.format("Download ฐานข้อมูล %d/%d", step, stepMax);
                progressUpdateInfo.progress = 0;
                progressUpdateInfo.progressMax = 100;
                progressUpdateInfo.progressNumberFormat = "";
                progressUpdateInfo.indeterminate = false;
                publishProgress(progressUpdateInfo);

                boolean downloadSuccess = DownloadFileFromServer(url, pathFileDBZip, 0, progressUpdateInfo);
                if (downloadSuccess) {
                    step += 1;
                    progressUpdateInfo.message = String.format("บันทึกฐานข้อมูล (ไม่ใช้อินเตอร์เน็ต) %d/%d", step, stepMax);
                    progressUpdateInfo.progress = 0;
                    progressUpdateInfo.progressMax = 100;
                    progressUpdateInfo.progressNumberFormat = "";
                    progressUpdateInfo.indeterminate = false;
                    publishProgress(progressUpdateInfo);

                    //BHUtilities.saveDatabase();
                    try {
                        String strFileUnZip = unzip(pathFileDBZip, BHStorage.getFolder(BHStorage.FolderType.Database), progressUpdateInfo);

                        ReCheckSqliteData(strFileUnZip);

                    } catch (IOException ex) {

                    }



                    //**Delete Folder Picture*//*
                    File delFolder = new File(BHStorage.getFolder(BHStorage.FolderType.Picture));
                    TSRController.deleteDir(delFolder);



                    // YIM Download Signature Image By DepartmentCode
                    File fileDB = new File(DatabaseManager.getInstance().getDatabasePath());
                    if(BHGeneral.isOpenDepartmentSignature && fileDB.exists()) {
                        step += 1;
                        progressUpdateInfo.message = String.format("Download ข้อมูลอื่นๆ %d/%d", step, stepMax);
                        progressUpdateInfo.progress = 50;
                        progressUpdateInfo.progressMax = 100;
                        progressUpdateInfo.progressNumberFormat = "";
                        progressUpdateInfo.indeterminate = false;
                        publishProgress(progressUpdateInfo);

                        try {
                            EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());

                            GetDepartmentSignatureImageInputInfo departmentSignatureInput = new GetDepartmentSignatureImageInputInfo();
                            departmentSignatureInput.DepartmentCode = saleLeader.DepartmentCode;
                            GetDepartmentSignatureImageOutputInfo departmentSignatureImage = TSRService.GetDepartmentSignatureImage(departmentSignatureInput, false);
                            if (departmentSignatureImage.Info != null) {
                                byte[] b = Base64.decode(departmentSignatureImage.Info.ImageData, Base64.DEFAULT);
                                String temp = Base64.encodeToString(b, Base64.DEFAULT);
                                TSRController.saveDepartmentSignatureImage(temp);
                            }
                        } catch (Exception e) {

                        }

                        progressUpdateInfo.progress = 100;
                        publishProgress(progressUpdateInfo);
                    }

                    resultInfo.ResultCode = 0;
                    resultInfo.ResultDescription = "Success";
                    return resultInfo;
                } else {
                    resultInfo.ResultCode = -1;
                    resultInfo.ResultDescription = "ErrorDownloadDB";
                    return resultInfo;
                }


                /*if (DownloadFileFromServer(url, pathFileDBZip, 0)) {
                    *//**Delete Folder Picture*//*
                    File delFolder = new File(BHStorage.getFolder(BHStorage.FolderType.Picture));
                    TSRController.deleteDir(delFolder);

                    // YIM Download Signature Image By DepartmentCode
                    if(BHGeneral.isOpenDepartmentSignature){
                        EmployeeDetailInfo saleLeader = new EmployeeDetailController().getTeamHeadDetailByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode());

                        GetDepartmentSignatureImageInputInfo departmentSignatureInput = new GetDepartmentSignatureImageInputInfo();
                        departmentSignatureInput.DepartmentCode = saleLeader.DepartmentCode;
                        GetDepartmentSignatureImageOutputInfo departmentSignatureImage = TSRService.GetDepartmentSignatureImage(departmentSignatureInput, false);
                        if (departmentSignatureImage.Info != null) {
                            byte[] b = Base64.decode(departmentSignatureImage.Info.ImageData, Base64.DEFAULT);
                            String temp = Base64.encodeToString(b, Base64.DEFAULT);
                            TSRController.saveDepartmentSignatureImage(temp);
                        }
                    }

                    return "Success";
                } else {
                    return "NotSuccess";
                }*/
            } catch (Exception e) {

                resultInfo.ResultCode = -1;
                resultInfo.ResultDescription = e.toString();
                return resultInfo;
            }
            /*finally {
                try {
                    if (output != null)
                        output.close();
                    if (input != null)
                        input.close();
                } catch (IOException ignored) {
                    return ignored.toString();
                }

                if (connection != null)
                    connection.disconnect();
                return "Success";
            }*/
        }

        @Override
        protected void onProgressUpdate(DownloadProgressUpdateInfo... progress) {
            super.onProgressUpdate(progress);
            // if we get here, length is known, now set indeterminate to false
            /*mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(progress[0]);*/

            DownloadProgressUpdateInfo info = progress[0];
            mProgressDialog.setIndeterminate(info.indeterminate);
            mProgressDialog.setMessage(info.message);
            mProgressDialog.setMax(info.progressMax);
            mProgressDialog.setProgress(info.progress);
            mProgressDialog.setProgressNumberFormat(info.progressNumberFormat);
        }

        @Override
        protected void onPostExecute(final DownloadResultInfo result) {
            mWakeLock.release();
            mProgressDialog.dismiss();

            /*if (result.equals("Success")) {
                //-- Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                EmployeeDetailInfo empDet = new EmployeeDetailController().getCurrentTreeHistoryID(BHPreference.organizationCode());
                String currTreeHisID = (empDet != null) ? ((empDet.TreeHistoryID != null) ? empDet.TreeHistoryID : "") : "";
                BHPreference.setCurrentTreeHistoryID(currTreeHisID);
//				Log.i("Test Synch Tree", currTreeHisID);

                if (currTreeHisID.equals("")) {
                    File oldFileDB = new File(DatabaseManager.getInstance().getDatabasePath());
                    if (oldFileDB.exists()) {
                        oldFileDB.delete();
                    }

                    Builder setupAlert;
                    setupAlert = new AlertDialog.Builder(activity)
                            .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
                            .setMessage(
                                    "ระบบไม่พบข้อมูลโครงสร้างพนักงาน Version ปัจจุบัน \nให้ดำเนินการปรับปรุงข้อมูลก่อนเริ่มใช้งานต่อไป")
                            .setPositiveButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                    setupAlert.show();
                }
            }

            if (delegate != null) {
                delegate.postResult(result);
            } else {
                Log.e("ApiAccess", "You have not assigned IApiAccessResponse delegate");
            }*/
//            if (result != null)
//                Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
//            else
//                Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();
            File fileDB = new File(DatabaseManager.getInstance().getDatabasePath());
            if (result.ResultCode == 0 && fileDB.exists()) {
                //-- Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                EmployeeDetailInfo empDet = new EmployeeDetailController().getCurrentTreeHistoryID(BHPreference.organizationCode());
                String currTreeHisID = (empDet != null) ? ((empDet.TreeHistoryID != null) ? empDet.TreeHistoryID : "") : "";
                BHPreference.setCurrentTreeHistoryID(currTreeHisID);
//				Log.i("Test Synch Tree", currTreeHisID);

                if (currTreeHisID.equals("")) {
                    if (fileDB.exists()) {
                        //fileDB.delete();
//                        Toast.makeText(context, "exists DB", Toast.LENGTH_LONG).show();
                    }

                    Builder setupAlert;
                    setupAlert = new AlertDialog.Builder(activity)
                            .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
                            .setMessage(
                                    "ระบบไม่พบข้อมูลโครงสร้างพนักงาน Version ปัจจุบัน \nให้ดำเนินการปรับปรุงข้อมูลก่อนเริ่มใช้งานต่อไป")
                            .setPositiveButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                    setupAlert.show();

                } else {
                    if (delegate != null) {
                        delegate.postResult(result.ResultDescription);
                    }

                    Intent i = context.getPackageManager().getLaunchIntentForPackage("com.gps_tracking");
                    if (i != null) {
                        Intent in = new Intent(Intent.ACTION_VIEW, Uri.parse("gis://empid/" + BHPreference.employeeID() + "/BH"));
//                      i.putExtra("empid", BHPreference.employeeID());
                      context.startActivity(in);
                    }
                }
            } else {
                /*if (fileDB.exists()) {
                    fileDB.delete();
                }*/
                new BaseController().removeDatabase();

                Builder setupAlert;
                setupAlert = new AlertDialog.Builder(activity)
                        .setTitle("แจ้งเตือน ปรับปรุงฐานข้อมูล")
                        .setCancelable(false)
                        .setMessage("เกิดข้อผิดพลาดในการปรับปรุงฐานข้อมูล กรุณาลองใหม่อีกครั้ง")
                        .setNegativeButton(activity.getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                final DownloadTask downloadTask = new DownloadTask(context);
                                downloadTask.delegate = delegate;
                                downloadTask.execute(result.url);

                                downloadTask.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                    @Override
                                    public void onCancel(DialogInterface dialog) {
                                        downloadTask.cancel(true);
                                    }
                                });
                            }
                        })
                        .setPositiveButton(activity.getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });
                setupAlert.show();
            }
        }
    }

    private class SynchronizeReceiver extends BroadcastReceiver {
        private SynchronizeReceiver instance;

        private ProgressDialog dialog;
        private SynchronizeService.SynchronizeResult result;
        private boolean isProcessing;

        private SynchronizeReceiver() {
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setTitle("");
            dialog.setMessage("");

            result = null;
            isProcessing = false;
        }

        public SynchronizeReceiver getInstance() {
            if (instance == null) {
                instance = new SynchronizeReceiver();
                LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(instance, new IntentFilter(SynchronizeService.SYNCHRONIZE_BROADCAST_ACTION));
            }

            return instance;
        }

        public void show() {
            if (isProcessing && !dialog.isShowing()) {
                dialog.show();
            }
        }

        private void start() {
            if (!isProcessing) {
                isProcessing = true;
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        }

        private void stop() {
            LocalBroadcastManager.getInstance(MainActivity.this).unregisterReceiver(this);
            isProcessing = false;
            dialog = null;
            result = null;
            instance = null;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            if (intent != null) {
                result = intent.getParcelableExtra(SynchronizeService.SYNCHRONIZE_RESULT_DATA_KEY);
                dialog.setTitle(result.title);
                dialog.setMessage(result.message);
                dialog.setProgress(result.progress);

                if (result.progress >= 100) {
                    dialog.setProgress(100);
                }

                if (result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED || result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                    dialog.dismiss();
                    if (result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                        showWarningDialog(result.error);
                        showView(BHFragment.newInstance(SynchronizeMainFragment.class));
                        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
                        menu.showContent();
                    } else {
                        if (result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED && checkLogout) {
                            checkLogout = false;
                            Log.i("SynchronizeReceiver", "logout");
                            logout(BHPreference.userID(), BHPreference.userDeviceId(), AddUserDeviceLogInputInfo.UserDeviceLogProcessType.ANDROID_LOGOUT.toString());
                        } else if (result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED) {
                            checkLogin = true;

                            final DownloadTask downloadTask = new DownloadTask(activity);
                            String URL = String.format("%s/%s/%s/%s", BHPreference.TSR_DB_URL, BHPreference.teamCode(), BHPreference.employeeID() + (BHPreference.IsAdmin() ? BHGeneral.FOLDER_ADMIN : ""), "tsr.db.zip");
                            downloadTask.execute(URL);

                            downloadTask.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });
                        }
                    }
                    stop();
                }

            }
        }
    }

    /**
     *
     * Edit by Teerayut Klinsanga
     *
     * Created: 2019-08-02 09:00.00
     *
     * == Print with image ==
     *
     */

    public synchronized void printImageNew(final Bitmap[] bmp, final List<List<PrintTextInfo>> detailPrint, final PrintHandler handler, final String receiptType) {
        if (bhBluetoothPrinter != null) {
            bhBluetoothPrinter.SetPrintWithBitmap(bmp, detailPrint, handler, receiptType);
        }
    }

    /**
     * End
     */
}
