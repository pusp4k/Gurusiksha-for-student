package com.androweb.voyage.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.GridView;

public class ExpendableGridView extends GridView {

    private boolean expendable = false;

    public ExpendableGridView(Context context) {
        super(context);
    }

    public ExpendableGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpendableGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public boolean isExpendable() {
        return expendable;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if(isExpendable()) {
            // Get the entire height by providing a very large height.
            // View.MEASURED_SIZE_MASK represents the largest height possible.
            int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK,
                    MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = getMeasuredHeight();
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void setExpandable(boolean expandable) {
        this.expendable = expandable;
    }
}
