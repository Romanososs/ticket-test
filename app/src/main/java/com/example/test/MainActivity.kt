package com.example.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.test.ui.theme.Route
import com.example.test.ui.theme.TestTheme
import com.example.test.ui.theme.baseNavGraph
import com.example.test.ui.theme.historyNavGraph
import com.example.test.ui.theme.navigateToMain
import com.example.test.ui.theme.recordsNavGraph
import com.example.test.ui.theme.searchNavGraph
import com.example.test.ui.theme.settingsNavGraph

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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TestTheme {
        Greeting("Android")
    }
}