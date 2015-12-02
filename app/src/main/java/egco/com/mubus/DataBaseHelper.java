package egco.com.mubus;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Bream on 15/11/2558.
 */
public class DataBaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "mubus.db";
    private static final int DATABASE_VERSION = 1;


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //ดึงสายรถทั้งหมด
    public Cursor getAllRoute() {

        String querystring = "SELECT * FROM all_bus";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(querystring, null);
        c.moveToFirst();

        return c;

    }

    //ดึงค่า id ของสายรถ
    public int getidRoute(String route){
        //String querystring = "SELECT id FROM all_bus where route_thai = " + route;
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor c = db.rawQuery(querystring, null);
        Cursor c = db.query("all_bus",new String[] {"id"},"route_thai =? ",new String[]{route},null,null,null,null);
        c.moveToFirst();
        String id_s = c.getString(0);
        int id = Integer.parseInt(id_s);

        return id;
    }

    //ดึงค่า bus id
    public int getidBus(int id,String time){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query("route_all", new String[]{"bus"}, "route =? and time = ? ", new String[]{String.valueOf(id),time}, null, null, null, null);
        c.moveToFirst();
        String id_s = c.getString(0);
        int bus = Integer.parseInt(id_s);

        return bus;
    }

    //ดึงไอดีของรถที่กดเพิ่มไว้ในรายการโปรด
    public int getidBusFav(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query("fav_route", new String[]{"bus_id"}, "id =? ", new String[]{String.valueOf(id)}, null, null, null, null);
        c.moveToFirst();
        String id_s = c.getString(0);
        int bus = Integer.parseInt(id_s);

        return bus;
    }

    //ดึง ค่าของสายรถนั้น
    public Cursor getOneRoute(int id){
        String querystring = "SELECT * FROM route_all where route = " + id ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(querystring, null);
        c.moveToFirst();

        return c;
    }

    //เพิ่มสายรถเช้าในรายการโปรด
    public int addFavRoute(FavRoute favRoute){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("route", favRoute.getRoute());
        values.put("time", favRoute.getTime());
        values.put("position", favRoute.getPosition());
        values.put("bus_id",favRoute.getBusID());
        //values.put("request_code",request_code);
        // Inserting Row
        db.insert("fav_route", null, values);
        db.close(); // Closing database connection

        SQLiteDatabase dbs = this.getReadableDatabase();
        Cursor c = dbs.query("fav_route", new String[]{"id"}, "route =? and position =?", new String[]{favRoute.getRoute(), String.valueOf(favRoute.getPosition())}, null, null, null);
        c.moveToFirst();
        int code = Integer.parseInt(c.getString(0));
        return code;
    }

    //ดึงค่าที่ได้จากการกด favorite
    public Cursor getCheckedPosition(String route){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query("fav_route", new String[]{"position"}, "route = ? ", new String[]{route}, null, null, null, null);
        c.moveToFirst();


        return c;
    }

    // Deleting
    public int Delete_Fav(String route,int position) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.query("fav_route",new String[] {"id"},"route =? and position =?",new String[] {route,String.valueOf(position)},null,null,null);
        c.moveToFirst();
        int code = Integer.parseInt(c.getString(0));
        db.delete("fav_route", "route =? and position =?", new String[]{route, String.valueOf(position)});
        db.close();
        return code;
    }


    // ดึงสารรถที่กดเพิ่มในรายการโปรด
    public Cursor getFavRoute(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from fav_route", null);

        c.moveToFirst();

        return c;
    }
    //ดึงค่าจำนวนสายรถที่กดเพิ่มเป็นรายการโปรด
    public int getSizeOfFavRoute(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * from fav_route", null);
        int size = c.getCount();
        return size;
    }



}
