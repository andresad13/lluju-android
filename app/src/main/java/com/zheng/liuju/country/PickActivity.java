package com.zheng.liuju.country;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gyf.immersionbar.ImmersionBar;
import com.zheng.liuju.R;


import java.util.ArrayList;
import java.util.List;

public class PickActivity extends AppCompatActivity {

    private ArrayList<Country> selectedCountries = new ArrayList<>();
    private ArrayList<Country> allCountries = new ArrayList<>();
    private TextView tvLetter;
    private LinearLayoutManager manager;
    private  CAdapter adapter;
    private ImageView back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        ImmersionBar.with(this). statusBarDarkFont(true) .init();
        RecyclerView rvPick = (RecyclerView) findViewById(R.id.rv_pick);
        SideBar side = (SideBar) findViewById(R.id.side);
        EditText etSearch = (EditText) findViewById(R.id.et_search);
        tvLetter = (TextView) findViewById(R.id.tv_letter);
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in=new Intent();
                in.putExtra("fomart", "");
                setResult(1, in);
                finish();
            }
        });
        allCountries.clear();
        allCountries.addAll(Country.getAll(this, null));
        selectedCountries.clear();
        selectedCountries.addAll(allCountries);
        adapter = new CAdapter(selectedCountries);
        rvPick.setAdapter(adapter);
        manager = new LinearLayoutManager(this);
        rvPick.setLayoutManager(manager);
        rvPick.setAdapter(adapter);
        rvPick.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override public void afterTextChanged(Editable s) {
                String string = s.toString();
                selectedCountries.clear();
                for (Country country : allCountries) {
                    if(country.name.toLowerCase().contains(string.toLowerCase()))
                        selectedCountries.add(country);
                }
                adapter.update(selectedCountries);
            }
        });

        side.addIndex("#", side.indexes.size());
        side.setOnLetterChangeListener(new SideBar.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                tvLetter.setVisibility(View.VISIBLE);
                tvLetter.setText(letter);
                int  position = adapter.getLetterPosition(letter);
                if (position != -1) {
                    manager.scrollToPositionWithOffset(position, 0);
                }
            }

            @Override
            public void onReset() {
                tvLetter.setVisibility(View.GONE);
            }
        });
    }

    class CAdapter extends PyAdapter<RecyclerView.ViewHolder> {

        public CAdapter(List<? extends PyEntity> entities) {
            super(entities);
        }

        @Override
        public RecyclerView.ViewHolder onCreateLetterHolder(ViewGroup parent, int viewType) {
            return new LetterHolder(getLayoutInflater().inflate(R.layout.item_letter, parent, false));
        }

        @Override
        public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new VH(getLayoutInflater().inflate(R.layout.item_country_large_padding, parent, false));
        }

        @Override
        public void onBindHolder(RecyclerView.ViewHolder holder, PyEntity entity, int position) {
            VH vh = (VH)holder;
            final Country country = (Country)entity;
            vh.ivFlag.setImageResource(country.flag);
            vh.tvName.setText(country.name);
            vh.tvCode.setText("+" + country.code);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                 //   Log.e("fomart",country.toString());
                    Intent data = new Intent();
                    data.putExtra("fomart", country.code+"");

                    setResult(1, data);
                    finish();
                }

            });
        }

        @Override
        public void onBindLetterHolder(RecyclerView.ViewHolder holder, LetterEntity entity, int position) {
            ((LetterHolder)holder).textView.setText(entity.letter.toUpperCase());
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode== KeyEvent.KEYCODE_BACK){


                Intent in=new Intent();
                in.putExtra("fomart", "");
                setResult(1, in);



            finish();
            return false;

        }
        return super.onKeyDown(keyCode,event);
    }
}
