package edu.ucsd.cse110.cse110_team17_project;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Pair;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.*;

import java.text.DecimalFormat;
import java.util.Optional;

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

    public static double updateAngle(double lat_1, double lon_1, double lat_2, double lon_2) {
        double y = Math.sin(Math.toRadians(lon_2 - lon_1)) * Math.cos(Math.toRadians(lat_2));
        double x = Math.cos(Math.toRadians(lat_1)) * Math.sin(Math.toRadians(lat_2)) -
                Math.sin(Math.toRadians(lat_1)) * Math.cos(Math.toRadians(lat_2)) * Math.cos(Math.toRadians(lon_2 - lon_1));
        double res = (Math.toDegrees(Math.atan2(y, x)) + 360) % 360;
        return res;
    }

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
}