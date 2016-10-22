package com.hydratech19gmail.notify;

/**
 * Created by nischal on 22/10/16.
 */

public class StringConverter {
    static String userIdToKey(String userId){
        return userId.replace("@","").replace(".","");
    }
}
