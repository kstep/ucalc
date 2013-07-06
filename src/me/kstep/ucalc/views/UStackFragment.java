package me.kstep.ucalc.views;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Context;
import android.content.ClipboardManager;
import android.content.ClipData;

import java.util.Stack;
import me.kstep.ucalc.activities.UCalcActivity;
import me.kstep.ucalc.numbers.UNumber;
import me.kstep.ucalc.numbers.UFloat;

import com.mobeta.android.dslv.DragSortListView;
import com.mobeta.android.dslv.DragSortController;

import me.kstep.ucalc.R;

public class UStackFragment extends ListFragment {

    static class StackAdapter<T> extends ArrayAdapter<T> {
        public StackAdapter(Context ctx, int txtresid, Stack<T> objs) { super(ctx, txtresid, objs); }
        public StackAdapter(Context ctx, int resid, int txtresid, Stack<T> objs) { super(ctx, resid, txtresid, objs); }

        @Override
        public T getItem(int position) { return super.getItem(getCount() - position - 1); }

        @Override
        public long getItemId(int position) { return super.getItemId(getCount() - position - 1); }

        @Override
        public void insert(T item, int to) { super.insert(item, getCount() - to); }

        @Override
        public int getPosition(T item) { return getCount() - super.getPosition(item) - 1; }
    }

    ArrayAdapter<UNumber> adapter;

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.stack_context_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final UCalcActivity activity = (UCalcActivity) getActivity();
        final ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);

        switch (item.getItemId()) {
            case R.id.menu_copy:
                try {
                    ListView lv = getListView();
                    int index = lv.getCheckedItemPosition();
                    clipboard.setPrimaryClip(ClipData.newPlainText(null, adapter.getItem(index).toString()));
                    lv.setItemChecked(index, false);

                } catch (ArrayIndexOutOfBoundsException e) {
                }
            return true;

            case R.id.menu_paste:
                try {
                    Double value = Double.valueOf(clipboard.getPrimaryClip().getItemAt(0).coerceToText(activity).toString());
                    if (value != null) {
                        UNumber num = new UFloat(value);
                        adapter.insert(num, 0);
                        getListView().setItemChecked(0, true);
                    }

                } catch (NullPointerException e) {
                } catch (NumberFormatException e) {
                }
            return true;
        }

        return false;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);
        setHasOptionsMenu(true);

        final UCalcActivity activity = (UCalcActivity) getActivity();

        adapter = new StackAdapter<UNumber>(activity, R.layout.list_item, R.id.list_item, activity.getStack());
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
        View view = inflater.inflate(R.layout.ustack_view, null);
        return view;
    }
}
