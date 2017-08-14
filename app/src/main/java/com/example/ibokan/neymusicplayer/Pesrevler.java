package com.example.ibokan.neymusicplayer;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;




/**
 * Created by ibokan on 01.08.2016.
 */
public class Pesrevler extends ActionBarActivity implements AudioManager.OnAudioFocusChangeListener {
    private static final String[] items={"Aðýt",
            "Asude",
            "Bahar",
            "Beyhude",
            "Bir Eski Ýstanbul",
            "Çeçen Kýzý",
            "Dostluk",
            "Gurbet",
            "Hasbihal",
            "Kar Çiçekleri",
            "Kervan",
            "Mektup",
            "Pervane",
            "Rüzgarda Baþaklar",
            "Senden Kalan",
            "Sonbahar",
            "Son Kuþlar",
            "Uzakta",
            "Yaban Gülü",
            "Yakamoz",
    };
    private final int[] resID = { R.raw.agit,
            R.raw.asude,
            R.raw.bahar,
            R.raw.beyhude,
            R.raw.bireskiistanbul,
            R.raw.cecenkizi,
            R.raw.dostluk,
            R.raw.gurbet,
            R.raw.hasbihal,
            R.raw.karcicekleri,
            R.raw.kervan,
            R.raw.mektup,
            R.raw.pervane,
            R.raw.ruzgardabasaklar,
            R.raw.sendenkalan,
            R.raw.sonbahar,
            R.raw.sonkuslar,
            R.raw.uzakta,
            R.raw.yabangul,
            R.raw.yakamoz,
    };

    private MediaPlayer mediaPlayer;
    public TextView duration,title,tv;
    private int timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();
    private SeekBar seekbar;
    private ListView musics;
    private ImageButton btnnext,btnprev;
    private int currentSongIndex ;
    private InterstitialAd gecisReklam;
    private AdRequest adRequest;
    private AudioManager mAudioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.musics);
        title= (TextView) findViewById(R.id.textTitle);
        Typeface face= Typeface.createFromAsset(getAssets(), "fonts/com.ttf");
        title.setTypeface(face);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest addRequest = new AdRequest.Builder().build();
        mAdView.loadAd(addRequest);

        gecisReklam = new InterstitialAd(this);
        gecisReklam.setAdUnitId("ca-app-pub-9661018467406372/7620638846");
        adRequest = new AdRequest.Builder().build();
        loadGecisReklam();
        gecisReklam.setAdListener(new AdListener() {

            @Override
            public void onAdLoaded() {
                showGecisreklam();
            }
        });

        mediaPlayer = new MediaPlayer();
        btnnext = (ImageButton) findViewById(R.id.media_ff);
        btnprev = (ImageButton) findViewById(R.id.media_rew);
        musics = (ListView) findViewById(R.id.song_list);
        tv= (TextView) findViewById(R.id.gettext);

        ArrayAdapter<String> veriAdaptoru = new ArrayAdapter<String>(this,
                R.layout.list_image, R.id.list_content, items);
        musics.setAdapter(veriAdaptoru);


        musics.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentSongIndex=position;
                playSong(position);
            }
        });

        mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

       btnnext.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               currentSongIndex = currentSongIndex + 1;
               if (currentSongIndex == 20) {
                   Toast.makeText(getApplicationContext(), "Son Þarký.", Toast.LENGTH_SHORT).show();
                   currentSongIndex = -1;
               } else
                   playSong(currentSongIndex);
           }
       });

        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentSongIndex--;
                if (currentSongIndex == -1) {
                    Toast.makeText(getApplicationContext(), "Daha Fazla Gidilemez.", Toast.LENGTH_SHORT).show();
                    currentSongIndex = 0;
                } else
                    playSong(currentSongIndex);
            }
        });


    }

    private void showGecisreklam() {
        if (gecisReklam.isLoaded()) {
            gecisReklam.show();
        } else {
        }
    }
    private void loadGecisReklam() {
        gecisReklam.loadAd(adRequest);
    }

    public void play(View view) {
        mediaPlayer.start();

    }

    public void pause(View view) {
        mediaPlayer.pause();
    }

    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {
            //get current position
            timeElapsed = mediaPlayer.getCurrentPosition();
            //set seekbar progress
            seekbar.setProgress((int) timeElapsed);
            //set time remaing
            double timeRemaining = finalTime - timeElapsed;
            duration.setText(String.format("%d:%d", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            //repeat yourself that again in 100 miliseconds
            durationHandler.postDelayed(this, 100);
        }
    };

    private void playSong(final int position) {

        mediaPlayer.reset();
        mediaPlayer=MediaPlayer.create(getApplicationContext(), resID[position]);
        finalTime = mediaPlayer.getDuration();
        duration = (TextView) findViewById(R.id.songDuration);
        seekbar = (SeekBar) findViewById(R.id.seekBar);
        seekbar.setMax((int) finalTime);
        seekbar.setClickable(false);
        timeElapsed = mediaPlayer.getCurrentPosition();
        seekbar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateSeekBarTime, 100);

        String text = (String) musics.getItemAtPosition(position);
        tv.setText("Çalan Eser : "+text);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                nextSong();

            }
        });
    }

    private void nextSong() {
        currentSongIndex = currentSongIndex + 1;
        if (currentSongIndex == 20) {
            Toast.makeText(getApplicationContext(), "Son Þarký.", Toast.LENGTH_SHORT).show();
            currentSongIndex = -1;
        } else
            playSong(currentSongIndex);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAudioManager.abandonAudioFocus(this);
        if(mediaPlayer!=null)
            mediaPlayer.stop();
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        if(focusChange<=0) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

}