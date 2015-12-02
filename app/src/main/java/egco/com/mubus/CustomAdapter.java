package egco.com.mubus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Bream on 17/11/2558.
 */
public class CustomAdapter extends BaseAdapter { //Custom Listview
    Context mContext;
    ArrayList<FavRoute> favRoutes ;
    public CustomAdapter() {

    }

    public CustomAdapter(Context context,ArrayList<FavRoute> favRoutes) {
        this.mContext= context;
        this.favRoutes = favRoutes;
        //this.strTime = strTime;
    }
    @Override
    public int getCount() {
        return favRoutes.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(view == null)
            view = mInflater.inflate(R.layout.fav_custom, parent, false);

        TextView textView1 = (TextView)view.findViewById(R.id.routeTextView);
        textView1.setText(favRoutes.get(position).getRoute());

        TextView textView2 = (TextView)view.findViewById(R.id.timeTextView);
        textView2.setText(favRoutes.get(position).getTime());

        return view;
    }
}
