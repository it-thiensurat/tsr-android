package th.co.thiensurat.fragments.sales;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.controller.EmployeeController;

public class EditContractsMainFragment extends BHFragment {

    @InjectView private Button btnEditCustomerDetails; //แก้ไขรายละเอียดลูกค้า
    @InjectView private Button btnEditPhoto; //แก้ไขภาพถ่าย
    @InjectView private Button btnEditDetailsMoreClients; //แก้ไขรายละเอียดลูกค้าเพิ่มเติม

    @Override
    protected int fragmentID() {
        return R.layout.fragment_sale_edit_contracts_main;
    }

    @Override
    protected int[] processButtons() {
        return new int[0];
    }

    @Override
    protected int titleID() {
        return R.string.title_edit_contracts;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        btnEditCustomerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*EditContractsCustomerDetailsFragment.Data data = new EditContractsCustomerDetailsFragment.Data();
                data.idCard = "55";
                EditContractsCustomerDetailsFragment fm = BHFragment.newInstance(EditContractsCustomerDetailsFragment.class, data);
                showNextView(fm);*/
                EditContractsCustomerDetailsFragment.info = "1";
                showNextView(new EditContractsCustomerDetailsFragment());
                showMessage("แก้ไขรายละเอียดลูกค้า");
            }
        });

        btnEditPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditContractsCustomerDetailsFragment.info = "2";
                showNextView(new EditContractsCustomerDetailsFragment());
                showMessage("แก้ไขภาพถ่าย");
            }
        });

        btnEditDetailsMoreClients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditContractsCustomerDetailsFragment.info = "3";
                showNextView(new EditContractsCustomerDetailsFragment());
                showMessage("แก้ไขรายละเอียดลูกค้าเพิ่มเติม");
            }
        });

        if(BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString())){
            btnEditCustomerDetails.setVisibility(View.GONE);
            btnEditDetailsMoreClients.setVisibility(View.GONE);
        }
    }
}
