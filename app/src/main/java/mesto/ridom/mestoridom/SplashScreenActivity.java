package mesto.ridom.mestoridom;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);

        startSplashAmimation();
    }

    private void startSplashAmimation() {
        startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent SplashIntent = new Intent(SplashScreenActivity.this, NavigationActivity.class);
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
