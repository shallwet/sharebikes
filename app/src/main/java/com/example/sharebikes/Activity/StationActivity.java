package com.example.sharebikes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sharebikes.Adapter.StationAdapter;
import com.example.sharebikes.BmobDatabase.Station;
import com.example.sharebikes.R;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import java.util.ArrayList;
import java.util.List;

public class StationActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private StationAdapter stationAdapter;
    private List<Station> stationList;
    private String stationId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station);
        // 初始化控件
        recyclerView = findViewById(R.id.stationView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 初始化数据列表和适配器
        stationList = new ArrayList<>();
        queryStationsFromBmob();
        stationAdapter = new StationAdapter(this, recyclerView, stationList);
        stationAdapter.setOnItemClickListener(new StationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, android.view.View view, int position, Station data) {
                // 点击事件
                Toast.makeText(StationActivity.this, "Click on station: " + data.getObjectId(), Toast.LENGTH_SHORT).show();

                Log.d("StationActivity", "Click on station: " + data.getObjectId());
            }
        });
        recyclerView.setAdapter(stationAdapter);
    }

    private void queryStationsFromBmob() {
        BmobQuery<Station> query = new BmobQuery<>();
        query.findObjects(new FindListener<Station>() {
            @Override
            public void done(List<Station> stations, BmobException e) {
                if (e == null) {
                    // 查询成功，更新数据列表和适配器
                    stationList.clear();
                    stationList.addAll(stations);
                    stationAdapter.notifyDataSetChanged();
                } else {
                    // 查询失败，处理错误
                    Log.e("Bmob", "Error: " + e.getMessage());
                }
            }
        });
    }
    // 回到上一个页面
    private void back() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);  // 清除之前的Activity栈
        intent.putExtra("stationId", stationId); // 使用putExtra方法传递数据
        startActivity(intent);
    }
}