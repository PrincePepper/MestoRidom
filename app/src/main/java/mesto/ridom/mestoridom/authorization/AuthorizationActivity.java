package mesto.ridom.mestoridom.authorization;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;

import java.util.List;

import mesto.ridom.mestoridom.activities.BaseActivity;
import mesto.ridom.mestoridom.activities.MainActivity;
import mesto.ridom.mestoridom.R;
import mesto.ridom.mestoridom.activities.SplashScreenActivity;


public class AuthorizationActivity extends BaseActivity implements View.OnClickListener {
    public static final String APP_PREFERENCES_PEOPLE = "Person";
    public static SharedPreferences sp2;
    public static final String TAG_EMAIL = "EmailPasswordLogin";
    private EditText mEmailField;

    // [START declare_auth]
    protected FirebaseAuth mAuth;
    // [END declare_auth]

    public static final String TAG_GOOGLE = "GoogleActivity";
    private GoogleSignInClient mGoogleSignInClient;
    // private ActivityGoogleBinding mBinding;
    private static final int RC_SIGN_IN = 9001;

    private boolean temp = true;

    private static final String TAG_ANON = "AnonymousAuth";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_1);
        KeyboardVisibilityEvent();

        sp2 = getSharedPreferences(APP_PREFERENCES_PEOPLE, MODE_PRIVATE);

        mEmailField = findViewById(R.id.email_shield);

        findViewById(R.id.check_login_reg).setOnClickListener(this);
        findViewById(R.id.skip_login_step).setOnClickListener(this);
        findViewById(R.id.button_login_google).setOnClickListener(this);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (sp2.contains(APP_PREFERENCES_PEOPLE))
            if (!sp2.getBoolean(APP_PREFERENCES_PEOPLE, false))
                signOut();
    }

    @Override
    public void onRestart() {
        super.onRestart();

        if (temp)
            overridePendingTransition(R.anim.reg_left_in, R.anim.reg_right_out);
        else overridePendingTransition(R.anim.login_right_in, R.anim.login_left_out);
    }

    public void differentiateLink(final String email) {
        if (validateForm(email)) {
            return;
        }
        SplashScreenActivity.Email = email;
        // [START auth_differentiate_link]
        mAuth.fetchSignInMethodsForEmail(email)
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        if (task.isSuccessful()) {
                            SignInMethodQueryResult result = task.getResult();
                            List<String> signInMethods = result.getSignInMethods();
                            if (signInMethods.contains(EmailAuthProvider.EMAIL_PASSWORD_SIGN_IN_METHOD)) {
                                Intent LoginIntent = new Intent(AuthorizationActivity.this, LoginActivity.class);
                                AuthorizationActivity.this.startActivity(LoginIntent);
                                overridePendingTransition(R.anim.login_right_in, R.anim.login_left_out);
                            } else {
                                Intent RegIntent = new Intent(AuthorizationActivity.this, RegistrationActivity.class);
                                AuthorizationActivity.this.startActivity(RegIntent);
                                overridePendingTransition(R.anim.reg_left_in, R.anim.reg_right_out);
                                temp = false;
                            }
                        } else {
                            Log.e(TAG_EMAIL, "Error getting sign in methods for user", task.getException());
                        }
                    }
                });
        // [END auth_differentiate_link]
        findViewById(R.id.check_login_reg).setEnabled(true);
        findViewById(R.id.skip_login_step).setEnabled(true);
        findViewById(R.id.button_login_google).setEnabled(true);
        mEmailField.setEnabled(true);
    }


    private void signIn_Google() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
        findViewById(R.id.check_login_reg).setEnabled(true);
        findViewById(R.id.skip_login_step).setEnabled(true);
        findViewById(R.id.button_login_google).setEnabled(true);
        mEmailField.setEnabled(true);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG_GOOGLE, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG_GOOGLE, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }


    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_GOOGLE, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_GOOGLE, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                    }
                });
    }

    private void signInAnonymously() {
        // [START signin_anonymously]
        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG_ANON, "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_ANON, "signInAnonymously:failure", task.getException());
                            Toast.makeText(AuthorizationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END signin_anonymously]
        findViewById(R.id.check_login_reg).setEnabled(true);
        findViewById(R.id.skip_login_step).setEnabled(true);
        findViewById(R.id.button_login_google).setEnabled(true);
        mEmailField.setEnabled(true);
    }

    private boolean validateForm(String email) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Field can't be empty.");
            valid = false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailField.setError("Please enter a valid email address");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        return !valid;
    }


    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
        mAuth.signOut();
    }


    private void getCurrentUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();
            Uri photoUrl = user.getPhotoUrl();

            // Check if user's email is verified
            boolean emailVerified = user.isEmailVerified();

            // The user's ID, unique to the Firebase project. Do NOT use this value to
            // authenticate with your backend server, if you have one. Use
            // FirebaseUser.getIdToken() instead.
            String uid = user.getUid();
        }
    }


    private void updateUI(FirebaseUser user) {
        SharedPreferences.Editor editor = sp2.edit();
        if (user != null) {
            editor.putBoolean(APP_PREFERENCES_PEOPLE, true);
            editor.apply();
            Intent AuthorizationIntent = new Intent(AuthorizationActivity.this, MainActivity.class);
            AuthorizationActivity.this.startActivity(AuthorizationIntent);
        }
    }

    @Override
    public void onClick(View view) {
        findViewById(R.id.check_login_reg).setEnabled(false);
        findViewById(R.id.skip_login_step).setEnabled(false);
        findViewById(R.id.button_login_google).setEnabled(false);
        mEmailField.setEnabled(false);
        int i = view.getId();
        if (i == R.id.check_login_reg) {
            hideKeyboard(AuthorizationActivity.this);
            differentiateLink(mEmailField.getText().toString());
            //createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
        } else if (i == R.id.button_login_google) {
            signIn_Google();

        } else if (i == R.id.skip_login_step) {

            signInAnonymously();
        }
    }
}