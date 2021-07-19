package com.example.gaetalk.activity;

import android.content.Intent;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Login extends AppCompatActivity {
    private FirebaseAuth mAuth;//안드로이드와 파이어베이스 사이의 인증을 확인하기 위한 인스턴스 선언
    EditText edtLoginEmail, edtLoginPassword;
    Button btnLogin, btnGoToPasswordReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();//선언한 인스턴스 초기화
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoToPasswordReset = findViewById(R.id.btnGoToPasswordReset);

        btnLogin.setOnClickListener(onClickListener);
        btnGoToPasswordReset.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnLogin:
                    login();
                    break;
                case R.id.btnGoToPasswordReset:
                    myStartActivity(PasswordReset.class);
                    break;
            }
        }
    };

    //파이어베이스 로그인
    private void login() {
        String email = edtLoginEmail.getText().toString();
        String password = edtLoginPassword.getText().toString();
        if (email.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        showToast("로그인 성공");
                        myStartActivity(MainActivity.class);
                    } else {
                        if (task.getException() != null) {
                            showToast("로그인 실패");
                        }
                    }
                }
            });
        } else {
            showToast("이메일 또는 비밀번호를 입력해 주세요");
        }
    }

    void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(this, c);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}