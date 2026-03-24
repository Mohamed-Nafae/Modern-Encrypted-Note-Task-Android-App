package com.bm.encryptednoteapp.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.bm.encryptednoteapp.domian.util.BiometricPromptManager
import com.bm.encryptednoteapp.presentation.editaddnote.EditAddNoteScreen
import com.bm.encryptednoteapp.presentation.editaddnote.EditAddNotesViewModel
import com.bm.encryptednoteapp.presentation.mynotes.HomeScreen
import com.bm.encryptednoteapp.presentation.mynotes.MyNotesViewModel
import com.bm.encryptednoteapp.ui.theme.EncriptedNotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val promptManager by lazy {
        BiometricPromptManager(activity = this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EncriptedNotesTheme {

                Surface {
                    var isAuthenticated by rememberSaveable { mutableStateOf(false)}
                    val navController = rememberNavController()
                    NavHost(
                        navController,
                        startDestination = "splash"
                    ){
                        composable("splash") {
                            SplashScreen(navController)
                        }

                        composable("first_launch") {
                            FirstLaunchScreen(navController)
                        }

                        composable(route = "home"){
                            val homeScreenViewModel = hiltViewModel<MyNotesViewModel>()
                            HomeScreen(
                                navController,
                                homeScreenViewModel,
                                isAuthenticated,
                                promptManager
                            ) {
                                isAuthenticated = true
                            }
                        }

                        composable(
                            route = "edit_note?noteId={noteId}",
                            arguments = listOf(
                                navArgument(name= "noteId"){
                                    type = NavType.IntType
                                    defaultValue = -1
                                }
                            )
                        ) {
                            val editAddNoteViewModel = hiltViewModel<EditAddNotesViewModel>()
                            EditAddNoteScreen(
                                editAddNoteViewModel,
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                    }
                }
            }
        }
    }
}
