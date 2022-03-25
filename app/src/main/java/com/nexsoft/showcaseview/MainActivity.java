package com.nexsoft.showcaseview;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.nexsoft.showcaseviewlib.ShowGuideView;
import com.nexsoft.showcaseviewlib.entity.GuideModel;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    View view1, view2, view3, view4, view5, view6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view1 = findViewById(R.id.view1);
        view2 = findViewById(R.id.view2);
        view3 = findViewById(R.id.view3);
        view4 = findViewById(R.id.view4);
        view5 = findViewById(R.id.view5);
        view6 = findViewById(R.id.view6);

        List<GuideModel> list = new ArrayList<>();
        list.add(new GuideModel(view1, "ini title view 1", "ini message view 1"));
        list.add(new GuideModel(view2, "ini title view 2", "ini message view 2", WelcomeActivity.class,true));
        list.add(new GuideModel(view3, "ini title view 3", "ini message view 3"));
        list.add(new GuideModel(view4, "ini title view 4", "ini message view 4", getString(R.string.link_facebook)));
        list.add(new GuideModel(view5, "ini title view 5", "ini message view 5", LinkActivity.class));
        list.add(new GuideModel(view6, "ini title view 6", "ini message view 6"));
        ShowGuideView showGuideView = new ShowGuideView(getResources().getColor(R.color.colorAccent), false);
        showGuideView.showGuide(this, list, "guideLogin");

    }


}
