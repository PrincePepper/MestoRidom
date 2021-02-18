package mesto.ridom.mestoridom.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import mesto.ridom.mestoridom.R;
import mesto.ridom.mestoridom.adapters.DisplayPlaceAdapter;
import mesto.ridom.mestoridom.adapters.FloorAdapter;
import mesto.ridom.mestoridom.adapters.PlaceCategory;
import mesto.ridom.mestoridom.adapters.PlaceCategoryAdapter;
import mesto.ridom.mestoridom.viewmodel.PlaceCategoryViewModel;
import mesto.ridom.mestoridom.viewmodel.PlacesViewModel;


public class MainActivity extends BaseActivity {

    private static long back_pressed; //подчет секунд перед нажатием выхода

    private ViewPager2 viewPager;
    private FragmentStateAdapter fragmentStateAdapter;
    private TabLayout tabLayout;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        onWindowFocusChanged(false);
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

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        showSystemUI();
    }

    @Override//выход по времени
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else Toast.makeText(getBaseContext(), "Нажми еще раз чтобы выйти!",
                Toast.LENGTH_SHORT).show();

        back_pressed = System.currentTimeMillis();
    }

    class FragmentAdapter extends FragmentStateAdapter {

        Bundle mapScreenFragmentState;
        Bundle settingsScreenFragmentState;

        public FragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        ViewGroup v = (ViewGroup) findViewById(R.id.main_screen_bottom_sheet);
        if (v != null)
//            requestDisallowInterceptTouchEvent(v, true);
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    requestDisallowInterceptTouchEvent(v, true);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    requestDisallowInterceptTouchEvent(v, false);
                    break;
                default:
                    break;
            }
        return super.dispatchTouchEvent(event);
    }

    private void requestDisallowInterceptTouchEvent(ViewGroup v, boolean disallowIntercept) {
        v.requestDisallowInterceptTouchEvent(disallowIntercept);
        int childCount = v.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = v.getChildAt(i);
            if (child instanceof ViewGroup) {
                requestDisallowInterceptTouchEvent((ViewGroup) child, disallowIntercept);
            }
        }
    }

    public static class MapScreenFragment extends Fragment {

        public static final String ARGS_MAP_FRAGMENT = "ARGS_MAP_FRAGMENT";

        private Bundle args;
        private CoordinatorLayout rootView;
        private ConstraintLayout bottomSheet;
        private LinearLayout mainScreenHelpSnippet;
        private BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior;
        private RecyclerView recyclerView;
        private PlaceCategoryAdapter placeCategoryAdapter;
        private FrameLayout searchPlaceHolder;
        private EditText searchPlace;
        private InputMethodManager imm;
        private RecyclerView placeSearchRecycler;
        private DisplayPlaceAdapter displayPlaceAdapter;
        private TextView topHint1;
        private ImageButton fab_filter;
        private ConstraintLayout ConstraintLayoutFloor;
        private RecyclerView recyclerViewFloor;
        private TextView topHint2;
        private ImageView dummyMap;
        private ImageView backButton;
        private ArrayList<String> states;
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
            return dpToPixels(dp, requireContext());
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                                 @Nullable Bundle savedInstanceState) {
            rootView = (CoordinatorLayout) inflater.inflate(R.layout.map_screen_layout, container, true);

            rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                    dummyMap = new ImageView(getContext());
                    dummyMap.setId(View.generateViewId());
                    //dummyMap.setScaleType(ImageView.ScaleType.FIT_XY);
                    dummyMap.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_dummy_map, null));
                    rootView.addView(dummyMap, rootView.getWidth(), rootView.getHeight());

                    mainScreenHelpSnippet = rootView.findViewById(R.id.main_screen_help_snippet);
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mainScreenHelpSnippet.getLayoutParams();
                    layoutParams.setAnchorId(dummyMap.getId());
                    layoutParams.width = rootView.getWidth();
                    mainScreenHelpSnippet.setLayoutParams(layoutParams);

                    rootView.bringChildToFront(mainScreenHelpSnippet);
                }
            });

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
            searchPlace = rootView.findViewById(R.id.main_screen_search_edit_text);

