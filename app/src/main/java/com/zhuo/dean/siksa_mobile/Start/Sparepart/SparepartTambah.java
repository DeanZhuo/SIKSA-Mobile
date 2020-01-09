package com.zhuo.dean.siksa_mobile.Start.Sparepart;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.zhuo.dean.siksa_mobile.R;
import com.zhuo.dean.siksa_mobile.Start.API.ApiSparepart;
import com.zhuo.dean.siksa_mobile.Start.API.Helper;
import com.zhuo.dean.siksa_mobile.Start.Main.MainActivity;
import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.IOException;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SparepartTambah extends AppCompatActivity {

    EditText eKBarang, eTipe, eNama, eJual, eBeli, eMerk, eKPeletakan, eStok;
    String KBarang, Tipe, Nama, Merk, KPeletakan, Gambar = "";
    int Jual, Beli, Stok;
    Button eSimpan;
    ImageButton eGambar;
    private AlertDialog dialog;
    private Bitmap bitmap;
    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sparepart_tambah);

        getSupportActionBar().setTitle("Add");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AlertDialog.Builder builder = Helper.loadDialog(SparepartTambah.this);
        dialog = builder.create();
        requestStoragePermission();

        setAtribut();

        eGambar = (ImageButton) findViewById(R.id.SpAddGambar);
        eGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        eSimpan = (Button) findViewById(R.id.SpAddButton);
        eSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSave();
            }
        });
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }


    private void uploadGambar() {
        String imgName = Nama;
        String path = getPath(filePath);

        try {
            String uploadId = UUID.randomUUID().toString();

            new MultipartUploadRequest(this, uploadId, Helper.BASE_URL+"uploader/")
                    .addFileToUpload(path, "image")
                    .addParameter("name", imgName)
                    .setNotificationConfig(new UploadNotificationConfig())
                    .setMaxRetries(2)
                    .startUpload();

        } catch (Exception exc) {
            Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("getImage", "uploadGambar: "+exc.getMessage());
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                eGambar.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private void onClickSave() {
        KBarang = eKBarang.getText().toString();
        Tipe = eTipe.getText().toString();
        Nama = eNama.getText().toString();
        Jual = Integer.parseInt(eJual.getText().toString());
        Beli = Integer.parseInt(eBeli.getText().toString());
        Merk = eMerk.getText().toString();
        KPeletakan = eKPeletakan.getText().toString();
        Stok = Integer.parseInt(eStok.getText().toString());
        Gambar = Nama+".jpg";

        if (eKBarang.getText().toString().isEmpty() || eTipe.getText().toString().isEmpty() || eNama.getText().toString().isEmpty() || eJual.getText().toString().isEmpty() || eBeli.getText().toString().isEmpty() || eMerk.getText().toString().isEmpty() || eKPeletakan.getText().toString().isEmpty() || eStok.getText().toString().isEmpty()){
            Toast.makeText(this, "Field Tidak Boleh Kosong", Toast.LENGTH_SHORT).show();
        } else {
            dialog.show();
            try {
                Retrofit.Builder builder = new Retrofit.Builder()
                        .baseUrl(Helper.BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create());
                Retrofit retrofit = builder.build();
                ApiSparepart apiSparepart = retrofit.create(ApiSparepart.class);

                Call<String> objectCall = apiSparepart.addSparepart(KBarang, Tipe, Nama, Jual, Beli, Merk, KPeletakan, Stok, Gambar);
                objectCall.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        int Status = response.code();
                        if (Status == 500) {
                            Toast.makeText(SparepartTambah.this, "Failed To Save", Toast.LENGTH_SHORT).show();
                            Log.d("500", "onResponse: " + response.message() + "\n" + response.body());
                            Intent intent = new Intent(SparepartTambah.this, MainActivity.class);
                            startActivity(intent);
                        } else if (Status == 409) {
                            Toast.makeText(SparepartTambah.this, "Already Exist", Toast.LENGTH_SHORT).show();
                        } else {
                            uploadGambar();
                            Toast.makeText(SparepartTambah.this, "Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SparepartTambah.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Toast.makeText(SparepartTambah.this, "Network Connection Error", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SparepartTambah.this, MainActivity.class);
                        if (t instanceof IOException) {
                            Toast.makeText(SparepartTambah.this, "this is an actual network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                            // logging probably not necessary
                        }
                        else {
                            Toast.makeText(SparepartTambah.this, "conversion issue! big problems :( ", Toast.LENGTH_SHORT).show();
                            Log.d("error", t.getMessage(), t);
                            // todo log to some central bug tracking service
                        }
                        startActivity(intent);
                    }
                });
            }catch (Exception e){
                Log.d("add", "onClickSave: " + e.getMessage());
            }
            dialog.dismiss();
        }
    }

    private void setAtribut() {
        eKBarang = (EditText) findViewById(R.id.SpAddKBarang);
        eTipe = (EditText) findViewById(R.id.SpAddTipe);
        eNama = (EditText) findViewById(R.id.SpAddNama);
        eJual= (EditText) findViewById(R.id.SpAddJual);
        eBeli = (EditText) findViewById(R.id.SpAddBeli);
        eMerk = (EditText) findViewById(R.id.SpAddMerk);
        eKPeletakan = (EditText) findViewById(R.id.SpAddKPeletakan);
        eStok = (EditText) findViewById(R.id.SpAddStok);
    }
}
