package th.co.thiensurat.fragments.impound;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.ContractAdapter;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.ImpoundProductController;
import th.co.thiensurat.data.controller.LogScanProductSerialController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatus;
import th.co.thiensurat.data.info.ImpoundProductInfo;
import th.co.thiensurat.data.info.LogScanProductSerialInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.share.BarcodeScanFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment.ScanCallBack;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetContractForSearchInputInfo;
import th.co.thiensurat.service.data.GetContractForSearchOutputInfo;
import th.co.thiensurat.service.data.GetImpoundProductsByTeamCodeForSearchInputInfo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class ImpoundProductListFragment extends BHFragment {

    public static String FRAGMENT_IMPOUND_PRODUCT_LIST_TAG = "impound_product_list_tag";

    @InjectView
    private ListView listCustomer;
    @InjectView
    private Button btnSerch;
    @InjectView
    private EditText etSerialNumber;
    @InjectView
    private TextView textSearch;

    List<SalePaymentPeriodInfo> info = new ArrayList<SalePaymentPeriodInfo>();
    String Date, Time, ProductScan, name;


    private List<ImpoundProductInfo> impoundProductList = null;
    private ContractAdapter impoundAdapter;

    private AlertDialog alertDialog;

    @Override
    public String fragmentTag() {
        return FRAGMENT_IMPOUND_PRODUCT_LIST_TAG;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_impound_product_list;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back};
    }

    @Override
    protected int titleID() {
        return R.string.title_remove;
    }

    protected void SetHeader() {
        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.list_contract_header, listCustomer, false);
        listCustomer.addHeaderView(header, null, false);
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        btnSerch.setText("ค้นหา");
        textSearch.setText("ค้นหา");
        SetHeader();

        impoundProductList = new ArrayList<ImpoundProductInfo>();

        impoundAdapter = new ContractAdapter(activity, R.layout.list_contract_item, impoundProductList);
        listCustomer.setAdapter(impoundAdapter);

        bindImpoundProductList(null);


        btnSerch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etSerialNumber.getText().toString().trim().isEmpty()) {
                    BHUtilities.alertDialog(activity, "คำเตือน", "กรุณากรอกข้อมูลที่ต้องการค้นหา").show();
                } else {
                    bindImpoundProductList(etSerialNumber.getText().toString());
                }
            }
        });

        listCustomer.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(alertDialog == null){
                    final ImpoundProductInfo info = (ImpoundProductInfo) listCustomer.getItemAtPosition(position);

                    (new BackgroundProcess(activity) {
                        List<ImpoundProductInfo> checkImpoundProduct;

                        @Override
                        protected void before() {
                            checkImpoundProduct = TSRController.getImpoundProductByTeamCodeForSearch(BHPreference.organizationCode(), BHPreference.teamCode(), null, info.RefNo);
                        }

                        @Override
                        protected void calling() {
                            if(checkImpoundProduct == null || checkImpoundProduct.size() == 0){
                                TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), info.RefNo);
                            }
                        }

                        @Override
                        protected void after() {
                            if (info.IsMigrate) {
                                dialogBoxIsMigrate(info);
                            } else {
                                BarcodeScanFragment fm = callBarcodeScan(info.OrganizationCode, info.ProductSerialNumber);
                                fm.setTitle(R.string.title_remove);
                                fm.setViewTitle(R.string.caption_customer_detail_impound);
                                showNextView(fm);
                            }
                        }
                    }).start();
                }
            }
        });
    }

    private void dialogBoxIsMigrate(final ImpoundProductInfo info) {
        Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle(getResources().getString(titleID()))
                .setMessage("ตรวจพบเป็นข้อมูลที่มาจากการ Migrate ต้องการสแกนสินค้าหรือไม่")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_scan), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        saveLogScanProductSerial(true, info);

                        BarcodeScanFragment fm = callBarcodeScan(info.OrganizationCode, info.ProductSerialNumber);
                        fm.setTitle(R.string.title_remove);
                        fm.setViewTitle(R.string.caption_customer_detail_impound);
                        showNextView(fm);

                        alertDialog = null;
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_not_scan), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        saveLogScanProductSerial(false, info);

                        if (info.ProductSerialNumber != null) {
                            ContractInfo result = TSRController.getContractBySerialNo(info.OrganizationCode, info.ProductSerialNumber, "");

                            if (result != null) {
                                ImpoundProductDetailFragment.Data data1 = new ImpoundProductDetailFragment.Data();
                                data1.Serialnumber = result.ProductSerialNumber;
                                data1.Name = result.ProductName;
                                data1.IsImpoundProductOtherTeam = false;
                                ImpoundProductDetailFragment fmProductCheck = BHFragment.newInstance(ImpoundProductDetailFragment.class, data1);
                                showNextView(fmProductCheck);
                            } else {
                                String title = (getResources().getString(titleID()));
                                String message = "ไม่พบข้อมูลสินค้า";
                                BHUtilities.alertDialog(activity, title, message).show();
                            }
                        } else {
                            String title = (getResources().getString(titleID()));
                            String message = "ไม่พบรหัสสินค้า";
                            BHUtilities.alertDialog(activity, title, message).show();
                        }

                        alertDialog = null;
                    }
                });
        alertDialog = setupAlert.show();
    }

    private void saveLogScanProductSerial(boolean isScanProductSerial, ImpoundProductInfo info) {
        LogScanProductSerialInfo logInfo = new LogScanProductSerialInfo();

        logInfo.LogScanProductSerialID = DatabaseHelper.getUUID();
        logInfo.OrganizationCode = BHPreference.organizationCode();
        logInfo.TaskType = LogScanProductSerialController.LogScanProductSerialTaskType.ImpoundProduct.toString();        // ประเภทของการร้องขอ (ขออนุมัติถอดเครื่อง=Impound, ขออนุมัติเปลี่ยนเครื่อง=ChangeProduct, ขออนุมัติเปลี่ยนสัญญา=ChangeContract)
        logInfo.RequestID = info.ImpoundProductID;                        // GUID การร้องขอ (ขออนุมัติถอดเครื่องใช้-ImpoundProductID, ขออนุมัติเปลี่ยนเครื่องใช้-ChangeProductID, ขออนุมัติเปลี่ยนสัญญาใช้-ChangeContractID)
        logInfo.IsScanProductSerial = isScanProductSerial;                // บอกว่ามีการ  Scan Product Serial Number หรือไม่
        logInfo.RefNo = info.RefNo;
        logInfo.ProductSerialNumber = info.ProductSerialNumber;            // หมายเลขเครื่อง
        logInfo.Status = ImpoundProductController.ImpoundProductStatus.COMPLETED.toString();        // สถานะคำร้อง (REQUEST=คำร้องขอรออนุมัติ, APPROVED=คำร้องขอที่ถูกอนุมัติแล้วแต่รอดำเนินการ, COMPLETED=คำร้องขอที่ดำเนินการเรียบร้อยแล้ว)
        logInfo.CreateDate = new Date();
        logInfo.CreateBy = BHPreference.employeeID();

        TSRController.addLogScanProductSerial(logInfo, true);
    }

    private void bindImpoundProductList(final String SearchText) {
        (new BackgroundProcess(activity) {
            @Override
            protected void before() {
                impoundProductList.clear();
            }

            @Override
            protected void calling() {
                List<ImpoundProductInfo> result = new ArrayList<ImpoundProductInfo>();
                result = TSRController.getImpoundProductByTeamCodeForSearch(BHPreference.organizationCode(), BHPreference.teamCode(), SearchText, null);

                if (result != null && result.size() > 0) {
                    impoundProductList.addAll(result);
                } else if (SearchText != null) {
                    GetImpoundProductsByTeamCodeForSearchInputInfo input = new GetImpoundProductsByTeamCodeForSearchInputInfo();

                    input.OrganizationCode = BHPreference.organizationCode();
                    input.TeamCode = BHPreference.teamCode();
                    input.SearchText = etSerialNumber.getText().toString();

                    result = TSRService.getImpoundProductOtherTeamForSearch(input, false).Info;

                    if (result != null && result.size() > 0) {
                        impoundProductList.addAll(result);
                    }
                }
            }

            @Override
            protected void after() {
                listCustomer.clearChoices();
                impoundAdapter.notifyDataSetChanged();

                if (impoundProductList == null || impoundProductList.size() == 0) {
                    BHUtilities.alertDialog(activity, "คำเตือน", "ไม่พบข้อมูล").show();
                }
            }
        }).start();
    }

    public BarcodeScanFragment callBarcodeScan(final String orgCodeSelected, final String serialNoSelected) {
        return BHFragment.newInstance(BarcodeScanFragment.class, new ScanCallBack() {

            @Override
            public void onResult(BHParcelable data) {
                final BarcodeScanFragment.Result barcodeResult = (BarcodeScanFragment.Result) data;

                if (!barcodeResult.barcode.equals(serialNoSelected)) {
                    final String title = "กรุณาตรวจสอบสินค้า";
                    String message = "รหัสสินค้าไม่ตรงกับรายการที่ท่านเลือก!";
                    Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                            .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {
                                }
                            });
                    setupAlert.show();
                    //showMessage("รหัสสินค้าไม่ตรงกับรายการที่ท่านเลือก!");
                } else {
                    (new BackgroundProcess(activity) {
                        ContractInfo result;

                        @Override
                        protected void calling() {
                            /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                            result = getContractBySerialNo(orgCodeSelected, serialNoSelected, ContractStatus.NORMAL.toString());
                            result = getContractBySerialNo(orgCodeSelected, serialNoSelected, "");
                        }

                        @Override
                        protected void after() {
                            if (result != null) {
                                ImpoundProductDetailFragment.Data data1 = new ImpoundProductDetailFragment.Data();
                                data1.Serialnumber = result.ProductSerialNumber;
                                data1.Name = result.ProductName;
                                data1.IsImpoundProductOtherTeam = false;
                                ImpoundProductDetailFragment fmProductCheck = BHFragment.newInstance(ImpoundProductDetailFragment.class, data1);
                                showNextView(fmProductCheck);

                            } else {
                                final String title = "กรุณาตรวจสอบสินค้า";
                                String message = "ไม่พบสินค้ากรุณาทำการสแกนใหม";
                                Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                            }
                                        });
                                setupAlert.show();
                                //showMessage("ไม่พบสินค้า");
                            }
                        }
                    }).start();
                }
            }

            @Override
            public String onNextClick() {
                return serialNoSelected;//ProductScan;
            }

        });
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }

}
