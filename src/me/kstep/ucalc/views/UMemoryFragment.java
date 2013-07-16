package me.kstep.ucalc.views;

import android.app.ActionBar;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import java.util.EmptyStackException;
import me.kstep.ucalc.R;
import me.kstep.ucalc.activities.UCalcActivity;
import me.kstep.ucalc.collections.UStack;
import me.kstep.ucalc.numbers.UNumber;

public class UMemoryFragment extends ListFragment {
    ArrayAdapter<UNumber> adapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.memory_context_menu, menu);
    }

    public void setSelection(int pos) {
        ListView lv = getListView();
        lv.requestFocusFromTouch();
        lv.setSelection(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final UStack stack = ((UCalcActivity) getActivity()).getStack();

        switch (item.getItemId()) {
            case R.id.menu_store:
                try {
                    adapter.insert(stack.peek(), 0);
                    setSelection(0);

                } catch (EmptyStackException e) {
                }
            return true;

            case R.id.menu_recall:
                try {
                    stack.push(adapter.getItem(getListView().getCheckedItemPosition()));

                } catch (ArrayIndexOutOfBoundsException e) {
                }
            return true;

            case R.id.menu_clear:
                adapter.clear();
            return true;
        }

        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);
        setHasOptionsMenu(true);

        final UCalcActivity activity = (UCalcActivity) getActivity();

        adapter = new ArrayAdapter<UNumber>(activity, R.layout.list_item, R.id.list_item, activity.getMemory());
        setListAdapter(adapter);

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

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.frag_title_memory);
        ab.show();

        return view;
    }
}
