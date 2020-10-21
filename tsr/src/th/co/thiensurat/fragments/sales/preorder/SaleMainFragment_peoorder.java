package th.co.thiensurat.fragments.sales.preorder;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viewpagerindicator.TabPageIndicator;

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
import th.co.thiensurat.fragments.sales.SaleMoreDetailAddress;
import th.co.thiensurat.fragments.sales.preorder_setting.SaleMainFragment_preorder_setting2;
import th.co.thiensurat.fragments.share.BarcodeScanFragment.ScanCallBack;
import th.co.thiensurat.fragments.share.PreorderFragment;

import static th.co.thiensurat.fragments.share.BarcodeScanFragment.select_baecode;

public class SaleMainFragment_peoorder extends BHFragment {

    private ProductStockInfo productInfo;

    static public String status = "";
    public String getProductID,getProductName,getProductSerialNumber,getCONTNO,getRefNo,getContractReferenceNo,getReceiptCode,getReceiptID;


    @InjectView
    private ViewPager vpSaleMain;
    @InjectView
    private TabPageIndicator tabSaleMain;
    private String title;
    private String message;
    String BAR="";

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_add_customer};
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_main;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_sales_preorder;
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
            private final String[] PAGE_TITLE = {"รายการจองคงค้าง", "รายการจองเสร็จสมบูรณ์"};

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
                        return BHFragment.newInstance(SaleMainUnfinishedFragment_preorder.class);

                    case 1:

                       // return BHFragment.newInstance(SaleMainFragment_preorder_setting2.class);

                   return BHFragment.newInstance(SaleMainFinishedFragment_preorder.class);

                    default:
                        break;
                }

                return null;
            }
        });

        tabSaleMain.setViewPager(vpSaleMain);
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_add_customer:
                if (BHGeneral.DEVELOPER_MODE) {
                    new BackgroundProcess(activity) {

                        @Override
                        protected void calling() {
                            // TODO Auto-generated method stub
                            if (BHGeneral.DEVELOPER_MODE)
                                new ProductStockController().checkedProducts();
                        }
                    }.start();
                }

                BHPreference.setRefNo("");
                // ใช้งานจริงเปิด Method BarcodeScan แล้วปิด บรรทัดที่ 113-119

                BarcodeScan();

                // ใช้เทส เปิดคอมเม้นด้านล่าง บรรทัดที่ 113-118 ปิด Method
                // BarcodeScan
                // BHPreference.setProductSerialNumber("SA04580002");
                // SaleProductCheckFragment.Data data = new
                // SaleProductCheckFragment.Data();
                // data.productID = "SA";
                // data.productSerialNumber = "SA04580002";
                // SaleProductCheckFragment fm =
                // BHFragment.newInstance(SaleProductCheckFragment.class, data);
                // showNextView(fm);

                break;
            default:
                break;
        }
    }

    public void BarcodeScan() {
        // TODO Auto-generated method stub
        PreorderFragment fm = BHFragment.newInstance(PreorderFragment.class, new ScanCallBack() {

            @Override
            public void onResult(BHParcelable data) {
                // TODO Auto-generated method stub
                final PreorderFragment.Result barcodeResult = (PreorderFragment.Result) data;

                (new BackgroundProcess(activity) {
                    @Override
                    protected void calling() {
                        // TODO Auto-generated method stub



                                 getProductID=barcodeResult.getProductID;
                                  getProductName=barcodeResult.getProductName;
                                  getProductSerialNumber=barcodeResult.getProductSerialNumber;
                                  getCONTNO=barcodeResult.getCONTNO;
                                  getRefNo=barcodeResult.getRefNo;

                                 getContractReferenceNo=barcodeResult.getContractReferenceNo;
                                 getReceiptCode=barcodeResult.getReceiptCode;
                                  getReceiptID=barcodeResult.getReceiptID;



                        Log.e("select_baecode", String.valueOf(select_baecode));
                        if(BHPreference.IsSaleForCRD()) {
                                productInfo = getProductStockSerialNumberForCRD(barcodeResult.getProductID, BHPreference.employeeID());
                                BAR=barcodeResult.getProductID;

                        } else {

                                productInfo = getProductStockSerialNumber(barcodeResult.getProductID);
                                BAR=barcodeResult.getProductID;

                        }
                    }

                    @Override
                    protected void after() {
                        // TODO Auto-generated method


                        UpdateProductStockStatus("-");

                    }

                }).start();
            }
            // @Override
            // public String onNextClick() {
            // return "SA10578236";
            // }
        });
        fm.setTitle(R.string.title_sales);
     //   fm.setViewTitle("บันทึกข้อมูลสินค้า");
        showNextView(fm);
    }

    public void UpdateProductStockStatus(final String barcode) {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            @Override
            protected void before() {
                // TODO Auto-generated method stub


/*
                productInfo.ProductSerialNumber = "1234";
                productInfo.OrganizationCode = BHPreference.organizationCode();
                productInfo.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.teamCode();
               productInfo.ScanDate = new Date();
*/


               // productInfo.
            }

            ;

            @Override
            protected void calling() {
                // TODO Auto-generated method stub

                //updateProductStock(productInfo, true);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                super.after();

           //     BHPreference.setProductSerialNumber(productInfo.ProductSerialNumber);
              //  BHPreference.setProductSerialNumber2(barcode2);


                SaleProductCheckFragment_preorder2.Data data = new SaleProductCheckFragment_preorder2.Data();
                data.getProductID = getProductID;
                data.getProductName = getProductName;
                data.getProductSerialNumber=getProductSerialNumber;
                data.getCONTNO = getCONTNO;
                data.getRefNo = getRefNo;

                data.getContractReferenceNo=getContractReferenceNo;
                data.getReceiptCode = getReceiptCode;
                data.getReceiptID = getReceiptID;







  /*              BHPreference.setProductSerialNumber2(productInfo2.ProductSerialNumber);
                SaleProductCheckFragment.Data2 data2 = new SaleProductCheckFragment.Data2();
                data2.productSerialNumber = productInfo2.ProductSerialNumber;
*/

                //    data.
                SaleProductCheckFragment_preorder2 fm = BHFragment.newInstance(SaleProductCheckFragment_preorder2.class, data);
                showNextView(fm);
            }
        }).start();
    }
}
