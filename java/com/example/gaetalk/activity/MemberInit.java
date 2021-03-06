package com.example.gaetalk.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.gaetalk.MemberInfo;
import com.example.gaetalk.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MemberInit extends AppCompatActivity {
    private static final String TAG = "MemberInit";
    private ImageView profileImageView;
    private String profilePath;
    private FirebaseUser user;
    EditText edtMemInitHumanName, edtMemInitName, edtMemInitGender, edtMemInitBirth, edtMemInitBreed, edtMemInitPersonality;
    Button btnMemInitCheck, btnTakePicture, btnGallery;
    LinearLayout linearInitButton;
    boolean check;
    Spinner spGu, spDong;
    SQLiteDatabase sqlDB;
    ArrayAdapter<String> adapter, adapter1;
    ArrayList<String> guData, dongData;
    String address1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_init);
        edtMemInitHumanName = findViewById(R.id.edtMemInitHumanName);
        edtMemInitName = findViewById(R.id.edtMemInitName);
        edtMemInitGender = findViewById(R.id.edtMemInitGender);
        edtMemInitBirth = findViewById(R.id.edtMemInitBirth);
        edtMemInitBreed = findViewById(R.id.edtMemInitBreed);
        edtMemInitPersonality = findViewById(R.id.edtMemInitPersonality);
        btnMemInitCheck = findViewById(R.id.btnMemInitCheck);
        btnTakePicture = findViewById(R.id.btnTakePicture);
        btnGallery = findViewById(R.id.btnGallery);
        profileImageView = findViewById(R.id.profileImageView);
        linearInitButton = findViewById(R.id.linearInitButton);
        spGu = findViewById(R.id.spGu);
        spDong = findViewById(R.id.spDong);

        btnMemInitCheck.setOnClickListener(onClickListener);
        profileImageView.setOnClickListener(onClickListener);
        btnTakePicture.setOnClickListener(onClickListener);
        btnGallery.setOnClickListener(onClickListener);

        //?????? DB??????~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        guData = new ArrayList<String>();//??????????????????
        dongData = new ArrayList<String>();//??????????????????
        check = isCheckDB(this);
        if (!check) {//????????? check??? false?????? copyDB
            copyDB(this);
        }//false??? if??? ????????????
        sqlDB = SQLiteDatabase.openDatabase("/data/data/com.example.gaetalk/databases/addressDB.db", null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor;
        cursor = sqlDB.rawQuery("SELECT distinct(gu) FROM address;", null);//??????????????? rawQuery, ???????????? distinct
        while (cursor.moveToNext()) {
            guData.add(cursor.getString(0));
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, guData);//?????? ???????????????????????? ?????? ????????????????????? this??????
        spGu.setAdapter(adapter);
        spGu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor1;
                dongData.clear();//????????????************* ??????????????? ?????? ????????? ???????????????.
                cursor1 = sqlDB.rawQuery("SELECT dong FROM address WHERE gu='" + spGu.getSelectedItem().toString() + "';", null);
                while (cursor1.moveToNext()) {
                    dongData.add(cursor1.getString(0));//??????!!?????? ?????????????????? name?????????, ??????????????? ??????????????? 1
                }
                adapter1 = new ArrayAdapter<String>(MemberInit.this, android.R.layout.simple_spinner_dropdown_item, dongData);//????????????????????? ??????????????? mainactivity??????
                spDong.setAdapter(adapter1);
                cursor1.close();//??? ??????????????????.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //???????????? ??? ??????
        spDong.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Cursor cursor2;
                cursor2 = sqlDB.rawQuery("SELECT * FROM address WHERE gu='" + spGu.getSelectedItem().toString() + "' AND dong='" + spDong.getSelectedItem().toString() + "';", null);
                cursor2.moveToFirst();
                address1 = cursor2.getString(1) + cursor2.getString(2);
                cursor2.close();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }//onCreate????????? ???

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: {
                if (resultCode == Activity.RESULT_OK) {
                    profilePath = data.getStringExtra("profilePath");
                    Log.e("?????? : ", "profilePath : " + profilePath);
                    Bitmap bmp = BitmapFactory.decodeFile(profilePath);
                    profileImageView.setImageBitmap(bmp);
                }
                break;
            }
        }
    }

    View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()) {
            case R.id.btnMemInitCheck:
                profileUpdate();
                break;
            case R.id.profileImageView:
                LinearLayout linearLayout = findViewById(R.id.linearInitButton);
                if (linearLayout.getVisibility() == View.VISIBLE) {
                    linearLayout.setVisibility(View.GONE);
                } else {
                    linearLayout.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btnTakePicture:
                myStartActivity(CameraActivity.class);
                break;
            case R.id.btnGallery:
                if (ContextCompat.checkSelfPermission(MemberInit.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(MemberInit.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        ActivityCompat.requestPermissions(MemberInit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        ActivityCompat.requestPermissions(MemberInit.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                        showToast("????????? ????????? ?????????");
                    }
                } else {
                    //myStartActivity(Gallery.class);
                }
                break;
        }
    };

    //@SuppressLint("MissingSuperCall")
    /*@Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myStartActivity(Gallery.class);
                } else {
                    showToast("????????? ????????? ?????????");
                }
            }
        }
    }*/

    private void profileUpdate() {
        final String humanName = edtMemInitHumanName.getText().toString();
        final String name = edtMemInitName.getText().toString();
        final String gender = edtMemInitGender.getText().toString();
        final String birth = edtMemInitBirth.getText().toString();
        final String breed = edtMemInitBreed.getText().toString();
        final String personality = edtMemInitPersonality.getText().toString();
        final String address = address1;

        if (humanName.length() > 0 && name.length() > 0 && gender.length() > 0 && birth.length() > 0 && breed.length() > 0 && personality.length() > 0 && address.length() > 0) {
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/" + user.getUid() + "/profileImage.jpg");

            if (profilePath == null) {
                MemberInfo memberInfo = new MemberInfo(humanName, name, gender, birth, breed, address, personality);
                uploader(memberInfo);
            } else {
                try {
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();

                                MemberInfo memberInfo = new MemberInfo(humanName, name, gender, birth, breed, address, personality, downloadUri.toString());
                                uploader(memberInfo);
                            } else {
                                showToast("??????????????? ???????????? ?????????????????????.");
                            }
                        }
                    });
                } catch (FileNotFoundException e) {
                    Log.e("?????? : ", "?????? : " + e.toString());
                }
            }
        } else {
            showToast("??????????????? ??????????????????");
        }
    }

    private void uploader(MemberInfo memberInfo) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(memberInfo)
                .addOnSuccessListener((OnSuccessListener) (aVoid) -> {
                    showToast("???????????? ????????? ?????????????????????");
                    myStartActivity(MainActivity.class);
                })
                .addOnFailureListener((e) -> {
                    showToast("???????????? ????????? ?????????????????????.");
                    Log.w(TAG, "Error writing document", e);
                });
    }

    private void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        startActivityForResult(intent, 0);
    }


    //DB??? ????????? ???????????? ????????? ????????? ???????????? ?????????
    public boolean isCheckDB(Context context) {
        String filePath = "/data/data/com.example.gaetalk/databases/addressDB.db";
        File file = new File(filePath);
        long newdbSize = 0;
        long olddbSize = file.length();//?????? DB?????? ??????
        AssetManager manager = context.getAssets(); //???????????? ???????????? ??????
        try {
            InputStream inputS = manager.open("addressDB.db");//??????????????? ??????
            newdbSize = inputS.available();// available:????????? ??????
        } catch (IOException e) {
            showToast("?????? ????????? ?????? ??? ????????????");
        }
        if (file.exists()) {//????????? ??????????????? ??????
            if (newdbSize != olddbSize) {
                return false;
            } else {
                return true;//??????????????? ????????? ????????? ????????? ??????
            }
        }
        return false;//????????? ????????? ?????? ???????????? false
    }

    //assets????????? ?????? DB??? ?????? ??? DB???????????? ??????(????????? ???????????? ??????:????????? ????????????)
    public void copyDB(Context context) {
        AssetManager manager = context.getAssets();
        String folderPath = "/data/data/com.example.gaetalk/databases";
        String filePath = "/data/data/com.example.gaetalk/databases/addressDB.db";
        File folder = new File(folderPath);
        File file = new File(filePath);
        FileOutputStream fileOS = null;
        BufferedOutputStream bufferOS = null;
        try {
            InputStream inputS = manager.open("addressDB.db");
            BufferedInputStream bufferIS = new BufferedInputStream(inputS);//???????????????db??? ?????????
            if (!folder.exists()) {
                folder.mkdir();//????????? ????????? ??????
            }
            if (file.exists()) {
                file.delete();
                file.createNewFile();
            }
            fileOS = new FileOutputStream(file);
            bufferOS = new BufferedOutputStream(fileOS);
            int read = -1;
            int size = bufferIS.available();
            byte buffer[] = new byte[size];
            while ((read = bufferIS.read(buffer, 0, size)) != -1) {//?????? ??????
                bufferOS.write(buffer, 0, read);
            }
            bufferOS.flush();
            bufferOS.close();
            fileOS.close();
            bufferIS.close();
            inputS.close();
        } catch (IOException e) {
            showToast("?????? ????????? ?????? ??? ????????????");
        }
    }

}