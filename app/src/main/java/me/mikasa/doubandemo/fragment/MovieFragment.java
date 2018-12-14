package me.mikasa.doubandemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.mikasa.doubandemo.R;
import me.mikasa.doubandemo.activity.WebviewActivity;
import me.mikasa.doubandemo.adapter.MovieAdapter;
import me.mikasa.doubandemo.base.BaseFragment;
import me.mikasa.doubandemo.base.BaseRvAdapter;
import me.mikasa.doubandemo.bean.Movie;
import me.mikasa.doubandemo.http.ApiService;
import me.mikasa.doubandemo.http.Constant;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MovieFragment extends BaseFragment
        implements BaseRvAdapter.OnRvItemClickListener {
    private static MovieFragment sInstance;
    private XRecyclerView recyclerView;
    private List<Movie.SubjectsBean> list=null;
    private MovieAdapter mAdapter;
    private List<Movie.SubjectsBean>appendData=null;
    public static MovieFragment getInstance(){
        if (sInstance==null){
            sInstance=new MovieFragment();
        }
        return sInstance;
    }
    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_xrv;
    }

    @Override
    protected void initData(Bundle bundle) {
        mAdapter=new MovieAdapter(mBaseActivity);
        list=new ArrayList<>();
        appendData=new ArrayList<>();
    }

    @Override
    protected void initView() {
        recyclerView=mRootView.findViewById(R.id.xrv);
        recyclerView.setLayoutManager(new GridLayoutManager(mBaseActivity,2));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLimitNumberToCallLoadMore(1);
    }

    @Override
    protected void setListener() {
        mAdapter.setOnRvItemClickListener(this);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getMovie(0,20);
            }

            @Override
            public void onLoadMore() {
                loadMore();
            }
        });
        recyclerView.refresh();//请求数据
    }
    private void loadMore(){
        Collections.shuffle(appendData);
        list.addAll(appendData);
        mAdapter.appendData(appendData);
        recyclerView.loadMoreComplete();//loadMoreComplete()
    }

    @Override
    public void onItemClick(int pos) {
        Intent intent=new Intent(mBaseActivity,WebviewActivity.class);
        intent.putExtra("title",list.get(pos-1).getTitle());//xrv有一个header
        intent.putExtra("url",list.get(pos-1).getAlt());
        startActivity(intent);
    }
    private void getMovie(int start,int count){
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(Constant.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService apiService=retrofit.create(ApiService.class);
        apiService.getMovie(start, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Movie>() {
                    @Override
                    public void onCompleted() {
                        recyclerView.refreshComplete();
                        mAdapter.appendData(list);
                        appendData=list;
                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.refreshComplete();
                        showToast("请求失败");
                    }

                    @Override
                    public void onNext(Movie movie) {
                        list.addAll(movie.getSubjects());
                    }
                });
    }
}
