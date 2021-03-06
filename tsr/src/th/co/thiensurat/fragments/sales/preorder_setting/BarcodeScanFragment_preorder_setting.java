package th.co.thiensurat.fragments.sales.preorder_setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.sales.SaleMainFragment;
import th.co.thiensurat.views.ViewTitle;

//import com.google.zxing.client.android.CaptureActivity;

public class BarcodeScanFragment_preorder_setting extends BHFragment {
	public static abstract class ScanCallBack extends BHFragmentCallback {
		public String onNextClick() {
			return null;
		}
	}

	public static class Result extends BHParcelable {
		public String barcode,barcode2,barcode3;

		private Result(String barcode,String barcode2,String barcode3) {
			this.barcode = barcode;
			this.barcode2 = barcode2;
			this.barcode3 = barcode3;

		}
	}

	private static class Data extends BHParcelable {
		public int titleResID = -1;
		public int viewTitleResID = -1;
		public String viewTitle = null;
		public int descriptionResID = -1;
		public String description = null;
	}

	private static final String FRAGMENT_DATA = "th.co.thiensurat.barcode.data";
	private static final int REQUEST_QR_SCAN = 2468;
    private static final int REQUEST_QR_SCAN2 = 2469;
	private static final int REQUEST_QR_SCAN3 = 2470;

    public  static String barcode2 = "";
	public  static String barcode3 = "";

	public  static int select_baecode = 0,oncick=0,check_scan2=0;
	SaleMainFragment saleMainFragment;


	private ProductStockInfo productInfo;
	TSRController controller;
	private String title;
	private String message;


	@InjectView
	private ViewTitle vwTitle;
	@InjectView
	private ImageButton ibScanBarcode,ibScanBarcode2,ibScanBarcode3;
	@InjectView
	private TextView tvDescription;
	@InjectView
	private EditText edtBarcode,edtBarcode2,edtBarcode3;

    @InjectView
    private LinearLayout li_scan2,li_scan3;






	private Data data = new Data();

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_scan_barcode;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_next };
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return data.titleResID;
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
		case R.string.button_back:
			showLastView();
			break;

		case R.string.button_next:
//			fragmentCallback.getClass().isAssignableFrom(ScanCallBack.class);
//			ScanCallBack scanCallBack = (ScanCallBack) fragmentCallback;
//			if (scanCallBack != null) {
//				String barcode = scanCallBack.onNextClick();
//				Result result = new Result(barcode);
//				setResult(result);				
//			}

			select_baecode=0;
             oncick=1;

			String barcode = edtBarcode.getText().toString();
            String barcode2 = edtBarcode2.getText().toString();
			String barcode3 = edtBarcode3.getText().toString();


			Result result = new Result(barcode,barcode2,barcode3);
			setResult(result);



		/*	if(saleMainFragment.status.equals("CHECKED")){
			//	saleMainFragment=new SaleMainFragment();
			//	saleMainFragment.UpdateProductStockStatus(barcode);
			}*/



			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			data = savedInstanceState.getParcelable(BarcodeScanFragment_preorder_setting.FRAGMENT_DATA);
		}

		ibScanBarcode.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(activity, CaptureActivity.class);

				/*** [START] :: Permission ***/

				/*Intent intent = new IntentIntegrator(activity).createScanIntent();
				startActivityForResult(intent, REQUEST_QR_SCAN);*/

				new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

					@Override
					public void onSuccess(BHPermissions bhPermissions) {
						Intent intent = new IntentIntegrator(activity).createScanIntent();
						startActivityForResult(intent, REQUEST_QR_SCAN);
					}

					@Override
					public void onNotSuccess(BHPermissions bhPermissions) {
						bhPermissions.openAppSettings(getActivity());
					}

					@Override
					public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
						bhPermissions.showMessage(getActivity(), permissionType);
					}

				}, BHPermissions.PermissionType.CAMERA);
				/*** [END] :: Permission ***/


			}
		});
        ibScanBarcode2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(activity, CaptureActivity.class);

                /*** [START] :: Permission ***/

				/*Intent intent = new IntentIntegrator(activity).createScanIntent();
				startActivityForResult(intent, REQUEST_QR_SCAN);*/

                new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

                    @Override
                    public void onSuccess(BHPermissions bhPermissions) {
                        Intent intent = new IntentIntegrator(activity).createScanIntent();
                        startActivityForResult(intent, REQUEST_QR_SCAN2);
                    }

                    @Override
                    public void onNotSuccess(BHPermissions bhPermissions) {
                        bhPermissions.openAppSettings(getActivity());
                    }

                    @Override
                    public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
                        bhPermissions.showMessage(getActivity(), permissionType);
                    }

                }, BHPermissions.PermissionType.CAMERA);
                /*** [END] :: Permission ***/


            }
        });

		ibScanBarcode3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(activity, CaptureActivity.class);

				/*** [START] :: Permission ***/

				/*Intent intent = new IntentIntegrator(activity).createScanIntent();
				startActivityForResult(intent, REQUEST_QR_SCAN);*/

				new BHPermissions().requestPermissions(getActivity(), new BHPermissions.IBHPermissions() {

					@Override
					public void onSuccess(BHPermissions bhPermissions) {
						Intent intent = new IntentIntegrator(activity).createScanIntent();
						startActivityForResult(intent, REQUEST_QR_SCAN3);
					}

					@Override
					public void onNotSuccess(BHPermissions bhPermissions) {
						bhPermissions.openAppSettings(getActivity());
					}

					@Override
					public void onShouldShowRequest(BHPermissions bhPermissions, BHPermissions.PermissionType... permissionType) {
						bhPermissions.showMessage(getActivity(), permissionType);
					}

				}, BHPermissions.PermissionType.CAMERA);
				/*** [END] :: Permission ***/


			}
		});




		if (data != null) {
			if (data.viewTitle != null) {
				vwTitle.setText(data.viewTitle);
			} else if (data.viewTitleResID > -1) {
				vwTitle.setText(data.viewTitleResID);
			}

			if (data.description != null) {
				tvDescription.setText(data.description);
			} else if (data.descriptionResID > -1) {
				tvDescription.setText(data.descriptionResID);
			}
		}

