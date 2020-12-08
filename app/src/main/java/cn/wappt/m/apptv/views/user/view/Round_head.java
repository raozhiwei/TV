package cn.wappt.m.apptv.views.user.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

/**
 * @author 纵游四方悠自得
 * @create 2020/11/2--13:55
 * @effect 自定义用户头像
 */
public class Round_head extends AppCompatImageView {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);     //定义画笔，消除锯齿
    private Bitmap mbitmap;     //定义位图
    private BitmapShader bitmapShader;      //图片渲染器,是shader渲染的子类，主要是对图片进行渲染的
    private Matrix matrix = new Matrix();       //定义矩阵,可以对矩阵内的图片进行缩放，渲染，裁剪，拉伸等操作


    public Round_head(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Bitmap nbitmap = getBitmap(getDrawable());
        if (nbitmap != null) {
            int viewwidth = getWidth();
            int viewhight = getHeight();
            int viewminsize = Math.min(viewwidth, viewhight);
            float destwidth = viewminsize;
            float desthight = viewminsize;
            if (bitmapShader == null || !nbitmap.equals(mbitmap)) {
                mbitmap = nbitmap;
                bitmapShader = new BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            }
            if (bitmapShader != null) {
                matrix.setScale(destwidth / nbitmap.getWidth(), desthight / nbitmap.getHeight());
                bitmapShader.setLocalMatrix(matrix);
            }
            paint.setShader(bitmapShader);
            float radius = viewminsize / 2.0f;
            canvas.drawCircle(radius, radius, radius, paint);

        } else {

            super.onDraw(canvas);
        }
    }

    //新建图片获取方法
    private Bitmap getBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof ColorDrawable) {
            // 获取边界
            Rect rect = drawable.getBounds();
            int width = rect.right- rect.left;
            int height = rect.bottom - rect.top;
            //获取图片颜色
            int color = ((ColorDrawable) drawable).getColor();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawARGB(Color.alpha(color), Color.red(color), Color.green(color), Color.blue(color));
            return bitmap;
        } else {
            return null;
        }

    }
}
