import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout

/**
 * A custom layout Modifier that positions the composable it's applied to
 * at the bottom of its available space.
 *
 *This is useful for creating footers within a layout like a Box.
 *
 * @return A Modifier that will position the element at the bottom.
 */
fun Modifier.positionAsFooter(): Modifier = this.then(
    // The 'layout' modifier is the core of custom placement logic.
    layout { measurable, constraints ->
        // 1. Measure the composable (the "footer").
        // We pass the incoming constraints directly to the child.
        // The result is a 'Placeable', which is a measured object.
        val placeable = measurable.measure(constraints)

        //2. Determine the size of the layout itself.
        // We will take up the full width and height given by the parent.
        // This is crucial because we need the full height to know where the "bottom" is.
        val layoutWidth = constraints.maxWidth
        val layoutHeight = constraints.maxHeight - placeable.height

        // 3. Place the composable on the screen.
        // The `layout(width, height)` block defines the size of our layout modifier.
        layout(layoutWidth, layoutHeight) {
            // Calculate the Y position for the footer.
            // To place it at thebottom, its top edge should be at:
            // (Total Height of the container) - (Height of the footer itself).
            val footerYPosition = layoutHeight

            // Place the composable at the calculated Y position.
            // We place it at x=0.You could also center it horizontally if needed.
            placeable.placeRelative(x = 0, y = footerYPosition)
        }
    }
)
