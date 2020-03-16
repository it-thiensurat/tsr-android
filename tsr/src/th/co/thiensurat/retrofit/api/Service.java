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

}


