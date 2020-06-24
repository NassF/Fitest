package com.example.fitest

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_body_params_edit.*



class Params_Edit : AppCompatActivity() {

    private fun showSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }

    private fun hideSystemUI() {

        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_body_params_edit)

        loadData()
        editWeight.limitLength()
        editShoulder.limitLength()
        editTall.limitLength()
        editBreast.limitLength()
        editBiceps.limitLength()
        editWaist.limitLength()
        editButtocks.limitLength()
        editHip.limitLength()
    }

    private fun loadData(){

        Firebase.auth.currentUser?.uid?.let {
            val up =
                ddb.collection("sportsmen")
                    .document(it)
            up
                .addSnapshotListener { snapshot, e ->
                    if (e != null) {
                        Toast.makeText(
                            baseContext, "Считать неудалось$e",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@addSnapshotListener
                    }
                    if (snapshot != null && snapshot.exists()) {

                        textWeight.text = snapshot.getString("weight")
                        textShoulder.text= snapshot.getString("shoulder")
                        textBreast.text= snapshot.getString("breast")
                        textBiceps.text= snapshot.getString("biceps")
                        textWaist.text= snapshot.getString("waist")
                        textButtocks.text=snapshot.getString("buttock")
                        textHip.text= snapshot.getString("hip")
                    }
                    else {
                        Toast.makeText(
                            baseContext, "Нет данных",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
        }
    }
    private fun EditText.limitLength() {
        this.filters = arrayOf(LengthFilter(4))
    }


    fun paramEditClick(view: View) {
        when (view.id) {
            R.id.toolbarProf2 -> {
                startActivity(Intent(this, Params_Sportsmen::class.java))
            }
            R.id.button_save -> {
                editParam()

            }

        }
    }
    private val ddb = FirebaseFirestore.getInstance()
    private fun editParam(){

        Firebase.auth.currentUser?.uid?.let {
            val up =
                ddb.collection("sportsmen")
                    .document(it)

            if(editWeight.text.toString().isNotEmpty()){
                editWeight.error = "Введите данные"
                editWeight.requestFocus()
                return
            }
            if(editShoulder.text.toString().isNotEmpty()){
                editShoulder.error = "Введите данные"
                editShoulder.requestFocus()
                return
            }
            if(editBreast.text.toString().isNotEmpty()){
                editBreast.error = "Введите данные"
                editBreast.requestFocus()
                return
            }
            if(editButtocks.text.toString().isNotEmpty()){
                editButtocks.error = "Введите данные"
                editButtocks.requestFocus()
                return
            }
            if(editHip.text.toString().isNotEmpty()){
                editHip.error = "Введите данные"
                editHip.requestFocus()
                return
            }
            if(editWaist.text.toString().isNotEmpty()){
                editWaist.error = "Введите данные"
                editWaist.requestFocus()
                return
            }
            if(editBiceps.text.toString().isNotEmpty()){
                editBiceps.error = "Введите данные"
                editBiceps.requestFocus()
                return
            }
            if(editTall.text.toString().isNotEmpty()){
                up
                    .update(
                        "height",editTall.text.toString())
                    .addOnSuccessListener {
                    }
            }
            else {
                up
                    .update(
                        "weight2", editWeight.text.toString(),
                        "shoulder2", editShoulder.text.toString(),
                        "breast2", editBreast.text.toString(),
                        "buttock2", editButtocks.text.toString(),
                        "hip2", editHip.text.toString(),
                        "waist2", editWaist.text.toString(),
                        "biceps2", editBiceps.text.toString()
                    )
                    .addOnSuccessListener {
                        Toast.makeText(
                            baseContext, "Так держать!",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, Params_Sportsmen::class.java))
                    }

            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }
}


