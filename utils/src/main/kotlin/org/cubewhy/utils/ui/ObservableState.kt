package org.cubewhy.utils.ui

class ObservableState<T>(initialValue: T) {
    private var value: T = initialValue
    private var onChange: ((T) -> Unit)? = null

    fun get(): T {
        return value
    }

    fun set(newValue: T) {
        if (value != newValue) {
            value = newValue
            onChange?.invoke(newValue)
        }
    }

    fun observe(onChange: (T) -> Unit) {
        this.onChange = onChange
    }
}

fun <T> observableStateOf(initialValue: T): ObservableState<T> {
    return ObservableState(initialValue)
}
