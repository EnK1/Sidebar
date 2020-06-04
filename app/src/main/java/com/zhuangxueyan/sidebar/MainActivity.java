package com.zhuangxueyan.sidebar;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    private ArrayList<Item> items = new ArrayList<>();
    private Context context = MainActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMenu();
    }
    private void initMenu(){
        SlidingMenu menu = new SlidingMenu(context);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);

        View view = getLayoutInflater().inflate(R.layout.left_menu,null);
        ListView(view);
        menu.setMenu(view);
        menu.attachToActivity(MainActivity.this,SlidingMenu.SLIDING_CONTENT);
    }

    private void ListView(View view){
        ImageView imageView = view.findViewById(R.id.iv_head);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ImagePicker
            }
        });
        ListView listView = view.findViewById(R.id.list_item);

        Item item1 = new Item("我的钱包",R.mipmap.wallet);

        items.add(item1);

        MyAdapter adapter = new MyAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int i) {
            return items.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if(view == null){
                view = getLayoutInflater().inflate(R.layout.item_menu,viewGroup,false);
                holder = new ViewHolder();
                holder.imageView = view.findViewById(R.id.imageView);
                holder.textView = view.findViewById(R.id.text);

                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            holder.imageView.setImageResource(items.get(i).getImage());
            holder.textView.setText(items.get(i).getItemName());
            return view;
        }
    }
    class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
}
