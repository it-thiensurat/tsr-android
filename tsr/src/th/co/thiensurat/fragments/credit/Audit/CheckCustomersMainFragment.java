package th.co.thiensurat.fragments.credit.Audit;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.fragments.share.BarcodeScanFragment;

public class CheckCustomersMainFragment extends BHFragment {

    @InjectView TextView txtCountSaleAudit;
    @InjectView EditText txtSearch;
    @InjectView Button btnSearch;
    @InjectView private DragSortListView lvCustomerList;


    private List<AssignInfo> checkCustomerList;
    private DragSortController mController;
    CustomerAdapter customerAdapter;

    private boolean header_txt_no_DESC, header_txt_name_DESC, header_txt_period_number_DESC, header_txt_payment_DESC;
    private boolean CheckedAll, isChecked;

    private long timeDoubleClick = 500; //ms
    private long timestampLastClick;
    private int positionClickListView;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_credit_audit_check_customers_main;
    }

    @Override
    protected int titleID() {
        return R.string.title_check_customers;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_check_audit, R.string.button_display_map};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        if (checkCustomerList == null) {
            checkCustomerList = new AssignController().getSaleAuditByAssigneeEmpID(BHPreference.organizationCode(), BHPreference.employeeID(), AddressInfo.AddressType.AddressPayment.toString());
        }

        if (checkCustomerList != null) {
            binHeader();
            bindListView(false, false);

            txtCountSaleAudit.setText(String.format("ลูกค้าที่ต้องตรวจสอบจำนวน %d คน", checkCustomerList.size()));
        } else {
            txtCountSaleAudit.setText("ลูกค้าที่ต้องตรวจสอบจำนวน 0 คน");

            showDialog("แจ้งเตือน", "ไม่พบข้อมูล");
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = "%" + txtSearch.getText().toString() + "%";
                checkCustomerList = new AssignController().getSaleAuditByAssigneeEmpIDAndSearch(BHPreference.organizationCode(), BHPreference.employeeID(), search);

                if (checkCustomerList != null) {
                    for (int i = 0; i < checkCustomerList.size(); i++) {
                        checkCustomerList.get(i).Order = i + 1;
                    }

                    lvCustomerList.setVisibility(View.VISIBLE);
                    lvCustomerList.setAdapter(null);

                    bindListView(false, false);

                    txtCountSaleAudit.setText(String.format("ลูกค้าที่ต้องตรวจสอบจำนวน %d คน", checkCustomerList.size()));
                } else {
                    lvCustomerList.setVisibility(View.GONE);
                    lvCustomerList.setAdapter(null);

                    txtCountSaleAudit.setText("ลูกค้าที่ต้องตรวจสอบจำนวน 0 คน");

                    showDialog("แจ้งเตือน", "ไม่พบข้อมูล");
                }
            }
        });

    }

    private void binHeader() {
        LayoutInflater li = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) li.inflate(R.layout.list_credit_audit_check_customers_header, lvCustomerList, false);
        lvCustomerList.addHeaderView(header, null, false);

        mController = buildController(lvCustomerList);
        lvCustomerList.setFloatViewManager(mController);
        lvCustomerList.setOnTouchListener(mController);
        lvCustomerList.setDragEnabled(true);
        lvCustomerList.setDropListener(onDrop);
        lvCustomerList.setEnableAlpha(false);

        //ลำดับ
        final TextView header_txt_no = (TextView) header.findViewById(R.id.header_txt_no);
        header_txt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvCustomerList.setAdapter(null);

                if (header_txt_no_DESC) {
                    //ORDER BY Order ASC
                    Collections.sort(checkCustomerList, new Comparator<AssignInfo>() {
                        public int compare(AssignInfo a1, AssignInfo a2) {
                            return String.valueOf(String.format("%4d", (a1.Order))).compareTo(String.valueOf(String.format("%4d", (a2.Order))));
                            //return String.valueOf(a1.Order).compareTo(String.valueOf(a2.Order));
                        }
                    });
                    header_txt_no_DESC = false;

                } else {
                    //ORDER BY Order DESC
                    Collections.sort(checkCustomerList, new Comparator<AssignInfo>() {
                        public int compare(AssignInfo a1, AssignInfo a2) {
                            return String.valueOf(String.format("%4d", (a2.Order))).compareTo(String.valueOf(String.format("%4d", (a1.Order))));
                            //return String.valueOf(a2.Order).compareTo(String.valueOf(a1.Order));
                        }
                    });
                    header_txt_no_DESC = true;
                }
                bindListView(false, false);
            }
        });

        //ชื่อ-นามสกุล
        TextView header_txt_name = (TextView) header.findViewById(R.id.header_txt_name);
        header_txt_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvCustomerList.setAdapter(null);

                if (header_txt_name_DESC) {
                    //ORDER BY CustomerFullName ASC
                    Collections.sort(checkCustomerList, new Comparator<AssignInfo>() {
                        public int compare(AssignInfo a1, AssignInfo a2) {
                            return a1.CustomerFullName.compareTo(a2.CustomerFullName);
                        }
                    });
                    header_txt_name_DESC = false;

                } else {
                    //ORDER BY CustomerFullName DESC
                    Collections.sort(checkCustomerList, new Comparator<AssignInfo>() {
                        public int compare(AssignInfo a1, AssignInfo a2) {
                            return a2.CustomerFullName.compareTo(a1.CustomerFullName);
                        }
                    });
                    header_txt_name_DESC = true;
                }
                bindListView(false, false);
            }
        });

        //งวดที่
        TextView header_txt_period_number = (TextView) header.findViewById(R.id.header_txt_period_number);
        header_txt_period_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvCustomerList.setAdapter(null);

                if (header_txt_period_number_DESC) {
                    //ORDER BY PaymentPeriodNumber ASC
                    Collections.sort(checkCustomerList, new Comparator<AssignInfo>() {
                        public int compare(AssignInfo a1, AssignInfo a2) {
                            return String.valueOf(String.format("%2d", (a1.PaymentPeriodNumber))).compareTo(String.valueOf(String.format("%2d", (a2.PaymentPeriodNumber))));
                            //return String.valueOf(a1.PaymentPeriodNumber).compareTo(String.valueOf(a2.PaymentPeriodNumber));
                        }
                    });
                    header_txt_period_number_DESC = false;

                } else {
                    //ORDER BY PaymentPeriodNumber DESC
                    Collections.sort(checkCustomerList, new Comparator<AssignInfo>() {
                        public int compare(AssignInfo a1, AssignInfo a2) {
                            return String.valueOf(String.format("%2d", (a2.PaymentPeriodNumber))).compareTo(String.valueOf(String.format("%2d", (a1.PaymentPeriodNumber))));
                            //return String.valueOf(a2.PaymentPeriodNumber).compareTo(String.valueOf(a1.PaymentPeriodNumber));
                        }
                    });
                    header_txt_period_number_DESC = true;
                }
                bindListView(false, false);
            }
        });

        //ค่างวด
        TextView header_txt_payment = (TextView) header.findViewById(R.id.header_txt_payment);
        header_txt_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvCustomerList.setAdapter(null);

                if (header_txt_payment_DESC) {
                    //ORDER BY NetAmount ASC
                    Collections.sort(checkCustomerList, new Comparator<AssignInfo>() {
                        public int compare(AssignInfo a1, AssignInfo a2) {
                            return String.valueOf(String.format("%5f", (a1.NetAmount))).compareTo(String.valueOf(String.format("%5f", (a2.NetAmount))));
                            //return String.valueOf(a1.NetAmount).compareTo(String.valueOf(a2.NetAmount));
                        }
                    });
                    header_txt_payment_DESC = false;

                } else {
                    //ORDER BY NetAmount DESC
                    Collections.sort(checkCustomerList, new Comparator<AssignInfo>() {
                        public int compare(AssignInfo a1, AssignInfo a2) {
                            return String.valueOf(String.format("%5f", (a2.NetAmount))).compareTo(String.valueOf(String.format("%5f", (a1.NetAmount))));
                            //return String.valueOf(a2.NetAmount).compareTo(String.valueOf(a1.NetAmount));
                        }
                    });
                    header_txt_payment_DESC = true;
                }
                bindListView(false, false);
            }
        });

        //CheckedAll
        CheckBox header_checkBox = (CheckBox) header.findViewById(R.id.header_checkBox);
        header_checkBox.setChecked(CheckedAll);

        header_checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                lvCustomerList.setAdapter(null);
                bindListView(true, isChecked);
            }
        });
    }

    private void bindListView(final boolean CheckedAll1, final boolean isChecked1) {

        CheckedAll = CheckedAll1;
        isChecked = isChecked1;
        customerAdapter = new CustomerAdapter(activity, R.layout.list_credit_audit_check_customers_item, checkCustomerList);
        lvCustomerList.setAdapter(customerAdapter);
    }

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                AssignInfo itemFrom = checkCustomerList.get(from);
                customerAdapter.remove(itemFrom);
                customerAdapter.insert(itemFrom, to);

                for (int i = 0; i < checkCustomerList.size(); i++) {
                    checkCustomerList.get(i).Order = i + 1;
                }

                CheckedAll = false;
                isChecked = false;
            }
        }
    };

    public class CustomerAdapter extends BHArrayAdapter<AssignInfo> {

        public CustomerAdapter(Context context, int resource, List<AssignInfo> objects) {
            super(context, resource, objects);
        }

        private class ViewHolder {
            public TextView txt_no, txt_name, txt_period_number, txt_payment;
            public CheckBox checkBox;
        }

        @Override
        protected void onViewItem(final int position, View view, Object holder, AssignInfo info) {
            ViewHolder vh = (ViewHolder) holder;

            vh.txt_no.setText(String.valueOf(info.Order));

            /*** [START] :: Fixed - [BHPROJ-0026-740] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/
//            AddressInfo addressInfo = TSRController.getAddress(info.RefNo, AddressInfo.AddressType.AddressInstall);

            SpannableString txtCustomer = new SpannableString( info.CustomerFullName + "\n" + info.CONTNO + "\n" + info.getAddress().Address() + "\nTel. " + info.getAddress().Telephone());
            txtCustomer.setSpan(new ForegroundColorSpan(Color.BLACK), 0, info.CustomerFullName.length() + info.CONTNO.length(), 0);
            txtCustomer.setSpan(new ForegroundColorSpan(Color.GRAY), info.CustomerFullName.length() + info.CONTNO.length() + 1, txtCustomer.length(), 0);
            vh.txt_name.setText(txtCustomer, TextView.BufferType.SPANNABLE);

            //vh.txt_name.setText(info.CustomerFullName);
            /*** [END] :: Fixed - [BHPROJ-0026-740] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/

            vh.txt_period_number.setText(String.valueOf(info.PaymentPeriodNumber));
            vh.txt_payment.setText(BHUtilities.numericFormat(info.NetAmount, "#,##0"));


            if (CheckedAll) {
                vh.checkBox.setChecked(isChecked);
                checkCustomerList.get(position).Selected = isChecked;
            } else {
                vh.checkBox.setChecked(info.Selected);
            }

            //ลำดับ
            vh.txt_no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BarcodeScan(checkCustomerList.get(position).RefNo);
                }
            });

            //ชื่อ-นามสกุล
            /*vh.txt_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDoubleClick(position);
                }
            });*/

            vh.txt_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BarcodeScan(checkCustomerList.get(position).RefNo);
                }
            });

            //งวดที่
            /*vh.txt_period_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDoubleClick(position);
                }
            });*/

            vh.txt_period_number.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BarcodeScan(checkCustomerList.get(position).RefNo);
                }
            });

            //ค่างวด
            /*vh.txt_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDoubleClick(position);
                }
            });*/

            vh.txt_payment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BarcodeScan(checkCustomerList.get(position).RefNo);
                }
            });

            //CheckBox
            vh.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    AssignInfo getOutput = checkCustomerList.get(position);
                    getOutput.Selected = isChecked;
                    checkCustomerList.set(position, getOutput);
                    //showMessage("checkBox");
                }
            });

        }
    }

    private void onDoubleClick(int position) {
        if (((SystemClock.elapsedRealtime() - timestampLastClick) < timeDoubleClick) && (positionClickListView == position)) {
            BarcodeScan(checkCustomerList.get(position).RefNo);
        }
        positionClickListView = position;
        timestampLastClick = SystemClock.elapsedRealtime();
    }

    /*public DragSortController getController() {
        return mController;
    }*/

    public DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.txt_no);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
        controller.setBackgroundColor(getResources().getColor(R.color.bg_list_view_selected));
        return controller;
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        if (checkCustomerList != null) {
            List<AssignInfo> newCheckCustomerList = new ArrayList<AssignInfo>();


            for (AssignInfo info : checkCustomerList) {
                if (info.Selected) {
                    newCheckCustomerList.add(info);
                }
            }

            switch (buttonID) {
                case R.string.button_check_audit:

                    switch (newCheckCustomerList.size()) {
                        case 0:
                            showDialog("แจ้งเตือน", "กรุณาเลือกข้อมูล");
                            break;
                        case 1:
                            BarcodeScan(newCheckCustomerList.get(0).RefNo);
                            break;
                        default:
                            showDialog("แจ้งเตือน", "กรุณาเลือกรายการที่ต้องการตรวจสอบเพียงรายการเดียว");
                            break;
                    }

                    break;
                case R.string.button_display_map:

                    if (newCheckCustomerList.size() > 0) {
                        CheckCustomersMapFragment.Data data = new CheckCustomersMapFragment.Data();
                        data.checkCustomerList = newCheckCustomerList;

                        CheckCustomersMapFragment fm = BHFragment.newInstance(CheckCustomersMapFragment.class, data);
                        showNextView(fm);
                    } else {
                        showDialog("แจ้งเตือน", "กรุณาเลือกข้อมูล");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void BarcodeScan(final String refNo) {
        // TODO Auto-generated method stub

        /*** [START] :: Fixed - [BHPROJ-0025-757] :: [Android-ตรวจสอบลูกค้า] หากสัญญาที่ถูกเลือกมาจากหน้า List รายการเพื่อจะเข้ามาตรวจสอบเป็นสัญญาที่ไม่มี ProductSerialNumber ก็ให้กระโดดไปที่หน้าจอตรวจสอบลูกค้าเลย ***/
        /*
        BarcodeScanFragment fm = BHFragment.newInstance(BarcodeScanFragment.class, new BarcodeScanFragment.ScanCallBack() {

            @Override
            public void onResult(BHParcelable data) {
                // TODO Auto-generated method stub
                final BarcodeScanFragment.Result barcodeResult = (BarcodeScanFragment.Result) data;

                (new BackgroundProcess(activity) {

                    ContractInfo contractInfo = new ContractInfo();

                    @Override
                    protected void calling() {
                        // TODO Auto-generated method stub
                        contractInfo = TSRController.getContract(refNo);
                    }

                    @Override
                    protected void after() {
                        // TODO Auto-generated method stub
                        if (contractInfo.ProductSerialNumber.equals(barcodeResult.barcode)) {
                            BHPreference.setRefNo(contractInfo.RefNo);
                            showNextView(new CheckCustomersAuditFragment());
                        } else {
                            showDialog("แจ้งเตือน", "รหัสสินค้าไม่ตรงกับรายการที่ท่านเลือก");
                        }
                    }

                }).start();
            }
            // @Override
            // public String onNextClick() {
            // return "SA10578236";
            // }
        });
        fm.setTitle(R.string.title_check_customers);
        fm.setViewTitle(R.string.title_scan_product_serial);
        showNextView(fm);
        */


        (new BackgroundProcess(activity) {
            ContractInfo contractInfo = new ContractInfo();

            @Override
            protected void calling() {
                contractInfo = TSRController.getContract(refNo);
            }

            @Override
            protected void after() {
                if (contractInfo != null) {
                    if ((contractInfo.ProductSerialNumber == null) || (contractInfo.ProductSerialNumber.equals("") || (contractInfo.ProductSerialNumber.length() == 0))) {
                        // (A) No has ProductSerialNumber ==> Go to SaleAudit screen
                        BHPreference.setRefNo(contractInfo.RefNo);
                        showNextView(new CheckCustomersAuditFragment());
                    } else {
                        // (B) Has ProductSerialNumber
                        BarcodeScanFragment fm = BHFragment.newInstance(BarcodeScanFragment.class, new BarcodeScanFragment.ScanCallBack() {
                            @Override
                            public void onResult(BHParcelable data) {
                                // TODO Auto-generated method stub
                                final BarcodeScanFragment.Result barcodeResult = (BarcodeScanFragment.Result) data;
                                if (contractInfo.ProductSerialNumber.equals(barcodeResult.barcode)) {
                                    BHPreference.setRefNo(contractInfo.RefNo);
                                    showNextView(new CheckCustomersAuditFragment());
                                } else {
                                    showDialog("แจ้งเตือน", "รหัสสินค้าไม่ตรงกับรายการที่ท่านเลือก");
                                }
                            }
                        });
                        fm.setTitle(R.string.title_check_customers);
                        fm.setViewTitle(R.string.title_scan_product_serial);
                        showNextView(fm);
                    }   // (B)
                }
            }   // after

        }).start();


        /*** [END] :: Fixed - [BHPROJ-0025-757] :: [Android-ตรวจสอบลูกค้า] หากสัญญาที่ถูกเลือกมาจากหน้า List รายการเพื่อจะเข้ามาตรวจสอบเป็นสัญญาที่ไม่มี ProductSerialNumber ก็ให้กระโดดไปที่หน้าจอตรวจสอบลูกค้าเลย ***/


    }
}
