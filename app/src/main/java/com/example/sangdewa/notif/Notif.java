/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa.notif;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.sangdewa.Login;
import com.example.sangdewa.R;
import com.example.sangdewa.config.AppControler;
import com.example.sangdewa.config.ModelData;
import com.example.sangdewa.config.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Notif extends AppCompatActivity {
    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    List<ModelData> mItems;
    ProgressDialog pd;
    RecyclerView.LayoutManager mManager;


    SharedPreferences iduser;

    String ambiliduser;
    TextView textnotif;

    Toolbar toolbar;
    ImageView gambar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notif);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getContext().setTheme(R.style.AppThemebaru);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerviewTemp);
        textnotif =  findViewById(R.id.kosong);
        gambar=(ImageView)findViewById(R.id.gambar);
        pd = new ProgressDialog(Notif.this);
        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterNotif(this, mItems);
        mRecyclerview.setAdapter(mAdapter);

        iduser = getSharedPreferences(
                Login.IDUSER,
                Context.MODE_PRIVATE +
                        Context.MODE_PRIVATE | Context.MODE_PRIVATE);
        ambiliduser = (iduser.getString(
                Login.KEY_IDUSER, "NA"));




        MengambilData();

    }

    private void MengambilData() {
        pd.setMessage("Fetching data notifikasi...");
        pd.setCancelable(true);
        pd.show();
        System.out.println(Server.URL + "web_service/notifikasi.php?iduser=" + ambiliduser);
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET,
                Server.URL + "web_service/notifikasi.php?iduser=" + ambiliduser
                , null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ModelData md = new ModelData();
                                md.setJudul(data.getString("judul"));
                                md.setIsi(data.getString("isi"));
                                md.setTanggal(data.getString("tanggal"));
                                md.setStatusbaca(data.getString("status"));


//                                img.setVisibility(View.GONE);
//                                label.setVisibility(View.GONE);
                                if (data.getString("message")=="kosong"|| data.getString("message").equals("kosong")){
                                    textnotif.setVisibility(View.VISIBLE);
                                    gambar.setVisibility(View.VISIBLE);

                                }else {
                                    textnotif.setVisibility(View.GONE);
                                    gambar.setVisibility(View.GONE);

                                }

                                mItems.add(md);

                            } catch (JSONException e) {
                                e.printStackTrace();


                            }
                        }

                        mAdapter.notifyDataSetChanged();
                        pd.cancel();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.cancel();
//                        Toast.makeText(getApplication(), "Ada Kesalahan Mohon Periksa Kembali", Toast.LENGTH_LONG).show();
                    }
                });

        AppControler.getInstance().addToRequestQueue(reqData);
    }

}
