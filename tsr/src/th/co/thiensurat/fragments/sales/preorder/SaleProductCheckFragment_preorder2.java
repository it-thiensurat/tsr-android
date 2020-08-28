package th.co.thiensurat.fragments.sales.preorder;

import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.FortnightController;
import th.co.thiensurat.data.controller.ProductStockController.ProductStockStatus;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatus;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.ProductInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.sales.SaleScanEmployeesFragment;

import static th.co.thiensurat.fragments.share.BarcodeScanFragment.check_scan2;
import static th.co.thiensurat.fragments.share.BarcodeScanFragment.oncick;

public class SaleProductCheckFragment_preorder2 extends BHFragment {
    private String STATUS_CODE = "01";
    private ContractInfo contract = null;

    String productSerialNumber_all="";
    public static class Data extends BHParcelable {
        public String getProductID;
        public String getProductName;
        public String getProductSerialNumber;
        public String getCONTNO;
        public String getRefNo;
        public String getContractReferenceNo;
        public String getReceiptCode;
        public String getReceiptID;
    }

    public static class Data2 extends BHParcelable {
        public String productSerialNumber;

    }

    private Data data;
    @InjectView
    private TextView textShowScan;
    @InjectView
    private TextView textShowType;
    @InjectView
    private LinearLayout linearLayoutHeadNumber;
    @InjectView
    private TextView txtNumber1;
    @InjectView
    private TextView txtNumber2;
    @InjectView
    private TextView txtNumber3;
    @InjectView
    private TextView txtNumber4;
    @InjectView
    private TextView txtNumber5;

    protected ProductInfo product;

    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.title_sales_preorder;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_productcheck;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[] { R.string.button_back, R.string.button_save_emp };
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        oncick=0;

        if (BHPreference.ProcessType().equals(ProcessType.Sale.toString())) {
            txtNumber1.setBackgroundResource(R.drawable.circle_number_sale_color_red);
        }

        data = getData();
        ContactDB();
    }

    private void ContactDB() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                // contract =
                // getContractByRefNo(BHPreference.organizationCode(),
                // BHPreference.RefNo());
                contract = getContract(BHPreference.RefNo());
                product = getProductInfo(BHPreference.organizationCode(), data.getProductID);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                // if (contract == null) {
                // Toast.makeText(activity, "contract is null",
                // Toast.LENGTH_SHORT).show();
                // } else {
                // Toast.makeText(activity, "contract is not null",
                // Toast.LENGTH_SHORT).show();
                // }


                textShowScan.setText("-");
                //if (product != null) {
                textShowType.setText(data.getProductName);




            }
        }).start();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_save_emp:
                addContractDB();
                break;
            case R.string.button_back:
                showLastView();
                break;
            default:
                break;
        }
    }

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

                   // cont.CONTNO = cont.RefNo;
                    cont.CONTNO = data.getContractReferenceNo;


                    cont.CustomerID = "";
                    cont.OrganizationCode = BHPreference.organizationCode();
                    cont.STATUS = ContractStatus.DRAFT.toString();
                    cont.StatusCode = STATUS_CODE;
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
                    cont.ProductSerialNumber = data.getProductSerialNumber;
                    cont.ProductID = data.getProductID;

                    //-- Fixed - [BHPROJ-0024-829] :: ระบบไม่แสดงรายละเอียดการซื้อและสินค้าที่ยังไม่ได้ออกสัญญาไม่พบในรายการขายคงค้าง
                    cont.SaleEmployeeLevelPath = BHPreference.currentTreeHistoryID();

                    cont.MODE = 0;
                    cont.FortnightID = fortnightID;
                    cont.ProblemID =  productSerialNumber_all;   // ทดสอบ ส่งค่า เข้าฐานข้อมูล
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
                    cont.CONTNO = data.getContractReferenceNo;


                    //   showMessage(BHPreference.RefNo());
                    addContract(cont, true);

                    productStockStatus = ProductStockStatus.SOLD.toString();
                    log("Change Product Status of " + data.getProductSerialNumber + " to SOLD");
                } else {
                	
                	productStock = getProductStock(contract.ProductSerialNumber);
                	   if (productStock != null) {
                           // productStock = new ProductStockInfo();
                		   
                		   productStockStatus = ProductStockStatus.CHECKED.toString();
                           //productStock.ProductSerialNumber = data.productSerialNumber; // contract.ProductSerialNumber;
                           productStock.OrganizationCode = BHPreference.organizationCode();
                           productStock.Status = productStockStatus;
                           updateProductStock(productStock,true);
                       }
                	
                    contract.ProductSerialNumber = data.getProductSerialNumber;
                    updateContract(contract, true);

                    productStockStatus = ProductStockStatus.SOLD.toString();
                    log("Change Product Status of " + contract.ProductSerialNumber + " to CHECK");
                }

                productStock = getProductStock(data.getProductSerialNumber);
                if (productStock != null) {
                    // productStock = new ProductStockInfo();
                    productStock.ProductSerialNumber = data.getProductSerialNumber; // contract.ProductSerialNumber;
                    productStock.OrganizationCode = BHPreference.organizationCode();
                    productStock.Status = productStockStatus;
                    updateProductStock(productStock,true);
                }
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                showNextView(new SaleScanEmployeesFragment_preorder());
            }
        }).start();
    }
}
