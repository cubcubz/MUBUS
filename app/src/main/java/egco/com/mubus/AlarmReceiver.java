package egco.com.mubus;

/**
 * Created by Bream on 1/12/2558.
 */
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;



import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmReceiver extends WakefulBroadcastReceiver implements AsyncResponse { //เมื่อ alarmmanager ทำงาน
    private Timer mTimer;
    //final static long REFRESH_TIME = 300000;
    final static long REFRESH_TIME = 120000;

    private Save km;

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    private SelectTask task;

    private ArrayList<String> arrayList;

    private Context context;
    private Intent intent;

    private int code;
    AlarmManager alarmManager;
    @Override
    public void onReceive(final Context context, Intent intent) { //เมื่อได้รับสัญญานจาก AlarmManager
       //alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        km = new Save(context);

        //Log.d("come","here");
        String request_code = intent.getStringExtra("request_code"); //นำรีเควสโค้ดมาใช้ในการ get bus id

        DataBaseHelper db =new DataBaseHelper(context); //connect database
        code = db.getidBusFav(Integer.parseInt(request_code));
        this.context = context;
        this.intent = intent;

        mTimer = new Timer(); //วนลูปทำทุกๆ 2 นาที
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //Log.d("test","test");
                getAllData(); //get data form database in sever
            }
        }, 0, REFRESH_TIME);

    }

    private void getAllData() {
        String url = "http://mubussql.esy.es/getData.php"; //connect sever
        task = new SelectTask(this.context, url, String.valueOf(5));
        task.delegate = (AsyncResponse) this;
        task.execute();
    }

    @Override
    public void processFinish(ArrayList<String> output) { //if async finish
        arrayList = new ArrayList<String>();
        arrayList = output;

        // check position to notification
        if(km.readKm() == 0.5){
            if(Double.parseDouble(arrayList.get(4))>100.324969){
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                Notification notification = intent.getParcelableExtra(NOTIFICATION);
                int id = intent.getIntExtra(NOTIFICATION_ID, 0);
                notificationManager.notify(id, notification);
                mTimer.cancel();
            }
        }

        if(km.readKm() == 1){
            if(Double.parseDouble(arrayList.get(4))>100.320078 ){
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                Notification notification = intent.getParcelableExtra(NOTIFICATION);
                int id = intent.getIntExtra(NOTIFICATION_ID, 0);
                notificationManager.notify(id, notification);

                mTimer.cancel();
            }
        }
        if(km.readKm() == 1.5){
            if(Double.parseDouble(arrayList.get(4))>100.317393 ){
                NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

                Notification notification = intent.getParcelableExtra(NOTIFICATION);
                int id = intent.getIntExtra(NOTIFICATION_ID, 0);
                notificationManager.notify(id, notification);

                mTimer.cancel();
            }
        }
    }
}
