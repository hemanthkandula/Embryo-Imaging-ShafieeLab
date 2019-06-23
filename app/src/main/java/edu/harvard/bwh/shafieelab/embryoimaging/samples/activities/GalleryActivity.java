package edu.harvard.bwh.shafieelab.embryoimaging.samples.activities;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnPageChange;
import edu.harvard.bwh.shafieelab.embryoimaging.R;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.fragments.GalleryInfoFragment;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.fragments.SlideFragment;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.gallery.SlideFragmentAdapter;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.gallery.SlideImageAdapter;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.Const;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.Holder;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.Intents;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.TransitionListenerAdapter;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.Utils;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.ui.FloatingActionButton;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.ui.LockableViewPager;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.ui.ViewUtils;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.vault.Gallery;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.vault.Image;

public class GalleryActivity extends AppCompatActivity {
    private static final ArgbEvaluator EVALUATOR = new ArgbEvaluator();
    @BindView(R.id.photo)
    ImageView photo;
    @BindView(R.id.star)
    FloatingActionButton star;
    @BindView(R.id.pager)
    LockableViewPager viewPager;
    @BindView(R.id.info_container)
    ViewGroup infoContainer;
    private SlideImageAdapter imageAdapter;
    private SlideFragmentAdapter fragmentAdapter;
    private GalleryInfoFragment galleryInfoFragment;
    private BroadcastReceiver colorizeReceiver;
    private Gallery gallery;
    private Image image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);

        extractIntentArguments();
        attachGalleryInfoFragment();
        createAdapterInstances();
        setViewPagerAdapter();
        initializeViews();
        setupColorizeReceiver(true);
    }

    @Override
    protected void onDestroy() {
        setupColorizeReceiver(false);
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        int index = viewPager.getCurrentItem();
        setViewPagerAdapter();
        viewPager.setCurrentItem(index);
    }

    @Override
    public void onBackPressed() {
        if (Const.HAS_L) {
            if (isShowingInitialImage()) {
                createAlphaAnimator(star, false).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        GalleryActivity.super.onBackPressed();
                    }
                }).start();
            } else {
                getWindow().setSharedElementReturnTransition(null);
                finish();
            }

            return;
        }

        super.onBackPressed();
    }

    @OnPageChange(value = R.id.pager, callback = OnPageChange.Callback.PAGE_SCROLLED)
    void onPageSelected(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffsetPixels == 0) {
            return;
        }

        SlideFragment leftFragment = fragmentAdapter.fragmentForPosition(position);
        SlideFragment rightFragment = fragmentAdapter.fragmentForPosition(position + 1);

        if (leftFragment != null && rightFragment != null) {
            int leftVibrant = leftFragment.getColorVibrant();
            int rightVibrant = rightFragment.getColorVibrant();

            int leftDarkMuted = leftFragment.getColorDarkMuted();
            int rightDarkMuted = rightFragment.getColorDarkMuted();

            int vibrant = (Integer) EVALUATOR.evaluate(
                    positionOffset, leftVibrant, rightVibrant);

            int darkMuted = (Integer) EVALUATOR.evaluate(
                    positionOffset, leftDarkMuted, rightDarkMuted);

            colorizeButton(vibrant, darkMuted);
        }
    }

    @OnClick(R.id.star)
    void onClickStar(View v) {
        toggleInfoContainer(v);
    }

    private void initializeViews() {
        viewPager.setCurrentItem(gallery.images().indexOf(image));
        viewPager.setOffscreenPageLimit(getResources().getInteger(
                R.integer.gallery_pager_offscreen_limit));

        repositionStar();
        ViewUtils.setViewHeight(infoContainer, Const.IMAGE_HEIGHT, true);

        if (Const.HAS_L && Utils.isPortrait(this)) {
            ViewUtils.setViewHeight(photo, Const.IMAGE_HEIGHT, false);
            photo.setImageDrawable(Holder.get());

            getWindow().getSharedElementEnterTransition().addListener(new TransitionListenerAdapter() {
                @Override
                public void onTransitionEnd(Transition transition) {
                    getWindow().getSharedElementEnterTransition().removeListener(this);
                    photo.setVisibility(View.GONE);
                }
            });
        }
    }

    private void attachGalleryInfoFragment() {
        galleryInfoFragment = (GalleryInfoFragment) GalleryInfoFragment
                .instantiate(this, GalleryInfoFragment.class.getName(), getIntent().getExtras());

        getSupportFragmentManager().beginTransaction()
                .add(R.id.info_container, galleryInfoFragment)
                .commit();
    }

    private void extractIntentArguments() {
        Intent intent = getIntent();
        gallery = Parcels.unwrap(intent.getParcelableExtra(Intents.EXTRA_GALLERY));
        image = Parcels.unwrap(intent.getParcelableExtra(Intents.EXTRA_IMAGE));
    }

    private void createAdapterInstances() {
        fragmentAdapter = new SlideFragmentAdapter(this, getSupportFragmentManager(), gallery);
        imageAdapter = new SlideImageAdapter(gallery.images());
    }

    private void setViewPagerAdapter() {
        if (Utils.isPortrait(this)) {
            viewPager.setAdapter(fragmentAdapter);
        } else {
            viewPager.setAdapter(imageAdapter);
        }
    }

    private void toggleInfoContainer(final View view) {
        final boolean show = ViewUtils.isGone(infoContainer);

        if (show) {
            SlideFragment slideFragment = fragmentAdapter.fragmentForPosition(viewPager.getCurrentItem());

            galleryInfoFragment.colorize(slideFragment.getColorDarkMuted(),
                    slideFragment.getColorVibrant(), slideFragment.getColorLightMuted());
        }

        Animator animator;
        Runnable startRunnable = new Runnable() {
            @Override
            public void run() {
                star.setEnabled(false);
                viewPager.setLocked(show);
            }
        };

        Runnable endRunnable = new Runnable() {
            @Override
            public void run() {
                star.setEnabled(true);
                viewPager.setLocked(show);
            }
        };

        int duration = getResources().getInteger(R.integer.toggle_animation_duration);
        if (Const.HAS_L) {
            float radius = Math.max(photo.getWidth(), photo.getHeight()) * 2.0f;
            animator = ViewUtils.toggleViewCircular(view, infoContainer, show, radius, duration,
                    startRunnable, endRunnable);
        } else {
            animator = ViewUtils.toggleViewFade(infoContainer, show, duration,
                    startRunnable, endRunnable);
        }

        animator.start();
    }

    private void repositionStar() {
        int fabSize = getResources().getDimensionPixelSize(R.dimen.fab_size);
        star.setTranslationY(Const.IMAGE_HEIGHT - (fabSize / 2.0f));
    }

    private boolean isShowingInitialImage() {
        int originalIndex = gallery.images().indexOf(image);
        return originalIndex == viewPager.getCurrentItem();
    }

    private void colorizeButton(int colorNormal, int colorPressed) {
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.info_background);
        drawable.setColorFilter(colorNormal, PorterDuff.Mode.MULTIPLY);

        if (Const.HAS_L) {
            ColorStateList stateList = new ColorStateList(
                    new int[][]{
                            new int[]{}
                    },
                    new int[]{
                            colorPressed
                    });

            RippleDrawable rippleDrawable = new RippleDrawable(stateList, drawable, null);
            ViewUtils.setBackground(star, rippleDrawable);
        } else {
            ViewUtils.setBackground(star, drawable);
        }
    }

    private void setupColorizeReceiver(boolean register) {
        if (register) {
            if (colorizeReceiver == null) {
                colorizeReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        Image image = Parcels.unwrap(intent.getParcelableExtra(Intents.EXTRA_IMAGE));
                        int vibrant = intent.getIntExtra(Intents.EXTRA_CLR_VIBRANT, 0);
                        int darkMuted = intent.getIntExtra(Intents.EXTRA_CLR_DARK_MUTED, 0);
                        colorize(image, vibrant, darkMuted);
                    }
                };
            }

            registerReceiver(colorizeReceiver, new IntentFilter(Intents.ACTION_COLORIZE));
        } else {
            unregisterReceiver(colorizeReceiver);
        }
    }

    private void colorize(Image image, int colorVibrant, int colorDarkMuted) {
        if (image.equals(fragmentAdapter.getImage(viewPager.getCurrentItem()))) {
            colorizeButton(colorVibrant, colorDarkMuted);

            if (star.getAlpha() == 0) {
                long startDelay = getResources().getInteger(R.integer.toggle_animation_duration);
                createAlphaAnimator(star, true).setStartDelay(startDelay).start();
            }
        }
    }

    private ViewPropertyAnimator createAlphaAnimator(View v, boolean show) {
        int duration = getResources().getInteger(R.integer.gallery_alpha_duration);
        float alpha = show ? 1.0f : 0.0f;
        return v.animate().alpha(alpha).setDuration(duration);
    }
}
