package th.co.thiensurat.fragments.sales;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import th.co.bighead.utilities.BHFragment;
import th.co.bighead.utilities.BHPreference;
import th.co.bighead.utilities.BHSpinnerAdapter;
import th.co.bighead.utilities.annotation.InjectView;
import th.co.thiensurat.R;
import th.co.thiensurat.activities.SurveyActivity;
import th.co.thiensurat.business.controller.BackgroundProcess;
import th.co.thiensurat.business.controller.TSRController;
import th.co.thiensurat.data.info.CareerInfo;
import th.co.thiensurat.data.info.ContractInfo;
import th.co.thiensurat.data.info.DebtorCustomerInfo;
import th.co.thiensurat.data.info.HabitatTypeInfo;
import th.co.thiensurat.data.info.HobbyInfo;
import th.co.thiensurat.data.info.SuggestionInfo;
import th.co.thiensurat.fragments.credit.Audit.CheckCustomersMainFragment;
import th.co.thiensurat.retrofit.api.Service;

import static th.co.thiensurat.fragments.sales.New2SaleCustomerAddressCardFragment.check_box_status;
import static th.co.thiensurat.fragments.sales.New2SaleCustomerAddressCardFragment.status;
import static th.co.thiensurat.retrofit.api.client.BASE_URL;

public class SaleMoreDetailAddress extends BHFragment {

    @InjectView
    EditText editTextHabitat, editTextCareer, editTextHobby, editTextSuggestion, editTextReferencePersonName, editTextReferencePersonTelephone;
    @InjectView
    private LinearLayout linearLayoutHeadNumber;
    @InjectView
    private TextView txtNumber1;
    @InjectView
    private TextView txtNumber2;
    @InjectView
    private TextView txtNumber3;
    @InjectView
    private TextView txtNumber4;
    @InjectView
    private TextView txtNumber5;
    @InjectView
    Spinner spinnerHabitat, spinnerStatusLive, spinnerCareer, spinnerHobby, spinnerSuggestion;

    private List<HabitatTypeInfo> HabitatTypeList;
    private List<CareerInfo> CareerList;
    private List<HobbyInfo> HobbyList;
    private List<SuggestionInfo> SuggestionList;
    private DebtorCustomerInfo debcustomer;
    private ContractInfo contract;

    private String HabitatCode, HabitatDetail;
    private String CareerCode, CareerDetail;
    private String HobbyCode, HobbyDetail;
    private String SuggestionCode, SuggestionDetail;

    public static String FRAGMENT_SALE_MOREDETAIL = "sale_more_detail_fragment";


    @Override
    protected int titleID() {
        // TODO Auto-generated method stub
        return R.string.caption_customer_detail_more;
    }

    @Override
    protected int fragmentID() {
        // TODO Auto-generated method stub
        return R.layout.fragment_sale_more_detail_address;
    }

    @Override
    protected int[] processButtons() {
        // TODO Auto-generated method stub
        return new int[]{R.string.button_back, R.string.button_survey_befor_contract_confirm, R.string.button_end};
    }


