package mobi.garden.bottomnavigationtest.Model;

/**
 * Created by ASUS on 5/8/2018.
 */

public class apotek {

    public String id_apotek;
    public String nama_apotek;
    public String address;
    public String jam_operasi;
    public String keterangan;
    public String outletSIA;
    public  String outletSIPA;
    public String metodePengiriman;
    public String outletOprOpen;
    public String outletOprClose;
    public  String outletOprDay;
    public int harga;
    public  int stok;
    public int delivery_fee;

    public apotek(String nama_apotek) {
        this.nama_apotek = nama_apotek;
    }

    public apotek(String id_apotek, String nama_apotek, String outletOprOpen, String outletOprClose) {
        this.id_apotek = id_apotek;
        this.nama_apotek = nama_apotek;
        this.outletOprOpen = outletOprOpen;
        this.outletOprClose = outletOprClose;
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

    public apotek(String id_apotek, String nama_apotek, String keterangan, int delivery_fee, int harga, int stok) {
        this.id_apotek = id_apotek;
        this.nama_apotek = nama_apotek;
        this.keterangan = keterangan;
        this.delivery_fee = delivery_fee;
        this.harga = harga;
        this.stok = stok;
    }


    public int getDelivery_fee() {
        return delivery_fee;
    }

    public void setDelivery_fee(int delivery_fee) {
        this.delivery_fee = delivery_fee;
    }

    public String getId_apotek() {
        return id_apotek;
    }

    public void setId_apotek(String id_apotek) {
        this.id_apotek = id_apotek;
    }

    public String getNama_apotek() {
        return nama_apotek;
    }

    public void setNama_apotek(String nama_apotek) {
        this.nama_apotek = nama_apotek;
    }

    public String getJam_operasi() {
        return jam_operasi;
    }

    public void setJam_operasi(String jam_operasi) {
        this.jam_operasi = jam_operasi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getOutletSIA() {
        return outletSIA;
    }

    public void setOutletSIA(String outletSIA) {
        this.outletSIA = outletSIA;
    }

    public String getOutletSIPA() {
        return outletSIPA;
    }

    public void setOutletSIPA(String outletSIPA) {
        this.outletSIPA = outletSIPA;
    }

    public String getOutletOprOpen() {
        return outletOprOpen;
    }

    public void setOutletOprOpen(String outletOprOpen) {
        this.outletOprOpen = outletOprOpen;
    }

    public String getOutletOprClose() {
        return outletOprClose;
    }

    public void setOutletOprClose(String outletOprClose) {
        this.outletOprClose = outletOprClose;
    }

    public String getOutletOprDay() {
        return outletOprDay;
    }

    public void setOutletOprDay(String outletOprDay) {
        this.outletOprDay = outletOprDay;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    String noTelepon;

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public String getMetodePengiriman() {
        return metodePengiriman;
    }

    public void setMetodePengiriman(String metodePengiriman) {
        this.metodePengiriman = metodePengiriman;
    }
}
