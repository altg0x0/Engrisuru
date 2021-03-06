package com.example.lord.engrisuru;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.math3.util.Pair;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;

import de.mfietz.jhyphenator.HyphenationPattern;
import de.mfietz.jhyphenator.Hyphenator;

/**
 * Created by lord on 23/03/18.
 */

public final class Utils {
    public static void toast(String data) {
        Toast toast = Toast.makeText(MainActivity.getAppContext(), data, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static List<Pair<String, Double>> zip(String[] words, Double[] probabilities) {
        List<Pair<String, Double>> pairs = new LinkedList<>();
        int length = words.length;
        for (int i = 0; i < length; i++) {
            String word = words[i];
            double prob = probabilities[i];
            pairs.add(new Pair<>(word, prob));
        }
        return pairs;
    }

    // Copied from https://stackoverflow.com/questions/124671/picking-a-random-element-from-a-set
    public static <E> E randomChoice(Collection<? extends E> coll) {
        Random rand = new Random();
        if (coll.size() == 0) {
            return null;
        }
        int index = rand.nextInt(coll.size());
        Iterator<? extends E> iterator = coll.iterator();
        for (int i = 0; i < index; i++) {
            iterator.next();
        }
        return iterator.next();
    }

    public static SharedPreferences getSharedPreferences() {
        return MainActivity.getAppContext().getSharedPreferences("engrisuru", Context.MODE_PRIVATE);
    }

    public final static class FS {
        public static boolean fileExistsInSandbox(String filename) {
            File file = new File(MainActivity.getAppContext().getFilesDir(), filename);
            return file.exists();
        }

        public static boolean writeToSandbox(String filename, String data) // Returns true if written successfully
        {
            File file = new File(MainActivity.getAppContext().getFilesDir(), filename);
            return writeStringToFile(file, data);
        }

        public static String readFile(File file) {
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
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }
            return null;
        }

        public static String readStringFromRes(String filename, Context appContext) {
            try {
                InputStream ins = appContext.getResources().openRawResource(
                        appContext.getResources().getIdentifier(filename,
                                "raw", appContext.getPackageName()));
                ByteArrayOutputStream result = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int length;
                while ((length = ins.read(buffer)) != -1) {
                    result.write(buffer, 0, length);
                }
                return result.toString("UTF-8");
            } catch (Exception ignored) {
            }
            return null;
        }


        public static String readFromSandbox(String filename) {
            File file = new File(MainActivity.getAppContext().getFilesDir(), filename);
            return readFile(file);
        }

        public static String readFileFromSD(String filename) {
            if (!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                return null;
            String basePath = getExternalStoragePath();
            File file = new File(basePath + filename);
            return readFile(file);
        }

        public static boolean writeStringToFile(File file, String data) {

            File dir = new File(file.getParent());
            if (!dir.exists() && !dir.mkdirs()) {
                return false;
            }
            try (FileOutputStream outputStreamWriter = new FileOutputStream(file)) {
                outputStreamWriter.write(data.getBytes());
                return true;
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
                return false;
            }
        }

        public static boolean writeStringToFileOnSD(String relativePath, String data) {
            String basePath = getExternalStoragePath();
            File file = new File(basePath + relativePath);
            return writeStringToFile(file, data);
        }

        public static boolean writeStreamToFile(InputStream is, File dest) {
            OutputStream os = null;
            try {
                os = new FileOutputStream(dest);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
                is.close();
                os.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                return false;
            }
            return true;
        }

        private static boolean copyFile(File source, File dest) {
            try {
                InputStream is = new FileInputStream(source);
                return writeStreamToFile(is, dest);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return false;
        }

        public static boolean copyFileToExternalStorage(File source, String relativePath) {
            File dest = new File(getExternalStoragePath() + "/" + relativePath);
            return copyFile(source, dest);
        }

        public static boolean copyFileFromExternalStorage(String relativePath, File dest) {
            File source = new File(getExternalStoragePath() + "/" + relativePath);
            return copyFile(source, dest);
        }

        public static String getExternalStoragePath() {
//            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
//            intent.addCategory(Intent.CATEGORY_DEFAULT);
//            MainActivity.getMainActivity().startActivityForResult(Intent.createChooser(intent, "Choose directory"), 9999);
//            Log.i("PATH", intent.getData().getPath());
            return Environment.getExternalStorageDirectory().getAbsolutePath() + "/engrisuru/";
        }

    }

    final static class StringUtils {
        //region HYPHENATION
        private static Pattern ignoredSymbols = Pattern.compile("[^\\p{Alpha}]"); // Normal java syntax is '''[^\p{IsAlphabetic}]''' but android is 'special'
        private static Pattern patternRU = Pattern.compile("[а-яА-Я]+");
        private static Pattern patternEN = Pattern.compile("[a-zA-Z]+");

        static private Hyphenator getHyphenatorByLanguage(Language lang) {
            switch (lang) {
                case RU:
                    return Hyphenator.getInstance(HyphenationPattern.RU);
                case EN:
                default:
                    return Hyphenator.getInstance(HyphenationPattern.CEN_US);
            }
        }

        static Language detectLanguage(String text) {
            text = ignoredSymbols.matcher(text).replaceAll("");
            if (patternEN.matcher(text).matches()) return Language.EN;
            if (patternRU.matcher(text).matches()) return Language.RU;
            return Language.UNKNOWN;
        }

        static String hyphenate(String text) {
            Language language = detectLanguage(text);
            return hyphenate(text, language);
        }

        static String hyphenate(String text, Language language) {
            return TextUtils.join("\u00AD", getHyphenatorByLanguage(language).hyphenate(text));
        }

        private enum Language {
            RU, EN, UNKNOWN
        }
        //endregion

    }

    public static class Converter {
        // Copied from https://stackoverflow.com/questions/15685282/elegant-way-to-deserialize-enumset-from-string
        public static <E extends Enum<E>> EnumSet<E> stringToEnumSet(String str, Class<E> eClass) {
            String[] arr = str.split(",");
            EnumSet<E> set = EnumSet.noneOf(eClass);
            for (String e : arr) set.add(E.valueOf(eClass, e.trim()));
            return set;
        }

        public static <E extends Enum<E>> String enumSetToString(EnumSet<E> enumSet) {
            String str = enumSet.toString();
            return str.substring(1, str.length() - 1).replace(" ", "");
        }


    }

}