//            Drawable searchPlaceHolderBackground = ResourcesCompat.getDrawable(getResources(), R.drawable.bottom_round_corner_search, null);
//            searchPlaceHolderBackground.setTint(ResourcesCompat.getColor(getResources(), R.color.searchPlaceHolder, null));
//            searchPlaceHolderBackground.setAlpha((int) (0.12f * 255));
//            searchPlaceHolder.setBackground(searchPlaceHolderBackground);


///TODO надо чекнуть почему срабатывает только со второго раза:
//            searchPlace.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                        if (placeSearchRecycler == null) {
//                            initPlaceSearchRecycler();
//                        }
//                        placeSearchRecycler.setVisibility(View.VISIBLE);
//                        BottomSheetAnimationsKt.hideTopText(getContext(), topHint1, topHint2, recyclerView, searchPlaceHolder, 500);
//                    }
//                }
//            });

            searchPlace.setOnTouchListener(new View.OnTouchListener() {
                @SuppressLint("ClickableViewAccessibility")
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        if (placeSearchRecycler == null) {
                            initPlaceSearchRecycler();
                        }
                        placeSearchRecycler.setVisibility(View.VISIBLE);
//                        BottomSheetAnimationsKt.hideTopText(getContext(), topHint1, topHint2, recyclerView, searchPlaceHolder, 500);
                    }
                    return false;
                }
            });

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            bottomSheetBehavior.setDraggable(true);
            bottomSheetBehavior.setPeekHeight((int) dpToPixels(245, requireActivity())); //высота видимой части нижнего бара

            // настройка колбэков при изменениях
            bottomSheetBehavior.addBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                    // этот код скрывает {@click fab_filter} сразу же
                    // и отображает после того как нижний экран полностью свернется
                    if (BottomSheetBehavior.STATE_DRAGGING == newState) {
                        fab_filter.animate().scaleX(0).scaleY(0).setDuration(300).start();
                        hideKeyboard(requireActivity());
                    } else if (BottomSheetBehavior.STATE_COLLAPSED == newState) {
                        fab_filter.animate().scaleX(1).scaleY(1).setDuration(300).start();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            });


            initRecyclerFloor();
            ConstraintLayoutFloor = rootView.findViewById(R.id.ConstraintLayoutFloor);
            ConstraintLayoutFloor.setTranslationY(dpToPixels(-90));

            fab_filter = rootView.findViewById(R.id.fab_filter);
            fab_filter.setTranslationY(dpToPixels(-60));

            topHint1 = rootView.findViewById(R.id.top_text_hint1);
            topHint2 = rootView.findViewById(R.id.top_text_hint2);

            initRecycler();
        }

        private void initRecyclerFloor() {
            recyclerView = rootView.findViewById(R.id.recyclerViewFloor);
//            recyclerView.setHasFixedSize(true);
            SnapHelper snapHelper = new PagerSnapHelper();
            snapHelper.attachToRecyclerView(recyclerView);
            states = new ArrayList<>();
            states.add("");
            for (int i = 1; i < 15; i++) {
                states.add(String.valueOf(i));
            }
            states.add("");

            FloorAdapter floorAdapter = new FloorAdapter(getContext(), states);
            floorAdapter.setOnItemClickListener(new FloorAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int pos) {
                    Toast.makeText(v.getContext(), "clicked:" + pos, Toast.LENGTH_SHORT).show();
                }
            });
            recyclerView.setAdapter(floorAdapter);
        }

        private void initRecycler() {
            recyclerView = rootView.findViewById(R.id.place_categories_recycler);
            recyclerView.setHasFixedSize(true);

            RecyclerView.ItemDecoration itemDecoration = new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                    int parentWidth = parent.getWidth();
                    int spaceBetween = (parentWidth - ((int) dpToPixels(57)) * 4) / 4;
                    outRect.right = spaceBetween / 2;
                    outRect.left = spaceBetween / 2;
                }
            };

            PlaceCategoryAdapter.Callback callback = new PlaceCategoryAdapter.Callback() {
                @Override
                public void extFn(@NotNull View view) {
                    Log.i(PlaceCategoryAdapter.VIEW_HOLDER_CLICKED, "viewholder clicked");
                    if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    }
                }
            };

            placeCategoryAdapter = new PlaceCategoryAdapter(callback);

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
