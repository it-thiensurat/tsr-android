package th.co.thiensurat.business.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHBluetoothPrinter.ZJMiniThemalPrint;
import th.co.bighead.utilities.BHPreference;
import th.co.thiensurat.activities.MainActivity;
import th.co.thiensurat.data.controller.ContractController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DocumentController;
import th.co.thiensurat.data.controller.DocumentHistoryController;
import th.co.thiensurat.data.controller.EmployeeController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.controller.preorder.DocumentController_preorder;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.ChangeContractInfo;
import th.co.thiensurat.data.info.ChangeProductInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.ImpoundProductInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.PrintTextInfo;
import th.co.thiensurat.data.info.ReturnProductDetailInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.SendDocumentInfo;
import th.co.thiensurat.data.info.SendMoneyInfo;

import static th.co.thiensurat.business.controller.TSRController.getAddress;
import static th.co.thiensurat.business.controller.TSRController.getChangeProductByID;
import static th.co.thiensurat.business.controller.TSRController.getContractByRefNo;

public class PrinterController {

    private Context mContext;
    private static MainActivity mainActivity;

    public PrinterController(MainActivity mainActivity) {
        PrinterController.mainActivity = mainActivity;
    }

    //public void printContract(final ContractInfo contract, AddressInfo defaultAddress, AddressInfo installAddress, ManualDocumentInfo manual) {
    public void printContract(final ContractInfo contract, AddressInfo defaultAddress, AddressInfo installAddress) {
        /*DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(contract.RefNo, DocumentHistoryController.DocumentType.Contract.toString());
        List<Bitmap> documents = new ArrayList<Bitmap>();
        int limit = checkExist != null ? 1 : 2;
        for (int x = 0; x < limit; x++) {
            Bitmap document = DocumentController.getContract(contract, defaultAddress, installAddress, manual);
            documents.add(document);
        }

        mainActivity.printImage(documents.toArray(new Bitmap[documents.size()]), new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {
                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(contract.RefNo,
                        DocumentHistoryController.DocumentType.Contract.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }


                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = new Date();
                docHist.DocumentType = DocumentHistoryController.DocumentType.Contract.toString();
                docHist.DocumentNumber = contract.RefNo;
                docHist.SyncedDate = new Date();
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = new Date();
                docHist.LastUpdateDate = null;
                docHist.LastUpdateBy = "";
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.PrintOrder = num;
                docHist.Status = "";
                docHist.SentDate = null;
                docHist.SentEmpID = "";
                docHist.SentSaleCode = "";
                docHist.SentSubTeamCode = "";
                docHist.SentTeamCode = "";
                docHist.ReceivedDate = null;
                docHist.ReceivedEmpID = "";


                TSRController.addDocumentHistory(docHist, true);


            }
        });*/

        DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(contract.RefNo, DocumentHistoryController.DocumentType.Contract.toString());
        List<List<PrintTextInfo>> documents = new ArrayList<>();
        int limit = checkExist != null ? 1 : 2;
        for (int x = 0; x < limit; x++) {
            /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
            //List<PrintTextInfo> document = DocumentController.getTextContract(contract, defaultAddress, installAddress, manual);
            List<PrintTextInfo> document = DocumentController.getTextContract(contract, defaultAddress, installAddress);
            /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
            documents.add(document);
        }

        mainActivity.printText(documents, new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {
                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(contract.RefNo,
                        DocumentHistoryController.DocumentType.Contract.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = new Date();
                docHist.DocumentType = DocumentHistoryController.DocumentType.Contract.toString();
                docHist.DocumentNumber = contract.RefNo;
                docHist.SyncedDate = new Date();
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = new Date();
                docHist.LastUpdateDate = null;
                docHist.LastUpdateBy = "";
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.PrintOrder = num;
                docHist.Status = "";
                docHist.SentDate = null;
                docHist.SentEmpID = "";
                docHist.SentSaleCode = "";
                docHist.SentSubTeamCode = "";
                docHist.SentTeamCode = "";
                docHist.ReceivedDate = null;
                docHist.ReceivedEmpID = "";

                TSRController.addDocumentHistory(docHist, true);
            }
        });
    }

    public void printSendMoney(final SendMoneyInfo sendMoney) {
        /*Bitmap document = DocumentController.getSendMoney(sendMoney);
//        DocumentController.saveImage(document);
        mainActivity.printImage(document, new MainActivity.PrintHandler() {
//            @Override
//            public void onBackgroundPrinting(int index) {
//                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
//                docHist.PrintHistoryID = DatabaseHelper.getUUID();
//                docHist.OrganizationCode = BHPreference.organizationCode();
//                docHist.DatePrint = new Date();
//                docHist.DocumentType = DocumentHistoryController.DocumentType.SendMoney.toString();
//                docHist.DocumentNumber = sendMoney.SendMoneyID;
//                TSRController.addDocumentHistory(docHist, true);
//            }
        });*/



        List<Bitmap> bitmapList = new ArrayList<>();
        List<List<PrintTextInfo>> document = new ArrayList<>();
        document.add(DocumentController.getTextSendMoney(sendMoney));

        /**
         *
         * Edit by Teerayut Klinsanga
         *
         * Date 2019-08-21 14:15
         *
         */

        Bitmap bmp = DocumentController.getNewSendMoneyImage(sendMoney);
        String barcode = DocumentController.barcodeString(sendMoney);
        bitmapList.add(bmp);
        mainActivity.printImageNew(bitmapList.toArray(new Bitmap[bitmapList.size()]), document, new MainActivity.PrintHandler() {
            @Override
            public void onPrintCompleted() {
                super.onPrintCompleted();
//                ZJMiniThemalPrint.setBarcodeString(barcode);
            }
        });

        /**
         * End
         */

//        mainActivity.printText(document, new MainActivity.PrintHandler() {
//        });
    }


    /*** [START] :: Fixed - [BHPROJ-0026-3275] :: [Android-พิมพ์ใบเสร็จ] การพิมพ์ใบเสร็จกรณีเก็บเงินพร้อมกันหลายงวด  ***/
    public void newPrintReceipt(List<PaymentInfo> payments){
        newPrintReceipt(payments, false);
    }

