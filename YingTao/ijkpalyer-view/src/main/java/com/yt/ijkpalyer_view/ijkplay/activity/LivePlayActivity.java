package com.yt.ijkpalyer_view.ijkplay.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yt.ijkpalyer_view.R;
import com.yt.ijkpalyer_view.ijkplay.listener.OnShowThumbnailListener;
import com.yt.ijkpalyer_view.ijkplay.manager.IjkPlayManager;
import com.yt.ijkpalyer_view.ijkplay.manager.PlayStateParams;
import com.yt.ijkpalyer_view.ijkplay.utils.MediaUtils;

import java.util.List;

/**
 * Created by jiatao on 2017/3/20.
 * 直播
 */

public class LivePlayActivity extends FragmentActivity {

    private IjkPlayManager player;
    private Context mContext;
    private View rootView;
//    private List<LiveBean> list;
    private String url = "http://hdl.9158.com/live/744961b29380de63b4ff129ca6b95849.flv";

    private String url1 = "rtmp://203.207.99.19:1935/live/CCTV2";
    private String url2 = "http://zv.3gv.ifeng.com/live/zhongwen800k.m3u8";

    private String title = "标题";
    private PowerManager.WakeLock wakeLock;
//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (list.size() > 1) {
//                url = list.get(1).getLiveStream();
//                title = list.get(1).getNickname();
//            }
//            player.setPlaySource(url)
//                    .startPlay();
//        }
//    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        rootView = getLayoutInflater().from(this).inflate(R.layout.simple_player_view_player, null);
        setContentView(rootView);

        /**常亮*/
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "liveTAG");
        wakeLock.acquire();

        player = new IjkPlayManager(this, rootView)
                .setTitle(title)
                .setScaleType(PlayStateParams.fitparent)
                .hideMenu(true)
                .hideSteam(true)
                .setForbidDoulbeUp(false)//是否可以双击，双击可以切换横竖屏
                .hideCenterPlayer(true)
                .hideControlPanl(true)
                .hideHideTopBar(false)
                .showThumbnail(new OnShowThumbnailListener() {
                    @Override
                    public void onShowThumbnail(ImageView ivThumbnail) {
                        Glide.with(mContext)
                                .load("http://pic2.nipic.com/20090413/406638_125424003_2.jpg")
                                .placeholder(R.color.cl_default)
                                .error(R.color.cl_error)
                                .into(ivThumbnail);
                    }
                });

        player.setPlaySource(url2)
                .startPlay();

        new Thread() {
            @Override
            public void run() {
                //这里多有得罪啦，网上找的直播地址，如有不妥之处，可联系删除
//                list = ApiServiceUtils.getLiveList();
//                mHandler.sendEmptyMessage(0);
            }
        }.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (player != null) {
            player.onPause();
        }
        MediaUtils.muteAudioFocus(mContext, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (player != null) {
            player.onResume();
        }
        MediaUtils.muteAudioFocus(mContext, false);
        if (wakeLock != null) {
            wakeLock.acquire();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.onDestroy();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (player != null) {
            player.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void onBackPressed() {
        if (player != null && player.onBackPressed()) {
            return;
        }
        super.onBackPressed();
        if (wakeLock != null) {
            wakeLock.release();
        }
    }

}
