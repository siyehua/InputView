package com.siyehua.inputlibrary;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

/**
 * input text view
 * Created by siyehua on 2016/11/8.
 */
public class InputView extends Dialog {
    public InputView(Context context) {
        super(context);
    }

    public InputView(Context context, int themeResId) {
        super(context, themeResId);
    }

    public InputView(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**
     * the input call back
     */
    public interface CallBack {
        /**
         * the call back method
         *
         * @param s input text
         */
        void textChange(CharSequence s);
    }

    private ViewGroup rootView;
    private EditText topEditText, editText;
    private int screenHeight;
    private int screenWidth;
    /**
     * soft key bord height
     */
    private int softKeyBordHeight;
    /**
     * key board showing flag
     */
    private boolean showKeyBordFlag = false;

    /**
     * init dialog
     *
     * @param context context
     */
    public static InputView getInsance(Context context) {
        return new InputView(context, R.style.my_dialog);
    }

    /**
     * init dialog
     *
     * @param layoutId               the layout's id, must be a editText view.
     * @param cancelable             can cancel
     * @param canceledOnTouchOutside can touch outside
     * @param backgroundDimEnabled   backgroundDimEnabled
     * @param callBack               input call back
     */
    public InputView init(int layoutId, boolean cancelable, boolean canceledOnTouchOutside, final
    boolean backgroundDimEnabled, final CallBack callBack) {
        /**
         * get screen width and height.
         */
        screenHeight = getContext().getResources().getDisplayMetrics().heightPixels;
        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;

//        getWindow().setWindowAnimations(R.style.dialogAnimation);// set animations.

        setCancelable(cancelable);
        /**
         * because this input dialog is full of screen,so it can't touch any outside.<br>
         * we can set rootView setOnClickListener replace
         */
        //setCanceledOnTouchOutside(canceledOnTouchOutside);

        if (backgroundDimEnabled) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        } else getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        /**
         * add layout
         */
        rootView = (ViewGroup) getLayoutInflater().inflate(R.layout.dialog_input, null);
        topEditText = (EditText) rootView.findViewById(R.id._001);
        //add input editText
        editText = (EditText) getLayoutInflater().inflate(layoutId, rootView, false);
        rootView.addView(editText);
        editText.setVisibility(View.GONE);
        RelativeLayout.LayoutParams userParams = (RelativeLayout.LayoutParams) editText
                .getLayoutParams();
        userParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        userParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        editText.setLayoutParams(userParams);
        getWindow().setContentView(rootView);// add layout into window

        //set width and height
        WindowManager.LayoutParams window = getWindow().getAttributes();
        window.height = screenHeight;
        window.width = screenWidth;
        getWindow().setAttributes(window);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


        /**
         * use this replace setCanceledOnTouchOutside method
         */
        if (canceledOnTouchOutside) rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //set text change
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (callBack != null) callBack.textChange(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //set dismiss listener
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                /**
                 * set property to default status.
                 */
                editText.setVisibility(View.GONE);
                topEditText.setVisibility(View.VISIBLE);
                showKeyBordFlag = false;
                //remove the layout listener
                if (rootView.getViewTreeObserver().isAlive()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        rootView.getViewTreeObserver().removeOnGlobalLayoutListener(layoutListener);
                    } else {
                        rootView.getViewTreeObserver().removeGlobalOnLayoutListener(layoutListener);
                    }
                }
            }
        });

        return this;
    }

    /**
     * the dialog layout change listener
     */
    private ViewTreeObserver.OnGlobalLayoutListener layoutListener = new ViewTreeObserver
            .OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int visible = r.bottom - r.top;
            int change = screenHeight - visible - getStatusBarHeight(getContext());
            Log.e("siyehua", change + "");
            if (change == 0 && showKeyBordFlag) {//the status is hide the key board
                dismiss();
            }
        }
    };

    /**
     * the keyboard show listener
     */
    private ViewTreeObserver.OnGlobalLayoutListener keyBoardShowListener = new ViewTreeObserver
            .OnGlobalLayoutListener() {
        @Override
        public void onGlobalLayout() {
            showKeyBordFlag = true;
            topEditText.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);

            Rect r = new Rect();
            rootView.getWindowVisibleDisplayFrame(r);
            int visible = r.bottom - r.top;
            int change = screenHeight - visible - getStatusBarHeight(getContext());
//            Log.e("siyehua", change + "");
            if (change > 0)
                handler.sendEmptyMessage(softKeyBordHeight = change + getStatusBarHeight
                        (getContext()));
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //remove the key board show listener
            if (rootView.getViewTreeObserver().isAlive()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener
                            (keyBoardShowListener);
                } else {
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener
                            (keyBoardShowListener);
                }
            }

            //set the input editText position
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) editText
                    .getLayoutParams();
            layoutParams.setMargins(0, 0, 0, msg.what);
            editText.setLayoutParams(layoutParams);
            //get focus
            editText.requestFocus();
        }
    };


    public void show(CharSequence s) {
        editText.setText(s);
        editText.setSelection(s.length());
        show();
    }

    @Override
    public void show() {
        super.show();
        //add layout listener
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(layoutListener);

        /**
         * because dialog showing must have a time,so the soft key board can't show  if  you
         * invoke showSoftKeyboard() immediately.
         */
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                //add softKeyBord show listener
                rootView.getViewTreeObserver().addOnGlobalLayoutListener(keyBoardShowListener);
                //show softKeyBord
                showSoftKeyboard(getContext(), topEditText);
            }
        }, 400);
    }


    /**
     * show soft key board
     *
     * @param activity
     * @param view
     */
    public static void showSoftKeyboard(Context activity, View view) {
        ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .showSoftInput(view, 0);
    }

    /**
     * get status bar height
     *
     * @param context context
     * @return height
     */
    public static int getStatusBarHeight(Context context) {
        int x;
        int sbar;
        Class<?> c;
        Object obj;
        Field field;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            sbar = context.getResources().getDimensionPixelSize(x);
            return sbar;
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return 0;
    }
}
