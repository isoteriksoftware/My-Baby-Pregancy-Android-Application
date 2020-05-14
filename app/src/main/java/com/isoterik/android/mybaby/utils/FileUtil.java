package com.isoterik.android.mybaby.utils;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class FileUtil
{
    public static final String KICKS_DIRECTORY = "kicks/";
    public static final String CONTRACTIONS_DIRECTORY = "contractions/";
    public static final String EXTENSION = ".txt";
    public static final String LINE_DATA_SEPARATOR = "#";

    public static File getDir (Context context, String path)
    {
        File dir = new File(context.getFilesDir(), path);
        if (!dir.exists())
            dir.mkdirs();

        return dir;
    }

    public static File newFile (Context context, String directory, String baseFileName)
    {
        File file = new File(getDir(context, directory), baseFileName + EXTENSION);
        int index = 1;
        while (file.exists())
        {
            String newName = baseFileName + index + EXTENSION;
            file = new File(getDir(context, directory), newName);
            index++;
        }

        try {
            file.createNewFile();
        } catch (IOException e) {
            Log.e(FileUtil.class.getSimpleName(), "Error while creating file: " + e.getMessage() + "; " + file.getAbsolutePath());
            return null;
        }

        return file;
    }

    public static File newKickFile (Context context)
    {
        return newFile(context, KICKS_DIRECTORY, "kick_data");
    }

    public static File newContractionFile (Context context)
    {
        return newFile(context, CONTRACTIONS_DIRECTORY, "contraction_data");
    }

    public static boolean writeKickData (File file, String date, String startTime, String duration, int kicks)
    {
        try
        {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(date);
            printWriter.println(startTime);
            printWriter.println(duration);
            printWriter.println(kicks);
            printWriter.flush();
            printWriter.close();
            return !printWriter.checkError();
        } catch (IOException e)
        {
            Log.e(FileUtil.class.getSimpleName(), "Error while saving kick file: " + e.getMessage() + "; " + file.getAbsolutePath());
            return false;
        }
    }

    public static boolean writeContractionData (File file, String date, String startTime, String durations,
                                                String restDurations, int contractions)
    {
        try
        {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.println(date);
            printWriter.println(startTime);
            printWriter.println(durations);
            printWriter.println(contractions);
            printWriter.println(restDurations);
            printWriter.flush();
            printWriter.close();
            return !printWriter.checkError();
        } catch (IOException e)
        {
            Log.e(FileUtil.class.getSimpleName(), "Error while saving contraction file: " + e.getMessage() + "; " + file.getAbsolutePath());
            return false;
        }
    }

    public static String[] readKicksData (Context context)
    {
        String[] contents = null;

        try
        {
            File dir = getDir(context, KICKS_DIRECTORY);
            File[] files = dir.listFiles();

            if (files.length == 0)
                return new String[0];

            contents = new String[files.length];

            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuffer stringBuffer = new StringBuffer();
                String line = null;

                while ((line = reader.readLine()) != null)
                    stringBuffer.append(line).append(LINE_DATA_SEPARATOR);

                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                contents[i] = stringBuffer.toString();
            }

            return contents;
        } catch (IOException e)
        {
            Log.e(FileUtil.class.getSimpleName(), "Error while reading kicks directory: " + e.getMessage());
            return null;
        }
    }

    public static String[] readContractionsData (Context context)
    {
        String[] contents = null;

        try
        {
            File dir = getDir(context, CONTRACTIONS_DIRECTORY);
            File[] files = dir.listFiles();

            if (files.length == 0)
                return new String[0];

            contents = new String[files.length];

            for (int i = 0; i < files.length; i++)
            {
                File file = files[i];
                BufferedReader reader = new BufferedReader(new FileReader(file));
                StringBuffer stringBuffer = new StringBuffer();
                String line = null;

                while ((line = reader.readLine()) != null)
                    stringBuffer.append(line).append(LINE_DATA_SEPARATOR);

                stringBuffer.deleteCharAt(stringBuffer.length() - 1);
                contents[i] = stringBuffer.toString();
            }

            return contents;
        } catch (IOException e)
        {
            Log.e(FileUtil.class.getSimpleName(), "Error while reading contractions directory: " + e.getMessage());
            return null;
        }
    }

    public static String[] splitData (String data)
    { return data.split(LINE_DATA_SEPARATOR); }

    public static void clearKicksData (Context context)
    {
        clearDirectory(getDir(context, KICKS_DIRECTORY));
    }

    public static void clearContractionsData (Context context)
    {
        clearDirectory(getDir(context, CONTRACTIONS_DIRECTORY));
    }

    public static void reset(Context context)
    {
        clearKicksData(context);
        clearContractionsData(context);
    }

    public static void clearDirectory (File directory)
    {
        for (File file : directory.listFiles())
            file.delete();
    }
}

























