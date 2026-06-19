package com.igl.programadorlabores

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.igl.programadorlabores.ui.screens.TaskScreen
import com.igl.programadorlabores.ui.theme.ProgramadorLaboresTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProgramadorLaboresTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    TaskScreen()
                }
            }
        }
    }
}
