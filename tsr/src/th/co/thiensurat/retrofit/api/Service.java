package th.co.thiensurat.retrofit.api;




import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import th.co.bighead.utilities.BHGeneral;
import th.co.bighead.utilities.BHUtilities;
import th.co.thiensurat.activities.MainActivity;

/**
 * Created by frank on 12/16/16.
 */

public interface Service {

    //@GET("/api/api-createpaymentdaily.php")
    @GET("/api/api-createpaymentdaily-uat.php")
    Call<Object> data(@Query("Empid") String data);

    //@GET("/api/api-creditpaymentstatus.php")
    @GET("/api/api-creditpaymentstatus-uat.php")
    Call<Object> data2(@Query("Empid") String data);

    //@GET("/api/api-creditpaymentstatus.php")
//    @GET("/api/api-receiptupdatedaily-uat.php")
//    Call<Object> payment(@Query("contno") String data);

    @GET("/api/api-receiptupdatedaily1-uat.php")
    Call<Object> payment2(@Query("contno") String data);

    @GET("/api/api-vipsaleteam-uat.php")
    Call<Object> check_vid(@Query("empid") String data);

    @GET("/assanee/bighead_api_new/get_date_time.php")
    Call<Object> check_datetime(@Query("RefNo") String RefNo,@Query("EmpID") String EmpID);

    @GET("Production/GIS/TrackingLocation")
    Call<Object> updateGIS(@Query("latitude") String data1, @Query("longitude") String data2, @Query("deviceId") String data3, @Query("empId") String data4, @Query("speed") String data5, @Query("source") String data6);

    @GET("/api/api-gis-Receipt.php")
    Call<Object> getgps(@Query("ReceiptID") String ReceiptID, @Query("EmpID") String EmpID, @Query("Latitude") String Latitude, @Query("Longitude") String Longitude);

    @GET("/api/api-setInsertInstallDate.php")
    Call<Object> InsertInstallDate(@Query("RefNo") String RefNo,@Query("InstallDate") String InstallDate,@Query("Empid") String Empid);

    @GET("/api/api-setInsertInstallDate.php")
    Call<Object> InsertInstalltime(@Query("RefNo") String RefNo,@Query("InstallDate") String InstallDate,@Query("Empid") String Empid);


    @GET("UAT/SurveryAppSale/GetSurvey")
    Call<Object> getSurvey();

    @GET("/api/api-creditScoreCheck_UAT.php")
    Call<Object> getCheckScoreStatus();

    @GET("/api/api-creditScoreInsert_UAT.php")
    Call<Object> saveSurvey(@Query("RefNo") String refno, @Query("Contno") String conto, @Query("CusStatus") int cusstatus, @Query("ResStatus") int resstatus, @Query("ResTime") int restime, @Query("JobName") int jobname, @Query("JobTime") int jobtime, @Query("CusSalary") int cussalary, @Query("EmpID") String empid, @Query("ResStatusDis") String homeOther, @Query("JobNameDisc") String jobOther);

    /***** uat  */
    @GET("/api/api-productListBooking_UAT.php")
    Call<Object> product_UAT(@Query("Empid") String Empid,@Query("ProductCat") String ProductCat);

    @GET("/api/api-booking-contract_UAT.php")
    Call<Object> load_data_contact_online_preoder_UAT(@Query("Empid") String EMPID);

    @GET("/api/api-creditScoreQuestion_UAT.php")
    Call<Object> checkQuestion(@Query("Contno") String data);

    @GET("/assanee_UAT/assanee/bighead_api_new/get_max_numeber.php")
    Call<Object> get_max_numeber_uat(@Query("SaleCode") String SaleCode,@Query("YearMonthTH") String YearMonthTH);

    @GET("/api/api-productListPreBooking_UAT.php")
    Call<Object> productListPreBooking_UAT(@Query("Empid") String Empid);

    @GET("/assanee_UAT/assanee/bighead_api_new/get_teamcode_select_position.php")
    Call<Object> get_teamcode_select_position_uat(@Query("EmpID") String EmpID,@Query("SourceSystem") String SourceSystem);

