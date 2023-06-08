package co.fatweb.com.wedding.api;



import co.fatweb.com.wedding.DataObject.TopRatedMovies;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface MovieService {
    /*
        @GET("movie/top_rated")
        Call<TopRatedMovies> getTopRatedMovies(
                @Query("api_key") String apiKey,
                @Query("language") String language,
                @Query("page") int pageIndex
        );*/
    @Headers({"Content-type: application/json",
            "Accept: */*"})
    @POST("business_list")
//Call<TopRatedMovies> getUser();
    Call<TopRatedMovies> getUser(@Body TopRatedMovies dataSet);

}
