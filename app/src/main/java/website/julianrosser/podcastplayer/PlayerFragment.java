package website.julianrosser.podcastplayer;


import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;

import java.util.Random;


/**
 * A fragment containing a the playerview
 */
public class PlayerFragment extends android.support.v4.app.Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    public static AudioPlayer mPlayer;
    /**
     * Create AudioPlayer instance
     */

    static SeekBar seekBar;

    static Thread checkForPrep;

    static Uri[] tracks;

    /**
     * Required empty public constructor
     */
    public PlayerFragment() {    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlayerFragment newInstance(int sectionNumber) {
        PlayerFragment fragment = new PlayerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        tracks = new Uri[]{Uri.parse("android.resource://website.julianrosser.podcastplayer/" +
                R.raw.i), Uri.parse("android.resource://website.julianrosser.podcastplayer/" + R.raw.nero_satisfy),
                Uri.parse("android.resource://website.julianrosser.podcastplayer/" + R.raw.sam_smith_lay_me_down_flume),
                Uri.parse("android.resource://website.julianrosser.podcastplayer/" + R.raw.seven),
                Uri.parse("android.resource://website.julianrosser.podcastplayer/" + R.raw.leanon)};

        mPlayer = new AudioPlayer(getActivity(), tracks[new Random().nextInt(tracks.length)]);

        final ImageButton playPause = (ImageButton) view.findViewById(R.id.buttonPlay);
        // Set Image depending on play state
        if (mPlayer.getIsPlaying()) {
            //noinspection deprecation
            playPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
        } else {
            //noinspection deprecation
            playPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
        }

        // Create listener
        playPause.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View view) {
                if (mPlayer.getIsPlaying()) {
                    mPlayer.pause();
                    playPause.setImageDrawable(getResources().getDrawable(R.drawable.play));
                } else {
                    mPlayer.resume();
                    playPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));
                }
            }
        });

        //noinspection deprecation
        playPause.setImageDrawable(getResources().getDrawable(R.drawable.pause));

        ImageButton rewind = (ImageButton) view.findViewById(R.id.buttonRewind);
        rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.rewind();
            }
        });

        ImageButton forward = (ImageButton) view.findViewById(R.id.buttonForward);
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPlayer.forward();
            }
        });

        seekBar = (SeekBar) view.findViewById(R.id.seekBar);
        seekBar.setMax(1000);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                if (fromUser) {
                    mPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        Button buttonTrack = (Button) view.findViewById(R.id.buttonTrack);
        buttonTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random r = new Random();
                int i = r.nextInt(3);
                mPlayer.setTrack(tracks[i]);
            }
        });

        mPlayer.play();

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPlayer.release();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPlayer.stop();
    }
}

