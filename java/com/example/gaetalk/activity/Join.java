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


public class Join extends AppCompatActivity {
    private FirebaseAuth mAuth;//안드로이드와 파이어베이스 사이의 인증을 확인하기 위한 인스턴스 선언
    EditText edtJoinEmail, edtJoinPassword, edtJoinPasswordCheck;
    Button btnCheck, btnGoToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        mAuth = FirebaseAuth.getInstance();//선언한 인스턴스 초기화
        edtJoinEmail = findViewById(R.id.edtJoinEmail);
        edtJoinPassword = findViewById(R.id.edtJoinPassword);
        edtJoinPasswordCheck = findViewById(R.id.edtJoinPasswordCheck);
        btnCheck = findViewById(R.id.btnCheck);
        btnGoToLogin = findViewById(R.id.btnGoToLogin);

        btnCheck.setOnClickListener(onClickListener);
        btnGoToLogin.setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnCheck:
                    signUp();
                    break;
                case R.id.btnGoToLogin:
                    myStartActivity(Login.class);
                    break;
            }
        }
    };

    //회원가입
    private void signUp() {
        String email = edtJoinEmail.getText().toString();
        String password = edtJoinPassword.getText().toString();
        String passwordCheck = edtJoinPasswordCheck.getText().toString();
        if (email.length() > 0 && password.length() > 0 && passwordCheck.length() > 0) {
            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override//사용자가 입력한 이메일과 비밀번호 파이어베이스에 저장시키는거
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {//정상적으로 저장될경우
                            FirebaseUser user = mAuth.getCurrentUser();
                            showToast("회원가입에 성공했습니다");
                            myStartActivity(MainActivity.class);
                        } else {//저장에 실해할 경우
                            if (task.getException() != null) {
                                showToast("회원가입에 실패했습니다");
                            }
                        }
                    }
                });
            } else {
                showToast("비밀번호가 일치하지 않습니다");
            }
        } else {
            showToast("이메일 또는 비밀번호를 입력해주세요");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
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
