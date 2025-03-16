package org.cubewhy.utils.ui

class ObservableState<T>(initialValue: T) {
    private var value: T = initialValue
    private var dispatchers: MutableList<(T) -> Unit> = mutableListOf()

    fun get(): T {
        return value
    }

    fun set(newValue: T) {
        if (value != newValue) {
            value = newValue
            dispatchers.forEach { it.invoke(newValue) }
        }
    }

    fun observe(onChange: (T) -> Unit) {
        this.dispatchers.add(onChange)
    }

    fun setSilently(newValue: T) {
        this.value = newValue
    }
}

fun <T> observableStateOf(initialValue: T): ObservableState<T> {
    return ObservableState(initialValue)
}
