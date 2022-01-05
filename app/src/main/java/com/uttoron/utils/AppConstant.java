package com.uttoron.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.uttoron.model.AllDataResponse;
import com.uttoron.model.Category;
import com.uttoron.model.Content;
import com.uttoron.model.GeneralSetting;
import com.uttoron.model.ImageBmpModel;
import com.uttoron.model.SubCategory;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

//package com.uttoron.utils;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Base64;
//
//import java.io.ByteArrayOutputStream;
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//
//
//import static android.content.Context.MODE_PRIVATE;
//
///**
// * Created by Sadi on 2/14/2018.
// */
//
public class AppConstant {

//    public static String clockInOu="";
//    public static String lat="";
//    public static String lng="";
//    public static String localpic="localpic";
//    //public static String officname="officname";
//    public static String locationName="";
//
//    public static String alarmInOnOff="alarmInOnOff";
//    public static boolean isGallery=false;
//    public static boolean isHq=false;
//
//    //public static List<LocationInfo> locationInfoList = new ArrayList<>();
//    public static String path ="path";
//    public static String photourl ="http://css-bd.com/attendance-system/uploads/users/";
//    public static String bitmap = "bitmap";
//    public static String serverTime;
//    public static String activitiname="";
//    public static String chatActivityName;
//    public static String fcm_token = "fcm_token";
//    public static String otheruserId = "";
//    public static String chatType;
//    public static String otheruserName;
//    public static String loginToken = "loginToken";
//    public static String token = "token";
//    public static String loginUserid = "loginUserid";


//    public static String language = "language";
//    public static String BitMapToString(Bitmap bitmap){
//        ByteArrayOutputStream baos=new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
//        byte [] b=baos.toByteArray();
//        String temp= Base64.encodeToString(b, Base64.DEFAULT);
//        return temp;
//    }
//
//    public static Bitmap StringToBitMap(String encodedString){
//        try {
//            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
//            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
//            return bitmap;
//        } catch(Exception e) {
//            e.getMessage();
//            return null;
//        }
//    }
//
//
//    public static void saveProfileData(Context con, ProfileDataLogin loginData) {
//        SharedPreferences mPrefs = con.getSharedPreferences("loginData",MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = mPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(loginData);
//        prefsEditor.putString("loginData", json);
//        prefsEditor.commit();
//
//    }
//
//    public static ProfileDataLogin getProfileData(Context con){
//        SharedPreferences mPrefs = con.getSharedPreferences("loginData",MODE_PRIVATE);
//        ProfileDataLogin loginData ;
//        Gson gson = new Gson();
//        String json = mPrefs.getString("loginData", "");
//        loginData = gson.fromJson(json, ProfileDataLogin.class);
//        return loginData;
//    }
//
//
//
//    public static void saveUserdat(Context con, SignUpResponse loginData) {
//        SharedPreferences mPrefs = con.getSharedPreferences("loginData",MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = mPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(loginData);
//        prefsEditor.putString("loginData", json);
//        prefsEditor.commit();
//
//    }
//
//    public static SignUpResponse getUserdata(Context con){
//        SharedPreferences mPrefs = con.getSharedPreferences("loginData",MODE_PRIVATE);
//        SignUpResponse loginData = new SignUpResponse();
//        Gson gson = new Gson();
//        String json = mPrefs.getString("loginData", "");
//        loginData = gson.fromJson(json, SignUpResponse.class);
//        return loginData;
//    }
//
//    private static final char[] banglaDigits = {'০','১','২','৩','৪','৫','৬','৭','৮','৯'};
//    private static final char[] engDigits = {'0','1','2','3','4','5','6','7','8','9'};
//
//    public  static final String  getDigitBanglaFromEnglish(String number){
//        if(number==null)
//            return new String("");
//        StringBuilder builder = new StringBuilder();
//        try{
//            for(int i =0;i<number.length();i++){
//                if(Character.isDigit(number.charAt(i))){
//                    if(((int)(number.charAt(i))-48)<=9){
//                        builder.append(banglaDigits[(int)(number.charAt(i))-48]);
//                    }else{
//                        builder.append(number.charAt(i));
//                    }
//                }else{
//                    builder.append(number.charAt(i));
//                }
//            }
//        }catch(Exception e){
//
//            return new String("");
//        }
//        return builder.toString();
//    }
//
//    public  static final String  getEngnumber(String number){
//        String replacedOne = number.replaceAll("০","0").replaceAll("১","1").replaceAll("২","2").replaceAll("৩","3").replaceAll("৪","4").replaceAll("৫","5").replaceAll("৬","6").replaceAll("৭","7").replaceAll("৮","8").replaceAll("৯","9");
//
//        return replacedOne;
//    }
//
//    public  static final String  getBngnumber(String number){
//        String replacedOne = number.replaceAll("0","০").replaceAll("1","১").replaceAll("2","২").replaceAll("3","৩").replaceAll("4","৪").replaceAll("5","৫").replaceAll("6","৬").replaceAll("7","৭").replaceAll("8","৮").replaceAll("9","৯");
//
//        return replacedOne;
//    }
//
//
//    public  static final String  getDigitEngFromBng(String number){
//        if(number==null)
//            return new String("");
//        StringBuilder builder = new StringBuilder();
//        try{
//            for(int i =0;i<number.length();i++){
//                if(Character.isDigit(number.charAt(i))){
//                    if(((int)(number.charAt(i))-48)<=9){
//                        builder.append(engDigits[(int)(number.charAt(i))-48]);
//                    }else{
//                        builder.append(number.charAt(i));
//                    }
//                }else{
//                    builder.append(number.charAt(i));
//                }
//            }
//        }catch(Exception e){
//
//            return new String("");
//        }
//        return builder.toString();
//    }
//
//
//    public static ArrayList<DrotoJogajogModel> loadData(Context context, ArrayList<DrotoJogajogModel> courseModalArrayList) {
//        // method to load arraylist from shared prefs
//        // initializing our shared prefs with name as
//        // shared preferences.
//
//        ArrayList<DrotoJogajogModel> list = new ArrayList<>();
//
//        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
//
//        // creating a variable for gson.
//        Gson gson = new Gson();
//
//        // below line is to get to string present from our
//        // shared prefs if not present setting it as null.
//        String json = sharedPreferences.getString("courses", null);
//
//        // below line is to get the type of our array list.
//        Type type = new TypeToken<ArrayList<DrotoJogajogModel>>() {}.getType();
//
//        // in below line we are getting data from gson
//        // and saving it to our array list
//        courseModalArrayList = gson.fromJson(json, type);
//
//        // checking below if the array list is empty or not
//        if (courseModalArrayList == null) {
//            // if the array list is empty
//            // creating a new array list.
//            courseModalArrayList = new ArrayList<>();
//        }else {
//            list = courseModalArrayList;
//        }
//         return list;
//
//    }
//
//    public static void saveData(Context context, ArrayList<DrotoJogajogModel> courseModalArrayList) {
//        // method for saving the data in array list.
//        // creating a variable for storing data in
//        // shared preferences.
//        SharedPreferences sharedPreferences = context.getSharedPreferences("shared preferences", MODE_PRIVATE);
//
//        // creating a variable for editor to
//        // store data in shared preferences.
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        // creating a new variable for gson.
//        Gson gson = new Gson();
//
//        // getting data from gson and storing it in a string.
//        String json = gson.toJson(courseModalArrayList);
//
//        // below line is to save data in shared
//        // prefs in the form of string.
//        editor.putString("courses", json);
//
//        // below line is to apply changes
//        // and save data in shared prefs.
//        editor.apply();
//
//        // after saving data we are displaying a toast message.
//        //Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
//    }
//
//
////    public  static final String bn2enNumber (String number){
////
////        String[] bn = {"০","১","২,৩","৪","৫","৬","৭","৮","৯"};
////        String[] en = {"0","1","2","3","4","5","6","7","8","9"};
////
////        String en_number = String.(banglaDigits, engDigits, number);
////
////        return en_number;
////    }
//
//
////    public static void alarmClockIn(Context con) {
////        Calendar calendar = Calendar.getInstance();
////        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
////
////        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(PersistData.getStringData(con,AppConstant.alarmClockInHour)));
////        calendar.set(Calendar.MINUTE, Integer.parseInt(PersistData.getStringData(con,AppConstant.alarmClockInMin)));
////        calendar.set(Calendar.SECOND, 0);
////
////        Intent intent1 = new Intent(con, AlarmReceiver.class);
////        PendingIntent pendingIntent = PendingIntent.getBroadcast(con, 1,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
////        AlarmManager am = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
////        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
////
////
//////        Intent myIntent = new Intent(con , AlarmReceiver.class);
//////        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
//////        PendingIntent pendingIntent = PendingIntent.getService(con, 1, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//////        Calendar calendar = Calendar.getInstance();
//////        calendar.set(Calendar.HOUR_OF_DAY, 8);
//////        calendar.set(Calendar.MINUTE, 45);
//////        calendar.set(Calendar.SECOND, 00);
//////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
////
////    }
////
////    public static void alarmClockInNext(Context con) {
////        Calendar calendar = Calendar.getInstance();
////        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
////
////        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(PersistData.getStringData(con,AppConstant.alarmClockInHour)));
////        calendar.set(Calendar.MINUTE, Integer.parseInt(PersistData.getStringData(con,AppConstant.alarmClockInMin)));
////        calendar.set(Calendar.SECOND, 0);
////
////        Intent intent1 = new Intent(con, AlarmReceiver.class);
////        PendingIntent pendingIntent = PendingIntent.getBroadcast(con, 1,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
////        AlarmManager am = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
////        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
////
////
//////        Intent myIntent = new Intent(con , AlarmReceiver.class);
//////        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
//////        PendingIntent pendingIntent = PendingIntent.getService(con, 1, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//////        Calendar calendar = Calendar.getInstance();
//////        calendar.set(Calendar.HOUR_OF_DAY, 8);
//////        calendar.set(Calendar.MINUTE, 45);
//////        calendar.set(Calendar.SECOND, 00);
//////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
////
////    }
////
////
////    public static void alarmClockOut(Context con) {
////
////        Calendar calendar = Calendar.getInstance();
////        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)-1);
////        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(PersistData.getStringData(con,AppConstant.alarmClockOutHour)));
////        calendar.set(Calendar.MINUTE, Integer.parseInt(PersistData.getStringData(con,AppConstant.alarmClockOutMin)));
////        calendar.set(Calendar.SECOND, 0);
////        Intent intent1 = new Intent(con, AlarmReceiver.class);
////        PendingIntent pendingIntent = PendingIntent.getBroadcast(con, 2,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
////        AlarmManager am = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
////        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
////
//////        Intent myIntent = new Intent(con , AlarmReceiver.class);
//////        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
//////        PendingIntent pendingIntent = PendingIntent.getService(con, 2, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//////        Calendar calendar = Calendar.getInstance();
//////        calendar.set(Calendar.HOUR_OF_DAY, 17);
//////        calendar.set(Calendar.MINUTE, 25);
//////        calendar.set(Calendar.SECOND, 00);
//////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
////    }
////
////    public static void alarmClockOutNext(Context con) {
////
////        Calendar calendar = Calendar.getInstance();
////        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
////        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(PersistData.getStringData(con,AppConstant.alarmClockOutHour)));
////        calendar.set(Calendar.MINUTE, Integer.parseInt(PersistData.getStringData(con,AppConstant.alarmClockOutMin)));
////        calendar.set(Calendar.SECOND, 0);
////        Intent intent1 = new Intent(con, AlarmReceiver.class);
////        PendingIntent pendingIntent = PendingIntent.getBroadcast(con, 2,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
////        AlarmManager am = (AlarmManager) con.getSystemService(con.ALARM_SERVICE);
////        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
////
//////        Intent myIntent = new Intent(con , AlarmReceiver.class);
//////        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
//////        PendingIntent pendingIntent = PendingIntent.getService(con, 2, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//////        Calendar calendar = Calendar.getInstance();
//////        calendar.set(Calendar.HOUR_OF_DAY, 17);
//////        calendar.set(Calendar.MINUTE, 25);
//////        calendar.set(Calendar.SECOND, 00);
//////        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24*60*60*1000, pendingIntent);
////    }


