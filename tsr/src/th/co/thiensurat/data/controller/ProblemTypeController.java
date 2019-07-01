package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ProblemTypeInfo;

public class ProblemTypeController extends BaseController {


	public List<ProblemTypeInfo> getProblemTypeAll() {
		final String QUERY_ALL_PROBLEMTYPE = "SELECT ProblemTypeID, ProblemCode AS ProblemTypeCode, ProblemName AS ProblemTypeName FROM ProblemType";
		return executeQueryList(QUERY_ALL_PROBLEMTYPE, null, ProblemTypeInfo.class);
	}
	
	public void addProblemType(ProblemTypeInfo info) {
		String sql = "INSERT INTO ProblemType(ProblemTypeID, ProblemCode, ProblemName) VALUES(?,?,?)";
		executeNonQuery(sql, new String[] {info.ProblemTypeID, info.ProblemTypeCode, info.ProblemTypeName});		
	}
	
	public void deleteProblemTypeAll() {
		executeNonQuery("DELETE FROM ProblemType", null);
	}

}
