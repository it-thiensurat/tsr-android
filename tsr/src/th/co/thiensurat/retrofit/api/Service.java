package th.co.thiensurat.retrofit.api;




import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
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
    @GET("/api/api-receiptupdatedaily-uat.php")
    Call<Object> payment(@Query("contno") String data);

    @GET("/api/api-receiptupdatedaily1-uat.php")
    Call<Object> payment2(@Query("contno") String data);

    @GET("/api/api-vipsaleteam-uat.php")
    Call<Object> check_vid(@Query("empid") String data);

    @GET("Production/GIS/TrackingLocation")
    Call<Object> updateGIS(@Query("latitude") String data1, @Query("longitude") String data2, @Query("deviceId") String data3, @Query("empId") String data4, @Query("speed") String data5, @Query("source") String data6);

    @GET("/api/api-gis-Receipt.php")
    Call<Object> getgps(@Query("ReceiptID") String ReceiptID, @Query("EmpID") String EmpID, @Query("Latitude") String Latitude, @Query("Longitude") String Longitude);

    @GET("/api/api-setInsertInstallDate.php")
    Call<Object> InsertInstallDate(@Query("RefNo") String RefNo,@Query("InstallDate") String InstallDate,@Query("Empid") String Empid);







    @GET("UAT/SurveryAppSale/GetSurvey")
    Call<Object> getSurvey();

    @GET("/api/api-creditScoreCheck_UAT.php")
    Call<Object> getCheckScoreStatus();

    @GET("/api/api-LastPeriodCheck_UAT.php")
    Call<Object> getLastPeriod(@Query("Contno") String contno);


    @GET("/api/api-LastPeriodUpdate_UAT.php")
    Call<Object> updateCustomerPhone(@Query("telphone") String phoneNumber, @Query("refno") String refno, @Query("CreateBy") String createby);






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


    /***** uat by moo*/








    /***** production  */

    @GET("/api/api-productListBooking.php")
    Call<Object> product(@Query("Empid") String Empid);


    @GET("/api/api-booking-contract.php")
    Call<Object> load_data_contact_online_preoder(@Query("Empid") String EMPID);

    @GET("/assanee/bighead_api_new/get_max_numeber.php")
    Call<Object> get_max_numeber(@Query("SaleCode") String SaleCode,@Query("YearMonthTH") String YearMonthTH);

    @GET("/api/api-productListPreBooking.php")
    Call<Object> productListPreBooking(@Query("Empid") String Empid);




    @GET("/assanee/bighead_api_new/get_teamcode_select_position.php")
    Call<Object> get_teamcode_select_position(@Query("EmpID") String EmpID,@Query("SourceSystem") String SourceSystem);


    /***** production  */

}


