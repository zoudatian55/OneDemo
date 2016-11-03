package com.example.administrator.onedemo.service;

import com.example.administrator.onedemo.bean.MovieList;
import com.example.administrator.onedemo.utils.Contant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Administrator on 2016/10/29 0029.
 */

public interface MovieService {

    @GET(Contant.LIST_BASE_URL)
    Call<MovieList> getListData(@Query("id") int id, @Query("user_id") String user_id, @Query("version") String version, @Query("platform") String platform);
}