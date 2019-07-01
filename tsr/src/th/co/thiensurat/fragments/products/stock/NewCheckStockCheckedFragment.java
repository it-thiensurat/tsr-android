package th.co.thiensurat.fragments.products.stock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

//import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.info.ProductInfo;
import th.co.thiensurat.data.info.ProductStockHistoryInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.AddProductStockHistoryInputInfo;
import th.co.thiensurat.service.data.AddProductStockHistoryOutputInfo;
import th.co.thiensurat.service.data.ScanProductStockBySerialNoInputInfo;
import th.co.thiensurat.service.data.ScanProductStockBySerialNoOutputInfo;
import th.co.thiensurat.service.data.ScanProductStockForCRDBySerialNoInputInfo;
import th.co.thiensurat.service.data.ScanProductStockForCRDBySerialNoOutputInfo;
import th.co.thiensurat.service.data.UpdateScanProductStockForCRDInputInfo;
import th.co.thiensurat.service.data.UpdateScanProductStockForCRDOutputInfo;

public class NewCheckStockCheckedFragment extends BHFragment {

    private static final String PRODUCT_STOCK_STATUS_OVER = "OVER";
    private static final String PRODUCT_STOCK_STATUS_CHECKED = "CHECKED";
    private static final String PRODUCT_STOCK_STATUS_WAIT = "WAIT";
    private static final String PRODUCT_STOCK_STATUS_SOLD = "SOLD";
    private static final String PRODUCT_STOCK_STATUS_DAMAGE = "DAMAGE";
    private static final String PRODUCT_STOCK_STATUS_TEAM_DESTROY = "TEAM_DESTROY";

    private static final String LIST_PRODUCT = "ScannedProductStockList";
    private static final String LIST_SERIAL = "ScannedSerialList";
    private static final int REQUEST_QR_SCAN = 0;
    private static final String COUNT_SCANNED_ALL = "ScannnedCounter";
    private static final String COUNT_SCANNED_TRUE = "ScannedTrue";
    private static final String COUNT_SCANNED_FALSE = "ScannedFalse";

    public static class Data extends BHParcelable {
        public List<ProductStockInfo> scanProductList;
    }

    Data dataWiat;

    @InjectView private ImageButton imgbuttonSacnBarcode;
    @InjectView private TextView txtProduct, txtTrue, txtNum, txtSum, txtFalse;
    @InjectView private EditText showTextScan;
    @InjectView private Button btnOK;
   

    int iScanCounter, iTrueCounter, iFalseCounter;// , waitProduct;

    ProductStockInfo scannedProduct;
    List<ProductStockInfo> scannedProductList;
    List<String> scannedSerialList;

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_check_stock_checked;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back, R.string.button_close};
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_check_stock;
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_close:
                try {
                    NewCheckStockCheckResultFragment.Data resultData = new NewCheckStockCheckResultFragment.Data();
                    resultData.scannedCount = iScanCounter;
                    resultData.scannedProductList = scannedProductList;
                    NewCheckStockCheckResultFragment fmResult = BHFragment.newInstance(NewCheckStockCheckResultFragment.class, resultData);
                    showNextView(fmResult);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case R.string.button_back:
                showLastView();
                break;
        }
    }

    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        dataWiat = getData();

        /*//Test+
        btnOK.setVisibility(View.VISIBLE);
        showTextScan.setEnabled(true);
        //Test

        btnOK.setVisibility(View.INVISIBLE);
        showTextScan.setEnabled(false);*/

        if(!BHGeneral.DEVELOPER_MODE){
            btnOK.setVisibility(View.INVISIBLE);
            showTextScan.setEnabled(false);
        }else{
            btnOK.setVisibility(View.VISIBLE);
            showTextScan.setEnabled(true);
        }

        if (savedInstanceState != null) {
            iScanCounter = savedInstanceState.getInt(COUNT_SCANNED_ALL);
            iTrueCounter = savedInstanceState.getInt(COUNT_SCANNED_TRUE);
            iFalseCounter = savedInstanceState.getInt(COUNT_SCANNED_FALSE);
            if (savedInstanceState.containsKey(LIST_PRODUCT)) {
                scannedProductList = savedInstanceState.getParcelableArrayList(LIST_PRODUCT);
            }
            if (savedInstanceState.containsKey(LIST_SERIAL)) {
                scannedSerialList = savedInstanceState.getStringArrayList(LIST_SERIAL);
            }
        }

        if (scannedProductList == null) {
            scannedProductList = new ArrayList<ProductStockInfo>();
            for (ProductStockInfo item : dataWiat.scanProductList) {
                if (!item.Status.equals(PRODUCT_STOCK_STATUS_OVER) && !item.Status.equals(PRODUCT_STOCK_STATUS_CHECKED))
                    item.Status = PRODUCT_STOCK_STATUS_WAIT;
                scannedProductList.add(item);
            }

        }

        if (scannedSerialList == null) {
            scannedSerialList = new ArrayList<String>();
        }

        int stockCount = 0;
        int waitCount = 0;

        int trueCount = 0;
