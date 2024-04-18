package com.example.sharebikes.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMapException;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.example.sharebikes.Adapter.RvAdapter;
import com.example.sharebikes.R;
import com.amap.api.navi.AMapNavi;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Poi;
import com.amap.api.navi.AmapNaviPage;
import com.amap.api.navi.AmapNaviParams;
import com.amap.api.navi.AmapNaviType;
import com.amap.api.navi.AmapPageType;
import com.amap.api.services.core.LatLonPoint;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends BaseActivity implements Inputtips.InputtipsListener, TextWatcher, RvAdapter.OnItemClickListener {

    private EditText editText;
    private Inputtips inputTips;
    private RecyclerView recyclerView;
    private RvAdapter rvAdapter;
    private AMapNavi mAMapNavi;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editText = findViewById(R.id.edit_query);
        editText.addTextChangedListener(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new RvAdapter(this, recyclerView, new ArrayList<>());
        rvAdapter.setOnItemClickListener(this);
        recyclerView.setAdapter(rvAdapter);

        inputTips = new Inputtips(this, (InputtipsQuery) null);
        inputTips.setInputtipsListener(this);
        back = findViewById(R.id.im_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        try {
            Log.d("SearchActivity", "创建成功");
            mAMapNavi = AMapNavi.getInstance(this);
        } catch (AMapException e) {
            Log.e("SearchActivity", "创建失败", e);
            throw new RuntimeException(e);
        }
        //设置内置语音播报
        mAMapNavi.setUseInnerVoice(true, false);
    }

    @Override
    public void onGetInputtips(List<Tip> list, int i) {
        rvAdapter.setData(list);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        InputtipsQuery inputquery = new InputtipsQuery(String.valueOf(s), "0512");
        inputquery.setCityLimit(true);//限制在当前城市
        inputTips.setQuery(inputquery);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }


    @Override
    public void onItemClick(RecyclerView parent, View view, int position, Tip data) {
        Toast.makeText(this, data.getName(), Toast.LENGTH_SHORT).show();
        LatLonPoint point = data.getPoint();
        Poi poi = new Poi(data.getName(), new LatLng(point.getLatitude(), point.getLongitude()), data.getPoiID());
        AmapNaviParams params = new AmapNaviParams(null, null, poi, AmapNaviType.RIDE, AmapPageType.ROUTE);
        AmapNaviPage.getInstance().showRouteActivity(getApplicationContext(), params, null);
    }

}