package edu.ucsd.cse110.cse110_team17_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.*;
import java.util.*;
import java.lang.*;

import java.text.DecimalFormat;

public class Utilities {
    public static void showSuccess(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Successfully Registered!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    public static void showError(Activity activity, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity);

        alertBuilder
                .setTitle("Error!")
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, id) -> {
                    dialog.cancel();
                })
                .setCancelable(true);

        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();
    }

    // calculate orientation
    public static double updateAngle(double lat_1, double lon_1, double lat_2, double lon_2) {
        double y = Math.sin(Math.toRadians(lon_2 - lon_1)) * Math.cos(Math.toRadians(lat_2));
        double x = Math.cos(Math.toRadians(lat_1)) * Math.sin(Math.toRadians(lat_2)) -
                Math.sin(Math.toRadians(lat_1)) * Math.cos(Math.toRadians(lat_2)) * Math.cos(Math.toRadians(lon_2 - lon_1));
        double res = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
        return res;
    }

    // check valid coordinates
    public static Pair<Double, Double> validCoordinate(String coordinate) {

        if (!coordinate.contains(",")){
            return null;
        }
        String[] coordinates = coordinate.split(",");
        String latitudeStr = coordinates[0];
        String longitudeStr = coordinates[1];
        double latitude, longitude;
        try {
            latitude = Double.parseDouble(latitudeStr);
        }catch (NumberFormatException ex) {
            return null;
        }
        try {
            longitude = Double.parseDouble(longitudeStr);
        }catch (NumberFormatException ex) {
            return null;
        }
        int decimalPlacesLati = 0;
        int decimalPlacesLong = 0;
        DecimalFormat dfZero = new DecimalFormat("0.000000");
        if (latitudeStr.contains(".")){
            int integerPlacesLati = latitudeStr.indexOf('.');
            decimalPlacesLati = latitudeStr.substring(integerPlacesLati).length();
            if (decimalPlacesLati > 6){
                latitudeStr = dfZero.format(latitude);
            }
        }
        if (longitudeStr.contains(".")){
            int integerPlacesLong = longitudeStr.indexOf('.');
            decimalPlacesLong = longitudeStr.substring(integerPlacesLong).length();
            if (decimalPlacesLong > 6){
                longitudeStr = dfZero.format(longitude);
            }
        }
        latitude = Double.parseDouble(latitudeStr);
        longitude = Double.parseDouble(longitudeStr);

        double latitudeLimit = 90.000;
        double longitudeLimit = 180.000;
        if (Math.abs(latitude) > latitudeLimit || Math.abs(longitude) > longitudeLimit){
            return null;
        }
        return new Pair<>(latitude, longitude);
    }

    public static boolean isValidUID(String uid) {
        if (uid.length() != 18 || !uid.matches("^[a-zA-Z0-9]*$")) {
            return false;
        }
        return true;
    }

    public static List<String> parseFriendListString(String friendListString) {
        if (friendListString.isEmpty()) {
            return new ArrayList<>();
        }
        return new ArrayList<>(Arrays.asList(friendListString.split("-")));
    }

    public static String buildFriendListString(List<String> friendList){
//        if (friendList.isEmpty()){
//            return "";
//        } else if(friendList.size() < 2){
//            return friendList.get(0);
//        }
//        for(String friend : friendList){
//
//        }
        return String.join("-", friendList);
    }
    
    // calculate distance in miles
    public static double distance(double latitude1, double longitude1,
                                  double latitude2, double longitude2) {
        latitude1 = Math.toRadians(latitude1);
        longitude1 = Math.toRadians(longitude1);
        latitude2 = Math.toRadians(latitude2);
        longitude2 = Math.toRadians(longitude2);

        double dlon = longitude2 - longitude1;
        double dlat = latitude2 - latitude1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(latitude1) * Math.cos(latitude2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 6371;
        return(c * r) / 1.609;
    }


    //TODO: Change to 4 zones later!
    public static double distanceToViewRadius(double distance) {

        //Zone 1: 0-1 miles
        if (distance < 1) {
            return distance * 180.0;
        }
        //Zone 2: 1-10 miles
        else if (distance < 10) {
            distance -= 1.0;
            return 180.0 + (distance / (10.0-1.0)  * (330.0-180.0));
        }
        //Zone 3: 10-100 miles
        else if (distance < 500) {
            distance -= 10.0;
            return 330.0 + (distance / (500.0-10.0)  * (500.0-330.0));
        }
        //Zone 5: 1000+ miles
        else return 550.0;
    }

//    public static float correctZoomRatio(int zoomPosition) {
//        switch (zoomPosition) {
//            case 1:
//                return 1.5F;
//            case 2:
//                return 3F;
//            default:
//                return 1F;
//        }
//    }

}