package th.co.thiensurat.fragments.credit.SortOrderDefault;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.info.AssignInfo;

public class NewSortOrderDefaultMainFragment extends BHFragment {

    public enum SortType {
        Audit, Credit
    }

    public static class Data extends BHParcelable {
        public SortType sortType;
    }

    private Data data;

    @InjectView
    private TextView txtViewCount, header_CONTNO_No, header_Name, header_Order;
    @InjectView
    private ScrollView svOrder;
    @InjectView
    private EditText txtSearch;
    @InjectView
    private Button btnSearch;

    private String mTempSearch;
    private boolean Is_CONTNO_No_DESC, Is_Name_DESC, Is_Order_DESC;

    private List<AssignInfo> mAssignInfoList;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_new_sort_order_default_main;
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

                txtSearch.setText("");
                mTempSearch = "";
                Is_CONTNO_No_DESC = false;
                Is_Name_DESC = false;
                Is_Order_DESC = true;
            }

            @Override
            protected void calling() {
                assignInfoList = getQuery();

                if (assignInfoList.size() > 0) {
                    /*int i = 1;
                    for (AssignInfo info : assignInfoList) {
                        info.NewOrder = i;
                        info.IsDuplicateValues = false;
                        i++;
                    }*/

                    mAssignInfoList.addAll(assignInfoList);
                }
            }

            @Override
            protected void after() {
                if (assignInfoList.size() > 0) {

                    txtViewCount.setText(getTextView(assignInfoList.size()));

                    buildHeader();
                    buildView(false);
                }

                btnSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mTempSearch = txtSearch.getText().toString();
                        buildView(true);
                    }
                });

                activity.menu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
                    @Override
                    public void onOpened() {
                        if (mAssignInfoList != null) {
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
        }).start(true, true);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_save:
                new BackgroundProcess(activity) {

                    String title;
                    String message;
                    String messageOrderNotUsed;
                    boolean isSave;
                    boolean isNoOrder;

                    List<AssignInfo> errorList;

                    @Override
                    protected void before() {
                        title = "";
                        message = "";
                        messageOrderNotUsed = "";
                        isSave = true;
                        isNoOrder = true;

                        errorList = new ArrayList<AssignInfo>();
                    }

                    @Override
                    protected void calling() {

                        if (isNoOrder) {
                            //ตรวจสอบ Order ที่ไม่ใส่ลำดับ
                            for (AssignInfo info : mAssignInfoList) {
                                if (info.NewOrder == 0) {
                                    //title = "แจ้งเตือน";
                                    //message = "ตรวจพบข้อมูลที่ไม่ถูกใส่ลำดับ กดตกลงเพื่อโชว์ข้อมูล";
                                    isNoOrder = false;

                                    errorList.add(info);
                                    //break;
                                }
                            }
                        }

                        if (isSave) {
                            //ตรวจสอบ Order ที่เกินลำดับ
                            int maxOrder = mAssignInfoList.size();
                            for (AssignInfo info : mAssignInfoList) {
                                if (info.NewOrder > maxOrder) {
                                    title = "แจ้งเตือน";
                                    message = "ตรวจพบข้อมูลที่เกินลำดับ กดตกลงเพื่อโชว์ข้อมูล";
                                    isSave = false;

                                    errorList.add(info);
                                    //break;
                                }
                            }
                        }

                        if (isSave) {
                            //ตรวจสอบค่าซ้ำ
                            for (AssignInfo info : mAssignInfoList) {
                                if(info.NewOrder != 0) {
                                    if (checkDuplicateValuesByOrder(info)) {
                                        title = "แจ้งเตือน";
                                        message = "ตรวจพบข้อมูลที่ใส่ลำดับซ้ำกัน กดตกลงเพื่อโชว์ข้อมูล";
                                        isSave = false;

                                        errorList.add(info);
                                        //break;
                                    }
                                }
                            }
                        }

                        if (!isSave || !isNoOrder) {
                            List<Integer> list = new ArrayList<>();
                            List<Integer> listCompare = new ArrayList<>();

                            Integer i = 1;
                            for (AssignInfo info : mAssignInfoList) {
                                if (info.NewOrder != 0) {
                                    list.add(info.NewOrder);
                                }
                                listCompare.add(i);
                                i++;
                            }

                            Collections.sort(list, new Comparator<Integer>() {
                                @Override
                                public int compare(Integer a1, Integer a2) {
                                    return String.valueOf(String.format("%5d", (a1))).compareTo(String.valueOf(String.format("%5d", (a2))));
                                }
                            });

                            for (Integer l : list) {
                                listCompare.remove(l);
                            }

                            messageOrderNotUsed += "\n\nลำดับที่ไม่ถูกใช้งาน : ";

                            for (int lc = 0; lc < listCompare.size(); lc++) {
                                messageOrderNotUsed += String.format("%d%s", listCompare.get(lc), lc != (listCompare.size() - 1) ? ", " : "");
                            }
                        }
                    }

                    @Override
                    protected void after() {

                        if (isSave) {
                            List<AssignInfo> infoList = new ArrayList<>(getOrderIsChanging());

                            if (infoList.size() == 0) {
                                showWarningDialog("แจ้งเตือน", "ไม่พบการเปลี่ยนลำดับของข้อมูล");
                            } else {
                                if(isNoOrder){
                                    showDialogBoxConfirm("แจ้งเตือน", "ยืนยันการบันทึกข้อมูล", infoList);
                                } else {
                                    showDialogBoxConfirm("แจ้งเตือน", "ตรวจพบข้อมูลที่ไม่ถูกใส่ลำดับ กดตกลงเพื่อโชว์ข้อมูล", infoList, errorList);
                                }
                            }
                        } else {
                            message += messageOrderNotUsed;
                            showDialogBoxWithMove(title, message, errorList);
                        }
                    }
                }.start();

                break;
        }
    }

    private List<AssignInfo> getQuery() {
        List<AssignInfo> info = new AssignController().getAssignSortOrderDefaultByAssigneeEmpID(
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

    private void buildHeader() {
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

                Is_CONTNO_No_DESC = !Is_CONTNO_No_DESC;
                buildView(true);
            }
        });

        //ชื่อลูกค้า
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

                Is_Name_DESC = !Is_Name_DESC;
                buildView(true);
            }
        });

        //ลำดับ
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

                Is_Order_DESC = !Is_Order_DESC;
                buildView(true);
            }
        });
    }

    private LinearLayout getLinearLayout(final AssignInfo info) {
        LayoutInflater inflater = activity.getLayoutInflater();
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.list_new_sort_order_default, null);

        TextView txtCONTNO_No = (TextView) ll.findViewById(R.id.txtCONTNO_No);
        txtCONTNO_No.setText(info.CONTNO);

        TextView txtName = (TextView) ll.findViewById(R.id.txtName);
        txtName.setText(info.CustomerFullName);

        final TextView txtOrder = (TextView) ll.findViewById(R.id.txtOrder);
        txtOrder.setText(info.NewOrder == 0 ? "" : String.valueOf(info.NewOrder));

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ArrayAdapter<String> arrayAdapter = getOrderNotUsed();
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                builder.setTitle("เลือกลำดับ");
                //builder.setMessage("");
                builder.setCancelable(false);
                builder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String str = which == 0 ? "" : arrayAdapter.getItem(which);
                        info.NewOrder = str.toString().equals("") ? 0 : Integer.parseInt(str.toString());
                        txtOrder.setText(str);
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
        });

        /*TextView txtAssignID = (TextView) ll.findViewById(R.id.txtAssignID);
        txtAssignID.setText(info.AssignID);*/

        /*final EditText etOrder = (EditText) ll.findViewById(R.id.etOrder);
        etOrder.setText(String.valueOf(info.NewOrder == 0 ? "" : info.NewOrder));
        etOrder.setHint(String.format("%d - %d", 1, mAssignInfoList.size()));
        etOrder.setHintTextColor(Color.GRAY);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etOrder.requestFocus();

                *//*int pos = etOrder.getText().length();
                etOrder.setSelection(pos);*//*

                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etOrder, InputMethodManager.SHOW_IMPLICIT);
            }
        };

        txtCONTNO_No.setOnClickListener(onClickListener);
        txtName.setOnClickListener(onClickListener);

        etOrder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == KeyEvent.KEYCODE_CALL || actionId == KeyEvent.KEYCODE_ENDCALL)) {
                    final EditText nextField = (EditText) etOrder.focusSearch(View.FOCUS_DOWN);

                    if (nextField != null) {
                        nextField.requestFocus();

                        *//*nextField.post(new Runnable() {
                            @Override
                            public void run() {
                                int pos = nextField.getText().length();
                                nextField.setSelection(pos);
                            }
                        });*//*


                        *//*InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(nextField, InputMethodManager.SHOW_IMPLICIT);    *//*
                    }
                }
                return false;
            }
        });

        etOrder.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    EditText et = (EditText) v;
                    int pos = et.getText().toString().length();
                    etOrder.setSelection(pos);
                }
            }
        });

        etOrder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("0")) {
                    etOrder.setText("");
                }

                info.NewOrder = s.toString().equals("") ? 0 : Integer.parseInt(s.toString());
            }
        });*/

        return ll;
    }

    private LinearLayout getAll(String search) {
        LinearLayout linearLayout = new LinearLayout(activity.getApplicationContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        //String strSearch = txtSearch.getText().toString();
        String strPattern = String.format("(?i).*%s.*", search);

        for (final AssignInfo info : mAssignInfoList) {
            if (!search.toString().equals("")) {
                if (info.CONTNO.matches(strPattern) || info.CustomerFullName.matches(strPattern)) {
                    linearLayout.addView(getLinearLayout(info));
                }
            } else {
                linearLayout.addView(getLinearLayout(info));
            }
        }

        return linearLayout;
    }

    private LinearLayout getError(List<AssignInfo> errorList) {
        LinearLayout linearLayout = new LinearLayout(activity.getApplicationContext());
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        for (final AssignInfo info : mAssignInfoList) {
            if(errorList.contains(info)){
                linearLayout.addView(getLinearLayout(info));
            }
        }

        return linearLayout;
    }

    private void buildView(boolean enableLoading) {
        new BackgroundProcess(activity) {
            LinearLayout ll;

            @Override
            protected void before() {
                //svOrder.removeAllViews();
            }

            @Override
            protected void calling() {
                ll = getAll(mTempSearch);
            }

            @Override
            protected void after() {
                svOrder.removeAllViews();
                svOrder.addView(ll);
            }
        }.start(enableLoading);
    }

    private void buildViewError(final List<AssignInfo> errorList) {
        new BackgroundProcess(activity) {
            LinearLayout ll;

            @Override
            protected void before() {
                //svOrder.removeAllViews();
            }

            @Override
            protected void calling() {
                ll = getError(errorList);
            }

            @Override
            protected void after() {
                svOrder.removeAllViews();
                svOrder.addView(ll);
            }
        }.start();
    }

    private List<AssignInfo> getOrderIsChanging() {
        //ตรวจสอบ Order ที่มีการเปลี่ยนแปลง
        List<AssignInfo> newList = new ArrayList<>();
        for (AssignInfo info : mAssignInfoList) {
            if ((info.NewOrder != info.Order) || (info.NewOrder != info.OrderExpect)) {
                newList.add(info);
            }
        }
        return newList;
    }

    private boolean checkDuplicateValuesByOrder(AssignInfo assignInfo) {
        for (AssignInfo info : mAssignInfoList) {
            if (!info.AssignID.equals(assignInfo.AssignID)) {
                if (info.NewOrder == assignInfo.NewOrder) {
                    return true;
                }
            }
        }
        return false;
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
                buildViewError(errorList);
                dialog.dismiss();
            }
        });
        builder.show();
    }


    private void saveAssign(final List<AssignInfo> infoList) {
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

                txtSearch.setText("");
                mTempSearch = "";
                Is_CONTNO_No_DESC = false;
                Is_Name_DESC = false;
                Is_Order_DESC = true;
            }

            @Override
            protected void calling() {
                lastUpdateDate = new Date();
                lastUpdateBy = BHPreference.employeeID();

                for (AssignInfo info : infoList) {
                    info.LastUpdateDate = lastUpdateDate;
                    info.LastUpdateBy = lastUpdateBy;
                    TSRController.updateAssignForSortOrderDefault(info, true);
                }


                assignInfoList = getQuery();

                if (assignInfoList.size() > 0) {
                    /*int i = 1;
                    for (AssignInfo info : assignInfoList) {
                        info.NewOrder = i;
                        info.IsDuplicateValues = false;
                        i++;
                    }*/
                    mAssignInfoList.addAll(assignInfoList);
                }
            }

            @Override
            protected void after() {
                if (assignInfoList.size() > 0) {
                    txtViewCount.setText(getTextView(assignInfoList.size()));
                    buildView(false);
                }
            }


        }.start(true, true);
    }

    private void showDialogBoxWithMove(String title, String message, final List<AssignInfo> errorList) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buildViewError(errorList);
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

    private  ArrayAdapter<String> getOrderNotUsed(){
        List<Integer> list = new ArrayList<>();
        List<Integer> listCompare = new ArrayList<>();

        Integer i = 1;
        for (AssignInfo info : mAssignInfoList) {
            if (info.NewOrder != 0) {
                list.add(info.NewOrder);
            }
            listCompare.add(i);
            i++;
        }

        listCompare.removeAll(list);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.select_dialog_item);
        arrayAdapter.add("ไม่ระบุลำดับ");
        for (Integer l : listCompare) {
            arrayAdapter.add(String.valueOf(l));
        }

        return arrayAdapter;
    }


}
