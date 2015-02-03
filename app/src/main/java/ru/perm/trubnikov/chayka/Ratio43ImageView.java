package ru.perm.trubnikov.chayka;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class Ratio43ImageView extends ImageView {

    public Ratio43ImageView(Context context) {
        super(context);
    }

    public Ratio43ImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Ratio43ImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //super.onMeasure(widthMeasureSpec, widthMeasureSpec); // square!
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        setMeasuredDimension(width, DBHelper.getRatio43Height(width) );
    }


}