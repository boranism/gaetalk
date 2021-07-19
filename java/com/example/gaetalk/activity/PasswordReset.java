package com.example.gaetalk.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gaetalk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


public class PasswordReset extends AppCompatActivity {
    private FirebaseAuth mAuth;//안드로이드와 파이어베이스 사이의 인증을 확인하기 위한 인스턴스 선언
    EditText edtPasswordReset;
    Button btnPasswordReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.password_reset);
        mAuth = FirebaseAuth.getInstance();//선언한 인스턴스 초기화
        btnPasswordReset = findViewById(R.id.btnPasswordReset);
        edtPasswordReset = findViewById(R.id.edtPasswordReset);

        btnPasswordReset.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = (v) -> {
        switch (v.getId()) {
            case R.id.btnPasswordReset:
                send();
                break;
        }
    };

    //이메일 보내기
    private void send() {
        String email = edtPasswordReset.getText().toString();

        if (email.length() > 0) {
            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                showToast("이메일을 보냈습니다");
                            }
                        }
                    });
        } else {
            showToast("이메일을 입력해 주세요");
        }
    }

    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }
}