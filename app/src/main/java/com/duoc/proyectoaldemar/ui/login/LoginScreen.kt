package com.duoc.proyectoaldemar.ui.login

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.ui.componentes.*
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazTextField

@Composable
fun LoginScreen(
    // Inyectamos el ViewModel
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    AquaTrazScreen {

        /*
        Para esta SCREEN, se podria agrupar todo dentro de una única "COLUMN"
        para mejor organizacion.

        Considerar tambien el uso de la funcion "SPACER".
        */
        AquaTrazHeader()

        Column(modifier = Modifier
            .padding(20.dp)) { }

        // Leemos el valor desde el ViewModel y le notificamos el cambio
        AquaTrazTextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = "Correo Electrónico",
            placeholder = "usuario@empresa.com"
        )

        Column(modifier = Modifier
            .padding(10.dp)) { }

        AquaTrazTextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = "Contraseña",
            placeholder = "••••••••",
            isPassword = true
        )

        Column(modifier = Modifier
            .padding(10.dp)) { }

        AquaTrazButton(
            text = "Ingresar",
            onClick = {
                viewModel.login()
                onLoginSuccess()
            }
        )
    }
}