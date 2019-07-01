package th.co.thiensurat.fragments.payment.first;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.AssignController.AssignTaskType;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.AddressInfo.AddressType;
import th.co.thiensurat.data.info.AssignInfo;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class FirstPaymentEmployeeAssignFragment extends BHFragment {

	private List<EmployeeInfo> mEmployeeList;
	@InjectView
	private TextView txtCustomerNopayment;
	@InjectView
	private TextView txtContNO;
	@InjectView
	private TextView txtCustomerFullName;
	@InjectView
	private TextView txtInstallDate;
	@InjectView
	private TextView txtPaymentAmount;
	@InjectView
	private TextView txtAddress;
	@InjectView
	private Spinner spnEmployee;
	@InjectView
	private TextView txtTitle;

	// private final String TASK_TYPE_SALE_PAYMENT_PERIOD = "SalePaymentPeriod";

	public static class Data extends BHParcelable {
		public SalePaymentPeriodInfo salepaymentperiod;
		public int customerNoPayment;
	}

	private Data data;

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_payment_first;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_first_payment_employee_assign;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_ok };
	}

	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		data = getData();
		txtTitle.setText(String.format("ลูกค้าที่ยังไม่ได้จัดกลุ่ม %d ราย", data.customerNoPayment));
		loadAllTeamMember();
		// bindData();
		// setWidgetsEventListener();
	}

	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_back:
			showLastView();
			break;
		case R.string.button_ok:
			final String title = "คำเตือน";
			final String message = "คุณต้องการบันทึกข้อมูล?";
			showYesNoDialogBox(title, message);
			break;
		default:
			break;
		}
	}

	private void showYesNoDialogBox(final String title, final String message) {
		Builder setupAlert;
		setupAlert = new AlertDialog.Builder(activity).setTitle(title).setMessage(message).setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				int position = spnEmployee.getSelectedItemPosition();
				if (position == 0) {
					// delete
					SalePaymentPeriodInfo spp = data.salepaymentperiod;
					if (spp.AssigneeEmpID != null && !spp.AssigneeEmpID.isEmpty())
						deleteAssign();
					else
						showLastView();
				} else {
					// save Data
					saveAssign();
				}
			}
		}).setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// just do nothing
			}
		});
		setupAlert.show();
	}

	private void bindData() {
		SalePaymentPeriodInfo spp = data.salepaymentperiod;

		txtContNO.setText(spp.CONTNO);
		
		//txtCustomerFullName.setText(spp.CustomerFullName);
		txtCustomerFullName.setText(String.format("%s %s", BHUtilities.trim(spp.CustomerFullName), BHUtilities.trim(spp.CompanyName)));

		
		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
		// sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
		// String installDate = sdf.format(spp.InstallDate);
		txtInstallDate.setText(BHUtilities.dateFormat(spp.InstallDate));

		// String paymentAmount = Float.toString(spp.PaymentAmount);
		txtPaymentAmount.setText(BHUtilities.numericFormat(spp.PaymentAmount));

		bindAddressInstall(spp.RefNo);

		if (spp.AssigneeEmpID != null) {
			ArrayAdapter myAdap = (ArrayAdapter) spnEmployee.getAdapter();
			int spinnerPosition = myAdap.getPosition(spp.AssigneeEmpName);
			spnEmployee.setSelection(spinnerPosition);
		}
	}

	// private void setWidgetsEventListener() {
	//
	// }

	private void loadAllTeamMember() {
		(new BackgroundProcess(activity) {
			List<EmployeeInfo> output;

			@Override
			protected void before() {
				// TODO Auto-generated method stub
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
//				output = getAllTeamMember(BHPreference.organizationCode(), BHPreference.teamCode());
				output = getEmployees(BHPreference.teamCode());
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				// mEmployeeList = output.Info;
				List<EmployeeInfo> ret = output;
				mEmployeeList = new ArrayList<EmployeeInfo>();
				EmployeeInfo emp = new EmployeeInfo();
				emp.EmpID = "";
				emp.FirstName = "";
				emp.LastName = "";
				mEmployeeList.add(emp);

				for (EmployeeInfo item : ret) {
					if (!mEmployeeList.get(mEmployeeList.size()-1).EmpID.equals(item.EmpID) && !item.EmpID.equals("999999") && item.PositionCode != null && !item.PositionCode.equals("Driver") && (item.Status == null || (item.Status != null && !item.Status.equals("R")))) {
						mEmployeeList.add(item);
					}
				}

				bindEmployee(mEmployeeList);
				bindData();
			}
		}).start();
	}

	private void bindEmployee(List<EmployeeInfo> employeeList) {
		List<String> newEmployeeList = new ArrayList<String>();

		for (EmployeeInfo item : employeeList) {
			newEmployeeList.add(String.format("%s %s", item.FirstName, BHUtilities.trim(item.LastName)));
		}
		BHSpinnerAdapter<String> dataAdapter = new BHSpinnerAdapter<String>(activity,newEmployeeList);
		spnEmployee.setAdapter(dataAdapter);
	}

	private void bindAddressInstall(final String refNO) {
		(new BackgroundProcess(activity) {
			AddressInfo output;

			@Override
			protected void before() {
				// TODO Auto-generated method stub
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getAddressByRefNoByAddressTypeCode(refNO, AddressType.AddressInstall.toString());
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				try {
					if (output != null) {
						AddressInfo addressInstall = output;
						String addressInstallString = String.format("%s %s %s %s %s",
								(addressInstall.AddressDetail == null || addressInstall.AddressDetail.isEmpty()) ? "" : addressInstall.AddressDetail,
								(addressInstall.SubDistrictName == null || addressInstall.SubDistrictName.isEmpty()) ? "" : addressInstall.SubDistrictName,
								(addressInstall.DistrictName == null || addressInstall.DistrictName.isEmpty()) ? "" : addressInstall.DistrictName,
								(addressInstall.ProvinceName == null || addressInstall.ProvinceName.isEmpty()) ? "" : addressInstall.ProvinceName,
								(addressInstall.Zipcode == null || addressInstall.Zipcode.isEmpty()) ? "" : addressInstall.Zipcode);
						txtAddress.setText(addressInstallString);
					}
				} catch (Exception e) {
					Log.e(this.getClass().getName(), e.getMessage());
				}
			}
		}).start();
	}

	private void saveAssign() {
		(new BackgroundProcess(activity) {

			AssignInfo input = new AssignInfo();
			SalePaymentPeriodInfo spp = data.salepaymentperiod;

			@Override
			protected void before() {
				// TODO Auto-generated method stub
				int position = spnEmployee.getSelectedItemPosition();
				EmployeeInfo employee = mEmployeeList.get(position);
				String assigneeEmpID = employee.EmpID;
				String assigneeTeamCode = BHPreference.teamCode();

				input.AssignID = "";
				input.TaskType = AssignTaskType.SalePaymentPeriod.toString();
				input.OrganizationCode = spp.OrganizationCode;
				input.RefNo = spp.RefNo;
				input.AssigneeEmpID = assigneeEmpID;
				input.AssigneeTeamCode = assigneeTeamCode;
				input.Order = 0;
				input.OrderExpect = 0;
				input.CreateDate = new Date();
				input.CreateBy = BHPreference.employeeID();//BHPreference.userID();
				input.LastUpdateDate = new Date();
				input.LastUpdateBy = BHPreference.employeeID();//BHPreference.userID();
				input.ReferenceID = data.salepaymentperiod.SalePaymentPeriodID;
			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				addOrUpdateAssign(input);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				showLastView();
			}
		}).start();
	}

	private void deleteAssign() {
		(new BackgroundProcess(activity) {

			SalePaymentPeriodInfo spp = data.salepaymentperiod;

			@Override
			protected void before() {
				// TODO Auto-generated method stub

			}

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				deleteAssignByRefNoByTaskTypeByReferenceID(spp.RefNo, AssignTaskType.SalePaymentPeriod.toString(), spp.SalePaymentPeriodID);
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				showLastView();
			}
		}).start();
	}

	private void showNoticeDialogBox(final String title, final String message) {
		Builder setupAlert;
		setupAlert = new AlertDialog.Builder(activity);
		setupAlert.setTitle(title);
		setupAlert.setMessage(message);
		setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				// showLastView();
			}
		});
		setupAlert.show();
	}
}
