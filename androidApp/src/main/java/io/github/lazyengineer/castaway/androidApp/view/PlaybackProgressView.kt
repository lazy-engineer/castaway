package io.github.lazyengineer.castaway.androidApp.view

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.DragInteraction.Start
import androidx.compose.foundation.interaction.DragInteraction.Stop
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction.Cancel
import androidx.compose.foundation.interaction.PressInteraction.Press
import androidx.compose.foundation.interaction.PressInteraction.Release
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap.Round
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun PlaybackProgressView(
  modifier: Modifier,
  @FloatRange(from = 0.0, to = 1.0) progress: Float,
  onValueChange: (Float) -> Unit,
  onValueChangeStarted: (() -> Unit)? = null,
  onValueChangeFinished: ((Float) -> Unit)? = null,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
  val scope = rememberCoroutineScope()

  val steps = 0
  val valueRange: ClosedFloatingPointRange<Float> = 0f..1f

  val position = remember(valueRange, steps, scope) {
	SliderPosition(progress, valueRange, steps, scope, onValueChange)
  }
  position.onValueChange = onValueChange
  position.scaledValue = progress

  BoxWithConstraints(modifier = modifier.height(48.dp)) {

	val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
	val maxPx = constraints.maxWidth.toFloat()
	val minPx = 0f
	val widthDp = with(LocalDensity.current) {
	  maxPx.toDp()
	}

	position.setBounds(minPx, maxPx)

	val gestureEndAction: (Float) -> Unit = { velocity: Float ->
	  if (position.anchorsPx.isNotEmpty()) {
		val now = position.holder.value
		val point = position.anchorsPx.minByOrNull { abs(it - now) }
		val target = point ?: now
		scope.launch {
		  position.holder.animateTo(target, TweenSpec(durationMillis = 100), velocity) {
			position.onHolderValueUpdated(this.value)
		  }
		  onValueChangeFinished?.invoke(progress)
		}
	  } else {
		onValueChangeFinished?.invoke(progress)
	  }
	}

	val press = Modifier.pointerInput(Unit) {
	  detectTapGestures(
		onPress = { pos ->
		  position.snapTo(if (isRtl) maxPx - pos.x else pos.x)
		  val interaction = Press(pos)
		  coroutineScope {
			launch {
			  interactionSource.emit(interaction)
			}
			try {
			  val success = tryAwaitRelease()
			  if (success) gestureEndAction(0f)
			  launch {
				interactionSource.emit(Release(interaction))
			  }
			} catch (c: CancellationException) {
			  launch {
				interactionSource.emit(Cancel(interaction))
			  }
			}
		  }
		}
	  )
	}

	val drag = Modifier.draggable(
	  orientation = Orientation.Horizontal,
	  reverseDirection = isRtl,
	  interactionSource = interactionSource,
	  onDragStopped = { velocity -> gestureEndAction(velocity) },
	  startDragImmediately = position.holder.isRunning,
	  state = rememberDraggableState {
		position.snapTo(position.holder.value + it)
	  }
	)

	val center = Modifier.align(Alignment.CenterStart)
	val thumbSize = 20.dp
	val offset = (widthDp - thumbSize) * progress

	LaunchedEffect(interactionSource) {
	  interactionSource.interactions.collect { interaction ->
		when (interaction) {
		  is Start -> {
			onValueChangeStarted?.invoke()
		  }
		}
	  }
	}

	PlaybackTrack(modifier = center.fillMaxWidth(), playbackPosition = progress)
	PlaybackThumb(
	  modifier = center.then(press).then(drag),
	  thumbOffset = offset,
	  thumbSize = thumbSize,
	  interactionSource = interactionSource,
	)
  }
}

@Composable
private fun PlaybackTrack(
  modifier: Modifier,
  playbackPosition: Float,
  trackColor: Color = MaterialTheme.colors.primary.copy(.25f),
  progressColor: Color = MaterialTheme.colors.primary,
  thumbDp: Dp = 16.dp,
  trackStrokeWidth: Dp = 4.dp,
) {
  Canvas(modifier) {
	val start = Offset(thumbDp.toPx(), center.y)
	val end = Offset(size.width - thumbDp.toPx(), center.y)

	drawLine(
	  trackColor,
	  start,
	  end,
	  trackStrokeWidth.toPx(),
	  Round
	)

	val sliderValue = Offset(start.x + (end.x - start.x) * playbackPosition, center.y)

	drawLine(
	  progressColor,
	  start,
	  sliderValue,
	  trackStrokeWidth.toPx(),
	  Round
	)
  }
}

