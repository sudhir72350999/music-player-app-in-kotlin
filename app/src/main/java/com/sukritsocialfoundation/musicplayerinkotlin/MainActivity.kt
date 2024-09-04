package com.sukritsocialfoundation.musicplayerinkotlin

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var playPauseButton: ImageButton
    private lateinit var songTitle: TextView
    private lateinit var recyclerView: RecyclerView
    private val songPaths: MutableList<String> = ArrayList()
    private var currentSongIndex = 0
    private var isPlaying = false
    private var currentPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        playPauseButton = findViewById(R.id.playPauseButton)
        songTitle = findViewById(R.id.songTitle)
        recyclerView = findViewById(R.id.recyclerView)

        playPauseButton.setOnClickListener {
            if (isPlaying) {
                pauseSong()
            } else {
                playSong()
            }
        }

        val prevButton: ImageButton = findViewById(R.id.prevButton)
        val nextButton: ImageButton = findViewById(R.id.nextButton)
        val selectDirectoryButton: Button = findViewById(R.id.selectDirectoryButton)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), STORAGE_PERMISSION_CODE
                )
            }
        }

        selectDirectoryButton.setOnClickListener {
            openDirectoryChooser()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        prevButton.setOnClickListener { playPreviousSong() }
        nextButton.setOnClickListener { playNextSong() }
    }

    private fun playPreviousSong() {
        if (currentSongIndex > 0) {
            currentSongIndex--
            playSongFromList(songPaths[currentSongIndex])
        }
    }

    private fun playNextSong() {
        if (currentSongIndex < songPaths.size - 1) {
            currentSongIndex++
            playSongFromList(songPaths[currentSongIndex])
        }
    }

    private fun openDirectoryChooser() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT_TREE)
        startActivityForResult(intent, DIRECTORY_SELECT_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == DIRECTORY_SELECT_CODE && resultCode == RESULT_OK) {
            data?.data?.let { uri ->
                loadMusicFiles(uri)
            } ?: run {
                Toast.makeText(this, "Failed to select directory", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadMusicFiles(uri: Uri) {
        songPaths.clear()
        val directory = DocumentFile.fromTreeUri(this, uri)
        if (directory != null && directory.exists() && directory.isDirectory) {
            for (file in directory.listFiles()) {
                if (file.isFile && file.name?.endsWith(".mp3") == true) {
                    songPaths.add(file.uri.toString())
                }
            }
        } else {
            Toast.makeText(this, "Invalid directory", Toast.LENGTH_SHORT).show()
        }

        val adapter = MusicAdapter(songPaths, this) { path ->
            playSongFromList(path)
        }
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun playSongFromList(path: String?) {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer.create(this, Uri.parse(path))
            currentPath = path

            val fileName = File(path ?: "").name
            val songName = fileName.substringBeforeLast(".")
            songTitle.text = songName

            playSong()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            Log.e("PlaySongError", "Exception occurred: ${e.message}", e)
        }
    }

    private fun playSong() {
        mediaPlayer?.let {
            if (!it.isPlaying) {
                it.start()
                playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
                isPlaying = true
            }
        }
    }

    private fun pauseSong() {
        mediaPlayer?.let {
            if (it.isPlaying) {
                it.pause()
                playPauseButton.setImageResource(android.R.drawable.ic_media_play)
                isPlaying = false
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            playSongFromList(currentPath)
        } else {
            Toast.makeText(this, "Please grant permission to access storage", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        mediaPlayer?.release()
        mediaPlayer = null
        super.onDestroy()
    }

    companion object {
        private const val STORAGE_PERMISSION_CODE = 101
        private const val DIRECTORY_SELECT_CODE = 102
        private const val REQUEST_PERMISSION_CODE = 101
    }
}


//package com.sukritsocialfoundation.musicplayerappfortest;
//import android.media.MediaPlayer;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import java.util.ArrayList;
//
//public class MainActivity extends AppCompatActivity {
//
//    private MediaPlayer mediaPlayer;
//    private ImageButton playPauseButton;
//    private TextView songTitle;
//    private int currentSongIndex = 0;
//    private ArrayList<Integer> songList = new ArrayList<>();
//    private boolean isPlaying = false;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        playPauseButton = findViewById(R.id.playPauseButton);
//        songTitle = findViewById(R.id.songTitle);
//        ImageButton nextButton = findViewById(R.id.nextButton);
//        ImageButton prevButton = findViewById(R.id.prevButton);
//
//        // Add your song resources to the songList
//        songList.add(R.raw.a);
//        songList.add(R.raw.abb);
////        songList.add(R.raw.song3);
//
//        // Set up the MediaPlayer with the first song
//        mediaPlayer = MediaPlayer.create(this, songList.get(currentSongIndex));
//        songTitle.setText("Song " + (currentSongIndex + 1));
//
//        // Play/Pause button functionality
//        playPauseButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isPlaying) {
//                    pauseSong();
//                } else {
//                    playSong();
//                }
//            }
//        });
//
//        // Next button functionality
//        nextButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nextSong();
//            }
//        });
//
//        // Previous button functionality
//        prevButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                previousSong();
//            }
//        });
//    }
//
//    private void playSong() {
//        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
//            mediaPlayer.start();
//            isPlaying = true;
//            playPauseButton.setImageResource(android.R.drawable.ic_media_pause);
//        }
//    }
//
//    private void pauseSong() {
//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//            isPlaying = false;
//            playPauseButton.setImageResource(android.R.drawable.ic_media_play);
//        }
//    }
//
//    private void nextSong() {
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            currentSongIndex = (currentSongIndex + 1) % songList.size();
//            mediaPlayer = MediaPlayer.create(this, songList.get(currentSongIndex));
//            songTitle.setText("Song " + (currentSongIndex + 1));
//            playSong();
//        }
//    }
//
//    private void previousSong() {
//        if (mediaPlayer != null) {
//            mediaPlayer.stop();
//            mediaPlayer.release();
//            currentSongIndex = (currentSongIndex - 1 + songList.size()) % songList.size();
//            mediaPlayer = MediaPlayer.create(this, songList.get(currentSongIndex));
//            songTitle.setText("Song " + (currentSongIndex + 1));
//            playSong();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//        super.onDestroy();
//    }
//}