package th.co.thiensurat.fragments.sales;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viewpagerindicator.TabPageIndicator;

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
import th.co.thiensurat.fragments.share.BarcodeScanFragment;
import th.co.thiensurat.fragments.share.BarcodeScanFragment.ScanCallBack;

import static th.co.thiensurat.fragments.share.BarcodeScanFragment.barcode2;
import static th.co.thiensurat.fragments.share.BarcodeScanFragment.barcode3;

public class SaleMainFragment extends BHFragment {

    private ProductStockInfo productInfo,productInfo2;


    @InjectView
    private ViewPager vpSaleMain;
    @InjectView
    private TabPageIndicator tabSaleMain;
    private String title;
    private String message;

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
            private final String[] PAGE_TITLE = {"รายการขายคงค้าง", "รายการขายเสร็จสมบูรณ์"};

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
                        return BHFragment.newInstance(SaleMainUnfinishedFragment.class);

                    case 1:
                        return BHFragment.newInstance(SaleMainFinishedFragment.class);

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

    private void BarcodeScan() {
        // TODO Auto-generated method stub
        BarcodeScanFragment fm = BHFragment.newInstance(BarcodeScanFragment.class, new ScanCallBack() {

            @Override
            public void onResult(BHParcelable data) {
                // TODO Auto-generated method stub
                final BarcodeScanFragment.Result barcodeResult = (BarcodeScanFragment.Result) data;
               // final BarcodeScanFragment.Result2 barcodeResult2 = (BarcodeScanFragment.Result2) data;

                (new BackgroundProcess(activity) {
                    @Override
                    protected void calling() {
                        // TODO Auto-generated method stub

                        /*** [START] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย ***/
                        //productInfo = getProductStockSerialNumber(barcodeResult.barcode);

                        if(BHPreference.IsSaleForCRD()) {
                            productInfo = getProductStockSerialNumberForCRD(barcodeResult.barcode, BHPreference.employeeID());
                        } else {
                            productInfo = getProductStockSerialNumber(barcodeResult.barcode);
                          //  productInfo2 =barcodeResult.barcode2;
                        }
                        /*** [END] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย  ***/
                    }

                    // @Override
                    // protected void after() {
                    // // TODO Auto-generated method stub
                    // title = "กรุณาตรวจสอบสินค้า";
                    // message = String.format("รหัสสินค้า %s สถานะ  %s",
                    // productInfo.ProductSerialNumber, productInfo.Status);
                    // showDialog(title, message);
                    // }

                    @Override
                    protected void after() {
                        // TODO Auto-generated method
                        if (productInfo != null) {
                            String status = productInfo.Status;


                                        switch (status) {
                                            case "CHECKED":

                                                    UpdateProductStockStatus(barcodeResult.barcode);


                                                break;
                                            case "OVER":
                                                title = "กรุณาตรวจสอบสินค้า";
                                                message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "สินค้าเกิน");
                                                showWarningDialog(title, message);
                                                break;
                                            case "RETURN":
                                                title = "กรุณาตรวจสอบสินค้า";
                                                message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "สินค้ารีเทิร์น");
                                                showWarningDialog(title, message);
                                                break;
                                            case "SOLD":
                                                title = "กรุณาตรวจสอบสินค้า";
                                                message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "สินค้าถูกขาย");
                                                showWarningDialog(title, message);
                                                break;
                                            case "WAIT":
                                                title = "กรุณาตรวจสอบสินค้า";
                                                message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "สินค้ารอการตรวจสอบ");
                                                showWarningDialog(title, message);
                                                break;
                                            case "DAMAGE":
                                                title = "กรุณาตรวจสอบสินค้า";
                                                message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "เครื่องชำรุด");
                                                showWarningDialog(title, message);
                                                break;
                                            case "TEAM_DESTROY":
                                                title = "กรุณาตรวจสอบสินค้า";
                                                message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "ถูกยุบทีมขาย");
                                                showWarningDialog(title, message);
                                                break;
                                            case "WAIT_RETURN":
                                                title = "กรุณาตรวจสอบสินค้า";
                                                message = String.format("รหัสสินค้า %s สถานะ  %s", productInfo.ProductSerialNumber, "สินค้านี้ถูกส่งคืนเข้าระบบ");
                                                showWarningDialog(title, message);
                                                break;
                                            default:
                                                break;
                                        }


                        } else {
                            title = "กรุณาตรวจสอบสินค้า";
                            message = String.format("ไม่พบรหัสสินค้า  %s อยู่ในระบบ", barcodeResult.barcode);
                            showWarningDialog(title, message);
                        }
                    }

                }).start();
            }
            // @Override
            // public String onNextClick() {
            // return "SA10578236";
            // }
        });
        fm.setTitle(R.string.title_sales);
        fm.setViewTitle("บันทึกข้อมูลสินค้า");
        showNextView(fm);
    }

    private void UpdateProductStockStatus(final String barcode) {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            @Override
            protected void before() {
                // TODO Auto-generated method stub
                productInfo.ProductSerialNumber = barcode;
                productInfo.OrganizationCode = BHPreference.organizationCode();
                productInfo.ScanByTeam = BHPreference.selectTeamCodeOrSubTeamCode();//BHPreference.teamCode();
                productInfo.ScanDate = new Date();
               // productInfo.
            }

            ;

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                updateProductStock(productInfo, true);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                super.after();

                BHPreference.setProductSerialNumber(productInfo.ProductSerialNumber);
              //  BHPreference.setProductSerialNumber2(barcode2);


                SaleProductCheckFragment.Data data = new SaleProductCheckFragment.Data();
                data.productID = productInfo.ProductID;
                data.productSerialNumber = productInfo.ProductSerialNumber;
                data.productSerialNumber2 = barcode2;
                data.productSerialNumber3 = barcode3;





  /*              BHPreference.setProductSerialNumber2(productInfo2.ProductSerialNumber);
                SaleProductCheckFragment.Data2 data2 = new SaleProductCheckFragment.Data2();
                data2.productSerialNumber = productInfo2.ProductSerialNumber;
*/

                //    data.
                SaleProductCheckFragment fm = BHFragment.newInstance(SaleProductCheckFragment.class, data);
                showNextView(fm);
            }
        }).start();
    }
}
