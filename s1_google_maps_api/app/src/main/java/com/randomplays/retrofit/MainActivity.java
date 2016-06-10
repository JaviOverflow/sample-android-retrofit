package com.randomplays.retrofit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    public static final String API_URL = "https://maps.googleapis.com/maps/api/";
    public static final String GOOGLE_API_KEY = "AIzaSyDvSyX_kftUaRXGnuLMR0ee_PKBg1etUvw";

    public static class GoogleApiResponse {

        public List<Result> results;

        public GoogleApiResponse(List<Result> results) {
            this.results = results;
        }
    }

    public static class Result {
        public float elevation;

        public Result(float elevation) {
            this.elevation = elevation;
        }
    }

    public interface GoogleApiMaps {
        @GET("elevation/json")
        Call<GoogleApiResponse> elevationFromLatitudeLongitude(
                @Query("locations") String locations,
                @Query("key") String key);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create a very simple REST adapter which points the GoogleApiMaps API.
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of our GoogleApiMaps API interface.
        GoogleApiMaps github = retrofit.create(GoogleApiMaps.class);


        // Create a call instance for looking up Retrofit elevationFromLatitudeLongitude.
        final double LATITUDE = 39.7391536;
        final double LONGITUDE = -104.9847034;
        final String LATITUDE_AND_LONGITUDE = String.format("%s,%s", LATITUDE, LONGITUDE);

        Call<GoogleApiResponse> call = github.elevationFromLatitudeLongitude(LATITUDE_AND_LONGITUDE, GOOGLE_API_KEY);

        call.enqueue(new Callback<GoogleApiResponse>() {
            @Override
            public void onResponse(Call<GoogleApiResponse> call, Response<GoogleApiResponse> response) {

                GoogleApiResponse elevationResponses = response.body();
                Log.i("me", "Elevation is " + elevationResponses.results.get(0).elevation);
            }

            @Override
            public void onFailure(Call<GoogleApiResponse> call, Throwable t) {
                Log.i("me", "Ha ido mal");
            }
        });

    }


}
