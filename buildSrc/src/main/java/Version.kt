object Version {
    const val okHttp = "4.2.1"
    const val grpc = "1.24.0"
}


infix fun String.version(version: Version.() -> String): String {
    return "$this:${version(Version)}"
}