package th.co.thiensurat.fragments.contracts.change;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Arrays;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.controller.EmployeeController;

public class ChangeContractMainFragment extends BHFragment {

    @InjectView
    private Button btnCCBySaleLeader, btnCCBySale;

    private final boolean isCredit = BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Credit.toString());

    private List<String> positionCode = Arrays.asList(BHPreference.PositionCode().split(","));

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_change_contract_main;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_change_contract;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        if(isCredit){
            showNextView(new ChangeContractListFragment());
        } else {
            // TODO Auto-generated method stub
            btnCCBySaleLeader.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    showNextView(new ChangeContractListFragment());
                }
            });

            btnCCBySale.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    showNextView(new ChangeContractListFragment());
                }
            });

            if (positionCode.contains("SaleLeader")) {
                btnCCBySaleLeader.setVisibility(View.VISIBLE);
                btnCCBySale.setVisibility(View.GONE);
            } else if (positionCode.contains("SubTeamLeader")) {
                btnCCBySaleLeader.setVisibility(View.GONE);
                btnCCBySale.setVisibility(View.VISIBLE);
            } else {
                btnCCBySaleLeader.setVisibility(View.GONE);
                btnCCBySale.setVisibility(View.GONE);
            }
        }
    }

}