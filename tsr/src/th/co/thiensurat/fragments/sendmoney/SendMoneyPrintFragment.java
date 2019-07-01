package th.co.thiensurat.fragments.sendmoney;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import th.co.bighead.utilities.BHBarcode;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.PrinterController;
import th.co.thiensurat.data.controller.SendMoneyController;
import th.co.thiensurat.data.info.EmployeeInfo;
import th.co.thiensurat.data.info.SendMoneyInfo;

public class SendMoneyPrintFragment extends BHFragment {

	public static String FRAGMENT_SEND_MONEY_SUMMARY_MAIN_TAG = "send_money_summary_main_tag";
	@InjectView private TextView txtTitle;
	@InjectView private TextView txtEmpName;
	@InjectView private TextView txtEmpCode;
	@InjectView private TextView txtSendDate;
//	@InjectView private TextView txtPrintDate;
	@InjectView private TextView txtSendAmount;
	@InjectView private TextView txtReference;
	@InjectView private ImageView ivBarcode;
	@InjectView private LinearLayout creditview;
	@InjectView private TextView txtCreditAmount;
	@InjectView private LinearLayout chequeview;
	@InjectView private TextView txtChequeAmount;
	@InjectView private TextView txtReference1;
	@InjectView private TextView txtReference2;
	@InjectView private TextView txtPayeeName;

	private EmployeeInfo emp;
	

	public static class Data extends BHParcelable {
		public String paymentType;
		public String paymentTypeName;
		// public PaymentInfo payment;
		public String channelCode;
		public String channelName;
		public String channelItemName;
		public String headOfficeOrBranch;
		// public String headOfficeOrBranchPayeeName;
		// public String headOfficeOrBranchRefNo;
		public String bankName;
		// public String bankBranch;
		// public String bankRefNo;
		public String counterService;
		// public String counterServiceBranch;
		// public String counterServiceRefNo;
		public String accountCode1;
		public String empName;
		public String empCode;
		public Date sendDate;
		public float sendAmount;
		/*** [START] :: Fixed - [BHPROJ-0026-972] ***/
		public String reference1;	// Barcode
		/*** [END] :: Fixed - [BHPROJ-0026-972] ***/
		public String reference2;	// Same as reference1 when choose HeadOfficeOrBranch
		public String payeeName;
		public String sendMoneyID;
	}

	private Data data;
	Bitmap barcode = null;
	SendMoneyInfo send;
	
	
	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_payment_send;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sendmoney_print;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_print, R.string.button_end };
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		data = getData();
		updateTitle();
		bindData();

		creditview.setVisibility(View.GONE);
		chequeview.setVisibility(View.GONE);
		if (data.paymentType.toLowerCase().equals("credit"))
			creditview.setVisibility(View.VISIBLE);
		else if (data.paymentType.toLowerCase().equals("cheque"))
			chequeview.setVisibility(View.VISIBLE);
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_end:
			showLastView(SendMoneyPrintFragment.FRAGMENT_SEND_MONEY_SUMMARY_MAIN_TAG);
			break;
		case R.string.button_print:
			printDocument();
			break;
		}
	}

	private void printDocument() {
		// TODO Auto-generated method stub
		
		(new BackgroundProcess(activity) {
			
			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				send = new SendMoneyController().getSendMoneyByIDForPrint(data.sendMoneyID);
			}
			
			@Override
			protected void after() {
				// TODO Auto-generated method stub
                new PrinterController(activity).printSendMoney(send);
			}
		}).start();
	}

	private void updateTitle() {
		String titleString = null;
		if(data.headOfficeOrBranch != null){
			titleString = String.format("ใบนำส่ง%s %s",data.paymentTypeName, data.headOfficeOrBranch);
		}else if (data.bankName != null) {
			titleString = String.format("ใบนำส่ง%s %s",data.paymentTypeName, data.bankName);
		}else if (data.counterService != null) {
			titleString = String.format("ใบนำส่ง%s %s",data.paymentTypeName, data.counterService);
		}
		txtTitle.setText(titleString);
	}

	private void bindData() {

		(new BackgroundProcess(activity) {

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				emp = getEmployeeByID(data.empCode);
			}

            @Override
            protected void after() {
                if (emp !=null) {
                    data.empName = emp.FullName();
                    txtEmpName.setText(data.empName);
                }
            }
		}).start();

		String barCodeText;
		/*** [START] :: Fixed - [BHPROJ-0026-972] ***/
		/*
		if (data.headOfficeOrBranch != null) {
			barCodeText = data.reference2;
		} else {
			barCodeText = data.accountCode1;
		}
		*/
		barCodeText = data.reference2;
		txtReference1.setText(data.reference1);
		/*** [END] :: Fixed - [BHPROJ-0026-972] ***/
		generateBarcode(barCodeText);
		txtReference.setText("Ref. 2 : " +  barCodeText);
		
//		txtEmpName.setText(emp.FirstName + "  " + emp.LastName);
		txtEmpCode.setText(data.empCode);
		txtSendDate.setText(BHUtilities.dateFormat(data.sendDate));
		try {
			String strPrintDate = BHUtilities.dateFormat(new Date());
			String strPrintTime = BHUtilities.dateFormat(new Date(), "HH:mm");
//			txtPrintDate.setText(String.format("%s เวลา %s น.", strPrintDate, strPrintTime));
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		txtSendAmount.setText(BHUtilities.numericFormat(data.sendAmount));
		txtPayeeName.setText(data.payeeName);
		txtReference2.setText(data.reference2);
		txtCreditAmount.setText(BHUtilities.numericFormat(data.sendAmount));
		txtChequeAmount.setText(BHUtilities.numericFormat(data.sendAmount));
	}

	private void generateBarcode(String reference) {
        barcode = BHBarcode.generateCode128(reference, 900, 200);
        ivBarcode.setImageBitmap(barcode);
		//ivBarcode.setVisibility(View.GONE);
		//txtReference.setVisibility(View.GONE);
	}
}
