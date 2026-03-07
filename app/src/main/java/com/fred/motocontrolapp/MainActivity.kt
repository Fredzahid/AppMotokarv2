package com.fred.motocontrolapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.fred.motocontrolapp.ui.navigation.AppNavigation
import com.fred.motocontrolapp.ui.viewmodel.MainViewModel
import com.fred.motocontrolapp.ui.theme.MotoControlAppTheme

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotoControlAppTheme {
                AppNavigation(viewModel)
            }
        }
    }
}