package com.chat.base.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.animation.DecelerateInterpolator;

import com.chat.base.WKBaseApplication;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;

public class AndroidUtilities {
    public static DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    private static final Hashtable<String, Typeface> typefaceCache = new Hashtable<>();
    public static DisplayMetrics displayMetrics = new DisplayMetrics();
    public static float density = 1;
    public static boolean isRTL = false;
    public static final RectF rectTmp2 = new RectF();
    public static final RectF rectTmp = new RectF();
    public static Point displaySize = new Point();
    public static float screenRefreshRate = 60;
    public static Paint chat_actionBackgroundGradientDarkenPaint;

    private static final boolean useSoftLight = Build.VERSION.SDK_INT >= 29;

    public static void setDensity(float density1) {
        density = density1;
        chat_actionBackgroundGradientDarkenPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chat_actionBackgroundGradientDarkenPaint.setColor(0x2a000000);
    }

    public static int dp(float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(density * value);
    }

    public static float dpf2(float value) {
        if (value == 0) {
            return 0;
        }
        return density * value;
    }

    public static float px2sp(float pxVal) {
        return (pxVal / WKBaseApplication.getInstance().getContext().getResources().getDisplayMetrics().scaledDensity);
    }

    public static int getScreenWidth() {
        Resources resources = WKBaseApplication.getInstance().getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();

        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        Resources resources = WKBaseApplication.getInstance().getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * sp转px
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal,
                context.getResources().getDisplayMetrics());
    }

    public static void cancelRunOnUIThread(Runnable runnable) {
        if (WKBaseApplication.applicationHandler == null) {
            return;
        }
        WKBaseApplication.applicationHandler.removeCallbacks(runnable);
    }

    public static void runOnUIThread(Runnable runnable) {
        runOnUIThread(runnable, 0);
    }

    public static void runOnUIThread(Runnable runnable, long delay) {
        if (WKBaseApplication.applicationHandler == null) {
            return;
        }
        if (delay == 0) {
            WKBaseApplication.applicationHandler.post(runnable);
        } else {
            WKBaseApplication.applicationHandler.postDelayed(runnable, delay);
        }
    }

    public static final String[] numbersSignatureArray = {"", "K", "M", "G", "T", "P"};

    public static String formatWholeNumber(int v, int dif) {
        if (v == 0) {
            return "0";
        }
        float num_ = v;
        int count = 0;
        if (dif == 0) dif = v;
        if (dif < 1000) {
            return AndroidUtilities.formatCount(v);
        }
        while (dif >= 1000 && count < numbersSignatureArray.length - 1) {
            dif /= 1000;
            num_ /= 1000;
            count++;
        }
        if (num_ < 0.1) {
            return "0";
        } else {
            if ((num_ * 10) == (int) (num_ * 10)) {
                return String.format(Locale.ENGLISH, "%s%s", AndroidUtilities.formatCount((int) num_), numbersSignatureArray[count]);
            } else {
                return String.format(Locale.ENGLISH, "%.1f%s", (int) (num_ * 10) / 10f, numbersSignatureArray[count]);
            }
        }
    }


    public static String formatCount(int count) {
        if (count < 1000) return Integer.toString(count);

        ArrayList<String> strings = new ArrayList<>();
        while (count != 0) {
            int mod = count % 1000;
            count /= 1000;
            if (count > 0) {
                strings.add(String.format(Locale.ENGLISH, "%03d", mod));
            } else {
                strings.add(Integer.toString(mod));
            }
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = strings.size() - 1; i >= 0; i--) {
            stringBuilder.append(strings.get(i));
            if (i != 0) {
                stringBuilder.append(",");
            }
        }

        return stringBuilder.toString();
    }

    public static Typeface getTypeface(String assetPath) {
        synchronized (typefaceCache) {
            if (!typefaceCache.containsKey(assetPath)) {
                try {
                    Typeface t;
                    if (Build.VERSION.SDK_INT >= 26) {
                        Typeface.Builder builder = new Typeface.Builder(WKBaseApplication.getInstance().getContext().getAssets(), assetPath);
                        if (assetPath.contains("medium")) {
                            builder.setWeight(700);
                        }
                        if (assetPath.contains("italic")) {
                            builder.setItalic(true);
                        }
                        t = builder.build();
                    } else {
                        t = Typeface.createFromAsset(WKBaseApplication.getInstance().getContext().getAssets(), assetPath);
                    }
                    typefaceCache.put(assetPath, t);
                } catch (Exception e) {
                    return null;
                }
            }
            return typefaceCache.get(assetPath);
        }
    }

    public static int getShadowHeight() {
        if (density >= 4.0f) {
            return 3;
        } else if (density >= 2.0f) {
            return 2;
        } else {
            return 1;
        }
    }

    public static boolean isDark(int color1, int color2, int color3, int color4) {
        int averageColor = AndroidUtilities.getAverageColor(color1, color2);
        if (color3 != 0) {
            averageColor = AndroidUtilities.getAverageColor(averageColor, color3);
        }
        if (color4 != 0) {
            averageColor = AndroidUtilities.getAverageColor(averageColor, color4);
        }
        float[] hsb = AndroidUtilities.RGBtoHSB(Color.red(averageColor), Color.green(averageColor), Color.blue(averageColor));
        return hsb[2] < 0.3f;
    }

    public static int getPatternColor(int color1, int color2, int color3, int color4) {
        if (isDark(color1, color2, color3, color4)) {
            return !useSoftLight ? 0x7fffffff : 0xffffffff;
        } else {
//            if (!useSoftLight) {
//                int averageColor = AndroidUtilities.getAverageColor(color3, AndroidUtilities.getAverageColor(color1, color2));
//                if (color4 != 0) {
//                    averageColor = AndroidUtilities.getAverageColor(color4, averageColor);
//                }
//                Log.e("返回黑色111","-->");
//                return (AndroidUtilities.getPatternColor(averageColor, true) & 0x00ffffff) | 0x64000000;
//            } else {
//                Log.e("返回黑色333","-->");
//                return 0xff000000;
//            }

            int averageColor = AndroidUtilities.getAverageColor(color3, AndroidUtilities.getAverageColor(color1, color2));
            if (color4 != 0) {
                averageColor = AndroidUtilities.getAverageColor(color4, averageColor);
            }
            Log.e("返回黑色111", "-->");
            return (AndroidUtilities.getPatternColor(averageColor, true) & 0x00ffffff) | 0x64000000;

        }
    }

    public static int getPatternColor(int color) {
        return getPatternColor(color, false);
    }

    public static float[] RGBtoHSB(int r, int g, int b) {
        float hue, saturation, brightness;
        float[] hsbvals = new float[3];
        int cmax = Math.max(r, g);
        if (b > cmax) {
            cmax = b;
        }
        int cmin = Math.min(r, g);
        if (b < cmin) {
            cmin = b;
        }

        brightness = ((float) cmax) / 255.0f;
        if (cmax != 0) {
            saturation = ((float) (cmax - cmin)) / ((float) cmax);
        } else {
            saturation = 0;
        }
        if (saturation == 0) {
            hue = 0;
        } else {
            float redc = ((float) (cmax - r)) / ((float) (cmax - cmin));
            float greenc = ((float) (cmax - g)) / ((float) (cmax - cmin));
            float bluec = ((float) (cmax - b)) / ((float) (cmax - cmin));
            if (r == cmax) {
                hue = bluec - greenc;
            } else if (g == cmax) {
                hue = 2.0f + redc - bluec;
            } else {
                hue = 4.0f + greenc - redc;
            }
            hue = hue / 6.0f;
            if (hue < 0) {
                hue = hue + 1.0f;
            }
        }
        hsbvals[0] = hue;
        hsbvals[1] = saturation;
        hsbvals[2] = brightness;
        return hsbvals;
    }

    public static int getPatternColor(int color, boolean alwaysDark) {
        float[] hsb = RGBtoHSB(Color.red(color), Color.green(color), Color.blue(color));
        if (hsb[1] > 0.0f || (hsb[2] < 1.0f && hsb[2] > 0.0f)) {
            hsb[1] = Math.min(1.0f, hsb[1] + (alwaysDark ? 0.15f : 0.05f) + 0.1f * (1.0f - hsb[1]));
        }
        if (alwaysDark || hsb[2] > 0.5f) {
            hsb[2] = Math.max(0.0f, hsb[2] * 0.65f);
        } else {
            hsb[2] = Math.max(0.0f, Math.min(1.0f, 1.0f - hsb[2] * 0.65f));
        }
        return HSBtoRGB(hsb[0], hsb[1], hsb[2]) & (alwaysDark ? 0x99ffffff : 0x66ffffff);
    }

    public static int HSBtoRGB(float hue, float saturation, float brightness) {
        int r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = (int) (brightness * 255.0f + 0.5f);
        } else {
            float h = (hue - (float) Math.floor(hue)) * 6.0f;
            float f = h - (float) java.lang.Math.floor(h);
            float p = brightness * (1.0f - saturation);
            float q = brightness * (1.0f - saturation * f);
            float t = brightness * (1.0f - (saturation * (1.0f - f)));
            switch ((int) h) {
                case 0:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (t * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 1:
                    r = (int) (q * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (p * 255.0f + 0.5f);
                    break;
                case 2:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (brightness * 255.0f + 0.5f);
                    b = (int) (t * 255.0f + 0.5f);
                    break;
                case 3:
                    r = (int) (p * 255.0f + 0.5f);
                    g = (int) (q * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 4:
                    r = (int) (t * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (brightness * 255.0f + 0.5f);
                    break;
                case 5:
                    r = (int) (brightness * 255.0f + 0.5f);
                    g = (int) (p * 255.0f + 0.5f);
                    b = (int) (q * 255.0f + 0.5f);
                    break;
            }
        }
        return 0xff000000 | ((r & 0xff) << 16) | ((g & 0xff) << 8) | (b & 0xff);
    }

    public static int getAverageColor(int color1, int color2) {
        int r1 = Color.red(color1);
        int r2 = Color.red(color2);
        int g1 = Color.green(color1);
        int g2 = Color.green(color2);
        int b1 = Color.blue(color1);
        int b2 = Color.blue(color2);
        return Color.argb(255, (r1 / 2 + r2 / 2), (g1 / 2 + g2 / 2), (b1 / 2 + b2 / 2));
    }
}