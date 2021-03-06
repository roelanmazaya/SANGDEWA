/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa.detailtransaksi;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sangdewa.Login;
import com.example.sangdewa.R;
import com.example.sangdewa.adapter.AdapterDetail;
import com.example.sangdewa.config.AppControler;
import com.example.sangdewa.config.ModelData;
import com.example.sangdewa.config.Server;
import com.example.sangdewa.histori.Histori;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DetailTransaksi extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        ResultCallback<Status> {
    SharedPreferences idduser,idlevel;
    String ambiliduser,sidlevel;
    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelData> mItems;
    TextView harga, total, subtotal, pesan, kendaraan;
    TextView catatan;
    Toolbar toolbar;
    private ShimmerFrameLayout mShimmerViewContainer;
    private GoogleMap map;
    private GoogleApiClient googleApiClient;
    private Location lastLocation;
    private TextView textLat, textLong;
    private MapFragment mapFragment;

    private ArrayList<ModelData> listkendaraan;
    ProgressDialog pd;

    TextView cancel;


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;
    String idtransaksi;
    TextView alamat;

    LinearLayout terima,petunjuk;

    ImageView imageView2;

    String ambillat,ambillong;
    private Object LatLng;

    TextView nama, jk, tempat_lahir, tgl_lahir, telp, pekerjaan, kewarganegaraan, agama, pendidikan, alamat_domisili,
    waktu_kejadian, tempat_kejadian, kelurahan, kecamatan, kabupaten, apa_terjadi, korban, bagaimana_terjadi, dilaporkan, terlapor, status;
    LinearLayout LStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailtransaksi);
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
        mShimmerViewContainer = findViewById(R.id.shimmer_view_container);

        mRecyclerview = (RecyclerView) findViewById(R.id.recyclerviewTemp);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        initGMaps();
        initGMaps();
        createGoogleApi();

        total =  findViewById(R.id.total);
        pesan =  findViewById(R.id.pesan);
        alamat =  findViewById(R.id.alamat);
        catatan =  findViewById(R.id.catatan);
        imageView2 =  findViewById(R.id.imageView2);
        terima=findViewById(R.id.terima);
        petunjuk=findViewById(R.id.petunjuk);

        init();




        mItems = new ArrayList<>();
        mManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerview.setLayoutManager(mManager);
        mAdapter = new AdapterDetail(this, mItems);
        mRecyclerview.setAdapter(mAdapter);
        pd = new ProgressDialog(DetailTransaksi.this);


        idlevel = getSharedPreferences(
                Login.IDLEVEL,
                Context.MODE_PRIVATE +
                        Context.MODE_PRIVATE | Context.MODE_PRIVATE);
        sidlevel = (idlevel.getString(
                Login.KEY_IDLEVEL, "NA"));





        Intent data = getIntent();
        final int update = data.getIntExtra("update", 0);

        if (update == 5) {
            idtransaksi = data.getStringExtra("idtransaksi");


            ListBarang();

        }



        if (sidlevel=="3"||sidlevel.equals("3")){
            terima.setVisibility(View.VISIBLE);
            terima.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Terima();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });

            petunjuk.setVisibility(View.VISIBLE);
            petunjuk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Petunjuk();
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                }
            });
        }

    }

    private void init() {

        nama = findViewById(R.id.nama);
        jk = findViewById(R.id.jk);
        tempat_lahir = findViewById(R.id.tempat_lahir);
        tgl_lahir = findViewById(R.id.tanggal_lahir);
        telp = findViewById(R.id.telp);
        pekerjaan = findViewById(R.id.pekerjaan);
        kewarganegaraan = findViewById(R.id.kewarganegaraan);
        agama = findViewById(R.id.agama);
        pendidikan = findViewById(R.id.pendidikan);
        alamat_domisili = findViewById(R.id.alamat_tinggal);

        waktu_kejadian = findViewById(R.id.waktu_kejadian);
        tempat_kejadian = findViewById(R.id.alamat);
        kelurahan = findViewById(R.id.kelurahan);
        kecamatan = findViewById(R.id.kecamatan);
        kabupaten = findViewById(R.id.kabupaten);
        apa_terjadi = findViewById(R.id.apa_terjadi);
        korban = findViewById(R.id.korban);
        terlapor = findViewById(R.id.terlapor);
        bagaimana_terjadi = findViewById(R.id.bagaimana_terjadi);
        dilaporkan = findViewById(R.id.dilaporkan);

        status = findViewById(R.id.status);
        LStatus = findViewById(R.id.LStatus);

    }


    void getAddress(double lat, double lng) {
//    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        Geocoder geocoder = new Geocoder(DetailTransaksi.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
//            alamatt=(""+add);
            alamat.setText("" + add);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    // Create GoogleApiClient instance
    private void createGoogleApi() {
//        Log.d(TAG, "createGoogleApi()");
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }


    public void onStart() {
        super.onStart();

        // Call GoogleApiClient connection when starting the Activity
        googleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();

        // Disconnect GoogleApiClient when stopping Activity
        googleApiClient.disconnect();
    }


    private final int REQ_PERMISSION = 999;

    // Check for permission to access Location
    private boolean checkPermission() {
//        Log.d(TAG, "checkPermission()");
        // Ask for permission if it wasn't granted yet
        return (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED);
    }

    // Asks for permission
    private void askPermission() {
//        Log.d(TAG, "askPermission()");
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQ_PERMISSION
        );
    }

    // Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        Log.d(TAG, "onRequestPermissionsResult()");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted

                } else {
                    // Permission denied
                    permissionsDenied();
                }
                break;
            }
        }
    }

    // App cannot work without the permissions
    private void permissionsDenied() {
//        Log.w(TAG, "permissionsDenied()");
        // TODO close app and warn user
    }

    // Initialize GoogleMaps
    private void initGMaps() {

        mapFragment.getMapAsync(this);
    }

    // Callback called when Map is ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
