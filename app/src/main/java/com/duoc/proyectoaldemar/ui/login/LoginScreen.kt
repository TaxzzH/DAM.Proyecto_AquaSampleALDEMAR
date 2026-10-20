package com.duoc.proyectoaldemar.ui.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazButton
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazHeader
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazTextField
import com.duoc.proyectoaldemar.ui.theme.EP2_ProyectoALDEMARTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    AquaTrazScreen {
        AquaTrazHeader()

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de Correo Electrónico
        AquaTrazTextField(
            value = viewModel.email,
            onValueChange = { viewModel.onEmailChanged(it) },
            label = "Correo Electrónico",
            placeholder = "usuario@empresa.com"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña
        AquaTrazTextField(
            value = viewModel.password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = "Contraseña",
            placeholder = "••••••••",
            isPassword = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Ingreso
        AquaTrazButton(
            text = "Ingresar",
            onClick = {
                viewModel.login()
                onLoginSuccess()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    EP2_ProyectoALDEMARTheme {
        LoginScreen(
            onLoginSuccess = {}
        )
    }
}
