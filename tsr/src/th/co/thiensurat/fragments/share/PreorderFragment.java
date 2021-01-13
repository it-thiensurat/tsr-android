package th.co.thiensurat.fragments.share;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.GetData_data_product;
import th.co.thiensurat.data.GetData_data_product_c;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.sales.SaleMainFragment;
import th.co.thiensurat.retrofit.api.Service;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;

//import com.google.zxing.client.android.CaptureActivity;

public class PreorderFragment extends BHFragment {
	public static abstract class ScanCallBack extends BHFragmentCallback {
		public String onNextClick() {
			return null;
		}
	}

	public static class Result extends BHParcelable {
		public String getProductID,getProductName,getProductSerialNumber,getCONTNO,getRefNo,getContractReferenceNo,getReceiptCode,getReceiptID;

		private Result(String getProductID,String getProductName,
					   String getProductSerialNumber,String getCONTNO,
					   String getRefNo,String getContractReferenceNo,
					   String getReceiptCode,String getReceiptID) {
			this.getProductID = getProductID;
			this.getProductName = getProductName;
			this.getProductSerialNumber = getProductSerialNumber;
			this.getCONTNO = getCONTNO;
			this.getRefNo = getRefNo;
			this.getContractReferenceNo = getContractReferenceNo;
			this.getReceiptCode = getReceiptCode;
			this.getReceiptID = getReceiptID;
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


	 ProductStockInfo productInfo;
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
	private Spinner spDemo,spDemo_c;



	private Data data = new Data();
	private ProductStockInfo getProductInfo;
	ProductStockController productStockController;

	List<GetData_data_product> getData_data_products;
	GetData_data_product data_data_product;

	List<GetData_data_product_c> getData_data_product_cs;
	GetData_data_product_c getData_data_product_c;



	String C_ID="",getProductID="",getProductName="",getProductSerialNumber="",
			getCONTNO="",getRefNo="",getContractReferenceNo="",getReceiptCode="",getReceiptID="",getOrganizationCode="";

	int C_ID_int;
	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_preorder;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_next };
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_sales_preorder;
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {

			case R.string.button_back:
				showLastView();
				break;

		case R.string.button_next:


/*
			String barcode = edtBarcode.getText().toString();
            String barcode2 = edtBarcode2.getText().toString();
			String barcode3 = edtBarcode3.getText().toString();


			Result result = new Result(barcode,barcode2,barcode3);
			setResult(result);

*/

            Result result = new Result(getProductID,getProductName,getProductSerialNumber,getCONTNO,getRefNo,getContractReferenceNo,getReceiptCode,getReceiptID);
            setResult(result);

			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {

		if (savedInstanceState != null) {
			data = savedInstanceState.getParcelable(PreorderFragment.FRAGMENT_DATA);
		}


	//	productStockController=new ProductStockController();
		getData_data_products=new ArrayList<>();
		getData_data_product_cs=new ArrayList<>();

	//String DF= String.valueOf(productStockController.getProductStockByProductSerialNumberzzzz(BHPreference.organizationCode()));
		//load_data_product();
		load_data_product_c();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putParcelable(PreorderFragment.FRAGMENT_DATA, data);
		super.onSaveInstanceState(outState);
	}


	public void setTitle(int titleResID) {
		data.titleResID = titleResID;
	}



	private void load_data_product_c() {
		String MODE=  BHGeneral.SERVICE_MODE.toString();
		try {
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			Service request = retrofit.create(Service.class);
			// Call call = request.product(BHPreference.employeeID());
			Call call=null;
			if(MODE.equals("UAT")){
				call = request.productListPreBooking_UAT(BHPreference.employeeID());
			}
			else {
				call = request.productListPreBooking(BHPreference.employeeID());
			}

			call.enqueue(new Callback() {
				@Override
				public void onResponse(Call call, retrofit2.Response response) {
					Gson gson = new Gson();
					try {
						JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
//                        int item = jsonObject.getJSONArray("data").length();
//                        Log.e("New item", "" + jsonObject.getJSONArray("data"));
						// JSON_PARSE_DATA_AFTER_WEBCALL2(jsonObject.getJSONArray("data"));
						JSON_PARSE_DATA_AFTER_WEBCALL_load_data_product_c(jsonObject.getJSONArray("data"));
						//JSON_PARSE_DATA_AFTER_WEBCALL_TEST(jsonObject.getJSONArray("data"));

					} catch (JSONException e) {
						e.printStackTrace();
						Log.e("data", "22");
					}
				}

				@Override
				public void onFailure(Call call, Throwable t) {
					Log.e("data", "2");
				}
			});

		} catch (Exception e) {
			Log.e("data", "3");
		}
	}

	public void JSON_PARSE_DATA_AFTER_WEBCALL_load_data_product_c(JSONArray array) {

		for (int i = 0; i < array.length(); i++) {

			final GetData_data_product_c GetDataAdapter2 = new GetData_data_product_c();

			JSONObject json = null;
			try {
				json = array.getJSONObject(i);
				GetDataAdapter2.setProductCat(json.getInt("ProductCat"));
				GetDataAdapter2.setProductGroupName(json.getString("ProductGroupName"));



			} catch (JSONException e) {

				e.printStackTrace();
			}
			getData_data_product_cs.add(GetDataAdapter2);
			// value=GetDataAdapter2.getProblemName();
		}





		String[] array2 = new String[getData_data_product_cs.size()];

		//int i;
		ArrayAdapter<String> adapter = null ;

		for ( int i = 0; i < getData_data_product_cs.size(); i++) {
			final GetData_data_product_c contact = getData_data_product_cs.get(i);
			array2[i]= contact.getProductGroupName();

			try {
				adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2){
				@Override
				public View getView(int position, View convertView, ViewGroup parent){
					// Cast the list view each item as text view
					TextView item = (TextView) super.getView(position,convertView,parent);

					// Change the item text size
					item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);

					// return the view
					return item;
				}
			};
			}
			catch (Exception ex){

			}

		}

