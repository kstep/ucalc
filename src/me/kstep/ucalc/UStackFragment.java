package me.kstep.ucalc;

import android.app.ListFragment;
import android.os.Bundle;
import android.widget.ArrayAdapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.view.DragEvent;
import android.content.Context;

import android.widget.Toast;

import java.util.Stack;
import java.util.Collections;

import com.commonsware.cwac.tlv.TouchListView;

class UStackFragment extends ListFragment implements TouchListView.DropListener, TouchListView.RemoveListener {

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
        public int getPosition(T item) {  return getCount() - super.getPosition(item) - 1; }
    }

    ArrayAdapter<Number> adapter;

    @Override
    public void onActivityCreated(Bundle savedInstanceBundle) {
        super.onActivityCreated(savedInstanceBundle);

        UCalcActivity activity = (UCalcActivity) getActivity();

        adapter = new StackAdapter<Number>(activity, R.layout.ustack_item, R.id.stack_item, activity.getStack());
        setListAdapter(adapter);

        TouchListView tlv = (TouchListView) getListView();
        tlv.setDropListener(this);
        tlv.setRemoveListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceBundle) {
        View view = inflater.inflate(R.layout.ustack_view, null); 
        return view;
    }

    public void drop(int from, int to) {
        Number item = adapter.getItem(from);
        adapter.remove(item);
        adapter.insert(item, to);
        showToast("Moving " + item.toString() + " to " + new Integer(to).toString());
    }

    public void remove(int which) {
        Number item = adapter.getItem(which);
        showToast("Removing " + item.toString());
        adapter.remove(item);
    }

    private void showToast(Object msg) {
        Toast toast = Toast.makeText(getActivity(), msg.toString(), Toast.LENGTH_SHORT);
        toast.show();
    }
}