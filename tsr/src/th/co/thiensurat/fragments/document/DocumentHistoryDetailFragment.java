package th.co.thiensurat.fragments.document;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHPagerFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.BankInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.SendDocumentDetailInfo;
import th.co.thiensurat.data.info.SendDocumentInfo;
import th.co.thiensurat.data.info.SendMoneyInfo;
import th.co.thiensurat.fragments.contracts.change.ChangeContractPrintFragment;
import th.co.thiensurat.fragments.impound.ImpoundProductPrintFragment;
import th.co.thiensurat.fragments.products.change.ChangeProductPrintFragment;
import th.co.thiensurat.fragments.sales.SaleContractPrintFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.sales.SaleReceiptPayment;
import th.co.thiensurat.fragments.sendmoney.SendMoneyPrintFragment;

import static th.co.thiensurat.business.controller.TSRController.addSendDocument;
import static th.co.thiensurat.business.controller.TSRController.addSendDocumentDetail;
import static th.co.thiensurat.business.controller.TSRController.deleteSendDocumentDetail;
import static th.co.thiensurat.business.controller.TSRController.getSendDocumentBySentTeamCodeAndSentSubTeamCode;
import static th.co.thiensurat.business.controller.TSRController.getSendDocumentDetailByPrintHistoryID;
import static th.co.thiensurat.business.controller.TSRController.updateDocumentHistory;
import static th.co.thiensurat.business.controller.TSRController.updateSendDocument;

public class DocumentHistoryDetailFragment extends BHPagerFragment {
    public static final String DOCUMENT_HISTORY_DETAIL = "DocumentHistoryDetailFragment";
    @InjectView
    private ListView lvDocumentList;

