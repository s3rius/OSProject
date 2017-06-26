package com.example.s3rius.surveyclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Looper;
import android.os.StatFs;
import android.util.Log;

import com.example.s3rius.surveyclient.fragments.surveypac.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.Locale;

class ExeptionsHandler implements Thread.UncaughtExceptionHandler {
    private static Context context1;
    private Context context;
    private Activity activity;

    ExeptionsHandler(Context context) {
        this.context = context;
        context1 = context;
        activity = (Activity) context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        StringBuilder message = new StringBuilder();
        Date date = new Date();
        message.append("Error Report collected on : ")
                .append(date.toString())
                .append('\n').append('\n');
        User savedUser = null;
        SharedPreferences preferences = activity.getPreferences(Context.MODE_PRIVATE);
        String Juser = preferences.getString("saved_user", null);
        if (Juser != null) {
            try {
                savedUser = new ObjectMapper().readValue(Juser, User.class);
            } catch (IOException r) {
                r.printStackTrace();
            }
        }
        message.append("login : ").append(savedUser.getLogin())
                .append('\n')
                .append('\n');
        message.append("Cause: ").append(e.getCause()).append("\n");
        addInformation(message);
        message.append("\n").append("\n");
        message.append("Stack:\n");
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        message.append(result.toString());
        message.append("################################################################");
        printWriter.close();
        sendErrorMail(message);
    }

    private long getAvailableInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    private long getTotalInternalMemorySize(StatFs stat) {
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    private void addInformation(StringBuilder message) {
        message.append("Locale: ").append(Locale.getDefault()).append('\n');
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi;
            pi = pm.getPackageInfo(context.getPackageName(), 0);

            message.append("Version: ").append(pi.versionName).append('\n');
            message.append("Package: ").append(pi.packageName).append('\n');
        } catch (Exception e) {
            Log.e("CustomExceptionHandler", "Error", e);
            message.append("Could not get Version information for ").append(
                    context.getPackageName());
        }
        message.append("Phone Model: ").append(android.os.Build.MODEL)
                .append('\n');
        message.append("Android Version: ")
                .append(android.os.Build.VERSION.RELEASE).append('\n');
        message.append("Board: ").append(android.os.Build.BOARD).append('\n');
        message.append("Brand: ").append(android.os.Build.BRAND).append('\n');
        message.append("Device: ").append(android.os.Build.DEVICE).append('\n');
        message.append("Host: ").append(android.os.Build.HOST).append('\n');
        message.append("ID: ").append(android.os.Build.ID).append('\n');
        message.append("Model: ").append(android.os.Build.MODEL).append('\n');
        message.append("Product: ").append(android.os.Build.PRODUCT)
                .append('\n');
        message.append("Type: ").append(android.os.Build.TYPE).append('\n');
        StatFs stat = getStatFs();
        message.append("Total Internal memory: ")
                .append(getTotalInternalMemorySize(stat)).append('\n');
        message.append("Available Internal memory: ")
                .append(getAvailableInternalMemorySize(stat)).append('\n');
    }

    private StatFs getStatFs() {
        File path = Environment.getDataDirectory();
        return new StatFs(path.getPath());
    }

    private void sendErrorMail(final StringBuilder errorContent) {
//        new Thread() {
//            @Override
//            public void run() {
//                StringBuilder errorBody = new StringBuilder();
//                errorBody.append("Бля,  опять всё пиздой пошло. За работу!")
//                        .append("\n").append("\n");
//                errorBody.append(errorContent);
//                AsyncHttpClient client = new AsyncHttpClient();
//                RequestParams params = new RequestParams();
//                params.put("message", errorBody.toString());
//                client.post("http://46.0.193.227:8080/survey/client/exeption", params, new AsyncHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        Toast.makeText(context, "Thanks, for your report", Toast.LENGTH_SHORT).show();
//                        System.exit(0);
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                        Toast.makeText(context, "Error not sent", Toast.LENGTH_SHORT).show();
//                        System.exit(0);
//                    }
//                });
//            }
//        }.start();
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                builder.setTitle(context.getString(R.string.sorry));
                builder.create();
                builder.setNegativeButton(context.getString(R.string.cancel),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                System.exit(10);
                            }
                        });
                builder.setPositiveButton(context.getString(R.string.report),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
//                                Intent sendIntent = new Intent(
//                                        Intent.ACTION_SEND);
//                                String subject = "Your App crashed! Fix it!";
                                StringBuilder errorBody = new StringBuilder();
                                errorBody.append("Приложение упало, иди работай!")
                                        .append("\n").append("\n");
                                errorBody.append('\n').append('\n');
                                errorBody.append(errorContent).append('\n')
                                        .append('\n');
//                                sendIntent.setType("message/rfc822");
//                                sendIntent.putExtra(Intent.EXTRA_EMAIL,
//                                        new String[]{"win10@list.ru"});
//                                sendIntent.putExtra(Intent.EXTRA_TEXT,
//                                        errorBody.toString());
//                                sendIntent.putExtra(Intent.EXTRA_SUBJECT,
//                                        subject);
//                                sendIntent.setType("message/rfc822");
                                // sendIntent.setType("text/plain");
                                Intent intent = new Intent(context1, ReportSenderActivity.class);
                                intent.putExtra("ErrorMessage", errorBody.toString());
                                context1.startActivity(intent);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(10);
                            }
                        });
                builder.setMessage(context.getString(R.string.app_has_crash));
                builder.show();
                Looper.loop();
            }
        }.start();
    }
}


