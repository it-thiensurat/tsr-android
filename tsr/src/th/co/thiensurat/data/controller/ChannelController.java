package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ChannelInfo;
import th.co.thiensurat.data.info.ChannelItemInfo;

public class ChannelController extends BaseController {

	public enum ChannelCode {
		HeadOffice, Bank, CounterService;
	}

	public List<ChannelItemInfo> getChannelByChannelCode(
			String organizationCode, String channelCode) {
		List<ChannelItemInfo> ret;		
		String sql="SELECT c.ChannelID, c.ChannelCode, c.ChannelName," 
				+" ci.ChannelItemID, ci.ChannelItemName, ci.AccountCode1, ci.AccountCode2, ci.OrganizationCode"
				+" FROM Channel as c inner join ChannelItem as ci on c.ChannelID=ci.ChannelID"
				+" where ci.OrganizationCode=? and c.ChannelCode like ? Order By ci.ChannelItemName"; 
		ret = executeQueryList(sql, new String[] { organizationCode,
				channelCode }, ChannelItemInfo.class);
		return ret;
	}
	
	public void addChannel(ChannelInfo info) {
		String sql = "insert into Channel(ChannelID, ChannelCode, ChannelName)"
				    +" values(?,?,?)";
		executeNonQuery(sql, new String[] {info.ChannelID, info.ChannelCode, info.ChannelName});
	}

	public void deleteChannel() {
		executeNonQuery("delete from Channel", null);		
	}
}
