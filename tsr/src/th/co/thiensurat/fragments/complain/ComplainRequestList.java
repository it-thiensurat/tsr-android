package th.co.thiensurat.fragments.complain;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetContractForSearchInputInfo;

public class ComplainRequestList extends BHFragment {

    private static final String COMPLAIN_LIST = "COMPLAIN_LIST";

    @InjectView
    private ListView lvCustomerList;
    @InjectView
    private Button btnSearch;
    @InjectView
    private EditText etSearch;

    private ComplainAdapter complainAdapter;

    private List<ContractInfo> contractList;

    @Override
    protected int titleID() {
        return R.string.title_complain;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_complain_request_list;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_next};
    }

    @Override
    public String fragmentTag() {
        return ComplainPrintFragment.FRAGMENT_COMPLAIN;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindContractList(etSearch.getText().toString(), true);
            }

        });

        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.complain_list_head, lvCustomerList, false);
        lvCustomerList.addHeaderView(header, null, false);

        if (contractList == null) {
            contractList = new ArrayList<ContractInfo>();
        } else {
            contractList.clear();
        }
        complainAdapter = new ComplainAdapter(activity, R.layout.list_customer_complain, contractList);
        lvCustomerList.setAdapter(complainAdapter);

        bindContractList(null, false);
    }

    private void bindContractList(final String searchText, final boolean isSearch) {
        (new BackgroundProcess(activity) {

            @Override
            protected void before() {
                if (contractList == null) {
                    contractList = new ArrayList<ContractInfo>();
                } else {
                    contractList.clear();
                }
            }

            @Override
            protected void calling() {
                /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
//                List<ContractInfo> list = new ContractController().getContractListByRefNoOrSearchText(BHPreference.organizationCode(), ContractInfo.ContractStatus.NORMAL.toString(), searchText, null);
                //List<ContractInfo> list = new ContractController().getContractListByRefNoOrSearchText(BHPreference.organizationCode(), searchText, null);
                List<ContractInfo> list = new ContractController().getContractListForComplainRequestList
                        (searchText, BHPreference.organizationCode(), null, BHPreference.employeeID(), BHPreference.teamCode(), BHPreference.sourceSystem(), BHPreference.PositionCode());
                if (list != null && list.size() > 0) {
                    //Local
                    contractList.addAll(list);
                } else {
                    // Server
                    if (isSearch && !searchText.trim().isEmpty()) {
                        GetContractForSearchInputInfo input = new GetContractForSearchInputInfo();
                        input.OrganizationCode = BHPreference.organizationCode();
                        input.SaleTeamCode = "A"; //ALL
                        input.Status = ContractInfo.ContractStatus.NORMAL.toString();
                        input.SearchText = etSearch.getText().toString();

                        List<ContractInfo> contractListForSearch = TSRService.getContractForSearch(input, false).Info;
                        if (contractListForSearch != null && contractListForSearch.size() > 0) {
                            /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                            /*
                            for (ContractInfo cont : contractListForSearch) {
                                TSRController.importContractFromServer(cont.OrganizationCode, cont.SaleTeamCode, cont.RefNo);
                            }
                            */
                            /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                            contractList.addAll(contractListForSearch);
                        }
                    }
                }
            }

            @Override
            protected void after() {
                if (complainAdapter != null) {
                    lvCustomerList.clearChoices();
                    complainAdapter.notifyDataSetChanged();
                }
                if (isSearch && searchText.trim().isEmpty()) {
                    BHUtilities.alertDialog(activity, "คำเตือน", "กรุณากรอกข้อมูลที่ต้องการค้นหา").show();
                } else {
                    if (contractList != null && contractList.size() == 0) {
                        BHUtilities.alertDialog(activity, "ผลการค้นหา", "ไม่พบข้อมูลลูกค้าที่จะทำการแจ้งปัญญหา").show();
                    }
                }
            }
        }).start();
    }

    public class ComplainAdapter extends BHArrayAdapter<ContractInfo> {
        class ViewHolder {
            public TextView txtContractNo, txtName;
        }

        public ComplainAdapter(Context context, int resource, List<ContractInfo> objects) {
            super(context, resource, objects);
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, ContractInfo info) {
            ViewHolder vh = (ViewHolder) holder;
            vh.txtContractNo.setText(info.CONTNO);
            vh.txtName.setText(info.CustomerFullName);
        }
    }

    private void next(final ContractInfo selectedContract) {
        /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
        (new BackgroundProcess(activity) {
            List<ContractInfo> listContractInfo;

            @Override
            protected void before() {
            }

            @Override
            protected void calling() {
                listContractInfo = new ContractController().getContractListForComplainRequestList
                        (null, BHPreference.organizationCode(), selectedContract.RefNo, BHPreference.employeeID(), BHPreference.teamCode(), BHPreference.sourceSystem(), BHPreference.PositionCode());
                if (listContractInfo == null) {
                    TSRController.importContractFromServer(BHPreference.organizationCode(), null, selectedContract.RefNo);
                }
            }

            @Override
            protected void after() {
                ComplainDetailFragment.Data data = new ComplainDetailFragment.Data();
                data.RefNo = selectedContract.RefNo;
                ComplainDetailFragment fragment = BHFragment.newInstance(ComplainDetailFragment.class, data);
                showNextView(fragment);
            }
        }).start();
        /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_next:
                if (lvCustomerList.getCheckedItemPosition() != -1) {
                    next(contractList.get(lvCustomerList.getCheckedItemPosition() - 1));
                } else {
                    BHUtilities.alertDialog(activity, "เลือกรายการ", "กรุณาเลือกสัญญาที่จะทำการแจ้งปัญหา").show();
                }
                break;
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }
}
