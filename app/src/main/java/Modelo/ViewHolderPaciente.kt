package Modelo

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import aplicacion.josuehernandez.myapplication.R

class ViewHolderPaciente(view: View): RecyclerView.ViewHolder(view) {

    val txtNombrePaciente: TextView = view.findViewById(R.id.txtNombrePaciente)
    val txtEdadPaciente: TextView = view.findViewById(R.id.txtEdadPaciente)
    val txtEnfermedadPaciente: TextView = view.findViewById(R.id.txtEnfermedadPaciente)
    val imgEditar: ImageView = view.findViewById(R.id.imgEditar)
    val imgBorrar: ImageView = view.findViewById(R.id.imgBorrar)
}