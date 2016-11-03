package com.example.administrator.onedemo.fragment;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.onedemo.MovieDetailActivity;
import com.example.administrator.onedemo.R;
import com.example.administrator.onedemo.adapter.CommonAdapter;
import com.example.administrator.onedemo.adapter.MainMovieListAdapter;
import com.example.administrator.onedemo.adapter.MovieListAdapter;
import com.example.administrator.onedemo.bean.MovieList;

import com.example.administrator.onedemo.net.RetrofitUtils;
import com.example.administrator.onedemo.utils.Contant;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainFragment extends Fragment {


    private ListView listview;
    private List<MovieList.DataBean> data;
    private int page=0;
    private boolean isBottom;
    private int[] ids = {0, 130, 107, 94, 70, 47, 27};

    private MainMovieListAdapter mainadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View inflate = inflater.inflate(R.layout.fragment_main, null);
        return inflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listview = ((ListView) view.findViewById(R.id.movie_list));
        //第一次加载的数据
      String firsturl=Contant.BASE_URL+Contant.LIST_BASE_URL+ids[page]+Contant.MOVIE_DETAIL_LIST;
      data=getNetWork(firsturl);
      //  RetrofitUtils.getMovieData(ids[page], handler, REQUEST_MOVIE);

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && isBottom) {
                     page++;
                    List<MovieList.DataBean> been = getNetWork(Contant.BASE_URL + Contant.LIST_BASE_URL + ids[page] + Contant.MOVIE_DETAIL_LIST);
                    MainFragment.this.data.addAll(been);
                    mainadapter.notifyDataSetChanged();
                 }

            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                isBottom=(firstVisibleItem+visibleItemCount==totalItemCount);
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MovieList.DataBean bean = data.get(position);
                String beanId = bean.getId();
                Intent intent=new Intent();
                intent.putExtra("id",beanId);
                String title = bean.getTitle();
                intent.putExtra("title",title);
                intent.setClass(getContext(),MovieDetailActivity.class);
                startActivity(intent);
            }
        });


    }
    public List<MovieList.DataBean> getNetWork(String url){
        Request request=new Request.Builder().get().url(url).build();
        OkHttpClient okHttpClient=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(12,TimeUnit.SECONDS)
                .writeTimeout(8,TimeUnit.SECONDS)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                data = new Gson().fromJson(result, MovieList.class).getData();
                listview.post(new Runnable() {
                    @Override
                    public void run() {
                        mainadapter = new MainMovieListAdapter(getContext(),data);
                        listview.setAdapter(mainadapter);


                    }
                });

            }
        });
    return data;
    }
}
