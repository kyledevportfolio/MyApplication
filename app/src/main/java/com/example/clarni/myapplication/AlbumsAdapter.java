package com.example.clarni.myapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;





public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<AlbumsModel> albums;

    public AlbumsAdapter(Context mContext, ArrayList<AlbumsModel> albums) {
        this.mContext = mContext;
        this.albums = albums;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.albums_item_list, parent, false);
        ViewHolder view = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(!albums.get(position).getImage().isEmpty()){
            Glide.with(mContext).load(albums.get(position).getImage()).into(holder.albumImage);
        }
        holder.albumTitle.setText(albums.get(position).getName());
        holder.albumArtist.setText(albums.get(position).getArtist());
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView albumImage;
        private TextView albumTitle, albumArtist;

        public ViewHolder(View itemView) {
            super(itemView);

            albumImage = (ImageView)itemView.findViewById(R.id.albumImage);
            albumTitle = (TextView)itemView.findViewById(R.id.albumTitle);
            albumArtist = (TextView)itemView.findViewById(R.id.albumArtist);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri albumUri = Uri.parse(albums.get(getAdapterPosition()).getUrl());
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, albumUri);
                    mContext.startActivity(webIntent);
                }
            });
        }
    }
}
