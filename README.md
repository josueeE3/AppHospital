Create table Enfermedad(
UUID_enfermedad Varchar2(50) Primary key,
Nombre Varchar2(50)
);

Create table Num_Habitacion(
UUID_habitacion Varchar2(50) Primary key,
NumHabitacion int
);


Create table Num_cama(
UUID_cama Varchar2(50) Primary key,
NumCama int
);

Create table Medicamento(
UUID_medicamento Varchar2(50) Primary key,
Nombre Varchar2(50)
);


Create table Paciente (
UUID_paciente Varchar2(50) Primary key,
Nombre Varchar2(50),
Apellido Varchar2(50),
Edad Int,
UUID_enfermedad Varchar2(50),
UUID_habitacion Varchar2(50),
UUID_cama Varchar2(50),
UUID_medicamento Varchar2(50),
FechaNacimiento Varchar2(150),
HoraMedicamento Varchar2(150),

Constraint fk_enfermedad FOREIGN KEY (UUID_enfermedad) references Enfermedad(UUID_enfermedad),
Constraint fk_habitacion FOREIGN KEY (UUID_habitacion) references Num_Habitacion(UUID_habitacion),
Constraint fk_cama FOREIGN KEY (UUID_cama) references Num_cama(UUID_cama),
Constraint fk_medicamento FOREIGN KEY (UUID_medicamento) references Medicamento(UUID_medicamento)
);

Select Paciente.Nombre, Paciente.Apellido, Paciente.Edad, Paciente.FechaNacimiento, Enfermedad.Nombre, Num_Habitacion.NumHabitacion,Num_cama.NumCama,Medicamento.Nombre
from Paciente
Inner Join Enfermedad On Paciente.UUID_enfermedad = Enfermedad.UUID_enfermedad
Inner Join Num_Habitacion On Paciente.UUID_habitacion = Num_Habitacion.UUID_habitacion
Inner Join Num_cama On Paciente.UUID_cama = Num_cama.UUID_cama
Inner Join Medicamento On Paciente.UUID_medicamento = Medicamento.UUID_medicamento;
