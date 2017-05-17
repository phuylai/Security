package com.ecnu.security.Controller;

import java.util.Observable;

/**
 * Created by Phuylai on 2017/5/16.
 */

public class ObservableObject extends Observable {
    private static ObservableObject instance = new ObservableObject();

    public static ObservableObject getInstance(){
        return instance;
    }

    private ObservableObject(){

    }

    public void updateValue(Object data){
        synchronized (this){
            setChanged();
            notifyObservers(data);
        }
    }
}
