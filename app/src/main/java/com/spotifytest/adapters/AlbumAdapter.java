package com.spotifytest.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.spotifytest.models.Album;
import com.spotifytest.models.Artist;
import com.spotifytest.spotifytest.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Camilo on 16/10/2015.
 */
public class AlbumAdapter extends ArrayAdapter {

    private static final String TAG = AlbumAdapter.class.getSimpleName();

    private RequestQueue queue;
    List<Album> albumsList;

    public AlbumAdapter(Context context, String artistId) {
        super(context, 0);
        queue = Volley.newRequestQueue(context);
        String url = "https://api.spotify.com/v1/artists/" + artistId + "/albums?offset=0&limit=50&album_type=album";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                albumsList = parseJsonToAlbumsList(jsonObject);
                notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e(TAG, volleyError.getMessage());
            }
        });
        queue.add(jsonObjectRequest);
    }

    @Override
    public int getCount() {
        return albumsList != null ? albumsList.size() : 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View listAlbumsView;

        listAlbumsView = null == convertView ? layoutInflater.inflate(R.layout.listitem_album, parent, false) : convertView;

        Album album = albumsList.get(position);

        TextView tvAlbumName = (TextView)listAlbumsView.findViewById(R.id.tvAlbumName);
        TextView tvCountries = (TextView)listAlbumsView.findViewById(R.id.tvCountries);
        final ImageView ivAlbumImage = (ImageView)listAlbumsView.findViewById(R.id.ivAlbumImage);

        tvAlbumName.setText(album.getName());
        tvCountries.setText(album.getCountries());

        ImageRequest imageRequest = new ImageRequest(album.getImage(), new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap bitmap) {
                ivAlbumImage.setImageBitmap(bitmap);
            }
        }, 0, 0, null, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                ivAlbumImage.setImageResource(R.drawable.standar_album_img_small);
                Log.e(TAG, volleyError.getMessage());
            }
        });
        queue.add(imageRequest);
        return listAlbumsView;
    }

    public List<Album> parseJsonToAlbumsList(JSONObject jsonObject){
        List<Album> result = new ArrayList<Album>();
        try {
            JSONArray jsonArrayAlbums = jsonObject.getJSONArray("items");
            for(int i=0; i < jsonArrayAlbums.length(); i++){
                JSONObject jsonObjectAlbum = jsonArrayAlbums.getJSONObject(i);
                Album album = new Album();
                album.setName(jsonObjectAlbum.getString("name"));
                album.setCountries("");
                album.setImage("");
                JSONArray jsonArrayCountries = jsonObjectAlbum.getJSONArray("available_markets");
                if(jsonArrayCountries.length() <= 5 && jsonArrayCountries.length() != 0){
                    album.setCountries(jsonArrayCountries.toString());
                }
                JSONArray jsonArrayImages = jsonObjectAlbum.getJSONArray("images");
                JSONObject jsonObjectImage = null;
                if(jsonArrayImages.length() == 3){
                    jsonObjectImage = jsonArrayImages.getJSONObject(2);
                } else if(jsonArrayImages.length() > 0){
                    jsonObjectImage = jsonArrayImages.getJSONObject(0);
                }
                if(jsonObjectImage != null){
                    album.setImage(jsonObjectImage.getString("url"));
                }
                result.add(album);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return result;
    }

}
