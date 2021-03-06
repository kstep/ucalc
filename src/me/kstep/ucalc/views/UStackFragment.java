package me.kstep.ucalc.views;

import android.app.ActionBar;
import android.app.ListFragment;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ShareActionProvider;
import com.mobeta.android.dslv.DragSortController;
import com.mobeta.android.dslv.DragSortListView;
import java.util.Stack;
import me.kstep.ucalc.R;
import me.kstep.ucalc.activities.UCalcActivity;
import me.kstep.ucalc.numbers.UFloat;
import me.kstep.ucalc.numbers.UNumber;

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

    public void setSelection(int pos) {
        ListView lv = getListView();
        lv.requestFocusFromTouch();
        lv.setSelection(0);
    }

    public String getSelection(boolean resetSelection) {
        try {
            ListView lv = getListView();
            int index = lv.getCheckedItemPosition();
            if (resetSelection) lv.setItemChecked(index, false);
            return adapter.getItem(index).toString();

        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final UCalcActivity activity = (UCalcActivity) getActivity();
        final ClipboardManager clipboard = (ClipboardManager) activity.getSystemService(Context.CLIPBOARD_SERVICE);

        switch (item.getItemId()) {
            case R.id.menu_copy:
                clipboard.setPrimaryClip(ClipData.newPlainText(null, getSelection(true)));
            return true;

            case R.id.menu_paste:
                try {
                    UNumber value = UNumber.valueOf(clipboard.getPrimaryClip().getItemAt(0).coerceToText(activity).toString());
                    if (value != null) {
                        adapter.insert(value, 0);
                        setSelection(0);
                    }

                } catch (NullPointerException e) {
                } catch (NumberFormatException e) {
                }
            return true;

            case R.id.menu_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getSelection(true));
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
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

        ActionBar ab = getActivity().getActionBar();
        ab.setTitle(R.string.frag_title_stack);
        ab.show();

        return view;
    }
}
