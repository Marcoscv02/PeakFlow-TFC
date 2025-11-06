package marcos.peakflow.ui.theme

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class Theme{
    DARK,
    LIGHT,
    SYSTEM
}

class ThemeViewModel: ViewModel() {
    private val _theme = MutableStateFlow(Theme.SYSTEM)
    val theme: StateFlow<Theme> = _theme.asStateFlow()


    fun changeTheme (newTheme: Theme){
        _theme.value = newTheme
    }
}