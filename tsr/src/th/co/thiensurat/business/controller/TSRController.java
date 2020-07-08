package th.co.thiensurat.business.controller;

import android.database.sqlite.SQLiteDatabase;
import android.util.Base64;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHStorage;
import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.data.controller.AddressController;
import th.co.thiensurat.data.controller.AssignController;
import th.co.thiensurat.data.controller.BankController;
import th.co.thiensurat.data.controller.CareerController;
import th.co.thiensurat.data.controller.ChangeContractController;
import th.co.thiensurat.data.controller.ChangeContractController.ChangeContractStatus;
import th.co.thiensurat.data.controller.ChangePartSpareController;
import th.co.thiensurat.data.controller.ChangePartSpareDetailController;
import th.co.thiensurat.data.controller.ChangeProductController;
import th.co.thiensurat.data.controller.ChangeProductController.ChangeProductStatus;
import th.co.thiensurat.data.controller.ChannelController;
import th.co.thiensurat.data.controller.ComplainController;
import th.co.thiensurat.data.controller.ContractCloseAccountController;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.controller.CutDivisorContractController;
import th.co.thiensurat.data.controller.CutOffContractController;
import th.co.thiensurat.data.controller.DamageProductController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DatabaseManager;
import th.co.thiensurat.data.controller.DebtorCustomerController;
import th.co.thiensurat.data.controller.DiscountLimitController;
import th.co.thiensurat.data.controller.DistrictController;
import th.co.thiensurat.data.controller.DocumentHistoryController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.EmployeeDetailController;
import th.co.thiensurat.data.controller.FortnightController;
import th.co.thiensurat.data.controller.HabitatTypeController;
import th.co.thiensurat.data.controller.HobbyController;
import th.co.thiensurat.data.controller.ImpoundProductController;
import th.co.thiensurat.data.controller.ImpoundProductController.ImpoundProductStatus;
import th.co.thiensurat.data.controller.LogScanProductSerialController;
import th.co.thiensurat.data.controller.ManualDocumentController;
import th.co.thiensurat.data.controller.ManualDocumentWithdrawalController;
import th.co.thiensurat.data.controller.PackageController;
import th.co.thiensurat.data.controller.PackagePeriodDetailController;
import th.co.thiensurat.data.controller.PartSpareStockOnHandController;
import th.co.thiensurat.data.controller.PaymentAppointmentController;
import th.co.thiensurat.data.controller.PaymentController;
import th.co.thiensurat.data.controller.PrefixController;
import th.co.thiensurat.data.controller.ProblemContractController;
import th.co.thiensurat.data.controller.ProblemController;
import th.co.thiensurat.data.controller.ProductController;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.controller.ProductStockController.ProductStockStatus;
import th.co.thiensurat.data.controller.ProvinceController;
import th.co.thiensurat.data.controller.ReceiptController;
import th.co.thiensurat.data.controller.ReportApprovedController;
import th.co.thiensurat.data.controller.ReportController;
import th.co.thiensurat.data.controller.ReportCustomerProblemController;
import th.co.thiensurat.data.controller.ReportDailySummaryController;
import th.co.thiensurat.data.controller.ReportFirstAndNextReceiveController;
import th.co.thiensurat.data.controller.ReportInstallAndPaymentController;
import th.co.thiensurat.data.controller.ReportInventoryController;
import th.co.thiensurat.data.controller.ReportSaleAndDriverController;
import th.co.thiensurat.data.controller.RequestNextPaymentController;
import th.co.thiensurat.data.controller.ReturnProductController;
import th.co.thiensurat.data.controller.SaleAuditController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.controller.SalePaymentPeriodPaymentController;
import th.co.thiensurat.data.controller.SendDocumentController;
import th.co.thiensurat.data.controller.SendDocumentDetailController;
import th.co.thiensurat.data.controller.SendMoneyController;
import th.co.thiensurat.data.controller.SpareDrawdownController;
import th.co.thiensurat.data.controller.SpareDrawdownDetailController;
import th.co.thiensurat.data.controller.SubDistrictController;
import th.co.thiensurat.data.controller.SubTeamController;
import th.co.thiensurat.data.controller.SuggestionController;
import th.co.thiensurat.data.controller.TeamController;
import th.co.thiensurat.data.controller.TradeInBrandController;
import th.co.thiensurat.data.controller.TransactionLogController;
import th.co.thiensurat.data.controller.TransactionLogSkipController;
import th.co.thiensurat.data.controller.TripController;
import th.co.thiensurat.data.controller.UserController;
import th.co.thiensurat.data.controller.WriteOffNPLController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.BankInfo;
import th.co.thiensurat.data.info.CareerInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ChangePartSpareDetailInfo;
import th.co.thiensurat.data.info.ChangePartSpareInfo;
import th.co.thiensurat.data.info.ChangeProductInfo;
import th.co.thiensurat.data.info.ChannelItemInfo;
import th.co.thiensurat.data.info.ComplainInfo;
import th.co.thiensurat.data.info.ContractCloseAccountInfo;
import th.co.thiensurat.data.info.ContractImageInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatus;
import th.co.thiensurat.data.info.ContractListInfo;
import th.co.thiensurat.data.info.CutDivisorContractInfo;
import th.co.thiensurat.data.info.CutOffContractInfo;
import th.co.thiensurat.data.info.DamageProductInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.DiscountLimitInfo;
import th.co.thiensurat.data.info.DistrictInfo;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.EmployeeDetailInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.FortnightInfo;
import th.co.thiensurat.data.info.HabitatTypeInfo;
import th.co.thiensurat.data.info.HobbyInfo;
import th.co.thiensurat.data.info.ImpoundProductInfo;
import th.co.thiensurat.data.info.LogScanProductSerialInfo;
import th.co.thiensurat.data.info.ManualDocumentInfo;
import th.co.thiensurat.data.info.ManualDocumentWithdrawalInfo;
import th.co.thiensurat.data.info.PackageInfo;
import th.co.thiensurat.data.info.PackagePeriodDetailInfo;
import th.co.thiensurat.data.info.PartSpareStockOnHandInfo;
import th.co.thiensurat.data.info.PaymentAppointmentInfo;
import th.co.thiensurat.data.info.PaymentDataInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.PrefixInfo;
import th.co.thiensurat.data.info.ProblemContractInfo;
import th.co.thiensurat.data.info.ProblemInfo;
import th.co.thiensurat.data.info.ProductInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.data.info.ProvinceInfo;
import th.co.thiensurat.data.info.ReceiptInfo;
import th.co.thiensurat.data.info.ReportApprovedInfo;
import th.co.thiensurat.data.info.ReportCustomerProblemInfo;
import th.co.thiensurat.data.info.ReportDailyChangeContTranInfo;
import th.co.thiensurat.data.info.ReportDailyChangeProTranInfo;
import th.co.thiensurat.data.info.ReportDailyImpoundTranInfo;
import th.co.thiensurat.data.info.ReportDailySalesTranInfo;
import th.co.thiensurat.data.info.ReportDailySendDocumentTranInfo;
import th.co.thiensurat.data.info.ReportDailySendMoneyTranInfo;
import th.co.thiensurat.data.info.ReportDailySummaryInfo;
import th.co.thiensurat.data.info.ReportDailyWriteOffNPLTranInfo;
import th.co.thiensurat.data.info.ReportFirstAndNextReceiveInfo;
import th.co.thiensurat.data.info.ReportInstallAndPaymentInfo;
import th.co.thiensurat.data.info.ReportInventoryInfo;
import th.co.thiensurat.data.info.ReportSaleAndDriverInfo;
import th.co.thiensurat.data.info.ReportSummaryChangeContractInfo;
import th.co.thiensurat.data.info.ReportSummaryChangeProductInfo;
import th.co.thiensurat.data.info.ReportSummaryImpoundProductInfo;
import th.co.thiensurat.data.info.ReportSummaryRangOfFirstReceiveInfo;
import th.co.thiensurat.data.info.ReportSummaryTradeProductNotReceiveInfo;
import th.co.thiensurat.data.info.ReportSummaryTradeProductReceiveInfo;
import th.co.thiensurat.data.info.ReportSummaryWriteOffNPLInfo;
import th.co.thiensurat.data.info.RequestNextPaymentInfo;
import th.co.thiensurat.data.info.ReturnProductDetailInfo;
import th.co.thiensurat.data.info.ReturnProductInfo;
import th.co.thiensurat.data.info.SaleAuditInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodPaymentInfo;
import th.co.thiensurat.data.info.SendDocumentDetailInfo;
import th.co.thiensurat.data.info.SendDocumentInfo;
import th.co.thiensurat.data.info.SendMoneyInfo;
import th.co.thiensurat.data.info.SpareDrawdownDetailInfo;
import th.co.thiensurat.data.info.SpareDrawdownInfo;
import th.co.thiensurat.data.info.SubDistrictInfo;
import th.co.thiensurat.data.info.SubTeamInfo;
import th.co.thiensurat.data.info.SuggestionInfo;
import th.co.thiensurat.data.info.TeamInfo;
import th.co.thiensurat.data.info.TradeInBrandInfo;
import th.co.thiensurat.data.info.TransactionLogInfo;
import th.co.thiensurat.data.info.TransactionLogSkipInfo;
import th.co.thiensurat.data.info.TripInfo;
import th.co.thiensurat.data.info.WriteOffNPLInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.service.SynchronizeService;
import th.co.thiensurat.service.TSRService;
import th.co.thiensurat.service.TransactionService;
import th.co.thiensurat.service.data.*;

import static java.util.Calendar.getInstance;

public class TSRController {

    private static void processTransaction(Runnable process) {
        if (process == null) return;

        File file = new File(DatabaseManager.getInstance().getDatabasePath());
        if(!file.exists()){
            process.run();
            return;
        }

        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
        try {
            db.beginTransaction();

            process.run();

            db.setTransactionSuccessful();
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            throw e;
        } finally {
            db.endTransaction();
            DatabaseManager.getInstance().closeDatabase();
        }
    }

    public static AddressInfo getAddressByRefNoByAddressTypeCode(String refNo, String addressTypeCode) {
        return new AddressController().getAddressByRefNoByAddressTypeCode(refNo, addressTypeCode);
    }

    public static List<EmployeeInfo> getAllTeamMember(String organizationCode, String teamCode, String subTeamCode) {
        return new EmployeeController().getAllTeamMember(organizationCode, teamCode, subTeamCode);
    }

    public static List<ReportSaleAndDriverInfo> getReportByFortnight(String teamcode, String fortnight) {
        return new ReportSaleAndDriverController().getReportByFortnight(teamcode, fortnight);
    }

    /***
     * [START] :: Fixed - [BHPROJ-0016-769] ปรับปรุง Process การ Login ผ่านระบบ Android
     ***/
    public static AuthenticateOutputInfo authenticate(AuthenticateInputInfo info) {
        return TSRService.authenticate(info, false);
    }

    public static IsUserLoggedInOutputInfo isUserLoggedIn(IsUserLoggedInInputInfo info) {
        return TSRService.isUserLoggedIn(info, false);
    }

    public static UpdateUserLoggedInOutputInfo updateUserLoggedIn(UpdateUserLoggedInInputInfo info) {
        return TSRService.updateUserLoggedIn(info, false);
    }

    /***
     * [END] :: Fixed - [BHPROJ-0016-769] ปรับปรุง Process การ Login ผ่านระบบ Android
     ***/

    public static AuthenticateOutputInfo logout(AuthenticateInputInfo info) {
        return new UserController().logout(info);
    }

    public static GetUserByUserNameOutputInfo getUserByUserName(GetUserByUserNameInputInfo info) {
        return TSRService.getUserByUserName(info, false);
    }

    public static List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByContractTeam(String organizationCode, String saleTeamCode) {
        return new SalePaymentPeriodController().getNextSalePaymentPeriodByContractTeam(organizationCode, saleTeamCode);
    }

    public static List<PackageInfo> getPackage() {
        return new PackageController().getPackage();
    }

    public static List<ContractInfo> getContract() {
        return new ContractController().getContract();
    }

    public static ContractInfo getContractByRefNo(String organizationCode, String refNo) {
        return new ContractController().getContractByRefNo(organizationCode, refNo);
    }

    public static ContractInfo getContractByRefNoForCredit(String organizationCode, String refNo) {
        return new ContractController().getContractByRefNoForCredit(organizationCode, refNo);
    }

    public static ContractInfo getContractByRefNoForSendDocuments(String organizationCode, String refNo) {
        return new ContractController().getContractByRefNoForSendDocuments(organizationCode, refNo);
    }

    public static ContractInfo getContractByRefNoForChangeContract(String organizationCode, String refNo) {
        return new ContractController().getContractByRefNoForChangeContract(organizationCode, refNo);
    }

    public static ContractInfo getContractByRefNoForManualDocument(String organizationCode, String refNo) {
        return new ContractController().getContractByRefNoForManualDocument(organizationCode, refNo);
    }

    public static ContractInfo getContractByRefNoByPaymentPeriodNumber(String organizationCode, String refNO, String paymentPeriodNumber) {
        return new ContractController().getContractByRefNoByPaymentPeriodNumber(organizationCode, refNO, paymentPeriodNumber);
    }

    public static ContractInfo getContractByRefNoByPaymentPeriodNumberNotTeam(String organizationCode, String refNO, String paymentPeriodNumber) {
        return new ContractController().getContractByRefNoByPaymentPeriodNumberNotTeam(organizationCode, refNO, paymentPeriodNumber);
    }

    public static ContractInfo getContractByIDCard(String idCard) {
        return new ContractController().getContractByIDCard(idCard);
    }

    public static List<AddressInfo> getAddressByRefNo(String refNo) {
        return new AddressController().getAddressByRefNo(refNo);
    }

    public static List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByContractTeamGroupByAssignee(String organizationCode, String saleTeamCode) {
        return new SalePaymentPeriodController().getNextSalePaymentPeriodByContractTeamGroupByAssignee(organizationCode, saleTeamCode);
    }

    public static List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByContractTeamGroupByAppointmentDate(String organizationCode, String saleTeamCode, String empID) {
        return new SalePaymentPeriodController().getNextSalePaymentPeriodByContractTeamGroupByAppointmentDate(organizationCode, saleTeamCode, empID);
    }

    public static List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByAssigneeEmpID(String organizationCode, String saleTeamCode, String assigneeEmpID) {
        return new SalePaymentPeriodController().getNextSalePaymentPeriodByAssigneeEmpID(organizationCode, saleTeamCode, assigneeEmpID);
    }

    public static List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByContractTeamByAppointmentDate(String organizationCode, String assigneeTeamCode, Date paymentAppointmentDate) {
        return new SalePaymentPeriodController().getNextSalePaymentPeriodByContractTeamByAppointmentDate(
                organizationCode, assigneeTeamCode, paymentAppointmentDate);
    }

    public static ProductStockInfo getProductStock(String productSerialNumber) {
        return new ProductStockController().getProductStock(productSerialNumber);
    }

    public static ProductStockInfo getProductStockForCRD(String productSerialNumber, String EmployeeID) {
        return new ProductStockController().getProductStockForCRD(productSerialNumber, EmployeeID);
    }

    public static List<ProvinceInfo> getProvinces() {
        return new ProvinceController().getProvinces();
    }

    public static ProvinceInfo getProvinceByProvinceCode(String ProvincesCode) {
        return new ProvinceController().getProvinceByProvinceCode(ProvincesCode);
    }

    public static ProvinceInfo getProvinceByProvinceName(String ProvincesName) {
        return new ProvinceController().getProvinceByProvinceName(ProvincesName);
    }

    public static List<DistrictInfo> getDistrict(String ProvinceCode) {
        List<DistrictInfo> ret = null;

        ret = new DistrictController().getDistrict(ProvinceCode);
        return ret;
    }

    public static DistrictInfo getDistrictByDistrictCode(String DistrictCode) {
        return new DistrictController().getDistrictByDistrictCode(DistrictCode);
    }

    public static DistrictInfo getDistrictByProvinceCodeAndDistrictName(String ProvinceCode, String DistrictName) {
        return new DistrictController().getDistrictByProvinceCodeAndDistrictName(ProvinceCode, DistrictName);
    }

    public static List<SubDistrictInfo> getSubDistrict(String DistrictCode) {
        List<SubDistrictInfo> ret = null;
        ret = new SubDistrictController().getSubDistrict(DistrictCode);
        return ret;
    }

    public static SubDistrictInfo getSubDistrictBySubDistrictCode(String SubDistrictCode) {
        return new SubDistrictController().getSubDistrictBySubDistrictCode(SubDistrictCode);
    }

    public static SubDistrictInfo getSubDistrictByDistrictCodeAndSubDistrictName(String DistrictCode, String SubDistrictName) {
        return new SubDistrictController().getSubDistrictByDistrictCodeAndSubDistrictName(DistrictCode, SubDistrictName);
    }

    public static List<HabitatTypeInfo> getHabitatType() {
        return new HabitatTypeController().getHabitatType();
    }

    public static HabitatTypeInfo getHabitatTypeByHabitatTypeCode(String HabitatTypeCode) {
        return new HabitatTypeController().getHabitatTypeByHabitatTypeCode(HabitatTypeCode);
    }

    public static List<CareerInfo> getCareer() {
        return new CareerController().getCareer();
    }

    public static CareerInfo getCareerByCareerCode(String CareerCode) {
        return new CareerController().getCareerByCareerCode(CareerCode);
    }

    public static List<HobbyInfo> getHobby() {
        return new HobbyController().getHobby();
    }

    public static HobbyInfo getHobbyByHobbyCode(String HobbyCode) {
        return new HobbyController().getHobbyByHobbyCode(HobbyCode);
    }

    public static List<SuggestionInfo> getSuggestion() {
        return new SuggestionController().getSuggestion();
    }

    public static SuggestionInfo getSuggestionBySuggestionCode(String SuggestionCode) {
        return new SuggestionController().getSuggestionBySuggestionCode(SuggestionCode);
    }

    public static List<PrefixInfo> getPrefixes() {
        return new PrefixController().getPrefixes();
    }

    public static PrefixInfo getPrefixeByPrefixName(String prefixName) {
        return new PrefixController().getPrefixeByPrefixName(prefixName);
    }

    public static List<TradeInBrandInfo> getTradeInBrand() {
        return new TradeInBrandController().getTradeInBrand();
    }

    public static List<BankInfo> getBank() {
        return new BankController().getBank();
    }

    public static BankInfo getBankInfo(String BackCode) {
        return new BankController().getBankInfo(BackCode);
    }

    public static List<PackagePeriodDetailInfo> getPackagePeriodDetail(String organizationCode, String Model) {
        List<PackagePeriodDetailInfo> output = new PackagePeriodDetailController().getPackagePeriodDetail(organizationCode, Model);
        return output;
    }

    public static PackagePeriodDetailInfo getMaxPackagePeriodDetailByModel(String organizationCode, String model) {
        return new PackagePeriodDetailController().getMaxPackagePeriodDetailByModel(organizationCode, model);
    }

    public static List<DebtorCustomerInfo> getidcardDebcustomer(String IDCard, String personType, String personTypeCard) {
        List<DebtorCustomerInfo> output = new DebtorCustomerController().getidcardDecustomer(IDCard, personType, personTypeCard);
        return output;
    }

    public static List<DebtorCustomerInfo> getDetailDecustomer(String customerID) {
        return new DebtorCustomerController().getDetailDecustomer(customerID);
    }

    protected List<PackageInfo> getPackageByProductInfo(String organizationCode, String ProductID) {
        List<PackageInfo> output = new PackageController().getPackage(organizationCode, ProductID);
        return output;
    }

    public static void employeeImport(final String organizationCode, final String teamCode) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                new TeamController().deleteTeamAll();
                new SubTeamController().deleteSubTeamAll();
                new EmployeeController().deleteEmployeeAll();
                new EmployeeDetailController().deleteEmployeeDetailAll();

                /** (1) Team : ทีม **/
                /*
                ** Fixed - [BHPRJ00301-4084] - Android-Synchronize-Server2Local for Team/SubTeam/Employee/EmployeeDetail **
                GetTeamByIDInputInfo teamInput = new GetTeamByIDInputInfo();
                teamInput.Code = teamCode;
                GetTeamByIDOutputInfo teamOutput = TSRService.getTeamByID(teamInput, false);
                */
                GetAllTeamInputInfo teamInput = new GetAllTeamInputInfo();
                teamInput.OrganizationCode = organizationCode;
                GetAllTeamOutputInfo teamOutput = TSRService.getAllTeam(teamInput, false);
                if (teamOutput.Info != null) {
                    List<TeamInfo> teamList = teamOutput.Info;
                    for (TeamInfo item : teamList) {
                        TeamInfo t = new TeamInfo();
                        t.Code = item.Code;
                        t.OrganizationCode = item.OrganizationCode;
                        t.Name = item.Name;
                        t.NoPrint = item.NoPrint;
                        t.TeamType = item.TeamType;
                        t.SourceSystem = item.SourceSystem;
                        t.SourceSystemName = item.SourceSystemName;
                        t.TeamHeadCode = item.TeamHeadCode;
                        t.SupervisorCode = item.SupervisorCode;
                        t.SubDepartmentCode = item.SubDepartmentCode;
                        new TeamController().addTeam(t);
                    }
                }

                /** (2) SubTeam : หน่วย **/
                /*
                ** Fixed - [BHPRJ00301-4084] - Android-Synchronize-Server2Local for Team/SubTeam/Employee/EmployeeDetail **
                GetSubTeamByTeamCodeInputInfo subTeamInput = new GetSubTeamByTeamCodeInputInfo();
                subTeamInput.TeamCode = teamCode;
                GetSubTeamByTeamCodeOutputInfo subTeamOutput = TSRService.getSubTeamByTeamCode(subTeamInput, false);
                */
                GetAllSubTeamInputInfo subTeamInput = new GetAllSubTeamInputInfo();
                subTeamInput.OrganizationCode = organizationCode;
                GetAllSubTeamOutputInfo subTeamOutput = TSRService.getAllSubTeam(subTeamInput, false);
                if (subTeamOutput.Info != null) {
                    List<SubTeamInfo> subTeamList = subTeamOutput.Info;
                    for (SubTeamInfo item : subTeamList) {
                        SubTeamInfo st = new SubTeamInfo();
                        st.SubTeamCode = item.SubTeamCode;
                        st.TeamCode = item.TeamCode;
                        st.SubTeamName = item.SubTeamName;
                        st.SourceSystem = item.SourceSystem;
                        st.SourceSystemName = item.SourceSystemName;
                        new SubTeamController().addSubTeam(st);
                    }
                }

