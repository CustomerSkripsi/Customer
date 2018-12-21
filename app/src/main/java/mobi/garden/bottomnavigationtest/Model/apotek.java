package mobi.garden.bottomnavigationtest.Model;

/**
 * Created by ASUS on 5/8/2018.
 */

public class apotek {

    public String id_apotek;
    public String nama_apotek,productName;
    public String address;
    public String jam_operasi;
    public String keterangan;
    public String metodePengiriman;
    public String outletOprOpen,outletOprClose,outletOprDay;
    public int ratingbar;
    public int rating;
    public String jarak;
    public int harga;
    public  int stok;
    public double latitude,longitude;
    public int delivery_fee;
    String noTelepon;

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public apotek(String id_apotek, String address, String outletOprOpen, String outletOprClose, int rating, String noTelepon) {
        this.id_apotek = id_apotek;
        this.address = address;
        this.outletOprOpen = outletOprOpen;
        this.outletOprClose = outletOprClose;
        this.rating = rating;
        this.noTelepon = noTelepon;
    }


    public int getRatingbar() {
        return ratingbar;
    }

    public void setRatingbar(int ratingbar) {
        this.ratingbar = ratingbar;
    }

    public apotek(String nama_apotek) {
        this.nama_apotek = nama_apotek;
    }

    public apotek(String id_apotek, String nama_apotek ,String outletOprOpen,int ratingbar, String outletOprClose,double latitude,double longitude ) {
        this.id_apotek = id_apotek;
        this.nama_apotek = nama_apotek;
        this.outletOprOpen = outletOprOpen;
        this.ratingbar = ratingbar;
        this.outletOprClose = outletOprClose;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public apotek(String nama_apotek, String jam_operasi) {
        this.nama_apotek = nama_apotek;
        this.jam_operasi = jam_operasi;
    }

    public apotek(String id_apotek, String nama_apotek, String jam_operasi, String keterangan, int harga, int stok) {
        this.id_apotek = id_apotek;
        this.nama_apotek = nama_apotek;
        this.jam_operasi = jam_operasi;
        this.keterangan = keterangan;
        this.harga = harga;
        this.stok = stok;
    }

    public String getProductName() {
        return productName;
    }

    public apotek(String id_apotek, String nama_apotek, String productName, int harga, int stok, double latitude, double longitude , int rating, String outletOprOpen, String outletOprClose) {
  //  public apotek(String id_apotek, String address, String outletOprOpen, String outletOprClose, int rating, String noTelepon) {
        this.id_apotek = id_apotek;
        this.nama_apotek = nama_apotek;
        this.productName = productName;
        this.harga = harga;
       // this.address = address;
        this.stok = stok;
        this.latitude = latitude;
        this.longitude = longitude;
        this.outletOprOpen = outletOprOpen;
        this.outletOprClose = outletOprClose;
        this.rating = rating;
      //  this.noTelepon = noTelepon;
    }

    public apotek(String id_apotek, String nama_apotek, String keterangan, int delivery_fee, int harga, int stok) {
        this.id_apotek = id_apotek;
        this.nama_apotek = nama_apotek;
//        this.keterangan = keterangan;
//        this.delivery_fee = delivery_fee;
        this.harga = harga;
        this.stok = stok;
    }

    public double getLatitude() {
        return latitude;
    }
    public apotek() {
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getId_apotek() {
        return id_apotek;
    }

    public String getNama_apotek() {
        return nama_apotek;
    }

    public String getJam_operasi() {
        return jam_operasi;
    }

    public String getOutletOprOpen() {
        return outletOprOpen;
    }

    public String getOutletOprClose() {
        return outletOprClose;
    }

    public int getHarga() {
        return harga;
    }

    public int getStok() {
        return stok;
    }



}
