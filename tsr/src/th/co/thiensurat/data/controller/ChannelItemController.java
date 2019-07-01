package th.co.thiensurat.data.controller;

import th.co.thiensurat.data.info.BankInfo;
import th.co.thiensurat.data.info.ChannelItemInfo;

public class ChannelItemController extends BaseController {

	public void addChannelItem(ChannelItemInfo info) {
		String sql = "insert into ChannelItem(ChannelItemID, ChannelItemName, ChannelID, AccountCode1, AccountCode2, OrganizationCode)"
				+" values(?,?,?,?,?,?)";
		executeNonQuery(sql, new String[] {info.ChannelItemID, info.ChannelItemName, info.ChannelID, info.AccountCode1, info.AccountCode2, info.OrganizationCode});
	}
	
	public void deleteChannelItem() {
		executeNonQuery("delete from ChannelItem", null);
	}

	public ChannelItemInfo getChannelItemByChannelItemID (String OrganizationCode, String ChannelItemID) {
		String sql=" SELECT ci.*, c.ChannelCode, c.ChannelName "
				+" FROM ChannelItem as ci inner join Channel as c on c.ChannelID = ci.ChannelID"
				+" where ci.OrganizationCode = ? and ci.ChannelItemID =  ? ";
		return executeQueryObject(sql, new String[] {OrganizationCode, ChannelItemID}, ChannelItemInfo.class);
	}
}
