package th.co.thiensurat.fragments.contracts.change;

import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.AssignController.AssignTaskType;
import th.co.thiensurat.data.controller.ChangeContractController.ChangeContractStatus;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.PackageController;
import th.co.thiensurat.data.controller.TripController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.PackageInfo;
import th.co.thiensurat.data.info.PackagePeriodDetailInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.ProblemInfo.ProblemType;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.TripInfo;
import th.co.thiensurat.fragments.sales.New2SaleCustomerAddressCardFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.sales.SalePayFragment;

public class ChangeContractDetailFragment extends BHFragment {

    private final String LOGIN_ORGANIZATION_CODE = BHPreference.organizationCode();// "0";
    private final String LOGIN_TEAM_CODE = BHPreference.teamCode();// "PAK23";
    private final String LOGIN_EMPID = BHPreference.employeeID();// "PAK1070";

    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());

    public static class Data extends BHParcelable {
        public String oldRefNo;
    }

    @InjectView private TextView txtCustomerFullName, txtIDCard, txtAddressIDCard, txtAddressInstall, txtProduct, txtSerialNo, txtContNo, txtEFFDate, txtSaleAmount, txtSaleDiscount, txtFirstPaymentAmount, txtTwoPaymentAmount, txtNextPaymentAmountLabel, txtNextPaymentAmountValue, txtSaleNetAmount, lblCustomerFullName;
    @InjectView private LinearLayout linearLayoutDisplayAmount, linearLayoutDisplayCustomer, linearLayoutSaleAmount, linearLayoutSaleDiscount, linearLayoutFirstPaymentAmount, linearLayoutTwoPaymentAmount, linearLayoutSaleNetAmount, linearLayoutNextPaymentAmount, linearLayoutDisplayCCPackage;
    @InjectView private ListView lvPackageList;
    @InjectView private EditText etCCDate;
    @InjectView ImageButton imgbtnCCDate;
    @InjectView private Spinner spnProblem;

    private Data dataOldContract = null;
    private ContractInfo oldContract = null;
    private List<SalePaymentPeriodInfo> oldSPPList = null;

    private ContractInfo newContract = null;
    private List<SalePaymentPeriodInfo> newSPPList = null;
    private AddressInfo oldAddressIDCard = null;
    private AddressInfo oldAddressInstall = null;
    private AddressInfo oldAddressPayment = null;

    private List<PackageInfo> packageList = null;
    private BHArrayAdapter<PackageInfo> adapter;
    PackageInfo selectedNewModel = null;

    private List<ProblemInfo> problemList;
    private ProblemInfo selectedProblem = null;

    private List<PackagePeriodDetailInfo> pkgPeriodDetailList = null;

    Calendar myCal = Calendar.getInstance();

    List<PaymentInfo> oldPayment;
    List<PaymentInfo> newPayment;

    private List<String> positionCode = Arrays.asList(BHPreference.PositionCode().split(","));

    @Override
    public String fragmentTag() {
        /*return SaleCustomerAddressMainFragment.FRAGMENT_SALE_CUSTOMER_ADDRESS_TAG; // PARADA
        public static String FRAGMENT_SALE_CUSTOMER_ADDRESS_TAG = "sale_customer_address_tag";*/

        return "sale_customer_address_tag";
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_change_contract_detail;
    }

    @Override
    protected int titleID() {
        return R.string.title_change_contract;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_next};
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_next:
                boolean isPaymentComplete = true;
                for (int i = 0; i < oldSPPList.size(); i++) {
                    if (oldSPPList.get(i).PaymentComplete == false) {
                        isPaymentComplete = false;
                    }
                }
                if (isPaymentComplete == true) {
                    showMessage("ไม่สามารถทำการเปลี่ยนสัญญาได้!");
                } else {
                    PrepareChangeContractData();
                }
                break;
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        packageList = new ArrayList<PackageInfo>();

        initialDateControl();

        (new BackgroundProcess(activity) {
            List<ProblemInfo> output = new ArrayList<ProblemInfo>();

            @Override
            protected void calling() {
                output = getProblemByProblemType(LOGIN_ORGANIZATION_CODE,
                        ProblemType.ChangeContract.toString());
            }

            @Override
            protected void after() {
                if (output != null) {
                    problemList = output;
                }

                dataOldContract = getData();
                if (dataOldContract.oldRefNo.equals("")) {
                    final String title = "กรุณาตรวจสอบสินค้า";
                    String message = "ไม่พบข้อมูลสัญญาซื้อขาย!";
                    Builder builder = BHUtilities.alertDialog(getActivity(), title, message, "ปิด");
                    builder.show();
                } else {
                    displayOldContractAndSPP();
                    setWidgetsEventListener();
                    linearLayoutDisplayAmount.setVisibility(View.VISIBLE);
                    linearLayoutDisplayCCPackage.setVisibility(View.GONE);
                }
            }
        }).start();
    }

    private void displayOldContractAndSPP() {
        // (1) Get Contract detail
        (new BackgroundProcess(activity) {
            ContractInfo resultContract;
            List<SalePaymentPeriodInfo> resultSPP;

            @Override
            protected void calling() {
                if (isCredit) {
                    resultContract = getContractByRefNoForCredit(LOGIN_ORGANIZATION_CODE, dataOldContract.oldRefNo);
                } else {
                    resultContract = getContractByRefNo(LOGIN_ORGANIZATION_CODE, dataOldContract.oldRefNo);
                }
            }

            @Override
            protected void after() {
                if (resultContract != null) {
                    oldContract = resultContract;
                    // (2) Get SalePaymentPeriod of this contract
                    new BackgroundProcess(activity) {
                        @Override
                        protected void calling() {
                            resultSPP = getSalePaymentPeriodByRefNo(resultContract.RefNo);

                            List<PackageInfo> list = new PackageController().getPackageForChangeContract(LOGIN_ORGANIZATION_CODE, oldContract.ProductID, oldContract.MODE);

                            if (packageList == null) {
                                packageList = new ArrayList<PackageInfo>();
                            } else {
                                packageList.clear();
                            }

                            if (list != null && list.size() > 0) {
                                packageList.addAll(list);
                            }

                            oldAddressIDCard = getAddress(resultContract.RefNo, AddressType.AddressIDCard);
                            oldAddressInstall = getAddress(resultContract.RefNo, AddressType.AddressInstall);
                            oldAddressPayment = getAddress(resultContract.RefNo, AddressType.AddressPayment);
                        }

                        @Override
                        protected void after() {
                            oldSPPList = resultSPP;
                            // (3) Binding contract
                            bindContract();

                            adapter = new BHArrayAdapter<PackageInfo>(activity, R.layout.list_package_pay, packageList) {

                                class ViewHolder {
                                    public TextView textView, textViewPrice;
                                }

                                @Override
                                protected void onViewItem(int position,
                                                          View view, Object holder,
                                                          final PackageInfo info) {
                                    ViewHolder vh = (ViewHolder) holder;
                                    vh.textView.setText(BHUtilities.trim(info.PackageTitle));
                                    vh.textViewPrice.setText(BHUtilities.numericFormat(info.TotalPrice));
                                }
                            };
                            lvPackageList.setAdapter(adapter);
//                            setListViewHeightBasedOnChildren(lvPackageList);
                            BHUtilities.setListViewHeightBasedOnChildren(lvPackageList);

                            String addressString = "";
                            if (oldAddressIDCard != null) {
                                addressString = oldAddressIDCard.Address();
                            }
                            txtAddressIDCard.setText(addressString);

                            String addressInstallString = "";
                            if (oldAddressInstall != null) {
                                addressInstallString = oldAddressInstall.Address();
                            }
                            txtAddressInstall.setText(addressInstallString);

                            List<String> problem = new ArrayList<String>();
                            problem.add("");
                            List<ProblemInfo> newProblemList = new ArrayList<ProblemInfo>(problemList);

                            //ถ้าเปลี่ยนสัญญาด้วยงานเก็บเงิน จะมี แค่ case 03, 04 เท่านั้น
                            int i = 0;
                            for (ProblemInfo info : newProblemList) {
                                if ((oldContract.MODE == 1 || isCredit) && (info.ProblemName.length() >= 2 && info.ProblemName.substring(0, 2).equals("02"))) {
                                    problemList.remove(i);
                                } else if (oldContract.MODE > 1 && (info.ProblemName.length() >= 2 && info.ProblemName.substring(0, 2).equals("01"))) {
                                    problemList.remove(i);
                                } else {
                                    problem.add(String.format(info.ProblemName));
                                    i++;
                                }
                            }

                            BHSpinnerAdapter<String> arrayProblem = new BHSpinnerAdapter<String>(
                                    activity, problem);
                            spnProblem.setAdapter(arrayProblem);
                        }
                    }.start();
                } else {
                    final String title = "กรุณาตรวจสอบสินค้า";
                    String message = "ไม่พบสินค้า!";
                    Builder builder = BHUtilities.alertDialog(getActivity(), title, message, "ปิด");
                    builder.show();
                }
            }
        }).start();
    }

    private void initialDateControl() {

        etCCDate.setText(BHUtilities.dateFormat(new Date(), "dd/MM/yyyy"));
        etCCDate.setEnabled(false);
        etCCDate.setKeyListener(null);

        final DatePickerDialog.OnDateSetListener dpl = new OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCal.set(Calendar.YEAR, year);
                myCal.set(Calendar.MONTH, monthOfYear);
                myCal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                etCCDate.setText(BHUtilities.dateFormat(myCal.getTime(), "dd/MM/yyyy"));
            }
        };

        imgbtnCCDate.setVisibility(View.GONE);