    @GET("/assanee_UAT/assanee/bighead_api_new/get_ProductSerialNumber_by_preorder.php")
    Call<Object> get_ProductSerialNumber_by_preorder_uat();

    @GET("/assanee_UAT/assanee/bighead_api_new/get_ProductSerialNumber_by_preorder_setting.php")
    Call<Object> get_ProductSerialNumber_by_preorder_setting_uat();

    @GET("/api/api-checkCompanyReceipt-UAT.php")
    Call<Object> checkCompanyReceipt(@Query("refno") String refno);


    @GET("/api/api-checkBookingFirstPeriodAmount_UAT.php")
    Call<Object> checkBookingFirstPeriodAmount_UAT(@Query("refno") String refno);

    @GET("/api/api-checkBookingFirstPeriodAmount.php")
    Call<Object> checkBookingFirstPeriodAmount(@Query("refno") String refno);





    @GET("/assanee_UAT/assanee/bighead_api_new/api_1.php")
    Call<Object> get_api_1();
    @GET("/assanee_UAT/assanee/bighead_api_new/api_2.php")
    Call<Object> get_api_2();
    @GET("/assanee_UAT/assanee/bighead_api_new/api_3.php")
    Call<Object> get_api_3();

    @GET("/assanee/bighead_api_new/check_save_data.php")
    Call<Object> check_save_data(@Query("RefNo") String refno);


    @GET("/assanee_UAT/assanee/bighead_api_new/check_contract_for_cancal_preorder.php")
    Call<Object> load_data_contact_online_preoder2_UAT(@Query("RefNo") String RefNo);

    @GET("/assanee_UAT/assanee/bighead_api_new/check_contract_for_cancal_preorder.php")
    Call<Object> load_data_contact_online_preoder2(@Query("RefNo") String RefNo);

    @GET("/assanee_UAT/assanee/bighead_api_new/load_data_lead.php")
    Call<Object> get_load_data_lead();

/**test by tong**/
//Get lead on line by EmpId
    @GET("/api/api-leadonline.php")
    Call<Object> get_api_leadonline(@Query("emp") String emp);
//Get status lead on line
   @GET("/api/api-leadonlineCoeStamp.php")
    Call<Object> get_leadonlineCoeStamp();
// Update status lead on line
    @GET("/api/api-leadonlineUpdate.php")
    Call<Object> updates_status_leadonline(@Query("id") String idl, @Query("StatusWork") String statuswork, @Query("StatusCus") String statuscus, @Query("Namecustomer") String namecustomer, @Query("EmpSale") String empsale, @Query("IdProvince") String idprovince);
    /**test by tong**/

    /***** uat by moo*/

    /***** production  */
    @GET("/api/api-productListBooking.php")
    Call<Object> product(@Query("Empid") String Empid,@Query("ProductCat") String ProductCat);

    @GET("/api/api-booking-contract.php")
    Call<Object> load_data_contact_online_preoder(@Query("Empid") String EMPID);

    @GET("/assanee/bighead_api_new/get_max_numeber.php")
    Call<Object> get_max_numeber(@Query("SaleCode") String SaleCode,@Query("YearMonthTH") String YearMonthTH);

    @GET("/api/api-productListPreBooking.php")
    Call<Object> productListPreBooking(@Query("Empid") String Empid);

    @GET("/assanee/bighead_api_new/get_teamcode_select_position.php")
    Call<Object> get_teamcode_select_position(@Query("EmpID") String EmpID,@Query("SourceSystem") String SourceSystem);

    @GET("/assanee/bighead_api_new/get_ProductSerialNumber_by_preorder.php")
    Call<Object> get_ProductSerialNumber_by_preorder();

    @GET("/assanee/bighead_api_new/get_ProductSerialNumber_by_preorder_setting.php")
    Call<Object> get_ProductSerialNumber_by_preorder_setting();


    /***** production  */


    /**
     * Edit Teerayut Klinsanga 26/01/2021
     */
        /**
         *
         * UAT
         */
        @GET("/api/api-LastPeriodCheck_UAT.php")
        Call<Object> getLastPeriodUAT(@Query("Contno") String contno);

