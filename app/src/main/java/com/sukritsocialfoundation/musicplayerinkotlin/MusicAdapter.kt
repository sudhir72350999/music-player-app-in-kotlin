package com.sukritsocialfoundation.musicplayerinkotlin

import android.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class MusicAdapter(
    private val songPaths: List<String>,
    private val context: Context,
    songClickListener: (String?) -> Unit
) :
    RecyclerView.Adapter<com.sukritsocialfoundation.musicplayerinkotlin.MusicAdapter.MusicViewHolder>() {
    private val songClickListener: (String?) -> Unit =
        songClickListener

    interface OnSongClickListener {
        fun onSongClick(path: String?)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): com.sukritsocialfoundation.musicplayerinkotlin.MusicAdapter.MusicViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.simple_list_item_1, parent, false)
        return com.sukritsocialfoundation.musicplayerinkotlin.MusicAdapter.MusicViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: com.sukritsocialfoundation.musicplayerinkotlin.MusicAdapter.MusicViewHolder,
        position: Int
    ) {
        val path = songPaths[position]
        val title =
            path.substring(path.lastIndexOf("/") + 1) // Extract the song title from the file path
        holder.songTitle.setText(title)
        holder.itemView.setOnClickListener(View.OnClickListener { v: View? ->
            songClickListener.onSongClick(
                path
            )
        }) // Set click listener
    }

    override fun getItemCount(): Int {
        return songPaths.size
    }

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var songTitle: TextView = itemView.findViewById(R.id.text1)
    }
} //import android.content.Context;

private fun Any.onSongClick(path: String) {

}
//import android.media.MediaPlayer;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.io.File;
//import java.util.List;
//
//public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MusicViewHolder> {
//
//    private List<String> songPaths;
//    private Context context;
//    private MediaPlayer mediaPlayer;
//
//    public MusicAdapter(List<String> songPaths, Context context) {
//        this.songPaths = songPaths;
//        this.context = context;
//    }
//
//    @NonNull
//    @Override
//    public MusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
//        return new MusicViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull MusicViewHolder holder, int position) {
//        holder.bind(songPaths.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return songPaths.size();
//    }
//
//    class MusicViewHolder extends RecyclerView.ViewHolder {
//        TextView songTitle;
//
//        MusicViewHolder(View itemView) {
//            super(itemView);
//            songTitle = itemView.findViewById(android.R.id.text1);
//
//            itemView.setOnClickListener(v -> {
//                if (mediaPlayer != null) {
//                    mediaPlayer.stop();
//                    mediaPlayer.release();
//                }
//                mediaPlayer = MediaPlayer.create(context, Uri.parse(songPaths.get(getAdapterPosition())));
//                mediaPlayer.start();
//            });
//        }
//
//        void bind(String path) {
//            songTitle.setText(new File(path).getName());
//        }
//    }
//}