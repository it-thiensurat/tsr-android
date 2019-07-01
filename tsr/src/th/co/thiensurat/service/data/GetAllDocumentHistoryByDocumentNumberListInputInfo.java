package th.co.thiensurat.service.data;

import java.util.List;

import th.co.bighead.utilities.BHParcelable;

public class GetAllDocumentHistoryByDocumentNumberListInputInfo extends BHParcelable {
    //public List<String> documentNumberList;
    public String[] documentNumberList;
    public String TeamCode;
    //public ContractInfo[] contractList;

    public void setContractListValue(List<String> _docNumList) {
        String[] ar = new String[_docNumList.size()];
        int _index = 0;
        for(String cInfo : _docNumList)
        {
            ar[_index] = cInfo;
            _index++;
        }
        documentNumberList = ar;
    }
}
