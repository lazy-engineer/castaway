package io.github.lazyengineer.castaway.androidApp.view.shared

import androidx.annotation.FloatRange
import androidx.compose.foundation.MutatePriority
import androidx.compose.foundation.MutatorMutex
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.DragScope
import androidx.compose.foundation.gestures.DraggableState
import androidx.compose.foundation.gestures.GestureCancellationException
import androidx.compose.foundation.gestures.Orientation.Horizontal
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.hoverable
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
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ContentAlpha
import androidx.compose.material.SliderColors
import androidx.compose.material.SliderDefaults
import androidx.compose.material.contentColorFor
import androidx.compose.material.minimumInteractiveComponentSize
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.setProgress
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection.Rtl
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import io.github.lazyengineer.castaway.androidApp.theme.CastawayTheme
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private val ThumbRadius = 10.dp
private val ThumbRippleRadius = 24.dp
private val ThumbDefaultElevation = 1.dp
private val ThumbPressedElevation = 6.dp

private val SliderHeight = 48.dp
private val SliderMinWidth = 144.dp
private val DefaultSliderConstraints =
  Modifier
    .widthIn(min = SliderMinWidth)
    .heightIn(max = SliderHeight)

private const val SENSITIVITY_THRESHOLD = 100

@Composable
fun PlaybackSliderView(
  @FloatRange(from = 0.0, to = 1.0) progress: Float,
  onValueChange: (Float) -> Unit,
  modifier: Modifier = Modifier,
  onValueChangeStarted: (() -> Unit)? = null,
  onValueChangeFinished: (() -> Unit)? = null,
) {

  val progressRange: ClosedFloatingPointRange<Float> = 0f..1f
  val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
  val onValueChangeState = rememberUpdatedState(onValueChange)

  BoxWithConstraints(
    modifier = modifier
      .minimumInteractiveComponentSize()
      .requiredSizeIn(minWidth = ThumbRadius * 2, minHeight = ThumbRadius * 2)
      .sliderSemantics(
        value = progress,
        onValueChange = onValueChange,
        onValueChangeStarted = onValueChangeStarted,
        onValueChangeFinished = onValueChangeFinished,
      )
      .focusable(interactionSource = interactionSource)
  ) {

    val isRtl = LocalLayoutDirection.current == Rtl
    val widthPx = constraints.maxWidth.toFloat()
    val maxPx: Float
    val minPx: Float

    with(LocalDensity.current) {
      maxPx = max(widthPx - ThumbRadius.toPx(), 0f)
      minPx = min(ThumbRadius.toPx(), maxPx)
    }

    fun scaleToUserValue(offset: Float) =
      scale(minPx, maxPx, offset, progressRange.start, progressRange.endInclusive)

    fun scaleToOffset(userValue: Float) =
      scale(progressRange.start, progressRange.endInclusive, userValue, minPx, maxPx)

    val rawOffset = remember { mutableFloatStateOf(scaleToOffset(progress)) }
    val pressOffset = remember { mutableFloatStateOf(0f) }

    val draggableState = remember(minPx, maxPx, progressRange) {
      SliderDraggableState {
        rawOffset.floatValue = (rawOffset.floatValue + it + pressOffset.floatValue)
        pressOffset.floatValue = 0f
        val offsetInTrack = rawOffset.floatValue.coerceIn(minPx, maxPx)
        onValueChangeState.value.invoke(scaleToUserValue(offsetInTrack))
      }
    }

    CorrectValueSideEffect(
      scaleToOffset = ::scaleToOffset,
      valueRange = progressRange,
      trackRange = minPx..maxPx,
      valueState = rawOffset,
      value = progress
    )

    val gestureStartAction = rememberUpdatedState<() -> Unit> {
      onValueChangeStarted?.invoke()
    }

    val gestureEndAction = rememberUpdatedState<(Float) -> Unit> { _ ->
      if (!draggableState.isDragging) {
        onValueChangeFinished?.invoke()
      }
    }

    val press = Modifier.sliderTapModifier(
      draggableState = draggableState,
      interactionSource = interactionSource,
      maxPx = widthPx,
      isRtl = isRtl,
      rawOffset = rawOffset,
      gestureStartAction = gestureStartAction,
      gestureEndAction = gestureEndAction,
      pressOffset = pressOffset,
      enabled = true
    )

    val drag = Modifier.draggable(
      orientation = Horizontal,
      reverseDirection = isRtl,
      interactionSource = interactionSource,
      onDragStarted = { gestureStartAction.value.invoke() },
      onDragStopped = { velocity -> gestureEndAction.value.invoke(velocity) },
      startDragImmediately = draggableState.isDragging,
      state = draggableState
    )

    val coerced = progress.coerceIn(progressRange.start, progressRange.endInclusive)
    val fraction = calcFraction(progressRange.start, progressRange.endInclusive, coerced)

    PlaybackSliderImpl(
      progress = fraction,
      width = maxPx - minPx,
      colors = sliderColor(),
      interactionSource = interactionSource,
      modifier = press.then(drag)
    )
  }
}

