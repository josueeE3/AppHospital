package aplicacion.josuehernandez.myapplication.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import aplicacion.josuehernandez.myapplication.R
import aplicacion.josuehernandez.myapplication.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)

        val nombre = arguments?.getString("Nombre")
        val apellido = arguments?.getString("Apellido")
        val edad = arguments?.getString("Edad")
        val uuidEnfermedad = arguments?.getString("UUID_enfermedad")
        val uuidHabitacion = arguments?.getString("UUID_habitacion")
        val uuidCama = arguments?.getString("UUID_cama")
        val uuidMedicamento = arguments?.getString("UUID_medicamento")
        val fechaNacimiento = arguments?.getString("FechaNacimiento")
        val horaMedicamento = arguments?.getString("HoraMedicamento")



        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}