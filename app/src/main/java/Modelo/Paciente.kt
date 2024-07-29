package Modelo

data class Paciente(
    val uuid: String,
    var nombre: String,
    var apellido: String ,
    var edad: String,
    var enfermedad: String,
    var habitacion: String,
    var cama: String,
    var medicamento: String,
    var fechaNacimiento: String,
    var horaMedicamento: String
    )
