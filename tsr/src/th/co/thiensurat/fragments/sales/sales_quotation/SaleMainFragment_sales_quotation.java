package th.co.thiensurat.fragments.sales.sales_quotation;

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
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.FortnightController;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.sales.SaleMoreDetailAddress;
import th.co.thiensurat.fragments.sales.preorder.SaleProductCheckFragment_preorder2;
import th.co.thiensurat.fragments.sales.preorder.SaleScanEmployeesFragment_preorder;
import th.co.thiensurat.fragments.sales.preorder_setting.BarcodeScanFragment_preorder_setting;
import th.co.thiensurat.fragments.sales.preorder_setting.BarcodeScanFragment_preorder_setting.ScanCallBack;
import th.co.thiensurat.fragments.sales.preorder_setting.SaleMainFinishedFragment_SETTING;
import th.co.thiensurat.fragments.sales.preorder_setting.SaleMainFragment_preorder_setting2;
import th.co.thiensurat.fragments.sales.preorder_setting.SaleMainUnfinishedFragment_preorder_setting;
import th.co.thiensurat.fragments.sales.preorder_setting.SaleProductCheckFragment_preorder_setting;

import static th.co.thiensurat.fragments.sales.preorder_setting.BarcodeScanFragment_preorder_setting.barcode2;
import static th.co.thiensurat.fragments.sales.preorder_setting.BarcodeScanFragment_preorder_setting.barcode3;
import static th.co.thiensurat.fragments.sales.preorder_setting.BarcodeScanFragment_preorder_setting.oncick;
import static th.co.thiensurat.fragments.sales.preorder_setting.BarcodeScanFragment_preorder_setting.select_baecode;

public class SaleMainFragment_sales_quotation extends BHFragment {

    private ProductStockInfo productInfo;

    static public String status = "";

    @InjectView
    private ViewPager vpSaleMain;
    @InjectView
    private TabPageIndicator tabSaleMain;
    private String title;
    private String message;
    String BAR="";

    public static int select_page_s=0;

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_add_sales_quotation};
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_main;
    }

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_sales_quotation;
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
            private final String[] PAGE_TITLE = {"รายการ", "รออนุมัติ", "อนุมัติแล้ว", "สำเร็จ"};

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
                        return BHFragment.newInstance(SaleMainUnfinishedFragment_sale_Q.class);
                    //return BHFragment.newInstance(SaleMainFragment_preorder_setting2.class);


                    case 1:
                        return BHFragment.newInstance(SaleMainFinishedFragment_SETTING.class);

                    case 2:
                        return BHFragment.newInstance(SaleMainFinishedFragment_SETTING.class);

                    case 3:
                        return BHFragment.newInstance(SaleMainFinishedFragment_SETTING.class);

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
            case R.string.button_add_sales_quotation:
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


                select_page_s=1;

