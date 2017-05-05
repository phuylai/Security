package com.ecnu.security.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.CalendarContract;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.common.design.MaterialDialog;
import com.ecnu.security.Model.DeviceModel;
import com.ecnu.security.R;

import org.w3c.dom.Text;

/**
 * Created by Phuylai on 2017/4/25.
 */

public class ResourceUtil {
    /**
     * this context for appliction
     */
    private static Context mContext = null;

    /**
     * resource refrenerce
     */
    private static Resources mRessurces = null;

    /**
     *
     * @param context
     */
    public static void initConfig(Context context) {
        mContext = context;
        mRessurces = mContext.getResources();
    }

    /**
     * Provide color by resource id
     *
     * @param colorResId
     * @return
     */
    public static int getColor(int colorResId) {
        return mRessurces.getColor(colorResId);
    }

    public static int getColor(Context context, int colorId) {
        if (context == null) {
            return 0;
        }

        return getColor(context.getResources(), colorId);
    }

    public static int getColor(Resources resources, int colorId) {
        if (resources == null) {
            return 0;
        }

        return resources.getColor(colorId);
    }

    public static String getString(int colorResId) {
        return mRessurces.getString(colorResId);
    }

    /**
     * prodide drawable by resource id
     *
     * @param drawableResId
     * @return
     */
    public static Drawable getDrawable(int drawableResId) {
        return mRessurces.getDrawable(drawableResId);
    }

    public static LayoutInflater getLayoutInflater() {
        return (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * provider arra by arr resource id
     *
     * @param arrResId
     */
    public static String[] getResourceArr(int arrResId) {
        return mRessurces.getStringArray(arrResId);
    }

    public static Context getContext(){
        return mContext;
    }

    public static Resources getmRessurces() {
        return mRessurces;
    }

    public static void setmRessurces(Resources mRessurces) {
        ResourceUtil.mRessurces = mRessurces;
    }

    public static void addGreyLine(Context context, LinearLayout ll, int height){
        View view = new View(context);
        view.setBackgroundColor(context.getResources().getColor(R.color.line_grey));
        view.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
        addView(ll,view);
    }

    public static void addView(LinearLayout bodyView,View view) {
        if (view == null) {
            return;
        }
        if (bodyView == null) {
            return;
        }
        if(view.getParent() != null){
            bodyView.removeAllViews();
        }
        bodyView.addView(view);
    }

    public static void setMarginTop(int marginTop, View view) {
        if (view == null) {
            return;
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (lp == null){
            lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        lp.topMargin = marginTop;
        view.setLayoutParams(lp);
    }

    public static void setEditTextMaxLength(EditText editText, int maxLength) {
        InputFilter[] filters = new InputFilter[] { new InputFilter.LengthFilter(
                maxLength) };
        editText.setFilters(filters);
    }

    /*
   * set text for the warning textview
   * */
    public static void setWarningText(TextView textView, int textId) {
        if (textId < 0) {
            hiddenWarning(textView);
            return;
        }
        setVisible(true, textView);
        if (textView == null) {
            return;
        }

        textView.setText(textId);
    }

    /*
hide the warning textview
* */
    public static void hiddenWarning(TextView textView) {
        if (textView == null) {
            return;
        }
        textView.setVisibility(View.INVISIBLE);
    }

    public static void setVisibility(View view, int visibility) {
        if (view == null) {
            return;
        }

        if (view.getVisibility() == visibility) {
            return;
        }

        view.setVisibility(visibility);
    }

    private static void setVisibilityGone(View view) {
        setVisibility(view, View.GONE);
    }

    public static void setVisible(boolean visible, View view) {
        if (visible) {
            setVisibilityVisible(view);
        } else {
            setVisibilityGone(view);
        }
    }

    private static void setVisibilityVisible(View view) {
        setVisibility(view, View.VISIBLE);
    }

    /**
     * set top maring for warning text in linear layout
     */
    public static void setWarningTopMarginByLinearLayout(Context context,
                                                         TextView textView) {
        if (textView == null) {
            return;
        }
        android.widget.LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) textView
                .getLayoutParams();
        lp.topMargin = getDimenPx(context,
                R.dimen.twenty_dp);
        textView.setLayoutParams(lp);
    }

    public static int getDimenPx(Context context, int dimenId) {
        if (context == null) {
            return 0;
        }

        return context.getResources().getDimensionPixelOffset(dimenId);
    }

    /**
     * To recycle the bitmap when activity is destroyed
     * @author phuylai
     * @param view
     */

    public static void recycleBackground(View view) {
        if (view == null) {
            return;
        }
        Drawable drawable = view.getBackground();
        if (!(drawable instanceof BitmapDrawable)) {
            drawable = null;
            return;
        }
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
            bitmap = null;
        }
        bitmapDrawable = null;
    }

    /**
     * set top maring for warning text in relative layout
     */
    public static void setWarningTopMarginByRelativeLayout(Context context,
                                                           TextView textView) {
        if (textView == null) {
            return;
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
        lp.topMargin = ResourceUtil.getDimenPx(context,
                R.dimen.twenty_dp);
        textView.setLayoutParams(lp);
    }

}

