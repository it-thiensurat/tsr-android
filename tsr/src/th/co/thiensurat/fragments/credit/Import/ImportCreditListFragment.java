package th.co.thiensurat.fragments.credit.Import;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AssignInfo;

public class ImportCreditListFragment extends BHFragment {
    @InjectView
    private TextView txtCountCredit;
    @InjectView
    DragSortListView listView;
    private DragSortController mController;
    private CustomerAdapter customerAdapter;
    private List<AssignInfo> creditList;
    private Data data;

    public static class Data extends BHParcelable {
        Date selectedDate;
    }

    @Override
    protected int fragmentID() {
        return R.layout.fragment_credit_import_credit_list;
    }

    @Override
    protected int[] processButtons() {
        return new int[]{R.string.button_back, R.string.button_save, R.string.button_display_map};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();

        LayoutInflater inflater = activity.getLayoutInflater();
        ViewGroup headerListView = (ViewGroup) inflater.inflate(R.layout.credit_import_credit_list_header, listView, false);
        listView.addHeaderView(headerListView, null, false);

        mController = buildController(listView);
        listView.setFloatViewManager(mController);
        listView.setOnTouchListener(mController);
        listView.setDragEnabled(true);
        listView.setDropListener(onDrop);
        listView.setEnableAlpha(false);

        creditList = new ArrayList<AssignInfo>();
        customerAdapter = new CustomerAdapter(activity, R.layout.list_credit_import_credit, creditList);
        listView.setAdapter(customerAdapter);

        getCreditList();
    }

    public DragSortController buildController(DragSortListView dslv) {
        DragSortController controller = new DragSortController(dslv);
        controller.setDragHandleId(R.id.imgMove);
        controller.setRemoveEnabled(false);
        controller.setSortEnabled(true);
        controller.setDragInitMode(DragSortController.ON_DOWN);
        controller.setBackgroundColor(getResources().getColor(R.color.bg_list_view_selected));
        return controller;
    }

    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            if (from != to) {
                AssignInfo itemFrom = creditList.get(from);
                customerAdapter.remove(itemFrom);
                customerAdapter.insert(itemFrom, to);
            }
        }
    };

    public void getCreditList() {
        new BackgroundProcess(activity) {
            @Override
            protected void before() {
                if (creditList == null) {
                    creditList = new ArrayList<AssignInfo>();
                }
            }

            @Override
            protected void calling() {
                creditList.clear();
                List<AssignInfo> result = new AssignController().getSalePaymentPeriodListForAssignCredit(BHPreference.organizationCode(),
                        BHPreference.teamCode(), BHPreference.employeeID(),
                        data != null ? data.selectedDate : BHUtilities.parseDate(new Date(), "yyyy-MM-dd", BHUtilities.LOCALE_EN), "%%", AddressInfo.AddressType.AddressPayment.toString());
                if (result != null && result.size() > 0) {
                    creditList.addAll(result);
                }
            }

            @Override
            protected void after() {
                txtCountCredit.setText(String.format("วันที่ " + BHUtilities.dateFormat(data.selectedDate) + " ลูกค้าที่ต้องเก็บเงินจำนวน %d คน", creditList.size()));
                customerAdapter.notifyDataSetChanged();
            }
        }.start();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_save:
                SaveCreditSortOrder();
                break;
            case R.string.button_display_map:
                ImportCreditMapFragment.Data input = new ImportCreditMapFragment.Data();
                input.selectedDate = data.selectedDate;
                ImportCreditMapFragment fragment = BHFragment
                        .newInstance(ImportCreditMapFragment.class, input);
                showNextView(fragment);
                break;
            default:
                break;
        }
    }

    public void SaveCreditSortOrder() {
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                if (creditList != null && creditList.size() > 0) {
                    int i = 1;
                    for (AssignInfo assign : creditList) {
                        assign.Order = i;
//                        assign.OrderExpect = i;
                        assign.LastUpdateBy = BHPreference.employeeID();
                        assign.LastUpdateDate = new Date();
                        TSRController.updateAssign(assign, true);
                        i++;
                    }
                }
            }
        }.start();
    }

    public class CustomerAdapter extends BHArrayAdapter<AssignInfo> {

        public CustomerAdapter(Context context, int resource, List<AssignInfo> objects) {
            super(context, resource, objects);
        }

        private class ViewHolder {
            public TextView txtNo, txtCustomerFullNameAndCONTNO, txtPaymentPeriodNumber, txtNetAmount;
            public ImageView imgMove;
        }

        @Override
        protected void onViewItem(int position, View view, Object holder, AssignInfo info) {
            ViewHolder viewHolder = (ViewHolder) holder;
            viewHolder.txtNo.setText(String.valueOf(position + 1));

            /*** [START] :: Fixed - [BHPROJ-0026-740] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/
//            AddressInfo addressInfo = TSRController.getAddress(info.RefNo, AddressInfo.AddressType.AddressPayment);
//
            SpannableString txtCustomer = new SpannableString( info.CustomerFullName + "\n" + info.CONTNO + "\n" + info.getAddress().Address() + "\nTel. " + info.getAddress().Telephone());
            txtCustomer.setSpan(new ForegroundColorSpan(getResources().getColor(getColor(info.HoldSalePaymentPeriod))), 0, info.CustomerFullName.length(), 0);
            txtCustomer.setSpan(new ForegroundColorSpan(Color.BLACK), info.CustomerFullName.length(), txtCustomer.length(), 0);
            txtCustomer.setSpan(new ForegroundColorSpan(Color.GRAY), info.CustomerFullName.length() + info.CONTNO.length() + 1, txtCustomer.length(), 0);
            viewHolder.txtCustomerFullNameAndCONTNO.setText(txtCustomer, TextView.BufferType.SPANNABLE);

//            viewHolder.txtCustomerFullNameAndCONTNO.setText(info.CustomerFullName + "\n" + info.CONTNO);
            /*** [END] :: Fixed - [BHPROJ-0026-740] :: [Android-ระบบเก็บเงิน] แก้ไขการแสดงผลของหน้าจอ List รายการสัญญา  ***/

            viewHolder.txtPaymentPeriodNumber.setText(String.valueOf(info.PaymentPeriodNumber));
            viewHolder.txtNetAmount.setText(BHUtilities.numericFormat(Double.valueOf(info.OutStandingPayment)));
            //view.setBackgroundColor(getResources().getColor(getColor(info.HoldSalePaymentPeriod)));
        }

        public int getColor(int HoldSalePaymentPeriod){
            switch (HoldSalePaymentPeriod){
                case 0: return R.color.hold_payment_0;
                case 1: return R.color.hold_payment_1;
                case 2: return R.color.hold_payment_2;
                default: return R.color.hold_payment_3;
            }
        }
    }
}
