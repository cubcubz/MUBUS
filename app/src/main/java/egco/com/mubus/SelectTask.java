package egco.com.mubus;

/**
 * Created by Bream on 28/11/2558.
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.android.gms.fitness.data.Value;

public class SelectTask extends AsyncTask<Void, Void, String> {

    private Context mContext;
    private String mUrl;
    private ArrayList<String> arraylist;
    private ArrayList<String>  arraylists;

    public AsyncResponse delegate = null;

    public SelectTask(Context context, String url,String params) {
        super();

        arraylist = new ArrayList<String>();
        arraylists = new ArrayList<String>();

        mContext = context;
        mUrl = url;

        if (params != null){
            //String paramString = URLEncodeedUtils.format(params,"utf-8");
            mUrl  += "?id=" + params;
        }
        //Toast.makeText(mContext, mUrl, Toast.LENGTH_SHORT).show();
        //Log.d("come",mUrl);
    }

    @Override
    protected String doInBackground(Void... params) {
        String jsonString = JsonHttp.makeHttpRequest(mUrl);
        return jsonString;
    }

    @Override
    protected void onPostExecute(String jsonString) { //ดึงค่าจาก sever

       // ArrayList<HashMap<String, String>> busList;


        try {
            JSONObject json = new JSONObject(jsonString);
            int success = json.getInt("success");

            if (success == 1) {
                JSONArray bus = json.getJSONArray("bus");
                //busList = new ArrayList<HashMap<String, String>>();

                for (int i = 0; i < bus.length(); i++) {
                    JSONObject bu = bus.getJSONObject(i);

                    String id = bu.getString("id");
                    String date = bu.getString("date");
                    String time = bu.getString("time");
                    String latitude = bu.getString("latitude");
                    String longtitude = bu.getString("longtitude");



                    arraylist.add(id);
                    arraylist.add(date);
                    arraylist.add(time);
                    arraylist.add(latitude);
                    arraylist.add(longtitude);


                    //myMethod(arraylist);

                    delegate.processFinish(arraylist);





                    Log.d(arraylist.get(4),arraylist.get(4));

                }
            } else if (success == 0) {
                //((MainActivity) mContext).list.setAdapter(null);
                String msg = json.getString("message");
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




}