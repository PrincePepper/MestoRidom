package mesto.ridom.mestoridom

import android.content.Context
import android.util.DisplayMetrics

fun Context.dpToPixels(dp: Int): Int = dp * this.resources.displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT
