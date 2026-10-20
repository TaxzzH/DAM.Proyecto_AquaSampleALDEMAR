package com.duoc.proyectoaldemar.ui.nuevamuestra

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazButton
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazHeader
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazScreen
import com.duoc.proyectoaldemar.ui.componentes.AquaTrazTextField
import com.duoc.proyectoaldemar.ui.theme.EP2_ProyectoALDEMARTheme

@Composable
fun AddSampleScreen(
    viewModel: AddSampleViewModel = viewModel(),
    onSampleAdded: () -> Unit
) {
    AquaTrazScreen {
        AquaTrazHeader()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Registro de Nueva Muestra",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        AquaTrazTextField(
            value = viewModel.centroCultivo,
            onValueChange = { viewModel.onCentroCultivoChanged(it) },
            label = "Centro de Cultivo",
            placeholder = "Ej. Seno Reloncaví Módulo 4"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AquaTrazTextField(
            value = viewModel.cantidadSemillas,
            onValueChange = { viewModel.onCantidadSemillasChanged(it) },
            label = "Cantidad / Conteo de Muestras",
            placeholder = "Ej. 2500"
        )

        Spacer(modifier = Modifier.height(12.dp))

        AquaTrazTextField(
            value = viewModel.observaciones,
            onValueChange = { viewModel.onObservacionesChanged(it) },
            label = "Observaciones de Terreno",
            placeholder = "Ej. Estado saludable, buena densidad"
        )

        Spacer(modifier = Modifier.height(24.dp))

        AquaTrazButton(
            text = "Guardar Muestra",
            onClick = {
                viewModel.guardarMuestra(onSampleAdded)
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddSampleScreenPreview() {
    EP2_ProyectoALDEMARTheme {
        AddSampleScreen(
            onSampleAdded = {}
        )
    }
}
