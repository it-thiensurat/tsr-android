package th.co.thiensurat.fragments.credit.SortOrderDefault;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.controller.TripController;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.TripInfo;

//-- Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย

public class SortOrderDefaultForCreditMainFragment extends BHFragment {

    public enum SortType {
        Audit, Credit
    }

    public static class Data extends BHParcelable {
        public SortType sortType;
    }

    private Data data;

    @InjectView
    private TextView txtViewCount;
    @InjectView
    private LinearLayout llHeaderOrder;
    @InjectView
    private ListView lvOrderList;
    @InjectView
    private EditText txtSearch;

    private boolean Is_CONTNO_No_DESC, Is_Name_DESC, Is_AppointmentDate_DESC,  Is_Order_DESC;

    private List<AssignInfo> mAssignInfoList;
    private List<AssignInfo> mTempAssignInfoList;
    private OrderAdapter orderAdapter;

    private ProgressDialog progressDoalog;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_sort_order_default_main;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_save};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        if (data == null) {
            data = getData();
        }

        (new BackgroundProcess(activity) {

            List<AssignInfo> assignInfoList;

            @Override
            protected void before() {
                assignInfoList = new ArrayList<AssignInfo>();

                if (mAssignInfoList == null) {
                    mAssignInfoList = new ArrayList<AssignInfo>();
                } else {
                    mAssignInfoList.clear();
                }

                if (mTempAssignInfoList == null) {
                    mTempAssignInfoList = new ArrayList<AssignInfo>();
                } else {
                    mTempAssignInfoList.clear();
                }

                Is_CONTNO_No_DESC = false;
                Is_Name_DESC = false;
                Is_AppointmentDate_DESC = false;
                Is_Order_DESC = true;

                orderAdapter = new OrderAdapter(activity, R.layout.list_sort_order_default_for_credit, mAssignInfoList);
                lvOrderList.setAdapter(orderAdapter);
            }

            @Override
            protected void calling() {
                assignInfoList = getQuery();
            }

            @Override
            protected void after() {
                if (assignInfoList.size() > 0) {

                    binHeader();
                    mAssignInfoList.addAll(assignInfoList);
                    mTempAssignInfoList.addAll(assignInfoList);

                    txtViewCount.setText(getTextView(assignInfoList.size()));

                    txtSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (mAssignInfoList == null) {
                                mAssignInfoList = new ArrayList<AssignInfo>();
                            } else {
                                mAssignInfoList.clear();
                            }

                            String strPattern = String.format("(?i).*%s.*", s.toString());
                            if (!s.toString().equals("")) {
                                for (AssignInfo info : mTempAssignInfoList) {
                                    if (info.CONTNO.matches(strPattern) || info.CustomerFullName.matches(strPattern)) {
                                        mAssignInfoList.add(info);
                                    }
                                }
                            } else {
                                mAssignInfoList.addAll(mTempAssignInfoList);
                            }
                            orderAdapter.notifyDataSetChanged();
                        }
                    });


                    /*lvOrderList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final AssignInfo info = mAssignInfoList.get(position);
                            final AssignInfo infoTemp = mTempAssignInfoList.get(getIndex(mTempAssignInfoList, info.AssignID));

                            final ArrayAdapter<String> arrayAdapter = getOrderNotUsed();
                            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                            builder.setTitle("เลือกลำดับ");
                            builder.setCancelable(false);
                            builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String str = which == 0 ? "" : arrayAdapter.getItem(which);
                                    info.NewOrder = str.toString().equals("") ? 0 : Integer.parseInt(str.toString());
                                    infoTemp.NewOrder = info.NewOrder;
                                    orderAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            });
                            builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }
                    });*/

                    activity.menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
                        @Override
                        public void onOpened() {
                            if (mTempAssignInfoList != null) {
                                List<AssignInfo> infoList = new ArrayList<>(getOrderIsChanging());

                                if (infoList.size() != 0) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                                    builder.setTitle("แจ้งเตือน");
                                    builder.setMessage("พบการเปลี่ยนลำดับของข้อมูล ต้องการกลับไปบันทึกหรือไม่");
                                    builder.setCancelable(false);
                                    builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            activity.menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
                                            activity.menu.showContent();
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.show();
                                }
                            }
                        }
                    });
                }

                if (orderAdapter != null) {
                    lvOrderList.clearChoices();
                    orderAdapter.notifyDataSetChanged();
                }
            }
        }).start();
    }

    /*** [START] :: Fixed - [BHPROJ-0026-3263][Android-เมนูการจัดลำดับค่าเริ่มต้นของฝ่ายเครดิต] ถ้าเข้าเมนูจัดลำดับ แล้วไปเข้าเมนูอื่นๆ จากนั้นกลับมาเข้าเมนูจัดลำดับอีกครั้ง ระบบจะ Hang ***/
    @Override
    public void onDetach() {
        super.onDetach();

            activity.menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
                @Override
                public void onOpened() {

                }
            });
        }
    /*** [END] :: Fixed - [BHPROJ-0026-3263][Android-เมนูการจัดลำดับค่าเริ่มต้นของฝ่ายเครดิต] ถ้าเข้าเมนูจัดลำดับ แล้วไปเข้าเมนูอื่นๆ จากนั้นกลับมาเข้าเมนูจัดลำดับอีกครั้ง ระบบจะ Hang ***/

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_save:
                new BackgroundProcess(activity) {

                    String messageOrderNotUsed;
                    boolean isNoOrder;
                    boolean isOverOrder;
                    boolean isDuplicateOrder;

                    List<AssignInfo> errorList;

                    @Override
                    protected void before() {
                        messageOrderNotUsed = "";

                        errorList = new ArrayList<AssignInfo>();
                    }

                    @Override
                    protected void calling() {
                        int maxOrder = mTempAssignInfoList.size();
                        for (AssignInfo info : mTempAssignInfoList) {

                            //ตรวจสอบ Order ที่ไม่ใส่ลำดับ
                            if (info.NewOrder == 0) {
                                isNoOrder = true;
                                errorList.add(info);
                            } else {
                                //ตรวจสอบ Order ที่เกินลำดับ
                                if (info.NewOrder > maxOrder) {
                                    isOverOrder = true;
                                    errorList.add(info);
                                } else {
                                    //ตรวจสอบค่าซ้ำ
                                    if (checkDuplicateValuesByOrder(info)) {
                                        isDuplicateOrder = true;
                                        errorList.add(info);
                                    }
                                }
                            }
                        }


                        if (isNoOrder || isOverOrder || isDuplicateOrder) {
                            List<Integer> listCompare = getOrderNotUsed();

                            messageOrderNotUsed += "\n\nลำดับที่ไม่ถูกใช้งาน : ";

                            for (int lc = 0; lc < listCompare.size(); lc++) {
                                messageOrderNotUsed += String.format("%d%s", listCompare.get(lc), lc != (listCompare.size() - 1) ? ", " : "");
                            }
                        }
                    }

                    @Override
                    protected void after() {

                        List<AssignInfo> infoList = new ArrayList<>(getOrderIsChanging());

                        if(isNoOrder || isOverOrder || isDuplicateOrder) {
                            String strNoOrder = "ข้อมูลที่ไม่ถูกใส่ลำดับ";
                            String strOverOrder = "ข้อมูลที่เกินลำดับ";
                            String strDuplicateOrder = "ข้อมูลที่ใส่ลำดับซ้ำกัน";
                            String message = "";

                            if(isNoOrder) {
                                message += String.format("ตรวจพบ%s", strNoOrder);
                            }

                            if(isOverOrder) {
                                if(message.isEmpty()){
                                    message += String.format("ตรวจพบ%s", strOverOrder);
                                } else {
                                    if(isDuplicateOrder) {
                                        message += String.format(" %s", strOverOrder);
                                    } else {
                                        message += String.format(" และ%s", strOverOrder);
                                    }
                                }
                            }

                            if(isDuplicateOrder) {
                                if(message.isEmpty()){
                                    message += String.format("ตรวจพบ%s", strDuplicateOrder);
                                } else {
                                    message += String.format(" และ%s", strDuplicateOrder);
                                }
                            }

                            message += " กดตกลงเพื่อโชว์ข้อมูล";
                            message += messageOrderNotUsed;
                            showDialogBoxConfirm("แจ้งเตือน", message, infoList, errorList);
                        } else {
                            if (infoList.size() == 0) {
                                showWarningDialog("แจ้งเตือน", "ไม่พบการเปลี่ยนลำดับของข้อมูล");
                            } else {
                                showDialogBoxConfirm("แจ้งเตือน", "ยืนยันการบันทึกข้อมูล", infoList);
                            }
                        }

//                        if (isSave) {
//
//                            if (infoList.size() == 0 && isNoOrder) {
//                                showWarningDialog("แจ้งเตือน", "ไม่พบการเปลี่ยนลำดับของข้อมูล");
//                            } else {
//                                if (isNoOrder) {
//                                    showDialogBoxConfirm("แจ้งเตือน", "ยืนยันการบันทึกข้อมูล", infoList);
//                                } else {
//                                    showDialogBoxConfirm("แจ้งเตือน", "ตรวจพบข้อมูลที่ไม่ถูกใส่ลำดับ กดตกลงเพื่อโชว์ข้อมูล" + messageOrderNotUsed, infoList, errorList);
//                                }
//                            }
//                        } else {
//                            message += messageOrderNotUsed;
//                            showDialogBoxWithMove(title, message, errorList);
//                        }

                    }
                }.start();

                break;
        }
    }

    private class OrderAdapter extends BHArrayAdapter<AssignInfo> {
        public class ViewHolder {
            public TextView txtCONTNO_No, txtName, txtAppointmentDate, txtOrder;
        }

        public OrderAdapter(Context context, int resource, List<AssignInfo> objects) {
            super(context, resource, objects);
        }

        @Override
        protected void onViewItem(final int position, final View view, Object holder, final AssignInfo info) {

            final ViewHolder vh = (ViewHolder) holder;

            vh.txtCONTNO_No.setText(info.CONTNO);
            vh.txtName.setText(info.CustomerFullName);

            vh.txtAppointmentDate.setText( info.NewPaymentDueDay == 0 ? "" : String.valueOf(info.NewPaymentDueDay));
            vh.txtAppointmentDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AssignInfo info = mAssignInfoList.get(position);
                    //final AssignInfo infoTemp = mTempAssignInfoList.get(getIndex(mTempAssignInfoList, info.AssignID));


                    boolean isTrip = true;
                    List<SalePaymentPeriodInfo> salePaymentPeriodInfo = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoAndPaymentComplete(info.RefNo, 0);
                    for(SalePaymentPeriodInfo sppInfo : salePaymentPeriodInfo) {

                        TripInfo tripInfo = new TripController().getTrip(sppInfo.PaymentAppointmentDate);
                        if (tripInfo == null) {
                            isTrip = false;
                            showDialog("แจ้งเตือน", String.format("ไม่พบทริปการชำระเงินของวันที่ %s", BHUtilities.dateFormat(sppInfo.PaymentAppointmentDate)));
                            break;
                        }
                    }

                    if(isTrip) {
                        final ArrayAdapter<String> arrayAdapter = getDay();
                        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setTitle("เลือกวันที่นัดชำระ");
                        builder.setCancelable(false);
                        builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String str = arrayAdapter.getItem(which);
                                info.NewPaymentDueDay = Integer.parseInt(str.toString());
                                //infoTemp.NewPaymentDueDay = info.NewPaymentDueDay;
                                orderAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
            });

            vh.txtOrder.setText(String.valueOf(info.NewOrder == 0 ? "" : info.NewOrder));
            vh.txtOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AssignInfo info = mAssignInfoList.get(position);
                    //final AssignInfo infoTemp = mTempAssignInfoList.get(getIndex(mTempAssignInfoList, info.AssignID));


                    final ArrayAdapter<String> arrayAdapter = getListOrderNotUsed();
                    final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("เลือกลำดับ หรือระบุบลำดับที่ต้องการ");
                    builder.setCancelable(false);
                    builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            lvOrderList.requestFocus();
                            dialog.dismiss();
                        }
                    });
