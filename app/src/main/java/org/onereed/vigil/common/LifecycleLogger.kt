package org.onereed.vigil.common

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.common.collect.HashMultiset
import java.util.Locale
import timber.log.Timber

object LifecycleLogger : LifecycleEventObserver {

  fun Lifecycle.addLogger() = addObserver(this@LifecycleLogger)

  override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
    val tag = getTag(source)
    val state = event.targetState

    val dir =
      when (event) {
        Lifecycle.Event.downTo(state) -> '\u2198' // ↘
        Lifecycle.Event.upTo(state) -> '\u2197' // ↗
        else -> '\u219d' // ↝
      }

    Timber.tag(tag).d("%-10s %c %s", event, dir, state)
  }

  private data class SourceId(val className: String, val hashCode: Int) {
    constructor(source: LifecycleOwner) : this(source.javaClass.simpleName, source.hashCode())
  }

  private val classInstanceCounts = HashMultiset.create<String>()
  private val sourceIdTags = mutableMapOf<SourceId, String>()

  private fun getTag(source: LifecycleOwner): String {
    val sourceId = SourceId(source)

    return sourceIdTags.getOrPut(sourceId) {
      // add() returns count before insertion, and we want 1-origin.
      val instanceNumber = classInstanceCounts.add(sourceId.className, 1) + 1
      String.format(Locale.US, "LC_%.18s_%d", sourceId.className, instanceNumber)
    }
  }
}
