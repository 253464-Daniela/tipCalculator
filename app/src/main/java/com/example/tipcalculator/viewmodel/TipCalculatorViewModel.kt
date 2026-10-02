package com.example.tipcalculator.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlin.math.ceil

data class TipUiState(
    val billAmountInput: String = "",
    val tipPercentage: Float = 15f,
    val splitBy: Int = 1,
    val roundUp: Boolean = false,
    val tipAmount: Double = 0.0,
    val totalAmount: Double = 0.0,
    val amountPerPerson: Double = 0.0
)

class TipCalculatorViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TipUiState())
    val uiState: StateFlow<TipUiState> = _uiState.asStateFlow()

    fun updateBillAmount(amount: String) {
        val validatedAmount = amount.replace(",", ".")
        if (validatedAmount.count { it == '.' } <= 1) {
            _uiState.update { it.copy(billAmountInput = validatedAmount) }
            calculateValues()
        }
    }

    fun updateTipPercentage(percentage: Float) {
        _uiState.update { it.copy(tipPercentage = percentage) }
        calculateValues()
    }

    fun updateSplitBy(people: Int) {
        _uiState.update { it.copy(splitBy = people) }
        calculateValues()
    }

    fun updateRoundUp(roundUp: Boolean) {
        _uiState.update { it.copy(roundUp = roundUp) }
        calculateValues()
    }

    fun reset() {
        _uiState.value = TipUiState()
    }

    private fun calculateValues() {
        val currentState = _uiState.value
        val billAmount = currentState.billAmountInput.toDoubleOrNull() ?: 0.0

        var tip = billAmount * (currentState.tipPercentage / 100)

        if (currentState.roundUp) {
            tip = ceil(tip)
        }

        val total = billAmount + tip
        val perPerson = if (currentState.splitBy > 0) total / currentState.splitBy else 0.0

        _uiState.update {
            it.copy(
                tipAmount = tip,
                totalAmount = total,
                amountPerPerson = perPerson
            )
        }
    }
}