@Composable
private fun PlaybackSliderImpl(
  progress: Float,
  width: Float,
  interactionSource: MutableInteractionSource,
  modifier: Modifier = Modifier,
  colors: SliderColors = SliderDefaults.colors(),
) {
  Box(modifier.then(DefaultSliderConstraints)) {

    val widthDp: Dp
    with(LocalDensity.current) {
      widthDp = width.toDp()
    }

    val offset = widthDp * progress

    PlaybackTrackView(
      modifier = Modifier
        .align(Alignment.CenterStart)
        .fillMaxWidth(),
      playbackPosition = progress,
      thumbSize = ThumbRadius,
    )

    SliderThumb(
      offset = offset,
      interactionSource = interactionSource,
      colors = colors,
    )
  }
}

@Composable
private fun BoxScope.SliderThumb(
  offset: Dp,
  interactionSource: MutableInteractionSource,
  colors: SliderColors,
  modifier: Modifier = Modifier,
  thumbSize: Dp = ThumbRadius * 2,
) {
  Box(
    Modifier
      .padding(start = offset)
      .align(Alignment.CenterStart)
  ) {
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

    val elevation = if (interactions.isNotEmpty()) {
      ThumbPressedElevation
    } else {
      ThumbDefaultElevation
    }

    Spacer(
      modifier
        .size(thumbSize, thumbSize)
        .indication(
          interactionSource = interactionSource,
          indication = rememberRipple(bounded = false, radius = ThumbRippleRadius)
        )
        .hoverable(interactionSource = interactionSource)
        .shadow(elevation, CircleShape, clip = false)
        .background(colors.thumbColor(true).value, CircleShape)
    )
  }
}

@Suppress("LongParameterList")
private fun Modifier.sliderTapModifier(
  draggableState: DraggableState,
  interactionSource: MutableInteractionSource,
  maxPx: Float,
  isRtl: Boolean,
  rawOffset: State<Float>,
  gestureStartAction: State<() -> Unit>,
  gestureEndAction: State<(Float) -> Unit>,
  pressOffset: MutableState<Float>,
  enabled: Boolean
) = composed(
  factory = {
    if (enabled) {
      val scope = rememberCoroutineScope()
      pointerInput(draggableState, interactionSource, maxPx, isRtl) {
        detectTapGestures(
          onPress = { pos ->
            val to = if (isRtl) maxPx - pos.x else pos.x
            pressOffset.value = to - rawOffset.value
            try {
              awaitRelease()
            } catch (_: GestureCancellationException) {
              pressOffset.value = 0f
            }
          },
          onTap = {
            scope.launch {
              draggableState.drag(MutatePriority.UserInput) {
                // just trigger animation, press offset will be applied
                dragBy(0f)
              }
              gestureStartAction.value.invoke()
              gestureEndAction.value.invoke(0f)
            }
          }
        )
      }
    } else {
      this
    }
  },
  inspectorInfo = debugInspectorInfo {
    name = "sliderTapModifier"
    properties["draggableState"] = draggableState
    properties["interactionSource"] = interactionSource
    properties["maxPx"] = maxPx
    properties["isRtl"] = isRtl
    properties["rawOffset"] = rawOffset
    properties["gestureEndAction"] = gestureEndAction
    properties["pressOffset"] = pressOffset
  })

