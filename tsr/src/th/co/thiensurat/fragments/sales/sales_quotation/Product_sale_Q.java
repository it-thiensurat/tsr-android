package th.co.thiensurat.fragments.sales.sales_quotation;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Multipart;
import th.co.bighead.utilities.BHApplication;
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHLoading;
import th.co.bighead.utilities.BHParcelable;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.GetData_data_product;
import th.co.thiensurat.data.GetData_data_product_c;
import th.co.thiensurat.data.controller.ProductStockController;
import th.co.thiensurat.data.info.ProductStockInfo;
import th.co.thiensurat.fragments.sales.SaleFirstPaymentChoiceFragment;
import th.co.thiensurat.fragments.sales.SaleMainFragment;
import th.co.thiensurat.fragments.sales.lead_online.adapter.RecyclerViewDataAdapter;
import th.co.thiensurat.fragments.sales.lead_online.models.Getdata;
import th.co.thiensurat.fragments.sales.sales_quotation.adapter.RecyclerViewDataAdapter_sale_Q;
import th.co.thiensurat.fragments.sales.sales_quotation.models.ProductSpecModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.QuotationWaitModel;
import th.co.thiensurat.fragments.sales.sales_quotation.models.get_product_sale_q;
import th.co.thiensurat.retrofit.api.Service;
import th.co.thiensurat.views.ViewTitle;

import static th.co.thiensurat.retrofit.api.client.BASE_URL;
import static th.co.thiensurat.retrofit.api.client.GIS_BASE_URL;

//import com.google.zxing.client.android.CaptureActivity;

public class Product_sale_Q extends BHFragment implements RecyclerViewDataAdapter_sale_Q.ItemClickListener {

//	public static abstract class ScanCallBack extends BHFragmentCallback {
//		public String onNextClick() {
//			return null;
//		}
//	}

//	public static class Result extends BHParcelable {
//		public String getProductID,getProductName,getProductSerialNumber,getCONTNO,getRefNo,getContractReferenceNo,getReceiptCode,getReceiptID;
//
//		private Result(String getProductID,String getProductName,
//					   String getProductSerialNumber,String getCONTNO,
//					   String getRefNo,String getContractReferenceNo,
//					   String getReceiptCode,String getReceiptID) {
//			this.getProductID = getProductID;
//			this.getProductName = getProductName;
//			this.getProductSerialNumber = getProductSerialNumber;
//			this.getCONTNO = getCONTNO;
//			this.getRefNo = getRefNo;
//			this.getContractReferenceNo = getContractReferenceNo;
//			this.getReceiptCode = getReceiptCode;
//			this.getReceiptID = getReceiptID;
//		}
//	}

//	private static class Data extends BHParcelable {
//		public int titleResID = -1;
//		public int viewTitleResID = -1;
//		public String viewTitle = null;
//		public int descriptionResID = -1;
//		public String description = null;
//	}

	public static class Data extends BHParcelable {
		public String actionType = "";
		public JSONObject objectCustomer;
		public List<get_product_sale_q> objectProduct;
		public List<QuotationWaitModel> quotationWaitModelList;
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

	private String STATUS_CODE = "03";

	@InjectView
	private ViewTitle vwTitle;
	@InjectView
	private Spinner spDemo,spDemo_c;
	@InjectView
	private RecyclerView row1;
	@InjectView
	private Button btn_add;
	@InjectView
	private TextView txtNumber3;
	@InjectView
	private EditText editTextProjectName;

	private Data data;
	private ProductStockInfo getProductInfo;
	ProductStockController productStockController;

	List<GetData_data_product> getData_data_products;
	GetData_data_product data_data_product;

	List<GetData_data_product_c> getData_data_product_cs;
	GetData_data_product_c getData_data_product_c;

	List<get_product_sale_q> selectedProductList = new ArrayList<>();
	List<ProductSpecModel> productSpecModelList = new ArrayList<>();

