package th.co.thiensurat.data.controller;

import java.security.acl.LastOwnerException;
import java.util.List;

import android.R.bool;
import th.co.thiensurat.data.info.BankInfo;
import th.co.thiensurat.data.info.DiscountLimitInfo;
import th.co.thiensurat.data.info.ProductInfo;

public class DiscountLimitController extends BaseController {

	final String QUERY_DISCOUNT_LIMIT_GET_ALL = "SELECT * FROM DiscountLimit";
	final String QUERY_DISCOUNT_LIMIT_GET_BY_TYPE = "SELECT * FROM DiscountLimit WHERE DiscountType = ?";

	public List<DiscountLimitInfo> getDiscountLimits() {
		return executeQueryList(QUERY_DISCOUNT_LIMIT_GET_ALL, null, DiscountLimitInfo.class);
	}
	
	public DiscountLimitInfo getDiscountLimit(boolean isCash) {
		String discountType = isCash ? "Cash" : "Credit";
		return executeQueryObject(QUERY_DISCOUNT_LIMIT_GET_BY_TYPE, new String[] { discountType }, DiscountLimitInfo.class);			
	}

    public DiscountLimitInfo getDiscountLimitByTypeAndProductID(String DiscountType, String ProductID, float DiscountPrice) {
        String sql = "SELECT * FROM DiscountLimit WHERE DiscountType = ? and ProductID = ? and DiscountPrice = ?";
        return executeQueryObject(sql, new String[]{DiscountType, ProductID, valueOf(DiscountPrice)}, DiscountLimitInfo.class);
    }

	public void deleteDiscountLimitAll() {
		executeNonQuery("DELETE FROM DiscountLimit", null);
	}
	
	public void addDiscountLimit(DiscountLimitInfo info)
	{
//		final String QUERY_ADD_DISCOUNT_LIMIT = "insert into DiscountLimit (DiscountLimitID, DiscountType, DiscountPrice, ProductID, CreateDate, CreateBy, LastUpdateDate, LastUpdateBy) values (?, ?, ?, ?, ?, ?, ?, ?)";
//		executeNonQuery(QUERY_ADD_DISCOUNT_LIMIT, new String[] {info.DiscountLimitID, info.DiscountType, valueOf(info.DiscountPrice), info.ProductID, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy});
		final String QUERY_ADD_DISCOUNT_LIMIT = "insert into DiscountLimit (DiscountLimitID, DiscountType, DiscountPrice, ProductID) values (?, ?, ?, ?)";
		executeNonQuery(QUERY_ADD_DISCOUNT_LIMIT, new String[] {info.DiscountLimitID, info.DiscountType, valueOf(info.DiscountPrice), info.ProductID});
		
	}
}
