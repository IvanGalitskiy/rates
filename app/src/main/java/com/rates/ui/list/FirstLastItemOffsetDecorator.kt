package com.rates.ui.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView


class FirstLastItemOffsetDecorator(
    private val topPadding: Int = 0,
    private val bottomPadding: Int = 0
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val itemCount = state.itemCount
        val itemPosition = parent.getChildAdapterPosition(view)
        // no position, leave it alone
        if (itemPosition == RecyclerView.NO_POSITION) {
            return
        }
        // first item
        if (itemPosition == 0) {
            outRect.set(
                outRect.left,
                view.paddingTop + topPadding,
                outRect.right,
                outRect.bottom
            )
        } else if (itemCount > 0 && itemPosition == itemCount - 1) {
            outRect.set(
                outRect.left,
                outRect.top,
                outRect.right,
                view.paddingBottom + bottomPadding
            )
        } else {
//            outRect.set(
//                view.paddingLeft,
//                view.paddingTop,
//                view.paddingRight,
//                view.paddingBottom
//            )
        }
    }
}