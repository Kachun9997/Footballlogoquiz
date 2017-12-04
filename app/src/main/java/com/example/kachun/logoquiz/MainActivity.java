package com.example.kachun.logoquiz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kachun.logoquiz.Adapter.GridViewAnswerAdapter;
import com.example.kachun.logoquiz.Adapter.GridViewSuggestAdapter;
import com.example.kachun.logoquiz.Common.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    public List<String> suggestSource = new ArrayList<>();

    public GridViewAnswerAdapter answerAdapter;

    public GridViewSuggestAdapter suggestAdapter;

    public Button btnSubmit;

    public GridView gridViewAnswer,gridViewSuggest;

    public ImageView imgViewQuestion;

    private static final String KEY_INDEX = "index";

    private int mscore = 0;

    private TextView mscoreView;



    int[] image_list= {

            R.drawable.manchesterunited,
            R.drawable.bayernmunich,
            R.drawable.barcelona,
            R.drawable.parissaintgermain,
            R.drawable.realmadrid

    };


    public char[] answer;

    String correct_answer;
    private String TAG;
    private int mCurrentIndex;

    public MainActivity() {
    }

    public void exit(View v) {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }



    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MediaPlayer mediaPlayer = MediaPlayer.create(MainActivity.this,R.raw.background);
        mediaPlayer.setLooping(false);

        Button Music = (Button) this.findViewById(R.id.btnMusic);

        Music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.start();


            }
        });


        final MediaPlayer mediaPlay = MediaPlayer.create(MainActivity.this,R.raw.background);
        mediaPlay.start();

        Button Mute = (Button) this.findViewById(R.id.btnMute);


        Mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlay.pause();


            }
        });

        //Init View
        initView();
    }



    private void initView() {

        gridViewAnswer = (GridView)findViewById(R.id.gridViewAnswer);
        gridViewSuggest = (GridView)findViewById(R.id.gridViewSuggest);

        imgViewQuestion = (ImageView)findViewById(R.id.imgLogo);

        //Add SetupList Here
        setupList();
        btnSubmit = (Button)findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = "";
                for (int i = 0; i < Common.user_submit_answer.length; i++)
                    result += String.valueOf(Common.user_submit_answer[i]);
                if (result.equals(correct_answer)) {
                    Toast.makeText(getApplicationContext(), "Finish ! This is" + " " + result, Toast.LENGTH_SHORT).show();

                    //Reset
                    Common.count = 0;
                    Common.user_submit_answer = new char[correct_answer.length()];

                    //set Adapter
                    GridViewAnswerAdapter answerAdapter = new GridViewAnswerAdapter(setupNullList(),getApplicationContext());
                    gridViewAnswer.setAdapter(answerAdapter);
                    answerAdapter.notifyDataSetChanged();

                    GridViewSuggestAdapter suggestAdapter = new GridViewSuggestAdapter(suggestSource,getApplicationContext(),MainActivity.this);
                    gridViewSuggest.setAdapter(suggestAdapter);
                    suggestAdapter.notifyDataSetChanged();

                    setupList();
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Incorrect!!!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupList() {
        //Random logo
        Random random = new Random();
        int imageSelected = image_list[random.nextInt(image_list.length)];
        imgViewQuestion.setImageResource(imageSelected);

        correct_answer = getResources().getResourceName(imageSelected);
        correct_answer = correct_answer.substring(correct_answer.lastIndexOf("/")+1);

        answer = correct_answer.toCharArray();

        Common.user_submit_answer = new char[answer.length];

        //Add Answer character to List
        suggestSource.clear();
        for(char item:answer)
        {
            //Add logo name to list
            suggestSource.add(String.valueOf(item));
        }

        //Random add some character to list
        for(int i= answer.length;i<answer.length*2;i++)
            suggestSource.add(Common.alphabet_character[random.nextInt(Common.alphabet_character.length)]);

        //Set for GridView
        answerAdapter = new GridViewAnswerAdapter(setupNullList(),this);
        suggestAdapter = new GridViewSuggestAdapter(suggestSource,this,this);

        answerAdapter.notifyDataSetChanged();
        suggestAdapter.notifyDataSetChanged();


        gridViewSuggest.setAdapter(suggestAdapter);
        gridViewAnswer.setAdapter(answerAdapter);


    }

    private char[] setupNullList(){
            char result[] = new char[answer.length];
            for(int i=0;i<answer.length;i++)
                result[i]=' ';
            return result;
    }

}


