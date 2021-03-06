package finalproject.se.kmitl.findmythings.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import finalproject.se.kmitl.findmythings.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText tvUsername;
    private EditText tvPassword;
    private TextView tvSignUp;
    private Button btnLogin;
    private ProgressDialog mLoginProgress;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_login);
        initInstances();
        mAuth = FirebaseAuth.getInstance();
    }

    private void initInstances() {
        tvUsername = findViewById(R.id.etUserName);
        tvPassword = findViewById(R.id.etPassword);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setPaintFlags(tvSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        btnLogin = findViewById(R.id.btnLogin);

        mLoginProgress = new ProgressDialog(this);

        btnLogin.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tvSignUp) {
            Intent intent = new Intent(LoginActivity.this, CreateAccountActivity.class);
            startActivity(intent);
        } else if (view.getId() == R.id.btnLogin) {
            String email = tvUsername.getText().toString().trim();
            String password = tvPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                tvUsername.setError("กรุณากรอกอีเมล");
            } else if (TextUtils.isEmpty(password)) {
                tvPassword.setError("กรุณากรอกรหัสผ่าน");
            } else {
                mLoginProgress.setTitle("กำลังเข้าสู่ระบบ");
                mLoginProgress.setMessage("โปรดรอสักครู่");
                mLoginProgress.setCanceledOnTouchOutside(false);
                mLoginProgress.show();
                loginUser(email, password);
            }

        }
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("", "signInWithEmail:success");
                            mLoginProgress.dismiss();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            mLoginProgress.hide();
                            FirebaseAuthException e = (FirebaseAuthException) task.getException();
                            Toast.makeText(LoginActivity.this, e.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }
}
