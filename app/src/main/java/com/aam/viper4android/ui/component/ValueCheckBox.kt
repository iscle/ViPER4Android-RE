package com.aam.viper4android.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ValueCheckBox(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f),
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Preview
@Composable
fun ValueCheckBoxPreview() {
    ValueCheckBox(
        title = "Title",
        checked = true,
        onCheckedChange = { }
    )
}
