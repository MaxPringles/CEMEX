package com.telstock.tmanager.cemex.funciones;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.firebase.crash.FirebaseCrash;

/*IMPORTANTE: 
 * 
 * Esta clase requiere el siguiente permiso:
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" /> 
 * 
 */

/**
 * @author Alessandry Cruz
 * @version 1.0
 * @since Jueves, 19 de Septiembre de 2013
 */
public class InformacionTelefonica {

	private static String strTAG = "InformacionTelefonica";
	private TelephonyManager telephonyManager;

	public InformacionTelefonica(Context context) {
		telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * Obtiene el IMEI del Tel�fono
	 * 
	 * @return String strImei
	 */
	public String obtenerImei() {
		String strImei = new String();
		try {
			strImei = telephonyManager.getDeviceId();
		} catch (Exception e) {
			Log.e(strTAG + ", " + this, e.toString());
			strImei = e.toString();
		}
		return strImei;
	}

	/**
	 * Obtiene el N�mero del Tel�fono
	 * 
	 * @return String strTelefono
	 */
	public String obtenerNumero() {
		String strTelefono = new String();
		try {
			strTelefono = telephonyManager.getLine1Number();
		} catch (Exception e) {
			Log.e(strTAG + ", " + this, e.toString());
		}
		if (strTelefono == null || strTelefono.isEmpty()) {
			strTelefono = "No Disponible";
		}
		return strTelefono;
	}

	/**
	 * Obtiene la Marca del Tel�fono
	 * 
	 * @return String strTelefono
	 */
	public String obtenerMarca() {
		String strMarca = new String();
		try {
			strMarca = Build.MANUFACTURER;
		} catch (Exception e) {
			Log.e(strTAG + ", " + this, e.toString());
		}
		return strMarca;
	}

	/**
	 * Obtiene el Modelo del Tel�fono
	 * 
	 * @return String strModelo
	 */
	public String obtenerModelo() {
		String strModelo = new String();
		try {
			strModelo = Build.MODEL;
		} catch (Exception e) {
			Log.e(strTAG + ", " + this, e.toString());
		}
		return strModelo;
	}

	/**
	 * Obtiene el N�mero de Serie del Tel�fono
	 * 
	 * @return String strNoSerie
	 */
	public String obtenerNoSerie() {
		String strNoSerie = new String();
		try {
			strNoSerie = Build.SERIAL;
		} catch (Exception e) {
			Log.e(strTAG + ", " + this, e.toString());
		}
		return strNoSerie;
	}

	/**
	 * Obtiene la Versi�n Android del Tel�fono
	 * 
	 * @return String strVersionAndroid
	 */
	public String obtenerVersionAndroid() {
		String strVersionAndroid = new String();
		try {
			strVersionAndroid = Build.VERSION.RELEASE;
		} catch (Exception e) {
			Log.e(strTAG + ", " + this, e.toString());
		}
		return strVersionAndroid;
	}

	/**
	 * Obtiene la Versi�n Reciente de la Aplicaci�n
	 * 
	 * @param context
	 * @return String strVersionAplicacion
	 */
	public String obtenerVersionAplicacion(Context context) {
		String strVersionAplicacion = new String();
		try {
			ComponentName comp = new ComponentName(context, context.getClass());
			PackageInfo pinfo = context.getPackageManager().getPackageInfo(
					comp.getPackageName(), 0);
			strVersionAplicacion = pinfo.versionName;
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			FirebaseCrash.log("Error versión");
			FirebaseCrash.report(e);
		}
		return strVersionAplicacion;
	}

}
