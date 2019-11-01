package th.co.thiensurat.fragments.credit;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.fragments.sales.SaleMainFinishedFragment;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.data.GetContractStatusFinishForCreditBySearchInputInfo;
import th.co.thiensurat.service.data.GetContractStatusFinishForCreditBySearchOutputInfo;

public class ContractDetails extends BHFragment {

    @InjectView private ViewPager viewPager;
    @InjectView private EditText txtSearch;
    @InjectView private Button btnSearch;
    @InjectView private Button btnRefresh;
    public static List<ContractInfo> contractList = null;

    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());

    @Override
    protected int fragmentID() {
        return R.layout.fragment_contract_details_for_credit;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_contract_details;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return 1;
            }

            @Override
            public Fragment getItem(int position) {
                // TODO Auto-generated method stub


                //return BHFragment.newInstance(SaleMainFinishedFragment.class);

                SaleMainFinishedFragment fm = BHFragment.newInstance(SaleMainFinishedFragment.class);
                fm.isContractDetails = true;

                return  fm;
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchContractList();
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {

                    @Override
                    public int getCount() {
                        // TODO Auto-generated method stub
                        return 1;
                    }

                    @Override
                    public Fragment getItem(int position) {
                        // TODO Auto-generated method stub


                        //return BHFragment.newInstance(SaleMainFinishedFragment.class);

                        SaleMainFinishedFragment fm = BHFragment.newInstance(SaleMainFinishedFragment.class);
                        fm.isContractDetails = true;

                        return  fm;
                    }
                });

            }
        });

    }


    private void searchContractList() {

        if (txtSearch.getText().toString().equals("")) {
            showWarningDialog("แจ้งเตือน", "กรุณากรอกข้อมูลที่ต้องการค้าหา");
        } else {

            SaleMainFinishedFragment.listViewFinish.setAdapter(null);

            new BackgroundProcess(activity) {
                final String strSearch = "%" + txtSearch.getText().toString() + "%";

                @Override
                protected void before() {
                }
                @Override
                protected void calling() {
                    // TODO Auto-generated method stub
                    try {
                        if(isCredit){
                            contractList = TSRController.getContractStatusFinishForCreditBySearch(BHPreference.organizationCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), strSearch);
                            /*** [START] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/

                            if (contractList == null) {
                                //-- Search form server --> Only display on List not yet importContractFromServer
                                GetContractStatusFinishForCreditBySearchInputInfo input = new GetContractStatusFinishForCreditBySearchInputInfo();
                                input.OrganizationCode = BHPreference.organizationCode();
                                input.StatusName = ContractInfo.ContractStatusName.COMPLETED.toString();
                                input.SearchText = strSearch;
                                GetContractStatusFinishForCreditBySearchOutputInfo result = TSRService.getContractStatusFinishForCreditBySearch(input, false);
                                log("test", "AAAA");
                                if (result.Info != null) {
                                    contractList = result.Info;
                                }
                            }
                            /*** [END] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/
                        } else {
                            contractList = TSRController.getContractStatusFinishBySearch(BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString(), strSearch);
                        }
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
                @Override
                protected void after() {
                    // TODO Auto-generated method stub
                    SaleMainFinishedFragment.contractList = contractList;

                    SaleMainFinishedFragment.contractAdapter = new SaleMainFinishedFragment.ContractAdapter(activity, R.layout.list_main_status, SaleMainFinishedFragment.contractList);
                    SaleMainFinishedFragment.listViewFinish.setAdapter(SaleMainFinishedFragment.contractAdapter);

                    if (SaleMainFinishedFragment.contractList == null) {
                        showWarningDialog("แจ้งเตือน", "ไม่พบข้อมูล");
                    }
                }
            }.start();
        }
    }



}
