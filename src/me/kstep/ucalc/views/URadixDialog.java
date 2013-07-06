package me.kstep.ucalc.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import me.kstep.ucalc.activities.UCalcActivity;

public class URadixDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public void onClick(DialogInterface dialog, int id) {
        if (id > -1) {
            ((UCalcActivity) getActivity()).setRadix(indexToRadix(id));
        }
    }

    ArrayAdapter<String> adapter;

    final static String[] RADIX_NAMES = {
        /* 0 */ "Floating point",        // i == 0 => r = 0; r == 0 => i = 0
        /* 1 */ "Decimal (base 10)",     // i == 1 => r = 10; r == 10 => i = 1
        /* 2 */ "Hexadecimal (base 16)", // i == 2 => r = 16; r == 16; i = 2
        /* 3 */ "Octal (base 8)",        // i == 3 => r = 8; r == 8 => i = 3

        /* 4 */ "Binary (base 2)", // 3 < i && i < 10 => r = i - 2; 1 < r && r < 8 => i = r + 2
        /* 5 */ "Base 3",
        /* 6 */ "Base 4",
        /* 7 */ "Base 5",
        /* 8 */ "Base 6",
        /* 9 */ "Base 7",

       /* 10 */ "Base 9", // i == 10 => r = 9; r == 9 => i = 10

       /* 11 */ "Base 11", // 10 < i && i < 16 => r = i; 10 < r && r < 16 => i = r
       /* 12 */ "Base 12",
       /* 13 */ "Base 13",
       /* 14 */ "Base 14",
       /* 15 */ "Base 15",

       /* 16 */ "Base 17", // i > 15 => r = i + 1; r > 16 => i = r - 1
       /* 17 */ "Base 18",
       /* 18 */ "Base 19",

       /* 19 */ "Base 20",
       /* 20 */ "Base 21",
       /* 21 */ "Base 22",
       /* 22 */ "Base 23",
       /* 23 */ "Base 24",
       /* 24 */ "Base 25",
       /* 25 */ "Base 26",
       /* 26 */ "Base 27",
       /* 27 */ "Base 28",
       /* 28 */ "Base 29",

       /* 29 */ "Base 30",
       /* 30 */ "Base 31",
       /* 31 */ "Base 32",
       /* 32 */ "Base 33",
       /* 33 */ "Base 34",
       /* 34 */ "Base 35",
       /* 35 */ "Base 36",
    };

    private static int indexToRadix(int index) {
        return index == 0? 0:
            index == 1? 10:
            index == 2? 16:
            index == 3? 8:
            index == 10? 9:
            3 < index && index < 10? index - 2:
            10 < index && index < 16? index:
            index + 1;
    }

    private static int radixToIndex(int radix) {
        return radix == 0? 0:
            radix == 10? 1:
            radix == 16? 2:
            radix == 8? 3:
            radix == 9? 10:
            radix < 8? radix + 2:
            10 < radix && radix < 16? radix:
            radix - 1;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Choose radix");
        builder.setItems(RADIX_NAMES, this);
        builder.setNegativeButton("Cancel", this);

        return builder.create();
    }
}
