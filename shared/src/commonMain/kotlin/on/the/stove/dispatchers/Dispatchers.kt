package on.the.stove.dispatchers

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

expect val ioDispatcher: CoroutineContext

expect val uiDispatcher: CoroutineContext

expect val ktorDispatcher: CoroutineContext