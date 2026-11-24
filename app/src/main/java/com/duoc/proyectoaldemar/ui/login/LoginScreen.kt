package com.duoc.proyectoaldemar.ui.login

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
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

        // Campo de Correo Electrónico o Código Demo
        AquaTrazTextField(
            value = vm.correo,
            onValueChange = { vm.correo = it },
            label = if (vm.esModoDemo) "Código de Usuario (Demo)" else "Correo Electrónico",
            placeholder = if (vm.esModoDemo) "ej. OP-01, SUP-01, AN-01" else "ejemplo@duoc.cl"
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
            text = if (vm.cargando) "Iniciando sesión..." else if (vm.esModoDemo) "Ingresar (Modo Demo)" else "Ingresar con InsForge",
            enabled = !vm.cargando,
            onClick = {
                vm.ingresar(onSuccess = onSuccess)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Alternar entre InsForge y Modo Demo
        TextButton(
            onClick = {
                vm.esModoDemo = !vm.esModoDemo
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(
                text = if (vm.esModoDemo) "← Volver a Login con InsForge" else "Usar Modo Demo local (sin conexión)",
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        if (vm.esModoDemo) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Usuarios Demo: OP-01 (Operador), SUP-01 (Supervisor) o AN-01 (Analista) con clave 1234",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
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
