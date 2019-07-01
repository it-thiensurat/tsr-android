package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.PackageInfo;

public class PackageController extends BaseController {

    public List<PackageInfo> getPackage() {
        return executeQueryList("select *from Package", null, PackageInfo.class);
    }

    public List<PackageInfo> getPackage(String organizationCode, String ProductID) {
        final String QUERY_PACKAGE_GET_BY_ID = "" +
                " SELECT distinct Package.* " +
                " FROM Package " +
                " inner join PackagePeriodDetail on Package.Model = PackagePeriodDetail.Model " +
                " WHERE Package.organizationCode = ? AND Package.ProductID = ? ORDER BY PackageCode ASC";
        return executeQueryList(QUERY_PACKAGE_GET_BY_ID, new String[]{organizationCode, ProductID}, PackageInfo.class);
    }

    public List<PackageInfo> getPackageForChangeContract(String organizationCode, String ProductID, int Mode) {
        String QUERY_PACKAGE_GET_BY_ID = "" +
                " SELECT DISTINCT p.* " +
                " FROM Package p" +
                " INNER JOIN (" +
                "       SELECT COUNT(*) AS Mode, pd.Model" +
                "       FROM PackagePeriodDetail pd" +
                "       GROUP BY pd.Model";
        if (Mode == 1) {
            // case 01 เปลี่ยนจากเงินสดเป็นเงินผ่อน จะแสดงแต่ package ชำระเงินผ่อน ของ productID นั้นๆ :: MODE 1
            QUERY_PACKAGE_GET_BY_ID +=
                "       HAVING COUNT(*) > 1";
        } else  if (Mode > 1) {
            // case 02 เปลี่ยนจากเงินผ่อนเป็นเงินสด จะแสดงแต่ package ชำระเงินสด ของ productID นั้นๆ :: MODE 2
            QUERY_PACKAGE_GET_BY_ID +=
                "       HAVING COUNT(*) = 1";
        }
        QUERY_PACKAGE_GET_BY_ID +=
                " ) pd on pd.Model = p.PackageCode" +
                " WHERE p.organizationCode = ?" +
                //" AND p.Status = ? " +
                " AND p.ProductID = ?" +
                " ORDER BY p.PackageCode ASC";
        return executeQueryList(QUERY_PACKAGE_GET_BY_ID, new String[]{ organizationCode, ProductID}, PackageInfo.class);
    }

    public List<PackageInfo> getPackageForChangeContractForCase04(String organizationCode, String ProductID, int Mode, String Model) {
        String QUERY_PACKAGE_GET_BY_ID = "" +
                " SELECT DISTINCT p.* " +
                " FROM Package p" +
                " INNER JOIN (" +
                "       SELECT COUNT(*) AS Mode, pd.Model" +
                "       FROM PackagePeriodDetail pd" +
                "       GROUP BY pd.Model";
        if (Mode == 1) {
            // case 01 เปลี่ยนจากเงินสดเป็นเงินผ่อน จะแสดงแต่ package ชำระเงินผ่อน ของ productID นั้นๆ :: MODE 1
            QUERY_PACKAGE_GET_BY_ID +=
                    "       HAVING COUNT(*) = 1";
        } else  if (Mode > 1) {
            // case 02 เปลี่ยนจากเงินผ่อนเป็นเงินสด จะแสดงแต่ package ชำระเงินสด ของ productID นั้นๆ :: MODE 2
            QUERY_PACKAGE_GET_BY_ID +=
                    "       HAVING COUNT(*) > 1";
        }
        QUERY_PACKAGE_GET_BY_ID +=
                " ) pd on pd.Model = p.PackageCode" +
                        " WHERE p.organizationCode = ?" +
                        //" AND p.Status = ? " +
                        " AND p.ProductID = ? AND p.Model <> ?" +
                        " ORDER BY p.PackageCode ASC";
        return executeQueryList(QUERY_PACKAGE_GET_BY_ID, new String[]{ organizationCode, ProductID, Model}, PackageInfo.class);
    }

    public void deleteAllPackage() {
        String sql = "delete from Package";
        executeNonQuery(sql, new String[]{});
    }

    public void insertAllPackage(List<PackageInfo> packageList) {
        String sql = "insert into Package (Model, OrganizationCode, ProductID, PackageCode, PackageTitle, TotalPrice) "
                + "values (?,?,?,?,?,?)";
        //SyncedDate
        //datetime('now')

        for (int i = 0; i < packageList.size(); i++) {

            executeNonQuery(sql, new String[]{packageList.get(i).Model, packageList.get(i).OrganizationCode, packageList.get(i).ProductID,
                    packageList.get(i).PackageCode, packageList.get(i).PackageTitle,
                    Float.toString(packageList.get(i).TotalPrice)});
        }
    }
}
