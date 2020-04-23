package mesto.ridom.mestoridom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //убираем вернее меню
        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final TextView textViewGip = findViewById(R.id.login_giptext);
        textViewGip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SplashIntent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(SplashIntent);

                LoginActivity.this.finish();
            }
        });
    }
}
