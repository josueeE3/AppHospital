package Modelo

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import aplicacion.josuehernandez.myapplication.R
import aplicacion.josuehernandez.myapplication.ui.dashboard.DashboardFragment
import aplicacion.josuehernandez.myapplication.ui.notifications.NotificationsFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AdaptadorPaciente(private var Datos: List<Paciente>) : RecyclerView.Adapter<ViewHolderPaciente>() {


    fun eliminarPaciente(nombre: String, posicion: Int){
        val listadoDatos = Datos.toMutableList()
        listadoDatos.removeAt(posicion)

        GlobalScope.launch(Dispatchers.IO){

            val objConexion = Conexion().cadenaConexion()

            val deletePaciente = objConexion?.prepareStatement("delete Paciente where Nombre = ?")!!
            deletePaciente.setString(1, nombre)
            deletePaciente.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
        Datos = listadoDatos.toList()

        notifyItemRemoved(posicion)
        notifyDataSetChanged()
    }


    fun actualizarListadoDespuesDeEditar(uuid: String, nuevoNombre: String, nuevoApellido: String,
                                         nuevaEdad: String, nuevaEnfermedad: String, nuevaHabitacion:String,
                                         nuevaCama:String, nuevoMedicamento:String,
                                         nuevafechaNacimiento:String, nuevahoraMedicamento:String){

        val identificador = Datos.indexOfFirst { it.uuid == uuid }

        if (identificador != -1){
            Datos[identificador].nombre = nuevoNombre
            Datos[identificador].apellido = nuevoApellido
            Datos[identificador].edad = nuevaEdad
            Datos[identificador].enfermedad = nuevaEnfermedad
            Datos[identificador].habitacion = nuevaHabitacion
            Datos[identificador].cama = nuevaCama
            Datos[identificador].medicamento = nuevoMedicamento
            Datos[identificador].fechaNacimiento = nuevafechaNacimiento
            Datos[identificador].horaMedicamento = nuevahoraMedicamento

            notifyItemChanged(identificador)
        }else{
            Log.e("ActualizarListado", "UUID no encontrado: $uuid")
        }
    }

    fun editarPaciente(uuid: String, nuevoNombre: String, nuevoApellido: String,
                       nuevaEdad: String, nuevaEnfermedad: String, nuevaHabitacion:String,
                       nuevaCama:String, nuevoMedicamento:String,
                       nuevafechaNacimiento:String, nuevahoraMedicamento:String){

        GlobalScope.launch(Dispatchers.IO){
            //1- Creo un objeto de la clase conexion
            val objConexion = Conexion().cadenaConexion()

            val updatePaciente = objConexion?.prepareStatement("update Paciente set Nombre = ? , Apellido = ? , Edad = ? ,UUID_enfermedad = ? ,UUID_habitacion = ? ,UUID_cama = ? ,UUID_medicamento = ? ,FechaNacimiento = ? , HoraMedicamento = ?   where UUID_paciente = ?")!!
            updatePaciente.setString(1, nuevoNombre)
            updatePaciente.setString(2, nuevoApellido)
            updatePaciente.setString(3, nuevaEdad)
            updatePaciente.setString(4, nuevaEnfermedad)
            updatePaciente.setString(5, nuevaHabitacion)
            updatePaciente.setString(6, nuevaCama)
            updatePaciente.setString(7, nuevoMedicamento)
            updatePaciente.setString(8, nuevafechaNacimiento)
            updatePaciente.setString(9, nuevahoraMedicamento)
            updatePaciente.setString(10, uuid)
            updatePaciente.executeUpdate()

            val commit = objConexion.prepareStatement("commit")
            commit.executeUpdate()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPaciente {
        val vista =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.activity_card_paciente, parent, false)

        return ViewHolderPaciente(vista)
    }

    override fun getItemCount() = Datos.size


    override fun onBindViewHolder(holder: ViewHolderPaciente, position: Int) {

        val paciente = Datos[position]
        holder.txtNombrePaciente.text = paciente.nombre
        holder.txtEdadPaciente.text = paciente.edad
        holder.txtEnfermedadPaciente.text = paciente.enfermedad

        holder.imgEditar.setOnClickListener{

            val alertDialogBuilder = AlertDialog.Builder(holder.itemView.context)
            alertDialogBuilder.setTitle("Editar paciente")
            alertDialogBuilder.setMessage("Realize los cambios necesarios al paciente:")

            val layout = LinearLayout(holder.itemView.context)
            layout.orientation = LinearLayout.VERTICAL

            val inputNombre = EditText(holder.itemView.context)
            inputNombre.setText(paciente.nombre)
            layout.addView(inputNombre)

            val inputApellido = EditText(holder.itemView.context)
            inputApellido.setText(paciente.apellido)
            layout.addView(inputApellido)

            val inputEdad = EditText(holder.itemView.context)
            inputEdad.setText(paciente.edad)
            layout.addView(inputEdad)

            var spinnerEnfermedad: Spinner? = null
            var spinnerHabitacion: Spinner? = null
            var spinnerCama: Spinner? = null
            var spinnerMedicamento: Spinner? = null



            GlobalScope.launch(Dispatchers.IO) {
                val enfermedades: List<Enfermedades> = DashboardFragment().getEnfermedad()
                val habitaciones: List<Num_Habitacion> = DashboardFragment().getNumHabitacion()
                val camas: List<Num_cama> = DashboardFragment().getNumCama()
                val medicamentos: List<Medicamento> = DashboardFragment().getMedicamento()

                withContext(Dispatchers.Main) {
                    spinnerEnfermedad = Spinner(holder.itemView.context).apply {
                        adapter = ArrayAdapter(
                            holder.itemView.context,
                            android.R.layout.simple_spinner_item,
                            enfermedades
                        ).apply {
                            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                    }
                    layout.addView(spinnerEnfermedad)

                    spinnerHabitacion = Spinner(holder.itemView.context).apply {
                        adapter = ArrayAdapter(
                            holder.itemView.context,
                            android.R.layout.simple_spinner_item,
                            habitaciones
                        ).apply {
                            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                    }
                    layout.addView(spinnerHabitacion)


                    spinnerCama = Spinner(holder.itemView.context).apply {
                        adapter = ArrayAdapter(
                            holder.itemView.context,
                            android.R.layout.simple_spinner_item,
                            camas
                        ).apply {
                            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                    }
                    layout.addView(spinnerCama)

                    spinnerMedicamento = Spinner(holder.itemView.context).apply {
                        adapter = ArrayAdapter(
                            holder.itemView.context,
                            android.R.layout.simple_spinner_item,
                            medicamentos
                        ).apply {
                            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                        }
                    }
                    layout.addView(spinnerMedicamento)

                }
            }

            val inputFecha = EditText(holder.itemView.context)
            inputFecha.setText(paciente.fechaNacimiento)
            layout.addView(inputFecha)

            val inputHora = EditText(holder.itemView.context)
            inputHora.setText(paciente.horaMedicamento)
            layout.addView(inputHora)

            alertDialogBuilder.setView(layout)

            inputFecha.setOnClickListener {
                val calendario = Calendar.getInstance()
                val anio = calendario.get(Calendar.YEAR)
                val mes = calendario.get(Calendar.MONTH)
                val dia = calendario.get(Calendar.DAY_OF_MONTH)


                val fechaMinima = Calendar.getInstance()
                fechaMinima.set(anio - 18,mes, dia)

                val fechaMaxima = Calendar.getInstance()
                fechaMaxima.set(anio, mes, dia)

                val datePickerDialog = DatePickerDialog(
                    holder.itemView.context,
                    { _, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                        val fechaSeleccionada = "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                        inputFecha.setText(fechaSeleccionada)
                    },
                    anio - 18, mes, dia
                )
                datePickerDialog.datePicker.minDate = fechaMinima.timeInMillis
                datePickerDialog.datePicker.maxDate = fechaMaxima.timeInMillis

                datePickerDialog.show()
            }

            inputHora.setOnClickListener {
                val cal = Calendar.getInstance()
                val hour = cal.get(Calendar.HOUR_OF_DAY)
                val minute = cal.get(Calendar.MINUTE)

                val timePickerDialog = TimePickerDialog(
                    holder.itemView.context,
                    { _: TimePicker, hourOfDay: Int, minute: Int ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)
                        val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                        val formattedTime = format.format(cal.time)
                        inputHora.setText(formattedTime)
                    },
                    hour,
                    minute,
                    false
                )
                timePickerDialog.show()
            }

            alertDialogBuilder.setPositiveButton("Guardar") { dialog, which ->
                val nuevoNombre = inputNombre.text.toString().trim()
                val nuevoApellido = inputApellido.text.toString().trim()
                val nuevaEdad = inputEdad.text.toString().trim()
                val nuevaEnfermedad = spinnerEnfermedad?.selectedItem.toString()
                val nuevaHabitacion = spinnerHabitacion?.selectedItem.toString()
                val nuevaCama = spinnerCama?.selectedItem.toString()
                val nuevoMedicamento = spinnerMedicamento?.selectedItem.toString()
                val nuevaFecha = inputFecha.text.toString().trim()
                val nuevaHora = inputHora.text.toString().trim()


                if (nuevoNombre.isNotEmpty() && nuevoApellido.isNotEmpty() &&
                    nuevaEdad.isNotEmpty() && nuevaEnfermedad.isNotEmpty() &&
                    nuevaFecha.isNotEmpty() && nuevaHora.isNotEmpty()) {

                    editarPaciente(paciente.uuid,nuevoNombre,nuevoApellido,nuevaEdad,nuevaEnfermedad,
                        nuevaHabitacion,nuevaCama,nuevoMedicamento,nuevaFecha,nuevaHora)

                    actualizarListadoDespuesDeEditar(paciente.uuid,nuevoNombre,nuevoApellido,nuevaEdad,nuevaEnfermedad,
                        nuevaHabitacion,nuevaCama,nuevoMedicamento,nuevaFecha,nuevaHora)

                } else {
                    Toast.makeText(holder.itemView.context, "Complete todos los campos", Toast.LENGTH_SHORT).show()
                }
            }

            alertDialogBuilder.setNegativeButton("Cancelar") { dialog, which ->
                dialog.dismiss()
            }

            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

        holder.imgBorrar.setOnClickListener {
            val context = holder.txtNombrePaciente.context

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Eliminar")
            builder.setMessage("¿Estas seguro que deseas eliminar?")

            //botones de mi alerta
            builder.setPositiveButton("Si") { dialog, which ->
                val ticket = Datos[position]
                eliminarPaciente(paciente.nombre, position)
            }

            builder.setNegativeButton("No") { dialog, wich ->
                //Si doy en clic en "No" se cierra la alerta
                dialog.dismiss()
            }

            val dialog = builder.create()
            dialog.show()
        }



        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val fragment = NotificationsFragment()

            // Crear un bundle para pasar datos
            val bundle = Bundle().apply {
                putString("Nombre", paciente.nombre)
                putString("Apellido", paciente.apellido)
                putString("Edad", paciente.edad)
                putString("UUID_enfermedad", paciente.enfermedad)
                putString("UUID_habitacion", paciente.habitacion)
                putString("UUID_cama", paciente.cama)
                putString("UUID_medicamento", paciente.medicamento)
                putString("FechaNacimiento", paciente.fechaNacimiento)
                putString("HoraMedicamento", paciente.horaMedicamento)
            }

            // Asignar el bundle al fragmento
            fragment.arguments = bundle

            // Verifica que el contexto es una instancia de AppCompatActivity
            if (context is AppCompatActivity) {
                context.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit()
            } else {
                Toast.makeText(context, "Error: Context is not AppCompatActivity", Toast.LENGTH_SHORT).show()
            }
        }

    }

}