                /** (3) Employee : ข้อมูลพนักงาน + EmployeeDetail : ข้อมูลโครงสร้างพนักงาน **/
                /*
                ** Fixed - [BHPRJ00301-4084] - Android-Synchronize-Server2Local for Team/SubTeam/Employee/EmployeeDetail **
                GetAllTeamMemberInputInfo input = new GetAllTeamMemberInputInfo();
                input.OrganizationCode = organizationCode;
                input.TeamCode = teamCode;
                GetAllTeamMemberOutputInfo output = TSRService.getAllTeamMember(input, false);
                */
                GetAllEmployeeInputInfo empInput = new GetAllEmployeeInputInfo();
                empInput.OrganizationCode = organizationCode;
                GetAllEmployeeOutputInfo empOutput = TSRService.getAllEmployee(empInput, false);
                if (empOutput.Info != null) {
                    List<EmployeeInfo> employeeList = empOutput.Info;
                    List<String> empIDs = new ArrayList<String>();
                    for (EmployeeInfo item : employeeList) {
                        /** (3.1) Employee : ข้อมูลพนักงาน **/
                        if (!empIDs.contains(item.EmpID)) {
                            new EmployeeController().addEmployee(item);
                            empIDs.add(item.EmpID);
                        }
                        /** (3.2) EmployeeDetail : ข้อมูลโครงสร้างพนักงาน **/
                        EmployeeDetailInfo ed = new EmployeeDetailInfo();
                        ed.EmployeeDetailID = item.EmployeeDetailID;
                        ed.EmployeeCode = item.EmpID;
                        ed.ParentEmployeeCode = item.ParentEmployeeCode;
                        ed.EmployeeTypeCode = item.EmployeeTypeCode;
                        ed.OrganizationCode = item.OrganizationCode;
                        ed.BranchCode = item.BranchCode;
                        ed.DepartmentCode = item.DepartmentCode;
                        ed.SubDepartmentCode = item.SubDepartmentCode;
                        ed.TeamCode = item.TeamCode;
                        ed.SubTeamCode = item.SubTeamCode;
                        ed.PositionCode = item.PositionCode;
                        ed.CreateDate = item.CreateDate;
                        ed.CreateBy = item.CreateBy;
                        ed.LastUpdateDate = item.LastUpdateDate;
                        ed.LastUpdateBy = item.LastUpdateBy;
                        // ed.SupervisorCode = item.SupervisorCode;
                        ed.SupervisorHeadCode = item.SupervisorHeadCode;
                        ed.SaleCode = item.SaleCode;
                        ed.ParentEmployeeDetailID = item.ParentEmployeeDetailID;
                        ed.SupervisorCode = item.SupervisorCode;
                        ed.SubTeamHeadCode = item.SubTeamHeadCode;
                        ed.TeamHeadCode = item.TeamHeadCode;
                        ed.SubDepartmentHeadCode = item.SubDepartmentHeadCode;
                        ed.DepartmentHeadCode = item.DepartmentHeadCode;
                        ed.SourceSystem = item.SourceSystem;
                        ed.SourceSystemName = item.SourceSystemName;
                        new EmployeeDetailController().addEmployeeDetail(ed);

                    }
                }    // if (output.Info != null)

            }
        });
    }

    protected ProductStockInfo getProductStock(String productSerialNumber, ProductStockStatus status) {
        return new ProductStockController().getProductStock(productSerialNumber, status);
    }

    public static ProductStockInfo getProductStock(String organizationCode, String productSerialNumber, String status) {
        return new ProductStockController().getProductStock(organizationCode, productSerialNumber, status);
    }

    protected ProductInfo getProductInfo(String OrganizationCode, String ProductID) {
        ProductInfo output = new ProductController().getProduct(OrganizationCode, ProductID);
        return output;
    }

    public static List<ProductInfo> getProduct() {
        return new ProductController().getProduct();
    }

    public static DebtorCustomerInfo getDebtorCustomerInfo(String CustomerID) {
        DebtorCustomerInfo ret = null;
        ret = new DebtorCustomerController().getDebtorCustomer(CustomerID);
        return ret;
    }

    public static List<ProductStockInfo> getProductStockAllTeamByStatus(String organizationCode, String status) {
        List<ProductStockInfo> output = new ProductStockController().getProductStockAllTeamByStatus(organizationCode, status);
        return output;
    }

    public static List<ProductStockInfo> getProductStockOfTeam(String organizationCode, String teamcode) {
        return new ProductStockController().getProductStockOfTeam(organizationCode, teamcode);
    }

    public static List<ProductStockInfo> getProductStockByEmployeeID(String organizationCode, String employeeID) {
        return new ProductStockController().getProductStockByEmployeeID(organizationCode, employeeID);
    }

    public static SalePaymentPeriodInfo getSalePaymentPeriodInfoByPaymentComplete(String RefNo, String PaymentComplete) {
        return new SalePaymentPeriodController().getSalePaymentPeriodInfoByPaymentComplete(RefNo, PaymentComplete);
    }

    public static ContractInfo getContract(String refNo) {
        return new ContractController().getContract(refNo);
    }

    public static void addContract(ContractInfo info, boolean isSchedule) {
        new ContractController().addContract(info);

        AddContractInputInfo addContInput = AddContractInputInfo.from(info);
        TSRService.addContract(addContInput, isSchedule);
    }

    public static void updateContract(ContractInfo info, boolean isSchedule) {
        ContractInfo cont = new ContractController().getContractByRefNo(info.OrganizationCode, info.RefNo);

        if (cont != null) {

            if (cont.STATUS.equals(ContractStatus.DRAFT.toString()) && info.STATUS.equals(ContractStatus.NORMAL.toString())) {
                /*info.CONTNO = getAutoGenerateDocumentID(TSRController.DocumentGenType.Contract.toString(),
                        info.SaleSubTeamCode, info.SaleCode);*/

                //info.CONTNO = getAutoGenerateDocumentID(TSRController.DocumentGenType.Contract.toString(), BHPreference.SubTeamCode(), info.SaleCode);
                info.CONTNO = getCONTNOForContract(info);
            }
        }

        new ContractController().updateContract(info);
        UpdateContractInputInfo updateContInput = UpdateContractInputInfo.from(info);
        updateContInput.todate = new Date();
        TSRService.updateContract(updateContInput, isSchedule);
    }

    public static String getCONTNOForContract(ContractInfo info) {
        List<ManualDocumentInfo> manualDocumentContract = new ManualDocumentController().getManualDocumentForContract(info.RefNo
                , DocumentHistoryController.DocumentType.ManualDocument.toString(), DocumentHistoryController.DocumentType.Contract.toString());
        if (manualDocumentContract != null && manualDocumentContract.size() > 0) {
            return String.valueOf(manualDocumentContract.get(0).ManualRunningNo);
        }
        return null;
    }

    public static void updateContract(ContractInfo info, boolean isSchedule, boolean isChangeContract) {
        ContractInfo cont = new ContractController().getContractByRefNo(info.OrganizationCode, info.RefNo);

        if (cont != null) {

            if (cont.STATUS.equals(ContractStatus.DRAFT.toString()) && info.STATUS.equals(ContractStatus.NORMAL.toString())) {
                /*info.CONTNO = getAutoGenerateDocumentID(TSRController.DocumentGenType.Contract.toString(),
                        info.SaleSubTeamCode, info.SaleCode);*/
//                info.CONTNO = getAutoGenerateDocumentID(TSRController.DocumentGenType.Contract.toString(), BHPreference.SubTeamCode(), info.SaleCode);
            }
        }

        new ContractController().updateContract(info);
        UpdateContractInputInfo updateContInput = UpdateContractInputInfo.from(info);
        if (!isChangeContract) {
            updateContInput.todate = new Date();
        }
        TSRService.updateContract(updateContInput, isSchedule);
    }

    public static void updateContractForChangeContrac(ContractInfo info, boolean isSchedule) {
        new ContractController().updateContract(info);
        UpdateContractInputInfo updateContInput = UpdateContractInputInfo.from(info);
        //updateContInput.todate = new Date();
        TSRService.updateContract(updateContInput, isSchedule);
    }

    /*
    public static void updateContract(ContractInfo contract, ProductInfo product) {
        updateContract(contract, product, null);
    }

    public static void updateContract(ContractInfo contract, EmployeeInfo employee) {
        updateContract(contract, null, employee);
    }
    */

    /*
    public static void updateContract(final ContractInfo contract, final DebtorCustomerInfo customer, final AddressInfo address) {

        if (contract != null) {
            processTransaction(new Runnable() {
                @Override
                public void run() {
                    Date date = new Date();

                    if (customer != null) {
                        customer.LastUpdateBy = BHPreference.employeeID();
                        customer.LastUpdateDate = date;
                        DebtorCustomerController controller = new DebtorCustomerController();
                        DebtorCustomerInfo customerExist = controller.getDebtorCustomer(customer.CustomerID);
                        if (customerExist != null) {
                            controller.updateDebtorCustomer(customer);
                            UpdateDebtorCustomerInputInfo updateCustInput = UpdateDebtorCustomerInputInfo.from(customer);
                            TSRService.updateDebtorCustomer(updateCustInput, false);
                        } else {
                            customer.CreateBy = BHPreference.employeeID();
                            customer.CreateDate = date;
                            controller.addDebtorCustomer(customer);
                            AddDebtorCustomerInputInfo addCustInput = AddDebtorCustomerInputInfo.from(customer);
                            TSRService.addDebtorCustomer(addCustInput, false);

                            contract.CustomerID = customer.CustomerID;
                            contract.LastUpdateBy = BHPreference.employeeID();
                            contract.LastUpdateDate = date;
                            new ContractController().updateContract(contract);
                            UpdateContractInputInfo contInput = UpdateContractInputInfo.from(contract);
                            TSRService.updateContract(contInput, false);
                        }
                    }

                    if (address != null) {
                        address.LastUpdateBy = BHPreference.employeeID();
                        address.LastUpdateDate = date;
                        AddressController controller = new AddressController();
                        AddressInfo addressExist = controller.getAddress(contract.RefNo, address.AddressTypeCode);
                        if (addressExist != null) {
                            address.AddressID = addressExist.AddressID;
                            controller.updateAddress(address);
                            UpdateAddressInputInfo addressInput = UpdateAddressInputInfo.from(address);
                            TSRService.updateAddress(addressInput, false);
                        } else {
                            address.AddressID = DatabaseHelper.getUUID();
                            address.CreateBy = BHPreference.employeeID();
                            address.CreateDate = date;
                            controller.addAddress(address);
                            AddAddressInputInfo addressInput = AddAddressInputInfo.from(address);
                            TSRService.addAddress(addressInput, false);
                        }
                    }
                }
            });
        }
    }

    private static void updateContract(final ContractInfo contract, final ProductInfo product, final EmployeeInfo employee) {
        if (contract != null) {
            processTransaction(new Runnable() {
                @Override
                public void run() {
                    if (product != null) {
                        if (!TextUtils.isEmpty(product.ProductSerialNumber)
                                && !product.ProductSerialNumber.equals(contract.ProductSerialNumber)) {
                            ProductStockController stockController = new ProductStockController();
                            UpdateProductStockInputInfo psInput;
                            if (!TextUtils.isEmpty(contract.ProductSerialNumber)) {
                                ProductStockInfo stock = stockController.getProductStock(contract.ProductSerialNumber);
                                if (stock != null) {
                                    stock.Status = ProductStockStatus.CHECKED.name();
                                    stock.LastUpdateBy = BHPreference.employeeID();
                                    stockController.updateProductStock(stock);
                                    psInput = UpdateProductStockInputInfo.from(stock);
                                    TSRService.updateProductStock(psInput, false);
                                }
                            }

                            ProductStockInfo stock = stockController.getProductStock(product.ProductSerialNumber);
                            if (stock != null) {
                                stock.Status = ProductStockStatus.SOLD.name();
                                stock.LastUpdateBy = BHPreference.employeeID();
                                stockController.updateProductStock(stock);
                                psInput = UpdateProductStockInputInfo.from(stock);
                                TSRService.updateProductStock(psInput, false);
                            }

                            contract.ProductSerialNumber = product.ProductSerialNumber;
                            contract.ProductID = product.ProductID;
                        }
                    }

                    if (employee != null) {
                        EmployeeDetailInfo team = new TeamEmployeeController().getTeamEmployee(BHPreference.teamCode(), employee.EmpID);
                        if (team != null) {
                            contract.SaleCode = employee.EmpID;
                            contract.SaleEmployeeCode = employee.EmpID;
                            contract.SaleTeamCode = team.TeamCode;
                        }
                    }

                    contract.LastUpdateBy = BHPreference.employeeID();
                    contract.LastUpdateDate = new Date();

                    ContractController contractController = new ContractController();
                    ContractInfo contractExist = contractController.getContract(contract.RefNo);
                    if (contractExist != null) {
                        contractController.updateContract(contract);
                        UpdateContractInputInfo input = UpdateContractInputInfo.from(contract);
                        TSRService.updateContract(input, false);
                    } else {
                        contractController.addContract(contract);
                        AddContractInputInfo input = AddContractInputInfo.from(contract);
                        TSRService.addContract(input, false);
                    }
                }
            });
        }
    }
    */

    public static void deleteContract(final String refNo, final String customerID) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                if (refNo != null) {
                    new AddressController().deleteAddressByRefNo(refNo);
                    DeleteAddressInputInfo addressInput = new DeleteAddressInputInfo();
                    addressInput.RefNo = refNo;
                    TSRService.deleteAddressByRefNo(addressInput, true);

                    deleteSalePaymentPeriodByRefNo(refNo, true);

                    new ContractController().deleteContractByRefNo(refNo);
                    DeleteContractInputInfo contInput = new DeleteContractInputInfo();
                    contInput.RefNo = refNo;
                    contInput.CustomerID = customerID;
                    TSRService.deleteContract(contInput, true);
                }
                if (customerID != null) {
                    new DebtorCustomerController().deleteDebcustomerByCustomerID(customerID);
                    DeleteDebtorCustomerInputInfo dCustInput = new DeleteDebtorCustomerInputInfo();
                    dCustInput.CustomerID = customerID;
                    TSRService.deleteDebtorCustomer(dCustInput, true);
                }
            }
        });
    }

    public static ProductStockInfo getProductStockSerialNumber(
            String productSerialNumber) {
        return new ProductStockController().getProductStock(productSerialNumber);
    }

    public static ProductStockInfo getProductStockSerialNumberForCRD(String productSerialNumber, String EmployeeID) {
        return new ProductStockController().getProductStockForCRD(productSerialNumber, EmployeeID);
    }

    public static List<ProblemInfo> getProblemByProblemType(String organizationCode, String problemType) {
        List<ProblemInfo> output = new ProblemController().getProblemByProblemType(organizationCode, problemType);
        return output;
    }

    /* [START] :: Fixed - [BHPROJ-0026-941] */
    public static ProblemInfo getProblemByProblemID(String ProblemID) {
        return new ProblemController().getProblemByProblemID(ProblemID);
    }
    /* [END] :: Fixed - [BHPROJ-0026-941] */


    /***
     * [START] :: Fixed - [BHPROJ-0025-744] :: [MoM-15/12/2015] [Android-ตรวจสอบลูกค้า] Spinner ที่แสดงปัญหา ประเภท Complain ให้กรองเฉพาะปัญหาของฝ่ายตรวจสอบ/เก็บเงิน
     ***/
    public static List<ProblemInfo> getProblemByProblemTypeBySourceSystem(String organizationCode, String problemType, String sourceSystem) {
        List<ProblemInfo> output = new ProblemController().getProblemByProblemTypeBySourceSystem(organizationCode, problemType, sourceSystem);
        return output;
    }

    /***
     * [END] :: Fixed - [BHPROJ-0025-744] :: [MoM-15/12/2015] [Android-ตรวจสอบลูกค้า] Spinner ที่แสดงปัญหา ประเภท Complain ให้กรองเฉพาะปัญหาของฝ่ายตรวจสอบ/เก็บเงิน
     ***/

    public static void deleteProductStockAll() {
        new ProductStockController().deleteProductStockAll();
    }

    public static void deleteProductStockBySerialNumber(String OrganizationCode, String SerialNumber) {
        new ProductStockController().deleteProductStockBySerialNumber(OrganizationCode, SerialNumber);
    }

    public static void deleteProductAll() {
        new ProductController().deleteProductAll();
    }

    public static void deleteDiscountLimitAll() {
        new DiscountLimitController().deleteDiscountLimitAll();
    }

    public static void addProductStockForCRD(ProductStockInfo info) {
        new ProductStockController().addProductStock(info);
    }

    /**
     * DocumentHistory
     **/
    public static List<DocumentHistoryInfo> getDocumentHistoryGroupByType(String organizationCode, String DocumentType) {
        return new DocumentHistoryController().getDocumentHistoryGroupByType(organizationCode, DocumentType);
    }

    public static List<DocumentHistoryInfo> getDocumentHistoryGroupByTypeAndEmployeeCode(String organizationCode, String DocumentType, String EmployeeCode) {
        return new DocumentHistoryController().getDocumentHistoryGroupByTypeAndEmployeeCode(organizationCode, DocumentType, EmployeeCode);
    }

    public static List<DocumentHistoryInfo> getDocumentHistoryByType(String organizationCode, String DocumentType) {
        return new DocumentHistoryController().getDocumentHistoryByType(organizationCode, DocumentType);
    }

    public static List<DocumentHistoryInfo> getDocumentHistoryByTypeAndEmployeeCode(String organizationCode, String DocumentType, String EmployeeCode) {
        return new DocumentHistoryController().getDocumentHistoryByTypeAndEmployeeCode(organizationCode, DocumentType, EmployeeCode);
    }

    public static DocumentHistoryInfo getDocumentHistoryByDocumentNumber(String DocumentNumber, String DocumentType) {
        return new DocumentHistoryController().getDocumentHistoryByDocumentNumber(DocumentNumber, DocumentType);
    }

    public static DocumentHistoryInfo getDocumentHistoryByID(String OrganizationCode, String PrintHistoryID) {
        return new DocumentHistoryController().getDocumentHistoryByID(OrganizationCode, PrintHistoryID);
    }

    public static void addDocumentHistory(DocumentHistoryInfo info, boolean isSchedule) {
        new DocumentHistoryController().addDocumentHistory(info);
        AddDocumentHistoryInputInfo addInfo = AddDocumentHistoryInputInfo.from(info);
        TSRService.addDocumentHistory(addInfo, isSchedule);
    }

    public static void updateDocumentHistory(DocumentHistoryInfo info, boolean isSchedule) {
        new DocumentHistoryController().updateDocumentHistory(info);
        UpdateDocumentHistoryInputInfo inputInfo = UpdateDocumentHistoryInputInfo.from(info);
        TSRService.updateDocumentHistory(inputInfo, isSchedule);
    }


    public static void importProduct(final String OrganizationCode, final String TeamCode) {

        processTransaction(new Runnable() {
            @Override
            public void run() {
                deleteProductAll();
                new PackagePeriodDetailController().deleteAllPackagePeriodDetail();
                new PackageController().deleteAllPackage();
                new ProductStockController().deleteProductStockAllByTeamCode(OrganizationCode, TeamCode);
                //deleteProductStockAll();
                deleteDiscountLimitAll();
                new ReturnProductController().deleteReturnProductAll();
                new ReturnProductController().deleteReturnProductDetailAll();

                GetAllProductInputInfo input = new GetAllProductInputInfo();
                input.OrganizationCode = OrganizationCode;
                GetAllProductOutputInfo output = TSRService.getAllProduct(input, false);
                if (output != null) {
                    ProductController pController = new ProductController();
                    for (ProductInfo product : output.Info) {
                        pController.addProduct(product);
                    }
                }

                GetAllPackageInputInfo packageInput = new GetAllPackageInputInfo();
                packageInput.OrganizationCode = OrganizationCode;
                GetAllPackageOutputInfo packageOutput = TSRService.getAllPackage(packageInput, false);
                if (packageOutput != null) {
                    new PackageController().insertAllPackage(packageOutput.Info);
                }

                GetAllPackagePeriodDetailInputInfo pDetailInput = new GetAllPackagePeriodDetailInputInfo();
                pDetailInput.OrganizationCode = OrganizationCode;
                GetAllPackagePeriodDetailOutputInfo pDetailOutput = TSRService.getAllPackagePeriodDetail(pDetailInput, false);
                if (pDetailOutput != null) {
                    new PackagePeriodDetailController().insertAllPackagePeriodDetail(pDetailOutput.Info);
                }

                GetProductStockOfTeamInputInfo pStockInput = new GetProductStockOfTeamInputInfo();
                pStockInput.OrganizationCode = OrganizationCode;
                pStockInput.TeamCode = TeamCode;
                pStockInput.CreateBy = BHPreference.employeeID();  // [Modified@05/08/2016] :: For support CRD take by EmpID
                GetProductStockOfTeamOutputInfo pStockOutput = TSRService.getProductStockOfTeam(pStockInput, false);
                if (pStockOutput != null && pStockOutput.ResultCode == 0) {
                    ProductStockController pStockController = new ProductStockController();
                    for (ProductStockInfo pStockInfo : pStockOutput.Info) {
                        pStockController.addProductStock(pStockInfo);
                    }
                }

                GetAllDiscountLimitInputInfo discountLimitInput = new GetAllDiscountLimitInputInfo();
                GetAllDiscountLimitOutputInfo discountLimitOutput = TSRService.getAllDiscountLimit(discountLimitInput, false);
                if (discountLimitOutput != null && discountLimitOutput.ResultCode == 0 && discountLimitOutput.Info != null) {
                    DiscountLimitController discountLimitController = new DiscountLimitController();
                    for (DiscountLimitInfo discountLimitInfo : discountLimitOutput.Info) {
                        discountLimitController.addDiscountLimit(discountLimitInfo);
                    }
                }

                // Synch (UL) ReturnProduct
                GetAllRequestReturnProductByTeamInputInfo retProdInput = new GetAllRequestReturnProductByTeamInputInfo();
                retProdInput.OrganizationCode = OrganizationCode;
                retProdInput.TeamCode = TeamCode;
                GetAllRequestReturnProductByTeamOutputInfo retProdOutput = TSRService.getAllRequestReturnProductByTeam(retProdInput, false);
                if (retProdOutput != null && retProdOutput.ResultCode == 0 && retProdOutput.Info != null) {
                    for (ReturnProductInfo item : retProdOutput.Info) {
                        new ReturnProductController().insertReturnProduct(item);
                    }
                }

                // Synch (UL) ReturnProductDetail
                GetAllRequestReturnProductDetailByTeamInputInfo retProdDetInput = new GetAllRequestReturnProductDetailByTeamInputInfo();
                retProdDetInput.OrganizationCode = OrganizationCode;
                retProdDetInput.TeamCode = TeamCode;
                GetAllRequestReturnProductDetailByTeamOutputInfo retProdDetOutput = TSRService.getAllRequestReturnProductDetailByTeam(retProdDetInput, false);
                if (retProdDetOutput != null && retProdDetOutput.ResultCode == 0 && retProdDetOutput.Info != null) {
                    for (ReturnProductDetailInfo item : retProdDetOutput.Info) {
                        new ReturnProductController().insertReturnProductDetail(item);
                    }
                }


            }
        });
    }

    public static void importProductStockFromInterface(String organizationCode, String teamCode, String StockTeamCode) {
        ImportProductStockFromInterfaceInputInfo input = new ImportProductStockFromInterfaceInputInfo();
        input.OrganizationCode = organizationCode;
        input.TeamCode = teamCode;
        input.StockTeamCode = StockTeamCode;
        input.CreateDate = getInstance().getTime();
        input.CreateBy = BHPreference.employeeID();
        input.LastUpdateDate = input.CreateDate;
        input.LastUpdateBy = input.CreateBy;
        TSRService.importProductStockFromInterface(input, false);
    }

    public static ProductStockInfo addProductStock(ProductStockInfo info) {
        ProductStockInfo prod = new ProductStockController().getProductStock(info.ProductSerialNumber);
        if (prod == null) {
            new ProductStockController().addProductStock(info);
        }
        return info;// prod;
    }

    public static void updateProductStockStatus(ProductStockInfo info, boolean callWebService, boolean isSchedule) {
        new ProductStockController().updateProductStockStatus(info);

        if (callWebService == true) {

            UpdateProductStockStatusInputInfo updateProductStockStatusInfo = UpdateProductStockStatusInputInfo.from(info);
            updateProductStockStatusInfo.ImportDate = new Date();
            updateProductStockStatusInfo.SyncedDate = new Date();

            TSRService.updateProductStockStatus(updateProductStockStatusInfo, true);
            String x = "";
        }
    }

    public static void updateProductStock(ProductStockInfo ps, boolean isSchedule) {
        ps.LastUpdateBy = BHPreference.employeeID();
        ps.LastUpdateDate = new Date();
        new ProductStockController().updateProductStock(ps);
        UpdateProductStockInputInfo psInput = UpdateProductStockInputInfo.from(ps);
        UpdateProductStockOutputInfo psOutput = TSRService.updateProductStock(psInput, isSchedule);
    }

    public static void addPayment(final PaymentInfo info, final boolean isSchedule, final SaleFirstPaymentChoiceFragment.ProcessType processType, final ContractCloseAccountInfo contractCloseAccount, final Date dateOfAppointmentsForPartlyPaid, final int maxPaymentPeriodNumber) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                SalePaymentPeriodController sppController = new SalePaymentPeriodController();
                List<SalePaymentPeriodInfo> periods = sppController.getNextPayment(info.RefNo);
                new PaymentController().addPayment(info);
                AddPaymentInputInfo payInput = AddPaymentInputInfo.from(info);
                AddPaymentOutputInfo payOutput = TSRService.addPayment(payInput, isSchedule);

                /*float totalAmount = info.PAYAMT;

                float CloseAccountDiscountAmount = 0;
                ReceiptInfo receiptOfCloseAccount = new ReceiptInfo();
                if (contractCloseAccount != null) {
                    totalAmount += contractCloseAccount.DiscountAmount;
                    CloseAccountDiscountAmount = contractCloseAccount.DiscountAmount;

                    receiptOfCloseAccount.ReceiptID = DatabaseHelper.getUUID();
                    receiptOfCloseAccount.OrganizationCode = BHPreference.organizationCode();
                    receiptOfCloseAccount.ReceiptCode = getAutoGenerateDocumentID(DocumentGenType.Receipt.toString(), BHPreference.SubTeamCode(), BHPreference.saleCode());
                    receiptOfCloseAccount.PaymentID = info.PaymentID;
                    receiptOfCloseAccount.RefNo = info.RefNo;
                    receiptOfCloseAccount.DatePayment = info.CreateDate;
                    receiptOfCloseAccount.CreateBy = BHPreference.employeeID();
                    receiptOfCloseAccount.CreateDate = info.CreateDate;
                    receiptOfCloseAccount.LastUpdateBy = BHPreference.employeeID();
                    receiptOfCloseAccount.LastUpdateDate = info.CreateDate;

                    receiptOfCloseAccount.TotalPayment = contractCloseAccount.OutstandingAmount;

                    new ReceiptController().addReceipt(receiptOfCloseAccount);
                    AddReceiptInputInfo rInput = AddReceiptInputInfo.from(receiptOfCloseAccount);
                    rInput.SyncedDate = new Date();
                    AddReceiptOutputInfo rOutput = TSRService.addReceipt(rInput, isSchedule);
                }

                for (SalePaymentPeriodInfo period : periods) {

                    ReceiptInfo receipt = new ReceiptInfo();

                    if (contractCloseAccount != null) {
                        receipt = receiptOfCloseAccount;
                    } else {
                        receipt.ReceiptID = DatabaseHelper.getUUID();
                        receipt.OrganizationCode = BHPreference.organizationCode();
                        receipt.ReceiptCode = getAutoGenerateDocumentID(DocumentGenType.Receipt.toString(), BHPreference.SubTeamCode(), BHPreference.saleCode());
                        receipt.PaymentID = info.PaymentID;
                        receipt.RefNo = info.RefNo;
                        receipt.DatePayment = info.CreateDate;

                        *//*** [START] Fixed - [BHPROJ-0024-573] ***//*
//                    receipt.CreateBy = info.EmpID;
                        receipt.CreateBy = BHPreference.employeeID();
                        *//*** [END] Fixed - [BHPROJ-0024-573] ***//*

                        receipt.CreateDate = info.CreateDate;

                        *//*** [START] Fixed - [BHPROJ-0024-573] ***//*
//                    receipt.LastUpdateBy = info.EmpID;
                        receipt.LastUpdateBy = BHPreference.employeeID();
                        *//*** [END] Fixed - [BHPROJ-0024-573] ***//*

                        receipt.LastUpdateDate = info.CreateDate;
                    }

                    SalePaymentPeriodPaymentInfo periodPayment = new SalePaymentPeriodPaymentInfo();
                    periodPayment.SalePaymentPeriodID = period.SalePaymentPeriodID;
                    periodPayment.PaymentID = info.PaymentID;
                    periodPayment.ReceiptID = receipt.ReceiptID;
                    periodPayment.CreateDate = info.CreateDate;

                    *//*** [START] Fixed - [BHPROJ-0024-573] ***//*
//                    periodPayment.CreateBy = info.EmpID;
                    periodPayment.CreateBy = BHPreference.employeeID();
                    *//*** [END] Fixed - [BHPROJ-0024-573] ***//*


                    boolean finishPeriod = false;
                    if (period.OutstandingAmount <= totalAmount) {
                        period.CloseAccountDiscountAmount = period.OutstandingAmount >= CloseAccountDiscountAmount ? CloseAccountDiscountAmount : period.OutstandingAmount;
                        periodPayment.Amount = period.OutstandingAmount - period.CloseAccountDiscountAmount;
                        periodPayment.CloseAccountDiscountAmount = period.CloseAccountDiscountAmount;
                        CloseAccountDiscountAmount -= period.CloseAccountDiscountAmount;
                        finishPeriod = true;
                    } else {
                        period.CloseAccountDiscountAmount = totalAmount >= CloseAccountDiscountAmount ? CloseAccountDiscountAmount : totalAmount;
                        periodPayment.Amount = totalAmount - period.CloseAccountDiscountAmount;
                        periodPayment.CloseAccountDiscountAmount = period.CloseAccountDiscountAmount;
                        CloseAccountDiscountAmount -= period.CloseAccountDiscountAmount;
                    }

                    if (contractCloseAccount == null) {
                        receipt.TotalPayment = periodPayment.Amount;

                        new ReceiptController().addReceipt(receipt);
                        AddReceiptInputInfo rInput = AddReceiptInputInfo.from(receipt);
                        rInput.SyncedDate = new Date();
                        AddReceiptOutputInfo rOutput = TSRService.addReceipt(rInput, isSchedule);
                    }

                    new SalePaymentPeriodPaymentController().addSalePaymentPeriodPayment(periodPayment);
                    AddSalePaymentPeriodPaymentInputInfo addSpppInput = AddSalePaymentPeriodPaymentInputInfo.from(periodPayment);
                    addSpppInput.SyncedDate = new Date();
                    AddSalePaymentPeriodPaymentOutputInfo addSppOutput = TSRService.addSalePaymentPeriodPayment(addSpppInput, isSchedule);

                    if (finishPeriod) {
                        period.PaymentComplete = true;
                        period.LastUpdateBy = info.EmpID;
                        period.LastUpdateDate = info.CreateDate;

                        sppController.updateSalePaymentPeriod(period);
                        UpdateSalePaymentPeriodInputInfo updateSppInput = UpdateSalePaymentPeriodInputInfo.from(period);
                        UpdateSalePaymentPeriodOutputInfo updateSppOutput = TSRService.updateSalePaymentPeriod(updateSppInput, isSchedule);

                        *//*** [START] :: Fixed - [BHPROJ-0024-2045] :: [Android] วันที่ครบกำหนดชำระ PaymentDueDate ของงวดที่ 2-n ทางคุณไพฑูรย์ต้องการให้คิดจากวันที่มีการเก็บเงินงวดแรก บวก 1 เดือน ไปเรื่อยๆ จนถึง n งวด  ***//*
                        if(period.PaymentPeriodNumber == 1){
                            List<SalePaymentPeriodInfo> newSppList = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(info.RefNo);
                            for(SalePaymentPeriodInfo infoBy : newSppList){
                                if(infoBy.PaymentPeriodNumber > 1){
                                    Calendar cal = getInstance();
                                    cal.add(Calendar.MONTH, infoBy.PaymentPeriodNumber - 1 );
                                    cal.set(Calendar.HOUR_OF_DAY, 0);
                                    cal.set(Calendar.MINUTE, 0);
                                    cal.set(Calendar.SECOND, 0);
                                    cal.set(Calendar.MILLISECOND, 0);
                                    Date result = cal.getTime();

                                    infoBy.PaymentDueDate = result;
                                    infoBy.PaymentAppointmentDate = result;
                                    infoBy.LastUpdateBy = info.EmpID;
                                    infoBy.LastUpdateDate = info.CreateDate;

                                    sppController.updateSalePaymentPeriod(infoBy);
                                    UpdateSalePaymentPeriodInputInfo newUpdateSppInput = UpdateSalePaymentPeriodInputInfo.from(infoBy);
                                    UpdateSalePaymentPeriodOutputInfo newUpdateSppOutput = TSRService.updateSalePaymentPeriod(newUpdateSppInput, isSchedule);
                                }
                            }
                        }
                        *//*** [END] :: Fixed - [BHPROJ-0024-2045] :: [Android] วันที่ครบกำหนดชำระ PaymentDueDate ของงวดที่ 2-n ทางคุณไพฑูรย์ต้องการให้คิดจากวันที่มีการเก็บเงินงวดแรก บวก 1 เดือน ไปเรื่อยๆ จนถึง n งวด  ***//*

                        if (processType != null && processType == SaleFirstPaymentChoiceFragment.ProcessType.NextPayment) {
                            List<AssignInfo> assignSalePaymentPeriodList = new AssignController().getAssignByRefNoByTaskTypeByReferenceID(period.RefNo
                                    , AssignController.AssignTaskType.SalePaymentPeriod.toString(), period.SalePaymentPeriodID);
                            if (assignSalePaymentPeriodList != null && assignSalePaymentPeriodList.size() > 0) {
                                AssignInfo assignSalePaymentPeriod = assignSalePaymentPeriodList.get(0);
                                assignSalePaymentPeriod.AssigneeTeamCode = BHPreference.teamCode();
                                assignSalePaymentPeriod.AssigneeEmpID = BHPreference.employeeID();
                                assignSalePaymentPeriod.LastUpdateDate = new Date();
                                assignSalePaymentPeriod.LastUpdateBy = BHPreference.employeeID();
                                TSRController.updateAssign(assignSalePaymentPeriod, true);
                            } else {
                                AssignInfo assignSalePaymentPeriod = new AssignInfo();
                                assignSalePaymentPeriod.AssignID = DatabaseHelper.getUUID();
                                assignSalePaymentPeriod.TaskType = AssignController.AssignTaskType.SalePaymentPeriod.toString();
                                assignSalePaymentPeriod.OrganizationCode = BHPreference.organizationCode();
                                assignSalePaymentPeriod.RefNo = period.RefNo;
                                assignSalePaymentPeriod.AssigneeEmpID = BHPreference.employeeID();
                                assignSalePaymentPeriod.AssigneeTeamCode = BHPreference.teamCode();
                                assignSalePaymentPeriod.Order = 0;
                                assignSalePaymentPeriod.OrderExpect = 0;
                                assignSalePaymentPeriod.CreateDate = new Date();
                                assignSalePaymentPeriod.CreateBy = BHPreference.employeeID();
                                assignSalePaymentPeriod.LastUpdateDate = new Date();
                                assignSalePaymentPeriod.LastUpdateBy = BHPreference.employeeID();
                                assignSalePaymentPeriod.ReferenceID = period.SalePaymentPeriodID;
                                TSRController.addAssign(assignSalePaymentPeriod, true);
                            }
                        }
                    }

                    totalAmount -= periodPayment.Amount;
                    if (totalAmount <= 0) {
                        break;
                    }
                }*/

                float totalAmount = info.PAYAMT;
                float totalAmountForContractCloseAccount = 0;
                if (contractCloseAccount != null) {
                    totalAmountForContractCloseAccount = contractCloseAccount.OutstandingAmount;
                    totalAmount -= (totalAmountForContractCloseAccount - contractCloseAccount.DiscountAmount);
                }

                for (SalePaymentPeriodInfo period : periods) {
                    if (totalAmount > 0) {
                        //ชำระปกติ หรือชำระงวดค้าง
                        String receiptID = DatabaseHelper.getUUID();

                        SalePaymentPeriodPaymentInfo periodPayment = new SalePaymentPeriodPaymentInfo();
                        periodPayment.SalePaymentPeriodID = period.SalePaymentPeriodID;
                        periodPayment.PaymentID = info.PaymentID;
                        periodPayment.ReceiptID = receiptID;
                        periodPayment.CreateDate = info.CreateDate;
                        periodPayment.CreateBy = BHPreference.employeeID();

                        boolean finishPeriod = false;
                        if (period.OutstandingAmount <= totalAmount) {
                            period.CloseAccountDiscountAmount = 0;
                            periodPayment.Amount = period.OutstandingAmount - period.CloseAccountDiscountAmount;
                            periodPayment.CloseAccountDiscountAmount = period.CloseAccountDiscountAmount;
                            finishPeriod = true;
                        } else {
                            period.CloseAccountDiscountAmount = 0;
                            periodPayment.Amount = totalAmount - period.CloseAccountDiscountAmount;
                            periodPayment.CloseAccountDiscountAmount = period.CloseAccountDiscountAmount;
                        }

                        new SalePaymentPeriodPaymentController().addSalePaymentPeriodPayment(periodPayment);
                        AddSalePaymentPeriodPaymentInputInfo addSpppInput = AddSalePaymentPeriodPaymentInputInfo.from(periodPayment);
                        addSpppInput.SyncedDate = new Date();
                        AddSalePaymentPeriodPaymentOutputInfo addSppOutput = TSRService.addSalePaymentPeriodPayment(addSpppInput, isSchedule);

                        ReceiptInfo receipt = new ReceiptInfo();
                        receipt.ReceiptID = receiptID;
                        receipt.OrganizationCode = BHPreference.organizationCode();
                        receipt.ReceiptCode = getAutoGenerateDocumentID(DocumentGenType.Receipt, BHPreference.SubTeamCode(), BHPreference.saleCode());
                        receipt.PaymentID = info.PaymentID;
                        receipt.RefNo = info.RefNo;
                        receipt.DatePayment = info.CreateDate;
                        receipt.CreateBy = BHPreference.employeeID();
                        receipt.CreateDate = info.CreateDate;
                        receipt.LastUpdateBy = BHPreference.employeeID();
                        receipt.LastUpdateDate = info.CreateDate;
                        receipt.TotalPayment = periodPayment.Amount;
                        new ReceiptController().addReceipt(receipt);
                        AddReceiptInputInfo rInput = AddReceiptInputInfo.from(receipt);
                        rInput.SyncedDate = new Date();
                        AddReceiptOutputInfo rOutput = TSRService.addReceipt(rInput, isSchedule);

                        if (finishPeriod) {
                            period.PaymentComplete = true;
                            period.LastUpdateBy = info.EmpID;
                            period.LastUpdateDate = info.CreateDate;

                            sppController.updateSalePaymentPeriod(period);
                            UpdateSalePaymentPeriodInputInfo updateSppInput = UpdateSalePaymentPeriodInputInfo.from(period);
                            UpdateSalePaymentPeriodOutputInfo updateSppOutput = TSRService.updateSalePaymentPeriod(updateSppInput, isSchedule);

                            /*** [START] :: Fixed - [BHPROJ-0024-2045] :: [Android] วันที่ครบกำหนดชำระ PaymentDueDate ของงวดที่ 2-n ทางคุณไพฑูรย์ต้องการให้คิดจากวันที่มีการเก็บเงินงวดแรก บวก 1 เดือน ไปเรื่อยๆ จนถึง n งวด  ***/
                            if(period.PaymentPeriodNumber == 1){
                                List<SalePaymentPeriodInfo> newSppList = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoORDERBYPaymentPeriodNumber(info.RefNo);
                                for(SalePaymentPeriodInfo infoBy : newSppList){
                                    if(infoBy.PaymentPeriodNumber > 1){
                                        Calendar cal = getInstance();
                                        cal.add(Calendar.MONTH, infoBy.PaymentPeriodNumber - 1 );
                                        cal.set(Calendar.HOUR_OF_DAY, 0);
                                        cal.set(Calendar.MINUTE, 0);
                                        cal.set(Calendar.SECOND, 0);
                                        cal.set(Calendar.MILLISECOND, 0);
                                        Date result = cal.getTime();

                                        infoBy.PaymentDueDate = result;
                                        infoBy.PaymentAppointmentDate = result;
                                        infoBy.LastUpdateBy = info.EmpID;
                                        infoBy.LastUpdateDate = info.CreateDate;

                                        sppController.updateSalePaymentPeriod(infoBy);
                                        UpdateSalePaymentPeriodInputInfo newUpdateSppInput = UpdateSalePaymentPeriodInputInfo.from(infoBy);
                                        UpdateSalePaymentPeriodOutputInfo newUpdateSppOutput = TSRService.updateSalePaymentPeriod(newUpdateSppInput, isSchedule);
                                    }
                                }
                            }
                            /*** [END] :: Fixed - [BHPROJ-0024-2045] :: [Android] วันที่ครบกำหนดชำระ PaymentDueDate ของงวดที่ 2-n ทางคุณไพฑูรย์ต้องการให้คิดจากวันที่มีการเก็บเงินงวดแรก บวก 1 เดือน ไปเรื่อยๆ จนถึง n งวด  ***/

                            if (processType != null && processType == SaleFirstPaymentChoiceFragment.ProcessType.NextPayment) {
                                List<AssignInfo> assignSalePaymentPeriodList = new AssignController().getAssignByRefNoByTaskTypeByReferenceID(period.RefNo
                                        , AssignController.AssignTaskType.SalePaymentPeriod.toString(), period.SalePaymentPeriodID);
                                if (assignSalePaymentPeriodList != null && assignSalePaymentPeriodList.size() > 0) {
                                    AssignInfo assignSalePaymentPeriod = assignSalePaymentPeriodList.get(0);
                                    assignSalePaymentPeriod.AssigneeTeamCode = BHPreference.teamCode();
                                    assignSalePaymentPeriod.AssigneeEmpID = BHPreference.employeeID();
                                    assignSalePaymentPeriod.LastUpdateDate = new Date();
                                    assignSalePaymentPeriod.LastUpdateBy = BHPreference.employeeID();
                                    TSRController.updateAssign(assignSalePaymentPeriod, true);
                                } else {
                                    AssignInfo assignSalePaymentPeriod = new AssignInfo();
                                    assignSalePaymentPeriod.AssignID = DatabaseHelper.getUUID();
                                    assignSalePaymentPeriod.TaskType = AssignController.AssignTaskType.SalePaymentPeriod.toString();
                                    assignSalePaymentPeriod.OrganizationCode = BHPreference.organizationCode();
                                    assignSalePaymentPeriod.RefNo = period.RefNo;
                                    assignSalePaymentPeriod.AssigneeEmpID = BHPreference.employeeID();
                                    assignSalePaymentPeriod.AssigneeTeamCode = BHPreference.teamCode();
                                    assignSalePaymentPeriod.Order = 0;
                                    assignSalePaymentPeriod.OrderExpect = 0;
                                    assignSalePaymentPeriod.CreateDate = new Date();
                                    assignSalePaymentPeriod.CreateBy = BHPreference.employeeID();
                                    assignSalePaymentPeriod.LastUpdateDate = new Date();
                                    assignSalePaymentPeriod.LastUpdateBy = BHPreference.employeeID();
                                    assignSalePaymentPeriod.ReferenceID = period.SalePaymentPeriodID;
                                    TSRController.addAssign(assignSalePaymentPeriod, true);
                                }
                            }
                        }

                        totalAmount -= periodPayment.Amount;
                        if (totalAmount <= 0) {
                            if (contractCloseAccount == null) {
                                break;
                            }
                        }
                    } else if (contractCloseAccount != null) {
                        //ตัดสด
                        ReceiptInfo receiptOfCloseAccount = new ReceiptInfo();
                        receiptOfCloseAccount.ReceiptID = DatabaseHelper.getUUID();
                        receiptOfCloseAccount.OrganizationCode = BHPreference.organizationCode();
                        receiptOfCloseAccount.ReceiptCode = getAutoGenerateDocumentID(DocumentGenType.Receipt, BHPreference.SubTeamCode(), BHPreference.saleCode());
                        receiptOfCloseAccount.PaymentID = info.PaymentID;
                        receiptOfCloseAccount.RefNo = info.RefNo;
                        receiptOfCloseAccount.DatePayment = info.CreateDate;
                        receiptOfCloseAccount.CreateBy = BHPreference.employeeID();
                        receiptOfCloseAccount.CreateDate = info.CreateDate;
                        receiptOfCloseAccount.LastUpdateBy = BHPreference.employeeID();
                        receiptOfCloseAccount.LastUpdateDate = info.CreateDate;

                        receiptOfCloseAccount.TotalPayment = contractCloseAccount.OutstandingAmount;

                        new ReceiptController().addReceipt(receiptOfCloseAccount);
                        AddReceiptInputInfo rInput = AddReceiptInputInfo.from(receiptOfCloseAccount);
                        rInput.SyncedDate = new Date();
                        AddReceiptOutputInfo rOutput = TSRService.addReceipt(rInput, isSchedule);


                        float CloseAccountDiscountAmount = contractCloseAccount.DiscountAmount;
                        List<SalePaymentPeriodInfo> periodsOfCloseAccount = sppController.getNextPayment(info.RefNo);

                        for (SalePaymentPeriodInfo periodOfCloseAccount : periodsOfCloseAccount) {
                            if (totalAmountForContractCloseAccount > 0) {

                                SalePaymentPeriodPaymentInfo periodPaymentOfCloseAccount = new SalePaymentPeriodPaymentInfo();
                                periodPaymentOfCloseAccount.SalePaymentPeriodID = periodOfCloseAccount.SalePaymentPeriodID;
                                periodPaymentOfCloseAccount.PaymentID = info.PaymentID;
                                periodPaymentOfCloseAccount.ReceiptID = receiptOfCloseAccount.ReceiptID;
                                periodPaymentOfCloseAccount.CreateDate = info.CreateDate;
                                periodPaymentOfCloseAccount.CreateBy = BHPreference.employeeID();

                                if (CloseAccountDiscountAmount > 0) {
                                    periodOfCloseAccount.CloseAccountDiscountAmount = periodOfCloseAccount.OutstandingAmount >= CloseAccountDiscountAmount ? CloseAccountDiscountAmount : periodOfCloseAccount.OutstandingAmount;
                                    CloseAccountDiscountAmount -= periodOfCloseAccount.CloseAccountDiscountAmount;
                                } else {
                                    periodOfCloseAccount.CloseAccountDiscountAmount = 0;
                                }

                                periodPaymentOfCloseAccount.Amount = periodOfCloseAccount.OutstandingAmount - periodOfCloseAccount.CloseAccountDiscountAmount;
                                periodPaymentOfCloseAccount.CloseAccountDiscountAmount = periodOfCloseAccount.CloseAccountDiscountAmount;

                                new SalePaymentPeriodPaymentController().addSalePaymentPeriodPayment(periodPaymentOfCloseAccount);
                                AddSalePaymentPeriodPaymentInputInfo addSpppInput = AddSalePaymentPeriodPaymentInputInfo.from(periodPaymentOfCloseAccount);
                                addSpppInput.SyncedDate = new Date();
                                AddSalePaymentPeriodPaymentOutputInfo addSppOutput = TSRService.addSalePaymentPeriodPayment(addSpppInput, isSchedule);

                                periodOfCloseAccount.PaymentComplete = true;
                                periodOfCloseAccount.LastUpdateBy = info.EmpID;
                                periodOfCloseAccount.LastUpdateDate = info.CreateDate;

                                sppController.updateSalePaymentPeriod(periodOfCloseAccount);
                                UpdateSalePaymentPeriodInputInfo updateSppInput = UpdateSalePaymentPeriodInputInfo.from(periodOfCloseAccount);
                                UpdateSalePaymentPeriodOutputInfo updateSppOutput = TSRService.updateSalePaymentPeriod(updateSppInput, isSchedule);


                                if (processType != null && processType == SaleFirstPaymentChoiceFragment.ProcessType.NextPayment) {
                                    List<AssignInfo> assignSalePaymentPeriodList = new AssignController().getAssignByRefNoByTaskTypeByReferenceID(periodOfCloseAccount.RefNo
                                            , AssignController.AssignTaskType.SalePaymentPeriod.toString(), periodOfCloseAccount.SalePaymentPeriodID);
                                    if (assignSalePaymentPeriodList != null && assignSalePaymentPeriodList.size() > 0) {
                                        AssignInfo assignSalePaymentPeriod = assignSalePaymentPeriodList.get(0);
                                        assignSalePaymentPeriod.AssigneeTeamCode = BHPreference.teamCode();
                                        assignSalePaymentPeriod.AssigneeEmpID = BHPreference.employeeID();
                                        assignSalePaymentPeriod.LastUpdateDate = new Date();
                                        assignSalePaymentPeriod.LastUpdateBy = BHPreference.employeeID();
                                        TSRController.updateAssign(assignSalePaymentPeriod, true);
                                    } else {
                                        AssignInfo assignSalePaymentPeriod = new AssignInfo();
                                        assignSalePaymentPeriod.AssignID = DatabaseHelper.getUUID();
                                        assignSalePaymentPeriod.TaskType = AssignController.AssignTaskType.SalePaymentPeriod.toString();
                                        assignSalePaymentPeriod.OrganizationCode = BHPreference.organizationCode();
                                        assignSalePaymentPeriod.RefNo = periodOfCloseAccount.RefNo;
                                        assignSalePaymentPeriod.AssigneeEmpID = BHPreference.employeeID();
                                        assignSalePaymentPeriod.AssigneeTeamCode = BHPreference.teamCode();
                                        assignSalePaymentPeriod.Order = 0;
                                        assignSalePaymentPeriod.OrderExpect = 0;
                                        assignSalePaymentPeriod.CreateDate = new Date();
                                        assignSalePaymentPeriod.CreateBy = BHPreference.employeeID();
                                        assignSalePaymentPeriod.LastUpdateDate = new Date();
                                        assignSalePaymentPeriod.LastUpdateBy = BHPreference.employeeID();
                                        assignSalePaymentPeriod.ReferenceID = periodOfCloseAccount.SalePaymentPeriodID;
                                        TSRController.addAssign(assignSalePaymentPeriod, true);
                                    }
                                }

                                totalAmountForContractCloseAccount -= periodPaymentOfCloseAccount.Amount;
                                if (totalAmountForContractCloseAccount <= 0) {
                                    break;
                                }
                            }
                        }

                        break;
                    } else {
                        break;
                    }
                }

                /*boolean completed = true;
                for (SalePaymentPeriodInfo period : periods) {
                    if (!period.PaymentComplete) {
                        completed = false;
                        break;
                    }
                }*/

                List<SalePaymentPeriodInfo> sppCheckCompleted = sppController.getNextPayment(info.RefNo);
                if (sppCheckCompleted == null || sppCheckCompleted.size() == 0) {
                    ContractController ctController = new ContractController();
                    ContractInfo contract = ctController.getContract(info.RefNo);
                    if (contract != null) {
                        contract.STATUS = ContractStatus.F.toString();
                        contract.LastUpdateBy = info.EmpID;
                        contract.LastUpdateDate = info.CreateDate;
                        ctController.updateContract(contract);
                        UpdateContractInputInfo updateContInput = UpdateContractInputInfo.from(contract);
                        UpdateContractOutputInfo updateContOutput = TSRService.updateContract(updateContInput, isSchedule);

                        DebtorCustomerInfo customer = getDebCustometByID(contract.CustomerID);
                        if (customer != null) {
                            customer.DebtStatus = 2;//*** 1 = ลูกหนี้ (ยังชำระเงินไม่เสร็จสิ้น) / 2 = ลูกค้า (ไม่มีหนี้คงค้างกับบริษัท)
                            new DebtorCustomerController().updateDebtorCustomer(customer);
                            UpdateDebtorCustomerInputInfo updateDebCustInput = UpdateDebtorCustomerInputInfo.from(customer);
                            TSRService.updateDebtorCustomer(updateDebCustInput, isSchedule);
                        }

                        if (contractCloseAccount != null) {
                            new ContractCloseAccountController().addContractCloseAccount(contractCloseAccount);
                            AddContractCloseAccountInputInfo contractCloseAccountInput = AddContractCloseAccountInputInfo.from(contractCloseAccount);
                            TSRService.addContractCloseAccount(contractCloseAccountInput, true);
                        }
                    }
                }

                /*** [START] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/
                if (dateOfAppointmentsForPartlyPaid != null) {
                    postponeAppointment(processType, info.RefNo, dateOfAppointmentsForPartlyPaid, maxPaymentPeriodNumber, true);//
                }
                /*** [END] Fixed - [BHPROJ-0026-750] [Android-ชำระเงิน] หากชำระเงินบางส่วน ให้สามารถนัดชำระส่วนที่เหลือได้เลย ***/
            }
        });
    }

    public static void addPaymentByChangeContract(final PaymentInfo info, final boolean isSchedule) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                SalePaymentPeriodController sppController = new SalePaymentPeriodController();
                List<SalePaymentPeriodInfo> periods = sppController.getNextPayment(info.RefNo);
                new PaymentController().addPayment(info);
                AddPaymentInputInfo payInput = AddPaymentInputInfo.from(info);
                AddPaymentOutputInfo payOutput = TSRService.addPayment(payInput, isSchedule);

                float totalAmount = info.PAYAMT;
                for (SalePaymentPeriodInfo period : periods) {

                    ReceiptInfo receipt = new ReceiptInfo();
                    receipt.ReceiptID = DatabaseHelper.getUUID();
                    receipt.OrganizationCode = BHPreference.organizationCode();

                    List<String> positionCode = Arrays.asList(BHPreference.PositionCode().split(","));
                    if (positionCode.contains("SaleLeader")) {
                        receipt.ReceiptCode = getAutoGenerateDocumentID(DocumentGenType.Receipt, BHPreference.SubTeamCode(), BHPreference.saleCode());
                    } else {
                        receipt.ReceiptCode = receipt.ReceiptID;
                    }

                    receipt.PaymentID = info.PaymentID;
                    receipt.RefNo = info.RefNo;
                    receipt.DatePayment = info.CreateDate;
                    receipt.CreateBy = BHPreference.employeeID();
                    receipt.CreateDate = info.CreateDate;
                    receipt.LastUpdateBy = BHPreference.employeeID();
                    receipt.LastUpdateDate = info.CreateDate;

                    SalePaymentPeriodPaymentInfo periodPayment = new SalePaymentPeriodPaymentInfo();
                    periodPayment.SalePaymentPeriodID = period.SalePaymentPeriodID;
                    periodPayment.PaymentID = info.PaymentID;
                    periodPayment.ReceiptID = receipt.ReceiptID;
                    periodPayment.CreateDate = info.CreateDate;
                    periodPayment.CreateBy = BHPreference.employeeID();

                    boolean finishPeriod = false;
                    if (period.OutstandingAmount <= totalAmount) {
                        periodPayment.Amount = period.OutstandingAmount;
                        finishPeriod = true;
                    } else {
                        periodPayment.Amount = totalAmount;
                    }
                    receipt.TotalPayment = periodPayment.Amount;

                    new ReceiptController().addReceipt(receipt);
                    AddReceiptInputInfo rInput = AddReceiptInputInfo.from(receipt);
                    rInput.SyncedDate = new Date();
                    AddReceiptOutputInfo rOutput = TSRService.addReceipt(rInput, isSchedule);

                    new SalePaymentPeriodPaymentController().addSalePaymentPeriodPayment(periodPayment);
                    AddSalePaymentPeriodPaymentInputInfo addSpppInput = AddSalePaymentPeriodPaymentInputInfo.from(periodPayment);
                    addSpppInput.SyncedDate = new Date();
                    AddSalePaymentPeriodPaymentOutputInfo addSppOutput = TSRService.addSalePaymentPeriodPayment(addSpppInput, isSchedule);

                    if (finishPeriod) {
                        period.PaymentComplete = true;
                        period.LastUpdateBy = BHPreference.employeeID();
                        period.LastUpdateDate = info.CreateDate;
                        sppController.updateSalePaymentPeriod(period);
                        UpdateSalePaymentPeriodInputInfo updateSppInput = UpdateSalePaymentPeriodInputInfo.from(period);
                        UpdateSalePaymentPeriodOutputInfo updateSppOutput = TSRService.updateSalePaymentPeriod(updateSppInput, isSchedule);

                    }

                    totalAmount -= periodPayment.Amount;
                    if (totalAmount <= 0) {
                        break;
                    }
                }

                boolean completed = true;
                for (SalePaymentPeriodInfo period : periods) {
                    if (!period.PaymentComplete) {
                        completed = false;
                        break;
                    }
                }

                if (completed) {
                    ContractController ctController = new ContractController();
                    ContractInfo contract = ctController.getContract(info.RefNo);
                    if (contract != null) {
                        contract.STATUS = ContractStatus.F.toString();
                        contract.LastUpdateBy = BHPreference.employeeID();
                        contract.LastUpdateDate = info.CreateDate;
                        ctController.updateContract(contract);
                        UpdateContractInputInfo updateContInput = UpdateContractInputInfo.from(contract);
                        UpdateContractOutputInfo updateContOutput = TSRService.updateContract(updateContInput, isSchedule);

                        DebtorCustomerInfo customer = getDebCustometByID(contract.CustomerID);
                        if (customer != null) {
                            customer.DebtStatus = 2;//*** 1 = ลูกหนี้ (ยังชำระเงินไม่เสร็จสิ้น) / 2 = ลูกค้า (ไม่มีหนี้คงค้างกับบริษัท)
                            new DebtorCustomerController().updateDebtorCustomer(customer);
                            UpdateDebtorCustomerInputInfo updateDebCustInput = UpdateDebtorCustomerInputInfo.from(customer);
                            TSRService.updateDebtorCustomer(updateDebCustInput, isSchedule);
                        }
                    }

                }
            }
        });
    }

    protected AddContractWithSalePaymentPeriodOutputInfo getAddContractWithSalePaymentPeriod(SalePaymentPeriodInfo info) {
        AddContractWithSalePaymentPeriodOutputInfo output = new AddContractWithSalePaymentPeriodOutputInfo();

        SalePaymentPeriodInfo salepayment = new SalePaymentPeriodInfo();
        salepayment.SalePaymentPeriodID = info.SalePaymentPeriodID;
        salepayment.RefNo = info.RefNo;
        salepayment.PaymentPeriodNumber = info.PaymentPeriodNumber;
        salepayment.PaymentAmount = info.PaymentAmount;
        salepayment.Discount = info.Discount;
        salepayment.NetAmount = info.NetAmount;
        salepayment.PaymentComplete = info.PaymentComplete;
        salepayment.PaymentDueDate = info.PaymentDueDate;
        salepayment.PaymentAppointmentDate = info.PaymentAppointmentDate;
        salepayment.TripID = info.TripID;
        salepayment.CreateDate = info.CreateDate;
        salepayment.CreateBy = info.CreateBy;
        salepayment.LastUpdateDate = info.LastUpdateDate;
        salepayment.LastUpdateBy = info.LastUpdateBy;
        salepayment.SyncedDate = info.SyncedDate;

        new SalePaymentPeriodController().addSalePaymentPeriod(salepayment);
        return output;

    }

    protected void saveAddress(AddressInfo info) {
        AddressInfo ret = new AddressController().getAddress(info.RefNo, info.AddressTypeCode);
        if (ret == null) {
            info.AddressID = DatabaseHelper.getUUID();
            new AddressController().addAddress(info);

            AddAddressInputInfo addAddrInput = AddAddressInputInfo.from(info);
            TSRService.addAddress(addAddrInput, true);
        } else {
            info.AddressID = ret.AddressID;
            new AddressController().updateAddress(info);

            UpdateAddressInputInfo updateAddrInput = UpdateAddressInputInfo.from(info);
            TSRService.updateAddress(updateAddrInput, true);
        }
    }

    public static void saveCustomerData(DebtorCustomerInfo customer, boolean isSchedule) {
        saveCustomerData(customer, null, null, isSchedule);
    }

    public static void saveCustomerData(DebtorCustomerInfo customer, AddressInfo address, boolean isSchedule) {
        saveCustomerData(customer, address, null, isSchedule);
    }

    public static void saveCustomerData(final DebtorCustomerInfo customer, final AddressInfo address, final ContractInfo contract, final boolean isSchedule) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                if (customer != null) {
                    DebtorCustomerInfo retCust = new DebtorCustomerController().getDebtorCustomer(customer.CustomerID);
                    if (retCust == null) {
                        new DebtorCustomerController().addDebtorCustomer(customer);
                        AddDebtorCustomerInputInfo addDebCustInput = AddDebtorCustomerInputInfo.from(customer);
                        TSRService.addDebtorCustomer(addDebCustInput, isSchedule);

                    } else {
                        new DebtorCustomerController().updateDebtorCustomer(customer);
                        UpdateDebtorCustomerInputInfo updateDebCustInput = UpdateDebtorCustomerInputInfo.from(customer);
                        TSRService.updateDebtorCustomer(updateDebCustInput, isSchedule);
                    }
                }

                if (address != null) {
                    AddressInfo retAddr = new AddressController().getAddress(address.RefNo, address.AddressTypeCode);
                    if (retAddr == null) {
                        address.AddressID = DatabaseHelper.getUUID();
                        new AddressController().addAddress(address);

                        AddAddressInputInfo addAddrInput = AddAddressInputInfo.from(address);
                        TSRService.addAddress(addAddrInput, isSchedule);
                    } else {
                        address.AddressID = retAddr.AddressID;
                        new AddressController().updateAddress(address);

                        UpdateAddressInputInfo updateAddrInput = UpdateAddressInputInfo.from(address);
                        TSRService.updateAddress(updateAddrInput, isSchedule);
                    }
                }

                if (contract != null) {
                    ContractInfo cont = new ContractController().getContractByRefNo(contract.OrganizationCode, contract.RefNo);
//                    if (cont != null) {
//                        if (cont.STATUS.equals(ContractStatus.DRAFT.toString())
//                                && contract.STATUS.equals(ContractStatus.NORMAL.toString())) {
//                            contract.CONTNO = getAutoGenerateDocumentID(
//                                    TSRController.DocumentGenType.Contract.toString(),
//                                    BHPreference.SubTeamCode(), BHPreference.saleCode());
//                        }
//                    }
                    new ContractController().updateContract(contract);
                    UpdateContractInputInfo updateContInput = UpdateContractInputInfo.from(contract);
                    TSRService.updateContract(updateContInput, isSchedule);
                }
            }
        });
    }

    protected EmployeeInfo getSaleTeamByTeamCode(String organizationCode, String teamCode, String empID) {
        return new EmployeeController().getSaleTeamByTeamCode(organizationCode, teamCode, empID);
    }

    public FortnightInfo getCurrentFortnight(String organizationCode) {
        return new FortnightController().getCurrentFortnight(organizationCode);
    }

    public FortnightInfo getCurrentFortnightInfo() {
        return new FortnightController().getCurrentFortnightInfo();
    }

    protected List<FortnightInfo> getAllFortnight(String organizationCode) {
        return new FortnightController().getAllFortnight(organizationCode);
    }

    public static List<ContractInfo> getContractBySaleTeamCode(String organizationCode, String saleTeamCode) {
        return new ContractController().getContractBySaleTeamCode(organizationCode, saleTeamCode);
    }

    public static List<ContractInfo> getContractStatusFinish(String organizationCode, String saleTeamCode, String StatusName) {
        return new ContractController().getContractStatusFinish(organizationCode, saleTeamCode, StatusName);
    }

    public static List<ContractInfo> getContractStatusFinishForCRD(String organizationCode, String saleTeamCode, String StatusName, String EmployeeID) {
        return new ContractController().getContractStatusFinishForCRD(organizationCode, saleTeamCode, StatusName, EmployeeID);
    }

    public static List<ContractInfo> getContractStatusFinishBySearch(String organizationCode, String saleTeamCode, String StatusName, String strSearch) {
        return new ContractController().getContractStatusFinishBySearch(organizationCode, saleTeamCode, StatusName, strSearch);
    }

    //-- Fixed - [BHPROJ-0026-3283][Android-รายละเอียดสัญญา] ให้ Sort ตาม วันที่ Payment ล่าสุดเรียงลงไป (Comment ตัวนี้ไปใช้ getContractStatusFinishForCreditBySearch แทน)
    /*
    public static List<ContractInfo> getContractStatusFinishForCredit(String organizationCode, String StatusName) {
        return new ContractController().getContractStatusFinishForCredit(organizationCode, StatusName);
    }
    */

    public static List<ContractInfo> getContractStatusFinishForCreditBySearch(String organizationCode, String StatusName, String strSearch) {
        return new ContractController().getContractStatusFinishForCreditBySearch(organizationCode, StatusName, strSearch);
    }

    public static List<ContractInfo> getContractStatusUnFinish(String organizationCode, String saleTeamCode, String StatusName) {
        return new ContractController().getContractStatusUnFinish(organizationCode, saleTeamCode, StatusName);
    }

    public static List<ContractInfo> getContractStatusUnFinishForCRD(String organizationCode, String saleTeamCode, String StatusName, String SaleEmployeeCode) {
        return new ContractController().getContractStatusUnFinishForCRD(organizationCode, saleTeamCode, StatusName, SaleEmployeeCode);
    }

    public static List<ContractInfo> getContractStatusFinishByEFFDATE(String organizationCode, String saleTeamCode, String StatusName, Date StartDate, Date EndDate, boolean inDay) {
        return new ContractController().getContractStatusFinishByEFFDATE(organizationCode, saleTeamCode, StatusName, StartDate, EndDate, inDay);
    }

    public static List<ContractInfo> getContractStatusFinishByEFFDATEAndSearch(String organizationCode, String saleTeamCode, String StatusName, Date StartDate, Date EndDate, String Search, boolean inDay) {
        return new ContractController().getContractStatusFinishByEFFDATEAndSearch(organizationCode, saleTeamCode, StatusName, StartDate, EndDate, Search, inDay);
    }

    public static List<PaymentInfo> getSummaryPaymentGroupByPaymentDate(String organizationCode, String TeamCode, String EmpIDList) {
        return new PaymentController().getSummaryPaymentGroupByPaymentDate(organizationCode, TeamCode, EmpIDList);
    }

    public static List<PaymentInfo> getSummaryPaymentGroupByPaymentTypeByPaymentDate(String organizationCode, Date paymentDate, String TeamCode, String EmpIDOfMember) {
        return new PaymentController().getSummaryPaymentGroupByPaymentTypeByPaymentDate(organizationCode, paymentDate, TeamCode, EmpIDOfMember);
    }

    public static List<SalePaymentPeriodInfo> getSalePaymentPeriodByRefNo(String refNo) {
        return new SalePaymentPeriodController().getSalePaymentPeriodByRefNo(refNo);
    }

    public static PaymentDataInfo getNextPaymentDetail(String refNo) {
        PaymentDataInfo result = null;

        ContractInfo contract = new ContractController().getContract(refNo);
        if (contract != null) {
            result = new PaymentDataInfo();
            result.contract = contract;

            DebtorCustomerInfo customer = new DebtorCustomerController().getDebtorCustomer(contract.CustomerID);
            result.customer = customer;

            AddressController addressController = new AddressController();
            List<AddressInfo> address = addressController.getAddress(refNo);
            result.installAddress = addressController.getAddress(address, AddressType.AddressInstall);
            result.paymentAddress = addressController.getAddress(address, AddressType.AddressPayment);
            result.defaultAddress = addressController.getAddress(address, AddressType.AddressIDCard);

            ProductInfo product = new ProductController().getProduct(contract.OrganizationCode, contract.ProductID);
            result.product = product;

            TripInfo trip = new TripController().getCurrentTrip();
            result.trip = trip;

            List<SalePaymentPeriodInfo> payments = new SalePaymentPeriodController().getNextPayment(refNo);
            result.periods = payments;
        }

        return result;
    }

    public static List<SalePaymentPeriodInfo> getSummaryFirstPayment(String organizationCode, String teamCode, String assigneeEmpID) {
        return new SalePaymentPeriodController().getSummaryFirstPayment(organizationCode, teamCode, assigneeEmpID);
    }

    public static List<SalePaymentPeriodInfo> getSummaryFirstPaymentComplete(String organizationCode, String teamCode, String empID) {
        return new SalePaymentPeriodController().getSummaryFirstPaymentComplete(organizationCode, teamCode, empID);
    }

    public static List<SalePaymentPeriodInfo> getSummaryNextPayment(String organizationCode, String teamCode, String assigneeEmpID) {
        return new SalePaymentPeriodController().getSummaryNextPayment(organizationCode, teamCode, assigneeEmpID);
    }

    public static List<ChannelItemInfo> getChannelByChannelCode(String organizationCode, String channelCode) {
        return new ChannelController().getChannelByChannelCode(organizationCode, channelCode);
    }

    public static void addSendMoney(SendMoneyInfo info) {
        new SendMoneyController().addSendMoney(info);

        AddSendMoneyInputInfo input = AddSendMoneyInputInfo.from(info);
        TSRService.addSendMoney(input, true);
    }

    public static SendMoneyInfo getLastSendMoneyToBankAndPayPoint(String EmpID,boolean isReference1) {
        return new SendMoneyController().getLastSendMoneyToBankAndPayPoint(EmpID,isReference1);
    }

    /***
     * [START] - Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน
     ***/
    public static void deleteSendMoneyByID(String SendMoneyID, boolean isSchedule) {
        new SendMoneyController().deleteSendMoneyByID(SendMoneyID);

        DeleteSendMoneyByIDInputInfo input = new DeleteSendMoneyByIDInputInfo();
        input.SendMoneyID = SendMoneyID;
        TSRService.deleteSendMoneyByID(input, isSchedule);
    }

    /***
     * [END] - Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน
     ***/

    public static void addRequestLoss(WriteOffNPLInfo writeOffNPLInfo, boolean isSchedule) {
        new WriteOffNPLController().addWriteOffNPL(writeOffNPLInfo);
        AddRequestWriteOffNPLInputInfo info = AddRequestWriteOffNPLInputInfo.from(writeOffNPLInfo);
        TSRService.addRequestWriteOffNPL(info, isSchedule);
    }

    public static void approveLoss(WriteOffNPLInfo writeOffNPLInfo, AssignInfo assignInfo, boolean isSchedule) {
        new WriteOffNPLController().updateWriteOffNPL(writeOffNPLInfo);
        new AssignController().addAssign(assignInfo);

        ApproveWriteOffNPLInputInfo info = ApproveWriteOffNPLInputInfo.from(writeOffNPLInfo);
        TSRService.approveWriteOffNPL(info, isSchedule);

        AddOrUpdateAssignActionWriteOffNPLInputInfo assignInfoService = AddOrUpdateAssignActionWriteOffNPLInputInfo.from(assignInfo);
        TSRService.addOrUpdateAssignActionWriteOffNPL(assignInfoService, isSchedule);
    }

    public static void actionLoss(WriteOffNPLInfo writeOffNPLInfo, boolean isSchedule) {
        new WriteOffNPLController().updateWriteOffNPL(writeOffNPLInfo);

        ActionWriteOffNPLInputInfo info = ActionWriteOffNPLInputInfo.from(writeOffNPLInfo);
        ActionWriteOffNPLOutputInfo output = TSRService.actionWriteOffNPL(info, isSchedule);
    }

    public static void addRequestImpoundProduct(ImpoundProductInfo impoundProductInfo) {
        new ImpoundProductController().addImpoundProduct(impoundProductInfo);

        AddRequestImpoundProductInputInfo info = AddRequestImpoundProductInputInfo.from(impoundProductInfo);
        info.CreateDate = new Date();
        TSRService.addRequestImpoundProduct(info, true);
    }

    public static void addRequestImpoundProductOtherTeam(ImpoundProductInfo impoundProductInfo, boolean isSchedule) {
        new ImpoundProductController().addImpoundProductOtherTeam(impoundProductInfo);

        AddRequestImpoundProductInputInfo info = AddRequestImpoundProductInputInfo.from(impoundProductInfo);
        info.CreateDate = new Date();
        TSRService.addRequestImpoundProduct(info, isSchedule);
    }

    public static void approveImpoundProduct(ImpoundProductInfo impoundProductInfo, AssignInfo assignInfo) {
        new ImpoundProductController().updateImpoundProduct(impoundProductInfo);
        new AssignController().addAssign(assignInfo);

        ApproveImpoundProductInputInfo info = ApproveImpoundProductInputInfo.from(impoundProductInfo);
        info.LastUpdateDate = new Date();
        TSRService.approveImpoundProduct(info, true);

        AddOrUpdateAssignActionImpoundProductInputInfo assignInfoService = AddOrUpdateAssignActionImpoundProductInputInfo.from(assignInfo);
        assignInfoService.LastUpdateDate = new Date();
        TSRService.addOrUpdateAssignActionImpoundProduct(assignInfoService, true);
    }

    public static void actionImpoundProduct(ImpoundProductInfo impoundProductInfo, boolean isSchedule) {
        new ImpoundProductController().updateImpoundProduct(impoundProductInfo);

        ActionImpoundProductInputInfo info = ActionImpoundProductInputInfo.from(impoundProductInfo);
        info.LastUpdateDate = new Date();
        TSRService.actionImpoundProduct(info, isSchedule);
    }

    /*
    public static void addChangeProduct(ChangeProductInfo info) {

        ChangeProductInfo ret = new ChangeProductController().getChangeProductByRefNoByStatus(info.RefNo, ChangeProductStatus.APPROVED.toString());
        if (ret != null) {
            ret.Status = ChangeProductStatus.CANCELED.toString();
            new ChangeProductController().updateChangeProduct(ret);

            UpdateChangeProductInputInfo updateChangeProdInput = UpdateChangeProductInputInfo.from(ret);
            TSRService.updateChangeProduct(updateChangeProdInput, true);
        }
        new ChangeProductController().addChangeProduct(info);

        AddChangeProductInputInfo addChangeProdInput = AddChangeProductInputInfo.from(info);
        TSRService.addChangeProduct(addChangeProdInput, true);
    }
    */

    public static void addChangeProduct(ChangeProductInfo changeProduct, boolean isSchedule) {
        new ChangeProductController().addChangeProduct(changeProduct);
        AddChangeProductInputInfo addChangeProdInput = AddChangeProductInputInfo.from(changeProduct);
        TSRService.addChangeProduct(addChangeProdInput, isSchedule);
    }

    public static void updateChangeProduct(ChangeProductInfo changeProduct, boolean isSchedule) {
        new ChangeProductController().updateChangeProduct(changeProduct);
        UpdateChangeProductInputInfo updateChangeProdInput = UpdateChangeProductInputInfo.from(changeProduct);
        TSRService.updateChangeProduct(updateChangeProdInput, isSchedule);
    }

    public static void saveChangeProduct(final ProductStockInfo oldProductStock,
                                         final ProductStockInfo newProductStock, final ContractInfo contract,
                                         final ChangeProductInfo changeProduct, final AssignInfo assign,
                                         final ContractImageInfo contractImageInfo, final boolean isCredit) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                new ProductStockController().updateProductStock(oldProductStock);
                UpdateProductStockInputInfo oldPSInput = UpdateProductStockInputInfo.from(oldProductStock);
                oldPSInput.LastUpdateBy = BHPreference.employeeID();
                TSRService.updateProductStock(oldPSInput, true);

                new ProductStockController().updateProductStock(newProductStock);
                UpdateProductStockInputInfo newPSInput = UpdateProductStockInputInfo.from(newProductStock);
                newPSInput.LastUpdateBy = BHPreference.employeeID();
                TSRService.updateProductStock(newPSInput, true);

                new ContractController().updateContract(contract);
                UpdateContractInputInfo updateContInput = UpdateContractInputInfo.from(contract);
                TSRService.updateContract(updateContInput, true);


                if (isCredit) {
                    // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                    changeProduct.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                    changeProduct.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                    updateChangeProduct(changeProduct, true);
                } else {
                    // Fixed - [BHPROJ-0016-777] :: [Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
                    changeProduct.RequestEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                    changeProduct.EffectiveEmployeeLevelPath = BHPreference.currentTreeHistoryID();
                    addChangeProduct(changeProduct, true);
                }

                new AssignController().addAssign(assign);
                AddAssignInputInfo addAssignInput = AddAssignInputInfo.from(assign);
                TSRService.addAssign(addAssignInput, true);

                if (contractImageInfo != null) {
                    addContractImage(contractImageInfo, true);
                }
            }
        });
    }

    public static List<SendMoneyInfo> getSummarySendMoneyGroupByPaymentDate(
            String organizationCode, String EmpIDOfMember) {
        return new SendMoneyController()
                .getSummarySendMoneyGroupByPaymentDate(organizationCode, EmpIDOfMember);
    }

    public static SendMoneyInfo getSendMoneyByID(String sendMoneyID) {
        return new SendMoneyController().getSendMoneyByID(sendMoneyID);
    }

    public static void updateSendMoney(SendMoneyInfo info) {
        new SendMoneyController().updateSendMoney(info);

        UpdateSendMoneyInputInfo input = UpdateSendMoneyInputInfo.from(info);
        TSRService.updateSendMoney(input, true);
    }

    // Fixed - [BHPROJ-0026-865] :: [Meeting@TSR@11/02/59] [Android-บันทึก Transaction No. หลังจากนำส่งเงินแล้ว] ให้เรียกใช้ WS method ชื่อ UpdateSendMoneyTransactionNo แทน
    public static void updateSendMoneyTransactionNo(SendMoneyInfo info) {
        new SendMoneyController().updateSendMoneyTransactionNo(info);

        UpdateSendMoneyTransactionNoInputInfo input = UpdateSendMoneyTransactionNoInputInfo.from(info);
        TSRService.updateSendMoneyTransactionNo(input, true);
    }

    public static void deleteSalePaymentPeriodByRefNo(String refNo,
                                                      boolean isSchedule) {
        new SalePaymentPeriodController().deleteSalePaymentPeriodByRefNo(refNo);

        DeleteSalePaymentPeriodByRefNoInputInfo info = new DeleteSalePaymentPeriodByRefNoInputInfo();
        info.RefNo = refNo;
        TSRService.deleteSalePaymentPeriodByRefNo(info, isSchedule);
    }

    public static TripInfo getCurrentTrip() {
        return new TripController().getCurrentTrip();
    }

    public static void postponeAppointment(final SaleFirstPaymentChoiceFragment.ProcessType processType, final String refNo, final Date date, final int maxPaymentPeriodNumber, final boolean isSchedule) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                SalePaymentPeriodController sppController = new SalePaymentPeriodController();
                List<SalePaymentPeriodInfo> periods = sppController.getNextPayment(refNo);
                Date currentDate = new Date();
                if (periods != null) {

                    //BHPRJ00301-3990
                    /*for (SalePaymentPeriodInfo period : periods) {
                        PaymentAppointmentInfo appointment = new PaymentAppointmentInfo();
                        appointment.AppointmentID = DatabaseHelper.getUUID();
                        appointment.OrganizationCode = period.OrganizationCode;
                        appointment.RefNo = period.RefNo;
                        appointment.SalePaymentPeriodID = period.SalePaymentPeriodID;
                        appointment.AppointmentDate = date;
                        appointment.CreateDate = currentDate;
                        appointment.CreateBy = BHPreference.employeeID();
                        new PaymentAppointmentController().addAppointment(appointment);
                        AddPaymentAppointmentInputInfo paInput = AddPaymentAppointmentInputInfo.from(appointment);
                        AddPaymentAppointmentOutputInfo paOutput = TSRService.addPaymentAppointment(paInput, isSchedule);

                        period.PaymentAppointmentDate = date;
                        period.LastUpdateBy = BHPreference.employeeID();
                        period.LastUpdateDate = currentDate;
                        sppController.updateSalePaymentPeriod(period);

                        UpdateSalePaymentPeriodInputInfo updateSalePaymentPeriod = UpdateSalePaymentPeriodInputInfo.from(period);
                        TSRService.updateSalePaymentPeriod(updateSalePaymentPeriod, isSchedule);

                    }*/

                    /*if (periods.size() > 0) {
                        PaymentAppointmentInfo appointment = new PaymentAppointmentInfo();
                        appointment.AppointmentID = DatabaseHelper.getUUID();
                        appointment.OrganizationCode = BHPreference.organizationCode();
                        appointment.RefNo = periods.get(0).RefNo;
                        appointment.SalePaymentPeriodID = periods.get(0).SalePaymentPeriodID;
                        appointment.AppointmentDate = date;
                        appointment.CreateDate = currentDate;
                        appointment.CreateBy = BHPreference.employeeID();
                        new PaymentAppointmentController().addAppointment(appointment);
                        AddPaymentAppointmentInputInfo paInput = AddPaymentAppointmentInputInfo.from(appointment);
                        AddPaymentAppointmentOutputInfo paOutput = TSRService.addPaymentAppointment(paInput, isSchedule);

                        periods.get(0).PaymentAppointmentDate = date;
                        periods.get(0).LastUpdateBy = BHPreference.employeeID();
                        periods.get(0).LastUpdateDate = currentDate;
                        sppController.updateSalePaymentPeriod(periods.get(0));

                        UpdateSalePaymentPeriodInputInfo updateSalePaymentPeriod = UpdateSalePaymentPeriodInputInfo.from(periods.get(0));
                        TSRService.updateSalePaymentPeriod(updateSalePaymentPeriod, isSchedule);
                    }*/

                    if (processType == SaleFirstPaymentChoiceFragment.ProcessType.Credit || processType == SaleFirstPaymentChoiceFragment.ProcessType.NextPayment) {
                        List<AssignInfo> inserts = new AssignController().getAssignForPostpone(refNo, maxPaymentPeriodNumber, BHPreference.employeeID());
                        if (inserts != null && inserts.size() > 0) {
                            Date oldPaymentAppointmentDate = BHUtilities.parseDate(inserts.get(inserts.size() - 1).PaymentAppointmentDate, "dd/MM/yyyy", BHUtilities.LOCALE_THAI);
                            Date newPaymentAppointmentDate = BHUtilities.parseDate(date, "dd/MM/yyyy", BHUtilities.LOCALE_THAI);
                            if (oldPaymentAppointmentDate.equals(newPaymentAppointmentDate)) return;

                            List<AssignInfo> shiftAssign = new AssignController().getAssignShiftOrder(date, BHPreference.employeeID());
                            int order = 0;
                            boolean isOrderUpdated = false;
                            if (shiftAssign != null && shiftAssign.size() > 0) {
                                for (AssignInfo assign : shiftAssign) {
                                    if (!isOrderUpdated) {
                                        order = assign.Order;
                                    }

                                    if (assign.OrderExpect >= inserts.get(inserts.size() - 1).OrderExpect) {
                                        if (isOrderUpdated) {
                                            assign.Order = order;
                                            updateAssignForPostpone(assign, true);
                                            order++;
                                        } else {
                                            for (AssignInfo insert : inserts) {
                                                insert.Order = order;
                                                updateAssignForPostpone(insert, true);
                                                order++;
                                            }

                                            isOrderUpdated = true;

                                            assign.Order = order;
                                            updateAssignForPostpone(assign, true);
                                            order++;
                                        }
                                    }
                                }
                            }

                            if (!isOrderUpdated) {
                                order = order + 1;
                                for (AssignInfo assign : inserts) {
                                    assign.Order = order;
                                    updateAssignForPostpone(assign, true);
                                    order++;
                                }
                            }
                        }
                    }

                    for (SalePaymentPeriodInfo period : periods) {
                        if (maxPaymentPeriodNumber >= period.PaymentPeriodNumber && period.PaymentComplete == false) {
                            PaymentAppointmentInfo appointment = new PaymentAppointmentInfo();
                            appointment.AppointmentID = DatabaseHelper.getUUID();
                            appointment.OrganizationCode = period.OrganizationCode;
                            appointment.RefNo = period.RefNo;
                            appointment.SalePaymentPeriodID = period.SalePaymentPeriodID;
                            appointment.AppointmentDate = date;
                            appointment.AppointmentDetail = String.format("เลื่อนนัดชำระบน Android จากเดิมวันที่ %s", BHUtilities.dateFormat(period.PaymentAppointmentDate, "dd/MM/yyyy") );  //-- Fixed - [BHPROJ-0016-987] + [BHPROJ-0026-1019]
                            appointment.CreateDate = currentDate;
                            appointment.CreateBy = BHPreference.employeeID();
                            new PaymentAppointmentController().addAppointment(appointment);
                            AddPaymentAppointmentInputInfo paInput = AddPaymentAppointmentInputInfo.from(appointment);
                            AddPaymentAppointmentOutputInfo paOutput = TSRService.addPaymentAppointment(paInput, isSchedule);

                            /*** [START] :: Fixed - [BHPROJ-0024-2045] :: [Android] วันที่ครบกำหนดชำระ PaymentDueDate ของงวดที่ 2-n ทางคุณไพฑูรย์ต้องการให้คิดจากวันที่มีการเก็บเงินงวดแรก บวก 1 เดือน ไปเรื่อยๆ จนถึง n งวด  ***/
                            if(period.PaymentPeriodNumber == 1){
                                period.PaymentDueDate = date;
                            }
                            /*** [END] :: Fixed - [BHPROJ-0024-2045] :: [Android] วันที่ครบกำหนดชำระ PaymentDueDate ของงวดที่ 2-n ทางคุณไพฑูรย์ต้องการให้คิดจากวันที่มีการเก็บเงินงวดแรก บวก 1 เดือน ไปเรื่อยๆ จนถึง n งวด  ***/
                            period.PaymentAppointmentDate = date;
                            period.LastUpdateBy = BHPreference.employeeID();
                            period.LastUpdateDate = currentDate;
                            sppController.updateSalePaymentPeriod(period);

                            UpdateSalePaymentPeriodInputInfo updateSalePaymentPeriod = UpdateSalePaymentPeriodInputInfo.from(period);
                            TSRService.updateSalePaymentPeriod(updateSalePaymentPeriod, isSchedule);
                        }
                    }
                }
            }
        });
    }

    public static void updateSalePaymentPeriod(SalePaymentPeriodInfo info, boolean isSchedule) {
        new SalePaymentPeriodController().updateSalePaymentPeriod(info);

        UpdateSalePaymentPeriodInputInfo updateSalePaymentPeriod = UpdateSalePaymentPeriodInputInfo
                .from(info);
        TSRService.updateSalePaymentPeriod(updateSalePaymentPeriod,
                isSchedule);

    }

    public static List<PaymentInfo> getPaymentByRefNo(String organizationCode,
                                                      String refNo) {
        return new PaymentController().getPaymentByRefNo(organizationCode,
                refNo);
    }

    public static List<PaymentInfo> getPaymentByRefNoAndReceiptID(String organizationCode,
                                                                  String refNo, String receiptID) {
        return new PaymentController().getPaymentByRefNoAndReceiptID(organizationCode,
                refNo, receiptID);
    }

    public static PaymentInfo getPaymentRefNo(String refNo) {
        return new PaymentController().getPaymentRefNo(refNo);
    }

    public static AddressInfo getAddress(String refNo, AddressType type) {
        return new AddressController().getAddress(refNo, type);
    }

    public static List<AddressInfo> getAddress() {
        return new AddressController().getAddress();
    }

    public static void addSalePaymentPeriod(
            SalePaymentPeriodInfo salePaymentPeriod, boolean isSchedule) {
        new SalePaymentPeriodController()
                .addSalePaymentPeriod(salePaymentPeriod);

        AddSalePaymentPeriodInputInfo SalePaymentPeriod = AddSalePaymentPeriodInputInfo
                .from(salePaymentPeriod);
        AddSalePaymentPeriodOutputInfo output = TSRService
                .addSalePaymentPeriod(SalePaymentPeriod, isSchedule);
    }

    public static void addSalePaymentPeriodList(
            List<SalePaymentPeriodInfo> salePaymentPeriodList,
            boolean isSchedule) {
        new SalePaymentPeriodController()
                .addSalePaymentPeriodList(salePaymentPeriodList);

//        AddSalePaymentPeriodListInputInfo SalePaymentPeriodList = new AddSalePaymentPeriodListInputInfo();
//        SalePaymentPeriodList.salePaymentPeriodList = new SalePaymentPeriodInfo[salePaymentPeriodList
//              .size()];
//        for (int i = 0; i < salePaymentPeriodList.size(); i++) {
//            SalePaymentPeriodList.salePaymentPeriodList[i] = salePaymentPeriodList
//                    .get(i);
//        }
//        TSRService.addSalePaymentPeriodList(SalePaymentPeriodList, isSchedule);

        for (SalePaymentPeriodInfo s : salePaymentPeriodList) {
            AddSalePaymentPeriodInputInfo SalePaymentPeriod = AddSalePaymentPeriodInputInfo
                    .from(s);
            AddSalePaymentPeriodOutputInfo output = TSRService
                    .addSalePaymentPeriod(SalePaymentPeriod, isSchedule);
        }
    }

    public static void addContractWithSalePaymentPeriod(ContractInfo contract,
                                                        List<SalePaymentPeriodInfo> salePaymentPeriodList,
                                                        boolean isSchedule) {
        addContract(contract, isSchedule);
        addSalePaymentPeriodList(salePaymentPeriodList, isSchedule);
    }

    public static void addRequestChangeContract(ChangeContractInfo info) {
        new ChangeContractController().addChangeContract(info);

        AddRequestChangeContractInputInfo changeContractInfoService = AddRequestChangeContractInputInfo.from(info);
        TSRService.addRequestChangeContract(changeContractInfoService, true);
    }

    public static void approveChangeContract(ChangeContractInfo info,
                                             AssignInfo assign) {
        new ChangeContractController().updateChangeContract(info);
        new AssignController().addAssign(assign);

        ApproveChangeContractInputInfo approveChangeContractInfoService = ApproveChangeContractInputInfo
                .from(info);
        TSRService
                .ApproveChangeContract(approveChangeContractInfoService, true);

        AddOrUpdateAssignActionChangeContractInputInfo assignInfoService = AddOrUpdateAssignActionChangeContractInputInfo
                .from(assign);
        TSRService.AddOrUpdateAssignActionChangeContract(assignInfoService,
                true);
    }

    public static void actionChangeContract(ChangeContractInfo info) {
        new ChangeContractController().updateChangeContract(info);

        ActionChangeContractInputInfo actionChangeContractInfoService = ActionChangeContractInputInfo.from(info);
        TSRService.ActionChangeContract(actionChangeContractInfoService, true);
    }

    public static void actionChangeContract1(ChangeContractInfo info) {
        new ChangeContractController().updateChangeContractByCOMPLETED(info);

        ActionChangeContractInputInfo actionChangeContractInfoService = ActionChangeContractInputInfo.from(info);
        TSRService.ActionChangeContract(actionChangeContractInfoService, true);
    }

    public static void actionChangeContractWithSalePaymentPeriod(ChangeContractInfo changeContract, ContractInfo newContract, List<SalePaymentPeriodInfo> newSPPList) {
        if (changeContract != null) {
            actionChangeContract(changeContract);
        }
        addContractWithSalePaymentPeriod(newContract, newSPPList, true);
    }


    public static List<EmployeeInfo> getEmployees(String teamCode) {
        return new EmployeeController().getEmployeesByTeamCode(teamCode);
    }

    public static EmployeeInfo getEmployeeByID(String empID) {
        return new EmployeeController().getEmployeeByID(empID);
    }

    public static ChangeProductInfo getChangeProductByID(
            String OrganizationCode, String ChangeProductID) {
        return new ChangeProductController().getChangeProductByID(
                OrganizationCode, ChangeProductID);
    }

    /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
    public static List<ChangeProductInfo> getChangeProductList(String organizationCode, String teamCode, String searchText, String refNo) {
        List<ChangeProductInfo> ret = null;
        ret = new ChangeProductController().getChangeProductList(organizationCode, teamCode, searchText, refNo);
        if (ret == null && searchText != null) {
            GetChangeProductListInputInfo getChangeProdInput = new GetChangeProductListInputInfo();
            getChangeProdInput.OrganizationCode = organizationCode;
            getChangeProdInput.TeamCode = teamCode;
//            getChangeProdInput.Status = status;
            getChangeProdInput.SearchText = searchText;
            GetChangeProductListOutputInfo getChangeProdOutput = TSRService.getChangeProductList(getChangeProdInput, false);
            if (getChangeProdOutput != null && getChangeProdOutput.Info != null) {
                ret = getChangeProdOutput.Info;
                /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                /*
                for (ChangeProductInfo info : ret) {
                    //** Fixed - [BHPRJ00301-4073]
//                    importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), info.RefNo, false, false, true);
                    TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), info.RefNo);
                }
                */
                /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
            }
        }
        return ret;
    }

    /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */
    public static List<ChangeProductInfo> getChangeProductListForCredit(String organizationCode, String searchText, String refNo, String empId) {
        List<ChangeProductInfo> ret = null;
        ret = new ChangeProductController().getChangeProductListForCredit(organizationCode, searchText, refNo, empId);
        if (ret == null && searchText != null) {
            GetChangeProductListInputInfo getChangeProdInput = new GetChangeProductListInputInfo();
            getChangeProdInput.OrganizationCode = organizationCode;
            getChangeProdInput.SearchText = searchText;
            GetChangeProductListOutputInfo getChangeProdOutput = TSRService.getChangeProductListForCredit(getChangeProdInput, false);
            if (getChangeProdOutput != null && getChangeProdOutput.Info != null) {
                ret = getChangeProdOutput.Info;
                /*** [START] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
                /*
                for (ChangeProductInfo info : ret) {
                    TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), info.RefNo);
                }
                */
                /*** [END] :: Fixed - [BHPROJ-0016-983] :: [Android-Request ต่าง ๆ] ให้เรียก method importContractFromServer() หลังจากการเลือกสัญญานั้น ๆ ในหน้า list แล้วกดปุ่มถัดไป ***/
            }
        }
        return ret;
    }

    public static ImpoundProductInfo getImpoundProductByID(String OrganizationCode, String ImpoundProductID) {
        return new ImpoundProductController().getImpoundProductByID(OrganizationCode, ImpoundProductID);
    }

    public static List<ChangeContractInfo> getChangeContractByNewSaleIDReturnChangeContract(String OrganizationCode, String NewSaleID) {
        return new ChangeContractController().getChangeContractByNewSaleIDReturnChangeContract(OrganizationCode, NewSaleID);
    }

    public static void updateStatusCode(String refNo, String statusCode) {
        new ContractController().updateStatusCode(refNo, statusCode);
        UpdateContractInputInfo info = new UpdateContractInputInfo();
        info.RefNo = refNo;
        info.StatusCode = statusCode;

        TSRService.UpdateContractStatusCode(info, true);
    }

    public static DebtorCustomerInfo getDebCustometByID(String CustomerID) {
        return new DebtorCustomerController().getDebCustometByID(CustomerID);
    }

    public static List<EmployeeInfo> getEmployeeInfoSaleLeader(String teamCode) {
        return new EmployeeController().getEmployeesTeamCodeBySaleLeader(teamCode);
    }

    public static List<EmployeeInfo> getEmployeeInfoSubTeamLeader(String teamCode, String subTeamCode) {
        return new EmployeeController().getEmployeesTeamCodeBySubTeamLeader(teamCode, subTeamCode);
    }

    public static EmployeeInfo getEmpByempID(String empID, String teamCode) {
        return new EmployeeController().getEmpByEmpID(empID, teamCode);
    }
