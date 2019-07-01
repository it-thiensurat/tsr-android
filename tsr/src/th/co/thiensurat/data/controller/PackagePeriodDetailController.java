package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.PackagePeriodDetailInfo;

public class PackagePeriodDetailController extends BaseController {

    private static final String QUERY_PACKAGEPREIODDETAIL_GET_BY_ID = "SELECT * FROM PackagePeriodDetail WHERE organizationCode = ? AND Model = ? ORDER BY PaymentPeriodNumber ASC";
    private static final String QUERY_PACKAGEPREIODDETAIL_GET_PAYMENT = "SELECT * FROM PackagePeriodDetail WHERE organizationCode = ? AND Model = ? AND PaymentPeriodNumber = ?";

    public List<PackagePeriodDetailInfo> getPackagePeriodDetail(String organizationCode, String Model) {
        return executeQueryList(QUERY_PACKAGEPREIODDETAIL_GET_BY_ID, new String[]{organizationCode, Model}, PackagePeriodDetailInfo.class);
    }

    public PackagePeriodDetailInfo getPackagePeriodDetailPayment(String OrganizationCode, String Model, int paymentPeriodNumber) {
        return executeQueryObject(QUERY_PACKAGEPREIODDETAIL_GET_PAYMENT, new String[]{OrganizationCode, Model, valueOf(paymentPeriodNumber)},
                PackagePeriodDetailInfo.class);
    }

    public PackagePeriodDetailInfo getMaxPackagePeriodDetailByModel(String organizationCode, String model) {
        PackagePeriodDetailInfo ret = null;
        String sql = "SELECT Model, MAX(PaymentPeriodNumber) as PaymentPeriodNumber, SUM(PaymentAmount) as PaymentAmount" + " FROM PackagePeriodDetail"
                + " WHERE PaymentPeriodNumber <> '1' and organizationCode =?" + " AND Model = ?" + " group by Model";
        ret = executeQueryObject(sql, new String[]{organizationCode, model}, PackagePeriodDetailInfo.class);
        return ret;
    }

    public void deleteAllPackagePeriodDetail() {
        String sql = "delete from PackagePeriodDetail";
        executeNonQuery(sql, new String[]{});
    }

    public void insertAllPackagePeriodDetail(List<PackagePeriodDetailInfo> packagePeriodDetailList) {
        String sql = "insert into PackagePeriodDetail (PaymentPeriodDetailID, Model, OrganizationCode, PaymentPeriodNumber, PaymentAmount, CloseDiscountAmount) "
                + "values(?,?,?,?,?,?)";

        //SyncedDate
        //datetime('now')

        for (int i = 0; i < packagePeriodDetailList.size(); i++) {
            executeNonQuery(sql, new String[]{packagePeriodDetailList.get(i).PaymentPeriodDetailID, packagePeriodDetailList.get(i).Model, packagePeriodDetailList.get(i).OrganizationCode,
                    Integer.toString(packagePeriodDetailList.get(i).PaymentPeriodNumber), Float.toString(packagePeriodDetailList.get(i).PaymentAmount), Float.toString(packagePeriodDetailList.get(i).CloseDiscountAmount)});
        }
    }

    public List<PackagePeriodDetailInfo> getPackagePeriodDetail() {
        return executeQueryList("select *from PackagePeriodDetail", null, PackagePeriodDetailInfo.class);
    }

}
