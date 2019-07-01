package th.co.thiensurat.fragments.credit.Audit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.MenuInfo;
import th.co.thiensurat.fragments.sales.SaleCheckPhotographyFragment;

public class CheckCustomersDetailsFragment extends BHFragment {

    private String ImageTypeCode;

    public static class Data extends BHParcelable {
        public boolean isAudtiMenu;
    }
    private Data data;


    @InjectView private ListView lvMenuPhoto;
    @InjectView private TextView textViewContractNo, textViewName, textViewAddress;


    @Override
    protected int titleID() {
        // TODO Auto-generated method stub

        if(data == null){
            data = getData();
        }
        return (data.isAudtiMenu == true) ? R.string.title_check_customers : R.string.title_main_credit;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_credit_audit_check_customers_details;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();
        ContractInfo contractInfo = new TSRController().getContract(BHPreference.RefNo());
        if (contractInfo != null) {
            AddressInfo addressInfo = new TSRController().getAddressByRefNoByAddressTypeCode(BHPreference.RefNo(), "AddressInstall");
            DebtorCustomerInfo debtorCustomerInfo = new TSRController().getDebCustometByID(contractInfo.CustomerID);

            if (addressInfo != null && debtorCustomerInfo != null) {
                textViewContractNo.setText(contractInfo.CONTNO);
                textViewName.setText(debtorCustomerInfo.CustomerFullName());
                textViewAddress.setText(addressInfo.Address());
            }
        }

        bindMenu();
    }

    private void bindMenu() {
        MenuInfo[] menus = MenuInfo.from(R.array.sale_photo);
        BHArrayAdapter<MenuInfo> adapter = new BHArrayAdapter<MenuInfo>(activity, R.layout.list_sale_photo, menus) {

            class ViewHolder {
                public TextView txtMenu;
            }

            @Override
            protected void onViewItem(int position, View view, Object holder, MenuInfo info) {
                // TODO Auto-generated method stub
                ViewHolder vh = (ViewHolder) holder;
                vh.txtMenu.setText(info.titleID);
            }
        };

        lvMenuPhoto.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvMenuPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                MenuInfo info = (MenuInfo) parent.getItemAtPosition(position);
                view.setSelected(true);
                selectedMenu(position, info.titleID);
            }

        });
    }

    protected void selectedMenu(int position, int titleResourceID) {
        // TODO Auto-generated method stub
        switch (titleResourceID) {
            case R.string.sale_photo_home:
                ImageTypeCode = ContractImageController.ImageType.ADDRESS.toString();
                CheckPhoto(ImageTypeCode);
                break;
            case R.string.sale_photo_id:
                ImageTypeCode = ContractImageController.ImageType.IDCARD.toString();
                CheckPhoto(ImageTypeCode);
                break;
            case R.string.sale_photo_gps:
                ImageTypeCode = ContractImageController.ImageType.MAP.toString();
                CheckPhoto(ImageTypeCode);
                break;
            /*** [START] Fixed - [BHPROJ-0024-690] ***/
            case R.string.sale_photo_gps_payment:
                ImageTypeCode = ContractImageController.ImageType.MAPPAYMENT.toString();
                CheckPhoto(ImageTypeCode);
                break;
            /*** [END] Fixed - [BHPROJ-0024-690] ***/
            case R.string.sale_photo_product:
                ImageTypeCode = ContractImageController.ImageType.PRODUCT.toString();
                CheckPhoto(ImageTypeCode);
                break;
            case R.string.sale_photo_payment_card:
                ImageTypeCode = ContractImageController.ImageType.PAYMENTCARD.toString();
                CheckPhoto(ImageTypeCode);
                break;
            default:
                break;
        }
    }

    private void CheckPhoto(String imageTypeCode) {
        // TODO Auto-generated method stub
        SaleCheckPhotographyFragment.Data data1 = new SaleCheckPhotographyFragment.Data();
        data1.TypeCode = imageTypeCode;
        data1.title = titleID();
        SaleCheckPhotographyFragment fm = BHFragment.newInstance(SaleCheckPhotographyFragment.class, data1);
        showNextView(fm);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;

            default:
                break;
        }
    }


}
