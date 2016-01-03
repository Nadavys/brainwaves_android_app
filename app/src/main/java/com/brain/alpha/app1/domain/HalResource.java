package com.brain.alpha.app1.domain;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public abstract class HalResource {
    public JSONObject links;

    public List<String> getUriLinksTo(String hop){
        List<String> returnList = new ArrayList<>();
        try {
            if((links.get(hop) instanceof JSONArray)){
                JSONArray tempList = (JSONArray)links.get(hop);
                for(int i=0; i < tempList.length(); i++){
                    returnList.add((String) ((JSONObject) tempList.get(i)).get("href"));
                }
            }else{
                returnList.add((String) ((JSONObject) links.get(hop)).get("href"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return returnList;
    }

    public String getUriLinkTo(String hop){
        try {
            return getUriLinksTo(hop).get(0);
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();
        }

        return null;
    }

}
