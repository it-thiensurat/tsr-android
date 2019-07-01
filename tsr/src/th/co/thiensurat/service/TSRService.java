package th.co.thiensurat.service;

import android.graphics.Bitmap;

import com.google.gson.Gson;

import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;

import java.io.File;

import th.co.bighead.utilities.BHBitmap;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHService;
import th.co.bighead.utilities.BHStorage;
import th.co.thiensurat.data.controller.ContractImageController;
import th.co.thiensurat.data.info.TransactionLogInfo;
import th.co.thiensurat.service.data.*;

public class TSRService extends BHService {
    private static final TSRService service = new TSRService();

    @Override
    protected String serviceUrl() {
        // TODO Auto-generated method stub
        return BHPreference.TSR_SERVICE_URL;
    }

    @Override
    protected Element headers() {
        Element h = new Element().createElement(namespace(), "AuthenticationHeader");
        Element accessKey = new Element().createElement(namespace(), "AccessKey");
        accessKey.addChild(Node.TEXT, BHPreference.TSR_SERVICE_ACCESS_KEY);
        h.addChild(Node.ELEMENT, accessKey);

        return h;
    }

    @Override
    protected Element userHeaders() {
        Element h = new Element().createElement(null, "UserHeader");

        String userDeviceId = BHPreference.userDeviceId();
        if (!userDeviceId.isEmpty()) {
            Element elementDeviceID = new Element().createElement(null, "DeviceID");
            elementDeviceID.addChild(Node.TEXT, userDeviceId );
            h.addChild(Node.ELEMENT, elementDeviceID);
        }

        String employeeID = BHPreference.employeeID();
        if (employeeID != null && !employeeID.isEmpty()) {
            Element elementEmployeeID = new Element().createElement(null, "EmployeeID");
            elementEmployeeID.addChild(Node.TEXT, employeeID);
            h.addChild(Node.ELEMENT, elementEmployeeID);
        }

        return h;
    }

    public static Object synchronizeTransaction(TransactionLogInfo info) throws ClassNotFoundException {
        Gson gson = new Gson();
        Class<?> inputClass = null;
        Class<?> outputClass = null;
        inputClass = Class.forName(info.ServiceInputType);
        outputClass = Class.forName(info.ServiceOutputType);

        Object data = gson.fromJson(info.ServiceInputData, inputClass);

        // Additional process;
        if (inputClass.isAssignableFrom(AddContractImageInputInfo.class) || inputClass.isAssignableFrom(UpdateContractImageInputInfo.class)) {
            data = getContractImage(data);
        }

        return service.call(info.ServiceName, info.ServiceInputName, data, outputClass, false, false);
    }

    //**************************************************************************************//
    //**************************** Start Additional Process ********************************//
    //**************************************************************************************//
    private static Object getContractImage(Object data) {
        if (data != null) {
            if (data.getClass().isAssignableFrom(AddContractImageInputInfo.class)) {
                AddContractImageInputInfo info = (AddContractImageInputInfo) data;
                info.ImageData = null;

                File imageFile = new File(String.format("%s/%s/%s/%s", BHStorage.getFolder(BHStorage.FolderType.Picture), info.RefNo, info.ImageTypeCode, info.ImageName));
                if (imageFile.isFile()) {
                    Bitmap bm = BHBitmap.decodeSampledBitmapFromImagePathForUploadToServer(imageFile.getAbsolutePath(), 800);
                    Bitmap bitmap = null;

                    if (bm != null) {
                        if (!info.ImageTypeCode.equals(ContractImageController.ImageType.MAP.toString()) && !info.ImageTypeCode.equals(ContractImageController.ImageType.MAPPAYMENT.toString())) {
                            bitmap = BHBitmap.setRotateImageFromImagePath(imageFile.getAbsolutePath(), bm);
                        } else {
                            bitmap = bm;
                        }
                        info.ImageData = BHBitmap.getBase64ToString(bitmap);
                    }
                }
            } else if (data.getClass().isAssignableFrom(UpdateContractImageInputInfo.class)) {
                UpdateContractImageInputInfo info = (UpdateContractImageInputInfo) data;
                info.ImageData = null;

                File imageFile = new File(String.format("%s/%s/%s/%s", BHStorage.getFolder(BHStorage.FolderType.Picture), info.RefNo, info.ImageTypeCode, info.ImageName));
                if (imageFile.isFile()) {
                    Bitmap bm = BHBitmap.decodeSampledBitmapFromImagePathForUploadToServer(imageFile.getAbsolutePath(), 800);
                    Bitmap bitmap = null;

                    if (bm != null) {
                        if (!info.ImageTypeCode.equals(ContractImageController.ImageType.MAP.toString()) && !info.ImageTypeCode.equals(ContractImageController.ImageType.MAPPAYMENT.toString())) {
                            bitmap = BHBitmap.setRotateImageFromImagePath(imageFile.getAbsolutePath(), bm);
                        } else {
                            bitmap = bm;
                        }
                        info.ImageData = BHBitmap.getBase64ToString(bitmap);
                    }
                }
            }
        }

        return data;
    }
    //**************************************************************************************//
    //***************************** End Additional Process *********************************//
    //**************************************************************************************//

    /**
     * ** Synchronize Employee (OLD): employeeImport() ***
     **/
    public static GetAllTeamMemberOutputInfo getAllTeamMember(GetAllTeamMemberInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB (OLD)
        return service.call("GetAllTeamMember", info, GetAllTeamMemberOutputInfo.class, isSchedule);
    }

