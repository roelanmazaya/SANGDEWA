package com.example.sangdewa.histori;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
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
import com.example.sangdewa.adapter.AdapterHistori;
import com.example.sangdewa.config.AppControler;
import com.example.sangdewa.config.ModelData;
import com.example.sangdewa.config.Server;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Histori extends AppCompatActivity {


    SharedPreferences idduser,idlevel;
    String ambiliduser,sidlevel;
    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelData> mItems;

    TextView harga, total, subtotal, pesan;

    EditText catatan;

    private ShimmerFrameLayout mShimmerViewContainer;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.histori);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getContext().setTheme(R.style.AppThemebaru);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        idduser = getSharedPreferences(
                Login.IDUSER,
                Context.MODE_PRIVATE +
                        Context.MODE_PRIVATE | Context.MODE_PRIVATE);
        ambiliduser = (idduser.getString(
                Login.KEY_IDUSER, "NA"));



        idlevel = getSharedPreferences(
                Login.IDLEVEL,
                Context.MODE_PRIVATE +
                        Context.MODE_PRIVATE | Context.MODE_PRIVATE);
        sidlevel = (idlevel.getString(
                Login.KEY_IDLEVEL, "NA"));




        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerviewTemp);
        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterHistori(this, mItems);
        mRecyclerview.setAdapter(mAdapter);

        if (sidlevel.equals("2")||sidlevel=="2") {
            Histori();

        } else {
            Histori2();
        }





    }

    private void Histori() {
        System.out.println("masyarakat");
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET,
                Server.URL + "web_service/histori.php?iduser=" + ambiliduser+"&user=1", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ModelData md = new ModelData();
                                md.setIdtransaksi(data.getString("id"));
                                md.setNama(data.getString("nama"));
                                md.setTanggal(data.getString("tanggal"));
                                md.setStatus(data.getString("status"));

                                mItems.add(md);

                            } catch (JSONException e) {
                                e.printStackTrace();
//                                System.out.println("data kosong");

                            }
                        }

                        mAdapter.notifyDataSetChanged();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
//                        p2.setVisibility(View.GONE);
//                        pd.cancel();
//                        mShimmerViewContainer.stopShimmerAnimation();
//                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
//                        p2.setVisibility(View.GONE);
//                        pd.cancel();
//                        Toast.makeText(getApplication(), "Ada Kesalahan Mohon Periksa Kembali", Toast.LENGTH_LONG).show();
                    }
                });

        AppControler.getInstance().addToRequestQueue(reqData);
    }


    private void Histori2() {
        System.out.println("petugas");
        System.out.println(Server.URL + "web_service/histori.php?idpetugas="+ ambiliduser+"&user=2");
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET,
                Server.URL + "web_service/histori.php?idpetugas="+ ambiliduser+"&user=2", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ModelData md = new ModelData();
                                md.setIdtransaksi(data.getString("id"));
                                md.setNama(data.getString("nama"));
                                md.setTanggal(data.getString("tanggal"));
                                md.setStatus(data.getString("status"));

                                mItems.add(md);

                            } catch (JSONException e) {
                                e.printStackTrace();
//                                System.out.println("data kosong");

                            }
                        }

                        mAdapter.notifyDataSetChanged();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
//                        p2.setVisibility(View.GONE);
//                        pd.cancel();
//                        mShimmerViewContainer.stopShimmerAnimation();
//                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
//                        p2.setVisibility(View.GONE);
//                        pd.cancel();
//                        Toast.makeText(getApplication(), "Ada Kesalahan Mohon Periksa Kembali", Toast.LENGTH_LONG).show();
                    }
                });

        AppControler.getInstance().addToRequestQueue(reqData);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

}
