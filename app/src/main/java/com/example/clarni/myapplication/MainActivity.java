package com.example.clarni.myapplication;


import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<AlbumsModel>>{

    private static final String REQUEST_URL = "http://ws.audioscrobbler.com/2.0/?";
    private static final int ALBUM_LOADER_ID1 = 1;
    private static final int ALBUM_LOADER_ID2 = 2;
    private int searchBy = 1;
    private TextView noAlbum, searchLabel;
    private EditText searchText;
    private RecyclerView recyclerView;
    private ProgressBar progBar;
    private String search = "";
    private AlbumsAdapter albumsAdapter;
    private LoaderManager loaderManager;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Search Music");

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("blue")));

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        noAlbum = (TextView)findViewById(R.id.noAlbumLabel);
        searchLabel = (TextView)findViewById(R.id.searchLabel);
        searchText = (EditText)findViewById(R.id.searchText);
        progBar = (ProgressBar)findViewById(R.id.progBar);

        noAlbum.setText("No album present at this time.");

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        if (networkInfo != null && networkInfo.isConnected()) {


        } else {
            progBar.setVisibility(View.GONE);
            noAlbum.setText("No internet connection.");
        }

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    setSearch(searchText.getText().toString());
                    performSearch();
                    return true;
                }
                return false;
            }
        });
    }
    @Override
    public Loader<ArrayList<AlbumsModel>> onCreateLoader(int id, Bundle args) {
        Uri baseUri = Uri.parse(REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        if(id == ALBUM_LOADER_ID1) {
            uriBuilder.appendQueryParameter("method", "artist.gettopalbums");
            uriBuilder.appendQueryParameter("artist", getSearch()); // edit later
            uriBuilder.appendQueryParameter("api_key", "ef3f4141081152266be948cc5c2af09f");
            uriBuilder.appendQueryParameter("limit", "50");
            uriBuilder.appendQueryParameter("format", "json");
        }
        else if (id == ALBUM_LOADER_ID2){
            uriBuilder.appendQueryParameter("method", "album.search");
            uriBuilder.appendQueryParameter("album", getSearch()); // edit later
            uriBuilder.appendQueryParameter("api_key", "ef3f4141081152266be948cc5c2af09f");
            uriBuilder.appendQueryParameter("limit", "50");
            uriBuilder.appendQueryParameter("format", "json");
        }
        Log.d("eee",uriBuilder.toString());

        return new AlbumsLoader(this,uriBuilder.toString(),id);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<AlbumsModel>> loader, ArrayList<AlbumsModel> data) {
        recyclerView.setVisibility(View.VISIBLE);
        albumsAdapter = new AlbumsAdapter(this,data);
        if(data != null && !data.isEmpty()){
            noAlbum.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(albumsAdapter);
        }
        else{
            recyclerView.setVisibility(View.GONE);
            progBar.setVisibility(View.GONE);
            noAlbum.setVisibility(View.VISIBLE);
        }
        getLoaderManager().destroyLoader(searchBy);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<AlbumsModel>> loader) {
    }

    public void performSearch(){
        noAlbum.setVisibility(View.GONE);
        progBar.setVisibility(View.VISIBLE);
        loaderManager = getLoaderManager();
        loaderManager.initLoader(searchBy, null, this);
        hideSoftKeyboard(MainActivity.this);

    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.clear){
            searchText.setText("");
        }
        if(id == R.id.artistName){
            searchLabel.setText("Search album by artist");
            searchBy = 1;
        }
        if(id == R.id.albumName){
            searchLabel.setText("Search album by name");
            searchBy = 2;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
