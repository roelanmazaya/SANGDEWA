/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa.simpankepetugas;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sangdewa.Login;
import com.example.sangdewa.R;
import com.example.sangdewa.config.AppControler;
import com.example.sangdewa.config.ModelData;
import com.example.sangdewa.config.Server;
import com.example.sangdewa.simpankepetugas.adapter.AdapterKecamatan;
import com.example.sangdewa.simpankepetugas.adapter.AdapterKelurahan;
import com.example.sangdewa.simpankepetugas.item.ItemKecamatan;
import com.example.sangdewa.simpankepetugas.item.ItemKelurahan;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class SimpanKePetugas extends AppCompatActivity implements View.OnClickListener {
    SharedPreferences idduser;
    String ambiliduser;
    RecyclerView mRecyclerview;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mManager;
    List<ModelData> mItems;
    TextView harga,total,subtotal,pesan;
    EditText catatan;
    Toolbar toolbar;
    String ambillat,ambillong;
    private ShimmerFrameLayout mShimmerViewContainer;


    private ArrayList<ModelData> listkendaraan;
    ProgressDialog pd;
    private Spinner tahunspinner;
    String idkendaraan,hargaa;


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    TextView txtLocationResult;
//    TextView txtUpdatedOn;


    // location last updated time
    private String mLastUpdateTime;

    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 50000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private static final int REQUEST_CHECK_SETTINGS = 100;


    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;

    // boolean flag to toggle the ui
    private Boolean mRequestingLocationUpdates;
    double lat;
    double lng;
    String lat2,long2;
    String alamatt,nama,plat;

    TextView alamat,belum;

    CardView tblpesan,tbldetail;

    String idpetugas;

    Spinner spJK, spKewarganegaraan, spAgama, spPendidikan, spKecamatan, spKelurahan;
    String sJK, sKewarganegaraan, sAgama, sPendidikan, sKecamatan, sKelurahan;

    EditText etNama, etTempatLahir, etTglLahir, etTelp, etPekerjaan, etAlamat, etWaktuKejadian,
    etKel, etKec, etKab, etApaYgTerjadi, etKorban, etTerlapor, etBagaimanaTerjadi, etDilaporkan;

    ImageView ivTgl;

    private DatePicker datePicker;
    private Calendar calendar;
    private int year, month, day;

    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_TAKE_PHOTO = 2;
    public Uri mUri;
    public Bitmap bitmap;
    public ImageView foto;
    TextView namaimage;

    Button pilihfoto;

    ArrayAdapter<ItemKecamatan> adapterKec;
    ArrayAdapter<ItemKelurahan> adapterKel;
    ArrayList<ItemKecamatan> kecArrayList = new ArrayList<>();
    ArrayList<ItemKelurahan> kelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jual);
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
        harga =  findViewById(R.id.harga);
        total =  findViewById(R.id.total);
        subtotal =  findViewById(R.id.subtotal);
        pesan =  findViewById(R.id.pesan);
        alamat =  findViewById(R.id.alamat);
