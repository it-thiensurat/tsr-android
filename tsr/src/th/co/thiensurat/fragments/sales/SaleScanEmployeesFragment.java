package th.co.thiensurat.fragments.sales;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.EmployeeAdapter;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;

public class SaleScanEmployeesFragment extends BHFragment implements EmployeeAdapter.ItemClickListener {

    private String STATUS_CODE = "02";
    private int REQUEST_QR_SCAN = 0;
    // public static final String EMPLOYEE_LIST =
    // "th.co.thiensurat.salescanemployee.employeelist";
    public static final String EMPLOYEE_SELECTED = "th.co.thiensurat.salescanemployee.employeeselected";

    // private String empID;
    // private String tempCode;

    @InjectView
    private TextView textShowName;
    @InjectView
    private ImageButton ibScanBarcode;
    @InjectView
    private Spinner spinnerEmp;
  
    @InjectView
    private LinearLayout linearLayoutHeadNumber;
    @InjectView
    private TextView txtNumber1;
    @InjectView
    private TextView txtNumber2;
    @InjectView
    private TextView txtNumber3;
    @InjectView
    private TextView txtNumber4;
    @InjectView
    private TextView txtNumber5;

    @InjectView
    private RecyclerView recyclerview;

    // private ContractInfo CONTRACT;
    private ContractInfo contract;
    // private ProductStockInfo PRODUCTSTOCK;
    private ProductStockInfo productStock = null;
    private List<EmployeeInfo> employeeList = null;
    private List<EmployeeInfo> employeeListTemp = new ArrayList<EmployeeInfo>();
    // private EmployeeInfo selectedEmployee = null;
    // private TeamEmployeeInfo teamemp = null;
    private EmployeeInfo employee = null;
    String selectedEmpID = null;

    private boolean isAutoCompletePreSaleEmployeeCode;
    private List<EmployeeInfo> employeeForPreSaleList = null;

    private EmployeeAdapter employeeAdapter;

    /*@InjectView
    public EditText PreSaleEmployeeCode; // รหัสพนักงานผู้แนะนำ*/
    @InjectView
    AutoCompleteTextView autoCompletePreSaleEmployeeCode;//รหัสพนักงานผู้แนะนำ

    @InjectView
    public EditText PreSaleEmployeeName; // ชื่อ-นามสกุลผู้แนะนำ

    @Override
    public String fragmentTag() {
        // TODO Auto-generated method stub
        /*return SaleCustomerAddressMainFragment.FRAGMENT_SALE_CUSTOMER_ADDRESS_TAG;		// PARADA [Tab-Address]
//		return NewSaleCustomerAddressCardFragment.FRAGMENT_SALE_CUSTOMER_ADDRESS_TAG;	// PARADA [Separate-Address]
        public static String FRAGMENT_SALE_CUSTOMER_ADDRESS_TAG = "sale_customer_address_tag";*/

        return "sale_customer_address_tag";
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_sales;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_scanemployees;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[] { R.string.button_back, R.string.button_save_employee };
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        Log.e("xxxxxx2","xxxxx");


        if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
            saveStatusCode();
            txtNumber2.setBackgroundResource(R.drawable.circle_number_sale_color_red);
        }

        if (savedInstanceState != null) {
            // employeeList = savedInstanceState
            // .getParcelableArrayList(EMPLOYEE_LIST);
            employee = savedInstanceState.getParcelable(EMPLOYEE_SELECTED);
        }

        clearData();
        ContactDB();
        // if (employeeList == null) {
        // ContactDB();
        // } else
        // bindEmployeeList();

        // if (selectedEmployee != null) {
        // showSelectedEmployee();
        // }


