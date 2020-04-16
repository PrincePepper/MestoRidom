package mesto.ridom.mestoridom;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String MY_SETTINGS = "my_settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);
        SharedPreferences sp = getSharedPreferences(MY_SETTINGS,
                Context.MODE_PRIVATE);
        // проверяем, первый ли раз открывается программа
        boolean hasVisited = sp.getBoolean("hasVisited", false);
        if (!hasVisited) {
            // выводим нужную активность

            SharedPreferences.Editor e = sp.edit();
            e.putBoolean("hasVisited", true);
            e.apply(); // не забудьте подтвердить изменения
            startSplashAmimation();
        }else{
            startSplashAmimation2();
        }

    }
    private void startSplashAmimation2() {
        startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent SplashIntent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                SplashScreenActivity.this.startActivity(SplashIntent);

                SplashScreenActivity.this.finish();

            }
        }, 3000);
    }
    private void startSplashAmimation() {
        startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent SplashIntent = new Intent(SplashScreenActivity.this, IntroActivity.class);
                SplashScreenActivity.this.startActivity(SplashIntent);

                SplashScreenActivity.this.finish();

            }
        }, 3000);
    }

    private void startAnimation() {
        ImageView imageView = findViewById(R.id.progressbar_screen);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.comboanim);
        imageView.startAnimation(animation);
    }
}
