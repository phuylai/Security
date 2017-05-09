package com.ecnu.security.Controller;

import android.os.Bundle;

import com.ecnu.security.Helper.Constants;
import com.ecnu.security.Util.StringUtil;
import com.ecnu.security.view.fragments.BaseFragment;
import com.ecnu.security.view.fragments.ChangeDeviceNameFragment;
import com.ecnu.security.view.fragments.ChangeNameFragment;
import com.ecnu.security.view.fragments.DeviceEditFragment;
import com.ecnu.security.view.fragments.ModeFragment;
import com.ecnu.security.view.fragments.SettingFragment;
import com.ecnu.security.view.fragments.ViewIdFragment;

/**
 * Created by Phuylai on 2017/5/2.
 */

public class FragmentFactory {
    public static BaseFragment getFragment(String param, Bundle bundle,
                                           BaseFragment.SelectedItemCallBackListener selected){
        if(StringUtil.isNull(param)){
            return null;
        }
        if(param.equals(Constants.FRAG_SETTING)){
            return new SettingFragment();
        }
        else if(param.equals(Constants.FRAG_MODE)){
            return new ModeFragment();
        }
        else if(param.equals(Constants.PARAM_NICKNAME)){
            ChangeNameFragment changeNameFragment = new ChangeNameFragment();
            changeNameFragment.setArguments(bundle);
            return changeNameFragment;
        }else if(param.equals(Constants.PARAM_DEV_ID)){
            ViewIdFragment viewIdFragment = new ViewIdFragment();
            viewIdFragment.setArguments(bundle);
            return viewIdFragment;
        }else if(param.equals(Constants.PARAM_DEV_NAME)){
            ChangeDeviceNameFragment changeDeviceNameFragment = new ChangeDeviceNameFragment();
            changeDeviceNameFragment.setArguments(bundle);
            changeDeviceNameFragment.registerSelectedCallBackListener(selected);
            return changeDeviceNameFragment;
        }else if(param.equals(Constants.FRAG_DETAIL)){
            DeviceEditFragment deviceEditFragment = new DeviceEditFragment();
            deviceEditFragment.setArguments(bundle);
            return deviceEditFragment;
        }
        return null;
    }
}
