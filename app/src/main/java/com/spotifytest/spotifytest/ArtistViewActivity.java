package com.spotifytest.spotifytest;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.spotifytest.adapters.AlbumAdapter;
import com.spotifytest.models.Artist;

public class ArtistViewActivity extends AppCompatActivity {

    private static final String TAG = ArtistViewActivity.class.getSimpleName();

    private Artist artistToShow;
    private ImageView ivArtist;
    private TextView tvArtistName;
    private TextView tvArtistFollowers;
    private TextView tvArtistPopularity;
    private ListView lvAlbumsList;
    private ArrayAdapter albumAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarArtistView);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        artistToShow = (Artist)getIntent().getSerializableExtra(ArtistSearchActivity.ARTIST_KEY);
        ivArtist = (ImageView)findViewById(R.id.imageArtist);
        tvArtistName = (TextView)findViewById(R.id.tvArtistName);
        tvArtistFollowers = (TextView)findViewById(R.id.tvArtistFollowers);
        tvArtistPopularity = (TextView)findViewById(R.id.tvArtistPopularity);
        tvArtistName.setText("Nombre: " + artistToShow.getName());
        tvArtistFollowers.setText("Seguidores: " + artistToShow.getFollowers());
        tvArtistPopularity.setText("Popularidad: " + artistToShow.getPopularity());
        this.getImageBitmap();

        lvAlbumsList = (ListView)findViewById(R.id.lvAlbumsList);
        albumAdapter = new AlbumAdapter(this, artistToShow.getId());
        lvAlbumsList.setAdapter(albumAdapter);
    }

    public void getImageBitmap(){
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        ImageRequest imageRequest = new ImageRequest(artistToShow.getImage(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                ivArtist.setImageBitmap(bitmap);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.image_artist_not_found), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(imageRequest);
    }

}