//        int falseCount = 0;

        for (ProductStockInfo item : scannedProductList) {
            if (!item.Status.equals(PRODUCT_STOCK_STATUS_OVER)) {
                stockCount++;
            }
            if (item.Status.equals(PRODUCT_STOCK_STATUS_WAIT)) {
                waitCount++;
            } else if (item.Status.equals(PRODUCT_STOCK_STATUS_CHECKED)) {
                trueCount++;
                scannedSerialList.add(item.ProductSerialNumber);
            }
//            else if (item.Status.equals(PRODUCT_STOCK_STATUS_OVER)) {
//            	falseCount++;
//            }
        }
        iScanCounter = trueCount;
        iTrueCounter = trueCount;
//        iFalseCounter = falseCount;

        txtProduct.setText(String.valueOf(waitCount)); // สินค้ารอตรวจสอบ
        txtSum.setText(String.valueOf(stockCount)); // สินค้าทั้งหมดที่ต้อง scan(สินค้าที่เบิกจากคลัง)

        txtNum.setText(String.valueOf(iScanCounter)); // สินค้าที่ scan ไปแล้ว
        txtTrue.setText(String.valueOf(iTrueCounter)); // สินค้าที่ถูกต้อง
        txtFalse.setText(String.valueOf(iFalseCounter)); // สินค้าที่ไม่ถูกต้อง

        imgbuttonSacnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(activity, CaptureActivity.class);


                /*** [START] :: Permission ***/
                /*Intent intent = new IntentIntegrator(activity).createScanIntent();
                startActivityForResult(intent, REQUEST_QR_SCAN);*/

                new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

                    @Override
                    public void onSuccess(BHPermissions bhPermissions) {
                        Intent intent = new IntentIntegrator(activity).createScanIntent();
                        startActivityForResult(intent, REQUEST_QR_SCAN);
                    }

                    @Override
                    public void onNotSuccess(BHPermissions bhPermissions) {
                        bhPermissions.openAppSettings(getActivity());
                    }

                    @Override
                    public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
                        bhPermissions.showMessage(getActivity(), permissionType);
                    }

                }, BHPermissions.PermissionType.CAMERA);
                /*** [END] :: Permission ***/
            }
        });

        btnOK.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                onScanProduct();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
        outState.putInt(COUNT_SCANNED_ALL, iScanCounter);
        outState.putInt(COUNT_SCANNED_TRUE, iTrueCounter);
        outState.putInt(COUNT_SCANNED_FALSE, iFalseCounter);
        outState.putParcelableArrayList(LIST_PRODUCT, (ArrayList<ProductStockInfo>) scannedProductList);
        outState.putStringArrayList(LIST_SERIAL, (ArrayList<String>) scannedSerialList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_QR_SCAN && resultCode == Activity.RESULT_OK) {
            String content = intent.getStringExtra(Intents.Scan.RESULT);
            showTextScan.setText(content);
            onScanProduct();
        } else if (requestCode == REQUEST_QR_SCAN && resultCode == Activity.RESULT_CANCELED) {
            showTextScan.setText("");
        }
    }

    private void onScanProduct() {
        final String scannedID = showTextScan.getText().toString();
        // (1) Check duplicate scan
        boolean bFound = false;
        for (String prodSerial : scannedSerialList) {
            if (scannedID.equals(prodSerial)) {
                bFound = true;
            }
        }
        if (bFound) {
            // (1.1) Duplicate scanning
            final String title = "กรุณาตรวจสอบสินค้า";
            String message = "รายการสินค้านี้มีอยู่ในระบบแล้ว";
            Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                    .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // TODO Auto-generated method stub
                        }
                    });
            setupAlert.show();

        } else {

            if(BHPreference.IsSaleForCRD()) {

                final int timeOut = 20000;
                final ProgressDialog pdia = new ProgressDialog(activity);
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
                                ScanProductStockForCRDBySerialNo(scannedID);
                            }
                        }
                    }).execute(BHPreference.TSR_SERVICE_URL);
                } else {
                    pdia.dismiss();
                    showDialog("Connecting To Internet", "ไม่พบการเชื่อมต่ออินเตอร์เน็ต");
                }

            } else {
                // (1.2) New scanning
                (new BackgroundProcess(activity) {
                    ProductStockInfo output = new ProductStockInfo();

                    @Override
                    protected void calling() {
                        // TODO Auto-generated method stub

                        /*** [START] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย ***/
                        //output = getProductStock(scannedID);
                        if(BHPreference.IsSaleForCRD()) {
                            output = getProductStockForCRD(scannedID, BHPreference.employeeID());
                        } else {
                            output = getProductStock(scannedID);
                        }
                        /*** [END] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย  ***/
                    }

                    @Override
                    protected void after() {
                        if (output != null) {
                            scannedProduct = output;
                            // (1.2.1) Found ProductStockInfo in LocalDB
                            if (scannedProduct.Status.equals(PRODUCT_STOCK_STATUS_SOLD)) {
                                // (1.2.1-A) SOLD-OUT
                                final String title = "กรุณาตรวจสอบสินค้า";
                                String message = "รายการสินค้ารหัส " + scannedProduct.ProductSerialNumber + " ถูกขายไปแล้ว!";
                                Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                // TODO Auto-generated method stub
                                            }
                                        });
                                setupAlert.show();

                            } else if (scannedProduct.Status.equals(PRODUCT_STOCK_STATUS_DAMAGE)) {
                                showWarningDialog("คำเตือน","รายการสินค้ารหัส " + scannedProduct.ProductSerialNumber + " เป็นเครื่องชำรุด!");
                            } else if (scannedProduct.Status.equals(PRODUCT_STOCK_STATUS_TEAM_DESTROY)) {
                                showWarningDialog("คำเตือน","รายการสินค้ารหัส " + scannedProduct.ProductSerialNumber + " ถูกยุบทีมขาย!");
                            } else {
                                ProductInfo productInfo = getProductInfo(BHPreference.organizationCode(), scannedProduct.ProductID);
                                if(productInfo != null){
                                    scannedSerialList.add(scannedID);
//                              String tCode = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.SubTeamCode() == null ? BHPreference.teamCode() : BHPreference.SubTeamCode();
                                    if (scannedProduct.TeamCode.equals(BHPreference.selectTeamCodeOrSubTeamCode())) {
                                        // (1.2.1-B) CHECKED
                                        iTrueCounter++;
                                        txtTrue.setText(String.valueOf(iTrueCounter));
                                        updateScannedProduct(PRODUCT_STOCK_STATUS_CHECKED);
                                        iScanCounter++;
                                        onProcessCounter();
                                    } else {
                                        // (1.2.1-C) OVER
                                        final String title = "ไม่พบรายการสินค้า";
                                        String message = "รหัส " + scannedProduct.ProductSerialNumber + " เป็นของทีม    " + scannedProduct.TeamCode;
                                        Builder setupAlert;
                                        setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                iFalseCounter++;
                                                txtFalse.setText(String.valueOf(iFalseCounter));
                                                updateScannedProduct(PRODUCT_STOCK_STATUS_OVER);
                                                onProcessCounter();
                                            }
                                        });
                                        setupAlert.show();
                                    }
                                }else{
                                    //ไม่พบรหัส Product
                                    final String title = "ตรวจสอบประเภทสินค้า";
                                    String message = "รายการสินค้ารหัส " + scannedProduct.ProductSerialNumber + " ไม่พบประเภทสินค้า " + scannedProduct.ProductID;
                                    Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                                            .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int whichButton) {
                                                    // TODO Auto-generated method stub
                                                }
                                            });
                                    setupAlert.show();
                                }


                            }
                        } else {
                            // (1.2.2) Not found in LocalDB (OVER) --> Must call web
                            // service for retrieve data
                            onScanProductStockViaWebService(); // call WS
                            // onProcessCounter();
                        }
                    }
                }).start();
            }
        }
    }

    private void updateScannedProduct(final String Status) {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            String scannedStatus = Status.toString();

            @Override
            protected void before() {
                // TODO Auto-generated method stub
                scannedProduct.Status = scannedStatus;
                scannedProduct.ScanDate = new Date();
                scannedProduct.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.SubTeamCode() == null ? BHPreference.teamCode() : BHPreference.SubTeamCode();
                scannedProduct.ImportDate = new Date();
                //scannedProduct.LastUpdateDate = new Date();
                //scannedProduct.LastUpdateBy = BHPreference.employeeID();

                List<ProductStockInfo> tmpList = new ArrayList<ProductStockInfo>();
                boolean bFound = false;
                for (ProductStockInfo item : scannedProductList) {
                    if (item.ProductSerialNumber.equals(scannedProduct.ProductSerialNumber)) {
                        item.Status = scannedStatus;
                        bFound = true;
                    }
                    tmpList.add(item);
                }
                if (!bFound) {
                    tmpList.add(scannedProduct);
                }
                scannedProductList.clear();
                scannedProductList = tmpList;
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                updateProductStock(scannedProduct, false); // In
                // background
                // must
                // call both of
                // Local-Method +
                // Web-Service
            }

        }).start();
    }

    private void addAndUpdateScannedProductOver(final String productStockStatusChecked) {

        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                scannedProduct = addProductStock(scannedProduct);
            }

            @Override
            protected void after() {
                (new BackgroundProcess(activity) {

                    @Override
                    protected void before() {
                        // TODO Auto-generated method stub
                        String scannedStatus = productStockStatusChecked.toString();
                        List<ProductStockInfo> tmpList = new ArrayList<ProductStockInfo>();
                        boolean bFound = false;
                        for (ProductStockInfo item : scannedProductList) {
                            if (item.ProductSerialNumber.equals(scannedProduct.ProductSerialNumber)) {
                                item.Status = scannedStatus;
                                bFound = true;
                            }
                            tmpList.add(item);
                        }
                        if (!bFound) {
                            tmpList.add(scannedProduct);
                        }
                        scannedProductList.clear();
                        scannedProductList = tmpList;
                    }

                    @Override
                    protected void calling() {
                        // TODO Auto-generated method stub
                        if (scannedProduct != null) {
                            updateProductStock(scannedProduct, false); // Must
                            // to
                            // call
                            // again
                            // for
                            // call
                            // both
                            // of
                            // Local-Method
                            // +
                            // Web-Service
                            // in
                            // [updateProductStockStatus]
                        }
                    }
                }).start();
            }
        }).start();

    }

    private void onProcessCounter() {
//        iScanCounter++;
        txtNum.setText(String.valueOf(iScanCounter));

        int waitCount = 0;
        for (ProductStockInfo item : scannedProductList) {
            if (item.Status.equals(PRODUCT_STOCK_STATUS_WAIT)) {
                waitCount++;
            }
        }
        txtProduct.setText(String.valueOf(waitCount)); // สินค้ารอตรวจสอบ
    }

    private void onScanProductStockViaWebService() {
        (new BackgroundProcess(activity) {
            // GetProductStockBySerialNoInputInfo input = new
            // GetProductStockBySerialNoInputInfo();
            // GetProductStockBySerialNoOutputInfo output = new
            // GetProductStockBySerialNoOutputInfo();

            ScanProductStockBySerialNoInputInfo input = new ScanProductStockBySerialNoInputInfo();
            ScanProductStockBySerialNoOutputInfo output = new ScanProductStockBySerialNoOutputInfo();

            @Override
            protected void before() {
                // TODO Auto-generated method stub
                // input.OrganizationCode = BHPreference.organizationCode();
                // input.ProductSerialNumber =
                // showTextScan.getText().toString();
                // // input.Status = PRODUCT_STOCK_STATUS_WAIT;
                // // input.TeamCode = BHPreference.teamCode();

                input.OrganizationCode = BHPreference.organizationCode();
                input.ProductSerialNumber = showTextScan.getText().toString();
                input.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.SubTeamCode() == null ? BHPreference.teamCode() : BHPreference.SubTeamCode();
                //input.TeamCode = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.SubTeamCode() == null ? BHPreference.teamCode() : BHPreference.SubTeamCode();
                input.CreateBy = BHPreference.employeeID();
                input.LastUpdateBy = input.CreateBy;
                input.CreateDate = new Date();
                input.LastUpdateDate = input.CreateDate;
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                // output = TSRService.getProductStockBySerialNo(input, false);
                output = TSRService.scanProductStockBySerialNo(input, false);
            }

            @Override
            protected void after() {
                if (output == null || output.Info == null || output.Info.Status == null) {
                    final String title = "กรุณาตรวจสอบสินค้า";
                    String message = "ไม่พบรหัสสินค้า " + showTextScan.getText().toString() + " อยู่ในระบบ!";
                    Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                            .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // TODO Auto-generated method stub
                                }
                            });
                    setupAlert.show();
                    onProcessCounter();
                } else if (output.Info != null) {

                    // scannedProduct = new ProductStockInfo();
                    // scannedProduct.ProductSerialNumber =
                    // output.Info.ProductSerialNumber;
                    // scannedProduct.OrganizationCode =
                    // output.Info.OrganizationCode;
                    // scannedProduct.ProductID = output.Info.ProductID;
                    // scannedProduct.Type = output.Info.Type;
                    // scannedProduct.TeamCode = output.Info.TeamCode;
                    // scannedProduct.Status = PRODUCT_STOCK_STATUS_CHECKED;
                    // scannedProduct.ScanDate = new Date();
                    // scannedProduct.ScanByTeam = BHPreference.teamCode();
                    // addAndUpdateScannedProductOver(PRODUCT_STOCK_STATUS_CHECKED);
                    // onProcessCounter();

                    final String scannedID = showTextScan.getText().toString();
                    scannedProduct = output.Info;
                    if (scannedProduct.Status.equals(PRODUCT_STOCK_STATUS_SOLD)) {
                        final String title = "กรุณาตรวจสอบสินค้า";
                        String message = "รายการสินค้ารหัส " + scannedProduct.ProductSerialNumber + " ถูกขายไปแล้ว!";
                        Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        // TODO Auto-generated method stub
                                    }
                                });
                        setupAlert.show();
                        onProcessCounter();
                    } else {

                        /*if (BHPreference.IsSaleForCRD()) {
                            if (scannedProduct.TeamCode.equals(BHPreference.selectTeamCodeOrSubTeamCode()) && scannedProduct.CreateBy.equals(BHPreference.employeeID())) {
                                scannedSerialList.add(scannedID);
                                iTrueCounter++;
                                txtTrue.setText(String.valueOf(iTrueCounter));
                                scannedProduct = new ProductStockInfo();
                                scannedProduct.ProductSerialNumber = output.Info.ProductSerialNumber;
                                scannedProduct.OrganizationCode = output.Info.OrganizationCode;
                                scannedProduct.ProductID = output.Info.ProductID;
                                scannedProduct.Type = output.Info.Type;
                                scannedProduct.TeamCode = output.Info.TeamCode;
                                scannedProduct.Status = PRODUCT_STOCK_STATUS_CHECKED;
                                scannedProduct.ScanDate = new Date();
                                scannedProduct.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.SubTeamCode() == null ? BHPreference.teamCode() : BHPreference.SubTeamCode();
                                scannedProduct.ImportDate = new Date();
                                scannedProduct.CreateBy = BHPreference.employeeID();
                                scannedProduct.LastUpdateBy = scannedProduct.CreateBy;
                                scannedProduct.CreateDate = new Date();
                                scannedProduct.LastUpdateDate = scannedProduct.CreateDate;
                                addAndUpdateScannedProductOver(PRODUCT_STOCK_STATUS_CHECKED);
                                iScanCounter++;
                                onProcessCounter();
                            } else {
                                final String title = "ไม่พบรายการสินค้า";
                                String message = "";

                                if (scannedProduct.TeamCode.equals(BHPreference.selectTeamCodeOrSubTeamCode())) {
                                    message = "รหัส " + scannedProduct.ProductSerialNumber + " เป็นของพนักงาน " + scannedProduct.CreateBy;
                                } else {
                                    message = "รหัส " + scannedProduct.ProductSerialNumber + " เป็นของทีม " + scannedProduct.TeamCode;
                                }

                                Builder setupAlert;
                                setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {

                                        //onProcessCounter();
                                    }
                                });
                                setupAlert.show();
                                onProcessCounter();
                            }
                        } else {
                            if (scannedProduct.TeamCode.equals(BHPreference.selectTeamCodeOrSubTeamCode())) {
                                scannedSerialList.add(scannedID);
                                iTrueCounter++;
                                txtTrue.setText(String.valueOf(iTrueCounter));
                                scannedProduct = new ProductStockInfo();
                                scannedProduct.ProductSerialNumber = output.Info.ProductSerialNumber;
                                scannedProduct.OrganizationCode = output.Info.OrganizationCode;
                                scannedProduct.ProductID = output.Info.ProductID;
                                scannedProduct.Type = output.Info.Type;
                                scannedProduct.TeamCode = output.Info.TeamCode;
                                scannedProduct.Status = PRODUCT_STOCK_STATUS_CHECKED;
                                scannedProduct.ScanDate = new Date();
                                scannedProduct.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.SubTeamCode() == null ? BHPreference.teamCode() : BHPreference.SubTeamCode();
                                scannedProduct.ImportDate = new Date();
                                scannedProduct.CreateBy = BHPreference.employeeID();
                                scannedProduct.LastUpdateBy = scannedProduct.CreateBy;
                                scannedProduct.CreateDate = new Date();
                                scannedProduct.LastUpdateDate = scannedProduct.CreateDate;
                                addAndUpdateScannedProductOver(PRODUCT_STOCK_STATUS_CHECKED);
                                iScanCounter++;
                                onProcessCounter();
                            } else {
                                final String title = "ไม่พบรายการสินค้า";
                                String message = "รหัส " + scannedProduct.ProductSerialNumber + " เป็นของทีม " + scannedProduct.TeamCode;
                                Builder setupAlert;
                                setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        scannedSerialList.add(scannedID);
                                        iFalseCounter++;
                                        txtFalse.setText(String.valueOf(iFalseCounter));
                                        scannedProduct = new ProductStockInfo();
                                        scannedProduct.ProductSerialNumber = output.Info.ProductSerialNumber;
                                        scannedProduct.OrganizationCode = output.Info.OrganizationCode;
                                        scannedProduct.ProductID = output.Info.ProductID;
                                        scannedProduct.Type = output.Info.Type;
                                        scannedProduct.TeamCode = output.Info.TeamCode;
                                        scannedProduct.Status = PRODUCT_STOCK_STATUS_OVER;
                                        scannedProduct.ScanDate = new Date();
                                        scannedProduct.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.SubTeamCode() == null ? BHPreference.teamCode() : BHPreference.SubTeamCode();
                                        scannedProduct.ImportDate = new Date();
                                        scannedProduct.CreateBy = BHPreference.employeeID();
                                        scannedProduct.LastUpdateBy = scannedProduct.CreateBy;
                                        scannedProduct.CreateDate = new Date();
                                        scannedProduct.LastUpdateDate = scannedProduct.CreateDate;
                                        addAndUpdateScannedProductOver(PRODUCT_STOCK_STATUS_OVER);
                                        onProcessCounter();
                                    }
                                });
                                setupAlert.show();
                            }
                        }*/

                        if (scannedProduct.TeamCode.equals(BHPreference.selectTeamCodeOrSubTeamCode())) {
                            scannedSerialList.add(scannedID);
                            iTrueCounter++;
                            txtTrue.setText(String.valueOf(iTrueCounter));
                            scannedProduct = new ProductStockInfo();
                            scannedProduct.ProductSerialNumber = output.Info.ProductSerialNumber;
                            scannedProduct.OrganizationCode = output.Info.OrganizationCode;
                            scannedProduct.ProductID = output.Info.ProductID;
                            scannedProduct.Type = output.Info.Type;
                            scannedProduct.TeamCode = output.Info.TeamCode;
                            scannedProduct.Status = PRODUCT_STOCK_STATUS_CHECKED;
                            scannedProduct.ScanDate = new Date();
                            scannedProduct.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.SubTeamCode() == null ? BHPreference.teamCode() : BHPreference.SubTeamCode();
                            scannedProduct.ImportDate = new Date();
                            scannedProduct.CreateBy = BHPreference.employeeID();
                            scannedProduct.LastUpdateBy = scannedProduct.CreateBy;
                            scannedProduct.CreateDate = new Date();
                            scannedProduct.LastUpdateDate = scannedProduct.CreateDate;
                            addAndUpdateScannedProductOver(PRODUCT_STOCK_STATUS_CHECKED);
                            iScanCounter++;
                            onProcessCounter();
                        } else {
                            final String title = "ไม่พบรายการสินค้า";
                            String message = "รหัส " + scannedProduct.ProductSerialNumber + " เป็นของทีม " + scannedProduct.TeamCode;
                            Builder setupAlert;
                            setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    scannedSerialList.add(scannedID);
                                    iFalseCounter++;
                                    txtFalse.setText(String.valueOf(iFalseCounter));
                                    scannedProduct = new ProductStockInfo();
                                    scannedProduct.ProductSerialNumber = output.Info.ProductSerialNumber;
                                    scannedProduct.OrganizationCode = output.Info.OrganizationCode;
                                    scannedProduct.ProductID = output.Info.ProductID;
                                    scannedProduct.Type = output.Info.Type;
                                    scannedProduct.TeamCode = output.Info.TeamCode;
                                    scannedProduct.Status = PRODUCT_STOCK_STATUS_OVER;
                                    scannedProduct.ScanDate = new Date();
                                    scannedProduct.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.SubTeamCode() == null ? BHPreference.teamCode() : BHPreference.SubTeamCode();
                                    scannedProduct.ImportDate = new Date();
                                    scannedProduct.CreateBy = BHPreference.employeeID();
                                    scannedProduct.LastUpdateBy = scannedProduct.CreateBy;
                                    scannedProduct.CreateDate = new Date();
                                    scannedProduct.LastUpdateDate = scannedProduct.CreateDate;
                                    addAndUpdateScannedProductOver(PRODUCT_STOCK_STATUS_OVER);
                                    onProcessCounter();
                                }
                            });
                            setupAlert.show();
                        }
                    }

                }
            }
        }).start();
    }


    public boolean isConnectingToInternet() {
        ConnectivityManager cm = (ConnectivityManager)activity.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    public void  ScanProductStockForCRDBySerialNo(final String ProductSerialNumber) {
        (new BackgroundProcess(activity){

            ScanProductStockForCRDBySerialNoInputInfo input = new ScanProductStockForCRDBySerialNoInputInfo();
            ScanProductStockForCRDBySerialNoOutputInfo output = new ScanProductStockForCRDBySerialNoOutputInfo();

            @Override
            protected void before() {
                input.OrganizationCode = BHPreference.organizationCode();
                input.ProductSerialNumber = ProductSerialNumber;
            }

            @Override
            protected void calling() {

                output = TSRService.ScanProductStockForCRDBySerialNo(input, false);
            }

            @Override
            protected void after() {

                if (output != null) {
                    if (output.ResultCode == 0) {
                        if (output.Info.Status.equals(PRODUCT_STOCK_STATUS_SOLD)) {
                            // (1.2.1-A) SOLD-OUT
                            showWarningDialog("กรุณาตรวจสอบสินค้า", "รายการสินค้ารหัส " + output.Info.ProductSerialNumber + " ถูกขายไปแล้ว!");
                        } else if (output.Info.Status.equals(PRODUCT_STOCK_STATUS_DAMAGE)) {
                            showWarningDialog("คำเตือน","รายการสินค้ารหัส " + output.Info.ProductSerialNumber + " เป็นเครื่องชำรุด!");
                        } else if (output.Info.Status.equals(PRODUCT_STOCK_STATUS_TEAM_DESTROY)) {
                            showWarningDialog("คำเตือน","รายการสินค้ารหัส " + output.Info.ProductSerialNumber + " ถูกยุบทีมขาย!");
                        } else {

                            ProductInfo productInfo = getProductInfo(BHPreference.organizationCode(), output.Info.ProductID);
                            if(productInfo != null){

                                if (output.Info.TeamCode.equals(BHPreference.selectTeamCodeOrSubTeamCode())) {
                                    if (output.Info.CreateBy.equals(BHPreference.employeeID())){
                                        if (output.Info.Status.equals(PRODUCT_STOCK_STATUS_WAIT)) {
                                            ScanProductStockForCRDSuccess(output.Info, false);
                                        } else {
                                            updateScanProductStockForCRDSuccess(output.Info);
                                        }
                                    } else {
                                        if (output.Info.Status.equals(PRODUCT_STOCK_STATUS_WAIT)) {
                                            final String title = "กรุณาตรวจสอบสินค้า";
                                            String message = "รหัส " + output.Info.ProductSerialNumber + " เป็นของพนักงาน " + output.Info.CreateBy + " ยืนยันการยืมเครื่อง";
                                            Builder setupAlert;
                                            setupAlert = new AlertDialog.Builder(activity)
                                                    .setTitle(title)
                                                    .setMessage(message)
                                                    .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {
                                                            ScanProductStockForCRDSuccess(output.Info, true);
                                                        }
                                                    })
                                                    .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int whichButton) {

                                                        }
                                                    });
                                            setupAlert.show();
                                        } else {
                                            if (output.Info.OldOwnerEmpID != null && output.Info.NewOwnerEmpID != null) {

                                                if (output.Info.OldOwnerEmpID.equals(BHPreference.employeeID())) {
                                                    showWarningDialog("ไม่พบรายการสินค้า", "รหัส " + output.Info.ProductSerialNumber + " ถูกยืมไปแล้ว");

                                                    ///
                                                    TSRController.deleteProductStockBySerialNumber(BHPreference.organizationCode(),  output.Info.ProductSerialNumber);
                                                    TSRController.addProductStockForCRD(output.Info);


                                                    List<ProductStockInfo> tmpList = new ArrayList<ProductStockInfo>();
                                                    for (ProductStockInfo item : scannedProductList) {
                                                        if (!item.ProductSerialNumber.equals(output.Info.ProductSerialNumber)) {
                                                            tmpList.add(item);
                                                        }
                                                    }
                                                    scannedProductList.clear();
                                                    scannedProductList = tmpList;
                                                    onProcessCounter();
                                                    updateTxtSum();
                                                    ///

                                                } else {
                                                    showWarningDialog("ไม่พบรายการสินค้า", "รหัส " + output.Info.ProductSerialNumber + " เป็นของพนักงาน " + output.Info.CreateBy + " ถูกใช้งานแล้ว");
                                                }
                                            } else {
                                                showWarningDialog("ไม่พบรายการสินค้า", "รหัส " + output.Info.ProductSerialNumber + " เป็นของพนักงาน " + output.Info.CreateBy + " ถูกใช้งานแล้ว");
                                            }
                                        }
                                    }
                                } else {
                                    //showWarningDialog("ไม่พบรายการสินค้า", "รหัส " + output.Info.ProductSerialNumber + " เป็นของทีม " + output.Info.TeamCode);
                                    onScanProductStockViaWebService();
                                }
                            }else{
                                //ไม่พบรหัส Product
                                showWarningDialog("ตรวจสอบประเภทสินค้า", "รายการสินค้ารหัส " + output.Info.ProductSerialNumber + " ไม่พบประเภทสินค้า " + scannedProduct.ProductID);
                            }
                        }


                    } else if (output.ResultCode == 3000) {
                        showWarningDialog("กรุณาตรวจสอบสินค้า", "ไม่พบรหัสสินค้า " + ProductSerialNumber + " อยู่ในระบบ!");
                    } else {
                        showWarningDialog("แจ้งเตือน", output.ResultDescription);
                    }
                }

            }
        }).start();
    }

    public void  ScanProductStockForCRDSuccess(final ProductStockInfo info, final boolean IsMoveByEmp) {


        (new BackgroundProcess(activity) {

            UpdateScanProductStockForCRDInputInfo input = new UpdateScanProductStockForCRDInputInfo();
            UpdateScanProductStockForCRDOutputInfo output = new UpdateScanProductStockForCRDOutputInfo();

            @Override
            protected void before() {
                input.OrganizationCode = BHPreference.organizationCode();
                input.ProductSerialNumber = info.ProductSerialNumber;
                input.EmployeeID = BHPreference.employeeID();
                input.TeamCode = BHPreference.selectTeamCodeOrSubTeamCode();
            }

            @Override
            protected void calling() {
                output = TSRService.UpdateScanProductStockForCRD(input, false);
            }

            @Override
            protected void after() {
                if(output != null) {
                    if(output.ResultCode == 0) {


                        if (IsMoveByEmp) {

                            (new BackgroundProcess(activity) {
                                AddProductStockHistoryInputInfo pshInput;
                                AddProductStockHistoryOutputInfo pshOutput = new AddProductStockHistoryOutputInfo();
                                ProductStockHistoryInfo pshInfo = new ProductStockHistoryInfo();


                                @Override
                                protected void before() {
                                    Date date = new Date();

                                    pshInfo.ProductStockHisID = DatabaseHelper.getUUID();
                                    pshInfo.OrganizationCode = BHPreference.organizationCode();
                                    pshInfo.ProductSerialNumber = info.ProductSerialNumber;
                                    pshInfo.ProductID = info.ProductID;
                                    pshInfo.Type = info.Type;
                                    pshInfo.TeamCode = info.TeamCode;
                                    pshInfo.Status = info.Status;
                                    pshInfo.ImportDate = info.ImportDate;
                                    pshInfo.NewTeamCode = output.Info.TeamCode;
                                    pshInfo.NewStatus = output.Info.Status;
                                    //pshInfo.Result = "";
                                    //pshInfo.SyncedDate;
                                    pshInfo.CreateDate = date;
                                    pshInfo.CreateBy = BHPreference.employeeID();
                                    pshInfo.LastUpdateDate = date;
                                    pshInfo.LastUpdateBy = BHPreference.employeeID();

                                    /*** [START] :: Fixed-[BHPROJ-1036-7663] ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย - แก้ไขให้ CRD สามารถยืมเครื่องได้ ***/
                                    pshInfo.OldOwnerEmpID = info.CreateBy;   // พนักงานเจ้าของสินค้า (คนเก่าก่อนการยืม)
                                    pshInfo.NewOwnerEmpID = output.Info.CreateBy;   // พนักงานที่ขอยืมสินค้ามาขาย
                                    pshInfo.IsMoveByEmp = true;      // บอกว่าเป็นการโอนย้าย/โอนยิมสินค้ารายบคุลล (สำหรับฝ่าย Sale จะเป็นการโอนย้ายรายทีม แต่ CRD เป็นการโอนย้ายรายบุคคล)
                                    /*** [END] :: Fixed-[BHPROJ-1036-7663] ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย - แก้ไขให้ CRD สามารถยืมเครื่องได้ ***/

                                    pshInput = AddProductStockHistoryInputInfo.from(pshInfo);
                                }

                                @Override
                                protected void calling() {
                                    pshOutput = TSRService.AddProductStockHistory(pshInput, false);
                                }

                                @Override
                                protected void after() {
                                    if (pshOutput != null) {
                                        if (pshOutput.ResultCode == 0) {
                                            updateScanProductStockForCRDSuccess(output.Info);
                                        } else {
                                            showWarningDialog("แจ้งเตือน", output.ResultDescription);
                                        }
                                    }
                                }
                            }).start();

                        } else {
                            updateScanProductStockForCRDSuccess(output.Info);
                        }


                    }else if (output.ResultCode == 3000) {
                        showWarningDialog("กรุณาตรวจสอบสินค้า", "ไม่พบรหัสสินค้า " + info.ProductSerialNumber + " อยู่ในระบบ!");
                    } else {
                        showWarningDialog("แจ้งเตือน", output.ResultDescription);
                    }
                }
            }
        }).start();
    }

    public void  updateScanProductStockForCRDSuccess(ProductStockInfo info) {
        TSRController.deleteProductStockBySerialNumber(BHPreference.organizationCode(), info.ProductSerialNumber);
        TSRController.addProductStockForCRD(info);

        ///
        scannedProduct = info;
        iTrueCounter++;
        txtTrue.setText(String.valueOf(iTrueCounter));
        updateScannedProductListForCRD();
        iScanCounter++;
        onProcessCounter();
        updateTxtSum();
    }


    public void updateScannedProductListForCRD() {
        List<ProductStockInfo> tmpList = new ArrayList<ProductStockInfo>();
        boolean bFound = false;
        for (ProductStockInfo item : scannedProductList) {
            if (item.ProductSerialNumber.equals(scannedProduct.ProductSerialNumber)) {
                item.Status = scannedProduct.Status;
                bFound = true;
            }
            tmpList.add(item);
        }
        if (!bFound) {
            tmpList.add(scannedProduct);
        }
        scannedProductList.clear();
        scannedProductList = tmpList;
    }

    public void updateTxtSum() {
        int stockCount = 0;

        for (ProductStockInfo item : scannedProductList) {
            if (!item.Status.equals(PRODUCT_STOCK_STATUS_OVER)) {
                stockCount++;
            }
        }

        txtSum.setText(String.valueOf(stockCount)); // สินค้าทั้งหมดที่ต้อง scan(สินค้าที่เบิกจากคลัง)
    }
}
