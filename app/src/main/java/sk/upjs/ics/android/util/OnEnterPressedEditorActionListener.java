package sk.upjs.ics.android.util;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

/**
 * http://stackoverflow.com/a/13264058
 */
public abstract class OnEnterPressedEditorActionListener implements TextView.OnEditorActionListener {
    public static final String TAG = OnEnterPressedEditorActionListener.class.getName();
    public static final boolean CONSUME_EVENT = true;
    public static final boolean PROPAGATE_EVENT = false;

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (isSoftEnterInMultilineOrHardEnter(actionId, event) || isImeActionDone(actionId, event)) {
            onEnterPressed(textView);
            return CONSUME_EVENT;
        } else {
            return PROPAGATE_EVENT;
        }
    }

    protected abstract void onEnterPressed(TextView textView);

    protected boolean isSoftEnterInMultilineOrHardEnter(int actionId, KeyEvent event) {
        return actionId == EditorInfo.IME_NULL && event.getAction() == KeyEvent.ACTION_DOWN;
    }

    public boolean isImeActionDone(int actionId, KeyEvent event) {
        return event == null && actionId == EditorInfo.IME_ACTION_DONE;
    }
}
