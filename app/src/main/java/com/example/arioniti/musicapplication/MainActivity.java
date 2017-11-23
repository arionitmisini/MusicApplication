package com.example.arioniti.musicapplication;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import java.io.File;
import java.util.ArrayList;

import static com.example.arioniti.musicapplication.Player.mp;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    MSD msd;
    Player p=new Player();
    int current;
    Button nowPlayingButon;
    SearchView searchView;
    String[]items;
    private int musicPosition;
    ArrayList <File>mySongs;
    ArrayAdapter<String>adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView)findViewById(R.id.listaMuzikav);
        mySongs=findSongs(Environment.getExternalStorageDirectory());
        items=new String[mySongs.size()];

        for(int i=0;i<mySongs.size();i++){
            items[i]=mySongs.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }

        final ArrayAdapter<String>adp=new ArrayAdapter<String>(getApplicationContext(),R.layout.song_list,R.id.textView,items);
        lv.setAdapter(adp);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(mp ==null){
                    startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("listaMuzikav",mySongs));
                    musicPosition=position;
                }
                else{
                    mp.stop();
                    mp.release();
                    startActivity(new Intent(getApplicationContext(),Player.class).putExtra("pos",position).putExtra("listaMuzikav",mySongs));
                    musicPosition=position;

                }

            }
        });

        /*********NOW PLAYING BUTTON*********/
        nowPlaying();
        /*searchView=(SearchView)findViewById(R.id.searchView);
        final ArrayAdapter<String>adapter=new ArrayAdapter<String>(getApplicationContext(),R.layout.song_list,R.id.textView,items);
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            public boolean onQueryTextSubmit(String query){

                return false;
            }
            public boolean onQueryTextChange(String newText){
                adapter.getFilter().filter(newText);
                return false;
            }
        });*/

    }


    public ArrayList<File>findSongs(File root){
        msd = new MSD();

        ArrayList<File>al=new ArrayList<File>();
        File[]files=root.listFiles();

        for(File singleFile : files){
            if(singleFile.isDirectory()&&!singleFile.isHidden()){
                al.addAll(findSongs(singleFile));

            } else{
                if(singleFile.getName().endsWith(".mp3")||singleFile.getName().endsWith(".wav"))
                    al.add(singleFile);
            }
        }
        /**********************MSD ALGORITHM SORT MUSIC*********************/
        File[]muzikat=new File[al.size()];

        for(int i=0;i<al.size();i++){
            muzikat[i]=al.get(i);
        }

        String[]muzikatString=new String[muzikat.length];

        for(int i=0;i<muzikat.length;i++){
            muzikatString[i]=muzikat[i].getName().toString();
        }

        int n = muzikatString.length;

        msd.sort(muzikatString);

        ArrayList<File>MuzikatFund=new ArrayList<File>();

        /*String fjala;
        for(int i=0;i<n;i++){
            fjala=muzikatString[i];
                if (fjala.equals(".mp3")) {

                 }

        }*/
        for (String x : muzikatString) {
            for (File y : muzikat) {
                if(x.equals(y.getName().toString())){
                    MuzikatFund.add(y);
                }
            }
        }
        /********************************************************************/
        return MuzikatFund;
    }

    public void nowPlaying(){


        nowPlayingButon=(Button)findViewById(R.id.nowPlayingButon);
        nowPlayingButon.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    mp.setVolume(0,0);
                    startActivity(new Intent(getApplicationContext(), Player.class).putExtra("pos", musicPosition).putExtra("listaMuzikav", mySongs));



            }
        });


    }


    
}
