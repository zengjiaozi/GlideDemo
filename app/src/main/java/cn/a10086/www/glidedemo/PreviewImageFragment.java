package cn.a10086.www.glidedemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * @author
 * @time 2017/3/9  11:47
 * @desc ${TODD}
 */
public class PreviewImageFragment extends Fragment {

    public PreviewImageFragment() {
        super();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        String url = args.getString("url");
        View v = inflater.inflate(R.layout.preview_image, container, false);
        ImageView img = (ImageView) v.findViewById(R.id.preview);
        Glide.with(this).load(url).fitCenter().into(img);
        return v;
    }
}