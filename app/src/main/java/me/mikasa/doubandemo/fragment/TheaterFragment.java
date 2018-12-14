package me.mikasa.doubandemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.mikasa.doubandemo.R;
import me.mikasa.doubandemo.activity.WebviewActivity;
import me.mikasa.doubandemo.adapter.TheaterAdapter;
import me.mikasa.doubandemo.base.BaseFragment;
import me.mikasa.doubandemo.base.BaseRvAdapter;
import me.mikasa.doubandemo.bean.Theater;
import me.mikasa.doubandemo.http.ApiService;
import me.mikasa.doubandemo.http.Constant;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TheaterFragment extends BaseFragment
        implements BaseRvAdapter.OnRvItemClickListener {
    private static TheaterFragment sInstance;
    private TheaterAdapter mAdapter;
    private XRecyclerView recyclerView;
    private List<Theater.SubjectsBean>list=null;
    private List<Theater.SubjectsBean>appendData=null;
    public static TheaterFragment getInstance(){
        if (sInstance==null){
            sInstance=new TheaterFragment();
        }
        return sInstance;
    }
    @Override
    protected int setLayoutResId() {
        return R.layout.fragment_xrv;
    }

    @Override
    protected void initData(Bundle bundle) {
        mAdapter=new TheaterAdapter(mBaseActivity);
        list=new ArrayList<>();
        appendData=new ArrayList<>();
    }

    @Override
    protected void initView() {
        recyclerView=mRootView.findViewById(R.id.xrv);
        recyclerView.setLayoutManager(new GridLayoutManager(mBaseActivity,2));
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLimitNumberToCallLoadMore(1);//xrv属性
    }

    @Override
    public void onItemClick(int pos) {
        //showToast("点击"+pos);
        Intent intent=new Intent(mBaseActivity,WebviewActivity.class);
        intent.putExtra("title",list.get(pos-1).getTitle());//xrv有一个header
        intent.putExtra("url",list.get(pos-1).getAlt());
        startActivity(intent);
    }

    @Override
    protected void setListener() {
        mAdapter.setOnRvItemClickListener(this);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                getTheater(1,20);
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

    private void getTheater(int start,int count){

        Retrofit retrofit=new Retrofit.Builder()//retrofit.builder
                .baseUrl(Constant.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService apiService=retrofit.create(ApiService.class);//apiService=retrofit.create()
        apiService.getTheaterMovie(start, count)//apiService.get
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Theater>() {
                    @Override
                    public void onCompleted() {
                        recyclerView.refreshComplete();
                        mAdapter.appendData(list);
                        appendData.addAll(list);
                    }

                    @Override
                    public void onError(Throwable e) {
                        recyclerView.refreshComplete();
                        showToast("请求失败");
                    }

                    @Override
                    public void onNext(Theater theater) {
                        list.addAll(theater.getSubjects());
                    }
                });
    }
}
