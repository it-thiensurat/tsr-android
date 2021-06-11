package th.co.thiensurat.fragments.sales.sales_quotation;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHArrayAdapter;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHPagerFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHUtilities;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.controller.ProductStockController.ProductStockStatus;
import th.co.thiensurat.data.controller.SalePaymentPeriodController;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.ContractInfo.ContractStatusName;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.data.info.SalePaymentPeriodInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment.ProcessType;
import th.co.thiensurat.fragments.sales.preorder.models.Get_data_api3;
import th.co.thiensurat.fragments.sales.sales_quotation.models.CustomerAPModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.ProductSpecModel;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;
import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

public class SaleMainUnfinishedFragment_sale_Q extends BHPagerFragment {

	@InjectView
	private TextView textViewUnfinished;
	@InjectView
	private ListView listViewUnfinish;
	private List<CustomerAPModel> customerList;

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_sales_preorder;
	}

	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_main_unfinished;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		customerList = new ArrayList<>();
		getCustomerList();
	}

	private void getCustomerList() {
		try {
			Call call = null;
			Service request = null;
			Retrofit retrofit = null;
			BHLoading.show(activity);
			if (BHGeneral.SERVICE_MODE.toString() == "UAT") {
				retrofit = new Retrofit.Builder()
						.baseUrl(GIS_BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				request = retrofit.create(Service.class);
				call = request.getCustomerListUAT(BHPreference.employeeID());
			}

			call.enqueue(new Callback() {
				@Override
				public void onResponse(Call call, retrofit2.Response response) {
					Gson gson=new Gson();
					try {
						Log.e("Response", String.valueOf(response));
						Log.e("JSON body", String.valueOf(response.body()));
						JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
						if (jsonObject.getString("status").equals("SUCCESS")) {
							JSONArray jsonArray = jsonObject.getJSONArray("data");
							CustomerAPModel customerAPModel;
							for (int i = 0; i < jsonArray.length(); i++) {
								JSONObject object = jsonArray.getJSONObject(i);
								customerAPModel = new CustomerAPModel();
								customerAPModel.setCustomerId(object.getInt("APCUS_ID"));
								customerAPModel.setCustomerName(object.getString("APCUS_NAME"));
								customerAPModel.setCustomerTax(object.getString("APCUS_IDCARD"));
								customerAPModel.setCustomerType(object.getInt("APCUS_TYPE"));
								customerAPModel.setCustomerAddr(object.getString("APCUS_ADDR"));
								customerAPModel.setCustmerMoo(object.getInt("APCUS_MOO"));
								customerAPModel.setCustomerSoi(object.getString("APCUS_SOI"));
								customerAPModel.setCustomerRoad(object.getString("APCUS_ROAD"));
								customerAPModel.setCustomerProvince(object.getInt("APCUS_PROVINCE_ID"));
								customerAPModel.setCustomerDistrict(object.getInt("APCUS_DISTRICT_ID"));
								customerAPModel.setCustomerSubdistrict(object.getInt("APCUS_SUBDISTRICT_ID"));
								customerAPModel.setCustomerZipcode(object.getString("APCUS_ZIPCODE"));
								customerAPModel.setCustomerPhone(object.getString("APCUS_PHONE"));
								customerAPModel.setCustomerEmail(object.getString("APCUS_EMAIL"));
								customerAPModel.setCustomerContactName(object.getString("APCUS_CONTACT_NAME"));
								customerAPModel.setCustomerContactPhone(object.getString("APCUS_CONTACT_PHONE"));
								customerAPModel.setCustomerContactEmail(object.getString("APCUS_CONTACT_EMAIL"));

								customerList.add(customerAPModel);
							}

							setUI();
						}
						Log.e("Get customer", String.valueOf(jsonObject));
						BHLoading.close();
					} catch (JSONException e) {
						e.printStackTrace();
						Log.e("data",e.getLocalizedMessage());
						BHLoading.close();
					}
				}

				@Override
				public void onFailure(Call call, Throwable t) {
					Log.e("onFailure Get customer:",t.getLocalizedMessage());
					BHLoading.close();
				}
			});
		} catch (Exception e) {
			Log.e("Exception Get customer",e.getLocalizedMessage());
			BHLoading.close();
		}
	}

	private void setUI() {
		textViewUnfinished.setVisibility(View.INVISIBLE);
		BHArrayAdapter<CustomerAPModel> customerAPModelBHArrayAdapter = new BHArrayAdapter<CustomerAPModel>(activity, R.layout.list_main_status, customerList) {
			@Override
			protected void onViewItem(int position, View view, Object holder, CustomerAPModel info) {
				ViewHolder vh = (ViewHolder) holder;
				vh.textViewContractnumber.setText("เลขที่ใบเสนอราคา  :  -");
				vh.textViewName.setText          ("ชื่อลูกค้า        :  "+ BHUtilities.trim(info.getCustomerName()));
				vh.textViewStatus.setText        ("สถานะ           :  ยังไม่ได้ออกใบเสนอราคา");

				vh.imageDelete.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						LayoutInflater layoutInflaterAndroid = LayoutInflater.from(activity);
						View mView = layoutInflaterAndroid.inflate(R.layout.custom_dialog_delete, null);
						Builder alertDialogBuilderUserInput = new Builder(activity);
						alertDialogBuilderUserInput.setView(mView);


						final TextView dialogTitle2 = (TextView) mView.findViewById(R.id.dialogTitle2);
						final EditText userInputDialogEditText = (EditText) mView.findViewById(R.id.userInputDialog);

						dialogTitle2.setText("คุณต้องการลบข้อมูลหรือไม่?");
						alertDialogBuilderUserInput
								.setCancelable(false)
								.setPositiveButton("ตกลง",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialogBox, int id) {
												String OK = userInputDialogEditText.getText().toString();
												if(OK.equals("ตกลง")){
													delete(info.getCustomerId());
												}
											}
								}).setNegativeButton("ยกเลิก",
										new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialogBox, int id) {
												dialogBox.cancel();
											}
										});

						AlertDialog alertDialogAndroid = alertDialogBuilderUserInput.create();
						alertDialogAndroid.show();
					}
				});
			}

			class ViewHolder {
				public TextView textViewContractnumber;
				public TextView textViewName;
				public TextView textViewStatus;
				public ImageView imageDelete;
				public LinearLayout aaa;
			}
		};

		listViewUnfinish.setAdapter(customerAPModelBHArrayAdapter);
		listViewUnfinish.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				CustomerAPModel customerAPModel = customerList.get(position);
				List<CustomerAPModel> customerAPModelList = new ArrayList<>();
				customerAPModelList.add(customerAPModel);
				New2SaleCustomerAddressCardFragment_sale_Q.Data data = new New2SaleCustomerAddressCardFragment_sale_Q.Data();
				data.actionType = "edit";
				data.customerAPModelList = customerAPModelList;

				Log.e("Selected", String.valueOf(customerAPModelList));

				New2SaleCustomerAddressCardFragment_sale_Q unfinish = BHFragment.newInstance(New2SaleCustomerAddressCardFragment_sale_Q.class, data);
				showNextView(unfinish);
			}
		});
	}

	private void delete(int customerId) {
		try {
			Call call = null;
			Service request = null;
			Retrofit retrofit = null;

			if (BHGeneral.SERVICE_MODE.toString() == "UAT") {
				retrofit = new Retrofit.Builder()
						.baseUrl(GIS_BASE_URL)
						.addConverterFactory(GsonConverterFactory.create())
						.build();
				request = retrofit.create(Service.class);
				call = request.removeCustomerUAT(BHPreference.employeeID(), String.valueOf(customerId));
			}

			call.enqueue(new Callback() {
				@Override
				public void onResponse(Call call, retrofit2.Response response) {
					Gson gson=new Gson();
					try {
						Log.e("Response", String.valueOf(response));
						Log.e("JSON body", String.valueOf(response.body()));
						JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
						if (jsonObject.getString("status").equals("SUCCESS")) {
							String msg = jsonObject.getString("message");
							AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
							alertDialog.setTitle("ลบ");
							alertDialog.setMessage(msg);
							alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											customerList.clear();
											dialog.dismiss();
											getCustomerList();
										}
									});
							alertDialog.show();
						}
						Log.e("Get customer", String.valueOf(jsonObject));
						BHLoading.close();
					} catch (JSONException e) {
						e.printStackTrace();
						Log.e("data",e.getLocalizedMessage());
						BHLoading.close();
					}
				}

				@Override
				public void onFailure(Call call, Throwable t) {
					Log.e("onFailure Get customer:",t.getLocalizedMessage());
					BHLoading.close();
				}
			});
		} catch (Exception e) {
			Log.e("Exception Get customer",e.getLocalizedMessage());
			BHLoading.close();
		}
	}
}
