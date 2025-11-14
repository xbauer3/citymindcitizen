package com.example.projectobcane.screens.reports.addEdit

import com.example.projectobcane.database.reports.Report


interface AddEditReportScreenActions {


    fun onTitleChanged(value: String)
    fun onDescriptionChanged(value: String)
    fun onCategoryChanged(value: String)
    fun onStatusChanged(value: String)



    //fun onPhotoChanged(value: String)



    fun saveReport()
    fun deleteReport()

}
