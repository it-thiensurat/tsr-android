package th.co.thiensurat.data.info;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHParcelable;

public class ContractListInfo extends BHParcelable{
	public List<ContractInfo> contractList; 
	public ContractListInfo() { 
		contractList = new ArrayList<ContractInfo>();
	} 
}
