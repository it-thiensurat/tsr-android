package th.co.thiensurat.fragments.sendmoney;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.data.controller.ChannelController;
import th.co.thiensurat.data.controller.ChannelItemController;
import th.co.thiensurat.data.controller.DatabaseHelper;
import th.co.thiensurat.data.controller.DocumentHistoryController;
import th.co.thiensurat.data.controller.SendMoneyController;
import th.co.thiensurat.data.info.ChannelItemInfo;
import th.co.thiensurat.data.info.DocumentHistoryInfo;
import th.co.thiensurat.data.info.SendMoneyInfo;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;

public class SendMoneySpecifyTransactionNoFragment extends BHFragment {

    @InjectView private EditText edtTransactionNo;
    @InjectView private EditText edtSendDate;
    @InjectView private EditText edtSendTime;
    /*@InjectView private ImageButton btnSendDate;*/

    Calendar c = Calendar.getInstance();
    private Pattern pattern;
    private Matcher matcher;

    private static final String TIME24HOURS_PATTERN =
            "([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]";

    private SendMoneyInfo sendMoney = null;

    public static class Data extends BHParcelable {
        public String sendMoneyID;
        public String transactionNo;
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
        return R.layout.fragment_sendmoney_specify_transaction_no;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back, R.string.button_ok};
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {
        data = getData();

//		if (!data.transactionNo.equals("")) {
//			edtTransactionNo.setText(data.transactionNo);
//		}			
        loadData();

        setWidgetsEventListener();

        edtSendTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    timeDialog();
                }
            }
        });

        edtSendTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                timeDialog();
            }
        });
    }

    private void timeDialog() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layoutTimeDialog = inflater.inflate(R.layout.time_dialog, null);
        final NumberPicker npHour = (NumberPicker) layoutTimeDialog.findViewById(R.id.npHour);
        final NumberPicker npMinute = (NumberPicker) layoutTimeDialog.findViewById(R.id.npMinute);
        final NumberPicker npSeconds = (NumberPicker) layoutTimeDialog.findViewById(R.id.npSeconds);

        NumberPicker.Formatter formatter = new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                String tmpStr = String.valueOf(value);
                if (value < 10) {
                    tmpStr = "0" + tmpStr;
                }
                return tmpStr;
            }
        };

        npHour.setFormatter(formatter);
        npHour.setMinValue(0);
        npHour.setMaxValue(23);

        npMinute.setFormatter(formatter);
        npMinute.setMinValue(0);
        npMinute.setMaxValue(59);

        npSeconds.setFormatter(formatter);
        npSeconds.setMinValue(0);
        npSeconds.setMaxValue(59);

        /*Date date = null;
        if (!edtSendTime.getText().toString().equals("")) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String strDateTime = String.format("%s %s", edtSendDate.getText(), edtSendTime.getText());
            try {
                date = dateFormat.parse(strDateTime);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else {
            date = new Date();
        }

        Calendar calendar = Calendar.getInstance(new Locale("th"));
        calendar.setTime(date);
        npHour.setValue(calendar.get(Calendar.HOUR_OF_DAY));
        npMinute.setValue(calendar.get(Calendar.MINUTE));
        npSeconds.setValue(calendar.get(Calendar.SECOND));*/
        npHour.setValue(c.get(Calendar.HOUR_OF_DAY));
        npMinute.setValue(c.get(Calendar.MINUTE));
        npSeconds.setValue(c.get(Calendar.SECOND));

        final AlertDialog.Builder setupAlert;
        setupAlert = new AlertDialog.Builder(activity)
                .setTitle("ตั้งเวลา")
                .setView(layoutTimeDialog)
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.dialog_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        //edtSendTime.setText(String.format("%02d:%02d:%02d", npHour.getValue(), npMinute.getValue(), npSeconds.getValue()));
                        c.set(Calendar.HOUR_OF_DAY, npHour.getValue());
                        c.set(Calendar.MINUTE, npMinute.getValue());
                        c.set(Calendar.SECOND, npSeconds.getValue());

                        String strSendTime = BHUtilities.dateFormat(c.getTime(), "HH:mm:ss");
                        edtSendTime.setText(strSendTime);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.cancel();
                    }
                });
        setupAlert.show();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_ok:
                /*if (validateData() == true) {
                    save();
                }*/
                if (newValidateData()) {
                    save();
                }
                break;
            default:
                break;
        }
    }

    private void setWidgetsEventListener() {
        final DatePickerDialog.OnDateSetListener dpl = new OnDateSetListener() {

            int chkDisplay = 0;

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                Calendar currentTime = Calendar.getInstance();
                currentTime.set(Calendar.HOUR_OF_DAY, 0);
                currentTime.set(Calendar.MINUTE, 0);
                currentTime.set(Calendar.SECOND, 0);
                currentTime.set(Calendar.MILLISECOND, 0);

                Calendar newCalendar = Calendar.getInstance();
                newCalendar.setTime(c.getTime());
                newCalendar.set(Calendar.YEAR, year);
                newCalendar.set(Calendar.MONTH, monthOfYear);
                newCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                newCalendar.set(Calendar.HOUR_OF_DAY, 0);
                newCalendar.set(Calendar.MINUTE, 0);
                newCalendar.set(Calendar.SECOND, 0);
                newCalendar.set(Calendar.MILLISECOND, 0);

                //if (c.getTime().after(Calendar.getInstance().getTime())) {
                if (newCalendar.getTime().after(currentTime.getTime())) {
                    String title = "คำเตือน";
                    String message = "กรุณาตรวจสอบวันที่!";
                    showNoticeDialogBox(title, message);
                } else {
//					edtSendDate.setText("" + dayOfMonth + "/" + (monthOfYear + 1)
//							+ "/" + year);
                    c.set(Calendar.YEAR, year);
                    c.set(Calendar.MONTH, monthOfYear);
                    c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    edtSendDate.setText(BHUtilities.dateFormat(c.getTime(), "dd/MM/yyyy"));
                }
            }
        };

		/*btnSendDate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				DatePickerDialog dpd = new DatePickerDialog(arg0.getContext(),
						dpl, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
						.get(Calendar.DAY_OF_MONTH));
				dpd.show();
			}
		});*/

        edtSendDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View arg0, boolean b) {
                if (b) {
                    DatePickerDialog dpd = new DatePickerDialog(arg0.getContext(),
                            dpl, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                            .get(Calendar.DAY_OF_MONTH));
                    dpd.show();
                }
            }
        });

        edtSendDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                DatePickerDialog dpd = new DatePickerDialog(arg0.getContext(),
                        dpl, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                        .get(Calendar.DAY_OF_MONTH));
                dpd.show();
            }
        });
    }

    private void loadData() {
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                sendMoney = getSendMoneyByID(data.sendMoneyID);
            }

            @Override
            protected void after() {

                String strSendDate = "";
                String strSendTime = "";
                if (sendMoney != null) {
                    edtTransactionNo.setText(sendMoney.TransactionNo == null ? sendMoney.Reference1 : sendMoney.TransactionNo);

                    /*strSendDate = BHUtilities.dateFormat(new Date(), "dd/MM/yyyy");
                    strSendTime = BHUtilities.dateFormat(new Date(), "HH:mm:ss");*/
                    strSendDate = BHUtilities.dateFormat(c.getTime(), "dd/MM/yyyy");
                    strSendTime = BHUtilities.dateFormat(c.getTime(), "HH:mm:ss");

                    edtSendDate.setText(strSendDate);
                    edtSendTime.setText(strSendTime);
                }

            }
        }).start();
    }

    private  boolean newValidateData() {
        String _transNO = edtTransactionNo.getText().toString();
        String _sendDate = edtSendDate.getText().toString();
        String _sendTime = edtSendTime.getText().toString();

        if (_transNO.equals("") || _sendDate.equals("") || _sendTime.equals("")) {
            String title = "SendMoney";
            String message = "";
            if (_transNO.equals("")) {
                message = "กรุณาป้อนข้อมูล Transaction No.";
            } else if (_sendDate.equals("")) {
                message = "กรุณากำหนดวันที่";
            } else if (_sendTime.equals("")) {
                message = "กรุณากำหนดเวลา";
            }
            showNoticeDialogBox(title, message);

            return false;
        }

        return true;
    }

    private boolean validateData() {
        //ใช้ newValidateData แทน
        boolean ret = false;

        final String _transNO = edtTransactionNo.getText().toString();
        final String _sendDate = edtSendDate.getText().toString();
        final String _sendTime = edtSendTime.getText().toString();
        SendMoneyInfo sm = new SendMoneyController().getSendMoneyByID(data.sendMoneyID);
        List<SendMoneyInfo> smList = new SendMoneyController().getSendMoneyByTransactionNo(BHPreference.organizationCode(), _transNO, sm.PaymentType);
        /*** [START] :: Fixed - [BHPROJ-0026-1056] :: [Android-บันทึก TransactionNo] ตรวจสอบ Not Existing ของ Field [SendMoney].SaveTransactionDate (Date + Time) ***/
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String strDateTime = String.format("%s %s",  BHUtilities.dateFormat(c.getTime(), "dd/MM/yyyy", new Locale("en_US")), edtSendTime.getText());
        Date saveDate = null;
        try {
            saveDate = formatter.parse(strDateTime);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        List<SendMoneyInfo> smListExistTrnDate = new SendMoneyController().getSendMoneyBySaveTransactionDate(BHPreference.organizationCode(), saveDate);
        /*** [END] :: Fixed - [BHPROJ-0026-1056] :: [Android-บันทึก TransactionNo] ตรวจสอบ Not Existing ของ Field [SendMoney].SaveTransactionDate (Date + Time) ***/

        if (_transNO.equals("") || _sendDate.equals("") || _sendTime.equals("")) {
            String title = "SendMoney";
            String message = "";
            if (_transNO.equals("")) {
                message = "กรุณาป้อนข้อมูล Transaction No.";
            } else if (_sendDate.equals("")) {
                message = "กรุณากำหนดวันที่";
            } else if (_sendTime.equals("")) {
                message = "กรุณากำหนดเวลา";
            }
            showNoticeDialogBox(title, message);
        }
/*        else if (smList != null && smList.size() > 0) {
            String title = "SendMoney";
            String message = "เลขที่อ้างอิงนี้มีการใช้งานแล้ว!!!";
            showNoticeDialogBox(title, message);
        } */

/*        else if (smListExistTrnDate != null && smListExistTrnDate.size() > 0) {
            *//*** [START] :: Fixed - [BHPROJ-0026-1056] :: [Android-บันทึก TransactionNo] ตรวจสอบ Not Existing ของ Field [SendMoney].SaveTransactionDate (Date + Time) ***//*
            String title = "SendMoney";
            String message = "วันเวลาที่บันทึกนี้มีการใช้งานแล้ว!!!";
            showNoticeDialogBox(title, message);
            *//*** [END] :: Fixed - [BHPROJ-0026-1056] :: [Android-บันทึก TransactionNo] ตรวจสอบ Not Existing ของ Field [SendMoney].SaveTransactionDate (Date + Time) ***//*
        } */

        else {
            String time = edtSendTime.getText().toString();
            pattern = Pattern.compile(TIME24HOURS_PATTERN);
            matcher = pattern.matcher(time);

            if (matcher.matches() == false) {
                String title = "SendMoney";
                String message = "รูปแบบเวลาไม่ถูกต้อง!!!";
                showNoticeDialogBox(title, message);
            } else
                ret = true;
        }

        return ret;
    }

    private void save() {
        (new BackgroundProcess(activity) {
            //SendMoneyInfo sm = null;
            //Date saveDate = null;

            /*@Override
            protected void before() {
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                //String strDateTime = String.format("%s %s", edtSendDate.getText(), edtSendTime.getText());
                String strDateTime = String.format("%s %s",  BHUtilities.dateFormat(c.getTime(), "dd/MM/yyyy", new Locale("en_US")), edtSendTime.getText());

                try {
                    saveDate = formatter.parse(strDateTime);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }*/

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                //sm = getSendMoneyByID(data.sendMoneyID);
                if (sendMoney != null) {
                    String transNO = edtTransactionNo.getText().toString();
                    sendMoney.TransactionNo = transNO;

                    //sendMoney.SaveTransactionNoDate = saveDate;
                    c.set(Calendar.MILLISECOND, 0);
                    sendMoney.SaveTransactionNoDate = c.getTime();

                    sendMoney.LastUpdateBy = BHPreference.employeeID();//BHPreference.userID();
                    sendMoney.LastUpdateDate = new Date();
                    // Fixed - [BHPROJ-0026-865] :: [Meeting@TSR@11/02/59] [Android-บันทึก Transaction No. หลังจากนำส่งเงินแล้ว] ให้เรียกใช้ WS method ชื่อ UpdateSendMoneyTransactionNo แทน
//                    updateSendMoney(sendMoney);
                    updateSendMoneyTransactionNo(sendMoney);

                    DocumentHistoryInfo docHist = getDocumentHistoryByDocumentNumber(sendMoney.SendMoneyID, DocumentHistoryController.DocumentType.PayInSlipBank.toString());
                    if (docHist == null) {

                        ChannelItemInfo channelItemInfo = new ChannelItemController().getChannelItemByChannelItemID(BHPreference.organizationCode(), sendMoney.ChannelItemID);
                        String DocumentType = "";
                        if (channelItemInfo.ChannelCode.equals(ChannelController.ChannelCode.Bank.toString())) {
                            DocumentType = DocumentHistoryController.DocumentType.PayInSlipBank.toString();
                        } else if (channelItemInfo.ChannelCode.equals(ChannelController.ChannelCode.CounterService.toString())) {
                            DocumentType = DocumentHistoryController.DocumentType.PayInSlipPayPoint.toString();
                        }

                        if (!DocumentType.equals("")) {
                            docHist = new DocumentHistoryInfo();
                            docHist.PrintHistoryID = DatabaseHelper.getUUID();
                            docHist.OrganizationCode = BHPreference.organizationCode();
                            docHist.DatePrint = new Date();
                            docHist.DocumentType = DocumentType;
                            docHist.DocumentNumber = sendMoney.SendMoneyID;
                            docHist.Selected = false;
                            docHist.Deleted = false;
                            docHist.CreateBy = BHPreference.employeeID();
                            docHist.CreateDate = new Date();
                            docHist.LastUpdateBy = BHPreference.employeeID();
                            docHist.LastUpdateDate = new Date();
                            docHist.PrintOrder = 1;

                            addDocumentHistory(docHist, true);
                        }
                    }
                }
            }

            @Override
            protected void after() {
                showLastView();
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
                        dialog.cancel();
                    }
                });
        setupAlert.show();
    }

}