/*
    public static EmployeeInfo getEmpByempID_for_credit(String empID, String teamCode) {
        return new EmployeeController().getEmpByEmpID_for_credit(empID, teamCode);
    }
*/

    public static EmployeeInfo getEmployeeDetailByEmployeeIDAndPositionCode(String OrganizationCode, String empID, String PositionCode, String teamCode) {
        return new EmployeeController().getEmployeeByEmployeeIDAndPositionCode(OrganizationCode, empID, PositionCode, teamCode);
    }

    /*public static EmployeeInfo getEmployeeByEmployeeIDAndPositionCodeSaleAndCerdit(String OrganizationCode, String empID) {
        return new EmployeeController().getEmployeeByEmployeeIDAndPositionCodeSaleAndCerdit(OrganizationCode, empID);
    }*/

    public static EmployeeInfo getEmployeeByTreeHistoryIDAndEmployeeID(String OrganizationCode, String TreeHistoryID, String EmployeeID) {
        return new EmployeeController().getEmployeeByTreeHistoryIDAndEmployeeID(OrganizationCode, TreeHistoryID, EmployeeID);
    }

    public static ContractInfo getLastContract(String employeeID, String yearMonth) {
        return new ContractController().getLastContract(employeeID, yearMonth);
    }

    public static ReceiptInfo getLastReceipt(String employeeID, String yearMonth) {
        return new ReceiptController().getLastReceipt(employeeID, yearMonth);
    }

    public static ReceiptInfo getLastReceiptWithSaleCode(String saleCode, String yearMonth) {
        return new ReceiptController().getLastReceiptWithSaleCode(saleCode, yearMonth);
    }

    public static ChangeContractInfo getLastChangeContract(String employeeID, String yearMonth) {
        return new ChangeContractController().getLastChangeContract(employeeID, yearMonth);
    }

    public static ImpoundProductInfo getLastImpoundProduct(String employeeID, String yearMonth) {
        return new ImpoundProductController().getLastImpoundProduct(employeeID, yearMonth);
    }

    public static ChangeProductInfo getLastChangeProduct(String employeeID, String yearMonth) {
        return new ChangeProductController().getLastChangeProduct(employeeID, yearMonth);
    }

    public static ComplainInfo getLastComplain(String employeeID, String yearMonth) {
        return new ComplainController().getLastComplain(employeeID, yearMonth);
    }

    /*public enum DocumentGenType {
        //Contract,
        Receipt, ChangeContract, ImpoundProduct, ChangeProduct, ReturnProduct, Complain//, ProductRequisition
    }*/

    public enum DocumentGenType {
        //Contract, ProductRequisition,

        Receipt("2"), ChangeContract("3"), ReturnProduct("4"), ImpoundProduct("5"), ChangeProduct("6"),  Complain("7");

        DocumentGenType(String value) {
            this.value = value;
        }

        private final String value;

        public String getValue() {
            return value;
        }
    }

    /*** [START] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ ***/

    /*public static String getAutoGenerateDocumentID(String documentGenType, String subTeamCode, String saleCode) {

        Calendar c = getInstance();
        String dateString = BHUtilities.dateFormat(c.getTime(), "yyMM");
        String type = "0";

        Integer runningNumber = 0;

//        if (documentGenType.equals(DocumentGenType.Contract.toString())) {
//            *//** Contract : สัญญา **//*
//            type = "1";
//            ContractInfo tmpContract = TSRController.getLastContract(saleCode, dateString);
//            if (tmpContract != null) {
//                String runningNumberNow = tmpContract.CONTNO.substring(tmpContract.CONTNO.length() - 3);
//                runningNumber = Integer.valueOf(runningNumberNow);
//            }
//        } else
        if (documentGenType.equals(DocumentGenType.Receipt.toString())) {
            *//** Receipt : ใบเสร็จ **//*
            type = "2";
            ///ReceiptInfo tmpReceipt = TSRController.getLastReceipt(BHPreference.employeeID(), dateString);
            ReceiptInfo tmpReceipt = TSRController.getLastReceiptWithSaleCode(BHPreference.saleCode(), dateString);
            if (tmpReceipt != null) {
                String runningNumberNow = tmpReceipt.ReceiptCode.substring(tmpReceipt.ReceiptCode.length() - 4);
                runningNumber = Integer.valueOf(runningNumberNow);
            }
        } else if (documentGenType.equals(DocumentGenType.ChangeContract.toString())) {
            *//** ChangeContract : เปลี่ยนสัญญา **//*
            type = "3";
            ChangeContractInfo tmpChangeContract = TSRController.getLastChangeContract(BHPreference.employeeID(), dateString);
            if (tmpChangeContract != null) {
                String runningNumberNow = tmpChangeContract.ChangeContractPaperID.substring(tmpChangeContract.ChangeContractPaperID.length() - 3);
                runningNumber = Integer.valueOf(runningNumberNow);
            }
        } else if (documentGenType.equals(DocumentGenType.ReturnProduct.toString())) {
            *//** ReturnProduct : ส่งคืนสินค้าเข้าระบบ **//*
            type = "4";
            ReturnProductInfo tmpReturnProduct = TSRController.getLastReturnProduct(BHPreference.employeeID(), dateString);
            if (tmpReturnProduct != null) {
                String runningNumberNow = tmpReturnProduct.ReturnProductID.substring(tmpReturnProduct.ReturnProductID.length() - 3);
                runningNumber = Integer.valueOf(runningNumberNow);
            }
        } else if (documentGenType.equals(DocumentGenType.ImpoundProduct.toString())) {
            *//** ImpoundProduct : ถอดเครื่อง **//*
            type = "5";
            ImpoundProductInfo tmpImpoundProduct = TSRController.getLastImpoundProduct(BHPreference.employeeID(), dateString);
            if (tmpImpoundProduct != null) {
                String runningNumberNow = tmpImpoundProduct.ImpoundProductPaperID.substring(tmpImpoundProduct.ImpoundProductPaperID.length() - 3);
                runningNumber = Integer.valueOf(runningNumberNow);
            }
        } else if (documentGenType.equals(DocumentGenType.ChangeProduct.toString())) {
            *//** ChangeProduct : เปลี่ยนเครื่อง **//*
            type = "6";
            ChangeProductInfo tmpChangeProduct = TSRController.getLastChangeProduct(BHPreference.employeeID(), dateString);
            if (tmpChangeProduct != null) {
                String runningNumberNow = tmpChangeProduct.ChangeProductPaperID.substring(tmpChangeProduct.ChangeProductPaperID.length() - 3);
                runningNumber = Integer.valueOf(runningNumberNow);
            }
        } else if (documentGenType.equals(DocumentGenType.Complain.toString())) {
            *//** Complain : แจ้งปัญหา **//*
            type = "7";
            ComplainInfo tmpComplain = TSRController.getLastComplain(BHPreference.employeeID(), dateString);
            if (tmpComplain != null) {
                String runningNumberNow = tmpComplain.ComplainPaperID.substring(tmpComplain.ComplainPaperID.length() - 3);
                runningNumber = Integer.valueOf(runningNumberNow);
            }
        }

        runningNumber++;
        //subTeamCode = subTeamCode.replace("-", "");
//        String nextNumber = type + subTeamCode + saleCode + dateString
//                + String.format("%03d", runningNumber);
        String nextNumber = "";
        if (documentGenType.equals(DocumentGenType.Receipt.toString())) {
            nextNumber = type + saleCode + dateString
                    + String.format("%04d", runningNumber);
        } else {
            nextNumber = type + saleCode + dateString
                    + String.format("%03d", runningNumber);
        }

        return nextNumber;
    }*/

    public static String getAutoGenerateDocumentID(DocumentGenType documentGenType, String subTeamCode, String saleCode) {

        String dateString = BHPreference.getDateFormatGenerateDocument(); //BHUtilities.dateFormat(c.getTime(), "yyMM");
        int runningNumber = 0;
        String nextNumber = "";

        switch (documentGenType) {
            case Receipt:
                /** Receipt : ใบเสร็จ **/
                runningNumber = BHPreference.getRunningNumberReceipt() + 1;
                nextNumber = documentGenType.getValue() + saleCode + dateString + String.format("%04d", runningNumber);
                //BHPreference.setRunningNumberReceipt(runningNumber);
                break;

            case ChangeContract:
                /** ChangeContract : เปลี่ยนสัญญา **/
                runningNumber = BHPreference.getRunningNumberChangeContract() + 1;
                nextNumber = documentGenType.getValue() + saleCode + dateString + String.format("%03d", runningNumber);
                //BHPreference.setRunningNumberChangeContract(runningNumber);
                break;

            case ReturnProduct:
                /** ReturnProduct : ส่งคืนสินค้าเข้าระบบ **/
                runningNumber = BHPreference.getRunningNumberReturnProduct() + 1;
                nextNumber = documentGenType.getValue() + saleCode + dateString + String.format("%03d", runningNumber);
                //BHPreference.setRunningNumberReturnProduct(runningNumber);
                break;

            case ImpoundProduct:
                /** ImpoundProduct : ถอดเครื่อง **/
                runningNumber = BHPreference.getRunningNumberImpoundProduct() + 1;
                nextNumber = documentGenType.getValue() + saleCode + dateString + String.format("%03d", runningNumber);
                //BHPreference.setRunningNumberImpoundProduct(runningNumber);
                break;

            case ChangeProduct:
                /** ChangeProduct : เปลี่ยนเครื่อง **/
                runningNumber = BHPreference.getRunningNumberChangeProduct() + 1;
                nextNumber = documentGenType.getValue() + saleCode + dateString + String.format("%03d", runningNumber);
                //BHPreference.setRunningNumberChangeProduct(runningNumber);
                break;

            case Complain:
                /** Complain : แจ้งปัญหา **/
                runningNumber = BHPreference.getRunningNumberComplain() + 1;
                nextNumber = documentGenType.getValue() + saleCode + dateString + String.format("%03d", runningNumber);
                //BHPreference.setRunningNumberComplain(runningNumber);
                break;
        }

        return nextNumber;
    }

    public static void updateRunningNumber(DocumentGenType documentGenType, String documentNumber, String byEmployeeID) {

        String employeeID = BHPreference.employeeID();
        String saleCode = BHPreference.saleCode();
        String dateString = BHPreference.getDateFormatGenerateDocument();
        int digit = 0;

        int number = 0;

        switch (documentGenType) {
            case Receipt:
                /** Receipt : ใบเสร็จ **/
                if (documentNumber != null && !documentNumber.equals("")) {
                    digit = 4;
                    if (documentNumber.substring(0, 1).equals(documentGenType.getValue())){
                        if (documentNumber.substring(1, saleCode.length() + 1).equals(saleCode)){
                            if (documentNumber.substring(saleCode.length() + 1, saleCode.length() + dateString.length() + 1).equals(dateString)) {
                                if (documentNumber.length() == (documentGenType.getValue().length() + saleCode.length() + dateString.length() + digit)) {
                                    number = Integer.valueOf(documentNumber.substring(documentNumber.length() - digit));

                                    if (number == BHPreference.getRunningNumberReceipt() + 1){

                                        /*** [START] :: Fixed - [BHPROJ-1036-8870] - ใบเสร็จรับเงินหายไปหลังจากเครื่องค้าง ***/
                                        //BHPreference.setRunningNumberReceipt(number);
                                        ReceiptInfo receiptInfo = new ReceiptController().getReceiptByReceiptCode(documentNumber);
                                        if (receiptInfo != null) {
                                            BHPreference.setRunningNumberReceipt(number);
                                        }
                                        /*** [END] :: Fixed - [BHPROJ-1036-8870] - ใบเสร็จรับเงินหายไปหลังจากเครื่องค้าง  ***/
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case ChangeContract:
                /** ChangeContract : เปลี่ยนสัญญา **/
                if ((documentNumber != null && !documentNumber.equals("")) && (byEmployeeID != null && !byEmployeeID.equals(""))) {
                    digit = 3;
                    if (byEmployeeID.equals(employeeID)){
                        if (documentNumber.substring(0, 1).equals(documentGenType.getValue())){
                            if (documentNumber.substring(1, saleCode.length() + 1).equals(saleCode)){
                                if (documentNumber.substring(saleCode.length() + 1, saleCode.length() + dateString.length() + 1).equals(dateString)) {
                                    if (documentNumber.length() == (documentGenType.getValue().length() + saleCode.length() + dateString.length() + digit)) {
                                        number = Integer.valueOf(documentNumber.substring(documentNumber.length() - digit));

                                        if (number == BHPreference.getRunningNumberChangeContract() + 1){
                                            BHPreference.setRunningNumberChangeContract(number);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case ReturnProduct:
                /** ReturnProduct : ส่งคืนสินค้าเข้าระบบ **/
                if ((documentNumber != null && !documentNumber.equals("")) && (byEmployeeID != null && !byEmployeeID.equals(""))) {
                    digit = 3;
                    if (byEmployeeID.equals(employeeID)) {
                        if (documentNumber.substring(0, 1).equals(documentGenType.getValue())){
                            if (documentNumber.substring(1, saleCode.length() + 1).equals(saleCode)){
                                if (documentNumber.substring(saleCode.length() + 1, saleCode.length() + dateString.length() + 1).equals(dateString)) {
                                    if (documentNumber.length() == (documentGenType.getValue().length() + saleCode.length() + dateString.length() + digit)) {
                                        number = Integer.valueOf(documentNumber.substring(documentNumber.length() - digit));

                                        if (number == BHPreference.getRunningNumberReturnProduct() + 1){
                                            BHPreference.setRunningNumberReturnProduct(number);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case ImpoundProduct:
                /** ImpoundProduct : ถอดเครื่อง **/
                if ((documentNumber != null && !documentNumber.equals("")) && (byEmployeeID != null && !byEmployeeID.equals(""))) {
                    digit = 3;
                    if (byEmployeeID.equals(employeeID)) {
                        if (documentNumber.substring(0, 1).equals(documentGenType.getValue())){
                            if (documentNumber.substring(1, saleCode.length() + 1).equals(saleCode)){
                                if (documentNumber.substring(saleCode.length() + 1, saleCode.length() + dateString.length() + 1).equals(dateString)) {
                                    if (documentNumber.length() == (documentGenType.getValue().length() + saleCode.length() + dateString.length() + digit)) {
                                        number = Integer.valueOf(documentNumber.substring(documentNumber.length() - digit));

                                        if (number == BHPreference.getRunningNumberImpoundProduct() + 1){
                                            BHPreference.setRunningNumberImpoundProduct(number);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case ChangeProduct:
                /** ChangeProduct : เปลี่ยนเครื่อง **/
                if ((documentNumber != null && !documentNumber.equals("")) && (byEmployeeID != null && !byEmployeeID.equals(""))) {
                    digit = 3;
                    if (byEmployeeID.equals(employeeID)) {
                        if (documentNumber.substring(0, 1).equals(documentGenType.getValue())){
                            if (documentNumber.substring(1, saleCode.length() + 1).equals(saleCode)){
                                if (documentNumber.substring(saleCode.length() + 1, saleCode.length() + dateString.length() + 1).equals(dateString)) {
                                    if (documentNumber.length() == (documentGenType.getValue().length() + saleCode.length() + dateString.length() + digit)) {
                                        number = Integer.valueOf(documentNumber.substring(documentNumber.length() - digit));

                                        if (number == BHPreference.getRunningNumberChangeProduct() + 1){
                                            BHPreference.setRunningNumberChangeProduct(number);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;

            case Complain:
                /** Complain : แจ้งปัญหา **/
                if ((documentNumber != null && !documentNumber.equals("")) && (byEmployeeID != null && !byEmployeeID.equals(""))) {
                    digit = 3;
                    if (byEmployeeID.equals(employeeID)) {
                        if (documentNumber.substring(0, 1).equals(documentGenType.getValue())){
                            if (documentNumber.substring(1, saleCode.length() + 1).equals(saleCode)){
                                if (documentNumber.substring(saleCode.length() + 1, saleCode.length() + dateString.length() + 1).equals(dateString)) {
                                    if (documentNumber.length() == (documentGenType.getValue().length() + saleCode.length() + dateString.length() + digit)) {
                                        number = Integer.valueOf(documentNumber.substring(documentNumber.length() - digit));

                                        if (number == BHPreference.getRunningNumberComplain() + 1){
                                            BHPreference.setRunningNumberComplain(number);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                break;
        }
    }
    /*** [END] :: Fixed - [BHPROJ-0026-6574] ลูกค้าพบปัญหาเลขที่ใบเสร็จรับเงินซ้ำ   ***/


    public static void updateRunningNumberReference1AndReference2(String Reference1, String Reference2) {
        int digit = 4;
        String sendMoneyYearFormatGenerate = BHPreference.getSendMoneyYearFormatGenerate();
        String sendMoneyReference1FormatGenerate = BHPreference.getSendMoneyReference1FormatGenerate();

        if (Reference1 != null) {
            if (Reference1.length() == (sendMoneyYearFormatGenerate.length() + digit + sendMoneyReference1FormatGenerate.length())) {

                String strYear = Reference1.substring(0, sendMoneyYearFormatGenerate.length());
                if (strYear.equals(sendMoneyYearFormatGenerate)) {

                    String strEmployeeFormatGenerate = Reference1.substring(sendMoneyYearFormatGenerate.length() + digit, Reference1.length());
                    if (strEmployeeFormatGenerate.equals(sendMoneyReference1FormatGenerate)) {

                        String strRunningNumber = Reference1.substring(sendMoneyYearFormatGenerate.length(), sendMoneyYearFormatGenerate.length() + digit);
                        try {
                            int numReference1 = Integer.parseInt(strRunningNumber);
                            if (numReference1 == BHPreference.getRunningNumberReference1() + 1) {
                                BHPreference.setRunningNumberReference1(numReference1);
                            }
                        } catch (NumberFormatException nfe) {

                        }
                    }
                }
            }
        }

        if (Reference2 != null) {
            if (Reference2.length() == (sendMoneyYearFormatGenerate.length() + digit)) {

                String strYear = Reference2.substring(0, sendMoneyYearFormatGenerate.length());
                if (strYear.equals(sendMoneyYearFormatGenerate)) {

                    String strRunningNumber = Reference2.substring(sendMoneyYearFormatGenerate.length(), Reference2.length());
                    try {
                        int numReference2 = Integer.parseInt(strRunningNumber);
                        if (numReference2 == BHPreference.getRunningNumberReference2() + 1) {
                            BHPreference.setRunningNumberReference2(numReference2);
                        }
                    } catch (NumberFormatException nfe) {

                    }
                }
            }
        }
    }
    /*
    public static List<ContractInfo> getContractByStatus(String organizationCode, String saleTeamCode, String Status) {
        return new ContractController().getContractByStatus(organizationCode, saleTeamCode, Status);
    }
    */

    public static List<ImpoundProductInfo> getImpoundProductByStatus(String Status) {
        return new ImpoundProductController().getImpoundProductByStatus(Status);
    }

    public static List<ImpoundProductInfo> getImpoundProductByTeamCodeForSearch(String organizationCode, String TeamCode, String SearchText, String refNo) {
        return new ImpoundProductController().getImpoundProductByTeamCodeForSearch(organizationCode, TeamCode, SearchText, refNo);
    }

    public static List<ImpoundProductInfo> getImpoundProductOtherTeamByStatusRequestOrApproved(
            String organizationCode, String EmpID, String TeamCode, String SearchText, boolean PaymentComplete, String refNo) {
        return new ImpoundProductController().getImpoundProductOtherTeamByStatusRequestOrApproved(organizationCode, EmpID, TeamCode, SearchText, PaymentComplete, refNo);
    }


    /////////////////////////////////////////////// [END] Fixed - [BHPROJ-0024-418] ////////////////////////////////////////////////////////
    /* Fixed - [BHPROJ-0024-418] :: เปลี่ยนเครื่อง เพิ่มการค้นหา STATUS = 'F' */

    public static ContractInfo getContractByProductSerialNumberByStatus(String organizationCode, String productSerialNumber) {  // , String status
        // Called by ChangeProductCustomerListFragment.java
//        return new ContractController().getContractByProductSerialNumberByStatus(organizationCode, productSerialNumber, status);
        return new ContractController().getContractByProductSerialNumberByStatus(organizationCode, productSerialNumber);
    }

    public static ContractInfo getContractProductSerialByStatus(String organizationCode, String productSerialNumber) { // , String status
        // Called by ImpoundProductDetailFragment.java + ImpoundProductListOtherTeamFragment.java
//        ContractInfo output = new ContractController().getContractProductSerialByStatus(organizationCode, productSerialNumber, status);
        ContractInfo output = new ContractController().getContractProductSerialByStatus(organizationCode, productSerialNumber);
        return output;
    }

    public static ContractInfo getContractBySerialNo(String organizationCode, String productSerialNumber, String status) {
        // Called by ChangeContractListFragment.java + ImpoundProductListFragment.java + LossDetailFragment.java + LossMainFragment.java + ChangeProductDetailFragment.java
        ContractInfo output = new ContractController().getContractBySerialNo(organizationCode, productSerialNumber, status);
        return output;
    }

    /////////////////////////////////////////////// [END] Fixed - [BHPROJ-0024-418] ////////////////////////////////////////////////////////

    public static ContractInfo getContractBySerialNoForCredit(String organizationCode, String productSerialNumber, String status) {
        return new ContractController().getContractBySerialNoForCredit(organizationCode, productSerialNumber, status);
    }


//    public static void addComplain(ComplainInfo info, boolean isSchedule) {
//        new ComplainController().addComplain(info);
//        AddRequestComplainInputInfo Input = AddRequestComplainInputInfo
//                .from(info);
//        TSRService.addRequestComplain(Input, isSchedule);
//    }

    public static void addComplainStatusREQUEST(ComplainInfo info, boolean isSchedule) {
        new ComplainController().addComplainStatusREQUEST(info);

        AddComplainStatusREQUESTInputInfo Input = AddComplainStatusREQUESTInputInfo.from(info);
        TSRService.addComplainStatusREQUEST(Input, isSchedule);
    }

    public static void updateComplainStatusAPPROVED(ComplainInfo info, boolean isSchedule) {
        new ComplainController().updateComplainStatusAPPROVED(info);

        UpdateComplainStatusAPPROVEDInputInfo Input = UpdateComplainStatusAPPROVEDInputInfo.from(info);
        TSRService.updateComplainStatusAPPROVED(Input, isSchedule);
    }

    public static void updateComplainStatusCOMPLETED(ComplainInfo info, boolean isSchedule) {
        new ComplainController().updateComplainStatusCOMPLETED(info);

        UpdateComplainStatusCOMPLETEDInputInfo Input = UpdateComplainStatusCOMPLETEDInputInfo.from(info);
        TSRService.updateComplainStatusCOMPLETED(Input, isSchedule);
    }

    public static List<ManualDocumentInfo> getManualDocumentByDocumentNumber(String documentNumber) {
        return new ManualDocumentController().getManualDocumentByDocumentNumber(documentNumber);
    }

    public static ManualDocumentInfo getManualDocumentContractByDocumentNumber(String documentNumber) {
        return new ManualDocumentController().getManualDocumentContractByDocumentNumber(documentNumber);
    }

    public static ManualDocumentInfo getManualDocumentByManualDocTypeID(String ManualDocTypeID) {
        return new ManualDocumentController().getManualDocumentByManualDocTypeID(ManualDocTypeID);
    }

    public static void addManualDocument(ManualDocumentInfo manualDocumentInfo) {
        // (1) Local-DB
        new ManualDocumentController().addManualDocument(manualDocumentInfo);
        // (2) Server-DB
        AddManualDocumentInputInfo input = AddManualDocumentInputInfo.from(manualDocumentInfo);
        AddManualDocumentOutputInfo output = TSRService.addManualDocument(input, true);
    }

    public static void updateManualDocument(ManualDocumentInfo manualDocumentInfo) {
        // (1) Local-DB
        new ManualDocumentController().updateManualDocument(manualDocumentInfo);
        // (2) Server-DB
        UpdateManualDocumentInputInfo input = UpdateManualDocumentInputInfo.from(manualDocumentInfo);
        UpdateManualDocumentOutputInfo output = TSRService.updateManualDocument(input, true);
    }

    public static ContractInfo getContractByChangeContractID(String OrganizationCode, String ChangeContractID) {
        return new ChangeContractController().getContractByChangeContractID(OrganizationCode, ChangeContractID);
    }

    public static ChangeContractInfo getChangeContractByID(String OrganizationCode, String ChangeContractID) {
        return new ChangeContractController().getChangeContractByID(OrganizationCode, ChangeContractID);
    }

    public static SendMoneyInfo getSendMoneyByID(String organizationCode,
                                                 String sendMoneyID) {
        return new SendMoneyController().getSendMoneyByID(organizationCode,
                sendMoneyID);
    }

    public static ContractImageInfo getContractImage(String RefNo,
                                                     String ImageTypeCode) {
        return new ContractImageController().getContractImage(RefNo,
                ImageTypeCode);
    }

    public static List<ContractImageInfo> getContractImageList(String RefNo, String ImageTypeCode) {
        return new ContractImageController().getContractImageList(RefNo, ImageTypeCode);
    }

    public static void addContractImage(ContractImageInfo ContractImage,
                                        boolean isSchedule) {
        if (ContractImage != null) {
            ContractImageInfo contractImageByImageID = getContractImageByImageID(ContractImage.ImageID);
            if (contractImageByImageID == null) {
                /*ContractImageInfo ret = new ContractImageController()
                        .getContractImage(ContractImage.RefNo,
                                ContractImage.ImageTypeCode);
                if (ret == null) {*/
                new ContractImageController().addContractImage(ContractImage);

                AddContractImageInputInfo input = AddContractImageInputInfo.from(ContractImage);
                AddContractImageOutputInfo output = TSRService.addContractImage(input, isSchedule);

            } else {
                new ContractImageController().updateContractImage(ContractImage);

                UpdateContractImageInputInfo input = UpdateContractImageInputInfo.from(ContractImage);
                UpdateContractImageOutputInfo output = TSRService.updateContractImage(input, isSchedule);
            }
        }
    }

    public static void copyImageForChangeContract(ContractImageInfo oldContractImage, ContractImageInfo newContractImage) {
        if (oldContractImage != null && newContractImage != null) {
            CopyImageForChangeContractInputInfo input = new CopyImageForChangeContractInputInfo();
            input.oldContractImage = oldContractImage;
            input.newContractImage = newContractImage;

            CopyImageForChangeContractOutputInfo output = TSRService.copyImageForChangeContract(input, true);
        }
    }

    public static ContractImageInfo getContractImageByImageID(String imageID) {
        return new ContractImageController().getContractImageByImageID(imageID);
    }

    public static void addSaleAudit(SaleAuditInfo info, boolean isSchedule) {
        new SaleAuditController().addSaleAudit(info);

        AddSaleAuditInputInfo input = AddSaleAuditInputInfo.from(info);
        TSRService.addSaleAudit(input, isSchedule);
    }

    public static void updateSaleAudit(SaleAuditInfo info, boolean isSchedule) {
        new SaleAuditController().updateSaleAudit(info);

        UpdateSaleAuditInputInfo input = UpdateSaleAuditInputInfo.from(info);
        UpdateSaleAuditOutputInfo output = TSRService.updateSaleAudit(input, isSchedule);
    }

    public static void importContractFromServer(final String OrganizationCode, final String TeamCode, final String ContractRefNo) {

        // Fixed - [BHPRJ00301-4073] :: Call for Synch Transaction (Local2Server) before call method importContractFromServer()
        TransactionService.synchronizeTransactions(new TransactionService.TransactionServiceHandler() {
            @Override
            protected void onFinish(Exception e) {

                // Fixed - [BHPRJ00301-4073] :: Remove old parameter ==> boolean ImportPayment, boolean ImportReceipt, boolean ImportProductStock

                // Fixed - [BHPRJ00301-4073] :: If has existing must be delete old and re-insert new
                /*
                // Check existing in Local-DB
                ContractInfo contract = new ContractController().getContract(ContractRefNo);
                if (contract != null) {
                    return importResult;
                }
                */
                /** Delete related data in Local-DB **/
                new ContractController().deleteContractByRefNo(ContractRefNo);
                new ContractImageController().deleteContractImageByRefNo(ContractRefNo);
                new DebtorCustomerController().deleteDebcustomerByRefNo(ContractRefNo);
                new AddressController().deleteAddressByRefNo(ContractRefNo);
                new SalePaymentPeriodController().deleteSalePaymentPeriodByRefNo(ContractRefNo);
                new SalePaymentPeriodPaymentController().deleteSalePaymentPeriodPaymentByRefNo(ContractRefNo);
                new PaymentController().deletePaymentByRefNo(ContractRefNo);
                new ReceiptController().deleteReceiptByRefNo(ContractRefNo);
                new ContractCloseAccountController().deleteContractCloseAccountByRefNo(ContractRefNo);
                new CutDivisorContractController().deleteCutDivisorContractByRefNo(ContractRefNo);
                new CutOffContractController().deleteCutOffContractByRefNo(ContractRefNo);
                new ProductStockController().deleteProductStockByRefNo(ContractRefNo);
                new SaleAuditController().deleteSaleAuditByRefNo(ContractRefNo);
                new AssignController().deleteAssignByRefNo(ContractRefNo);
                new ManualDocumentWithdrawalController().deleteManualDocumentWithdrawalAll();

                SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
                try {
                    db.beginTransaction();

                    /*******************************/
                    /** Import data into Local-DB **/
                    /*******************************/

                    List<ReceiptInfo> receiptList = null;

                    GetContractByRefNoInputInfo cInputInfo = new GetContractByRefNoInputInfo();
                    cInputInfo.RefNo = ContractRefNo;
                    cInputInfo.OrganizationCode = OrganizationCode;
                    GetContractByRefNoOutputInfo cOutputInfo = TSRService.getContractByRefNo(cInputInfo, false);
                    if (cOutputInfo.ResultCode == 0 && cOutputInfo.Info != null) {

                        /** Contract : ข้อมูลสัญญา **/
                        new ContractController().addContract(cOutputInfo.Info);
                        importContractImage(cOutputInfo.Info);    // Fixed - [BHPROJ-0024-497]

                        ContractListInfo contListInfo = new ContractListInfo();
                        contListInfo.contractList.add(cOutputInfo.Info);

                        /** SalePaymentPeriod : เก็บงวดทั้งหมดของสัญญา **/
                        GetAllSalePaymentPeriodByContractListInputInfo getAllSppInput = new GetAllSalePaymentPeriodByContractListInputInfo();
                        getAllSppInput.setContractListValue(contListInfo.contractList);
                        GetAllSalePaymentPeriodByContractListOutputInfo getAllSppOutput = TSRService.getAllSalePaymentPeriodByContractList(getAllSppInput, false);
                        List<SalePaymentPeriodInfo> sppList = getAllSppOutput.Info;
                        if (sppList != null) {
                            for (SalePaymentPeriodInfo spp : sppList) {
                                new SalePaymentPeriodController().addSalePaymentPeriod(spp);
                            }
                        }

                        /** DebtorCustomer : ข้อมูลลูกหนี้ **/
                        GetAllDebtorByContractListInputInfo getAllDebCustInput = new GetAllDebtorByContractListInputInfo();
                        getAllDebCustInput.setContractListValue(contListInfo.contractList);
                        GetAllDebtorByContractListOutputInfo getAllDebCustOutput = TSRService.getAllDebtorByContractList(getAllDebCustInput, false);
                        List<DebtorCustomerInfo> debCustomerList = getAllDebCustOutput.Info;
                        if (debCustomerList != null) {
                            DebtorCustomerController dcController = new DebtorCustomerController();
                            for (DebtorCustomerInfo debCust : debCustomerList) {
                                if (dcController.getDebtorCustomer(debCust.CustomerID) == null) {
                                    dcController.addDebtorCustomer(debCust);
                                }
                            }
                        }

                        /** Address : ข้อมูลที่อยู่ **/
                        GetAllAddressByContractListInputInfo getAllAddrInput = new GetAllAddressByContractListInputInfo();
                        getAllAddrInput.setContractListValue(contListInfo.contractList);
                        GetAllAddressByContractListOutputInfo getAllAddrOutput = TSRService.getAllAddressByContractList(getAllAddrInput, false);
                        List<AddressInfo> addressList = getAllAddrOutput.Info;
                        if (addressList != null) {
                            AddressController aController = new AddressController();
                            for (AddressInfo address : addressList) {
                                if (aController.getAddress(ContractRefNo, address.AddressTypeCode) == null) {
                                    aController.addAddress(address);
                                }
                            }
                        }

                        /** Contract-CloseAccount : ข้อมูลการตัดสด **/
                        GetAllContractCloseAccountByContractListInputInfo getAllContCloseAcctInput = new GetAllContractCloseAccountByContractListInputInfo();
                        getAllContCloseAcctInput.setContractListValue(contListInfo.contractList);
                        GetAllContractCloseAccountByContractListOutputInfo getAllContCloseAcctOutput = TSRService.getAllContractCloseAccountByContractList(getAllContCloseAcctInput, false);
                        List<ContractCloseAccountInfo> contCloseAcctList = getAllContCloseAcctOutput.Info;
                        if (contCloseAcctList != null) {
                            for (ContractCloseAccountInfo contCloseAcct : contCloseAcctList) {
                                new ContractCloseAccountController().addContractCloseAccount(contCloseAcct);
                            }
                        }

                        /** Cut-Divisor-Contract : ข้อมูลการตัดตัวหาร **/
                        GetAllCutDivisorContractByContractListInputInfo getAllCutDivContInput = new GetAllCutDivisorContractByContractListInputInfo();
                        getAllCutDivContInput.setContractListValue(contListInfo.contractList);
                        GetAllCutDivisorContractByContractListOutputInfo getAllCutDivContOutput = TSRService.getAllCutDivisorContractByContractList(getAllCutDivContInput, false);
                        List<CutDivisorContractInfo> cutDivContList = getAllCutDivContOutput.Info;
                        if (cutDivContList != null) {
                            for (CutDivisorContractInfo cutDivCont : cutDivContList) {
                                new CutDivisorContractController().addCutDivisorContract(cutDivCont);
                            }
                        }

                        /** CutOff-Contract : ข้อมูลการตัดสัญญาออกจากฟอร์ม **/
                        GetAllCutOffContractByContractListInputInfo getAllCutOffContInput = new GetAllCutOffContractByContractListInputInfo();
                        getAllCutOffContInput.setContractListValue(contListInfo.contractList);
                        GetAllCutOffContractByContractListOutputInfo getAllCutOffContOutput = TSRService.getAllCutOffContractByContractList(getAllCutOffContInput, false);
                        List<CutOffContractInfo> cutOffContList = getAllCutOffContOutput.Info;
                        if (cutOffContList != null) {
                            for (CutOffContractInfo cutOffCont : cutOffContList) {
                                new CutOffContractController().addCutOffContract(cutOffCont);
                            }
                        }

                        /** ProductStock : ข้อมูลสินค้า **/
//                if (ImportProductStock == true) {
                        GetAllProductStockByContractListInputInfo getAllProductStockInput = new GetAllProductStockByContractListInputInfo();
                        getAllProductStockInput.setContractListValue(contListInfo.contractList);
                        GetAllProductStockByContractListOutputInfo getAllProductStockOutput = TSRService.GetAllProductStockByContractList(getAllProductStockInput, false);
                        List<ProductStockInfo> productStockList = getAllProductStockOutput.Info;
                        if (productStockList != null) {
                            ProductStockController pStockController = new ProductStockController();
                            for (ProductStockInfo pInfo : productStockList) {
                                if (pStockController.getProductStock(pInfo.ProductSerialNumber) == null) {
                                    pStockController.addProductStock(pInfo);
                                }
                            }
                        }
//                }

                        /** Payment : ข้อมูลการชำระเงินของสัญญานั้น ๆ **/
//                if (ImportPayment == true) {
                        GetAllPaymentByContractListInputInfo getAllPayInput = new GetAllPaymentByContractListInputInfo();
                        getAllPayInput.setContractListValue(contListInfo.contractList);
                        GetAllPaymentByContractListOutputInfo getAllPayOutput = TSRService.getAllPaymentByContractList(getAllPayInput, false);
                        List<PaymentInfo> paymentList = getAllPayOutput.Info;
                        if (paymentList != null) {
                            for (PaymentInfo payment : paymentList) {
                                new PaymentController().addPayment(payment);
                            }
                        }
//                }

//                if (ImportReceipt == true) {
                        /** SalePaymentPeriodPayment : ข้อมูลการชำระเงินของสัญญานั้น ๆ แบ่งตามงวดการชำระ (Associated Table) **/
                        GetAllSalePaymentPeriodPaymentByContractListInputInfo getAllSpppInput = new GetAllSalePaymentPeriodPaymentByContractListInputInfo();
                        getAllSpppInput.setContractListValue(contListInfo.contractList);
                        GetAllSalePaymentPeriodPaymentByContractListOutputInfo getAllSpppOutput = TSRService.getAllSalePaymentPeriodPaymentByContractList(getAllSpppInput, false);
                        List<SalePaymentPeriodPaymentInfo> spppList = getAllSpppOutput.Info;
                        if (spppList != null) {
                            for (SalePaymentPeriodPaymentInfo sppp : spppList) {
                                new SalePaymentPeriodPaymentController().addSalePaymentPeriodPayment(sppp);
                            }
                        }
                        /** Receipt : ข้อมูลใบเสร็จตามการชำระเงินของสัญญานั้น ๆ แบ่งตามงวดการชำระ  **/
                        GetAllReceiptByContractListInputInfo getAllReceiptInput = new GetAllReceiptByContractListInputInfo();
                        getAllReceiptInput.setContractListValue(contListInfo.contractList);
                        GetAllReceiptByContractListOutputInfo getAllReceiptOutput = TSRService.getAllReceiptByContractList(getAllReceiptInput, false);
                        receiptList = getAllReceiptOutput.Info;
                        if (receiptList != null) {
                            for (ReceiptInfo receipt : receiptList) {
                                new ReceiptController().addReceipt(receipt);
                            }
                        }
//                }

                        /** SaleAudit : ข้อมูลการตรวจสอบลูกค้าของสัญญานั้น ๆ **/
                        GetAllSaleAuditByContractListInputInfo getAllSaleAuditInput = new GetAllSaleAuditByContractListInputInfo();
                        getAllSaleAuditInput.setContractListValue(contListInfo.contractList);
                        GetAllSaleAuditByContractListOutputInfo getAllSaleAuditOutput = TSRService.getAllSaleAuditByContractList(getAllSaleAuditInput, false);
                        List<SaleAuditInfo> saleAuditList = getAllSaleAuditOutput.Info;
                        if (saleAuditList != null) {
                            for (SaleAuditInfo saleAudit : saleAuditList) {
                                SaleAuditInfo chkExist = new SaleAuditController().getSaleAuditBySaleAuditID(saleAudit.SaleAuditID);
                                if (chkExist != null) {
                                    new SaleAuditController().updateSaleAudit(saleAudit);
                                } else {
                                    new SaleAuditController().addSaleAudit(saleAudit);
                                }
                            }
                        }

                        /** Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ **/
                        GetAllAssignByContractListInputInfo getAllAssignInput = new GetAllAssignByContractListInputInfo();
                        getAllAssignInput.setContractListValue(contListInfo.contractList);
                        GetAllAssignByContractListOutputInfo getAllAssignOutput = TSRService.getAllAssignByContractList(getAllAssignInput, false);
                        List<AssignInfo> assignList = getAllAssignOutput.Info;
                        if (assignList != null) {
                            for (AssignInfo assign : assignList) {
                                AssignInfo chkExist = new AssignController().getAssignByAssignID(assign.AssignID);
                                if (chkExist != null) {
                                    new AssignController().updateAssignByAssignID(assign);
                                } else {
                                    new AssignController().addAssign(assign);
                                }
                            }
                        }

                        /** ManualDocumentWithdrawal : ข้อมูลการเบิก-คืนเอกสารมือ **/
                        GetAllActiveManualDocumentWithdrawalByTeamInputInfo getAllManualWithdrawalInput = new GetAllActiveManualDocumentWithdrawalByTeamInputInfo();
                        getAllManualWithdrawalInput.TeamCode = BHPreference.teamCode();
                        GetAllActiveManualDocumentWithdrawalByTeamOutputInfo getAllManualWithdrawalOutput = TSRService.getAllActiveManualDocumentWithdrawalByTeam(getAllManualWithdrawalInput, false);
                        List<ManualDocumentWithdrawalInfo> manualWithdrawalList = getAllManualWithdrawalOutput.Info;
                        if (manualWithdrawalList != null) {
                            for (ManualDocumentWithdrawalInfo item : manualWithdrawalList) {
                                new ManualDocumentWithdrawalController().addManualDocumentWithdrawal(item);
                            }
                        }

                        /** ManualDocument : ข้อมูลการใช้เอกสารมือ **/
                        int iDocCnt = (contListInfo.contractList != null ? contListInfo.contractList.size() : 0) + (receiptList != null ? receiptList.size() : 0);
                        String[] docNum = new String[iDocCnt];
                        for (int i = 0; i < iDocCnt; i++) {
                            int j = 0;
                            // (1) Insert list of Contract (docNum) to retrieve Contract-ManualDocument
                            if (contListInfo.contractList != null) {
                                for (ContractInfo cont : contListInfo.contractList) {
                                    docNum[j] = cont.RefNo;
                                    j++;
                                }
                            }
                            // (2) Insert list of Receipt (docNum) to retrieve Receipt-ManualDocument
                            if (receiptList != null) {
                                for (ReceiptInfo rcp : receiptList) {
                                    docNum[j] = rcp.ReceiptID;
                                    j++;
                                }
                            }
                        }
                        GetAllManualDocumentByDocumentNumberListInputInfo getAllManualInput = new GetAllManualDocumentByDocumentNumberListInputInfo();
                        getAllManualInput.documentNumberList = docNum;
                        GetAllManualDocumentByDocumentNumberListOutputInfo getAllManualOutput = TSRService.getAllManualDocumentByDocumentNumberList(getAllManualInput, false);
                        List<ManualDocumentInfo> manualList = getAllManualOutput.Info;
                        if (manualList != null) {
                            for (ManualDocumentInfo item : manualList) {
                                ManualDocumentInfo chkExist = new ManualDocumentController().getManualDocumentByDocumentID(item.DocumentID);
                                if (chkExist != null) {
                                    new ManualDocumentController().updateManualDocument(item);
                                } else {
                                    new ManualDocumentController().addManualDocument(item);
                                }
                            }
                        }

                /*
                // DocumentHistory : ข้อมูลการพิมพ์เอกสารต่าง ๆ จากระบบ (ยกเว้นใบรับคืนเครื่อง + ใบนำส่งเงิน)
                int iDocCntDocHist = (contListInfo.contractList != null ? contListInfo.contractList.size() : 0) + (receiptList != null ? receiptList.size() : 0) + (manualList != null ? manualList.size() : 0);
                String[] docNumDocHist = new String[iDocCntDocHist];
                for (int i = 0; i < iDocCntDocHist; i++) {
                    int j = 0;
                    // (1) Insert list of Contract+Receipt (docNum) to retrieve DocumentHistory
                    if (docNum != null) {
                        for (String item : docNum) {
                            docNumDocHist[j] = item;
                            j++;
                        }
                    }
                    // (2) Insert list of ManualDocument (manualList :- Contract_ManualDocument + Receipt_ManualDocument)
                    if (manualList != null) {
                        for (ManualDocumentInfo mDoc : manualList) {
                            docNumDocHist[j] = mDoc.DocumentID;
                            j++;
                        }
                    }
                }
                GetAllDocumentHistoryByDocumentNumberListInputInfo getAllDocumentHistoryInput = new GetAllDocumentHistoryByDocumentNumberListInputInfo();
                getAllDocumentHistoryInput.documentNumberList = docNumDocHist;
                getAllDocumentHistoryInput.TeamCode = BHPreference.teamCode();
                GetAllDocumentHistoryByDocumentNumberListOutputInfo getAllDocumentHistoryOutput = TSRService.getAllDocumentHistoryByDocumentNumberList(getAllDocumentHistoryInput, false);
                List<DocumentHistoryInfo> documentHistoryList = getAllDocumentHistoryOutput.Info;
                if (documentHistoryList != null) {
                    for (DocumentHistoryInfo item : documentHistoryList) {
                    	DocumentHistoryInfo chkExist = new DocumentHistoryController().getDocumentHistoryByID(OrganizationCode, item.PrintHistoryID);
                    	if (chkExist != null) {
                    		new DocumentHistoryController().updateDocumentHistory(item);
                    	} else {
                    		new DocumentHistoryController().addDocumentHistory(item);
                    	}
                    }
                }
                */

                        /** DocumentHistory : ข้อมูลการพิมพ์เอกสารต่าง ๆ จากระบบ (ยกเว้นใบรับคืนเครื่อง + ใบนำส่งเงิน) **/
                        GetAllActiveDocumentHistoryByTeamCodeInputInfo getAllDocumentHistoryInput = new GetAllActiveDocumentHistoryByTeamCodeInputInfo();
                        getAllDocumentHistoryInput.TeamCode = BHPreference.teamCode();
                        GetAllActiveDocumentHistoryByTeamCodeOutputInfo getAllDocumentHistoryOutput = TSRService.getAllActiveDocumentHistoryByTeamCode(getAllDocumentHistoryInput, false);
                        List<DocumentHistoryInfo> documentHistoryList = getAllDocumentHistoryOutput.Info;
                        if (documentHistoryList != null) {
                            for (DocumentHistoryInfo item : documentHistoryList) {
                                DocumentHistoryInfo chkExist = new DocumentHistoryController().getDocumentHistoryByID(item.OrganizationCode, item.PrintHistoryID);
                                if (chkExist != null) {
                                    new DocumentHistoryController().updateDocumentHistory(item);
                                } else {
                                    new DocumentHistoryController().addDocumentHistory(item);
                                }
                            }
                        }


                        /** Employee + EmployeeDetail : ข้อมูลพนักงาน ของ (พนักงานขาย + หัวหน้าทีม) ของสัญญานั้น ๆ  **/
//                        new TSRController().importEmployeeByEmployeeCode(OrganizationCode, cOutputInfo.Info.SaleEmployeeCode, cOutputInfo.Info.SaleTeamCode);

                    }   // if (cOutputInfo.ResultCode == 0 && cOutputInfo.Info != null) ===> Check existing on Server-DB

                    db.setTransactionSuccessful();
                } catch (Exception ex) {
                    // TODO: handle exception
                    ex.printStackTrace();
                    throw ex;
                } finally {
                    db.endTransaction();
                    DatabaseManager.getInstance().closeDatabase();
                }

            }
        });

    }


//    public static boolean importContractFromServerForCredit(String OrganizationCode, String RefNo, boolean ImportPayment,
//                                                   boolean ImportReceipt, boolean ImportProductStock) {
//        boolean importResult = false;
//        ContractInfo contract = new ContractController().getContract(RefNo);
//        if (contract != null) {
//            return importResult;
//        }
//
//        SQLiteDatabase db = DatabaseManager.getInstance().openDatabase();
//        try {
//            db.beginTransaction();
//
//            /*******************************/
//            /** Import data into Local-DB **/
//            /*******************************/
//
//            GetContractByRefNoInputInfo cInputInfo = new GetContractByRefNoInputInfo();
//            cInputInfo.RefNo = RefNo;
//            cInputInfo.OrganizationCode = OrganizationCode;
//            GetContractByRefNoOutputInfo cOutputInfo = TSRService.getContractByRefNo(cInputInfo, false);
//            if (cOutputInfo.ResultCode == 0 && cOutputInfo.Info != null) {
//
//                /** Contract : ข้อมูลสัญญา **/
//                new ContractController().addContract(cOutputInfo.Info);
//                importContractImage(cOutputInfo.Info);
//
//                ContractListInfo contListInfo = new ContractListInfo();
//                contListInfo.contractList.add(cOutputInfo.Info);
//
//                /** SalePaymentPeriod : เก็บงวดทั้งหมดของสัญญา **/
//                GetAllSalePaymentPeriodByContractListInputInfo getAllSppInput = new GetAllSalePaymentPeriodByContractListInputInfo();
//                getAllSppInput.setContractListValue(contListInfo.contractList);
//                GetAllSalePaymentPeriodByContractListOutputInfo getAllSppOutput = TSRService.getAllSalePaymentPeriodByContractList(getAllSppInput, false);
//                List<SalePaymentPeriodInfo> sppList = getAllSppOutput.Info;
//                if (sppList != null) {
//                    for (SalePaymentPeriodInfo spp : sppList) {
//                        new SalePaymentPeriodController().addSalePaymentPeriod(spp);
//                    }
//                }
//
//                /** DebtorCustomer : ข้อมูลลูกหนี้ **/
//                GetAllDebtorByContractListInputInfo getAllDebCustInput = new GetAllDebtorByContractListInputInfo();
//                getAllDebCustInput.setContractListValue(contListInfo.contractList);
//                GetAllDebtorByContractListOutputInfo getAllDebCustOutput = TSRService.getAllDebtorByContractList(getAllDebCustInput, false);
//                List<DebtorCustomerInfo> debCustomerList = getAllDebCustOutput.Info;
//                if (debCustomerList != null) {
//                    DebtorCustomerController dcController = new DebtorCustomerController();
//                    for (DebtorCustomerInfo debCust : debCustomerList) {
//                        if (dcController.getDebtorCustomer(debCust.CustomerID) == null) {
//                            dcController.addDebtorCustomer(debCust);
//                        }
//                    }
//                }
//
//                /** Address : ข้อมูลที่อยู่ **/
//                GetAllAddressByContractListInputInfo getAllAddrInput = new GetAllAddressByContractListInputInfo();
//                getAllAddrInput.setContractListValue(contListInfo.contractList);
//                GetAllAddressByContractListOutputInfo getAllAddrOutput = TSRService.getAllAddressByContractList(getAllAddrInput, false);
//                List<AddressInfo> addressList = getAllAddrOutput.Info;
//                if (addressList != null) {
//                    AddressController aController = new AddressController();
//                    for (AddressInfo address : addressList) {
//                        if (aController.getAddress(RefNo, address.AddressTypeCode) == null) {
//                            aController.addAddress(address);
//                        }
//                    }
//                }
//
//                /** ProductStock : ข้อมูลสินค้า **/
//                if (ImportProductStock == true) {
//                    GetAllProductStockByContractListInputInfo getAllProductStockInput = new GetAllProductStockByContractListInputInfo();
//                    getAllProductStockInput.setContractListValue(contListInfo.contractList);
//                    GetAllProductStockByContractListOutputInfo getAllProductStockOutput = TSRService.GetAllProductStockByContractList(getAllProductStockInput, false);
//                    List<ProductStockInfo> productStockList = getAllProductStockOutput.Info;
//                    if (productStockList != null) {
//                        ProductStockController pStockController = new ProductStockController();
//                        for (ProductStockInfo pInfo : productStockList) {
//                            if (pStockController.getProductStock(pInfo.ProductSerialNumber) == null) {
//                                pStockController.addProductStock(pInfo);
//                            }
//                        }
//                    }
//                }
//
//                /** Payment : ข้อมูลการชำระเงินของสัญญานั้น ๆ **/
//                if (ImportPayment == true) {
//                    GetAllPaymentByContractListInputInfo getAllPayInput = new GetAllPaymentByContractListInputInfo();
//                    getAllPayInput.setContractListValue(contListInfo.contractList);
//                    GetAllPaymentByContractListOutputInfo getAllPayOutput = TSRService.getAllPaymentByContractList(getAllPayInput, false);
//                    List<PaymentInfo> paymentList = getAllPayOutput.Info;
//                    if (paymentList != null) {
//                        for (PaymentInfo payment : paymentList) {
//                            new PaymentController().addPayment(payment);
//                        }
//                    }
//                }
//
//                if (ImportReceipt == true) {
//                    /** SalePaymentPeriodPayment : ข้อมูลการชำระเงินของสัญญานั้น ๆ แบ่งตามงวดการชำระ (Associated Table) **/
//                    GetAllSalePaymentPeriodPaymentByContractListInputInfo getAllSpppInput = new GetAllSalePaymentPeriodPaymentByContractListInputInfo();
//                    getAllSpppInput.setContractListValue(contListInfo.contractList);
//                    GetAllSalePaymentPeriodPaymentByContractListOutputInfo getAllSpppOutput = TSRService.getAllSalePaymentPeriodPaymentByContractList(getAllSpppInput, false);
//                    List<SalePaymentPeriodPaymentInfo> spppList = getAllSpppOutput.Info;
//                    if (spppList != null) {
//                        for (SalePaymentPeriodPaymentInfo sppp : spppList) {
//                            new SalePaymentPeriodPaymentController().addSalePaymentPeriodPayment(sppp);
//                        }
//                    }
//                    /** Receipt : ข้อมูลใบเสร็จตามการชำระเงินของสัญญานั้น ๆ แบ่งตามงวดการชำระ  **/
//                    GetAllReceiptByContractListInputInfo getAllReceiptInput = new GetAllReceiptByContractListInputInfo();
//                    getAllReceiptInput.setContractListValue(contListInfo.contractList);
//                    GetAllReceiptByContractListOutputInfo getAllReceiptOutput = TSRService.getAllReceiptByContractList(getAllReceiptInput, false);
//                    List<ReceiptInfo> receiptList = getAllReceiptOutput.Info;
//                    if (receiptList != null) {
//                        for (ReceiptInfo receipt : receiptList) {
//                            new ReceiptController().addReceipt(receipt);
//                        }
//                    }
//                }
//            }
//
//            db.setTransactionSuccessful();
//            importResult = true;
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            throw e;
//        } finally {
//            db.endTransaction();
//            DatabaseManager.getInstance().closeDatabase();
//        }
//
//        return importResult;
//    }


//    public static void importContract(final String OrganizationCode, final String TeamCode) {
//        processTransaction(new Runnable() {
//            @Override
//            public void run() {
//                /** Delete all in Local-DB **/
//                new ContractController().deleteContractAll();
//                new ContractImageController().deleteContractImageAll();
//                new DebtorCustomerController().deleteDebtorCustomerAll();
//                new AddressController().deleteAddressAll();
//                new ContractCloseAccountController().deleteContractCloseAccountAll();
//                new CutDivisorContractController().deleteCutDivisorContractAll();
//                new CutOffContractController().deleteCutOffContractAll();
//                new SalePaymentPeriodController().deleteSalePaymentPeriodAll();
//                new SalePaymentPeriodPaymentController().deleteSalePaymentPeriodPaymentAll();
//                new PaymentController().deletePaymentAll();
//                new ReceiptController().deleteReceiptAll();
//                new PaymentAppointmentController().deletePaymentAppointmentAll();
//                new AssignController().deleteAssignAll();
//                new SaleAuditController().deleteSaleAuditAll();
//                new ManualDocumentController().deleteManualDocumentAll();
//                new ManualDocumentWithdrawalController().deleteManualDocumentWithdrawalAll();
//                new DocumentHistoryController().deleteDocumentHistoryAll();
//                new SendDocumentController().deleteSendDocumentAll();
//                new SendDocumentDetailController().deleteSendDocumentDetailAll();
//
//                /**Delete Folder Picture*/
//                File delFolder = new File(BHStorage.getFolder(BHStorage.FolderType.Picture));
//                deleteDir(delFolder);
//
//                GetAllContractInputInfo getContInput = new GetAllContractInputInfo();
//                getContInput.OrganizationCode = OrganizationCode; // BHPreference.organizationCode();
//                getContInput.SaleTeamCode = TeamCode; // BHPreference.teamCode();
//                //getContInput.StatusCode = "00";
//                getContInput.FortnightNumber = 0;
//                getContInput.FortnightYear = 0;
//                GetAllContractOutputInfo getContOutput = TSRService.getAllContract(getContInput, false);
//
//
//                /*******************************/
//                /** Import data into Local-DB **/
//                /*******************************/
//
//                List<ContractInfo> contractList = getContOutput.Info;
//
//                ContractListInfo contListInfo = new ContractListInfo();
//                List<ReceiptInfo> receiptList = new ArrayList<ReceiptInfo>();
//
//                if (contractList != null) {
//                    /** Contract : ข้อมูลสัญญา **/
//                    for (ContractInfo cont : contractList) {
//                        new ContractController().addContract(cont);
//                        importContractImage(cont);
//                    }
//
//                    contListInfo.contractList = contractList;
//
//                    /*GetAllContractImageByContractListInputInfo getAllContractImageInput = new GetAllContractImageByContractListInputInfo();
//                    getAllContractImageInput.setContractListValue(contListInfo.contractList);
//                    GetAllContractImageByContractListOutputInfo getAllContractImageOutput = TSRService.getAllContractImageByContractList(getAllContractImageInput, false);
//                    List<ContractImageInfo> contractImageList = getAllContractImageOutput.Info;
//                    if (contractImageList != null) {
//                        for (ContractImageInfo contractImageInfo : contractImageList) {
//                            new ContractImageController().addContractImage(contractImageInfo);
//
//                            *//**Create Folder*//*
//                            File newFolder = new File(String.format("%s/%s/%s/", BHStorage.getFolder(BHStorage.FolderType.Picture), contractImageInfo.RefNo, contractImageInfo.ImageTypeCode));
//                            if (!newFolder.exists() || !newFolder.isDirectory()) {
//                                if (!newFolder.isDirectory()) {
//                                    newFolder.delete();
//                                }
//                                newFolder.mkdirs();
//                            }
//
//                            *//**Create Image*//*
//                            File importImage = new File(newFolder, contractImageInfo.ImageName);
//                            byte[] Decoded = Base64.decode(contractImageInfo.ImageData, Base64.DEFAULT);
//
//                            try {
//                                importImage.createNewFile();
//                                FileOutputStream fos = new FileOutputStream(importImage);
//                                fos.write(Decoded);
//                                fos.flush();
//                                fos.close();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    }*/
//
//                    /** SalePaymentPeriod : เก็บงวดทั้งหมดของสัญญา **/
//                    GetAllSalePaymentPeriodByContractListInputInfo getAllSppInput = new GetAllSalePaymentPeriodByContractListInputInfo();
//                    getAllSppInput.setContractListValue(contListInfo.contractList);
//                    GetAllSalePaymentPeriodByContractListOutputInfo getAllSppOutput = TSRService.getAllSalePaymentPeriodByContractList(getAllSppInput, false);
//                    List<SalePaymentPeriodInfo> sppList = getAllSppOutput.Info;
//                    if (sppList != null) {
//                        for (SalePaymentPeriodInfo spp : sppList) {
//                            new SalePaymentPeriodController().addSalePaymentPeriod(spp);
//                        }
//                    }
//
//                    /** DebtorCustomer : ข้อมูลลูกหนี้ **/
//                    GetAllDebtorByContractListInputInfo getAllDebCustInput = new GetAllDebtorByContractListInputInfo();
//                    getAllDebCustInput.setContractListValue(contListInfo.contractList);
//                    GetAllDebtorByContractListOutputInfo getAllDebCustOutput = TSRService.getAllDebtorByContractList(getAllDebCustInput, false);
//                    List<DebtorCustomerInfo> debCustomerList = getAllDebCustOutput.Info;
//                    if (debCustomerList != null) {
//                        for (DebtorCustomerInfo debCust : debCustomerList) {
//                            new DebtorCustomerController().addDebtorCustomer(debCust);
//                        }
//                    }
//
//                    /** Address : ข้อมูลที่อยู่ **/
//                    GetAllAddressByContractListInputInfo getAllAddrInput = new GetAllAddressByContractListInputInfo();
//                    getAllAddrInput.setContractListValue(contListInfo.contractList);
//                    GetAllAddressByContractListOutputInfo getAllAddrOutput = TSRService.getAllAddressByContractList(getAllAddrInput, false);
//                    List<AddressInfo> addressList = getAllAddrOutput.Info;
//                    if (addressList != null) {
//                        for (AddressInfo address : addressList) {
//                            new AddressController().addAddress(address);
//                        }
//                    }
//
//                    /** Contract-CloseAccount : ข้อมูลการตัดสด **/
//                    GetAllContractCloseAccountByContractListInputInfo getAllContCloseAcctInput = new GetAllContractCloseAccountByContractListInputInfo();
//                    getAllContCloseAcctInput.setContractListValue(contListInfo.contractList);
//                    GetAllContractCloseAccountByContractListOutputInfo getAllContCloseAcctOutput = TSRService.getAllContractCloseAccountByContractList(getAllContCloseAcctInput, false);
//                    List<ContractCloseAccountInfo> contCloseAcctList = getAllContCloseAcctOutput.Info;
//                    if (contCloseAcctList != null) {
//                        for (ContractCloseAccountInfo contCloseAcct : contCloseAcctList) {
//                            new ContractCloseAccountController().addContractCloseAccount(contCloseAcct);
//                        }
//                    }
//
//                    /** Cut-Divisor-Contract : ข้อมูลการตัดตัวหาร **/
//                    GetAllCutDivisorContractByContractListInputInfo getAllCutDivContInput = new GetAllCutDivisorContractByContractListInputInfo();
//                    getAllCutDivContInput.setContractListValue(contListInfo.contractList);
//                    GetAllCutDivisorContractByContractListOutputInfo getAllCutDivContOutput = TSRService.getAllCutDivisorContractByContractList(getAllCutDivContInput, false);
//                    List<CutDivisorContractInfo> cutDivContList = getAllCutDivContOutput.Info;
//                    if (cutDivContList != null) {
//                        for (CutDivisorContractInfo cutDivCont : cutDivContList) {
//                            new CutDivisorContractController().addCutDivisorContract(cutDivCont);
//                        }
//                    }
//
//                    /** CutOff-Contract : ข้อมูลการตัดสัญญาออกจากฟอร์ม **/
//                    GetAllCutOffContractByContractListInputInfo getAllCutOffContInput = new GetAllCutOffContractByContractListInputInfo();
//                    getAllCutOffContInput.setContractListValue(contListInfo.contractList);
//                    GetAllCutOffContractByContractListOutputInfo getAllCutOffContOutput = TSRService.getAllCutOffContractByContractList(getAllCutOffContInput, false);
//                    List<CutOffContractInfo> cutOffContList = getAllCutOffContOutput.Info;
//                    if (cutOffContList != null) {
//                        for (CutOffContractInfo cutOffCont : cutOffContList) {
//                            new CutOffContractController().addCutOffContract(cutOffCont);
//                        }
//                    }
//
//                    /** Payment : ข้อมูลการชำระเงินของสัญญานั้น ๆ **/
//                    GetAllPaymentByContractListInputInfo getAllPayInput = new GetAllPaymentByContractListInputInfo();
//                    getAllPayInput.setContractListValue(contListInfo.contractList);
//                    GetAllPaymentByContractListOutputInfo getAllPayOutput = TSRService.getAllPaymentByContractList(getAllPayInput, false);
//                    List<PaymentInfo> paymentList = getAllPayOutput.Info;
//                    if (paymentList != null) {
//                        for (PaymentInfo payment : paymentList) {
//                            new PaymentController().addPayment(payment);
//                        }
//                    }
//
//                    /** SalePaymentPeriodPayment : ข้อมูลการชำระเงินของสัญญานั้น ๆ แบ่งตามงวดการชำระ (Associated Table) **/
//                    GetAllSalePaymentPeriodPaymentByContractListInputInfo getAllSpppInput = new GetAllSalePaymentPeriodPaymentByContractListInputInfo();
//                    getAllSpppInput.setContractListValue(contListInfo.contractList);
//                    GetAllSalePaymentPeriodPaymentByContractListOutputInfo getAllSpppOutput = TSRService.getAllSalePaymentPeriodPaymentByContractList(getAllSpppInput, false);
//                    List<SalePaymentPeriodPaymentInfo> spppList = getAllSpppOutput.Info;
//                    if (spppList != null) {
//                        for (SalePaymentPeriodPaymentInfo sppp : spppList) {
//                            new SalePaymentPeriodPaymentController().addSalePaymentPeriodPayment(sppp);
//                        }
//                    }
//
//                    /** Receipt : ข้อมูลใบเสร็จตามการชำระเงินของสัญญานั้น ๆ แบ่งตามงวดการชำระ  **/
//                    GetAllReceiptByContractListInputInfo getAllReceiptInput = new GetAllReceiptByContractListInputInfo();
//                    getAllReceiptInput.setContractListValue(contListInfo.contractList);
//                    GetAllReceiptByContractListOutputInfo getAllReceiptOutput = TSRService.getAllReceiptByContractList(getAllReceiptInput, false);
//                    receiptList = getAllReceiptOutput.Info;
//                    if (receiptList != null) {
//                        for (ReceiptInfo receipt : receiptList) {
//                            new ReceiptController().addReceipt(receipt);
//                        }
//                    }
//
//                    /** PaymentAppointment : ข้อมูลการเลื่อนนัดชำระ (ไม่นับการนัดชำระครั้งแรกที่มาจาก Flow การขาย) **/
//                    GetAllPaymentAppointmentByContractListInputInfo getAllPayAppointInput = new GetAllPaymentAppointmentByContractListInputInfo();
//                    getAllPayAppointInput.setContractListValue(contListInfo.contractList);
//                    GetAllPaymentAppointmentByContractListOutputInfo getAllPayAppointOutput = TSRService.getAllPaymentAppointmentByContractList(getAllPayAppointInput, false);
//                    List<PaymentAppointmentInfo> paymentAppointmentList = getAllPayAppointOutput.Info;
//                    if (paymentAppointmentList != null) {
//                        for (PaymentAppointmentInfo paymentAppointment : paymentAppointmentList) {
//                            new PaymentAppointmentController().addAppointment(paymentAppointment);
//                        }
//                    }
//
//                    /** SaleAudit : ข้อมูลการตรวจสอบลูกค้าของสัญญานั้น ๆ **/
//                    GetAllSaleAuditByContractListInputInfo getAllSaleAuditInput = new GetAllSaleAuditByContractListInputInfo();
//                    getAllSaleAuditInput.setContractListValue(contListInfo.contractList);
//                    GetAllSaleAuditByContractListOutputInfo getAllSaleAuditOutput = TSRService.getAllSaleAuditByContractList(getAllSaleAuditInput, false);
//                    List<SaleAuditInfo> SaleAuditList = getAllSaleAuditOutput.Info;
//                    if (SaleAuditList != null) {
//                        for (SaleAuditInfo SaleAudit : SaleAuditList) {
//                            new SaleAuditController().addSaleAudit(SaleAudit);
//                        }
//                    }
//
//                    /** Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ **/
//                    GetAllAssignByContractListInputInfo getAllAssignInput = new GetAllAssignByContractListInputInfo();
//                    getAllAssignInput.setContractListValue(contListInfo.contractList);
//                    GetAllAssignByContractListOutputInfo getAllAssignOutput = TSRService.getAllAssignByContractList(getAllAssignInput, false);
//                    List<AssignInfo> assignList = getAllAssignOutput.Info;
//                    if (assignList != null) {
//                        for (AssignInfo assign : assignList) {
//                            new AssignController().addAssign(assign);
//                        }
//                    }
//
//                }   // if contractList != null
//
//
//                /** ManualDocumentWithdrawal : ข้อมูลการเบิก-คืนเอกสารมือ **/
//                GetAllActiveManualDocumentWithdrawalByTeamInputInfo getAllManualWithdrawalInput = new GetAllActiveManualDocumentWithdrawalByTeamInputInfo();
//                getAllManualWithdrawalInput.TeamCode = BHPreference.teamCode();
//                GetAllActiveManualDocumentWithdrawalByTeamOutputInfo getAllManualWithdrawalOutput = TSRService.getAllActiveManualDocumentWithdrawalByTeam(getAllManualWithdrawalInput, false);
//                List<ManualDocumentWithdrawalInfo> manualWithdrawalList = getAllManualWithdrawalOutput.Info;
//                if (manualWithdrawalList != null) {
//                    for (ManualDocumentWithdrawalInfo item : manualWithdrawalList) {
//                        new ManualDocumentWithdrawalController().addManualDocumentWithdrawal(item);
//                    }
//                }
//
//                /** ManualDocument : ข้อมูลการใช้เอกสารมือ **/
//                GetAllManualDocumentByTeamCodeInputInfo getAllManualInput = new GetAllManualDocumentByTeamCodeInputInfo();
//                getAllManualInput.TeamCode = TeamCode;
//                GetAllManualDocumentByTeamCodeOutputInfo getAllManualOutput = TSRService.getAllManualDocumentByTeamCode(getAllManualInput, false);
//                List<ManualDocumentInfo> manualList = getAllManualOutput.Info;
//                if (manualList != null) {
//                    for (ManualDocumentInfo item : manualList) {
////                        new ManualDocumentController().addManualDocument(item);
//                        /** Fixed - [BHPROJ-0024-360] **/
//                        ManualDocumentInfo chkExist = new ManualDocumentController().getManualDocumentByDocumentID(item.DocumentID);
//                        if (chkExist != null) {
//                            new ManualDocumentController().updateManualDocument(item);
//                        } else {
//                            new ManualDocumentController().addManualDocument(item);
//                        }
//
//                        // Add contract into table Contract when remain send document
//                        if (item.ManualDocTypeID.equals(DocumentHistoryController.DocumentType.Contract.toString())) {
//                            ContractInfo cont = new ContractController().getContract(item.DocumentNumber);
//                            if (cont == null) {
//                                importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), item.DocumentNumber);
//                            }
//                        }
//                        // Add receipt into table Receipt when remain send document
//                        if (item.ManualDocTypeID.equals(DocumentHistoryController.DocumentType.Receipt.toString())) {
//                            ContractInfo cont = null;
//                            ReceiptInfo recp = new ReceiptController().getReceiptByReceiptID(BHPreference.organizationCode(), item.DocumentNumber);
//                            if (recp == null) {
//                                GetContractByReceiptIDInputInfo inputRecp = new GetContractByReceiptIDInputInfo();
//                                inputRecp.OrganizationCode = BHPreference.organizationCode();
//                                inputRecp.ReceiptID = item.DocumentNumber;
//                                GetContractByReceiptIDOutputInfo outputRecp = TSRService.getContractByReceiptID(inputRecp, false);
//                                cont = outputRecp.Info;
//                            } else {
//                                cont = new ContractController().getContract(recp.RefNo);
//                            }
//                            if (cont == null) {
//                                importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), item.DocumentNumber);
//                            }
//                        }
//                    }
//                }
//
//                /*
//                if (contListInfo != null)
//                {
//                    int iDocCnt = (contListInfo.contractList != null ? contListInfo.contractList.size() : 0) + (receiptList != null ? receiptList.size() : 0);
//                    String[] docNum = new String[iDocCnt];
//                    for (int i = 0; i < iDocCnt; i++) {
//                        int j = 0;
//                        // (1) Insert list of Contract (docNum) to retrieve Contract-ManualDocument
//                        if (contListInfo.contractList != null) {
//                            for (ContractInfo cont : contListInfo.contractList) {
//                                docNum[j] = cont.RefNo;
//                                j++;
//                            }
//                        }
//                        // (2) Insert list of Receipt (docNum) to retrieve Receipt-ManualDocument
//                        if (receiptList != null) {
//                            for (ReceiptInfo rcp : receiptList) {
//                                docNum[j] = rcp.ReceiptID;
//                                j++;
//                            }
//                        }
//                    }   // for iDocCnt
//                    // ManualDocument : ข้อมูลการใช้เอกสารมือ
//                    GetAllManualDocumentByDocumentNumberListInputInfo getAllManualInput = new GetAllManualDocumentByDocumentNumberListInputInfo();
//                    getAllManualInput.documentNumberList = docNum;
//                    GetAllManualDocumentByDocumentNumberListOutputInfo getAllManualOutput = TSRService.getAllManualDocumentByDocumentNumberList(getAllManualInput, false);
//                    List<ManualDocumentInfo> manualList = getAllManualOutput.Info;
//                    if (manualList != null) {
//                        for (ManualDocumentInfo item : manualList) {
//                            new ManualDocumentController().addManualDocument(item);
//                        }
//                    }
//
//                    // DocumentHistory : ข้อมูลการพิมพ์เอกสารต่าง ๆ จากระบบ (ยกเว้นใบรับคืนเครื่อง + ใบนำส่งเงิน)
//                    int iDocCntDocHist = (contListInfo.contractList != null ? contListInfo.contractList.size() : 0) + (receiptList != null ? receiptList.size() : 0) + (manualList != null ? manualList.size() : 0);
//                    String[] docNumDocHist = new String[iDocCntDocHist];
//                    for (int i = 0; i < iDocCntDocHist; i++) {
//                        int j = 0;
//                        // (1) Insert list of Contract+Receipt (docNum) to retrieve DocumentHistory
//                        if (docNum != null) {
//                            for (String item : docNum) {
//                                docNumDocHist[j] = item;
//                                j++;
//                            }
//                        }
//                        // (2) Insert list of ManualDocument (manualList :- Contract_ManualDocument + Receipt_ManualDocument)
//                        if (manualList != null) {
//                            for (ManualDocumentInfo mDoc : manualList) {
//                                docNumDocHist[j] = mDoc.DocumentID;
//                                j++;
//                            }
//                        }
//                    }
//                    GetAllDocumentHistoryByDocumentNumberListInputInfo getAllDocumentHistoryInput = new GetAllDocumentHistoryByDocumentNumberListInputInfo();
//                    getAllDocumentHistoryInput.documentNumberList = docNumDocHist;
//                    getAllDocumentHistoryInput.TeamCode = BHPreference.teamCode();
//                    GetAllDocumentHistoryByDocumentNumberListOutputInfo getAllDocumentHistoryOutput = TSRService.getAllDocumentHistoryByDocumentNumberList(getAllDocumentHistoryInput, false);
//                    List<DocumentHistoryInfo> documentHistoryList = getAllDocumentHistoryOutput.Info;
//                    if (documentHistoryList != null) {
//                        for (DocumentHistoryInfo item : documentHistoryList) {
//                            new DocumentHistoryController().addDocumentHistory(item);
//                        }
//                    }
//                }
//                */
//
//                /** DocumentHistory : ข้อมูลการพิมพ์เอกสารต่าง ๆ จากระบบ (ยกเว้นใบรับคืนเครื่อง + ใบนำส่งเงิน) **/
//                GetAllActiveDocumentHistoryByTeamCodeInputInfo getAllDocumentHistoryInput = new GetAllActiveDocumentHistoryByTeamCodeInputInfo();
//                getAllDocumentHistoryInput.TeamCode = BHPreference.teamCode();
//                GetAllActiveDocumentHistoryByTeamCodeOutputInfo getAllDocumentHistoryOutput = TSRService.getAllActiveDocumentHistoryByTeamCode(getAllDocumentHistoryInput, false);
//                List<DocumentHistoryInfo> documentHistoryList = getAllDocumentHistoryOutput.Info;
//                if (documentHistoryList != null) {
//                    for (DocumentHistoryInfo item : documentHistoryList) {
////                        new DocumentHistoryController().addDocumentHistory(item);
//                        /** Fixed - [BHPROJ-0024-360] **/
//                        DocumentHistoryInfo chkExist = new DocumentHistoryController().getDocumentHistoryByID(item.OrganizationCode, item.PrintHistoryID);
//                        if (chkExist != null) {
//                            new DocumentHistoryController().updateDocumentHistory(item);
//                        } else {
//                            new DocumentHistoryController().addDocumentHistory(item);
//                        }
//
//                        // Add contract into table Contract when remain send document
//                        if (item.DocumentType.equals(DocumentHistoryController.DocumentType.Contract.toString())) {
//                            ContractInfo cont = new ContractController().getContract(item.DocumentNumber);
//                            if (cont == null) {
//                                importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), item.DocumentNumber);
//                            }
//                        }
//                        // Add receipt into table Receipt when remain send document
//                        if (item.DocumentType.equals(DocumentHistoryController.DocumentType.Receipt.toString())) {
//                            ContractInfo cont = null;
//                            ReceiptInfo recp = new ReceiptController().getReceiptByReceiptID(BHPreference.organizationCode(), item.DocumentNumber);
//                            if (recp == null) {
//                                GetContractByReceiptIDInputInfo inputRecp = new GetContractByReceiptIDInputInfo();
//                                inputRecp.OrganizationCode = BHPreference.organizationCode();
//                                inputRecp.ReceiptID = item.DocumentNumber;
//                                GetContractByReceiptIDOutputInfo outputRecp = TSRService.getContractByReceiptID(inputRecp, false);
//                                cont = outputRecp.Info;
//                            } else {
//                                cont = new ContractController().getContract(recp.RefNo);
//                            }
//                            if (cont == null) {
//                                importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), item.DocumentNumber);
//                            }
//                        }
//                    }
//                }
//
//                /** SendDocument : ข้อมูลการนำส่งเอกสาร (Master) **/
//                GetAllSendDocumentBySubTeamAndTeamInputInfo getSendDocInput = new GetAllSendDocumentBySubTeamAndTeamInputInfo();
//                getSendDocInput.OrganizationCode = OrganizationCode;
//                getSendDocInput.SentSubTeamCode = BHPreference.SubTeamCode();
//                getSendDocInput.SentTeamCode = TeamCode;
//                GetAllSendDocumentBySubTeamAndTeamOutputInfo getSendDocOutput = TSRService.getAllSendDocumentBySubTeamAndTeam(getSendDocInput, false);
//                List<SendDocumentInfo> sendDocList = getSendDocOutput.Info;
//                if (sendDocList != null) {
//                    for (SendDocumentInfo item : sendDocList) {
//                        new SendDocumentController().addSendDocument(item);
//                    }
//                }
//
//                /** SendDocumentDetail : ข้อมูลการนำส่งเอกสารแบบรายละเอียด (Detail) **/
//                GetAllSendDocumentDetailBySubTeamAndTeamInputInfo getSendDocDetailInput = new GetAllSendDocumentDetailBySubTeamAndTeamInputInfo();
//                getSendDocDetailInput.OrganizationCode = OrganizationCode;
//                getSendDocDetailInput.SentSubTeamCode = BHPreference.SubTeamCode();
//                getSendDocDetailInput.SentTeamCode = TeamCode;
//                GetAllSendDocumentDetailBySubTeamAndTeamOutputInfo getSendDocDetailOutput = TSRService.getAllSendDocumentDetailBySubTeamAndTeam(getSendDocDetailInput, false);
//                List<SendDocumentDetailInfo> sendDocDetailList = getSendDocDetailOutput.Info;
//                if (sendDocDetailList != null) {
//                    for (SendDocumentDetailInfo item : sendDocDetailList) {
//                        new SendDocumentDetailController().addSendDocumentDetail(item);
//                    }
//                }
//
//
//            }
//        });
//    }

    public static void importContractImage(ContractInfo info) {
        GetContractImageInputInfo getContractImageInputInfo = new GetContractImageInputInfo();
        getContractImageInputInfo.RefNo = info.RefNo;

        getContractImageInputInfo.ImageTypeCode = ContractImageController.ImageType.IDCARD.toString();
        GetContractImageOutputInfo contractImageIDCARD = TSRService.GetContractImage(getContractImageInputInfo, false);
        if (contractImageIDCARD.Info != null) {
            for (ContractImageInfo contractImageInfo : contractImageIDCARD.Info) {
                new ContractImageController().addContractImage(contractImageInfo);
                //saveContractImage(contractImageInfo);
            }
        }

        getContractImageInputInfo.ImageTypeCode = ContractImageController.ImageType.PRODUCT.toString();
        GetContractImageOutputInfo contractImagePRODUCT = TSRService.GetContractImage(getContractImageInputInfo, false);
        if (contractImagePRODUCT.Info != null) {
            for (ContractImageInfo contractImageInfo : contractImagePRODUCT.Info) {
                new ContractImageController().addContractImage(contractImageInfo);
                //saveContractImage(contractImageInfo);
            }
        }

        getContractImageInputInfo.ImageTypeCode = ContractImageController.ImageType.ADDRESS.toString();
        GetContractImageOutputInfo contractImageADDRESS = TSRService.GetContractImage(getContractImageInputInfo, false);
        if (contractImageADDRESS.Info != null) {
            for (ContractImageInfo contractImageInfo : contractImageADDRESS.Info) {
                new ContractImageController().addContractImage(contractImageInfo);
                //saveContractImage(contractImageInfo);
            }
        }

        getContractImageInputInfo.ImageTypeCode = ContractImageController.ImageType.MAP.toString();
        GetContractImageOutputInfo contractImageMAP = TSRService.GetContractImage(getContractImageInputInfo, false);
        if (contractImageMAP.Info != null) {
            for (ContractImageInfo contractImageInfo : contractImageMAP.Info) {
                new ContractImageController().addContractImage(contractImageInfo);
                //saveContractImage(contractImageInfo);
            }
        }

        getContractImageInputInfo.ImageTypeCode = ContractImageController.ImageType.LOSS.toString();
        GetContractImageOutputInfo contractImageLOSS = TSRService.GetContractImage(getContractImageInputInfo, false);
        if (contractImageLOSS.Info != null) {
            for (ContractImageInfo contractImageInfo : contractImageLOSS.Info) {
                new ContractImageController().addContractImage(contractImageInfo);
                //saveContractImage(contractImageInfo);
            }
        }

        getContractImageInputInfo.ImageTypeCode = ContractImageController.ImageType.CUSTOMER.toString();
        GetContractImageOutputInfo contractImageCUSTOMER = TSRService.GetContractImage(getContractImageInputInfo, false);
        if (contractImageCUSTOMER.Info != null) {
            for (ContractImageInfo contractImageInfo : contractImageCUSTOMER.Info) {
                new ContractImageController().addContractImage(contractImageInfo);
                //saveContractImage(contractImageInfo);
            }
        }

        getContractImageInputInfo.ImageTypeCode = ContractImageController.ImageType.IMPOUNDPRODUCT.toString();
        GetContractImageOutputInfo contractImageIMPOUNDPRODUCT = TSRService.GetContractImage(getContractImageInputInfo, false);
        if (contractImageIMPOUNDPRODUCT.Info != null) {
            for (ContractImageInfo contractImageInfo : contractImageIMPOUNDPRODUCT.Info) {
                new ContractImageController().addContractImage(contractImageInfo);
                //saveContractImage(contractImageInfo);
            }
        }

        getContractImageInputInfo.ImageTypeCode = ContractImageController.ImageType.CHANGEPRODUCT.toString();
        GetContractImageOutputInfo contractImageCHANGEPRODUCT = TSRService.GetContractImage(getContractImageInputInfo, false);
        if (contractImageCHANGEPRODUCT.Info != null) {
            for (ContractImageInfo contractImageInfo : contractImageCHANGEPRODUCT.Info) {
                new ContractImageController().addContractImage(contractImageInfo);
                //saveContractImage(contractImageInfo);
            }
        }

        getContractImageInputInfo.ImageTypeCode = ContractImageController.ImageType.SALEAUDIT.toString();
        GetContractImageOutputInfo contractImageSALEAUDIT = TSRService.GetContractImage(getContractImageInputInfo, false);
        if (contractImageSALEAUDIT.Info != null) {
            for (ContractImageInfo contractImageInfo : contractImageSALEAUDIT.Info) {
                new ContractImageController().addContractImage(contractImageInfo);
                //saveContractImage(contractImageInfo);
            }
        }
    }

//    public static void importSendMoney() {
//        processTransaction(new Runnable() {
//            @Override
//            public void run() {
//                new SendMoneyController().deleteSendMoneyAll();
//                GetSendMoneyByTeamCodeInputInfo getSendMoneyInput = new GetSendMoneyByTeamCodeInputInfo();
//                getSendMoneyInput.OrganizationCode = BHPreference.organizationCode();
//                getSendMoneyInput.TeamCode = BHPreference.teamCode();
//                GetSendMoneyByTeamCodeOutputInfo getSendMoneyOutput = TSRService.getSendMoneyByTeamCode(getSendMoneyInput, false);
//                if (getSendMoneyOutput != null && getSendMoneyOutput.Info != null) {
//                    for (SendMoneyInfo sm : getSendMoneyOutput.Info) {
//                        new SendMoneyController().addSendMoney(sm);
//                    }
//                }
//            }
//        });
//    }
//
//    public static void importSaleAudit() {
//        processTransaction(new Runnable() {
//            @Override
//            public void run() {
////                new AssignController().deleteAssignByTaskType(AssignController.AssignTaskType.SaleAudit.toString());
////                new SaleAuditController().deleteSaleAuditAll();
//
//                GetAssignByAssigneeTeamCodeInputInfo assignInput = new GetAssignByAssigneeTeamCodeInputInfo();
//                assignInput.OrganizationCode = BHPreference.organizationCode();
//                assignInput.TaskType = AssignController.AssignTaskType.SaleAudit.toString();
//                assignInput.AssigneeTeamCode = BHPreference.teamCode();
//                assignInput.AssigneeEmpID = BHPreference.employeeID();
//
//                List<AssignInfo> assignList = TSRService.GetAssignSaleAuditByTaskTypeAndAssigneeTeamCode(assignInput, false).Info;
//                new AssignController().deleteAssignByAssignList(assignList);
//
//                List<SaleAuditInfo> saleAuditList = TSRService.GetSaleAuditByAssigneeTeamCode(assignInput, false).Info;
//                if (assignList != null && assignList.size() > 0 && saleAuditList != null && saleAuditList.size() > 0) {
////                    int i = 1;
//                    for (AssignInfo assign : assignList) {
//                        AssignInfo exist = new AssignController().getAssignByAssignID(assign.AssignID);
//                        if (exist == null) {
////                            assign.Order = i;
////                            assign.OrderExpect = i;
//                            new AssignController().addAssign(assign);
////                            i++;
//                        }
//                    }
//                    for (SaleAuditInfo saleAudit : saleAuditList) {
//                        SaleAuditInfo exist = new SaleAuditController().getSaleAuditBySaleAuditID(saleAudit.SaleAuditID);
//                        if (exist == null) {
//                            new SaleAuditController().addSaleAudit(saleAudit);
//                        } else {
//                            new SaleAuditController().updateSaleAudit(saleAudit);
//                        }
//
////                        TSRController.importContractFromServerForCredit(saleAudit.OrganizationCode, saleAudit.RefNo, true, true, true);
//                        //** Fixed - [BHPRJ00301-4073]
////                        TSRController.importContractFromServer(saleAudit.OrganizationCode, BHPreference.teamCode(), saleAudit.RefNo, true, true, true);
//                        TSRController.importContractFromServer(saleAudit.OrganizationCode, BHPreference.teamCode(), saleAudit.RefNo);
//                    }
//                }
//            }
//        });
//    }
//
//    public static void importRequest(final String organizationCode, final String requestTeamCode) {
//        processTransaction(new Runnable() {
//            @Override
//            public void run() {
//                /** Delete all request in Local-DB **/
//                new ChangeProductController().deleteChangeProductAll();
//                new ChangeContractController().deleteChangeContractAll();
//                new ImpoundProductController().deleteImpoundProductAll();
//                new ComplainController().deleteComplainAll();
//                new RequestNextPaymentController().deleteRequestNextPaymentAll();
////                new AssignController().deleteAssignAll();
//
//
//                /*******************************/
//                /** Import data into Local-DB **/
//                /*******************************/
//
//                /** Change-Product :ข้อมูลการเปลี่ยนเครื่อง **/
//                GetChangeProductByRequestTeamCodeInputInfo getChangeProdInput = new GetChangeProductByRequestTeamCodeInputInfo();
//                getChangeProdInput.OrganizationCode = organizationCode;
//                getChangeProdInput.RequestTeamCode = requestTeamCode;
//                GetChangeProductByRequestTeamCodeOutputInfo getChangeProdOutput = TSRService.getChangeProductByRequestTeamCode(getChangeProdInput, false);
//                List<ChangeProductInfo> changeProdList = getChangeProdOutput.Info;
//                if (changeProdList != null) {
//                    for (ChangeProductInfo cp : changeProdList) {
//                        if ((cp.Status.equals(ChangeProductStatus.REQUEST.toString())) || (cp.Status.equals(ChangeProductStatus.APPROVED.toString()))) {
//                            // (A) เพิ่มข้อมูลที่ยังไม่ Completed
//                            new ChangeProductController().addChangeProduct(cp);
//                            //** Fixed - [BHPRJ00301-4073]
////                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo, false, false, true);	// Add Contract from Change Product
//                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo);    // Add Contract from Change Product
//                        } else {
//                            // (B) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับการส่งเอกสาร
//                            DocumentHistoryInfo existDocHistCP = TSRController.getDocumentHistoryByDocumentNumber(cp.ChangeProductID, DocumentHistoryController.DocumentType.ChangeProduct.toString());
//                            if (existDocHistCP != null) {
//                                new ChangeProductController().addChangeProduct(cp);
//                                //** Fixed - [BHPRJ00301-4073]
////                                TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo, false, false, true);	// Add Contract from Send Document
//                                TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo);    // Add Contract from Send Document
//                            }
//                            // (C) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับ ProductStock เก่า --> ถ้าเครื่องเก่ายังไม่ได้ส่งคืนเครื่องเข้าระบบก็ต้อง import product เข้ามาใน Local
//                            ProductStockInfo productStock = getProductStock(cp.OldProductSerialNumber);
//                            if (productStock == null) {
//                                GetProductStockBySerialNoInputInfo productStockInput = new GetProductStockBySerialNoInputInfo();
//                                productStockInput.OrganizationCode = cp.OrganizationCode;
//                                productStockInput.ProductSerialNumber = cp.OldProductSerialNumber;
//                                productStock = TSRService.getProductStockBySerialNo(productStockInput, false).Info;
//                                if (productStock != null && productStock.ProductSerialNumber != null) {
//                                    new ProductStockController().addProductStock(productStock);
//                                }
//                            }   // if C
//                        }    // if A else B
//                    }
//                }    // if (changeProdList != null)
//
//                /** Change-Contract : ข้อมูลการเปลี่ยนสัญญา **/
//                GetChangeContractByRequestTeamCodeInputInfo getChangeContInput = new GetChangeContractByRequestTeamCodeInputInfo();
//                getChangeContInput.OrganizationCode = organizationCode;
//                getChangeContInput.RequestTeamCode = requestTeamCode;
//                GetChangeContractByRequestTeamCodeOutputInfo getChangeContOutput = TSRService.getChangeContractByRequestTeamCode(getChangeContInput, false);
//                List<ChangeContractInfo> changeContList = getChangeContOutput.Info;
//                if (changeContList != null) {
//                    for (ChangeContractInfo cc : changeContList) {
//                        if ((cc.Status.equals(ChangeContractStatus.REQUEST.toString())) || (cc.Status.equals(ChangeContractStatus.APPROVED.toString())) || (cc.Status.equals(ChangeContractStatus.REJECT.toString()))) {
//                            // (A) เพิ่มข้อมูลที่ยังไม่ Completed
//                            new ChangeContractController().addChangeContract(cc);
//                            //** Fixed - [BHPRJ00301-4073]
////                            TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID, true, true, false);		// Add Old-Contract from Change Contract
//                            TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID);        // Add Old-Contract from Change Contract
//                            if (cc.NewSaleID != null) {
//                                //** Fixed - [BHPRJ00301-4073]
////                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID, true, true, false);	// Add New-Contract from Change Contract
//                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID);    // Add New-Contract from Change Contract
//                            }
//                        } else {
//                            // (B) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับการส่งเอกสาร
//                            DocumentHistoryInfo existDocHistCC = TSRController.getDocumentHistoryByDocumentNumber(cc.ChangeContractID, DocumentHistoryController.DocumentType.ChangeContract.toString());
//                            if (existDocHistCC != null) {
//                                new ChangeContractController().addChangeContract(cc);
//                                //** Fixed - [BHPRJ00301-4073]
////                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID, true, true, false);		// Add Old-Contract from Send Document
//                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID);        // Add Old-Contract from Send Document
//                                if (cc.NewSaleID != null) {
//                                    //** Fixed - [BHPRJ00301-4073]
////                                    TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID, true, true, false);	// Add New-Contract from Send Document
//                                    TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID);    // Add New-Contract from Send Document
//                                }
//                            }
//                        }    // if A else B
//                    }
//                }    // if (changeContList != null)
//
//                /** Impound-Product : ข้อมูลการถอดเครื่อง **/
//                GetImpoundProductByRequestTeamCodeInputInfo getImpoundProdInput = new GetImpoundProductByRequestTeamCodeInputInfo();
//                getImpoundProdInput.OrganizationCode = organizationCode;
//                getImpoundProdInput.RequestTeamCode = requestTeamCode;
//                GetImpoundProductByRequestTeamCodeOutputInfo getImpoundProdOutput = TSRService.getImpoundProductByRequestTeamCode(getImpoundProdInput, false);
//                // Add Contract from Change Contract
//                List<ImpoundProductInfo> impoundProdList = getImpoundProdOutput.Info;
//                if (impoundProdList != null) {
//                    for (ImpoundProductInfo ip : impoundProdList) {
//                        if ((ip.Status.equals(ImpoundProductStatus.REQUEST.toString())) || (ip.Status.equals(ImpoundProductStatus.APPROVED.toString())) || (ip.Status.equals(ImpoundProductStatus.REJECT.toString()))) {
//                            // (A) เพิ่มข้อมูลที่ยังไม่ Completed
//                            new ImpoundProductController().addImpoundProduct(ip);
//                            //** Fixed - [BHPRJ00301-4073]
////                            TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo, false, false, true);	// Add Contract from Impound Product
//                            TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo);    // Add Contract from Impound Product
//                        } else {
//                            // (B-1) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับการส่งเอกสาร
//                            DocumentHistoryInfo existDocHistIP = TSRController.getDocumentHistoryByDocumentNumber(ip.ImpoundProductID, DocumentHistoryController.DocumentType.ImpoundProduct.toString());
//                            if (existDocHistIP != null) {
//                                new ImpoundProductController().addImpoundProduct(ip);                                                       // Add Contract from Send Document
//                            }
//                            // (B-2) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับถอดเครื่องทีมอื่น
//                            //** Fixed - [BHPRJ00301-4073]
////                            TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo, false, false, true);	// Add Other-Team-Contract from Send Document
//                            TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo);    // Add Other-Team-Contract from Send Document
//                        }    // if A else B
//                    }
//                }    //if (impoundProdList != null)
//
//                /** Complain : ข้อมูลการแจ้งปัญหาและแก้ไขปัญหา **/
//                GetComplainInputInfo getComplainInput = new GetComplainInputInfo();
//                getComplainInput.OrganizationCode = organizationCode;
//                getComplainInput.TaskType = AssignController.AssignTaskType.Complain.toString();    // [Assign].TaskType + in query use 'LEFT OUTER JOIN'
//                getComplainInput.AssigneeTeamCode = requestTeamCode;                                // [Assign].AssigneeTeamCode + in query use 'OR'
//                getComplainInput.RequestTeamCode = requestTeamCode;                                 // [Complain].RequestTeamCode + in query use 'OR'
//                GetComplainOutputInfo getComplainOutput = TSRService.getComplainByRequestTeamCodeAndForAction(getComplainInput, false);
//                List<ComplainInfo> getComplainList = getComplainOutput.Info;
//                if (getComplainList != null && getComplainList.size() > 0) {
//                    for (ComplainInfo cp : getComplainList) {
//                        new ComplainController().addComplain(cp);
//                        //** Fixed - [BHPRJ00301-4073]
////                        TSRController.importContractFromServer(cp.OrganizationCode, null, cp.RefNo, false, false, true);
//                        TSRController.importContractFromServer(cp.OrganizationCode, null, cp.RefNo);
//                    }
//                }
//
//                /** Request-NextPayment : ข้อมูลการขออนุมัติเก็บเงินค่างวด **/
//                GetRequestNextPaymentByRequestTeamCodeInputInfo getReqNextPayInput = new GetRequestNextPaymentByRequestTeamCodeInputInfo();
//                getReqNextPayInput.OrganizationCode = organizationCode;
//                getReqNextPayInput.RequestTeamCode = requestTeamCode;
//                GetRequestNextPaymentByRequestTeamCodeOutputInfo getReqNextPayOutput = TSRService.getRequestNextPaymentByRequestTeamCode(getReqNextPayInput, false);
//                List<RequestNextPaymentInfo> reqNextPayList = getReqNextPayOutput.Info;
//                if (reqNextPayList != null) {
//                    for (RequestNextPaymentInfo cp : reqNextPayList) {
//                        if ((cp.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.REQUEST.toString())) || (cp.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString()))) {
//                            // เพิ่มข้อมูลที่ยังไม่ Completed
//                            new RequestNextPaymentController().addRequestNextPayment(cp);
//                            //** Fixed - [BHPRJ00301-4073]
////                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo, false, false, true);	// Add Contract from Request NextPayment
//                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo);    // Add Contract from Request NextPayment
//                        }
//                    }
//                }    // if (reqNextPayList != null)
//
//                /** Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ **/
//                TSRController.importAssignFromServer(organizationCode, requestTeamCode);
//
//            }    // run
//        });
//    }



    /*** [START] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/

/*

    //---- Comment Old-SourceCode

    // [START] :: Fixed - [BHPROJ-0026-970] :: [Meeting@TSR@07/03/2559] [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา/แจ้งปัญหา] ทำ Performance Tuning ในขณะทำการปรับปรุงรายการ
    public static void importAssignFromServer(String organizationCode, String requestTeamCode) {
        GetAssignByAssigneeTeamCodeInputInfo getAssignInput = new GetAssignByAssigneeTeamCodeInputInfo();
        getAssignInput.OrganizationCode = organizationCode;
        getAssignInput.AssigneeTeamCode = requestTeamCode;
        GetAssignByAssigneeTeamCodeOutputInfo getAssignOutput = TSRService.getAssignByAssigneeTeamCode(getAssignInput, false);
        List<AssignInfo> assignList = getAssignOutput.Info;
        if (assignList != null) {
            for (AssignInfo assign : assignList) {
//                        new AssignController().addAssign(assign);
                AssignInfo chkExist = new AssignController().getAssignByAssignID(assign.AssignID);
                if (chkExist != null) {
                    new AssignController().updateAssignByAssignID(assign);
                } else {
                    new AssignController().addAssign(assign);
                }
            }
        }   // if (assignList != null)
    }

    public static void importRequestChangeContract(final String organizationCode, final String requestTeamCode, final SynchronizeService.SynchronizeResult result) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                // Delete all request in Local-DB
                new ChangeContractController().deleteChangeContractAll();

                // Change-Contract : ข้อมูลการเปลี่ยนสัญญา
                GetChangeContractByRequestTeamCodeInputInfo getChangeContInput = new GetChangeContractByRequestTeamCodeInputInfo();
                getChangeContInput.OrganizationCode = organizationCode;
                getChangeContInput.RequestTeamCode = requestTeamCode;
                GetChangeContractByRequestTeamCodeOutputInfo getChangeContOutput = TSRService.getChangeContractByRequestTeamCode(getChangeContInput, false);
                List<ChangeContractInfo> changeContList = getChangeContOutput.Info;
                float percent = (100f / changeContList.size());
                int num = 0;
                if (changeContList != null) {
                    for (ChangeContractInfo cc : changeContList) {
                        // [START] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว
                        if (cc.Status.equals(ChangeContractStatus.REJECT.toString())) {
                            continue;   // if reject go to next iteration in for loop
                        }
//                        if ((cc.Status.equals(ChangeContractStatus.REQUEST.toString())) || (cc.Status.equals(ChangeContractStatus.APPROVED.toString())) || (cc.Status.equals(ChangeContractStatus.REJECT.toString()))) {
                        // [END] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว
                        if ((cc.Status.equals(ChangeContractStatus.REQUEST.toString())) || (cc.Status.equals(ChangeContractStatus.APPROVED.toString()))) {
                            // (A) เพิ่มข้อมูลที่ยังไม่ Completed
                            new ChangeContractController().addChangeContract(cc);
                            // Fixed - [BHPRJ00301-4073]
//                            TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID, true, true, false);		// Add Old-Contract from Change Contract
                            TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID);        // Add Old-Contract from Change Contract
                            if (cc.NewSaleID != null) {
                                // Fixed - [BHPRJ00301-4073]
//                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID, true, true, false);	// Add New-Contract from Change Contract
                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID);    // Add New-Contract from Change Contract
                            }
                        } else {
                            // (B) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับการส่งเอกสาร
                            DocumentHistoryInfo existDocHistCC = TSRController.getDocumentHistoryByDocumentNumber(cc.ChangeContractID, DocumentHistoryController.DocumentType.ChangeContract.toString());
                            if (existDocHistCC != null) {
                                new ChangeContractController().addChangeContract(cc);
                                // Fixed - [BHPRJ00301-4073]
//                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID, true, true, false);		// Add Old-Contract from Send Document
                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID);        // Add Old-Contract from Send Document
                                if (cc.NewSaleID != null) {
                                    // Fixed - [BHPRJ00301-4073]
//                                    TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID, true, true, false);	// Add New-Contract from Send Document
                                    TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID);    // Add New-Contract from Send Document
                                }
                            }
                        }    // if A else B
                        num++;
                        result.progress = (int) Math.ceil(percent * num);
                        SynchronizeService.sendBroadcast(result);
                    }
                }    // if (changeContList != null)

                // Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ
                TSRController.importAssignFromServer(organizationCode, requestTeamCode);

            }    // run
        });
    }

    public static void importRequestChangeProduct(final String organizationCode, final String requestTeamCode, final SynchronizeService.SynchronizeResult result) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                // Delete all request in Local-DB
                new ChangeProductController().deleteChangeProductAll();

                // Change-Product :ข้อมูลการเปลี่ยนเครื่อง
                GetChangeProductByRequestTeamCodeInputInfo getChangeProdInput = new GetChangeProductByRequestTeamCodeInputInfo();
                getChangeProdInput.OrganizationCode = organizationCode;
                getChangeProdInput.RequestTeamCode = requestTeamCode;
                GetChangeProductByRequestTeamCodeOutputInfo getChangeProdOutput = TSRService.getChangeProductByRequestTeamCode(getChangeProdInput, false);
                List<ChangeProductInfo> changeProdList = getChangeProdOutput.Info;
                float percent = (100f / changeProdList.size());
                int num = 0;
                if (changeProdList != null) {
                    for (ChangeProductInfo cp : changeProdList) {
                        // [START] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว
                        if (cp.Status.equals(ChangeProductStatus.REJECT.toString())) {
                            continue;   // if reject go to next iteration in for loop
                        }
                        // [END] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว
                        if ((cp.Status.equals(ChangeProductStatus.REQUEST.toString())) || (cp.Status.equals(ChangeProductStatus.APPROVED.toString()))) {
                            // (A) เพิ่มข้อมูลที่ยังไม่ Completed
                            new ChangeProductController().addChangeProduct(cp);
                            // Fixed - [BHPRJ00301-4073]
//                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo, false, false, true);	// Add Contract from Change Product
                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo);    // Add Contract from Change Product
                        } else {
                            // (B) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับการส่งเอกสาร --> ถ้ายังไม่ได้นำส่งเอกสารก็ต้อง import contract เข้ามาใน Local
                            DocumentHistoryInfo existDocHistCP = TSRController.getDocumentHistoryByDocumentNumber(cp.ChangeProductID, DocumentHistoryController.DocumentType.ChangeProduct.toString());
                            if (existDocHistCP != null) {
                                new ChangeProductController().addChangeProduct(cp);
                                // Fixed - [BHPRJ00301-4073]
//                                TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo, false, false, true);	// Add Contract from Send Document
                                TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo);    // Add Contract from Send Document
                            }
                            // (C) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับ ProductStock เก่า --> ถ้าเครื่องเก่ายังไม่ได้ส่งคืนเครื่องเข้าระบบก็ต้อง import product เข้ามาใน Local
                            ProductStockInfo productStock = getProductStock(cp.OldProductSerialNumber);
                            if (productStock == null) {
                                GetProductStockBySerialNoInputInfo productStockInput = new GetProductStockBySerialNoInputInfo();
                                productStockInput.OrganizationCode = cp.OrganizationCode;
                                productStockInput.ProductSerialNumber = cp.OldProductSerialNumber;
                                productStock = TSRService.getProductStockBySerialNo(productStockInput, false).Info;
                                if (productStock != null && productStock.ProductSerialNumber != null) {
                                    new ProductStockController().addProductStock(productStock);
                                }
                            }   // if C
                        }    // if A else B
                        num++;
                        result.progress = (int) Math.ceil(percent * num);
                        SynchronizeService.sendBroadcast(result);
                    }
                }    // if (changeProdList != null)

                // Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ
                TSRController.importAssignFromServer(organizationCode, requestTeamCode);

            }    // run
        });
    }

    public static void importRequestImpoundProduct(final String organizationCode, final String requestTeamCode, final SynchronizeService.SynchronizeResult result) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                // Delete all request in Local-DB
                new ImpoundProductController().deleteImpoundProductAll();

                // Impound-Product : ข้อมูลการถอดเครื่อง
                GetImpoundProductByRequestTeamCodeInputInfo getImpoundProdInput = new GetImpoundProductByRequestTeamCodeInputInfo();
                getImpoundProdInput.OrganizationCode = organizationCode;
                getImpoundProdInput.RequestTeamCode = requestTeamCode;
                GetImpoundProductByRequestTeamCodeOutputInfo getImpoundProdOutput = TSRService.getImpoundProductByRequestTeamCode(getImpoundProdInput, false);
                // Add Contract from Change Contract
                List<ImpoundProductInfo> impoundProdList = getImpoundProdOutput.Info;
                float percent = (100f / impoundProdList.size());
                int num = 0;
                if (impoundProdList != null) {
                    for (ImpoundProductInfo ip : impoundProdList) {
                        // [START] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว
                        if (ip.Status.equals(ImpoundProductStatus.REJECT.toString())) {
                            continue;   // if reject go to next iteration in for loop
                        }
//                        if ((ip.Status.equals(ImpoundProductStatus.REQUEST.toString())) || (ip.Status.equals(ImpoundProductStatus.APPROVED.toString())) || (ip.Status.equals(ImpoundProductStatus.REJECT.toString()))) {
                        // [END] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว
                        if ((ip.Status.equals(ImpoundProductStatus.REQUEST.toString())) || (ip.Status.equals(ImpoundProductStatus.APPROVED.toString()))) {
                            // (A) เพิ่มข้อมูลที่ยังไม่ Completed
                            new ImpoundProductController().addImpoundProduct(ip);
                            // Fixed - [BHPRJ00301-4073]
//                            TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo, false, false, true);	// Add Contract from Impound Product
                            TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo);    // Add Contract from Impound Product
                        } else {
                            // (B-1) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับการส่งเอกสาร
                            DocumentHistoryInfo existDocHistIP = TSRController.getDocumentHistoryByDocumentNumber(ip.ImpoundProductID, DocumentHistoryController.DocumentType.ImpoundProduct.toString());
                            if (existDocHistIP != null) {
                                new ImpoundProductController().addImpoundProduct(ip);                                                       // Add Contract from Send Document
                                // (B-2) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับถอดเครื่องทีมอื่น
                                // Fixed - [BHPRJ00301-4073]
//                            TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo, false, false, true);	// Add Other-Team-Contract from Send Document
                                TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo);    // Add Other-Team-Contract from Send Document
                            }
                        }    // if A else B
                        num++;
                        result.progress = (int) Math.ceil(percent * num);
                        SynchronizeService.sendBroadcast(result);
                    }
                }    //if (impoundProdList != null)

                // Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ
                TSRController.importAssignFromServer(organizationCode, requestTeamCode);

            }    // run
        });
    }

    public static void importRequestComplain(final String organizationCode, final String requestTeamCode, final SynchronizeService.SynchronizeResult result) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                // Delete all request in Local-DB
                new ComplainController().deleteComplainAll();

                // Complain : ข้อมูลการแจ้งปัญหาและแก้ไขปัญหา
                GetComplainInputInfo getComplainInput = new GetComplainInputInfo();
                getComplainInput.OrganizationCode = organizationCode;
                getComplainInput.TaskType = AssignController.AssignTaskType.Complain.toString();    // [Assign].TaskType + in query use 'LEFT OUTER JOIN'
                getComplainInput.AssigneeTeamCode = requestTeamCode;                                // [Assign].AssigneeTeamCode + in query use 'OR'
                getComplainInput.RequestTeamCode = requestTeamCode;                                 // [Complain].RequestTeamCode + in query use 'OR'
                GetComplainOutputInfo getComplainOutput = TSRService.getComplainByRequestTeamCodeAndForAction(getComplainInput, false);
                List<ComplainInfo> getComplainList = getComplainOutput.Info;
                float percent = (100f / getComplainList.size());
                int num = 0;
                if (getComplainList != null && getComplainList.size() > 0) {
                    for (ComplainInfo cp : getComplainList) {
                        new ComplainController().addComplain(cp);
                        // Fixed - [BHPRJ00301-4073]
//                        TSRController.importContractFromServer(cp.OrganizationCode, null, cp.RefNo, false, false, true);
                        TSRController.importContractFromServer(cp.OrganizationCode, null, cp.RefNo);

                        num++;
                        result.progress = (int) Math.ceil(percent * num);
                        SynchronizeService.sendBroadcast(result);
                    }
                }

                // Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ
                TSRController.importAssignFromServer(organizationCode, requestTeamCode);

            }    // run
        });
    }

    public static void importRequestNextPayment(final String organizationCode, final String requestTeamCode, final SynchronizeService.SynchronizeResult result) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                // Delete all request in Local-DB
                new RequestNextPaymentController().deleteRequestNextPaymentAll();

                // Request-NextPayment : ข้อมูลการขออนุมัติเก็บเงินค่างวด
                GetRequestNextPaymentByRequestTeamCodeInputInfo getReqNextPayInput = new GetRequestNextPaymentByRequestTeamCodeInputInfo();
                getReqNextPayInput.OrganizationCode = organizationCode;
                getReqNextPayInput.RequestTeamCode = requestTeamCode;
                GetRequestNextPaymentByRequestTeamCodeOutputInfo getReqNextPayOutput = TSRService.getRequestNextPaymentByRequestTeamCode(getReqNextPayInput, false);
                List<RequestNextPaymentInfo> reqNextPayList = getReqNextPayOutput.Info;
                float percent = (100f / reqNextPayList.size());
                int num = 0;
                if (reqNextPayList != null) {
                    for (RequestNextPaymentInfo cp : reqNextPayList) {
                        // [START] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว
                        if (cp.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.REJECT.toString())) {
                            continue;   // if reject go to next iteration in for loop
                        }
                        // [END] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว
                        if ((cp.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.REQUEST.toString())) || (cp.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString()))) {
                            // เพิ่มข้อมูลที่ยังไม่ Completed
                            new RequestNextPaymentController().addRequestNextPayment(cp);
                            // Fixed - [BHPRJ00301-4073]
//                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo, false, false, true);	// Add Contract from Request NextPayment
                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo);    // Add Contract from Request NextPayment
                        }

                        num++;
                        result.progress = (int) Math.ceil(percent * num);
                        SynchronizeService.sendBroadcast(result);
                    }
                }    // if (reqNextPayList != null)

                // Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ
                TSRController.importAssignFromServer(organizationCode, requestTeamCode);

            }    // run
        });
    }
    // [END] :: Fixed - [BHPROJ-0026-970] :: [Meeting@TSR@07/03/2559] [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา/แจ้งปัญหา] ทำ Performance Tuning ในขณะทำการปรับปรุงรายการ
*/

    public static void importRequestChangeContract(final String organizationCode, final String requestTeamCode, final String requestBy, final SynchronizeService.SynchronizeResult result) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                /** Delete all request in Local-DB **/
//                new ChangeContractController().deleteChangeContractAll();

                /** Change-Contract : ข้อมูลการเปลี่ยนสัญญา **/
                GetChangeContractByRequestTeamCodeByRequestEmpInputInfo getChangeContInput = new GetChangeContractByRequestTeamCodeByRequestEmpInputInfo();
                getChangeContInput.OrganizationCode = organizationCode;
                getChangeContInput.RequestTeamCode = requestTeamCode;
                getChangeContInput.RequestBy = requestBy;
                GetChangeContractByRequestTeamCodeByRequestEmpOutputInfo getChangeContOutput = TSRService.getChangeContractByRequestTeamCodeByRequestEmp(getChangeContInput, false);
                List<ChangeContractInfo> changeContList = getChangeContOutput.Info;
                float percent = (100f / changeContList.size());
                int num = 0;
                if (changeContList != null) {
                    for (ChangeContractInfo cc : changeContList) {
                        new ChangeContractController().deleteChangeContractByID(organizationCode, cc.ChangeContractID);     // For loop delete by ID in Local-DB

                        /*** [START] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว **/
                        if (cc.Status.equals(ChangeContractStatus.REJECT.toString())) {
                            continue;   // if reject go to next iteration in for loop
                        }
//                        if ((cc.Status.equals(ChangeContractStatus.REQUEST.toString())) || (cc.Status.equals(ChangeContractStatus.APPROVED.toString())) || (cc.Status.equals(ChangeContractStatus.REJECT.toString()))) {
                        /*** [END] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว **/
                        if ((cc.Status.equals(ChangeContractStatus.REQUEST.toString())) || (cc.Status.equals(ChangeContractStatus.APPROVED.toString()))) {
                            // (A) เพิ่มข้อมูลที่ยังไม่ Completed
                            new ChangeContractController().addChangeContract(cc);
                            //** Fixed - [BHPRJ00301-4073]
//                            TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID, true, true, false);		// Add Old-Contract from Change Contract
                            TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID);        // Add Old-Contract from Change Contract
                            if (cc.NewSaleID != null) {
                                //** Fixed - [BHPRJ00301-4073]
//                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID, true, true, false);	// Add New-Contract from Change Contract
                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID);    // Add New-Contract from Change Contract
                            }
                        } else {
                            // (B) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับการส่งเอกสาร
                            DocumentHistoryInfo existDocHistCC = TSRController.getDocumentHistoryByDocumentNumber(cc.ChangeContractID, DocumentHistoryController.DocumentType.ChangeContract.toString());
                            if (existDocHistCC != null) {
                                new ChangeContractController().addChangeContract(cc);
                                //** Fixed - [BHPRJ00301-4073]
//                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID, true, true, false);		// Add Old-Contract from Send Document
                                TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.OldSaleID);        // Add Old-Contract from Send Document
                                if (cc.NewSaleID != null) {
                                    //** Fixed - [BHPRJ00301-4073]
//                                    TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID, true, true, false);	// Add New-Contract from Send Document
                                    TSRController.importContractFromServer(cc.OrganizationCode, cc.RequestTeamCode, cc.NewSaleID);    // Add New-Contract from Send Document
                                }
                            }
                        }    // if A else B
                        num++;
                        result.progress = (int) Math.ceil(percent * num);
                        SynchronizeService.sendBroadcast(result);
                    }
                }    // if (changeContList != null)

                /** Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ **/
                TSRController.importAssignFromServerByTaskTypeAndEmpID(organizationCode, requestTeamCode, AssignController.AssignTaskType.ChangeContract.toString(), requestBy);

            }    // run
        });
    }

    public static void importRequestChangeProduct(final String organizationCode, final String requestTeamCode, final String requestBy, final SynchronizeService.SynchronizeResult result) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                /** Delete all request in Local-DB **/
//                new ChangeProductController().deleteChangeProductAll();

                /** Change-Product :ข้อมูลการเปลี่ยนเครื่อง **/
                GetChangeProductByRequestTeamCodeByRequestEmpInputInfo getChangeProdInput = new GetChangeProductByRequestTeamCodeByRequestEmpInputInfo();
                getChangeProdInput.OrganizationCode = organizationCode;
                getChangeProdInput.RequestTeamCode = requestTeamCode;
                getChangeProdInput.RequestBy = requestBy;
                GetChangeProductByRequestTeamCodeByRequestEmpOutputInfo getChangeProdOutput = TSRService.getChangeProductByRequestTeamCodeByRequestEmp(getChangeProdInput, false);
                List<ChangeProductInfo> changeProdList = getChangeProdOutput.Info;
                float percent = (100f / changeProdList.size());
                int num = 0;
                if (changeProdList != null) {
                    for (ChangeProductInfo cp : changeProdList) {
                        new ChangeProductController().deleteChangeProductByID(organizationCode, cp.ChangeProductID);    // For loop delete by ID in Local-DB

                        /*** [START] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว **/
                        if (cp.Status.equals(ChangeProductStatus.REJECT.toString())) {
                            continue;   // if reject go to next iteration in for loop
                        }
                        /*** [END] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว **/
                        if ((cp.Status.equals(ChangeProductStatus.REQUEST.toString())) || (cp.Status.equals(ChangeProductStatus.APPROVED.toString()))) {
                            // (A) เพิ่มข้อมูลที่ยังไม่ Completed
                            new ChangeProductController().addChangeProduct(cp);
                            //** Fixed - [BHPRJ00301-4073]
//                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo, false, false, true);	// Add Contract from Change Product
                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo);    // Add Contract from Change Product
                        } else {
                            // (B) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับการส่งเอกสาร --> ถ้ายังไม่ได้นำส่งเอกสารก็ต้อง import contract เข้ามาใน Local
                            DocumentHistoryInfo existDocHistCP = TSRController.getDocumentHistoryByDocumentNumber(cp.ChangeProductID, DocumentHistoryController.DocumentType.ChangeProduct.toString());
                            if (existDocHistCP != null) {
                                new ChangeProductController().addChangeProduct(cp);
                                //** Fixed - [BHPRJ00301-4073]
//                                TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo, false, false, true);	// Add Contract from Send Document
                                TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo);    // Add Contract from Send Document
                            }
                            // (C) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับ ProductStock เก่า --> ถ้าเครื่องเก่ายังไม่ได้ส่งคืนเครื่องเข้าระบบก็ต้อง import product เข้ามาใน Local
                            ProductStockInfo productStock = getProductStock(cp.OldProductSerialNumber);
                            if (productStock == null) {
                                GetProductStockBySerialNoInputInfo productStockInput = new GetProductStockBySerialNoInputInfo();
                                productStockInput.OrganizationCode = cp.OrganizationCode;
                                productStockInput.ProductSerialNumber = cp.OldProductSerialNumber;
                                productStock = TSRService.getProductStockBySerialNo(productStockInput, false).Info;
                                if (productStock != null && productStock.ProductSerialNumber != null) {
                                    new ProductStockController().addProductStock(productStock);
                                }
                            }   // if C
                        }    // if A else B
                        num++;
                        result.progress = (int) Math.ceil(percent * num);
                        SynchronizeService.sendBroadcast(result);
                    }
                }    // if (changeProdList != null)

                /** Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ **/
                TSRController.importAssignFromServerByTaskTypeAndEmpID(organizationCode, requestTeamCode, AssignController.AssignTaskType.ChangeProduct.toString(), requestBy);

            }    // run
        });
    }

    public static void importRequestImpoundProduct(final String organizationCode, final String requestTeamCode, final String requestBy, final SynchronizeService.SynchronizeResult result) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                /** Delete all request in Local-DB **/
//                new ImpoundProductController().deleteImpoundProductAll();

                /** Impound-Product : ข้อมูลการถอดเครื่อง **/
                GetImpoundProductByRequestTeamCodeByRequestEmpInputInfo getImpoundProdInput = new GetImpoundProductByRequestTeamCodeByRequestEmpInputInfo();
                getImpoundProdInput.OrganizationCode = organizationCode;
                getImpoundProdInput.RequestTeamCode = requestTeamCode;
                getImpoundProdInput.RequestBy = requestBy;
                GetImpoundProductByRequestTeamCodeByRequestEmpOutputInfo getImpoundProdOutput = TSRService.getImpoundProductByRequestTeamCodeByRequestEmp(getImpoundProdInput, false);
                // Add Contract from Change Contract
                List<ImpoundProductInfo> impoundProdList = getImpoundProdOutput.Info;
                float percent = (100f / impoundProdList.size());
                int num = 0;
                if (impoundProdList != null) {
                    for (ImpoundProductInfo ip : impoundProdList) {
                        new ImpoundProductController().deleteImpoundProductByID(organizationCode, ip.ImpoundProductID);     // For loop delete by ID in Local-DB

                        /*** [START] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว **/
                        if (ip.Status.equals(ImpoundProductStatus.REJECT.toString())) {
                            continue;   // if reject go to next iteration in for loop
                        }
//                        if ((ip.Status.equals(ImpoundProductStatus.REQUEST.toString())) || (ip.Status.equals(ImpoundProductStatus.APPROVED.toString())) || (ip.Status.equals(ImpoundProductStatus.REJECT.toString()))) {
                        /*** [END] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว **/
                        if ((ip.Status.equals(ImpoundProductStatus.REQUEST.toString())) || (ip.Status.equals(ImpoundProductStatus.APPROVED.toString()))) {
                            // (A) เพิ่มข้อมูลที่ยังไม่ Completed
                            new ImpoundProductController().addImpoundProduct(ip);
                            //** Fixed - [BHPRJ00301-4073]
//                            TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo, false, false, true);	// Add Contract from Impound Product
                            TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo);    // Add Contract from Impound Product
                        } else {
                            // (B-1) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับการส่งเอกสาร
                            DocumentHistoryInfo existDocHistIP = TSRController.getDocumentHistoryByDocumentNumber(ip.ImpoundProductID, DocumentHistoryController.DocumentType.ImpoundProduct.toString());
                            if (existDocHistIP != null) {
                                new ImpoundProductController().addImpoundProduct(ip);                                                       // Add Contract from Send Document
                                // (B-2) เพิ่มข้อมูลในส่วนที่เกี่ยวข้องกับถอดเครื่องทีมอื่น
                                //** Fixed - [BHPRJ00301-4073]
//                            TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo, false, false, true);	// Add Other-Team-Contract from Send Document
                                TSRController.importContractFromServer(ip.OrganizationCode, ip.RequestTeamCode, ip.RefNo);    // Add Other-Team-Contract from Send Document
                            }
                        }    // if A else B
                        num++;
                        result.progress = (int) Math.ceil(percent * num);
                        SynchronizeService.sendBroadcast(result);
                    }
                }    //if (impoundProdList != null)

                /** Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ **/
                TSRController.importAssignFromServerByTaskTypeAndEmpID(organizationCode, requestTeamCode, AssignController.AssignTaskType.ImpoundProduct.toString(), requestBy);

            }    // run
        });
    }

    public static void importRequestComplain(final String organizationCode, final String requestTeamCode, final String requestBy, final SynchronizeService.SynchronizeResult result) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                /** Delete all request in Local-DB **/
//                new ComplainController().deleteComplainAll();

                /** Complain : ข้อมูลการแจ้งปัญหาและแก้ไขปัญหา **/
                GetComplainByRequestTeamCodeAndForActionByEmpIDInputInfo getComplainInput = new GetComplainByRequestTeamCodeAndForActionByEmpIDInputInfo();
                getComplainInput.OrganizationCode = organizationCode;
                getComplainInput.TaskType = AssignController.AssignTaskType.Complain.toString();    // [Assign].TaskType + in query use 'LEFT OUTER JOIN'
                getComplainInput.AssigneeTeamCode = requestTeamCode;                                // [Assign].AssigneeTeamCode + in query use 'OR'
                getComplainInput.RequestTeamCode = requestTeamCode;                                 // [Complain].RequestTeamCode + in query use 'OR'
                getComplainInput.RequestBy = requestBy;
                GetComplainByRequestTeamCodeAndForActionByEmpIDOutputInfo getComplainOutput = TSRService.getComplainByRequestTeamCodeAndForActionByEmpID(getComplainInput, false);
                List<ComplainInfo> getComplainList = getComplainOutput.Info;
                float percent = (100f / getComplainList.size());
                int num = 0;
                if (getComplainList != null && getComplainList.size() > 0) {
                    for (ComplainInfo cp : getComplainList) {
                        new ComplainController().deleteComplainByID(organizationCode, cp.ComplainID);       // For loop delete by ID in Local-DB

                        new ComplainController().addComplain(cp);
                        //** Fixed - [BHPRJ00301-4073]
//                        TSRController.importContractFromServer(cp.OrganizationCode, null, cp.RefNo, false, false, true);
                        TSRController.importContractFromServer(cp.OrganizationCode, null, cp.RefNo);

                        num++;
                        result.progress = (int) Math.ceil(percent * num);
                        SynchronizeService.sendBroadcast(result);
                    }
                }

                /** Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ **/
                TSRController.importAssignFromServerByTaskTypeAndEmpID(organizationCode, requestTeamCode, AssignController.AssignTaskType.Complain.toString(), requestBy);

            }    // run
        });
    }

    public static void importRequestNextPayment(final String organizationCode, final String requestTeamCode, final String requestBy, final SynchronizeService.SynchronizeResult result) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                /** Delete all request in Local-DB **/
//                new RequestNextPaymentController().deleteRequestNextPaymentAll();

                /** Request-NextPayment : ข้อมูลการขออนุมัติเก็บเงินค่างวด **/
                GetRequestNextPaymentByRequestTeamCodeByRequestEmpInputInfo getReqNextPayInput = new GetRequestNextPaymentByRequestTeamCodeByRequestEmpInputInfo();
                getReqNextPayInput.OrganizationCode = organizationCode;
                getReqNextPayInput.RequestTeamCode = requestTeamCode;
                getReqNextPayInput.RequestBy = requestBy;
                GetRequestNextPaymentByRequestTeamCodeByRequestEmpOutputInfo getReqNextPayOutput = TSRService.getRequestNextPaymentByRequestTeamCodeByRequestEmp(getReqNextPayInput, false);
                List<RequestNextPaymentInfo> reqNextPayList = getReqNextPayOutput.Info;
                float percent = (100f / reqNextPayList.size());
                int num = 0;
                if (reqNextPayList != null) {
                    for (RequestNextPaymentInfo cp : reqNextPayList) {
                        new RequestNextPaymentController().deleteRequestNextPaymentByID(organizationCode, cp.RequestNextPaymentID);     // For loop delete by ID in Local-DB

                        /*** [START] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว **/
                        if (cp.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.REJECT.toString())) {
                            continue;   // if reject go to next iteration in for loop
                        }
                        /*** [END] :: แก้ไข ไม่นำการร้องขอที่เป็น REJECT เข้ามาด้วย เพราะไม่จำเป็นต้องมีการส่งเอกสารอะไรแล้ว **/
                        if ((cp.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.REQUEST.toString())) || (cp.Status.equals(RequestNextPaymentController.RequestNextPaymentStatus.APPROVED.toString()))) {
                            // เพิ่มข้อมูลที่ยังไม่ Completed
                            new RequestNextPaymentController().addRequestNextPayment(cp);
                            //** Fixed - [BHPRJ00301-4073]
//                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo, false, false, true);	// Add Contract from Request NextPayment
                            TSRController.importContractFromServer(cp.OrganizationCode, cp.RequestTeamCode, cp.RefNo);    // Add Contract from Request NextPayment
                        }

                        num++;
                        result.progress = (int) Math.ceil(percent * num);

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        SynchronizeService.sendBroadcast(result);
                    }
                }    // if (reqNextPayList != null)

                /** Assign : ข้อมูลการมอบหมายงานการร้องขอต่าง ๆ **/
                TSRController.importAssignFromServerByTaskTypeAndEmpID(organizationCode, requestTeamCode, AssignController.AssignTaskType.RequestNextPayment.toString(), requestBy);

            }    // run
        });
    }

    public static void importAssignFromServerByTaskTypeAndEmpID(String organizationCode, String requestTeamCode, String taskType, String requestEmpID) {
        GetAssignByAssigneeTeamCodeByTaskTypeAndEmpIDInputInfo getAssignInput = new GetAssignByAssigneeTeamCodeByTaskTypeAndEmpIDInputInfo();
        getAssignInput.OrganizationCode = organizationCode;
        getAssignInput.AssigneeTeamCode = requestTeamCode;
        getAssignInput.TaskType = taskType;
        getAssignInput.AssigneeEmpID = requestEmpID;
        GetAssignByAssigneeTeamCodeByTaskTypeAndEmpIDOutputInfo getAssignOutput = TSRService.getAssignByAssigneeTeamCodeByTaskTypeAndEmpID(getAssignInput, false);
        List<AssignInfo> assignList = getAssignOutput.Info;
        if (assignList != null) {
            for (AssignInfo assign : assignList) {
                AssignInfo chkExist = new AssignController().getAssignByAssignID(assign.AssignID);
                if (chkExist != null) {
                    new AssignController().updateAssignByAssignID(assign);
                } else {
                    new AssignController().addAssign(assign);
                }
            }
        }   // if (assignList != null)
    }

    /*** [END] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/



//    public static void importMasterData(final String OrganizationCode, final String TeamCode) {
//        processTransaction(new Runnable() {
//            @Override
//            public void run() {
//                new AddressTypeController().deleteAddressType();
//                new BankController().deleteBank();
//                new CareerController().deleteCareer();
//                new ChannelController().deleteChannel();
//                new ChannelItemController().deleteChannelItem();
//                new ContractStatusController().deleteContractStatus();
//                new ProvinceController().deleteProvince();
//                new DistrictController().deleteDistrict();
//                new SubDistrictController().deleteSubDistrict();
//                new FortnightController().deleteFortnight();
//                new HabitatTypeController().deleteHabitatType();
//                new HobbyController().deleteHobby();
//                new ImageTypeController().deleteImageType();
//                new OrganizationController().deleteOrganization();
//                new ProductController().deleteProductAll();
//                new PackageController().deleteAllPackage();
//                new PackagePeriodDetailController().deleteAllPackagePeriodDetail();
//                new PositionController().deletePosition();
//                new PrefixController().deletePrefix();
//                new ProblemTypeController().deleteProblemTypeAll();
//                new ProblemController().deleteProblem();
//                new RoleController().deleteRole();
////                new SaleTypeController().deleteSaleType();
////                new TaskController().deleteTask();
//                new SuggestionController().deleteSuggestion();
//                new TradeInBrandController().deleteTradeInBrand();
//                new TripController().deleteTrip();
//                new ManualDocumentTypeController().deleteManualDocumentTypeAll();
//                new LimitController().deleteLimitAll();
//                new AreaController().deleteAreaAll();
//                new AreaEmployeeController().deleteAreaEmployeeAll();
//                new SubAreaController().deleteSubAreaAll();
//
//                /** Import All-AddressType **/
//                GetAddressTypeInputInfo getAddressTypeInput = new GetAddressTypeInputInfo();
//                GetAddressTypeOutputInfo getAddressTypeOutput = TSRService.getAddressType(getAddressTypeInput, false);
//                if (getAddressTypeOutput != null && getAddressTypeOutput.Info != null) {
//                    for (AddressTypeInfo at : getAddressTypeOutput.Info) {
//                        new AddressTypeController().addAddressType(at);
//                    }
//                }
//
//                /** Import All-Bank **/
//                GetBankInputInfo getBankInput = new GetBankInputInfo();
//                GetBankOutputInfo getBankOutput = TSRService.getBank(getBankInput, false);
//                if (getBankOutput != null && getBankOutput.Info != null) {
//                    for (BankInfo b : getBankOutput.Info) {
//                        new BankController().addBank(b);
//                    }
//                }
//
//                /** Import All-Career **/
//                GetCareerInputInfo getCareerInput = new GetCareerInputInfo();
//                GetCareerOutputInfo getCareerOutput = TSRService.getCareer(getCareerInput, false);
//                if (getCareerOutput != null && getCareerOutput.Info != null) {
//                    for (CareerInfo c : getCareerOutput.Info) {
//                        new CareerController().addCareer(c);
//                    }
//                }
//
//                /** Import All-Channel **/
//                GetChannelInputInfo getChannelInput = new GetChannelInputInfo();
//                GetChannelOutputInfo getChannelOutput = TSRService.getChannel(getChannelInput, false);
//                if (getChannelOutput != null && getChannelOutput.Info != null) {
//                    for (ChannelInfo channel : getChannelOutput.Info) {
//                        new ChannelController().addChannel(channel);
//                    }
//                }
//
//                /** Import All-ChannelItem **/
//                GetChannelItemInputInfo getChannelItemInput = new GetChannelItemInputInfo();
//                GetChannelItemOutputInfo getChannelItemOutput = TSRService.getChannelItem(getChannelItemInput, false);
//                if (getChannelItemOutput != null
//                        && getChannelItemOutput.Info != null) {
//                    for (ChannelItemInfo ci : getChannelItemOutput.Info) {
//                        new ChannelItemController().addChannelItem(ci);
//                    }
//                }
//
//                /** Import All-ContractStatus **/
//                GetContractStatusInputInfo getContractStatusInput = new GetContractStatusInputInfo();
//                GetContractStatusOutputInfo getContractStatusOutput = TSRService.getContractStatus(getContractStatusInput, false);
//                if (getContractStatusOutput != null && getContractStatusOutput.Info != null) {
//                    for (ContractStatusInfo cs : getContractStatusOutput.Info) {
//                        new ContractStatusController().addContractStatus(cs);
//                    }
//                }
//
//                /** Import All-Province **/
//                GetProvinceInputInfo getProvinceInput = new GetProvinceInputInfo();
//                GetProvinceOutputInfo getProvinceOutput = TSRService.getProvince(getProvinceInput, false);
//                if (getProvinceOutput != null && getProvinceOutput.Info != null) {
//                    for (ProvinceInfo province : getProvinceOutput.Info) {
//                        new ProvinceController().addProvince(province);
//                    }
//                }
//
//                /** Import All-District **/
//                GetDistrictInputInfo getDistrictInput = new GetDistrictInputInfo();
//                GetDistrictOutputInfo getDistrictOutput = TSRService.getDistrict(getDistrictInput, false);
//                if (getDistrictOutput != null && getDistrictOutput.Info != null) {
//                    for (DistrictInfo d : getDistrictOutput.Info) {
//                        new DistrictController().addDistrict(d);
//                    }
//                }
//
//                /** Import All-SubDistrict **/
//                GetSubDistrictInputInfo getSubDistrictInput = new GetSubDistrictInputInfo();
//                GetSubDistrictOutputInfo getSubDistrictOutput = TSRService.getSubDistrict(getSubDistrictInput, false);
//                if (getSubDistrictOutput != null && getSubDistrictOutput.Info != null) {
//                    for (SubDistrictInfo sd : getSubDistrictOutput.Info) {
//                        new SubDistrictController().addSubDistrict(sd);
//                    }
//                }
//
//                /** Import All-Fortnight **/
//                GetFortnightInputInfo getFortnightInput = new GetFortnightInputInfo();
//                GetFortnightOutputInfo getFortnightOutput = TSRService.getFortnight(getFortnightInput, false);
//                if (getFortnightOutput != null && getFortnightOutput.Info != null) {
//                    for (FortnightInfo fn : getFortnightOutput.Info) {
//                        new FortnightController().addFortnight(fn);
//                    }
//                }
//
//                /** Import All-HabitatType **/
//                GetHabitatTypeInputInfo getHabitatTypeInput = new GetHabitatTypeInputInfo();
//                GetHabitatTypeOutputInfo getHabitatTypeOutput = TSRService.getHabitatType(getHabitatTypeInput, false);
//                if (getHabitatTypeOutput != null && getHabitatTypeOutput.Info != null) {
//                    for (HabitatTypeInfo ht : getHabitatTypeOutput.Info) {
//                        new HabitatTypeController().addHabitatType(ht);
//                    }
//                }
//
//                /** Import All-Hobby **/
//                GetHobbyInputInfo getHobbyInput = new GetHobbyInputInfo();
//                GetHobbyOutputInfo getHobbyOutput = TSRService.getHobby(getHobbyInput, false);
//                if (getHobbyOutput != null && getHobbyOutput.Info != null) {
//                    for (HobbyInfo h : getHobbyOutput.Info) {
//                        new HobbyController().addHobby(h);
//                    }
//                }
//
//                /** Import All-ImageType **/
//                GetImageTypeInputInfo getImageTypeInput = new GetImageTypeInputInfo();
//                GetImageTypeOutputInfo getImageTypeOutput = TSRService.getImageType(getImageTypeInput, false);
//                if (getImageTypeOutput != null && getImageTypeOutput.Info != null) {
//                    for (ImageTypeInfo imgType : getImageTypeOutput.Info) {
//                        new ImageTypeController().addImageType(imgType);
//                    }
//                }
//
//                /** Import All-Organization **/
//                GetOrganizationInputInfo getOrgInput = new GetOrganizationInputInfo();
//                GetOrganizationOutputInfo getOrgOutput = TSRService.getOrganization(getOrgInput, false);
//                if (getOrgOutput != null && getOrgOutput.Info != null) {
//                    for (OrganizationInfo org : getOrgOutput.Info) {
//                        new OrganizationController().addOrganization(org);
//                    }
//                }
//
//                /** Import All-Product **/
//                GetAllProductInputInfo getAllProductInput = new GetAllProductInputInfo();
//                getAllProductInput.OrganizationCode = BHPreference.organizationCode();
//                GetAllProductOutputInfo getAllProductOutput = TSRService.getAllProduct(getAllProductInput, false);
//                if (getAllProductOutput != null && getAllProductOutput.Info != null) {
//                    for (ProductInfo product : getAllProductOutput.Info) {
//                        new ProductController().addProduct(product);
//                    }
//                }
//
//                /** Import All-Package **/
//                GetAllPackageInputInfo getAllPackageInput = new GetAllPackageInputInfo();
//                getAllPackageInput.OrganizationCode = OrganizationCode;// BHPreference.organizationCode();
//                GetAllPackageOutputInfo getAllPackageOutput = TSRService.getAllPackage(getAllPackageInput, false);
//                if (getAllPackageOutput != null && getAllPackageOutput.Info != null) {
//                    new PackageController().insertAllPackage(getAllPackageOutput.Info);
//                }
//
//                /** Import All-PackagePeriodDetail **/
//                GetAllPackagePeriodDetailInputInfo getAllPackagePeriodDetailInput = new GetAllPackagePeriodDetailInputInfo();
//                getAllPackagePeriodDetailInput.OrganizationCode = OrganizationCode;// BHPreference.organizationCode();
//                GetAllPackagePeriodDetailOutputInfo getAllPackagePeriodDetailOutput = TSRService.getAllPackagePeriodDetail(getAllPackagePeriodDetailInput, false);
//                if (getAllPackagePeriodDetailOutput != null && getAllPackagePeriodDetailOutput.Info != null) {
//                    new PackagePeriodDetailController().insertAllPackagePeriodDetail(getAllPackagePeriodDetailOutput.Info);
//                }
//
//                /** Import All-Position **/
//                GetPositionInputInfo getPosInput = new GetPositionInputInfo();
//                GetPositionOutputInfo getPosOutput = TSRService.getPosition(getPosInput, false);
//                if (getPosOutput != null && getPosOutput.Info != null) {
//                    for (PositionInfo p : getPosOutput.Info) {
//                        new PositionController().addPosition(p);
//                    }
//                }
//
//                /** Import All-Prefix **/
//                GetPrefixInputInfo getPrefixInput = new GetPrefixInputInfo();
//                GetPrefixOutputInfo getPrefixOutput = TSRService.getPrefix(getPrefixInput, false);
//                if (getPrefixOutput != null && getPrefixOutput.Info != null) {
//                    for (PrefixInfo p : getPrefixOutput.Info) {
//                        new PrefixController().addPrefix(p);
//                    }
//                }
//
//                /** Import All-ProblemType **/
//                GetAllProblemTypeInputInfo getProblemTypeInput = new GetAllProblemTypeInputInfo();
//                GetAllProblemTypeOutputInfo getProblemTypeOutput = TSRService.getAllProblemType(getProblemTypeInput, false);
//                if (getProblemTypeOutput != null && getProblemTypeOutput.Info != null) {
//                    for (ProblemTypeInfo item : getProblemTypeOutput.Info)
//                        new ProblemTypeController().addProblemType(item);
//                }
//
//                /** Import All-Problem **/
//                GetProblemInputInfo getProblemInput = new GetProblemInputInfo();
//                getProblemInput.OrganizationCode = BHPreference.organizationCode();
//                GetProblemOutputInfo getProblemOutput = TSRService.getProblem(getProblemInput, false);
//                if (getProblemOutput != null && getProblemOutput.Info != null) {
//                    for (ProblemInfo problem : getProblemOutput.Info) {
//                        new ProblemController().addProblem(problem);
//                    }
//                }
//
//                /** Import All-Role **/
//                GetRoleInputInfo getRoleInput = new GetRoleInputInfo();
//                GetRoleOutputInfo getRoleOutput = TSRService.getRole(getRoleInput, false);
//                if (getRoleOutput != null && getRoleOutput.Info != null) {
//                    for (RoleInfo r : getRoleOutput.Info) {
//                        new RoleController().addRole(r);
//                    }
//                }
//
//                /*
//                GetSaleTypeInputInfo getSaleTypeInput = new GetSaleTypeInputInfo();
//                GetSaleTypeOutputInfo getSaleTypeOutput = TSRService.getSaleType(getSaleTypeInput, false);
//                if (getSaleTypeOutput != null && getSaleTypeOutput.Info != null) {
//                    for (SaleTypeInfo st : getSaleTypeOutput.Info) {
//                        new SaleTypeController().addSaleType(st);
//                    }
//                }
//
//                GetTaskInputInfo getTaskInput = new GetTaskInputInfo();
//                GetTaskOutputInfo getTaskOutput = TSRService.getTask(getTaskInput, false);
//                if (getTaskOutput != null && getTaskOutput.Info != null) {
//                    for (TaskInfo t : getTaskOutput.Info) {
//                        new TaskController().addTask(t);
//                    }
//                }
//                */
//
//                /** Import All-Suggestion **/
//                GetSuggestionInputInfo getSuggestionInput = new GetSuggestionInputInfo();
//                GetSuggestionOutputInfo getSuggestionOutput = TSRService.getSuggestion(getSuggestionInput, false);
//                if (getSuggestionOutput != null && getSuggestionOutput.Info != null) {
//                    for (SuggestionInfo s : getSuggestionOutput.Info) {
//                        new SuggestionController().addSuggestion(s);
//                    }
//                }
//
//                /** Import All-TradeInBrand **/
//                GetTradeInBrandInputInfo getTradeInBrandInput = new GetTradeInBrandInputInfo();
//                GetTradeInBrandOutputInfo getTradeInBrandOutput = TSRService.getTradeInBrand(getTradeInBrandInput, false);
//                if (getTradeInBrandOutput != null && getTradeInBrandOutput.Info != null) {
//                    for (TradeInBrandInfo tib : getTradeInBrandOutput.Info) {
//                        new TradeInBrandController().addTradeInBrand(tib);
//                    }
//                }
//
//                /** Import All-Trip **/
//                GetTripInputInfo getTripInput = new GetTripInputInfo();
//                GetTripOutputInfo getTripOutput = TSRService.getTrip(getTripInput, false);
//                if (getTripOutput != null && getTripOutput.Info != null) {
//                    for (TripInfo t : getTripOutput.Info)
//                        new TripController().addTrip(t);
//                }
//
//                /** Import All-ManualDocumentType **/
//                GetManualDocumentTypeInputInfo getManualDocTypeInput = new GetManualDocumentTypeInputInfo();
//                GetManualDocumentTypeOutputInfo getManualDocTypeOutput = TSRService.getManualDocumentType(getManualDocTypeInput, false);
//                if (getManualDocTypeOutput != null && getManualDocTypeOutput.Info != null) {
//                    for (ManualDocumentTypeInfo item : getManualDocTypeOutput.Info)
//                        new ManualDocumentTypeController().addManualDocumentType(item);
//                }
//
//                /** Import All-Limit **/
//                GetAllLimitInputInfo getAllLimitInput = new GetAllLimitInputInfo();
//                getAllLimitInput.OrganizationCode = OrganizationCode;// BHPreference.organizationCode();
//                GetAllLimitOutputInfo getAllLimitOutput = TSRService.getAllLimit(getAllLimitInput, false);
//                if (getAllLimitOutput != null && getAllLimitOutput.Info != null) {
//                    for (LimitInfo item : getAllLimitOutput.Info)
//                        new LimitController().addLimit(item);
//                }
//
//                /** Import Area By Team **/
//                GetAllAreaByTeamCodeInputInfo getAllAreInput = new GetAllAreaByTeamCodeInputInfo();
//                getAllAreInput.TeamCode = TeamCode;
//                GetAllAreaByTeamCodeOutputInfo getAllAreOutput = TSRService.getAllAreaByTeamCode(getAllAreInput, false);
//                if (getAllAreOutput != null && getAllAreOutput.Info != null) {
//                    for (AreaInfo item : getAllAreOutput.Info)
//                        new AreaController().addArea(item);
//                }
//
//                /** Import AreaEmployee By Team **/
//                GetAllAreaEmployeeByTeamCodeInputInfo getAllAreaEmployeeInput = new GetAllAreaEmployeeByTeamCodeInputInfo();
//                getAllAreaEmployeeInput.TeamCode = TeamCode;
//                GetAllAreaEmployeeByTeamCodeOutputInfo getAllAreaEmployeeOutput = TSRService.getAllAreaEmployeeByTeamCode(getAllAreaEmployeeInput, false);
//                if (getAllAreaEmployeeOutput != null && getAllAreaEmployeeOutput.Info != null) {
//                    for (AreaEmployeeInfo item : getAllAreaEmployeeOutput.Info)
//                        new AreaEmployeeController().addAreaEmployee(item);
//                }
//
//                /** Import SubArea By Team **/
//                GetAllSubAreaByTeamCodeInputInfo getAllSubAreaInput = new GetAllSubAreaByTeamCodeInputInfo();
//                getAllSubAreaInput.TeamCode = TeamCode;
//                GetAllSubAreaByTeamCodeOutputInfo getAllSubAreaOutput = TSRService.getAllSubAreaByTeamCode(getAllSubAreaInput, false);
//                if (getAllSubAreaOutput != null && getAllSubAreaOutput.Info != null) {
//                    for (SubAreaInfo item : getAllSubAreaOutput.Info)
//                        new SubAreaController().addSubArea(item);
//                }
//
//
//            }
//        });
//    }
//
//    public static void importEmployeeByEmployeeCode(final String organizationCode, final String employeeCode, final String teamCode) {
//        processTransaction(new Runnable() {
//            @Override
//            public void run() {
//
//                new EmployeeController().deleteEmployeeByID(employeeCode);
//                new EmployeeDetailController().deleteEmployeeDetailByTeamCodeByEmployeeCode(teamCode, employeeCode);
//
//                // (1) Import Sale
//                GetEmployeeDetailByEmployeeCodeInputInfo inputSaleEmp = new GetEmployeeDetailByEmployeeCodeInputInfo();
//                inputSaleEmp.OrganizationCode = organizationCode;
//                inputSaleEmp.EmployeeCode = employeeCode;
//                inputSaleEmp.TeamCode = teamCode;
//                GetEmployeeDetailByEmployeeCodeOutputInfo outputSaleEmp = TSRService.getEmployeeDetailByEmployeeCode(inputSaleEmp, false);
//                if (outputSaleEmp != null && outputSaleEmp.Info != null) {
//                    List<EmployeeDetailInfo> saleEmpDetailList = outputSaleEmp.Info;
//                    EmployeeInfo saleEmp = new EmployeeInfo();
//                    saleEmp.OrganizationCode = saleEmpDetailList.get(0).OrganizationCode;
//                    saleEmp.EmpID = saleEmpDetailList.get(0).EmployeeCode;
//                    saleEmp.FirstName = saleEmpDetailList.get(0).FirstName;
//                    saleEmp.LastName = saleEmpDetailList.get(0).LastName;
//                    saleEmp.TeamCode = saleEmpDetailList.get(0).TeamCode;
//                    saleEmp.TeamName = saleEmpDetailList.get(0).TeamName;
//                    saleEmp.TeamHeadCode = saleEmpDetailList.get(0).TeamHeadCode;
//                    saleEmp.Status = saleEmpDetailList.get(0).Status;
//                    new EmployeeController().addEmployee(saleEmp);
//                    for (EmployeeDetailInfo saleEmpDetail : saleEmpDetailList) {
//                        new EmployeeDetailController().addEmployeeDetail(saleEmpDetail);
//                    }
//
//                    // (2) Import Sale-TeamLeader (When SaleEmpID <> TeamHeadCode)
//                    if (!saleEmp.EmpID.equals(saleEmp.TeamHeadCode)) {
//                        new EmployeeController().deleteEmployeeByID(saleEmp.TeamHeadCode);
//                        new EmployeeDetailController().deleteEmployeeDetailByTeamCodeByEmployeeCode(saleEmp.TeamCode, saleEmp.TeamHeadCode);
//
//                        GetEmployeeDetailByEmployeeCodeInputInfo inputTeamHeadEmp = new GetEmployeeDetailByEmployeeCodeInputInfo();
//                        inputTeamHeadEmp.OrganizationCode = saleEmp.OrganizationCode;
//                        inputTeamHeadEmp.EmployeeCode = saleEmp.TeamHeadCode;
//                        inputTeamHeadEmp.TeamCode = saleEmp.TeamCode;
//                        GetEmployeeDetailByEmployeeCodeOutputInfo outputTeamHeadEmp = TSRService.getEmployeeDetailByEmployeeCode(inputTeamHeadEmp, false);
//                        if (outputTeamHeadEmp != null && outputTeamHeadEmp.Info != null) {
//                            List<EmployeeDetailInfo> teamHeadEmpDetailList = outputTeamHeadEmp.Info;
//                            EmployeeInfo teamHeadEmp = new EmployeeInfo();
//                            teamHeadEmp.OrganizationCode = teamHeadEmpDetailList.get(0).OrganizationCode;
//                            teamHeadEmp.EmpID = teamHeadEmpDetailList.get(0).EmployeeCode;
//                            teamHeadEmp.FirstName = teamHeadEmpDetailList.get(0).FirstName;
//                            teamHeadEmp.LastName = teamHeadEmpDetailList.get(0).LastName;
//                            teamHeadEmp.TeamCode = teamHeadEmpDetailList.get(0).TeamCode;
//                            teamHeadEmp.TeamName = teamHeadEmpDetailList.get(0).TeamName;
//                            teamHeadEmp.TeamHeadCode = teamHeadEmpDetailList.get(0).TeamHeadCode;
//                            teamHeadEmp.Status = teamHeadEmpDetailList.get(0).Status;
//                            new EmployeeController().addEmployee(teamHeadEmp);
//                            for (EmployeeDetailInfo teamHeadEmpDetail : teamHeadEmpDetailList) {
//                                new EmployeeDetailController().addEmployeeDetail(teamHeadEmpDetail);
//                            }
//                        }
//                    }
//
//                    // (3) Import Team from Contract
//                    new TeamController().deleteTeamByCode(teamCode);
//                    GetTeamByIDInputInfo teamInput = new GetTeamByIDInputInfo();
//                    teamInput.Code = teamCode;
//                    GetTeamByIDOutputInfo teamOutput = TSRService.getTeamByID(teamInput, false);
//                    if (teamOutput.Info != null) {
//                        new TeamController().addTeam(teamOutput.Info);
//                    }
//
//                    // (4) Import Team from SaleEmployeeCode
//                    if (!teamCode.equals(saleEmp.TeamCode)) {
//                        new TeamController().deleteTeamByCode(saleEmp.TeamCode);
//                        GetTeamByIDInputInfo saleTeamInput = new GetTeamByIDInputInfo();
//                        saleTeamInput.Code = saleEmp.TeamCode;
//                        GetTeamByIDOutputInfo saleTeamOutput = TSRService.getTeamByID(saleTeamInput, false);
//                        if (saleTeamOutput.Info != null) {
//                            new TeamController().addTeam(saleTeamOutput.Info);
//                        }
//                    }
//
//                }
//
//
//            }
//        });
//    }

    public static void deleteContractImage(String ImageID) {
        new ContractImageController().deleteContractImageByImageID(ImageID);
        DeleteContractImageInputInfo contimgInput = new DeleteContractImageInputInfo();
        contimgInput.ImageID = ImageID;
        TSRService.deleteContractImage(contimgInput, false);

    }


    public static List<EmployeeInfo> getEmployee() {
        return new EmployeeController().getEmployee();
    }

    public static List<EmployeeInfo> getEmployeeForPreSale() {
        return new EmployeeController().getEmployeeForPreSale();
    }

    public static List<ProductStockInfo> getProductStock(
            String organizationCode, String teamCode) {
        return new ProductStockController().getProductStock(organizationCode,
                teamCode);
    }

    public static List<PaymentInfo> getPayment() {
        return new PaymentController().getPayment();
    }

    public static List<ReceiptInfo> getReceipt() {
        return new ReceiptController().getReceipt();
    }

    public static List<ReceiptInfo> getReceiptByRefNo(String RefNo) {
        return new ReceiptController().getReceiptByRefNo(RefNo);
    }

    public static ReceiptInfo getReceiptByReceiptID(String organizationCode, String ReceiptID) {
        return new ReceiptController().getReceiptByReceiptID(organizationCode, ReceiptID);
    }

    public static void updateReceipt(ReceiptInfo info, boolean isSchedule) {
        ReceiptInfo receiptInfo = new ReceiptController().getReceiptByReceiptID(info.OrganizationCode, info.ReceiptID);

        if (receiptInfo != null) {
            new ReceiptController().updateReceipt(info);

            UpdateReceiptInputInfo updateReceiptInputInfo = UpdateReceiptInputInfo.from(info);
            TSRService.updateReceipt(updateReceiptInputInfo, isSchedule);
        }
    }

    public static List<ProblemContractInfo> getProblemContractList() {
        return new ProblemContractController()
                .getProblemContractByProblemCode();
    }

    public static List<ReturnProductInfo> getReturnProductByCondition(String organizationCode, String returnProductID, String teamCode, String empID) {
        return new ReturnProductController().getReturnProductByCondition(organizationCode, returnProductID, teamCode, empID);
    }

    public static List<ReturnProductDetailInfo> getReturnProductDetailByID(String organizationCode, String returnProductID) {
        return new ReturnProductController().getReturnProductDetailByID(organizationCode, returnProductID);
    }

    public static List<ProductStockInfo> getAvailableProductStockForReturn(
            String organizationCode, String subTeamCode, String teamCode) {
        return new ReturnProductController().getAvailableProductStockForReturn(
                organizationCode, subTeamCode, teamCode);
    }

    public static void insertRequestReturnProduct(final ReturnProductInfo retProduct,
                                                  final List<ReturnProductDetailInfo> retProdDetailList) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                // (1) Insert table ReturnProduct
                new ReturnProductController().insertReturnProduct(retProduct); // Local-DB
                TSRService.addReturnProduct(AddReturnProductInputInfo.from(retProduct), false); // Server-WS

                for (ReturnProductDetailInfo retProdDetail : retProdDetailList) {
                    // (2) Insert table ReturnProductDetail
                    new ReturnProductController().insertReturnProductDetail(retProdDetail); // Local-DB
                    TSRService.addReturnProductDetail(AddReturnProductDetailInputInfo.from(retProdDetail), false); // Server-WS
                    // (3) Update ProductStock.Status Or Contract.TradeInReturnFlag
                    ProductStockInfo item = getProductStockSerialNumber(retProdDetail.ProductSerialNumber);
                    if (item != null) {
                        // 3.1 ProductStock
                        item.Status = ProductStockStatus.WAIT_RETURN.toString();
                        updateProductStock(item, false);
                    } else {
                        // 3.2 Contract
                        ContractInfo cont = getContract(retProdDetail.RefNo);
                        if (cont != null) {
                            cont.TradeInReturnFlag = true;
                            cont.LastUpdateBy = BHPreference.employeeID();
                            cont.LastUpdateDate = new Date();
                            new ContractController().updateTradeInReturnFlag(cont); // Local-DB
                            TSRService.updateTradeInReturnFlag(UpdateTradeInReturnFlagInputInfo.from(cont), false); // Server-DB
                        }
                    }
                }
            }
        });
    }

    public static ReturnProductInfo getLastReturnProduct(String employeeID, String yearMonth) {
        return new ReturnProductController().getLastReturnProduct(employeeID, yearMonth);
    }

    /*
    public static void updateProblemContract(ProblemContractInfo info,
                                             boolean isSchedule) {
        new ProblemContractController().updateProblemContract(info);

        UpdateProblemContractInputInfo input = UpdateProblemContractInputInfo
                .from(info);
        UpdateProblemContractOutputInfo output = TSRService
                .updateProblemContract(input, isSchedule);

    }
    */
    public static void addProblemContractInfo(ProblemContractInfo info) {
        new ProblemContractController().addProblemContract(info);
    }

    public static ProblemContractInfo getProblemContractByRefNo(String RefNo) {
        return new ProblemContractController().getProblemContractByRefNo(RefNo);
    }

    public static void UpdateImpoundProduct(ImpoundProductInfo impoundProductInfo) {
        new ImpoundProductController().updateImpoundProduct(impoundProductInfo);
    }

    public static SalePaymentPeriodInfo getSummaryNetAmount(String refNO) {
        return new SalePaymentPeriodController().getSummaryNetAmount(refNO);
    }

    public static List<ReportDailySummaryInfo> getReportDailySummary(int saleLevel, String fillterString) {
        return new ReportDailySummaryController().getReportDailySummary(saleLevel, fillterString);
    }

    public static List<ReportDailySalesTranInfo> getReportDailySalesTran(String SaleLevel, String EmpD) {
        return new ReportDailySummaryController().getReportDailySalesTran(SaleLevel, EmpD);
    }

    public static List<ReportDailyImpoundTranInfo> getReportDailyImpoundTran(String SaleLevel, String EmpD) {
        return new ReportDailySummaryController().getReportDailyImpoundTran(SaleLevel, EmpD);
    }

    public static List<ReportDailyChangeProTranInfo> getReportDailyChangeProTran(String SaleLevel, String EmpD) {
        return new ReportDailySummaryController().getReportDailyChangeProTran(SaleLevel, EmpD);
    }

    public static List<ReportDailyChangeContTranInfo> getReportDailyChangeContTran(String SaleLevel, String EmpD) {
        return new ReportDailySummaryController().getReportDailyChangeContTran(SaleLevel, EmpD);
    }

    public static List<ReportDailyWriteOffNPLTranInfo> getReportDailyWriteOffNPLTran(String SaleLevel, String EmpD) {
        return new ReportDailySummaryController().getReportDailyWriteOffNPLTran(SaleLevel, EmpD);
    }

    public static List<ReportDailySendMoneyTranInfo> getReportDailySendMoneyTran(String SaleLevel, String EmpD) {
        return new ReportDailySummaryController().getReportDailySendMoneyTran(SaleLevel, EmpD);
    }

    public static List<ReportDailySendDocumentTranInfo> getReportDailySendDocumentTran(String SaleLevel, String EmpD) {
        return new ReportDailySummaryController().getReportDailySendDocumentTran(SaleLevel, EmpD);
    }

    public static List<ReportInventoryInfo> getReportInventory(String fortnight, String productcode, String teamcode) {
        return new ReportInventoryController().getReportInventory(fortnight, productcode, teamcode);
    }

    public static List<ReportSummaryChangeContractInfo> getReportSummaryChangeContract(String FortnightID, String TeamCode) {
        return new ReportController().getReportSummaryChangeContract(FortnightID, TeamCode);
    }

    public static List<ReportSummaryChangeProductInfo> getReportSummaryChangeProduct(String FortnightID, String TeamCode) {
        return new ReportController().getReportSummaryChangeProduct(FortnightID, TeamCode);
    }

    public static List<ReportInstallAndPaymentInfo> getReportByTeam(String fortnight, String reporttype, String teamcode) {
        return new ReportInstallAndPaymentController().getReportByTeam(fortnight, reporttype, teamcode);
    }

    public static List<ReportInstallAndPaymentInfo> getReportBySale(String fortnight, String reporttype, String salecode) {
        return new ReportInstallAndPaymentController().getReportBySale(fortnight, reporttype, salecode);
    }

    public static List<ReportFirstAndNextReceiveInfo> getReportFirstandNext(String fortnight, String teamcode) {
        return new ReportFirstAndNextReceiveController().getReportFirstandNext(fortnight, teamcode);
    }

    public static List<ReportSummaryTradeProductReceiveInfo> getReportSummaryTradeProductReceive(String FortnightID, String TeamCode, String SaleCode) {
        return new ReportController().getReportSummaryTradeProductReceive(FortnightID, TeamCode, SaleCode);
    }

    public static List<ReportSummaryTradeProductNotReceiveInfo> getReportSummaryTradeProductNotReceive(String FortnightID, String TeamCode, String SaleCode) {
        return new ReportController().getReportSummaryTradeProductNotReceive(FortnightID, TeamCode, SaleCode);
    }

    public static List<ReportCustomerProblemInfo> getReportProblem(String fortnight, String teamCode, String reporttype) {
        return new ReportCustomerProblemController().getReportProblem(fortnight, teamCode, reporttype);
    }

    public static List<ReportSummaryImpoundProductInfo> getReportSummaryImpoundProduct(String FortnightID, String TeamCode) {
        return new ReportController().getReportSummaryImpoundProduct(FortnightID, TeamCode);
    }

    public static List<TeamInfo> getTeam() {
        return new TeamController().getTeam();
    }

    public static List<TeamInfo> getTeamBySup(String supervisorcode) {
        return new TeamController().getTeamBySup(supervisorcode);
    }

    public static List<ReportSummaryWriteOffNPLInfo> getReportSummaryWriteOffNPL(String FortnightID, String TeamCode, String WriteOffNPLType) {
        return new ReportController().getReportSummaryWriteOffNPL(FortnightID, TeamCode, WriteOffNPLType);
    }

    public static List<ReportSummaryRangOfFirstReceiveInfo> getReportSummaryRangOfFirstReceive(String FortnightID, String TeamCode) {
        return new ReportController().getReportSummaryRangOfFirstReceive(FortnightID, TeamCode);
    }

    public static List<ReportApprovedInfo> getReportRequest(String fortnight, String teamcode, String reporttype) {
        return new ReportApprovedController().getReportRequest(fortnight, teamcode, reporttype);
    }

    public static DiscountLimitInfo getDiscountLimitByTypeAndProductID(String DiscountType, String ProductID, float DiscountPrice) {
        return new DiscountLimitController().getDiscountLimitByTypeAndProductID(DiscountType, ProductID, DiscountPrice);
    }

    public static FortnightInfo getFortnight(String OrganizationCode) {
        return new FortnightController().getFortnight(OrganizationCode);
    }

    public static GetDeviceMenusOutputInfo getDeviceMenus(GetDeviceMenusInputInfo info) {
        return TSRService.getDeviceMenus(info);
    }


    public static void addDamageProduct(DamageProductInfo info, boolean isSchedule) {
        // (1) Local-DB
        new DamageProductController().addDamageProduct(info);
        // (2) Server-DB
        AddDamageProductInputInfo input = AddDamageProductInputInfo.from(info);
        AddDamageProductOutputInfo output = TSRService.addDamageProduct(input, isSchedule);
    }

    public static List<TransactionLogInfo> getTransactionLogBySyncStatus(boolean SyncStatus) {
        return new TransactionLogController().getTransactionLogBySyncStatus(SyncStatus);
    }

    public static List<TransactionLogInfo> getAllTransactionLog() {
        return new TransactionLogController().getAllTransactionLog();
    }


    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            for (File f : dir.listFiles()) {
                deleteDir(f);
            }
        }
        return dir.delete();
    }

    public static void saveContractImage(ContractImageInfo info) {
        /**Create Folder*/
        File newFolder = new File(String.format("%s/%s/%s/", BHStorage.getFolder(BHStorage.FolderType.Picture), info.RefNo, info.ImageTypeCode));
        if (!newFolder.exists() || !newFolder.isDirectory()) {
            if (!newFolder.isDirectory()) {
                newFolder.delete();
            }
            newFolder.mkdirs();
        }

        /**Create Image*/
        File importImage = new File(newFolder, info.ImageName);
        byte[] Decoded = Base64.decode(info.ImageData, Base64.DEFAULT);

        try {
            importImage.createNewFile();
            FileOutputStream fos = new FileOutputStream(importImage);
            fos.write(Decoded);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //YIM Add here
    public static void saveDepartmentSignatureImage(String signatureImage) {

        /**Create Folder*/
        File newFolder = new File(BHPreference.departmentSignaturePath);
        if (!newFolder.exists() || !newFolder.isDirectory()) {
            if (!newFolder.isDirectory()) {
                newFolder.delete();
            }
            newFolder.mkdirs();
        }

        /**Create Image*/
        File importImage = new File(newFolder,BHPreference.departmentSignatureName);
        byte[] Decoded = Base64.decode(signatureImage, Base64.DEFAULT);

        try {
            importImage.createNewFile();
            FileOutputStream fos = new FileOutputStream(importImage);
            fos.write(Decoded);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    public static void addSendDocument(SendDocumentInfo info, boolean isSchedule) {
        new SendDocumentController().addSendDocument(info);

        AddSendDocumentInputInfo input = AddSendDocumentInputInfo.from(info);
        AddSendDocumentOutputInfo output = TSRService.addSendDocument(input, isSchedule);
    }

    public static void updateSendDocument(SendDocumentInfo info, boolean isSchedul) {
        new SendDocumentController().updateSendDocument(info);
        UpdateSendDocumentInputInfo input = UpdateSendDocumentInputInfo.from(info);
        UpdateSendDocumentOutputInfo output = TSRService.updateSendDocument(input, isSchedul);
    }

    public static SendDocumentInfo getSendDocumentBySentTeamCodeAndSentSubTeamCode(String SentTeamCode, String SentSubTeamCode) {
        return new SendDocumentController().getSendDocumentBySentTeamCodeAndSentSubTeamCode(SentTeamCode, SentSubTeamCode);
    }

    public static void addSendDocumentDetail(SendDocumentDetailInfo info, boolean isSchedule) {
        new SendDocumentDetailController().addSendDocumentDetail(info);
        AddSendDocumentDetailInputInfo input = AddSendDocumentDetailInputInfo.from(info);
        AddSendDocumentDetailOutputInfo output = TSRService.addSendDocumentDetail(input, isSchedule);
    }

    public static void deleteSendDocumentDetail(SendDocumentDetailInfo info, boolean isSchedule) {
        new SendDocumentDetailController().deleteSendDocumentDetail(info);
        DeleteSendDocumentDetailByPrintHistoryIDInputInfo input = DeleteSendDocumentDetailByPrintHistoryIDInputInfo.from(info);
        DeleteSendDocumentDetailByPrintHistoryIDOutputInfo output = TSRService.deleteSendDocumentDetailByPrintHistoryID(input, isSchedule);
    }

    public static SendDocumentDetailInfo getSendDocumentDetailByPrintHistoryID(String PrintHistoryID) {
        return new SendDocumentDetailController().getSendDocumentDetailByPrintHistoryID(PrintHistoryID);
    }

    public static ManualDocumentWithdrawalInfo getManualDocumentWithdrawalByType(String manualDocTypeID) {
        return new ManualDocumentWithdrawalController().getManualDocumentWithdrawalByType(manualDocTypeID);
    }

    public static ManualDocumentWithdrawalInfo getManualDocumentWithdrawalByVolumeNoASC(String manualDocTypeID, String EmpID) {
        return new ManualDocumentWithdrawalController().getManualDocumentWithdrawalByVolumeNoASC(manualDocTypeID, EmpID);
    }

    public static ManualDocumentWithdrawalInfo getManualDocumentWithdrawalByVolume(String manualDocTypeID, String VolumeNo, String EmpID) {
        return new ManualDocumentWithdrawalController().getManualDocumentWithdrawalByVolume(manualDocTypeID, VolumeNo, EmpID);
    }

    public static ManualDocumentInfo getManualDocumentByNumber(String manualDocTypeID, String VolumnNo, String DocumentNumber) {
        return new ManualDocumentWithdrawalController().getManualDocumentByNumber(manualDocTypeID, VolumnNo, DocumentNumber);
    }



    /**
     * ** Assign ***
     **/
    public static void addAssign(AssignInfo assign, boolean isSchedule) {
        new AssignController().addAssign(assign);
        AddAssignInputInfo assignInput = AddAssignInputInfo.from(assign);
        TSRService.addAssign(assignInput, isSchedule);
    }

    public static void updateAssign(AssignInfo assign, boolean isSchedule) {
        new AssignController().updateAssign(assign);
        UpdateAssignInputInfo updateAssignFromInput = UpdateAssignInputInfo.from(assign);
        TSRService.updateAssign(updateAssignFromInput, isSchedule);
    }

    public static void updateAssignForSortOrderDefault(AssignInfo assign, boolean isSchedule) {
        new AssignController().updateAssignForSortOrderDefault(assign);
        UpdateAssignForSortOrderDefaultInputInfo  input = UpdateAssignForSortOrderDefaultInputInfo.from(assign);
        TSRService.updateAssignForSortOrderDefault(input, isSchedule);
    }

    /*** [START] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/
    public static void updateAssignForSortOrderDefaultForCredit(AssignInfo assign, boolean isSchedule) {
        new AssignController().updateAssignForSortOrderDefaultForCredit(assign);

        //จะ update เมื่อไม่เป็นวันที่เดียวกัน
        if(assign.OldPaymentDueDay != assign.NewPaymentDueDay){
            List<SalePaymentPeriodInfo> salePaymentPeriodInfo = new SalePaymentPeriodController().getSalePaymentPeriodByRefNoAndPaymentComplete(assign.RefNo, 0);

            TripInfo currentPaymentApointmentTrip;

            for(SalePaymentPeriodInfo info : salePaymentPeriodInfo){
                /*//PaymentDueDate
                Calendar dueDate = getInstance();
                dueDate.setTime(info.PaymentDueDate);
                dueDate.set(Calendar.HOUR_OF_DAY, 0);
                dueDate.set(Calendar.MINUTE, 0);
                dueDate.set(Calendar.SECOND, 0);
                dueDate.set(Calendar.MILLISECOND, 0);
                dueDate.set(Calendar.DAY_OF_MONTH, dueDate.getActualMaximum(Calendar.DAY_OF_MONTH) < assign.NewPaymentDueDay ? dueDate.getActualMaximum(Calendar.DAY_OF_MONTH) : assign.NewPaymentDueDay);*/

                //PaymentAppointmentDate
                Calendar appointmentDate = getInstance();
                appointmentDate.setTime(info.PaymentAppointmentDate);
                appointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                appointmentDate.set(Calendar.MINUTE, 0);
                appointmentDate.set(Calendar.SECOND, 0);
                appointmentDate.set(Calendar.MILLISECOND, 0);
                appointmentDate.set(Calendar.DAY_OF_MONTH, appointmentDate.getActualMaximum(Calendar.DAY_OF_MONTH) < assign.NewPaymentDueDay ? appointmentDate.getActualMaximum(Calendar.DAY_OF_MONTH) : assign.NewPaymentDueDay);

                //info.PaymentDueDate = dueDate.getTime();
                info.LastUpdateDate = assign.LastUpdateDate;
                info.LastUpdateBy = assign.LastUpdateBy;

                Calendar StartTripDate = getInstance();

                currentPaymentApointmentTrip = new TripController().getTrip(info.PaymentAppointmentDate);
                StartTripDate.setTime(currentPaymentApointmentTrip.StartDate);
                StartTripDate.set(Calendar.HOUR_OF_DAY, 0);
                StartTripDate.set(Calendar.MINUTE, 0);
                StartTripDate.set(Calendar.SECOND, 0);
                StartTripDate.set(Calendar.MILLISECOND, 0);

                Calendar EndTripDate = getInstance();
                EndTripDate.setTime(currentPaymentApointmentTrip.EndDate);
                EndTripDate.set(Calendar.HOUR_OF_DAY, 0);
                EndTripDate.set(Calendar.MINUTE, 0);
                EndTripDate.set(Calendar.SECOND, 0);
                EndTripDate.set(Calendar.MILLISECOND, 0);

                Calendar NewAppointmentDate = getInstance();
                NewAppointmentDate.setTime(info.PaymentAppointmentDate);
                NewAppointmentDate.set(Calendar.HOUR_OF_DAY, 0);
                NewAppointmentDate.set(Calendar.MINUTE, 0);
                NewAppointmentDate.set(Calendar.SECOND, 0);
                NewAppointmentDate.set(Calendar.MILLISECOND, 0);



                if (assign.NewPaymentDueDay > StartTripDate.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                    NewAppointmentDate.setTime(StartTripDate.getTime());
                    NewAppointmentDate.set(Calendar.DAY_OF_MONTH, StartTripDate.getActualMaximum(Calendar.DAY_OF_MONTH));
                } else {
                    Calendar compareDate = getInstance();
                    compareDate.setTime(StartTripDate.getTime());
                    compareDate.set(Calendar.DAY_OF_MONTH, assign.NewPaymentDueDay);
                    if(StartTripDate.compareTo(compareDate) == 1) {
                        NewAppointmentDate.setTime(EndTripDate.getTime());
                        NewAppointmentDate.set(Calendar.DAY_OF_MONTH, assign.NewPaymentDueDay);
                    } else {
                        NewAppointmentDate.setTime(StartTripDate.getTime());
                        NewAppointmentDate.set(Calendar.DAY_OF_MONTH, assign.NewPaymentDueDay);
                    }
                }

                Calendar CurrentDate = getInstance();
                CurrentDate.setTime(new Date());
                CurrentDate.set(Calendar.HOUR_OF_DAY, 0);
                CurrentDate.set(Calendar.MINUTE, 0);
                CurrentDate.set(Calendar.SECOND, 0);
                CurrentDate.set(Calendar.MILLISECOND, 0);

                if(CurrentDate.compareTo(NewAppointmentDate) == 1){
                    NewAppointmentDate.setTime(CurrentDate.getTime());
                }

                info.PaymentAppointmentDate = NewAppointmentDate.getTime();
                new SalePaymentPeriodController().updateSalePaymentPeriodForSortOrderDefaultForCredit(info);

//                if(assign.PaymentPeriodNumber == info.PaymentPeriodNumber){
//                    //ถ้าเป็นงวด min จะต้องตรวจสอบว่าวัดที่เลื่อนนัด มากกว่าของเดิมหรือไม ถึงจะทำการ update
//                    Calendar c = getInstance();
//                    c.setTime(new Date());
//                    c.set(Calendar.HOUR_OF_DAY, 0);
//                    c.set(Calendar.MINUTE, 0);
//                    c.set(Calendar.SECOND, 0);
//                    c.set(Calendar.MILLISECOND, 0);
//
//                    if(c.getTime().before(appointmentDate.getTime()) || c.getTime().equals(appointmentDate.getTime())){
//                        info.PaymentAppointmentDate = appointmentDate.getTime();
//                    }
//
//                    new SalePaymentPeriodController().updateSalePaymentPeriodForSortOrderDefaultForCredit(info);
//                } else if(info.PaymentPeriodNumber > assign.PaymentPeriodNumber) {
//                    info.PaymentAppointmentDate = appointmentDate.getTime();
//                    new SalePaymentPeriodController().updateSalePaymentPeriodForSortOrderDefaultForCredit(info);
//                }
            }

            new ContractController().updateContractForSortOrderDefaultForCredit(assign.RefNo, assign.OrganizationCode, String.valueOf(assign.NewPaymentDueDay), assign.LastUpdateDate, assign.LastUpdateBy);
        }

        UpdateNewAssignForSortOrderDefaultInputInfo input = UpdateNewAssignForSortOrderDefaultInputInfo.from(assign);
        TSRService.updateNewAssignForSortOrderDefault(input, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/

    public static void updateAssignForPostpone(AssignInfo assign, boolean isSchedule) {
        new AssignController().updateAssignForPostpone(assign);
        UpdateAssignInputInfo updateAssignFromInput = UpdateAssignInputInfo.from(assign);
        TSRService.updateAssign(updateAssignFromInput, isSchedule);
    }

    public static void addOrUpdateAssign(AssignInfo info) {
        List<AssignInfo> ret = new AssignController().getAssignByRefNoByTaskTypeByReferenceID(info.RefNo, info.TaskType, info.ReferenceID);
        if (ret != null && ret.size() > 0) {
            new AssignController().updateAssign(info);
            UpdateAssignInputInfo updateAssInput = UpdateAssignInputInfo.from(info);
            TSRService.updateAssign(updateAssInput, false);
        } else {
            info.AssignID = DatabaseHelper.getUUID();
            new AssignController().addAssign(info);
            AddAssignInputInfo addAssInput = AddAssignInputInfo.from(info);
            TSRService.addAssign(addAssInput, false);
        }
    }

    public static void deleteAssign(String AssignID, boolean isSchedule) {
        new AssignController().deleteAssignByAssignID(AssignID);
        DeleteAssignInputInfo assignInput = new DeleteAssignInputInfo();
        assignInput.AssignID = AssignID;
        TSRService.deleteAssign(assignInput, isSchedule);
    }

    public static void deleteAssignByRefNoByTaskTypeByReferenceID(String refNO, String taskType, String referenceID) {
        new AssignController().deleteAssignByRefNoByTaskTypeByReferenceID(refNO, taskType, referenceID);
        AssignInfo assign = new AssignInfo();
        assign.RefNo = refNO;
        assign.TaskType = taskType;
        assign.ReferenceID = referenceID;
        DeleteAssignByRefNoByTaskTypeByReferenceIDInputInfo input = DeleteAssignByRefNoByTaskTypeByReferenceIDInputInfo.from(assign);
        DeleteAssignByRefNoByTaskTypeByReferenceIDOutputInfo output = TSRService.deleteAssignByRefNoByTaskTypeByReferenceID(input, false);
    }


    public static void importCredit() {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                GetAssignByAssigneeTeamCodeInputInfo assignInput = new GetAssignByAssigneeTeamCodeInputInfo();
                assignInput.OrganizationCode = BHPreference.organizationCode();
                assignInput.AssigneeTeamCode = BHPreference.teamCode();
                assignInput.AssigneeEmpID = BHPreference.employeeID();

                List<AssignInfo> assignList = TSRService.GetAssignSalePaymentPeriod(assignInput, false).Info;
                new AssignController().deleteAssignByAssignList(assignList);

                List<SalePaymentPeriodInfo> salePaymentPeriodList = TSRService.GetImportCredit(assignInput, false).Info;
                if (assignList != null && assignList.size() > 0 && salePaymentPeriodList != null && salePaymentPeriodList.size() > 0) {
                    for (SalePaymentPeriodInfo salePaymentPeriod : salePaymentPeriodList) {
                        //** Fixed - [BHPRJ00301-4073]
//                        TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), salePaymentPeriod.RefNo, true, true, true);
                        TSRController.importContractFromServer(BHPreference.organizationCode(), BHPreference.teamCode(), salePaymentPeriod.RefNo);
                    }
                    for (AssignInfo assign : assignList) {
                        AssignInfo exist = new AssignController().getAssignByAssignID(assign.AssignID);
                        if (exist == null) {
//                            AssignInfo maxOrderByPaymentAppointmentDate = new AssignController().getMaxOrderByPaymentAppointmentDate(BHPreference.organizationCode(), BHPreference.teamCode()
//                                    , BHPreference.employeeID(), assign.ReferenceID);
//                            assign.Order = maxOrderByPaymentAppointmentDate.Order;
//                            assign.OrderExpect = maxOrderByPaymentAppointmentDate.Order;
                            new AssignController().addAssign(assign);
                        }
                    }
                }
            }
        });
    }

    public static void importSpareDrawdown() {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                new SpareDrawdownController().deleteSpareDrawdownAll();
                new SpareDrawdownDetailController().deleteSpareDrawdownDetailAll();

                GetSpareDrawdownByEmployeeIDInputInfo spareDrawdownInput = new GetSpareDrawdownByEmployeeIDInputInfo();
                spareDrawdownInput.OrganizationCode = BHPreference.organizationCode();
                spareDrawdownInput.EmployeeID = BHPreference.employeeID();

                /*** [STATRT] Fixed - [BHPROJ-0026-606] :: ยกเลิกขั้นตอนในการบันทึกใบร้องขอเพื่อเบิกเครื่อง/อะไหล่ (สำรอง) เนื่องจากระบบ TSR-Stock ใหม่ มีอยู่แล้ว ***/
                /*
                List<SpareDrawdownInfo> spareDrawdownList = TSRService.GetSpareDrawdownByEmployeeID(spareDrawdownInput, false).Info;

                if (spareDrawdownList != null && spareDrawdownList.size() > 0) {
                    for (SpareDrawdownInfo sdInfo : spareDrawdownList) {
                        new SpareDrawdownController().addSpareDrawdown(sdInfo);

                        GetSpareDrawdownDetailBySpareDrawdownIDInputInfo spareDrawdownDetailInput = new GetSpareDrawdownDetailBySpareDrawdownIDInputInfo();
                        spareDrawdownDetailInput.SpareDrawdownID = sdInfo.SpareDrawdownID;

                        List<SpareDrawdownDetailInfo> spareDrawdownDetailList = TSRService.GetSpareDrawdownDetailBySpareDrawdownID(spareDrawdownDetailInput, false).Info;

                        if (spareDrawdownDetailList != null && spareDrawdownDetailList.size() > 0) {
                            for (SpareDrawdownDetailInfo sddInfo : spareDrawdownDetailList) {
                                new SpareDrawdownDetailController().addSpareDrawdownDetail(sddInfo);
                            }
                        }
                    }
                }
                */
                /*** [END] Fixed - [BHPROJ-0026-606] :: ยกเลิกขั้นตอนในการบันทึกใบร้องขอเพื่อเบิกเครื่อง/อะไหล่ (สำรอง) เนื่องจากระบบ TSR-Stock ใหม่ มีอยู่แล้ว ***/

            }
        });
    }

    public static void addCutOffContract(CutOffContractInfo info, boolean isSchedule) {
        new CutOffContractController().addCutOffContract(info);
        AddCutOffContractInputInfo cutOffContractInput = AddCutOffContractInputInfo.from(info);
        TSRService.addCutOffContract(cutOffContractInput, isSchedule);
    }

    public static void updateCutOffContract(CutOffContractInfo info, boolean isSchedule) {
        new CutOffContractController().updateCutOffContract(info);
        UpdateCutOffContractInputInfo cutOffContractInput = UpdateCutOffContractInputInfo.from(info);
        TSRService.updateCutOffContract(cutOffContractInput, isSchedule);
    }

    public static void addCutDivisorContract(CutDivisorContractInfo info, boolean isSchedule) {
        new CutDivisorContractController().addCutDivisorContract(info);
        AddCutDivisorContractInputInfo CutDivisorContractInput = AddCutDivisorContractInputInfo.from(info);
        TSRService.addCutDivisorContract(CutDivisorContractInput, isSchedule);
    }

    public static void addRequestNextPayment(RequestNextPaymentInfo info, boolean isSchedule) {
        new RequestNextPaymentController().addRequestNextPayment(info);
        AddRequestNextPaymentInputInfo RequestNextPaymentInput = AddRequestNextPaymentInputInfo.from(info);
        TSRService.addRequestNextPayment(RequestNextPaymentInput, isSchedule);
    }

    public static void updateRequestNextPayment(RequestNextPaymentInfo info, boolean isSchedule) {
        new RequestNextPaymentController().updateRequestNextPayment(info);
        UpdateRequestNextPaymentInputInfo RequestNextPaymentInput = UpdateRequestNextPaymentInputInfo.from(info);
        TSRService.updateRequestNextPayment(RequestNextPaymentInput, isSchedule);
    }

    public static void SynchDataFromServer2Local(final String OrganizationCode, final String TeamCode, final String EmpID) {
        processTransaction(new Runnable() {
            @Override
            public void run() {
                SynchStatusInputInfo input = new SynchStatusInputInfo();
                input.OrganizationCode = OrganizationCode;
                input.TeamCode = TeamCode;
                input.EmpID = EmpID;
                input.CallStatus = "CREATE";
                input.IsAdmin = BHPreference.IsAdmin();
                SynchStatusOutputInfo output = TSRService.synchDataFromServer2Local(input, false);
                if (output.ResultCode == 0) {

                } else {
//                    throw new EmptyStackException();
                    throw new RuntimeException(output.ResultDescription);
                }

            }
        });
    }

    //region SpareDrawdown
    public static void addSpareDrawdown(SpareDrawdownInfo info, boolean isSchedule) {
        new SpareDrawdownController().addSpareDrawdown(info);
        AddSpareDrawdownInputInfo SpareDrawdownInput = AddSpareDrawdownInputInfo.from(info);
        //-- Fixed - [BHPROJ-0026-606] :: ยกเลิกขั้นตอนในการบันทึกใบร้องขอเพื่อเบิกเครื่อง/อะไหล่ (สำรอง) เนื่องจากระบบ TSR-Stock ใหม่ มีอยู่แล้ว
//        TSRService.addSpareDrawdown(SpareDrawdownInput, isSchedule);
    }

    public static void updateSpareDrawdown(SpareDrawdownInfo info, boolean isSchedule) {
        new SpareDrawdownController().updateSpareDrawdown(info);
        UpdateSpareDrawdownInputInfo SpareDrawdownInput = UpdateSpareDrawdownInputInfo.from(info);
        //-- Fixed - [BHPROJ-0026-606] :: ยกเลิกขั้นตอนในการบันทึกใบร้องขอเพื่อเบิกเครื่อง/อะไหล่ (สำรอง) เนื่องจากระบบ TSR-Stock ใหม่ มีอยู่แล้ว
//        TSRService.updateSpareDrawdown(SpareDrawdownInput, isSchedule);
    }
    //endregion

    //region SpareDrawdownDetail
    public static void addSpareDrawdownDetail(SpareDrawdownDetailInfo info, boolean isSchedule) {
        new SpareDrawdownDetailController().addSpareDrawdownDetail(info);
        AddSpareDrawdownDetailInputInfo SpareDrawdownDetailInput = AddSpareDrawdownDetailInputInfo.from(info);
        //-- Fixed - [BHPROJ-0026-606] :: ยกเลิกขั้นตอนในการบันทึกใบร้องขอเพื่อเบิกเครื่อง/อะไหล่ (สำรอง) เนื่องจากระบบ TSR-Stock ใหม่ มีอยู่แล้ว
//        TSRService.addSpareDrawdownDetail(SpareDrawdownDetailInput, isSchedule);
    }

    public static void updateSpareDrawdownDetail(SpareDrawdownDetailInfo info, boolean isSchedule) {
        new SpareDrawdownDetailController().updateSpareDrawdownDetail(info);
        UpdateSpareDrawdownDetailInputInfo SpareDrawdownDetailInput = UpdateSpareDrawdownDetailInputInfo.from(info);
        //-- Fixed - [BHPROJ-0026-606] :: ยกเลิกขั้นตอนในการบันทึกใบร้องขอเพื่อเบิกเครื่อง/อะไหล่ (สำรอง) เนื่องจากระบบ TSR-Stock ใหม่ มีอยู่แล้ว
//        TSRService.updateSpareDrawdownDetail(SpareDrawdownDetailInput, isSchedule);
    }
    //endregion


    /**
     * ** SpareStockOnHand ***
     **/
    public static void addPartSpareStockOnHand(PartSpareStockOnHandInfo info, boolean isSchedule) {
        new PartSpareStockOnHandController().addPartSpareStockOnHand(info);
        AddPartSpareStockOnHandInputInfo input = AddPartSpareStockOnHandInputInfo.from(info);
        TSRService.addPartSpareStockOnHand(input, isSchedule);
    }

    public static void updatePartSpareStockOnHand(PartSpareStockOnHandInfo info, boolean isSchedule) {
        new PartSpareStockOnHandController().updatePartSpareStockOnHand(info);
        UpdatePartSpareStockOnHandInputInfo input = UpdatePartSpareStockOnHandInputInfo.from(info);
        TSRService.updatePartSpareStockOnHand(input, isSchedule);
    }


    /**
     * ** ChangePartSpare ***
     **/
    public static void addChangePartSpare(ChangePartSpareInfo info, boolean isSchedule) {
        new ChangePartSpareController().addChangePartSpare(info);
        AddChangePartSpareInputInfo input = AddChangePartSpareInputInfo.from(info);
        TSRService.addChangePartSpare(input, isSchedule);
    }

    public static void updateChangePartSpare(ChangePartSpareInfo info, boolean isSchedule) {
        new ChangePartSpareController().updateChangePartSpare(info);
        UpdateChangePartSpareInputInfo input = UpdateChangePartSpareInputInfo.from(info);
        TSRService.updateChangePartSpare(input, isSchedule);
    }


    /**
     * ** ChangePartSpareDetail ***
     **/
    public static void addChangePartSpareDetail(ChangePartSpareDetailInfo info, boolean isSchedule) {
        new ChangePartSpareDetailController().addChangePartSpareDetail(info);
        AddChangePartSpareDetailInputInfo input = AddChangePartSpareDetailInputInfo.from(info);
        TSRService.addChangePartSpareDetail(input, isSchedule);
    }

    public static void updateChangePartSpareDetail(ChangePartSpareDetailInfo info, boolean isSchedule) {
        new ChangePartSpareDetailController().updateChangePartSpareDetail(info);
        UpdateChangePartSpareDetailInputInfo input = UpdateChangePartSpareDetailInputInfo.from(info);
        TSRService.updateChangePartSpareDetail(input, isSchedule);
    }

    /***
     * [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้
     ***/
    public static void updateReferencePerson(DebtorCustomerInfo customer, boolean isSchedule) {
        new DebtorCustomerController().updateReferencePerson(customer);
        UpdateReferencePersonInputInfo updateeReferencePersonInput = UpdateReferencePersonInputInfo.from(customer);
        TSRService.updateReferencePerson(updateeReferencePersonInput, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/

    /***
     * [START] :: Fixed - [BHPROJ-0026-1037] :: [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา] กรณีเป็นสัญญา Migrate ให้มีปุ่ม เพื่อข้ามการ scan สินค้าได้
     ***/
    public static void addLogScanProductSerial(LogScanProductSerialInfo info, boolean isSchedule) {
        new LogScanProductSerialController().addLogScanProductSerial(info);
        AddLogScanProductSerialInputInfo wsInput = AddLogScanProductSerialInputInfo.from(info);
        TSRService.addLogScanProductSerial(wsInput, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-1037] :: [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา] กรณีเป็นสัญญา Migrate ให้มีปุ่ม เพื่อข้ามการ scan สินค้าได้ ***/

    /***
     * [START] :: Fixed-[BHPROJ-0016-1061] :: [Android-Logout-GCM] กรณีเป็นการ Logout จากการยิง GCM ให้ตรวจสอบก่อนว่า ณ เวลานั้นมีใคร Login ด้วย UserID ของเราด้วย DeviceID อื่นหรือเปล่า ถ้ามีให้ข้ามการทำ TRansactionLogService ของ Android เลย
     ***/
    public static void addTransactionLogSkip(TransactionLogSkipInfo info, boolean isSchedule) {
        new TransactionLogSkipController().addLogScanProductSerial(info);
        AddTransactionLogSkipInputInfo wsInput = AddTransactionLogSkipInputInfo.from(info);
        TSRService.addTransactionLogSkip(wsInput, isSchedule);
    }
    /*** [END] :: Fixed-[BHPROJ-0016-1061] :: [Android-Logout-GCM] กรณีเป็นการ Logout จากการยิง GCM ให้ตรวจสอบก่อนว่า ณ เวลานั้นมีใคร Login ด้วย UserID ของเราด้วย DeviceID อื่นหรือเปล่า ถ้ามีให้ข้ามการทำ TRansactionLogService ของ Android เลย ***/


    /*** [START] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***/
    public static void addUserDeviceLog(AddUserDeviceLogInputInfo info) {
        TSRService.addUserDeviceLog(info, false);
    }
    /*** [END] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***/

    public static GetCurrentFortnightOutputInfo getCurrentFortnight(GetCurrentFortnightInputInfo info) {
        return TSRService.getCurrentFortnight(info, false);
    }

    /*** [START] :: Fixed - [BHPROJ-0026-3265] [Backend+Frontend] เพิ่มเมนูสำหรับทำการ ย้ายเข้า/ย้ายออก ข้อมูล Assign การเก็บเงิน  ***/
    public static void updateAssignForPushOrPull(String OrganizationCode, String ContractList, String AssigneeEmpID, String AssignAction, boolean isSchedule) {
        UpdateAssignForPushOrPullInputInfo input = new UpdateAssignForPushOrPullInputInfo();
        input.OrganizationCode = OrganizationCode;
        input.ContractList = ContractList;
        input.AssigneeEmpID = AssigneeEmpID;
        input.AssignAction = AssignAction;
        TSRService.updateAssignForPushOrPull(input, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3265] [Backend+Frontend] เพิ่มเมนูสำหรับทำการ ย้ายเข้า/ย้ายออก ข้อมูล Assign การเก็บเงิน  ***/


    /*** [START] :: Fixed - [BHPROJ-0026-3269][Back-End] เพิ่ม web service ชื่อ CommitChangeOrder เก็บไว้เพื่อ run store procedure ที่มีการทำงานหลังจากที่มีการแก้ไขการจัดลำดับค่าเริ่มต้นเสร็จสิ้น ***/
    public static void commitChangeOrder(String AssigneeEmpID, String TaskType, boolean isSchedule) {
        CommitChangeOrderInputInfo input = new CommitChangeOrderInputInfo();
        input.AssigneeEmpID = AssigneeEmpID;
        input.TaskType = TaskType;
        TSRService.commitChangeOrder(input, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3269][Back-End] เพิ่ม web service ชื่อ CommitChangeOrder เก็บไว้เพื่อ run store procedure ที่มีการทำงานหลังจากที่มีการแก้ไขการจัดลำดับค่าเริ่มต้นเสร็จสิ้น ***/

    public static void voidReceipt(String RefNo, String ReceiptID, String UpdatedBy,  boolean isSchedule) {

        /*new ReceiptController().doVoidReceipt(ReceiptID);
        VoidReceiptInputInfo input = new VoidReceiptInputInfo();
        input.RefNo = RefNo;
        input.ReceiptID = ReceiptID;
        input.ContractNo = "";
        input.LastUpdateBy = UpdatedBy;
        TSRService.voidReceipt(input, isSchedule);*/


        processTransaction(new Runnable() {
            @Override
            public void run() {
                ReceiptInfo receiptInfo = new ReceiptController().getReceiptForDoVoidReceipt(ReceiptID, RefNo);
                if (receiptInfo != null
                        && receiptInfo.ReceiptID != null
                        && receiptInfo.RefNo != null
                        && receiptInfo.PaymentID != null) {

                    new ReceiptController().doVoidReceipt(receiptInfo.ReceiptID, receiptInfo.RefNo, receiptInfo.PaymentID, receiptInfo.PaymentAmount, receiptInfo.PaymentDiscount);

                    VoidReceiptInputInfo input = new VoidReceiptInputInfo();
                    input.RefNo = RefNo;
                    input.ReceiptID = ReceiptID;
                    input.ContractNo = "";
                    input.LastUpdateBy = UpdatedBy;
                    TSRService.voidReceipt(input, isSchedule);
                }
            }
        });

    }


    public static void voidContract(String RefNo, String ContractNo, String UpdatedBy, boolean isSchedule) {
        new ContractController().doVoidContract(RefNo, ContractNo);

        VoidContractInputInfo input = new VoidContractInputInfo();
        input.RefNo = RefNo;
        input.ContractNo = ContractNo;
        input.LastUpdateBy = UpdatedBy;
        TSRService.voidContract(input, isSchedule);

        // อ่านข้อมูล Receipt ทั้งหมด และทำการยกเลิกทีละรายการ
        List<ReceiptInfo> receiptInfoList = new ReceiptController().getReceiptByRefNo(RefNo);
        if(receiptInfoList != null && receiptInfoList.size() > 0) {
            for (ReceiptInfo r : receiptInfoList) {
                voidReceipt(r.RefNo, r.ReceiptID, UpdatedBy, isSchedule);
            }
        }
    }

    /*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
    public static PlatformVersionOutputInfo ValidateVersion(PlatformVersionInputInfo info) {
        return TSRService.ValidateVersion(info, false);
    }
    /*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/

    /*** [START] :: Fixed - [BHPROJ-1036-8579] - มีการล็อคอินชื่อเดียวเข้าพร้อมกันได้ 2 เครื่อง ***/
    public static CheckSoapOutputInfo checkSoap() {
        return TSRService.checkSoap(false);
    }
    /*** [END] :: Fixed - [BHPROJ-1036-8579] - มีการล็อคอินชื่อเดียวเข้าพร้อมกันได้ 2 เครื่อง  ***/


    /*** [START] :: ReCheckSqliteData ***/
    public static ContractInfo getContractByRefNo(String RefNo) {
        return new ContractController().getContractByRefNo(RefNo);
    }

    public static ReceiptInfo getReceiptByReceiptID (String ReceiptID) {
        return new ReceiptController().getReceiptByReceiptID(ReceiptID);
    }

    public static PaymentInfo getPaymentByPaymentID(String PaymentID) {
        return new PaymentController().getPaymentByPaymentID(PaymentID);
    }

    public static void addTransactionLogBackup(List<TransactionLogInfo> oldTransactionLogList, String deviceID, Date backupDate, String empID) {

        if (oldTransactionLogList != null && oldTransactionLogList.size() > 0) {
            for (TransactionLogInfo info : oldTransactionLogList) {
                if (info.ServiceName != null) {
                    if (!info.ServiceName.equals("AddTransactionLogSkip")
                            && !info.ServiceName.equals("AddTransactionLogBackup")) {
                        AddTransactionLogBackupInputInfo inputInfo = new AddTransactionLogBackupInputInfo().from(info,  deviceID, backupDate, empID);
                        TSRService.addTransactionLogBackup(inputInfo, true);
                    }
                }
            }
        }
    }
    /*** [END] :: ReCheckSqliteData ***/


}
