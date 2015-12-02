package egco.com.mubus;

/**
 * Created by Bream on 2/12/2558.
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Save {
    private SharedPreferences sharedPref;
    public Save(Context context) {
        sharedPref = (context).getSharedPreferences("USER", Context.MODE_PRIVATE);

    }

    //ความสูง
    public void writeKm(float height){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putFloat("KM", height);
        editor.commit();
    }
    public float readKm(){
        return sharedPref.getFloat("KM", -1f);
    }


}
