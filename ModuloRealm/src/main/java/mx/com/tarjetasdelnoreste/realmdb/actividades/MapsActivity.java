package mx.com.tarjetasdelnoreste.realmdb.actividades;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import mx.com.tarjetasdelnoreste.realmdb.R;
import mx.com.tarjetasdelnoreste.realmdb.funciones.LocationTracker;
import mx.com.tarjetasdelnoreste.realmdb.util.Valores;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    Activity activity;
    Context context;
    CoordinatorLayout coordinatorLayout;

    private Button btn_mapa_cancelar;

    private static final int CODIGO_DE_PERMISOS_MAPA = 100;
    private GoogleMap mMap;
    LocationTracker gps;

    private String titleMapa = "Mapa";
    private LatLng latLngMapa;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_mapa);

        activity = this;
        context = this;

        Intent i = getIntent();
        titleMapa = i.getStringExtra(Valores.MAPA_TITLE);
        latLngMapa = i.getParcelableExtra(Valores.MAPA_LATLNG);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(titleMapa);

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        btn_mapa_cancelar = (Button) findViewById(R.id.btn_mapa_cerrar);
        btn_mapa_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); //Ejecuta la misma acción que el botón físico de regresar del dispositivo.
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Una vez que el mapa está inicializado, se pregunta si se tienen los permisos para acceder a la ubicación.
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Si no hay permisos, entonces los pide.
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    CODIGO_DE_PERMISOS_MAPA);
        } else { //Si ya hay permisos, entonces carga el mapa.
            cargarMapa();
        }
    }


    /** MÉTODO QUE PIDE LOS PERMISOS NECESARIOS DEL MAPA **/
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CODIGO_DE_PERMISOS_MAPA) { //Una vez obtenidos los permisos, se muestra el mapa.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                cargarMapa();
            }
        }
    }

    public void cargarMapa() {

        // Si aceptan los permisos activa el botón
        gps = new LocationTracker(context);

        // Revisa si el GPS está habilitado.
        if(gps.canGetLocation()) {

            if (latLngMapa.latitude == 0 || latLngMapa.longitude == 0) {
                latLngMapa = new LatLng(gps.getLatitude(), gps.getLongitude());

                Snackbar.make(coordinatorLayout, getString(R.string.mapa_ubicacion_fail), Snackbar.LENGTH_LONG).show();

            } else {
                //Toast.makeText(context, "La ubicación del prospecto es: - \nLat: " + latLngMapa.latitude +
                  //      "\nLong: " + latLngMapa.longitude, Toast.LENGTH_LONG).show();
            }

            flatMarkers(latLngMapa);
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
    }

    public  void changeMap() {
        //Cambiar el tipo de Mapa
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

    }

    public void flatMarkers(LatLng latLng){
        //MARCADORES FLAT
        // Flat markers will rotate when the map is rotated,
        // and change perspective when the map is tilted.
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_prospecto))
                .position(latLng)
                .flat(true));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(latLng)
                .zoom(13)
                .build();

        // Animate the change in camera view over 2 seconds
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed(); //Ejecuta la misma acción que el botón físico de regresar del dispositivo.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}





