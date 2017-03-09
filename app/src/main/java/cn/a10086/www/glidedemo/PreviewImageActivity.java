package cn.a10086.www.glidedemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author
 * @time 2017/3/9  11:46
 * @desc ${TODD}
 */
public class PreviewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreviewImageFragment fragment = new PreviewImageFragment();
        Intent intent = getIntent();

        Bundle args = new Bundle();
        args.putString("url", intent.getStringExtra("url"));
        fragment.setArguments(args);
        this.getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }
}
