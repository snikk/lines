package com.intuit.linesexperiment.util;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.intuit.linesexperiment.R;

import java.io.FileOutputStream;

/**
 * Created by jglenn2 on 8/8/16.
 */
public class ConvertUtil {
    public static void saveBitmap(final Context context, final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        final EditText et = (EditText) View.inflate(context, R.layout.select_filename, null);
        builder.setMessage("Choose a filename")
                .setView(et)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bitmap b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                        Canvas c = new Canvas(b);
                        v.draw(c);
                        String url = MediaStore.Images.Media.insertImage(context.getContentResolver(), b, et.getText().toString(), "Custom Image");
                        Log.d("Lines", "Save location = " + url);
                    }
                })
                .create().show();
    }
}
