package com.marquelo.getstars.working.interfaces;
import androidx.recyclerview.widget.RecyclerView;

public interface CallBackItemTouch {
    void itemTouchMode(int oldPosition, int newPosition);
    void onSwiped(RecyclerView.ViewHolder viewHolder, int position);
}
