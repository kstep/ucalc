package me.kstep.ucalc;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UCalcActivity extends Activity {
    private UStack stack;
	private UConstants constants;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        constants = new UConstants();
        stack = new UStack();
    }
    
    private void pushStack() {
    	TextView stack_head = (TextView) findViewById(R.id.view_stack_head);
    	String text = stack_head.getText().toString();
    	if (text.length() > 0) {
    		stack_head.setText("");
    		stack.push(Float.valueOf(text));
    		((TextView) findViewById(R.id.view_stack_tail)).setText(stack.toString());
    	}
    }
    
    private void popStack() {
    	if (stack.size() == 0) {
    		showToast("Stack is empty!");
    		return;
    	}
    	
    	((TextView) findViewById(R.id.view_stack_head)).setText(stack.pop().toString());
		((TextView) findViewById(R.id.view_stack_tail)).setText(stack.toString());
    }

    private void showToast(CharSequence ch) {
    	Toast toast = Toast.makeText(getApplicationContext(), ch, 100);
    	toast.show();
    }

    public void onDigitButtonClick(View view) {
        Button button = (Button) view;
        TextView stack_head = (TextView) findViewById(R.id.view_stack_head);
        stack_head.append(button.getText());
    }
    
    public void onDotButtonClick(View view) {
    	String text = ((TextView) findViewById(R.id.view_stack_head)).getText().toString();
    	if (!text.contains(".")) {
    		onDigitButtonClick(view);
    	}
    }
    
    public void onConstantButtonClick(View view) {
    	CharSequence name = ((Button) view).getText();
    	if (constants.containsKey(name)) {
    		pushStack();
    		((TextView) findViewById(R.id.view_stack_head)).setText(constants.get(name).toString());
    	}
    }
    
    public void onEnterButtonClick(View view) {
    	pushStack();
    }
    
    public void onBackspaceButtonClick(View view) {
    	TextView stack_head = (TextView) findViewById(R.id.view_stack_head);
    	CharSequence text = stack_head.getText();
    	if (text.length() > 0) {
        	stack_head.setText(text.subSequence(0, text.length() - 1));    		
    	}
    }
    
    public void onBinaryOpButtonClick(View view) {
    	pushStack();

    	CharSequence name = ((Button) view).getText();
    	Number arg1, arg2, result = null;
    	arg2 = stack.pop();
    	arg1 = stack.pop();
    	
    	if (name.equals("+")) {
    		result = arg1.floatValue() + arg2.floatValue();
    		
    	} else if (name.equals("−")) {
    		result = arg1.floatValue() - arg2.floatValue();
    		
    	} else if (name.equals("×")) {
    		result = arg1.floatValue() * arg2.floatValue();
    		
    	} else if (name.equals("÷")) {
    		result = arg1.floatValue() / arg2.floatValue();
    	}
    	stack.push(result);

    	popStack();
    }
    
    public void onUndoButtonClick(View view) {
    	// TODO
    }
    
    public void onClearButtonClick(View view) {
    	stack.clear();
    	((TextView) findViewById(R.id.view_stack_head)).setText("");
    	((TextView) findViewById(R.id.view_stack_tail)).setText("");
    }
}
