/*
 * Copyright (c) Bakulapp.com 2020
 * Dev : Moh. Lukman Sholeh
 */

package com.example.sangdewa;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.sangdewa.config.AppControler;
import com.example.sangdewa.config.InternetDialog;
import com.example.sangdewa.config.PrefManager;
import com.example.sangdewa.config.Server;
import com.example.sangdewa.home.Home;
import com.example.sangdewa.petugas.HomePetugas;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {

    TextView btnlogin, btndaftar;
    private EditText username1, password1;
    public static final String SATU = "PREFS_WORLD_READABLE_WRITABLE";
    public static final String KEY_SATU = "KEY_WORLD_READ_WRITE";
    public static final String PASSOWRD = "password";
    public static final String KEY_PASSWORD = "key_password";

    public static final String IDUSER = "iduser";
    public static final String KEY_IDUSER = "key_iduser";


    public static final String IDLEVEL = "idlevel";
    public static final String KEY_IDLEVEL = "key_level";

    public static final String NAMA = "nama";
    public static final String KEY_NAMA = "key_nama";


    private SharedPreferences prefssatu, prefpassword, iduser, idlevel, nama;
    private ProgressDialog pDialog;

    //String usernameambil,passwordambil;
    private String username, password;
    private CheckBox saveLoginCheckBox;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    private Boolean saveLogin;
    //    private WelcomeActivity.MyViewPagerAdapter myViewPagerAdapter;
    private PrefManager prefManager;
    static final int tampil_error = 1;
    static final int regg = 2;
    static final int restart = 3;
    public String lo_Koneksi;
    private String jos, abjad;
    private TextView jumlah;


    //firebase
    private static final String TAG = Login.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    TextView txtRegId;

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    int success;

    String tag_json_obj = "json_obj_req";

    String ambiliduser, sidlevel;

    boolean showPass = false;

    private SharedPreferences permissionStatus;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE
    };
    private boolean sentToSettings = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);


        idlevel = getSharedPreferences(
                Login.IDLEVEL,
                Context.MODE_PRIVATE +
                        Context.MODE_PRIVATE | Context.MODE_PRIVATE);
        sidlevel = (idlevel.getString(
                Login.KEY_IDLEVEL, "NA"));

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            prefManager.setFirstTimeLaunch(false);
            if (sidlevel.equals("2")||sidlevel=="2") {
                Intent i = new Intent(Login.this, Home.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(Login.this, HomePetugas.class);
                startActivity(i);
                finish();
            }


        }

        if (new InternetDialog(this).getInternetStatus()) {
//            Toast.makeText(this, "INTERNET VALIDATION PASSED", Toast.LENGTH_SHORT).show();
        }
        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        btnlogin =  findViewById(R.id.login);
        btndaftar =  findViewById(R.id.daftar);
        username1 =  findViewById(R.id.username);
        password1 =  findViewById(R.id.password);
        txtRegId =  findViewById(R.id.txt_reg_id);
        /////SIMPAN LOGIN
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        loginPreferences = getSharedPreferences("loginPrefsbanksampah", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            username1.setText(loginPreferences.getString("username", ""));
            password1.setText(loginPreferences.getString("password", ""));
            saveLoginCheckBox.setChecked(true);
        }

        btndaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Daftar.class);
                startActivity(intent);


            }
        });

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(Login.this, Home2.class));

                prefssatu = getSharedPreferences(Login.SATU,
                        Context.MODE_PRIVATE + Context.MODE_PRIVATE
                                | Context.MODE_PRIVATE);
                SharedPreferences.Editor worldReadWriteEdit = prefssatu.edit();
                worldReadWriteEdit.putString(Login.KEY_SATU, username1.getText()
                        .toString());
                worldReadWriteEdit.commit();


                prefpassword = getSharedPreferences(Login.PASSOWRD,
                        Context.MODE_PRIVATE + Context.MODE_PRIVATE
                                | Context.MODE_PRIVATE);
                SharedPreferences.Editor worldReadWriteEdit1 = prefpassword.edit();
                worldReadWriteEdit1.putString(Login.KEY_PASSWORD, password1.getText()
                        .toString());
                worldReadWriteEdit1.commit();

                if (username1.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Masukkan Username dengan benar", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password1.length() < 1) {
                    Toast.makeText(getApplicationContext(), "Masukkan Password dengan benar", Toast.LENGTH_SHORT).show();
                    return;
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(username1.getWindowToken(), 0);

                username = username1.getText().toString();
                password = password1.getText().toString();

                if (saveLoginCheckBox.isChecked()) {
                    loginPrefsEditor.putBoolean("saveLogin", true);
                    loginPrefsEditor.putString("username", username);
                    loginPrefsEditor.putString("password", password);
                    loginPrefsEditor.commit();
                } else {
                    loginPrefsEditor.clear();
                    loginPrefsEditor.commit();
                }


                Login();


            }
        });


        password1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (password1.getRight() - password1.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        showPass = showPass == false;
                        Log.e("touch_mata", String.valueOf(showPass));
                        cekPassword(showPass);
                        return true;
                    }
                }
                return false;
            }
        });

