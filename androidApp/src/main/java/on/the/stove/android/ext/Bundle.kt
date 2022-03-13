package on.the.stove.android.ext

import android.os.Bundle

internal fun Bundle?.requireGetString(key: String, defaultValue: String): String {
    return requireNotNull(this?.getString(key, defaultValue))
}