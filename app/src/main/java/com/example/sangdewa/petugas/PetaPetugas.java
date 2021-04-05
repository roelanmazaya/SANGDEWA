package com.example.sangdewa.petugas;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.sangdewa.R;
import com.example.sangdewa.config.AppControler;
import com.example.sangdewa.config.Server;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PetaPetugas extends AppCompatActivity implements OnMapReadyCallback {

    MapFragment mapFragment;
    GoogleMap gMap;
    MarkerOptions markerOptions = new MarkerOptions();
    CameraPosition cameraPosition;
    LatLng center, latLng;
    Double latt, longg;
    String title;

    String tag_json_obj = "json_obj_req";
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.peta);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getContext().setTheme(R.style.AppThemebaru);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;

        // Mengarahkan ke tempat-8.174215, 113.716254

        center = new LatLng(-7.094789, 108.821540);
//        center = new LatLng(-5.4386706, 105.1578069);
        cameraPosition = new CameraPosition.Builder().target(center).zoom(5).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        getMarkers();
    }

    private void addMarker(LatLng latlng, final String title, Double lattt, Double longgg) {
        markerOptions.position(latlng);
        markerOptions.title(title);

        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.sales));
        gMap.addMarker(markerOptions);


        gMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void getMarkers() {
        String URL = Server.URL_DEV+"/android/peta";
//        String URL = Server.URL + "web_service/peta.php";
        System.out.println(URL);
        JsonArrayRequest reqData = new JsonArrayRequest(Request.Method.GET,
                URL
                , null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        double ipk_ = 0;
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject json_data = response.getJSONObject(i);
                                title = json_data.getString("username");
                                latLng = new LatLng(Double.parseDouble(json_data.getString("lat")), Double.parseDouble(json_data.getString("long")));
                                latt = Double.parseDouble(json_data.getString("lat"));
                                longg = Double.parseDouble(json_data.getString("long"));
                                // Menambah data marker untuk di tampilkan ke google map
                                addMarker(latLng, title, latt, longg);


                            } catch (JSONException e) {
                                e.printStackTrace();


                            }
                        }


//                        pd.cancel();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        pd.cancel();
//                        Toast.makeText(getApplication(), "Ada Kesalahan Mohon Periksa Kembali", Toast.LENGTH_LONG).show();
                    }
                });

        AppControler.getInstance().addToRequestQueue(reqData);
    }

}

