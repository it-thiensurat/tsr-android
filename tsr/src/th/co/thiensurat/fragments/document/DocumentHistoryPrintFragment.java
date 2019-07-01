package th.co.thiensurat.fragments.document;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHBarcode;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.PrinterController;
import th.co.thiensurat.data.controller.DocumentHistoryController.DocumentType;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.EmployeeInfo;

public class DocumentHistoryPrintFragment extends BHFragment {

    @InjectView private TextView txtDate, txtCustomer, txtTotalCount, txtBarcode;
    @InjectView private LinearLayout linearLayoutDetail;
    @InjectView private ImageView ivBarcode;

    /*@InjectView private ListView lvContract, lvReceipt, lvChangeProduct, lvImpoundProduct, lvChangeContract, lvManualDocument, lvPayInSlipBank, lvPayInSlipPayPoint;*/


    private int sumTotalCount = 0;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_document_print;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_document_print;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back, R.string.button_print};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        bindData();
    }

    /*protected void setHeader(final ListView listview, final String DocumentType, final String ItemCount) {
        // TODO Auto-generated method stub
        try {
            LayoutInflater inflater = activity.getLayoutInflater();
            ViewGroup header = (ViewGroup) inflater
                    .inflate(R.layout.list_document_print_list_header,
                            listview, false);

            TextView txtDocumentTypeName = (TextView) header
                    .findViewById(R.id.txtDocumentTypeName);
            TextView txtCount = (TextView) header.findViewById(R.id.txtCount);

            if (DocumentType.equals("0"))
                txtDocumentTypeName.setText("เอกสารใบสัญญา");
            else if (DocumentType.equals("1"))
                txtDocumentTypeName.setText("เอกสารใบเสร็จ");
            else if (DocumentType.equals("2"))
                txtDocumentTypeName.setText("เอกสารใบเปลี่ยนเครื่อง");
            else if (DocumentType.equals("3"))
                txtDocumentTypeName.setText("เอกสารใบถอดเครื่อง");
            else if (DocumentType.equals("4"))
                txtDocumentTypeName.setText("เอกสารใบเปลี่ยนสัญญา");
            else if (DocumentType.equals("5"))
                txtDocumentTypeName.setText("เอกสารมือ");
            else if (DocumentType.equals("6"))
                txtDocumentTypeName.setText("เอกสาร Slip ธนาคาร");
            else if (DocumentType.equals("7"))
                txtDocumentTypeName.setText("เอกสาร Slip เพย์พอยท์");
//			 else
//			 txtDocumentName.setText("");

//			txtDocumentTypeName.setText("เลขที่ใบสัญญา");
//			txtCount.setText(Integer.toString(output.size()) + " ใบ");
//			lvContract.addHeaderView(header, null, false);

            txtCount.setText(ItemCount + " ใบ");
            listview.addHeaderView(header, null, false);

            sumTotalCount += Integer.parseInt(ItemCount);

        } catch (Exception e) {
            // TODO: handle exception
            // Log.e("error ------ >>>", e.getMessage());
            e.printStackTrace();
        }
    }*/

    private void bindData() {
        (new BackgroundProcess(activity) {

            String OrganizationCode = BHPreference.organizationCode();
            EmployeeInfo outputEmp;

            List<DocumentHistoryInfo> outputContract;
            List<DocumentHistoryInfo> outputReceipt;
            List<DocumentHistoryInfo> outputChangeProduct;
            List<DocumentHistoryInfo> outputImpoundProduct;
            List<DocumentHistoryInfo> outputChangeContract;
            //List<DocumentHistoryInfo> outputSendMoney;
            List<DocumentHistoryInfo> outputManualDocument;
            List<DocumentHistoryInfo> outputPayInSlipBank;
            List<DocumentHistoryInfo> outputPayInSlipPayPoint;


            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                String[] positionCode = BHPreference.PositionCode().split(",");
                outputEmp = getEmployeeByID(BHPreference.employeeID());
                if (Arrays.asList(positionCode).contains("SaleLeader")) {
                    outputContract = getDocumentHistoryGroupByType(OrganizationCode, DocumentType.Contract.toString());
                    outputReceipt = getDocumentHistoryGroupByType(OrganizationCode, DocumentType.Receipt.toString());
                    outputChangeProduct = getDocumentHistoryGroupByType(OrganizationCode, DocumentType.ChangeProduct.toString());
                    outputImpoundProduct = getDocumentHistoryGroupByType(OrganizationCode, DocumentType.ImpoundProduct.toString());
                    outputChangeContract = getDocumentHistoryGroupByType(OrganizationCode, DocumentType.ChangeContract.toString());
//				outputSendMoney = getDocumentHistoryGroupByType(OrganizationCode, DocumentType.SendMoney.toString());
                    outputManualDocument = getDocumentHistoryGroupByType(OrganizationCode, DocumentType.ManualDocument.toString());
                    outputPayInSlipBank = getDocumentHistoryGroupByType(OrganizationCode, DocumentType.PayInSlipBank.toString());
                    outputPayInSlipPayPoint = getDocumentHistoryGroupByType(OrganizationCode, DocumentType.PayInSlipPayPoint.toString());
                } else {
                    outputContract = getDocumentHistoryGroupByTypeAndEmployeeCode(OrganizationCode, DocumentType.Contract.toString(), BHPreference.employeeID());
                    outputReceipt = getDocumentHistoryGroupByTypeAndEmployeeCode(OrganizationCode, DocumentType.Receipt.toString(), BHPreference.employeeID());
                    outputChangeProduct = getDocumentHistoryGroupByTypeAndEmployeeCode(OrganizationCode, DocumentType.ChangeProduct.toString(), BHPreference.employeeID());
                    outputImpoundProduct = getDocumentHistoryGroupByTypeAndEmployeeCode(OrganizationCode, DocumentType.ImpoundProduct.toString(), BHPreference.employeeID());
                    outputChangeContract = getDocumentHistoryGroupByTypeAndEmployeeCode(OrganizationCode, DocumentType.ChangeContract.toString(), BHPreference.employeeID());
                    outputManualDocument = getDocumentHistoryGroupByTypeAndEmployeeCode(OrganizationCode, DocumentType.ManualDocument.toString(), BHPreference.employeeID());
                    outputPayInSlipBank = getDocumentHistoryGroupByTypeAndEmployeeCode(OrganizationCode, DocumentType.PayInSlipBank.toString(), BHPreference.employeeID());
                    outputPayInSlipPayPoint = getDocumentHistoryGroupByTypeAndEmployeeCode(OrganizationCode, DocumentType.PayInSlipPayPoint.toString(), BHPreference.employeeID());
                }

            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                super.after();

                if (outputContract != null) {
                    sumTotalCount +=  outputContract.size();
                    createHeadView("เอกสารใบสัญญา", outputContract.size());
                    for (int i = 0; i < outputContract.size(); i++) {
                        createDetailView(i + 1, outputContract.get(i).DocumentNo, outputContract.get(i).CustomerFullName);
                    }
                    //setListViewByType(lvContract, DocumentType.Contract.toString(), outputContract);
                }
                if (outputReceipt != null) {
                    sumTotalCount +=  outputReceipt.size();
                    createHeadView("เอกสารใบเสร็จ", outputReceipt.size());
                    for (int i = 0; i < outputReceipt.size(); i++) {
                        createDetailView(i + 1, outputReceipt.get(i).DocumentNo, outputReceipt.get(i).CustomerFullName);
                    }
                    //setListViewByType(lvReceipt, DocumentType.Receipt.toString(), outputReceipt);
                }
                if (outputChangeProduct != null) {
                    sumTotalCount +=  outputChangeProduct.size();
                    createHeadView("เอกสารใบเปลี่ยนเครื่อง", outputChangeProduct.size());
                    for (int i = 0; i < outputChangeProduct.size(); i++) {
                        createDetailView(i + 1, outputChangeProduct.get(i).DocumentNo, outputChangeProduct.get(i).CustomerFullName);
                    }
                    //setListViewByType(lvChangeProduct, DocumentType.ChangeProduct.toString(), outputChangeProduct);
                }
                if (outputImpoundProduct != null) {
                    sumTotalCount +=  outputImpoundProduct.size();
                    createHeadView("เอกสารใบถอดเครื่อง", outputImpoundProduct.size());
                    for (int i = 0; i < outputImpoundProduct.size(); i++) {
                        createDetailView(i + 1, outputImpoundProduct.get(i).DocumentNo, outputImpoundProduct.get(i).CustomerFullName);
                    }
                    //setListViewByType(lvImpoundProduct, DocumentType.ImpoundProduct.toString(), outputImpoundProduct);
                }
                if (outputChangeContract != null){
                    sumTotalCount +=  outputChangeContract.size();
                    createHeadView("เอกสารใบเปลี่ยนสัญญา", outputChangeContract.size());
                    for (int i = 0; i < outputChangeContract.size(); i++) {
                        createDetailView(i + 1, outputChangeContract.get(i).DocumentNo, outputChangeContract.get(i).CustomerFullName);
                    }
                    //setListViewByType(lvChangeContract, DocumentType.ChangeContract.toString(), outputChangeContract);
                }
//				if (outputSendMoney != null)
//					setListViewByType(lvSendMoney, DocumentType.SendMoney.toString(), outputSendMoney);
                if (outputManualDocument != null) {
                    sumTotalCount +=  outputManualDocument.size();
                    createHeadView("เอกสารมือ", outputManualDocument.size());
                    for (int i = 0; i < outputManualDocument.size(); i++) {

                        if (outputManualDocument.get(i).ManualDocumentTypeID.equals("0")) {
                            createDetailView(i + 1, outputManualDocument.get(i).con0CONTNO, outputManualDocument.get(i).dc0CustomerFullName);
                        } else if (outputManualDocument.get(i).ManualDocumentTypeID.equals("1")) {
                            createDetailView(i + 1, outputManualDocument.get(i).re1ReceiptCode, outputManualDocument.get(i).dc1CustomerFullName);
                        }
                    }
                    //setListViewByType(lvManualDocument, DocumentType.ManualDocument.toString(), outputManualDocument);
                }
                if (outputPayInSlipBank != null) {
                    sumTotalCount +=  outputPayInSlipBank.size();
                    createHeadView("เอกสาร Slip ธนาคาร", outputPayInSlipBank.size());
                    for (int i = 0; i < outputPayInSlipBank.size(); i++) {
                        createDetailView(i + 1, outputPayInSlipBank.get(i).TransactionNo, outputPayInSlipBank.get(i).employeeFullName);
                    }
                    //setListViewByType(lvPayInSlipBank, DocumentType.PayInSlipBank.toString(), outputPayInSlipBank);
                }
                if (outputPayInSlipPayPoint != null) {
                    sumTotalCount +=  outputPayInSlipPayPoint.size();
                    createHeadView("เอกสาร Slip เพย์พอยท์", outputPayInSlipPayPoint.size());
                    for (int i = 0; i < outputPayInSlipPayPoint.size(); i++) {
                        createDetailView(i + 1, outputPayInSlipPayPoint.get(i).TransactionNo, outputPayInSlipPayPoint.get(i).employeeFullName);
                    }
                    //setListViewByType(lvPayInSlipPayPoint, DocumentType.PayInSlipPayPoint.toString(), outputPayInSlipPayPoint);
                }


                txtDate.setText(BHUtilities.dateFormat(new Date())); //รายการส่งเอกสาร ประจำวันที่
                txtCustomer.setText(outputEmp.FullName()); //พนักงาน
                txtTotalCount.setText(Integer.toString(sumTotalCount) + " ใบ"); //จำนวนเอกสารทั้งหมด

                if (outputContract != null || outputReceipt != null || outputChangeProduct != null || outputImpoundProduct != null || outputChangeContract != null || outputManualDocument != null
                        || outputPayInSlipBank != null || outputPayInSlipPayPoint != null) {

                    //txtDate.setText(new SimpleDateFormat("dd/MM/yy").format(new Date()));

                    //if (outputEmp != null)
                    //txtCustomer.setText(outputEmp.FullName());

                    //String strBarcodeNo = "PAK2301201410300001";

                    /*** [START] :: Fixed-[BHPROJ-0024-3215] :: [LINE-25/07/2016][Android-ระบบส่งเอกสาร] กรณีที่ รหัสทีม/รหัสหน่วย/SaleCode ยาวเกินไป ทำให้ Barcode ที่ใช้ส่งเอกสารมีความยาวเกิน 23 ตัวอักษร เครื่องพิมพ์จะไม่สามารถพิมพ์ Barcode ได้ ***/
//                    String strBarcodeNo = BHPreference.teamCode() + "|" + BHPreference.SubTeamCode() + "|" + BHPreference.saleCode();
                    String strBarcodeNo = BHPreference.SubTeamCode() + "|" + BHPreference.saleCode();
                    /*** [END] :: Fixed-[BHPROJ-0024-3215] :: [LINE-25/07/2016][Android-ระบบส่งเอกสาร] กรณีที่ รหัสทีม/รหัสหน่วย/SaleCode ยาวเกินไป ทำให้ Barcode ที่ใช้ส่งเอกสารมีความยาวเกิน 23 ตัวอักษร เครื่องพิมพ์จะไม่สามารถพิมพ์ Barcode ได้ ***/
                    generateBarcode(strBarcodeNo);
                    txtBarcode.setText(strBarcodeNo);
                } else {
                    txtBarcode.setText("");
                    AlertDialog.Builder setupAlert;
                    setupAlert = new AlertDialog.Builder(activity)
                            .setTitle("แจ้งเตือน ระบบส่งเอกสาร")
                            .setMessage("ไม่พบรายการส่งเอกสาร")
                            .setCancelable(false)
                            .setNegativeButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    showLastView();
                                }
                            });
                    setupAlert.show();
                }

            }

        }).start();
    }

    /*private void setListViewByType(ListView listView, String DocumentType, List<DocumentHistoryInfo> output) {
        BHArrayAdapter<DocumentHistoryInfo> adapter = new BHArrayAdapter<DocumentHistoryInfo>(
                activity, R.layout.list_document_print_list_item,
                output) {

            class ViewHolder {
                public TextView txtNo, txtDocumentNo, txtCustomer;
            }

            @Override
            protected void onViewItem(int position, View view,
                                      Object holder, DocumentHistoryInfo info) {
                // TODO Auto-generated method stub
                try {
                    ViewHolder vh = (ViewHolder) holder;
                    // vh.txtDocumentTypeName.setText("ใบสัญญา");
                    // vh.txtCount.setText(Integer.toString(output.size())
                    // + " ใบ");
                    //vh.txtNo.setText(info.No);
                    //vh.txtDocumentNo.setText(info.DocumentNo);
                    //vh.txtCustomer.setText(info.CustomerName);

                    *//*if (info.DocumentType.equals("0") || info.DocumentType.equals("1")) {
                        vh.txtNo.setText(info.No);
                        vh.txtDocumentNo.setText(info.DocumentNo);
                        vh.txtCustomer.setText(info.CustomerFullName);
                    } else if (info.DocumentType.equals("5")) {
                        vh.txtNo.setText(info.No);
                        if (info.ManualDocumentTypeID.equals("0")) {
                            vh.txtDocumentNo.setText(info.con0CONTNO);
                            vh.txtCustomer.setText(info.dc0CustomerFullName);
                        } else if (info.ManualDocumentTypeID.equals("1")) {
                            vh.txtDocumentNo.setText(info.re1ReceiptCode);
                            vh.txtCustomer.setText(info.dc1CustomerFullName);
                        }
                    }*//*

                    if (info.DocumentType.equals("5")) {
                        vh.txtNo.setText(info.No);
                        if (info.ManualDocumentTypeID.equals("0")) {
                            vh.txtDocumentNo.setText(info.con0CONTNO);
                            vh.txtCustomer.setText(info.dc0CustomerFullName);
                        } else if (info.ManualDocumentTypeID.equals("1")) {
                            vh.txtDocumentNo.setText(info.re1ReceiptCode);
                            vh.txtCustomer.setText(info.dc1CustomerFullName);
                        }
                    } else if (info.DocumentType.equals("6") || info.DocumentType.equals("7")) {
                        vh.txtNo.setText(info.No);
                        vh.txtDocumentNo.setText(info.TransactionNo);
                        vh.txtCustomer.setText(info.employeeFullName);
                    } else {
                        vh.txtNo.setText(info.No);
                        vh.txtDocumentNo.setText(info.DocumentNo);
                        vh.txtCustomer.setText(info.CustomerFullName);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        };

        setHeader(listView, DocumentType, Integer.toString(output.size()));
        listView.setAdapter(adapter);


        //txtTotalCount.setText(Integer.toString(sumTotalCount) + " ใบ");

        setListViewHeightBasedOnChildren(listView);

    }*/


    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        super.onProcessButtonClicked(buttonID);

        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_print:
                new PrinterController(activity).printDocumentHistory();
                //PrinterController.printDocumentHistory();
                break;
            default:
                break;
        }

    }

    /*private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }*/

    private void generateBarcode(String accountCode1) {
        Bitmap bitmap = BHBarcode.generateCode128(accountCode1, 850, 300);
        ivBarcode.setImageBitmap(bitmap);
    }

    private void createHeadView(String documentTypeName, int count) {

        /**<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:orientation="vertical" >

         <LinearLayout
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:orientation="horizontal" >
         <TextView
         style="@style/TextView.Label.bold"
         android:id="@+id/txtDocumentTypeName"
         android:layout_width="0dp"
         android:layout_height="wrap_content"
         android:layout_weight="0.5"
         android:layout_marginTop="5dp"
         android:layout_gravity="center_vertical"
         android:gravity="left"
         android:text="ชื่อเอกสาร" />

         <TextView
         style="@style/TextView.Label.bold"
         android:id="@+id/txtCount"
         android:layout_width="0dp"
         android:layout_height="wrap_content"
         android:layout_weight="0.5"
         android:layout_marginTop="5dp"
         android:layout_gravity="center_vertical"
         android:gravity="right"
         android:text="จำนวนใบ"
         android:paddingRight="20dp"/>
         </LinearLayout>

         <LinearLayout
         android:layout_width="match_parent"
         android:layout_height="1dp"
         android:orientation="horizontal">

         <TextView
         android:id="@+id/txtDocumentTypeName"
         android:layout_width="0dp"
         android:layout_height="wrap_content"
         android:layout_weight="1"
         android:paddingLeft="20dp"
         android:paddingRight="20dp"
         android:background="@color/bg_sub_header"/>

         </LinearLayout>

         </LinearLayout>
         **/

        LinearLayout linearLayoutHead = new LinearLayout(this.getActivity());
        linearLayoutHead.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayoutHead.setOrientation(LinearLayout.VERTICAL);


        //++linearLayoutHeadDetail
        LinearLayout linearLayoutHeadDetail = new LinearLayout(this.getActivity());
        linearLayoutHeadDetail.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayoutHeadDetail.setOrientation(LinearLayout.HORIZONTAL);

        //ชื่อเอกสาร
        LinearLayout.LayoutParams paramsForTextViewDocumentTypeName = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f);
        paramsForTextViewDocumentTypeName.setMargins(0, 5, 0, 0);

        TextView textViewDocumentTypeName = new TextView(this.getActivity());
        textViewDocumentTypeName.setLayoutParams(paramsForTextViewDocumentTypeName);
        textViewDocumentTypeName.setGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
        textViewDocumentTypeName.setTextAppearance(this.getActivity(), R.style.TextView_Label_bold);
        textViewDocumentTypeName.setText(documentTypeName);

        //จำนวนใบ
        TextView textViewCount = new TextView(this.getActivity());
        textViewCount.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        textViewCount.setPadding(0, 0, 20, 0);
        textViewCount.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
        textViewCount.setTextAppearance(this.getActivity(), R.style.TextView_Label_bold);
        textViewCount.setText(String.format("%d ใบ", count));

        linearLayoutHeadDetail.addView(textViewDocumentTypeName);
        linearLayoutHeadDetail.addView(textViewCount);
        //--linearLayoutHeadDetail


        //++linearLayoutHeadLine
        LinearLayout linearLayoutHeadLine = new LinearLayout(this.getActivity());
        linearLayoutHeadLine.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
        linearLayoutHeadLine.setOrientation(LinearLayout.HORIZONTAL);

        //เส้น
        TextView textViewLine = new TextView(this.getActivity());
        textViewLine.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
        textViewLine.setBackgroundResource(R.color.bg_sub_header);

        linearLayoutHeadLine.addView(textViewLine);
        //--linearLayoutHeadLine

        linearLayoutHead.addView(linearLayoutHeadDetail);
        linearLayoutHead.addView(linearLayoutHeadLine);

        linearLayoutDetail.addView(linearLayoutHead);


    }

    private void createDetailView(int no, String documentNo, String customer) {

        /**
         <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
         android:layout_width="match_parent"
         android:layout_height="wrap_content"
         android:orientation="horizontal"
         android:padding="5dp">

         <TextView
         android:id="@+id/txtNo"
         style="@style/TextView.Value"
         android:layout_width="0dp"
         android:layout_height="wrap_content"
         android:layout_weight="0.1"
         android:layout_gravity="top"
         android:text="No"
         android:gravity="center" />

         <TextView
         android:id="@+id/txtDocumentNo"
         style="@style/TextView.Value"
         android:layout_width="0dp"
         android:layout_height="wrap_content"
         android:layout_weight="0.5"
         android:layout_gravity="top"
         android:text="เลขที่"
         android:gravity="left" />

         <TextView
         android:id="@+id/txtCustomer"
         style="@style/TextView.Value"
         android:layout_width="0dp"
         android:layout_height="wrap_content"
         android:layout_weight="0.4"
         android:layout_gravity="top"
         android:text="ชื่อลูกค้า"
         android:gravity="right"
         android:paddingRight="20dp" />
         </LinearLayout>
         **/

        LinearLayout linearLayoutDetailList = new LinearLayout(this.getActivity());
        linearLayoutDetailList.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        linearLayoutDetailList.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutDetailList.setPadding(0, 5, 0, 5);

        //textNo
        TextView textViewNo = new TextView(this.getActivity());
        textViewNo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.1f));
        textViewNo.setGravity(Gravity.TOP | Gravity.CENTER);
        textViewNo.setTextAppearance(this.getActivity(), R.style.TextView_Value);
        textViewNo.setText(String.valueOf(no));

        //textDocumentNo
        TextView textViewDocumentNo = new TextView(this.getActivity());
        textViewDocumentNo.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.5f));
        textViewDocumentNo.setGravity(Gravity.TOP | Gravity.LEFT);
        textViewDocumentNo.setTextAppearance(this.getActivity(), R.style.TextView_Value);
        textViewDocumentNo.setText(documentNo);

        //textCustomer
        TextView textViewCustomer = new TextView(this.getActivity());
        textViewCustomer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.4f));
        textViewCustomer.setGravity(Gravity.TOP | Gravity.RIGHT);
        textViewCustomer.setTextAppearance(this.getActivity(), R.style.TextView_Value);
        textViewCustomer.setPadding(0, 0, 20, 0);
        textViewCustomer.setText(customer);

        linearLayoutDetailList.addView(textViewNo);
        linearLayoutDetailList.addView(textViewDocumentNo);
        linearLayoutDetailList.addView(textViewCustomer);

        linearLayoutDetail.addView(linearLayoutDetailList);
    }
}
