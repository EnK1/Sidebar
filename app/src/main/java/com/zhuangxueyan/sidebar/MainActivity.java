package com.zhuangxueyan.sidebar;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.ypx.imagepicker.ImagePicker;
import com.ypx.imagepicker.bean.ImageItem;
import com.ypx.imagepicker.bean.MimeType;
import com.ypx.imagepicker.bean.selectconfig.CropConfig;
import com.ypx.imagepicker.data.OnImagePickCompleteListener;

import java.io.IOException;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private final String TAG = "MainActivity";
    private ArrayList<Item> items = new ArrayList<>();
    private Context context = MainActivity.this;
    private Bitmap bitmap;
    private SlidingMenu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initSystemBar(true);
        setContentView(R.layout.activity_main);

        initMenu();
    }
    private void initMenu(){
        menu = new SlidingMenu(context);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setTouchModeBehind(SlidingMenu.TOUCHMODE_FULLSCREEN);

        View view = getLayoutInflater().inflate(R.layout.left_menu,null);
        ListView(view);
        menu.setMenu(view);
        menu.attachToActivity(MainActivity.this,SlidingMenu.SLIDING_CONTENT);
    }

    private void ListView(View view){
        ListView listView = view.findViewById(R.id.list_item);

        Item item1 = new Item("我的钱包",R.mipmap.wallet);
        Item item2 = new Item("相机",R.mipmap.camera);

        items.add(item1);
        items.add(item2);

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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_head:
                final ImageView imageView = v.findViewById(R.id.iv_head);
                Log.e(TAG, "onClick: img" );
                takePic(v,imageView);
                imageView.setImageBitmap(bitmap);
                break;
            case R.id.x:
                menu.setSelectedView(v);
                menu.setOnCloseListener();
                break;

        }
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

    public void initSystemBar(Boolean isLight) {
        if (Build.VERSION.SDK_INT >= 21) {
            //LAYOUT_FULLSCREEN 、LAYOUT_STABLE：让应用的主体内容占用系统状态栏的空间；
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
            Window window = getWindow();
            //取消设置透明状态栏,使 ContentView 内容不再覆盖状态栏
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            //设置状态栏颜色
            if (isLight) {
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
            } else {
                window.setStatusBarColor(getResources().getColor(R.color.colorAccent));
            }

            //状态栏颜色接近于白色，文字图标变成黑色
            View decor = window.getDecorView();
            int ui = decor.getSystemUiVisibility();
            if (isLight) {
                //light --> a|=b的意思就是把a和b按位或然后赋值给a,   按位或的意思就是先把a和b都换成2进制，然后用或操作，相当于a=a|b
                ui |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                //dark  --> &是位运算里面，与运算,  a&=b相当于 a = a&b,  ~非运算符
                ui &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decor.setSystemUiVisibility(ui);
        }
    }

    //拍照并裁剪
    public void takePic(View view,ImageView imageView){
        CropConfig cropConfig = new CropConfig();
        cropConfig.setCropRatio(1, 1);
        //设置剪裁框间距，单位px
        cropConfig.setCropRectMargin(100);
        //是否圆形剪裁，圆形剪裁时，setCropRatio无效
        cropConfig.setCircle(false);
        //设置剪裁模式，留白或充满  CropConfig.STYLE_GAP 或 CropConfig.STYLE_FILL
        cropConfig.setCropStyle(CropConfig.STYLE_GAP);
        //设置留白模式下生成的图片背景色，支持透明背景
        cropConfig.setCropGapBackgroundColor(Color.TRANSPARENT );
        //调用拍照
        String name = null;
        boolean isCopyInDCIM=true;
        ImagePicker.takePhotoAndCrop(this, new RedBookPresenter(), cropConfig, new OnImagePickCompleteListener() {
            @Override
            public void onImagePickComplete(ArrayList<ImageItem> items) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),Uri.parse(items.get(0).getCropUrl()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
