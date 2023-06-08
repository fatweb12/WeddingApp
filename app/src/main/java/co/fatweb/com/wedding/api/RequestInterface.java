package co.fatweb.com.wedding.api;


import java.util.List;

import co.fatweb.com.wedding.DataObject.Allergy;
import co.fatweb.com.wedding.DataObject.Cat_Listing;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RequestInterface {

    /*@GET(*//*"android/jsonandroid"*//*"41")
    Call<JSONResponse> getJSON();
*/
   // @GET(/*"dds861/e5f0ae4e99ff820e003c38f35276d4ce/raw/35f7b1452b3cfd55009416c62b055592858b9177/json1.json"*/"41")
    @GET("category")
    //Call<List<User>> getUserData();

    Call<List<Allergy>> getUserData();
    /*@GET("41")
    Call<JSONResponse> getJSON();*/
}