        ibScanBarcode.setOnClickListener(new View.OnClickListener() {
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

        spinnerEmp.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                selectedEmpID = (String) parent.getItemAtPosition(position);
                if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                    String empID = employeeListTemp.get(position - 1).EmpID;
                    bindSelectedEmployee(empID, BHPreference.teamCode());
//                    Log.e("sale_select",empID+","+BHPreference.teamCode());
//                    Log.e("Emp id", employeeListTemp.get(position - 1).EmpID);
//                    Log.e("Emp position", employeeListTemp.get(position - 1).PositionID);
//                    Toast.makeText(activity, employeeListTemp.get(position - 1).SaleCode, Toast.LENGTH_LONG).show();
                } else {
                    Log.e("sale_select2",BHPreference.teamCode());
                    bindSelectedEmployee(BHPreference.employeeID(), BHPreference.teamCode());
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                employeeForPreSaleList = TSRController.getEmployeeForPreSale();
            }

            @Override
            protected void after() {
                if (employeeForPreSaleList != null && employeeForPreSaleList.size() > 0) {
                    List<String> strList = new ArrayList<String>();
                    for (EmployeeInfo item : employeeForPreSaleList) {
                        strList.add(item.SaleCode);
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_dropdown_item_1line, strList);
                    autoCompletePreSaleEmployeeCode.setThreshold(0);
                    autoCompletePreSaleEmployeeCode.setAdapter(adapter);
                    autoCompletePreSaleEmployeeCode.setDropDownHeight(LinearLayout.LayoutParams.MATCH_PARENT);

                    Log.e("Emp list", String.valueOf(employeeForPreSaleList));


                }

            }
        }).start();

        autoCompletePreSaleEmployeeCode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isAutoCompletePreSaleEmployeeCode = false;
                String str = getPreSaleEmployeeNameByPreSaleEmployeeCode();
                if (str != null) {
                    PreSaleEmployeeName.setText(str);
                }

                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                in.hideSoftInputFromWindow(autoCompletePreSaleEmployeeCode.getWindowToken(), 0);
            }
        });

        autoCompletePreSaleEmployeeCode.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View paramView, MotionEvent paramMotionEvent) {
                // TODO Auto-generated method stub

                if (paramMotionEvent.getAction() == MotionEvent.ACTION_DOWN && !isAutoCompletePreSaleEmployeeCode) {
                    isAutoCompletePreSaleEmployeeCode = true;
                    //autoCompletePreSaleEmployeeCode.showDropDown();
                    //autoCompletePreSaleEmployeeCode.requestFocus();

                    autoCompletePreSaleEmployeeCode.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //adapter.notifyDataSetChanged();
                            //autoCompletePreSaleEmployeeCode.setSelection(autoCompletePreSaleEmployeeCode.getText().length());
                            autoCompletePreSaleEmployeeCode.showDropDown();
                        }
                    }, 500);
                }

                return false;
            }
        });

