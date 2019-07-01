package th.co.thiensurat.fragments.cutdivisor.contract;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
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
import th.co.thiensurat.data.controller.CutDivisorContractController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.info.CutDivisorContractInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetCutDivisorContractInputInfo;

public class CutDivisorContractListFragment extends BHFragment {

    @InjectView
    private EditText txtSearch;
    @InjectView
    private Button btnSearch;
    @InjectView
    private ListView listView;

    private List<CutDivisorContractInfo> cutDivisorContractList;
    private ContractAdapter cutDivisorContractAdapter;

    private AlertDialog alertDialog;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_cut_divisor_contract_list_fragment;
    }

    @Override
    protected int titleID() {
        return R.string.title_cut_divisor_contract;
    }

    @Override
    protected int[] processButtons() {
        return new int[0];
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        LayoutInflater li = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) li.inflate(R.layout.list_contract_header, listView, false);
        listView.addHeaderView(header, null, false);

        getContractList("", false);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getContractList(txtSearch.getText().toString(), true);
            }
        });
    }

    private void getContractList(final String searchText, final boolean isSearch) {
        new BackgroundProcess(activity) {
            @Override
            protected void before() {
                if (cutDivisorContractList == null) {
                    cutDivisorContractList = new ArrayList<CutDivisorContractInfo>();
                } else {
                    cutDivisorContractList.clear();
                }
            }

            @Override
            protected void calling() {
                List<CutDivisorContractInfo> local = new CutDivisorContractController().getContractNoCutDivisorContractBySearch(BHPreference.organizationCode(), searchText);
                if (local != null && local.size() > 0) {
                    cutDivisorContractList.addAll(local);
                } else {
                    if (!searchText.trim().isEmpty() && isSearch) {
                        // Server
                        GetCutDivisorContractInputInfo input = new GetCutDivisorContractInputInfo();
                        input.OrganizationCode = BHPreference.organizationCode();
                        input.Status = "NEW";
                        input.SearchText = searchText;
                        List<CutDivisorContractInfo> server = TSRService.getCutDivisorContractByCutDivisorContractIDOrStatusOrSearchText(input, false).Info;
                        if(server != null && server.size() > 0){
                            for(CutDivisorContractInfo info : server){
                                importContractFromServer(info.ContractOrganizationCode, null, info.ContractRefNo);
                                cutDivisorContractList.add(info);
                            }
                        }
                    }
                }
            }

            @Override
            protected void after() {
                if (cutDivisorContractAdapter == null) {
//                    cutDivisorContractAdapter = new CutDivisorContractAdapter(activity, R.layout.list_cut_divisor_contract_item, cutDivisorContractList);
                    cutDivisorContractAdapter = new ContractAdapter(activity, R.layout.list_contract_item, cutDivisorContractList);
                    listView.setAdapter(cutDivisorContractAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> patent, View view, int position, long id) {
                            if(alertDialog == null){
                                beforeSave((CutDivisorContractInfo) patent.getItemAtPosition(position));
                            }
                        }
                    });
                } else {
                    cutDivisorContractAdapter.notifyDataSetChanged();
                }

                if (cutDivisorContractList.size() == 0) {
                    if (searchText.trim().isEmpty() && isSearch) {
                        BHUtilities.alertDialog(activity, "แจ้งเตือน", "กรุณากรอกข้อมูลที่ต้องการค้นหา").show();
                    } else {
                        BHUtilities.alertDialog(activity, "แจ้งเตือน", "ไม่พบข้อมูล").show();
                    }
                }
            }
        }.start();
    }

    private void beforeSave(final CutDivisorContractInfo cutDivisorContract) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.cut_divisor_contract_dialog, null);
        final EditText edtRequestProblemDetail = (EditText) alertLayout.findViewById(R.id.edtRequestProblemDetail);

        AlertDialog.Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle("การตัดตัวหาร")
                .setView(alertLayout)
                .setCancelable(false)
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        hideKeyboard(edtRequestProblemDetail);
                        dialog.dismiss();
                        alertDialog = null;
                    }
                })
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        alertDialog.dismiss();
                        hideKeyboard(edtRequestProblemDetail);
                        String requestProblemDetail = edtRequestProblemDetail.getText().toString().trim();
                        if (!requestProblemDetail.isEmpty()) {
                            save(cutDivisorContract, requestProblemDetail);
                        } else {
                            dialog.dismiss();
                            alertDialog(cutDivisorContract);
                        }
                        alertDialog = null;
                    }
                });

        alertDialog =  setupAlert.show();
    }

    public void hideKeyboard(View v){
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void alertDialog(final CutDivisorContractInfo cutDivisorContract) {
        AlertDialog.Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle("แจ้งเตือน")
                .setMessage("กรุณากรอก รายละเอียดการตัดตัวหาร")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        beforeSave(cutDivisorContract);
                    }
                });
        setupAlert.show();
    }

    public class CutDivisorContractAdapter extends BHArrayAdapter<CutDivisorContractInfo> {

        public CutDivisorContractAdapter(Context context, int resource, List<CutDivisorContractInfo> objects) {
            super(context, resource, objects);
        }

        private class ViewHolder {
            public TextView txtNo, txtCONTNOAndCustomerFullName, txtProductSerialNumber;
        }

        @Override
        protected void onViewItem(final int position, View view, Object holder, CutDivisorContractInfo info) {
            ViewHolder vh = (ViewHolder) holder;
            vh.txtNo.setText(String.valueOf(position + 1));
            vh.txtCONTNOAndCustomerFullName.setText(String.format("%s\n%s", info.CONTNO, info.CustomerFullName));
            vh.txtProductSerialNumber.setText(info.ProductSerialNumber);
        }
    }

    private void save(final CutDivisorContractInfo cutDivisorContract, final String requestProblemDetail) {
        (new BackgroundProcess(activity) {
            CutDivisorContractInfo newCutDivisorContract;

            @Override
            protected void before() {
                newCutDivisorContract = new CutDivisorContractInfo();
                newCutDivisorContract.CutDivisorContractID = DatabaseHelper.getUUID();
                newCutDivisorContract.OrganizationCode = BHPreference.organizationCode();
                newCutDivisorContract.RefNo = cutDivisorContract.ContractRefNo;
                newCutDivisorContract.Status = CutDivisorContractController.CutDivisorContractStatus.REQUEST.toString();
                newCutDivisorContract.RequestProblemDetail = requestProblemDetail;
                newCutDivisorContract.RequestDate = new Date();
                newCutDivisorContract.RequestBy = BHPreference.employeeID();
                newCutDivisorContract.RequestTeamCode = BHPreference.teamCode();
                newCutDivisorContract.CreateDate = new Date();
                newCutDivisorContract.CreateBy = BHPreference.employeeID();
                newCutDivisorContract.LastUpdateDate = new Date();
                newCutDivisorContract.LastUpdateBy = BHPreference.employeeID();
                newCutDivisorContract.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
            }

            @Override
            protected void calling() {
                TSRController.addCutDivisorContract(newCutDivisorContract, true);
                cutDivisorContractList.remove(cutDivisorContract);
            }

            @Override
            protected void after() {
                cutDivisorContractAdapter.notifyDataSetChanged();
            }
        }).start();
    }
}