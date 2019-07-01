package th.co.thiensurat.fragments.credit.Spare;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;

public class SpareDrawdownMainFragment extends BHFragment{

    @InjectView
    private Button  btnRequestSpareDrawdown, btnImportingSpare;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_spare_drawdown_main;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    protected int titleID() {
        return R.string.title_spare_drawdown;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        //ขอเบิกเครื่อง/อะไหล่
        btnRequestSpareDrawdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showNextView(new RequestSpareDrawdownListFragment());
                showMessage("ขอเบิกเครื่อง/อะไหล่");
            }
        });

        //นำเข้าข้อมูลเบิกเครื่อง/อะไหล่
        btnImportingSpare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showNextView(new ImportDataSpareListFragment());
                showMessage("นำเข้าข้อมูลเบิกเครื่อง/อะไหล่");
            }
        });

    }
}
