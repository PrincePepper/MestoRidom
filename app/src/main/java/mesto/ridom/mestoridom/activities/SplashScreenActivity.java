package mesto.ridom.mestoridom.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import mesto.ridom.mestoridom.R;
import mesto.ridom.mestoridom.authorization.AuthorizationActivity;

public class SplashScreenActivity extends BaseActivity {
    private static final String MY_SETTINGS = "my_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);
        SharedPreferences sp = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sp.getBoolean("hasVisited", false);

        if (!hasVisited) {
            // выводим нужную активность
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.apply(); // не забудьте подтвердить изменения
            startIntroAmimation();
        } else {
            missIntro();
        }

    }

    private void missIntro() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent SplashIntent = new Intent(SplashScreenActivity.this, AuthorizationActivity.class);
                startActivity(SplashIntent);
                finish();
            }
        }, 3000);
    }

    private void startIntroAmimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent SplashIntent = new Intent(SplashScreenActivity.this, IntroActivity.class);
                startActivity(SplashIntent);
                finish();
            }
        }, 3000);
    }
}
