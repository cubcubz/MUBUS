package egco.com.mubus;

import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,LocationListener,AsyncResponse {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleApiClient googleApiClient;

    private Location location;

    //private markers ;
    private Marker user,bus_marker;

    private Timer mTimer;
    final static long REFRESH_TIME = 5000;
    int i =0;

    private int bus_id;

    private ArrayList<String> arrayList;

    private SelectTask task;
    //SelectTask selectTask = new SelectTask();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bus_id = Integer.parseInt(getIntent().getStringExtra("bus_id"));
        Toast.makeText(MapsActivity.this,"Bus id = " +  String.valueOf(bus_id), Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplicationContext(), String.valueOf(bus_id), Toast.LENGTH_SHORT).show();

        googleApiClient = new GoogleApiClient.Builder(this) //เชื่อมต่อ google api เพื่อดึงค่าพิกัดแผนที่ ของผู้ใช้
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() { //ดึงพิกัดของรถทุกๆ 5 วินาที เพื่อแสดงในแผนที่
                getAllData();
               /* runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });*/
            }
        }, 0, REFRESH_TIME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

  /*  public void test(){
            getAllData();
    }*/

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        user = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
        bus_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        CameraUpdate locations = CameraUpdateFactory.newLatLngZoom(new LatLng(13.79,100.32),15);
        mMap.animateCamera(locations);
    }

    @Override
    public void onConnected(Bundle bundle) {
        LocationAvailability locationAvailability = LocationServices.FusedLocationApi.getLocationAvailability(googleApiClient);
        if (locationAvailability.isLocationAvailable()) {
            // Call Location Services
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                    .setInterval(5000);
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        } else {
            // Do something when Location Provider not available
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) { // ถ้า location มีการเปลี่ยนตำแหน่ง
        double latitude = location.getLatitude();
        double longtitude  = location.getLongitude();

        user.remove();
        user = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)) //set marker of user
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin84))
                .title("User"));

        //bus_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)).title("User"));
        //CameraUpdate locations = CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longtitude),15);
        //mMap.animateCamera(locations);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        super.onStart();
        googleApiClient.connect(); //เชื่อมต่อ googlr api
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }


    private void getAllData() { //ดึงค่าพิกัดตำแน่งรถ
        String url = "http://mubussql.esy.es/getData.php";

        task = new SelectTask(getApplicationContext(), url, String.valueOf(bus_id));
        task.delegate = this;
        task.execute();
    }


    @Override
    public void processFinish(ArrayList<String> output) { //เมื่อได้ค่าพิกัดของตำแหน่งรถแล้ว ปักลงบนแผนที่
        arrayList = new ArrayList<String>();
        arrayList = output;

        bus_marker.remove();
        bus_marker = mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(arrayList.get(3)), Double.parseDouble(arrayList.get(4))))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.vehicle29))
                .title("Bus"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.munu_map_act, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.home) {
            mTimer.cancel();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
