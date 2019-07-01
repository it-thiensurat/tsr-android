package th.co.thiensurat.fragments.sales;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.ContractAdapter;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.info.ContractInfo;

public class EditContractsCustomerDetailsFragment extends BHFragment {

    @InjectView private ListView lvCustomerList;
    @InjectView EditText txtSearch;
    @InjectView Button btnSearch;

    public static String info = "";
    private boolean search = false;

    private List<ContractInfo> contractList = null;

    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());


    @Override
    protected int fragmentID() {
        return R.layout.fragment_sale_edit_contracts_customer_details;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back};
    }

    @Override
    protected int titleID() {
        return R.string.title_edit_customer_details;
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                info = "";
                showLastView();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        binHeader();
        GetContractStatusFinish();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtSearch.length() != 0) {
                    search = true;
                    lvCustomerList.setAdapter(null);
                    hideKeyboard();
                    GetContractStatusFinish();
                } else {
                    showDialog("คำเตือน", "กรุณากรอกข้อมูลที่ต้องการค้นหา");
                }

            }
        });
    }

    private void GetContractStatusFinish() {
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
            	
            	/** Test **/
//            	Date d1 = new Date();
//            	Date d2 = new Date();
//            	d2.setTime(d1.getTime() - 1 * 24 * 60 * 60 * 1000);
//                String strToDay = new SimpleDateFormat("yyyy-MM-dd").format(d2);	// adds 24 hours

                Date currentServerDate = BHPreference.getCurrentServerDate();
                Calendar StartDate = Calendar.getInstance();
                StartDate.setTime(currentServerDate);
                /*** [START] :: Fixed - [BHPROJ-1036-8796] - ไม่ให้แก้ไขชื่อ ที่อยู่ และภาพถ่าย ข้ามวัน ให้แก้ไขได้ภายในวันที่ทำสัญญาเท่านั้น รวมทั้งการพิมพ์สัญญาต้องพิมพ์ข้ามวันไม่ได้ด้วย ***/
                //StartDate.add(Calendar.DAY_OF_MONTH, -14);
                /*** [END] :: Fixed - [BHPROJ-1036-8796] - ไม่ให้แก้ไขชื่อ ที่อยู่ และภาพถ่าย ข้ามวัน ให้แก้ไขได้ภายในวันที่ทำสัญญาเท่านั้น รวมทั้งการพิมพ์สัญญาต้องพิมพ์ข้ามวันไม่ได้ด้วย  ***/
                StartDate.set(Calendar.HOUR_OF_DAY, 0);
                StartDate.set(Calendar.MINUTE, 0);
                StartDate.set(Calendar.SECOND, 0);
                StartDate.set(Calendar.MILLISECOND, 0);

                Calendar EndDate = Calendar.getInstance();
                EndDate.setTime(currentServerDate);
                EndDate.set(Calendar.HOUR_OF_DAY, 23);
                EndDate.set(Calendar.MINUTE, 59);
                EndDate.set(Calendar.SECOND, 59);
                EndDate.set(Calendar.MILLISECOND, 999);

                /*String strToDay = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                String strStartDate = strToDay + " 00:00:00.000";
                String strEndDate = strToDay + " 23:59:59.999";*/

                boolean inDay = false;
                /*** [START] :: Fixed - [BHPROJ-1036-8796] - ไม่ให้แก้ไขชื่อ ที่อยู่ และภาพถ่าย ข้ามวัน ให้แก้ไขได้ภายในวันที่ทำสัญญาเท่านั้น รวมทั้งการพิมพ์สัญญาต้องพิมพ์ข้ามวันไม่ได้ด้วย ***/
                /*if(info.equals("1")){ //แก้ไขรายละเอียดลูกค้า
                    inDay = true;
                }else if(info.equals("2") || info.equals("3")){//แก้ไขภาพถ่าย||แก้ไขรายละเอียดลูกค้าเพิ่มเติม
                    inDay = false;
                }*/
                if(info.equals("1") || info.equals("2")){ //แก้ไขรายละเอียดลูกค้า||แก้ไขภาพถ่าย
                    inDay = true;
                }else if(info.equals("3")){//แก้ไขรายละเอียดลูกค้าเพิ่มเติม
                    inDay = false;
                }
                /*** [END] :: Fixed - [BHPROJ-1036-8796] - ไม่ให้แก้ไขชื่อ ที่อยู่ และภาพถ่าย ข้ามวัน ให้แก้ไขได้ภายในวันที่ทำสัญญาเท่านั้น รวมทั้งการพิมพ์สัญญาต้องพิมพ์ข้ามวันไม่ได้ด้วย  ***/


                if(isCredit){
                    contractList = new ContractController().getContractForEditContractsCustomerDetailsFragmentForCredit
                            (BHPreference.organizationCode(), "%" + txtSearch.getText().toString() + "%");
                } else{
                    if(search){
                        search = false;
                        String strSearch = "%" + txtSearch.getText() + "%";
                        contractList = TSRController.getContractStatusFinishByEFFDATEAndSearch(
                                BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString(),
                                StartDate.getTime(), EndDate.getTime(), strSearch, inDay);
                    }else{
                        contractList = TSRController.getContractStatusFinishByEFFDATE(
                                BHPreference.organizationCode(), BHPreference.teamCode(), ContractInfo.ContractStatusName.COMPLETED.toString(),
                                StartDate.getTime(), EndDate.getTime(), inDay);
                    }
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub

                if (contractList != null) {
                    txtSearch.setText("");
                    bindList();
                    lvCustomerList.setVisibility(View.VISIBLE);
                } else {
                    txtSearch.setText("");
                    showDialog("คำเตือน", "ไม่พบข้อมูล");
                    lvCustomerList.setVisibility(View.GONE);
                }
            }
        }.start();
    }

    private void binHeader() {
        LayoutInflater li = activity.getLayoutInflater();
        ViewGroup header = (ViewGroup) li.inflate(
                R.layout.list_contract_header, lvCustomerList,
                false);
        lvCustomerList.addHeaderView(header, null, false);
    }

    private void bindList() {
        ContractAdapter Contract = new ContractAdapter(activity, R.layout.list_contract_item, contractList);
//        BHArrayAdapter<ContractInfo> Contract = new BHArrayAdapter<ContractInfo>(
//                activity, R.layout.list_customer, contractList) {
//
//            class ViewHolder {
//                public TextView txtEffDate;
//                public TextView txtName;
//                public TextView txtSerialNo;
//            }
//
//            @Override
//            protected void onViewItem(final int position, View view,
//                                      Object holder, ContractInfo info) {
//                // TODO Auto-generated method stub
//                ViewHolder vh = (ViewHolder) holder;
//                vh.txtEffDate.setText(BHUtilities.dateFormat(info.EFFDATE));
//                vh.txtName.setText(String.format("%s %s",
//                        BHUtilities.trim(info.CustomerFullName),
//                        BHUtilities.trim(info.CompanyName)));
//
//                vh.txtSerialNo.setText(BHUtilities
//                        .trim(info.ProductSerialNumber));
//            }
//        };
        lvCustomerList.setAdapter(Contract);
        setWidgetsEventListener();
    }

    private void setWidgetsEventListener() {
        lvCustomerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final String RefNo = ((ContractInfo) lvCustomerList.getItemAtPosition(position)).RefNo;
                final String CustomerID = ((ContractInfo) lvCustomerList.getItemAtPosition(position)).CustomerID;
                //showMessage(RefNo);
                BHPreference.setRefNo(RefNo);
                BHPreference.setCustomerID(CustomerID);
                BHPreference.setProcessType(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString());

                if(info.equals("1")){ //แก้ไขรายละเอียดลูกค้า
                    showNextView(new New2SaleCustomerAddressCardFragment());
                }else if(info.equals("2")){//แก้ไขภาพถ่าย
                    if(isCredit){
                        showNextView(new SaleListPhotoFragment());
                    } else{
                        showNextView(new SalePhotographyFragment());
                    }
                }else if(info.equals("3")){//แก้ไขรายละเอียดลูกค้าเพิ่มเติม
                    showNextView(new SaleMoreDetailAddress());

                }

            }

        });
    }

    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
