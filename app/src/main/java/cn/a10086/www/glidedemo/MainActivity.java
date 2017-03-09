package cn.a10086.www.glidedemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recycleview;
    private int index = 1;
    private String mUrl = "http://gank.io/api/data/%E7%A6%8F%E5%88%A9/10/";
    private OkHttpClient okHttpClient;
    public List<String> mUrls = new ArrayList<String>();
    private GankAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        okHttpClient = new OkHttpClient();
        recycleview = (RecyclerView) findViewById(R.id.recycleview);
        recycleview.setLayoutManager(new StaggeredGridLayoutManager(2 , StaggeredGridLayoutManager.VERTICAL));
//         加载图片数据
        loadDatas(index);
        mAdapter = new GankAdapter(getApplicationContext(), mUrls);
        recycleview.setAdapter(mAdapter);
//        给RecycleView加入滑动监听
        recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isScrollToEnd(recycleview)) {
                    Log.e("tag", "============scroll to end");
                    index += 1;
                    loadDatas(index);
                }
            }
        });

    }

    //判断是否滑动到最后一个item
    private boolean isScrollToEnd(RecyclerView recycleview) {
        if (recycleview == null) return false;
        if (recycleview.computeVerticalScrollExtent() + recycleview.computeVerticalScrollOffset() >= recycleview.computeVerticalScrollRange())
            return true;
        return false;


    }

    //   加载网路图片数据
    private void loadDatas(int page) {
//        创建一个请求体
        Request request = new Request.Builder().url(mUrl + page).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "加载错误 IOException=" + e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
//       如果响应成功
                if (response.isSuccessful()) {
//                    获得响应体的结果
                    String result = response.body().string();
                    try {
                        JSONObject json = new JSONObject(result);
//                       获得了一个array的几何
                        JSONArray array = new JSONArray(json.getString("results"));
//                        看见几何或者数组 想要拿到里面的东西需要进行for循环
                        for (int i = 0; i < array.length(); i++) {
//                           获取一个json对象
                            JSONObject jsonObject = array.getJSONObject(i);
                            String urls = jsonObject.getString("url");
                            Log.e("tag", "========== url: " + urls);
                            mUrls.add(urls);
                        }
//                       通过handle来到主线程修改Ui
                        mHandler.sendEmptyMessage(2);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }
        });

    }

    public class GankAdapter extends RecyclerView.Adapter<GankAdapter.ViewHolder> {
        private List<String> mItems;
        private Context mContext;

        public GankAdapter(Context context, List<String> items) {
            super();
            mItems = items;
            mContext = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final String url = mItems.get(position);
            Log.i("tag", "============onBindViewHolder url: " + url);
            Glide.with(mContext).load(url).placeholder(R.mipmap.ic_launcher).diskCacheStrategy(DiskCacheStrategy.RESULT)
                    //.bitmapTransform(new CropCircleTransformation(mContext))  //如果想使用变换效果，这个注释可以打开
                    .into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(mContext,PreviewImageActivity.class);
                    intent.putExtra("url",url);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    mContext.startActivity(intent);


                }
            });
        }


        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView image;

            public ViewHolder(View itemView) {
                super(itemView);
                image = (ImageView) itemView.findViewById(R.id.images);
            }
        }
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 2:
                    setAdapter();
                    break;
            }
        }
    };

    private void setAdapter() {
        mAdapter.notifyDataSetChanged();

    }
}
