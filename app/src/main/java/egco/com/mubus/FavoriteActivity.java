package egco.com.mubus;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class FavoriteActivity extends AppCompatActivity {

    private DataBaseHelper db;
    private Cursor resource;
    private ArrayList<FavRoute> favRoutes;
    private FavRoute fav;
    private CustomAdapter listAdapter ;
    private ListView listView;

    AlarmManager alarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        listView = (ListView)findViewById(R.id.listView);

        db = new DataBaseHelper(this);
        resource = db.getFavRoute(); //ดึงสายรถที่กดรายการโปรดไว้

        favRoutes = new ArrayList<FavRoute>();

        while (!resource.isAfterLast()){
            fav = new FavRoute();
            fav.setRoute(resource.getString(1));
            fav.setTime(resource.getString(2));
            fav.setPosition(Integer.parseInt(resource.getString(3)));
            fav.setBusID(Integer.parseInt(resource.getString(4)));

            favRoutes.add(fav);

            resource.moveToNext();
        }

        listAdapter = new CustomAdapter(this, favRoutes);
        listView.setAdapter(listAdapter);

        db.close();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) { //ถ้าคลิกจะไปที่ map activity ที่แสดงตำแน่งของรถ
                db = new DataBaseHelper(getApplicationContext());
                int route_id = db.getidRoute(favRoutes.get(i).getRoute());
                String route_time = favRoutes.get(i).getTime();

                int bus_id = db.getidBus(route_id,route_time);
                //Toast.makeText(getApplicationContext(),String.valueOf(bus_id),Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(FavoriteActivity.this,MapsActivity.class);
                intent.putExtra("bus_id",String.valueOf(bus_id));
                startActivity(intent);
            }
        });

        listView.setLongClickable(true);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int position, long l) {
                //Toast.makeText(getApplicationContext(), "Long Clicked " , Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(FavoriteActivity.this);

                builder.setTitle("ยืนยันการลบ");

                builder.setMessage("คุณต้องการลบสายการเดินรถ " + favRoutes.get(position).getRoute() + " เวลา " + String.valueOf(favRoutes.get(position).getTime()));

                builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db = new DataBaseHelper(getApplicationContext());
                        int code = db.Delete_Fav(favRoutes.get(position).getRoute(), favRoutes.get(position).getPosition());
                        Toast.makeText(getApplicationContext(), "ลบรายการเสร็จสิ้น", Toast.LENGTH_SHORT).show();


                        cancelAlarm(code);

                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }
                });

                builder.setNegativeButton("ยกเลิก", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_favorite, menu);
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
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void gotoRoute(View view){
        Intent intent = new Intent(FavoriteActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void gotoSetting(View view){
        Intent intent = new Intent(FavoriteActivity.this,SettingActivity.class);
        startActivity(intent);
    }

    private void cancelAlarm(int code) {
        if (alarmManager!= null) {
            Intent myIntent = new Intent(FavoriteActivity.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(FavoriteActivity.this,code, myIntent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }
}
