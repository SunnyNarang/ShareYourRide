package exodiasolutions.bikepool;

public class Ride {
    String id,name,from,to,phone,bikenumber,datetime,s_long,s_lat,d_long,d_lat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getS_long() {
        return s_long;
    }

    public void setS_long(String s_long) {
        this.s_long = s_long;
    }

    public Ride(String id, String name, String from, String to, String phone, String bikenumber, String datetime, String s_long, String s_lat, String d_long, String d_lat) {
        this.id = id;
        this.name = name;
        this.from = from;
        this.to = to;
        this.phone = phone;
        this.bikenumber = bikenumber;
        this.datetime = datetime;
        this.s_long = s_long;
        this.s_lat = s_lat;
        this.d_long = d_long;
        this.d_lat = d_lat;
    }

    public String getS_lat() {
        return s_lat;
    }

    public void setS_lat(String s_lat) {
        this.s_lat = s_lat;
    }

    public String getD_long() {
        return d_long;
    }

    public void setD_long(String d_long) {
        this.d_long = d_long;
    }

    public String getD_lat() {
        return d_lat;
    }

    public void setD_lat(String d_lat) {
        this.d_lat = d_lat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Ride(String id,String name, String from, String to, String phone, String bikenumber, String datetime) {
        this.id = id;
        this.name = name;
        this.from = from;
        this.to = to;
        this.phone = phone;
        this.bikenumber = bikenumber;
        this.datetime = datetime;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBikenumber() {
        return bikenumber;
    }

    public void setBikenumber(String bikenumber) {
        this.bikenumber = bikenumber;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
