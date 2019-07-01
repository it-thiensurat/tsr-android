package th.co.thiensurat.data.info;

import th.co.bighead.utilities.BHParcelable;

public class ChannelItemInfo extends BHParcelable {

	public ChannelItemInfo() {

	}
	
//	public ChannelItemInfo(String channelItemID, String channelItemName) {
//		ChannelItemID = channelItemID;
//		ChannelItemName = channelItemName;
//	}

	public ChannelItemInfo(String channelItemID, String channelItemName,
			String channelID, String accountCode1, String accountCode2,
			String organizationCode, String channelCode, String channelName) {
		ChannelItemID = channelItemID;
		ChannelItemName = channelItemName;
		ChannelID = channelID;
		AccountCode1 = accountCode1;
		AccountCode2 = accountCode2;
		ChannelCode = channelCode;
		ChannelName = channelName;
	}

	public String ChannelItemID;
	public String ChannelItemName;
	public String ChannelID;
	public String AccountCode1;
	public String AccountCode2;
	public String OrganizationCode;
	
	//Channel
	public String ChannelCode;
	public String ChannelName;
	
	public String toString() {
		return ChannelItemName;
	}

}
