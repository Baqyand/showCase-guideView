package com.nexsoft.showcaseviewlib;

import android.content.Context;
import android.os.Handler;
import android.view.View;

import com.nexsoft.showcaseviewlib.config.Gravity;
import com.nexsoft.showcaseviewlib.config.PointerType;
import com.nexsoft.showcaseviewlib.entity.GuideModel;
import com.nexsoft.showcaseviewlib.listener.GuideListener;
import com.nexsoft.showcaseviewlib.storage.SessionGuide;

import java.util.List;

public class ShowGuideView {
    private boolean isOnlyFirstTime;
    private GuideView mGuideView;
    private GuideView.Builder builder;
    private int color;
    private boolean isAllowToShowCheckBox;

    public ShowGuideView(int background, Boolean isAllowToShowCheckBox) {
        this.color = background;
        this.isAllowToShowCheckBox = isAllowToShowCheckBox;
    }

    public ShowGuideView(int background, Boolean isAllowToShowCheckBox,Boolean isOnlyFirstTime) {
        this.color = background;
        this.isAllowToShowCheckBox = isAllowToShowCheckBox;
        this.isOnlyFirstTime = isOnlyFirstTime;
    }


    public void showGuide(Context context, List<GuideModel> guideModelList, String sessionKey) {
        SessionGuide.beginInitialization(context);
        new Handler().postDelayed(() -> {

            if (!SessionGuide.getSessionGlobalBoolean(sessionKey) && guideModelList.size() > 0) {
                builder = new GuideView.Builder(context)
                        .setTitle(guideModelList.get(0).getTitle())
                        .setContentText(guideModelList.get(0).getMessage())
                        .setGravity(Gravity.center)
                        .setBackGroundColor(color)
                        .setPointerType(PointerType.none)
                        .setTargetView(guideModelList.get(0).getView())
                        .setLastIndex(guideModelList.size())
                        .setSessionKey(sessionKey)
                        .setOnlyFirstTime(isOnlyFirstTime)
                        .setGuideListener((view, index) -> {
                            index = index + 1;
                            if (index < guideModelList.size()) {
                                setTitleMessageGuide(guideModelList.get(index).getTitle()
                                        , guideModelList.get(index).getMessage()
                                        , guideModelList.get(index).getView()
                                        , guideModelList.get(index).getLinkClass()
                                        , guideModelList.get(index).getLinkText()
                                        , guideModelList.get(index).isGotoNewClassWithButton()
                                        , index);
                            } else {
                                return;
                            }

                            mGuideView = builder.build(index, isAllowToShowCheckBox);
                            mGuideView.show();
                        });

                mGuideView = builder.build(0, isAllowToShowCheckBox);
                mGuideView.show();
            }
        }, 500);
    }

    private void setTitleMessageGuide(String title, String message, View view, Class<?> linkClass, String linkText,boolean gotoNewClassWithButton, int index) {

        builder.setLinkClass(linkClass);
        builder.setGoToNewClassWithButton(gotoNewClassWithButton);
        builder.setLinkText(linkText);

        builder.setTitle(title)
                .setContentText(message)
                .setTargetView(view).build(index, isAllowToShowCheckBox);

    }

}
