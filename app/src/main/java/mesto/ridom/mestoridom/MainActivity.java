package mesto.ridom.mestoridom;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.LinkedList;
import java.util.List;

import mesto.ridom.mestoridom.adapters.PlaceCategory;
import mesto.ridom.mestoridom.adapters.PlaceCategoryAdapter;


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
        private RecyclerView recyclerView;
        private PlaceCategoryAdapter placeCategoryAdapter;
        private List<PlaceCategory> tmpData;
        private FrameLayout searchPlaceHolder;
        private EditText searchPlace;

        public MapScreenFragment(Bundle args) {
            this.args = args;
        }

        private float dpToPixels(int dp, Context context) {
            return dp * (float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        }

        private float dpToPixels(int dp) {
            return dp * (float) this.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT;
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
            searchPlaceHolder = rootView.findViewById(R.id.main_screen_search_field_holder);
            searchPlace = searchPlaceHolder.findViewById(R.id.main_screen_search_edit_text);

            Drawable searchPlaceHolderBackground = ResourcesCompat.getDrawable(getResources(), R.drawable.bottom_round_corner_search, null);
            assert searchPlaceHolderBackground != null;
            searchPlaceHolderBackground.setTint(ResourcesCompat.getColor(getResources(), R.color.searchPlaceHolder, null));
            searchPlaceHolderBackground.setAlpha((int) (0.12f * 255));
            searchPlaceHolder.setBackground(searchPlaceHolderBackground);

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setDraggable(true);
            bottomSheetBehavior.setHideable(false);
            bottomSheetBehavior.setPeekHeight((int) dpToPixels(260, getActivity()));

            initRecycler();

        }

        private void initRecycler() {
            recyclerView = rootView.findViewById(R.id.place_categories_recycler);
            tmpData = new LinkedList<PlaceCategory>();
            RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    int parentWidth = parent.getWidth();
                    int spaceBetween = (parentWidth - ((int)dpToPixels(57)) * 4 ) / 4;
                    outRect.right = spaceBetween / 2;
                    outRect.left = spaceBetween / 2;
                }
            };
            /*
            TODO handle exceptions with null
             */
            tmpData.add(new PlaceCategory("Выход", 0xFF85C2CC, ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_log_out, null)));
            tmpData.add(new PlaceCategory("Еда", 0xFFFFF6E8, ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_coffee, null)));
            tmpData.add(new PlaceCategory("Туалет", 0xFFF1FFF0, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_group_15, null)));
            tmpData.add(new PlaceCategory("Лифт", 0xFFD7FAFF, ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_select_o, null)));
            tmpData.add(new PlaceCategory("Выход", 0xFF85C2CC, ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_log_out, null)));
            tmpData.add(new PlaceCategory("Еда", 0xFFFFF6E8, ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_coffee, null)));
            tmpData.add(new PlaceCategory("Туалет", 0xFFF1FFF0, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_group_15, null)));
            tmpData.add(new PlaceCategory("Лифт", 0xFFD7FAFF, ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_select_o, null)));
            tmpData.add(new PlaceCategory("Выход", 0xFF85C2CC, ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_log_out, null)));
            tmpData.add(new PlaceCategory("Еда", 0xFFFFF6E8, ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_coffee, null)));
            tmpData.add(new PlaceCategory("Туалет", 0xFFF1FFF0, ResourcesCompat.getDrawable(getResources(), R.drawable.ic_group_15, null)));
            tmpData.add(new PlaceCategory("Лифт", 0xFFD7FAFF, ResourcesCompat.getDrawable(getResources() ,R.drawable.ic_select_o, null)));
            placeCategoryAdapter = new PlaceCategoryAdapter(tmpData);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(placeCategoryAdapter);
            recyclerView.addItemDecoration(itemDecoration);
        }
    }

    public static class SettingsScreenFragment extends Fragment {
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            return inflater.inflate(R.layout.settings_screen_layout, container, false);
        }
    }
}
