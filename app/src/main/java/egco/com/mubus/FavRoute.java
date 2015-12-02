package egco.com.mubus;

/**
 * Created by Bream on 16/11/2558.
 */
public class FavRoute {
    public int _id;
    public String _route;
    public String _time;
    public int _position;
    public int _bus_id;

    public FavRoute() {
    }

    // constructor
    public FavRoute(int id, String _route, String _time, int _position,int _bus_id) {
        this._id = id;
        this._route = _route;
        this._time = _time;
        this._position = _position;
        this._bus_id = _bus_id;

    }

    // constructor
    public FavRoute(String _route, String _time, int _position,int _bus_id) {
        this._route = _route;
        this._time = _time;
        this._position = _position;
        this._bus_id = _bus_id;

    }

    // getting ID
    public int getID() {
        return this._id;
    }

    // setting id
    public void setID(int id) {
        this._id = id;
    }

    // getting route
    public String getRoute() {
        return this._route;
    }

    // setting route
    public void setRoute(String route) {
        this._route = route;
    }

    // getting time
    public String getTime() {
        return this._time;
    }

    // setting  time
    public void setTime(String time) {
        this._time = time;
    }

    // getting position
    public int getPosition() {
        return this._position;
    }

    // setting position
    public void setPosition(int position) {
        this._position = position;
    }

    // getting bus id
    public int getBusID() {
        return this._bus_id;
    }

    // setting  bus id
    public void setBusID(int _bus_id) {
        this._bus_id = _bus_id;
    }

}