	String C_ID="",getProductID="",getProductName="",getProductSerialNumber="",
			getCONTNO="",getRefNo="",getContractReferenceNo="",getReceiptCode="",getReceiptID="",getOrganizationCode="", productPrice="";

	int C_ID_int;
	@Override
	protected int fragmentID() {
		// TODO Auto-generated method stub
		return R.layout.fragment_sale_q;
	}

	@Override
	protected int[] processButtons() {
		// TODO Auto-generated method stub
		return new int[] { R.string.button_back, R.string.button_next };
	}

	@Override
	protected int titleID() {
		// TODO Auto-generated method stub
		return R.string.title_sales_quotation;
	}

	@Override
	public void onProcessButtonClicked(int buttonID) {
		// TODO Auto-generated method stub
		switch (buttonID) {
			case R.string.button_back:
				showLastView();
				break;
		case R.string.button_next:
			onNext();
			break;

		default:
			break;
		}
	}

	@Override
	protected void onCreateViewSuccess(Bundle savedInstanceState) {
		vwTitle.setText("เลือกสินค้า");
		data = getData();
		getData_data_products = new ArrayList<>();
		getData_data_product_cs = new ArrayList<>();

		row1.setLayoutManager(new LinearLayoutManager(activity));
		row1.setHasFixedSize(true);
		adapter = new RecyclerViewDataAdapter_sale_Q();
		row1.setAdapter(adapter);

		load_data_product_c();
		btn_add.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addProduct();
			}
		});

		if (data.actionType.equals("edit")) {
			try {
				if (data.objectProduct.size() > 0) {
					selectedProductList = data.objectProduct;
					adapter.setSelectedProductList(selectedProductList);
					adapter.notifyDataSetChanged();
				}
			} catch (Exception e) {
				Log.e("Edit quotation", e.getLocalizedMessage());
			}
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
	}


	private void load_data_product_c() {
		String MODE=  BHGeneral.SERVICE_MODE.toString();
		try {
			Retrofit retrofit = new Retrofit.Builder()
					.baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create())
					.build();
			Service request = retrofit.create(Service.class);
			Call call=null;
			if(MODE.equals("UAT")){
				call = request.productListPreBooking_UAT(BHPreference.employeeID());
			} else {
				call = request.productListPreBooking(BHPreference.employeeID());
			}

			call.enqueue(new Callback() {
				@Override
				public void onResponse(Call call, retrofit2.Response response) {
					Gson gson = new Gson();
					try {
						JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
						Log.e("product group", String.valueOf(jsonObject.getJSONArray("data")));
						JSON_PARSE_DATA_AFTER_WEBCALL_load_data_product_c(jsonObject.getJSONArray("data"));
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
		String[] array2 = new String[array.length()];
		for (int i = 0; i < array.length(); i++) {
			final GetData_data_product_c GetDataAdapter2 = new GetData_data_product_c();
			JSONObject json = null;
			try {
				json = array.getJSONObject(i);
				GetDataAdapter2.setProductCat(json.getInt("ProductCat"));
				GetDataAdapter2.setProductGroupName(json.getString("ProductGroupName"));
				array2[i]= json.getString("ProductGroupName");
			} catch (JSONException e) {
				e.printStackTrace();
			}

			getData_data_product_cs.add(GetDataAdapter2);
		}

		ArrayAdapter<String> adapter = null ;
		try {
			adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2){
				@Override
				public View getView(int position, View convertView, ViewGroup parent){
					TextView item = (TextView) super.getView(position,convertView,parent);
					item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,16);
					return item;
				}
			};
		} catch (Exception ex){

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
				} catch (Exception ex){

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
			Call call=null;
			if(MODE.equals("UAT")){
				call = request.product_UAT(BHPreference.employeeID(), String.valueOf(C_ID_int));
			} else {
				call = request.product(BHPreference.employeeID(), String.valueOf(C_ID_int));
			}

            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Gson gson = new Gson();
                    try {
                        JSONObject jsonObject = new JSONObject(gson.toJson(response.body()));
						Log.e("product", String.valueOf(jsonObject.getJSONArray("data")));
                        JSON_PARSE_DATA_AFTER_WEBCALL_load_data_product(jsonObject.getJSONArray("data"));
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
		String[] array2 = new String[array.length()];
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
				GetDataAdapter2.setProductPrice(String.valueOf(json.getInt("ProductPrice")));

				array2[i]= json.getString("ProductName");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			getData_data_products.add(GetDataAdapter2);
		}

		ArrayAdapter<String> adapter = null ;
		try {
			adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1, android.R.id.text1, array2){
				@Override
				public View getView(int position, View convertView, ViewGroup parent){
					TextView item = (TextView) super.getView(position,convertView,parent);
					TextView tv = (TextView) item.findViewById(android.R.id.text1);
					tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
					return item;
				}
			};
		} catch (Exception ex){

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
					getOrganizationCode= contact.getOrganizationCode();
					productPrice = contact.getProductPrice();
				} catch (Exception ex){

				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}

	RecyclerViewDataAdapter_sale_Q adapter;
	private void addProduct() {
		get_product_sale_q productList = new get_product_sale_q();
		productList.setProduct_id(getProductID);
		productList.setProduct_name(getProductName);
		productList.setProduct_qty("1");
		productList.setProduct_price(productPrice);
			for (int i = 0; i < selectedProductList.size(); i++) {
				get_product_sale_q productSaleQ = selectedProductList.get(i);
				if (getProductID == productSaleQ.getProduct_id()) {
					int qty = Integer.parseInt(productSaleQ.getProduct_qty()) + 1;
					productSaleQ.setProduct_qty(String.valueOf(qty));
					selectedProductList.set(i, productSaleQ);
					adapter.notifyDataSetChanged();
					return;
				}
			}

		selectedProductList.add(productList);
		adapter.setSelectedProductList(selectedProductList);

		adapter.notifyDataSetChanged();
		adapter.setClickListener(this);
	}

	@Override
	public void onItemClick(View view, int position) {

	}

	@Override
	public void onDecrease(View view, int position) {
		get_product_sale_q productSaleQ = selectedProductList.get(position);
		if (selectedProductList.size() > 0) {
			int qty = Integer.parseInt(productSaleQ.getProduct_qty());
			if (qty > 1) {
				qty--;
				productSaleQ.setProduct_qty(String.valueOf(qty));
				selectedProductList.set(position, productSaleQ);
			} else {
				selectedProductList.remove(position);
			}
		}

		adapter.setSelectedProductList(selectedProductList);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onIncrease(View view, int position) {
		get_product_sale_q productSaleQ = selectedProductList.get(position);
		int qty = Integer.parseInt(productSaleQ.getProduct_qty());
		qty++;
		productSaleQ.setProduct_qty(String.valueOf(qty));
		selectedProductList.set(position, productSaleQ);
		adapter.setSelectedProductList(selectedProductList);
		adapter.notifyDataSetChanged();

	}

	@Override
	public void onRemove(View view, int position) {
		selectedProductList.remove(position);
		adapter.setSelectedProductList(selectedProductList);
		adapter.notifyDataSetChanged();
	}

	private void onNext() {
		if (selectedProductList.size() > 0) {
			Log.e("select product", String.valueOf(selectedProductList));
			QuotationPromotion.Data qData = new QuotationPromotion.Data();
			qData.actionType = data.actionType;
			qData.projectName = editTextProjectName.getText().toString();
			qData.objectCustomer = data.objectCustomer;
			qData.objectProduct = selectedProductList;
			qData.quotationWaitModelList = data.quotationWaitModelList;
			QuotationPromotion quotationPromotion = BHFragment.newInstance(QuotationPromotion.class, qData);
			showNextView(quotationPromotion);
		} else {
			String title = "คำเตือน";
			String message = "กรุณาเลือกข้อมูลสินค้าก่อน";
			showWarningDialog(title, message);
		}
	}
}
