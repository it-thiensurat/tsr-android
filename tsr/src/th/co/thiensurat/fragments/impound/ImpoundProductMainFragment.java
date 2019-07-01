package th.co.thiensurat.fragments.impound;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.PositionController;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ImpoundProductMainFragment extends BHFragment {

	@InjectView
	private Button btnTeamOwn, btnTeamOther,btnRequestApprove;

	@Override
	protected int fragmentID() {
		return R.layout.fragment_impound_product_main;
	}

	@Override
	protected int titleID() {
		return R.string.title_remove;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {

        BHPreference.setProcessType(SaleFirstPaymentChoiceFragment.ProcessType.ImpoundProduct.toString());


		btnTeamOwn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showNextView(new ImpoundProductListFragment());
			}
		});

		btnTeamOther.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showNextView(new ImpoundProductListOtherTeamFragment());
			}
		});

		if (BHPreference.sourceSystem().equals(EmployeeController.SourceSystem.Sale.toString())) {
			if(BHPreference.PositionCode().contains(PositionController.PositionCode.SaleLeader.toString())){
				btnTeamOwn.setVisibility(View.VISIBLE);
				btnTeamOther.setVisibility(View.VISIBLE);
			}else{
				btnTeamOwn.setVisibility(View.GONE);
				btnTeamOther.setVisibility(View.GONE);
			}
		}else{
			String[] positions = BHPreference.PositionCode().split(",");
			boolean authen = false;
			for(String position : positions){
				if(position.equals(PositionController.PositionCode.Credit.toString())) {
					authen = true;
					break;
				}
			}
			if(authen){
				btnTeamOwn.setVisibility(View.GONE);
				btnTeamOther.setVisibility(View.VISIBLE);
			}else{
				btnTeamOwn.setVisibility(View.GONE);
				btnTeamOther.setVisibility(View.GONE);
			}
		}

		//-- [Checked@09/03/2016] ยังไม่เปิดให้ใช้ Feature นี้ (Feature Approved รายการขออนุมัติถอดเครื่องบน Android)
        btnRequestApprove.setVisibility(View.GONE);
		btnRequestApprove.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showNextView(new ImpoundProductListRequestApproveFragment());
			}
		});
	}

	@Override
	protected int[] processButtons() {
		return null;
	}

}