    public static GetSubTeamByTeamCodeOutputInfo getSubTeamByTeamCode(GetSubTeamByTeamCodeInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB (OLD)
        return service.call("GetSubTeamByTeamCode", info, GetSubTeamByTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetTeamByIDOutputInfo getTeamByID(GetTeamByIDInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB (OLD)
        return service.call("GetTeamByID", info, GetTeamByIDOutputInfo.class, isSchedule);
    }

    /*** [START] :: Fixed - [BHPROJ-0016-769] ปรับปรุง Process การ Login ผ่านระบบ Android ***/
    public static AuthenticateOutputInfo authenticate(AuthenticateInputInfo info, boolean isSchedule) {
        return service.call("Authenticate", info, AuthenticateOutputInfo.class, isSchedule);
    }
    public static IsUserLoggedInOutputInfo isUserLoggedIn(IsUserLoggedInInputInfo info, boolean isSchedule) {
        return service.call("IsUserLoggedIn", info, IsUserLoggedInOutputInfo.class, isSchedule);
    }
    public static UpdateUserLoggedInOutputInfo updateUserLoggedIn(UpdateUserLoggedInInputInfo info, boolean isSchedule) {
        return service.call("UpdateUserLoggedIn", info, UpdateUserLoggedInOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0016-769] ปรับปรุง Process การ Login ผ่านระบบ Android ***/

    public static UserDeviceOutputInfo getUserDevice(UserInputInfo info, boolean isSchedule) {
        return service.call("GetUserDevice", info, UserDeviceOutputInfo.class, isSchedule);
    }

    public static AuthenticateOutputInfo Logout(AuthenticateInputInfo info, boolean isSchedule) {
        return service.call("Logout", info, AuthenticateOutputInfo.class, isSchedule);
    }

    public static GetUserByUserNameOutputInfo getUserByUserName(GetUserByUserNameInputInfo info, boolean isSchedule) {
        return service.call("GetUserByUserName", info, GetUserByUserNameOutputInfo.class, isSchedule);
    }

    public static GetNextSalePaymentPeriodByContractTeamOutputInfo getNextSalePaymentPeriodByContractTeam(GetNextSalePaymentPeriodByContractTeamInputInfo info,
                                                                                                          boolean isSchedule) {
        return service.call("GetNextSalePaymentPeriodByContractTeam", info, GetNextSalePaymentPeriodByContractTeamOutputInfo.class, isSchedule);
    }

    public static GetProductStockBySerialNoOutputInfo getProductStockBySerialNo(GetProductStockBySerialNoInputInfo info, boolean isSchedule) {
        return service.call("GetProductStockBySerialNo", info, GetProductStockBySerialNoOutputInfo.class, isSchedule);
    }

    public static GetProductStockByStatusOutputInfo getProductStockByStatus(GetProductStockByStatusInputInfo info, boolean isSchedule) {
        return service.call("GetProductStockByStatus", info, GetProductStockByStatusOutputInfo.class, isSchedule);
    }

    public static GetPackageByProductIDOutputInfo getPackageByProductID(GetPackageByProductIDInputInfo info, boolean isSchedule) {
        return service.call("GetPackageByProductID", info, GetPackageByProductIDOutputInfo.class, isSchedule);
    }

    public static GetPackagePeriodByModelOutputInfo getPackagePeriodByModel(GetPackagePeriodByModelInputInfo info, boolean isSchedule) {
        return service.call("GetPackagePeriodByModel", info, GetPackagePeriodByModelOutputInfo.class, isSchedule);
    }

    public static GetContractByRefNoOutputInfo getContractByRefNo(GetContractByRefNoInputInfo info, boolean isSchedule) {
        return service.call("GetContractByRefNo", info, GetContractByRefNoOutputInfo.class, isSchedule);
    }

    public static GetAddressByRefNoOutputInfo getAddressByRefNo(GetAddressByRefNoInputInfo info, boolean isSchedule) {
        return service.call("GetAddressByRefNo", info, GetAddressByRefNoOutputInfo.class, isSchedule);
    }

    public static DeleteAddressOutputInfo deleteAddressByRefNo(DeleteAddressInputInfo info, boolean isSchedule) {
        return service.call("DeleteAddressByRefNo", info, DeleteAddressOutputInfo.class, isSchedule);
    }

    public static AddContractWithSalePaymentPeriodOutputInfo getAddContractWithSalePaymentPeriod(AddContractWithSalePaymentPeriodInputInfo info,
                                                                                                 boolean isSchedule) {
        return service.call("AddContractWithSalePaymentPeriod", info, AddContractWithSalePaymentPeriodOutputInfo.class, isSchedule);
    }

    public static GetAddressByRefNoByAddressTypeCodeOutputInfo getAddressByRefNoByAddressTypeCode(GetAssignByAssigneeTeamCodeInputInfo info, boolean isSchedule) {
        return null;
    }

    public static GetNextSalePaymentPeriodByContractTeamGroupByAssigneeOutputInfo getNextSalePaymentPeriodByContractTeamGroupByAssignee(
            GetNextSalePaymentPeriodByContractTeamGroupByAssigneeInputInfo info, boolean isSchedule) {
        return service.call("GetNextSalePaymentPeriodByContractTeamGroupByAssignee", info,
                GetNextSalePaymentPeriodByContractTeamGroupByAssigneeOutputInfo.class, isSchedule);
    }

    public static RefreshProductStockListOutputInfo refreshProductStockList(RefreshProductStockListInputInfo info, boolean isSchedule) {
        return service.call("RefreshProductStockList", info, RefreshProductStockListOutputInfo.class, isSchedule);
    }

    public static GetNextSalePaymentPeriodByContractTeamGroupByAppointmentDateOutputInfo getNextSalePaymentPeriodByContractTeamGroupByAppointmentDate(
            GetNextSalePaymentPeriodByContractTeamGroupByAppointmentDateInputInfo info, boolean isSchedule) {
        return service.call("GetNextSalePaymentPeriodByContractTeamGroupByAppointmentDate", info,
                GetNextSalePaymentPeriodByContractTeamGroupByAppointmentDateOutputInfo.class, isSchedule);
    }

    public static GetNextSalePaymentPeriodByAssigneeEmpIDOutputInfo getNextSalePaymentPeriodByAssigneeEmpID(
            GetNextSalePaymentPeriodByAssigneeEmpIDInputInfo info, boolean isSchedule) {
        return service.call("GetNextSalePaymentPeriodByAssigneeEmpID", info, GetNextSalePaymentPeriodByAssigneeEmpIDOutputInfo.class, isSchedule);
    }

    public static GetNextSalePaymentPeriodByContractTeamByAppointmentDateOutputInfo getNextSalePaymentPeriodByContractTeamByAppointmentDate(
            GetNextSalePaymentPeriodByContractTeamByAppointmentDateInputInfo info, boolean isSchedule) {
        return service.call("GetNextSalePaymentPeriodByContractTeamByAppointmentDate", info,
                GetNextSalePaymentPeriodByContractTeamByAppointmentDateOutputInfo.class, isSchedule);
    }

    public static GetInCompleteContractBySaleTeamCodeOutputInfo getInCompleteContractBySaleTeamCode(GetInCompleteContractBySaleTeamCodeInputInfo info,
                                                                                                    boolean isSchedule) {
        return service.call("GetInCompleteContractBySaleTeamCode", info, GetInCompleteContractBySaleTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetCompletedContractBySaleTeamCodeOutputInfo getCompletedContractBySaleTeamCode(GetCompletedContractBySaleTeamCodeInputInfo info,
                                                                                                  boolean isSchedule) {
        return service.call("GetCompletedContractBySaleTeamCode", info, GetCompletedContractBySaleTeamCodeOutputInfo.class, isSchedule);
    }

    /*
    public static AddOrUpdateAssignSalePaymentPeriodOutputInfo addOrUpdateAssignSalePaymentPeriod(AddOrUpdateAssignSalePaymentPeriodInputInfo info,
                                                                                                  boolean isSchedule) {
        return service.call("AddOrUpdateAssignSalePaymentPeriod", info, AddOrUpdateAssignSalePaymentPeriodOutputInfo.class, isSchedule);
    }
    */

    public static GetScannedProductByScanTeamOutputInfo getScannedProductByScanTeam(GetScannedProductByScanTeamInputInfo info, boolean isSchedule) {
        return service.call("GetScannedProductByScanTeam", info, GetScannedProductByScanTeamOutputInfo.class, isSchedule);
    }

    public static GetAllPrefixOutputInfo getAllPrefix(GetAllPrefixInputInfo info, boolean isSchedule) {
        return service.call("GetAllPrefix", info, GetAllPrefixOutputInfo.class, isSchedule);
    }

    public static GetCustomerByIDCardOutputInfo getCustomerByIDCard(GetCustomerByIDCardInputInfo info, boolean isSchedule) {
        return service.call("GetCustomerByIDCard", info, GetCustomerByIDCardOutputInfo.class, isSchedule);
    }

    public static GetContractBySerialNoOutputInfo getContractBySerialNo(GetContractBySerialNoInputInfo info, boolean isSchedule) {
        return service.call("GetContractBySerialNo", info, GetContractBySerialNoOutputInfo.class, isSchedule);
    }

    public static GetAllTradeInBrandOutputInfo getAllTradeInBrand(GetAllTradeInBrandInputInfo info, boolean isSchedule) {
        return service.call("GetAllTradeInBrand", info, GetAllTradeInBrandOutputInfo.class, isSchedule);
    }

    public static GetContractHistoryByCustomerIDOutputInfo getContractHistoryByCustomerID(GetContractHistoryByCustomerIDInputInfo info, boolean isSchedule) {
        return service.call("GetContractHistoryByCustomerID", info, GetContractHistoryByCustomerIDOutputInfo.class, isSchedule);
    }

    public static GetContractHistoryByIDCardOutputInfo GetContractHistoryByIDCard(GetContractHistoryByIDCardInputInfo info, boolean isSchedule) {
        return service.call("GetContractHistoryByIDCard", info, GetContractHistoryByIDCardOutputInfo.class, isSchedule);
    }

    public static GetAllBankOutputInfo getAllBank(GetAllBankInputInfo info, boolean isSchedule) {
        return service.call("GetAllBank", info, GetAllBankOutputInfo.class, isSchedule);
    }

    public static GetAllHabitatTypeOutputInfo getAllHabitatType(GetAllHabitatTypeInputInfo info, boolean isSchedule) {
        return service.call("GetAllHabitatType", info, GetAllHabitatTypeOutputInfo.class, isSchedule);
    }

    public static GetAllCareerOutputInfo getAllCareer(GetAllCareerInputInfo info, boolean isSchedule) {
        return service.call("GetAllCareer", info, GetAllCareerOutputInfo.class, isSchedule);
    }

    public static GetAllHobbyOutputInfo getAllHobby(GetAllHobbyInputInfo info, boolean isSchedule) {
        return service.call("GetAllHobby ", info, GetAllHobbyOutputInfo.class, isSchedule);
    }

    public static GetAllSuggestionOutputInfo getAllSuggestion(GetAllSuggestionInputInfo info, boolean isSchedule) {
        return service.call("GetAllSuggestion ", info, GetAllSuggestionOutputInfo.class, isSchedule);
    }

    public static GetProductStockOfTeamOutputInfo getProductStockOfTeam(GetProductStockOfTeamInputInfo info, boolean isSchedule) {
        return service.call("GetProductStockOfTeam", info, GetProductStockOfTeamOutputInfo.class, isSchedule);
    }

    public static void importProductStockFromInterface(ImportProductStockFromInterfaceInputInfo info, boolean isSchedule) {
        service.call("ImportProductStockFromInterface", info, null, isSchedule);
    }

    /*
    public static AddRequestChangeProductOutputInfo addRequestChangeProduct(AddRequestChangeProductInputInfo info, boolean isSchedule) {
        return service.call("AddRequestChangeProduct", info, AddRequestChangeProductOutputInfo.class, isSchedule);
    }

    public static ApproveChangeProductOutputInfo approveChangeProduct(ApproveChangeProductInputInfo info, boolean isSchedule) {
        return service.call("ApproveChangeProduct", info, ApproveChangeProductOutputInfo.class, isSchedule);
    }

    public static ActionChangeProductOutputInfo actionChangeProduct(ActionChangeProductInputInfo info, boolean isSchedule) {
        return service.call("ActionChangeProduct", info, ActionChangeProductOutputInfo.class, isSchedule);
    }

    public static AddOrUpdateAssignActionChangeProductOutputInfo addOrUpdateAssignActionChangeProduct(AddOrUpdateAssignActionChangeProductInputInfo info,
                                                                                                      boolean isSchedule) {
        return service.call("AddOrUpdateAssignActionChangeProduct", info, AddOrUpdateAssignActionChangeProductOutputInfo.class, isSchedule);
    }
    */

    public static UpdateContractOutputInfo updateContract(UpdateContractInputInfo info, boolean isSchedule) {
        return service.call("UpdateContract", info, UpdateContractOutputInfo.class, isSchedule);
    }

    public static AddContractOutputInfo addContract(AddContractInputInfo info, boolean isSchedule) {
        return service.call("AddContract", info, AddContractOutputInfo.class, isSchedule);
    }

    /**
     *** WriteOffNPL ***
     **/
    public static AddRequestWriteOffNPLOutputInfo addRequestWriteOffNPL(AddRequestWriteOffNPLInputInfo info, boolean isSchedule) {
        return service.call("AddRequestWriteOffNPL", info, AddRequestWriteOffNPLOutputInfo.class, isSchedule);
    }

    public static ActionWriteOffNPLOutputInfo actionWriteOffNPL(ActionWriteOffNPLInputInfo info, boolean isSchedule) {
        return service.call("ActionWriteOffNPL", info, ActionWriteOffNPLOutputInfo.class, isSchedule);
    }

    public static ApproveWriteOffNPLOutputInfo approveWriteOffNPL(ApproveWriteOffNPLInputInfo info, boolean isSchedule) {
        return service.call("ApproveWriteOffNPL", info, ApproveWriteOffNPLOutputInfo.class, isSchedule);
    }

    public static AddOrUpdateAssignActionWriteOffNPLOutputInfo addOrUpdateAssignActionWriteOffNPL(AddOrUpdateAssignActionWriteOffNPLInputInfo info,
                                                                                                  boolean isSchedule) {
        return service.call("AddOrUpdateAssignActionWriteOffNPL", info, AddOrUpdateAssignActionWriteOffNPLOutputInfo.class, isSchedule);
    }

    public static GetAllPackageOutputInfo getAllPackage(GetAllPackageInputInfo info, boolean isSchedule) {
        return service.call("GetAllPackage", info, GetAllPackageOutputInfo.class, isSchedule);
    }

    public static GetAllPackagePeriodDetailOutputInfo getAllPackagePeriodDetail(GetAllPackagePeriodDetailInputInfo info, boolean isSchedule) {
        return service.call("GetAllPackagePeriodDetail", info, GetAllPackagePeriodDetailOutputInfo.class, isSchedule);
    }

    public static DeleteAssignByRefNoByTaskTypeByReferenceIDOutputInfo deleteAssignByRefNoByTaskTypeByReferenceID(
            DeleteAssignByRefNoByTaskTypeByReferenceIDInputInfo info, boolean isSchedule) {
        return service.call("DeleteAssignByRefNoByTaskTypeByReferenceID", info, DeleteAssignByRefNoByTaskTypeByReferenceIDOutputInfo.class, isSchedule);
    }

    public static AddRequestChangeContractOutputInfo addRequestChangeContract(AddRequestChangeContractInputInfo info, boolean isSchedule) {
        return service.call("AddRequestChangeContract", info, AddRequestChangeContractOutputInfo.class, isSchedule);
    }

    public static ApproveChangeContractOutputInfo ApproveChangeContract(ApproveChangeContractInputInfo info, boolean isSchedule) {
        return service.call("ApproveChangeContract", info, ApproveChangeContractOutputInfo.class, isSchedule);
    }

    public static AddOrUpdateAssignActionChangeContractOutputInfo AddOrUpdateAssignActionChangeContract(AddOrUpdateAssignActionChangeContractInputInfo info,
                                                                                                        boolean isSchedule) {
        return service.call("AddOrUpdateAssignActionChangeContract", info, AddOrUpdateAssignActionChangeContractOutputInfo.class, isSchedule);
    }

    public static ActionChangeContractOutputInfo ActionChangeContract(ActionChangeContractInputInfo info, boolean isSchedule) {
        return service.call("ActionChangeContract", info, ActionChangeContractOutputInfo.class, isSchedule);
    }


    /**
     * DocumentHistory *
     **/
//    public static GetAllDocumentHistoryByDocumentNumberListOutputInfo getAllDocumentHistoryByDocumentNumberList(GetAllDocumentHistoryByDocumentNumberListInputInfo info, boolean isSchedule) {
//        // For Synch (UL) to Local-DB
//        return service.call("GetAllDocumentHistoryByDocumentNumberList", info, GetAllDocumentHistoryByDocumentNumberListOutputInfo.class, isSchedule);
//    }
    public static GetAllActiveDocumentHistoryByTeamCodeOutputInfo getAllActiveDocumentHistoryByTeamCode(GetAllActiveDocumentHistoryByTeamCodeInputInfo info, boolean isSchedule) {
        // For Synch (UL) to Local-DB
        return service.call("GetAllActiveDocumentHistoryByTeamCode", info, GetAllActiveDocumentHistoryByTeamCodeOutputInfo.class, isSchedule);
    }

    public static AddDocumentHistoryOutputInfo addDocumentHistory(AddDocumentHistoryInputInfo info, boolean isSchedule) {
        return service.call("AddDocumentHistory", info, AddDocumentHistoryOutputInfo.class, isSchedule);
    }

    public static UpdateDocumentHistoryOutputInfo updateDocumentHistory(UpdateDocumentHistoryInputInfo info, boolean isSchedule) {
        return service.call("UpdateDocumentHistory", info, UpdateDocumentHistoryOutputInfo.class, isSchedule);
    }


    /**
     * SendMoney *
     **/
    public static AddSendMoneyOutputInfo addSendMoney(AddSendMoneyInputInfo info, boolean isSchedule) {
        return service.call("AddSendMoney", info, AddSendMoneyOutputInfo.class, isSchedule);
    }

    /*** [START] - Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน ***/
    public static DeleteSendMoneyByIDOutputInfo deleteSendMoneyByID(DeleteSendMoneyByIDInputInfo info, boolean isSchedule) {
        return service.call("DeleteSendMoneyByID", info, DeleteSendMoneyByIDOutputInfo.class, isSchedule);
    }
    /*** [END] - Fixed - [BHPROJ-0026-1033] :: เพิ่มการยกเลิกรายการส่งเงิน กรณียังไม่ได้ทำการรับเงิน ***/

    // public static SaveTransactionNoOutputInfo
    // saveTransactionNo(SaveTransactionNoInputInfo info, boolean isSchedule) {
    // return service.call("SaveTransactionNo", info,
    // SaveTransactionNoOutputInfo.class, isSchedule);
    // }
    public static UpdateSendMoneyOutputInfo updateSendMoney(UpdateSendMoneyInputInfo info, boolean isSchedule) {
        return service.call("UpdateSendMoney", info, UpdateSendMoneyOutputInfo.class, isSchedule);
    }

    // Fixed - [BHPROJ-0026-865] :: [Meeting@TSR@11/02/59] [Android-บันทึก Transaction No. หลังจากนำส่งเงินแล้ว] ให้เรียกใช้ WS method ชื่อ UpdateSendMoneyTransactionNo แทน
    public static UpdateSendMoneyTransactionNoOutputInfo updateSendMoneyTransactionNo(UpdateSendMoneyTransactionNoInputInfo info, boolean isSchedule) {
        return service.call("UpdateSendMoneyTransactionNo", info, UpdateSendMoneyTransactionNoOutputInfo.class, isSchedule);
    }

    public static UpdateProductStockScanOutputInfo updateProductStockScan(UpdateProductStockScanInputInfo info, boolean isSchedule) {
        return service.call("UpdateProductStockScan", info, UpdateProductStockScanOutputInfo.class, isSchedule);
    }

    public static DeleteContractOutputInfo deleteContract(DeleteContractInputInfo info, boolean isSchedule) {
        return service.call("DeleteContract", info, DeleteContractOutputInfo.class, isSchedule);
    }

    public static DeleteSalePaymentPeriodByRefNoOutputInfo deleteSalePaymentPeriodByRefNo(DeleteSalePaymentPeriodByRefNoInputInfo info, boolean isSchedule) {
        return service.call("DeleteSalePaymentPeriodByRefNo", info, DeleteSalePaymentPeriodByRefNoOutputInfo.class, isSchedule);
    }

    public static AddSalePaymentPeriodOutputInfo addSalePaymentPeriod(AddSalePaymentPeriodInputInfo info, boolean isSchedule) {
        return service.call("AddSalePaymentPeriod", info, AddSalePaymentPeriodOutputInfo.class, isSchedule);
    }

//    public static AddSalePaymentPeriodListOutputInfo addSalePaymentPeriodList(AddSalePaymentPeriodListInputInfo info, boolean isSchedule) {
//        return service.call("AddSalePaymentPeriodList", info, AddSalePaymentPeriodListOutputInfo.class, isSchedule);
//    }

    public static AddDebtorCustomerOutputInfo addDebtorCustomer(AddDebtorCustomerInputInfo info, boolean isSchedule) {
        return service.call("AddDebtorCustomer", info, AddDebtorCustomerOutputInfo.class, isSchedule);
    }

    public static UpdateDebtorCustomerOutputInfo updateDebtorCustomer(UpdateDebtorCustomerInputInfo info, boolean isSchedule) {
        return service.call("UpdateDebtorCustomer", info, UpdateDebtorCustomerOutputInfo.class, isSchedule);
    }

    // public static AddAppointmentOutputInfo
    // addAppointment(AddAppointmentInputInfo info, boolean isSchedule) {
    // return service.call("AddAppointment", info,
    // AddAppointmentOutputInfo.class, isSchedule);
    // }
    public static AddPaymentAppointmentOutputInfo addPaymentAppointment(AddPaymentAppointmentInputInfo info, boolean isSchedule) {
        return service.call("AddPaymentAppointment", info, AddPaymentAppointmentOutputInfo.class, isSchedule);
    }

    public static UpdateSalePaymentPeriodOutputInfo updateSalePaymentPeriod(UpdateSalePaymentPeriodInputInfo info, boolean isSchedule) {
        return service.call("UpdateSalePaymentPeriod", info, UpdateSalePaymentPeriodOutputInfo.class, isSchedule);
    }

    public static AddAddressOutputInfo addAddress(AddAddressInputInfo info, boolean isSchedule) {
        return service.call("AddAddress", info, AddAddressOutputInfo.class, isSchedule);
    }

    public static UpdateAddressOutputInfo updateAddress(UpdateAddressInputInfo info, boolean isSchedule) {
        return service.call("UpdateAddress", info, UpdateAddressOutputInfo.class, isSchedule);
    }

    public static UpdateProductStockStatusOutputInfo updateProductStockStatus(UpdateProductStockStatusInputInfo info, boolean isSchedule) {
        return service.call("UpdateProductStock", info, UpdateProductStockStatusOutputInfo.class, isSchedule);
    }

    public static UpdateProductStockOutputInfo updateProductStock(UpdateProductStockInputInfo info, boolean isSchedule) {
        return service.call("UpdateProductStock", info, UpdateProductStockOutputInfo.class, isSchedule);
    }

    public static DeleteDebtorCustomerOutputInfo deleteDebtorCustomer(DeleteDebtorCustomerInputInfo info, boolean isSchedule) {
        return service.call("DeleteDebtorCustomer", info, DeleteDebtorCustomerOutputInfo.class, isSchedule);
    }

    public static AddPaymentOutputInfo addPayment(AddPaymentInputInfo info, boolean isSchedule) {
        return service.call("AddPayment", info, AddPaymentOutputInfo.class, isSchedule);
    }

    public static AddReceiptOutputInfo addReceipt(AddReceiptInputInfo info, boolean isSchedule) {
        return service.call("AddReceipt", info, AddReceiptOutputInfo.class, isSchedule);
    }

    public static UpdateReceiptOutputInfo updateReceipt(UpdateReceiptInputInfo info, boolean isSchedule) {
        return service.call("UpdateReceipt", info, UpdateReceiptOutputInfo.class, isSchedule);
    }

    public static AddSalePaymentPeriodPaymentOutputInfo addSalePaymentPeriodPayment(AddSalePaymentPeriodPaymentInputInfo info, boolean isSchedule) {
        return service.call("AddSalePaymentPeriodPayment", info, AddSalePaymentPeriodPaymentOutputInfo.class, isSchedule);
    }

    /*public static GetContractImageOutputInfo getContractImage(GetContractImageInputInfo info, boolean isSchedule) {
        return service.call("GetContractImage", info, GetContractImageOutputInfo.class, isSchedule);
    }*/

    public static AddContractImageOutputInfo addContractImage(AddContractImageInputInfo info, boolean isSchedule) {
        return service.call("AddContractImage", info, AddContractImageOutputInfo.class, isSchedule);
    }

    public static UpdateContractImageOutputInfo updateContractImage(UpdateContractImageInputInfo info, boolean isSchedule) {
        return service.call("UpdateContractImage", info, UpdateContractImageOutputInfo.class, isSchedule);
    }

    public static CopyImageForChangeContractOutputInfo copyImageForChangeContract(CopyImageForChangeContractInputInfo info, boolean isSchedule) {
        return service.call("CopyImageForChangeContract", info, CopyImageForChangeContractOutputInfo.class, isSchedule);
    }

    public static ScanProductStockBySerialNoOutputInfo scanProductStockBySerialNo(ScanProductStockBySerialNoInputInfo info, boolean isSchedule) {
        return service.call("ScanProductStockBySerialNo", info, ScanProductStockBySerialNoOutputInfo.class, isSchedule);
    }

    public static GetAllContractOutputInfo getAllContract(GetAllContractInputInfo info, boolean isSchedule) {
        return service.call("GetAllContract", info, GetAllContractOutputInfo.class, isSchedule);
    }

    public static GetAllContractImageByContractListOutputInfo getAllContractImageByContractList(GetAllContractImageByContractListInputInfo info, boolean isSchedule) {
        return service.call("GetAllContractImageByContractList", info, GetAllContractImageByContractListOutputInfo.class, isSchedule);
    }

    public static GetContractImageOutputInfo GetContractImage(GetContractImageInputInfo info, boolean isSchedule) {
        return service.call("GetContractImagesForAndroid", info, GetContractImageOutputInfo.class, isSchedule);
    }

    public static GetAllDebtorByContractListOutputInfo getAllDebtorByContractList(GetAllDebtorByContractListInputInfo info, boolean isSchedule) {
        return service.call("GetAllDebtorByContractList", info, GetAllDebtorByContractListOutputInfo.class, isSchedule);
    }

    public static GetAllAddressByContractListOutputInfo getAllAddressByContractList(GetAllAddressByContractListInputInfo info, boolean isSchedule) {
        return service.call("GetAllAddressByContractList", info, GetAllAddressByContractListOutputInfo.class, isSchedule);
    }

    public static GetAllPaymentByContractListOutputInfo getAllPaymentByContractList(GetAllPaymentByContractListInputInfo info, boolean isSchedule) {
        return service.call("GetAllPaymentByContractList", info, GetAllPaymentByContractListOutputInfo.class, isSchedule);
    }

    public static GetAllPaymentAppointmentByContractListOutputInfo getAllPaymentAppointmentByContractList(GetAllPaymentAppointmentByContractListInputInfo info,
                                                                                                          boolean isSchedule) {
        return service.call("GetAllPaymentAppointmentByContractList", info, GetAllPaymentAppointmentByContractListOutputInfo.class, isSchedule);
    }

    public static GetAllReceiptByContractListOutputInfo getAllReceiptByContractList(GetAllReceiptByContractListInputInfo info, boolean isSchedule) {
        return service.call("GetAllReceiptByContractList", info, GetAllReceiptByContractListOutputInfo.class, isSchedule);
    }

    public static GetAllSalePaymentPeriodByContractListOutputInfo getAllSalePaymentPeriodByContractList(GetAllSalePaymentPeriodByContractListInputInfo info,
                                                                                                        boolean isSchedule) {
        return service.call("GetAllSalePaymentPeriodByContractList", info, GetAllSalePaymentPeriodByContractListOutputInfo.class, isSchedule);
    }

    public static GetAllSalePaymentPeriodPaymentByContractListOutputInfo getAllSalePaymentPeriodPaymentByContractList(
            GetAllSalePaymentPeriodPaymentByContractListInputInfo info, boolean isSchedule) {
        return service.call("GetAllSalePaymentPeriodPaymentByContractList", info, GetAllSalePaymentPeriodPaymentByContractListOutputInfo.class, isSchedule);
    }

    public static GetAllProductOutputInfo getAllProduct(GetAllProductInputInfo info, boolean isSchedule) {
        return service.call("GetAllProduct", info, GetAllProductOutputInfo.class, isSchedule);
    }

    public static DeleteContractImageOutputInfo deleteContractImage(DeleteContractImageInputInfo info, boolean isSchedule) {
        return service.call("DeleteContractImage", info, DeleteContractImageOutputInfo.class, isSchedule);
    }

    public static GetMoreProductStockFromProductStockDBOfTeamOutputInfo getMoreProductStockFromProductStockDBOfTeam(
            GetMoreProductStockFromProductStockDBOfTeamInputInfo input, boolean isSchedule) {
        return service.call("GetMoreProductStockFromProductStockDBOfTeam", input, GetMoreProductStockFromProductStockDBOfTeamOutputInfo.class, isSchedule);
    }

    public static GetAllProductStockByContractListOutputInfo GetAllProductStockByContractList(GetAllProductStockByContractListInputInfo info, boolean isSchedule) {
        return service.call("GetAllProductStockByContractList", info, GetAllProductStockByContractListOutputInfo.class, isSchedule);

    }

    public static GetContractForSearchOutputInfo getContractForSearch(GetContractForSearchInputInfo info, boolean isSchedule) {
        return service.call("GetContractForSearch", info, GetContractForSearchOutputInfo.class, isSchedule);
    }

    public static GetNextSalePaymentPeriodForSearchOutputInfo getNextSalePaymentPeriodForSearch(GetNextSalePaymentPeriodForSearchInputInfo info,
                                                                                                boolean isSchedule) {
        return service.call("GetNextSalePaymentPeriodForSearch", info, GetNextSalePaymentPeriodForSearchOutputInfo.class, isSchedule);
    }

    public static GetChangeProductByRequestTeamCodeOutputInfo getChangeProductByRequestTeamCode(GetChangeProductByRequestTeamCodeInputInfo info,
                                                                                                boolean isSchedule) {
        return service.call("GetChangeProductByRequestTeamCode", info, GetChangeProductByRequestTeamCodeOutputInfo.class, isSchedule);
    }

    /*
    public static GetProblemContractForSearchOutputInfo getNextPaymentForSearch(GetProblemContractForSearchInputInfo info, boolean isSchedule) {
        return service.call("GetProblemContractForSearch", info, GetProblemContractForSearchOutputInfo.class, isSchedule);
    }
    */
    /*
    public static GetProblemContractByIDOutputInfo getProblemContract(GetProblemContractByIDInputInfo info, boolean isSchedule) {
        return service.call("GetProblemContractByID", info, GetProblemContractByIDOutputInfo.class, isSchedule);
    }
    */

    public static GetChangeContractByRequestTeamCodeOutputInfo getChangeContractByRequestTeamCode(GetChangeContractByRequestTeamCodeInputInfo info,
                                                                                                  boolean isSchedule) {
        return service.call("GetChangeContractByRequestTeamCode", info, GetChangeContractByRequestTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetChangeProductListOutputInfo getChangeProductList(GetChangeProductListInputInfo info, boolean isSchedule) {
        return service.call("GetChangeProductList", info, GetChangeProductListOutputInfo.class, isSchedule);
    }

    public static GetChangeProductListOutputInfo getChangeProductListForCredit(GetChangeProductListInputInfo info, boolean isSchedule) {
        return service.call("GetChangeProductListForCredit", info, GetChangeProductListOutputInfo.class, isSchedule);
    }

    public static AddChangeProductOutputInfo addChangeProduct(AddChangeProductInputInfo info, boolean isSchedule) {
        return service.call("AddChangeProduct", info, AddChangeProductOutputInfo.class, isSchedule);
    }

    public static UpdateChangeProductOutputInfo updateChangeProduct(UpdateChangeProductInputInfo info, boolean isSchedule) {
        return service.call("UpdateChangeProduct", info, UpdateChangeProductOutputInfo.class, isSchedule);
    }

    public static UpdateContractOutputInfo UpdateContractStatusCode(UpdateContractInputInfo info, boolean isSchedule) {
        return service.call("UpdateContractStatus", info, UpdateContractOutputInfo.class, isSchedule);
    }

    public static GetSendMoneyByTeamCodeOutputInfo getSendMoneyByTeamCode(GetSendMoneyByTeamCodeInputInfo info, boolean isSchedule) {
        return service.call("GetSendMoneyByTeamCode", info, GetSendMoneyByTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetAddressTypeOutputInfo getAddressType(GetAddressTypeInputInfo info, boolean isSchedule) {
        return service.call("GetAddressType", info, GetAddressTypeOutputInfo.class, isSchedule);
    }

    public static GetBankOutputInfo getBank(GetBankInputInfo info, boolean isSchedule) {
        return service.call("GetBank", info, GetBankOutputInfo.class, isSchedule);
    }

    public static GetCareerOutputInfo getCareer(GetCareerInputInfo info, boolean isSchedule) {
        return service.call("GetAllCareer", info, GetCareerOutputInfo.class, isSchedule);
    }

    public static GetChannelOutputInfo getChannel(GetChannelInputInfo info, boolean isSchedule) {
        return service.call("GetChannel", info, GetChannelOutputInfo.class, isSchedule);
    }

    public static GetContractStatusOutputInfo getContractStatus(GetContractStatusInputInfo info, boolean isSchedule) {
        return service.call("GetContractStatus", info, GetContractStatusOutputInfo.class, isSchedule);
    }

    public static GetDistrictOutputInfo getDistrict(GetDistrictInputInfo info, boolean isSchedule) {
        return service.call("GetDistrict", info, GetDistrictOutputInfo.class, isSchedule);
    }

    public static GetFortnightOutputInfo getFortnight(GetFortnightInputInfo info, boolean isSchedule) {
        return service.call("GetFortnight", info, GetFortnightOutputInfo.class, isSchedule);    //  ไม่ได้ใช้งานแล้ว เนื่องจากไปใช้ FullSynch แบบสร้าง file ที่ฝั่ง Back-End
    }

    public static GetHabitatTypeOutputInfo getHabitatType(GetHabitatTypeInputInfo info, boolean isSchedule) {
        return service.call("GetHabitatType", info, GetHabitatTypeOutputInfo.class, isSchedule);
    }

    public static GetHobbyOutputInfo getHobby(GetHobbyInputInfo info, boolean isSchedule) {
        return service.call("GetHobby", info, GetHobbyOutputInfo.class, isSchedule);
    }

    public static GetImageTypeOutputInfo getImageType(GetImageTypeInputInfo info, boolean isSchedule) {
        return service.call("GetImageType", info, GetImageTypeOutputInfo.class, isSchedule);
    }

    public static GetOrganizationOutputInfo getOrganization(GetOrganizationInputInfo info, boolean isSchedule) {
        return service.call("GetOrganization", info, GetOrganizationOutputInfo.class, isSchedule);
    }

    public static GetPositionOutputInfo getPosition(GetPositionInputInfo info, boolean isSchedule) {
        return service.call("GetPosition", info, GetPositionOutputInfo.class, isSchedule);
    }

    public static GetPrefixOutputInfo getPrefix(GetPrefixInputInfo info, boolean isSchedule) {
        return service.call("GetPrefix", info, GetPrefixOutputInfo.class, isSchedule);
    }

    /*
    public static UpdateProblemContractOutputInfo updateProblemContract(UpdateProblemContractInputInfo info, boolean isSchedule) {
        return service.call("UpdateProblemContract", info, UpdateProblemContractOutputInfo.class, isSchedule);
    }
    */
    public static GetProblemOutputInfo getProblem(GetProblemInputInfo info, boolean isSchedule) {
        return service.call("GetProblem", info, GetProblemOutputInfo.class, isSchedule);
    }

    public static GetProvinceOutputInfo getProvince(GetProvinceInputInfo info, boolean isSchedule) {
        return service.call("GetProvince", info, GetProvinceOutputInfo.class, isSchedule);
    }

    public static GetRoleOutputInfo getRole(GetRoleInputInfo info, boolean isSchedule) {
        return service.call("GetRole", info, GetRoleOutputInfo.class, isSchedule);
    }

    public static GetSaleTypeOutputInfo getSaleType(GetSaleTypeInputInfo info, boolean isSchedule) {
        return service.call("GetSaleType", info, GetSaleTypeOutputInfo.class, isSchedule);
    }

    public static GetSubDistrictOutputInfo getSubDistrict(GetSubDistrictInputInfo info, boolean isSchedule) {
        return service.call("GetSubDistrict", info, GetSubDistrictOutputInfo.class, isSchedule);
    }

    public static GetSuggestionOutputInfo getSuggestion(GetSuggestionInputInfo info, boolean isSchedule) {
        return service.call("GetSuggestion", info, GetSuggestionOutputInfo.class, isSchedule);
    }

    public static GetTaskOutputInfo getTask(GetTaskInputInfo info, boolean isSchedule) {
        return service.call("GetTask", info, GetTaskOutputInfo.class, isSchedule);
    }
    /*
    public static GetProblemContractByTeamByStatusOutputInfo getProblemContractByTeamByStatus(GetProblemContractByTeamByStatusInputInfo info, boolean isSchedule) {
        return service.call("GetProblemContractByTeamByStatus", info, GetProblemContractByTeamByStatusOutputInfo.class, isSchedule);
    }
    */
    public static GetTradeInBrandOutputInfo getTradeInBrand(GetTradeInBrandInputInfo info, boolean isSchedule) {
        return service.call("GetTradeInBrand", info, GetTradeInBrandOutputInfo.class, isSchedule);
    }

    public static GetTripOutputInfo getTrip(GetTripInputInfo info, boolean isSchedule) {
        return service.call("GetTrip", info, GetTripOutputInfo.class, isSchedule);
    }

    public static GetChannelItemOutputInfo getChannelItem(GetChannelItemInputInfo info, boolean isSchedule) {
        return service.call("GetChannelItem", info, GetChannelItemOutputInfo.class, isSchedule);
    }

    public static GetDashboardReportOutputInfo reportGetDashboard(GetDashboardReportInputInfo info, boolean isSchedule) {
        return service.call("ReportGetDashboard", info, GetDashboardReportOutputInfo.class, isSchedule);
    }

    public static GetEmployeeDetailByEmployeeCodeOutputInfo getEmployeeDetailByEmployeeCode(GetEmployeeDetailByEmployeeCodeInputInfo info, boolean isSchedule) {
        return service.call("GetEmployeeDetailByEmployeeCode", info, GetEmployeeDetailByEmployeeCodeOutputInfo.class, isSchedule);
    }

    public static GetAllDiscountLimitOutputInfo getAllDiscountLimit(GetAllDiscountLimitInputInfo info, boolean isSchedule) {
        return service.call("GetAllDiscountLimit", info, GetAllDiscountLimitOutputInfo.class, isSchedule);
    }

    public static GetDeviceMenusOutputInfo getDeviceMenus(GetDeviceMenusInputInfo info) {
        return service.call("GetDeviceMenus", info, GetDeviceMenusOutputInfo.class);
    }

    /**
     * ** ManualDocument-ManualDocumentWithdrawal-ManualDocumentType ***
     **/
    public static GetManualDocumentTypeOutputInfo getManualDocumentType(GetManualDocumentTypeInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetManualDocumentType", info, GetManualDocumentTypeOutputInfo.class, isSchedule);
    }

    public static GetAllActiveManualDocumentWithdrawalByTeamOutputInfo getAllActiveManualDocumentWithdrawalByTeam(GetAllActiveManualDocumentWithdrawalByTeamInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllActiveManualDocumentWithdrawalByTeam", info, GetAllActiveManualDocumentWithdrawalByTeamOutputInfo.class, isSchedule);
    }

    public static GetAllManualDocumentByDocumentNumberListOutputInfo getAllManualDocumentByDocumentNumberList(GetAllManualDocumentByDocumentNumberListInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllManualDocumentByDocumentNumberList", info, GetAllManualDocumentByDocumentNumberListOutputInfo.class, isSchedule);
    }

    public static GetAllManualDocumentByTeamCodeOutputInfo getAllManualDocumentByTeamCode(GetAllManualDocumentByTeamCodeInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllManualDocumentByTeamCode", info, GetAllManualDocumentByTeamCodeOutputInfo.class, isSchedule);
    }

    public static AddManualDocumentOutputInfo addManualDocument(AddManualDocumentInputInfo info, boolean isSchedule) {
        return service.call("AddManualDocument", info, AddManualDocumentOutputInfo.class, isSchedule);
    }

    public static UpdateManualDocumentOutputInfo updateManualDocument(UpdateManualDocumentInputInfo info, boolean isSchedule) {
        return service.call("UpdateManualDocument", info, UpdateManualDocumentOutputInfo.class, isSchedule);
    }


    /**
     * ** DamageProduct ***
     **/
    public static AddDamageProductOutputInfo addDamageProduct(AddDamageProductInputInfo info, boolean isSchedule) {
        return service.call("AddDamageProduct", info, AddDamageProductOutputInfo.class, isSchedule);
    }


    /**
     * ** ReturnProduct-ReturnProductDetail ***
     **/
    public static GetAllRequestReturnProductByTeamOutputInfo getAllRequestReturnProductByTeam(GetAllRequestReturnProductByTeamInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllRequestReturnProductByTeam", info, GetAllRequestReturnProductByTeamOutputInfo.class, isSchedule);
    }

    public static GetAllRequestReturnProductDetailByTeamOutputInfo getAllRequestReturnProductDetailByTeam(GetAllRequestReturnProductDetailByTeamInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllRequestReturnProductDetailByTeam", info, GetAllRequestReturnProductDetailByTeamOutputInfo.class, isSchedule);
    }

    public static void addReturnProduct(AddReturnProductInputInfo info, boolean isSchedule) {
        service.call("AddReturnProduct", info, null, isSchedule);
    }

    public static void addReturnProductDetail(AddReturnProductDetailInputInfo info, boolean isSchedule) {
        service.call("AddReturnProductDetail", info, null, isSchedule);
    }

    public static void updateTradeInReturnFlag(UpdateTradeInReturnFlagInputInfo info, boolean isSchedule) {
        service.call("UpdateTradeInReturnFlag", info, null, isSchedule);
    }


    /**
     * ** SendDocument-SendDocumentDetail ***
     **/
    public static GetAllSendDocumentBySubTeamAndTeamOutputInfo getAllSendDocumentBySubTeamAndTeam(GetAllSendDocumentBySubTeamAndTeamInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllSendDocumentBySubTeamAndTeam", info, GetAllSendDocumentBySubTeamAndTeamOutputInfo.class, isSchedule);
    }

    public static GetAllSendDocumentDetailBySubTeamAndTeamOutputInfo getAllSendDocumentDetailBySubTeamAndTeam(GetAllSendDocumentDetailBySubTeamAndTeamInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllSendDocumentDetailBySubTeamAndTeam", info, GetAllSendDocumentDetailBySubTeamAndTeamOutputInfo.class, isSchedule);
    }

    public static AddSendDocumentOutputInfo addSendDocument(AddSendDocumentInputInfo info, boolean isSchedule) {
        return service.call("AddSendDocument", info, AddSendDocumentOutputInfo.class, isSchedule);
    }

    public static AddSendDocumentDetailOutputInfo addSendDocumentDetail(AddSendDocumentDetailInputInfo info, boolean isSchedule) {
        return service.call("AddSendDocumentDetail", info, AddSendDocumentDetailOutputInfo.class, isSchedule);
    }

    public static UpdateSendDocumentOutputInfo updateSendDocument(UpdateSendDocumentInputInfo info, boolean isSchedule) {
        return service.call("UpdateSendDocument", info, UpdateSendDocumentOutputInfo.class, isSchedule);
    }

    public static DeleteSendDocumentDetailByPrintHistoryIDOutputInfo deleteSendDocumentDetailByPrintHistoryID(DeleteSendDocumentDetailByPrintHistoryIDInputInfo info, boolean isSchedule) {
        return service.call("DeleteSendDocumentDetailByPrintHistoryID", info, DeleteSendDocumentDetailByPrintHistoryIDOutputInfo.class, isSchedule);
    }


    /**
     * ** Assign ***
     **/
    public static GetAllAssignByContractListOutputInfo getAllAssignByContractList(GetAllAssignByContractListInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB (Use in method importContract() + importContractFromServer())
        return service.call("GetAllAssignByContractList", info, GetAllAssignByContractListOutputInfo.class, isSchedule);
    }

    public static GetAssignByAssigneeTeamCodeOutputInfo getAssignByAssigneeTeamCode(GetAssignByAssigneeTeamCodeInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB (Use in method importRequest())
        return service.call("GetAssignByAssigneeTeamCode", info, GetAssignByAssigneeTeamCodeOutputInfo.class, isSchedule);
    }

    public static AddAssignOutputInfo addAssign(AddAssignInputInfo info, boolean isSchedule) {
        return service.call("AddAssign", info, AddAssignOutputInfo.class, isSchedule);
    }

    public static UpdateAssignOutputInfo updateAssign(UpdateAssignInputInfo info, boolean isSchedule) {
        return service.call("UpdateAssign", info, UpdateAssignOutputInfo.class, isSchedule);
    }

    public static UpdateAssignForSortOrderDefaultOutputInfo updateAssignForSortOrderDefault(UpdateAssignForSortOrderDefaultInputInfo info, boolean isSchedule) {
        return service.call("UpdateAssignForSortOrderDefault", info, UpdateAssignForSortOrderDefaultOutputInfo.class, isSchedule);
    }

    /*** [START] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/
    public static UpdateNewAssignForSortOrderDefaultOutputInfo updateNewAssignForSortOrderDefault(UpdateNewAssignForSortOrderDefaultInputInfo info, boolean isSchedule) {
        return service.call("UpdateNewAssignForSortOrderDefault11", info, UpdateNewAssignForSortOrderDefaultOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/

    public static DeleteAssignOutputInfo deleteAssign(DeleteAssignInputInfo info, boolean isSchedule) {
        return service.call("DeleteAssign", info, DeleteAssignOutputInfo.class, isSchedule);
    }

    /**
     * ** ImpoundProduct ***
     **/
    /*
    public static GetContractApprovedImpoundProductByAssigneeTeamCodeOutputInfo getContractApprovedImpoundProductByAssigneeTeamCode(
            GetContractApprovedImpoundProductByAssigneeTeamCodeInputInfo info, boolean isSchedule) {
        return service.call("GetContractApprovedImpoundProductByAssigneeTeamCode", info, GetContractApprovedImpoundProductByAssigneeTeamCodeOutputInfo.class, isSchedule);
    }
    */
    public static GetImpoundProductsByTeamCodeForSearchOutputInfo getImpoundProductOtherTeamForSearch(GetImpoundProductsByTeamCodeForSearchInputInfo info,
                                                                                                    boolean isSchedule) {
        return service.call("GetImpoundProductsByTeamCodeForSearch", info, GetImpoundProductsByTeamCodeForSearchOutputInfo.class, isSchedule);
    }

    public static GetImpoundProductOtherTeamForSearchOutputInfo getImpoundProductOtherTeamForSearch(GetImpoundProductOtherTeamForSearchInputInfo info,
                                                                                                    boolean isSchedule) {
        return service.call("GetImpoundProductOtherTeamForSearch", info, GetImpoundProductOtherTeamForSearchOutputInfo.class, isSchedule);
    }

    public static GetImpoundProductByRequestTeamCodeOutputInfo getImpoundProductByRequestTeamCode(GetImpoundProductByRequestTeamCodeInputInfo info,
                                                                                                  boolean isSchedule) {
        return service.call("GetImpoundProductByRequestTeamCode", info, GetImpoundProductByRequestTeamCodeOutputInfo.class, isSchedule);
    }

    public static AddRequestImpoundProductOutputInfo addRequestImpoundProduct(AddRequestImpoundProductInputInfo info, boolean isSchedule) {
        return service.call("AddRequestImpoundProduct", info, AddRequestImpoundProductOutputInfo.class, isSchedule);
    }

    public static ApproveImpoundProductOutputInfo approveImpoundProduct(ApproveImpoundProductInputInfo info, boolean isSchedule) {
        return service.call("ApproveImpoundProduct", info, ApproveImpoundProductOutputInfo.class, isSchedule);
    }

    public static ActionImpoundProductOutputInfo actionImpoundProduct(ActionImpoundProductInputInfo info, boolean isSchedule) {
        return service.call("ActionImpoundProduct", info, ActionImpoundProductOutputInfo.class, isSchedule);
    }

    public static AddOrUpdateAssignActionImpoundProductOutputInfo addOrUpdateAssignActionImpoundProduct(AddOrUpdateAssignActionImpoundProductInputInfo info,
                                                                                                        boolean isSchedule) {
        return service.call("AddOrUpdateAssignActionImpoundProduct", info, AddOrUpdateAssignActionImpoundProductOutputInfo.class, isSchedule);
    }
    /*
    public static UpdateImpoundProductOutputInfo updateImpoundProduct(UpdateImpoundProductInputInfo info, boolean isSchedule) {
        return service.call("UpdateImpoundProduct", info, UpdateImpoundProductOutputInfo.class, isSchedule);
    }
    */

    public static GetImpoundProductByImpoundProductIDOutputInfo getImpoundProductByImpoundProductID(GetImpoundProductByImpoundProductIDInputInfo info, boolean isSchedule) {
        return service.call("GetImpoundProductByImpoundProductID", info, GetImpoundProductByImpoundProductIDOutputInfo.class, isSchedule);
    }

    public static GetContractOfOtherTeamForAvailableImpoundOutputInfo GetContractOfOtherTeamForAvailableImpound(GetContractOfOtherTeamForAvailableImpoundInputInfo info, boolean isSchedule) {
        return service.call("GetContractOfOtherTeamForAvailableImpound", info, GetContractOfOtherTeamForAvailableImpoundOutputInfo.class, isSchedule);
    }


    /**
     * ** ProblemType ***
     **/
    public static GetAllProblemTypeOutputInfo getAllProblemType(GetAllProblemTypeInputInfo info, boolean isSchedule) {
        return service.call("GetAllProblemType", info, GetAllProblemTypeOutputInfo.class, isSchedule);
    }


    /**
     * ** Limit ***
     **/
    public static GetAllLimitOutputInfo getAllLimit(GetAllLimitInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllLimit", info, GetAllLimitOutputInfo.class, isSchedule);
    }


    /**
     * ** Sale Audit ***
     **/
    public static GetAllSaleAuditByContractListOutputInfo getAllSaleAuditByContractList(GetAllSaleAuditByContractListInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB (Use in method importContract() + importContractFromServer())
        return service.call("GetAllSaleAuditByContractList", info, GetAllSaleAuditByContractListOutputInfo.class, isSchedule);
    }

    public static GetAssignByAssigneeTeamCodeOutputInfo GetAssignSaleAuditByTaskTypeAndAssigneeTeamCode(GetAssignByAssigneeTeamCodeInputInfo info, boolean isSchedule) {
        return service.call("GetAssignSaleAuditByTaskTypeAndAssigneeTeamCode", info, GetAssignByAssigneeTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetSaleAuditByAssigneeTeamCodeOutputInfo GetSaleAuditByAssigneeTeamCode(GetAssignByAssigneeTeamCodeInputInfo info, boolean isSchedule) {
        return service.call("GetSaleAuditByAssigneeTeamCode", info, GetSaleAuditByAssigneeTeamCodeOutputInfo.class, isSchedule);
    }

    public static AddSaleAuditOutputInfo addSaleAudit(AddSaleAuditInputInfo info, boolean isSchedule) {
        return service.call("AddSaleAudit", info, AddSaleAuditOutputInfo.class, isSchedule);
    }

    public static UpdateSaleAuditOutputInfo updateSaleAudit(UpdateSaleAuditInputInfo info, boolean isSchedule) {
        return service.call("UpdateSaleAudit", info, UpdateSaleAuditOutputInfo.class, isSchedule);
    }


    /**
     * ** Area-AreaEmployee-SubArea ***
     **/
    public static GetAllAreaByTeamCodeOutputInfo getAllAreaByTeamCode(GetAllAreaByTeamCodeInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllAreaByTeamCode", info, GetAllAreaByTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetAllAreaEmployeeByTeamCodeOutputInfo getAllAreaEmployeeByTeamCode(GetAllAreaEmployeeByTeamCodeInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllAreaEmployeeByTeamCode", info, GetAllAreaEmployeeByTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetAllSubAreaByTeamCodeOutputInfo getAllSubAreaByTeamCode(GetAllSubAreaByTeamCodeInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllSubAreaByTeamCode", info, GetAllSubAreaByTeamCodeOutputInfo.class, isSchedule);
    }


    /**
     * ** Credit ***
     **/
    public static GetAssignByAssigneeTeamCodeOutputInfo GetAssignSalePaymentPeriod(GetAssignByAssigneeTeamCodeInputInfo info, boolean isSchedule) {
        return service.call("GetAssignSalePaymentPeriod", info, GetAssignByAssigneeTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetSalePaymentPeriodOutputInfo GetImportCredit(GetAssignByAssigneeTeamCodeInputInfo info, boolean isSchedule) {
        return service.call("GetImportCredit", info, GetSalePaymentPeriodOutputInfo.class, isSchedule);
    }

    /**
     * ** Contract-CloseAccount ***
     **/
    public static GetAllContractCloseAccountByContractListOutputInfo getAllContractCloseAccountByContractList(GetAllContractCloseAccountByContractListInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllContractCloseAccountByContractList", info, GetAllContractCloseAccountByContractListOutputInfo.class, isSchedule);
    }

    public static AddContractCloseAccountOutputInfo addContractCloseAccount(AddContractCloseAccountInputInfo info, boolean isSchedule) {
        return service.call("AddContractCloseAccount", info, AddContractCloseAccountOutputInfo.class, isSchedule);
    }


    /**
     * ** CutOff-Contract ***
     **/
    public static GetAllCutOffContractByContractListOutputInfo getAllCutOffContractByContractList(GetAllCutOffContractByContractListInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllCutOffContractByContractList", info, GetAllCutOffContractByContractListOutputInfo.class, isSchedule);
    }

    public static AddCutOffContractOutputInfo addCutOffContract(AddCutOffContractInputInfo info, boolean isSchedule) {
        return service.call("AddCutOffContract", info, AddCutOffContractOutputInfo.class, isSchedule);
    }

    public static UpdateCutOffContractOutputInfo updateCutOffContract(UpdateCutOffContractInputInfo info, boolean isSchedule) {
        return service.call("UpdateCutOffContract", info, UpdateCutOffContractOutputInfo.class, isSchedule);
    }

    /**
     * ** Cut-Divisor-Contract ***
     **/
    public static GetAllCutDivisorContractByContractListOutputInfo getAllCutDivisorContractByContractList(GetAllCutDivisorContractByContractListInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetAllCutDivisorContractByContractList", info, GetAllCutDivisorContractByContractListOutputInfo.class, isSchedule);
    }

    public static AddCutDivisorContractOutputInfo addCutDivisorContract(AddCutDivisorContractInputInfo info, boolean isSchedule) {
        return service.call("AddCutDivisorContract", info, AddCutDivisorContractOutputInfo.class, isSchedule);
    }

    public static GetCutDivisorContractOutputInfo getCutDivisorContractByCutDivisorContractIDOrStatusOrSearchText(GetCutDivisorContractInputInfo info, boolean isSchedule) {
        return service.call("GetCutDivisorContractByCutDivisorContractIDOrStatusOrSearchText", info, GetCutDivisorContractOutputInfo.class, isSchedule);
    }

    /**
     * **  Change-Contract ***
     **/
    public static GetContractOfTeamForAvailableChangeContractForSaleOutputInfo GetContractOfTeamForAvailableChangeContractForSale(GetContractOfTeamForAvailableChangeContractForSaleInputInfo info, boolean isSchedule) {
        return service.call("GetContractOfTeamForAvailableChangeContractForSale", info, GetContractOfTeamForAvailableChangeContractForSaleOutputInfo.class, isSchedule);
    }

    public static GetContractOfTeamAndSubTeamForAvailableChangeContractForSaleOutputInfo GetContractOfTeamAndSubTeamForAvailableChangeContractForSale(GetContractOfTeamAndSubTeamForAvailableChangeContractForSaleInputInfo info, boolean isSchedule) {
        return service.call("GetContractOfTeamAndSubTeamForAvailableChangeContractForSale", info, GetContractOfTeamAndSubTeamForAvailableChangeContractForSaleOutputInfo.class, isSchedule);
    }

    public static GetContractForAvailableChangeContractForCreditOutputInfo GetContractForAvailableChangeContractForCredit(GetContractForAvailableChangeContractForCreditInputInfo info, boolean isSchedule) {
        return service.call("GetContractForAvailableChangeContractForCredit", info, GetContractForAvailableChangeContractForCreditOutputInfo.class, isSchedule);
    }

    /**
     *** Complain ***
     **/
//    public static AddRequestComplainOutputInfo addRequestComplain(AddRequestComplainInputInfo info, boolean isSchedule) {
//        return service.call("AddRequestComplain", info, AddRequestComplainOutputInfo.class, isSchedule);
//    }
    public static AddComplainStatusREQUESTOutputInfo addComplainStatusREQUEST(AddComplainStatusREQUESTInputInfo info, boolean isSchedule) {
        return service.call("AddComplainStatusREQUEST", info, AddComplainStatusREQUESTOutputInfo.class, isSchedule);
    }

    public static UpdateComplainStatusAPPROVEDOutputInfo updateComplainStatusAPPROVED(UpdateComplainStatusAPPROVEDInputInfo info, boolean isSchedule) {
        return service.call("UpdateComplainStatusAPPROVED", info, UpdateComplainStatusAPPROVEDOutputInfo.class, isSchedule);
    }

    public static UpdateComplainStatusCOMPLETEDOutputInfo updateComplainStatusCOMPLETED(UpdateComplainStatusCOMPLETEDInputInfo info, boolean isSchedule) {
        return service.call("UpdateComplainStatusCOMPLETED", info, UpdateComplainStatusCOMPLETEDOutputInfo.class, isSchedule);
    }

    public static GetComplainByRequestTeamCodeOutputInfo getComplainByRequestTeamCode(GetComplainByRequestTeamCodeInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetComplainByRequestTeamCode", info, GetComplainByRequestTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetComplainOutputInfo getComplainByRequestTeamCodeAndForAction(GetComplainInputInfo info, boolean isSchedule) {
        return service.call("GetComplainByRequestTeamCodeAndForAction", info, GetComplainOutputInfo.class, isSchedule);
    }

    /**
     * ** Request-NextPayment ***
     **/
    public static GetRequestNextPaymentByRequestTeamCodeOutputInfo getRequestNextPaymentByRequestTeamCode(GetRequestNextPaymentByRequestTeamCodeInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB
        return service.call("GetRequestNextPaymentByRequestTeamCode", info, GetRequestNextPaymentByRequestTeamCodeOutputInfo.class, isSchedule);
    }

    public static AddRequestNextPaymentOutputInfo addRequestNextPayment(AddRequestNextPaymentInputInfo info, boolean isSchedule) {
        return service.call("AddRequestNextPayment", info, AddRequestNextPaymentOutputInfo.class, isSchedule);
    }

    public static UpdateRequestNextPaymentOutputInfo updateRequestNextPayment(UpdateRequestNextPaymentInputInfo info, boolean isSchedule) {
        return service.call("UpdateRequestNextPayment", info, UpdateRequestNextPaymentOutputInfo.class, isSchedule);
    }

    public static GetRequestNextPaymentOutputInfo getRequestNextPaymentByIDOrStatusOrSearchText(GetRequestNextPaymentInputInfo info, boolean isSchedule) {
        return service.call("GetRequestNextPaymentByIDOrStatusOrSearchText", info, GetRequestNextPaymentOutputInfo.class, isSchedule);
    }

    public static GetRequestNextPaymentForNewRequestOutputInfo getRequestNextPaymentForNewRequest(GetRequestNextPaymentForNewRequestInputInfo info, boolean isSchedule) {
        return service.call("GetRequestNextPaymentForNewRequest", info, GetRequestNextPaymentForNewRequestOutputInfo.class, isSchedule);
    }

    /**
     * ** Synchronize Employee (NEW): employeeImport() ***
     **/
    public static GetAllEmployeeOutputInfo getAllEmployee(GetAllEmployeeInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB (NEW)
        return service.call("GetAllEmployee", info, GetAllEmployeeOutputInfo.class, isSchedule);
    }

    public static GetAllSubTeamOutputInfo getAllSubTeam(GetAllSubTeamInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB (NEW)
        return service.call("GetAllSubTeam", info, GetAllSubTeamOutputInfo.class, isSchedule);
    }

    public static GetAllTeamOutputInfo getAllTeam(GetAllTeamInputInfo info, boolean isSchedule) {
        // For Synch from Server-DB to Local-DB (NEW)
        return service.call("GetAllTeam", info, GetAllTeamOutputInfo.class, isSchedule);
    }


    public static GetContractByReceiptIDOutputInfo getContractByReceiptID(GetContractByReceiptIDInputInfo info, boolean isSchedule) {
        return service.call("GetContractByReceiptID", info, GetContractByReceiptIDOutputInfo.class, isSchedule);
    }

    /* DebtorCustomer*/
    public static GetDebtorCustomerOutputInfo getDebtorCustomerByIDCard(GetDebtorCustomerInputInfo info, boolean isSchedule) {
        return service.call("GetDebtorCustomerByIDCard", info, GetDebtorCustomerOutputInfo.class, isSchedule);
    }

    /* Database .zip */
    public static SynchStatusOutputInfo synchDataFromServer2Local(SynchStatusInputInfo info, boolean isSchedule) {
        return service.call("SynchDataFromServer2Local", info, SynchStatusOutputInfo.class, isSchedule);
    }


    /*** [STATRT] Fixed - [BHPROJ-0026-606] :: ยกเลิกขั้นตอนในการบันทึกใบร้องขอเพื่อเบิกเครื่อง/อะไหล่ (สำรอง) เนื่องจากระบบ TSR-Stock ใหม่ มีอยู่แล้ว ***/
    /*
    // SpareDrawdown

    public static AddSpareDrawdownOutputInfo addSpareDrawdown(AddSpareDrawdownInputInfo info, boolean isSchedule) {
        return service.call("AddSpareDrawdown", info, AddSpareDrawdownOutputInfo.class, isSchedule);
    }
    public static UpdateSpareDrawdownOutputInfo updateSpareDrawdown(UpdateSpareDrawdownInputInfo info, boolean isSchedule) {
        return service.call("UpdateSpareDrawdown", info, UpdateSpareDrawdownOutputInfo.class, isSchedule);
    }
    public static GetSpareDrawdownByEmployeeIDOutputInfo GetSpareDrawdownByEmployeeID(GetSpareDrawdownByEmployeeIDInputInfo info, boolean isSchedule) {
        return service.call("GetSpareDrawdownByEmployeeID", info, GetSpareDrawdownByEmployeeIDOutputInfo.class, isSchedule);
    }

    // SpareDrawdownDetail
    public static AddSpareDrawdownDetailOutputInfo addSpareDrawdownDetail(AddSpareDrawdownDetailInputInfo info, boolean isSchedule) {
        return service.call("AddSpareDrawdownDetail", info, AddSpareDrawdownDetailOutputInfo.class, isSchedule);
    }
    public static UpdateSpareDrawdownDetailOutputInfo updateSpareDrawdownDetail(UpdateSpareDrawdownDetailInputInfo info, boolean isSchedule) {
        return service.call("UpdateSpareDrawdownDetail", info, UpdateSpareDrawdownDetailOutputInfo.class, isSchedule);
    }
    public static GetSpareDrawdownDetailBySpareDrawdownIDOutputInfo GetSpareDrawdownDetailBySpareDrawdownID(GetSpareDrawdownDetailBySpareDrawdownIDInputInfo info, boolean isSchedule) {
        return service.call("GetSpareDrawdownDetailBySpareDrawdownID", info, GetSpareDrawdownDetailBySpareDrawdownIDOutputInfo.class, isSchedule);
    }
    */
    /*** [END] Fixed - [BHPROJ-0026-606] :: ยกเลิกขั้นตอนในการบันทึกใบร้องขอเพื่อเบิกเครื่อง/อะไหล่ (สำรอง) เนื่องจากระบบ TSR-Stock ใหม่ มีอยู่แล้ว ***/


    /**
     * ** Fixed - [BHPROJ-0026-648] :: Prepare Web-Service Method for PartSpareStockOnHand ***
     **/
    public static GetPartSpareStockOnHandByTeamCodeOutputInfo getPartSpareStockOnHandByTeamCode(GetPartSpareStockOnHandByTeamCodeInputInfo info, boolean isSchedule) {
        return service.call("GetPartSpareStockOnHandByTeamCode", info, GetPartSpareStockOnHandByTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetPartSpareStockOnHandByEmployeeCodeOutputInfo getPartSpareStockOnHandByEmployeeCode(GetPartSpareStockOnHandByEmployeeCodeInputInfo info, boolean isSchedule) {
        return service.call("GetPartSpareStockOnHandByEmployeeCode", info, GetPartSpareStockOnHandByEmployeeCodeOutputInfo.class, isSchedule);
    }

    public static AddPartSpareStockOnHandOutputInfo addPartSpareStockOnHand(AddPartSpareStockOnHandInputInfo info, boolean isSchedule) {
        return service.call("AddPartSpareStockOnHand", info, AddPartSpareStockOnHandOutputInfo.class, isSchedule);
    }

    public static UpdatePartSpareStockOnHandOutputInfo updatePartSpareStockOnHand(UpdatePartSpareStockOnHandInputInfo info, boolean isSchedule) {
        return service.call("UpdatePartSpareStockOnHand", info, UpdatePartSpareStockOnHandOutputInfo.class, isSchedule);
    }


    /**
     * ** Fixed - [BHPROJ-0026-648] :: Prepare Web-Service Method for ChangePartSpare ***
     **/
    public static GetChangePartSpareByRequestTeamCodeOutputInfo getChangePartSpareByRequestTeamCode(GetChangePartSpareByRequestTeamCodeInputInfo info, boolean isSchedule) {
        return service.call("GetChangePartSpareByRequestTeamCode", info, GetChangePartSpareByRequestTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetChangePartSpareByRequestEmpIDOutputInfo getChangePartSpareByRequestEmpID(GetChangePartSpareByRequestEmpIDInputInfo info, boolean isSchedule) {
        return service.call("GetChangePartSpareByRequestEmpID", info, GetChangePartSpareByRequestEmpIDOutputInfo.class, isSchedule);
    }

    public static AddChangePartSpareOutputInfo addChangePartSpare(AddChangePartSpareInputInfo info, boolean isSchedule) {
        return service.call("AddChangePartSpare", info, AddChangePartSpareOutputInfo.class, isSchedule);
    }

    public static UpdateChangePartSpareOutputInfo updateChangePartSpare(UpdateChangePartSpareInputInfo info, boolean isSchedule) {
        return service.call("UpdateChangePartSpare", info, UpdateChangePartSpareOutputInfo.class, isSchedule);
    }


    /**
     * ** Fixed - [BHPROJ-0026-648] :: Prepare Web-Service Method for ChangePartSpareDetail ***
     **/
    public static GetChangePartSpareDetailByRequestTeamCodeOutputInfo gGetChangePartSpareDetailByRequestTeamCode(GetChangePartSpareDetailByRequestTeamCodeInputInfo info, boolean isSchedule) {
        return service.call("GetChangePartSpareDetailByRequestTeamCode", info, GetChangePartSpareDetailByRequestTeamCodeOutputInfo.class, isSchedule);
    }

    public static GetChangePartSpareDetailByChangePartSpareIDOutputInfo getChangePartSpareDetailByChangePartSpareID(GetChangePartSpareDetailByChangePartSpareIDInputInfo info, boolean isSchedule) {
        return service.call("GetChangePartSpareDetailByChangePartSpareID", info, GetChangePartSpareDetailByChangePartSpareIDOutputInfo.class, isSchedule);
    }

    public static AddChangePartSpareDetailOutputInfo addChangePartSpareDetail(AddChangePartSpareDetailInputInfo info, boolean isSchedule) {
        return service.call("AddChangePartSpareDetail", info, AddChangePartSpareDetailOutputInfo.class, isSchedule);
    }

    public static UpdateChangePartSpareDetailOutputInfo updateChangePartSpareDetail(UpdateChangePartSpareDetailInputInfo info, boolean isSchedule) {
        return service.call("UpdateChangePartSpareDetail", info, UpdateChangePartSpareDetailOutputInfo.class, isSchedule);
    }


    /*** [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/
    public static UpdateReferencePersonOutputInfo updateReferencePerson(UpdateReferencePersonInputInfo info, boolean isSchedule) {
        return service.call("UpdateReferencePerson", info, UpdateReferencePersonOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/


    /*** [START] :: Fixed - [BHPROJ-0026-977] :: [Meeting@TSR@07/03/2559] [Report-รายการสรุปประจำวันของเก็บเงิน] เพิ่มเติม Report Dashboard ของระบบงานเก็บเงิน (หน้าตาจะคล้าย ๆ DailySummary) ***/
    public static GetReportDailySummaryOnlineOutputInfo getReportDailySummaryOnline(GetReportDailySummaryOnlineInputInfo info, boolean isSchedule) {
        return service.call("GetReportDailySummaryOnline", info, GetReportDailySummaryOnlineOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-977] :: [Meeting@TSR@07/03/2559] [Report-รายการสรุปประจำวันของเก็บเงิน] เพิ่มเติม Report Dashboard ของระบบงานเก็บเงิน (หน้าตาจะคล้าย ๆ DailySummary) ***/

    /*** [START] :: Fixed - [BHPROJ-0018-3098] :: [Report] จัดทำรายงานการขายสินค้า break ตามชื่อสินค้า โดยดูเป็นของพนักงานขายแต่ละคน ***/
    public static GetReportDailySummarySaleByProductOnlineOutputInfo getReportDailySummarySaleByProductOnline(GetReportDailySummarySaleByProductOnlineInputInfo info, boolean isSchedule) {
        return service.call("GetReportDailySummarySaleByProductOnline", info, GetReportDailySummarySaleByProductOnlineOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0018-3098] :: [Report] จัดทำรายงานการขายสินค้า break ตามชื่อสินค้า โดยดูเป็นของพนักงานขายแต่ละคน ***/

    /*** [START] :: Fixed - [BHPROJ-0026-1037] :: [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา] กรณีเป็นสัญญา Migrate ให้มีปุ่ม เพื่อข้ามการ scan สินค้าได้ ***/
    public static AddLogScanProductSerialOutputInfo addLogScanProductSerial(AddLogScanProductSerialInputInfo info, boolean isSchedule) {
        return service.call("AddLogScanProductSerial", info, AddLogScanProductSerialOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-1037] :: [Android-เปลี่ยนเครื่อง/ถอดเครื่อง/เปลี่ยนสัญญา] กรณีเป็นสัญญา Migrate ให้มีปุ่ม เพื่อข้ามการ scan สินค้าได้ ***/

    /*** [START] :: Fixed-[BHPROJ-0016-1061] :: [Android-Logout-GCM] กรณีเป็นการ Logout จากการยิง GCM ให้ตรวจสอบก่อนว่า ณ เวลานั้นมีใคร Login ด้วย UserID ของเราด้วย DeviceID อื่นหรือเปล่า ถ้ามีให้ข้ามการทำ TRansactionLogService ของ Android เลย ***/
    public static AddTransactionLogSkipOutputInfo addTransactionLogSkip(AddTransactionLogSkipInputInfo info, boolean isSchedule) {
        return service.call("AddTransactionLogSkip", info, AddTransactionLogSkipOutputInfo.class, isSchedule, true);
    }
    /*** [END] :: Fixed-[BHPROJ-0016-1061] :: [Android-Logout-GCM] กรณีเป็นการ Logout จากการยิง GCM ให้ตรวจสอบก่อนว่า ณ เวลานั้นมีใคร Login ด้วย UserID ของเราด้วย DeviceID อื่นหรือเปล่า ถ้ามีให้ข้ามการทำ TRansactionLogService ของ Android เลย ***/

    /*** [START] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***/
    public static AddUserDeviceLogOutputInfo addUserDeviceLog(AddUserDeviceLogInputInfo info, boolean isSchedule) {
        return service.call("AddUserDeviceLog", info, AddUserDeviceLogOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0016-1059] :: [DB/Back-End/Front-End] เก็บ Log ของการ Login mobile ด้วย DeviceID ต่าง ๆ เป็น Historical ***/

    public static GetCurrentFortnightOutputInfo getCurrentFortnight(GetCurrentFortnightInputInfo info, boolean isSchedule) {
        return service.call("GetCurrentFortnight", info, GetCurrentFortnightOutputInfo.class, isSchedule);
    }

    public static UpdateGCMProdStkAndContractOutputInfo updateGCMProdStkAndContract(UpdateGCMProdStkAndContractInputInfo info, boolean isSchedule) {
        return service.call("UpdateGCMProdStkAndContract", info, UpdateGCMProdStkAndContractOutputInfo.class, isSchedule);
    }

    /*** [START] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/
    public static GetChangeContractByRequestTeamCodeByRequestEmpOutputInfo getChangeContractByRequestTeamCodeByRequestEmp(GetChangeContractByRequestTeamCodeByRequestEmpInputInfo info, boolean isSchedule) {
        return service.call("GetChangeContractByRequestTeamCodeByRequestEmp", info, GetChangeContractByRequestTeamCodeByRequestEmpOutputInfo.class, isSchedule);
    }
    public static GetChangeProductByRequestTeamCodeByRequestEmpOutputInfo getChangeProductByRequestTeamCodeByRequestEmp(GetChangeProductByRequestTeamCodeByRequestEmpInputInfo info, boolean isSchedule) {
        return service.call("GetChangeProductByRequestTeamCodeByRequestEmp", info, GetChangeProductByRequestTeamCodeByRequestEmpOutputInfo.class, isSchedule);
    }
    public static GetComplainByRequestTeamCodeAndForActionByEmpIDOutputInfo getComplainByRequestTeamCodeAndForActionByEmpID(GetComplainByRequestTeamCodeAndForActionByEmpIDInputInfo info, boolean isSchedule) {
        return service.call("GetComplainByRequestTeamCodeAndForActionByEmpID", info, GetComplainByRequestTeamCodeAndForActionByEmpIDOutputInfo.class, isSchedule);
    }
    public static GetImpoundProductByRequestTeamCodeByRequestEmpOutputInfo getImpoundProductByRequestTeamCodeByRequestEmp(GetImpoundProductByRequestTeamCodeByRequestEmpInputInfo info, boolean isSchedule) {
        return service.call("GetImpoundProductByRequestTeamCodeByRequestEmp", info, GetImpoundProductByRequestTeamCodeByRequestEmpOutputInfo.class, isSchedule);
    }
    public static GetRequestNextPaymentByRequestTeamCodeByRequestEmpOutputInfo getRequestNextPaymentByRequestTeamCodeByRequestEmp(GetRequestNextPaymentByRequestTeamCodeByRequestEmpInputInfo info, boolean isSchedule) {
        return service.call("GetRequestNextPaymentByRequestTeamCodeByRequestEmp", info, GetRequestNextPaymentByRequestTeamCodeByRequestEmpOutputInfo.class, isSchedule);
    }
    public static GetAssignByAssigneeTeamCodeByTaskTypeAndEmpIDOutputInfo getAssignByAssigneeTeamCodeByTaskTypeAndEmpID(GetAssignByAssigneeTeamCodeByTaskTypeAndEmpIDInputInfo info, boolean isSchedule) {
        return service.call("GetAssignByAssigneeTeamCodeByTaskTypeAndEmpID", info, GetAssignByAssigneeTeamCodeByTaskTypeAndEmpIDOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3253] :: [Android-RequestNextPayment] กดปุ่ม ปรับปรุง แล้วค้างที่ 100% ==> เพิ่ม Method + Web-Service เพื่อกรองข้อมูลให้น้อยลง ในตอน Synch from Server-DB to Local-DB ***/


    /*** [START] :: Fixed - [BHPROJ-0026-3265] [Backend+Frontend] เพิ่มเมนูสำหรับทำการ ย้ายเข้า/ย้ายออก ข้อมูล Assign การเก็บเงิน  ***/
    public static UpdateAssignForPushOrPullOutputInfo updateAssignForPushOrPull(UpdateAssignForPushOrPullInputInfo info, boolean isSchedule) {
        return service.call("UpdateAssignForPushOrPull", info, UpdateAssignForPushOrPullOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3265] [Backend+Frontend] เพิ่มเมนูสำหรับทำการ ย้ายเข้า/ย้ายออก ข้อมูล Assign การเก็บเงิน  ***/


    /*** [START] :: Fixed - [BHPROJ-0026-3269][Back-End] เพิ่ม web service ชื่อ CommitChangeOrder เก็บไว้เพื่อ run store procedure ที่มีการทำงานหลังจากที่มีการแก้ไขการจัดลำดับค่าเริ่มต้นเสร็จสิ้น ***/
    public static CommitChangeOrderOutputInfo commitChangeOrder(CommitChangeOrderInputInfo info, boolean isSchedule) {
        return service.call("CommitChangeOrder", info, CommitChangeOrderOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3269][Back-End] เพิ่ม web service ชื่อ CommitChangeOrder เก็บไว้เพื่อ run store procedure ที่มีการทำงานหลังจากที่มีการแก้ไขการจัดลำดับค่าเริ่มต้นเสร็จสิ้น ***/


    /*** [START] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/
    public static GetContractStatusFinishForCreditBySearchOutputInfo getContractStatusFinishForCreditBySearch(GetContractStatusFinishForCreditBySearchInputInfo info, boolean isSchedule) {
        return service.call("GetContractStatusFinishForCreditBySearch", info, GetContractStatusFinishForCreditBySearchOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3277][Android-รายละเอียดสัญญา] ในเมนูนี้ เหมือนว่าต้องการจะทำให้สามารถค้นหาสัญญาใด ๆ ก็ได้ แต่ปัจจุบันค้นหาได้เฉพาะสัญญาของตนเองเท่านั้น ***/


    public static VoidReceiptOutputInfo voidReceipt(VoidReceiptInputInfo info, boolean isSchedule) {
        return service.call("VoidReceipt", info, VoidReceiptOutputInfo.class, isSchedule);
    }

    public static VoidContractOutputInfo voidContract(VoidContractInputInfo info, boolean isSchedule) {
        return service.call("VoidContract", info, VoidContractOutputInfo.class, isSchedule);
    }

    // YIM Add here
    public static GetDepartmentSignatureImageOutputInfo GetDepartmentSignatureImage(GetDepartmentSignatureImageInputInfo info, boolean isSchedule) {
        return service.call("GetSignatureImages", info, GetDepartmentSignatureImageOutputInfo.class, isSchedule);
    }

    /*** [START] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย ***/
    public static ScanProductStockForCRDBySerialNoOutputInfo ScanProductStockForCRDBySerialNo(ScanProductStockForCRDBySerialNoInputInfo info, boolean isSchedule) {
        return service.call("ScanProductStockForCRDBySerialNo", info, ScanProductStockForCRDBySerialNoOutputInfo.class, isSchedule);
    }

    public static UpdateScanProductStockForCRDOutputInfo UpdateScanProductStockForCRD(UpdateScanProductStockForCRDInputInfo info, boolean isSchedule) {
        return service.call("UpdateScanProductStockForCRD", info, UpdateScanProductStockForCRDOutputInfo.class, isSchedule);
    }

    public static AddProductStockHistoryOutputInfo AddProductStockHistory(AddProductStockHistoryInputInfo info, boolean isSchedule) {
        return service.call("AddProductStockHistory", info, AddProductStockHistoryOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-1036-7663] - ในระบบขายของธุรกิจต่อเนื่อง เมื่อทำการขายครบทุกขั้นตอนแล้ว จะเห็นสัญญาของพนักงานคนอื่นในทีมขึ้นมาด้วย  ***/

    /*** [START] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/
    public static PlatformVersionOutputInfo ValidateVersion(PlatformVersionInputInfo info, boolean isSchedule) {
        return service.call("ValidateVersion", info, PlatformVersionOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-1036-8542] :: ปรับโครงสร้าง โฟรเดอร์รูปภาพของ App Bighead ***/

    /*** [START] :: Fixed - [BHPROJ-1036-8579] - มีการล็อคอินชื่อเดียวเข้าพร้อมกันได้ 2 เครื่อง ***/
    public static CheckSoapOutputInfo checkSoap(boolean isSchedule) {
        return service.call("CheckSoap", null, CheckSoapOutputInfo.class, isSchedule);
    }
    /*** [END] :: Fixed - [BHPROJ-1036-8579] - มีการล็อคอินชื่อเดียวเข้าพร้อมกันได้ 2 เครื่อง  ***/


    /*** [START] :: ReCheckSqliteData ***/
    public static AddTransactionLogBackupOutputInfo addTransactionLogBackup(AddTransactionLogBackupInputInfo info, boolean isSchedule) {

        return service.call("AddTransactionLogBackup", info, AddTransactionLogBackupOutputInfo.class, isSchedule);
    }
    /*** [END] :: ReCheckSqliteData ***/

}
