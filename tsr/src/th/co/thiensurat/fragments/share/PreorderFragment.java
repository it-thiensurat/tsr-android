package th.co.thiensurat.fragments.share;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPermissions;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.GetData_data_product;
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
	private Spinner spDemo;



	private Data data = new Data();
	private ProductStockInfo getProductInfo;
	ProductStockController productStockController;

	List<GetData_data_product> getData_data_products;
	GetData_data_product data_data_product;

	String getProductID="",getProductName="",getProductSerialNumber="",
			getCONTNO="",getRefNo="",getContractReferenceNo="",getReceiptCode="",getReceiptID="";

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

	//String DF= String.valueOf(productStockController.getProductStockByProductSerialNumberzzzz(BHPreference.organizationCode()));
		load_data_product();
		//Log.e("DF",DF);
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




    private void load_data_product() {
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
				call = request.product_UAT(BHPreference.employeeID());
			}
			else {
				call = request.product(BHPreference.employeeID());
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
				adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2);

			}
			catch (Exception ex){

			}

		}

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



					BHApplication.getInstance().getPrefManager().setPreferrence("getRefNo", getRefNo);
					BHApplication.getInstance().getPrefManager().setPreferrence("getContractReferenceNo", getContractReferenceNo);
					BHApplication.getInstance().getPrefManager().setPreferrence("getReceiptCode", getReceiptCode);
					BHApplication.getInstance().getPrefManager().setPreferrence("getReceiptID", getReceiptID);

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
