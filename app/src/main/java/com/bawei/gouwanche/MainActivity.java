package com.bawei.gouwanche;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.third_recyclerview)
    RecyclerView thirdRecyclerview;
    List<ShopBean.OrderDataBean.CartlistBean> list = new ArrayList<ShopBean.OrderDataBean.CartlistBean>();
    @BindView(R.id.allprice)
    TextView allprice;
    @BindView(R.id.check_all)
    CheckBox checkAll;
    private LinearLayoutManager manager;
    private ShopAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        try {
            InputStream inputStream = getAssets().open("shop.json");
            String data = StringUtils.convertStreamToString(inputStream);
            Gson gson = new Gson();
            ShopBean shopBean = gson.fromJson(data, ShopBean.class);

            for (int i = 0; i < shopBean.getOrderData().size(); i++) {
                int length = shopBean.getOrderData().get(i).getCartlist().size();
                for (int j = 0; j < length; j++) {
                    list.add(shopBean.getOrderData().get(i).getCartlist().get(j));
                }
            }

            manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            thirdRecyclerview.setLayoutManager(manager);

            adapter = new ShopAdapter(this, list);

            thirdRecyclerview.setAdapter(adapter);

            adapter.setCheckListener(new ShopAdapter.CheckListener() {
                @Override
                public void check(boolean check, int position) {

                    boolean allCheck = true;

                    float price = 0;

                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).isSelect()) {
                            price += list.get(i).getPrice() * list.get(i).getCount();

                        }

                    }


                    for (int i = 0; i < list.size(); i++) {


                        if (!list.get(i).isSelect()) {
                            allCheck = false;
                            break;
                        }
                    }

                    allprice.setText(price + "元");

                    if (allCheck) {
                        checkAll.setChecked(true);
                    } else {
                        checkAll.setChecked(false);
                    }

                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @OnClick(R.id.check_all)
    public void onClick() {

        boolean check =  checkAll.isChecked();
        float price  = 0 ;

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setSelect(check);
            if(check){
                if (list.get(i).isSelect()) {
                    price += list.get(i).getPrice() * list.get(i).getCount();
                }
            }
        }
        adapter.notifyDataSetChanged();

        allprice.setText(price+"元");




    }
}
