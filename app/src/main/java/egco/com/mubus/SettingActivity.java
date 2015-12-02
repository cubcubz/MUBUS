package egco.com.mubus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    Spinner spinner;
    Save km;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        km = new Save(getApplicationContext());

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.km_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    km.writeKm(0.5f);
                    Toast.makeText(getApplicationContext(),String.valueOf(km.readKm()),Toast.LENGTH_SHORT).show();
                }
                if(i==1){
                    km.writeKm(1.0f);
                }
                if(i==2){
                    km.writeKm(1.5f);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                    km.writeKm(0.5f);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setting, menu);
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
        Intent intent = new Intent(SettingActivity.this,MainActivity.class);
        startActivity(intent);
    }

    public void gotoFav(View view){
        Intent intent = new Intent(SettingActivity.this,FavoriteActivity.class);
        startActivity(intent);
    }

    public void openTerm(View view){
        Intent intent = new Intent(SettingActivity.this,TermActivity.class);
        startActivity(intent);
    }

}
