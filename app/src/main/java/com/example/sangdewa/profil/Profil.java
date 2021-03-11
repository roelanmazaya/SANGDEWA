package com.example.sangdewa.profil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.sangdewa.Login;
import com.example.sangdewa.R;
import com.example.sangdewa.config.AppControler;
import com.example.sangdewa.config.Server;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Profil extends AppCompatActivity implements View.OnClickListener {
   SharedPreferences idduser;
   String ambiliduser;

   EditText namatoko,nama,alamat,telp,deskripsi,email;

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

    ProgressDialog pd;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profil);


        nama=findViewById(R.id.nama);
        alamat=findViewById(R.id.alamat);
        telp=findViewById(R.id.telp);
        deskripsi=findViewById(R.id.deskripsi);

        idduser = getSharedPreferences(
                Login.IDUSER,
                Context.MODE_PRIVATE +
                        Context.MODE_PRIVATE | Context.MODE_PRIVATE);
        ambiliduser = (idduser.getString(
                Login.KEY_IDUSER, "NA"));

        CekProfil();
        fotobukti = (ImageView) findViewById(R.id.fotobukti);
        pilihfoto = (Button) findViewById(R.id.pilihfoto);

        namaimage =  findViewById(R.id.labelfoto);


        simpan =  findViewById(R.id.simpan);
        simpan.setOnClickListener(this);



        pilihfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showfilechoser();


            }
        });

        pd = new ProgressDialog(Profil.this);
    }

    void CekProfil(){

        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET,
                Server.URL + "web_service/cekprofil.php?iduser="+ambiliduser, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject data = response.getJSONObject(i);



                                nama.setText(data.getString("nama"));
                                telp.setText(data.getString("telp"));
                                alamat.setText(data.getString("alamat"));


                            } catch (JSONException e) {
                                e.printStackTrace();
//                                System.out.println("data kosong");

                            }
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        AppControler.getInstance().addToRequestQueue(reqData);
    }
    @Override
    public void onClick(View v) {
        Simpan();
    }



    private void showfilechoser() {
        final CharSequence[] items = {"Ambil Foto", "Pilih dari Galeri",
                "Batal"};

        AlertDialog.Builder builder = new AlertDialog.Builder(Profil.this);
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
        final ProgressDialog loading = ProgressDialog.show(Profil.this, "Loading...", " Mohon Tunggu...", false, false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.URL + "web_service/update.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {

                                startActivity(new Intent(Profil.this,Login.class));
                                finish();
                                Toast.makeText(Profil.this, jObj.getString("message"), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(Profil.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
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

                params.put("iduser", ambiliduser.trim());

                params.put("nama", nama.getText().toString().trim());
                params.put("alamat", alamat.getText().toString().trim());
                params.put("telepon", telp.getText().toString().trim());
                params.put("namafoto", namaimage.getText().toString());
                params.put("foto", getStringImage(bitmap));

                return params;
            }
        };

        AppControler.getInstance().addToRequestQueue(stringRequest, "json");
    }

}