//        imgbtnCCDate.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                DatePickerDialog dpd = new DatePickerDialog(arg0.getContext(),
//                        dpl, myCal.get(Calendar.YEAR), myCal
//                        .get(Calendar.MONTH), myCal
//                        .get(Calendar.DAY_OF_MONTH));
//                dpd.show();
//            }
//        });

    }

    private void bindContract() {
        String titleLblCustomerFullName = oldContract.MODE > 1 ? "" + getResources().getString(R.string.change_contract_customer_fullname) : "" + getResources().getString(R.string.change_contract_customer_fullname_cash);
        lblCustomerFullName.setText(titleLblCustomerFullName);

        txtEFFDate.setText(BHUtilities.dateFormat(oldContract.EFFDATE));
        txtContNo.setText(BHUtilities.trim(oldContract.CONTNO));
        txtCustomerFullName.setText(String.format("%s %s",
                BHUtilities.trim(oldContract.CustomerFullName),
                BHUtilities.trim(oldContract.CompanyName)));

        txtIDCard.setText(BHUtilities.trim(oldContract.IDCard));

        txtProduct.setText(BHUtilities.trim(oldContract.ProductName));
        txtSerialNo.setText(BHUtilities.trim(oldContract.ProductSerialNumber));

        txtSaleAmount.setText(BHUtilities.numericFormat(oldContract.SALES));
        txtSaleDiscount.setText(BHUtilities.numericFormat(oldContract.TradeInDiscount));
        txtSaleNetAmount.setText(BHUtilities.numericFormat(oldContract.TotalPrice));

        if (oldContract.TradeInDiscount == 0) {
            linearLayoutSaleAmount.setVisibility(View.GONE);
            linearLayoutSaleDiscount.setVisibility(View.GONE);
        }

        if (oldSPPList != null) {
            if (oldSPPList.size() > 0) {
                txtFirstPaymentAmount.setText(BHUtilities.numericFormat(oldSPPList.get(0).NetAmount));

                if (oldSPPList.size() != 1) {
                    if (oldSPPList.size() == 2) {
                        linearLayoutNextPaymentAmount.setVisibility(View.GONE);
                        txtTwoPaymentAmount.setText(BHUtilities.numericFormat(oldSPPList.get(1).NetAmount));
                    } else {
                        if (oldSPPList.get(1).NetAmount == oldSPPList.get(2).NetAmount) {
                            linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
                            txtNextPaymentAmountLabel.setText("งวดที่ 2 ถึง " + BHUtilities.numericFormat(oldSPPList.size()) + " ต้องชำระงวดละ");
                            txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(oldSPPList.get(1).NetAmount));
                        } else {
                            if (oldSPPList.size() == 3) {
                                txtTwoPaymentAmount.setText(BHUtilities.numericFormat(oldSPPList.get(1).NetAmount));
                                txtNextPaymentAmountLabel.setText("งวดที่ 3 ที่ต้องชำระงวดละ");
                                txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(oldSPPList.get(2).NetAmount));
                            } else {
                                txtTwoPaymentAmount.setText(BHUtilities.numericFormat(oldSPPList.get(1).NetAmount));
                                txtNextPaymentAmountLabel.setText("งวดที่ 3 ถึง " + BHUtilities.numericFormat(oldSPPList.size()) + " ต้องชำระงวดละ");
                                txtNextPaymentAmountValue.setText(BHUtilities.numericFormat(oldSPPList.get(2).NetAmount));
                            }
                        }
                    }
                } else {
                    linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
                    linearLayoutNextPaymentAmount.setVisibility(View.GONE);
                }
            } else {
                linearLayoutFirstPaymentAmount.setVisibility(View.GONE);
                linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
                linearLayoutNextPaymentAmount.setVisibility(View.GONE);
            }
        } else {
            linearLayoutFirstPaymentAmount.setVisibility(View.GONE);
            linearLayoutTwoPaymentAmount.setVisibility(View.GONE);
            linearLayoutNextPaymentAmount.setVisibility(View.GONE);

            final String title = "กรุณาตรวจสอบสินค้า";
            String message = "ไม่พบข้อมูลงวดการชำระเงิน!";
            Builder builder = BHUtilities.alertDialog(getActivity(), title, message, "ปิด");
            builder.show();
        }
    }

    private void bindPackage(final boolean Case04, final int Mode) {
        (new BackgroundProcess(activity) {
            List<PackageInfo> tempList;

            @Override
            protected void before() {
                if (packageList == null) {
                    packageList = new ArrayList<PackageInfo>();
                } else {
                    packageList.clear();
                }
            }

            @Override
            protected void calling() {
                if (Case04) {
                    tempList = new PackageController().getPackageForChangeContractForCase04(LOGIN_ORGANIZATION_CODE,
                            oldContract.ProductID, Mode, oldContract.MODEL);
                } else {
                    tempList = new PackageController().getPackageForChangeContract(LOGIN_ORGANIZATION_CODE,
                            oldContract.ProductID, Mode);
                }

            }

            @Override
            protected void after() {
                if (tempList != null && tempList.size() > 0) {
                    packageList.addAll(tempList);
                } else {

                    final String title = "กรุณาตรวจสอบข้อมูล";
                    String message = "ไม่พบข้อมูล Package ที่สามารถใช้งานได้!";
                    Builder builder = BHUtilities.alertDialog(getActivity(), title, message, "ปิด");
                    builder.show();
                }

                adapter.notifyDataSetChanged();
//                setListViewHeightBasedOnChildren(lvPackageList);
                if (packageList.size() > 0) {
                    lvPackageList.setVisibility(View.VISIBLE);
                    BHUtilities.setListViewHeightBasedOnChildren(lvPackageList);
                } else {
                    lvPackageList.setVisibility(View.GONE);
                }
            }
        }).start();
    }

    /*
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
    */

    private void setWidgetsEventListener() {
        // (1) Event listener of spinner
        spnProblem.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                    selectedProblem = problemList.get(position - 1); // spnProblem
                    // start with blank row so not equal problemList
                    if (parent.getItemAtPosition(position).toString().substring(0, 2).equals("03")) {
                        linearLayoutDisplayAmount.setVisibility(View.VISIBLE);
                        linearLayoutDisplayCCPackage.setVisibility(View.GONE);
                        selectedNewModel = null;
                    } else {
                        linearLayoutDisplayAmount.setVisibility(View.GONE);
                        linearLayoutDisplayCCPackage.setVisibility(View.VISIBLE);
                        bindPackage(parent.getItemAtPosition(position).toString().substring(0, 2).equals("04"), oldContract.MODE);
                    }
                } else {
                    selectedProblem = null;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedNewModel = null;
            }
        });

        // (2) Event listener of list view
        lvPackageList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                view.setSelected(true);
                selectedNewModel = (PackageInfo) lvPackageList
                        .getItemAtPosition(position);
            }
        });

        // Event listener of TextView
        linearLayoutDisplayCustomer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedProblem != null) {
                    if (selectedProblem.ProblemName.substring(0, 2).equals(
                            "03")) {
                    }
                }
            }
        });

    }

    private boolean onValidateData() {
        boolean bSuccess = false;
        if (selectedProblem != null
                && !etCCDate.getText().toString().trim().equals("")) {
            String causePrefix = spnProblem.getSelectedItem().toString()
                    .substring(0, 2);
            switch (causePrefix) {
                case "01":
                    // [Case-01] เปลี่ยนจากเงินสดเป็นเงินผ่อน
                    if (selectedNewModel != null) {
                        if (selectedNewModel.PackageTitle.startsWith("ผ่อน")) {
                            bSuccess = true;
                        } else {
                            final String title = "กรุณาตรวจสอบสินค้า";
                            String message = "กรุณาเลือกงวดการชำระเงินที่เป็นเงินผ่อนเท่านั้น!";
                            Builder builder = BHUtilities.alertDialog(getActivity(), title, message, "ปิด");
                            builder.show();
                        }
                    } else {
                        final String title = "กรุณาตรวจสอบสินค้า";
                        String message = "กรุณาเลือกงวดการชำระเงิน!";
                        Builder builder = BHUtilities.alertDialog(getActivity(), title, message, "ปิด");
                        builder.show();
                    }
                    break;
                case "02":
                    // [Case-02] เปลี่ยนจากเงินผ่อนเป็นเงินสด
                    if (selectedNewModel != null) {
                        if (!selectedNewModel.PackageTitle.startsWith("ผ่อน")) {
                            bSuccess = true;
                        } else {
                            final String title = "กรุณาตรวจสอบสินค้า";
                            String message = "กรุณาเลือกงวดการชำระเงินที่เป็นเงินสดเท่านั้น!";
                            Builder builder = BHUtilities.alertDialog(getActivity(), title, message, "ปิด");
                            builder.show();
                        }
                    } else {
                        final String title = "กรุณาตรวจสอบสินค้า";
                        String message = "กรุณาเลือกงวดการชำระเงิน!";
                        Builder builder = BHUtilities.alertDialog(getActivity(), title, message, "ปิด");
                        builder.show();
                    }
                    break;
                case "03":
                    // [Case-03] เปลี่ยนผู้เช่าซื้อ ==>
                    // ต้องแตะหน้าจอเพื่อให้กระโดดไปที่
                    // หน้าจอเปลี่ยนแปลงข้อมูลลูกค้าก่อน แล้วค่อยกลับมาที่
                    // หน้าสรุปผลการเปลี่ยนสัญญา(Result)
                    bSuccess = true;
                    break;
                case "04":
                    // [Case-04] เปลี่ยนแปลงราคาเงินสด/เงินผ่อน ==>
                    // ไปที่หน้าระบุสินค้าเทิร์นก่อน แล้วค่อยกระโดดกลับมา
                    // หน้าสรุปผลการเปลี่ยนสัญญา(Result)
                    if (selectedNewModel != null) {
                        bSuccess = true;
                    } else {
                        final String title = "กรุณาตรวจสอบสินค้า";
                        String message = "กรุณาเลือกงวดการชำระเงิน!";
                        Builder builder = BHUtilities.alertDialog(getActivity(), title, message, "ปิด");
                        builder.show();
                    }
                    break;
                default:
                    break;
            }
        } else {
            final String title = "กรุณาตรวจสอบสินค้า";
            String message = "กรุณาระบุสาเหตุการเปลี่ยนสัญญา และวันที่เปลี่ยนสัญญา!";
            Builder builder = BHUtilities.alertDialog(getActivity(), title, message, "ปิด");
            builder.show();
        }
        return bSuccess;
    }

    private void PrepareChangeContractData() {
        // final boolean bSuccess = false;
        if (onValidateData()) {
            pkgPeriodDetailList = null;
            (new BackgroundProcess(activity) {

                String mModel = "";
                String causePrefix = spnProblem.getSelectedItem().toString().substring(0, 2);

                @Override
                protected void before() {
                    // (1) Assign new contract
                    // String newContRefNo =
                    // BHUtilities.getAutoGenerateDocumentID(th.co.bighead.utilities.BHUtilities.DocumentType.Contract.toString(),
                    // BHPreference.teamCode(),
                    // BHPreference.employeeID());//DatabaseHelper.getUUID();
                    // String oldContRefNo = oldContract.RefNo;
                    newContract = (ContractInfo) oldContract.copy();

                    // newContract = (ContractInfo)
                    // CloneUtils.copy(oldContract);
                    // newContract.RefNo = newContRefNo;
                    // newContract.CONTNO = oldContract.CONTNO;// + "(02)";

                    // newContract.EFFDATE = oldContract.EFFDATE;// etCCDate;
                    newContract.EFFDATE = myCal.getTime();// etCCDate;

                    //ถ้าเปลี่ยสัญญาของงานเก็บเงิน จะใช้ Sala ของสัญญาเดิม
                    if (isCredit) {
                        newContract.SaleCode = oldContract.SaleCode;
                        newContract.SaleEmployeeCode = oldContract.SaleEmployeeCode;
                        newContract.SaleTeamCode = oldContract.SaleTeamCode;
                    } else {
                        newContract.SaleCode = BHPreference.saleCode();
                        newContract.SaleEmployeeCode = BHPreference.employeeID();
                        newContract.SaleTeamCode = BHPreference.teamCode();
                    }

                    newContract.fromrefno = oldContract.RefNo;
                    newContract.fromcontno = oldContract.CONTNO;
                    newContract.CreateDate = new Date();
                    newContract.CreateBy = BHPreference.employeeID();
                    newContract.LastUpdateDate = new Date();
                    newContract.LastUpdateBy = BHPreference.employeeID();

                    // (2) Assign SalePaymentPeriod for each case
                    if (causePrefix.equals("01")
                            || causePrefix.equals("02")
                            || causePrefix.equals("04")) {
                        mModel = selectedNewModel.Model;
                        newContract.MODEL = selectedNewModel.Model;
                        newContract.SALES = selectedNewModel.TotalPrice;
                        newContract.TotalPrice = selectedNewModel.TotalPrice - newContract.TradeInDiscount;
                    } else {
                        mModel = oldContract.MODEL;
                    }

                }

                @Override
                protected void calling() {
                    // STEP_EDIT_2 :
                    // 2.1 ถ้าเปลี่ยนสัญญาโดยพนักงานขายจะยังไม่มีการ gen เลขที่สัญญาใหม่
                    //     และฝ่ายเก็บเงิน

                    newContract.RefNo = DatabaseHelper.getUUID();// newContract.CONTNO;
                    /*if (positionCode.contains("SaleLeader")) {
                        newContract.CONTNO = getAutoGenerateDocumentID(
                                DocumentGenType.Contract.toString(),
                                BHPreference.SubTeamCode(),
                                BHPreference.saleCode());
                    } else {
                        //newContract.CONTNO = null;

                        //ถ้าเปลี่ยสัญญาของงานเก็บเงิน จะใช้ CONTNO ของสัญญาเดิม
                        if(isCredit){
                            newContract.CONTNO = oldContract.CONTNO;
                        } else{
                            newContract.CONTNO = newContract.RefNo;
                        }

                    }*/

                    //ถ้าเปลี่ยนสัญญาจะใช้เลขที่ของสัญญาเดิมตลอด
                    newContract.CONTNO = oldContract.CONTNO;

                    pkgPeriodDetailList = getPackagePeriodDetail(LOGIN_ORGANIZATION_CODE, mModel);

                    oldPayment = getPaymentByRefNo(BHPreference.organizationCode(), oldContract.RefNo);
                }

                @Override
                protected void after() {
                    if (pkgPeriodDetailList != null) {

                        if (oldPayment != null) {
                            newPayment = new ArrayList<PaymentInfo>();

                            PaymentInfo paymentInfo = new PaymentInfo();
                            float sumPAYAMT = 0;

                            for (PaymentInfo info : oldPayment) {
                                /*info.PaymentID = DatabaseHelper.getUUID();
                                info.RefNo = newContract.RefNo;
                                newPayment.add(info);*/
                                paymentInfo = info;
                                sumPAYAMT += info.PAYAMT;
                            }

                            paymentInfo.PaymentID = DatabaseHelper.getUUID();
                            paymentInfo.RefNo = newContract.RefNo;
                            paymentInfo.PAYAMT = sumPAYAMT;
                            paymentInfo.Status = PaymentInfo.PaymentStatus.N.toString();
                            newPayment.add(paymentInfo);
                        }

                        // (3.1) Update new contract
                        newContract.MODE = pkgPeriodDetailList.size();
                        // (3.2) Create new SalePaymentPeriod
                        newSPPList = new ArrayList<SalePaymentPeriodInfo>();
                        for (PackagePeriodDetailInfo item : pkgPeriodDetailList) {
                            SalePaymentPeriodInfo spp = new SalePaymentPeriodInfo();
                            spp.SalePaymentPeriodID = DatabaseHelper.getUUID();
                            spp.RefNo = newContract.RefNo;
                            spp.PaymentPeriodNumber = item.PaymentPeriodNumber;
                            spp.PaymentAmount = item.PaymentAmount;
                            spp.Discount = (item.PaymentPeriodNumber == 1) ? newContract.TradeInDiscount : 0;
                            spp.NetAmount = (item.PaymentPeriodNumber == 1) ? (item.PaymentAmount - newContract.TradeInDiscount) : item.PaymentAmount;
                            spp.PaymentComplete = false;
                            spp.PaymentDueDate = BHUtilities.addMonth(new Date(), item.PaymentPeriodNumber);
                            spp.PaymentAppointmentDate = BHUtilities.addMonth(new Date(), item.PaymentPeriodNumber);

                            // Fixed - [BHPROJ-0024-407]
                            TripInfo trip = new TripController().getTrip(spp.PaymentAppointmentDate);
                            if (trip != null) {
                                spp.TripID = trip.TripID;
                            }

                            spp.CreateDate = new Date();
                            spp.CreateBy = BHPreference.employeeID();
                            spp.LastUpdateDate = new Date();
                            spp.LastUpdateBy = BHPreference.employeeID();
                            newSPPList.add(spp);
                        }
                        // *******
                        // ActionChangeContractWithSalePaymentPeriod(newContract,
                        // newSPPList);
                        // - AddContractWithSalePaymentPeriod
                        // - AddOrUpdateAddress

                        AddressInfo newAddressIDCard = new AddressInfo();
                        AddressInfo newAddressInstall = new AddressInfo();
                        AddressInfo newAddressPayment = new AddressInfo();

                        if (oldAddressIDCard != null) {
                            //  oldAddressIDCard.Address(); variable [AddressDetail3 & AddressDetail4] change '-' to ''
                            if (oldAddressIDCard.AddressDetail == null || (oldAddressIDCard.AddressDetail != null && oldAddressIDCard.AddressDetail.equals(""))) {
                                oldAddressIDCard.AddressDetail = "-";
                            }
                            if (oldAddressIDCard.AddressDetail2 == null || (oldAddressIDCard.AddressDetail2 != null && oldAddressIDCard.AddressDetail2.equals(""))) {
                                oldAddressIDCard.AddressDetail2 = "-";
                            }
                            if (oldAddressIDCard.AddressDetail3 == null || (oldAddressIDCard.AddressDetail3 != null && oldAddressIDCard.AddressDetail3.equals(""))) {
                                oldAddressIDCard.AddressDetail3 = "-";
                            }
                            if (oldAddressIDCard.AddressDetail4 == null || (oldAddressIDCard.AddressDetail4 != null && oldAddressIDCard.AddressDetail4.equals(""))) {
                                oldAddressIDCard.AddressDetail4 = "-";
                            }
                            newAddressIDCard = (AddressInfo) oldAddressIDCard.copy();
                            newAddressIDCard.RefNo = newContract.RefNo;
                        }

                        if (oldAddressInstall != null) {
                            if (oldAddressInstall.AddressDetail == null || (oldAddressInstall.AddressDetail != null && oldAddressInstall.AddressDetail.equals(""))) {
                                oldAddressInstall.AddressDetail = "-";
                            }
                            if (oldAddressInstall.AddressDetail2 == null || (oldAddressInstall.AddressDetail2 != null && oldAddressInstall.AddressDetail2.equals(""))) {
                                oldAddressInstall.AddressDetail2 = "-";
                            }
                            if (oldAddressInstall.AddressDetail3 == null || (oldAddressInstall.AddressDetail3 != null && oldAddressInstall.AddressDetail3.equals(""))) {
                                oldAddressInstall.AddressDetail3 = "-";
                            }
                            if (oldAddressInstall.AddressDetail4 == null || (oldAddressInstall.AddressDetail4 != null && oldAddressInstall.AddressDetail4.equals(""))) {
                                oldAddressInstall.AddressDetail4 = "-";
                            }
                            newAddressInstall = (AddressInfo) oldAddressInstall.copy();
                            newAddressInstall.RefNo = newContract.RefNo;
                        }

                        if (oldAddressPayment != null) {
                            newAddressPayment = (AddressInfo) oldAddressPayment.copy();
                            newAddressPayment.RefNo = newContract.RefNo;
                        }
                        // (3.4) Create/Update
                        // ChangeContractInfo for
                        // Request+Approve+Action

                        String chgContractID = DatabaseHelper.getUUID();

                        ChangeContractInfo chgCont = new ChangeContractInfo();

                        ChangeContractInfo chgContRequest = new ChangeContractInfo();
                        ChangeContractInfo chgContApprove = new ChangeContractInfo();
                        ChangeContractInfo chgContAction = new ChangeContractInfo();

                        // (3.4.1) Create
                        // ChangeContractInfo for
                        // Request
                        chgCont.ChangeContractID = chgContractID;
                        chgCont.OrganizationCode = BHPreference.organizationCode();
                        chgCont.RefNo = oldContract.RefNo;
                        chgCont.OldSaleID = oldContract.RefNo;
                        chgCont.NewSaleID = newContract.RefNo;
                        //chgCont.Status = CHANGE_CONTRACT_STATUS_ACTION;
                        chgCont.Status = ChangeContractStatus.REQUEST.toString();
                        chgCont.RequestProblemID = selectedProblem.ProblemID;
                        chgCont.RequestDate = myCal.getTime();
                        chgCont.RequestBy = LOGIN_EMPID;
                        chgCont.RequestTeamCode = LOGIN_TEAM_CODE;
                        chgCont.CreateDate = new Date();
                        chgCont.CreateBy = LOGIN_EMPID;
                        chgCont.LastUpdateDate = new Date();
                        chgCont.LastUpdateBy = LOGIN_EMPID;

                        chgContRequest = (ChangeContractInfo) chgCont
                                .copy();

                        chgCont.RequestDate = null;
                        chgCont.RequestBy = null;

                        // (3.4.2) Update
                        // ChangeContractInfo
                        // for Approve
                        // ถ้าเปลี่ยนสัญญาโดยพนักงานขายและงานเก็บเงิน ขั้นตอน approved จะยังไม่เกิดขึ้น
                        // และฝ่ายเก็บเงิน
                        if (positionCode.contains("SaleLeader")) {
                            chgCont.Status = ChangeContractStatus.APPROVED.toString();
                            chgCont.ApprovedDate = myCal.getTime();
                            chgCont.ApprovedBy = LOGIN_EMPID;
                            chgCont.LastUpdateDate = new Date();
                            chgCont.LastUpdateBy = LOGIN_EMPID;

                            chgContApprove = (ChangeContractInfo) chgCont
                                    .copy();
                        } else {
                            chgContApprove = null;
                        }

                        // (3.5) Create Assign for
                        // Approve change
                        // contract
                        AssignInfo assign = null;
                        if (positionCode.contains("SaleLeader")) {
                            assign = new AssignInfo();
                            assign.AssignID = DatabaseHelper.getUUID();
                            assign.TaskType = AssignTaskType.ChangeContract
                                    .toString();
                            assign.OrganizationCode = LOGIN_ORGANIZATION_CODE;
                            //assign.RefNo = newContract.RefNo;
                            assign.RefNo = oldContract.RefNo;
                            assign.AssigneeEmpID = LOGIN_EMPID;
                            assign.AssigneeTeamCode = LOGIN_TEAM_CODE;
                            assign.CreateDate = new Date();
                            assign.CreateBy = LOGIN_EMPID;
                            assign.LastUpdateDate = new Date();
                            assign.LastUpdateBy = LOGIN_EMPID;
                            assign.ReferenceID = chgContractID;
                        }

                        chgCont.ApprovedDate = null;
                        chgCont.ApprovedBy = null;

                        // (3.4.3)
                        // Create
                        // ChangeContractInfo
                        // for Action
                        // ถ้าเปลี่ยนสัญญาโดยพนักงานขายและงานเก็บเงิน ขั้นตอน action จะยังไม่เกิดขึ้น
                        // และฝ่ายเก็บเงิน
                        if (positionCode.contains("SaleLeader")) {
                            chgCont.Status = ChangeContractStatus.COMPLETED.toString();
                            chgCont.ResultProblemID = selectedProblem.ProblemID;
                            chgCont.EffectiveDate = myCal.getTime();
                            chgCont.EffectiveBy = LOGIN_EMPID;
                            //chgCont.ChangeContractPaperID = chgCont.ChangeContractID;
                            chgCont.ChangeContractPaperID = getAutoGenerateDocumentID(TSRController.DocumentGenType.ChangeContract, BHPreference.SubTeamCode(), BHPreference.saleCode());
                            chgCont.LastUpdateDate = new Date();
                            chgCont.LastUpdateBy = LOGIN_EMPID;

                            chgContAction = (ChangeContractInfo) chgCont
                                    .copy();
                        } else {
                            chgContAction = null;
                        }
                        // (3.6)
                        // Update
                        // old
                        // contract
                        // to be
                        // In-Active
                        // Contract
                        // STEP_EDIT_1 :
                        // 1.1 ถ้าเปลี่ยนสัญญาโดยพนักงานขายและงานเก็บเงินให้สัญญาเก่ายัง active อยู่ จนกว่าหัวหน้าทีมหรือ admin จะอนุมัติ
                        // 1.2 และสัญญาใหม่จะยังไม่ active
                        if (positionCode.contains("SaleLeader")) {
                            oldContract.isActive = false;
                            newContract.isActive = true;
                        } else {
                            oldContract.isActive = true;
                            newContract.isActive = false;
                        }

                        // STEP_EDIT_2 :
                        // 2.2 ถ้าเปลี่ยนสัญญาโดยพนักงาน เลขที่สัญญาใหม่จะยังไม่เกิดขึ้น จนกว่าจะเกิดการ approved หรือ action?
                        // 2.3 ถ้าเปลี่ยนโดยพนักงานเก็บเงิน จะใช้เลขที่ CONTNO ของสัญญาเดิม
                        if (positionCode.contains("SaleLeader")) {
                            oldContract.torefno = newContract.RefNo;
                            oldContract.tocontno = newContract.CONTNO;
                        } else {
                            oldContract.torefno = null;
                            oldContract.tocontno = null;
                        }
                        oldContract.todate = myCal.getTime();
                        oldContract.LastUpdateDate = new Date();
                        oldContract.LastUpdateBy = LOGIN_EMPID;

                        // (3.7)
                        // Decision
                        // for
                        // next
                        // page
                        switch (causePrefix) {
                            case "01":
                                // [Case-01]
                                // เปลี่ยนจากเงินสดเป็นเงินผ่อน
                                // ==>
                                // ไปที่
                                // หน้าสรุปผลการเปลี่ยนสัญญา(Result)
                            case "02":
                                // [Case-02]
                                // เปลี่ยนจากเงินผ่อนเป็นเงินสด
                                // ==>
                                // ไปที่
                                // หน้าสรุปผลการเปลี่ยนสัญญา(Result)

                                // ContractInfo
                                // x
                                // =
                                // oldContract;
                                // ContractInfo
                                // y
                                // =
                                // newContract;

                                ChangeContractResultFragment.Data data1 = new ChangeContractResultFragment.Data();
                                data1.oldContract = oldContract;
                                data1.selectedCauseName = selectedProblem.ProblemName;
                                data1.newContract = newContract;
                                data1.newSPPList = newSPPList;
                                // data1.newRefNo = newContract.RefNo;
                                //data1.newAddressIDCard = oldAddressIDCard;
                                //data1.newAddressInstall = oldAddressInstall;

                                data1.assign = assign;
                                data1.chgContractRequest = chgContRequest;
                                data1.chgContractApprove = chgContApprove;
                                data1.chgContractAction = chgContAction;

                                data1.newAddressIDCard = newAddressIDCard;
                                data1.newAddressInstall = newAddressInstall;
                                data1.newAddressPayment = newAddressPayment;

                                data1.changeContractType = causePrefix;

                                data1.newPayment = newPayment;

                                ChangeContractResultFragment fmCCResult = BHFragment
                                        .newInstance(
                                                ChangeContractResultFragment.class,
                                                data1);
                                showNextView(fmCCResult);
                                break;
                            case "03":
                                // [Case-03]
                                // เปลี่ยนผู้เช่าซื้อ
                                // ==>
                                // ต้องแตะหน้าจอเพื่อให้กระโดดไปที่
                                // หน้าจอเปลี่ยนแปลงข้อมูลลูกค้าก่อน
                                // แล้วค่อยกลับมาที่
                                // หน้าสรุปผลการเปลี่ยนสัญญา(Result)
                                // showMessage("กรุณาแตะที่ชื่อผู้เช่าซื้อหรือเลขที่บัตร เพื่อไปยังหน้าจอระบุข้อมูลผู้เช่าซื้อรายใหม่!");

                                // SaleCustomerAddressMainFragment.Data
                                // data03 = new
                                // SaleCustomerAddressMainFragment.Data();
                                // // PARADA [Tab-Address]
                                New2SaleCustomerAddressCardFragment.Data data03 = new New2SaleCustomerAddressCardFragment.Data(); // PARADA
                                // [Separate-Address]

                                // data03.oldcustomer
                                // =
                                // oldContract.CustomerID;
                                // data03.newcustomer
                                // =
                                // DatabaseHelper.getUUID();
                                // data03.oldrefno
                                // =
                                // oldContract.RefNo;
                                // data03.newrefno
                                // =
                                // newContract.RefNo;

                                // for
                                // Save
                                // Next
                                // Page
                                if (pkgPeriodDetailList != null) {
                                    // addRequestChangeContract(chgCont);
                                    // approveChangeContract(chgCont);
                                    // actionChangeContract(chgCont,
                                    // newContract);
                                    // addOrUpdateAssignActionChangeContract(assign);
                                    // AddOrUpdateContract(oldContract);

                                    BHPreference.setRefNo(oldContract.RefNo);
                                    BHPreference.setProcessType(ProcessType.ChangeContract
                                            .toString());
                                    // data03.productName =
                                    // oldContract.ProductName;
                                    //newContract.ProblemName = oldContract.ProductName;
                                    data03.selectedCauseName = selectedProblem.ProblemName;
                                    data03.chgContractRequest = chgContRequest;
                                    data03.chgContractApprove = chgContApprove;
                                    data03.chgContractAction = chgContAction;
                                    data03.assign = assign;
                                    data03.oldContract = oldContract;
                                    newContract.CustomerID = null;
                                    data03.newContract = newContract;
                                    // data03.newSPPList = oldSPPList;
                                    data03.newSPPList = newSPPList;
                                    data03.newPayment = newPayment;
                                    // data03.addressIDCard =
                                    // newAddressIDCard;
//                                    data03.addressInstall = newAddressInstall;
//                                    data03.addressPayment = newAddressPayment;

                                }
                                // SaleCustomerAddressMainFragment
                                // fmSaleCustomerAddress =
                                // BHFragment.newInstance(SaleCustomerAddressMainFragment.class,
                                // data03); // PARADA [Tab-Address]
                                New2SaleCustomerAddressCardFragment fmSaleCustomerAddress = BHFragment
                                        .newInstance(
                                                New2SaleCustomerAddressCardFragment.class,
                                                data03);// PARADA
                                // [Separate-Address]

                                showNextView(fmSaleCustomerAddress);
                                break;
                            case "04":
                                // [Case-04]
                                // เปลี่ยนแปลงราคาเงินสด/เงินผ่อน
                                // ==>
                                // ไปที่หน้าระบุสินค้าเทิร์นก่อน
                                // แล้วค่อยกระโดดกลับมา
                                // หน้าสรุปผลการเปลี่ยนสัญญา(Result)
                                // showMessage("Under Construction");
                                // AddChangeContract();
                                // SalePayFragment.Data
                                // data
                                // =
                                // new
                                // SalePayFragment.Data(SalePayFragment.ProcessType.ChangeContract);
                                // data.refNo
                                // =
                                // ChangeContractListFragment.refNo;
                                // SalePayFragment
                                // fmCCTradeInBrand
                                // =
                                // BHFragment.newInstance(SalePayFragment.class,
                                // data);
                                // showNextView(fmCCTradeInBrand);

                                // ContractInfo
                                // xx
                                // =
                                // oldContract;
                                // ContractInfo
                                // yy
                                // =
                                // newContract;


                                //data1.changeContractType = causePrefix;

                                SalePayFragment.Data data = new SalePayFragment.Data();
                                // data.refNo
                                // =
                                // oldContract.RefNo;
                                BHPreference.setRefNo(oldContract.RefNo);
                                BHPreference
                                        .setProcessType(ProcessType.ChangeContract
                                                .toString());
                                data.addressIDCard = newAddressIDCard;// oldAddressIDCard;
                                data.addressInstall = newAddressInstall;// oldAddressInstall;
                                data.addressPayment = newAddressPayment;// oldAddressPayment;
                                data.selectedCauseName = selectedProblem.ProblemName;

                                // data.refNo_New
                                // =
                                // newContract.RefNo;
                                // //data.contNo_New
                                // =
                                // newContract.CONTNO;
                                //
                                data.productName = oldContract.ProductName;
                                data.customerFullName = oldContract.CustomerFullName;
                                data.idCard = oldContract.IDCard;

                                // for
                                // Save
                                // Next
                                // Page
                                if (pkgPeriodDetailList != null) {
                                    saveAddress(newAddressIDCard);
                                    saveAddress(newAddressInstall);
                                    saveAddress(newAddressPayment);
                                    // addRequestChangeContract(chgCont);
                                    // approveChangeContract(chgCont);
                                    // actionChangeContract(chgCont,
                                    // newContract);
                                    // addOrUpdateAssignActionChangeContract(assign);
                                    // AddOrUpdateContract(oldContract);

                                    data.chgContractRequest = chgContRequest;
                                    data.chgContractApprove = chgContApprove;
                                    data.chgContractAction = chgContAction;
                                    data.assign = assign;
                                    data.oldContract = oldContract;
                                    data.newContract = newContract;

                                    // data.newSPPList = oldSPPList;
                                    data.newSPPList = newSPPList;
                                    data.newPayment = newPayment;

                                }

                                SalePayFragment fmCCTradeInBrand = BHFragment
                                        .newInstance(SalePayFragment.class,
                                                data);
                                showNextView(fmCCTradeInBrand);
                                break;
                            default:
                                break;
                        }
                    }
                }
            }).start(); // Background-Process of getPackagePeriodDetail()
        }
    }
}
