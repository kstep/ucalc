package me.kstep.ucalc;

import java.util.EmptyStackException;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortController;

import me.kstep.ucalc.numbers.UNumber;

class UMemoryFragment extends ListFragment {
    ArrayAdapter<UNumber> adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);

        final UCalcActivity activity = (UCalcActivity) getActivity();
 
        adapter = new ArrayAdapter<UNumber>(activity, R.layout.list_item, R.id.list_item, activity.getMemory());
        setListAdapter(adapter);

        ((Button) activity.findViewById(R.id.memory_store_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    adapter.insert(activity.getStack().peek(), 0);
                    getListView().setItemChecked(0, true);

                } catch (EmptyStackException e) {
                }
            }
        });

        ((Button) activity.findViewById(R.id.memory_recall_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    activity.getStack().push(adapter.getItem(getListView().getCheckedItemPosition()));

                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        });

        ((Button) activity.findViewById(R.id.memory_clear_button)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                adapter.clear();
            }
        });

        DragSortListView dslv = (DragSortListView) getListView();
        dslv.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                UNumber item = adapter.getItem(from);
                adapter.remove(item);
                adapter.insert(item, to);
            }
        });
        dslv.setRemoveListener(new DragSortListView.RemoveListener() {
            @Override
            public void remove(int which) {
                UNumber item = adapter.getItem(which);
                adapter.remove(item);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View view = inflater.inflate(R.layout.umemory_view, null); 
        return view;
    }
}
