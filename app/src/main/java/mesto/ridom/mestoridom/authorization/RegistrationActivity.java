package mesto.ridom.mestoridom.authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
import java.util.regex.Pattern;

import mesto.ridom.mestoridom.MainActivity;
import mesto.ridom.mestoridom.R;

public class RegistrationActivity extends AuthorizationActivity implements View.OnClickListener {
    EditText mNameField, mEmailField, mPasswordField, mPasswordFieldRepeat;
    CheckBox reg_checkBox;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    //"(?=.*[a-zA-Z])" +      //any letter
                    //"(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_3);
        KeyboardVisibilityEvent();

        mNameField = findViewById(R.id.reg_name);
        mEmailField = findViewById(R.id.reg_email_shield);
        mPasswordField = findViewById(R.id.reg_password_shield);
        mPasswordFieldRepeat = findViewById(R.id.reg_password_shield_repeat);

        findViewById(R.id.registration_btn).setOnClickListener(this);
        reg_checkBox = (CheckBox) findViewById(R.id.reg_checkBox);
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

    }

    private void createAccount(String email, String password) {
        Log.d(TAG_EMAIL, "createAccount:" + email);
        if (validateForm(email, password)) {
            return;
        }
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_EMAIL, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_EMAIL, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, R.string.authentication_failed, Toast.LENGTH_SHORT).show();
                        }
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Заполните поле");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailField.setError("Пожалуйста напишите почту верно.");
            valid = false;
        } else mEmailField.setError(null);

        if (mPasswordFieldRepeat.getText().toString().equals(password)) {
            if (TextUtils.isEmpty(password)) {
                mPasswordField.setError("Заполните поле");
                valid = false;
            } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
                mPasswordField.setError("Пароль слишком легкий");
                valid = false;
            } else {
                mPasswordField.setError(null);
            }
        } else mPasswordFieldRepeat.setError("Пароли не совпадают.");

        return !valid;
    }

    private void sendEmailVerification() {
        // Send verification email
        // [START send_email_verification]
        final FirebaseUser user = mAuth.getCurrentUser();
        Objects.requireNonNull(user).sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        // Re-enable button
                        // [END_EXCLUDE]
                    }
                });
        // [END send_email_verification]
    }

    private void updateUI(FirebaseUser user) {
        SharedPreferences.Editor editor = sp2.edit();
        if (user != null) {
            editor.putBoolean(APP_PREFERENCES_PEOPLE, true);
            editor.apply();
            Intent RegistrationIntent = new Intent(RegistrationActivity.this, MainActivity.class);
            RegistrationActivity.this.startActivity(RegistrationIntent);
        }
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();

        if (i == R.id.registration_btn) {
            if (reg_checkBox.isChecked())
                createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        }
    }

}
