package me.kstep.ucalc.views;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import me.kstep.ucalc.UCalcActivity;

import me.kstep.ucalc.R;

public class UModesFragment extends ListFragment {

    ArrayAdapter<String> adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);

        final UCalcActivity activity = (UCalcActivity) getActivity();

        final String[] modes = {
            "Floating point",
            "Decimal (base 10)",
            "Hexadecimal (base 16)",
            "Octal (base 8)",
            "Binary (base 2)",

            "Base 3",
            "Base 4",
            "Base 5",
            "Base 6",
            "Base 7",
            "Base 9",
            "Base 11",
            "Base 12",
            "Base 13",
            "Base 14",
            "Base 15",
            "Base 17",
            "Base 18",
            "Base 19",

            "Base 20",
            "Base 21",
            "Base 22",
            "Base 23",
            "Base 24",
            "Base 25",
            "Base 26",
            "Base 27",
            "Base 28",
            "Base 29",

            "Base 30",
            "Base 31",
            "Base 32",
            "Base 33",
            "Base 34",
            "Base 35",
            "Base 36",
        };

        adapter = new ArrayAdapter<String>(activity, R.layout.simple_list_item, modes);
        setListAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View view = inflater.inflate(R.layout.umodes_view, null); 
        return view;
    }
}
