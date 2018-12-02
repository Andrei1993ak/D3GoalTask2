package com.github.andrei1993ak.mentoring.task2.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class FileUtils {

    public static String readFromFile(final File pFile) {
        try {
            final InputStream inputStream = new FileInputStream(pFile.getAbsolutePath());

            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            final BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String receiveString;
            final StringBuilder stringBuilder = new StringBuilder();

            while ((receiveString = bufferedReader.readLine()) != null) {
                stringBuilder.append(receiveString);
            }

            inputStream.close();

            return stringBuilder.toString();
        } catch (final FileNotFoundException e) {
            return TextUtils.Constants.EMPTY;
        } catch (final IOException e) {
            return TextUtils.Constants.EMPTY;
        }
    }


    public static void writeToFile(final File pFile, final String pSource) {
        try {
            final OutputStream outputStream = new FileOutputStream(pFile, false);
            final OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            outputStreamWriter.write(pSource);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            outputStream.close();
        } catch (final IOException e) {
            Log.e(FileUtils.class.getSimpleName(), e.getLocalizedMessage(), e);
        }
    }
}
