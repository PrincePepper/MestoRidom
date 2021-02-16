package mesto.ridom.mestoridom.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import mesto.ridom.mestoridom.R;
import mesto.ridom.mestoridom.animations.FabAnimation;


public class FabFloorActivity extends AppCompatActivity {

    boolean isRotate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_floor);
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
                Toast.makeText(FabFloorActivity.this, "Calling", Toast.LENGTH_SHORT).show();
            }
        });

        fabthird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(FabFloorActivity.this, "mic", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
