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

    public static double updateCoordinates(double lat_1, double lon_1, double lat_2, double lon_2) {
        double y = Math.sin(Math.toRadians(lon_2 - lon_1)) * Math.cos(Math.toRadians(lat_2));
        double x = Math.cos(Math.toRadians(lat_1))*Math.sin(Math.toRadians(lat_2)) -
                Math.sin(Math.toRadians(lat_1))*Math.cos(Math.toRadians(lat_2))*Math.cos(Math.toRadians(lon_2 - lon_1));
        double res = (Math.atan2(y, x) * 180/Math.PI + 360) % 360;
        return res;
    }

    public static Pair<Double, Double> validCoordinate(String coordinate) {
        if (!coordinate.contains(",")){
            System.out.println("Place enter both longitudes and latitudes coordinates");
            return null;
        }
        String[] coordinates = coordinate.split(",");
        String latitudeStr = coordinates[0];
        String longitudeStr = coordinates[1];
        double latitude = Double.parseDouble(latitudeStr);
        double longitude = Double.parseDouble(longitudeStr);
        int decimalPlacesLati = 0;
        int decimalPlacesLong = 0;
        DecimalFormat dfZero = new DecimalFormat("0.000000");
        if (latitudeStr.contains(".")){
            decimalPlacesLati = 0;
            int integerPlacesLati = latitudeStr.indexOf('.');
            decimalPlacesLati = latitudeStr.length() - integerPlacesLati - 1;
            if (decimalPlacesLati > 6){
                latitudeStr = dfZero.format(latitude);
            }
        }
        if (longitudeStr.contains(".")){
            decimalPlacesLong = 0;
            int integerPlacesLong = longitudeStr.indexOf('.');
            decimalPlacesLong = longitudeStr.length() - integerPlacesLong - 1;
            if (decimalPlacesLong > 6){
                longitudeStr = dfZero.format(longitude);
            }
        }
        latitude = Double.parseDouble(latitudeStr);
        longitude = Double.parseDouble(longitudeStr);

        double latitudeLimit = 90.000;
        double longitudeLimit = 180.000;
        if (!(Math.abs(latitude) < latitudeLimit) || !(Math.abs(longitude) < longitudeLimit)){
            System.out.println("Latitude/Longitude coordinate out of range");
            return null;
        }
        return new Pair<>(latitude, longitude);
    }
}