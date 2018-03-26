package com.example.lord.engrisuru;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import org.apache.commons.math3.util.Pair;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by lord on 23/03/18.
 */

final class Utils {
    static class FS {
        static boolean fileExists(String filename) {
            File file = new File(MainActivity.getAppContext().getFilesDir(), filename);
            return  file.exists();
        }

        static void writeToSandbox(String filename, String data) {
            File file = new File(MainActivity.getAppContext().getFilesDir(), filename);
            try (FileOutputStream outputStreamWriter = new FileOutputStream(file)) {
                outputStreamWriter.write(data.getBytes());
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
        }

        static String readFromSandbox1(String filename) {
            File file = new File(MainActivity.getAppContext().getFilesDir(), filename);
            try (final InputStream inputStream = new FileInputStream(file)) {

                final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                final StringBuilder stringBuilder = new StringBuilder();
                boolean done = false;
                while (!done) {
                    final String line = reader.readLine();
                    done = (line == null);
                    if (line != null) {
                        stringBuilder.append(line);
                    }
                }
                reader.close();
                inputStream.close();
                return stringBuilder.toString();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
            return null;
        }

        private static String readFile(File file) {
            try (final InputStream inputStream = new FileInputStream(file)) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                final StringBuilder stringBuilder = new StringBuilder();
                boolean done = false;
                while (!done) {
                    final String line = reader.readLine();
                    done = (line == null);
                    if (line != null) {
                        stringBuilder.append(line);
                    }
                }
                reader.close();
                inputStream.close();
                return stringBuilder.toString();
            }
            catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
            return null;
        }

        static String readFromSandbox(String filename) {
            File file = new File(MainActivity.getAppContext().getFilesDir(), filename);
            return readFile(file);
        }

        static String readFileFromSD(String filename) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                return null;
            String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/engrisuru/";
            File file = new File(basePath + filename);
            return readFile(file);
        }
    }
    static void toast(String data)
    {
        Toast toast = Toast.makeText(MainActivity.getAppContext(), data, Toast.LENGTH_SHORT);
        toast.show();
    }
    static List<Pair<String, Double>> zip(String[] words, Double[] probabilities)
    {
        List<Pair<String, Double>> pairs = new LinkedList<>();
        int length = words.length;
        for (int i = 0; i < length; i++) {
            String word = words[i];
            double prob = probabilities[i];
            pairs.add(new Pair<>(word, prob));
        }
        return pairs;
    }

}

