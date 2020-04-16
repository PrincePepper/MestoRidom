package mesto.ridom.mestoridom;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class IntroActivity extends AppCompatActivity {
    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        final TextView textViewGip = findViewById(R.id.intro_giptext);
        final TextView textView = findViewById(R.id.intro_text);
        final TextView textViewH = findViewById(R.id.intro_htext);
        final ImageView imageView = findViewById(R.id.intro_image);
        final Button button = findViewById(R.id.intro_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (a) {
                    case 0:
                        textView.setText(R.string.intro_text_2);
                        textViewH.setText(R.string.intro_htext_2);
                        imageView.setImageResource(R.drawable.ic_e_loupe);
                        a++;
                        break;
                    case 1:
                        textView.setText(R.string.intro_text_3);
                        textViewH.setText(R.string.intro_htext_3);
                        imageView.setImageResource(R.drawable.ic_e_stars);
                        a++;
                        break;
                    case 2:
                        textView.setText(R.string.intro_text_4);
                        textViewH.setText(R.string.intro_htext_4);
                        imageView.setImageResource(R.drawable.ic_e_abc);
                        a++;
                        break;
                    case 3:
                        textView.setText(R.string.intro_text_5);
                        textViewH.setText(R.string.intro_htext_5);
                        imageView.setImageResource(R.drawable.ic_mesto_ridom);
                        a++;
                        break;
                    case 4:
                        Intent SplashIntent = new Intent(IntroActivity.this, MainActivity.class);
                        IntroActivity.this.startActivity(SplashIntent);
                        IntroActivity.this.finish();
                }
            }
        });
        textViewGip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent SplashIntent = new Intent(IntroActivity.this, MainActivity.class);
                IntroActivity.this.startActivity(SplashIntent);

                IntroActivity.this.finish();
            }
        });
    }
}
