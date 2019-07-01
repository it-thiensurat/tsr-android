package th.co.thiensurat.fragments.sendmoney;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.ChannelController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.SendMoneyController;
import th.co.thiensurat.data.info.ChannelItemInfo;
import th.co.thiensurat.data.info.SendMoneyInfo;

public class SendMoneySelectChannelFragment extends BHFragment {

	private String mPaymentType;
	private String mPaymentTypeName;
	private Date mPayDate;
	private float mSendAmount;
	private Date mSendDate;
	private ArrayList<ChannelItemInfo> mBankList;
	private ArrayList<ChannelItemInfo> mHeadOfficeList;
	private ArrayList<ChannelItemInfo> mCounterServiceList;
	Calendar c = Calendar.getInstance();

	@InjectView private CheckBox chkHeadOfficeOrBranch;
	@InjectView private CheckBox chkBank;
	@InjectView private CheckBox chkCounterService;
	@InjectView private LinearLayout headofficeorbranchview;
	@InjectView private LinearLayout bankview;
	@InjectView private LinearLayout counterserviceview;
	@InjectView private TextView txtSendAmount;
	@InjectView private LinearLayout bankheaderview;
	@InjectView private LinearLayout counterserviceheaderview;
	@InjectView private Spinner spnHeadOffice;
	@InjectView private EditText edtPayeeName;
	@InjectView private EditText edtHeadOfficeOrBranchRefNo;
	@InjectView private Spinner spnBank;
	@InjectView private EditText edtBranch;
	@InjectView private EditText edtBankRefNo;
	@InjectView private Spinner spnCounterService;
	@InjectView private EditText edtCounterServiceBranch;
	@InjectView private EditText edtCounterServiceRefNo;

	/*** [START] :: Fixed - [BHPROJ-0026-972] ***/
	List<ChannelItemInfo> channelByHeadOffice;
	List<ChannelItemInfo> channelByBank;
	List<ChannelItemInfo> channelByCounterService;
	ChannelItemInfo selectedChannelItem;
	/*** [END] :: Fixed - [BHPROJ-0026-972] ***/


	public static class Data extends BHParcelable {
		public String paymentType;
		public String paymentTypeName;
		public Date payDate;
		public Date sendDate;
		public float sendAmount;
	}

	private Data data;

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_payment_send;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sendmoney_select_channel;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		//-- Fixed - [BHPROJ-0026-1040] :: [Android-นำส่งเงิน] แก้ไข wording ตรงปุ่ม "พิมพ์" ให้เป็น "บันทึก" ที่ขั้นตอนการเลือกช่องทางการชำระเงิน
//		return new int[] { R.string.button_back, R.string.button_print };
		return new int[] { R.string.button_back, R.string.button_save };
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		data = getData();
		mPaymentType = data.paymentType;
		mPaymentTypeName = data.paymentTypeName;
		mPayDate = data.payDate;
		mSendAmount = data.sendAmount;
		mSendDate = data.sendDate;

		// setHeadOfficeOrBranchVisibility(View.GONE);
		headofficeorbranchview.setVisibility(View.GONE);
		bankview.setVisibility(View.GONE);
		counterserviceview.setVisibility(View.GONE);

		if (!mPaymentType.toLowerCase().equals("cash")) {
			bankheaderview.setVisibility(View.GONE);
			counterserviceheaderview.setVisibility(View.GONE);
		}

