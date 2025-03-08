package org.cubewhy.utils.ui

class Trigger {
    private var dispatchers: MutableList<() -> Unit> = mutableListOf()

    fun trig() {
        this.dispatchers.forEach { it.invoke() }
    }

    fun observe(onChange: () -> Unit) {
        this.dispatchers.add(onChange)
    }
}

fun trigger() = Trigger()