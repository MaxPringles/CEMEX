package com.telstock.tmanager.cemex.prospectos.funciones;

/**
 * Created by czamora on 9/6/16.
 */

/*
 * Copyright 2014 KC Ochibili
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
 *  The "‚‗‚" character is not a comma, it is the SINGLE LOW-9 QUOTATION MARK unicode 201A
 *  and unicode 2017 that are used for separating the items in a list.
 */
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.telstock.tmanager.cemex.prospectos.model.InformacionContacto;
import com.telstock.tmanager.cemex.prospectos.model.InformacionGeneral;
import com.telstock.tmanager.cemex.prospectos.model.ProductosSeleccionados;
import com.telstock.tmanager.cemex.prospectos.model.ServiciosSeleccionados;

import mx.com.tarjetasdelnoreste.realmdb.model.PlanSemanalDB;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaActividades.JsonAltaActividades;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.JsonAltaProspecto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.OportunidadVentaInicial;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Producto;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.Servicio;
import mx.com.tarjetasdelnoreste.realmdb.model.jsonAltaProspecto.SubsegmentosProducto;

public class TinyDB {

    private SharedPreferences preferences;
    private String DEFAULT_APP_IMAGEDATA_DIRECTORY;
    private String lastImagePath = "";

    public TinyDB(Context appContext) {
        preferences = PreferenceManager.getDefaultSharedPreferences(appContext);
    }


    /**
     * Get String value from SharedPreferences at 'key'. If key not found, return ""
     * @param key SharedPreferences key
     * @return String value at 'key' or "" (empty String) if key not found
     */
    public String getString(String key) {
        return preferences.getString(key, "");
    }

