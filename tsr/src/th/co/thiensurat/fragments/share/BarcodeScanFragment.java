package th.co.thiensurat.fragments.share;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

//import com.google.zxing.client.android.CaptureActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.zxing.client.android.Intents;
import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.adapter.CustomerStatusAdapter;
import th.co.thiensurat.adapter.ProductRecomdAdapter;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.ProductRecomendInfo;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.sales.SaleMainFragment;
import th.co.thiensurat.retrofit.api.Service;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;

public class BarcodeScanFragment extends BHFragment implements ProductRecomdAdapter.ItemClickListener {

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

    @InjectView
	private static RecyclerView recyclerview;
    @InjectView
	private static LinearLayout layout_recomend;

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
			data = savedInstanceState.getParcelable(BarcodeScanFragment.FRAGMENT_DATA);
		}
		BHLoading.show(activity);
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
		getLastLocation();

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
		outState.putParcelable(BarcodeScanFragment.FRAGMENT_DATA, data);
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
				} else {

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

	/**
	 * Edit by Teerayut Klinsanga
	 *
	 * Created date 13/03/2021
	 *
	 */

	static String latitude;
	static String longitude;
	static int PERMISSION_ID = 44;
	static ProductRecomendInfo productRecomendInfo;
	static List<ProductRecomendInfo> productRecomendInfoList;
	static FusedLocationProviderClient mFusedLocationClient;
	static ProductRecomdAdapter productRecomdAdapter;
	private boolean checkPermissions() {
		if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
				ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
			return true;
		}
		return false;
	}

	private void requestPermissions() {
		ActivityCompat.requestPermissions(
				activity,
				new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
				PERMISSION_ID
		);
	}

	private boolean isLocationEnabled() {
		LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
				LocationManager.NETWORK_PROVIDER
		);
	}

	@SuppressLint("MissingPermission")
	public void getLastLocation(){
		if (checkPermissions()) {
			if (isLocationEnabled()) {
				mFusedLocationClient.getLastLocation().addOnCompleteListener(
						new OnCompleteListener<Location>() {
							@Override
							public void onComplete(@NonNull Task<Location> task) {
								Location location = task.getResult();
								if (location == null) {
									requestNewLocationData();
								} else {
//									latitude = location.getLatitude() + "";
//									longitude = location.getLongitude() + "";
									Log.e("Current Latitude1", location.getLatitude()+"");
									Log.e("Current Longitude1", location.getLongitude()+"");
									getProductRecoment(String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude()));
								}
							}
						}
				);
			}
		} else {
			requestPermissions();
		}
	}

	@SuppressLint("MissingPermission")
	private void requestNewLocationData(){
		LocationRequest mLocationRequest = new LocationRequest();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mLocationRequest.setInterval(0);
		mLocationRequest.setFastestInterval(0);
		mLocationRequest.setNumUpdates(1);

		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
		mFusedLocationClient.requestLocationUpdates(
				mLocationRequest, mLocationCallback,
				Looper.myLooper()
		);
	}

	private LocationCallback mLocationCallback = new LocationCallback() {
		@Override
		public void onLocationResult(LocationResult locationResult) {
			Location mLastLocation = locationResult.getLastLocation();
			Log.e("Current Latitude2", mLastLocation.getLatitude()+"");
			Log.e("Current Longitude2", mLastLocation.getLongitude()+"");
//			latitude = mLastLocation.getLatitude() + "";
//			longitude = mLastLocation.getLongitude() + "";
			getProductRecoment(String.valueOf(mLastLocation.getLatitude()), String.valueOf(mLastLocation.getLongitude()));
		}
	};

	private void getProductRecoment(String latitude, String longitude) {
//		latitude = "9.1047298";
//		longitude = "99.3126167";
		try {
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl("https://app.thiensurat.co.th/")
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			Service request = retrofit.create(Service.class);
			Call call = request.getProductRecomend(latitude, longitude);
			call.enqueue(new Callback() {
				@Override
				public void onResponse(Call call, retrofit2.Response response) {
					Gson gson = new Gson();
					Log.e("Json body", String.valueOf(response.body()));
					try {
						JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						productRecomendInfoList = new ArrayList<>();
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							productRecomendInfo = new ProductRecomendInfo();
							productRecomendInfo.setBrandName(object.getString("brandName"));
							productRecomendInfo.setProductCode(object.getString("productCode"));
							productRecomendInfo.setProductName(object.getString("productName"));
							productRecomendInfo.setImgPath(object.getString("imgPath"));
							try {
								productRecomendInfo.setStickerPrice(object.getString("stickerPrice"));
								productRecomendInfo.setRetailPrice(object.getString("retailPrice"));
								productRecomendInfo.setWarranty(object.getString("warranty"));
							} catch (Exception e) {
								productRecomendInfo.setStickerPrice("");
								productRecomendInfo.setRetailPrice("");
								productRecomendInfo.setWarranty("");
							}

							productRecomendInfoList.add(productRecomendInfo);
						}
						setProductRecomed(productRecomendInfoList);
					} catch (JSONException e) {
						e.printStackTrace();
						Log.e("JSONException", e.getLocalizedMessage());
						layout_recomend.setVisibility(View.GONE);
						BHLoading.close();
					}
				}

				@Override
				public void onFailure(Call call, Throwable t) {
					Log.e("onFailure", t.getLocalizedMessage());
					BHLoading.close();
				}
			});

		} catch (Exception e) {
			Log.e("Exception", e.getLocalizedMessage());
			BHLoading.close();
		}
	}

	private void setProductRecomed(List<ProductRecomendInfo> productRecomendList) {
		layout_recomend.setVisibility(View.VISIBLE);
		recyclerview.setLayoutManager(new LinearLayoutManager(activity));
		recyclerview.setHasFixedSize(true);
		productRecomdAdapter = new ProductRecomdAdapter(productRecomendList, activity);
		recyclerview.setAdapter(productRecomdAdapter);
		productRecomdAdapter.setClickListener(this);
		productRecomdAdapter.notifyDataSetChanged();
		BHLoading.close();
	}

	@Override
	public void onItemClick(View view, int position) {

	}
}
