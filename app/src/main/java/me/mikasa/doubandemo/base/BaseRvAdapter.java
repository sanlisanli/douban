package me.mikasa.doubandemo.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by mikasacos on 2018/9/7.
 */

public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter {
    protected List<T>mDataList=new ArrayList<>();//dataList,protected可继承
    private OnRvItemClickListener mListener;//clickListener点击监听事件
    //分页加载，追加数据
    public void appendData(List<T>dataList){
        if (null!=dataList&&!dataList.isEmpty()){
            //addAll()追加
            mDataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
    public void setOnRvItemClickListener(OnRvItemClickListener listener){//设置监听事件
        this.mListener=listener;
    }
    //abstract viewHolder
    public abstract class BaseRvViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{
        public BaseRvViewHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);//itemView点击事件
            //itemView.setOnLongClickListener(this);//itemView
        }
        protected abstract void bindView(T t);//bindView

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onItemClick(getLayoutPosition());//getLayoutPosition()，mDataList从0开始
            }
        }
    }
    public interface OnRvItemClickListener{
        void onItemClick(int pos);
    }

}
