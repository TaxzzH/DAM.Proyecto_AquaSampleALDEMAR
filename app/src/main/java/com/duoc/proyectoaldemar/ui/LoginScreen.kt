package com.duoc.proyectoaldemar.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.viewmodel.AuthViewModel

@Composable
fun LoginScreen(onIngreso: () -> Unit, vm: AuthViewModel = viewModel()) {
    Column(
        Modifier.fillMaxSize().systemBarsPadding().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically)
    ) {
        Text("AquaSample - ALDEMAR SpA", style = MaterialTheme.typography.headlineSmall)
        Campo("Código de Usuario (ej. OP-01, SUP-01)", vm.codigo, null) { vm.codigo = it }
        OutlinedTextField(
            value = vm.clave,
            onValueChange = { vm.clave = it },
            label = { Text("Clave") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )
        vm.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = { if (vm.ingresar()) onIngreso() }, modifier = Modifier.fillMaxWidth()) { Text("Ingresar") }
        Text("Prueba: OP-01 (Operador), SUP-01 (Supervisor) o AN-01 (Analista) con clave 1234", style = MaterialTheme.typography.bodySmall)
    }
}