package com.osa.accidentreport;

public class Konfigurasi {
    //Dibawah ini merupakan Pengalamatan dimana Lokasi Skrip CRUD PHP disimpan
    //Pada tutorial Kali ini, karena kita membuat localhost maka alamatnya tertuju ke IP komputer
    //dimana File PHP tersebut berada
    //PENTING! JANGAN LUPA GANTI IP SESUAI DENGAN IP KOMPUTER DIMANA DATA PHP BERADA
    public static final String URL_ADD="http://192.168.10.129/accident_report/tambahlaporan.php";
    public static final String URL_GET_ALL = "http://192.168.10.129/accident_report/tampilsemualaporan.php";
    public static final String URL_GET_EMP = "http://192.168.10.129/accident_report/tampillaporan.php";
    public static final String URL_UPDATE_EMP = "http://192.168.10.129/accident_report/updatelaporan.php";
    public static final String URL_DELETE_EMP = "http://192.168.10.129/accident_report/hapuslaporan.php";

    //Dibawah ini merupakan Kunci yang akan digunakan untuk mengirim permintaan ke Skrip PHP
    public static final String KEY_EMP_ID = "id";
    public static final String KEY_EMP_NAMA = "name";
    public static final String KEY_EMP_VEHICLE = "kendaraan"; //kendaraan itu variabel untuk veicle
    public static final String KEY_EMP_NOTE = "keterangan";
    public static final String KEY_EMP_LOCATION = "lokasi";

    //JSON Tags
    public static final String TAG_JSON_ARRAY="result";
    public static final String TAG_ID = "id";
    public static final String TAG_NAMA = "name";
    public static final String TAG_VEHICLE = "kendaraan";
    public static final String TAG_NOTE = "kendaraan";
    public static final String TAG_LOCATION = "lokasi";

    //ID karyawan
    //emp itu singkatan dari Employee
    public static final String EMP_ID = "emp_id";
}
