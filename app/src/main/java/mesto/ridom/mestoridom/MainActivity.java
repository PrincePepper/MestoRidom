package mesto.ridom.mestoridom;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import mesto.ridom.mestoridom.authorization.AuthorizationActivity;

public class MainActivity extends BaseActivity {

    boolean isRotate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button fabfirst = findViewById(R.id.fabFirst);
        final Button fabsecond = findViewById(R.id.fabSecond);
        final Button fabthird = findViewById(R.id.fabThird);
        FabAnimation.init(fabsecond);
        FabAnimation.init(fabthird);

        fabfirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRotate = FabAnimation.rotateFab(v, !isRotate);
                if (isRotate) {
                    FabAnimation.showIn(fabsecond);
                    FabAnimation.showIn(fabthird);
                } else {
                    FabAnimation.showOut(fabsecond);
                    FabAnimation.showOut(fabthird);
                }

            }
        });
        fabsecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = AuthorizationActivity.sp2.edit();
                editor.putBoolean(AuthorizationActivity.APP_PREFERENCES_PEOPLE, false);
                editor.apply();

                Intent SplashIntent = new Intent(MainActivity.this, AuthorizationActivity.class);
                MainActivity.this.startActivity(SplashIntent);
                MainActivity.this.finish();
            }
        });

        fabthird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "mic", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private static long back_pressed;

    @Override//выход по времени
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Нажми еще раз чтобы выйти!",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }

}
