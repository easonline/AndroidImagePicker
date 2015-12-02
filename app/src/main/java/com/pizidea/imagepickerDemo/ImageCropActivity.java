package com.pizidea.imagepickerDemo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader;
import com.pizidea.imagepicker.AndroidImagePicker;
import com.pizidea.imagepicker.ui.AvatarCropFragment;
import com.pizidea.imagepicker.widget.AvatarRectView;
import com.pizidea.imagepicker.widget.TouchImageView;


/**
 * 截取头像
 */
public class ImageCropActivity extends FragmentActivity implements View.OnClickListener{

    private TextView btnReChoose;
    private TextView btnOk;
    private ImageView ivShow;

    AvatarCropFragment mFragment;

    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        btnOk = (TextView) findViewById(R.id.btn_pic_ok);
        btnReChoose = (TextView) findViewById(R.id.btn_pic_rechoose);
        ivShow = (ImageView) findViewById(R.id.iv_show);
        btnOk.setOnClickListener(this);
        btnReChoose.setOnClickListener(this);

        imagePath = getIntent().getStringExtra(AndroidImagePicker.KEY_PIC_PATH);
        mFragment = new AvatarCropFragment();
        Bundle data = new Bundle();
        data.putString(AndroidImagePicker.KEY_PIC_PATH,imagePath);
        mFragment.setArguments(data);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, mFragment)
                .commit();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_pic_rechoose:
                finish();
                break;
            case R.id.btn_pic_ok:
                //
                Bitmap bmp = mFragment.getCropBitmap(60*2);
                finish();
                AndroidImagePicker.getInstance().notifyImageCropComplete(bmp,0);
                /*ivShow.setVisibility(View.VISIBLE);
                ivShow.setImageBitmap(bmp);
                Intent data = new Intent();
                data.putExtra("bitmap",bmp);
                setResult(RESULT_OK, data);
                finish();*/

                break;
            default:
                break;
        }
    }


}
