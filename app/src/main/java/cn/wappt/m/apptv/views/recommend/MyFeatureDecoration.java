package cn.wappt.m.apptv.views.recommend;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author: wsq
 * @date: 2020/11/2
 * Description:
 */
public class MyFeatureDecoration extends RecyclerView.ItemDecoration{

@Override
public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //outRect.set()中的参数分别对应左、上、右、下的间隔
        outRect.set(5,5,5,5);
        }
}
