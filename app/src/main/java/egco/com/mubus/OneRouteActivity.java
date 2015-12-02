package egco.com.mubus;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class OneRouteActivity extends AppCompatActivity {
    AlarmManager alarmManager;


    private Intent intent;
    private ListView listView;
    private Cursor items;
    private DataBaseHelper db;
    private ArrayAdapter<String> listAdapter ;
    private TextView textView;
    private ArrayList<String> listOfTime,listOfBusID;
    private FavRoute favRoute;
    private String route;
    //Intent myIntent;
    private int request_code ;
    private Save km;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_route);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //myIntent = new Intent(OneRouteActivity.this, AlarmReceiver.class);

        km = new Save(getApplicationContext());


        listView = (ListView)findViewById(R.id.listView);
        intent = getIntent();
        route = intent.getStringExtra("route");

        textView = (TextView)findViewById(R.id.textviewtop);
        textView.setText(route);

        int id_route = getIDRoute(route);

        db = new DataBaseHelper(this);
        items = db.getOneRoute(id_route);


                listOfTime = new ArrayList<String>();
        listOfBusID = new ArrayList<String>();

        while(!items.isAfterLast()){
            listOfTime.add(items.getString(1));
            listOfBusID.add(items.getString(2));
            items.moveToNext();
        }

        //แสดงเวลาของสายรถที่เลือกบน list view
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, listOfTime);//แสดงเวลาเดินรถของสารการเกินรถใน listview
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(listView.isItemChecked(i)){
                    favRoute = new FavRoute(route,listOfTime.get(i),i,Integer.parseInt(listOfBusID.get(i)));
                    db = new DataBaseHelper(getApplicationContext());
                    String[] fn = listOfTime.get(i).split("\\.");
                    //Log.d(fn[0],fn[1]);
                    request_code = db.addFavRoute(favRoute); //เพิ่มสายรถนั้นในรายการโปรด
                    Toast.makeText(getApplicationContext(),listOfTime.get(i),Toast.LENGTH_SHORT).show();

                    //for present
                    Calendar c = Calendar.getInstance();
                    int min = c.get(Calendar.MINUTE) + 5;
                    int h = c.get(Calendar.HOUR);
                    setAlarm(min,h);
                    //for present

                    //setAlarm(Integer.parseInt(fn[0])-1,Integer.parseInt(fn[1])); //after present ตั้งค่าให้ ทำงานก่อนเวลาที่รถจะมาถึงจุดรับผู้โดยสาร 1 ชม. เพื่อใช้ในการแจ้งเตือน
                }
                else {
                    db = new DataBaseHelper(getApplicationContext());
                    int code = db.Delete_Fav(route, i);
                    cancelAlarm(code);

                }


            }
        });
        items = db.getCheckedPosition(route);
        while(!items.isAfterLast()){
            listView.setItemChecked(Integer.parseInt(items.getString(0)), true);
            items.moveToNext();
        }

        db.close();



    }

    public int getIDRoute(String route){ //get id ของสายการเดินรถ
        db = new DataBaseHelper(this);
        int id = db.getidRoute(route);
        return id;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_one_route, menu);
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

        return super.onOptionsItemSelected(item);
    }

    public void gotoRoute(View view){
        Intent intent = new Intent(OneRouteActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void gotoFav(View view){
        Intent intent = new Intent(OneRouteActivity.this,FavoriteActivity.class);
        startActivity(intent);
    }

    public void gotoSetting(View view){
        Intent intent = new Intent(OneRouteActivity.this,SettingActivity.class);
        startActivity(intent);
    }

    private void setAlarm(int hour,int minute){ //ตั้งค่าเวลารัน function เตือนก่อนรถมา 1 ชม. และ รัน function นี้เวลาเดิมทุกๆวัน
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);



        Intent myIntent = new Intent(OneRouteActivity.this, AlarmReceiver.class);
        myIntent.putExtra(AlarmReceiver.NOTIFICATION_ID, 1);
        String notiString = "Take " + km.readKm() + "km before bus arrive";
        myIntent.putExtra(AlarmReceiver.NOTIFICATION, getNotification(notiString));
        myIntent.putExtra("request_code",String.valueOf(request_code));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(OneRouteActivity.this, request_code, myIntent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }
    private void cancelAlarm(int code) { //ยกเลิกตั้งค่าเวลารัน function เตือนก่อนรถมา 1 ชม. และ รัน function นี้เวลาเดิมทุกๆวัน
        if (alarmManager!= null) {
            Intent myIntent = new Intent(OneRouteActivity.this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(OneRouteActivity.this,code, myIntent, 0);
            alarmManager.cancel(pendingIntent);
        }
    }

    private Notification getNotification(String content) { //set notification when bus arrive at point
        Notification.Builder builder = new Notification.Builder(this);
        builder.setContentTitle("Scheduled Notification");
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        return builder.build();
    }
}
