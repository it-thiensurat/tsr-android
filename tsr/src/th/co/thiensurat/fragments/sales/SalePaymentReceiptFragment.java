package th.co.thiensurat.fragments.sales;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import th.co.bighead.utilities.BHPagerFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.AddressInfo;
import th.co.thiensurat.data.info.BankInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.PaymentInfo;
import th.co.thiensurat.data.info.ProductInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;

public class SalePaymentReceiptFragment extends BHPagerFragment {

	public static class Data extends BHParcelable {
		public List<SalePaymentPeriodInfo> payments;
		public PaymentInfo payment;
		public ContractInfo contract;
		public DebtorCustomerInfo customer;
		public ProductInfo product;
		public AddressInfo address;
		public BankInfo bank;
		public boolean isCash;
        public int paymentCount;
		public SaleFirstPaymentChoiceFragment.ProcessType processType;
	}

	private Data data;

	@InjectView
	private TextView tvReceiptDate;
	@InjectView
	private TextView tvReceiptNo;
	@InjectView
	private TextView tvContractNo;
	@InjectView
	private TextView tvContractDate;
	@InjectView
	private TextView tvCustomerName;
	@InjectView
	private TextView tvCitizenNo;
	@InjectView
	private TextView tvCustomerAddress;
	@InjectView
	private TextView tvProductName;
	@InjectView
	private TextView tvProductNo;
	@InjectView
	private TextView tvPeriodAmountLabel;
	@InjectView
	private TextView tvPeriodAmount;

	@InjectView
	private LinearLayout llBalanceAmount;
	@InjectView
	private TextView tvBalanceAmountLabel;
	@InjectView
	private TextView tvBalanceAmount;

	@InjectView
	private LinearLayout llCreditAmount;
	@InjectView
	private TextView tvCreditName;
	@InjectView
	private TextView tvCreditNumber;

	@InjectView
	private LinearLayout llChequeAmount;
	@InjectView
	private TextView tvChequeName;
	@InjectView
	private TextView tvChequeBrach;
	@InjectView
	private TextView tvChequeNo;
	@InjectView
	private TextView tvChequeDate;
    @InjectView
    private TextView txtSaleEmpName;
    @InjectView
    private TextView txtSaleTeamName;
    @InjectView
    private TextView textViewModel;

    @InjectView
    private TextView txtThaiBaht;



    private int dotsCount;
    @InjectView
    private ViewPager viewPager;
    @InjectView
    private LinearLayout viewPagerCountDots;
    //    @InjectView
    private TextView[] dots;


	private boolean PayPartial;
	private String payPartialString;

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_payment_receipt;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		data = getData();

	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {


		if (savedInstanceState != null) {
			data = savedInstanceState.getParcelable(BH_FRAGMENT_DEFAULT_DATA);
		}
		PayPartial();
	}

	private void PayPartial() {
		// TODO Auto-generated method stub
		(new BackgroundProcess(activity) {
			PaymentInfo output;

			@Override
			protected void calling() {
				// TODO Auto-generated method stub
				output = getPaymentRefNo(BHPreference.RefNo());
			}

			@Override
			protected void after() {
				// TODO Auto-generated method stub
				super.after();
				if (output != null) {
					if (output.PayPartial == true) {
						PayPartial = true;
						bindData();
                        setUiPageViewController();
					} else {
						PayPartial = false;
						bindData();
                        setUiPageViewController();
					}
				}
			}
		}).start();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putParcelable(BH_FRAGMENT_DEFAULT_DATA, data);
		super.onSaveInstanceState(outState);
	}

