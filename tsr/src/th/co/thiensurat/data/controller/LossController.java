package th.co.thiensurat.data.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.data.info.SearchTypeInfo;


public class LossController extends BaseController {

    public List<SalePaymentPeriodInfo> getNextSalePaymentPeriodByContractTeam(String organizationCode, String saleTeamCode, String searchText, String refNo, int Year, int FortnightNumber) {
        List<String> args = new ArrayList<>();
        String sql = "select c.RefNo, c.CONTNO, c.ProductSerialNumber, p.ProductName, " +
                "            IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, dc.CompanyName) AS CustomerFullName " +
                "     from Contract as c " +
                "          inner join SalePaymentPeriod as spp ON spp.RefNo = c.RefNo and spp.PaymentComplete = 0 and spp.PaymentPeriodNumber = 1 " +
                "          inner join DebtorCustomer as dc ON dc.CustomerID = c.CustomerID and dc.OrganizationCode = c.OrganizationCode " +
                "          left join Product as p ON p.ProductID = c.ProductID " +
                "     where c.isActive = 1 and c.STATUS = ? and c.OrganizationCode = ? and c.SaleTeamCode = ? " +
                "           and c.FortnightID not in( select f1.FortnightID " +
                "                                     from Fortnight as f1 " +
                "                                          inner join (select * " +
                "                                                      from Fortnight " +
                "                                                      where Year = ? " +
                "                                                            and FortnightNumber = ? ) as f2 ON f2.OrganizationCode = f1.OrganizationCode and f2.EndDate >= f1.EndDate " +
                "                                      order by f1.EndDate DESC " +
                "                                      LIMIT 2 ) ";

        args.add(ContractInfo.ContractStatus.NORMAL.toString());
        args.add(organizationCode);
        args.add(saleTeamCode);
        args.add(valueOf(Year));
        args.add(valueOf(FortnightNumber));

        if (searchText != null) {
            sql += "        and (IFNULL(dc.CustomerName, dc.CompanyName) || IFNULL(dc.IDCard, '') || IFNULL(dc.AuthorizedIDCard, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ?) ";
            args.add("%" + searchText + "%");
        }

        if(refNo != null){
            sql += "        and c.RefNo = ? ";
            args.add(refNo);
        }

        sql += "      order by c.InstallDate";

        return executeQueryList(sql, args.toArray(new String[args.size()]), SalePaymentPeriodInfo.class);
    }

    public List<SearchTypeInfo> getSearchType() {
        List<SearchTypeInfo> typeLiss = new ArrayList<SearchTypeInfo>();
        SearchTypeInfo type = null;

        type = new SearchTypeInfo();
        type.SearchTypeCode = "001";
        type.SearchTypeName = "ชื่อ-สกุล";
        typeLiss.add(type);

        type = new SearchTypeInfo();
        type.SearchTypeCode = "002";
        type.SearchTypeName = "เลขที่บัตรประจำตัว";
        typeLiss.add(type);

        type = new SearchTypeInfo();
        type.SearchTypeCode = "003";
        type.SearchTypeName = "หมายเลขเครื่อง";
        typeLiss.add(type);

        type = new SearchTypeInfo();
        type.SearchTypeCode = "004";
        type.SearchTypeName = "เลขที่สัญญา";
        typeLiss.add(type);

        return typeLiss;

    }

//	public   List<SalePaymentPeriodInfo> getLoss(String teamCode) {
//		List<SalePaymentPeriodInfo> lossLis =  new ArrayList<SalePaymentPeriodInfo>();	;
//		SalePaymentPeriodInfo payment  = new SalePaymentPeriodInfo();
//		 
//		payment.InstallDate = DateFromYearMonthDate(2014,5,1);
//		payment.CustomerFullName = "นัดดา พิกุล";
//		payment.PaymentAmount = 2800; 
//		payment.FortnightNumber = 13;
//		payment.FortnightYear = 2014;
//		payment.CustomerID = "1";
//		payment.ProductSerialNumber = "1";
//		payment.CONTNO = "1";
//		lossLis.add(payment);
//		
//		payment  = new SalePaymentPeriodInfo(); 
//		payment.InstallDate = DateFromYearMonthDate(2014,6,1);
//		payment.CustomerFullName = "จันทรา เมฆาจักษ์";
//		payment.PaymentAmount = 9700;
//		payment.FortnightNumber = 14;
//		payment.FortnightYear = 2014;
//		payment.CustomerID = "2";
//		payment.ProductSerialNumber = "2";
//		payment.CONTNO = "2";
//		lossLis.add(payment);
//		
//		payment  = new SalePaymentPeriodInfo(); 
//		payment.InstallDate = DateFromYearMonthDate(2014,7,1);
//		payment.CustomerFullName = "ปิยะดา พิทักษ์";
//		payment.PaymentAmount = 1600;
//		payment.FortnightNumber = 15;
//		payment.FortnightYear = 2014;
//		payment.CustomerID = "3";
//		payment.ProductSerialNumber = "3";
//		payment.CONTNO = "3";
//		lossLis.add(payment);
//		
//		payment  = new SalePaymentPeriodInfo(); 
//		payment.InstallDate = DateFromYearMonthDate(2014,8,1);
//		payment.CustomerFullName = "นัดดา อารยะสงค์";
//		payment.PaymentAmount = 2000;
//		payment.FortnightNumber = 16;
//		payment.FortnightYear = 2014;
//		payment.CustomerID = "4";
//		payment.ProductSerialNumber = "4";
//		payment.CONTNO = "4";
//		lossLis.add(payment);		
//		return lossLis;
//	}

    private Date DateFromYearMonthDate(int year, int month, int date) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.MONTH, month - 1);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.DATE, date);
        return calendar.getTime();
    }


}
