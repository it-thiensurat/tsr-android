package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.SuggestionInfo;

public class SuggestionController extends BaseController {

	private static final String QUERY_SUGGEDTION_GET_ALL = "SELECT * FROM Suggestion";

	public List<SuggestionInfo> getSuggestion() {
		return executeQueryList(QUERY_SUGGEDTION_GET_ALL, null, SuggestionInfo.class);
	}

    public SuggestionInfo getSuggestionBySuggestionCode (String SuggestionCode ) {
        SuggestionInfo ret = null;
        String sql = "SELECT * FROM Suggestion where SuggestionCode = ?";
        ret = executeQueryObject(sql, new String[]{SuggestionCode}, SuggestionInfo.class);
        return ret;
    }

	public void addSuggestion(SuggestionInfo info) {
		String sql = "insert into Suggestion(SuggestionCode, SuggestionName)"
				+" values(?,?)";
		executeNonQuery(sql, new String[] {info.SuggestionCode, info.SuggestionName});
	}
	
	public void deleteSuggestion() {
		executeNonQuery("delete from Suggestion", null);
	}
}
