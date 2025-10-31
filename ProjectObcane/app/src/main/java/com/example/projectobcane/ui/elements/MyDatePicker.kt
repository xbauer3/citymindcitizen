package com.example.projectobcane.ui.elements

import android.widget.Toast
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.projectobcane.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDatePicker(
    modifier: Modifier = Modifier,
    date: Long? = null,
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = date)
    val context = LocalContext.current

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = { onDismiss() },
        confirmButton = {
            Button(onClick = {
                if (datePickerState.selectedDateMillis != null){
                    onDateSelected(datePickerState.selectedDateMillis!!)
                    onDismiss()
                }
                else{
                    Toast.makeText(context,
                        context.getString(R.string.cannot_save_empty_date_pls_pick), Toast.LENGTH_SHORT).show()
                }
            }
            ) {
                Text(text = stringResource(R.string.select))
            }
        },
        dismissButton = {
            Button(onClick = {
                onDismiss()
            }) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState
        )
    }
}
