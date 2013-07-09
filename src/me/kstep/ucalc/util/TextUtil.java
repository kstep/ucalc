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

                    .replace("₀", "<sub><small>0</small></sub>")
                    .replace("₁", "<sub><small>1</small></sub>")
                    .replace("₂", "<sub><small>2</small></sub>")
                    .replace("₃", "<sub><small>3</small></sub>")
                    .replace("₄", "<sub><small>4</small></sub>")
                    .replace("₅", "<sub><small>5</small></sub>")
                    .replace("₆", "<sub><small>6</small></sub>")
                    .replace("₇", "<sub><small>7</small></sub>")
                    .replace("₈", "<sub><small>8</small></sub>")
                    .replace("₉", "<sub><small>9</small></sub>")
                    .replace("₋", "<sub><small>−</small></sub>")

                    .replace("</small></sup><sup><small>", ""));
    }

    private static final char[] SUPERSCRIPT_DIGITS = new char[]{'⁰', '¹', '²', '³', '⁴', '⁵', '⁶', '⁷', '⁸', '⁹', '¯'};
    private static final char[] SUBSCRIPT_DIGITS = new char[]{'₀', '₁', '₂', '₃', '₄', '₅', '₆', '₇', '₈', '₉', '₋'};

    /**
     * This piece of code converts integer numbers into superscript
     * representation to autoformat unit name. It uses Unicode
     * superscript numbers.
     */
    public static String superscriptInt(int value) {
        return translateInt(value, SUPERSCRIPT_DIGITS);
    }

    public static String subscriptInt(int value) {
        return translateInt(value, SUBSCRIPT_DIGITS);
    }

    private static String translateInt(int value, char[] alphabet) {
        int base = alphabet.length - 1;
        StringBuilder name = new StringBuilder();

        /**
         * We loop through single digits of initial number
         * and convert them one by one.
         */
        for (int p = Math.abs(value); p > 0; p /= base) {
            name.append(alphabet[p % base]);
        }

        /**
         * Here we append superscript minus in case initial number is negative.
         */
        if (value < 0) {
            name.append(alphabet[base]);
        }

        /**
         * We built number from tail to head, so we get its reversed string representation.
         * We reverse the result string again to get normal representation.
         */
        return name.reverse().toString();
    }
}
