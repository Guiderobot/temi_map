package com.example.temimap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

import com.robotemi.sdk.Robot;
import com.robotemi.sdk.map.MapModel;

public class MainActivity extends AppCompatActivity {

    private Robot robot;
    ListView listView;
    MyAdapter adapter;
    Button button;
    Button finish;

    List<MapModel> maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        robot = Robot.getInstance();
        maps = getMaps();

        listView=(ListView)findViewById(R.id.list_view);
        adapter = new MyAdapter(getApplicationContext(), maps);
        listView.setAdapter(adapter);

        button = (Button)findViewById(R.id.show);
        finish = (Button)findViewById(R.id.kill);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maps = getMaps();
                Log.d("maps num", Integer.toString(maps.size()));
                for(int i = 0; i < maps.size(); i++){
                    Log.d("maps name", maps.get(i).getName());
                }
                adapter.updateAdapter(maps);
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private List<MapModel> getMaps(){
        return robot.getMapList();
    }

    class MyAdapter extends BaseAdapter {
        Context mContext = null;
        LayoutInflater mLayoutInflater = null;
        private HashMap<Integer, View> mView;
        List<MapModel> map_list;

        public MyAdapter(Context context, List<MapModel> maps) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
            mView = new HashMap<Integer, View>();
            map_list = maps;
            Log.d("maps num", Integer.toString(map_list.size()));
            for(int i = 0; i < map_list.size(); i++){
                Log.d("maps name", map_list.get(i).getName());
            }
        }

        public void updateAdapter(List<MapModel> maps){
            if(map_list.size() != 0) map_list.clear();
            map_list = maps;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return map_list.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public MapModel getItem(int position){
            return map_list.get(position);
        }

        @Override
        public View getView(int position, View coverView, ViewGroup parent) {
            View view = mLayoutInflater.inflate(R.layout.map_list, null);

            TextView mapName = (TextView)view.findViewById(R.id.map_name);
            Button loadButton = (Button)view.findViewById(R.id.load_map);

            //map 이름 표시
            mapName.setText(map_list.get(position).getName());

            loadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    robot.loadMap(map_list.get(position).getId());
                }
            });

            return view;
        }
    }
}