//        if (BHPreference.ProcessType() != null && BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.Sale.toString())){
//            edtBarcode.setEnabled(false);
//        }else {
//            edtBarcode.setEnabled(true);
//        }

		/*if(!BHGeneral.DEVELOPER_MODE){
			edtBarcode.setEnabled(false);
		}*/

		if(BHGeneral.BARCODE_KEY_IN_MODE){
			edtBarcode.setEnabled(true);
		} else {
			edtBarcode.setEnabled(false);
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putParcelable(BarcodeScanFragment_preorder_setting.FRAGMENT_DATA, data);
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_QR_SCAN) {
			if (resultCode == Activity.RESULT_CANCELED) {
				showMessage("ยังไม่ได้ทำการสแกน กรุณาทำการสแกนอีกครั้ง");
			} else if (resultCode == Activity.RESULT_OK) {
				String barcode = intent.getStringExtra(Intents.Scan.RESULT);

                Log.e("barcode",barcode);
				edtBarcode.setText(barcode);

				select_baecode=0;



                String substring_ProductSerialNumber = barcode.substring(0, 1);
              //  String substring_ProductSerialNumber = barcode;


                if(substring_ProductSerialNumber.equals("F")){  //F
                    li_scan2.setVisibility(View.VISIBLE);

					barcode2="";
					barcode3="";
					oncick=0;
                }
                else {

					oncick=1;

                    // Result result = new Result(barcode,"","");
                   // setResult(result);

                }



				String barcode1 = edtBarcode.getText().toString();
				String barcode2 = edtBarcode2.getText().toString();
				String barcode3 = edtBarcode3.getText().toString();


				Result result = new Result(barcode1,barcode2,barcode3);
				setResult(result);



			}
		}
       else if (requestCode == REQUEST_QR_SCAN2) {
            if (resultCode == Activity.RESULT_CANCELED) {
                showMessage("ยังไม่ได้ทำการสแกน กรุณาทำการสแกนอีกครั้ง");
            } else if (resultCode == Activity.RESULT_OK) {
                 barcode2 = intent.getStringExtra(Intents.Scan.RESULT);
                String barcode = edtBarcode.getText().toString();

                Log.e("barcode",barcode2);
				select_baecode=1;
                check_scan2=1;

                String substring_barcode2 = barcode2.substring(0, 1);


                      if((substring_barcode2.equals("R"))|(substring_barcode2.equals("S"))|
                        (substring_barcode2.equals("A"))|(substring_barcode2.equals("U"))|
                              (substring_barcode2.equals("I"))|(substring_barcode2.equals("E"))|
                              (substring_barcode2.equals("G"))|(substring_barcode2.equals("B"))|
                              (substring_barcode2.equals("N"))|(substring_barcode2.equals("M"))|
                              (substring_barcode2.equals("P"))|(substring_barcode2.equals("W"))|
                              (substring_barcode2.equals("C"))|(substring_barcode2.equals("L"))){


                          edtBarcode2.setText(barcode2);
						 // li_scan3.setVisibility(View.VISIBLE);
                         // Result result = new Result(barcode,barcode2,"");
                        //  setResult(result);
                        }
                        else {

						  barcode2="";
                          edtBarcode2.setText("Serial Number เครื่องไม่ถูกต้อง!");
                        //  Result result = new Result(barcode,barcode2);
                        //  setResult(result);
						  showWarningDialog("", "Serial Number เครื่องไม่ถูกต้อง!");

                        }






            }
        }
		else if (requestCode == REQUEST_QR_SCAN3) {
			if (resultCode == Activity.RESULT_CANCELED) {
				showMessage("ยังไม่ได้ทำการสแกน กรุณาทำการสแกนอีกครั้ง");
			} else if (resultCode == Activity.RESULT_OK) {
				barcode3 = intent.getStringExtra(Intents.Scan.RESULT);
				//String barcode = edtBarcode.getText().toString();
				//String barcode2 = edtBarcode2.getText().toString();

				select_baecode=2;

				String substring_barcode3 = barcode3.substring(0, 1);


				if(substring_barcode3.equals("F")){


					edtBarcode3.setText(barcode3);
					// Result result = new Result(barcode,barcode2,"");
					//  setResult(result);
				}
				else {

					barcode3="";
					edtBarcode3.setText("Serial Number เครื่องไม่ถูกต้อง!");
					//  Result result = new Result(barcode,barcode2);
					//  setResult(result);
					showWarningDialog("", "Serial Number เครื่องไม่ถูกต้อง!");

				}







			}
		}



	}

	public void setTitle(int titleResID) {
		data.titleResID = titleResID;
	}

	public void setViewTitle(int viewTitleResID) {
		data.viewTitleResID = viewTitleResID;
	}

	public void setViewTitle(String viewTitle) {
		data.viewTitle = viewTitle;
	}

	public void setDescription(int descriptionResID) {
		data.descriptionResID = descriptionResID;
	}

	public void setDescription(String description) {
		data.description = description;
	}






}
