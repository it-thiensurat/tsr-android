package th.co.thiensurat.fragments.products.stock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

//import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.info.DamageInfo;
import th.co.thiensurat.data.info.DamageProductInfo;
import th.co.thiensurat.data.info.ProductStockInfo;

/**
 * Created by Tanawut on 5/3/2558.
 */
public class NewCheckStockReturnProductFragment extends BHFragment {

    private int REQUEST_QR_SCAN = 0;

    @InjectView
    private ImageButton ibScanBarcode;
    @InjectView
    private TextView txtProductSerialNumber;
    @InjectView
    private TextView txtProductName;
    @InjectView
    private TextView txtProductTeam;
    @InjectView
    private Spinner spinnerReturnProblem;
    @InjectView
    private EditText editTextOther;
    @InjectView
    private LinearLayout visibleForDeveloperMode;
    @InjectView
    private EditText edtProductSerialNumber;
    @InjectView
    private Button btnProductSerialNumber;

    private List<DamageInfo> damageList;
    private ProductStockInfo productStock;
    private DamageProductInfo damageProductInfo = new DamageProductInfo();
    private String damage;


    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_check_stock_return_product;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_check_stock;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_save};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        if(BHGeneral.DEVELOPER_MODE){
            visibleForDeveloperMode.setVisibility(View.VISIBLE);
            btnProductSerialNumber.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bindData(edtProductSerialNumber.getText().toString());
                }
            });
        }
        bindProblemType();
        setWidgetsEventListener();
    }

    private void setWidgetsEventListener() {
        spinnerReturnProblem.setEnabled(false);

        ibScanBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        spinnerReturnProblem.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                damage = damageList.get(position).DamageCode;
                //                  showMessage(damage);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    private void bindProblemType() {

        damageList = new ArrayList<DamageInfo>();
        damageList.add(new DamageInfo("", ""));
        damageList.add(new DamageInfo("DAMAGE", "เครื่องชำรุด"));
        damageList.add(new DamageInfo("TEAM_DESTROY", "ยุบทีมขาย"));

        List<String> damageListName = new ArrayList<String>();
        for (DamageInfo item : damageList) {
            damageListName.add(item.DamageName);
        }

        BHSpinnerAdapter<String> arrayDamage = new BHSpinnerAdapter<String>(activity, damageListName);
        spinnerReturnProblem.setAdapter(arrayDamage);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_QR_SCAN && resultCode == Activity.RESULT_OK) {
            String Content = intent.getStringExtra(Intents.Scan.RESULT);
            bindData(Content);
        } else if (requestCode == REQUEST_QR_SCAN && resultCode == Activity.RESULT_CANCELED) {

        }
    }

    private void bindData(final String content) {
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                productStock = TSRController.getProductStock(content);
            }

            @Override
            protected void after() {
                if (productStock != null) {
                    if ((productStock.Status.equals(ProductStockController.ProductStockStatus.WAIT.toString())) || (productStock.Status.equals(ProductStockController.ProductStockStatus.CHECKED.toString()))) {
                        txtProductSerialNumber.setText(productStock.ProductSerialNumber);
                        txtProductName.setText(productStock.ProductName);
                        txtProductTeam.setText(productStock.TeamCode);
                        spinnerReturnProblem.setEnabled(true);
                    } else {
                        showWarningDialog("คำเตือน", "ไม่สามารถทำคืนสินค้าได้ เนื่องจากเงื่อนไขไม่ถูกต้อง! (STATUS=" + productStock.Status + ")");
                    }
                } else {
                    showWarningDialog("คำเตือน", "ไม่พบสินค้า กรุณาทำการสแกนใหม่");
                }
            }
        }).start();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_save:
                if (productStock != null) {
                    if (damage != "") {
                        Save();
                    } else {
                        showWarningDialog("คำเตือน", "กรุณาเลือกสาเหตุ");
                    }
                } else {
                    showWarningDialog("คำเตือน", "ไม่พบสินค้าที่จะแจ้งคืน");
                }
                break;
            default:
                break;
        }
    }

    private void Save() {
        (new BackgroundProcess(activity) {

            @Override
            protected void before() {

                //AddDamageProduct//
                damageProductInfo.DamageProductID = DatabaseHelper.getUUID();
                damageProductInfo.OrganizationCode = BHPreference.organizationCode();
                damageProductInfo.EmpID = BHPreference.employeeID();
                damageProductInfo.TeamCode = BHPreference.teamCode();
                damageProductInfo.SubTeamCode = BHPreference.SubTeamCode();
                damageProductInfo.ReturnDate = new Date();
                damageProductInfo.ReturnType = damage;
                damageProductInfo.ReturnCause = editTextOther.getText().toString();
                damageProductInfo.ProductSerialNumber = productStock.ProductSerialNumber;
                damageProductInfo.CreateDate = new Date();
                damageProductInfo.CreateBy = BHPreference.employeeID();
                damageProductInfo.LastUpdateDate = new Date();
                damageProductInfo.LastUpdateBy = BHPreference.employeeID();
                damageProductInfo.SyncedDate = new Date();
                //AddDamageProduct//

                //UpdateProductStock//
                productStock.Status = damage;
                productStock.LastUpdateDate = new Date();
                //UpdateProductStock//
            }

            @Override
            protected void calling() {
                addDamageProduct(damageProductInfo, false);
                updateProductStock(productStock, false);
            }

            @Override
            protected void after() {
                AlertDialog.Builder setupAlert;
                setupAlert = new AlertDialog.Builder(activity).setTitle("คำเตือน")
                        .setMessage("บันทึกข้อมูลเรียบร้อย")
                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                showLastView();
                            }
                        });
                setupAlert.show();
            }
        }).start();
    }
}
