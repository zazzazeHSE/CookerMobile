package on.the.stove

expect val platform: String

class Greeting {
    fun greeting() = "Hello, $platform!"
}