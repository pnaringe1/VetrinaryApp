package com.i.vetrinarykotlinapp

/**
 * A generic class that holds a value with its loading status.
 *
 * @see <a href="https://github.com/android/architecture-components-samples/blob/master/GithubBrowserSample/app/src/main/java/com/android/example/github/vo/com.i.vetrinarykotlinapp.Resource.kt">Sample apps for Android Architecture Components</a>
 */
data class Resource<out T>(val status: Status, val data: T?, val exception: String?) {
    enum class Status {
        SUCCESS,
        ERROR,
    }

    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(message: String): Resource<T> {
            return Resource(Status.ERROR, null, message)
        }

    }
}