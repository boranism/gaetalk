package com.example.gaetalk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.gaetalk.activity.MainActivity;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(2000); /*동시에 여러개 프로세스 처리하는 기능
            스플래시 액티비티가 실행된 후에 3초동안 가만히 있겠다는 뜻*/
        } catch (InterruptedException e) {//Thred는 인터럽트가 발생할수있다
            e.printStackTrace();
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);//메인을 띄우면서
        finish();//나(스플래시는 사라진다.) 이것이 스플래시 두번째 방법!
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}