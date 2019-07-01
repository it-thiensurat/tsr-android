package th.co.thiensurat.service.data;

import java.util.Date;

import th.co.bighead.utilities.BHParcelable;
import th.co.thiensurat.data.info.ProblemContractInfo;

public class UpdateProblemContractInputInfo extends BHParcelable {

	public String ProblemContract;
	public String RefNo;
	public String CONTNO;
	public String SalePaymentPeriodID;
	public int PaymentPeriodNumber;
	public Date ReceivedProblemDate;
	public String DataSourceSystem;
	public String Status;
	public String ProblemCode;
	public String ProbemDetail;
	public String AssignedEmployee;
	public Date CreateDate;
	public String CreateBy;
	public Date LastUpdateDate;
	public String LastUpdateBy;
	public Date SyncedDate;

	public static UpdateProblemContractInputInfo from(ProblemContractInfo problemContract) {
		UpdateProblemContractInputInfo info = new UpdateProblemContractInputInfo();

		info.ProblemContract = problemContract.ProblemContract;
		info.RefNo = problemContract.RefNo;
		info.CONTNO = problemContract.CONTNO;
		info.SalePaymentPeriodID = problemContract.SalePaymentPeriodID;
		info.PaymentPeriodNumber = problemContract.PaymentPeriodNumber;
		info.ReceivedProblemDate = problemContract.ReceivedProblemDate;
		info.DataSourceSystem = problemContract.DataSourceSystem;
		info.Status = problemContract.Status;
		info.ProblemCode = problemContract.ProblemCode;
		info.ProbemDetail = problemContract.ProbemDetail;
		info.AssignedEmployee = problemContract.AssignedEmployee;
		info.CreateDate = problemContract.CreateDate;
		info.CreateBy = problemContract.CreateBy;
		info.LastUpdateDate = problemContract.LastUpdateDate;
		info.LastUpdateBy = problemContract.LastUpdateBy;
		info.SyncedDate = problemContract.SyncedDate;

		return info;
	}
}
