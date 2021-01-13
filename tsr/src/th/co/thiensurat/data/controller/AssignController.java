package th.co.thiensurat.data.controller;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.thiensurat.data.info.AssignInfo;

public class AssignController extends BaseController {

    public enum AssignTaskType {
        ChangeProduct, ImpoundProduct, ChangeContract, WriteOffNPL, SalePaymentPeriod, SaleAudit, Complain, RequestNextPayment
    }

    /*** [START] :: Fixed - [BHPROJ-0026-3265] [Backend+Frontend] เพิ่มเมนูสำหรับทำการ ย้ายเข้า/ย้ายออก ข้อมูล Assign การเก็บเงิน  ***/
    public enum AssignAction {
        PUSH, PULL
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3265] [Backend+Frontend] เพิ่มเมนูสำหรับทำการ ย้ายเข้า/ย้ายออก ข้อมูล Assign การเก็บเงิน  ***/

    public List<AssignInfo> getAssignByRefNoByTaskTypeByReferenceID(String refNo, String taskType, String referenceID) {
        String sql = "SELECT  * FROM [Assign] WHERE (RefNo = ?) AND (TaskType = ?) AND (ReferenceID = ?)";
        return executeQueryList(sql, new String[]{refNo, taskType, referenceID}, AssignInfo.class);
    }

    public AssignInfo getAssignByAssignID(String AssignID) {
        String sql = "SELECT  * FROM [Assign] WHERE AssignID = ?";
        return executeQueryObject(sql, new String[]{AssignID}, AssignInfo.class);
    }

    public void addAssign(AssignInfo info) {
        String sql = "	INSERT INTO [Assign] (AssignID, TaskType, OrganizationCode, RefNo, AssigneeEmpID, AssigneeTeamCode,"
                + "					[Order], [OrderExpect], CreateDate, CreateBy, LastUpdateDate, LastUpdateBy, ReferenceID)"
                + "			VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        executeNonQuery(sql, new String[]{info.AssignID, info.TaskType, info.OrganizationCode, info.RefNo, info.AssigneeEmpID, info.AssigneeTeamCode,
                valueOf(info.Order), valueOf(info.OrderExpect), valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.ReferenceID});
    }

    public void updateAssign(AssignInfo info) {
        String sql = "	UPDATE [Assign] "
                + " SET "
                + "	AssigneeEmpID = ?,"
                + "	AssigneeTeamCode = ?,"
                + "	[Order] = ?,"
//                + "	OrderExpect = ?,"
                + "	LastUpdateDate = date('now'),"
                + "	LastUpdateBy = ?"
                + "	WHERE (RefNo = ?) AND (TaskType = ?) AND (ReferenceID = ?)";
        executeNonQuery(sql, new String[]{info.AssigneeEmpID, info.AssigneeTeamCode, valueOf(info.Order)//, valueOf(info.OrderExpect)
                , info.LastUpdateBy, info.RefNo, info.TaskType, info.ReferenceID});
    }

    public void updateAssignByAssignID(AssignInfo info) {
        String sql = "	UPDATE [Assign] "
                + "	SET "
                + " TaskType = ?, "
                + " RefNo = ?, "
                + " AssigneeEmpID = ?,"
                + "	AssigneeTeamCode = ?,"
                + "	[Order] = ?,"
                + "	OrderExpect = ?,"
                + "	LastUpdateDate = date('now'),"
                + "	LastUpdateBy = ?,"
                + " ReferenceID = ?"
                + "	WHERE (OrganizationCode = ?) AND (AssignID = ?) ";
        executeNonQuery(sql, new String[]{info.TaskType, info.RefNo, info.AssigneeEmpID, info.AssigneeTeamCode, valueOf(info.Order), valueOf(info.OrderExpect), info.LastUpdateBy,
                info.ReferenceID, info.OrganizationCode, info.AssignID});
    }

    public void updateAssignForPostpone(AssignInfo info) {
        String sql = "UPDATE Assign SET [Order] = ? WHERE AssignID = ?";
        executeNonQuery(sql, new String[]{valueOf(info.Order), info.AssignID});
    }

    public List<AssignInfo> getSaleAuditByAssigneeEmpID(String OrganizationCode, String AssigneeEmpID, String AddressTypeCode) {
        String complainStatus = ComplainController.ComplainStatus.COMPLETED.toString();
        String sql = "SELECT a.*" +
                ", c.CONTNO" +
                ", IFNULL(cust.PrefixName, '') || IFNULL(cust.CustomerName, '') || IFNULL(cust.CompanyName, '') AS CustomerFullName" +
                ", spp.NetAmount, spp.PaymentPeriodNumber" +
                "       , addr.AddressID, addr.AddressTypeCode, addr.AddressDetail, addr.AddressDetail2, addr.AddressDetail3, addr.AddressDetail4, addr.ProvinceCode" +
                "       , addr.DistrictCode, addr.SubDistrictCode, addr.Zipcode, addr.AddressInputMethod, addr.TelHome, addr.TelMobile, addr.TelOffice, addr.EMail" +
                "       , prov.ProvinceName, dt.DistrictName, sd.SubDistrictName, addr.Latitude, addr.Longitude" +
                " FROM Assign a" +
                " INNER JOIN SaleAudit sa on sa.SaleAuditID = a.ReferenceID  AND  IFNULL(sa.IsPassSaleAudit, 0) = 0" +
                " INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                " INNER JOIN DebtorCustomer cust on cust.CustomerID = c.CustomerID" +
                " INNER JOIN Address addr ON addr.RefNo = a.RefNo AND addr.AddressTypeCode = ?" +
                " LEFT OUTER JOIN Province prov ON prov.ProvinceCode = addr.ProvinceCode" +
                " LEFT OUTER JOIN AddressType at ON at.AddressTypeCode = addr.AddressTypeCode" +
                " LEFT OUTER JOIN District dt ON dt.DistrictCode = addr.DistrictCode " +
                " LEFT OUTER JOIN SubDistrict sd ON sd.SubDistrictCode = addr.SubDistrictCode " +
                " INNER JOIN SalePaymentPeriod spp on spp.RefNo = c.RefNo AND PaymentPeriodNumber = 1" +
                " LEFT JOIN Complain on Complain.ComplainID = sa.ComplainID" +
                " WHERE  a.OrganizationCode = ?" +
                " AND a.TaskType = 'SaleAudit'" +
                " AND a.AssigneeEmpID = ?" +
                " AND (Complain.Status = ? OR sa.ComplainID IS NULL OR sa.ComplainID = '')" +
                " ORDER BY a.[Order]";
        return executeQueryList(sql, new String[]{AddressTypeCode, OrganizationCode, AssigneeEmpID, complainStatus}, AssignInfo.class);
    }

    public List<AssignInfo> getSaleAuditByAssigneeEmpIDAndSearch(String OrganizationCode, String AssigneeEmpID, String Search) {
        String complainStatus = ComplainController.ComplainStatus.COMPLETED.toString();
        String sql = "SELECT a.*" +
                ", c.CONTNO" +
                ", IFNULL(cust.PrefixName, '') || IFNULL(cust.CustomerName, '') || IFNULL(cust.CompanyName, '') AS CustomerFullName" +
                ", spp.NetAmount, spp.PaymentPeriodNumber" +
                "       , addr.AddressID, addr.AddressTypeCode, addr.AddressDetail, addr.AddressDetail2, addr.AddressDetail3, addr.AddressDetail4, addr.ProvinceCode" +
                "       , addr.DistrictCode, addr.SubDistrictCode, addr.Zipcode, addr.AddressInputMethod, addr.TelHome, addr.TelMobile, addr.TelOffice, addr.EMail" +
                "       , prov.ProvinceName, dt.DistrictName, sd.SubDistrictName, addr.Latitude, addr.Longitude" +
                " FROM Assign a" +
                " INNER JOIN SaleAudit sa on sa.SaleAuditID = a.ReferenceID  AND  IFNULL(sa.IsPassSaleAudit, 0) = 0" +
                " INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                " INNER JOIN DebtorCustomer cust on cust.CustomerID = c.CustomerID" +
                " INNER JOIN Address addr ON addr.RefNo = a.RefNo AND addr.AddressTypeCode = 'AddressPayment'" +
                " LEFT OUTER JOIN Province prov ON prov.ProvinceCode = addr.ProvinceCode" +
                " LEFT OUTER JOIN AddressType at ON at.AddressTypeCode = addr.AddressTypeCode" +
                " LEFT OUTER JOIN District dt ON dt.DistrictCode = addr.DistrictCode " +
                " LEFT OUTER JOIN SubDistrict sd ON sd.SubDistrictCode = addr.SubDistrictCode " +
                " INNER JOIN SalePaymentPeriod spp on spp.RefNo = c.RefNo AND PaymentPeriodNumber = 1" +
                " LEFT JOIN Complain on Complain.ComplainID = sa.ComplainID" +
                " WHERE  a.OrganizationCode = ?" +
                " AND a.TaskType = 'SaleAudit'" +
                " AND a.AssigneeEmpID = ?" +
                " AND (Complain.Status = ? OR sa.ComplainID IS NULL OR sa.ComplainID = '')" +
                " AND IFNULL(cust.CustomerName, '') || IFNULL(cust.CompanyName, '') LIKE  ? " +
                " ORDER BY a.[Order]";
        return executeQueryList(sql, new String[]{OrganizationCode, AssigneeEmpID, complainStatus, Search}, AssignInfo.class);
    }

    /*public List<AssignInfo> getSalePaymentPeriodListForAssignCredit(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID, Date selectedDate, String search, String AddressTypeCode) {
        String sql = "SELECT a.*" +
                "		, spp.PaymentPeriodNumber, spp.NetAmount" +
                "		, c.CONTNO" +
                "		, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') AS CustomerFullName" +
                "		, IFNULL(sppCount.HoldSalePaymentPeriod, 0) AS HoldSalePaymentPeriod" +
                "		, spp.NetAmount - IFNULL(sppSum.SumAmount, 0) AS OutStandingPayment" +
                "       , addr.AddressID, addr.AddressTypeCode, addr.AddressDetail, addr.AddressDetail2, addr.AddressDetail3, addr.AddressDetail4, addr.ProvinceCode" +
                "       , addr.DistrictCode, addr.SubDistrictCode, addr.Zipcode, addr.AddressInputMethod, addr.TelHome, addr.TelMobile, addr.TelOffice, addr.EMail" +
                "       , prov.ProvinceName, dt.DistrictName, sd.SubDistrictName, addr.Latitude, addr.Longitude" +
                " FROM Assign a" +
                " 		INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                " 		INNER JOIN (" +
                "					SELECT MIN(spp.PaymentPeriodNumber) AS PaymentPeriodNumber, a.RefNo" +
                "					FROM Assign a" +
                "						INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                "					WHERE a.OrganizationCode = ?" +
                "						AND a.TaskType = 'SalePaymentPeriod' " +
                "						AND a.AssigneeTeamCode = ?" +
                "						AND a.AssigneeEmpID = ?" +
                "						AND spp.PaymentComplete = 0" +
                "						AND spp.PaymentPeriodNumber > 1" +
                "						AND DATE(spp.PaymentAppointmentDate) = DATE(?)" +
                "					GROUP BY a.RefNo" +
                "		) AS aMin ON aMin.RefNo = a.RefNo AND aMin.PaymentPeriodNumber = spp.PaymentPeriodNumber" +
                "       INNER JOIN Address addr ON addr.RefNo = a.RefNo AND addr.AddressTypeCode = ?" +
                "       LEFT OUTER JOIN Province prov ON prov.ProvinceCode = addr.ProvinceCode" +
                "       LEFT OUTER JOIN AddressType at ON at.AddressTypeCode = addr.AddressTypeCode" +
                "       LEFT OUTER JOIN District dt ON dt.DistrictCode = addr.DistrictCode " +
                "       LEFT OUTER JOIN SubDistrict sd ON sd.SubDistrictCode = addr.SubDistrictCode " +
                " 		LEFT OUTER JOIN (" +
                " 					SELECT COUNT(*) AS HoldSalePaymentPeriod, sppCountDistinct.RefNo  " +
                "					FROM (" +
                "							SELECT DISTINCT spp.PaymentDueDate, spp.PaymentPeriodNumber, spp.RefNo" +
                "							FROM  Assign a " +
                "								INNER JOIN SalePaymentPeriod spp on spp.RefNo = a.RefNo " +
                "							WHERE a.OrganizationCode = ? " +
                "								AND a.TaskType = 'SalePaymentPeriod'  " +
                "								AND a.AssigneeTeamCode = ? " +
                "								AND a.AssigneeEmpID = ? " +
                "								AND DATE(spp.PaymentDueDate) < DATE('now')  " +
                "								AND spp.PaymentComplete = 0 " +
                "						) AS sppCountDistinct" +
                "					GROUP BY sppCountDistinct.RefNo" +
                " 		) AS sppCount ON sppCount.RefNo = a.RefNo" +
                " 		LEFT OUTER JOIN (" +
                "				SELECT SUM(Amount) as SumAmount, SalePaymentPeriodID " +
                "				FROM SalePaymentPeriodPayment " +
                "				GROUP BY SalePaymentPeriodID" +
                "		) AS sppSum ON sppSum.SalePaymentPeriodID  = spp.SalePaymentPeriodID" +
                " 		INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                " 		INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                " WHERE a.OrganizationCode = ?" +
                " 		AND a.TaskType = 'SalePaymentPeriod' " +
                " 		AND a.AssigneeTeamCode = ?" +
                " 		AND a.AssigneeEmpID = ?" +
                "		AND spp.PaymentComplete = 0 " +
                " 		AND spp.PaymentPeriodNumber > 1" +
                "   	AND DATE(spp.PaymentAppointmentDate) = DATE(?)" +
                "		AND IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ?" +
                " ORDER BY a.[Order] ASC";
        return executeQueryList(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID, valueOf(selectedDate), AddressTypeCode, OrganizationCode, AssigneeTeamCode, AssigneeEmpID, OrganizationCode, AssigneeTeamCode, AssigneeEmpID, valueOf(selectedDate), search}, AssignInfo.class);
    }*/