	private void bindData() {
		if (data.contract != null) {
			tvContractNo.setText((data.contract.CONTNO));
			tvContractDate.setText(BHUtilities.dateFormat(data.contract.EFFDATE));
			tvProductNo.setText(BHUtilities.trim(data.contract.ProductSerialNumber));
			//txtSaleEmpName.setText("(" + data.contract.upperEmployeeName + ")");
            txtSaleEmpName.setText(String.format("(%s)", BHPreference.userFullName()));
	        //txtSaleTeamName.setText("(ทีม "+ data.contract.SaleTeamCode + ")");
            txtSaleTeamName.setText(String.format("ทีม %s", BHPreference.teamCode()));
            textViewModel.setText(data.contract.MODEL);

		}

		if (data.customer != null) {
			tvCustomerName.setText(BHUtilities.trim(data.customer.CustomerFullName()));
			tvCitizenNo.setText(BHUtilities.trim(data.customer.IDCard));
		}

		if (data.address != null) {
			tvCustomerAddress.setText(BHUtilities.trim(data.address.Address()));
		}

		if (data.product != null) {
			tvProductName.setText(BHUtilities.trim(data.product.ProductName));
		}

		if (data.payments != null) {
			SalePaymentPeriodInfo payment = data.payments.get(0);
			SalePaymentPeriodInfo lastPayment = data.payments.get(data.payments.size() - 1);
			tvReceiptNo.setText(BHUtilities.trim(payment.ReceiptCode));
			tvReceiptDate.setText(BHUtilities.dateFormat(payment.ReceiptDate));

//			if (PayPartial == true) {
				payPartialString = " (ชำระบางส่วน) ";
//			} else {
//				payPartialString = " ";
//			}

			String periodAmountLabel = getString(R.string.label_period_payment);
			String bahtLabel = " " + getString(R.string.label_baht);

			if (data.isCash) {
				periodAmountLabel = "ค่างวดเงินสด";
                List<SalePaymentPeriodInfo> viewPaymentPeriod = new SalePaymentPeriodController().getSalePaymentPeriodByRefNo(BHPreference.RefNo());
                //if (payment.CurrentPaymentAmount == (viewPaymentPeriod.get(payment.PaymentPeriodNumber-1).PaymentAmount - viewPaymentPeriod.get(payment.PaymentPeriodNumber-1).Discount)) {
				if (payment.PaymentComplete) {
                    periodAmountLabel += "";
                } else {
                    periodAmountLabel += payPartialString;
                }

			} else {
				List<SalePaymentPeriodInfo> viewPaymentPeriod;
				if(data.processType == SaleFirstPaymentChoiceFragment.ProcessType.Credit || data.processType == SaleFirstPaymentChoiceFragment.ProcessType.NextPayment){
					viewPaymentPeriod = new SalePaymentPeriodController().getPaymentSummaryForCredit(data.payment.PaymentID);
				}else{
					viewPaymentPeriod = new SalePaymentPeriodController().getSalePaymentPeriodByRefNo(BHPreference.RefNo());
				}
				if(data.processType == SaleFirstPaymentChoiceFragment.ProcessType.Credit || data.processType == SaleFirstPaymentChoiceFragment.ProcessType.NextPayment){
					periodAmountLabel += String.format(" %d/%d", payment.PaymentPeriodNumber, data.contract.MODE);
					if(!payment.PaymentComplete){
						periodAmountLabel += payPartialString;
					}
				}else{
//					if (payment.CurrentPaymentAmount == (viewPaymentPeriod.get(payment.PaymentPeriodNumber-1).PaymentAmount - viewPaymentPeriod.get(payment.PaymentPeriodNumber-1).Discount)) {
//						periodAmountLabel += String.format(" %d/%d", payment.PaymentPeriodNumber, lastPayment.PaymentPeriodNumber);
//					} else {
//						periodAmountLabel += String.format(" %d/%d", payment.PaymentPeriodNumber, lastPayment.PaymentPeriodNumber) + payPartialString;
//					}
					if (payment.PaymentComplete) {
						periodAmountLabel += String.format(" %d/%d", payment.PaymentPeriodNumber, lastPayment.PaymentPeriodNumber);
					} else {
						periodAmountLabel += String.format(" %d/%d", payment.PaymentPeriodNumber, lastPayment.PaymentPeriodNumber) + payPartialString;
					}
				}

				//periodAmountLabel += String.format(" %d", payment.PaymentPeriodNumber) + payPartialString;
			}

			tvPeriodAmountLabel.setText(BHUtilities.trim(periodAmountLabel));
			tvPeriodAmount.setText(BHUtilities.numericFormat(payment.CurrentPaymentAmount) + bahtLabel);
            
            txtThaiBaht.setText(ThaiBaht(BHUtilities.numericFormat(payment.CurrentPaymentAmount)));

			float totalBalanceAmount = payment.OutstandingAmount;
			for (int ii = 1; ii < data.payments.size(); ii++) {
				totalBalanceAmount += data.payments.get(ii).NetAmount;
			}
			if(data.processType == SaleFirstPaymentChoiceFragment.ProcessType.Credit || data.processType == SaleFirstPaymentChoiceFragment.ProcessType.NextPayment){
				// หาค่างวดที่เหลือถัดไป ที่ไม่เกี่ยวข้องกับการชำระเงินในครั้งนี้
				for(int i = lastPayment.PaymentPeriodNumber + 1; i <= data.contract.MODE; i++){
					SalePaymentPeriodInfo salePaymentPeriodInfo = new SalePaymentPeriodController().getSalePaymentPeriodInfoByRefNoAndPaymentPeriodNumber(data.contract.RefNo, i);
					totalBalanceAmount += salePaymentPeriodInfo != null ? salePaymentPeriodInfo.NetAmount : 0;
				}
			}

			if (totalBalanceAmount > 0) {
				llBalanceAmount.setVisibility(View.VISIBLE);
				String balanceAmountLabel = getString(R.string.label_balance_amount);
				if (data.isCash) {
					balanceAmountLabel += "เงินสด";
				} else {
					if(data.processType == SaleFirstPaymentChoiceFragment.ProcessType.Credit || data.processType == SaleFirstPaymentChoiceFragment.ProcessType.NextPayment){
						if(payment.PaymentPeriodNumber == data.contract.MODE){
							if(payment.PaymentComplete){
								// hidden
							}else{
								balanceAmountLabel += String.format("งวดที่ %d", payment.PaymentPeriodNumber);
							}
						}else if(data.contract.MODE - payment.PaymentPeriodNumber == 1) {
							if (payment.PaymentComplete) {
								balanceAmountLabel += String.format("งวดที่ %d", payment.PaymentPeriodNumber + 1);
							} else {
								balanceAmountLabel += String.format("งวดที่ %d - %d", payment.PaymentPeriodNumber, data.contract.MODE);
							}
						}else{
							if (payment.PaymentComplete) {
								balanceAmountLabel += String.format("งวดที่ %d - %d", payment.PaymentPeriodNumber + 1, data.contract.MODE);
							} else {
								balanceAmountLabel += String.format("งวดที่ %d - %d", payment.PaymentPeriodNumber, data.contract.MODE);
							}
						}
					}else{
						if (data.payments.size() == 1) {
							balanceAmountLabel += String.format("งวดที่ %d", payment.PaymentPeriodNumber);
						} else if (payment.OutstandingAmount > 0) {
							balanceAmountLabel += String.format("งวดที่ %d - %d", payment.PaymentPeriodNumber, lastPayment.PaymentPeriodNumber);
						} else {
							balanceAmountLabel += String.format("งวดที่ %d - %d", data.payments.get(1).PaymentPeriodNumber, lastPayment.PaymentPeriodNumber);
						}
					}
				}

				tvBalanceAmountLabel.setText(BHUtilities.trim(balanceAmountLabel));
				tvBalanceAmount.setText(BHUtilities.numericFormat(totalBalanceAmount) + bahtLabel);
			}
		}

		if (data.payment != null) {
			if (data.payment.PaymentType.equals("Credit")) {
				llCreditAmount.setVisibility(View.VISIBLE);
				if (data.bank != null) {
					tvCreditName.setText(BHUtilities.trim(data.bank.BankName));
				}
				tvCreditNumber.setText(BHUtilities.trim(data.payment.CreditCardNumber));
			} else if (data.payment.PaymentType.equals("Cheque")) {
				llChequeAmount.setVisibility(View.VISIBLE);
				if (data.bank != null) {
					tvChequeName.setText(BHUtilities.trim(data.bank.BankName));
				}
				tvChequeBrach.setText(BHUtilities.trim(data.payment.ChequeBankBranch));
				tvChequeNo.setText(BHUtilities.trim(data.payment.ChequeNumber));
				tvChequeDate.setText(BHUtilities.trim(data.payment.ChequeDate));
			}
		}
		else {
			PaymentInfo payment = TSRController.getPaymentRefNo(BHPreference.RefNo());
			BankInfo bank = TSRController.getBankInfo(payment.BankCode);
			if(payment != null){
			if (payment.PaymentType.equals("Credit")) {
				llCreditAmount.setVisibility(View.VISIBLE);
				if (bank != null) {
					tvCreditName.setText((bank.BankName));
					tvCreditNumber.setText((payment.CreditCardNumber));
				}
				
					
				
			} else if (payment.PaymentType.equals("Cheque")) {
				llChequeAmount.setVisibility(View.VISIBLE);
				if (bank != null) {
					tvChequeName.setText((bank.BankName));
					tvChequeBrach.setText((payment.ChequeBankBranch));
					tvChequeNo.setText((payment.ChequeNumber));
					tvChequeDate.setText((payment.ChequeDate));
				}
				
			}
		}
		}
	}

