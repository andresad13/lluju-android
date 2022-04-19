package com.zheng.liuju.utils;

import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.widget.TextView;

import java.text.DecimalFormat;

public class AnimationUtils {
    /**
     * @param tv    TextView
     * @param value 最终显示的值
     */
    public static void addTextViewAddAnim(TextView tv, double value) {
        TextViewEvaluator evaluator = new TextViewEvaluator(value);
        ValueAnimator animator = ValueAnimator.ofObject(evaluator, tv);
        //动画时间
        animator.setDuration(800);
        animator.start();
    }

    //核心类
    static class TextViewEvaluator implements TypeEvaluator {
        private double value = 0;

        TextViewEvaluator(double value) {
            this.value = value;
        }

        @Override
        public Object evaluate(float fraction, Object startValue, Object endValue) {
            //样式具体改变 (自定义)
            TextView tv = (TextView) endValue;
            tv.setText(value +"");
            return startValue;
        }

    }


}
