package mesto.ridom.mestoridom;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class MainActivity extends BaseActivity {

    private ViewPager2 viewPager;
    private FragmentStateAdapter fragmentStateAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.main_screen_pager);
        tabLayout = findViewById(R.id.main_screen_tab_layout);
        fragmentStateAdapter = new FragmentAdapter(this);
        viewPager.setAdapter(fragmentStateAdapter);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setText(getResources().getString(R.string.map_screen));
                        break;
                    default:
                        tab.setText(getResources().getString(R.string.settings_screen));
                        break;
                }
            }
        }).attach();
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

    class FragmentAdapter extends FragmentStateAdapter {

        Bundle mapScreenFragmentState;
        Bundle settingsScreenFragmentState;

        public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public FragmentAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new MapScreenFragment(mapScreenFragmentState);
                default:
                    return new SettingsScreenFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    public static class MapScreenFragment extends Fragment {

        public static final String ARGS_MAP_FRAGMENT = "ARGS_MAP_FRAGMENT";

        private Bundle args;
        private View rootView;
        private LinearLayout bottomSheet;
        private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;

        public MapScreenFragment(Bundle args) {
            this.args = args;
        }

        private float dpToPixels(int dp, Context context) {
            return dp * (float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.map_screen_layout, container, false);
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            // unpack args
            bottomSheet = rootView.findViewById(R.id.main_screen_bottom_sheet);
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setDraggable(true);
            bottomSheetBehavior.setHideable(false);
            bottomSheetBehavior.setPeekHeight((int)dpToPixels(260, getActivity()));
        }
    }

    public static class SettingsScreenFragment extends Fragment {
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            return inflater.inflate(R.layout.settings_screen_layout, container, false);
        }
    }
}
