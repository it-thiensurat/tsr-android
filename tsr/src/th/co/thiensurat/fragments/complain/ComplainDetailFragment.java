package th.co.thiensurat.fragments.complain;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ComplainController;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.ProblemController;
import th.co.thiensurat.data.info.ComplainInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.TeamInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetTeamByIDInputInfo;

public class ComplainDetailFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String RefNo;
    }

    private Data data;

    @InjectView
    TextView textViewContractNumber, textViewName, textViewIDcard, textContractDate, textViewEmpName, textViewProductSerialNumber, textViewProductName, textViewNote, textViewProblem, textViewLeaderName;
    @InjectView
    Spinner spinnerProblem;
    @InjectView
    EditText editTextNote;

    private ContractInfo contract;
    private List<ProblemInfo> problemList;
    private ProblemInfo selectedProblem;
    ProblemAdapter problemAdapter;

    @Override
    protected int titleID() {
        return R.string.title_complain;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_complain_detail;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_confirm};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();

        if (problemList == null) {
            problemList = new ArrayList<ProblemInfo>();
        } else {
            problemList.clear();
        }
        problemAdapter = new ProblemAdapter(activity, R.layout.spinner_item_problem, problemList);
        spinnerProblem.setAdapter(problemAdapter);
        spinnerProblem.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedProblem = (ProblemInfo) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bindData();

        textViewNote.setVisibility(View.GONE);
        textViewProblem.setVisibility(View.GONE);
    }

    private void bindData() {
        (new BackgroundProcess(activity) {
            boolean isOwnTeam = false;
            EmployeeInfo SaleInfo;
            EmployeeInfo TeamHeadInfo;

            @Override
            protected void before() {
                if (BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Sale.toString())) {
                    isOwnTeam = true;
                }

                if (problemList == null) {
                    problemList = new ArrayList<ProblemInfo>();
                } else {
                    problemList.clear();
                }
            }

            @Override
            protected void calling() {
                /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                List<ContractInfo> contractListTemp = new ContractController().getContractListByRefNoOrSearchText(BHPreference.organizationCode(), ContractInfo.ContractStatus.NORMAL.toString(), null, data.RefNo);
                List<ContractInfo> contractListTemp = new ContractController().getContractListByRefNoOrSearchText(BHPreference.organizationCode(), null, data.RefNo);

                if (contractListTemp != null && contractListTemp.size() > 0) {
                    contract = contractListTemp.get(0);
                    if (contract.SaleEmployeeName == null || contract.SaleEmployeeName.trim().isEmpty()) {
                        // import employee
//                        TSRController.importEmployeeByEmployeeCode(contract.OrganizationCode, contract.SaleEmployeeCode, contract.SaleTeamCode);
                        SaleInfo = new EmployeeController().getEmployeeByID(contract.SaleEmployeeCode);

                        if (contract.SaleLeaderName == null || contract.SaleLeaderName.trim().isEmpty()) {
                            EmployeeDetailInfo TeamHead = new EmployeeDetailController().getTeamHeadDetailByTeamCode(contract.OrganizationCode, contract.SaleTeamCode);
                            if (TeamHead == null) {
                                GetTeamByIDInputInfo team = new GetTeamByIDInputInfo();
                                team.Code = contract.SaleTeamCode;
                                TeamInfo output = TSRService.getTeamByID(team, false).Info;
                                if (output != null && output.TeamHeadCode != null) {
//                                    TSRController.importEmployeeByEmployeeCode(contract.OrganizationCode, output.TeamHeadCode, contract.SaleTeamCode);

                                    TeamHeadInfo = new EmployeeController().getEmployeeByID(output.TeamHeadCode);
                                }
                            } else {
                                TeamHeadInfo = new EmployeeController().getEmployeeByID(TeamHead.EmployeeCode);
                            }
                        }
                    }
                }

                List<ProblemInfo> problemListTemp = getProblemByProblemType(BHPreference.organizationCode(), ProblemController.ProblemType.Complain.toString());
                if (problemListTemp != null && problemListTemp.size() > 0) {
                    problemList.addAll(problemListTemp);
                }
            }

            @Override
            protected void after() {
                if (contract != null) {
                    textViewContractNumber.setText(contract.CONTNO);
                    textViewName.setText(contract.CustomerFullName);
                    textViewIDcard.setText(contract.IDCard);
                    textContractDate.setText(BHUtilities.dateFormat(contract.EFFDATE));
                    textViewProductSerialNumber.setText(contract.ProductSerialNumber);
                    textViewProductName.setText(contract.ProductName);

                    if (contract.SaleEmployeeName == null || contract.SaleEmployeeName.trim().isEmpty()) {
                        textViewEmpName.setText(String.format("%s %s", contract.SaleCode, SaleInfo != null ? SaleInfo.FullName() : ""));
                    } else {
                        textViewEmpName.setText(String.format("%s %s", contract.SaleCode, contract.SaleEmployeeName));
                    }
                    if (contract.SaleLeaderName == null || contract.SaleLeaderName.trim().isEmpty()) {
                        textViewLeaderName.setText(String.format("ทีม %s %s", contract.SaleTeamCode, TeamHeadInfo != null ? TeamHeadInfo.FullName() : ""));
                    } else {
                        textViewLeaderName.setText(String.format("ทีม %s %s", contract.SaleTeamCode, contract.SaleLeaderName));
                    }

                    if (problemAdapter != null) {
                        problemAdapter.notifyDataSetChanged();
                    }

                    if (problemList != null && problemList.size() > 0 && selectedProblem == null) {
                        spinnerProblem.setSelection(0);
                    }
                }
            }
        }).start();
    }

    public class ProblemAdapter extends BHSpinnerAdapter<ProblemInfo> {

        public ProblemAdapter(Context context, int resource, List<ProblemInfo> objects) {
            super(context, resource, objects);
        }

        @Override
        protected void setupView(TextView tv, ProblemInfo item) {
            tv.setText(item.ProblemName);
        }
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_confirm:
                if (selectedProblem != null) {
                    final String title = "แจ้งปัญหา";
                    String message = "ยืนยันการแจ้งปัญหา เรื่อง: " + selectedProblem.ProblemName;
                    AlertDialog.Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                            .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    addComplain();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                    setupAlert.show();
                } else {
                    BHUtilities.alertDialog(activity, "กรุณาเลือกรายการ", "เลือกสาเหตุที่จะแจ้งปัญหา").show();
                }
                break;
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }

    private void addComplain() {
        (new BackgroundProcess(activity) {
            ComplainInfo complain;

            @Override
            protected void calling() {
                //REQUEST
                complain = new ComplainInfo();
                complain.ComplainID = DatabaseHelper.getUUID();
                complain.OrganizationCode = BHPreference.organizationCode();
                complain.RefNo = contract.RefNo;
                complain.Status = ComplainController.ComplainStatus.REQUEST.toString();
                complain.RequestProblemID = selectedProblem.ProblemID;
                complain.RequestDetail = editTextNote.getText().toString();
                complain.RequestDate = new Date();
                complain.RequestBy = BHPreference.employeeID();
                complain.RequestTeamCode = BHPreference.teamCode();
                complain.CreateDate = new Date();
                complain.CreateBy = BHPreference.employeeID();
                complain.LastUpdateDate = new Date();
                complain.LastUpdateBy = BHPreference.employeeID();
                complain.TaskType = ComplainController.TaskType.Other.toString();
                complain.ComplainPaperID = new TSRController().getAutoGenerateDocumentID(TSRController.DocumentGenType.Complain, BHPreference.SubTeamCode(), BHPreference.saleCode());
                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                complain.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                TSRController.addComplainStatusREQUEST(complain, true);
            }

            @Override
            protected void after() {
                AlertDialog.Builder builder = BHUtilities.builderDialog(activity, "บันทึก", "แจ้งปัญหาการใช้งานเรียบร้อยแล้ว");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ComplainPrintFragment.Data data = new ComplainPrintFragment.Data();
                        data.ComplainID = complain.ComplainID;
                        ComplainPrintFragment fragment = BHFragment.newInstance(ComplainPrintFragment.class, data);
                        showNextView(fragment);
                    }
                });
                builder.show();
            }
        }).start();
    }
}
