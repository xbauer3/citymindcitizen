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
import android.app.TimePickerDialog

import java.util.*


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




@Composable
fun MyDateTimePicker(
    initialDateMillis: Long,
    onDateTimeSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance().apply { timeInMillis = initialDateMillis }
    var context = LocalContext.current

    // Show DatePicker first
    MyDatePicker(
        date = initialDateMillis,
        onDateSelected = { selectedDate ->
            // Update calendar with new date but keep existing hours/minutes
            val cal = Calendar.getInstance().apply { timeInMillis = selectedDate }
            calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR))
            calendar.set(Calendar.MONTH, cal.get(Calendar.MONTH))
            calendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH))

            // Then show TimePickerDialog
            val context = context
            val timePickerDialog = TimePickerDialog(
                context,
                { _, hourOfDay, minute ->
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    calendar.set(Calendar.MINUTE, minute)
                    onDateTimeSelected(calendar.timeInMillis)
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
            )
            timePickerDialog.setOnCancelListener { onDismiss() }
            timePickerDialog.show()
        },
        onDismiss = { onDismiss() }
    )
}
