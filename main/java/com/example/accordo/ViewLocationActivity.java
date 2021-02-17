package com.example.accordo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.gms.location.LocationCallback;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerView;
import com.mapbox.mapboxsdk.plugins.markerview.MarkerViewManager;
import com.mapbox.mapboxsdk.style.layers.PropertyFactory;
import com.mapbox.mapboxsdk.style.layers.SymbolLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.iconImage;

public class ViewLocationActivity extends AppCompatActivity implements OnMapReadyCallback{

    private LocationCallback locationCallback;

    private MapView mapView;
    private MapboxMap mapboxMap;
    private MarkerViewManager markerViewManager;

    Double longitude;
    Double latitude;

    private static final String SOURCE_ID = "SOURCE_ID";
    private static final String ICON_ID = "ICON_ID";
    private static final String LAYER_ID = "LAYER_ID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        longitude = getIntent().getDoubleExtra("longitude", 0);
        latitude = getIntent().getDoubleExtra("latitude", 0);
        Log.d("DaniloPos", longitude+"");
        Log.d("DaniloPos", latitude +"");

        Mapbox.getInstance(this, getString(R.string.mapbox_access_token));
        setContentView(R.layout.activity_view_location_post);
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);


    }


        @Override
        public void onMapReady(@NonNull final MapboxMap mapboxMap) {
            Log.d("Main Activity", "Map ready");


            //devo salvare l'oggetto mapboxMap perchè mi serve, ad esempio, per cambiare la posizione della camera
            ViewLocationActivity.this.mapboxMap = mapboxMap;

            List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
            symbolLayerIconFeatureList.add(Feature.fromGeometry(
                    Point.fromLngLat(longitude, latitude)));


            mapboxMap.setStyle(new Style.Builder().fromUri("mapbox://styles/mapbox/cjf4m44iw0uza2spb3q0a7s41")

// Add the SymbolLayer icon image to the map style
                    .withImage(ICON_ID, BitmapFactory.decodeResource(
                            ViewLocationActivity.this.getResources(), R.drawable.location))

// Adding a GeoJson source for the SymbolLayer icons.
                    .withSource(new GeoJsonSource(SOURCE_ID,
                            FeatureCollection.fromFeatures(symbolLayerIconFeatureList)))

// Adding the actual SymbolLayer to the map style. An offset is added that the bottom of the red
// marker icon gets fixed to the coordinate, rather than the middle of the icon being fixed to
// the coordinate point. This is offset is not always needed and is dependent on the image
// that you use for the SymbolLayer icon.
                    .withLayer(new SymbolLayer(LAYER_ID, SOURCE_ID)
                            .withProperties(
                                    iconImage(ICON_ID)
                            )

                    ), new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(latitude, longitude))
                            .zoom(6)
                            .tilt(20)
                            .build();

                    mapboxMap.moveCamera(CameraUpdateFactory.newCameraPosition(position));



// Map is set up and the style has loaded. Now you can add additional data or make other map adjustments.


                }
            });

         }




    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }





}