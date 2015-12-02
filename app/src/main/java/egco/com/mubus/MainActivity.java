package egco.com.mubus;

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

public class MainActivity extends AppCompatActivity {

    private Cursor items;
    private DataBaseHelper db;
    private ListView listView;
    private ArrayAdapter<String> listAdapter ;
    private Save km;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        km = new Save(getApplicationContext());

        if(km.readKm()!= -1) {
            km.writeKm(0.5f);
        }

        db = new DataBaseHelper(this);
        items = db.getAllRoute();

        final ArrayList<String> listOfAllRoute = new ArrayList<String>();

        while(!items.isAfterLast()){
            listOfAllRoute.add(items.getString(1));
            items.moveToNext();
        }

        listView = (ListView)findViewById(R.id.listView);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listOfAllRoute);
        listView.setAdapter(listAdapter);

        db.close();

        //กดเพื่อดูตารางเวลาของสายรถนั้น
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String route = ((TextView) view).getText().toString();
                Intent intent = new Intent(MainActivity.this,OneRouteActivity.class);
                intent.putExtra("route",route);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void gotoFav(View view){
        Intent intent = new Intent(MainActivity.this,FavoriteActivity.class);
        startActivity(intent);
    }

    public void gotoSetting(View view){
        Intent intent = new Intent(MainActivity.this,SettingActivity.class);
        startActivity(intent);
    }



}
