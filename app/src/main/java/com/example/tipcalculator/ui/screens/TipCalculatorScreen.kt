package com.example.tipcalculator.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tipcalculator.ui.components.ProfileCard
import com.example.tipcalculator.viewmodel.TipCalculatorViewModel
import java.text.NumberFormat

@Composable
fun TipCalculatorScreen(viewModel: TipCalculatorViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Calculadora de Propinas", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = uiState.billAmountInput,
            onValueChange = { viewModel.updateBillAmount(it) },
            label = { Text("Monto de la cuenta") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Text(text = "Propina: ${uiState.tipPercentage.toInt()}%")
        Slider(
            value = uiState.tipPercentage,
            onValueChange = { viewModel.updateTipPercentage(it) },
            valueRange = 0f..30f,
            steps = 29
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Redondear propina")
            Switch(
                checked = uiState.roundUp,
                onCheckedChange = { viewModel.updateRoundUp(it) }
            )
        }
        Text(text = "Dividir entre: ${uiState.splitBy} personas")
        Slider(
            value = uiState.splitBy.toFloat(),
            onValueChange = { viewModel.updateSplitBy(it.toInt()) },
            valueRange = 1f..20f,
            steps = 18
        )

        ResultCard(
            title = "Monto por persona",
            amount = uiState.amountPerPerson
        )

        ResultCard(
            title = "Total de Propina",
            amount = uiState.tipAmount
        )


        ResultCard(
            title = "Total a Pagar",
            amount = uiState.totalAmount
        )

        AnimatedVisibility(visible = uiState.isInfoVisible) {
            ProfileCard(
                name = "Daniela Michell Zúñiga",
                enrollment = "253464"
            )
        }

        Button(
            onClick = { viewModel.reset() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reiniciar")
        }
        Button(
            onClick = { viewModel.showInfo() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (uiState.isInfoVisible) "Ocultar info" else "Mostrar info")
        }
    }
}

@Composable
fun ResultCard(title: String, amount: Double) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(
                text = NumberFormat.getCurrencyInstance().format(amount),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}