package com.example.arasa.aplicacionmapas;

import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class Tesoro {

    private LatLng situacion;
    private Circle circulo;
    private Location localizacion;
    private Marker marca;
    public static final int MAX_DIST = 250;
    public static final int MID_DIST = 100;
    public static final int LOW_DIST = 50;

    public Tesoro(LatLng situacion, GoogleMap mMap) {
        this.situacion = situacion;
        CircleOptions circleOpt = new CircleOptions()
                .center(situacion)
                .radius(MAX_DIST)
                .strokeColor(Color.GREEN)
                .strokeWidth(5)
                .fillColor(R.color.relleno)
                .clickable(true);
        this.circulo = mMap.addCircle(circleOpt);

        this.localizacion  = new Location(LocationManager.NETWORK_PROVIDER);
        this.localizacion.setLatitude(situacion.latitude);
        this.localizacion.setLongitude(situacion.longitude);
        this.marca = mMap.addMarker(new MarkerOptions()
                .position(this.situacion)
                .draggable(false)
                .visible(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.treasurechesttwo)));

    }

    public LatLng getSituacion() {
        return situacion;
    }

    public void setSituacion(LatLng situacion) {
        this.situacion = situacion;
    }

    public Circle getCirculo() {
        return circulo;
    }

    public void setCirculo(Circle circulo) {
        this.circulo = circulo;
    }

    public Location getLocalizacion() {
        return localizacion;
    }

    public void setLocalizacion(Location localizacion) {
        this.localizacion = localizacion;
    }

    public Marker getMarca() {
        return marca;
    }

    public void setMarca(Marker marca) {
        this.marca = marca;
    }


}