        @GET("/api/api-LastPeriodUpdate_UAT.php")
        Call<Object> updateCustomerPhoneUAT(@Query("telphone") String phoneNumber, @Query("refno") String refno, @Query("CreateBy") String createby);

        @POST("UAT/BH/ApproveContno")
        @FormUrlEncoded
        Call<Object> ApproveContnoUAT(@Field("refno") String refno, @Field("empid") String empid, @Field("salecode") String salecode, @Field("empid4") String empid4,
                                  @Field("posid4") String posid4, @Field("empid5") String empid5, @Field("posid5") String posid5,
                                  @Field("empid6") String empid6, @Field("posid6") String posid6, @Field("teamcode") String teamcode);

        @POST("UAT/BH/GetImageCheck")
        @FormUrlEncoded
        Call<Object> getImageValidateUAT(@Field("empid") String empid);

        @POST("UAT/BH/VoidContract")
        @FormUrlEncoded
        Call<Object> voidToApproveContnoUAT(@Field("refno") String refno, @Field("empid") String empid);

        @GET("/api/api-receiptupdatedaily-uat.php")
        Call<Object> paymentUAT(@Query("contno") String data);

        @POST("UAT/BH/GetDivisionId")
        @FormUrlEncoded
        Call<Object> getDivisionIdUAT(@Field("empid") String empid);

        @POST("openticket/openticket")
        @FormUrlEncoded
        Call<Object> openTicketUAT(@Field("Contno") String contno, @Field("InformEmpID") String InformEmpID, @Field("InformDepartID") String InformDepartID,
                                   @Field("ProblemID") String ProblemID, @Field("ProblemDetail") String ProblemDetail, @Field("DatatChannel") String DatatChannel);

        /**
         * END
         */

        /**
         *
         * PROD
         */
        @GET("/api/api-LastPeriodCheck.php")
        Call<Object> getLastPeriod(@Query("Contno") String contno);

        @GET("/api/api-LastPeriodUpdate.php")
        Call<Object> updateCustomerPhone(@Query("telphone") String phoneNumber, @Query("refno") String refno, @Query("CreateBy") String createby);

        @POST("Production/BH/ApproveContno")
        @FormUrlEncoded
        Call<Object> ApproveContno(@Field("refno") String refno, @Field("empid") String empid, @Field("salecode") String salecode, @Field("empid4") String empid4,
                               @Field("posid4") String posid4, @Field("empid5") String empid5, @Field("posid5") String posid5,
                               @Field("empid6") String empid6, @Field("posid6") String posid6, @Field("teamcode") String teamcode);

        @POST("Production/BH/GetImageCheck")
        @FormUrlEncoded
        Call<Object> getImageValidate(@Field("empid") String empid);

        @POST("Production/BH/VoidContract")
        @FormUrlEncoded
        Call<Object> voidToApproveContno(@Field("refno") String refno, @Field("empid") String empid);

        @GET("/api/api-chkAreaSaleing.php")
        Call<Object> getSaleArea(@Query("lat") String lat, @Query("long") String lon);

        @GET("getlocation/{lat}/{lon}/")
        Call<Object> getProductRecomend(@Path(value = "lat", encoded = false) String lat, @Path(value = "lon", encoded = false) String lon);

        @GET("/api/api-receiptupdatedaily.php")
        Call<Object> payment(@Query("contno") String data);

        @GET("/api/customerreceiptapi.php")
        Call<Object> getQrReceipt(@Query("contno") String contno);

        /**
         * END
         */

    @Multipart
    @POST("UAT/BH/AddContractImage")
    Call<Object> uploadImageToServer(@Part List<MultipartBody.Part> contract_image, @Part("refnoBody") RequestBody refnoBody);

    @POST("UAT/BH/CustomerStatus/CustomerSearch")
    @FormUrlEncoded
    Call<Object> getCustomerStatus(@Field("search") String search);

    @GET("/maps/api/geocode/json")
    Call<Object> getAreaFromGoogle(@Query("latlng") String latlng, @Query("key") String key, @Query("language") String lang);
    /**
     *
     */
}


