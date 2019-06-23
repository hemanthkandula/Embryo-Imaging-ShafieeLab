package edu.harvard.bwh.shafieelab.embryoimaging.samples.activities;

import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.contentful.vault.Vault;

import org.parceler.Parcels;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import edu.harvard.bwh.shafieelab.embryoimaging.App;
import edu.harvard.bwh.shafieelab.embryoimaging.R;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.gallery.GalleryAdapter;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.Const;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.Holder;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.Intents;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.LoaderId;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.lib.Utils;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.loaders.GalleryListLoader;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.ui.AnimativeToolBar;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.vault.Gallery;
import edu.harvard.bwh.shafieelab.embryoimaging.samples.vault.Image;

import static edu.harvard.bwh.shafieelab.embryoimaging.samples.gallery.GalleryAdapter.ItemViewHolder;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Gallery>> {

    private static final int GRID_SPAN_COUNT = App.get().getResources().getInteger(R.integer.gallery_grid_span_count);

    private static final int LOADER_ID = LoaderId.forClass(MainActivity.class);
    @BindView(R.id.toolbar)
    AnimativeToolBar toolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    private GalleryAdapter adapter;
    private GridLayoutManager layoutManager;
    private BroadcastReceiver reloadReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_gal);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        calculateImageHeight();
        createAdapter();
        initSwipeRefresh();
        initRecycler();
        setupReloadReceiver(true);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Holder.set(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        setupReloadReceiver(false);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Gallery>> onCreateLoader(int id, Bundle args) {
        return new GalleryListLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Gallery>> loader, final List<Gallery> data) {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }

        if (data != null) {
            adapter.setData(data);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Gallery>> loader) {

    }

    private void initSwipeRefresh() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                App.requestSync();
            }
        });
    }

    private void setupReloadReceiver(boolean register) {
        if (register) {
            if (reloadReceiver == null) {
                reloadReceiver = new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        getSupportLoaderManager().restartLoader(LOADER_ID, null, MainActivity.this);
                    }
                };
            }

            registerReceiver(reloadReceiver, new IntentFilter(Vault.ACTION_SYNC_COMPLETE));
        } else {
            unregisterReceiver(reloadReceiver);
        }
    }

    private void initRecycler() {
        createLayoutManager();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnScrollListener(createRecyclerScrollListener());
    }

    private void createLayoutManager() {
        layoutManager = new GridLayoutManager(this, GRID_SPAN_COUNT);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return adapter.isSection(position) ? GRID_SPAN_COUNT : 1;
            }
        });
    }

    private void createAdapter() {
        int imageSize = Utils.getDisplayDimensions(this).x / GRID_SPAN_COUNT;
        adapter = new GalleryAdapter(createAdapterClickListener(), imageSize);
    }

    private void onClickImage(ItemViewHolder holder, Gallery gallery, Image image) {
        Intent intent = new Intent(this, GalleryActivity.class)
                .putExtra(Intents.EXTRA_GALLERY, Parcels.wrap(gallery))
                .putExtra(Intents.EXTRA_IMAGE, Parcels.wrap(image));

        Bundle options = null;
        if (Const.HAS_L) {
            Drawable drawable = holder.photo.getDrawable();
            if (drawable != null) {
                Holder.set(drawable);

                options = ActivityOptions.makeSceneTransitionAnimation(this, holder.photo,
                        getString(R.string.gallery_photo_hero)).toBundle();
            }
        }

        ActivityCompat.startActivity(this, intent, options);
    }

    private void calculateImageHeight() {
        if (Const.IMAGE_WIDTH == null || Const.IMAGE_HEIGHT == null) {
            WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
            Display defaultDisplay = windowManager.getDefaultDisplay();
            Point p = new Point();
            defaultDisplay.getSize(p);
            Const.IMAGE_WIDTH = p.x;
            Const.IMAGE_HEIGHT = (int) (p.y * 2 / 3.0f);
        }
    }

    private View.OnClickListener createAdapterClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = recyclerView.getChildAdapterPosition(v);
                int viewType = adapter.getItemViewType(position);
                RecyclerView.ViewHolder viewHolder = recyclerView.getChildViewHolder(v);
                Object item = adapter.getItem(position);

                if (GalleryAdapter.VIEW_TYPE_ITEM == viewType) {
                    onClickImage((ItemViewHolder) viewHolder, adapter.sectionFor(position), (Image) item);
                }
            }
        };
    }

    private RecyclerView.OnScrollListener createRecyclerScrollListener() {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (RecyclerView.SCROLL_STATE_DRAGGING != recyclerView.getScrollState()) {
                    if (dy > 0 && toolbar.visible()) {
                        toolbar.hide();
                    } else if (dy < 0 && !toolbar.visible()) {
                        toolbar.show();
                    }
                }
            }
        };
    }
}
