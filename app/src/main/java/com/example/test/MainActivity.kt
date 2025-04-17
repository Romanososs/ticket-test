package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.test.ui.theme.TestTheme

data class BottomNavigationItem(
    val route: Route,
    val title: String?,
)

val bottomNavigationItems = listOf(
    BottomNavigationItem(
        Route.HistoryMainGraph,
        "History",
    ),
    BottomNavigationItem(
        Route.RecordsMainGraph,
        "Records",
    ),
    BottomNavigationItem(
        Route.Add,
        "Add",
    ),
    BottomNavigationItem(
        Route.SearchMainGraph,
        "Search"
    ),
    BottomNavigationItem(
        Route.SettingsMainGraph,
        "Settings",
    ),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestTheme {
                val navController = rememberNavController()
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            bottomNavigationItems.forEach { item ->
                                val selected =
                                    currentDestination?.hierarchy?.any { it.hasRoute(item.route::class) } == true
                                NavigationBarItem(
                                    selected = selected,
                                    onClick = {
                                        navController.navigateToMain(selected, item.route)
                                    },
                                    icon = {

                                    },
                                    label = {
                                        Text(
                                            item.title ?: "",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis)
                                    }
                                )
                            }
                        }
                    },
                    contentWindowInsets = WindowInsets(0.dp),
                ) { padding ->
                    NavHost(
                        navController = navController,
                        startDestination = Route.HistoryMainGraph,
                        modifier = Modifier.padding(padding)
                    ) {
                        baseNavGraph(navController)
                        searchNavGraph(navController)
                        recordsNavGraph(navController)
                        settingsNavGraph(navController)
                        historyNavGraph(navController)
                    }
                }
            }
        }
    }
}