    public void newPrintReceipt_preorder(List<PaymentInfo> payments){
        newPrintReceipt_preorder(payments, false);
    }

    public void newPrintReceipt(List<PaymentInfo> payments,Boolean withInterrupt) {
        /*//List<Bitmap> documents = new ArrayList<Bitmap>();
        List<ShortReceiptInfo> shortReceiptInfoList= new ArrayList<>();
        List<PaymentInfo> payments1 = new ArrayList<>(payments);

        //region RePrint
        //List<Bitmap> documentsForReprint = new ArrayList<Bitmap>();
        List<ShortReceiptInfo> shortReceiptInfoListForReprint= new ArrayList<>();
        List<PaymentInfo> paymentsForReprint = new ArrayList<PaymentInfo>();
        //endregion

        for (PaymentInfo info : payments1) {
            DebtorCustomerInfo debtorCustomerInfo = TSRController.getDebCustometByID(info.CustomerID);
            AddressInfo addressInfo = TSRController.getAddress(info.RefNo, AddressInfo.AddressType.AddressInstall);
            //EmployeeInfo employeeInfo = TSRController.getEmployeeDetailByEmployeeIDAndPositionCode(BHPreference.organizationCode(), info.CreateBy, BHPreference.sourceSystem(), null);


           *//**ยอดเงินที่จ่ายมาตามใบเสร็จ**//*
            ShortReceiptInfo shortReceiptInfo = new ShortReceiptInfo(); //DocumentController.getShortReceipt(info, debtorCustomerInfo, addressInfo);

            *//*if (info.MODE == 1) {
                if (info.BalancesOfPeriod == 0) {   //จ่ายเต็ม
                    shortReceiptInfo = DocumentController.getTextReceipt(info, debtorCustomerInfo, addressInfo);
                } else {  //บางส่วน
                    shortReceiptInfo.receiptHeader = DocumentController.getNewReceipt(info, debtorCustomerInfo, addressInfo);
                }
            } else {
                if (info.CloseAccountPaymentPeriodNumber == info.PaymentPeriodNumber && info.BalancesOfPeriod == 0) {   //ตัดสด
                    shortReceiptInfo.receiptHeader = DocumentController.getNewReceipt(info, debtorCustomerInfo, addressInfo);
                } else{
                    if (info.BalancesOfPeriod == 0) {   //จ่ายเต็ม
                        shortReceiptInfo = DocumentController.getTextReceipt(info, debtorCustomerInfo, addressInfo);
                    } else {    //บางส่วน
                        shortReceiptInfo.receiptHeader = DocumentController.getNewReceipt(info, debtorCustomerInfo, addressInfo);
                    }
                }

            }*//*
            shortReceiptInfo = DocumentController.getTextReceipt(info, debtorCustomerInfo, addressInfo);
            shortReceiptInfoList.add(shortReceiptInfo);


            //region RePrint
            switch (Enum.valueOf(EmployeeController.SourceSystem.class, BHPreference.sourceSystem())) {
                case Sale:
                    DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(info.ReceiptID, DocumentHistoryController.DocumentType.Receipt.toString());
                    ShortReceiptInfo shortReceiptInfoForReprint = new ShortReceiptInfo();
                    if(checkExist == null){

                        *//*if (info.MODE == 1) {
                            if (info.BalancesOfPeriod == 0) {   //จ่ายเต็ม
                                shortReceiptInfoForReprint = DocumentController.getTextReceipt(info, debtorCustomerInfo, addressInfo);

                            } else {  //บางส่วน
                                shortReceiptInfoForReprint.receiptHeader = DocumentController.getNewReceipt(info, debtorCustomerInfo, addressInfo);
                            }
                        } else {
                            if (info.CloseAccountPaymentPeriodNumber == info.PaymentPeriodNumber && info.BalancesOfPeriod == 0) {   //ตัดสด
                                shortReceiptInfoForReprint.receiptHeader = DocumentController.getNewReceipt(info, debtorCustomerInfo, addressInfo);
                            } else{
                                if (info.BalancesOfPeriod == 0) {   //จ่ายเต็ม
                                    shortReceiptInfoForReprint = DocumentController.getTextReceipt(info, debtorCustomerInfo, addressInfo);

                                } else {    //บางส่วน
                                    shortReceiptInfoForReprint.receiptHeader = DocumentController.getNewReceipt(info, debtorCustomerInfo, addressInfo);
                                }
                            }

                        }*//*

                        shortReceiptInfoForReprint = DocumentController.getTextReceipt(info, debtorCustomerInfo, addressInfo);

                        shortReceiptInfoListForReprint.add(shortReceiptInfoForReprint);
                        paymentsForReprint.add(info);
                    }
                    break;
                default:
                    break;
            }
            //endregion

        }

        //region RePrint
        if(shortReceiptInfoListForReprint != null && shortReceiptInfoListForReprint.size() > 0) {
            shortReceiptInfoList.addAll(shortReceiptInfoListForReprint);
            payments1.addAll(paymentsForReprint);
        }
        //endregion

        final List<PaymentInfo> paymentsForPrint = new ArrayList<>(payments1);

        mainActivity.printImage(shortReceiptInfoList.toArray(new ShortReceiptInfo[shortReceiptInfoList.size()]), new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {

                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(paymentsForPrint.get(index).ReceiptID, DocumentHistoryController.DocumentType.Receipt.toString());


                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = new Date();
                docHist.DocumentType = DocumentHistoryController.DocumentType.Receipt.toString();
                docHist.DocumentNumber = paymentsForPrint.get(index).ReceiptID;
                docHist.SyncedDate = new Date();
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = new Date();
                docHist.LastUpdateDate = null;
                docHist.LastUpdateBy = "";
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.PrintOrder = num;
                docHist.Status = "";
                docHist.SentDate = null;
                docHist.SentEmpID = "";
                docHist.SentSaleCode = "";
                docHist.SentSubTeamCode = "";
                docHist.SentTeamCode = "";
                docHist.ReceivedDate = null;
                docHist.ReceivedEmpID = "";


                TSRController.addDocumentHistory(docHist, true);

            }
        });

        for (PaymentInfo info : paymentsForPrint)
        {

        }*/

        AddressInfo addressInfo = null;
        DebtorCustomerInfo debtorCustomerInfo = null;
        List<Bitmap> bitmapList = new ArrayList<>();
        List<List<PrintTextInfo>> document = new ArrayList<>();
        final List<PaymentInfo> paymentsForPrint = new ArrayList<>();

        for (PaymentInfo info : payments) {
            debtorCustomerInfo = TSRController.getDebCustometByID(info.CustomerID);
            addressInfo = TSRController.getAddress(info.RefNo, AddressInfo.AddressType.AddressInstall);

            document.add(DocumentController.getTextReceiptNew(info, debtorCustomerInfo, addressInfo));
            paymentsForPrint.add(info);

            bitmapList.add(DocumentController.getNewReceiptImage(info, debtorCustomerInfo, addressInfo));

            //region RePrint
            switch (Enum.valueOf(EmployeeController.SourceSystem.class, BHPreference.sourceSystem())) {
                case Sale:
//                    Bitmap bmp = DocumentController.getNewReceiptImage(info, debtorCustomerInfo, addressInfo);
                    DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(info.ReceiptID, DocumentHistoryController.DocumentType.Receipt.toString());
                    if(checkExist == null){
                        document.add(DocumentController.getTextReceiptNew(info, debtorCustomerInfo, addressInfo));
                        paymentsForPrint.add(info);
                        bitmapList.add(DocumentController.getNewReceiptImage(info, debtorCustomerInfo, addressInfo));
                    }
                    break;
                default:
                    break;
            }
            //endregion
        }
//        Log.e("PAYMENT FOR PRINT", String.valueOf(paymentsForPrint));

        MainActivity.PrintHandler handler = new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {

                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(paymentsForPrint.get(index).ReceiptID, DocumentHistoryController.DocumentType.Receipt.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = new Date();
                docHist.DocumentType = DocumentHistoryController.DocumentType.Receipt.toString();
                docHist.DocumentNumber = paymentsForPrint.get(index).ReceiptID;
                docHist.SyncedDate = new Date();
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = new Date();
                docHist.LastUpdateDate = null;
                docHist.LastUpdateBy = "";
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.PrintOrder = num;
                docHist.Status = "";
                docHist.SentDate = null;
                docHist.SentEmpID = "";
                docHist.SentSaleCode = "";
                docHist.SentSubTeamCode = "";
                docHist.SentTeamCode = "";
                docHist.ReceivedDate = null;
                docHist.ReceivedEmpID = "";

                TSRController.addDocumentHistory(docHist, true);

            }
        };
//        DocumentController.getTextReceipt(payments, debtorCustomerInfo, addressInfo);
        /**
         *
         * Edit by Teerayut Klinsanga
         *
         * Date 2019-08-21 10:35
         *
         */
        if(withInterrupt) {
            mainActivity.printTextWithInterrupt(document, handler);
        }else{
            mainActivity.printImageNew(bitmapList.toArray(new Bitmap[bitmapList.size()]), document, handler);
        }
        /**
         * End
         */

    }
    /*** [END] :: Fixed - [Android-พิมพ์ใบเสร็จ] การพิมพ์ใบเสร็จกรณีเก็บเงินพร้อมกันหลายงวด  ***/






