package me.kstep.ucalc.util;

import android.text.Html;
import android.text.Spanned;

public class TextUtil {
    public static Spanned unicodeToHtml(CharSequence value) {
        return Html.fromHtml(
               value.toString()
                    .replace("\n", "<br />")
                    .replace("⁰", "<sup><small>0</small></sup>")
                    .replace("¹", "<sup><small>1</small></sup>")
                    .replace("²", "<sup><small>2</small></sup>")
                    .replace("³", "<sup><small>3</small></sup>")
                    .replace("⁴", "<sup><small>4</small></sup>")
                    .replace("⁵", "<sup><small>5</small></sup>")
                    .replace("⁶", "<sup><small>6</small></sup>")
                    .replace("⁷", "<sup><small>7</small></sup>")
                    .replace("⁸", "<sup><small>8</small></sup>")
                    .replace("⁹", "<sup><small>9</small></sup>")
                    .replace("¯", "<sup><small>−</small></sup>")
                    .replace("</small></sup><sup><small>", ""));
    }

    /**
     * This piece of code converts integer numbers into superscript
     * representation to autoformat unit name. It uses Unicode
     * superscript numbers.
     */
    public static String superscriptInt(int power) {
        StringBuilder name = new StringBuilder();

        /**
         * We loop through single digits of initial number
         * and convert them one by one.
         */
        for (int p = Math.abs(power); p > 0; p /= 10) {
            switch (p % 10) {
            case 0: name.append('⁰'); break;
            case 1: name.append('¹'); break;
            case 2: name.append('²'); break;
            case 3: name.append('³'); break;
            case 4: name.append('⁴'); break;
            case 5: name.append('⁵'); break;
            case 6: name.append('⁶'); break;
            case 7: name.append('⁷'); break;
            case 8: name.append('⁸'); break;
            case 9: name.append('⁹'); break;
            }
        }

        /**
         * Here we append superscript minus in case initial number is negative.
         */
        if (power < 0) {
            name.append('¯');
        }

        /**
         * We built number from tail to head, so we get its reversed string representation.
         * We reverse the result string again to get normal representation.
         */
        return name.reverse().toString();
    }
}
