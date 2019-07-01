package th.co.thiensurat.fragments.credit.Import;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;

public class ImportCreditDataMainFragment extends BHFragment implements View.OnClickListener {

    @InjectView
    Button btnImportAudit, btnImportCredit;

    @Override
    protected int fragmentID() {
        return R.layout.fragment_credit_import_credit_data;
    }

    @Override
    protected int titleID() {
        return R.string.title_import_credit_data;
    }

    @Override
    protected int[] processButtons() {
        return null;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        btnImportAudit.setOnClickListener(this);
        btnImportCredit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnImportAudit:
                ImportAuditListFragment.Data inputData = new ImportAuditListFragment.Data();
                ImportAuditListFragment fragment = BHFragment
                        .newInstance(ImportAuditListFragment.class, inputData);
                showNextView(fragment);
                break;
            case R.id.btnImportCredit:
//                ImportCreditListFragment.Data inputData = new ImportCreditListFragment.Data();
//                ImportAuditListFragment fragment = BHFragment
//                        .newInstance(ImportAuditListFragment.class, inputData);
                showNextView(new ImportCreditSelectDateFragment());
                break;
            default:break;
        };
    }
}
