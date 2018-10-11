package mobi.garden.bottomnavigationtest.Model;

public class Model_Apotek {
    public String OutletID,OutletName;
    public String address;
    public String jam_operasi;
    public String no_telepon;
    public String keterangan;
    public  String outletSIA;
    public String outletSIPA;
    public String deliveryYN;
    public String outletOprOpen;
    public String outletOprClose;
    public  String outletOprDay;
    int harga;
    int stok;

    public Model_Apotek(String OutletID, String OutletName, String address, String no_telepon, String outletSIA, String outletSIPA) {
        this.OutletID = OutletID;
        this.OutletName = OutletName;
        this.address = address;
        this.no_telepon = no_telepon;
        this.outletSIA = outletSIA;
        this.outletSIPA = outletSIPA;
    }


    public String getOutletID() {
        return OutletID;
    }

    public void setOutletID(String outletID) {
        OutletID = outletID;
    }

    public String getOutletName() {
        return OutletName;
    }

    public void setOutletName(String outletName) {
        OutletName = outletName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getOutletSIA() {
        return outletSIA;
    }

    public void setOutletSIA(String outletSIA) {
        this.outletSIA = outletSIA;
    }

    public String getOutletSIPA() {
        return outletSIPA;
    }

    public String getNo_telepon() {
        return no_telepon;
    }

    public void setNo_telepon(String no_telepon) {
        this.no_telepon = no_telepon;
    }

    public void setOutletSIPA(String outletSIPA) {
        this.outletSIPA = outletSIPA;
    }

    public String getDeliveryYN() {
        return deliveryYN;
    }

    public void setDeliveryYN(String deliveryYN) {
        this.deliveryYN = deliveryYN;
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
}
