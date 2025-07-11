package marcos.peakflow.ui.screens.beforeLogin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import marcos.peakflow.app.PeakFlowApp
import marcos.peakflow.ui.navigation.Home
import marcos.peakflow.ui.navigation.Initial

class SessionViewModel : ViewModel() {

    private val supabase = PeakFlowApp.getClient()

    private val _startDestination = MutableStateFlow<Any?>(null)
    val startDestination = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            supabase.auth.sessionStatus.collect { status ->
                when (status) {
                    is SessionStatus.Authenticated -> _startDestination.value = Home
                    is SessionStatus.NotAuthenticated -> _startDestination.value = Initial
                    is SessionStatus.RefreshFailure -> {
                        _startDestination.value = Initial
                    }
                    else -> {} // En un futuro se implementarán mas estados de sesion aquí
                }
            }
        }
    }
}