    @Override
    public void onResume() {
        super.onResume();
        new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                if (!BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
                    TSRController.updateStatusCode(BHPreference.RefNo(), "12");
                }
                else {
                    TSRController.updateStatusCode(BHPreference.RefNo(), "12");

                }
            }
        }.start();
    }

    @Override
    protected void onCreateViewSuccess(Bundle savedInstanceState) {

        if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
            linearLayoutHeadNumber.setVisibility(View.GONE);
        }

        txtNumber1.setText("...");
        txtNumber2.setText("8");
        txtNumber3.setText("9");
        txtNumber4.setText("10");
        txtNumber5.setText("11");
        txtNumber5.setBackgroundResource(R.drawable.circle_number_sale_color_red);


        Log.e("vvvv","final");
        checkHasSurvey();
        BindingSpinner();
        GetContractData();
    }

    @Override
    public void onProcessButtonClicked(int buttonID) {
        // TODO Auto-generated method stub
        switch (buttonID) {
            case R.string.button_end:


                UpDateDebCustomer();


                break;
            case R.string.button_back:
                showLastView();
                break;
            case R.string.button_survey_befor_contract_confirm:
                SurveyPage();
                break;
            default:
                break;
        }
    }

    private void BindingSpinner() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            List<HabitatTypeInfo> HabitatTypeoutput = new ArrayList<HabitatTypeInfo>();
            List<CareerInfo> Careeroutput = new ArrayList<CareerInfo>();
            List<HobbyInfo> Hobbyoutput = new ArrayList<HobbyInfo>();
            List<SuggestionInfo> Suggestionoutput = new ArrayList<SuggestionInfo>();

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                HabitatTypeoutput = getHabitatType();
                Careeroutput = getCareer();
                Hobbyoutput = getHobby();
                Suggestionoutput = getSuggestion();
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub

                /** (1) Habitat-Type : ประเภทที่อยู่อาศัย **/
                if (HabitatTypeoutput != null) {
                    HabitatTypeList = HabitatTypeoutput;
                    List<String> HabitatType = new ArrayList<String>();
                    HabitatType.add(String.format(" "));
                    for (HabitatTypeInfo item : HabitatTypeList) {
                        HabitatType.add(String.format(item.HabitatTypeName));

                    }

                    BHSpinnerAdapter<String> arrayHabitatType = new BHSpinnerAdapter<String>(activity, HabitatType);
                    spinnerHabitat.setAdapter(arrayHabitatType);
                    spinnerHabitat.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // TODO Auto-generated method stub
                            if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                                HabitatCode = HabitatTypeList.get(position - 1).HabitatTypeCode;
                                HabitatDetail = HabitatTypeList.get(position - 1).HabitatTypeName;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                }

                /** (2) Career : อาชีพ **/
                if (Careeroutput != null) {
                    CareerList = Careeroutput;
                    List<String> Career = new ArrayList<String>();
                    Career.add(String.format("  "));
                    for (CareerInfo item : CareerList) {
                        Career.add(String.format(item.CareerName));
                    }
                    BHSpinnerAdapter<String> arrayCareer = new BHSpinnerAdapter<String>(activity, Career);
                    spinnerCareer.setAdapter(arrayCareer);
                    spinnerCareer.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // TODO Auto-generated method stub
                            if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                                CareerCode = CareerList.get(position - 1).CareerCode;
                                CareerDetail = CareerList.get(position - 1).CareerName;

                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });

                }

                /** (3) Hobby : งานอดิเรก **/
                if (Hobbyoutput != null) {
                    HobbyList = Hobbyoutput;
                    List<String> Hobby = new ArrayList<String>();
                    Hobby.add(String.format(" "));
                    for (HobbyInfo item : HobbyList) {
                        Hobby.add(String.format(item.HobbyName));
                    }

                    BHSpinnerAdapter<String> arraryHobby = new BHSpinnerAdapter<String>(activity, Hobby);
                    spinnerHobby.setAdapter(arraryHobby);
                    spinnerHobby.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // TODO Auto-generated method stub
                            if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                                HobbyCode = HobbyList.get(position - 1).HobbyCode;
                                HobbyDetail = HobbyList.get(position - 1).HobbyName;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });
                }

                /** (4) Suggestion : ข้อเสนอแนะ **/
                if (Suggestionoutput != null) {
                    SuggestionList = Suggestionoutput;
                    List<String> Suggestion = new ArrayList<String>();
                    Suggestion.add(String.format(" "));
                    for (SuggestionInfo item : SuggestionList) {
                        Suggestion.add(String.format(item.SuggestionName));
                    }

                    BHSpinnerAdapter<String> arraySuggestion = new BHSpinnerAdapter<String>(activity, Suggestion);
                    spinnerSuggestion.setAdapter(arraySuggestion);
                    spinnerSuggestion.setOnItemSelectedListener(new OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            // TODO Auto-generated method stub
                            if (!parent.getItemAtPosition(position).toString().trim().equals("")) {
                                SuggestionCode = SuggestionList.get(position - 1).SuggestionCode;
                                SuggestionDetail = SuggestionList.get(position - 1).SuggestionName;
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub
                        }
                    });
                }

            }
        }).start();

        /** (5) สถานะผู้อาศัย (Array-Value: เจ้าของ / ผู้อยู่อาศัย / ผู้เช่า) **/
        spinnerStatusLive.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                ((TextView) parent.getChildAt(0)).setTextSize(20);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    private void GetContractData() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                contract = getContract(BHPreference.RefNo());
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (contract != null) {
                    BindingDebCustomer();
                }
            }
        }).start();
    }

    private void BindingDebCustomer() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                debcustomer = getDebCustometByID(contract.CustomerID);
            }

            @Override
            protected void after() {

                if (debcustomer != null) {

                    /** Get specific data for selecting spinner **/
                    (new BackgroundProcess(activity) {
                        HabitatTypeInfo habitatTypeInfo = new HabitatTypeInfo();
                        CareerInfo careerInfo = new CareerInfo();
                        HobbyInfo hobbyInfo = new HobbyInfo();
                        SuggestionInfo suggestionInfo = new SuggestionInfo();

                        @Override
                        protected void calling() {
                            // TODO Auto-generated method stub
                            habitatTypeInfo = getHabitatTypeByHabitatTypeCode(isnull(debcustomer.HabitatTypeCode, ""));
                            careerInfo = getCareerByCareerCode(isnull(debcustomer.CareerCode, ""));
                            hobbyInfo = getHobbyByHobbyCode(isnull(debcustomer.HobbyCode, ""));
                            suggestionInfo = getSuggestionBySuggestionCode(isnull(debcustomer.SuggestionCode, ""));
                        }

                        @Override
                        protected void after() {
                            /** บุคคลอ้างอิง **/
                            //ชื่อ-นามสกุล
                            if (debcustomer.ReferencePersonName != null) {
                                editTextReferencePersonName.setText(debcustomer.ReferencePersonName);
                            }

                            //เบอร์โทรติดต่อ
                            if (debcustomer.ReferencePersonTelephone != null) {
                                editTextReferencePersonTelephone.setText(debcustomer.ReferencePersonTelephone);
                            }

                            /** (1) Habitat-Type : ประเภทที่อยู่อาศัย **/
                            ArrayAdapter spinnerHabitatAdapter = (ArrayAdapter) spinnerHabitat.getAdapter();
                            if (debcustomer.HabitatTypeCode != null) {
                                if (habitatTypeInfo != null) {
                                    int spinnerHabitatPosition = spinnerHabitatAdapter.getPosition(habitatTypeInfo.HabitatTypeName);
                                    spinnerHabitat.setSelection(spinnerHabitatPosition);
                                }
                            }
                            //อื่นๆ
                            if (debcustomer.HabitatDetail != null) {
                                editTextHabitat.setText(debcustomer.HabitatDetail);
                            }

                            /** (2) Career : อาชีพ **/
                            ArrayAdapter spinnerCareerAdapter = (ArrayAdapter) spinnerCareer.getAdapter();
                            if (debcustomer.CareerCode != null) {
                                if (careerInfo != null) {
                                    int spinnerCareerPosition = spinnerCareerAdapter.getPosition(careerInfo.CareerName);
                                    spinnerCareer.setSelection(spinnerCareerPosition);
                                }
                            }
                            //อื่นๆ
                            if (debcustomer.CareerDetail != null) {
                                editTextCareer.setText(debcustomer.CareerDetail);
                            }

                            /** (3) Hobby : งานอดิเรก **/
                            ArrayAdapter spinnerHobbyAdapter = (ArrayAdapter) spinnerHobby.getAdapter();
                            if (debcustomer.HobbyCode != null) {
                                if (hobbyInfo != null) {
                                    int spinnerHobbyPosition = spinnerHobbyAdapter.getPosition(hobbyInfo.HobbyName);
                                    spinnerHobby.setSelection(spinnerHobbyPosition);
                                }
                            }
                            //อื่นๆ
                            if (debcustomer.HobbyDetail != null) {
                                editTextHobby.setText(debcustomer.HobbyDetail);
                            }

                            /** (4) Suggestion : ข้อเสนอแนะ **/
                            ArrayAdapter spinnerSuggestionAdapter = (ArrayAdapter) spinnerSuggestion.getAdapter();
                            if (debcustomer.SuggestionCode != null) {
                                if (suggestionInfo != null) {
                                    int spinnerSuggestionPosition = spinnerSuggestionAdapter.getPosition(suggestionInfo.SuggestionName);
                                    spinnerSuggestion.setSelection(spinnerSuggestionPosition);
                                }
                            }
                            //อื่นๆ
                            if (debcustomer.SuggestionDetail != null) {
                                editTextSuggestion.setText(debcustomer.SuggestionDetail);
                            }

                            /** (5) สถานะผู้อาศัย (Array-Value: เจ้าของ / ผู้อยู่อาศัย / ผู้เช่า) **/
                            ArrayAdapter spinnerStatusLiveAdapter = (ArrayAdapter) spinnerStatusLive.getAdapter();
                            if (debcustomer.OccupyType != null) {
                                int spinnerStatusLivePosition = spinnerStatusLiveAdapter.getPosition(debcustomer.OccupyType);
                                spinnerStatusLive.setSelection(spinnerStatusLivePosition);
                            }

                        }    // after for selecting spinner
                    }).start();
                }    // if (debcustomer != null)
            }    // after for get contract data
        }).start();
    }

    public String isnull(String str, String strReturn) {

        if (str != null) {
            return str;
        }

        return strReturn;

    }

    private void UpDateDebCustomer() {
        // TODO Auto-generated method stub
        (new BackgroundProcess(activity) {
            DebtorCustomerInfo debcustomerReferencePerson = new DebtorCustomerInfo();

            @Override
            protected void before() {
                // TODO Auto-generated method stub
                //debcustomer.CustomerID = BHPreference.CustomerID();
                /*debcustomer.HabitatTypeCode = HabitatCode;
				debcustomer.HabitatDetail = HabitatDetail;
				debcustomer.OccupyType = spinnerStatusLive.getSelectedItem().toString();
				debcustomer.CareerCode = CareerCode;
				debcustomer.CareerDetail = CareerDetail;
				debcustomer.HobbyCode = HobbyCode;
				debcustomer.HobbyDetail = HobbyDetail;
				debcustomer.IsUsedProduct = "0";
				debcustomer.UsedProductModelID = "";
				debcustomer.SuggestionCode = SuggestionCode;
				debcustomer.SuggestionDetail = SuggestionDetail;*/

                debcustomer.HabitatTypeCode = HabitatCode;
                debcustomer.HabitatDetail = editTextHabitat.getText().toString();
                debcustomer.OccupyType = spinnerStatusLive.getSelectedItem().toString();
                debcustomer.CareerCode = CareerCode;
                debcustomer.CareerDetail = editTextCareer.getText().toString();
                debcustomer.HobbyCode = HobbyCode;
                debcustomer.HobbyDetail = editTextHobby.getText().toString();
                debcustomer.IsUsedProduct = false;

              //  debcustomer.UsedProductModelID = "";



                try {
                    if (debcustomer.UsedProductModelID.equals("VIP")) {

                        // checkBoxvip.se(false);
                        debcustomer.UsedProductModelID = "VIP";// ชื่อกรรมการผู้มีอำนาจ
                    } else {
                        debcustomer.UsedProductModelID = "";// ชื่อกรรมการผู้มีอำนาจ

                    }
                }
                catch (Exception ex){

                }




        /*        if(status.equals("OK")){
                    //cust.OrganizationCode = "3";// ชื่อกรรมการผู้มีอำนาจ

                    if(check_box_status==1){
                        debcustomer.UsedProductModelID = "VIP";// ชื่อกรรมการผู้มีอำนาจ

                    }
                    else {
                        debcustomer.UsedProductModelID = "";// ชื่อกรรมการผู้มีอำนาจ

                    }


                }
                else {
                    debcustomer.UsedProductModelID = "";// ชื่อกรรมการผู้มีอำนาจ

                    // cust.OrganizationCode = BHPreference.organizationCode();

                }*/



                debcustomer.SuggestionCode = SuggestionCode;
                debcustomer.SuggestionDetail = editTextSuggestion.getText().toString();

                /*** [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/
                debcustomerReferencePerson.CustomerID = debcustomer.CustomerID;
                debcustomerReferencePerson.OrganizationCode = debcustomer.OrganizationCode;
                debcustomerReferencePerson.LastUpdateBy = BHPreference.employeeID();
                debcustomerReferencePerson.LastUpdateDate = new Date();
                debcustomerReferencePerson.ReferencePersonName = editTextReferencePersonName.getText().toString();
                debcustomerReferencePerson.ReferencePersonTelephone = editTextReferencePersonTelephone.getText().toString();
                /*** [START] :: Fixed - [BHPROJ-0024-710] :: [ระบบงานขาย-บันทึกข้อมูลลูกค้าเพิ่มเติม] เพิ่มให้สามารถระบุ ชื่อ + เบอร์ติดต่อ บุคคลอ้างอิง ได้  ***/
            }

            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                // saveDebtorCustomer(debcustomer);
                saveCustomerData(debcustomer, true);
                updateReferencePerson(debcustomerReferencePerson, true);
            }

            @Override
            protected void after() {
                // TODO Auto-generated method stub
                if (BHPreference.ProcessType().equals(SaleFirstPaymentChoiceFragment.ProcessType.EditContract.toString())) {
                    activity.showView(new EditContractsMainFragment());
                } else {
                    saveStatusCode();
                }
            }

        }).start();
    }

    private void saveStatusCode() {
        (new BackgroundProcess(activity) {
            @Override
            protected void calling() {
                // TODO Auto-generated method stub
                TSRController.updateStatusCode(BHPreference.RefNo(), "13");
            }

            @Override
            protected void after() {
                showLastView(FRAGMENT_SALE_MOREDETAIL);
            }
        }).start();
    }

    /**
     * Edit by Teerayut Klinsanga
     * 05/10/2020
     */
    private void SurveyPage() {
        Intent intent = new Intent(getContext(), SurveyActivity.class);
        intent.putExtra("REFERRENCE_NUMBER", contract.RefNo);
        intent.putExtra("CONTRACT_NUMBER", contract.CONTNO);
        intent.putExtra("EMPLOYEE_NUMBER", contract.SaleEmployeeCode);
        startActivityForResult(intent, 333);
    }

    private void checkHasSurvey() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            Service request = retrofit.create(Service.class);
            Call call = request.checkQuestion(contract.CONTNO);
            call.enqueue(new Callback() {
                @Override
                public void onResponse(Call call, retrofit2.Response response) {
                    Log.e("Survey contno", contract.CONTNO);
                    Gson gson=new Gson();
                    try {
                        JSONObject jsonObject=new JSONObject(gson.toJson(response.body()));
                        Log.e("json question: ",jsonObject.toString());
                        JSONArray array = jsonObject.getJSONArray("data");
                        JSONObject obj = null;
                        for (int i = 0; i < array.length(); i++) {
                            obj = array.getJSONObject(i);
                            String status = obj.getString("Status");
                            Log.e("Question status", status);
//                            if ("Error".equals(status)) {
//                                List<Integer> listId = new ArrayList<Integer>();
//                                listId.add(R.string.button_pay);
//                                listId.add(R.string.button_print);
//
//                                activity.setViewProcessButtons(listId, View.GONE);
//                            } else {
//                                List<Integer> listId = new ArrayList<Integer>();
//                                listId.add(R.string.button_pay);
//                                listId.add(R.string.button_print);
//
//                                activity.setViewProcessButtons(listId, View.VISIBLE);
//                            }
                        }
//                        layoutSurvey.setVisibility(View.VISIBLE);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("data","22");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    Log.e("onFailure question:",t.getLocalizedMessage());
                }
            });

        } catch (Exception e) {
            Log.e("Exception question",e.getLocalizedMessage());
        }
    }

    /**
     * End
     */
}
