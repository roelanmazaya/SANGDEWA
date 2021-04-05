/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sangdewa.config.AppControler;
import com.example.sangdewa.config.ModelData;
import com.example.sangdewa.config.Server;
import com.example.sangdewa.home.Home;
import com.example.sangdewa.utilities.ItemRole;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Daftar extends AppCompatActivity implements   View.OnClickListener{
    private ArrayList<ModelData> kategorilist;
    ProgressDialog pd;
    private Spinner tahunkategori;
    String idkategori;
    EditText barang,harga,satuan,stok,deskripsi;


    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;



    private int PICK_IMAGE_REQUEST = 1;
    private int REQUEST_TAKE_PHOTO = 2;
    private int PICK_IMAGE_REQUEST1 = 3;
    private int REQUEST_TAKE_PHOTO1 = 4;
    public Uri mUri;
    public Bitmap bitmap;
    public ImageView fotobukti;
    TextView namaimage, namaimage1;
    Button pilihfoto;
    TextView simpan;

    private ArrayList<ItemRole> rolesList;
    Spinner spRole;
    String idRole = "0";


    SharedPreferences idduser;
    String ambiliduser;

    EditText nama,alamat,username,password,telp, nip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daftar);

        nama=findViewById(R.id.nama);
        alamat=findViewById(R.id.alamat);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        telp=findViewById(R.id.telepon);
        nip=findViewById(R.id.nip);


        fotobukti = (ImageView) findViewById(R.id.fotobukti);
        pilihfoto = (Button) findViewById(R.id.pilihfoto);

        namaimage =  findViewById(R.id.labelfoto);


        simpan =  findViewById(R.id.simpan);
        simpan.setOnClickListener(this);

        spRole = (Spinner) findViewById(R.id.spRole);
        rolesList = new ArrayList<ItemRole>();


        pilihfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showfilechoser();


            }
        });

        pd = new ProgressDialog(Daftar.this);

        getDataRole();

        spRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                idRole = rolesList.get(position).getId_inc();
//                Toast.makeText(Daftar.this, idRole, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getDataRole() {
        final String URL = Server.URL_DEV+"android/getRole";
//        String URL = Server.URL + "web_service/profil.php?iduser="+ambiliduser;
        System.out.println(URL);
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET,
                URL
                , null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        ItemRole irx = new ItemRole("0", "Pilih Role");
                        rolesList.add(irx);
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject data = response.getJSONObject(i);
                                ItemRole ir = new ItemRole(data.getString("id_inc"), data.getString("role"));
                                rolesList.add(ir);
                            } catch (JSONException e) {
                                e.printStackTrace();


                            }
                        }

                        populateSpinner();


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

//                        Toast.makeText(getApplication(), "Ada Kesalahan Mohon Periksa Kembali", Toast.LENGTH_LONG).show();
                    }
                });

        AppControler.getInstance().addToRequestQueue(reqData);
    }

    private void populateSpinner() {
        List<String> roles = new ArrayList<String>();

        for (int i = 0; i < rolesList.size(); i++) {
            roles.add(rolesList.get(i).getRole());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, roles);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spRole.setAdapter(spinnerAdapter);
    }


    @Override
    public void onClick(View v) {
        Simpan();
    }

    private void showfilechoser() {
        final CharSequence[] items = {"Ambil Foto", "Pilih dari Galeri",
                "Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Daftar.this);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
        }

        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {
            String path = mUri.getPath();
            String filepath = path;
            String filename = filepath.substring(filepath.lastIndexOf("/") + 1, filepath.length());
            Bundle extras = data.getExtras();
            bitmap = (Bitmap) extras.get("data");
            fotobukti.setImageBitmap(bitmap);
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
                fotobukti.setImageBitmap(bitmap);

            }

        }
    }

    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void Simpan() {
        //menampilkan progress dialog
        String URL = Server.URL_DEV+"android/daftar";
//        String URL = Server.URL + "web_service/daftar.php";
        System.out.println(URL);
        final ProgressDialog loading = ProgressDialog.show(Daftar.this, "Loading...", " Mohon Tunggu...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {

                                startActivity(new Intent(Daftar.this,Login.class));
                                finish();
                                Toast.makeText(Daftar.this, jObj.getString("message"), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(Daftar.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), "Maaf Ada Kesalahan!!", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                params.put("nama", nama.getText().toString().trim());
                params.put("alamat", alamat.getText().toString().trim());
                params.put("username", username.getText().toString().trim());
                params.put("password", password.getText().toString().trim());
                params.put("telepon", telp.getText().toString().trim());
                params.put("namafoto", namaimage.getText().toString());
                params.put("foto", getStringImage(bitmap));
                params.put("idrole", idRole);
                params.put("nip", nip.getText().toString());

                return params;
            }
        };

        AppControler.getInstance().addToRequestQueue(stringRequest, "json");
    }

}
