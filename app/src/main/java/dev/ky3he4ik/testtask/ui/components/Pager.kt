package dev.ky3he4ik.testtask.ui.components

import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.gestures.ScrollScope
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Pager(
    modifier: Modifier,
    count: Int,
    state: PagerState,
    content: @Composable (page: Int) -> Unit,
) {
    LazyRow(
        modifier = modifier,
        state = state.lazyListState,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        items(count = count) { page ->
            Box(Modifier.fillParentMaxWidth().wrapContentSize(),) {
                content(page)
            }
        }
    }
}


@Composable
fun rememberPagerState(
    initialPage: Int = 0,
): PagerState = rememberSaveable(saver = Saver(
    save = { it.currentPage },
    restore = { PagerState(currentPage = it) }
)) {
    PagerState(currentPage = initialPage)
}

@Stable
class PagerState(
    currentPage: Int = 0,
) {
    internal val lazyListState = LazyListState(firstVisibleItemIndex = currentPage)

    private var _currentPage by mutableStateOf(currentPage)

    var currentPage: Int
        get() = _currentPage
        internal set(value) {
            if (value != _currentPage) {
                _currentPage = value
            }
        }

    suspend fun scroll(
        scrollPriority: MutatePriority,
        block: suspend ScrollScope.() -> Unit
    ) = lazyListState.scroll(scrollPriority, block)

    fun dispatchRawDelta(delta: Float): Float {
        return lazyListState.dispatchRawDelta(delta)
    }

    val isScrollInProgress: Boolean
        get() = lazyListState.isScrollInProgress
}
