package mesto.ridom.mestoridom.authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

import mesto.ridom.mestoridom.R;
import mesto.ridom.mestoridom.activities.MainActivity;

public class LoginActivity extends AuthorizationActivity implements View.OnClickListener, TextView.OnEditorActionListener {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //at least 1 digit
                    "(?=.*[a-z])" +         //at least 1 lower case letter
                    "(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=\\S+$)" +           //no white spaces
                    ".{6,}" +               //at least 4 characters
                    "$");

    private EditText mPasswordField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_old);
        KeyboardVisibilityEvent();  //отключение клавы

        mPasswordField = findViewById(R.id.password_shield);
        mPasswordField.setOnClickListener(this);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        mPasswordField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(LoginActivity.this);
                    signIn_Email(PublicEmail, mPasswordField.getText().toString());
                }
                return true;
            }
        });
    }

    private void signIn_Email(String email, String password) {
        Log.d(TAG_EMAIL, "signIn_Email:" + email);
        if (validateForm(password)) {
            return;
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_EMAIL, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_EMAIL, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, R.string.authentication_failed,
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
//                            // [START_EXCLUDE]
//                            // [END_EXCLUDE]
                        }

                    }
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm(String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Поле не может быть пустым");
            valid = false;
        } else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            mPasswordField.setError("Пароль слишком слабый");
            valid = false;
        } else {
            mPasswordField.setError(null);
        }

        return !valid;
    }


    private void updateUI(FirebaseUser user) {
        if (user != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(APP_PREFERENCES_PEOPLE, true);
            editor.apply();
            Intent SplashIntent = new Intent(LoginActivity.this, MainActivity.class);
            LoginActivity.this.startActivity(SplashIntent);
            LoginActivity.this.finish();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        return false;
    }
}