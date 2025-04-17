package com.example.test.ui.theme

import androidx.annotation.Keep
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object HistoryMainGraph : Route

    @Serializable
    data object History : Route

    @Serializable
    data class Report(val number: String) : Route

    @Serializable
    data class Notes(val number: String) : Route

    @Serializable
    data class EventsGraph(val number: String) : Route

    @Serializable
    data class Events(val number: String) : Route

    @Serializable
    data object EditEvent : Route

    @Serializable
    data object LocationPick : Route

    @Serializable
    data object RecordsMainGraph : Route

    @Serializable
    data object Records : Route

    @Serializable
    data object Add : Route

    @Serializable
    data object SettingsMainGraph : Route

    @Serializable
    data object Settings : Route

    @Serializable
    data object Socials : Route

    @Serializable
    data object Auth : Route

    @Serializable
    data object SearchMainGraph : Route

    @Serializable
    data object SearchGraph : Route

    @Serializable
    data object Search : Route

    @Serializable
    data object Filter : Route

    @Serializable
    data class FilterList(val type: ListRoute) : Route

}

@Keep
@Serializable
enum class ListRoute {
    BrandRoute,
    ModelRoute,
    ColorRoute,
    YearRoute
}

fun NavGraphBuilder.baseNavGraph(navController: NavHostController) {
    dialog<Route.Add> {
        Button(
            onClick = {
                navController.popBackStack()
                navController.navigateSingleTop((Route.Report(number = "123"))) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                }
            }
        ) {
            Text("Add")
        }
    }
    dialog<Route.Auth> {
        Text("Auth")
    }
}

fun NavGraphBuilder.historyNavGraph(navController: NavHostController) =
    navigation<Route.HistoryMainGraph>(startDestination = Route.History) {
        reportNavGraph(navController)
        composable<Route.History> {
            Text("History")
        }
    }

fun NavGraphBuilder.recordsNavGraph(navController: NavHostController) =
    navigation<Route.RecordsMainGraph>(startDestination = Route.Records) {
        composable<Route.Records> {
            Text("Records")
        }
        //...
    }

fun NavGraphBuilder.searchNavGraph(navController: NavHostController) =
    navigation<Route.SearchMainGraph>(startDestination = Route.SearchGraph) {
        reportNavGraph(navController)
        navigation<Route.SearchGraph>(
            startDestination = Route.Search
        ) {
            composable<Route.Search> {
                Column {
                    Text("Search")
                    Button({ navController.navigateSingleTop(Route.Filter) }) {
                        Text("Navigate to Filter")
                    }
                }
            }
            composable<Route.Filter> {
                Text("Filter")
            }
            composable<Route.FilterList> {
                Text("FilterList")
            }
        }
    }

fun NavGraphBuilder.settingsNavGraph(navController: NavHostController) =
    navigation<Route.SettingsMainGraph>(startDestination = Route.Settings) {
        composable<Route.Settings> {
            Text("Settings")
        }
        dialog<Route.Socials> {
            Text("Socials")
        }
    }

fun NavGraphBuilder.reportNavGraph(navController: NavHostController) {
    composable<Route.Report> {
        val number = it.toRoute<Route.Report>().number
        Column {
            Text(it.destination.parent?.route ?: "null")
            Text("Report number = $number")
        }
    }
    composable<Route.Notes> {
        Text("Notes")
    }
    navigation<Route.EventsGraph>(
        startDestination = Route.Events::class
    ) {
        composable<Route.Events> {
            Text("Events")
        }
        composable<Route.EditEvent> {
            Text("EditEvent")
        }
        composable<Route.LocationPick> {
            Text("LocationPick")
        }
    }
}

fun NavHostController.navigateToMain(selected: Boolean, route: Route) {
    if (selected) {
        navigateSingleTop(route) {
            if (route != Route.Add) {
                popUpTo(graph.findStartDestination().id)
            }
        }
    } else {
        navigateSingleTop(route) {
            if (route != Route.Add) {
                popUpTo(graph.findStartDestination().id) {
                    saveState = true
                }
            }
            restoreState = true
        }
    }
}

fun NavHostController.navigateSingleTop(
    route: Route,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    navigate(route) {
        this.launchSingleTop = true
        builder?.invoke(this)
    }
}