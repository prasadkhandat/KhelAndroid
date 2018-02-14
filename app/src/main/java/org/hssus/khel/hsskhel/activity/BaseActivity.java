package org.hssus.khel.hsskhel.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

/**
 * Created by prasadkhandat on 1/3/18.
 */

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog dialog = null;
    private static final String TAG = BaseActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        try {
            dismissProgressDialog();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        super.onDestroy();
    }

    public Toast showToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }

    public void dismissProgressDialog() {
        try {
            if ((dialog != null) && (dialog.isShowing())) dialog.dismiss();
        } catch (Exception e) {
            Log.e(TAG, e.toString());

        }
    }

    public void showProgressDialog(String message, Boolean... setCancelable) {
        try {
            if (dialog != null) {
                dialog.dismiss();
            }
        } catch (Exception ex) {
            Log.d(TAG, ex.getMessage());
            ex.printStackTrace();
        }
        if (dialog == null) dialog = new ProgressDialog(this);

        dialog.setMessage(message);
        try {
            if (setCancelable != null && setCancelable.length > 0) {
                dialog.setCancelable(setCancelable[0]);
            } else {
                dialog.setCancelable(true);
            }

        } catch (Exception ex1) {
            dialog.setCancelable(true);
            ex1.printStackTrace();
        }
        try {
            if (!dialog.isShowing()) dialog.show();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    public void showProgressDialog(String message, String title, Boolean... setCancelable) {
        if (dialog == null) dialog = new ProgressDialog(this);
        dialog.setMessage(message);
        dialog.setTitle(title);
        try {
            if (setCancelable != null && setCancelable.length > 0) {
                dialog.setCancelable(setCancelable[0]);
            } else {
                dialog.setCancelable(true);
            }

        } catch (Exception ex1) {
            dialog.setCancelable(true);
        }
        try {
            if (!dialog.isShowing()) dialog.show();
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cn = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        return nf != null && nf.isConnected();
    }

    public void showAlert(String title,
                          String alertMsg,
                          String positiveActionName,
                          DialogInterface.OnClickListener positiveAction,
                          String negativeActionName,
                          DialogInterface.OnClickListener negativeAction,
                          boolean dual,
                          boolean cancelable) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(alertMsg);
        builder.setCancelable(cancelable);

        if (positiveActionName != null && positiveAction != null) {
            builder.setPositiveButton(positiveActionName, positiveAction);
        } else {

            if (positiveActionName != null) {
                builder.setPositiveButton(positiveActionName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            } else {
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        }

        if (dual) {
            if (negativeActionName != null && negativeAction != null) {
                builder.setNegativeButton(negativeActionName, negativeAction);
            } else {
                if (negativeActionName != null) {
                    builder.setNegativeButton(negativeActionName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                } else {
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
            }
        }

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void showAlertWithoutTitle(
            String alertMsg,
            String positiveActionName,
            DialogInterface.OnClickListener positiveAction,
            String negativeActionName,
            DialogInterface.OnClickListener negativeAction,
            boolean dual,
            boolean cancelable) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(alertMsg);
        builder.setCancelable(cancelable);


        if (positiveActionName != null && positiveAction != null) {
            builder.setPositiveButton(positiveActionName, positiveAction);
        } else {

            if (positiveActionName != null) {
                builder.setPositiveButton(positiveActionName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
            } else {
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
            }
        }

        if (dual) {
            if (negativeActionName != null && negativeAction != null) {
                builder.setNegativeButton(negativeActionName, negativeAction);
            } else {
                if (negativeActionName != null) {
                    builder.setNegativeButton(negativeActionName, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                } else {
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                }
            }
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
