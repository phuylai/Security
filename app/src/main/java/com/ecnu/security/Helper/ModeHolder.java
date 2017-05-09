package com.ecnu.security.Helper;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecnu.security.Model.ActionType;
import com.ecnu.security.R;

/**
 * Created by Phuylai on 2017/5/8.
 */

public class ModeHolder extends BaseViewHolder {

    private ImageView iv_mode;
    private TextView tv_mode;
    private CardView cardView;
    private ModeClickListener clickListener;
    private RelativeLayout rl;

    private ActionType actionType;

    boolean selected;

    public interface ModeClickListener{
        void peace();
        void work();
        void away();
    }

    public ModeHolder(Context context,ActionType actionType,ModeClickListener clickListener,boolean selected){
        super(context);
        this.actionType = actionType;
        this.clickListener = clickListener;
        this.selected = selected;
        setView();
        setMode();
    }

    private void setMode(){
      if(selected){
          rl.setBackground(context.getResources().getDrawable(R.drawable.mode_selected_style));
          tv_mode.setTextColor(context.getResources().getColor(R.color.colorAccent));
      }
    }

    private void setView() {
        switch (actionType) {
            case PEACE:
                iv_mode.setImageResource(R.drawable.peace);
                tv_mode.setText(context.getResources().getString(R.string.peace));
                break;
            case WORK:
                iv_mode.setImageResource(R.drawable.work);
                tv_mode.setText(context.getResources().getString(R.string.work));
                break;
            case AWAY:
                iv_mode.setImageResource(R.drawable.away);
                tv_mode.setText(context.getResources().getString(R.string.away));
                break;
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.mode_detail;
    }

    @Override
    protected void findViews(View view) {
        tv_mode = (TextView) view.findViewById(R.id.tv_mode);
        iv_mode = (ImageView) view.findViewById(R.id.iv_mode);
        rl = (RelativeLayout) view.findViewById(R.id.rl_background);
        cardView = (CardView) view.findViewById(R.id.card_view);
    }

    @Override
    protected void setListeners() {
        cardView.setOnClickListener(this);
    }

    @Override
    protected void setEnabled(boolean enabled) {

    }

    @Override
    protected void onDestroy() {
        iv_mode = null;
        tv_mode = null;
        cardView = null;
    }

    @Override
    public void onClick(View view) {
        if(clickListener == null)
            return;
        rl.setBackground(context.getResources().getDrawable(R.drawable.mode_selected_style));
        tv_mode.setTextColor(context.getResources().getColor(R.color.colorAccent));
        switch (actionType){
            case PEACE:
                clickListener.peace();
                break;
            case WORK:
                clickListener.work();
                break;
            case AWAY:
                clickListener.away();
                break;
        }
    }

    public void cancelClick(){
        rl.setBackgroundResource(0);
        tv_mode.setTextColor(context.getResources().getColor(R.color.black));
    }

}