		spDemo_c.setAdapter(adapter);

		spDemo_c.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();
				try {
					final GetData_data_product_c contact = getData_data_product_cs.get(position);
					C_ID_int= contact.getProductCat();
					Log.e("C_ID", String.valueOf(C_ID_int));
					load_data_product(C_ID_int);

				}
				catch (Exception ex){

				}

			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}






    private void load_data_product(int C_ID_int) {
		 String MODE=  BHGeneral.SERVICE_MODE.toString();
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
           // Call call = request.product(BHPreference.employeeID());
			Call call=null;
			if(MODE.equals("UAT")){
				call = request.product_UAT(BHPreference.employeeID(), String.valueOf(C_ID_int));
			}
			else {
				call = request.product(BHPreference.employeeID(), String.valueOf(C_ID_int));
			}

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
//                        int item = jsonObject.getJSONArray("data").length();
//                        Log.e("New item", "" + jsonObject.getJSONArray("data"));
                        // JSON_PARSE_DATA_AFTER_WEBCALL2(jsonObject.getJSONArray("data"));
                        JSON_PARSE_DATA_AFTER_WEBCALL_load_data_product(jsonObject.getJSONArray("data"));
                        //JSON_PARSE_DATA_AFTER_WEBCALL_TEST(jsonObject.getJSONArray("data"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data", "22");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("data", "2");
                }
            });

        } catch (Exception e) {
            Log.e("data", "3");
        }
    }



	String ID_MAIN="";
	public void JSON_PARSE_DATA_AFTER_WEBCALL_load_data_product(JSONArray array) {
		getData_data_products.clear();
		for (int i = 0; i < array.length(); i++) {

			final GetData_data_product GetDataAdapter2 = new GetData_data_product();

			JSONObject json = null;
			try {
				json = array.getJSONObject(i);
				GetDataAdapter2.setProductName(json.getString("ProductName"));
				GetDataAdapter2.setProductSerialNumber(json.getString("ProductSerialNumber"));
				GetDataAdapter2.setProductID(json.getString("ProductID"));
				GetDataAdapter2.setRefNo(json.getString("RefNo"));
				GetDataAdapter2.setCONTNO(json.getString("CONTNO"));

				GetDataAdapter2.setContractReferenceNo(json.getString("ContractReferenceNo"));
				GetDataAdapter2.setReceiptCode(json.getString("ReceiptCode"));
				GetDataAdapter2.setReceiptID(json.getString("ReceiptID"));
				GetDataAdapter2.setOrganizationCode(json.getString("OrganizationCode"));


			} catch (JSONException e) {

				e.printStackTrace();
			}
			getData_data_products.add(GetDataAdapter2);
			// value=GetDataAdapter2.getProblemName();
		}





		String[] array2 = new String[getData_data_products.size()];

		//int i;
		ArrayAdapter<String> adapter = null ;

		for ( int i = 0; i < getData_data_products.size(); i++) {
			final GetData_data_product contact = getData_data_products.get(i);
			array2[i]= contact.getProductName();

			try {
				//adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2);

		adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2){
					@Override
					public View getView(int position, View convertView, ViewGroup parent){
						// Cast the list view each item as text view
						TextView item = (TextView) super.getView(position,convertView,parent);


						TextView tv = (TextView) item.findViewById(android.R.id.text1);

						tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);

						return item;
					}
				};

			}
			catch (Exception ex){

			}

		}


		adapter.setDropDownViewResource(R.layout.simple_1111);




		spDemo.setAdapter(adapter);

		spDemo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				String item = parent.getItemAtPosition(position).toString();

				try {
					final GetData_data_product contact = getData_data_products.get(position);
					 getProductID= contact.getProductID();
					 getProductName= contact.getProductName();
					 getProductSerialNumber= contact.getProductSerialNumber();
					 getCONTNO= contact.getCONTNO();
					 getRefNo= contact.getRefNo();

					getContractReferenceNo= contact.getContractReferenceNo();
					getReceiptCode= contact.getReceiptCode();
					getReceiptID= contact.getReceiptID();
					getOrganizationCode= contact.getOrganizationCode();

Log.e("getOrganizationCode",getOrganizationCode);

					BHApplication.getInstance().getPrefManager().setPreferrence("getRefNo", getRefNo);
					BHApplication.getInstance().getPrefManager().setPreferrence("getContractReferenceNo", getContractReferenceNo);
					BHApplication.getInstance().getPrefManager().setPreferrence("getReceiptCode", getReceiptCode);
					BHApplication.getInstance().getPrefManager().setPreferrence("getReceiptID", getReceiptID);
					BHApplication.getInstance().getPrefManager().setPreferrence("getOrganizationCode", getOrganizationCode);


					Log.e("getReceiptCode",getReceiptCode);
					Log.e("getProductID",getProductID);


				}
				catch (Exception ex){

				}




				//getData_select_topic_problem_subs.clear();

				// Log.e("idididmain",ID_MAIN);

				//SELECT_DATA_PROBLEM_SUB();

			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}




}
