package th.co.thiensurat.data.controller;

import java.util.List;

import th.co.thiensurat.data.info.ProblemInfo;

public class ProblemController extends BaseController {

	public enum ProblemType {
		Complain, ImpoundProduct
	}

	public List<ProblemInfo> getProblemByProblemType(String organizationCode, String problemType) {
		final String QUERY_PROBLEM_BY_PROBLEMTYPE = "SELECT * FROM problem WHERE organizationCode = ? AND problemType = ? ";
		return executeQueryList(QUERY_PROBLEM_BY_PROBLEMTYPE, new String[] { organizationCode, problemType }, ProblemInfo.class);
	}

	/*** [START] :: Fixed - [BHPROJ-0025-744] :: [MoM-15/12/2015] [Android-ตรวจสอบลูกค้า] Spinner ที่แสดงปัญหา ประเภท Complain ให้กรองเฉพาะปัญหาของฝ่ายตรวจสอบ/เก็บเงิน ***/
	public List<ProblemInfo> getProblemByProblemTypeBySourceSystem(String organizationCode, String problemType, String sourceSystem) {
		final String QUERY_PROBLEM_BY_PROBLEMTYPE_BY_SOURCESYSTEM = "SELECT * FROM Problem WHERE (OrganizationCode = ?) AND (ProblemType = ?) AND (SourceSystem = ?) ";
		return executeQueryList(QUERY_PROBLEM_BY_PROBLEMTYPE_BY_SOURCESYSTEM, new String[] { organizationCode, problemType, sourceSystem }, ProblemInfo.class);
	}
	/*** [END] :: Fixed - [BHPROJ-0025-744] :: [MoM-15/12/2015] [Android-ตรวจสอบลูกค้า] Spinner ที่แสดงปัญหา ประเภท Complain ให้กรองเฉพาะปัญหาของฝ่ายตรวจสอบ/เก็บเงิน ***/

	public ProblemInfo getProblemByProblemID(String ProblemID) {
		String sql = "SELECT * FROM problem WHERE ProblemID = ?";
		return executeQueryObject(sql, new String[]{ProblemID}, ProblemInfo.class);
	}

	public void addProblem(ProblemInfo info) {
		String sql = "insert into Problem([ProblemID],[OrganizationCode],[ProblemName],[ProblemType],[CreateDate],[CreateBy],[LastUpdateDate],[LastUpdateBy],[SourceSystem])"
				+" values(?,?,?,?,?,?,?,?,?)";
		executeNonQuery(sql, new String[] {info.ProblemID, info.OrganizationCode, info.ProblemName, info.ProblemType, valueOf(info.CreateDate), info.CreateBy, valueOf(info.LastUpdateDate), info.LastUpdateBy, info.SourceSystem});		
	}
	
	public void deleteProblem() {
		executeNonQuery("delete from Problem", null);
	}

}
