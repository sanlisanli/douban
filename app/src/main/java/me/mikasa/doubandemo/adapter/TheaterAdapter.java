package me.mikasa.doubandemo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import me.mikasa.doubandemo.R;
import me.mikasa.doubandemo.base.BaseRvAdapter;
import me.mikasa.doubandemo.bean.Theater;

public class TheaterAdapter extends BaseRvAdapter<Theater.SubjectsBean> {
    private Context mContext;
    public TheaterAdapter(Context context){
        this.mContext=context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(mContext).inflate(R.layout.item_theater,parent,false);
        return new TheatreHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TheatreHolder)holder).bindView(mDataList.get(position));
    }
    class TheatreHolder extends BaseRvViewHolder{
        ImageView poster;
        TextView title,average,genres,ori_title;
        TheatreHolder(View itemView){
            super(itemView);
            poster=itemView.findViewById(R.id.iv_poster);
            title=itemView.findViewById(R.id.tv_title);
            average=itemView.findViewById(R.id.tv_rating_average);
            genres=itemView.findViewById(R.id.tv_theater_genres);
            ori_title=itemView.findViewById(R.id.tv_ori_title);
        }

        @Override
        protected void bindView(Theater.SubjectsBean bean) {//适配dataList与recyclerView
            Glide.with(mContext).load(bean.getImages().getSmall())
                    .error(R.drawable.ic_bili)
                    .placeholder(R.drawable.ic_bili)
                    .crossFade(1800).into(poster);
            String pingfen="评分："+String.valueOf(bean.getRating().getAverage());
            average.setText(pingfen);
            StringBuilder leixing=new StringBuilder();
            leixing.append("类型：");
            for (String s:bean.getGenres()){
                String str=s+" ";
                leixing.append(str);
            }
            genres.setText(leixing);
            title.setText(bean.getTitle());
            if (!bean.getTitle().equals(bean.getOriginal_title())){
                ori_title.setText(bean.getOriginal_title());
            }else {
                ori_title.setText(" ");
            }
        }
    }
}
