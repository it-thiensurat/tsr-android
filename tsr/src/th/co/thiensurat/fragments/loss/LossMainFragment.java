package th.co.thiensurat.fragments.loss;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.ContractAdapter;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.LossController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatus;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetNextSalePaymentPeriodForSearchInputInfo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class LossMainFragment extends BHFragment {
    @InjectView
    Button btnPaymentPeriodSearch;
    @InjectView
    ListView lvPaymentPeriodList;
    @InjectView
    EditText etPaymentPeriodSearch;

    private List<SalePaymentPeriodInfo> lossList = null;
    private ContractAdapter lossAdapter;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_loss_main;
    }

    @Override
    protected int titleID() {
        return R.string.title_loss;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_next};
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        super.onProcessButtonClicked(buttonID);
        switch (buttonID) {
            case R.string.button_next:
                if (lvPaymentPeriodList.getCheckedItemPosition() != -1) {
                    Next(lossList.get(lvPaymentPeriodList.getCheckedItemPosition() - 1));
                } else {
                    BHUtilities.alertDialog(activity, "คำเตือน", "กรุณาเลือกสัญญา").show();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        btnPaymentPeriodSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BHPreference.PositionCode().contains("SaleLeader") || BHPreference.PositionCode().contains("SubTeamLeader")) {
                    BindLossList(etPaymentPeriodSearch.getText().toString());
                } else {
                    showMessage("ไม่มีสิทธิ์ในการทำรายการ");
                }
            }
        });

        LayoutInflater li = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) li.inflate(R.layout.list_contract_header, lvPaymentPeriodList, false);
        lvPaymentPeriodList.addHeaderView(header, null, false);

        if (lossList == null) lossList = new ArrayList<>();
        lossAdapter = new ContractAdapter(activity, R.layout.list_contract_item, lossList);
        lvPaymentPeriodList.setAdapter(lossAdapter);

        if (BHPreference.PositionCode().contains("SaleLeader") || BHPreference.PositionCode().contains("SubTeamLeader")) {
            BindLossList(null);
        } else {
            showMessage("ไม่มีสิทธิ์ในการทำรายการ");
        }
    }

    protected void BindLossList(final String searchText) {
        new BackgroundProcess(activity) {
            @Override
            protected void before() {
                if (lossList == null) {
                    lossList = new ArrayList<>();
                } else {
                    lossList.clear();
                }
            }

            @Override
            protected void calling() {
                List<SalePaymentPeriodInfo> localList = new LossController().getNextSalePaymentPeriodByContractTeam
                        (BHPreference.organizationCode(), BHPreference.teamCode(), searchText, null, BHPreference.fortnightYear(), BHPreference.fortnightNumber());
                if (localList != null && localList.size() > 0) {
                    lossList.addAll(localList);
                } else {
                    if (searchText != null && !searchText.isEmpty()) {
                        GetNextSalePaymentPeriodForSearchInputInfo input = new GetNextSalePaymentPeriodForSearchInputInfo();
                        input.OrganizationCode = BHPreference.organizationCode();
                        input.SaleTeamCode = BHPreference.teamCode();
                        input.SearchText = etPaymentPeriodSearch.getText().toString();
                        input.FortnightYear = BHPreference.fortnightYear();
                        input.FortnightNumber = BHPreference.fortnightNumber();
                        //-- Fixed - [BHPROJ-0016-3231][Android-ตัดสัญญาค้าง-WriteOffNPL] แก้ไข Code เพื่อรองรับการเพิ่ม Field เพื่อแยกว่าเป็น ปักษ์การขาย (Fortnight) ของ ฝ่ายขาย (Sale) vs ฝ่ายธุรกิจต่อเนื่อง (CRD)
                        input.ProcessType = BHPreference.processTypeOfEmployee();

                        List<SalePaymentPeriodInfo> serverList = TSRService.getNextSalePaymentPeriodForSearch(input, false).Info;
                        if (serverList != null && serverList.size() > 0) {
                            lossList.addAll(serverList);
                        }
                    }
                }
            }

            @Override
            protected void after() {
                if (lossAdapter == null) {
                    lossAdapter = new ContractAdapter(activity, R.layout.list_contract_item, lossList);
                } else {
                    lvPaymentPeriodList.clearChoices();
                    lossAdapter.notifyDataSetChanged();
                }

                if (lossList != null && lossList.size() == 0) {
                    showMessage(searchText != null && searchText.isEmpty() ? "กรุณากรอกคำค้นหา" : "ไม่พบข้อมูล");
                }
            }
        }.start();
    }

    private void Next(final SalePaymentPeriodInfo info) {
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                List<SalePaymentPeriodInfo> localList = new LossController().getNextSalePaymentPeriodByContractTeam
                        (BHPreference.organizationCode(), BHPreference.teamCode(), null, info.RefNo, BHPreference.fortnightYear(), BHPreference.fortnightNumber());
                if (localList == null) {
                    TSRController.importContractFromServer(BHPreference.organizationCode(), null, info.RefNo);
                }
            }

            @Override
            protected void after() {
                LossDetailFragment.Data dataDetail = new LossDetailFragment.Data();
                dataDetail.Serialnumber = info.ProductSerialNumber;
                dataDetail.Name = info.ProductName;
                BHPreference.setRefNo(info.RefNo);
                LossDetailFragment fmLossDetail = BHFragment.newInstance(LossDetailFragment.class, dataDetail);
                showNextView(fmLossDetail);
            }
        }).start();
    }
}
