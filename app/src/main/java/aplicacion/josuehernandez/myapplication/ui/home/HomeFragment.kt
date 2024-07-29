package aplicacion.josuehernandez.myapplication.ui.home

import Modelo.AdaptadorPaciente
import Modelo.Conexion
import Modelo.Paciente
import Modelo.ViewHolderPaciente
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import aplicacion.josuehernandez.myapplication.R
import aplicacion.josuehernandez.myapplication.databinding.FragmentPacientesBinding
import aplicacion.josuehernandez.myapplication.databinding.FragmentVerPacienteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptadorPaciente: AdaptadorPaciente


    fun obtenerDatos(): List<Paciente> {
        val listadoPaciente = mutableListOf<Paciente>()
        try {
            val objConexion = Conexion().cadenaConexion()
            val statement = objConexion?.createStatement()
            val resultSet = statement?.executeQuery("select Paciente.UUID_Paciente, Paciente.Nombre, Paciente.Apellido, Paciente.Edad, Enfermedad.NombreEnfermedad, Num_habitacion.NumHabitacion, Num_cama.NumCama, Medicamento.NombreMedicamento, Paciente.FechaNacimiento, Paciente.HoraMedicamento from Paciente Inner join Enfermedad on Paciente.UUID_enfermedad = Enfermedad.UUID_enfermedad Inner join Num_habitacion on Paciente.UUID_habitacion = Num_habitacion.UUID_habitacion Inner join Num_cama on Paciente.UUID_cama = Num_cama.UUID_cama Inner join Medicamento on Paciente.UUID_medicamento = Medicamento.UUID_medicamento")!!

            while (resultSet.next()) {
                val uuid = resultSet.getString("UUID_Paciente")
                val nombre = resultSet.getString("Nombre")
                val apellido = resultSet.getString("Apellido")
                val edad = resultSet.getString("Edad")
                val enfermedad = resultSet.getString("NombreEnfermedad")
                val habitacion = resultSet.getString("NumHabitacion")
                val cama = resultSet.getString("NumCama")
                val medicamento = resultSet.getString("NombreMedicamento")
                val fechaNacimiento = resultSet.getString("FechaNacimiento")
                val horaMedicamento = resultSet.getString("HoraMedicamento")


                val paciente = Paciente(uuid, nombre, apellido, edad, enfermedad, habitacion, cama, medicamento, fechaNacimiento, horaMedicamento)
                listadoPaciente.add(paciente)
            }
        } catch (e: Exception) {
            Log.e("Error", "Error al cargar pacientes", e)
        }
        return listadoPaciente
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_ver_paciente, container, false)
        val rcvPaciente = root.findViewById<RecyclerView>(R.id.rcvPaciente)
        rcvPaciente.layoutManager = LinearLayoutManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            val pacienteBD = obtenerDatos()
            withContext(Dispatchers.Main) {
                val adapter = AdaptadorPaciente(pacienteBD)
                rcvPaciente.adapter = adapter
            }
        }

        return root
    }


}