    //Home4Catagory list save
    public static void saveHome4Catagories(Context con, List<Category> locationInfos) {
        SharedPreferences mPrefs = con.getSharedPreferences("homecategory",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(locationInfos);
        prefsEditor.putString("homecategory", json);
        prefsEditor.commit();

    }

    //Home4Catagory get
    public static List<Category> getHome4Catagories(Context con){
        SharedPreferences sharedPreferences = con.getSharedPreferences("homecategory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<Category> arrayItems = new ArrayList<Category>();
        String serializedObject = sharedPreferences.getString("homecategory", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Category>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    //Catagory list save
    public static void saveCatagories(Context con, List<Category> locationInfos) {
        SharedPreferences mPrefs = con.getSharedPreferences("category",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(locationInfos);
        prefsEditor.putString("category", json);
        prefsEditor.commit();

    }

    //catagory get
    public static List<Category> getCatagories(Context con){
          SharedPreferences sharedPreferences = con.getSharedPreferences("category", Context.MODE_PRIVATE);
          SharedPreferences.Editor editor = sharedPreferences.edit();
        List<Category> arrayItems = new ArrayList<Category>();
        String serializedObject = sharedPreferences.getString("category", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Category>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }




    //subcat list save
    public static void saveSubCatagories(Context con, List<SubCategory> locationInfos) {
        SharedPreferences mPrefs = con.getSharedPreferences("subcategory",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(locationInfos);
        prefsEditor.putString("subcategory", json);
        prefsEditor.commit();

    }

    //subcatagory get
    public static List<SubCategory> getSubCatagories(Context con){
        SharedPreferences sharedPreferences = con.getSharedPreferences("subcategory", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<SubCategory> arrayItems = new ArrayList<SubCategory>();
        String serializedObject = sharedPreferences.getString("subcategory", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<SubCategory>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    //content list save
    public static void saveContent(Context con, List<Content> locationInfos) {
        SharedPreferences mPrefs = con.getSharedPreferences("content",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(locationInfos);
        prefsEditor.putString("content", json);
        prefsEditor.commit();

    }

    //content get
    public static List<Content> getContent(Context con){
        SharedPreferences sharedPreferences = con.getSharedPreferences("content", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<Content> arrayItems = new ArrayList<Content>();
        String serializedObject = sharedPreferences.getString("content", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Content>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }


    //generalsettings list save
    public static void saveGeneralsettings(Context con, List<GeneralSetting> locationInfos) {
        SharedPreferences mPrefs = con.getSharedPreferences("generalsettings",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(locationInfos);
        prefsEditor.putString("generalsettings", json);
        prefsEditor.commit();

    }

    //Generalsettings get
    public static List<GeneralSetting> getGeneralsettings(Context con){
        SharedPreferences sharedPreferences = con.getSharedPreferences("generalsettings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<GeneralSetting> arrayItems = new ArrayList<GeneralSetting>();
        String serializedObject = sharedPreferences.getString("generalsettings", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<GeneralSetting>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

    //save all image bitmap
    public static void saveAllImageBitmap(Context con, List<ImageBmpModel> locationInfos) {
        SharedPreferences mPrefs = con.getSharedPreferences("imageBitmap",MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(locationInfos);
        prefsEditor.putString("imageBitmap", json);
        prefsEditor.commit();

    }

    //get all image bitmap
    public static List<ImageBmpModel> getAllImageBitmap(Context con){
        SharedPreferences sharedPreferences = con.getSharedPreferences("imageBitmap", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        List<ImageBmpModel> arrayItems = new ArrayList<ImageBmpModel>();
        String serializedObject = sharedPreferences.getString("imageBitmap", null);
        if (serializedObject != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<ImageBmpModel>>(){}.getType();
            arrayItems = gson.fromJson(serializedObject, type);
        }
        return arrayItems;
    }

//
//    //savelistAlquranAlhadit
//    public static void saveAlQuranAldaithList(Context context, List<AlquranAlhadits> listAlquranAlhadit) {
//        SharedPreferences mPrefs = context.getSharedPreferences("listAlquranAlhadit",MODE_PRIVATE);
//        SharedPreferences.Editor prefsEditor = mPrefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(listAlquranAlhadit);
//        prefsEditor.putString("listAlquranAlhadit", json);
//        prefsEditor.commit();
//
//    }
//
//    //getlistAlquranAlhadit
//
//    public static List<AlquranAlhadits> getlistAlquranAlhadit(Context con){
//        SharedPreferences mPrefs = con.getSharedPreferences("listAlquranAlhadit",MODE_PRIVATE);
//        List<AlquranAlhadits> locationInfos = new ArrayList<>();
//        Gson gson = new Gson();
//        String json = mPrefs.getString("listAlquranAlhadit", "");
//        locationInfos = (List<AlquranAlhadits>) gson.fromJson(json, AlquranAlhadits.class);
//        return locationInfos;
//    }
//
////
////    public static JSONArray getlistAlquranAlhaditson(Context con){
////        SharedPreferences mPrefs = con.getSharedPreferences("listAlquranAlhadit",MODE_PRIVATE);
////        Gson gson = new Gson();
////        String json = mPrefs.getString("listAlquranAlhadit", "");
////        JSONArray jsonArray = new JSONArray();
////        jsonArray.put(json);
////        return jsonArray;
////    }
////
////
//////    private void getImageReto(){
//////
//////        OkHttpClient client = new OkHttpClient();
//////
//////        Request request = new Request.Builder()
//////                .url("http://wwwns.akamai.com/media_resources/globe_emea.png")
//////                .build();
//////
//////        client.newCall(request).enqueue(new Callback() {
//////            @Override
//////            public void onFailure(Call call, IOException e) {
//////
//////            }
//////
//////            @Override
//////            public void onResponse(Call call, Response response) throws IOException {
//////            }
//////
//////        });
//////
//////    }
//////
//////
//////    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
//////        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
//////        Canvas canvas = new Canvas(output);
//////
//////        final int color = 0xff424242;
//////        final Paint paint = new Paint();
//////        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
//////        final RectF rectF = new RectF(rect);
//////        final float roundPx = pixels;
//////
//////        paint.setAntiAlias(true);
//////        canvas.drawARGB(0, 0, 0, 0);
//////        paint.setColor(color);
//////        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
//////
//////        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//////        canvas.drawBitmap(bitmap, rect, rect, paint);
//////
//////        return output;
//////    }
////
////    public static void dilogDetails(Context context,String title,String content,String publisher,String publishDate,String viewcount, String likecount,String commentcount){
////
////        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
////        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////        dialog.setContentView(R.layout.details_common_post);
////        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
////        dialog.show();
////
////        TextView tvTitle = (TextView)dialog.findViewById(R.id.tvTitle);
////        final TextView tvDescription = (TextView)dialog.findViewById(R.id.tvDescription);
////        tvDescription.setMovementMethod(new ScrollingMovementMethod());
////
////        TextView tvPublisher = (TextView)dialog.findViewById(R.id.tvPublisher);
////        TextView tvPublishDate = (TextView)dialog.findViewById(R.id.tvPublishDate);
////        TextView tvCountView = (TextView)dialog.findViewById(R.id.tvCountView);
////        TextView tvCommentCount = (TextView)dialog.findViewById(R.id.tvCommentCount);
////
////        WebView webView = (WebView)dialog.findViewById(R.id.webView);
////        webView.getSettings().setBuiltInZoomControls(true);
////
////        String htmlString = "<div style=\"color:#069\"><b>"+"প্রশ্ন: "+title+"</b></div\n" + "<p>"+content+"</p>";
////        webView.loadData(htmlString, "text/html; charset=utf-8", "UTF-8");
////
////
////
////        tvTitle.setText(title);
////        tvDescription.setText(content);
////        tvPublisher.setText(publisher);
////        tvPublishDate.setText(publishDate);
////        tvCountView.setText(viewcount);
////        tvCommentCount.setText(commentcount);
////
////        final int[] font = {13};
////        tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, font[0]);
////        tvDescription.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////
////                if(font[0] == 13){
////                    font[0] +=1;
////                    tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, font[0]);
////                }
////
////                if(font[0] == 25){
////                    font[0] =-1;
////                    tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, font[0]);
////                }
////
////            }
////        });
////
////    }

    public static AllDataResponse alldata = new AllDataResponse();
    @NotNull
    public static String catName;
    public static String catID;
    @NotNull
    public static ArrayList<SubCategory> subCatList = new ArrayList<>();
    @NotNull
    public static ArrayList<Category> home4Cat = new ArrayList<>();
    @NotNull
    public static String subCatName;
    public static boolean isHome = false;
}
