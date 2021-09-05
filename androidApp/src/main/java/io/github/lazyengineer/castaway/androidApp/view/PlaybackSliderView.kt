package io.github.lazyengineer.castaway.androidApp.view

import androidx.annotation.FloatRange
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.TweenSpec
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
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
import androidx.compose.material.Surface
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun PlaybackSliderView(
  modifier: Modifier,
  @FloatRange(from = 0.0, to = 1.0) progress: Float,
  onValueChange: (Float) -> Unit,
  onValueChangeStarted: (() -> Unit)? = null,
  onValueChangeFinished: (() -> Unit)? = null,
  interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
) {
  val steps = 0
  val valueRange: ClosedFloatingPointRange<Float> = 0f..1f

  require(steps >= 0) { "steps should be >= 0" }
  val onValueChangeState = rememberUpdatedState(onValueChange)
  val tickFractions = remember(steps) {
	if (steps == 0) emptyList() else List(steps + 2) { it.toFloat() / (steps + 1) }
  }

  BoxWithConstraints(modifier = modifier.height(48.dp)) {

	val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
	val maxPx = constraints.maxWidth.toFloat()
	val minPx = 0f

	fun scaleToUserValue(offset: Float) =
	  scale(minPx, maxPx, offset, valueRange.start, valueRange.endInclusive)

	fun scaleToOffset(userValue: Float) =
	  scale(valueRange.start, valueRange.endInclusive, userValue, minPx, maxPx)

	val scope = rememberCoroutineScope()
	val rawOffset = remember { mutableStateOf(scaleToOffset(progress)) }
	val draggableState = remember(minPx, maxPx, valueRange) {
	  SliderDraggableState {
		rawOffset.value = (rawOffset.value + it).coerceIn(minPx, maxPx)
		onValueChangeState.value.invoke(scaleToUserValue(rawOffset.value))
	  }
	}

	SideEffect {
	  val newOffset = scaleToOffset(progress)
	  // floating point error due to rescaling
	  val error = (valueRange.endInclusive - valueRange.start) / 1000
	  if (abs(newOffset - rawOffset.value) > error) rawOffset.value = newOffset
	}

	val gestureEndAction = rememberUpdatedState<(Float) -> Unit> { velocity: Float ->
	  val current = rawOffset.value
	  // target is a closest anchor to the `current`, if exists
	  val target = tickFractions
		.minByOrNull { abs(lerp(minPx, maxPx, it) - current) }
		?.run { lerp(minPx, maxPx, this) }
		?: current
	  if (current != target) {
		scope.launch {
		  animateToTarget(draggableState, current, target, velocity)
		  onValueChangeFinished?.invoke()
		}
	  } else {
		onValueChangeFinished?.invoke()
	  }
	}

	val press = Modifier.sliderPressModifier(
	  draggableState, interactionSource, maxPx, isRtl, rawOffset, gestureEndAction, true
	)

	val drag = Modifier.draggable(
	  orientation = Orientation.Horizontal,
	  reverseDirection = isRtl,
	  enabled = true,
	  interactionSource = interactionSource,
	  onDragStopped = { velocity -> gestureEndAction.value.invoke(velocity) },
	  startDragImmediately = draggableState.isDragging,
	  state = draggableState
	)

	LaunchedEffect(interactionSource) {
	  interactionSource.interactions.collect { interaction ->
		when (interaction) {
		  is Press -> {
			onValueChangeStarted?.invoke()
		  }
		  is Start -> {
			onValueChangeStarted?.invoke()
		  }
		}
	  }
	}

	PlaybackSliderImpl(
	  modifier = drag, //TODO Find a way to differentiate between drag-press and normal press gesture -> gestureEndAction shouldn't be called on drag-press -> press.then(drag),
	  progress = progress,
	  width = maxPx,
	  interactionSource = interactionSource,
	)
  }
}

@Composable
fun PlaybackSliderImpl(
  modifier: Modifier,
  progress: Float,
  width: Float,
  interactionSource: MutableInteractionSource,
) {

  val widthDp = with(LocalDensity.current) {
	width.toDp()
  }

  Box(modifier) {
	val center = Modifier.align(Alignment.CenterStart)
	val thumbSize = 20.dp
	val offset = (widthDp - thumbSize) * progress

	PlaybackProgressView(modifier = center.fillMaxWidth(), playbackPosition = progress)
	PlaybackThumb(
	  modifier = center,
	  thumbOffset = offset,
	  thumbSize = thumbSize,
	  interactionSource = interactionSource,
	)
  }
}

@Composable
private fun PlaybackThumb(
  modifier: Modifier,
  thumbOffset: Dp,
  thumbSize: Dp,
  thumbColor: Color = CastawayTheme.colors.primary,
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

private fun Modifier.sliderPressModifier(
  draggableState: DraggableState,
  interactionSource: MutableInteractionSource,
  maxPx: Float,
  isRtl: Boolean,
  rawOffset: State<Float>,
  gestureEndAction: State<(Float) -> Unit>,
  enabled: Boolean
): Modifier =
  if (enabled) {
	pointerInput(draggableState, interactionSource, maxPx, isRtl) {
	  detectTapGestures(
		onPress = { pos ->
		  draggableState.drag(MutatePriority.UserInput) {
			val to = if (isRtl) maxPx - pos.x else pos.x
			dragBy(to - rawOffset.value)
		  }
		  val interaction = Press(pos)
		  interactionSource.emit(interaction)
		  val finishInteraction =
			try {
			  val success = tryAwaitRelease()
			  gestureEndAction.value.invoke(0f)
			  if (success) {
				Release(interaction)
			  } else {
				Cancel(interaction)
			  }
			} catch (c: CancellationException) {
			  Cancel(interaction)
			}
		  interactionSource.emit(finishInteraction)
		}
	  )
	}
  } else {
	this
  }

private val SliderToTickAnimation = TweenSpec<Float>(durationMillis = 100)

private class SliderDraggableState(
  val onDelta: (Float) -> Unit
) : DraggableState {

  var isDragging by mutableStateOf(false)
	private set

  private val dragScope: DragScope = object : DragScope {
	override fun dragBy(pixels: Float): Unit = onDelta(pixels)
  }

  private val scrollMutex = MutatorMutex()

  override suspend fun drag(
	dragPriority: MutatePriority,
	block: suspend DragScope.() -> Unit
  ): Unit = coroutineScope {
	isDragging = true
	scrollMutex.mutateWith(dragScope, dragPriority, block)
	isDragging = false
  }

  override fun dispatchRawDelta(delta: Float) {
	return onDelta(delta)
  }
}

private suspend fun animateToTarget(
  draggableState: DraggableState,
  current: Float,
  target: Float,
  velocity: Float
) {
  draggableState.drag {
	var latestValue = current
	Animatable(initialValue = current).animateTo(target, SliderToTickAnimation, velocity) {
	  dragBy(this.value - latestValue)
	  latestValue = this.value
	}
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
