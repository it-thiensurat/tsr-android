package th.co.thiensurat.fragments.credit.credit;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viewpagerindicator.TabPageIndicator;

import java.util.Calendar;
import java.util.Date;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.sales.SaleMainFinishedFragment;
import th.co.thiensurat.fragments.sales.SaleMainUnfinishedFragment;
import th.co.thiensurat.fragments.sales.SaleMoreDetailAddress;
import th.co.thiensurat.fragments.sales.SaleProductCheckFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment.ScanCallBack;

import static th.co.thiensurat.fragments.share.BarcodeScanFragment.barcode2;
import static th.co.thiensurat.fragments.share.BarcodeScanFragment.barcode3;
import static th.co.thiensurat.fragments.share.BarcodeScanFragment.oncick;
import static th.co.thiensurat.fragments.share.BarcodeScanFragment.select_baecode;

public class CreditMainFragment_intro extends BHFragment {

    private ProductStockInfo productInfo;

    static public String status = "";

    @InjectView
    private ViewPager vpSaleMain;
    @InjectView
    private TabPageIndicator tabSaleMain;
    private String title;
    private String message;
    String BAR="";


    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_main;
    }

    @Override
    protected int[] processButtons() {
        return new int[0];
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_sales;
    }

    @Override
    public String fragmentTag() {
        // TODO Auto-generated method stub
        return SaleMoreDetailAddress.FRAGMENT_SALE_MOREDETAIL;
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        BHPreference.setProcessType(ProcessType.Sale.toString());


        Log.e("cccc","cccc");

        vpSaleMain.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            private final String[] PAGE_TITLE = {"เก็บเงินค่างวด", "เก็บเงินค่างวด PC"};

            @Override
            public CharSequence getPageTitle(int position) {
                // TODO Auto-generated method stub
                return PAGE_TITLE[position];
            }

            @Override
            public int getCount() {
                // TODO Auto-generated method stub
                return PAGE_TITLE.length;
            }


            @Override
            public Fragment getItem(int position) {
                // TODO Auto-generated method stub

                switch (position) {

                    case 0:




                  CreditListFragment.Data input = new CreditListFragment.Data();
                    input.selectedDate =  Calendar.getInstance().getTime();


                  //  CreditListFragment fragment = BHFragment
                     //       .newInstance(CreditListFragment.class, input);

                  //  showNextView(fragment);
                        return BHFragment.newInstance(CreditListFragment.class,input);

                       // break;
                    case 1:
                     //  return BHFragment.newInstance(CreditListFragment.class);


                        CreditListFragment_pc.Data input2 = new CreditListFragment_pc.Data();
                        input2.selectedDate =  Calendar.getInstance().getTime();


                        //  CreditListFragment fragment = BHFragment
                        //       .newInstance(CreditListFragment.class, input);

                        //  showNextView(fragment);
                        return BHFragment.newInstance(CreditListFragment_pc.class,input2);

                    default:
                        break;
                }

                return null;
            }
        });

        tabSaleMain.setViewPager(vpSaleMain);
    }


}