//        catatan =  findViewById(R.id.catatan);

        tbldetail=findViewById(R.id.tbldetail);
        tblpesan=findViewById(R.id.tblpesan);
        belum= findViewById(R.id.belum);

        spAgama = findViewById(R.id.sp_agama);
        spJK = findViewById(R.id.sp_jk);
        spKewarganegaraan = findViewById(R.id.sp_kewarganegaraan);
        spPendidikan = findViewById(R.id.sp_pendidikan);
        spKecamatan = findViewById(R.id.sp_kecamatan);
        spKelurahan = findViewById(R.id.sp_kelurahan);

        etNama = findViewById(R.id.nama);
        etTempatLahir = findViewById(R.id.tempat_lahir);
        etTglLahir = findViewById(R.id.tgl_lahir);
        etTelp = findViewById(R.id.telp);
        etPekerjaan = findViewById(R.id.pekerjaan);
        etAlamat = findViewById(R.id.alamat_tinggal);
        etWaktuKejadian = findViewById(R.id.waktu_kejadian);
        etKel = findViewById(R.id.kelurahan);
        etKec = findViewById(R.id.kecamatan);
        etKab = findViewById(R.id.kabupaten);
        etApaYgTerjadi = findViewById(R.id.apa_terjadi);
        etKorban = findViewById(R.id.korban);
        etTerlapor = findViewById(R.id.terlapor);
        etBagaimanaTerjadi = findViewById(R.id.bagaimana_terjadi);
        etDilaporkan = findViewById(R.id.dilaporkan);

        ivTgl = findViewById(R.id.iv_date);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        spJK.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sJK = spJK.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spPendidikan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sPendidikan = spPendidikan.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spAgama.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sAgama = spAgama.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spKewarganegaraan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sKewarganegaraan = spKewarganegaraan.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ItemKecamatan itemKecamatan = (ItemKecamatan) parent.getSelectedItem();

                sKecamatan = itemKecamatan.getId_kec();
                System.out.println("Kecamatan pilih : "+sKecamatan);
                if(!sKecamatan.equalsIgnoreCase("0")){
                    getKel(sKecamatan);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spKelurahan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ItemKelurahan itemKelurahan = (ItemKelurahan) parent.getSelectedItem();

                sKelurahan= itemKelurahan.getId_kel();
                System.out.println("Kelurahan pilih : "+sKelurahan);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        pesan.setOnClickListener(this);
        mItems = new ArrayList<>();


        Intent data = getIntent();
        final int update = data.getIntExtra("update", 0);

        if (update == 1) {
            idpetugas = data.getStringExtra("idpetugas");
        }

        foto = (ImageView) findViewById(R.id.imageView2);
        namaimage = (TextView) findViewById(R.id.labelfoto);
        pilihfoto = (Button) findViewById(R.id.button3);


        pilihfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showfilechoser();


            }
        });


        txtLocationResult=findViewById(R.id.location_result);
        init();
        restoreValuesFromBundle(savedInstanceState);
        startLocationUpdates();

        getKec();


    }

    private void getKec() {
        kecArrayList.clear();
        String URL = Server.URL_DEV+"android/getKec";
        System.out.println(URL);
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        kecArrayList.add(new ItemKecamatan("0","- Pilih -"));
                        System.out.println(response);
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);
                                System.out.println(data.getString("nama_kec"));
                                kecArrayList.add(new ItemKecamatan(data.getString("id"),data.getString("nama_kec")));

                            } catch (JSONException e) {
                                e.printStackTrace();
//                                System.out.println("data kosong");

                            }
                        }
                        System.out.println(kecArrayList.size());
                        adapterKec = new ArrayAdapter<ItemKecamatan>(SimpanKePetugas.this, R.layout.spinner_item, kecArrayList);

                        spKecamatan.setAdapter(adapterKec);
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

    private void getKel(String sKecamatan) {
        kelArrayList.clear();
        String URL = Server.URL_DEV+"android/getKel?id_kec="+sKecamatan;
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET,
                URL, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        System.out.println(response);
                        kelArrayList.add(new ItemKelurahan("0", "0", "- Pilih -"));
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);

                                kelArrayList.add(new ItemKelurahan(data.getString("id"),data.getString("id_kec"),data.getString("nama_kel")));

                            } catch (JSONException e) {
                                e.printStackTrace();
//                                System.out.println("data kosong");

                            }
                        }
                        adapterKel = new ArrayAdapter<ItemKelurahan>(SimpanKePetugas.this, R.layout.spinner_item, kelArrayList);

                        spKelurahan.setAdapter(adapterKel);
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

    private void showfilechoser() {
        final CharSequence[] items = {"Ambil Foto", "Pilih dari Galeri",
                "Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SimpanKePetugas.this);
        builder.setTitle("Tambah Foto");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Ambil Foto")) {
                    Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    mUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "Movie_pict" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                    startActivityForResult(galleryIntent, REQUEST_TAKE_PHOTO);
                } else if (items[item].equals("Pilih dari Galeri")) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                } else if (items[item].equals("Batal")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
//        Intent galleryIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        mUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "Movie_pict" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            String path = mUri.getPath();
            String filepath = path;
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            foto.setImageBitmap(bitmap);
            namaimage.setText(filename);


        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            Uri picturePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picturePath);
            } catch (IOException e) {
            } finally {
                File f = new File(String.valueOf(picturePath));
                String filename = f.getName();
                String newfilename = filename.replace("%", "");
                namaimage.setText(newfilename + ".jpg");
                foto.setImageBitmap(bitmap);

            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
   

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(SimpanKePetugas.this);
        mSettingsClient = LocationServices.getSettingsClient(SimpanKePetugas.this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Restoring values from saved instance state
     */
    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }


    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            lat= mCurrentLocation.getLatitude();
            lng=mCurrentLocation.getLongitude();

            ambillat= String.valueOf(mCurrentLocation.getLatitude());
            ambillong= String.valueOf(mCurrentLocation.getLongitude());
            getAddress(lat,lng);
            alamat.setText(alamatt);


            txtLocationResult.setText(
                    "Lat: " + mCurrentLocation.getLatitude() + ", " +
                            "Lng: " + mCurrentLocation.getLongitude()
            );

            // giving a blink animation on TextView
            txtLocationResult.setAlpha(0);
            txtLocationResult.animate().alpha(1).setDuration(3000);

        }

