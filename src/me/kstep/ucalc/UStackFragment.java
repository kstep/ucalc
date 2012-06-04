package me.kstep.ucalc;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;

class UStackFragment extends ListFragment {
    @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);

        UCalcActivity activity = (UCalcActivity) getActivity();
        setListAdapter(new ArrayAdapter<Number>(activity, R.layout.ustack_item, R.id.stack_item, activity.getStack()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View view = inflater.inflate(R.layout.ustack_view, null); 
        return view;
    }
}
