package finalproject.se.kmitl.findmythings.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import finalproject.se.kmitl.findmythings.R;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etDisplayName;
    private EditText etPhoneNumber;
    private Button btnCreateAccount;

    private FirebaseAuth mAuth;
    private DatabaseReference userProfileDatabase;
    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initInstance();
        mAuth = FirebaseAuth.getInstance();
        userProfileDatabase = FirebaseDatabase.getInstance().getReference().child("user_profile");
    }

    private void initInstance() {
        etEmail = findViewById(R.id.reg_email);
        etPassword = findViewById(R.id.reg_password);
        etConfirmPassword = findViewById(R.id.reg_confirmpass);
        etDisplayName = findViewById(R.id.reg_displayname);
        etPhoneNumber = findViewById(R.id.reg_phonenumber);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        mRegProgress = new ProgressDialog(this);

        btnCreateAccount.setOnClickListener(this);

    }

    private void registerUser(final String email, String password, String confirmPassword, final String displayName, final String phoneNumber) {
        if (password.equals(confirmPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Map<String, Object> map = new HashMap<>();
                        String key = FirebaseAuth.getInstance().getUid();
                        map.put(key, "");
                        userProfileDatabase.updateChildren(map);
                        DatabaseReference message_key2 = userProfileDatabase.child(key);
                        Map<String, Object> map2 = new HashMap<>();
                        map2.put("email", email);
                        map2.put("phone", phoneNumber);
                        map2.put("displayname", displayName);
                        message_key2.updateChildren(map2);
                        mRegProgress.dismiss();
                        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        mRegProgress.hide();
                        FirebaseAuthException e = (FirebaseAuthException) task.getException();
                        Toast.makeText(CreateAccountActivity.this, "Failed Registration: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });
        } else {
            Toast.makeText(CreateAccountActivity.this, "NOT MATCH PASSWORD!!", Toast.LENGTH_SHORT).show();
            mRegProgress.hide();
        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnCreateAccount) {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String confirmPassword = etConfirmPassword.getText().toString().trim();
            String displayName = etDisplayName.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                etEmail.setError("กรุณากรอกอีเมล");
            } else if (TextUtils.isEmpty(password)) {
                etPassword.setError("กรุณากรอกรหัสผ่าน");
            } else if (TextUtils.isEmpty(confirmPassword)) {
                etConfirmPassword.setError("กรุณากรอกยืนยันรหัสผ่าน");
            } else if (TextUtils.isEmpty(displayName)) {
                etDisplayName.setError("กรุณากรอกชื่อที่แสดงในระบบ");
            } else if (TextUtils.isEmpty(phoneNumber)) {
                etPhoneNumber.setError("กรุณากรอกหมายเลขโทรศัพท์");
            } else {
                mRegProgress.setTitle("กำลังสมัครผู้ใช้งาน");
                mRegProgress.setMessage("กรุณารอสักครู่");
                mRegProgress.show();
                registerUser(email, password, confirmPassword, displayName, phoneNumber);
            }
        }
    }


}
