package com.zarea.instabugtask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.zarea.instabugtask.di.DIContainer
import com.zarea.instabugtask.di.DIContainer.permissionManager
import com.zarea.instabugtask.ui.navigation.NavigationHost
import com.zarea.instabugtask.ui.theme.InstabugTaskTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DIContainer.init(this)

        setContent {
            InstabugTaskTheme {
                NavigationHost(
                    onAskForLocationPermission = { permissionManager.requestLocationPermissions(this) },
                    onAskForEnableLocation = { permissionManager.openLocationSettings() }
                )
            }
        }
    }
}

