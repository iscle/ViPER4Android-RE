package com.aam.viper4android

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun EffectCard(
    icon: Painter,
    name: String,
    enabled: Boolean?,
    onEnabledChange: ((Boolean) -> Unit)?,
    expandedContent: (@Composable BoxScope.() -> Unit)? = null
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    ElevatedCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clickable(
                    enabled = expandedContent != null || (enabled != null && onEnabledChange != null)
                ) {
                    if (expandedContent != null) {
                        expanded = !expanded
                    } else if (enabled != null && onEnabledChange != null) {
                        onEnabledChange(!enabled)
                    }
                }
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = icon, contentDescription = null
            )
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
            if (enabled != null && onEnabledChange != null) {
                Switch(checked = enabled, onCheckedChange = { onEnabledChange(it) })
            }
        }
        expandedContent?.let {
            AnimatedVisibility(visible = expanded) {
                Box(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    it()
                }
            }
        }
    }
}
