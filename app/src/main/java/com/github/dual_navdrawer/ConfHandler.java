package com.github.dual_navdrawer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.support.design.widget.NavigationView;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;

import static com.github.dual_navdrawer.MainActivity.leftDrawerAction;
import static com.github.dual_navdrawer.MainActivity.rightIcon;
import static com.github.dual_navdrawer.MainActivity.rightId;
import static com.github.dual_navdrawer.MainActivity.rightLabel;
import static com.github.dual_navdrawer.R.color.colorIcon;

public class ConfHandler {
    private Context cont;
    private NavigationView navView_left;
    private NavigationView navView_right;
    private RelativeLayout bot_RelLayout;

    private ColorStateList color;
    private int textWidth;
    private int btnWidth;
    private int btnHeight;
    private int btnMargin;

    private int position;
    private int index;
    private boolean reset;

    public ConfHandler(Context context, NavigationView nLeft, NavigationView nRight, RelativeLayout rlBottom) {
        cont = context;
        navView_left = nLeft;
        navView_right = nRight;
        bot_RelLayout = rlBottom;

        color = cont.getResources().getColorStateList(colorIcon, null);
        float scale = cont.getResources().getDisplayMetrics().density;

        textWidth = (int) (80 * scale + 0.5f);
        btnWidth = (int) (30 * scale + 0.5f);
        btnHeight = (int) (60 * scale + 0.5f);
        btnMargin = (int) (25 * scale + 0.5f);

        position = 0;
        index = 0;
        reset = true;

        rightId.clear();
        rightLabel.clear();
        rightIcon.clear();
    }

    public void onConfLeft(String label, String icon, boolean shortcut) {
        try {
            Menu menu = navView_left.getMenu();
            InputStream stream = cont.getAssets().open(icon);
            Drawable drawIcon = Drawable.createFromStream(stream, null);
            menu.add(R.id.group_left,index, Menu.NONE,label).setIcon(drawIcon).setCheckable(true);

            index++;
            if (shortcut) {
                RelativeLayout.LayoutParams params;

                TextView text = new TextView(cont);
                text.setText(label);
                text.setId(index);
                text.setTextColor(color);
                text.setGravity(Gravity.CENTER);

                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.END_OF, position);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.width = textWidth;
                text.setLayoutParams(params);
                text.setPadding(0, 0, 0, 15);
                bot_RelLayout.addView(text);

                Button btn = new Button(cont);
                btn.setId(index);
                btn.setBackgroundTintList(color);
                btn.setBackground(drawIcon);

                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.END_OF, position);
                params.width = btnWidth;
                params.height = btnHeight;
                params.setMargins(btnMargin, btnMargin-10, btnMargin, btnMargin+10);
                btn.setLayoutParams(params);

                final int id = index-1;

                btn.setClickable(true);
                btn.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        leftDrawerAction(cont, id);
                    }
                });
                bot_RelLayout.addView(btn);
                position = index;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onConfRight(String label, String icon, boolean shortcut) {
        if (reset) {
            index = 0;
            reset = false;
        }

        rightLabel.add(label);
        rightIcon.add(icon);
        if (shortcut)
            try {
                Menu menu = navView_right.getMenu();
                InputStream stream = cont.getAssets().open(icon);

                rightId.add(index);
                menu.add(R.id.group_right,index, Menu.NONE,label).setIcon(Drawable.createFromStream(stream, null)).setCheckable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        index++;
    }
}
