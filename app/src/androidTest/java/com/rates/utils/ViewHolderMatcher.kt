package com.rates.utils

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import com.rates.ui.list.RatesHolder
import org.hamcrest.Description
import org.hamcrest.Matcher

fun withHolder(itemMatcher: Matcher<View>) = ViewHolderMatcher(itemMatcher)

class ViewHolderMatcher(private val itemMatcher: Matcher<View>): BoundedMatcher<RecyclerView.ViewHolder, RatesHolder>(RatesHolder::class.java) {
    override fun describeTo(description: Description?) {
        description?.appendText("view holder with matcher: $itemMatcher");
    }

    override fun matchesSafely(item: RatesHolder?): Boolean {
        if (item == null){
            return false
        }
       return  itemMatcher.matches(item.itemView)
    }
}