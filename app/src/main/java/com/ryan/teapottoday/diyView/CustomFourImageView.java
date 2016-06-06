package com.ryan.teapottoday.diyView;

/**
 * Created by rory9 on 2016/6/4.
 */
import java.io.IOException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.ryan.teapottoday.R;

//自定义类继承View
@SuppressLint({"NewApi", "DrawAllocation" })
public class CustomFourImageView extends View {
    //用于判断只在第一次绘制才初始化一些资源数据
    private int state = -1;
    private final int START = 1;

    private Bitmap topImage;
    private Bitmap bottomImage;
    private Bitmap leftImage;
    private Bitmap rightImage;

    private Paint paint;
    private Matrix topMatrix;
    private Matrix bottomMatrix;
    private Matrix leftMatrix;
    private Matrix rightMatrix;

    private float padding = 14;//边框的大小（实则为10px）
    private Path top;
    private Path bottom;
    private Path left;
    private Path right;

    /** 记录是拖拉照片模式还是放大缩小照片模式 */
    private int mode = 0;// 初始状态
    /** 拖拉照片模式 */
    private static final int MODE_DRAG = 1;
    /** 放大缩小照片模式 */
    private static final int MODE_ZOOM = 2;
    /** 用于记录开始时候的坐标位置 */
    private PointF startPoint = new PointF();
    /** 两个手指的开始距离 */
    private float startDis;
    /** 两个手指的中间点 */
    private PointF midPoint;

    private static int TYPE = 0;