private fun Modifier.sliderSemantics(
  value: Float,
  onValueChange: (Float) -> Unit,
  onValueChangeStarted: (() -> Unit)? = null,
  onValueChangeFinished: (() -> Unit)? = null
): Modifier {
  val coerced = value.coerceIn(0f, 1f)
  return semantics {
    setProgress(
      action = { targetValue ->
        val newValue = targetValue.coerceIn(0f, 1f)

        // This is to keep it consistent with AbsSeekbar.java: return false if no
        // change from current.
        if (newValue == coerced) {
          false
        } else {
          onValueChange(newValue)
          onValueChangeStarted?.invoke()
          onValueChangeFinished?.invoke()
          true
        }
      }
    )
  }.progressSemantics(value)
}

// Scale x1 from a1..b1 range to a2..b2 range
private fun scale(a1: Float, b1: Float, x1: Float, a2: Float, b2: Float) =
  lerp(a2, b2, calcFraction(a1, b1, x1))

// Calculate the 0..1 fraction that `pos` value represents between `a` and `b`
private fun calcFraction(a: Float, b: Float, pos: Float) =
  (if (b - a == 0f) 0f else (pos - a) / (b - a)).coerceIn(0f, 1f)

@Suppress("MutableParams", "SideEffect")
@Composable
private fun CorrectValueSideEffect(
  scaleToOffset: (Float) -> Float,
  valueRange: ClosedFloatingPointRange<Float>,
  trackRange: ClosedFloatingPointRange<Float>,
  valueState: MutableState<Float>,
  value: Float
) {
  SideEffect {
    val error = (valueRange.endInclusive - valueRange.start) / SENSITIVITY_THRESHOLD
    val newOffset = scaleToOffset(value)
    if (abs(newOffset - valueState.value) > error) {
      if (valueState.value in trackRange) {
        valueState.value = newOffset
      }
    }
  }
}

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

@Composable
private fun sliderColor(): SliderColors {
  val activeTrackColor = CastawayTheme.colors.primary
  val activeTickColor = contentColorFor(activeTrackColor).copy(alpha = SliderDefaults.TickAlpha)
  val disabledActiveTrackColor = CastawayTheme.colors.onSurface.copy(alpha = SliderDefaults.DisabledActiveTrackAlpha)
  val disabledInactiveTrackColor = disabledActiveTrackColor.copy(alpha = SliderDefaults.DisabledInactiveTrackAlpha)

  return SliderDefaults.colors(
    thumbColor = CastawayTheme.colors.primary,
    disabledThumbColor = CastawayTheme.colors.onSurface
      .copy(alpha = ContentAlpha.disabled)
      .compositeOver(CastawayTheme.colors.surface),
    activeTrackColor = activeTrackColor,
    inactiveTrackColor = activeTrackColor.copy(alpha = SliderDefaults.InactiveTrackAlpha),
    disabledActiveTrackColor = disabledActiveTrackColor,
    disabledInactiveTrackColor = disabledInactiveTrackColor,
    activeTickColor = activeTickColor,
    inactiveTickColor = activeTrackColor.copy(alpha = SliderDefaults.TickAlpha),
    disabledActiveTickColor = activeTickColor.copy(alpha = SliderDefaults.DisabledTickAlpha),
    disabledInactiveTickColor = disabledInactiveTrackColor.copy(alpha = SliderDefaults.DisabledTickAlpha)
  )
}
