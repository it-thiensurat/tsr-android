package th.co.thiensurat.fragments.products.stock;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.TransactionService;
import th.co.thiensurat.service.data.GetProductStockOfTeamInputInfo;
import th.co.thiensurat.service.data.GetProductStockOfTeamOutputInfo;

public class NewCheckStockMainFragment extends BHFragment {

    private static final String PRODUCT_STOCK_STATUS_OVER = "OVER";
    private static final String PRODUCT_STOCK_STATUS_CHECKED = "CHECKED";
    private static final String PRODUCT_STOCK_STATUS_WAIT = "WAIT";
    //    private static final String PRODUCT_STOCK_STATUS_RETURN = "RETURN";	// ถอดเครื่อง/เปลี่ยนเครื่อง
//    private static final String PRODUCT_STOCK_STATUS_ACCEPT_RETURN = "ACCEPT_RETURN";	// นำคืนเข้าระบบจากการถอด/เปลี่ยนเครื่อง
    private static final String PRODUCT_STOCK_STATUS_DAMAGE = "DAMAGE";    // ชำรุด
    private static final String PRODUCT_STOCK_STATUS_TEAM_DESTROY = "TEAM_DESTROY";    // ยุบทีม

    private boolean Refresh = false;

    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());

    //CheckConnecting
    public static ProgressDialog pdia;
    public static int timeOut = 20000;//20s

    @InjectView
    private Button btnImport, btnRefresh; // btnCheck,
    @InjectView
    private TextView txtDetail, txtImport, txtProduct, txtShow;
    @InjectView
    private ListView lvProductStock;


    List<ProductStockInfo> ProductList;
    List<ProductStockInfo> checkedProductList = null;

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_check_stock_main;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_check_stock;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        // return null;
        return new int[]{R.string.button_return, R.string.button_check};
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_check:
                /*try {
                    if (ProductList != null) {
                        NewCheckStockCheckedFragment.Data scanData = new NewCheckStockCheckedFragment.Data();
                        scanData.scanProductList = ProductList;
                        NewCheckStockCheckedFragment fmScan = BHFragment.newInstance(NewCheckStockCheckedFragment.class, scanData);
                        showNextView(fmScan);
                    } else {
                        showMessage("ไม่มีสินค้ารอการตรวจสอบ!");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                pdia = new ProgressDialog(activity);
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
                                showDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้");
                            } else {
                                try {
                                    /*if (ProductList != null) {
                                        NewCheckStockCheckedFragment.Data scanData = new NewCheckStockCheckedFragment.Data();
                                        scanData.scanProductList = ProductList;
                                        NewCheckStockCheckedFragment fmScan = BHFragment.newInstance(NewCheckStockCheckedFragment.class, scanData);
                                        showNextView(fmScan);
                                    } else {
                                        showMessage("ไม่มีสินค้ารอการตรวจสอบ!");
                                    }*/

                                    List<ProductStockInfo> scanProductList = new ArrayList<>();
                                    if (ProductList != null) {
                                        scanProductList = ProductList;
                                    }

                                    NewCheckStockCheckedFragment.Data scanData = new NewCheckStockCheckedFragment.Data();
                                    scanData.scanProductList = scanProductList;
                                    NewCheckStockCheckedFragment fmScan = BHFragment.newInstance(NewCheckStockCheckedFragment.class, scanData);
                                    showNextView(fmScan);
                                    

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).execute(BHPreference.TSR_SERVICE_URL);
                } else {
                    pdia.dismiss();
                    showDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
                }

                break;

            case R.string.button_return:
                showNextView(new NewCheckStockReturnProductFragment());
                break;
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        bindProductStockOfTeam();
        setWidgetsEventListener();

    }

    private void setWidgetsEventListener() {

        btnImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ProductList != null && ProductList.size() > 0) {
                    Builder setupAlert;
                    setupAlert = new AlertDialog.Builder(activity).setTitle("การนำเข้าสินค้า")
                            .setMessage("ได้ทำการตรวจสอบสินค้าเรียบร้อยแล้ว ต้องการที่จะตรวจสอบอีกครั้งหรือไม่ ?")
                            .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    /*startSync(false);
                                    //importProductStockFromInterface();*/

                                    pdia = new ProgressDialog(activity);
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
                                                    showDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้");
                                                } else {
                                                    startSync(false);
                                                }
                                            }
                                        }).execute(BHPreference.TSR_SERVICE_URL);
                                    } else {
                                        pdia.dismiss();
                                        showDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
                                    }
                                }
                            }).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // just do nothing
                                }
                            });
                    setupAlert.show();
                } else {
                    importProductStockFromInterface();
                }
            }
        });

        btnRefresh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //startSync(true);

                pdia = new ProgressDialog(activity);
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
                                showDialog("Connecting To Server", "เกิดการผิดพลาด ไม่สามารถเชื่อมต่อกับเซิฟเวอร์ได้");
                            } else {
                                startSync(true);
                            }
                        }
                    }).execute(BHPreference.TSR_SERVICE_URL);
                } else {
                    pdia.dismiss();
                    showDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
                }

            } // OnClick
        });

    }

    private void importProductStockFromInterface() {
        (new BackgroundProcess(activity) {
            GetProductStockOfTeamInputInfo input = new GetProductStockOfTeamInputInfo();
            GetProductStockOfTeamOutputInfo output = new GetProductStockOfTeamOutputInfo();
            /*** [START] :: Fixed - [BHPROJ-1036-8530] - เพิ่มฝ่ายใหม่ แต่ดึงรายการสินค้าไม่ขึ้นครับ Production ***/
            /*String ProductStockTeamCode = null;
            String TeamPrefix = null;
            String TeamNo = null;
            String SubteamNo = null;*/
            /*** [END] :: Fixed - [BHPROJ-1036-8530] - เพิ่มฝ่ายใหม่ แต่ดึงรายการสินค้าไม่ขึ้นครับ Production  ***/

            // ProductStockInfo productinput = new ProductStockInfo();

            // GetMoreProductStockFromProductStockDBOfTeamInputInfo inputDB =
            // new GetMoreProductStockFromProductStockDBOfTeamInputInfo();
            // GetMoreProductStockFromProductStockDBOfTeamOutputInfo outputDB =
            // new GetMoreProductStockFromProductStockDBOfTeamOutputInfo();

            @Override
            protected void before() {
                // TODO Auto-generated method stub
                input.OrganizationCode = BHPreference.organizationCode();
                input.TeamCode = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.SubTeamCode() == null ? BHPreference.teamCode() : BHPreference.SubTeamCode();
                input.CreateBy = BHPreference.employeeID(); // [Modified@05/08/2016] :: For support CRD take by EmpID

                // inputDB.OrganizationCode = input.OrganizationCode;
                // inputDB.TeamCode = input.TeamCode;
            }

            @Override
            protected void calling() {
                //new ProductStockController().deleteProductStock(BHPreference.organizationCode(), BHPreference.teamCode());
                new ProductStockController().deleteProductStockByTeamCode(BHPreference.organizationCode(), BHPreference.selectTeamCodeOrSubTeamCode());

                /*** [START] :: Fixed - [BHPROJ-1036-8530] - เพิ่มฝ่ายใหม่ แต่ดึงรายการสินค้าไม่ขึ้นครับ Production ***/
                /*if (BHPreference.SubTeamCode() == null || BHPreference.SubTeamCode().equals("")) {
                    ProductStockTeamCode = BHPreference.teamCode();                                                         //-- ProductStockTeamCode = BAA-09
                    if (ProductStockTeamCode.substring(4, 5).equals("0"))
                        ProductStockTeamCode = ProductStockTeamCode.substring(0, 4) + ProductStockTeamCode.substring(5);    //-- ProductStockTeamCode = BAA-9 (From BAA-09)
                } else {
                    ProductStockTeamCode = BHPreference.SubTeamCode();                                                      //-- ProductStockTeamCode = BAA-0905
                    if (ProductStockTeamCode.length() > 6) {

                        TeamPrefix = ProductStockTeamCode.substring(0, 4);                                                  //-- BAA-
                        TeamNo = ProductStockTeamCode.substring(4, 6);                                                      //-- 09
                        SubteamNo = ProductStockTeamCode.substring(6, 8);                                                   //-- 05

                        TeamNo = TeamNo.substring(0, 1).equals("0") ? TeamNo.substring(1) : TeamNo;                                                             //-- 9
                        SubteamNo = SubteamNo.substring(0, 1).equals("0") ? (SubteamNo.substring(1).equals("0") ? "" : SubteamNo.substring(1)) : SubteamNo;     //-- 5 แต่ถ้าเป็น 0 จะ return ค่าว่าง
                        ProductStockTeamCode = TeamPrefix + TeamNo + (SubteamNo.equals("") ? "" : "/") + SubteamNo;                                                                           //-- ProductStockTeamCode = BAA-9/5

                    }

//							ProductStockTeamCode = ProductStockTeamCode.substring(0, 6) + "/" + ProductStockTeamCode.substring(6);
//						if (ProductStockTeamCode.substring(4, 5).equals("0"))
//							ProductStockTeamCode = ProductStockTeamCode.substring(0, 4) + ProductStockTeamCode.substring(5);
//						if (ProductStockTeamCode.length() > 6) {
//							if (ProductStockTeamCode.substring(7, 8).equals("0"))
//								ProductStockTeamCode = ProductStockTeamCode.substring(0, 6) + ProductStockTeamCode.substring(8);
//						}
                }

                importProductStockFromInterface(BHPreference.organizationCode(), BHPreference.selectTeamCodeOrSubTeamCode(), ProductStockTeamCode);*/
                importProductStockFromInterface(BHPreference.organizationCode(), BHPreference.selectTeamCodeOrSubTeamCode(), "");
                /*** [END] :: Fixed - [BHPROJ-1036-8530] - เพิ่มฝ่ายใหม่ แต่ดึงรายการสินค้าไม่ขึ้นครับ Production  ***/
                //
                // importProductStockFromInterface(BHPreference.organizationCode(),
                // BHPreference.teamCode());

                output = TSRService.getProductStockOfTeam(input, false);
                // if (output.Info != null) {
                // for (int i = 0; i < output.Info.size(); i++) {
                // if
                // (output.Info.get(i).Status.equals(PRODUCT_STOCK_STATUS_CHECKED))
                // {
                // output.Info.get(i).Status = PRODUCT_STOCK_STATUS_WAIT;
                // updateProductStock(output.Info.get(i));
                // }
                //
                // }
                // }
                //
                // outputDB =
                // TSRService.getMoreProductStockFromProductStockDBOfTeam(inputDB,
                // false);
                // if (outputDB.Info != null) {
                // for (int i = 0; i < outputDB.Info.size(); i++) {
                // outputDB.Info.get(i).Status = PRODUCT_STOCK_STATUS_WAIT;
                // addProductStock(outputDB.Info.get(i));
                // }
                // }
                //

            }

            @Override
            protected void after() {
                // bindProductStockOfTeam();

                if (output != null && output.Info != null) {

                    // ProductList = output.Info;
                    // bindListView();

                    (new BackgroundProcess(activity) {

                        @Override
                        protected void calling() {
                            // TODO Auto-generated method stub
                            try {
                                for (ProductStockInfo item : output.Info) {
                                    // productinput.ProductSerialNumber =
                                    // item.ProductSerialNumber;
                                    // productinput.OrganizationCode =
                                    // item.OrganizationCode;
                                    // productinput.ProductID = item.ProductID;
                                    // productinput.Type = item.Type;
                                    // productinput.TeamCode = item.TeamCode;
                                    // productinput.Status = item.Status;
                                    // productinput.ImportDate =
                                    // item.ImportDate;
                                    // productinput.ScanDate = item.ScanDate;
                                    // productinput.ScanByTeam =
                                    // item.ScanByTeam;
                                    //
                                    // addProductStock(productinput);

                                    ProductStockInfo checkProductStock = getProductStock(item.ProductSerialNumber);
                                    if (checkProductStock == null)
                                        addProductStock(item);
                                }

                                for (ProductStockInfo item : output.Info) {
                                    // productinput.ProductSerialNumber =
                                    // item.ProductSerialNumber;
                                    // productinput.OrganizationCode =
                                    // item.OrganizationCode;
                                    // productinput.ProductID = item.ProductID;
                                    // productinput.Type = item.Type;
                                    // productinput.TeamCode = item.TeamCode;
                                    // productinput.Status = item.Status;
                                    // productinput.ImportDate =
                                    // item.ImportDate;
                                    // productinput.ScanDate = item.ScanDate;
                                    // productinput.ScanByTeam =
                                    // item.ScanByTeam;
                                    //
                                    // if
                                    // (productinput.Status.equals(PRODUCT_STOCK_STATUS_CHECKED))
                                    // {
                                    // productinput.Status =
                                    // PRODUCT_STOCK_STATUS_WAIT;
                                    // updateProductStock(productinput);
                                    // }

                                    if (item.Status.equals(PRODUCT_STOCK_STATUS_CHECKED)) {
                                        item.Status = PRODUCT_STOCK_STATUS_WAIT;
                                        item.ScanByTeam = null;
                                        item.ScanDate = null;
                                        updateProductStock(item, false);
                                    }
                                }
                            } catch (Exception e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }

                        @Override
                        protected void after() {
                            // TODO Auto-generated method stub
                            bindProductStockOfTeam();
                        }
                    }).start();

                } else {
                    bindProductStockOfTeam();
                }
            }
        }).start();
    }

    private void bindProductStockOfTeam() {
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // ProductList = getProductStockOfTeam(BHPreference.organizationCode(), BHPreference.teamCode());
                if(isCredit || BHPreference.IsSaleForCRD()){
                    ProductList = getProductStockByEmployeeID(BHPreference.organizationCode(), BHPreference.employeeID());
                } else {
                    ProductList = getProductStockOfTeam(BHPreference.organizationCode(), BHPreference.selectTeamCodeOrSubTeamCode());
                }

            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (ProductList != null) {

                    bindListView();
                } else {
                    lvProductStock.setVisibility(View.GONE);
                    txtDetail.setVisibility(View.GONE);
                    txtImport.setVisibility(View.GONE);
                }

                setting();
            }
        }).start();
    }

    private void bindListView() {

        //for สำหรับกรณีสินค้าถูกคนอื่นนำไปสแกน ให้กลับมาแสดงที่ทีมตัวเองเป็น wait
        for (ProductStockInfo item : ProductList) {
            if (item.Status.equals(PRODUCT_STOCK_STATUS_OVER) && item.TeamCode.equals(BHPreference.selectTeamCodeOrSubTeamCode())) {
                item.Status = PRODUCT_STOCK_STATUS_WAIT;
            }
        }

        lvProductStock.setVisibility(View.VISIBLE);
        BHArrayAdapter<ProductStockInfo> adapter = new BHArrayAdapter<ProductStockInfo>(activity, R.layout.list_product, ProductList) {
            class ViewHolder {
                public TextView list;
                public TextView list_status;
            }

            @Override
            protected void onViewItem(int position, View view, Object holder, ProductStockInfo info) {
                // TODO Auto-generated method stub
                ViewHolder vh = (ViewHolder) holder;
                switch (info.Status) {
                    case PRODUCT_STOCK_STATUS_OVER:
                        // vh.list.setText("รหัสสินค้า       " +
                        // info.ProductSerialNumber + "   " + "( เกิน )");
                        vh.list.setText("รหัสสินค้า       " + info.ProductSerialNumber);
                        vh.list.setTextColor(Color.RED);
                        vh.list_status.setText("เกิน   " + "(" + info.TeamCode + ")");
                        vh.list_status.setTextColor(Color.RED);
                        view.setBackgroundResource(R.color.bg_body_white);
                        break;
                    case PRODUCT_STOCK_STATUS_WAIT:
                        // vh.list.setText("รหัสสินค้า       " +
                        // info.ProductSerialNumber + "   " + "( ขาด )");
                        vh.list.setText("รหัสสินค้า       " + info.ProductSerialNumber);
                        vh.list.setTextColor(Color.BLACK);
                        if (info.ScanByTeam != null && !info.ScanByTeam.equals(info.TeamCode)) {
                            vh.list_status.setText("รอตรวจสอบ " + "(" + info.ScanByTeam + ")");
                            vh.list_status.setTextColor(Color.RED);
                        } else {
                            vh.list_status.setText("รอตรวจสอบ");
                            vh.list_status.setTextColor(Color.BLACK);
                        }

                        view.setBackgroundResource(R.color.bg_body_white); // white
                        break;
                    case PRODUCT_STOCK_STATUS_CHECKED:
                        vh.list.setText("รหัสสินค้า       " + info.ProductSerialNumber);
                        vh.list.setTextColor(Color.BLACK);
                        vh.list_status.setText("ตรวจสอบแล้ว");
                        vh.list_status.setTextColor(Color.GREEN);
                        view.setBackgroundResource(R.color.bg_body_white); // grey
                        break;

                    case PRODUCT_STOCK_STATUS_DAMAGE:
                        vh.list.setText("รหัสสินค้า       " + info.ProductSerialNumber);
                        vh.list.setTextColor(Color.BLUE);
                        vh.list_status.setText("เครื่องชำรุด");
                        vh.list_status.setTextColor(Color.BLUE);
                        view.setBackgroundResource(R.color.bg_body_white);
                        break;
                    case PRODUCT_STOCK_STATUS_TEAM_DESTROY:
                        vh.list.setText("รหัสสินค้า       " + info.ProductSerialNumber);
                        vh.list.setTextColor(Color.BLUE);
                        vh.list_status.setText("ยุบทีมขาย");
                        vh.list_status.setTextColor(Color.BLUE);
                        view.setBackgroundResource(R.color.bg_body_white);
                        break;
                    default:
                        break;
                }
            }
        };
        lvProductStock.setAdapter(adapter);
        // setting();
    }

    protected void setting() {
        // TODO Auto-generated method stub
        int stockCount = 0;
        int stockCountWAIT = 0;
        if (ProductList != null) {
            for (ProductStockInfo item : ProductList) {
                if (item.Status.equals(PRODUCT_STOCK_STATUS_WAIT) || item.Status.equals(PRODUCT_STOCK_STATUS_CHECKED)) {
                    stockCount++;
                }
                if (item.Status.equals(PRODUCT_STOCK_STATUS_WAIT)) {
                    stockCountWAIT++;
                }
            }

            txtProduct.setText(String.valueOf(stockCountWAIT));
            txtImport.setText(String.valueOf(stockCount));
            txtDetail.setVisibility(View.VISIBLE);
            txtImport.setVisibility(View.VISIBLE);

        }

    }

    private class SynchronizeReceiver extends BroadcastReceiver {
        private SynchronizeReceiver instance;

        private ProgressDialog dialog;
        private SynchronizeService.SynchronizeResult result;
        private boolean isProcessing;

        private SynchronizeReceiver() {
            dialog = new ProgressDialog(activity);
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
                LocalBroadcastManager.getInstance(activity).registerReceiver(instance, new IntentFilter(SynchronizeService.SYNCHRONIZE_BROADCAST_ACTION));
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
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(this);
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
                    if (result.progress == SynchronizeService.SYNCHRONIZE_ALL_COMPLETED) {
                        if (!Refresh) {
                            importProductStockFromInterface();
                        } else {
                            showNextView(new NewCheckStockMainFragment());
                        }

                    }
                    if (result.progress == SynchronizeService.SYNCHRONIZE_LOCAL_ERROR || result.progress == SynchronizeService.SYNCHRONIZE_ALL_ERROR) {
                        showWarningDialog(result.error);

                    }
                    stop();
                }

            }
        }

    }

    private void startSync(boolean ref) {
        Refresh = ref;
        TransactionService.stopService(activity);
        SynchronizeReceiver synchronizeReceiver = new SynchronizeReceiver();
        synchronizeReceiver.getInstance().start();

        //new BaseController().removeDatabase();

        BHPreference.setLastloginID(BHPreference.userID());
        SynchronizeService.SynchronizeData request = new SynchronizeService.SynchronizeData();
        request.master = new SynchronizeService.SynchronizeMaster();
        if (ref) {
            request.master.syncProductRelated = ref;
        }
        /*request.master.syncTeamRelated = true;
        request.master.syncProductRelated = true;
        request.master.syncCustomerRelated = true;
        request.master.syncPaymentRelated = true;
        request.master.syncContractRelated = true;
        request.master.syncEditContractRelated = true;
        request.master.syncSendMoneyRelated = true;
        request.master.syncMasterDataRelated = true;*/

        Intent i = new Intent(activity, SynchronizeService.class);
        i.putExtra(SynchronizeService.SYNCHRONIZE_REQUEST_DATA_KEY, request);
        activity.startService(i);
    }

    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager)activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
}

