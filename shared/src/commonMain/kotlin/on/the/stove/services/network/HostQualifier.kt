package on.the.stove.services.network

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.QualifierValue

internal object HostQualifier : Qualifier {
    override val value: QualifierValue
        get() = "http://195.2.80.162:80"
}