    public CustomFourImageView(Context context, AttributeSet attrs,int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomFourImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomFourImageView(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (state > 0) {
            return;
        }
        state = START;
        init();
    }

    //初始化
    private void init() {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 软件加速

        topImage = BitmapFactory.decodeResource(getResources(), R.drawable.first01);
        bottomImage = BitmapFactory.decodeResource(getResources(), R.drawable.first02);
        leftImage = BitmapFactory.decodeResource(getResources(), R.drawable.first03);
        rightImage = BitmapFactory.decodeResource(getResources(), R.drawable.first04);

        paint = new Paint();
        paint.setAntiAlias(true);//反锯齿
        paint.setDither(true);// 防抖动
        paint.setFilterBitmap(true);// 过滤
        initMatrix();// 缩小图片
        initPath();
    }

    // 初始化矩阵(3X3)并缩放图片为原图的2分之1
    private void initMatrix() {
        topMatrix = new Matrix();
        bottomMatrix = new Matrix();
        leftMatrix = new Matrix();
        rightMatrix = new Matrix();

        float w = getWidth();
        float h = getWidth();
        // 第一个图片
        float scale = 1;// 缩放量
        float scaleX = w / topImage.getWidth();
        float scaleY = h / topImage.getHeight();
        scale = scaleX > scaleY ? scaleX : scaleY;
        topMatrix.setScale(scale, scale);// 开始缩放比例
        // 第二个图片
        scaleX = w / bottomImage.getWidth();
        scaleY = h / bottomImage.getHeight();
        scale = scaleX > scaleY ? scaleX : scaleY;
        bottomMatrix.setScale(scale, scale);
        // 第三个图片
        scaleX = w / leftImage.getWidth();
        scaleY = h / leftImage.getHeight();
        scale = scaleX > scaleY ? scaleX : scaleY;
        leftMatrix.setScale(scale, scale);
        // 第四个图片
        scaleX = w / rightImage.getWidth();
        scaleY = h / rightImage.getHeight();
        scale = scaleX > scaleY ? scaleX : scaleY;
        rightMatrix.setScale(scale, scale);
    }

    // 画好矩阵模块
    private void initPath() {
        float cpad = padding / 2;// padding = 10
        // 视图宽高
        float w = getWidth();
        float h = getWidth();
        float bx = w / 2;//
        float by = h / 2;// 相当于中心点

        float bxx = bx / 2;//
        float byy = by / 2;

        top = new Path();
        bottom = new Path();
        left = new Path();
        right = new Path();

        // 上图
        top.moveTo(padding, padding);
        top.lineTo(w -bxx-cpad, padding);
        top.lineTo(bx - cpad, by - cpad);
        top.lineTo(padding, byy - cpad);
        top.close();
        // 左图
        left.moveTo(padding, byy + cpad);
        left.lineTo(bx - cpad, by+cpad);
        left.lineTo(bxx - cpad, h - padding);
        left.lineTo(padding, h - padding);
        left.close();
        // 下图
        bottom.moveTo(bxx+cpad, h-padding);
        bottom.lineTo(w -padding, h-padding);
        bottom.lineTo(w - padding, h - byy+cpad);
        bottom.lineTo(bx+cpad, by+cpad);
        bottom.close();
        // 右图
        right.moveTo(w - bxx+cpad, padding);
        right.lineTo(w -padding, padding);
        right.lineTo(w - padding, h - byy-cpad);
        right.lineTo(bx+cpad, by-cpad);
        right.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (topImage != null) {
            // 设置抗锯齿
            PaintFlagsDrawFilter pfd = new PaintFlagsDrawFilter(0,Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
            canvas.setDrawFilter(pfd);

            canvas.save();
            canvas.clipPath(top);// 先画好模块
            canvas.drawBitmap(topImage, topMatrix, paint);// 再画图
            canvas.restore();

            canvas.save();
            canvas.clipPath(left);
            canvas.drawBitmap(leftImage, leftMatrix, paint);
            canvas.restore();

            canvas.save();
            canvas.clipPath(bottom);
            canvas.drawBitmap(bottomImage, bottomMatrix, paint);
            canvas.restore();

            canvas.save();
            canvas.clipPath(right);
            canvas.drawBitmap(rightImage, rightMatrix, paint);
            canvas.restore();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                // 当第一个手指按下时
                mode = MODE_DRAG;
                if (isInsideTop(event)) {
                    // 上图
                    TYPE = 1;
                    startPoint.set(event.getX(), event.getY());
                } else if (isInsideBottom(event)) {
                    // 下图
                    TYPE = 2;
                    startPoint.set(event.getX(), event.getY());
                } else if (isInsideLeft(event)) {
                    // 左图
                    TYPE = 3;
                    startPoint.set(event.getX(), event.getY());
                }else if(isInsideRight(event)){
                    // 右图
                    TYPE = 4;
                    startPoint.set(event.getX(), event.getY());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 移动或缩放
                if (mode == MODE_DRAG) {// 拖拉图片
                    float dx = event.getX() - startPoint.x;// 减去第一次的移动距离
                    float dy = event.getY() - startPoint.y;
                    startPoint.x = event.getX();
                    startPoint.y = event.getY();
                    // 在没有移动之前的位置上进行移动
                    getCurrentMatrix().postTranslate(dx, dy);
                } else if (mode == MODE_ZOOM) {// 放大缩小图片
                    float endDis = distance(event);
                    // 结束距离
                    if (endDis > 10f) {
                        // 两个手指并拢在一起的时候素大于10
                        float scale = endDis / startDis;
                        startDis = endDis;
                        // 得到缩放倍数进行缩放
                        getCurrentMatrix().postScale(scale, scale, midPoint.x,midPoint.y);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                // 当触点离开屏幕，但是屏幕上还有触点(手指)
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                mode = MODE_ZOOM;
                startDis = distance(event);
                if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                    midPoint = mid(event);
                    // 记录当前ImageView的缩放倍数
                }
                break;
        }
        invalidate();// 重绘
        return true;
    }

    private Matrix getCurrentMatrix() {
        switch (TYPE) {
            case 1:
                return topMatrix;
            case 2:
                return bottomMatrix;
            case 3:
                return leftMatrix;
            default:
                return rightMatrix;
        }
    }

    //计算上图的焦点范围
    private boolean isInsideTop(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float w = getWidth();
        return y < (-2*x + 3*w/2) && y < (x/2 + w/4);
    }

    //计算下图的焦点范围
    private boolean isInsideBottom(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float w = getWidth();
        return y > (-2*x + 3*w/2) && y > (x/2 + w/4);
    }

    //计算左图的焦点范围
    private boolean isInsideLeft(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float w = getWidth();
        return y < (-2*x + 3*w/2) && y > (x/2 + w/4);
    }

    //计算右图的焦点范围
    private boolean isInsideRight(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float w = getWidth();
        return y > (-2*x + 3*w/2) && y < (x/2 + w/4);
    }

    /** 计算两个手指间的距离 */
    private float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        /** 使用勾股定理返回两点之间的距离 */
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /** 计算两个手指间的中间点 */
    private PointF mid(MotionEvent event) {
        float midX = (event.getX(1) + event.getX(0)) / 2;
        float midY = (event.getY(1) + event.getY(0)) / 2;
        return new PointF(midX, midY);
    }
}