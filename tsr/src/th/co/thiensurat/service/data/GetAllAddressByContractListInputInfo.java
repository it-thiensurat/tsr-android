package th.co.thiensurat.service.data;

import java.util.List;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ContractInfo;

public class GetAllAddressByContractListInputInfo extends BHParcelable{
public ContractInfo[] contractList;
	
	public void setContractListValue(List<ContractInfo> _contractList) {
		ContractInfo[] ar = new ContractInfo[_contractList.size()];
		int _index = 0;
		for(ContractInfo cInfo : _contractList)
		{
			ar[_index] = new ContractInfo();
			ar[_index].RefNo = cInfo.RefNo;
			_index++;
		}
		contractList = ar;
	}
}
