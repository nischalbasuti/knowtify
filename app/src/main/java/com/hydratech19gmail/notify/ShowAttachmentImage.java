package com.hydratech19gmail.notify;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zappereton on 22/10/16.
 */
public class ShowAttachmentImage extends Activity {

    ImageView image;
    TextView nameOfImage;
    String url = "";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.view_attachment);
        ActionBar mActionBar = getActionBar();

        url = getIntent().getExtras().getString("url");

        image = (ImageView)findViewById(R.id.attachment_image);
        nameOfImage = (TextView)findViewById(R.id.name_of_image);

        nameOfImage.setText(url);
        image.setImageBitmap(BitmapFactory.decodeFile(url));
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus){
            //getActionBar().hide();
        }
        if(!hasFocus){
            //getActionBar().show();
        }
    }
}
