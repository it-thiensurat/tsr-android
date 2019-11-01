package th.co.thiensurat.retrofit.api;




import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

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



}


