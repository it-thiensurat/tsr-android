package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.PersonTypeCardInfo;

public class DebtorCustomerController extends BaseController {

    public static enum PersonType {
        Person("0"), Corporation("1"), Foreigners("2");
        private String value;

        private PersonType(String val) {
            this.value = val;
        }

        @Override
        public String toString() {
            return value;
        }
    }

    public void addDebtorCustomer(DebtorCustomerInfo info) {
        String sql = "INSERT INTO DebtorCustomer(CustomerID, OrganizationCode, PrefixCode, PrefixName, CustomerName, CustomerType, IDCardType, IDCard, "
                + "CompanyName, AuthorizedName, AuthorizedIDCard, Brithday, Sex, DebtStatus, HabitatTypeCode, HabitatDetail, OccupyType, CareerCode, "
                + "CareerDetail, HobbyCode, HobbyDetail, IsUsedProduct, UsedProductModelID, SuggestionCode, SuggestionDetail, "
                + "CreateBy, CreateDate, LastUpdateBy, LastUpdateDate, SyncedDate)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        executeNonQuery(sql, new String[]{info.CustomerID, info.OrganizationCode, info.PrefixCode, info.PrefixName, info.CustomerName, info.CustomerType,
                info.IDCardType, info.IDCard, info.CompanyName, info.AuthorizedName, info.AuthorizedIDCard, valueOf(info.Brithday), info.Sex,
                valueOf(info.DebtStatus), info.HabitatTypeCode, info.HabitatDetail, info.OccupyType, info.CareerCode, info.CareerDetail, info.HobbyCode, info.HobbyDetail,
                valueOf(info.IsUsedProduct), info.UsedProductModelID, info.SuggestionCode, info.SuggestionDetail, info.CreateBy, valueOf(info.CreateDate),
                info.LastUpdateBy, valueOf(info.LastUpdateDate), valueOf(info.SyncedDate)});
    }

    public void updateDebtorCustomer(DebtorCustomerInfo info) {
        String sql = "UPDATE DebtorCustomer SET OrganizationCode=?, PrefixCode=?, PrefixName=?, CustomerName=?, CustomerType=?, IDCardType=?,"
                + " IDCard=?, CompanyName=?, AuthorizedName=?, AuthorizedIDCard=?, Brithday=?, Sex=?, DebtStatus=?, LastUpdateBy=?, LastUpdateDate=?,"
                + " HabitatTypeCode=?, HabitatDetail=?, OccupyType=?, CareerCode=?, CareerDetail=?, HobbyCode=?, HobbyDetail=?, IsUsedProduct=?, UsedProductModelID=?, SuggestionCode=?, SuggestionDetail=?"
                + " WHERE CustomerID=?";
        executeNonQuery(sql, new String[]{info.OrganizationCode, info.PrefixCode, info.PrefixName, info.CustomerName, info.CustomerType, info.IDCardType,
                info.IDCard, info.CompanyName, info.AuthorizedName, info.AuthorizedIDCard, valueOf(info.Brithday), info.Sex, valueOf(info.DebtStatus),
                info.LastUpdateBy, valueOf(info.LastUpdateDate), info.HabitatTypeCode, info.HabitatDetail, info.OccupyType, info.CareerCode, info.CareerDetail,
                info.HobbyCode, info.HobbyDetail, valueOf(info.IsUsedProduct), info.UsedProductModelID, info.SuggestionCode, info.SuggestionDetail, info.CustomerID});
    }

    public void updateDebtorCustomerTest(DebtorCustomerInfo info) {
        String sql = "UPDATE DebtorCustomer SET CustomerName=? WHERE CustomerID=?";
        executeNonQuery(sql, new String[]{info.CustomerName, info.CustomerID});
    }

    // String QUERY_DEBTORCUSTOMER_GET_BY_ID =
    // "SELECT * FROM DebtorCustomer WHERE CustomerID = ?";
    public DebtorCustomerInfo getDebtorCustomer(String customerID) {
        DebtorCustomerInfo ret = null;
        String sql = "SELECT DebtorCustomer.*," + " CASE WHEN DebtorCustomer.CustomerType= '0' THEN 'บุคคลธรรมดา'"
                + " WHEN DebtorCustomer.CustomerType= '1' THEN 'นิติบุคคล'" + " WHEN DebtorCustomer.CustomerType= '2' THEN 'บุคคลต่างชาติ'"
                + " ELSE '' END AS CustomerTypeName," + " case when DebtorCustomer.IDCardType='" + PersonTypeCardInfo.PersonTypeCardEnum.PASSPORT.toString() + "' then 'หนังสือเดินทาง'"
                + " when DebtorCustomer.IDCardType='" + PersonTypeCardInfo.PersonTypeCardEnum.OUTLANDER.toString() + "' then 'บัตรต่างด้าว'" + "when DebtorCustomer.IDCardType='" + PersonTypeCardInfo.PersonTypeCardEnum.IDCARD.toString() + "' then 'บัตรประชาชน' " +
                "when DebtorCustomer.IDCardType='" + PersonTypeCardInfo.PersonTypeCardEnum.DRIVINGLICENSE.toString() + "' then 'ใบขับขี่' " +
                "when DebtorCustomer.IDCardType='" + PersonTypeCardInfo.PersonTypeCardEnum.OFFICIALCARD.toString() + "' then 'บัตรข้าราชการ'"
                + " else '' end as IDCardTypeName"
                + " FROM DebtorCustomer WHERE CustomerID = ?";
        ret = executeQueryObject(sql, new String[]{customerID}, DebtorCustomerInfo.class);
        return ret;
    }

    public DebtorCustomerInfo getDebtorCustomerIDCardTypeName(DebtorCustomerInfo customer) {
        if(customer.CustomerType != null){
            switch (customer.CustomerType){
                case "0" : customer.CustomerTypeName = "บุคคลธรรมดา"; break;
                case "1" : customer.CustomerTypeName = "นิติบุคคล"; break;
                case "2" : customer.CustomerTypeName = "บุคคลต่างชาติ"; break;
                default:  customer.CustomerTypeName = "";  break;
            }
        }
        if(customer.IDCardType != null) {
            if (customer.IDCardType.equals(PersonTypeCardInfo.PersonTypeCardEnum.PASSPORT.toString())) {
                customer.IDCardTypeName = "หนังสือเดินทาง";
            } else if (customer.IDCardType.equals(PersonTypeCardInfo.PersonTypeCardEnum.OUTLANDER.toString())) {
                customer.IDCardTypeName = "บัตรต่างด้าว";
            }
            else if (customer.IDCardType.equals(PersonTypeCardInfo.PersonTypeCardEnum.IDCARD.toString())) {
                customer.IDCardTypeName = "บัตรประชาชน";
            }
            else if (customer.IDCardType.equals(PersonTypeCardInfo.PersonTypeCardEnum.DRIVINGLICENSE.toString())) {
                customer.IDCardTypeName = "ใบขับขี่";
            }
            else if (customer.IDCardType.equals(PersonTypeCardInfo.PersonTypeCardEnum.OFFICIALCARD.toString())) {
                customer.IDCardTypeName = "บัตรข้าราชการ";
            } else {
                customer.IDCardTypeName = "";
            }
        }
        return customer;
    }

    // public DebtorCustomerInfo getidcardDecustomer(String IDCard) {
    // return executeQueryObject(QUERY_DEBTORCUSTOMER_IDCARD,
    // new String[] { IDCard }, DebtorCustomerInfo.class);
    // }

    public List<DebtorCustomerInfo> getidcardDecustomer(String IDCard, String personType, String personTypeCard) {
        String sql = null;
        if(personTypeCard != null){ //บุคคล, ต่างชาติ
            sql = "SELECT DebtorCustomer.*,Contract.RefNo,Contract.CustomerID,Contract.ProductSerialNumber,Contract.EFFDATE,Contract.STATUS FROM  DebtorCustomer "
                    + " INNER JOIN Contract on DebtorCustomer.CustomerID = Contract.CustomerID and Contract.torefno IS NULL "
                    + " WHERE replace(DebtorCustomer.IDCard, '-', '') = replace(?, '-', '') and DebtorCustomer.CustomerType = ? and UPPER(DebtorCustomer.IDCardType) = UPPER(?)";
            return executeQueryList(sql, new String[]{IDCard, personType, personTypeCard}, DebtorCustomerInfo.class);
        } else {  //นิติบุคคล
            sql = "SELECT DebtorCustomer.*,Contract.RefNo,Contract.CustomerID,Contract.ProductSerialNumber,Contract.EFFDATE,Contract.STATUS FROM  DebtorCustomer "
                    + " INNER JOIN Contract on DebtorCustomer.CustomerID = Contract.CustomerID and Contract.torefno IS NULL "
                    + " WHERE replace(DebtorCustomer.IDCard, '-', '') = replace(?, '-', '') and DebtorCustomer.CustomerType = ? and DebtorCustomer.IDCardType IS NULL";
            return executeQueryList(sql, new String[]{IDCard, personType}, DebtorCustomerInfo.class);
        }
    }

    // public DebtorCustomerInfo getDebCustometByID(String CustomerID) {
    // String sql = "SELECT * FROM DebtorCustomer WHERE (CustomerID = ?)";
    // executeNonQuery(sql, new String[] { CustomerID });
    // }

    public DebtorCustomerInfo getDebCustometByID(String CustomerID) {
        final String QUERY_DEBCUSTOMER_CUSTOMERID = "SELECT * FROM DebtorCustomer WHERE CustomerID = ?";
        return executeQueryObject(QUERY_DEBCUSTOMER_CUSTOMERID, new String[]{CustomerID}, DebtorCustomerInfo.class);
    }

    public List<DebtorCustomerInfo> getDebtorCustomer() {
        return executeQueryList("select *from DebtorCustomer", null, DebtorCustomerInfo.class);
    }

    /*** [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/
    public void updateReferencePerson(DebtorCustomerInfo info) {
        String sql = "UPDATE DebtorCustomer "
                + " SET     ReferencePersonName = ? "
                + "         , ReferencePersonTelephone = ?"
                + "         , LastUpdateDate = ?"
                + "         , LastUpdateBy = ?"
                + " WHERE (OrganizationCode = ?) AND (CustomerID = ?)";
        executeNonQuery(sql, new String[]{ info.ReferencePersonName, info.ReferencePersonTelephone, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.OrganizationCode, info.CustomerID});
    }
    /*** [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/

    //////////////////////////////////////////////////////////////////////////////////////////////////////
    public List<DebtorCustomerInfo> getDetailDecustomer(String CustomerID) {
        String sql = "select * from DebtorCustomer" +
                "INNER JOIN Contract on DebtorCustomer.CustomerID = Contract.CustomerID" +
                "INNER JOIN Address on Contract.RefNo = Address.RefNo" +
                "where DebtorCustomer.CustomerID = ?";
        return executeQueryList(sql, new String[]{CustomerID}, DebtorCustomerInfo.class);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void deleteDebtorCustomerAll() {
        String sql = "delete from DebtorCustomer";
        executeNonQuery(sql, null);
    }
    public void deleteDebcustomerByCustomerID(String CustomerID) {
        String sql = "DELETE FROM DebtorCustomer WHERE (CustomerID = ?)";
        executeNonQuery(sql, new String[]{CustomerID});
    }
    public void deleteDebcustomerByRefNo(String refNo) {
        String sql = "DELETE FROM DebtorCustomer WHERE (CustomerID IN (SELECT CustomerID FROM Contract WHERE (RefNo = ?)) )";
        executeNonQuery(sql, new String[]{refNo});
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////

}