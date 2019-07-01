package th.co.thiensurat.fragments.report;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHListViewAdapter;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ReportDailySummaryInfo;

/**
 * Created by macmini04 on 25/12/2014.
 */
public class ReportDailySummaryFragment extends BHFragment {

//    private List<ReportDailySummaryInfo> dailySummaryList;
//    private BHListViewAdapter adapter;
//
//    public static class Data extends BHParcelable {
//        //public String currentSaleLevel;
//        public int currentSaleLevel;
//        public String fillterString;
//    }
//
//    private Data data;
//    private boolean firstItem = false;
//
//    @InjectView
//    private ListView lvDailySummary;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_report_daily_summary;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    public boolean enableLandscape() {
        return bOrentation;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
//        data = getData();
//        firstItem = false;
//        getReport();
//        setWidgetsEventListener();

        setWebView();
    }

    @InjectView
    private WebView webView;

    private  void setWebView()
    {
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String urlNewString) {
                view.loadUrl(urlNewString);
                BHLoading.show(getActivity());



                return true;
            }

            Runnable rRun;
            Handler handler = new Handler();;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap facIcon) {
                //SHOW LOADING IF IT ISNT ALREADY VISIBLE

                BHLoading.show(getActivity());
                rRun=new Runnable() {
                    @Override
                    public void run() {
                        showMessage(getResources().getString(R.string.mobile_report_error));
                        BHLoading.close();
                    }
                };
                int delay = Integer.parseInt(getActivity().getResources().getString(R.string.mobile_report_delay));
                handler.postDelayed(rRun, delay);

            }

            @Override
            public void onPageFinished(WebView view, String url) {

                handler.removeCallbacks(rRun);
                showMessage(getResources().getString(R.string.mobile_report_success));
                BHLoading.close();
            }


        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(BHPreference.TSR_REPORT_MOBILE + "?EmpID=" + BHPreference.employeeID() + "&Report=" + getResources().getString(R.string.mobile_report_daily_summary) + "");

    }

    /*** [START] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/
        /*
    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }
    */
    /*** [END] :: Fixed - [BHPROJ-0016-855] :: Revise Menu Report tobe SubMenu ***/

    /*
    private void setWidgetsEventListener() {

        lvDailySummary.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (data.currentSaleLevel > 1) {
                    ReportDailySummaryInfo info = dailySummaryList.get(position-1);
                    int intSaleLevel = data.currentSaleLevel-1;
                    ReportDailySummaryFragment.Data selectedData = new ReportDailySummaryFragment.Data();

                    String strSaleLevel = "";
                    if (data.currentSaleLevel == 6)
                        strSaleLevel = info.SaleLevel06;
                    else if (data.currentSaleLevel == 5)
                        strSaleLevel = info.SaleLevel05;
                    else if (data.currentSaleLevel == 4)
                        strSaleLevel = info.SaleLevel04;
                    else if (data.currentSaleLevel == 3)
                        strSaleLevel = info.SaleLevel03;
                    else if (data.currentSaleLevel == 2)
                        strSaleLevel = info.SaleLevel02;
                    else if (data.currentSaleLevel == 1)
                        strSaleLevel = info.SaleLevel01;

                    selectedData.fillterString = strSaleLevel;
                    selectedData.currentSaleLevel = intSaleLevel;

                    ReportDailySummaryFragment fm = BHFragment.newInstance(ReportDailySummaryFragment.class, selectedData);
                    showNextView(fm);
                }
            }
        });
    }

    public void getReport() {
//        dailySummaryList = TSRController.getReportDailySummary();
//        bindReportDailySummary();


        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                dailySummaryList = TSRController.getReportDailySummary(data.currentSaleLevel, data.fillterString);
            }

            @Override
            protected void after() {
                if (dailySummaryList != null) {
                    bindReportDailySummary();
                }
            }
        }).start();

    }

    public void bindReportDailySummary() {
        adapter = new BHListViewAdapter(activity) {

            class ViewHolder {
                public TextView txtDepartment;
                public TextView txtLineManager;
                public TextView txtSupervisor;
                public TextView txtSaleLeader;
                public TextView txtSubTeamLeader;
                public TextView txtSale;
                public TextView txtCashSumNumber;
                public TextView txtCashSumMoney;
                public TextView txtCashNumber;
                public TextView txtCashMoney;
                public TextView txtCashOutNumber;
                public TextView txtCashOutMoney;
                public TextView txtOutSumNumber;
                public TextView txtOutSumMoney;
                public TextView txtOutNumber;
                public TextView txtOutMoney;
                public TextView txtOutStandingNumber;
                public TextView txtOutStandingMoney;
                public TextView txtSaleSumTotal;
                public TextView txtSaleSumMoney;
                public TextView txtProductTradeIn;
                public TextView txtSendMoneyAlready;
                public TextView txtSendMoneyOut;
                public TextView txtImpoundBySale;
                public TextView txtImpoundByTeam;
                public TextView txtChangeProduct;
                public TextView txtChangeContractBySale;
                public TextView txtChangeContractByTeam;
                public TextView txtWriteOffVPN;
                public TextView txtSendContract;
                public TextView txtSendSlip;
                public TextView txtSendImpound;
                public TextView txtSendChangeProduct;
                public TextView txtSendChangeContract;
                public TextView txtSendSentMoney;
            }

            @Override
            protected int viewForSectionHeader(int section) {
                return R.layout.list_report_daily_summary_header;
            }

            @Override
            protected int viewForItem(int section, int row) {
                return R.layout.list_report_daily_summary_item;
            }

            @Override
            protected int getItemCount(int section) {
                return dailySummaryList != null ? dailySummaryList.size() : 0;
            }

            @Override
            protected void onViewItem(View view, Object holder, int section, int row) {

                ViewHolder vh = (ViewHolder) holder;
                final ReportDailySummaryInfo info = dailySummaryList.get(row);
                try {

                    vh.txtDepartment.setText(info.SaleLevel06);
                    vh.txtLineManager.setText(info.SaleLevel05);
                    vh.txtSupervisor.setText(info.SaleLevel04);
                    vh.txtSaleLeader.setText(info.SaleLevel03);
                    vh.txtSubTeamLeader.setText(info.SaleLevel02);
                    vh.txtSale.setText(info.SaleLevel01);
                    vh.txtCashSumNumber.setText(BHUtilities.numericFormat(info.CashSumNumber));
                    vh.txtCashSumMoney.setText(BHUtilities.numericFormat(info.CashSumMoney));
                    vh.txtCashNumber.setText(BHUtilities.numericFormat(info.CashNumber));
                    vh.txtCashMoney.setText(BHUtilities.numericFormat(info.CashMoney));
                    vh.txtCashOutNumber.setText(BHUtilities.numericFormat(info.CashOutNumber));
                    vh.txtCashOutMoney.setText(BHUtilities.numericFormat(info.CashOutNumber));
                    vh.txtOutSumNumber.setText(BHUtilities.numericFormat(info.OutSumNumber));
                    vh.txtOutSumMoney.setText(BHUtilities.numericFormat(info.OutSumMoney));
                    vh.txtOutNumber.setText(BHUtilities.numericFormat(info.OutNumber));
                    vh.txtOutMoney.setText(BHUtilities.numericFormat(info.OutMoney));
                    vh.txtOutStandingNumber.setText(BHUtilities.numericFormat(info.OutStandingNumber));
                    vh.txtOutStandingMoney.setText(BHUtilities.numericFormat(info.OutStandingMoney));
                    vh.txtSaleSumTotal.setText(BHUtilities.numericFormat(info.SaleSumTotal));
                    vh.txtSaleSumMoney.setText(BHUtilities.numericFormat(info.SaleSumMoney));
                    vh.txtProductTradeIn.setText(BHUtilities.numericFormat(info.ProductTradeIn));
                    vh.txtSendMoneyAlready.setText(BHUtilities.numericFormat(info.SendMoneyAlready));
                    vh.txtSendMoneyOut.setText(BHUtilities.numericFormat(info.SendMoneyOut));
                    vh.txtImpoundBySale.setText(BHUtilities.numericFormat(info.ImpoundBySale));
                    vh.txtImpoundByTeam.setText(BHUtilities.numericFormat(info.ImpoundByTeam));
                    vh.txtChangeProduct.setText(BHUtilities.numericFormat(info.ChangeProduct));
                    vh.txtChangeContractBySale.setText(BHUtilities.numericFormat(info.ChangeContractBySale));
                    vh.txtChangeContractByTeam.setText(BHUtilities.numericFormat(info.ChangeContractByTeam));
                    vh.txtWriteOffVPN.setText(BHUtilities.numericFormat(info.WriteOffVPN));
                    vh.txtSendContract.setText(BHUtilities.numericFormat(info.SendContract));
                    vh.txtSendSlip.setText(BHUtilities.numericFormat(info.SendSlip));
                    vh.txtSendImpound.setText(BHUtilities.numericFormat(info.SendImpound));
                    vh.txtSendChangeProduct.setText(BHUtilities.numericFormat(info.SendChangeContract));
                    vh.txtSendChangeContract.setText(BHUtilities.numericFormat(info.SendChangeContract));
                    vh.txtSendSentMoney.setText(BHUtilities.numericFormat(info.SendSentMoney));

                    vh.txtCashSumNumber.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.currentSaleLevel <= 3) {
                                ReportSummaryDailySaleFragment.Data data = new ReportSummaryDailySaleFragment.Data();
                                data.SaleLevel = saleLevelToString();//saleLevel;
                                data.EmpID = getEmpID(data.SaleLevel, info);
                                ReportSummaryDailySaleFragment DailySale = BHFragment.newInstance(ReportSummaryDailySaleFragment.class, data);
                                showNextView(DailySale);
                            }
                        }
                    });

                    vh.txtImpoundBySale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.currentSaleLevel <= 3) {
                                ReportSummaryDailyImpoundFragment.Data data = new ReportSummaryDailyImpoundFragment.Data();
                                data.SaleLevel = saleLevelToString();//saleLevel;
                                data.EmpID = getEmpID(data.SaleLevel, info);
                                ReportSummaryDailyImpoundFragment DailyImpound = BHFragment.newInstance(ReportSummaryDailyImpoundFragment.class, data);
                                showNextView(DailyImpound);
                            }
                        }
                    });

                    vh.txtChangeProduct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.currentSaleLevel <= 3) {
                                ReportSummaryDailyChangeProductFragment.Data data = new ReportSummaryDailyChangeProductFragment.Data();
                                data.SaleLevel = saleLevelToString();//saleLevel;
                                data.EmpID = getEmpID(data.SaleLevel, info);
                                ReportSummaryDailyChangeProductFragment DailyChangeProduct = BHFragment.newInstance(ReportSummaryDailyChangeProductFragment.class, data);
                                showNextView(DailyChangeProduct);
                            }
                        }
                    });

                    vh.txtChangeContractBySale.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.currentSaleLevel <= 3) {
                                ReportSummaryDailyChangeContractFragment.Data data = new ReportSummaryDailyChangeContractFragment.Data();
                                data.SaleLevel = saleLevelToString();//saleLevel;
                                data.EmpID = getEmpID(data.SaleLevel, info);
                                ReportSummaryDailyChangeContractFragment DailyChangeContract = BHFragment.newInstance(ReportSummaryDailyChangeContractFragment.class, data);
                                showNextView(DailyChangeContract);
                            }
                        }
                    });

                    vh.txtWriteOffVPN.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.currentSaleLevel <= 3) {
                                ReportSummaryDailyWriteOffNPLFragment.Data data = new ReportSummaryDailyWriteOffNPLFragment.Data();
                                data.SaleLevel = saleLevelToString();//saleLevel;
                                data.EmpID = getEmpID(data.SaleLevel, info);
                                ReportSummaryDailyWriteOffNPLFragment DailyWriteOffNPL = BHFragment.newInstance(ReportSummaryDailyWriteOffNPLFragment.class, data);
                                showNextView(DailyWriteOffNPL);
                            }
                        }
                    });

                    vh.txtSendMoneyAlready.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.currentSaleLevel <= 3) {
                                ReportSummaryDailySendMoneyFragment.Data data = new ReportSummaryDailySendMoneyFragment.Data();
                                data.SaleLevel = saleLevelToString();//saleLevel;
                                data.EmpID = getEmpID(data.SaleLevel, info);
                                ReportSummaryDailySendMoneyFragment DailySendMoney = BHFragment.newInstance(ReportSummaryDailySendMoneyFragment.class, data);
                                showNextView(DailySendMoney);
                            }
                        }
                    });

                    vh.txtSendContract.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (data.currentSaleLevel <= 3) {
                                ReportSummaryDailySendDocumentFragment.Data data = new ReportSummaryDailySendDocumentFragment.Data();
                                data.SaleLevel = saleLevelToString();//saleLevel;
                                data.EmpID = getEmpID(data.SaleLevel, info);
                                ReportSummaryDailySendDocumentFragment DailySendMoney = BHFragment.newInstance(ReportSummaryDailySendDocumentFragment.class, data);
                                showNextView(DailySendMoney);
                            }
                        }
                    });


                   if (firstItem == false) {
                       firstItem = true;
                   } else {
                       if (data.currentSaleLevel == 6) {

                       } else if (data.currentSaleLevel == 5) {
                           vh.txtDepartment.setText("");

                       } else if (data.currentSaleLevel == 4) {
                           vh.txtDepartment.setText("");
                           vh.txtLineManager.setText("");

                       } else if (data.currentSaleLevel == 3) {
                           vh.txtDepartment.setText("");
                           vh.txtLineManager.setText("");
                           vh.txtSupervisor.setText("");

                       } else if (data.currentSaleLevel == 2) {
                           vh.txtDepartment.setText("");
                           vh.txtLineManager.setText("");
                           vh.txtSupervisor.setText("");
                           vh.txtSaleLeader.setText("");

                       } else if (data.currentSaleLevel == 1) {
                           vh.txtDepartment.setText("");
                           vh.txtLineManager.setText("");
                           vh.txtSupervisor.setText("");
                           vh.txtSaleLeader.setText("");
                           vh.txtSubTeamLeader.setText("");
                       }
                   }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        lvDailySummary.setAdapter(adapter);
    }

    private String saleLevelToString() {
        String saleLevel = "";
        if (data.currentSaleLevel == 6)
            saleLevel = "SaleLevel06";
        else if (data.currentSaleLevel == 5)
            saleLevel = "SaleLevel05";
        else if (data.currentSaleLevel == 4)
            saleLevel = "SaleLevel04";
        else if (data.currentSaleLevel == 3)
            saleLevel = "SaleLevel03";
        else if (data.currentSaleLevel == 2)
            saleLevel = "SaleLevel02";
        else if (data.currentSaleLevel == 1)
            saleLevel = "SaleLevel01";
        return saleLevel;
    }

    private String getEmpID(String saleLevel, ReportDailySummaryInfo info) {
        String empID = "";
        if (saleLevel.equals("SaleLevel03")) {
            empID = info.SaleLevel03;
        } else if (saleLevel.equals("SaleLevel02")) {
            empID = info.SaleLevel02;
        } else if (saleLevel.equals("SaleLevel01")) {
            empID = info.SaleLevel01;
        }
        return empID;
    }
    */


//    private void showNoticeDialogBox(final String title, final String message) {
//        AlertDialog.Builder setupAlert;
//        setupAlert = new AlertDialog.Builder(activity);
//        setupAlert.setTitle(title);
//        setupAlert.setMessage(message);
//        setupAlert.setPositiveButton("Ok",
//                new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        // ????
//                    }
//                });
//        setupAlert.show();
//    }
}