//        cepermission();


    }

    private void cekPassword(boolean pass) {
        if (pass == true) {
            password1.setTransformationMethod(PasswordTransformationMethod.getInstance());
        } else {
            password1.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
    }


    private void Login() {

        final ProgressDialog loading = ProgressDialog.show(this, "Signin...", " Mohon Tunggu...", false, false);
        String URL = Server.URL_DEV+"android/login";
//        String URL = Server.URL + "web_service/login.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);



                            iduser = getSharedPreferences(Login.IDUSER,
                                    Context.MODE_PRIVATE + Context.MODE_PRIVATE
                                            | Context.MODE_PRIVATE);
                            SharedPreferences.Editor id = iduser.edit();
                            id.putString(Login.KEY_IDUSER, jObj.getString("id"));
                            id.commit();


                            idlevel = getSharedPreferences(Login.IDLEVEL,
                                    Context.MODE_PRIVATE + Context.MODE_PRIVATE
                                            | Context.MODE_PRIVATE);
                            SharedPreferences.Editor idlev = idlevel.edit();
                            idlev.putString(Login.KEY_IDLEVEL, jObj.getString("level"));
                            idlev.commit();

                            nama = getSharedPreferences(Login.NAMA,
                                    Context.MODE_PRIVATE + Context.MODE_PRIVATE
                                            | Context.MODE_PRIVATE);
                            SharedPreferences.Editor nama_user = nama.edit();
                            nama_user.putString(Login.KEY_NAMA, jObj.getString("nama"));
                            nama_user.commit();


                            if (success == 1) {
                                System.out.println("bbbbbbb"+jObj.getString("level"));

                                if (jObj.getString("level").equals("2")||jObj.getString("level")=="2") {

                                    System.out.println("Petugas");
                                    prefManager.setFirstTimeLaunch(false);
                                    Intent i = new Intent(Login.this, Home.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    System.out.println("Pimpinan");
                                    prefManager.setFirstTimeLaunch(false);
                                    Intent i = new Intent(Login.this, HomePetugas.class);
                                    startActivity(i);
                                    finish();
                                }




                                Toast.makeText(Login.this, jObj.getString("message"), Toast.LENGTH_LONG).show();


                            } else {
                                Toast.makeText(Login.this, jObj.getString("message"), Toast.LENGTH_LONG).show();
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

                        Down();

                        //menampilkan toast
//                        Toast.makeText(Login.this, "Maaf Ada Kesalahan!!", Toast.LENGTH_LONG).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();
                params.put("Signin", username1.getText().toString().trim());
                params.put("Password", password1.getText().toString().trim());
                params.put("Device", txtRegId.getText().toString().trim());


                return params;
            }
        };

        AppControler.getInstance().addToRequestQueue(stringRequest, "json");
    }

    public void cepermission() {
        if (ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[2])) {
                //Show Information about why you need the permission
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                builder.setCancelable(false);
                builder.setTitle("Informasi");
                builder.setMessage("Untuk menggunakan aplikasi ini silahkan aktifkan permissions anda...");
                builder.setPositiveButton("Setuju", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Login.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
//                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(Login.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
            }

//            txtPermissions.setText("Permissions Required");

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(permissionsRequired[0], true);
            editor.commit();
        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CALLBACK_CONSTANT) {
            //check if all permissions are granted
            boolean allgranted = false;
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    allgranted = true;
                } else {
                    allgranted = false;
                    break;
                }
            }

            if (allgranted) {
                proceedAfterPermission();
            } else if (ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[1])
                    || ActivityCompat.shouldShowRequestPermissionRationale(Login.this, permissionsRequired[2])) {
//                txtPermissions.setText("Permissions Required");
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Login.this);
                builder.setTitle("Need Multiple Permissions");
                builder.setMessage("Izinkan permissions.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(Login.this, permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                    }
                });
//                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
                builder.show();
            } else {
//                Toast.makeText(getBaseContext(),"Permission Diaktifkan",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[0])
                    == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    private void proceedAfterPermission() {
//        txtPermissions.setText("We've got all permissions");
//        Toast.makeText(getBaseContext(), "Permissions Diaktifkan", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(Login.this, permissionsRequired[0]) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission();
            }
        }
    }

    void Down() {
        final Dialog dialog1 = new Dialog(Login.this, R.style.df_dialog);
        dialog1.setContentView(R.layout.down);
        dialog1.setCancelable(true);
        dialog1.setCanceledOnTouchOutside(true);
        dialog1.findViewById(R.id.btnSpinAndWinRedeem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                finish();
            }
        });
        dialog1.show();
    }


}