/*
                BHPreference.setRefNo("");
                // ใช้งานจริงเปิด Method BarcodeScan แล้วปิด บรรทัดที่ 113-119
                BarcodeScan();*/


                /*SaleScanEmployeesFragment_sale_Q fm =
                 BHFragment.newInstance(SaleScanEmployeesFragment_sale_Q.class);
                 showNextView(fm);*/

                addContractDB();

                break;
            default:
                break;
        }
    }

    public  void BarcodeScan() {
        // TODO Auto-generated method stub
        BarcodeScanFragment_preorder_setting fm = BHFragment.newInstance(BarcodeScanFragment_preorder_setting.class, new ScanCallBack() {

            @Override
            public void onResult(BHParcelable data) {
                // TODO Auto-generated method stub
                final BarcodeScanFragment_preorder_setting.Result barcodeResult = (BarcodeScanFragment_preorder_setting.Result) data;

                (new BackgroundProcess(activity) {
                    @Override
                    protected void calling() {
                        // TODO Auto-generated method stub



                        Log.e("select_baecode", String.valueOf(select_baecode));
                        if(BHPreference.IsSaleForCRD()) {
                            //if(select_baecode==0){
                                productInfo = getProductStockSerialNumberForCRD(barcodeResult.barcode, BHPreference.employeeID());
                                BAR=barcodeResult.barcode;

                        } else {

                            //if(select_baecode==0){
                                productInfo = getProductStockSerialNumber(barcodeResult.barcode);
                                BAR=barcodeResult.barcode;

                          //  productInfo2 =barcodeResult.barcode2;
                        }
                        /*** [END] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย  ***/
                    }


                    @Override
                    protected void after() {
                        // TODO Auto-generated method

                            if (productInfo != null) {
                                 status = productInfo.Status;


                                switch (status) {
                                    case "CHECKED":

                                        if(oncick==1){
                                            UpdateProductStockStatus(barcodeResult.barcode);
                                            Log.e("test","1111");

                                        }



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


                            }




                        else {
                            title = "กรุณาตรวจสอบสินค้า";
                            message = String.format("ไม่พบรหัสสินค้า  %s อยู่ในระบบ", BAR);
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

    public void UpdateProductStockStatus(final String barcode) {
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


                SaleProductCheckFragment_preorder_setting.Data data = new SaleProductCheckFragment_preorder_setting.Data();
                data.productID = productInfo.ProductID;
                data.productSerialNumber = productInfo.ProductSerialNumber;
                data.productSerialNumber2 = barcode2;
                data.productSerialNumber3 = barcode3;





  /*              BHPreference.setProductSerialNumber2(productInfo2.ProductSerialNumber);
                SaleProductCheckFragment.Data2 data2 = new SaleProductCheckFragment.Data2();
                data2.productSerialNumber = productInfo2.ProductSerialNumber;
*/

                //    data.
                SaleProductCheckFragment_preorder_setting fm = BHFragment.newInstance(SaleProductCheckFragment_preorder_setting.class, data);
                showNextView(fm);
            }
        }).start();
    }



















    private ContractInfo contract = null;


    private void addContractDB() {

        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            ContractInfo cont;
            ProductStockInfo productStock = null;

            @Override
            protected void before() {
                // TODO Auto-generated method stub

            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                String productStockStatus = "";
                FortnightInfo fort = new FortnightController().getCurrentFortnightInfo();
                String fortnightID =  (fort != null) ? fort.FortnightID : "";

                if (contract == null) {
                    cont = new ContractInfo();
                    cont.RefNo = DatabaseHelper.getUUID();
                    BHPreference.setRefNo(cont.RefNo);


                    cont.CONTNO = "-";

                    cont.CustomerID = "";
                    cont.OrganizationCode = BHPreference.organizationCode();
                    cont.STATUS = ContractInfo.ContractStatus.DRAFT.toString();
                    cont.StatusCode = "";
                    cont.SALES = 0;
                    cont.TotalPrice = 0;
                    cont.EFFDATE = new Date();
                    cont.HasTradeIn = 0;
                    cont.TradeInProductCode = "";
                    //  cont.TradeInProductCode = data.productSerialNumber2;   // ทดสอบ ส่งค่า เข้าฐานข้อมูล
                    cont.TradeInBrandCode = "";
                    cont.TradeInProductModel = "";
                    cont.TradeInDiscount = 0;
                    cont.PreSaleSaleCode = "";
                    cont.PreSaleEmployeeCode = "";
                    cont.PreSaleTeamCode = "";
                    cont.SaleCode = BHPreference.employeeID();
                    cont.SaleEmployeeCode = BHPreference.employeeID();
                    cont.SaleTeamCode = BHPreference.teamCode();
                    cont.InstallerSaleCode = "";
                    cont.InstallerEmployeeCode = "";
                    cont.InstallerTeamCode = "";
                    cont.InstallDate = new Date();
                    cont.ProductSerialNumber = "";
                    cont.ProductID = "";

                    //-- Fixed - [BHPROJ-0024-829] :: ระบบไม่แสดงรายละเอียดการซื้อและสินค้าที่ยังไม่ได้ออกสัญญาไม่พบในรายการขายคงค้าง
                    cont.SaleEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                    cont.MODE = 0;
                    cont.FortnightID = fortnightID;
                    cont.ProblemID =  "";   // ทดสอบ ส่งค่า เข้าฐานข้อมูล
                    cont.svcontno = "";
                    cont.isActive = true;
                    cont.MODEL = "";
                    cont.fromrefno = "";
                    cont.fromcontno = "";
                    cont.CreateDate = new Date();
                    cont.CreateBy = BHPreference.employeeID();//BHPreference.userID();
                    cont.LastUpdateDate = new Date();
                    cont.LastUpdateBy = BHPreference.employeeID();//BHPreference.userID();
                    cont.SyncedDate = new Date();
                    cont.PreSaleEmployeeName = "";
                    cont.SaleSubTeamCode = BHPreference.SubTeamCode();
                    //-- Fixed - [BHPROJ-0016-993] :: เพิ่ม Field เพื่อเก็บค่า RefNo ของข้อมูลที่มาจากระบบเก่าตอนทำ Migrate และยังเก็บค่าเลขที่อ้างอิง/เลขที่เอกสารสัญญามือที่เกิดคู่กับสัญญาฉบับนั้น ๆ ในกรณีที่เป็นสัญญาที่เกิดจากระบบ TSR Application
                    //  cont.ContractReferenceNo = cont.RefNo;
                    cont.CONTNO = "-";


                    //   showMessage(BHPreference.RefNo());
                    addContract(cont, true);

                    productStockStatus = ProductStockController.ProductStockStatus.SOLD.toString();
                   // log("Change Product Status of " + data.getProductSerialNumber + " to SOLD");
                } else {

                    productStock = getProductStock("");
                    if (productStock != null) {
                        // productStock = new ProductStockInfo();

                        productStockStatus = ProductStockController.ProductStockStatus.CHECKED.toString();
                        //productStock.ProductSerialNumber = data.productSerialNumber; // contract.ProductSerialNumber;
                        productStock.OrganizationCode = BHPreference.organizationCode();
                        productStock.Status = productStockStatus;
                        updateProductStock(productStock,true);
                    }

                    //contract.ProductSerialNumber ="";
                    //updateContract(contract, true);

                    productStockStatus = ProductStockController.ProductStockStatus.SOLD.toString();
                  //  log("Change Product Status of " + contract.ProductSerialNumber + " to CHECK");
                }

                productStock = getProductStock("");
                if (productStock != null) {
                    // productStock = new ProductStockInfo();
                    productStock.ProductSerialNumber = ""; // contract.ProductSerialNumber;
                    productStock.OrganizationCode = BHPreference.organizationCode();
                    productStock.Status = productStockStatus;
                    updateProductStock(productStock,true);
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                showNextView(new SaleScanEmployeesFragment_sale_Q());
            }
        }).start();
    }
}
