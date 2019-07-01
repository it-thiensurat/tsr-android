package th.co.thiensurat.fragments.complain;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import th.co.thiensurat.data.controller.ProblemController;
import th.co.thiensurat.data.info.ComplainInfo;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.TeamInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetTeamByIDInputInfo;

public class ComplainActionDetailFragment extends BHFragment {

    public static class Data extends BHParcelable {
        public String ComplainID;
    }

    private Data data;

    @InjectView
    TextView textViewContractNumber, textViewName, textViewIDcard, textContractDate, textViewEmpName, textViewProductSerialNumber, textViewProductName, textViewNote, textViewLeaderName, textRequestDate, textComplainPaperID, textRequestProblemName, textRequestDetail, txtApproveDetail;
    //    @InjectView TextView textViewProblem;
//    @InjectView Spinner spinnerProblem;
    @InjectView
    EditText editTextNote;
    @InjectView
    LinearLayout layoutResolve, layoutApproved;
    @InjectView
    View layout_underline;

    private ComplainInfo complain;

    //-- Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem
//    private ProblemInfo selectedProblem;
//    ProblemAdapter problemAdapter;
    private List<ProblemInfo> problemList;

    @Override
    protected int titleID() {
        return R.string.title_complain;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_complain_action_detail;
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

        /* [START] :: Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem */
        /*
        problemAdapter = new ProblemAdapter(activity, R.layout.spinner_item_problem, problemList);
        spinnerProblem.setAdapter(problemAdapter);
        spinnerProblem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                selectedProblem = (ProblemInfo) adapterView.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        */
        /* [END] :: Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem */

        bindData();

        textViewNote.setVisibility(View.GONE);

        //-- Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem */
//        textViewProblem.setVisibility(View.GONE);
    }

