package com.example.arasa.aplicacionmapas;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;


import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int LOCATION_REQUEST_CODE = 1;
    private GoogleMap mMap;
    private LocationManager locManager;
    private static final long TIEMPO_UPDATES = (long) 1000;
    private static final long DISTANCIA_UPDATES = (long) 10;
    private static final int MAX_DIST = 250;
    private static final int MID_DIST = 100;
    private static final int LOW_DIST = 50;




    public Marker marca;
    public Circle circle;
    public TextView texto;
    public Location loc;
    public LatLng lati;
    public ArrayList<Tesoro> tesoros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        texto = findViewById(R.id.distancia);
        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        permisos();
        tesoros = new ArrayList<>();

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;

        decorView.setSystemUiVisibility(uiOptions);


    }


    public void permisos(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Requiere permisos para Android 6.0
            Log.e("gpslog", "No se tienen permisos necesarios!, se requieren.");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 225);
            permisos();
        } else {
            Log.i("gpslog", "Permisos necesarios OK!.");
            // registra el listener para obtener actualizaciones
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TIEMPO_UPDATES, DISTANCIA_UPDATES, locationListener, Looper.getMainLooper());

        }
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            double dist = 0.0;
            Log.i("gpslog", "Lat " + location.getLatitude() + " Long " + location.getLongitude());

            texto.setText("");
            for (Tesoro tesoro:tesoros){
                texto.setText(texto.getText()+"Localizacion: "+location.distanceTo(tesoro.getLocalizacion())+"\n");
                dist =  location.distanceTo(tesoro.getLocalizacion());
                if(dist<50){
                    tesoro.getCirculo().setRadius(0);
                    tesoro.getMarca().setVisible(true);
                }else if(dist<100) {
                    tesoro.getCirculo().setRadius(Tesoro.LOW_DIST);
                    tesoro.getMarca().setVisible(false);
                }else if(dist<250){
                    tesoro.getCirculo().setRadius(Tesoro.MID_DIST);
                    tesoro.getMarca().setVisible(false);
                }else{
                    tesoro.getCirculo().setRadius(Tesoro.MAX_DIST);
                    tesoro.getMarca().setVisible(false);
                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        // Check of permision to show our position
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        lati = new LatLng(42.236979, -8.712613);

        loc = new Location(LocationManager.NETWORK_PROVIDER);
        loc.setLatitude(lati.latitude);
        loc.setLongitude(lati.longitude);
        ;
        cargarDatos();

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                


            }
        });


        // Controles UI
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Mostrar diálogo explicativo
            } else {
                // Solicitar permiso
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_REQUEST_CODE);
            }
        }
        float zoom = 16.0f;


        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLatitude(),locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getLongitude()),zoom));
    }


    public void cargarDatos() {
        tesoros.add(new Tesoro(new LatLng(42.237176, -8.714326),mMap)); // Generamos las situaciones
        tesoros.add(new Tesoro(new LatLng(42.237023, -8.712690),mMap));
        tesoros.add(new Tesoro(new LatLng(42.237016, -8.716358),mMap));

    }

}