    private void setUiPageViewController() {
        SalePaymentPeriodInfo payment = data.payments.get(0);

        dotsCount = data.paymentCount;
        dots = new TextView[dotsCount];

        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new TextView(activity);
            dots[i].setText(Html.fromHtml("&#149;"));
            dots[i].setTextSize(30);
            dots[i].setTextColor(getResources().getColor(R.color.main_menu_selected));
            viewPagerCountDots.addView(dots[i]);
        }
		if(data.processType == SaleFirstPaymentChoiceFragment.ProcessType.Credit || data.processType == SaleFirstPaymentChoiceFragment.ProcessType.NextPayment){
			dots[payment.index].setTextColor(getResources().getColor(R.color.dot_red));
		}else{
			dots[payment.PaymentPeriodNumber -1].setTextColor(getResources().getColor(R.color.dot_red));
		}
    }

    private String ThaiBaht(String strNumber) {
        //ตัดสิ่งที่ไม่ต้องการทิ้งลงโถส้วม
        for (int i = 0; i < strNumber.length(); i++)
        {
            strNumber = strNumber.replace (",", ""); //ไม่ต้องการเครื่องหมายคอมมาร์
            strNumber = strNumber.replace (" ", ""); //ไม่ต้องการช่องว่าง
            strNumber = strNumber.replace ("บาท", ""); //ไม่ต้องการตัวหนังสือ บาท
            strNumber = strNumber.replace ("฿", ""); //ไม่ต้องการสัญลักษณ์สกุลเงินบาท
        }
        //สร้างอะเรย์เก็บค่าที่ต้องการใช้เอาไว้
        String TxtNumArr[] = {"ศูยน์", "หนึ่ง", "สอง", "สาม", "สี่", "ห้า", "หก", "เจ็ด", "แปด", "เก้า", "สิบ"};
        String TxtDigitArr[] = {"", "สิบ", "ร้อย", "พัน", "หมื่น", "แสน", "ล้าน"};
        String BahtText = "";
        //ตรวจสอบดูซะหน่อยว่าใช่ตัวเลขที่ถูกต้องหรือเปล่า ด้วย isNaN == true ถ้าเป็นข้อความ == false ถ้าเป็นตัวเลข

        if (Float.isNaN(Float.parseFloat(strNumber.toString())))
        {
            showMessage("ข้อมูลนำเข้าไม่ถูกต้อง");
            //return "ข้อมูลนำเข้าไม่ถูกต้อง";
        } else
        {
            //ตรวสอบอีกสักครั้งว่าตัวเลขมากเกินความต้องการหรือเปล่า
            if ((Float.parseFloat(strNumber.toString()) - 0) > 9999999.9999)
            {
                showMessage("ข้อมูลนำเข้าเกินขอบเขตที่ตั้งไว้");
                //return "ข้อมูลนำเข้าเกินขอบเขตที่ตั้งไว้";
            } else
            {
                //พรากทศนิยม กับจำนวนเต็มออกจากกัน
                String[] splitNumber = strNumber.split("\\.");
                String integerNumber = splitNumber[0];
                String decimalNumber = splitNumber[1];

                //ขั้นตอนการประมวลผล
                int integerNumberSize = integerNumber.length();
                for(int i = 0; i < integerNumberSize; i++)
                {
                    String tmp = integerNumber.substring(i, i + 1);
                    if (!tmp.equals("0"))
                    {
                        if ((i == (integerNumberSize - 1)) && (tmp.equals("1")) && ( !(integerNumber.substring(i-1, i)).equals("0")))
                        {
                            BahtText += "เอ็ด";
                        } else if ((i == (integerNumberSize - 2)) && (tmp.equals("2")))
                        {
                            BahtText += "ยี่";
                        } else if ((tmp.equals("0")) || (i == (integerNumberSize - 2)) && (tmp.equals("1")))
                        {
                            BahtText += "";
                        } else
                        {
                            BahtText += TxtNumArr[Integer.parseInt(tmp.toString())];
                        }
                        BahtText += TxtDigitArr[integerNumberSize - i - 1];
                    }
                }
                BahtText += "บาท";

                if ((decimalNumber.equals("0")) || (decimalNumber.equals("00")))
                {
                    BahtText += "ถ้วน";
                } else
                {
                    BahtText += "\n";
                    int decimalNumberSize = decimalNumber.length();
                    for (int i = 0; i < decimalNumberSize; i++)
                    {
                        String tmp = decimalNumber.substring(i, i + 1);
                        if (!tmp.equals("0"))
                        {
                            if ((i == (decimalNumberSize - 1)) && (tmp.equals("1")) && ( !(decimalNumber.substring(i-1, i)).equals("0")))
                            {
                                BahtText += "เอ็ด";
                            } else if ((i == (decimalNumberSize - 2)) && (tmp.equals("2")))
                            {
                                BahtText += "ยี่";
                            } else if ((tmp.equals("0")) || (i == (decimalNumberSize - 2)) && (tmp.equals("1")))
                            {
                                BahtText += "";
                            } else
                            {
                                BahtText += TxtNumArr[Integer.parseInt(tmp.toString())];
                            }
                            BahtText += TxtDigitArr[decimalNumberSize - i - 1];
                        }
                    }
                    BahtText += "สตางค์";
                }
            }
        }
        return BahtText;
    }

}