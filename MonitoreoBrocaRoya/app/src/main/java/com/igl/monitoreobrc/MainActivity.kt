package com.igl.monitoreobrc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.igl.monitoreobrc.ui.screens.TreeMonitorScreen
import com.igl.monitoreobrc.ui.theme.MonitoreoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MonitoreoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TreeMonitorScreen()
                }
            }
        }
    }
}