    public void newPrintReceipt_preorder(List<PaymentInfo> payments,Boolean withInterrupt) {
          AddressInfo addressInfo = null;
        DebtorCustomerInfo debtorCustomerInfo = null;
        List<Bitmap> bitmapList = new ArrayList<>();
        List<List<PrintTextInfo>> document = new ArrayList<>();
        final List<PaymentInfo> paymentsForPrint = new ArrayList<>();

        for (PaymentInfo info : payments) {
            debtorCustomerInfo = TSRController.getDebCustometByID(info.CustomerID);
            addressInfo = TSRController.getAddress(info.RefNo, AddressInfo.AddressType.AddressInstall);

            document.add(DocumentController_preorder.getTextReceiptNew(info, debtorCustomerInfo, addressInfo));
            paymentsForPrint.add(info);

            bitmapList.add(DocumentController_preorder.getNewReceiptImage(info, debtorCustomerInfo, addressInfo));

            //region RePrint
            switch (Enum.valueOf(EmployeeController.SourceSystem.class, BHPreference.sourceSystem())) {
                case Sale:
//                    Bitmap bmp = DocumentController.getNewReceiptImage(info, debtorCustomerInfo, addressInfo);
                    DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(info.ReceiptID, DocumentHistoryController.DocumentType.Receipt.toString());
                    if(checkExist == null){
                        document.add(DocumentController_preorder.getTextReceiptNew(info, debtorCustomerInfo, addressInfo));
                        paymentsForPrint.add(info);
                        bitmapList.add(DocumentController_preorder.getNewReceiptImage(info, debtorCustomerInfo, addressInfo));
                    }
                    break;
                default:
                    break;
            }
            //endregion
        }
//        Log.e("PAYMENT FOR PRINT", String.valueOf(paymentsForPrint));

        MainActivity.PrintHandler handler = new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {

                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(paymentsForPrint.get(index).ReceiptID, DocumentHistoryController.DocumentType.Receipt.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = new Date();
                docHist.DocumentType = DocumentHistoryController.DocumentType.Receipt.toString();
                docHist.DocumentNumber = paymentsForPrint.get(index).ReceiptID;
                docHist.SyncedDate = new Date();
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = new Date();
                docHist.LastUpdateDate = null;
                docHist.LastUpdateBy = "";
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.PrintOrder = num;
                docHist.Status = "";
                docHist.SentDate = null;
                docHist.SentEmpID = "";
                docHist.SentSaleCode = "";
                docHist.SentSubTeamCode = "";
                docHist.SentTeamCode = "";
                docHist.ReceivedDate = null;
                docHist.ReceivedEmpID = "";

                TSRController.addDocumentHistory(docHist, true);

            }
        };
//        DocumentController.getTextReceipt(payments, debtorCustomerInfo, addressInfo);
        /**
         *
         * modify by assanee
         *
         * Date 2020-08-09 10:35
         *
         */
        if(withInterrupt) {
            mainActivity.printTextWithInterrupt(document, handler);
        }else{
            mainActivity.printImageNew(bitmapList.toArray(new Bitmap[bitmapList.size()]), document, handler);
        }
        /**
         * End
         */

    }






