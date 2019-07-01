package th.co.thiensurat.data.info;

import th.co.bighead.utilities.BHParcelable;

public class ChannelInfo extends BHParcelable {

	public ChannelInfo() {

	}

	public ChannelInfo(String channelID, String channelCode, String channelName) {
		ChannelID = channelID;
		ChannelCode = channelCode;
		ChannelName = channelName;
	}

	public String ChannelID;
	public String ChannelCode;
	public String ChannelName;
}
