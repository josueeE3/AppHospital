Create table Enfermedad(
UUID_enfermedad Varchar2(50) Primary key,
NombreEnfermedad Varchar2(50) NOT NULL
);

Create table Num_Habitacion(
UUID_habitacion Varchar2(50) Primary key,
NumHabitacion int NOT NULL
);


Create table Num_cama(
UUID_cama Varchar2(50) Primary key,
NumCama int NOT NULL
);

Create table Medicamento(
UUID_medicamento Varchar2(50) Primary key,
NombreMedicamento Varchar2(50) NOT NULL
);

Create table Paciente (
UUID_paciente Varchar2(50) Primary key,
Nombre Varchar2(50) NOT NULL,
Apellido Varchar2(50) NOT NULL,
Edad Int NOT NULL,
UUID_enfermedad Varchar2(50),
UUID_habitacion Varchar2(50) ,
UUID_cama Varchar2(50),
UUID_medicamento Varchar2(50),
FechaNacimiento Varchar2(150) NOT NULL,
HoraMedicamento Varchar2(150) NOT NULL,

Constraint fk_enfermedad FOREIGN KEY (UUID_enfermedad) references Enfermedad(UUID_enfermedad),
Constraint fk_habitacion FOREIGN KEY (UUID_habitacion) references Num_Habitacion(UUID_habitacion),
Constraint fk_cama FOREIGN KEY (UUID_cama) references Num_cama(UUID_cama),
Constraint fk_medicamento FOREIGN KEY (UUID_medicamento) references Medicamento(UUID_medicamento)
);


insert into Enfermedad(UUID_enfermedad,NombreEnfermedad) values (SYS_GUID(),'Dolor de cabeza');
insert into Enfermedad(UUID_enfermedad,NombreEnfermedad) values (SYS_GUID(),'Dolor de estomago');


insert into Num_Habitacion(UUID_habitacion,NumHabitacion) values (SYS_GUID(),1);
insert into Num_Habitacion(UUID_habitacion,NumHabitacion) values (SYS_GUID(),2);

insert into Num_cama(UUID_cama,NumCama) values (SYS_GUID(),001);
insert into Num_cama(UUID_cama,NumCama) values (SYS_GUID(),002);

insert into Medicamento(UUID_medicamento,NombreMedicamento) values (SYS_GUID(), 'Acetaminofen');
insert into Medicamento(UUID_medicamento,NombreMedicamento) values (SYS_GUID(), 'Panadol');


select * from Enfermedad;
select * from Medicamento;
select * from Num_cama;
select * from Num_habitacion;

INSERT INTO Paciente (UUID_paciente, Nombre, Apellido, Edad, UUID_enfermedad, UUID_habitacion, UUID_cama, UUID_medicamento, FechaNacimiento, HoraMedicamento)
VALUES (SYS_GUID(), 'John', 'Doe', 30, '726D11505F2E4CF8874CE61E01477C6B', 'BFA7478AD136441BB83A20D74CBA6EC6', '0906B9034B5E4D2C842C1638163825F9', '1C1A7358AF7D4C4D99F62EF0EF50C804', '01/01/1990', '12:00 PM');


select Paciente.UUID_Paciente, Paciente.Nombre, Paciente.Apellido, Paciente.Edad, 
Enfermedad.Nombre, Num_habitacion.NumHabitacion, Num_cama.NumCama, Medicamento.Nombre, Paciente.FechaNacimiento, 
Paciente.HoraMedicamento from Paciente Inner join Enfermedad on Paciente.UUID_enfermedad = Enfermedad.UUID_enfermedad Inner join Num_habitacion on Paciente.UUID_habitacion = Num_habitacion.UUID_habitacion Inner join Num_cama on Paciente.UUID_cama = Num_cama.UUID_cama Inner join Medicamento on Paciente.UUID_medicamento = Medicamento.UUID_medicamento;

select Paciente.UUID_Paciente, Paciente.Nombre, Paciente.Apellido, Paciente.Edad, Enfermedad.NombreEnfermedad, Num_habitacion.NumHabitacion, Num_cama.NumCama, Medicamento.NombreMedicamento, Paciente.FechaNacimiento, Paciente.HoraMedicamento from Paciente Inner join Enfermedad on Paciente.UUID_enfermedad = Enfermedad.UUID_enfermedad Inner join Num_habitacion on Paciente.UUID_habitacion = Num_habitacion.UUID_habitacion Inner join Num_cama on Paciente.UUID_cama = Num_cama.UUID_cama Inner join Medicamento on Paciente.UUID_medicamento = Medicamento.UUID_medicamento