    public void newImagePrintReceipt(PaymentInfo info) {
        List<Bitmap> bitmap = new ArrayList<>();

        final List<PaymentInfo> paymentsForPrint = new ArrayList<>();


        DebtorCustomerInfo debtorCustomerInfo = TSRController.getDebCustometByID(info.CustomerID);
        AddressInfo addressInfo = TSRController.getAddress(info.RefNo, AddressInfo.AddressType.AddressInstall);

        bitmap.add(DocumentController.getImageReceiptNew(info, debtorCustomerInfo, addressInfo));
        paymentsForPrint.add(info);

        //region RePrint
        switch (Enum.valueOf(EmployeeController.SourceSystem.class, BHPreference.sourceSystem())) {
            case Sale:
                DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(info.ReceiptID, DocumentHistoryController.DocumentType.Receipt.toString());
                if(checkExist == null){
                    bitmap.add(DocumentController.getImageReceiptNew(info, debtorCustomerInfo, addressInfo));
                    paymentsForPrint.add(info);
                }
                break;
            default:
                break;
        }
        //endregion


        mainActivity.newPrintImage(bitmap.toArray(new Bitmap[bitmap.size()]), new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {
                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(paymentsForPrint.get(index).ReceiptID, DocumentHistoryController.DocumentType.Receipt.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = new Date();
                docHist.DocumentType = DocumentHistoryController.DocumentType.Receipt.toString();
                docHist.DocumentNumber = paymentsForPrint.get(index).ReceiptID;
                docHist.SyncedDate = new Date();
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = new Date();
                docHist.LastUpdateDate = null;
                docHist.LastUpdateBy = "";
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.PrintOrder = num;
                docHist.Status = "";
                docHist.SentDate = null;
                docHist.SentEmpID = "";
                docHist.SentSaleCode = "";
                docHist.SentSubTeamCode = "";
                docHist.SentTeamCode = "";
                docHist.ReceivedDate = null;
                docHist.ReceivedEmpID = "";

                TSRController.addDocumentHistory(docHist, true);

            }
        });

    }


    public void printReturnProduct(final List<ReturnProductDetailInfo> returnList) {
        /*Bitmap document = DocumentController.getReturnProduct(returnList, 0);
        mainActivity.printImage(document, new MainActivity.PrintHandler() {
//              @Override
//              public void onBackgroundPrinting(int index) {
//                  DocumentHistoryInfo docHist = new DocumentHistoryInfo();
//                  DocumentHistoryInfo Hist;
//
//                  Hist = TSRController.getDocumentHistoryByDocumentNumber(returnProduct.RefNo, DocumentHistoryController.DocumentType.Contract.toString());
//
//              	docHist.PrintHistoryID = DatabaseHelper.getUUID();
//              	docHist.OrganizationCode = BHPreference.organizationCode();
//              	docHist.DatePrint = new Date();
//              	docHist.DocumentType = DocumentHistoryController.DocumentType.Contract.toString();
//              	docHist.DocumentNumber = contract.RefNo;
//              	docHist.SyncedDate = new Date();
//              	docHist.CreateBy = BHPreference.employeeID();
//              	docHist.CreateDate = new Date();
//              	docHist.LastUpdateDate = null;
//              	docHist.LastUpdateBy = "";
//              	docHist.Selected = false;
//              	docHist.Deleted = false;
//              	docHist.PrintOrder = 1;
//              	docHist.Status = "";
//  				docHist.SentDate = null;
//  				docHist.SentEmpID = "";
//  				docHist.SentSaleCode = "";
//  				docHist.SentSubTeamCode = "";
//  				docHist.SentTeamCode = "";
//  				docHist.ReceivedDate = null;
//  				docHist.ReceivedEmpID ="";
//
//  				if (Hist != null) {
//
//  					docHist.PrintOrder = Hist.PrintOrder + 1;
//  					TSRController.addDocumentHistory(docHist, true);
//
//  				} else {
//
//  					TSRController.addDocumentHistory(docHist, true);
//
//  				}
//
//
//
//              }
        });*/


        List<List<PrintTextInfo>> document = new ArrayList<>();
        document.add(DocumentController.getTextReturnProduct(returnList));
        mainActivity.printText(document, new MainActivity.PrintHandler() {
        });
    }

