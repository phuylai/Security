package com.ecnu.security.Controller;

import android.widget.Toast;

/**
 * Created by Phuylai on 2017/4/26.
 */

public class CurrentSession {
    private static Toast toast = null;
    private static CurrentSession instance = null;
    public static Toast getToast() {
        return toast;
    }

    private static CurrentSession getInstance() {
        if (instance == null) {
            instance = new CurrentSession();
        }
        return instance;
    }
}