//	public List<AssignInfo> getSalePaymentPeriodListForAssignCredit(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID, Date selectedDate, String search){
//		String sql = "SELECT a.*" +
//				", spp.PaymentPeriodNumber, spp.NetAmount" +
//				", c.CONTNO" +
//				", IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') AS CustomerFullName" +
//				", CASE WHEN strftime('%m','now') - strftime('%m',spp.PaymentDueDate) > 0 THEN strftime('%m','now') - strftime('%m',spp.PaymentDueDate) ELSE 0 END AS HoldSalePaymentPeriod" +
//				", spp.NetAmount - IFNULL(sppSum.SumAmount, 0) AS OutStandingPayment" +
//				", Address.Latitude, Address.Longitude" +
//				" FROM Assign a" +
//				" INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
//				" LEFT OUTER JOIN (" +
//				"		SELECT SUM(Amount) as SumAmount, SalePaymentPeriodID " +
//				"		FROM SalePaymentPeriodPayment " +
//				"		GROUP BY SalePaymentPeriodID" +
//				") AS sppSum ON sppSum.SalePaymentPeriodID  = spp.SalePaymentPeriodID" +
//				" INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL' AND c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE StatusName = 'COMPLETED')" +
//				" INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
//				" INNER JOIN Address ON Address.RefNo = c.RefNo and Address.AddressTypeCode = 'AddressPayment'"+
//				" WHERE a.OrganizationCode = ?" +
//				" 	AND a.TaskType = 'SalePaymentPeriod' " +
//				" 	AND a.AssigneeTeamCode = ?" +
//				" 	AND a.AssigneeEmpID = ?" +
//				"	AND spp.PaymentComplete = 0 " +
//				" 	AND spp.PaymentPeriodNumber > 1" +
//                "   AND DATE(spp.PaymentAppointmentDate) = DATE(?)" +
//				"	AND IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') LIKE  ? " +
//				" ORDER BY a.[Order] ASC";
//		return executeQueryList(sql, new String[] {OrganizationCode, AssigneeTeamCode, AssigneeEmpID, valueOf(selectedDate), search}, AssignInfo.class);
//	}

    public List<AssignInfo> getSalePaymentPeriodGroupByPaymentAppointmentDate(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID, ArrayList<String> dateList) {
        String sql = "SELECT " +
                " IFNULL(DATE(Result.PaymentAppointmentDate), DATE(LimitDate.LimitPaymentAppointmentDate)) AS PaymentAppointmentDate" +
                " , CASE WHEN DATE(Result.PaymentAppointmentDate) IS NULL THEN 0 ELSE COUNT(*) END AS CountCreditGroupByPaymentAppointmentDate" +
                " FROM (";
        List<String> sqls = new ArrayList<String>();
        for (String date : dateList) {
            String s = "SELECT '" + date + "' AS LimitPaymentAppointmentDate";
            sqls.add(s);
        }
        String sqlPlus = TextUtils.join(" UNION ", sqls.toArray());
        sql += sqlPlus;
        sql += " ) AS LimitDate" +
                " LEFT OUTER JOIN (" +
                "       SELECT spp.PaymentAppointmentDate " +
                "       FROM Assign a" +
                "       INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID " +
                " 		INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL' AND c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE StatusName = 'COMPLETED')" +
                "       WHERE a.OrganizationCode = ?" +
                " 	        AND a.TaskType = 'SalePaymentPeriod' " +
                " 	        AND a.AssigneeTeamCode = ?" +
                " 	        AND a.AssigneeEmpID = ?" +
                "	        AND spp.PaymentComplete = 0 " +
                " 	        AND spp.PaymentPeriodNumber > 1" +
                "			AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ?) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1)" +
                "       GROUP BY a.RefNo, DATE(spp.PaymentAppointmentDate)" +
                " ) AS Result ON DATE(LimitDate.LimitPaymentAppointmentDate) = DATE(Result.PaymentAppointmentDate)" +
                " GROUP BY DATE(LimitDate.LimitPaymentAppointmentDate)";
        return executeQueryList(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID, AssigneeEmpID}, AssignInfo.class);
    }

    /***
     * [START] :: Fixed - [BHPROJ-0026-3258][LINE@06/09/2559] เมนูเก็บเงินค่างวด ได้รับ แสดงรายการสัญญาในทุก ๆ งวดที่เข้าเงื่อนไขในหน้า GrpByAppointmentDate (โดยไม่จำเป็นต้องเลือกงวดที่ Max)
     ***/
    /*
    public List<AssignInfo> getAssignSalePaymentPeriodGroupByPaymentAppointmentDate(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID) {
        String sql = "SELECT  COUNT(*) AS CountCreditGroupByPaymentAppointmentDate , DATE(spp.PaymentAppointmentDate) AS PaymentAppointmentDate" +
                " FROM (" +
                "       SELECT a.RefNo" +
                "           , MAX(spp.PaymentPeriodNumber) AS MaxPaymentPeriodNumber" +
                "       FROM Assign a" +
                " 		INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                " 		INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                " 		INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                " 		INNER JOIN Address ON Address.RefNo = c.RefNo and Address.AddressTypeCode = 'AddressPayment'" +
                "       WHERE a.OrganizationCode = ?" +
                " 		    AND a.TaskType = 'SalePaymentPeriod' " +
                " 		    AND a.AssigneeTeamCode = ?" +
                " 		    AND a.AssigneeEmpID = ?" +
                "		    AND spp.PaymentComplete = 0 " +
                " 		    AND spp.PaymentPeriodNumber > 1" +
                "		    AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ?) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1)" +
                "       GROUP BY a.RefNo" +
                ") AS p" +
                " INNER JOIN SalePaymentPeriod spp ON spp.RefNo = p.RefNo AND spp.PaymentPeriodNumber = p.MaxPaymentPeriodNumber" +
                " GROUP BY DATE(spp.PaymentAppointmentDate)" +
                " ORDER BY PaymentAppointmentDate ASC";
        return executeQueryList(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID, AssigneeEmpID}, AssignInfo.class);
        //return executeQueryList(sql, new String[] {OrganizationCode, AssigneeTeamCode, AssigneeEmpID}, AssignInfo.class);
    }
    */
    public List<AssignInfo> getAssignSalePaymentPeriodGroupByPaymentAppointmentDate(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID) {
        //-- Fixed - [BHPROJ-0026-3279][Android-ระบบเก็บเงินค่างวด] ในหน้าจอ Group by AppointmentDate การนับจำนวนลูกค้า ให้นับจำนวนลูกค้า ตามจำนวนคน **/
        //String sql = "SELECT  DATE(spp.PaymentAppointmentDate) AS PaymentAppointmentDate, COUNT(a.RefNo) AS CountCreditGroupByPaymentAppointmentDate" +
        String sql = "SELECT  DATE(spp.PaymentAppointmentDate) AS PaymentAppointmentDate, COUNT(DISTINCT a.RefNo) AS CountCreditGroupByPaymentAppointmentDate" +
                " FROM Assign a" +
                " 		    INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                " 		    INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                " 		    INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                " 		    INNER JOIN Address ON Address.RefNo = c.RefNo and Address.AddressTypeCode = 'AddressPayment'" +
                " WHERE a.OrganizationCode = ?" +
                " 		    AND a.TaskType = 'SalePaymentPeriod' " +
                " 		    AND a.AssigneeTeamCode = ?" +
                " 		    AND a.AssigneeEmpID = ?" +
                "		    AND spp.PaymentComplete = 0 " +
                " 		    AND spp.PaymentPeriodNumber > 1" +
                "		    AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ?) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1)" +
                " GROUP BY DATE(spp.PaymentAppointmentDate)" +
                " ORDER BY PaymentAppointmentDate ASC";
        return executeQueryList(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID, AssigneeEmpID}, AssignInfo.class);
        //return executeQueryList(sql, new String[] {OrganizationCode, AssigneeTeamCode, AssigneeEmpID}, AssignInfo.class);
    }

     public List<AssignInfo> getAssignSalePaymentPeriodGroupByPaymentAppointmentDateNotOverEndOfTrip(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID) {
                //-- Fixed - [BHPROJ-0026-3279][Android-ระบบเก็บเงินค่างวด] ในหน้าจอ Group by AppointmentDate การนับจำนวนลูกค้า ให้นับจำนวนลูกค้า ตามจำนวนคน **/
                //String sql = "SELECT  DATE(spp.PaymentAppointmentDate) AS PaymentAppointmentDate, COUNT(a.RefNo) AS CountCreditGroupByPaymentAppointmentDate" +
                String sql = "SELECT  DATE(spp.PaymentAppointmentDate) AS PaymentAppointmentDate, COUNT(DISTINCT a.RefNo) AS CountCreditGroupByPaymentAppointmentDate" +
                        " FROM Assign a" +
                        " 		    INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                        " 		    INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                        " 		    INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                        " 		    INNER JOIN Address ON Address.RefNo = c.RefNo and Address.AddressTypeCode = 'AddressPayment'" +
                        " WHERE a.OrganizationCode = ?" +
                        " 		    AND a.TaskType = 'SalePaymentPeriod' " +
                        " 		    AND a.AssigneeTeamCode = ?" +
                        " 		    AND a.AssigneeEmpID = ?" +
                        "		    AND spp.PaymentComplete = 0 " +
                        " 		    AND spp.PaymentPeriodNumber > 1" +
                        "		    AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ?) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1) " +
                        "           AND  (julianday(DATE(spp.PaymentAppointmentDate)) <= (SELECT julianday(DATE(EndDate)) FROM Trip WHERE DATE() BETWEEN DATE(StartDate) AND DATE(EndDate))) " +
                        " GROUP BY DATE(spp.PaymentAppointmentDate)" +
                        " ORDER BY PaymentAppointmentDate ASC";
                return executeQueryList(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID, AssigneeEmpID}, AssignInfo.class);
                //return executeQueryList(sql, new String[] {OrganizationCode, AssigneeTeamCode, AssigneeEmpID}, AssignInfo.class);
        }

    /*
    public List<AssignInfo> getSalePaymentPeriodListForAssignCredit(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID, Date selectedDate, String search, String AddressTypeCode) {
        String sql = "SELECT a.*" +
                "		, spp.PaymentPeriodNumber, spp.NetAmount" +
                "		, c.CONTNO" +
                "		, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') AS CustomerFullName" +
                "		, IFNULL(sppCount.HoldSalePaymentPeriod, 0) AS HoldSalePaymentPeriod" +
                "		, spp.NetAmount - IFNULL(sppSum.SumAmount, 0) AS OutStandingPayment" +
                "       , addr.AddressID, addr.AddressTypeCode, addr.AddressDetail, addr.AddressDetail2, addr.AddressDetail3, addr.AddressDetail4, addr.ProvinceCode" +
                "       , addr.DistrictCode, addr.SubDistrictCode, addr.Zipcode, addr.AddressInputMethod, addr.TelHome, addr.TelMobile, addr.TelOffice, addr.EMail" +
                "       , prov.ProvinceName, dt.DistrictName, sd.SubDistrictName, addr.Latitude, addr.Longitude" +
                " FROM Assign a" +
                " 		INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                " 		INNER JOIN (" +
                "					SELECT MAX(spp.PaymentPeriodNumber) AS PaymentPeriodNumber, a.RefNo" +
                "					FROM Assign a" +
                "						INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                "					WHERE a.OrganizationCode = ?" +
                "						AND a.TaskType = 'SalePaymentPeriod' " +
                "						AND a.AssigneeTeamCode = ?" +
                "						AND a.AssigneeEmpID = ?" +
                "						AND spp.PaymentComplete = 0" +
                "						AND spp.PaymentPeriodNumber > 1" +
                "                       AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ? ) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1) " +
                "					GROUP BY a.RefNo" +
                "		) AS aMin ON aMin.RefNo = a.RefNo AND aMin.PaymentPeriodNumber = spp.PaymentPeriodNumber" +
                "       INNER JOIN Address addr ON addr.RefNo = a.RefNo AND addr.AddressTypeCode = ?" +
                "       LEFT OUTER JOIN Province prov ON prov.ProvinceCode = addr.ProvinceCode" +
                "       LEFT OUTER JOIN AddressType at ON at.AddressTypeCode = addr.AddressTypeCode" +
                "       LEFT OUTER JOIN District dt ON dt.DistrictCode = addr.DistrictCode " +
                "       LEFT OUTER JOIN SubDistrict sd ON sd.SubDistrictCode = addr.SubDistrictCode " +
                " 		LEFT OUTER JOIN (" +
                " 					SELECT COUNT(*) AS HoldSalePaymentPeriod, sppCountDistinct.RefNo  " +
                "					FROM (" +
                "							SELECT DISTINCT spp.PaymentDueDate, spp.PaymentPeriodNumber, spp.RefNo" +
                "							FROM  SalePaymentPeriod spp " +
                "							WHERE DATE(spp.PaymentDueDate) < DATE('now')  " +
                "								AND spp.PaymentComplete = 0 " +
                "						) AS sppCountDistinct" +
                "					GROUP BY sppCountDistinct.RefNo" +
                " 		) AS sppCount ON sppCount.RefNo = a.RefNo" +
                " 		LEFT OUTER JOIN (" +
                "				SELECT SUM(Amount) as SumAmount, SalePaymentPeriodID " +
                "				FROM SalePaymentPeriodPayment " +
                "				GROUP BY SalePaymentPeriodID" +
                "		) AS sppSum ON sppSum.SalePaymentPeriodID  = spp.SalePaymentPeriodID" +
                " 		INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                " 		INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                " WHERE a.OrganizationCode = ?" +
                " 		AND a.TaskType = 'SalePaymentPeriod' " +
                " 		AND a.AssigneeTeamCode = ?" +
                " 		AND a.AssigneeEmpID = ?" +
                "		AND spp.PaymentComplete = 0 " +
                " 		AND spp.PaymentPeriodNumber > 1" +
                "   	AND DATE(spp.PaymentAppointmentDate) = DATE(?)" +
                "		AND IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ?" +
                " ORDER BY a.[Order] ASC";
        return executeQueryList(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID, AssigneeEmpID, AddressTypeCode, OrganizationCode, AssigneeTeamCode, AssigneeEmpID, valueOf(selectedDate), search}, AssignInfo.class);
    }
    */

    /***
     * [START] - Fixed - [Android-เก็บเงินค่างวด] เปลี่ยนเงื่อนไขการนับจำนวนงวดที่ค้าง
     ***/
    public List<AssignInfo> getSalePaymentPeriodListForAssignCredit(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID, Date selectedDate, String search, String AddressTypeCode) {
        /*** Fixed - [BHPROJ-0026-3280] :: [Android-ระบบเก็บเงินค่างวด] ในหน้าจอรายการสัญญาทั้งหมดของวัน AppointmentDate ที่ถูกเลือก การแสดงตัวเลขงวดเก็บเงิน ให้แสดงงวดต่ำสุด  ***/


        String sql = "SELECT a.*" +
                "		, spp.PaymentPeriodNumber, spp.NetAmount" +
                "		, c.CONTNO" +
                "		, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') AS CustomerFullName" +
                "		, IFNULL(sppCount.HoldSalePaymentPeriod, 0) AS HoldSalePaymentPeriod" +
         //       "		, spp.NetAmount - IFNULL(sppSum.SumAmount, 0) AS OutStandingPayment" +
                "       , spp.NetAmount as OutStandingPayment " +
                "       , addr.AddressID, addr.AddressTypeCode, addr.AddressDetail, addr.AddressDetail2, addr.AddressDetail3, addr.AddressDetail4, addr.ProvinceCode" +
                "       , addr.DistrictCode, addr.SubDistrictCode, addr.Zipcode, addr.AddressInputMethod, addr.TelHome, addr.TelMobile, addr.TelOffice, addr.EMail" +
                "       , prov.ProvinceName, dt.DistrictName, sd.SubDistrictName, addr.Latitude, addr.Longitude, aMin.MinPaymentPeriodNumber" +
                " FROM Assign a" +
                " 		INNER JOIN SalePaymentPeriod spp ON (spp.SalePaymentPeriodID = a.ReferenceID)" +
                " 		                                    AND (spp.RefNo = a.RefNo)     " +   // Fixed-[BHPROJ-1036-7383] เพิ่ม Join RefNo เพื่อ avoid งวดเก่าหลังทำ Change Package
                "       INNER JOIN Contract cc ON (cc.RefNo = a.RefNo)" +
                " 		INNER JOIN (" +
                "					SELECT a.RefNo, DATE(spp.PaymentAppointmentDate) AS PaymentAppointmentDate, MAX(spp.PaymentPeriodNumber) AS PaymentPeriodNumber, MIN(spp.PaymentPeriodNumber) AS MinPaymentPeriodNumber" +
                "					FROM Assign a" +
                "						   INNER JOIN SalePaymentPeriod spp ON (spp.SalePaymentPeriodID = a.ReferenceID)" +
                " 		                                                       AND (spp.RefNo = a.RefNo)     " +   // Fixed-[BHPROJ-1036-7383] เพิ่ม Join RefNo เพื่อ avoid งวดเก่าหลังทำ Change Package
                "					WHERE a.OrganizationCode = ?" +
                "						   AND a.TaskType = 'SalePaymentPeriod' " +
                //"						   AND a.AssigneeTeamCode = ?" +
                "						   AND a.AssigneeEmpID = ?" +
                "						   AND spp.PaymentComplete = 0" +
                "						   AND spp.PaymentPeriodNumber > 1" +
                "                          AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ? ) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1) " +
                "					GROUP BY a.RefNo, DATE(spp.PaymentAppointmentDate)" +
                "		           ) AS aMin ON aMin.RefNo = a.RefNo AND aMin.PaymentPeriodNumber = spp.PaymentPeriodNumber" +
                "       INNER JOIN Address addr ON addr.RefNo = a.RefNo AND addr.AddressTypeCode = ?" +
                "       LEFT OUTER JOIN Province prov ON prov.ProvinceCode = addr.ProvinceCode" +
                "       LEFT OUTER JOIN AddressType at ON at.AddressTypeCode = addr.AddressTypeCode" +
                "       LEFT OUTER JOIN District dt ON dt.DistrictCode = addr.DistrictCode " +
                "       LEFT OUTER JOIN SubDistrict sd ON sd.SubDistrictCode = addr.SubDistrictCode " +
                " 		LEFT OUTER JOIN (" +
                " 					SELECT COUNT(*) AS HoldSalePaymentPeriod, sppCountDistinct.RefNo  " +
                "					FROM (" +
                "							SELECT DISTINCT spp.PaymentDueDate, spp.PaymentPeriodNumber, spp.RefNo" +
                "							FROM  SalePaymentPeriod spp " +
                "							WHERE DATE(spp.PaymentDueDate) < DATE(?)  " +
                "								AND spp.PaymentComplete = 0 " +
                "						) AS sppCountDistinct" +
                "					GROUP BY sppCountDistinct.RefNo" +
                " 		) AS sppCount ON sppCount.RefNo = a.RefNo" +
        //        " 		LEFT OUTER JOIN (" +
        //        "				SELECT SUM(Amount) as SumAmount, SalePaymentPeriodID " +
        //        "				FROM SalePaymentPeriodPayment " +
        //        "				GROUP BY SalePaymentPeriodID" +
        //        "		) AS sppSum ON sppSum.SalePaymentPeriodID  = spp.SalePaymentPeriodID" +
                " 		INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                " 		INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                " WHERE  c.Model!='AC4003' AND substr(cc.SaleCode,1, 2) !='OP'  AND a.OrganizationCode = ?" +
                " 		AND a.TaskType = 'SalePaymentPeriod' " +
                //" 		AND a.AssigneeTeamCode = ?" +
                " 		AND a.AssigneeEmpID = ?" +
                "		AND spp.PaymentComplete = 0 " +
            //    " 		AND spp.PaymentPeriodNumber > 1  and c.CONTNO='81201615' " +
                " 		AND spp.PaymentPeriodNumber > 1   " +

                //    "   	AND DATE(spp.PaymentAppointmentDate) = DATE(?)" +
                "		AND IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ?" +
                " ORDER BY a.[OrderExpect], c.CONTNO ASC";
        //return executeQueryList(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID, AssigneeEmpID, AddressTypeCode, valueOf(new TripController().getDueDate()), OrganizationCode, AssigneeTeamCode, AssigneeEmpID, valueOf(selectedDate), search}, AssignInfo.class);
       // return executeQueryList(sql, new String[]{OrganizationCode, AssigneeEmpID, AssigneeEmpID, AddressTypeCode, valueOf(new TripController().getDueDate()), OrganizationCode, AssigneeEmpID, valueOf(selectedDate), search}, AssignInfo.class);
        return executeQueryList(sql, new String[]{OrganizationCode, AssigneeEmpID, AssigneeEmpID, AddressTypeCode, valueOf(new TripController().getDueDate()), OrganizationCode, AssigneeEmpID, search}, AssignInfo.class);


    }


    public List<AssignInfo> getSalePaymentPeriodListForAssignCredit_pc(String OrganizationCode , String AssigneeEmpID, Date selectedDate, String search, String AddressTypeCode) {
        /*** Fixed - [BHPROJ-0026-3280] :: [Android-ระบบเก็บเงินค่างวด] ในหน้าจอรายการสัญญาทั้งหมดของวัน AppointmentDate ที่ถูกเลือก การแสดงตัวเลขงวดเก็บเงิน ให้แสดงงวดต่ำสุด  ***/
        String sql = "SELECT a.*" +
                "		, spp.PaymentPeriodNumber, spp.NetAmount" +
                "		, c.CONTNO" +
                "		, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') AS CustomerFullName" +
                "		, IFNULL(sppCount.HoldSalePaymentPeriod, 0) AS HoldSalePaymentPeriod" +
                //       "		, spp.NetAmount - IFNULL(sppSum.SumAmount, 0) AS OutStandingPayment" +
                "       , spp.NetAmount as OutStandingPayment " +
                "       , addr.AddressID, addr.AddressTypeCode, addr.AddressDetail, addr.AddressDetail2, addr.AddressDetail3, addr.AddressDetail4, addr.ProvinceCode" +
                "       , addr.DistrictCode, addr.SubDistrictCode, addr.Zipcode, addr.AddressInputMethod, addr.TelHome, addr.TelMobile, addr.TelOffice, addr.EMail" +
                "       , prov.ProvinceName, dt.DistrictName, sd.SubDistrictName, addr.Latitude, addr.Longitude, aMin.MinPaymentPeriodNumber" +
                " FROM Assign a" +
                " 		INNER JOIN SalePaymentPeriod spp ON (spp.SalePaymentPeriodID = a.ReferenceID)" +
                " 		                                    AND (spp.RefNo = a.RefNo)     " +   // Fixed-[BHPROJ-1036-7383] เพิ่ม Join RefNo เพื่อ avoid งวดเก่าหลังทำ Change Package
                "       INNER JOIN Contract cc ON (cc.RefNo = a.RefNo)" +
                " 		INNER JOIN (" +
                "					SELECT a.RefNo, DATE(spp.PaymentAppointmentDate) AS PaymentAppointmentDate, MAX(spp.PaymentPeriodNumber) AS PaymentPeriodNumber, MIN(spp.PaymentPeriodNumber) AS MinPaymentPeriodNumber" +
                "					FROM Assign a" +
                "						   INNER JOIN SalePaymentPeriod spp ON (spp.SalePaymentPeriodID = a.ReferenceID)" +
                " 		                                                       AND (spp.RefNo = a.RefNo)     " +   // Fixed-[BHPROJ-1036-7383] เพิ่ม Join RefNo เพื่อ avoid งวดเก่าหลังทำ Change Package
                "					WHERE a.OrganizationCode = ?" +
                "						   AND a.TaskType = 'SalePaymentPeriod' " +
                //"						   AND a.AssigneeTeamCode = ?" +
                "						   AND a.AssigneeEmpID = ?" +
                "						   AND spp.PaymentComplete = 0" +
                "						   AND spp.PaymentPeriodNumber > 1" +
                "                          AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ? ) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1) " +
                "					GROUP BY a.RefNo, DATE(spp.PaymentAppointmentDate)" +
                "		           ) AS aMin ON aMin.RefNo = a.RefNo AND aMin.PaymentPeriodNumber = spp.PaymentPeriodNumber" +
                "       INNER JOIN Address addr ON addr.RefNo = a.RefNo AND addr.AddressTypeCode = ?" +
                "       LEFT OUTER JOIN Province prov ON prov.ProvinceCode = addr.ProvinceCode" +
                "       LEFT OUTER JOIN AddressType at ON at.AddressTypeCode = addr.AddressTypeCode" +
                "       LEFT OUTER JOIN District dt ON dt.DistrictCode = addr.DistrictCode " +
                "       LEFT OUTER JOIN SubDistrict sd ON sd.SubDistrictCode = addr.SubDistrictCode " +
                " 		LEFT OUTER JOIN (" +
                " 					SELECT COUNT(*) AS HoldSalePaymentPeriod, sppCountDistinct.RefNo  " +
                "					FROM (" +
                "							SELECT DISTINCT spp.PaymentDueDate, spp.PaymentPeriodNumber, spp.RefNo" +
                "							FROM  SalePaymentPeriod spp " +
                "							WHERE DATE(spp.PaymentDueDate) < DATE(?)  " +
                "								AND spp.PaymentComplete = 0 " +
                "						) AS sppCountDistinct" +
                "					GROUP BY sppCountDistinct.RefNo" +
                " 		) AS sppCount ON sppCount.RefNo = a.RefNo" +
                //        " 		LEFT OUTER JOIN (" +
                //        "				SELECT SUM(Amount) as SumAmount, SalePaymentPeriodID " +
                //        "				FROM SalePaymentPeriodPayment " +
                //        "				GROUP BY SalePaymentPeriodID" +
                //        "		) AS sppSum ON sppSum.SalePaymentPeriodID  = spp.SalePaymentPeriodID" +
                " 		INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                " 		INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                " WHERE (substr(cc.SaleCode,1, 2) ='OP' OR c.Model='AC4003')  AND a.OrganizationCode = ?" +
                " 		AND a.TaskType = 'SalePaymentPeriod' " +
                //" 		AND a.AssigneeTeamCode = ?" +
                " 		AND a.AssigneeEmpID = ?" +
                "		AND spp.PaymentComplete = 0 " +
                " 		AND spp.PaymentPeriodNumber > 1" +
                "   	AND DATE(spp.PaymentAppointmentDate) = DATE(?)" +
                "		AND IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ?" +
                " ORDER BY a.[OrderExpect], c.CONTNO ASC";
        //return executeQueryList(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID, AssigneeEmpID, AddressTypeCode, valueOf(new TripController().getDueDate()), OrganizationCode, AssigneeTeamCode, AssigneeEmpID, valueOf(selectedDate), search}, AssignInfo.class);
        return executeQueryList(sql, new String[]{OrganizationCode, AssigneeEmpID, AssigneeEmpID, AddressTypeCode, valueOf(new TripController().getDueDate()), OrganizationCode, AssigneeEmpID, valueOf(selectedDate), search}, AssignInfo.class);
    }

    /***
     * [START] - Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile
     ***/

    public List<AssignInfo> getNewSalePaymentPeriodListForAssignCredit(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID, String search, String AddressTypeCode) {
        /*** Fixed - [BHPROJ-0026-3280] :: [Android-ระบบเก็บเงินค่างวด] ในหน้าจอรายการสัญญาทั้งหมดของวัน AppointmentDate ที่ถูกเลือก การแสดงตัวเลขงวดเก็บเงิน ให้แสดงงวดต่ำสุด  ***/
        String sql = "SELECT a.*" +
                "       , spp.PaymentPeriodNumber, spp.NetAmount" +
                "       , c.CONTNO" +
                "       , IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') AS CustomerFullName" +
                "       , IFNULL(sppCount.HoldSalePaymentPeriod, 0) AS HoldSalePaymentPeriod" +
         //       "  , spp.NetAmount - IFNULL(sppSum.SumAmount, 0) AS OutStandingPayment" +
                "       , spp.NetAmount as OutStandingPayment " +
                "       , addr.AddressID, addr.AddressTypeCode, addr.AddressDetail, addr.AddressDetail2, addr.AddressDetail3, addr.AddressDetail4, addr.ProvinceCode" +
                "       , addr.DistrictCode, addr.SubDistrictCode, addr.Zipcode, addr.AddressInputMethod, addr.TelHome, addr.TelMobile, addr.TelOffice, addr.EMail" +
                "       , prov.ProvinceName, dt.DistrictName, sd.SubDistrictName, addr.Latitude, addr.Longitude, aMin.MinPaymentPeriodNumber" +
                " FROM Assign a" +
                "       INNER JOIN SalePaymentPeriod spp ON (spp.SalePaymentPeriodID = a.ReferenceID)" +
                " 		                                AND (spp.RefNo = a.RefNo)     " +   //-- Fixed-[BHPROJ-1036-7383] เพิ่ม Join RefNo เพื่อ avoid งวดเก่าหลังทำ Change Package
                "       INNER JOIN Contract cc ON (cc.RefNo = a.RefNo)" +
                "       INNER JOIN (" +
                "					SELECT a.RefNo, DATE(spp.PaymentAppointmentDate) AS PaymentAppointmentDate, MAX(spp.PaymentPeriodNumber) AS PaymentPeriodNumber, MIN(spp.PaymentPeriodNumber) AS MinPaymentPeriodNumber" +
                "					FROM Assign a" +
                "						  INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                " 		                                                      AND (spp.RefNo = a.RefNo)     " +   // Fixed-[BHPROJ-1036-7383] เพิ่ม Join RefNo เพื่อ avoid งวดเก่าหลังทำ Change Package
                "					WHERE a.OrganizationCode = ?" +
                "						  AND a.TaskType = 'SalePaymentPeriod' " +
                //"						  AND a.AssigneeTeamCode = ?" +
                "						  AND a.AssigneeEmpID = ?" +
                "						  AND spp.PaymentComplete = 0" +
                "						  AND spp.PaymentPeriodNumber > 1" +
         //       "                       AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ? ) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1) " +
                "					GROUP BY a.RefNo, DATE(spp.PaymentAppointmentDate)" +
                "                  ) AS aMin ON aMin.RefNo = a.RefNo AND aMin.PaymentPeriodNumber = spp.PaymentPeriodNumber" +
                "       INNER JOIN Address addr ON addr.RefNo = a.RefNo AND addr.AddressTypeCode = ?" +
                "       LEFT OUTER JOIN Province prov ON prov.ProvinceCode = addr.ProvinceCode" +
                "       LEFT OUTER JOIN AddressType at ON at.AddressTypeCode = addr.AddressTypeCode" +
                "       LEFT OUTER JOIN District dt ON dt.DistrictCode = addr.DistrictCode " +
                "       LEFT OUTER JOIN SubDistrict sd ON sd.SubDistrictCode = addr.SubDistrictCode " +
                "       LEFT OUTER JOIN (" +
                "                         SELECT COUNT(*) AS HoldSalePaymentPeriod, sppCountDistinct.RefNo  " +
                "                         FROM (" +
                "                               SELECT DISTINCT spp.PaymentDueDate, spp.PaymentPeriodNumber, spp.RefNo" +
                "                               FROM  SalePaymentPeriod spp " +
                "                               WHERE DATE(spp.PaymentDueDate) < DATE(?)  " +
                "                                     AND  spp.PaymentComplete = 0 " +
                "                               ) AS sppCountDistinct" +
                "                         GROUP BY sppCountDistinct.RefNo" +
                "                      ) AS sppCount ON sppCount.RefNo = a.RefNo" +
         //       "   LEFT OUTER JOIN (" +
         //       "                    SELECT SUM(Amount) as SumAmount, SalePaymentPeriodID " +
         //       "                    FROM SalePaymentPeriodPayment " +
         //       "                     GROUP BY SalePaymentPeriodID" +
         //       "                    ) AS sppSum ON sppSum.SalePaymentPeriodID  = spp.SalePaymentPeriodID" +

            //    "       INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                "       INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND (c.Status = 'NORMAL' or c.Status = 'R') " +
                "       INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +

                //-- Fixed-[BHPROJ-1036-7383] Comment 2 lined ด้านล่าง

                /*** [START] :: Fixed - [BHPROJ-1036-7510] - อัพเดทเวอร์ชั่นใหม่แล้ว (9/10/60) พบปัญหาแสดงค่างวดหลายๆงวดในหน้าระบบเก็บเงินค่างวด ***/
                "     INNER JOIN (select RefNo, min(PaymentPeriodNumber) as MinPeriod from SalePaymentPeriod where  PaymentComplete = 0 group by Refno) " +
                "                     as minPeriod on amin.RefNo = minPeriod.RefNo and amin.MinPaymentPeriodNumber = minPeriod.MinPeriod " +
                /*** [END] :: Fixed - [BHPROJ-1036-7510] - อัพเดทเวอร์ชั่นใหม่แล้ว (9/10/60) พบปัญหาแสดงค่างวดหลายๆงวดในหน้าระบบเก็บเงินค่างวด ***/


                " WHERE c.Model!='AC4003' AND substr(cc.SaleCode,1, 2) !='OP'  AND a.OrganizationCode = ?" +
                "       AND a.TaskType = 'SalePaymentPeriod' " +
                //"     AND a.AssigneeTeamCode = ?" +
                "       AND a.AssigneeEmpID = ?" +
                "       AND spp.PaymentComplete = 0 " +
                "       AND spp.PaymentPeriodNumber > 1" +
         //     "       AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ?) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1) " +
                "       AND IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ?" +
                " ORDER BY a.[OrderExpect], c.CONTNO ASC";
        //return executeQueryList(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID,   AddressTypeCode , valueOf(new TripController().getDueDate()), OrganizationCode, AssigneeTeamCode, AssigneeEmpID,   search}, AssignInfo.class);



        return executeQueryList(sql, new String[]{OrganizationCode, AssigneeEmpID,   AddressTypeCode , valueOf(new TripController().getDueDate()), OrganizationCode, AssigneeEmpID,   search}, AssignInfo.class);
    }










    public List<AssignInfo> getNewSalePaymentPeriodListForAssignCredit_PC(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID, String search, String AddressTypeCode) {
        /*** Fixed - [BHPROJ-0026-3280] :: [Android-ระบบเก็บเงินค่างวด] ในหน้าจอรายการสัญญาทั้งหมดของวัน AppointmentDate ที่ถูกเลือก การแสดงตัวเลขงวดเก็บเงิน ให้แสดงงวดต่ำสุด  ***/
        String sql = "SELECT a.*" +
                "       , spp.PaymentPeriodNumber, spp.NetAmount" +
                "       , c.CONTNO" +
                "       , IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') AS CustomerFullName" +
                "       , IFNULL(sppCount.HoldSalePaymentPeriod, 0) AS HoldSalePaymentPeriod" +
                //       "  , spp.NetAmount - IFNULL(sppSum.SumAmount, 0) AS OutStandingPayment" +
                "       , spp.NetAmount as OutStandingPayment " +
                "       , addr.AddressID, addr.AddressTypeCode, addr.AddressDetail, addr.AddressDetail2, addr.AddressDetail3, addr.AddressDetail4, addr.ProvinceCode" +
                "       , addr.DistrictCode, addr.SubDistrictCode, addr.Zipcode, addr.AddressInputMethod, addr.TelHome, addr.TelMobile, addr.TelOffice, addr.EMail" +
                "       , prov.ProvinceName, dt.DistrictName, sd.SubDistrictName, addr.Latitude, addr.Longitude, aMin.MinPaymentPeriodNumber" +
                " FROM Assign a" +
                "       INNER JOIN SalePaymentPeriod spp ON (spp.SalePaymentPeriodID = a.ReferenceID)" +
                " 		                                AND (spp.RefNo = a.RefNo)     " +   //-- Fixed-[BHPROJ-1036-7383] เพิ่ม Join RefNo เพื่อ avoid งวดเก่าหลังทำ Change Package
                "       INNER JOIN Contract cc ON (cc.RefNo = a.RefNo)" +
                "       INNER JOIN (" +
                "					SELECT a.RefNo, DATE(spp.PaymentAppointmentDate) AS PaymentAppointmentDate, MAX(spp.PaymentPeriodNumber) AS PaymentPeriodNumber, MIN(spp.PaymentPeriodNumber) AS MinPaymentPeriodNumber" +
                "					FROM Assign a" +
                "						  INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                " 		                                                      AND (spp.RefNo = a.RefNo)     " +   // Fixed-[BHPROJ-1036-7383] เพิ่ม Join RefNo เพื่อ avoid งวดเก่าหลังทำ Change Package
                "					WHERE a.OrganizationCode = ?" +
                "						  AND a.TaskType = 'SalePaymentPeriod' " +
                //"						  AND a.AssigneeTeamCode = ?" +
                "						  AND a.AssigneeEmpID = ?" +
                "						  AND spp.PaymentComplete = 0" +
                "						  AND spp.PaymentPeriodNumber > 1" +
                //       "                       AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ? ) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1) " +
                "					GROUP BY a.RefNo, DATE(spp.PaymentAppointmentDate)" +
                "                  ) AS aMin ON aMin.RefNo = a.RefNo AND aMin.PaymentPeriodNumber = spp.PaymentPeriodNumber" +
                "       INNER JOIN Address addr ON addr.RefNo = a.RefNo AND addr.AddressTypeCode = ?" +
                "       LEFT OUTER JOIN Province prov ON prov.ProvinceCode = addr.ProvinceCode" +
                "       LEFT OUTER JOIN AddressType at ON at.AddressTypeCode = addr.AddressTypeCode" +
                "       LEFT OUTER JOIN District dt ON dt.DistrictCode = addr.DistrictCode " +
                "       LEFT OUTER JOIN SubDistrict sd ON sd.SubDistrictCode = addr.SubDistrictCode " +
                "       LEFT OUTER JOIN (" +
                "                         SELECT COUNT(*) AS HoldSalePaymentPeriod, sppCountDistinct.RefNo  " +
                "                         FROM (" +
                "                               SELECT DISTINCT spp.PaymentDueDate, spp.PaymentPeriodNumber, spp.RefNo" +
                "                               FROM  SalePaymentPeriod spp " +
                "                               WHERE DATE(spp.PaymentDueDate) < DATE(?)  " +
                "                                     AND  spp.PaymentComplete = 0 " +
                "                               ) AS sppCountDistinct" +
                "                         GROUP BY sppCountDistinct.RefNo" +
                "                      ) AS sppCount ON sppCount.RefNo = a.RefNo" +
                //       "   LEFT OUTER JOIN (" +
                //       "                    SELECT SUM(Amount) as SumAmount, SalePaymentPeriodID " +
                //       "                    FROM SalePaymentPeriodPayment " +
                //       "                     GROUP BY SalePaymentPeriodID" +
                //       "                    ) AS sppSum ON sppSum.SalePaymentPeriodID  = spp.SalePaymentPeriodID" +

                //    "       INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL'" +
                "       INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND (c.Status = 'NORMAL' or c.Status = 'R') " +
                "       INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +

                //-- Fixed-[BHPROJ-1036-7383] Comment 2 lined ด้านล่าง

                /*** [START] :: Fixed - [BHPROJ-1036-7510] - อัพเดทเวอร์ชั่นใหม่แล้ว (9/10/60) พบปัญหาแสดงค่างวดหลายๆงวดในหน้าระบบเก็บเงินค่างวด ***/
                "     INNER JOIN (select RefNo, min(PaymentPeriodNumber) as MinPeriod from SalePaymentPeriod where  PaymentComplete = 0 group by Refno) " +
                "                     as minPeriod on amin.RefNo = minPeriod.RefNo and amin.MinPaymentPeriodNumber = minPeriod.MinPeriod " +
                /*** [END] :: Fixed - [BHPROJ-1036-7510] - อัพเดทเวอร์ชั่นใหม่แล้ว (9/10/60) พบปัญหาแสดงค่างวดหลายๆงวดในหน้าระบบเก็บเงินค่างวด ***/


                " WHERE (substr(cc.SaleCode,1, 2) ='OP' OR c.Model='AC4003')  AND a.OrganizationCode = ?" +
                "       AND a.TaskType = 'SalePaymentPeriod' " +
                //"     AND a.AssigneeTeamCode = ?" +
                "       AND a.AssigneeEmpID = ?" +
                "       AND spp.PaymentComplete = 0 " +
                "       AND spp.PaymentPeriodNumber > 1" +
                //     "       AND (julianday(DATE(spp.PaymentAppointmentDate)) - julianday(DATE('now'))) <= (SELECT LimitMax FROM [Limit] WHERE (LimitType = 'ImportCreditDay') AND ((EmpID = ?) OR (EmpID IS NULL)) ORDER BY EmpID DESC LIMIT 1) " +
                "       AND IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') || IFNULL(c.ProductSerialNumber, '') || c.CONTNO LIKE ?" +
                " ORDER BY a.[OrderExpect], c.CONTNO ASC";
        //return executeQueryList(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID,   AddressTypeCode , valueOf(new TripController().getDueDate()), OrganizationCode, AssigneeTeamCode, AssigneeEmpID,   search}, AssignInfo.class);



        return executeQueryList(sql, new String[]{OrganizationCode, AssigneeEmpID,   AddressTypeCode , valueOf(new TripController().getDueDate()), OrganizationCode, AssigneeEmpID,   search}, AssignInfo.class);
    }









    /***
     * [END] - Fixed - [Android-เก็บเงินค่างวด] เปลี่ยนเงื่อนไขการนับจำนวนงวดที่ค้าง
     ***/

    /*** [END] - Fixed - [BHPROJ-0026-3248] [Android-เก็บเงินค่างวด] หลังจากที่เลือกวันที่ในการเก็บเงินแล้วมาแสดงรายการที่ต้องเก็บเงิน ลูกค้าต้องการให้สามารถค้นหาสัญญาได้ในทุก ๆ วัน (เดิมค้นหาได้เฉพาะวันที่เลือกกดเข้ามา) ที่ถูกแสดงบน Mobile ***/

    /***
     * [END] :: Fixed - [BHPROJ-0026-3258][LINE@06/09/2559] เมนูเก็บเงินค่างวด ได้รับ แสดงรายการสัญญาในทุก ๆ งวดที่เข้าเงื่อนไขในหน้า GrpByAppointmentDate (โดยไม่จำเป็นต้องเลือกงวดที่ Max)
     ***/

    public AssignInfo getMaxOrderByPaymentAppointmentDate(String OrganizationCode, String AssigneeTeamCode, String AssigneeEmpID, String ReferenceID) {
        String sql = "SELECT " +
                " 		MAX(IFNULL(a.[Order], 0)) + 1 AS [Order]" +
                " FROM Assign a" +
                " 		INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID" +
                " 		INNER JOIN Contract c ON c.RefNo = a.RefNo AND c.IsActive = 1 AND c.Status = 'NORMAL' AND c.StatusCode = (SELECT StatusCode FROM ContractStatus WHERE StatusName = 'COMPLETED')" +
                " 		INNER JOIN DebtorCustomer dc ON dc.CustomerID = c.CustomerID" +
                " 		INNER JOIN Address ON Address.RefNo = c.RefNo and Address.AddressTypeCode = 'AddressPayment'" +
                " WHERE a.OrganizationCode = ?" +
                " 		AND a.TaskType = 'SalePaymentPeriod' " +
                " 		AND a.AssigneeTeamCode = ?" +
                " 		AND a.AssigneeEmpID = ?" +
                "		AND spp.PaymentComplete = 0 " +
                " 		AND spp.PaymentPeriodNumber > 1" +
                "		AND DATE(spp.PaymentAppointmentDate) = (SELECT DATE(PaymentAppointmentDate) AS PaymentAppointmentDate FROM SalePaymentPeriod WHERE SalePaymentPeriodID = ?)";
        return executeQueryObject(sql, new String[]{OrganizationCode, AssigneeTeamCode, AssigneeEmpID, ReferenceID}, AssignInfo.class);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////

    public void deleteAssignAll() {
        String sql = "DELETE FROM Assign";
        executeNonQuery(sql, null);
    }

    public void deleteAssignByRefNoByTaskTypeByReferenceID(String refNo, String taskType, String referenceID) {
        String sql = "DELETE FROM Assign WHERE (RefNo = ?) AND (TaskType = ?) AND (ReferenceID = ?)";
        executeNonQuery(sql, new String[]{refNo, taskType, referenceID});
    }

    public void deleteAssignByTaskType(String TaskType) {
        String sql = "DELETE FROM Assign WHERE (TaskType = ?)";
        executeNonQuery(sql, new String[]{TaskType});
    }

    public void deleteAssignByAssignID(String AssignID) {
        String sql = "DELETE FROM Assign WHERE (AssignID = ?)";
        executeNonQuery(sql, new String[]{AssignID});
    }

    public void deleteAssignByRefNo(String refNo) {
        String sql = "DELETE FROM Assign WHERE (RefNo = ?)";
        executeNonQuery(sql, new String[]{refNo});
    }

    public void deleteAssignByAssignList(List<AssignInfo> assignList) {
        if (assignList != null && assignList.size() > 0) {
            List<String> assignIDList = new ArrayList<String>();
            for (AssignInfo assign : assignList) {
                assignIDList.add(String.format("\'%s\'", assign.AssignID));
            }
            String sql = "DELETE FROM Assign WHERE AssignID IN (" + TextUtils.join(",", assignIDList) + ")";
            executeNonQuery(sql, new String[]{});
        }
    }

    public List<AssignInfo> getAssignForPostpone(String RefNo, int maxPaymentPeriodNumber, String assigneeEmpID) {
        String sql = "SELECT a.*, spp.PaymentPeriodNumber, spp.PaymentAppointmentDate" +
                " FROM Assign a" +
                " INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID AND spp.PaymentPeriodNumber <= ? AND spp.PaymentComplete = 0" +
                " WHERE a.RefNo = ? AND a.TaskType = 'SalePaymentPeriod' AND a.AssigneeEmpID = ?" +
                " ORDER BY spp.PaymentPeriodNumber ASC";
        return executeQueryList(sql, new String[]{valueOf(maxPaymentPeriodNumber), RefNo, assigneeEmpID}, AssignInfo.class);
    }

    public List<AssignInfo> getAssignShiftOrder(Date paymentAppointmentDate, String assigneeEmpID) {
        String sql = "SELECT a.*" +
                "       FROM Assign a" +
                "       INNER JOIN SalePaymentPeriod spp ON spp.SalePaymentPeriodID = a.ReferenceID AND spp.PaymentComplete = 0 AND spp.PaymentPeriodNumber > 1" +
                "       WHERE a.TaskType = 'SalePaymentPeriod' AND DATE(spp.PaymentAppointmentDate) = DATE(?) AND a.AssigneeEmpID = ?" +
                "       ORDER BY a.[Order] ASC";
        return executeQueryList(sql, new String[]{valueOf(paymentAppointmentDate), assigneeEmpID}, AssignInfo.class);
    }

    /***
     * [START] :: Fixed - [BHPROJ-0020-3251] ระบบจัดลำดับการเก็บเงิน Load นานแล้วสักพักก็ค้าง แก้ไขโดยการ Tunning Performance query ของ method ที่ชื่อ getAssignSortOrderDefaultByAssigneeEmpID ใน File AssignController.java
     ***/
    public List<AssignInfo> getAssignSortOrderDefaultByAssigneeEmpID(String OrganizationCode, String TaskType, String AssigneeEmpID) {
        String sql;
        if (TaskType.equals(AssignController.AssignTaskType.SaleAudit.toString())) {
            sql = "SELECT c.RefNo, c.CONTNO, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') AS CustomerFullName, " +
                    "               a.AssignID, a.[Order], a.OrderExpect, a.OrganizationCode, a.AssigneeEmpID, a.TaskType, a.OrderExpect AS NewOrder " +
                    "FROM Assign AS a " +
                    "INNER JOIN SaleAudit AS sa ON sa.RefNo = a.RefNo AND sa.OrganizationCode = a.OrganizationCode AND sa.IsPassSaleAudit = 0 " +
                    "INNER JOIN Contract AS c ON c.RefNo = a.RefNo AND c.OrganizationCode = a.OrganizationCode AND c.isActive = 1 " +
                    "INNER JOIN DebtorCustomer AS dc ON dc.CustomerID = c.CustomerID " +
                    "WHERE a.OrganizationCode = ? AND a.TaskType = ? AND a.AssigneeEmpID = ? " +
                    "ORDER BY a.OrderExpect ASC, a.[Order] ASC";
        } else {
            sql = "SELECT c.RefNo, c.CONTNO, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') AS CustomerFullName, " +
                    "   a.AssignID, a.[Order], a.OrderExpect, a.OrganizationCode, a.AssigneeEmpID, a.TaskType, a.OrderExpect AS NewOrder " +
                    "FROM Assign AS a " +
                    "   INNER JOIN SalePaymentPeriod AS spp1 ON spp1.SalePaymentPeriodID = a.ReferenceID " +
                    "   INNER JOIN SaleAudit AS sa ON sa.RefNo = a.RefNo AND sa.OrganizationCode = a.OrganizationCode AND sa.IsPassSaleAudit = 1 " +
                    "   INNER JOIN Contract AS c ON c.RefNo = a.RefNo AND c.OrganizationCode = a.OrganizationCode AND c.isActive = 1 " +
                    "   INNER JOIN DebtorCustomer AS dc ON dc.CustomerID = c.CustomerID " +
                    "WHERE a.OrganizationCode = ? AND a.TaskType = ? AND a.AssigneeEmpID = ? " +
                    "   AND spp1.SalePaymentPeriodID IN (SELECT spp1.SalePaymentPeriodID  " +
                    "                                       FROM SalePaymentPeriod AS spp1 " +
                    "                                               INNER JOIN (SELECT MIN(subspp.PaymentPeriodNumber) AS PaymentPeriodNumber, subspp.RefNo " +
                    "                                                           FROM SalePaymentPeriod as subspp " +
                    "                                                           WHERE (subspp.PaymentComplete = 0) AND (subspp.PaymentPeriodNumber > 1) " +
                    "                                                           GROUP BY subspp.RefNo) AS spp2 " +
                    "                                               ON (spp1.RefNo = spp2.RefNo) AND (spp1.PaymentPeriodNumber = spp2.PaymentPeriodNumber)) " +
                    "ORDER BY a.OrderExpect ASC, a.[Order] ASC";
        }
        return executeQueryList(sql, new String[]{OrganizationCode, TaskType, AssigneeEmpID}, AssignInfo.class);
    }

    /***
     * [END] :: Fixed - [BHPROJ-0020-3251] ระบบจัดลำดับการเก็บเงิน Load นานแล้วสักพักก็ค้าง แก้ไขโดยการ Tunning Performance query ของ method ที่ชื่อ getAssignSortOrderDefaultByAssigneeEmpID ใน File AssignController.java
     ***/

    /*** [START] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/
    public List<AssignInfo> getAssignSortOrderDefaultByAssigneeEmpIDForCredit(String OrganizationCode, String TaskType, String AssigneeEmpID){
            String  sql = "SELECT c.RefNo, c.CONTNO, IFNULL(dc.PrefixName, '') || IFNULL(dc.CustomerName, '') || IFNULL(dc.CompanyName, '') AS CustomerFullName, " +
                    "   a.AssignID, a.[Order], a.OrderExpect, a.OrganizationCode, a.AssigneeEmpID, a.TaskType, a.OrderExpect AS NewOrder, " +
                    "   spp1.SalePaymentPeriodID, spp1.PaymentPeriodNumber, spp1.PaymentDueDate, " +
                    //"   cast(strftime('%d', spp1.PaymentDueDate) as integer) AS OldPaymentDueDay, cast(strftime('%d', spp1.PaymentDueDate) as integer) AS NewPaymentDueDay " +
                    "   cast(IFNULL(c.PAYDAY, '1') as integer) AS OldPaymentDueDay, cast(IFNULL(c.PAYDAY, '1') as integer) AS NewPaymentDueDay " +
                    "FROM Assign AS a " +
                    "   INNER JOIN SalePaymentPeriod AS spp1 ON spp1.SalePaymentPeriodID = a.ReferenceID " +
                    "   INNER JOIN SaleAudit AS sa ON sa.RefNo = a.RefNo AND sa.OrganizationCode = a.OrganizationCode AND sa.IsPassSaleAudit = 1 " +
                    "   INNER JOIN Contract AS c ON c.RefNo = a.RefNo AND c.OrganizationCode = a.OrganizationCode AND c.isActive = 1 " +
                    "   INNER JOIN DebtorCustomer AS dc ON dc.CustomerID = c.CustomerID " +
                    "WHERE a.OrganizationCode = ? AND a.TaskType = ? AND a.AssigneeEmpID = ? " +
                    "   AND spp1.SalePaymentPeriodID IN (SELECT spp1.SalePaymentPeriodID  " +
                    "                                       FROM SalePaymentPeriod AS spp1 " +
                    "                                               INNER JOIN (SELECT MIN(subspp.PaymentPeriodNumber) AS PaymentPeriodNumber, subspp.RefNo " +
                    "                                                           FROM SalePaymentPeriod as subspp " +
                    "                                                           WHERE (subspp.PaymentComplete = 0) AND (subspp.PaymentPeriodNumber > 1) " +
                    "                                                           GROUP BY subspp.RefNo) AS spp2 " +
                    "                                               ON (spp1.RefNo = spp2.RefNo) AND (spp1.PaymentPeriodNumber = spp2.PaymentPeriodNumber)) " +
                    //"   AND DATE(PaymentAppointmentDate) <= (SELECT DATE(EndDate) FROM Trip WHERE DATE() BETWEEN DATE(StartDate) AND DATE(EndDate)) " +
                    "ORDER BY a.OrderExpect ASC, a.[Order] ASC";
            return executeQueryList(sql, new String[]{OrganizationCode, TaskType, AssigneeEmpID}, AssignInfo.class);
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/


    public void updateAssignForSortOrderDefault(AssignInfo info) {
        String sql;
        if (info.TaskType.equals(AssignController.AssignTaskType.SaleAudit.toString())) {
            sql = "UPDATE [Assign] " +
                    "SET " +
                    "   [Order] = ?, " +
                    "	OrderExpect = ?, " +
                    "	LastUpdateDate = ?, " +
                    "	LastUpdateBy = ? " +
                    "WHERE AssignID IN (SELECT a.AssignID " +
                    "                   FROM Assign AS a " +
                    "                   WHERE a.OrganizationCode = ? AND a.RefNo = ? AND a.TaskType = ? AND a.AssigneeEmpID = ?) ";
        } else {
            sql = "UPDATE [Assign] " +
                    "SET " +
                    "   [Order] = ?, " +
                    "	OrderExpect = ?, " +
                    "	LastUpdateDate = ?, " +
                    "	LastUpdateBy = ? " +
                    "WHERE AssignID IN (SELECT a.AssignID " +
                    "                   FROM Assign AS a " +
                    "                       INNER JOIN SalePaymentPeriod AS spp ON spp.RefNo = a.RefNo " +
                    "                                   AND spp.SalePaymentPeriodID = a.ReferenceID " +
                    "                                   AND spp.PaymentComplete = 0 " +
                    "                                   AND spp.PaymentPeriodNumber > 1 " +
                    "                   WHERE a.OrganizationCode = ? AND a.RefNo = ? AND a.TaskType = ? AND a.AssigneeEmpID = ?) ";
        }
        executeNonQuery(sql, new String[]{valueOf(info.NewOrder), valueOf(info.NewOrder), valueOf(info.LastUpdateDate), info.LastUpdateBy,
                info.OrganizationCode, info.RefNo, info.TaskType, info.AssigneeEmpID});
    }

    /*** [START] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/
    public void updateAssignForSortOrderDefaultForCredit(AssignInfo info) {
        String sql = "UPDATE [Assign] " +
                "   SET " +
                "       [Order] = ?, " +
                "     OrderExpect = ?, " +
                "     LastUpdateDate = ?, " +
                "     LastUpdateBy = ? " +
                "   WHERE ReferenceID = ? " +
                "       AND OrganizationCode = ? " +
                "       AND TaskType = ? " +
                "       AND AssigneeEmpID = ? ";
        executeNonQuery(sql, new String[]{valueOf(info.NewOrder), valueOf(info.NewOrder), valueOf(info.LastUpdateDate), info.LastUpdateBy,
                info.SalePaymentPeriodID, info.OrganizationCode, info.TaskType, info.AssigneeEmpID});
    }
    /*** [END] :: Fixed - [BHPROJ-0026-3255] [LINE@02/09/2016] [Android-การจัดลำดับค่าเริ่มต้นสำหรับงานเก็บเงิน] 1. เพิ่มการใส่ "วันนัดชำระ” โดยสามารถคีย์วันนัดลงใน app ได้เลย ***/


}
