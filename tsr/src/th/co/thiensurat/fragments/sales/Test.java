package th.co.thiensurat.fragments.sales;

import android.os.Bundle;

import th.co.bighead.utilities.BHFragment;
import th.co.thiensurat.R;

public class Test extends BHFragment {
    @Override
    protected int fragmentID() {
        return R.layout.fragment_sale_unfinished;
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:

                break;

            case R.string.bluetooth_connect:

                break;
        }
    }

    @Override
    protected int[] processButtons() {
        return new int[] {R.string.button_back, R.string.bluetooth_connect};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

    }
}
