package aplicacion.josuehernandez.myapplication.ui.dashboard

import Modelo.Conexion
import Modelo.Enfermedades
import Modelo.Medicamento
import Modelo.Num_Habitacion
import Modelo.Num_cama
import Modelo.ViewHolderPaciente
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import aplicacion.josuehernandez.myapplication.R
import aplicacion.josuehernandez.myapplication.databinding.FragmentPacientesBinding
import aplicacion.josuehernandez.myapplication.databinding.FragmentVerPacienteBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class DashboardFragment : Fragment() {

    private lateinit var pacienteViewModel: ViewHolderPaciente


    private var _binding: FragmentPacientesBinding? = null

    fun showTimePickerDialog(textView: EditText) {
        val cal = Calendar.getInstance()
        val hour = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _: TimePicker, hourOfDay: Int, minute: Int ->
                    cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    cal.set(Calendar.MINUTE, minute)
                    val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                    val formattedTime = format.format(cal.time)
                    textView.setText(formattedTime)
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    fun getEnfermedad(): List<Enfermedades>{
        val conexion = Conexion().cadenaConexion()
        val statement = conexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM Enfermedad")!!

        val listadoEnfermedades = mutableListOf<Enfermedades>()

        while (resultSet.next()) {
            val uuid = resultSet.getString("UUID_enfermedad")
            val nombre = resultSet.getString("NombreEnfermedad")

            val enfermedades = Enfermedades(uuid,nombre)

            listadoEnfermedades.add(enfermedades)

        }
        return listadoEnfermedades
    }

    fun getNumHabitacion(): List<Num_Habitacion>{
        val conexion = Conexion().cadenaConexion()
        val statement = conexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM Num_Habitacion")!!

        val listadoHabitacion = mutableListOf<Num_Habitacion>()

        while (resultSet.next()) {
            val uuid = resultSet.getString("UUID_habitacion")
            val numeroHab = resultSet.getString("NumHabitacion")

            val habitaciones = Num_Habitacion(uuid,numeroHab)

            listadoHabitacion.add(habitaciones)
        }
        return listadoHabitacion
    }

    fun getNumCama(): List<Num_cama>{
        val conexion = Conexion().cadenaConexion()
        val statement = conexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM Num_cama")!!

        val listadoCama = mutableListOf<Num_cama>()

        while (resultSet.next()) {
            val uuid = resultSet.getString("UUID_cama")
            val numeroCama = resultSet.getString("NumCama")

            val camas = Num_cama(uuid,numeroCama)

            listadoCama.add(camas)
        }
        return listadoCama
    }

    fun getMedicamento(): List<Medicamento>{
        val conexion = Conexion().cadenaConexion()
        val statement = conexion?.createStatement()
        val resultSet = statement?.executeQuery("SELECT * FROM Medicamento")!!

        val listaMedicamento = mutableListOf<Medicamento>()

        while (resultSet.next()) {
            val uuid = resultSet.getString("UUID_medicamento")
            val nombre = resultSet.getString("NombreMedicamento")

            val medicamentos = Medicamento(uuid,nombre)

            listaMedicamento.add(medicamentos)
        }
        return listaMedicamento
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_pacientes, container, false)

        val txtNombre = root.findViewById<EditText>(R.id.txtNombre)
        val txtApellido = root.findViewById<EditText>(R.id.txtApellido)
        val txtEdad = root.findViewById<EditText>(R.id.txtEdad)
        val spnEnfermedad = root.findViewById<Spinner>(R.id.spnEnfermedad)
        val spnHabitacion = root.findViewById<Spinner>(R.id.spnHabitacion)
        val spnCama = root.findViewById<Spinner>(R.id.spnCama)
        val spnMedicamento = root.findViewById<Spinner>(R.id.spnMedicamento)
        val txtFechaNacmiento = root.findViewById<EditText>(R.id.txtFechaNacimiento)
        val txtHoraMedicamento = root.findViewById<EditText>(R.id.txtHoraMedicamento)

        val btnGuardar = root.findViewById<Button>(R.id.btnCrearPaciente)

        GlobalScope.launch(Dispatchers.IO) {
            val listadoEnfermedad = getEnfermedad()
            val listadoHabitaciones = getNumHabitacion()
            val listadoCama = getNumCama()
            val listadoMedicamento = getMedicamento()

            val enfermedad = listadoEnfermedad.map { it.nombre }
            val habitacion = listadoHabitaciones.map { it.NumHabitacion }
            val cama = listadoCama.map { it.NumCama }
            val medicamento = listadoMedicamento.map { it.nombre }


            withContext(Dispatchers.Main) {
                val enfermedadAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, enfermedad)
                spnEnfermedad.adapter = enfermedadAdapter

                val habitacionAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, habitacion)
                spnHabitacion.adapter = habitacionAdapter

                val campaAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, cama)
                spnCama.adapter = campaAdapter

                val medicamentoAdapter =
                    ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item,medicamento)
                spnMedicamento.adapter = medicamentoAdapter
            }
        }

        txtFechaNacmiento.setOnClickListener {
            val calendario = Calendar.getInstance()
            val anio = calendario.get(Calendar.YEAR)
            val mes = calendario.get(Calendar.MONTH)
            val dia = calendario.get(Calendar.DAY_OF_MONTH)


            val fechaMinima = Calendar.getInstance()
            fechaMinima.set(anio - 18,mes, dia)

            val fechaMaxima = Calendar.getInstance()
            fechaMaxima.set(anio, mes, dia)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, anioSeleccionado, mesSeleccionado, diaSeleccionado ->
                    val fechaSeleccionada =
                        "$diaSeleccionado/${mesSeleccionado + 1}/$anioSeleccionado"
                    txtFechaNacmiento.setText(fechaSeleccionada)
                },
                anio - 18, mes, dia
            )

            datePickerDialog.datePicker.minDate = fechaMinima.timeInMillis

            datePickerDialog.datePicker.maxDate = fechaMaxima.timeInMillis

            datePickerDialog.show()
        }

        txtHoraMedicamento.setOnClickListener {
            showTimePickerDialog(txtHoraMedicamento)
        }


        btnGuardar.setOnClickListener {
            if (txtNombre.text.toString().isEmpty() || txtApellido.text.toString().isEmpty()
                || txtEdad.text.toString().isEmpty()  || txtFechaNacmiento.text.toString().isEmpty()
                || txtHoraMedicamento.text.toString().isEmpty()
            ) {
                Toast.makeText(
                    requireContext(),
                    "Por favor, complete todos los campos.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    //1- Crear un objeto de la clase de conexion
                    try{
                        val objConexion = Conexion().cadenaConexion()

                        val enfermedad = getEnfermedad()
                        val habitacion = getNumHabitacion()
                        val cama = getNumCama()
                        val medicamento = getMedicamento()

                        val addPaciente =
                            objConexion?.prepareStatement("INSERT INTO Paciente (UUID_paciente,Nombre,Apellido,Edad,UUID_enfermedad,UUID_habitacion,UUID_cama,UUID_medicamento,FechaNacimiento,HoraMedicamento) VALUES (?,?,?,?,?,?,?,?,?,?)")!!
                        addPaciente.setString(1, UUID.randomUUID().toString())
                        addPaciente.setString(2, txtNombre.text.toString())
                        addPaciente.setString(3, txtApellido.text.toString())
                        addPaciente.setString(4, txtEdad.text.toString())
                        addPaciente.setString(5, enfermedad[spnEnfermedad.selectedItemPosition].uuid)
                        addPaciente.setString(6, habitacion[spnHabitacion.selectedItemPosition].uuid)
                        addPaciente.setString(7, cama[spnCama.selectedItemPosition].uuid)
                        addPaciente.setString(8, medicamento[spnMedicamento.selectedItemPosition].uuid)
                        addPaciente.setString(9, txtFechaNacmiento.text.toString())
                        addPaciente.setString(10, txtHoraMedicamento.text.toString())

                        addPaciente.executeUpdate()

                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Paciente agendada exitosamente.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                requireContext(),
                                "Error al agendar la cita: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

            }

        }


        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}