//        toggleButtons();


    }



    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(SimpanKePetugas.this, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

//                        Toast.makeText(getContext(), "Started location updates!", Toast.LENGTH_SHORT).show();


                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        updateLocationUI();
                    }
                })
                .addOnFailureListener(SimpanKePetugas.this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the

                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(SimpanKePetugas.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

//                                Toast.makeText(Jual.this, errorMessage, Toast.LENGTH_LONG).show();
                        }

                        updateLocationUI();
                    }
                });
    }


    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(Jual.this, "Location updates stopped!", Toast.LENGTH_SHORT).show();
//                        toggleButtons();
                    }
                });
    }



    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();


        updateLocationUI();
    }



    @Override
    public void onPause() {
        super.onPause();
        mShimmerViewContainer.stopShimmerAnimation();
        if (mRequestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }


    @Override
    public void onClick(View v) {
//        if( catatan.getText().toString().length() == 0 ){
//            catatan.setError( "catatan wajib diisi!!!" );
//            Toast.makeText(getApplicationContext(),"Catatan Wajib diisi",Toast.LENGTH_LONG).show();
//        }else{
//            Simpan();
//        }

        Simpan();

    }

    private void Simpan() {

        String URL = Server.URL_DEV+"android/simpan_kriminal";
//        String URL = Server.URL + "web_service/simpankeranjang.php";
        System.out.println(URL);
        final ProgressDialog loading = ProgressDialog.show(this, "Simpan Transaksi...", " Mohon Tunggu...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);


                            if (success == 1) {
                                Toast.makeText(SimpanKePetugas.this, jObj.getString("message"), Toast.LENGTH_LONG).show();

                                finish();

                            } else {
                                Toast.makeText(SimpanKePetugas.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
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
                params.put("iduser", ambiliduser.trim());
                params.put("nama", etNama.getText().toString().trim());
                params.put("jk", sJK);
                params.put("tempat_lahir", etTempatLahir.getText().toString());
                params.put("namafoto", namaimage.getText().toString());
                params.put("foto", getStringImage(bitmap));
                params.put("lat", ambillat);
                params.put("long", ambillong);

                params.put("tgl_lahir", etTglLahir.getText().toString());
                params.put("telp", etTelp.getText().toString());
                params.put("pekerjaan", etPekerjaan.getText().toString());
                params.put("kewarganegaraan", sKewarganegaraan);
                params.put("agama", sAgama);
                params.put("pendidikan", sPendidikan);
                params.put("alamat", etAlamat.getText().toString());
                params.put("waktu_kejadian", etWaktuKejadian.getText().toString());
                params.put("tempat_kejadian", alamat.getText().toString());
                params.put("kel", sKelurahan);
                params.put("kec", sKecamatan);
                params.put("kab", etKab.getText().toString());
                params.put("apa_terjadi", etApaYgTerjadi.getText().toString());
                params.put("korban", etKorban.getText().toString());
                params.put("terlapor", etTerlapor.getText().toString());
                params.put("bagaimana_terjadi", etBagaimanaTerjadi.getText().toString());
                params.put("dilaporkan", etDilaporkan.getText().toString());


                return params;
            }
        };

        AppControler.getInstance().addToRequestQueue(stringRequest, "json");
    }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
//        Toast.makeText(getApplicationContext(), "ca",
//                Toast.LENGTH_SHORT)
//                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        etTglLahir.setText(new StringBuilder().append(day).append("-")
                .append(month).append("-").append(year));
    }

    void getAddress(double lat, double lng) {
//    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        Geocoder geocoder = new Geocoder(SimpanKePetugas.this);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String   add = obj.getAddressLine(0);
            alamatt=(""+add);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



}
