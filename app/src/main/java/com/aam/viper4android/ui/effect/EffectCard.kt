package com.aam.viper4android

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
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
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    expandedContent: (@Composable BoxScope.() -> Unit)? = null
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Card {
        Column {
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = expandedContent != null) { expanded = !expanded }
                .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = icon, contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = name,
                    modifier = Modifier.weight(1f),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(checked = enabled, onCheckedChange = { onEnabledChange(it) })
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
}