@Composable
private fun PlaybackThumb(
  modifier: Modifier,
  thumbOffset: Dp,
  thumbSize: Dp,
  thumbColor: Color = MaterialTheme.colors.primary,
  interactionSource: MutableInteractionSource,
) {
  Box(modifier.padding(start = thumbOffset)) {
	val interactions = remember { mutableStateListOf<Interaction>() }

	LaunchedEffect(interactionSource) {
	  interactionSource.interactions.collect { interaction ->
		when (interaction) {
		  is Press -> interactions.add(interaction)
		  is Release -> interactions.remove(interaction.press)
		  is Cancel -> interactions.remove(interaction.press)
		  is Start -> interactions.add(interaction)
		  is Stop -> interactions.remove(interaction.start)
		  is DragInteraction.Cancel -> interactions.remove(interaction.start)
		}
	  }
	}

	val hasInteraction = interactions.isNotEmpty()
	val elevation = if (hasInteraction) {
	  6.dp
	} else {
	  1.dp
	}

	Surface(
	  shape = CircleShape,
	  color = thumbColor,
	  elevation = elevation,
	  modifier = modifier
		.focusable(interactionSource = interactionSource)
		.indication(
		  interactionSource = interactionSource,
		  indication = rememberRipple(
			bounded = false,
			radius = 24.dp
		  )
		)
	) {
	  Spacer(Modifier.size(thumbSize, thumbSize))
	}
  }
}

private class SliderPosition(
  initial: Float = 0f,
  val valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
  /*@IntRange(from = 0)*/
  steps: Int = 0,
  val scope: CoroutineScope,
  var onValueChange: (Float) -> Unit
) {

  internal val startValue: Float = valueRange.start
  internal val endValue: Float = valueRange.endInclusive

  init {
	require(steps >= 0) {
	  "steps should be >= 0"
	}
  }

  internal var scaledValue: Float = initial
	set(value) {
	  val scaled = scale(startValue, endValue, value, startPx, endPx)
	  // floating point error due to rescaling
	  if ((scaled - holder.value) > floatPointMistakeCorrection) {
		snapTo(scaled)
	  }
	}

  private val floatPointMistakeCorrection = (valueRange.endInclusive - valueRange.start) / 100

  private var endPx = Float.MAX_VALUE
  private var startPx = Float.MIN_VALUE

  internal fun setBounds(min: Float, max: Float) {
	if (startPx == min && endPx == max) return
	val newValue = scale(startPx, endPx, holder.value, min, max)
	startPx = min
	endPx = max
	holder.updateBounds(min, max)
	anchorsPx = tickFractions.map {
	  lerp(startPx, endPx, it)
	}
	snapTo(newValue)
  }

  internal val tickFractions: List<Float> =
	if (steps == 0) emptyList() else List(steps + 2) { it.toFloat() / (steps + 1) }

  internal var anchorsPx: List<Float> = emptyList()
	private set

  internal val holder = Animatable(scale(startValue, endValue, initial, startPx, endPx))

  internal fun snapTo(newValue: Float) {
	scope.launch {
	  holder.snapTo(newValue)
	  onHolderValueUpdated(holder.value)
	}
  }

  internal val onHolderValueUpdated: (value: Float) -> Unit = {
	onValueChange(scale(startPx, endPx, it, startValue, endValue))
  }
}

// Scale x1 from a1..b1 range to a2..b2 range
private fun scale(a1: Float, b1: Float, x1: Float, a2: Float, b2: Float) =
  lerp(a2, b2, calcFraction(a1, b1, x1))

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
private fun calcFraction(a: Float, b: Float, pos: Float) =
  (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

private fun lerp(start: Float, stop: Float, fraction: Float): Float =
  (1 - fraction) * start + fraction * stop