//                    builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            if (!input.getText().toString().isEmpty()) {
//                                dialog.dismiss();
//                            } else {
//                                showMessage("กรุณาระบุบลำดับที่ต้องการ");
//                                dialog.cancel();
//                            }
//                        }
//                    });
                    //builder.show();

                    LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
                    View mView = layoutInflaterAndroid.inflate(R.layout.sort_order_dialog, null);
                    builder.setView(mView);
                    final EditText editText = (EditText) mView.findViewById(R.id.editText_order);
                    final Button button = (Button) mView.findViewById(R.id.button_order);
                    final ListView listView = (ListView) mView.findViewById(R.id.listView_order);
                    listView.setAdapter(arrayAdapter);
                    listView.requestFocus();

                    final AlertDialog alert = builder.create();
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int positioLlistView, long id) {
                            int intOrder = positioLlistView == 0 ? 0 : Integer.parseInt(arrayAdapter.getItem(positioLlistView).toString());
                            updataAutoNewOrder(info, info.NewOrder, intOrder);
                            alert.dismiss();
                        }
                    });

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String strOrder = editText.getText().toString();
                            if(strOrder.isEmpty()){
                                showMessage("กรุณาระบุบลำดับที่ต้องการ");
                            } else {
                                int intOrder = Integer.parseInt(strOrder.toString());
                                if(intOrder == 0){
                                    showMessage("กรุณาระบุบลำดับที่มากกว่า 0");
                                } else {
                                    updataAutoNewOrder(info, info.NewOrder, intOrder);
                                    alert.dismiss();
                                }
                            }
                        }
                    });
                    alert.show();

                }
            });
        }
    }

    private void binHeader() {
        LayoutInflater inflater = activity.getLayoutInflater();
        LinearLayout header = (LinearLayout) inflater.inflate(R.layout.sort_order_default_list_head_for_credit, lvOrderList, false);
        llHeaderOrder.addView(header);

        //เลขที่สัญญา
        final TextView header_CONTNO_No = (TextView) header.findViewById(R.id.header_CONTNO_No);
        header_CONTNO_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(mAssignInfoList, new Comparator<AssignInfo>() {
                    public int compare(AssignInfo a1, AssignInfo a2) {
                        if (!Is_CONTNO_No_DESC) {
                            //ASC
                            return a1.CONTNO.compareTo(a2.CONTNO);
                        } else {
                            //DESC
                            return a2.CONTNO.compareTo(a1.CONTNO);
                        }
                    }
                });

                Collections.sort(mTempAssignInfoList, new Comparator<AssignInfo>() {
                    public int compare(AssignInfo a1, AssignInfo a2) {
                        if (!Is_CONTNO_No_DESC) {
                            //ASC
                            return a1.CONTNO.compareTo(a2.CONTNO);
                        } else {
                            //DESC
                            return a2.CONTNO.compareTo(a1.CONTNO);
                        }
                    }
                });

                Is_CONTNO_No_DESC = !Is_CONTNO_No_DESC;

                orderAdapter.notifyDataSetChanged();
            }
        });

        //ชื่อลูกค้า
        final TextView header_Name = (TextView) header.findViewById(R.id.header_Name);
        header_Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(mAssignInfoList, new Comparator<AssignInfo>() {
                    public int compare(AssignInfo a1, AssignInfo a2) {
                        if (!Is_Name_DESC) {
                            //ASC
                            return a1.CustomerFullName.compareTo(a2.CustomerFullName);
                        } else {
                            //DESC
                            return a2.CustomerFullName.compareTo(a1.CustomerFullName);
                        }
                    }
                });

                Collections.sort(mTempAssignInfoList, new Comparator<AssignInfo>() {
                    public int compare(AssignInfo a1, AssignInfo a2) {
                        if (!Is_Name_DESC) {
                            //ASC
                            return a1.CustomerFullName.compareTo(a2.CustomerFullName);
                        } else {
                            //DESC
                            return a2.CustomerFullName.compareTo(a1.CustomerFullName);
                        }
                    }
                });

                Is_Name_DESC = !Is_Name_DESC;

                orderAdapter.notifyDataSetChanged();
            }
        });

        //วันที่นัดชำระ
        final TextView header_appointment_date = (TextView) header.findViewById(R.id.header_appointment_date);
        header_appointment_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(mAssignInfoList, new Comparator<AssignInfo>() {
                    public int compare(AssignInfo a1, AssignInfo a2) {
                        if (!Is_AppointmentDate_DESC) {
                            //ASC
                            return String.valueOf(String.format("%2d", (a1.NewPaymentDueDay))).compareTo(String.valueOf(String.format("%2d", (a2.NewPaymentDueDay))));

                        } else {
                            //DESC
                            return String.valueOf(String.format("%2d", (a2.NewPaymentDueDay))).compareTo(String.valueOf(String.format("%2d", (a1.NewPaymentDueDay))));
                        }
                    }
                });

                Collections.sort(mTempAssignInfoList, new Comparator<AssignInfo>() {
                    public int compare(AssignInfo a1, AssignInfo a2) {
                        if (!Is_AppointmentDate_DESC) {
                            //ASC
                            return String.valueOf(String.format("%2d", (a1.NewPaymentDueDay))).compareTo(String.valueOf(String.format("%2d", (a2.NewPaymentDueDay))));

                        } else {
                            //DESC
                            return String.valueOf(String.format("%2d", (a2.NewPaymentDueDay))).compareTo(String.valueOf(String.format("%2d", (a1.NewPaymentDueDay))));
                        }
                    }
                });

                Is_AppointmentDate_DESC = !Is_AppointmentDate_DESC;

                orderAdapter.notifyDataSetChanged();
            }
        });

        //ลำดับ
        final TextView header_Order = (TextView) header.findViewById(R.id.header_Order);
        header_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(mAssignInfoList, new Comparator<AssignInfo>() {
                    public int compare(AssignInfo a1, AssignInfo a2) {
                        if (!Is_Order_DESC) {
                            //ASC
                            return String.valueOf(String.format("%5d", (a1.NewOrder))).compareTo(String.valueOf(String.format("%5d", (a2.NewOrder))));

                        } else {
                            //DESC
                            return String.valueOf(String.format("%5d", (a2.NewOrder))).compareTo(String.valueOf(String.format("%5d", (a1.NewOrder))));
                        }
                    }
                });

                Collections.sort(mTempAssignInfoList, new Comparator<AssignInfo>() {
                    public int compare(AssignInfo a1, AssignInfo a2) {
                        if (!Is_Order_DESC) {
                            //ASC
                            return String.valueOf(String.format("%5d", (a1.NewOrder))).compareTo(String.valueOf(String.format("%5d", (a2.NewOrder))));

                        } else {
                            //DESC
                            return String.valueOf(String.format("%5d", (a2.NewOrder))).compareTo(String.valueOf(String.format("%5d", (a1.NewOrder))));
                        }
                    }
                });

                Is_Order_DESC = !Is_Order_DESC;

                orderAdapter.notifyDataSetChanged();
            }
        });
    }

    private boolean checkDuplicateValuesByOrder(AssignInfo assignInfo) {
        for (AssignInfo info : mTempAssignInfoList) {
            if (!info.AssignID.equals(assignInfo.AssignID)) {
                if (info.NewOrder == assignInfo.NewOrder) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showDialogBoxWithMove(String title, String message, final List<AssignInfo> errorList) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAssignInfoList.clear();
                mAssignInfoList.addAll(errorList);
                orderAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }

    private void showDialogBoxConfirm(String title, String message, final List<AssignInfo> infoList) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveAssign(infoList);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void showDialogBoxConfirm(String title, String message, final List<AssignInfo> infoList, final List<AssignInfo> errorList) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.button_save), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                saveAssign(infoList);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNeutralButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAssignInfoList.clear();
                mAssignInfoList.addAll(errorList);
                orderAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private List<AssignInfo> getOrderIsChanging() {
        //ตรวจสอบ Order ที่มีการเปลี่ยนแปลง
        List<AssignInfo> newList = new ArrayList<>();
        for (AssignInfo info : mTempAssignInfoList) {
            if ((info.NewOrder != info.Order) || (info.NewOrder != info.OrderExpect) || (info.NewPaymentDueDay != info.OldPaymentDueDay)) {
                newList.add(info);
            }
        }
        return newList;
    }

    private void saveAssign(final List<AssignInfo> infoList) {
        final Handler handle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                progressDoalog.incrementProgressBy(1);
            }
        };

        progressDoalog = new ProgressDialog(activity);
        progressDoalog.setIndeterminate(false);
        progressDoalog.setCancelable(false);
        progressDoalog.setMax(infoList.size());
        //progressDoalog.setMessage("loading....");
        progressDoalog.setMessage("กำลังบันทึก....");
        progressDoalog.setTitle("บันทึกลำดับค่าเริ่มต้น");
        progressDoalog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDoalog.show();

        new BackgroundProcess(activity) {
            List<AssignInfo> assignInfoList;
            Date lastUpdateDate;
            String lastUpdateBy;

            @Override
            protected void before() {
                assignInfoList = new ArrayList<AssignInfo>();
                if (mAssignInfoList == null) {
                    mAssignInfoList = new ArrayList<AssignInfo>();
                } else {
                    mAssignInfoList.clear();
                }

                if (mTempAssignInfoList == null) {
                    mTempAssignInfoList = new ArrayList<AssignInfo>();
                } else {
                    mTempAssignInfoList.clear();
                }

                Is_CONTNO_No_DESC = false;
                Is_Name_DESC = false;
                Is_AppointmentDate_DESC = false;
                Is_Order_DESC = true;
            }

            @Override
            protected void calling() {
                lastUpdateDate = new Date();
                lastUpdateBy = BHPreference.employeeID();

                for (AssignInfo info : infoList) {

                    /*if(info.OldPaymentDueDay != info.NewPaymentDueDay){
                        Calendar c = getInstance();
                        c.setTime(info.PaymentDueDate);
                        //c.set(Calendar.MONTH, 1);
                        while (c.getActualMaximum(Calendar.DAY_OF_MONTH) < info.NewPaymentDueDay){
                            c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
                        }
                        c.set(Calendar.DAY_OF_MONTH, info.NewPaymentDueDay);
                        info.NewPaymentDueDate = c.getTime();
                    } else {
                        info.NewPaymentDueDate = info.PaymentDueDate;
                    }*/

                    info.LastUpdateDate = lastUpdateDate;
                    info.LastUpdateBy = lastUpdateBy;
                    TSRController.updateAssignForSortOrderDefaultForCredit(info, true);
                    handle.sendMessage(handle.obtainMessage());
                }

                TSRController.commitChangeOrder(BHPreference.employeeID(), AssignController.AssignTaskType.SalePaymentPeriod.toString(), true);
                assignInfoList = getQuery();
            }

            @Override
            protected void after() {
                if (assignInfoList.size() > 0) {
                    mAssignInfoList.addAll(assignInfoList);
                    mTempAssignInfoList.addAll(assignInfoList);

                    txtViewCount.setText(getTextView(assignInfoList.size()));
                }

                if (orderAdapter != null) {
                    lvOrderList.clearChoices();
                    orderAdapter.notifyDataSetChanged();
                }

                progressDoalog.dismiss();
            }
        }.start(false);
    }

    private List<AssignInfo> getQuery() {
        List<AssignInfo> info = new AssignController().getAssignSortOrderDefaultByAssigneeEmpIDForCredit(
                BHPreference.organizationCode(),
                data.sortType == SortType.Audit ? AssignController.AssignTaskType.SaleAudit.toString() : AssignController.AssignTaskType.SalePaymentPeriod.toString(),
                BHPreference.employeeID());
        return info == null ? new ArrayList<AssignInfo>() : info;
    }

    private String getTextView(int size) {
        String str = "";

        switch (data.sortType) {
            case Audit:
                str = String.format("ลูกค้าที่ต้องตรวจสอบจำนวน %d คน", size);
                break;
            case Credit:
                str = String.format("ลูกค้าที่ต้องเก็บเงินจำนวน %d คน", size);
                break;
        }

        return str;

    }

    private ArrayAdapter<String> getListOrderNotUsed() {
        List<Integer> listCompare = getOrderNotUsed();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item);
        arrayAdapter.add("ไม่ระบุลำดับ");
        for (Integer l : listCompare) {
            arrayAdapter.add(String.valueOf(l));
        }

        return arrayAdapter;
    }

    private ArrayAdapter<String> getDay() {
        List<Integer> listCompare = new ArrayList<>();

        for(int i = 1; i <= 31; i++) {
            listCompare.add(i);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item);
        for (Integer l : listCompare) {
            arrayAdapter.add(String.valueOf(l));
        }

        return arrayAdapter;
    }

    private int getIndex(List<AssignInfo> assignInfoList, String AssignID) {
        for (int i = 0; i < assignInfoList.size(); i++) {
            if (assignInfoList.get(i).AssignID.equals(AssignID)) {
                return i;
            }
        }
        return -1;
    }

    private void updataAutoNewOrder(AssignInfo info, int oldOrder, int newOrder){

        if(newOrder == 0 || oldOrder == newOrder){
            info.NewOrder = newOrder;
        } else {
            boolean isDown = false;
            if(oldOrder == 0) {
                isDown = true;

                //เมื่อ oldOrder = 0 ให้กำหนด oldOrder ใหม่โดยหาจากลำดับที่ไม่ถูกใช้งาน และจะต้องมากกว่าหรือเท่า newOrder โดยเอาลำดับแรกสุด
                List<Integer> listCompare = getOrderNotUsed();
                for(Integer i : listCompare) {
                    if(i >= newOrder) {
                        oldOrder = i;
                        break;
                    }
                }

            } else {
                if(newOrder < oldOrder) {
                    isDown = true;
                }
            }

            for (AssignInfo i : mTempAssignInfoList) {
                if (i.AssignID.equals(info.AssignID)) {
                    i.NewOrder = newOrder;
                } else {
                    if(isDown) { //เลื่อนลง
                        if((i.NewOrder >= newOrder && i.NewOrder <= oldOrder) || (oldOrder == 0 && i.NewOrder >= newOrder)) { //test old 10, new 6 || old 0, new 6
                            i.NewOrder += 1;
                        }
                    } else { //เลื่อนขึ้น
                        if(i.NewOrder >= oldOrder && i.NewOrder <= newOrder){//test old 6, new 10
                            i.NewOrder -= 1;
                        }
                    }
                }
            }
        }

        orderAdapter.notifyDataSetChanged();
        lvOrderList.requestFocus();
    }


    private List<Integer> getOrderNotUsed(){
        List<Integer> list = new ArrayList<>();
        List<Integer> listCompare = new ArrayList<>();

        Integer i = 1;
        for (AssignInfo info : mTempAssignInfoList) {
            if (info.NewOrder != 0) {
                list.add(info.NewOrder);
            }
            listCompare.add(i);
            i++;
        }

        listCompare.removeAll(list);

        return  listCompare;
    }
}