    private void bindData() {
        (new BackgroundProcess(activity) {
            EmployeeInfo SaleInfo;
            EmployeeInfo TeamHeadInfo;

            @Override
            protected void before() {
                if (problemList == null) {
                    problemList = new ArrayList<ProblemInfo>();
                } else {
                    problemList.clear();
                }
            }

            @Override
            protected void calling() {
                List<ComplainInfo> complainListTemp = new ComplainController().getComplainListByTeamCodeAndEmployeeID(BHPreference.organizationCode(), null
                        , null, null, data.ComplainID, new String[]{ComplainController.ComplainStatus.REQUEST.toString(), ComplainController.ComplainStatus.APPROVED.toString()});
                if (complainListTemp != null && complainListTemp.size() > 0) {
                    complain = complainListTemp.get(0);
                    if (complain.SaleEmployeeName == null || complain.SaleEmployeeName.trim().isEmpty()) {
                        // import employee
//                        TSRController.importEmployeeByEmployeeCode(complain.OrganizationCode, complain.SaleEmployeeCode, complain.SaleTeamCode);
                        SaleInfo = new EmployeeController().getEmployeeByID(complain.SaleEmployeeCode);

                        if (complain.SaleLeaderName == null || complain.SaleLeaderName.trim().isEmpty()) {
                            EmployeeDetailInfo TeamHead = new EmployeeDetailController().getTeamHeadDetailByTeamCode(complain.OrganizationCode, complain.SaleTeamCode);
                            if (TeamHead == null) {
                                GetTeamByIDInputInfo team = new GetTeamByIDInputInfo();
                                team.Code = complain.SaleTeamCode;
                                TeamInfo output = TSRService.getTeamByID(team, false).Info;
                                if (output != null && output.TeamHeadCode != null) {
//                                    TSRController.importEmployeeByEmployeeCode(complain.OrganizationCode, output.TeamHeadCode, complain.SaleTeamCode);

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
                if (complain != null) {
                    textRequestDate.setText(BHUtilities.dateFormat(complain.RequestDate));
                    textComplainPaperID.setText(complain.ComplainPaperID);
                    textRequestProblemName.setText(complain.RequestProblemName);
                    textRequestDetail.setText(complain.RequestDetail);
                    textViewContractNumber.setText(complain.CONTNO);
                    textViewName.setText(complain.CustomerFullName);
                    textViewIDcard.setText(complain.IDCard);
                    textContractDate.setText(BHUtilities.dateFormat(complain.EFFDATE));
                    textViewProductSerialNumber.setText(complain.ProductSerialNumber);
                    textViewProductName.setText(complain.ProductName);

                    if (complain.SaleEmployeeName == null || complain.SaleEmployeeName.trim().isEmpty()) {
                        textViewEmpName.setText(String.format("%s %s", complain.SaleCode, SaleInfo != null ? SaleInfo.FullName() : ""));
                    } else {
                        textViewEmpName.setText(String.format("%s %s", complain.SaleCode, complain.SaleEmployeeName));
                    }
                    if (complain.SaleLeaderName == null || complain.SaleLeaderName.trim().isEmpty()) {
                        textViewLeaderName.setText(String.format("ทีม %s %s", complain.SaleTeamCode, TeamHeadInfo != null ? TeamHeadInfo.FullName() : ""));
                    } else {
                        textViewLeaderName.setText(String.format("ทีม %s %s", complain.SaleTeamCode, complain.SaleLeaderName));
                    }

                    List<Integer> listId = new ArrayList<Integer>();
                    listId.add(R.string.button_confirm);
                    if (complain.Status.equals(ComplainController.ComplainStatus.APPROVED.toString()) && complain.AssigneeEmpID != null && complain.AssigneeEmpID.equals(BHPreference.employeeID())) {
                        layout_underline.setVisibility(View.VISIBLE);
                        layoutApproved.setVisibility(View.GONE);
                        layoutResolve.setVisibility(View.VISIBLE);
                        activity.setViewProcessButtons(listId, View.VISIBLE);
                    } else {
                        layout_underline.setVisibility(complain.Status.equals(ComplainController.ComplainStatus.APPROVED.toString()) ? View.VISIBLE : View.GONE);
                        layoutApproved.setVisibility(complain.Status.equals(ComplainController.ComplainStatus.APPROVED.toString()) ? View.VISIBLE : View.GONE);
                        layoutResolve.setVisibility(View.GONE);
                        activity.setViewProcessButtons(listId, View.GONE);
                        if (layoutApproved.getVisibility() == View.VISIBLE)
                            txtApproveDetail.setText(complain.ApproveDetail != null ? complain.ApproveDetail : "-");
                    }

                    /* [START] :: Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem */
                    /*
                    if (problemAdapter != null) {
                        problemAdapter.notifyDataSetChanged();
                    }

                    if (problemList != null && problemList.size() > 0 && selectedProblem == null) {
                        spinnerProblem.setSelection(0);
                    }
                    */
                    /* [END] :: Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem */
                }
            }
        }).start();
    }

    /* [START] :: Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem */
    /*
    public class ProblemAdapter extends BHSpinnerAdapter<ProblemInfo> {
        public ProblemAdapter(Context context, int resource, List<ProblemInfo> objects) {
            super(context, resource, objects);
        }

        protected void setupView(TextView tv, ProblemInfo item) {
            tv.setText(item.ProblemName);
        }
    }
    */
    /* [END] :: Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem */

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_confirm:
                /* [START] :: Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem */
                /*
                if (selectedProblem != null) {
                    final String title = "บันทึกการแก้ไขปัญหา";
                    String message = "ยืนยันการแก้ไขปัญหา เรื่อง: " + selectedProblem.ProblemName;
                    AlertDialog.Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                            .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    updateComplain();
                                }
                            })
                            .setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            });
                    setupAlert.show();
                } else {
                    BHUtilities.alertDialog(activity, "กรุณาเลือกรายการ", "เลือกสาเหตุที่จะแก้ไขปัญหา").show();
                }
                */
                final String title = "บันทึกการแก้ไขปัญหา";
                String message = "ยืนยันการแก้ไขปัญหา เรื่อง: " + complain.RequestProblemName;
                AlertDialog.Builder setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message)
                        .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateComplain();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                setupAlert.show();
                /* [END] :: Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem */
                break;
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }

    private void updateComplain() {
        (new BackgroundProcess(activity) {
            ComplainInfo complain;

            @Override
            protected void calling() {
                //ACTION
                complain = new ComplainController().getComplainByComplainID(data.ComplainID);
                complain.Status = ComplainController.ComplainStatus.COMPLETED.toString();

                //-- Fixed - [BHPROJ-0026-841] :: Remove DDL Effective Problem
//                complain.ResultProblemID = selectedProblem.ProblemID;
                complain.ResultProblemID = complain.RequestProblemID;

                complain.ResultDetail = editTextNote.getText().toString();
                complain.EffectiveDate = new Date();
                complain.EffectiveBy = BHPreference.employeeID();
                complain.LastUpdateDate = new Date();
                complain.LastUpdateBy = BHPreference.employeeID();
                // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                complain.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                TSRController.updateComplainStatusCOMPLETED(complain, true);
            }

            @Override
            protected void after() {
                AlertDialog.Builder builder = BHUtilities.builderDialog(activity, "บันทึก", "บันทึกการแก้ไขปัญหาเรียบร้อยแล้ว");
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
