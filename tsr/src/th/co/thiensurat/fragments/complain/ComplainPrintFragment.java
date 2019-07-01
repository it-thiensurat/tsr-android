package th.co.thiensurat.fragments.complain;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ComplainController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.info.ComplainInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.TeamInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetTeamByIDInputInfo;
import th.co.thiensurat.views.ViewTitle;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class ComplainPrintFragment extends BHFragment {

    public static String FRAGMENT_COMPLAIN = "conplian_fragment";

    public static class Data extends BHParcelable {
        public String ComplainID;
    }

    private Data data;

    @InjectView
    TextView textViewIDcard, textContractDate, textViewEmpName, textViewProductSerialNumber, textViewProductName, textViewNote, textViewProblem, textRequestDate, textComplainPaperID, textViewContractNumber, textViewName, textViewLeaderName;

    @InjectView
    ViewTitle title;

    private ComplainInfo complain;

    @Override
    protected int titleID() {
        return R.string.title_complain;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_complain_print;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_end};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();
        bindData();
    }

    private void bindData() {
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                List<ComplainInfo> complainListTemp = new ComplainController().getComplainListByTeamCodeAndEmployeeID(BHPreference.organizationCode(), null
                        , null, null, data.ComplainID, null);
                if (complainListTemp != null && complainListTemp.size() > 0) {
                    complain = complainListTemp.get(0);
                }
            }

            @Override
            protected void after() {
                if (complain != null) {
                    // วันที่แจ้งปัญหา
                    textRequestDate.setText(BHUtilities.dateFormat(complain.RequestDate));
                    // เลขที่แจ้งปัญหา
                    textComplainPaperID.setText(complain.ComplainPaperID);
                    textViewContractNumber.setText(complain.CONTNO);
                    textViewName.setText(complain.CustomerFullName);
                    textViewIDcard.setText(complain.IDCard);
                    textContractDate.setText(BHUtilities.dateFormat(complain.EFFDATE));
                    textViewEmpName.setText(String.format("%s %s", complain.SaleCode, complain.SaleEmployeeName != null ? complain.SaleEmployeeName : ""));
                    textViewLeaderName.setText(String.format("ทีม %s %s", complain.SaleTeamCode, complain.SaleLeaderName != null ? complain.SaleLeaderName : ""));
                    textViewProductSerialNumber.setText(complain.ProductSerialNumber);
                    textViewProductName.setText(complain.ProductName);

                    if (complain.Status.equals(ComplainController.ComplainStatus.REQUEST.toString())) {
                        textViewProblem.setText(complain.RequestProblemName);
                        textViewNote.setText(complain.RequestDetail);
                    } else if (complain.Status.equals(ComplainController.ComplainStatus.COMPLETED.toString())) {
                        title.setText("รายละเอียดการแก้ไขปัญหา");
                        textViewProblem.setText(complain.ResultProblemName);
                        textViewNote.setText(complain.ResultDetail);
                    }
                }
            }
        }).start();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_print:
                break;
            case R.string.button_end:
                if (complain.Status.equals(ComplainController.ComplainStatus.REQUEST.toString())) {
                    showLastView(FRAGMENT_COMPLAIN);
                } else {
                    showLastView(ComplainActionList.COMPLAIN_ACTION_LIST);
                }
                break;
            default:
                break;
        }
    }
}