    public String documentType;

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_document_detail_list;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        bindData(getDocumentType(), true);
        // log("List view " + lvDocumentList == null ? "a" : "B");
    }

    private String getDocumentType() {
        // DocumentHistoryMainFragment main =
        // (DocumentHistoryMainFragment)parent;
        return documentType;
    }

    protected void setHeader(final String DocumentType) {
        // TODO Auto-generated method stub
        try {
            LayoutInflater inflater = activity.getLayoutInflater();
            ViewGroup header = (ViewGroup) inflater.inflate(
                    R.layout.list_document_detail_list_header, lvDocumentList,
                    false);

            TextView txtDocumentName = (TextView) header
                    .findViewById(R.id.txtDocumentName);

            if (DocumentType.equals("0"))
                txtDocumentName.setText("เลขที่ใบสัญญา");
            else if (DocumentType.equals("1"))
                txtDocumentName.setText("เลขที่ใบเสร็จ");
            else if (DocumentType.equals("2"))
                txtDocumentName.setText("เลขที่ใบเปลี่ยนเครื่อง");
            else if (DocumentType.equals("3"))
                txtDocumentName.setText("เลขที่ใบถอดเครื่อง");
            else if (DocumentType.equals("4"))
                txtDocumentName.setText("เลขที่ใบเปลี่ยนสัญญา");
            else if (DocumentType.equals("5"))
                txtDocumentName.setText("เอกสารมือ");
            else if (DocumentType.equals("6"))
                txtDocumentName.setText("Slip ธนาคาร");
            else if (DocumentType.equals("7"))
                txtDocumentName.setText("Slip เพย์พอยท์");
            // else
            // txtDocumentName.setText("");

            lvDocumentList.addHeaderView(header, null, false);

        } catch (Exception e) {
            // TODO: handle exception
            // Log.e("error ------ >>>", e.getMessage());
            e.printStackTrace();
        }
    }

    private void bindData(final String DocumentType, final Boolean StatusHeader) {

        (new BackgroundProcess(activity) {

            List<DocumentHistoryInfo> output;
            // DocumentHistoryInfo input;
            String OrganizationCode = BHPreference.organizationCode();

            @Override
            protected void before() {

            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                String[] positionCode = BHPreference.PositionCode().split(",");

                if (Arrays.asList(positionCode).contains("SaleLeader")) {
                    output = getDocumentHistoryByType(OrganizationCode, DocumentType);
                } else {
                    output = getDocumentHistoryByTypeAndEmployeeCode(OrganizationCode, DocumentType, BHPreference.employeeID());
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (lvDocumentList != null) {
                    if (output != null) {

                        if (StatusHeader == true) {
                            setHeader(DocumentType);
                        }

                        BHArrayAdapter<DocumentHistoryInfo> adapter = new BHArrayAdapter<DocumentHistoryInfo>(
                                activity,
                                R.layout.list_document_detail_list_item, output) {

                            class ViewHolder {
                                public TextView txtNo;
                                public CheckBox chkSelected;
                                // public TextView txtCustomerName;
                            }

                            @Override
                            protected void onViewItem(int position, View view,
                                                      Object holder, DocumentHistoryInfo info) {
                                // TODO Auto-generated method stub
                                final int currentPosition = position;
                                ViewHolder vh = (ViewHolder) holder;

                                if (info.DocumentType.equals("5")) {
                                    if (info.ManualDocumentTypeID.equals("0")) {
                                        vh.txtNo.setText(info.con0CONTNO + "\n"
                                                + info.dc0CustomerFullName);

                                        if (info.isActive0) {
                                            vh.txtNo.setTextColor(Color.parseColor("#000000"));
                                        } else {
                                            vh.txtNo.setTextColor(Color.parseColor("#FF0000"));
                                        }
                                    } else if (info.ManualDocumentTypeID.equals("1")) {
                                        vh.txtNo.setText(info.re1ReceiptCode + "\n"
                                                + info.dc1CustomerFullName);

                                        if (info.isActive1) {
                                            vh.txtNo.setTextColor(Color.parseColor("#000000"));
                                        } else {
                                            vh.txtNo.setTextColor(Color.parseColor("#FF0000"));
                                        }
                                    }
                                } else if (info.DocumentType.equals("6") || info.DocumentType.equals("7")) {
                                    vh.txtNo.setText(info.TransactionNo + "\n"
                                            + info.employeeFullName);
                                } else {
                                    vh.txtNo.setText(info.DocumentNo + "\n"
                                            + info.CustomerFullName);

                                    if (info.isActive) {
                                        vh.txtNo.setTextColor(Color.parseColor("#000000"));
                                    } else {
                                        vh.txtNo.setTextColor(Color.parseColor("#FF0000"));

                                    }
                                }

                                // vh.txtCustomerName.setText(info.CustomerName);
                                vh.chkSelected.setChecked(info.Selected);

                                vh.chkSelected.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (output.get(currentPosition).Selected){
                                            output.get(currentPosition).Selected = false;
                                        } else {
                                            output.get(currentPosition).Selected = true;
                                        }


                                        if (BHGeneral.DEVELOPER_MODE) {
                                            showMessage("Check : position : " + currentPosition + " Tab " + getDocumentType());
                                        }
                                    }
                                });

                                /*vh.chkSelected
                                        .setOnCheckedChangeListener(new OnCheckedChangeListener() {

                                            @Override
                                            public void onCheckedChanged(
                                                    CompoundButton buttonView,
                                                    boolean isChecked) {
                                                // TODO Auto-generated method
                                                // stub

                                                // Update Info
                                                DocumentHistoryInfo getOutput = (DocumentHistoryInfo) output
                                                        .get(currentPosition);
                                                getOutput.Selected = isChecked;
                                                output.set(currentPosition,
                                                        getOutput);

                                                // buttonView.setChecked(isChecked);

                                                if (BHGeneral.DEVELOPER_MODE) {
                                                    showMessage("Check : position : "
                                                            + currentPosition + " Tab " + getDocumentType());
                                                }
                                            }
                                        });*/
                            }
                        };

                        lvDocumentList.setAdapter(adapter);
                        if (BHGeneral.DEVELOPER_MODE) {
                            showMessage("bineData Tab " + getDocumentType());
                        }

                        lvDocumentList_Click();
                    }
                } else {
                    if (BHGeneral.DEVELOPER_MODE) {
                        showMessage("lvDocumentList  IS  NULL");
                    }

                }

            }
        }).start();

    }

    public void updateData(final Boolean statusSelected) {
        String text = "";
        if (lvDocumentList != null) {
            if (lvDocumentList.getAdapter() != null) {

                for (int i = 1; i < lvDocumentList.getAdapter().getCount(); i++) {

//					final String strPrintHistoryID = ((DocumentHistoryInfo) lvDocumentList.getItemAtPosition(i)).PrintHistoryID;
//					final String strDocumentNumber = ((DocumentHistoryInfo)lvDocumentList.getItemAtPosition(i)).DocumentNumber;
                    final boolean boolSelected = ((DocumentHistoryInfo) lvDocumentList.getItemAtPosition(i)).Selected;

//					 final String strSelected;
//					 if (boolSelected == true) {
//					 strSelected = "1";
//					 } else {
//					 strSelected = "0";
//					 }


					 /*if (statusSelected == true)
                        info.Selected = boolSelected;
					 else
					    info.Deleted = boolSelected;*/

                    final DocumentHistoryInfo info = (DocumentHistoryInfo) lvDocumentList.getItemAtPosition(i);
                    SendDocumentDetailInfo sendDocumentDetailInfo = getSendDocumentDetailByPrintHistoryID(info.PrintHistoryID);
                    SendDocumentInfo sendDocumentInfo = getSendDocumentBySentTeamCodeAndSentSubTeamCode(BHPreference.teamCode(), BHPreference.SubTeamCode());

                    if (boolSelected && sendDocumentDetailInfo == null) {
                        if (sendDocumentInfo != null) {
                            sendDocumentInfo.SumDocument = sendDocumentInfo.SumDocument + 1;
                            updateSendDocument(sendDocumentInfo, true);
                        } else {
                            sendDocumentInfo = new SendDocumentInfo();
                            sendDocumentInfo.SendDocumentID = DatabaseHelper.getUUID();
                            sendDocumentInfo.OrganizationCode = BHPreference.organizationCode();
                            sendDocumentInfo.SumDocument = 1;
                            sendDocumentInfo.SyncedDate = new Date();
                            sendDocumentInfo.SentSubTeamCode = BHPreference.SubTeamCode();
                            sendDocumentInfo.SentTeamCode = BHPreference.teamCode();
                            addSendDocument(sendDocumentInfo, true);
                        }
                        sendDocumentDetailInfo = new SendDocumentDetailInfo();
                        sendDocumentDetailInfo.SendDocumentID = sendDocumentInfo.SendDocumentID;
                        sendDocumentDetailInfo.OrganizationCode = BHPreference.organizationCode();
                        sendDocumentDetailInfo.PrintHistoryID = info.PrintHistoryID;
                        sendDocumentDetailInfo.SyncedDate = new Date();
                        addSendDocumentDetail(sendDocumentDetailInfo, true);

                        info.Selected = boolSelected;
                        info.Status = "Sent";
                        info.SentDate = new Date();
                        info.SentEmpID = BHPreference.employeeID();
                        info.SentSaleCode = BHPreference.saleCode();
                        info.SentSubTeamCode = BHPreference.SubTeamCode();
                        info.SentTeamCode = BHPreference.teamCode();
                        updateDocumentHistory(info, true);
                    } else if (!boolSelected && sendDocumentDetailInfo != null) {
                        if (sendDocumentInfo != null) {
                            if (sendDocumentInfo.SumDocument > 0) {
                                sendDocumentInfo.SumDocument = sendDocumentInfo.SumDocument - 1;
                                updateSendDocument(sendDocumentInfo, true);
                            }
                        }
                        deleteSendDocumentDetail(sendDocumentDetailInfo, true);

                        info.Selected = boolSelected;
                        info.Status = "";
                        info.SentDate = null;
                        info.SentEmpID = "";
                        info.SentSaleCode = "";
                        info.SentSubTeamCode = "";
                        info.SentTeamCode = "";
                        updateDocumentHistory(info, true);
                    }


					/*(new BackgroundProcess(activity) {
                        @Override
						protected void calling() {
							// TODO Auto-generated method stub
//							 updateDocumentHistory(strSelected, strPrintHistoryID, getDocumentType(), statusSelected);

//							DocumentHistoryInfo output = getDocumentHistoryByID(info);

//							if (statusSelected == true)
//								info.Selected = boolSelected;
//							else
//								info.Deleted = boolSelected;
							
							//updateDocumentHistory(info,  true);


						}
                    }).start();*/

                    // text += "Row " + i + " \r\n -- strPrintHistoryID = " +
                    // strPrintHistoryID + " -- boolSelected = " + strSelected +
                    // "\r\n ";
                    text = "บันทึกข้อมูล";
                }
                bindData(getDocumentType(), false);
            } else {
                text = "lvDocumentList.getAdapter()  IS  NULL";
            }
        } else {
            text = "lvDocumentList  IS  NULL";
        }

        if (BHGeneral.DEVELOPER_MODE) {
            showMessage(text + " Tab " + getDocumentType());
        }


        // showMessage("getDocumentType " + getDocumentType() +
        // "\r\n  Row:  \r\n" + text);
    }

    private void lvDocumentList_Click() {
        lvDocumentList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub

                final String strDocumentNumber = ((DocumentHistoryInfo) lvDocumentList.getItemAtPosition(position)).DocumentNumber;
                final boolean boolSelected = ((DocumentHistoryInfo) lvDocumentList.getItemAtPosition(position)).Selected;
                // final Integer intDocumentType = 10;//Skip selected item.//
                // Integer.parseInt(((DocumentHistoryInfo)lvDocumentList.getItemAtPosition(position)).DocumentType);
                final Integer intDocumentType = Integer.parseInt(((DocumentHistoryInfo) lvDocumentList.getItemAtPosition(position)).DocumentType);
                final String strRefNo = ((DocumentHistoryInfo) lvDocumentList.getItemAtPosition(position)).RefNo;

                switch (intDocumentType) {
                    case 0: // ใบสัญญา
                        (new BackgroundProcess(activity) {

                            ContractInfo output;

                            @Override
                            protected void before() {

                            }

                            @Override
                            protected void calling() {
                                // TODO Auto-generated method stub
                                try {
                                    // output =
                                    // getContractByCONTNO(strDocumentNumber);
                                    output = getContractByRefNoForSendDocuments(
                                            BHPreference.organizationCode(),
                                            strRefNo);
                                } catch (Exception e) {
                                    // TODO: handle exception

                                }
                            }

                            @Override
                            protected void after() {
                                // TODO Auto-generated method stub
                                if (output != null) {
                                    BHPreference.setRefNo(output.RefNo);
                                    BHPreference
                                            .setProcessType(ProcessType.SendDocument
                                                    .toString());
                                    // SaleDetailCheckContractFragment fmContract =
                                    // BHFragment.newInstance(SaleDetailCheckContractFragment.class);
                                    showNextView(BHFragment
                                            .newInstance(SaleContractPrintFragment.class));
                                } else {
                                    showMessage("ไม่พบข้อมูล");
                                }
                            }
                        }).start();

                        break;

                    case 1: // ใบเสร็จ
                        (new BackgroundProcess(activity) {

                            @Override
                            protected void calling() {
                                // TODO Auto-generated method stub
                                BHPreference.setProcessType(ProcessType.SendDocument.toString());
                                BHPreference.setRefNo(strRefNo);
                            }

                            @Override
                            protected void after() {
                                // TODO Auto-generated method stub
                                SaleReceiptPayment.Data dataReceiptID = new SaleReceiptPayment.Data();
                                dataReceiptID.ReceiptID = strDocumentNumber;
                                SaleReceiptPayment fmReceipt = BHFragment.newInstance(SaleReceiptPayment.class, dataReceiptID);
                                showNextView(fmReceipt);
                            }
                        }).start();

                        break;

                    case 2: // ใบเปลี่ยนเครื่อง
                        try {
                            ChangeProductPrintFragment.Data dataChangeProduct = new ChangeProductPrintFragment.Data();
                            dataChangeProduct.ChangeProductID = strDocumentNumber;
                            dataChangeProduct.StatusPageHistory = true;
                            ChangeProductPrintFragment frmChangeProduct = BHFragment
                                    .newInstance(ChangeProductPrintFragment.class,
                                            dataChangeProduct);
                            showNextView(frmChangeProduct);

                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                        break;

                    case 3: // ใบถอดเครื่อง
                        try {
                            ImpoundProductPrintFragment.Data dataImpoundProduct = new ImpoundProductPrintFragment.Data();
                            dataImpoundProduct.ImpoundProductID = strDocumentNumber;
                            ImpoundProductPrintFragment frmImpoundProduct = BHFragment
                                    .newInstance(ImpoundProductPrintFragment.class,
                                            dataImpoundProduct);
                            showNextView(frmImpoundProduct);

                        } catch (Exception e) {
                            // TODO: handle exception
                            e.printStackTrace();
                        }
                        break;

                    case 4: // ใบเปลี่ยนสัญญา
                        getChangeContract(strDocumentNumber);

                        break;

                    /*case 5: // ใบนำส่งเงิน
                        getSendMoney(strDocumentNumber);
                        break;*/
                    default:
                        break;
                }

                // showMessage("Even OnItemClick " + "strPrintHistoryID = " +
                // strPrintHistoryID + "------" + "boolSelected " +
                // boolSelected);
            }

        });
    }

    // ใบเปลี่ยนสัญญา
    private void getChangeContract(final String strNewSaleID) {
        (new BackgroundProcess(activity) {

            ContractInfo output = null;
            AddressInfo oldAddressIDCard = null;
            AddressInfo oldAddressInstall = null;
            String strOrganizationCode;
            List<SalePaymentPeriodInfo> newSPPList;

            @Override
            protected void before() {
                // TODO Auto-generated method stub
                strOrganizationCode = BHPreference.organizationCode();
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                try {
                    // output = getChangeContractByID(strOrganizationCode,
                    // strChangeContractID);
                    output = getContractByChangeContractID(strOrganizationCode, strNewSaleID);
                    oldAddressIDCard = getAddress(output.RefNo, AddressType.AddressIDCard);
                    oldAddressInstall = getAddress(output.RefNo, AddressType.AddressInstall);
                    newSPPList = getSalePaymentPeriodByRefNo(output.RefNo);
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }

            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (output != null && oldAddressIDCard != null
                        && oldAddressInstall != null && newSPPList != null) {
                    ChangeContractPrintFragment.Data data = new ChangeContractPrintFragment.Data();
                    data.newAddressIDCard = oldAddressIDCard;
                    data.newAddressInstall = oldAddressInstall;
                    data.newContract = output;
                    data.selectedCauseName = output.ProblemName;
                    data.newSPPList = newSPPList;
                    ChangeContractPrintFragment frmChangeContract = BHFragment
                            .newInstance(ChangeContractPrintFragment.class,
                                    data);
                    showNextView(frmChangeContract);
                }
            }
        }).start();
    }

    /*private void getSendMoney(final String strReference2) {
        (new BackgroundProcess(activity) {
            private SendMoneyInfo output;

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                output = getSendMoneyByID(BHPreference.organizationCode(),
                        strReference2);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                super.after();

                if (output != null) {
                    SendMoneyPrintFragment.Data selectedData = new SendMoneyPrintFragment.Data();

                    selectedData.payeeName = output.PayeeName;
                    selectedData.reference2 = output.Reference2;
                    selectedData.channelCode = output.ChannelCode;
                    selectedData.channelName = output.ChannelName;
                    selectedData.accountCode1 = output.AccountCode1;
                    selectedData.empName = BHPreference.employeeID();
                    selectedData.empCode = BHPreference.employeeID();
                    selectedData.paymentType = output.PaymentType;
                    selectedData.paymentTypeName = output.PaymentTypeName;
                    selectedData.sendDate = output.SendDate;
                    selectedData.sendAmount = output.SendAmount;

                    SendMoneyPrintFragment fm = BHFragment.newInstance(
                            SendMoneyPrintFragment.class, selectedData);
                    showNextView(fm);
                }
            }
        }).start();
    }*/

    public void process(int currentTab) {
        if (BHGeneral.DEVELOPER_MODE) {
            showMessage("Tab:" + currentTab);
        }

        // //bindData(Integer.toString(currentTab), "1", true);
        // // try {
        // // documentType = Integer.toString(currentTab);
        // // } catch (Exception e) {
        // // // TODO: handle exception
        // // Log.e("error ------ >>>", e.getMessage());
        // // }
        //
    }

}
