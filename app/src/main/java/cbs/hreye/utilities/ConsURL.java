package cbs.hreye.utilities;

import android.content.Context;

/**
 * Created by Administrator on 6/6/2017.
 *  Faisla Jo Kuch Bhi Ho Manzoor Hona Chahiye
 *  Jang Ho Ya Ishq Ho Bharpoor Hona Chahiye
 */
public class ConsURL {
    public static String LOCAL_BASE_URL = "http://103.75.33.98:86/";   //for local test
    // public static String BASE_URL = "http://112.196.47.100:85/";  // for New Era
    public static String CBS_BASE_URL = "http://hbmas.cogniscient.in/"; // for CBS HR
    // public static String HI_BASE_URL = "http://116.206.148.90:82/"; // for HiTech HR
    // public static String HI_BASE_URL = "http://103.76.253.26:82/"; // for HiTech HR New IP
    // public static String BASE_URL = "http://122.176.75.127:84/"; // for NuTech HR
    // public static String BASE_URL = "http://203.92.32.108:88/"; // for Rose ferry HR
    // public static String BASE_URL = "http://122.176.17.12:85/"; // for Lahag HR
    //public static String BASE_URL = "http://103.127.131.110:85/"; // for Lahag HR
    // public static String BASE_URL = "http://103.61.101.132:86/"; // for gk binding HR
    // public static String BASE_URL = "http://175.111.181.174:86/"; // for gk binding HR New 13 feb 2023
    // public static String BASE_URL = "http://135.181.115.154:82/"; // for Techscope  HR
    // public static String BASE_URL = "http://182.75.197.34:96/"; // for Indaag  HR
    // public static String BASE_URL = "http://122.176.77.2:94/"; // for AgPoly  HR
    // public static String BASE_URL = "http://103.210.50.36:82/"; // for Kineco  HR  5Jun 2023
    // public static String BASE_URL = "http://122.176.90.85:83/"; // for Green Fuel  HR  7Aug 2023

    public static String UTF_8 = "UTF-8";
    public static String CONTENT_TYPE = "Content-type";
    public static String APPLICATION_JSON = "application/json";

    public static String baseURL(Context context) {
        return CommonMethods.getPrefsDataURL(context, PrefrenceKey.SERVER_IP, "");
    }


}


