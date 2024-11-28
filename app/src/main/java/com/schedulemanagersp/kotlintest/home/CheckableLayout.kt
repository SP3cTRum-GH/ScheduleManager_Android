package com.schedulemanagersp.kotlintest.home

import android.content.Context
import android.util.AttributeSet
import android.widget.Checkable
import android.widget.RadioButton
import androidx.constraintlayout.widget.ConstraintLayout
import com.schedulemanagersp.kotlintest.R

class CheckableLayout : ConstraintLayout, Checkable {
    constructor(context: Context) : super(context)
    constructor(context: Context, attr: AttributeSet) : super(context, attr)

    override fun setChecked(p0: Boolean) {
        val rb = findViewById<RadioButton>(R.id.radioButton)
        if(rb.isChecked != p0) {
            rb.isChecked = p0
        }
    }

    override fun isChecked(): Boolean {
        return findViewById<RadioButton>(R.id.radioButton).isChecked
    }

    override fun toggle() {
        val rb = findViewById<RadioButton>(R.id.radioButton)
        rb.isChecked != rb.isChecked
    }

}