package on.the.stove.dispatchers

import kotlinx.coroutines.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.*


actual val ioDispatcher: CoroutineContext
    get() = MainDispatcher

actual val uiDispatcher: CoroutineContext
    get() = MainDispatcher

actual val ktorDispatcher: CoroutineContext
    get() = GlobalScope.coroutineContext + MainDispatcher

@ThreadLocal
private object MainDispatcher : CoroutineDispatcher() {

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        dispatch_async(dispatch_get_main_queue()) {
            try {
                block.run().freeze()
            } catch (err: Throwable) {
                throw err
            }
        }
    }
}
