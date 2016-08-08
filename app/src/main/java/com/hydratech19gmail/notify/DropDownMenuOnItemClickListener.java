package com.hydratech19gmail.notify;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jaelse on 02-08-2016.
 */
public class DropDownMenuOnItemClickListener implements AdapterView.OnItemClickListener{


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Context context = view.getContext();
        PopupWindow popupwindow = new PopupWindow(context);
        // add some animation when a list item was clicked
        Animation fadeInAnimation = AnimationUtils.loadAnimation(view.getContext(), android.R.anim.fade_in);
        fadeInAnimation.setDuration(10);
        view.startAnimation(fadeInAnimation);

        // dismiss the pop up
        popupwindow.dismiss();

        // get the id
        String selectedItemTag = ((TextView) view).getText().toString();
        Toast.makeText(context, "Drop Down List: " + selectedItemTag, Toast.LENGTH_SHORT).show();

    }
}
