package com.spotifytest.controllers;

import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.spotifytest.models.Artist;
import com.spotifytest.spotifytest.ArtistSearchActivity;
import com.spotifytest.spotifytest.R;
import com.spotifytest.util.ValidationUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistSearchActivityFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = ArtistSearchActivityFragment.class.getSimpleName();
    private static final String REGULAR_EXPRESSION_NAMES = "^[a-zA-Z0-9+#-.,_áéíóúÁÉÍÓÚñÑÜü\\s]{1,50}$";

    private ArtistSearchActivity activity;

    private EditText etArtistName;
    private Button btSearchArtist;

    public ArtistSearchActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_search, container, false);
        activity = (ArtistSearchActivity)getActivity();
        etArtistName = (EditText)view.findViewById(R.id.etArtistName);
        btSearchArtist = (Button)view.findViewById(R.id.btSearchArtist);
        btSearchArtist.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == btSearchArtist){
            String artistName = etArtistName.getText().toString().replaceAll("\\s", "+");
            if(ValidationUtil.isValid(REGULAR_EXPRESSION_NAMES, artistName)){
                this.searchArtist(artistName);
            } else {
                Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.invalid_artist_name), Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Metodo utilizado para buscar el artista utilizando el API
     * de Spotify
     * @param artistName
     */
    private void searchArtist(String artistName) {
        RequestQueue queue = Volley.newRequestQueue(activity.getApplicationContext());
        String url = "https://api.spotify.com/v1/search?query=" + artistName + "&offset=0&limit=1&type=artist&market=US";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Artist artist = parseJsonToArtist(jsonObject);
                if(artist != null){
                    activity.goToViewArtist(artist);
                } else {
                    Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.not_found_artist), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.error_searching_artist), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    /**
     * Metodo utilizado para convertir un objeto JSON obtenido
     * de la consulta de artistas por medio de la API de Spotify,
     * en un objeto Artist de java
     * @param jsonObject
     * @return
     */
    public Artist parseJsonToArtist(JSONObject jsonObject){
        try {
            JSONObject jsonObjectArtists = jsonObject.getJSONObject("artists");
            JSONArray jsonArrayItems = jsonObjectArtists.getJSONArray("items");
            if(jsonArrayItems.length() != 0){
                JSONObject jsonObjectArtist = jsonArrayItems.getJSONObject(0);
                Artist artist = new Artist();
                JSONObject jsonObjectFollowers = jsonObjectArtist.getJSONObject("followers");
                artist.setFollowers(jsonObjectFollowers.getInt("total"));
                artist.setPopularity(jsonObjectArtist.getInt("popularity"));
                artist.setName(jsonObjectArtist.getString("name"));
                artist.setId(jsonObjectArtist.getString("id"));
                JSONArray jsonArrayImages = jsonObjectArtist.getJSONArray("images");
                JSONObject jsonObjectImage = jsonArrayImages.getJSONObject(0);
                artist.setImage(jsonObjectImage.getString("url"));
                return artist;
            } else {
                return null;
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(activity.getApplicationContext(), getResources().getString(R.string.error_searching_artist), Toast.LENGTH_LONG).show();
        }
        return null;
    }


}
