package org.cubewhy.utils.ui

import java.util.*

class ObservableList<T> : MutableList<T> {
    private val innerList = LinkedList<T>()
    private val listeners = mutableListOf<(ChangeType, T?, Int) -> Unit>()

    enum class ChangeType { ADD, REMOVE, UPDATE, CLEAR }

    fun addListener(listener: (ChangeType, T?, Int) -> Unit) {
        listeners.add(listener)
    }

    fun removeListener(listener: (ChangeType, T?, Int) -> Unit) {
        listeners.remove(listener)
    }

    private fun notifyChange(type: ChangeType, element: T?, index: Int) {
        listeners.forEach { it(type, element, index) }
    }

    override val size: Int get() = innerList.size

    override fun contains(element: T) = innerList.contains(element)
    override fun containsAll(elements: Collection<T>) = innerList.containsAll(elements)
    override fun isEmpty() = innerList.isEmpty()
    override fun iterator() = innerList.iterator()
    override fun listIterator() = innerList.listIterator()
    override fun listIterator(index: Int) = innerList.listIterator(index)
    override fun subList(fromIndex: Int, toIndex: Int) = innerList.subList(fromIndex, toIndex)

    override fun get(index: Int) = innerList[index]
    override fun indexOf(element: T) = innerList.indexOf(element)
    override fun lastIndexOf(element: T) = innerList.lastIndexOf(element)

    override fun add(element: T): Boolean {
        val result = innerList.add(element)
        if (result) notifyChange(ChangeType.ADD, element, innerList.size - 1)
        return result
    }

    override fun add(index: Int, element: T) {
        innerList.add(index, element)
        notifyChange(ChangeType.ADD, element, innerList.size - 1)
    }

    override fun removeAt(index: Int): T {
        val removed = innerList.removeAt(index)
        notifyChange(ChangeType.REMOVE, removed, index)
        return removed
    }

    override fun remove(element: T): Boolean {
        val index = innerList.indexOf(element)
        val result = innerList.remove(element)
        if (result) notifyChange(ChangeType.REMOVE, element, index)
        return result
    }

    override fun set(index: Int, element: T): T {
        val old = innerList.set(index, element)
        notifyChange(ChangeType.UPDATE, element, index)
        return old
    }

    override fun clear() {
        innerList.clear()
        notifyChange(ChangeType.CLEAR, null, -1)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val result = innerList.addAll(elements)
        if (result) {
            elements.forEach {
                notifyChange(ChangeType.ADD, it, innerList.indexOf(it))
            }
        }
        return result
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val result = innerList.addAll(index, elements)
        if (result) {
            elements.forEach {
                notifyChange(ChangeType.ADD, it, innerList.indexOf(it))
            }
        }
        return result
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        val elementSet = elements.toSet()
        val result = innerList.removeAll(elementSet)
        if (result) {
            elementSet.forEach {
                notifyChange(ChangeType.REMOVE, it, innerList.indexOf(it))
            }
        }
        return result
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        val result = innerList.retainAll(elements.toSet())
        notifyChange(ChangeType.UPDATE, null, -1)
        return result
    }
}
