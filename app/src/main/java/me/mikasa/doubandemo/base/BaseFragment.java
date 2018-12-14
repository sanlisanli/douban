package me.mikasa.doubandemo.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


/**
 * Created by mikasacos on 2018/9/7.
 */

public abstract class BaseFragment extends Fragment {
    protected BaseActivity mBaseActivity;//贴附的activity(activity需继承baseActivity),Fragment中可能用到
    protected View mRootView;//onCreateView()中inflate的view，即fragment_view

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mBaseActivity=(BaseActivity)getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView=inflater.inflate(setLayoutResId(),container,false);
        initData(getArguments());
        initView();
        setListener();
        return mRootView;
    }

    protected abstract int setLayoutResId();
    protected abstract void initData(Bundle bundle);
    protected abstract void initView();//View view=mRootView.findViewById()
    protected abstract void setListener();
    protected void showToast(String msg){
        Toast.makeText(mBaseActivity,msg,Toast.LENGTH_SHORT).show();//mBaseActivity
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
