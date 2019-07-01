package th.co.thiensurat.fragments.complain;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;

public class ComplainMainFragment extends BHFragment {

    @InjectView
    private Button btnComplainRequest, btnComplainAction;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_complain_main;
    }

    @Override
    protected int titleID() {
        return R.string.title_complain;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        btnComplainRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextView(new ComplainRequestList());
            }
        });

        btnComplainAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextView(new ComplainActionList());
            }
        });
    }

    @Override
    protected int[] processButtons() {
        return null;
    }
}
