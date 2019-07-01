package th.co.thiensurat.fragments.cutoff.contract;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.ContractAdapter;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.CutOffContractController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.info.CutOffContractInfo;

public class CutOffContractMainFragment extends BHFragment{

    @InjectView
    private EditText txtSearch;
    @InjectView
    private Button btnSearch;
    @InjectView
    private ListView lvContractList;

    private List<CutOffContractInfo> cutOffContractInfoList;
    private ContractAdapter contractAdapter;
    private int num;
    private String search = "%%";

    private AlertDialog alertDialog;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_cut_off_contract_main;
    }

    @Override
    protected int titleID() {
        return R.string.title_cut_off_contract;
    }

    @Override
    protected int[] processButtons() {
        return new int[0];
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        cutOffContractInfoList = new CutOffContractController().getContractNoCutOffContractBySearch(BHPreference.organizationCode(), search);

        if(cutOffContractInfoList != null){
            binHeader();
            bindListView();
        }else{
            showDialog("แจ้งเตือน", "ไม่พบข้อมูล");
        }

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = "%" + txtSearch.getText().toString() + "%";
                bindNewContractInfoList();
            }
        });
    }

    private void binHeader() {
        LayoutInflater li = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) li.inflate(
                R.layout.list_contract_header, lvContractList,
                false);
        lvContractList.addHeaderView(header, null, false);
    }

    private void bindListView() {

        num = 1;
        contractAdapter = new ContractAdapter(activity, R.layout.list_contract_item, cutOffContractInfoList);
        lvContractList.setAdapter(contractAdapter);

        lvContractList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(alertDialog == null){
                    CutOffContractInfo cutOffContractInfo = cutOffContractInfoList.get(i - 1);
                    displayAlertDialog(cutOffContractInfo.RefNo);
                }
            }
        });

    }

    private void bindNewContractInfoList() {
        cutOffContractInfoList = new CutOffContractController().getContractNoCutOffContractBySearch(BHPreference.organizationCode(), search);

        if (cutOffContractInfoList != null) {
            lvContractList.setVisibility(View.VISIBLE);
            lvContractList.setAdapter(null);

            bindListView();

        } else {
            lvContractList.setVisibility(View.GONE);
            lvContractList.setAdapter(null);

            showDialog("แจ้งเตือน", "ไม่พบข้อมูล");
        }
    }

    private void displayAlertDialog(final String RefNo) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.cut_off_contract_dialog, null);
        final EditText editTextProblemDetail = (EditText) alertLayout.findViewById(R.id.editTextProblemDetail);

        AlertDialog.Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle("ตัดสัญญาออกจากฟอร์ม")
                .setView(alertLayout)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String problemDetail = editTextProblemDetail.getText().toString();

                        if(problemDetail != null && !problemDetail.isEmpty() && !problemDetail.trim().isEmpty()){
                            save(RefNo, problemDetail);
                        } else {
                            dialog.cancel();
                            alertDialog(RefNo);
                        }
                        alertDialog = null;
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        alertDialog = null;
                    }
                });

        alertDialog = setupAlert.show();
    }

    private void alertDialog(final String RefNo) {
        AlertDialog.Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle("แจ้งเตือน")
                .setMessage("กรุณากรอก รายละเอียด การตัดสัญญาออกจากฟอร์ม")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                        displayAlertDialog(RefNo);
                    }
                });
        setupAlert.show();
    }

//    public class ContractAdapter extends BHArrayAdapter<CutOffContractInfo> {
//
//        public ContractAdapter(Context context, int resource, List<CutOffContractInfo> objects) {
//            super(context, resource, objects);
//        }
//
//        private class ViewHolder {
//            public TextView txt_no, txt_CONTNO, txt_name, txt_product_serial_number;
//        }
//
//        @Override
//        protected void onViewItem(final int position, View view, Object holder, CutOffContractInfo info) {
//            ViewHolder vh = (ViewHolder) holder;
//
//            vh.txt_no.setText(String.valueOf(num));
//            vh.txt_CONTNO.setText(info.CONTNO);
//            vh.txt_name.setText(info.CustomerFullName);
//            vh.txt_product_serial_number.setText(info.ProductSerialNumber);
//            num++;
//
//        }
//
//    }

    private void save(final String RefNo, final String ProblemDetail) {

        (new BackgroundProcess(activity) {
            CutOffContractInfo newCutOffContract;
            Date newDate = new Date();

            @Override
            protected void before() {
                newCutOffContract = new CutOffContractInfo();
                newCutOffContract.CutOffContractID = DatabaseHelper.getUUID();
                newCutOffContract.OrganizationCode = BHPreference.organizationCode();
                newCutOffContract.RefNo = RefNo;
                newCutOffContract.Status = CutOffContractController.CutOffContractStatus.REQUEST.toString();
                newCutOffContract.RequestProblemDetail = ProblemDetail;
                newCutOffContract.RequestDate = newDate;
                newCutOffContract.RequestBy = BHPreference.employeeID();
                newCutOffContract.RequestTeamCode = BHPreference.teamCode();
                newCutOffContract.IsAvailableContract = false;
                newCutOffContract.CreateDate = newDate;
                newCutOffContract.CreateBy = BHPreference.employeeID();
                newCutOffContract.LastUpdateDate = newDate;
                newCutOffContract.LastUpdateBy = BHPreference.employeeID();
                newCutOffContract.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
            }

            @Override
            protected void calling() {
                TSRController.addCutOffContract(newCutOffContract, true);
            }

            @Override
            protected void after() {
                bindNewContractInfoList();
            }
        }).start();
    }

}
