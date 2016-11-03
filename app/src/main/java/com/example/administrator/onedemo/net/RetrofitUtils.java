package com.example.administrator.onedemo.net;

import android.os.Handler;
import android.os.Message;

import com.example.administrator.onedemo.bean.MovieList;
import com.example.administrator.onedemo.service.MovieService;
import com.example.administrator.onedemo.utils.Contant;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by axing on 16/10/27.
 */

public class RetrofitUtils {

    private static RetrofitUtils instance;
    private final Retrofit movieListRetrofit;

    public static RetrofitUtils getInstance() {
        if (instance == null) {
            instance = new RetrofitUtils();
        }

        return instance;
    }

    public RetrofitUtils() {
        movieListRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Contant.BASE_URL)
                .build();
    }

    public Retrofit getMovieListRetrofit() {
        return movieListRetrofit;
    }


    public static void getMovieData(int id, final Handler handler, final int what) {
        MovieService movieService = getInstance().getMovieListRetrofit().create(MovieService.class);
        Call<MovieList> listData = movieService.getListData(id,Contant.UESR_ID, Contant.VERSON, Contant.PLATFORM);
        listData.enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                MovieList movieList = response.body();
                Message message = handler.obtainMessage();
                message.obj=movieList;
                message.what=what;
                handler.sendMessage(message);
            }
            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {


            }
        });


    }
}
