package finalproject.se.kmitl.findmythings.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import finalproject.se.kmitl.findmythings.R;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private EditText etDisplayName;
    private EditText etPhoneNumber;
    private Button btnCreateAccount;

    private FirebaseAuth mAuth;
    private ProgressDialog mRegProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initInstance();
        mAuth = FirebaseAuth.getInstance();
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

    private void registerUser(String email, String password, String confirmPassword, String displayName, String phoneNumber) {
        if (password.equals(confirmPassword)) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        mRegProgress.dismiss();
                        Intent intent = new Intent(CreateAccountActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }else {
                        mRegProgress.hide();
                        Toast.makeText(CreateAccountActivity.this, "You got some error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else {
            Toast.makeText(CreateAccountActivity.this, "NOT MATCH PASSWORD!!", Toast.LENGTH_SHORT).show();
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
            mRegProgress.setTitle("Registering User");
            mRegProgress.setMessage("Please wait while we create your account!");
            mRegProgress.show();
            registerUser(email, password, confirmPassword, displayName, phoneNumber);

        }
    }


}