    public void printSendDocument(final SendDocumentInfo sendDocument) {
        Bitmap document = DocumentController.getSendDocument(sendDocument);
//        DocumentController.saveImage(document);
        mainActivity.printImage(document, new MainActivity.PrintHandler() {
//            @Override
//            public void onBackgroundPrinting(int index) {
//                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
//                docHist.PrintHistoryID = DatabaseHelper.getUUID();
//                docHist.OrganizationCode = BHPreference.organizationCode();
//                docHist.DatePrint = new Date();
//                docHist.DocumentType = DocumentHistoryController.DocumentType.SendMoney.toString();
//                docHist.DocumentNumber = sendMoney.SendMoneyID;
//                TSRController.addDocumentHistory(docHist, true);
//            }
        });
    }

//    public void printImpoundProduct(final ImpoundProductInfo impoundProduct) {
//        Bitmap document = DocumentController.getSendDocument(impoundProduct);
//        DocumentController.saveImage(document);
//        mainActivity.printImage(document, new MainActivity.PrintHandler() {
//            @Override
//            public void onBackgroundPrinting(int index) {
//                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
//                docHist.PrintHistoryID = DatabaseHelper.getUUID();
//                docHist.OrganizationCode = BHPreference.organizationCode();
//                docHist.DatePrint = new Date();
//                docHist.DocumentType = DocumentHistoryController.DocumentType.SendMoney.toString();
//                docHist.DocumentNumber = sendMoney.SendMoneyID;
//                TSRController.addDocumentHistory(docHist, true);
//            }
//        });
//    }

//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    private void print(final Bitmap... bitmap) {
//        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
//            return;
//        }
//
//        PrintDocumentAdapter adapter = new PrintDocumentAdapter() {
//            private int pageWidth;
//
//            private PdfDocument pdfDocument;
//            private boolean validPrinter;
//
//            private Bitmap[] images;
//            private int pageCount;
//
//            @Override
//            public void onStart() {
//                super.onStart();
//                images = bitmap;
//                pageCount = images.length;
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                for (Bitmap img : images) {
//                    img.recycle();
//                }
////                if (print != null) {
////                    print.recycle();
////                }
//            }
//
//            @Override
//            public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
//                Log.i("PRINTER", "LAYOUT");
//                pdfDocument = new PrintedPdfDocument(mContext, newAttributes);
//
////                if (cancellationSignal.isCanceled()) {
////                    callback.onLayoutCancelled();
////                    return;
////                }
//
//                String id = newAttributes.getMediaSize().getId();
//                List<String> allowIDs = Arrays.asList("2inches", "3inches");
//
//                if (!allowIDs.contains(id)) {
////                    validPrinter = false;
////                    callback.onLayoutFinished(new PrintDocumentInfo.Builder("Test").build(), false);
////                    validPrinter = true;
//                }
//
//                validPrinter = true;
//
////                pageHeight =
////                        newAttributes.getMediaSize().getHeightMils()/1000 * 72;
//                pageWidth = newAttributes.getMediaSize().getWidthMils() / 1000 * 72;
//
//                if (cancellationSignal.isCanceled()) {
//                    callback.onLayoutCancelled();
//                    return;
//                }
//
//                if (pageCount > 0) {
//                    PrintDocumentInfo.Builder builder = new PrintDocumentInfo
//                            .Builder("print_document.pdf")
//                            .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
//                            .setPageCount(pageCount);
//
//                    PrintDocumentInfo info = builder.build();
//                    callback.onLayoutFinished(info, true);
//                } else {
//                    callback.onLayoutFailed("Page count is zero.");
//                }
//            }
//
//            @Override
//            public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
////                if (!validPrinter) {
////                    callback.onWriteFailed("Invalid support printer!");
////                    return;
////                }
//                Log.i("PRINTER", "WRITE");
//
//                for (int ii = 0; ii < pageCount; ii++) {
//                    Bitmap image = images[ii];
//                    float scale = (float) pageWidth / image.getWidth();
//                    int pageHeight = (int) (scale * image.getHeight());
//                    PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, ii).create();
//
//                    PdfDocument.Page page = pdfDocument.startPage(newPage);
//
//                    if (cancellationSignal.isCanceled()) {
//                        callback.onWriteCancelled();
//                        pdfDocument.close();
//                        pdfDocument = null;
//                        return;
//                    }
//                    Matrix matrix = new Matrix();
//                    Canvas canvas = page.getCanvas();
//                    matrix.postScale(scale, scale);
//                    canvas.drawBitmap(image, matrix, null);
//
//                    pdfDocument.finishPage(page);
//                }
//
//                try {
//                    pdfDocument.writeTo(new FileOutputStream(destination.getFileDescriptor()));
//                } catch (IOException e) {
//                    callback.onWriteFailed(e.toString());
//                    return;
//                } finally {
//                    pdfDocument.close();
//                    pdfDocument = null;
//                }
//
//                callback.onWriteFinished(pageRanges);
//            }
//        };
//
//        PrintManager pm = (PrintManager) mContext.getSystemService(Context.PRINT_SERVICE);
//        pm.print("TSR", adapter, null);
//    }

    public static void printDocumentHistory() {
        /*List<DocumentHistoryInfo> outputContract = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.Contract.toString());
        List<DocumentHistoryInfo> outputReceipt = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.Receipt.toString());
        List<DocumentHistoryInfo> outputChangeProduct = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ChangeProduct.toString());
        List<DocumentHistoryInfo> outputImpoundProduct = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ImpoundProduct.toString());
        List<DocumentHistoryInfo> outputChangeContract = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ChangeContract.toString());
        List<DocumentHistoryInfo> outputManualDocument = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ManualDocument.toString());
        List<DocumentHistoryInfo> outputPayInSlipBank = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.PayInSlipBank.toString());
        List<DocumentHistoryInfo> outputPayInSlipPayPoint = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.PayInSlipPayPoint.toString());

        Bitmap document = DocumentController.getDocumentHistory(outputContract,
                outputReceipt,
                outputChangeProduct,
                outputImpoundProduct,
                outputChangeContract,
                outputManualDocument,
                outputPayInSlipBank,
                outputPayInSlipPayPoint,
                0);
        mainActivity.printImage(document, new MainActivity.PrintHandler() {
        });*/

        List<List<PrintTextInfo>> document = new ArrayList<>();

        /*** [START] :: Fixed - [BHPROJ-0024-3234] :: การส่งเอกสารไม่ตรง  ***/

         /*List<DocumentHistoryInfo> outputContract = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.Contract.toString());
        List<DocumentHistoryInfo> outputReceipt = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.Receipt.toString());
        List<DocumentHistoryInfo> outputChangeProduct = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ChangeProduct.toString());
        List<DocumentHistoryInfo> outputImpoundProduct = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ImpoundProduct.toString());
        List<DocumentHistoryInfo> outputChangeContract = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ChangeContract.toString());
        List<DocumentHistoryInfo> outputManualDocument = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ManualDocument.toString());
        List<DocumentHistoryInfo> outputPayInSlipBank = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.PayInSlipBank.toString());
        List<DocumentHistoryInfo> outputPayInSlipPayPoint = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.PayInSlipPayPoint.toString());*/

        List<DocumentHistoryInfo> outputContract;
        List<DocumentHistoryInfo> outputReceipt;
        List<DocumentHistoryInfo> outputChangeProduct;
        List<DocumentHistoryInfo> outputImpoundProduct;
        List<DocumentHistoryInfo> outputChangeContract;
        List<DocumentHistoryInfo> outputManualDocument;
        List<DocumentHistoryInfo> outputPayInSlipBank;
        List<DocumentHistoryInfo> outputPayInSlipPayPoint;

        String[] positionCode = BHPreference.PositionCode().split(",");
        if (Arrays.asList(positionCode).contains("SaleLeader")) {
            outputContract = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.Contract.toString());
            outputReceipt = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.Receipt.toString());
            outputChangeProduct = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ChangeProduct.toString());
            outputImpoundProduct = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ImpoundProduct.toString());
            outputChangeContract = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ChangeContract.toString());
            outputManualDocument = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ManualDocument.toString());
            outputPayInSlipBank = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.PayInSlipBank.toString());
            outputPayInSlipPayPoint = TSRController.getDocumentHistoryGroupByType(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.PayInSlipPayPoint.toString());
        } else {
            outputContract = TSRController.getDocumentHistoryGroupByTypeAndEmployeeCode(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.Contract.toString(), BHPreference.employeeID());
            outputReceipt = TSRController.getDocumentHistoryGroupByTypeAndEmployeeCode(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.Receipt.toString(), BHPreference.employeeID());
            outputChangeProduct = TSRController.getDocumentHistoryGroupByTypeAndEmployeeCode(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ChangeProduct.toString(), BHPreference.employeeID());
            outputImpoundProduct = TSRController.getDocumentHistoryGroupByTypeAndEmployeeCode(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ImpoundProduct.toString(), BHPreference.employeeID());
            outputChangeContract = TSRController.getDocumentHistoryGroupByTypeAndEmployeeCode(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ChangeContract.toString(), BHPreference.employeeID());
            outputManualDocument = TSRController.getDocumentHistoryGroupByTypeAndEmployeeCode(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.ManualDocument.toString(), BHPreference.employeeID());
            outputPayInSlipBank = TSRController.getDocumentHistoryGroupByTypeAndEmployeeCode(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.PayInSlipBank.toString(), BHPreference.employeeID());
            outputPayInSlipPayPoint = TSRController.getDocumentHistoryGroupByTypeAndEmployeeCode(BHPreference.organizationCode(), DocumentHistoryController.DocumentType.PayInSlipPayPoint.toString(), BHPreference.employeeID());
        }

        /*** [END] :: Fixed - [BHPROJ-0024-3234] :: การส่งเอกสารไม่ตรง  ***/

        document.add(DocumentController.getTextDocumentHistory(outputContract,
                outputReceipt,
                outputChangeProduct,
                outputImpoundProduct,
                outputChangeContract,
                outputManualDocument,
                outputPayInSlipBank,
                outputPayInSlipPayPoint));
        mainActivity.printText(document, new MainActivity.PrintHandler() {
        });
    }

    public static void printChangeProduct(final String ChangeProductID) {
        /*ChangeProductInfo changeProduct = getChangeProductByID(BHPreference.organizationCode(), ChangeProductID);
        ContractInfo contract = getContractByRefNo(BHPreference.organizationCode(), changeProduct.RefNo);
        AddressInfo address = getAddress(changeProduct.RefNo, AddressInfo.AddressType.AddressIDCard);

        DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(ChangeProductID, DocumentHistoryController.DocumentType.ChangeProduct.toString());
        List<Bitmap> documents = new ArrayList<Bitmap>();
        int limit = checkExist != null ? 1 : 2;
        for (int i = 0; i < limit; i++) {
            Bitmap document = DocumentController.getChangeProduct(changeProduct, contract, address);
            documents.add(document);
        }

        mainActivity.printImage(documents.toArray(new Bitmap[documents.size()]), new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {

                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(ChangeProductID, DocumentHistoryController.DocumentType.ChangeProduct.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                Date currentDate = new Date();
                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = currentDate;
                docHist.DocumentType = DocumentHistoryController.DocumentType.ChangeProduct.toString();
                docHist.DocumentNumber = ChangeProductID;
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = currentDate;
                docHist.LastUpdateBy = BHPreference.employeeID();
                docHist.LastUpdateDate = currentDate;
                docHist.PrintOrder = num;

                TSRController.addDocumentHistory(docHist, true);

            }
        });*/

        List<List<PrintTextInfo>> document = new ArrayList<>();
        ChangeProductInfo changeProduct = getChangeProductByID(BHPreference.organizationCode(), ChangeProductID);
        ContractInfo contract = getContractByRefNo(BHPreference.organizationCode(), changeProduct.RefNo);
        AddressInfo address = getAddress(changeProduct.RefNo, AddressInfo.AddressType.AddressIDCard);

        DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(ChangeProductID, DocumentHistoryController.DocumentType.ChangeProduct.toString());
        int limit = checkExist != null ? 1 : 2;
        for (int i = 0; i < limit; i++) {
            document.add(DocumentController.getTextChangeProduct(changeProduct, contract, address));
        }

        mainActivity.printText(document, new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {

                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(ChangeProductID, DocumentHistoryController.DocumentType.ChangeProduct.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                Date currentDate = new Date();
                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = currentDate;
                docHist.DocumentType = DocumentHistoryController.DocumentType.ChangeProduct.toString();
                docHist.DocumentNumber = ChangeProductID;
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = currentDate;
                docHist.LastUpdateBy = BHPreference.employeeID();
                docHist.LastUpdateDate = currentDate;
                docHist.PrintOrder = num;

                TSRController.addDocumentHistory(docHist, true);

            }
        });
    }

    public static void printImpoundProduct(final String ImpoundProductID) {
        /*ImpoundProductInfo impoundProduct = TSRController.getImpoundProductByID(BHPreference.organizationCode(), ImpoundProductID);
        //ContractInfo contract = getContractByRefNo(BHPreference.organizationCode(), impoundProduct.RefNo);
        //EmployeeInfo employeeInfo = TSRController.getEmployeeDetailByEmployeeIDAndPositionCode(BHPreference.organizationCode(), impoundProduct.SaleEmployeeCode, "Sale", impoundProduct.SaleTeamCode);
        AddressInfo address = getAddress(impoundProduct.RefNo, AddressInfo.AddressType.AddressIDCard);

        DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(ImpoundProductID, DocumentHistoryController.DocumentType.ImpoundProduct.toString());
        List<Bitmap> documents = new ArrayList<Bitmap>();
        int limit = checkExist != null ? 1 : 2;
        for (int i = 0; i < limit; i++) {
            Bitmap document = DocumentController.getImpoundProduct(impoundProduct, address);
            documents.add(document);
        }

        mainActivity.printImage(documents.toArray(new Bitmap[documents.size()]), new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {

                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(ImpoundProductID, DocumentHistoryController.DocumentType.ImpoundProduct.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                Date currentDate = new Date();
                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = currentDate;
                docHist.DocumentType = DocumentHistoryController.DocumentType.ImpoundProduct.toString();
                docHist.DocumentNumber = ImpoundProductID;
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = currentDate;
                docHist.LastUpdateBy = BHPreference.employeeID();
                docHist.LastUpdateDate = currentDate;
                docHist.PrintOrder = num;

                TSRController.addDocumentHistory(docHist, true);

            }
        });*/

        List<List<PrintTextInfo>> document = new ArrayList<>();
        ImpoundProductInfo impoundProduct = TSRController.getImpoundProductByID(BHPreference.organizationCode(), ImpoundProductID);
        AddressInfo address = getAddress(impoundProduct.RefNo, AddressInfo.AddressType.AddressIDCard);

        DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(ImpoundProductID, DocumentHistoryController.DocumentType.ImpoundProduct.toString());
        int limit = checkExist != null ? 1 : 2;
        for (int i = 0; i < limit; i++) {
            document.add(DocumentController.getTextImpoundProduct(impoundProduct, address));
        }

        mainActivity.printText(document, new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {

                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(ImpoundProductID, DocumentHistoryController.DocumentType.ImpoundProduct.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                Date currentDate = new Date();
                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = currentDate;
                docHist.DocumentType = DocumentHistoryController.DocumentType.ImpoundProduct.toString();
                docHist.DocumentNumber = ImpoundProductID;
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = currentDate;
                docHist.LastUpdateBy = BHPreference.employeeID();
                docHist.LastUpdateDate = currentDate;
                docHist.PrintOrder = num;

                TSRController.addDocumentHistory(docHist, true);

            }
        });
    }

    public static void printChangeContract(final String NewSaleID) {
        /*List<ChangeContractInfo> changeContractList = TSRController.getChangeContractByNewSaleIDReturnChangeContract(BHPreference.organizationCode(), NewSaleID);
        int i = 0;
        int selected = -1;
        for (ChangeContractInfo changeContract : changeContractList) {
            if (changeContract.EffectiveBySaleCode != null && !changeContract.EffectiveBySaleCode.isEmpty() && !changeContract.EffectiveBySaleCode.equals("")) {
                selected = i;
                break;
            }
            i++;
        }
        final ChangeContractInfo changeContract = changeContractList.get(selected > -1 ? selected : 0);
        ContractInfo oldContract = new ContractController().getContractByRefNoForPrintChangeContract(BHPreference.organizationCode(), changeContract.OldSaleID);
        ContractInfo newContract = new ContractController().getContractByRefNoForPrintChangeContract(BHPreference.organizationCode(), changeContract.NewSaleID);
        ManualDocumentInfo manual = TSRController.getManualDocumentContractByDocumentNumber(changeContract.NewSaleID);
        AddressInfo defaultAddress = getAddress(newContract.RefNo, AddressInfo.AddressType.AddressIDCard);
        AddressInfo installAddress = getAddress(newContract.RefNo, AddressInfo.AddressType.AddressInstall);
        List<SalePaymentPeriodInfo> sspList = new SalePaymentPeriodController().getSalePaymentPeriodByRefNo(newContract.RefNo);

        DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(changeContract.ChangeContractID, DocumentHistoryController.DocumentType.ChangeContract.toString());
        List<Bitmap> documents = new ArrayList<Bitmap>();
        int limit = checkExist != null ? 1 : 2;
        for (int x = 0; x < limit; x++) {
            Bitmap document = DocumentController.getChangeContract(changeContract, oldContract, newContract, defaultAddress, installAddress, sspList, manual);
            documents.add(document);
        }

        mainActivity.printImage(documents.toArray(new Bitmap[documents.size()]), new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {

                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(changeContract.ChangeContractID, DocumentHistoryController.DocumentType.ChangeContract.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                Date currentDate = new Date();
                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = currentDate;
                docHist.DocumentType = DocumentHistoryController.DocumentType.ChangeContract.toString();
                docHist.DocumentNumber = changeContract.ChangeContractID;
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = currentDate;
                docHist.LastUpdateBy = BHPreference.employeeID();
                docHist.LastUpdateDate = currentDate;
                docHist.PrintOrder = num;

                TSRController.addDocumentHistory(docHist, true);

            }
        });*/

        List<List<PrintTextInfo>> document = new ArrayList<>();
        List<ChangeContractInfo> changeContractList = TSRController.getChangeContractByNewSaleIDReturnChangeContract(BHPreference.organizationCode(), NewSaleID);
        int i = 0;
        int selected = -1;
        for (ChangeContractInfo changeContract : changeContractList) {
            if (changeContract.EffectiveBySaleCode != null && !changeContract.EffectiveBySaleCode.isEmpty() && !changeContract.EffectiveBySaleCode.equals("")) {
                selected = i;
                break;
            }
            i++;
        }
        final ChangeContractInfo changeContract = changeContractList.get(selected > -1 ? selected : 0);
        ContractInfo oldContract = new ContractController().getContractByRefNoForPrintChangeContract(BHPreference.organizationCode(), changeContract.OldSaleID);
        ContractInfo newContract = new ContractController().getContractByRefNoForPrintChangeContract(BHPreference.organizationCode(), changeContract.NewSaleID);

        /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
        //ManualDocumentInfo manual = TSRController.getManualDocumentContractByDocumentNumber(changeContract.NewSaleID);
        /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/

        AddressInfo defaultAddress = getAddress(newContract.RefNo, AddressInfo.AddressType.AddressIDCard);
        AddressInfo installAddress = getAddress(newContract.RefNo, AddressInfo.AddressType.AddressInstall);
        List<SalePaymentPeriodInfo> sspList = new SalePaymentPeriodController().getSalePaymentPeriodByRefNo(newContract.RefNo);

        DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(changeContract.ChangeContractID, DocumentHistoryController.DocumentType.ChangeContract.toString());

        int limit = checkExist != null ? 1 : 2;
        for (int x = 0; x < limit; x++) {
            /*** [START] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
            //document.add(DocumentController.getTextChangeContract(changeContract, oldContract, newContract, defaultAddress, installAddress, sspList, manual));
            document.add(DocumentController.getTextChangeContract(changeContract, oldContract, newContract, defaultAddress, installAddress, sspList));
            /*** [END] :: Fixed - [BHPROJ-0024-3080] :: [Android-รายละเอียดสัญญา] แก้ไขให้แสดงค่า 'เลขที่อ้างอิง' โดยเปลี่ยนให้ดึงมาจากค่าข้อมูลใน [Contract].ContractReferenceNo แทน ***/
        }

        mainActivity.printText(document, new MainActivity.PrintHandler() {
            @Override
            public void onBackgroundPrinting(int index) {

                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(changeContract.ChangeContractID, DocumentHistoryController.DocumentType.ChangeContract.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                Date currentDate = new Date();
                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = currentDate;
                docHist.DocumentType = DocumentHistoryController.DocumentType.ChangeContract.toString();
                docHist.DocumentNumber = changeContract.ChangeContractID;
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = currentDate;
                docHist.LastUpdateBy = BHPreference.employeeID();
                docHist.LastUpdateDate = currentDate;
                docHist.PrintOrder = num;

                TSRController.addDocumentHistory(docHist, true);

            }
        });
    }

    /**
     *
     * Edit by Teerayut Klinsanga
     *
     * Created: 2019-08-02 09:00.00
     *
     * == Print with image ==
     *
     */
    public static void printNewImageContract(final ContractInfo contract, AddressInfo defaultAddress, AddressInfo installAddress) {
        DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(contract.RefNo, DocumentHistoryController.DocumentType.Contract.toString());
        List<List<PrintTextInfo>> documents = new ArrayList<>();
        List<Bitmap> bitmapList = new ArrayList<>();
        int limit = checkExist != null ? 1 : 2;
        for (int x = 0; x < limit; x++) {
            List<PrintTextInfo> document = DocumentController.getTextContract(contract, defaultAddress, installAddress);
            documents.add(document);
            bitmapList.add(DocumentController.getNewContactImage(contract, defaultAddress, installAddress));
        }

        mainActivity.printImageNew(bitmapList.toArray(new Bitmap[bitmapList.size()]), documents, new MainActivity.PrintHandler(){
            @Override
            public void onBackgroundPrinting(int index) {
                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(contract.RefNo,
                        DocumentHistoryController.DocumentType.Contract.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = new Date();
                docHist.DocumentType = DocumentHistoryController.DocumentType.Contract.toString();
                docHist.DocumentNumber = contract.RefNo;
                docHist.SyncedDate = new Date();
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = new Date();
                docHist.LastUpdateDate = null;
                docHist.LastUpdateBy = "";
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.PrintOrder = num;
                docHist.Status = "";
                docHist.SentDate = null;
                docHist.SentEmpID = "";
                docHist.SentSaleCode = "";
                docHist.SentSubTeamCode = "";
                docHist.SentTeamCode = "";
                docHist.ReceivedDate = null;
                docHist.ReceivedEmpID = "";

                TSRController.addDocumentHistory(docHist, true);
            }
        });
    }




    public static void printNewImageContract_preorder(final ContractInfo contract, AddressInfo defaultAddress, AddressInfo installAddress) {
        DocumentHistoryInfo checkExist = TSRController.getDocumentHistoryByDocumentNumber(contract.RefNo, DocumentHistoryController.DocumentType.Contract.toString());
        List<List<PrintTextInfo>> documents = new ArrayList<>();
        List<Bitmap> bitmapList = new ArrayList<>();
        int limit = checkExist != null ? 1 : 2;
        for (int x = 0; x < limit; x++) {
            List<PrintTextInfo> document = DocumentController_preorder.getTextContract(contract, defaultAddress, installAddress);
            documents.add(document);
            bitmapList.add(DocumentController_preorder.getNewContactImage(contract, defaultAddress, installAddress));
        }

        mainActivity.printImageNew(bitmapList.toArray(new Bitmap[bitmapList.size()]), documents, new MainActivity.PrintHandler(){
            @Override
            public void onBackgroundPrinting(int index) {
                DocumentHistoryInfo docHist = new DocumentHistoryInfo();
                DocumentHistoryInfo Hist;

                Hist = TSRController.getDocumentHistoryByDocumentNumber(contract.RefNo,
                        DocumentHistoryController.DocumentType.Contract.toString());

                int num = 1;
                if (Hist != null) {
                    num = Hist.PrintOrder + 1;
                }

                docHist.PrintHistoryID = DatabaseHelper.getUUID();
                docHist.OrganizationCode = BHPreference.organizationCode();
                docHist.DatePrint = new Date();
                docHist.DocumentType = DocumentHistoryController.DocumentType.Contract.toString();
                docHist.DocumentNumber = contract.RefNo;
                docHist.SyncedDate = new Date();
                docHist.CreateBy = BHPreference.employeeID();
                docHist.CreateDate = new Date();
                docHist.LastUpdateDate = null;
                docHist.LastUpdateBy = "";
                docHist.Selected = false;
                docHist.Deleted = false;
                docHist.PrintOrder = num;
                docHist.Status = "";
                docHist.SentDate = null;
                docHist.SentEmpID = "";
                docHist.SentSaleCode = "";
                docHist.SentSubTeamCode = "";
                docHist.SentTeamCode = "";
                docHist.ReceivedDate = null;
                docHist.ReceivedEmpID = "";

                TSRController.addDocumentHistory(docHist, true);
            }
        });
    }

    /**
     *
     * End
     */
}