//        autoCompletePreSaleEmployeeCode.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//                in.hideSoftInputFromWindow(autoCompletePreSaleEmployeeCode.getWindowToken(), 0);
////                String provinceCode = getProvinceCode();
////                if (!provinceCode.equals("")) {
////                    bindDistrict(provinceCode);
////                }
//            }
//        });

        autoCompletePreSaleEmployeeCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b && isAutoCompletePreSaleEmployeeCode) {
                    isAutoCompletePreSaleEmployeeCode = false;

                    String str = getPreSaleEmployeeNameByPreSaleEmployeeCode();
                    if (str != null) {
                        PreSaleEmployeeName.setText(str);
                    }
                }
            }
        });
    }

    public String getPreSaleEmployeeNameByPreSaleEmployeeCode() {
        if (employeeForPreSaleList != null && employeeForPreSaleList.size() > 0) {
            String text = autoCompletePreSaleEmployeeCode.getText().toString();
            for (EmployeeInfo item : employeeForPreSaleList) {
                if(item.SaleCode.toUpperCase().equals(text.toUpperCase())) {
                    return  item.FullName();
                }
            }
        }

        return  null;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_QR_SCAN && resultCode == Activity.RESULT_OK) {
            String Content = intent.getStringExtra(Intents.Scan.RESULT);
            // ScanEmploy(Content);
            bindSelectedEmployee(Content, BHPreference.teamCode());
        } else if (requestCode == REQUEST_QR_SCAN && resultCode == Activity.RESULT_CANCELED) {
            textShowName.setText("ยังไม่ได้ทำการสแกนกรุณาทำการสแกนอีกครั้ง");
        }
    }

    private void bindSelectedEmployee(final String empID, final String teamCode) {
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                if(BHPreference.sourceSystem().equals("Credit")) {
                    Log.e("po","credit");
                    employee = getEmpByempID_for_credit(empID,teamCode);
                } else {
                    Log.e("po","sale");
                    employee = getEmpByempID(empID, teamCode);
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (employee != null) {
                    textShowName.setText(String.format("%s %s", employee.FirstName, BHUtilities.trim(employee.LastName)));

                    String empFullName = String.format(("%s    %s %s"), employee.SaleCode, employee.FirstName, BHUtilities.trim(employee.LastName));

                    ArrayAdapter<String> empAdapter = (ArrayAdapter) spinnerEmp.getAdapter();
                    if (empAdapter != null) {
                        int empPosition = empAdapter.getPosition(empFullName);
                        spinnerEmp.setSelection(empPosition);
                    }
                }
            }

        }).start();
    }

    // private void ScanEmploy(final String empID) {
    // // TODO Auto-generated method stub
    // (new BackgroundProcess(activity) {
    // List<EmployeeInfo> empidoutput = new ArrayList<EmployeeInfo>();
    //
    // @Override
    // protected void calling() {
    // // TODO Auto-generated method stub
    // empidoutput = getEmpID(empID);
    // }
    //
    // @Override
    // protected void after() {
    // // TODO Auto-generated method stub
    // if (empidoutput != null) {
    // employeeList = empidoutput;
    // for (EmployeeInfo item : employeeList) {
    // textShowName.setText(item.FirstName + "  "
    // + item.LastName);
    // //empID = item.EmpID;
    // }
    // } else {
    // showMessage("ไม่ผ่าน");
    // }
    // }
    //
    // }).start();
    // }

    private void ContactDB() {
        // TODO Auto-generated method stub

        (new BackgroundProcess(activity) {
            // List<EmployeeInfo> outputlistemp = new ArrayList<EmployeeInfo>();

            @Override
            protected void before() {
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                if (BHPreference.SubTeamCode() == null) {
                   // employeeList = getEmployeeInfoSaleLeader(BHPreference.teamCode());

                    if(BHPreference.sourceSystem().equals("Credit")) {
                        employeeList = getEmployeeInfoSaleLeader_for_credit(BHPreference.employeeID(),BHPreference.teamCode());
                    }
                    else {

                        employeeList = getEmployeeInfoSaleLeader(BHPreference.teamCode());
                    }

                } else {
                    if(BHPreference.sourceSystem().equals("Credit")) {
                        employeeList = getEmployeeInfoSubTeamLeader_for_credit(BHPreference.employeeID(),BHPreference.teamCode(), BHPreference.SubTeamCode());

                    }
                    else {
                        employeeList = getEmployeeInfoSubTeamLeader(BHPreference.teamCode(), BHPreference.SubTeamCode());

                    }

                  //  employeeList = getEmployeeInfoSubTeamLeader(BHPreference.teamCode(), BHPreference.SubTeamCode());
                }
                contract = getContract(BHPreference.RefNo());

                try {
                    productStock = getProductStock(contract.ProductSerialNumber);

                }
                catch (Exception ex){

                }

            }

            @Override
            protected void after() {

                if (employeeList != null) {
                	for (int i = employeeList.size()-1; i >= 0; i--) {

                        if(BHPreference.sourceSystem().equals("Credit")) {
                            //if (!employeeList.get(i).PositionCode.equals("Credit")) {
                            if (employeeList.get(i).PositionCode == null || !employeeList.get(i).PositionCode.equals("Credit") || employeeList.get(i).SaleCode == null) {


                                employeeList.remove(i);
                            }
                        }
                         else {
                            if (employeeList.get(i).PositionCode == null || !employeeList.get(i).PositionCode.equals("Sale") || employeeList.get(i).SaleCode == null) {

                                employeeList.remove(i);
                            }
                        }
        			}
                    bindEmployeeList();
                }

                if (contract != null) {
                    if (contract.InstallerEmployeeCode != null) {
                        // ScanEmploy(contract.InstallerEmployeeCode);
                        bindSelectedEmployee(contract.InstallerEmployeeCode, contract.SaleTeamCode);
                    }
                    if(contract.PreSaleEmployeeCode != null){
                        //PreSaleEmployeeCode.setText(contract.PreSaleEmployeeCode);
                        autoCompletePreSaleEmployeeCode.setText(contract.PreSaleEmployeeCode);
                    }
                    if(contract.PreSaleEmployeeName != null){
                        PreSaleEmployeeName.setText(contract.PreSaleEmployeeName);
                    }
                }

                if (productStock != null) {

                }
            }
        }).start();
    }

    private void bindEmployeeList() {
        // TODO Auto-generated method stub
        List<String> emp = new ArrayList<String>();
        emp.add(String.format(" "));
        employeeListTemp.clear();

        for (EmployeeInfo item : employeeList) {
            // emp.add(String.format(("%s    %s %s"), item.EmpID,
            // item.FirstName, item.LastName));

            if (BHPreference.SubTeamCode() == null || BHPreference.SubTeamCode().equals("") || BHPreference.SubTeamCode().equals(item.SubTeamCode)) {
                emp.add(String.format(("%s    %s %s"), item.SaleCode, item.FirstName, BHUtilities.trim(item.LastName)));
                employeeListTemp.add(item);
            }
        }
        BHSpinnerAdapter<String> arrayemp = new BHSpinnerAdapter<String>(activity, emp);
        spinnerEmp.setAdapter(arrayemp);

//        recyclerview.setLayoutManager(new GridLayoutManager(activity, 2, GridLayout.VERTICAL, false));
//        recyclerview.setHasFixedSize(true);
//        employeeAdapter = new EmployeeAdapter(activity, employeeList);
//        recyclerview.setAdapter(employeeAdapter);
//        employeeAdapter.setClickListener(this);
//        employeeAdapter.notifyDataSetChanged();
    }

    // private void showSelectedEmployee() {
    // textShowName.setText(selectedEmployee.FirstName + "  "
    // + selectedEmployee.LastName);
    // }
    //
    // private void TeamEmp() {
    // // TODO Auto-generated method stub
    // (new BackgroundProcess(activity) {
    // //TeamEmployeeInfo output = new TeamEmployeeInfo();
    //
    // @Override
    // protected void calling() {
    // // TODO Auto-generated method stub
    // teamemp = getTeamEmp(productStock.TeamCode, "",
    // contract.OrganizationCode);
    // }
    //
    // //@Override
    // //protected void after() {
    // // // TODO Auto-generated method stub
    // // if (output != null) {
    // // teamemp = output;
    // // tempCode = teamemp.SaleCode;
    // // }
    // //}
    // }).start();
    // }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // savedInstanceState.putParcelableArrayList(EMPLOYEE_LIST,
        // (ArrayList<EmployeeInfo>) employeeList);
        savedInstanceState.putParcelable(EMPLOYEE_SELECTED, employee);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_save_employee:
                if (employee == null || selectedEmpID == null || (selectedEmpID != null && selectedEmpID.trim().isEmpty())) {
                    // if (empID == null || empID.equals("")) {
                    showWarningDialog("คำเตือน","กรุณาเลือกพนักงาน");
//                    final String title = "คำเตือน";
//                    String message = "กรุณาเลือกพนักงาน";
//                    Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
//                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
//
//                                @Override
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    // TODO Auto-generated method stub
//
//                                }
//                            });
//                    setupAlert.show();
                } else {
                    loadEmpDetail();
                    updateContract();
                }
                break;
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }

    private void updateContract() {
//        Log.e("Emp info", String.valueOf(employee));
//        Log.e("Emp id", String.valueOf(BHPreference.employeeID()));
        (new BackgroundProcess(activity) {
            @Override
            protected void before() {
                try {
                    contract.SaleCode = employee.SaleCode;
                    contract.SaleEmployeeCode = employee.EmpID;
                    contract.SaleTeamCode = employee.TeamCode;// productStock.TeamCode;
                    contract.InstallerSaleCode = employee.SaleCode;
                    contract.InstallerEmployeeCode = employee.EmpID;
                    contract.InstallerTeamCode = employee.TeamCode;// productStock.TeamCode;
                    //contract.PreSaleEmployeeCode =  PreSaleEmployeeCode.getText().toString(); // รหัสพนักงานผู้แนะนำ
                    contract.PreSaleEmployeeCode =  autoCompletePreSaleEmployeeCode.getText().toString(); // รหัสพนักงานผู้แนะนำ
                    contract.PreSaleEmployeeName =  PreSaleEmployeeName.getText().toString(); // ชื่อ-นามสกุลผู้แนะนำ
                    contract.SaleEmployeeLevelPath = BHPreference.currentTreeHistoryID();



                } catch (Exception ex){
                    contract.SaleCode = "BBAI0020000";
                    contract.SaleEmployeeCode = "A16086";
                    contract.SaleTeamCode = "BBAI-02";// productStock.TeamCode;
                    contract.InstallerSaleCode = "BBAI-02";
                    contract.InstallerEmployeeCode = "A16086";
                    contract.InstallerTeamCode = "BBAI-02";// productStock.TeamCode;
                    //contract.PreSaleEmployeeCode =  PreSaleEmployeeCode.getText().toString(); // รหัสพนักงานผู้แนะนำ
                    contract.PreSaleEmployeeCode =  ""; // รหัสพนักงานผู้แนะนำ
                    contract.PreSaleEmployeeName =  ""; // ชื่อ-นามสกุลผู้แนะนำ
                    contract.SaleEmployeeLevelPath ="";
                }
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                updateContract(contract, true);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
//				showNextView(new SaleCustomerAddressMainFragment());	// PARADA [Tab-Address]
                showNextView(new New2SaleCustomerAddressCardFragment());	// PARADA [Separate-Address]
            }
        }).start();
    }

    private void saveStatusCode() {
        TSRController.updateStatusCode(BHPreference.RefNo(), STATUS_CODE);
    }

    private void clearData() {
        textShowName.setText("");
    }

    @Override
    public void onItemClick(View view, int position) {
        employee = employeeList.get(position);
        selectedEmpID = employee.EmpID;
        employeeAdapter.setBackgroundItemClick(position);
        employeeAdapter.notifyDataSetChanged();

        bindSelectedEmployee(selectedEmpID, BHPreference.teamCode());
    }

    private void loadEmpDetail() {
        (new BackgroundProcess(activity) {

            EmployeeInfo employeeDetail;

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                employeeDetail = getSaleTeamByTeamCode(BHPreference.organizationCode(), BHPreference.teamCode(), BHPreference.employeeID());
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (employeeDetail != null) {
                    Log.e("Emp team", String.valueOf(employeeDetail));
                    BHPreference.setEmpid4(employeeDetail.SupervisorEmpID);
                    BHPreference.setEmpid5(employeeDetail.LineManagerEmpID);
                    BHPreference.setEmpid6(employeeDetail.ManagerEmpID);
                }
            }
        }).start();
    }
}
