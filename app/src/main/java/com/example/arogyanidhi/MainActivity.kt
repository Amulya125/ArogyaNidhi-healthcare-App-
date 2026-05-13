package com.example.arogyanidhi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.arogyanidhi.ui.auth.AuthViewModel
import com.example.arogyanidhi.ui.auth.LoginScreen
import com.example.arogyanidhi.ui.auth.RegisterScreen
import com.example.arogyanidhi.ui.chatbot.ChatbotScreen
import com.example.arogyanidhi.ui.chatbot.ChatbotViewModel
import com.example.arogyanidhi.ui.dashboard.DashboardScreen
import com.example.arogyanidhi.ui.dashboard.DashboardViewModel
import com.example.arogyanidhi.ui.eligibility.EligibilityScreen
import com.example.arogyanidhi.ui.eligibility.EligibilityViewModel
import com.example.arogyanidhi.ui.hospitals.HospitalListScreen
import com.example.arogyanidhi.ui.hospitals.HospitalViewModel
import com.example.arogyanidhi.ui.navigation.Screen
import com.example.arogyanidhi.ui.onboarding.OnboardingScreen
import com.example.arogyanidhi.ui.onboarding.OnboardingViewModel
import com.example.arogyanidhi.ui.profile.ProfileScreen
import com.example.arogyanidhi.ui.profile.ProfileViewModel
import com.example.arogyanidhi.ui.schemes.SchemeDetailScreen
import com.example.arogyanidhi.ui.schemes.SchemeDetailViewModel
import com.example.arogyanidhi.ui.schemes.SchemeListScreen
import com.example.arogyanidhi.ui.schemes.SchemeViewModel
import com.example.arogyanidhi.ui.settings.SettingsScreen
import com.example.arogyanidhi.ui.settings.SettingsViewModel
import com.example.arogyanidhi.ui.splash.SplashViewModel
import com.example.arogyanidhi.ui.theme.ArogyaNidhiTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { ArogyaNidhiTheme { ArogyaNidhiMain() } }
    }
}

@Composable
fun ArogyaNidhiMain() {
    val navController = rememberNavController()
    val splashViewModel: SplashViewModel = hiltViewModel()
    val startDestination by splashViewModel.startDestination.collectAsState()
    val authViewModel: AuthViewModel = hiltViewModel()

    if (startDestination == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    NavHost(navController = navController, startDestination = startDestination!!) {
        composable<Screen.Onboarding> {
            val vm: OnboardingViewModel = hiltViewModel()
            OnboardingScreen(onContinue = {
                vm.completeOnboarding()
                navController.navigate(Screen.Login) { popUpTo(Screen.Onboarding) { inclusive = true } }
            })
        }
        composable<Screen.Login> {
            LoginScreen(viewModel = authViewModel,
                onNavigateToRegister = { navController.navigate(Screen.Register) },
                onLoginSuccess = { navController.navigate(Screen.Dashboard) { popUpTo(Screen.Login) { inclusive = true } } })
        }
        composable<Screen.Register> {
            RegisterScreen(viewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.Login) },
                onRegisterSuccess = { navController.navigate(Screen.Dashboard) { popUpTo(Screen.Register) { inclusive = true } } })
        }
        composable<Screen.Dashboard> {
            val vm: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel = vm,
                onNavigateToEligibility = { navController.navigate(Screen.EligibilityChecker) },
                onNavigateToSchemes = { navController.navigate(Screen.Schemes) },
                onNavigateToHospitals = { navController.navigate(Screen.Hospitals) },
                onNavigateToProfile = { navController.navigate(Screen.Profile) },
                onNavigateToSettings = { navController.navigate(Screen.Settings) },
                onNavigateToChatbot = { navController.navigate(Screen.Chatbot) }
            )
        }
        composable<Screen.Settings> {
            val vm: SettingsViewModel = hiltViewModel()
            SettingsScreen(viewModel = vm,
                onNavigateBack = { navController.popBackStack() },
                onLogout = { navController.navigate(Screen.Login) { popUpTo(0) { inclusive = true } } })
        }
        composable<Screen.EligibilityChecker> {
            val vm: EligibilityViewModel = hiltViewModel()
            EligibilityScreen(viewModel = vm, onNavigateBack = { navController.popBackStack() })
        }
        composable<Screen.Schemes> {
            val vm: SchemeViewModel = hiltViewModel()
            SchemeListScreen(viewModel = vm, onNavigateBack = { navController.popBackStack() },
                onSchemeClick = { navController.navigate(Screen.SchemeDetail(it)) })
        }
        composable<Screen.SchemeDetail> { back ->
            val route: Screen.SchemeDetail = back.toRoute()
            val vm: SchemeDetailViewModel = hiltViewModel()
            SchemeDetailScreen(schemeId = route.schemeId, viewModel = vm, onNavigateBack = { navController.popBackStack() })
        }
        composable<Screen.Hospitals> {
            val vm: HospitalViewModel = hiltViewModel()
            HospitalListScreen(viewModel = vm, onNavigateBack = { navController.popBackStack() })
        }
        composable<Screen.Profile> {
            val vm: ProfileViewModel = hiltViewModel()
            ProfileScreen(viewModel = vm, onNavigateBack = { navController.popBackStack() })
        }
        composable<Screen.Chatbot> {
            val vm: ChatbotViewModel = hiltViewModel()
            ChatbotScreen(viewModel = vm, onNavigateBack = { navController.popBackStack() })
        }
    }
}