//        Log.d(TAG, "onMapReady()");
        map = googleMap;
        map.setOnMapClickListener(this);
        map.setOnMarkerClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
//        Log.d(TAG, "onMapClick(" + latLng + ")");
//        markerForGeofence(latLng);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
//        Log.d(TAG, "onMarkerClickListener: " + marker.getPosition());
        return false;
    }

    private Marker locationMarker;

     void markerLocation(LatLng latLng) {

        System.out.println("aaaaaaaaaaaaaaaaaaaa");

//        Log.i(TAG, "markerLocation(" + latLng + ")");
        String title = latLng.latitude + ", " + latLng.longitude;
         System.out.println(title);

        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .title(title);
        if (map != null) {
            if (locationMarker != null)
                locationMarker.remove();
            locationMarker = map.addMarker(markerOptions);
            float zoom = 16f;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
            map.animateCamera(cameraUpdate);
        }
    }
    private void ListBarang() {
         String URL = Server.URL_DEV+"android/detail_kriminal?id="+idtransaksi;
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                ModelData md = new ModelData();

                                nama.setText(data.getString("nama"));
                                jk.setText(data.getString("jk"));
                                tempat_lahir.setText(data.getString("tempat_lahir"));
                                tgl_lahir.setText(data.getString("tgl_lahir"));
                                telp.setText(data.getString("telp"));
                                pekerjaan.setText(data.getString("pekerjaan"));
                                kewarganegaraan.setText(data.getString("kewarganegaraan"));
                                agama.setText(data.getString("agama"));
                                pendidikan.setText(data.getString("pendidikan"));
                                alamat_domisili.setText(data.getString("alamat_domisili"));

                                waktu_kejadian.setText(data.getString("waktu_kejadian"));
                                tempat_kejadian.setText(data.getString("tempat_kejadian"));
                                kelurahan.setText(data.getString("kelurahan"));
                                kecamatan.setText(data.getString("kecamatan"));
                                kabupaten.setText(data.getString("kabupaten"));
                                apa_terjadi.setText(data.getString("apa_terjadi"));
                                korban.setText(data.getString("korban"));
//                                bagaimana_terjadi.setText(data.getString("bagaimana_terjadi"));
                                terlapor.setText(data.getString("terlapor"));
                                catatan.setText(data.getString("bagaimana_terjadi"));
                                dilaporkan.setText(data.getString("dilaporkan"));
                                if(data.getString("status").equalsIgnoreCase("0")){
                                    status.setText("Sedang Diproses");
                                    LStatus.setBackgroundColor(Color.parseColor("#FBBD00"));
                                }else{
                                    status.setText("Selesai");
                                    LStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
                                    terima.setVisibility(View.GONE);
                                }

//                                total.setText(data.getString("status"));

                                if (data.getString("foto")=="kosong"||data.getString("foto").equals("kosong")){
                                    Picasso.with(DetailTransaksi.this).load(R.drawable.noimage).into(imageView2);
                                }else{
                                    Picasso.with(DetailTransaksi.this).load(Server.URL_DEV+"upload/"+data.getString("foto")).into(imageView2);
                                }

                                System.out.println(Server.URL_DEV+"upload/"+data.getString("foto"));

//                                alamat.setText(data.getString("alamatpembeli"));

                                Double latpem, longpem;
                                latpem = Double.valueOf(data.getString("lat"));
                                longpem = Double.valueOf(data.getString("lon"));
                                ambillat= String.valueOf(Double.valueOf(data.getString("lat")));
                                ambillong= String.valueOf(Double.valueOf(data.getString("lon")));
                                getAddress(latpem, longpem);
                                markerLocation(new LatLng(latpem, longpem));



                                mItems.add(md);

                            } catch (JSONException e) {
                                e.printStackTrace();
//                                System.out.println("data kosong");

                            }
                        }

                        mAdapter.notifyDataSetChanged();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                    }
                });

        AppControler.getInstance().addToRequestQueue(reqData);
    }


    private void Terima() {
        String URL_DEV = Server.URL_DEV+"android/terima";
//        String URL_DEV = Server.URL + "web_service/terima.php";
        System.out.println(URL_DEV);
        final ProgressDialog loading = ProgressDialog.show(this, "Update Status Data !", " Mohon Tunggu...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DEV,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);




                            if (success == 1) {


                                //finish();
//                                startActivity(new Intent(DetailTransaksi.this, Histori.class));
//                                finish();
                                ListBarang();
                                Toast.makeText(DetailTransaksi.this, jObj.getString("message"), Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(DetailTransaksi.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //menghilangkan progress dialog
                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menghilangkan progress dialog
                        loading.dismiss();


                        //menampilkan toast
//                        Toast.makeText(Login.this, "Maaf Ada Kesalahan!!", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", idtransaksi.trim());


                return params;
            }
        };

        AppControler.getInstance().addToRequestQueue(stringRequest, "json");
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



    void Petunjuk(){
        Uri gmmIntentUri = Uri.parse("google.navigation:q="+ambillat+","+ambillong);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull Status status) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }


}
