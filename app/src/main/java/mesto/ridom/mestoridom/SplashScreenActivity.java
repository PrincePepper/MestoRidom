package mesto.ridom.mestoridom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import mesto.ridom.mestoridom.authorization.AuthorizationActivity;

public class SplashScreenActivity extends BaseActivity {
    private static final String MY_SETTINGS = "my_settings";

    public static final String APP_PREFERENCES_NAME = "Name_Person";
    private static SharedPreferences sp;
    public static String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);
        sp = getSharedPreferences(MY_SETTINGS, MODE_PRIVATE);
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sp.getBoolean("hasVisited", false);

        if (!hasVisited) {
            // выводим нужную активность
            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.apply(); // не забудьте подтвердить изменения
            startSplashAmimation();
        } else {
            startSplashAmimation2();
        }

    }

    private void startSplashAmimation2() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent SplashIntent = new Intent(SplashScreenActivity.this, AuthorizationActivity.class);
                SplashScreenActivity.this.startActivity(SplashIntent);
                SplashScreenActivity.this.finish();

            }
        }, 3000);
    }

    private void startSplashAmimation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent SplashIntent = new Intent(SplashScreenActivity.this, IntroActivity.class);
                SplashScreenActivity.this.startActivity(SplashIntent);
                SplashScreenActivity.this.finish();

            }
        }, 3000);
    }
}
