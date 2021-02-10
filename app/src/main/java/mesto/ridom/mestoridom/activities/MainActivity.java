package mesto.ridom.mestoridom.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.LinkedList;
import java.util.List;

import mesto.ridom.mestoridom.R;
import mesto.ridom.mestoridom.activities.BaseActivity;
import mesto.ridom.mestoridom.adapters.DisplayPlaceAdapter;
import mesto.ridom.mestoridom.adapters.PlaceCategory;
import mesto.ridom.mestoridom.adapters.PlaceCategoryAdapter;
import mesto.ridom.mestoridom.viewmodel.PlaceCategoryViewModel;
import mesto.ridom.mestoridom.viewmodel.PlacesViewModel;


public class MainActivity extends BaseActivity {

    private ViewPager2 viewPager;
    private FragmentStateAdapter fragmentStateAdapter;
    private TabLayout tabLayout;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
                    return new MapScreenFragment(mapScreenFragmentState, imm);
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
        private FrameLayout searchPlaceHolder;
        private EditText searchPlace;
        private InputMethodManager imm;
        private RecyclerView placeSearchRecycler;
        private DisplayPlaceAdapter displayPlaceAdapter;

        private PlaceCategoryViewModel placeCategoryViewModel;
        private PlacesViewModel placesViewModel;

        public MapScreenFragment(Bundle args, InputMethodManager imm) {
            this.imm = imm;
            this.args = args;
        }

        private float dpToPixels(int dp, Context context) {
            return dp * (float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT;
        }

        private float dpToPixels(int dp) {
            return dpToPixels(dp, getContext());
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

            ViewModelProvider viewModelProvider = new ViewModelProvider(this);
            placeCategoryViewModel = viewModelProvider.get(PlaceCategoryViewModel.class);
            placesViewModel = viewModelProvider.get(PlacesViewModel.class);

            bottomSheet = rootView.findViewById(R.id.main_screen_bottom_sheet);
            searchPlaceHolder = rootView.findViewById(R.id.main_screen_search_field_holder);
            searchPlace = searchPlaceHolder.findViewById(R.id.main_screen_search_edit_text);

            Drawable searchPlaceHolderBackground = ResourcesCompat.getDrawable(getResources(), R.drawable.bottom_round_corner_search, null);
            assert searchPlaceHolderBackground != null;
            searchPlaceHolderBackground.setTint(ResourcesCompat.getColor(getResources(), R.color.searchPlaceHolder, null));
            searchPlaceHolderBackground.setAlpha((int) (0.12f * 255));
            searchPlaceHolder.setBackground(searchPlaceHolderBackground);

            searchPlace.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    if (placeSearchRecycler == null) {
                        initPlaceSearchRecycler();
                    }
                    placeSearchRecycler.setVisibility(View.VISIBLE);
                    return false;
                }
            });

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setDraggable(true);
            bottomSheetBehavior.setHideable(false);
            bottomSheetBehavior.setPeekHeight((int) dpToPixels(260, getActivity()));

            initRecycler();
        }

        private void initRecycler() {
            recyclerView = rootView.findViewById(R.id.place_categories_recycler);
            RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    int parentWidth = parent.getWidth();
                    int spaceBetween = (parentWidth - ((int) dpToPixels(57)) * 4) / 4;
                    outRect.right = spaceBetween / 2;
                    outRect.left = spaceBetween / 2;
                }
            };

            placeCategoryAdapter = new PlaceCategoryAdapter();

            placeCategoryViewModel.setResources(getResources());
            placeCategoryViewModel.getPaceCategories().observe(getViewLifecycleOwner(), new Observer<List<PlaceCategory>>() {
                @Override
                public void onChanged(List<PlaceCategory> placeCategories) {
                    placeCategoryAdapter.data = placeCategories;
                    placeCategoryAdapter.notifyDataSetChanged();
                }
            });

            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            recyclerView.setAdapter(placeCategoryAdapter);
            recyclerView.addItemDecoration(itemDecoration);
        }

        private void initPlaceSearchRecycler() {
            placeSearchRecycler = rootView.findViewById(R.id.place_search_recycler);
            displayPlaceAdapter = new DisplayPlaceAdapter();
            placesViewModel.getPlaces().observe(getViewLifecycleOwner(), new Observer<List<DisplayPlaceAdapter.Place>>() {
                @Override
                public void onChanged(List<DisplayPlaceAdapter.Place> places) {
                    displayPlaceAdapter.data = places;
                    displayPlaceAdapter.notifyDataSetChanged();
                }
            });
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            placeSearchRecycler.setAdapter(displayPlaceAdapter);
            placeSearchRecycler.setLayoutManager(linearLayoutManager);
        }

    }

    public static class SettingsScreenFragment extends Fragment {
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {

            return inflater.inflate(R.layout.settings_screen_layout, container, false);
        }
    }
}
