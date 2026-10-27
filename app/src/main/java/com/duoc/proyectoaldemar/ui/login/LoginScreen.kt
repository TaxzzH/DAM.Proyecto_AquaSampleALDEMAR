package com.duoc.proyectoaldemar.ui.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazButton
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazHeader
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazTextField
import com.duoc.proyectoaldemar.ui.theme.EP2_ProyectoALDEMARTheme
import com.duoc.proyectoaldemar.viewmodel.AuthViewModel

@Composable
fun LoginScreen(
    onIngreso: () -> Unit = {},
    onLoginSuccess: (() -> Unit)? = null,
    vm: AuthViewModel = viewModel()
) {
    val onSuccess = onLoginSuccess ?: onIngreso

    AquaTrazScreen {
        AquaTrazHeader()

        Spacer(modifier = Modifier.height(24.dp))

        // Campo de Código de Usuario
        AquaTrazTextField(
            value = vm.codigo,
            onValueChange = { vm.codigo = it },
            label = "Código de Usuario",
            placeholder = "ej. OP-01, SUP-01, AN-01"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Clave
        AquaTrazTextField(
            value = vm.clave,
            onValueChange = { vm.clave = it },
            label = "Clave",
            placeholder = "••••••••",
            isPassword = true
        )

        vm.error?.let { errorMsg ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMsg,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Botón de Ingreso
        AquaTrazButton(
            text = "Ingresar",
            onClick = {
                if (vm.ingresar()) {
                    onSuccess()
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Prueba: OP-01 (Operador), SUP-01 (Supervisor) o AN-01 (Analista) con clave 1234",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    EP2_ProyectoALDEMARTheme {
        LoginScreen(
            onIngreso = {}
        )
    }
}
