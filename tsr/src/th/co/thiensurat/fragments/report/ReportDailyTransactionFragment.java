package th.co.thiensurat.fragments.report;

import java.util.ArrayList;
import java.util.List;

import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.data.info.ChannelItemInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


/********************************************************
 *
 *
 * [Checked@03/02/2016] ไม่ได้ใช้งาน File นี้แล้ว
 *
 *
 *****************************************************/


public class ReportDailyTransactionFragment extends BHFragment{
	
	private List<DailyTransactionReportInfo> dailyTransList;
	
	@InjectView
	private ListView lvDailyTransReport;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_report_daily_transaction;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back};
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		getDailyReport();
	}
	
	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_back:
			showLastView();
			break;
		
		default:
			break;
		}
	}
	
	private void getDailyReport() {
		makeData();
		bindDailyTransReport();
	}
	
	private void bindDailyTransReport() {

		/*
		 LayoutInflater inflater = activity.getLayoutInflater();
		ViewGroup header = (ViewGroup) inflater.inflate(
				R.layout.list_sendmoney_summary_not_send_list_header,
				lvSumPaymentDate, false);
		lvSumPaymentDate.addHeaderView(header, null, false);

		BHArrayAdapter<PaymentInfo> adapter = new BHArrayAdapter<PaymentInfo>(
				activity, R.layout.list_sendmoney_summary_not_send_list_item,
				mPaymentList) {

			class ViewHolder {
				public TextView txtPayDate;
				public TextView txtPAYAMT;
			}

			@Override
			protected void onViewItem(final int position, View view,
					Object holder, PaymentInfo info) {
				// TODO Auto-generated method stub

				ViewHolder vh = (ViewHolder) holder;
				try {
					String strPayDate = BHUtilities.dateFormat(info.PayDate,
							"dd/MM/yyyy");
					vh.txtPayDate.setText(strPayDate);
					vh.txtPAYAMT.setText(BHUtilities
							.numericFormat(info.PAYAMT));

				} catch (Exception e) {
					// e.printStackTrace();
					Log.e(this.getClass().getName(), e.getMessage());
				}
			}
		};
		lvSumPaymentDate.setAdapter(adapter);
		 */
		BHArrayAdapter<DailyTransactionReportInfo> adapter = new BHArrayAdapter<ReportDailyTransactionFragment.DailyTransactionReportInfo>(activity, R.layout.list_report_daily_transaction_item, dailyTransList) {

			class ViewHolder {
				public TextView txtDepartment;
				public TextView txtSubDepartment;
				public TextView txtSupervisor;
				public TextView txtTeamCode;
				public TextView txtEmpName;
			}
			
			@Override
			protected void onViewItem(int position, View view, Object holder,
					DailyTransactionReportInfo info) {
				// TODO Auto-generated method stub
				ViewHolder vh = (ViewHolder) holder;
				try {
										
					vh.txtDepartment.setText(info.DepartmentName);
					vh.txtSubDepartment.setText(info.SubDepartmentName);
					vh.txtSupervisor.setText(info.Supervisor);
					vh.txtTeamCode.setText(info.TeamCode);
					vh.txtEmpName.setText(info.EmpName);
					
				} catch (Exception e) {					
					e.printStackTrace();					
				}
			}
		};
		lvDailyTransReport.setAdapter(adapter);
	}

	private void makeData() {
		//dailyTransList = new 
		//ArrayList<ChannelItemInfo> headOfficeList = new ArrayList<ChannelItemInfo>();
		dailyTransList = new ArrayList<DailyTransactionReportInfo>();
		
		DailyTransactionReportInfo info;
		
		info = new DailyTransactionReportInfo("ขาย","P", "PAK", "PAK23", "Sale A");
		dailyTransList.add(info);		
		info = new DailyTransactionReportInfo("ขาย","P", "PAK", "PAK23", "Sale B");
		dailyTransList.add(info);		
		info = new DailyTransactionReportInfo("ขาย","P", "PAK", "PAK23", "Sale C");
		dailyTransList.add(info);	
		info = new DailyTransactionReportInfo("ขาย","P", "PAK", "PAK23", "Sale D");
		dailyTransList.add(info);
		
		info = new DailyTransactionReportInfo("ขาย","P", "PAK", "PAK24", "Sale E");
		dailyTransList.add(info);
		info = new DailyTransactionReportInfo("ขาย","P", "PAK", "PAK24", "Sale F");
		dailyTransList.add(info);
		info = new DailyTransactionReportInfo("ขาย","P", "PAK", "PAK24", "Sale G");
		dailyTransList.add(info);	
		info = new DailyTransactionReportInfo("ขาย","P", "PAK", "PAK24", "Sale H");
		dailyTransList.add(info);
		
		info = new DailyTransactionReportInfo("ขาย","P", "PBB", "PBB01", "Sale A");
		dailyTransList.add(info);		
		info = new DailyTransactionReportInfo("ขาย","P", "PBB", "PBB01", "Sale B");
		dailyTransList.add(info);		
		info = new DailyTransactionReportInfo("ขาย","P", "PBB", "PBB01", "Sale C");
		dailyTransList.add(info);	
		info = new DailyTransactionReportInfo("ขาย","P", "PBB", "PBB01", "Sale D");
		dailyTransList.add(info);
		
		info = new DailyTransactionReportInfo("ขาย","P", "PBB", "PBB02", "Sale E");
		dailyTransList.add(info);
		info = new DailyTransactionReportInfo("ขาย","P", "PBB", "PBB02", "Sale F");
		dailyTransList.add(info);
		info = new DailyTransactionReportInfo("ขาย","P", "PBB", "PBB02", "Sale G");
		dailyTransList.add(info);	
		info = new DailyTransactionReportInfo("ขาย","P", "PBB", "PBB02", "Sale H");
		dailyTransList.add(info);
	}
	
	public class DailyTransactionReportInfo {
		public String DepartmentName;
		public String SubDepartmentName;
		public String Supervisor;
		public String TeamCode;
		public String EmpName;
		
		public DailyTransactionReportInfo() {
			
		}
		
		public DailyTransactionReportInfo(String departmentName, String subDepartmentName, String supervisor, String teamCode, String empName) {
			DepartmentName = departmentName;
			SubDepartmentName = subDepartmentName;
			Supervisor = supervisor;
			TeamCode = teamCode;
			EmpName = empName;
		}
	}
}
