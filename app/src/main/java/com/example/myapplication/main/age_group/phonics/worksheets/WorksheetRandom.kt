package com.example.myapplication.main.age_group.phonics.worksheets

import kotlin.math.ceil

/**
 * Deterministic RNG so a sheet prints identically every time it is opened. Same
 * xorshift sequence as iOS `SeededGenerator` (an LCG-seeded xorshift64); the exact
 * shuffle ordering differs from iOS because Swift's stdlib range-mapping differs,
 * but each Android sheet is stable across opens, which is what matters.
 */
class SeededGenerator(seed: ULong) {
    private var state: ULong =
        seed * 6364136223846793005UL + 1442695040888963407UL

    fun next(): ULong {
        state = state xor (state shl 13)
        state = state xor (state shr 7)
        state = state xor (state shl 17)
        return state
    }

    /** A value in 0 until [bound]. */
    fun nextInt(bound: Int): Int {
        if (bound <= 0) return 0
        return (next() % bound.toULong()).toInt()
    }
}

/** Fisher–Yates using the seeded generator. */
fun <T> List<T>.shuffledSeeded(rng: SeededGenerator): List<T> {
    val a = toMutableList()
    for (i in a.indices.reversed()) {
        if (i == 0) break
        val j = rng.nextInt(i + 1)
        val t = a[i]; a[i] = a[j]; a[j] = t
    }
    return a
}

/**
 * Shuffle so that NO item ends up on the row it started on (a derangement). A plain
 * shuffle leaves some items in place — on a matching sheet that means D sits level
 * with d and the child draws a straight line across without reading. Mirrors iOS
 * `deranged`.
 */
fun <T> deranged(items: List<T>, rng: SeededGenerator): List<T> {
    if (items.size <= 1) return items
    val out = items.shuffledSeeded(rng).toMutableList()
    var guardCount = 0
    while (guardCount < items.size * 4) {
        val i = out.indices.firstOrNull { out[it] == items[it] } ?: break
        val j = (i + 1) % out.size
        val t = out[i]; out[i] = out[j]; out[j] = t
        guardCount++
    }
    return out
}

/** Row ranges for [count] items at most [size] per row, balanced — 26 over 5 become
 *  6,5,5,5,5 rather than 6,6,6,6,2 with a ragged tail. Mirrors iOS `balancedChunks`. */
fun balancedChunks(count: Int, size: Int): List<IntRange> {
    if (count <= 0 || size <= 0) return emptyList()
    val rows = ceil(count.toDouble() / size).toInt()
    val base = count / rows
    val extra = count % rows
    val out = mutableListOf<IntRange>()
    var i = 0
    for (r in 0 until rows) {
        val take = base + if (r < extra) 1 else 0
        out.add(i until (i + take))
        i += take
    }
    return out
}

/** Balanced rows of this list. */
fun <T> List<T>.balancedChunked(size: Int): List<List<T>> =
    balancedChunks(this.size, size).map { r -> this.subList(r.first, r.last + 1) }