		updateSendAmountTitle();
		getChannel();
		setWidgetsEventListener();
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_back:
			showLastView();
			break;
		//-- Fixed - [BHPROJ-0026-1040] :: [Android-นำส่งเงิน] แก้ไข wording ตรงปุ่ม "พิมพ์" ให้เป็น "บันทึก" ที่ขั้นตอนการเลือกช่องทางการชำระเงิน
//		case R.string.button_print:
		case R.string.button_save:
			if (chkHeadOfficeOrBranch.isChecked() || chkBank.isChecked() || chkCounterService.isChecked()) {
				if(chkHeadOfficeOrBranch.isChecked() && edtPayeeName.getText().toString().trim().equals("")){
					String title = "กรุณาตรวจสอบข้อมูล";
					String message = "ชื่อผู้รับเงินไม่ถูกต้อง";
					showNoticeDialogBox(title, message);
				} else {
					String title = "ยืนยัน";
					//-- Fixed - [BHPROJ-0026-1040] :: [Android-นำส่งเงิน] แก้ไข wording ตรงปุ่ม "พิมพ์" ให้เป็น "บันทึก" ที่ขั้นตอนการเลือกช่องทางการชำระเงิน
					String message = "ต้องการบันทึกใบนำส่งเงินหรือไม่?";
					showYesNoDialogBox(title, message);
				}
			} else {
				String title = "กรุณาตรวจสอบข้อมูล";
				String message = "กรุณาเลือกช่องทางการส่งเงิน";
				showNoticeDialogBox(title, message);
			}
			break;
		default:
			break;
		}
	}

	private void updateSendAmountTitle() {
		txtSendAmount.setText(String.format("%s บาท", BHUtilities.numericFormat(mSendAmount)));
	}

	private void setWidgetsEventListener() {

		chkHeadOfficeOrBranch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked == true) {
					headofficeorbranchview.setVisibility(View.VISIBLE);
					bankview.setVisibility(View.GONE);
					counterserviceview.setVisibility(View.GONE);
					chkBank.setChecked(false);
					chkCounterService.setChecked(false);
					edtHeadOfficeOrBranchRefNo.setText(genReference2());
				} else {
					headofficeorbranchview.setVisibility(View.GONE);
				}
			}
				});

		chkBank.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked == true) {
					bankview.setVisibility(View.VISIBLE);
					headofficeorbranchview.setVisibility(View.GONE);
					counterserviceview.setVisibility(View.GONE);
					chkHeadOfficeOrBranch.setChecked(false);
					chkCounterService.setChecked(false);
					edtBankRefNo.setText(genReference2());
				} else {
					bankview.setVisibility(View.GONE);
				}
			}
		});

		chkCounterService.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked == true) {
					counterserviceview.setVisibility(View.VISIBLE);
					bankview.setVisibility(View.GONE);
					headofficeorbranchview.setVisibility(View.GONE);
					chkHeadOfficeOrBranch.setChecked(false);
					chkBank.setChecked(false);
					edtCounterServiceRefNo.setText(genReference2());
				} else {
					counterserviceview.setVisibility(View.GONE);
				}
			}
		});

	}
	
	private void getChannel() {
		(new BackgroundProcess(activity) {
			/*** [START] :: Fixed - [BHPROJ-0026-972] ***/
//			List<ChannelItemInfo> channelByHeadOffice = null;
//			List<ChannelItemInfo> channelByBank = null;
//			List<ChannelItemInfo> channelByCounterService = null;
			/*** [END] :: Fixed - [BHPROJ-0026-972] ***/

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				String organizationCode = BHPreference.organizationCode();
				channelByHeadOffice = getChannelByChannelCode(organizationCode, "%HeadOffice%");
				channelByBank = getChannelByChannelCode(organizationCode, "%Bank%");
				channelByCounterService = getChannelByChannelCode(organizationCode, "%CounterService%");
			}
			
			@Override
			protected void after() {
				if (channelByHeadOffice !=null) {
					bindChannelByHeadOffice(channelByHeadOffice);
				}
				
				if (channelByBank !=null) {
					bindChannelByBank(channelByBank);
				}
				
				if (channelByCounterService !=null) {
					bindChannelByCounterService(channelByCounterService);
				}
			}
		}).start();
	}

	private void bindChannelByHeadOffice(List<ChannelItemInfo> channelByHeadOffice) {
		int itemDefault = 0;
		int i = 0;
		
		ArrayList<ChannelItemInfo> headOfficeList = new ArrayList<ChannelItemInfo>();
		for (ChannelItemInfo ci : channelByHeadOffice) {
			headOfficeList.add(new ChannelItemInfo(
					ci.ChannelItemID, ci.ChannelItemName,
					ci.ChannelID, ci.AccountCode1, ci.AccountCode2,
					ci.OrganizationCode, ci.ChannelCode,
					ci.ChannelName));
			
			if (ci.ChannelItemName.equals("เธียรสุรัตน์ สำนักงานใหญ่")) {
				itemDefault = i;
			}
			i++;
		}
		mHeadOfficeList = headOfficeList;

		ArrayAdapter<ChannelItemInfo> adapter = new ArrayAdapter<ChannelItemInfo>( activity, android.R.layout.simple_spinner_dropdown_item, mHeadOfficeList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnHeadOffice.setAdapter(adapter);
		
		spnHeadOffice.setSelection(itemDefault);
	}

	private void bindChannelByBank(List<ChannelItemInfo> channelByBank) {
		ArrayList<ChannelItemInfo> bankList = new ArrayList<ChannelItemInfo>();
		for (ChannelItemInfo ci : channelByBank) {
			bankList.add(new ChannelItemInfo(ci.ChannelItemID,
					ci.ChannelItemName, ci.ChannelID,
					ci.AccountCode1, ci.AccountCode2,
					ci.OrganizationCode, ci.ChannelCode,
					ci.ChannelName));
		}
		mBankList = bankList;

		ArrayAdapter<ChannelItemInfo> adapter = new ArrayAdapter<ChannelItemInfo>(
				activity,
				android.R.layout.simple_spinner_dropdown_item,
				mBankList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnBank.setAdapter(adapter);
	}

	private void bindChannelByCounterService(List<ChannelItemInfo> channelByCounterService) {
		ArrayList<ChannelItemInfo> bankList = new ArrayList<ChannelItemInfo>();
		for (ChannelItemInfo ci : channelByCounterService) {
			if(ci.AccountCode2 != null &&  ci.AccountCode2.equals("LOTUS") && mSendAmount > 49000){

			} else {
				bankList.add(new ChannelItemInfo(ci.ChannelItemID,
						ci.ChannelItemName, ci.ChannelID,
						ci.AccountCode1, ci.AccountCode2,
						ci.OrganizationCode, ci.ChannelCode,
						ci.ChannelName));
			}
		}
		mCounterServiceList = bankList;

		ArrayAdapter<ChannelItemInfo> adapter = new ArrayAdapter<ChannelItemInfo>(
				activity,
				android.R.layout.simple_spinner_dropdown_item,
				mCounterServiceList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spnCounterService.setAdapter(adapter);
	}

	private void showYesNoDialogBox(final String title, final String message) {
		Builder setupAlert;
		setupAlert = new AlertDialog.Builder(activity)
				.setTitle(title)
				.setMessage(message)
				.setPositiveButton(getResources().getString(R.string.dialog_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// addSendMoney();
								print();
							}
						})
				.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// just do nothing
					}
				});
		setupAlert.show();
	}

	private void print() {
		String strHeadOffice = "";
		String strPayeeName = "";
		// String strHeadOfficeOrBranchRefNo = "";
		String strBankName = "";
		// String strBranch = "";
		// String strBankRefNo = "";
		String strCounterServiceName = "";
		// String strCounterServiceBranch = "";
		// String strCounterServiceRefNo = "";
		String strAccountCode1 = "";
		String strChannelCode = "";
		String strChannelName = "";
		/*** [START] :: Fixed - [BHPROJ-0026-972] ***/
		String strReference1 = "";
		/*** [END] :: Fixed - [BHPROJ-0026-972] ***/
		String strReference2 = "";
		String strChannelItemID = "";
		ChannelItemInfo ci;
		
		String strSendMoneyID = DatabaseHelper.getUUID();
		
		SendMoneyPrintFragment.Data selectedData = new SendMoneyPrintFragment.Data();

		if (chkHeadOfficeOrBranch.isChecked()) {
			ci = (ChannelItemInfo) spnHeadOffice.getSelectedItem();
			strChannelItemID = ci.ChannelItemID;
			strChannelCode = ChannelController.ChannelCode.HeadOffice.toString();
			strChannelName = ci.ChannelName;
			strHeadOffice = ci.ChannelItemName;
			strPayeeName = edtPayeeName.getText().toString();
			strReference2 = edtHeadOfficeOrBranchRefNo.getText().toString();
			strAccountCode1 = ci.AccountCode1;

			selectedData.channelCode = strChannelCode;
			selectedData.channelName = strChannelName;
			selectedData.headOfficeOrBranch = strHeadOffice;
			// selectedData.headOfficeOrBranchPayeeName = strPayeeName;
			// selectedData.headOfficeOrBranchRefNo =
			// strHeadOfficeOrBranchRefNo;

			/*** [START] :: Fixed - [BHPROJ-0026-972] ***/
			strReference1 = genReference1(); //edtHeadOfficeOrBranchRefNo.getText().toString();
			selectedData.reference1 = strReference1;
			/*** [END] :: Fixed - [BHPROJ-0026-972] ***/

		} else if (chkBank.isChecked()) {
			ci = (ChannelItemInfo) spnBank.getSelectedItem();
			strChannelItemID = ci.ChannelItemID;
			strChannelCode = ChannelController.ChannelCode.Bank.toString();
			strChannelName = ci.ChannelName;
			strBankName = ci.ChannelItemName;
			strPayeeName = edtBranch.getText().toString();
			strReference2 = edtBankRefNo.getText().toString();
			strAccountCode1 = ci.AccountCode1;

			selectedData.channelCode = strChannelCode;
			selectedData.channelName = strChannelName;
			selectedData.bankName = strBankName;
			// selectedData.bankBranch = strBranch;
			// selectedData.bankRefNo = strBankRefNo;

			/*** [START] :: Fixed - [BHPROJ-0026-972] ***/
			strReference1 = genReference1(); //genReference1OrBarcode(ci);
			selectedData.reference1 = strReference1;
			/*** [END] :: Fixed - [BHPROJ-0026-972] ***/
		} else if (chkCounterService.isChecked()) {
			ci = (ChannelItemInfo) spnCounterService.getSelectedItem();
			strChannelItemID = ci.ChannelItemID;
			strChannelCode = ChannelController.ChannelCode.CounterService.toString();
			strChannelName = ci.ChannelName;
			strCounterServiceName = ci.ChannelItemName;
			strPayeeName = edtCounterServiceBranch.getText().toString();
			strReference2 = edtCounterServiceRefNo.getText().toString();
			strAccountCode1 = ci.AccountCode1;

			selectedData.channelCode = strChannelCode;// "CounterService";
			selectedData.channelName = strChannelName;
			selectedData.counterService = strCounterServiceName;
			// selectedData.counterServiceBranch = strCounterServiceBranch;
			// selectedData.counterServiceRefNo = strCounterServiceRefNo;

			/*** [START] :: Fixed - [BHPROJ-0026-972] ***/
			strReference1 =  genReference1(); //genReference1OrBarcode(ci);
			selectedData.reference1 = strReference1;
			/*** [END] :: Fixed - [BHPROJ-0026-972] ***/
		}

		selectedData.payeeName = strPayeeName;
		selectedData.reference2 = strReference2;
		selectedData.accountCode1 = strAccountCode1;
		selectedData.empName = BHPreference.employeeID();
		selectedData.empCode = BHPreference.employeeID();
		selectedData.paymentType = mPaymentType;
		selectedData.paymentTypeName = mPaymentTypeName;
		selectedData.sendDate = mSendDate;
		selectedData.sendAmount = mSendAmount;
		selectedData.sendMoneyID = strSendMoneyID;

		SendMoneyInfo sm = new SendMoneyInfo();
		// SendMoneyID
		sm.SendMoneyID = strSendMoneyID;
		sm.OrganizationCode = BHPreference.organizationCode();
		sm.PaymentDate = mPayDate;
		sm.PaymentType = mPaymentType;
		/*** [START] :: Fixed - [BHPROJ-0026-972] ***/
		sm.Reference1 = strReference1;
		/*** [END] :: Fixed - [BHPROJ-0026-972] ***/
		sm.Reference2 = strReference2;
		sm.SendAmount = mSendAmount;
		sm.SendDate = mSendDate;
		// sm.BankCode = "";
		sm.Status = SendMoneyController.SendMoneyStatus.SENT.toString();
		// sm.TransactionNo = "";
		sm.CreateDate = new Date();
		sm.CreateBy = BHPreference.employeeID();
		sm.LastUpdateDate = sm.CreateDate;
		sm.LastUpdateBy = sm.CreateBy;
		// sm.SyncedDate = new Date();
		sm.ChannelItemID = strChannelItemID;
		sm.PayeeName = strPayeeName;

		//-- Fixed - [BHPROJ-0016-777] ::	[Meeting@BH-28/12/2558] 5. [Android-การบันทึก Transaction ใหม่] ในการบันทึก Transaction ต่าง ๆ ให้บันทึก Version ของโครงสร้างปัจจุบัน (Field TreeHistoryID) จาก ตาราง EmployeeDetail ลงไปด้วย
		sm.SendMoneyEmployeeLevelPath = BHPreference.currentTreeHistoryID();
		sm.EmpID = BHPreference.employeeID();
		sm.TeamCode = BHPreference.teamCode();
		addSendMoney(selectedData, sm);
	}

	private void addSendMoney(final SendMoneyPrintFragment.Data selectedData,
			final SendMoneyInfo sm) {
		(new BackgroundProcess(activity) {
	
			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				addSendMoney(sm);
			}

			@Override
			protected void after() {
//				SendMoneyPrintFragment.Data data = new SendMoneyPrintFragment.Data();
//				data.sendMoneyID = sm.SendMoneyID;
				SendMoneyPrintFragment fm = BHFragment.newInstance(SendMoneyPrintFragment.class, selectedData);
				showNextView(fm);
			}
		}).start();
	}

	private void showNoticeDialogBox(final String title, final String message) {
		Builder setupAlert;
		setupAlert = new AlertDialog.Builder(activity);
		setupAlert.setTitle(title);
		setupAlert.setMessage(message);
		setupAlert.setPositiveButton(getResources().getString(R.string.dialog_ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// ????
					}
				});
		setupAlert.show();
	}


	private String genReference1() {
		/*** [START] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ ***/
//		// YIM Edit here for change solution generate Reference1 (BHPROJ-0016-7262)
//		//YIM new gen ref1 solution
//		Date dtmNow = new Date();
//		SendMoneyInfo sm = TSRController.getLastSendMoneyToBankAndPayPoint(BHPreference.employeeID(),BHGeneral.isChangeSolutionGenerateReference);
//		String CurrentYear = BHUtilities.dateFormat(dtmNow, "yyyy");
//		String strRunningNo;
//		int runningNumber;
//		boolean isFirstGen = true;
//		String ret = "";
//
//		if(!BHGeneral.isChangeSolutionGenerateReference&&chkHeadOfficeOrBranch.isChecked()) {
//			ret = "00000000";
//		} else {
//			if(sm != null){
//				if (sm.Reference1 != null) {
//					if (sm.Reference1.length() == 8){
//						String RefYear = sm.Reference1.substring(0,4);
//						String RefRunning = sm.Reference1.substring(4,8);
//						if(!RefYear.equals(CurrentYear)) {
//							RefYear = CurrentYear;
//							runningNumber = 1;
//						} else {
//							try {
//								runningNumber = Integer.parseInt(RefRunning) + 1;
//							} catch (NumberFormatException e) {
//								runningNumber = 1;
//							}
//						}
//						strRunningNo = String.format("%04d",runningNumber);
//						ret = String.format("%s%s", RefYear, strRunningNo);
//						isFirstGen = false;
//					}
//				}
//			}
//
//			if (isFirstGen) {
//				strRunningNo = String.format("%04d",1);
//				ret = String.format("%s%s", CurrentYear, strRunningNo);
//			}
//		}
//
//
//		//YIM old grn ref1 solution
//		/*
//		String ret = "00000000";
//		if(chkHeadOfficeOrBranch.isChecked()) {
//			ret = "00000000";
//		} else {
//			ret = genReference2();
//		}
//		*/
//
//
//		String EmpID = BHPreference.employeeID();
////		switch (BHPreference.PositionCode().toLowerCase().contains("credit")) {
////			case "credit" : ret = String.format("%s%s", ret, "0000"); break;
////			case "sale"  : ret = String.format("%s%s", ret, "0200"); break;
////			default: ret = String.format("%s%s", ret, "0000"); break;
////		}
//
//		if(BHPreference.PositionCode().toLowerCase().contains("credit")) ret = String.format("%s%s", ret, "0000");
//		if(BHPreference.PositionCode().toLowerCase().contains("sale")) ret = String.format("%s%s", ret, "0200");
//		if(BHPreference.PositionCode().toLowerCase().contains("dept")) ret = String.format("%s%s", ret, "0400");
//		if(BHPreference.PositionCode().toLowerCase().contains("oper")) ret = String.format("%s%s", ret, "1000");
//
////		switch (BHPreference.PositionCode().toLowerCase()) {
////			case "credit" :
////				ret = String.format("%s%s", ret, "0000");
////				break;
////			case "sale" :
////				ret = String.format("%s%s", ret, "0200");
////				break;
////			case "dept" : //เร่งรัดหนี้สิ้น
////				ret = String.format("%s%s", ret, "0400");
////				break;
////			/*case "oper"  : //ปฏิบัติการ
////				ret = String.format("%s%s", ret, "1000");
////				break;*/
////		}
//
//		EmpID = EmpID.replace('A','1').replace('B','2').replace('C','3').replace('D','4');
//		ret = String.format("%s%s", ret, EmpID);
//
//		return ret;


		String ret = "";

		if(!BHGeneral.isChangeSolutionGenerateReference&&chkHeadOfficeOrBranch.isChecked()) {
			ret = "00000000";
		} else {
			ret = String.format("%s%04d", BHPreference.getSendMoneyYearFormatGenerate(), BHPreference.getRunningNumberReference1() + 1);
		}

		ret = String.format("%s%s", ret, BHPreference.getSendMoneyReference1FormatGenerate());
		return ret;
		/*** [END] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ   ***/
	}

	private String genReference2() {
		/*** [START] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ ***/
//		Date dtmNow = new Date();
//		SendMoneyInfo sm = TSRController.getLastSendMoneyToBankAndPayPoint(BHPreference.employeeID(),false);
//		String CurrentYear = BHUtilities.dateFormat(dtmNow, "yyyy");
//		String strRunningNo;
//		int runningNumber;
//		boolean isFirstGen = true;
//		String ret = "";
//
//		if(chkHeadOfficeOrBranch.isChecked()) {
//			ret = String.format("%s%s",BHPreference.saleCode(), BHUtilities.dateFormat(dtmNow, "yyMMddHHmmss"));
//		} else {
//			if(sm != null){
//				if (sm.Reference2.length() == 8){
//					String RefYear = sm.Reference2.substring(0,4);
//					String RefRunning = sm.Reference2.substring(4,8);
//					if(!RefYear.equals(CurrentYear)) {
//						RefYear = CurrentYear;
//						runningNumber = 1;
//					} else {
//						try {
//							runningNumber = Integer.parseInt(RefRunning) + 1;
//						} catch (NumberFormatException e) {
//							runningNumber = 1;
//						}
//					}
//					strRunningNo = String.format("%04d",runningNumber);
//					ret = String.format("%s%s", RefYear, strRunningNo);
//					isFirstGen = false;
//				}
//			}
//
//			if (isFirstGen) {
//				strRunningNo = String.format("%04d",1);
//				ret = String.format("%s%s", CurrentYear, strRunningNo);
//			}
//		}
//		//ret = BHUtilities.dateFormat(dtmNow, "yyMMddHHmmss");
//		return ret;


		String ret = "";

		if(chkHeadOfficeOrBranch.isChecked()) {
			Date dtmNow = new Date();
			ret = String.format("%s%s",BHPreference.saleCode(), BHUtilities.dateFormat(dtmNow, "yyMMddHHmmss"));
		} else {
			ret = String.format("%s%04d", BHPreference.getSendMoneyYearFormatGenerate(), BHPreference.getRunningNumberReference2() + 1);
		}
		return ret;
		/*** [END] :: Fixed - [BHPROJ-0016-7457] แก้เรื่องการดึงค่า Max Running No ของ [SendMoney].Reference1 ใหม่ เพื่อหลีกเลี่ยงปัญหาการ Gen. เลขที่ Reference1 ซ้ำ   ***/
	}

	/*** [START] :: Fixed - [BHPROJ-0026-972] ***/
	private String genReference1OrBarcode(ChannelItemInfo selChnItem) {
		return selChnItem.AccountCode1 + BHPreference.saleCode();
	}
	/*** [END] :: Fixed - [BHPROJ-0026-972] ***/

}
