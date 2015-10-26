package com.kevinquan.android.utils;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.CharacterStyle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utils for working with Spannable Strings
 *
 * @author Kevin Quan (kevin.quan@gmail.com)
 */
public class SpannableStringUtils {

    @SuppressWarnings("Unused")
    private static final String TAG = SpannableStringUtils.class.getSimpleName();

    public static void replace(SpannableStringBuilder source, int substitionIndex, CharSequence replacement, CharacterStyle... spans) {
        if (substitionIndex < 1 || TextUtils.isEmpty(source)) {
            return;
        }
        String substitutionString = "\\%"+substitionIndex+"\\$?";
        Pattern pattern = Pattern.compile(substitutionString);
        Matcher matcher = pattern.matcher(source.toString());
        if (matcher.find()) {
            source.delete(matcher.start(), matcher.end() + 1);
            source.insert(matcher.start(), replacement);
            if (spans != null && spans.length > 0) {
                for (CharacterStyle span : spans) {
                    source.setSpan(span, matcher.start(), matcher.start()+replacement.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                }
            }
        }
    }

}
