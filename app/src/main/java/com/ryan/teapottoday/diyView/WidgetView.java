package com.ryan.teapottoday.diyView;

/**
 * Created by rory9 on 2016/6/4.
 */
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
import android.view.MotionEvent;
import android.view.View;

import com.ryan.teapottoday.R;

//自定义类继承View
@SuppressLint({"NewApi", "DrawAllocation" })
public class WidgetView extends View {
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

    public WidgetView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public WidgetView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WidgetView(Context context) {
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

        float w = getWidth();
        float h = getWidth();
        // 第一个图片
        float scale = 1;// 缩放量
        float scaleX = w / topImage.getWidth();
        float scaleY = h / topImage.getHeight();
        scale = scaleX > scaleY ? scaleX : scaleY;
        topMatrix.setScale(scale, scale);// 开始缩放比例

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

        top.moveTo(padding,padding);
        top.lineTo(w,padding);
        top.lineTo(w,h-30);
        top.lineTo(padding,h);
        top.close();

//        // 上图
//        top.moveTo(padding, padding);
//        top.lineTo(w -bxx-cpad, padding);
//        top.lineTo(bx - cpad, by - cpad);
//        top.lineTo(padding, byy - cpad);
//        top.close();
//        // 左图
//        left.moveTo(padding, byy + cpad);
//        left.lineTo(bx - cpad, by+cpad);
//        left.lineTo(bxx - cpad, h - padding);
//        left.lineTo(padding, h - padding);
//        left.close();
//        // 下图
//        bottom.moveTo(bxx+cpad, h-padding);
//        bottom.lineTo(w -padding, h-padding);
//        bottom.lineTo(w - padding, h - byy+cpad);
//        bottom.lineTo(bx+cpad, by+cpad);
//        bottom.close();
//        // 右图
//        right.moveTo(w - bxx+cpad, padding);
//        right.lineTo(w -padding, padding);
//        right.lineTo(w - padding, h - byy-cpad);
//        right.lineTo(bx+cpad, by-cpad);
//        right.close();
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
//
//            canvas.save();
//            canvas.clipPath(left);
//            canvas.drawBitmap(leftImage, leftMatrix, paint);
//            canvas.restore();
//
//            canvas.save();
//            canvas.clipPath(bottom);
//            canvas.drawBitmap(bottomImage, bottomMatrix, paint);
//            canvas.restore();
//
//            canvas.save();
//            canvas.clipPath(right);
//            canvas.drawBitmap(rightImage, rightMatrix, paint);
//            canvas.restore();
        }
    }


}