    /**
     * Get parsed ArrayList of String from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of String
     */
    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(preferences.getString(key, ""), "‚‗‚")));
    }

    /**
     * Get boolean value from SharedPreferences at 'key'. If key not found, return 'defaultValue'
     * @param key SharedPreferences key
     * @return boolean value at 'key' or 'defaultValue' if key not found
     */
    public boolean getBoolean(String key) {
        return preferences.getBoolean(key, false);
    }

    /**
     * Get parsed ArrayList of Boolean from SharedPreferences at 'key'
     * @param key SharedPreferences key
     * @return ArrayList of Boolean
     */
    public ArrayList<Boolean> getListBoolean(String key) {
        ArrayList<String> myList = getListString(key);
        ArrayList<Boolean> newList = new ArrayList<Boolean>();

        for (String item : myList) {
            if (item.equals("true")) {
                newList.add(true);
            } else {
                newList.add(false);
            }
        }

        return newList;
    }

    /** MÉTODO QUE RECUPERA LA LISTA DE OBJETOS InformacionGeneral **/
    public ArrayList<InformacionGeneral> getListInformacionGeneral(String key, Class<?> mClass){
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<InformacionGeneral> objects =  new ArrayList<InformacionGeneral>();

        for(String jObjString : objStrings){
            InformacionGeneral value  = gson.fromJson(jObjString,  InformacionGeneral.class);
            objects.add(value);
        }
        return objects;
    }

    /** MÉTODO QUE RECUPERA LA LISTA DE OBJETOS SubsegmentosSeleccionados **/
    public ArrayList<SubsegmentosProducto> getSubsegmentosSeleccionados(String key, Class<?> mClass){
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<SubsegmentosProducto> objects =  new ArrayList<SubsegmentosProducto>();

        for(String jObjString : objStrings){
            SubsegmentosProducto value  = gson.fromJson(jObjString,  SubsegmentosProducto.class);
            objects.add(value);
        }
        return objects;
    }

    /** MÉTODO QUE RECUPERA LA LISTA DE OBJETOS ProductosSeleccionados **/
    public ArrayList<Producto> getProductosSeleccionados(String key, Class<?> mClass){
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<Producto> objects =  new ArrayList<Producto>();

        for(String jObjString : objStrings){
            Producto value  = gson.fromJson(jObjString,  Producto.class);
            objects.add(value);
        }
        return objects;
    }

    /** MÉTODO QUE RECUPERA LA LISTA DE OBJETOS ServiciosSeleccionados **/
    public ArrayList<Servicio> getServiciosSeleccionados(String key, Class<?> mClass){
        Gson gson = new Gson();

        ArrayList<String> objStrings = getListString(key);
        ArrayList<Servicio> objects =  new ArrayList<Servicio>();

        for(String jObjString : objStrings){
            Servicio value  = gson.fromJson(jObjString,  Servicio.class);
            objects.add(value);
        }
        return objects;
    }

    /** MÉTODO QUE RECUPERA EL OBJETO InformacionGeneral **/
    public JsonAltaProspecto getJsonAltaProspecto(String key, Class<JsonAltaProspecto> classOfT){

        String json = getString(key);
        JsonAltaProspecto value = new Gson().fromJson(json, classOfT);
        /*if (value == null)
            throw new NullPointerException();*/
        return value;
    }

    /** MÉTODO QUE RECUPERA EL OBJETO InformacionGeneral **/
    public InformacionContacto getInformacionContacto(String key, Class<InformacionContacto> classOfT){

        String json = getString(key);
        InformacionContacto value = new Gson().fromJson(json, classOfT);
        if (value == null)
            throw new NullPointerException();
        return value;
    }

    /** MÉTODO QUE RECUPERA EL OBJETO PlanSemanalDB **/
    public PlanSemanalDB getPlanSemanalDB(String key, Class<PlanSemanalDB> classOfT){

        String json = getString(key);
        PlanSemanalDB value = new Gson().fromJson(json, classOfT);
        if (value == null)
            throw new NullPointerException();
        return value;
    }

    /** MÉTODO QUE RECUPERA EL OBJETO OportunidadDeVenta **/
    public OportunidadVentaInicial getOportunidadDeVentaInicial(String key, Class<OportunidadVentaInicial> classOfT){

        String json = getString(key);
        OportunidadVentaInicial value = new Gson().fromJson(json, classOfT);
        if (value == null)
            throw new NullPointerException();
        return value;
    }


    // Put methods

    /**
     * Put int value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value int value to be added
     */
    public void putInt(String key, int value) {
        checkForNullKey(key);
        preferences.edit().putInt(key, value).apply();
    }

    /**
     * Put ArrayList of Integer into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param intList ArrayList of Integer to be added
     */
    public void putListInt(String key, ArrayList<Integer> intList) {
        checkForNullKey(key);
        Integer[] myIntList = intList.toArray(new Integer[intList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myIntList)).apply();
    }

    /**
     * Put long value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value long value to be added
     */
    public void putLong(String key, long value) {
        checkForNullKey(key);
        preferences.edit().putLong(key, value).apply();
    }

    /**
     * Put float value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value float value to be added
     */
    public void putFloat(String key, float value) {
        checkForNullKey(key);
        preferences.edit().putFloat(key, value).apply();
    }

    /**
     * Put double value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value double value to be added
     */
    public void putDouble(String key, double value) {
        checkForNullKey(key);
        putString(key, String.valueOf(value));
    }

    /**
     * Put ArrayList of Double into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param doubleList ArrayList of Double to be added
     */
    public void putListDouble(String key, ArrayList<Double> doubleList) {
        checkForNullKey(key);
        Double[] myDoubleList = doubleList.toArray(new Double[doubleList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myDoubleList)).apply();
    }

    /**
     * Put String value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value String value to be added
     */
    public void putString(String key, String value) {
        checkForNullKey(key); checkForNullValue(value);
        preferences.edit().putString(key, value).apply();
    }

    /**
     * Put ArrayList of String into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param stringList ArrayList of String to be added
     */
    public void putListString(String key, ArrayList<String> stringList) {
        checkForNullKey(key);
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        preferences.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    /**
     * Put boolean value into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param value boolean value to be added
     */
    public void putBoolean(String key, boolean value) {
        checkForNullKey(key);
        preferences.edit().putBoolean(key, value).apply();
    }

    /**
     * Put ArrayList of Boolean into SharedPreferences with 'key' and save
     * @param key SharedPreferences key
     * @param boolList ArrayList of Boolean to be added
     */
    public void putListBoolean(String key, ArrayList<Boolean> boolList) {
        checkForNullKey(key);
        ArrayList<String> newList = new ArrayList<String>();

        for (Boolean item : boolList) {
            if (item) {
                newList.add("true");
            } else {
                newList.add("false");
            }
        }

        putListString(key, newList);
    }

    /** MÉTODO QUE GUARDA EL OBJETO InformacionGeneral **/
    public void putJsonAltaProspecto(String key, JsonAltaProspecto obj){
        checkForNullKey(key);
        Gson gson = new Gson();
        putString(key, gson.toJson(obj));
    }

    /** MÉTODO QUE GUARDA EL OBJETO JsonAltaActividades **/
    public void putJsonAltaActividades(String key, JsonAltaActividades obj){
        checkForNullKey(key);
        Gson gson = new Gson();
        putString(key, gson.toJson(obj));
    }

    /** MÉTODO QUE GUARDA EL OBJETO InformacionContacto **/
    public void putDatosContacto(String key, InformacionContacto obj){
        checkForNullKey(key);
        Gson gson = new Gson();
        putString(key, gson.toJson(obj));
    }

    /** MÉTODO QUE GUARDA EL OBJETO PlanSemanalDB **/
    public void putPlanSemanalDB(String key, PlanSemanalDB obj){
        checkForNullKey(key);
        Gson gson = new Gson();
        putString(key, gson.toJson(obj));
    }



    /** MÉTODO QUE GUARDA EL OBJETO PlanSemanalDB **/
    public void putOfertaIntegral(String key, OportunidadVentaInicial obj){
        checkForNullKey(key);
        Gson gson = new Gson();
        putString(key, gson.toJson(obj));
    }

    /** MÉTODO QUE GUARDA LA LISTA DE OBJETOS InformacionGeneral **/
    public void putListInformacionGeneral(String key, ArrayList<InformacionGeneral> objArray){
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(Object obj : objArray){
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }

    /** MÉTODO QUE GUARDA LA LISTA DE OBJETOS SubsegmentosSeleccionados **/
    public void putListSubsegmentosSeleccionados(String key, ArrayList<SubsegmentosProducto> objArray){
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(Object obj : objArray){
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }

    /** MÉTODO QUE GUARDA LA LISTA DE OBJETOS ProductosSeleccionados **/
    public void putListProductosSeleccionados(String key, ArrayList<Producto> objArray){
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(Object obj : objArray){
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }

    /** MÉTODO QUE GUARDA LA LISTA DE OBJETOS ServiciosSeleccionados **/
    public void putListServiciosSeleccionados(String key, ArrayList<Servicio> objArray){
        checkForNullKey(key);
        Gson gson = new Gson();
        ArrayList<String> objStrings = new ArrayList<String>();
        for(Object obj : objArray){
            objStrings.add(gson.toJson(obj));
        }
        putListString(key, objStrings);
    }

    /**
     * Remove SharedPreferences item with 'key'
     * @param key SharedPreferences key
     */
    public void remove(String key) {
        preferences.edit().remove(key).apply();
    }

    /**
     * Delete image file at 'path'
     * @param path path of image file
     * @return true if it successfully deleted, false otherwise
     */
    public boolean deleteImage(String path) {
        return new File(path).delete();
    }


    /**
     * Clear SharedPreferences (remove everything)
     */
    public void clear() {
        preferences.edit().clear().apply();
    }

    /**
     * Retrieve all values from SharedPreferences. Do not modify collection return by method
     * @return a Map representing a list of key/value pairs from SharedPreferences
     */
    public Map<String, ?> getAll() {
        return preferences.getAll();
    }


    /**
     * Register SharedPreferences change listener
     * @param listener listener object of OnSharedPreferenceChangeListener
     */
    public void registerOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Unregister SharedPreferences change listener
     * @param listener listener object of OnSharedPreferenceChangeListener to be unregistered
     */
    public void unregisterOnSharedPreferenceChangeListener(
            SharedPreferences.OnSharedPreferenceChangeListener listener) {

        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }


    /**
     * Check if external storage is writable or not
     * @return true if writable, false otherwise
     */
    public static boolean isExternalStorageWritable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * Check if external storage is readable or not
     * @return true if readable, false otherwise
     */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();

        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }
    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     */
    public void checkForNullKey(String key){
        if (key == null){
            throw new NullPointerException();
        }
    }
    /**
     * null keys would corrupt the shared pref file and make them unreadable this is a preventive measure
     */
    public void checkForNullValue(String value){
        if (value == null){
            throw new NullPointerException();
        }
    }
}