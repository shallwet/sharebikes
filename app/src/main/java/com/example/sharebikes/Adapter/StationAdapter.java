package com.example.sharebikes.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharebikes.BmobDatabase.Station;
import com.example.sharebikes.R;

import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> implements View.OnClickListener {

    private final List<Station> list;
    private final Context context;
    private final RecyclerView rv;
    private OnItemClickListener mOnItemClickListener;

    @Override
    public void onClick(View v) {
        int position = rv.getChildAdapterPosition(v);
        //程序执行到此，会去执行具体实现的onItemClick()方法
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(rv, v, position, list.get(position));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RecyclerView parent, View view, int position, Station data);
    }

    public StationAdapter(Context context, RecyclerView rv, List<Station> list) {
        this.context = context;
        this.rv = rv;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener clickListener) {
        this.mOnItemClickListener = clickListener;
    }

    public void setData(List<Station> list) {
        if (list == null) return;
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.station_item, parent, false);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Station station = list.get(position);
        holder.stationName.setText(station.getStationName());
        holder.phone.setText(station.getPhone());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView stationName, phone;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stationName = itemView.findViewById(R.id.tv_station_name);
            phone = itemView.findViewById(R.id.tv_station_phone);
        }
    }
}