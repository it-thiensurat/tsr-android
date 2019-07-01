package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.TradeInBrandInfo;

public class TradeInBrandController extends BaseController {

	public List<TradeInBrandInfo> getTradeInBrand() {
        String sql;
        sql = "SELECT * FROM TradeInBrand ORDER BY TradeInBrandName ASC";
		return executeQueryList(sql, null, TradeInBrandInfo.class);
	}

    public TradeInBrandInfo getTradeInBrandByTradeInBrandCode(String tradeInBrandCode) {
        String sql;
        sql = "SELECT * FROM TradeInBrand WHERE TradeInBrandCode=? ORDER BY TradeInBrandName ASC";
        return executeQueryObject(sql, new String[]{tradeInBrandCode}, TradeInBrandInfo.class);
    }


	public void addTradeInBrand(TradeInBrandInfo info) {
		String sql = "insert into TradeInBrand([TradeInBrandCode],[TradeInBrandName])"
				+" values(?,?)";
		executeNonQuery(sql, new String[] {info.TradeInBrandCode, info.TradeInBrandName});
	}
	
	public void deleteTradeInBrand() {
		executeNonQuery("delete from TradeInBrand", null);
	}

}
