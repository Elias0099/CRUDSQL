package com.example.trabajocrud.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.example.trabajocrud.R;
import com.example.trabajocrud.database.DatabaseHelper;
import com.example.trabajocrud.model.Alumno;
import com.example.trabajocrud.model.Carrera;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private EditText nombreEditText, apellidoEditText, correoEditText, idEditText; // Agregamos el campo de correo y el campo ID
    private Spinner carreraSpinner;
    private List<Carrera> carreras;
    private ArrayAdapter<Carrera> carreraAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);

        nombreEditText = findViewById(R.id.nombretext);
        apellidoEditText = findViewById(R.id.apellidostext);
        correoEditText = findViewById(R.id.correotext); // Referencia al campo de correo
        carreraSpinner = findViewById(R.id.carrera);
        idEditText = findViewById(R.id.buscartext); // Referencia al campo de ID

        Button registrarButton = findViewById(R.id.button);
        Button listarButton = findViewById(R.id.button3);

        // Inicializar la lista de carreras y el adaptador del Spinner
        carreras = new ArrayList<>();
        carreraAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, carreras);
        carreraAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        carreraSpinner.setAdapter(carreraAdapter);

        // Manejar la selección de la carrera en el Spinner
        carreraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Obtener la carrera seleccionada
                Carrera carreraSeleccionada = (Carrera) parent.getItemAtPosition(position);
                // Obtener el ID de la carrera seleccionada
                int carreraId = carreraSeleccionada.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada si no se selecciona nada
            }
        });

        cargarCarreras();

        registrarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarAlumno();
            }
        });

        Button eliminarButton = findViewById(R.id.eliminarButton);

        eliminarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarAlumno();
            }
        });

        listarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar el Intent para abrir la actividad de listado
                Intent intent = new Intent(MainActivity.this, ListarActivity.class);
                startActivity(intent);
            }
        });

        // Agregar funcionalidad al botón de búsqueda
        Button buscarButton = findViewById(R.id.buscarButton);
        buscarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buscarAlumnoPorId();
            }
        });

        Button actualizarButton = findViewById(R.id.actualizarButton);
        actualizarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarAlumno();
            }
        });
    }


    private int obtenerIdAlumnoSeleccionado() {
        // Aquí debes implementar la lógica para obtener el ID del alumno seleccionado,
        // por ejemplo, desde un ListView o RecyclerView que muestra la lista de alumnos.
        // Retorna -1 si no se selecciona ningún alumno.
        // Reemplaza este valor de retorno con la lógica real.
        int idAlumnoSeleccionado = -1;

        // Implementa la lógica para obtener el ID del alumno seleccionado aquí.

        return idAlumnoSeleccionado;
    }

    // Método para buscar y mostrar los detalles del alumno para actualizar
    private void buscarYMostrarDetallesDelAlumno(int idAlumno) {
        // Utiliza el ID para buscar el alumno en la base de datos
        Alumno alumno = dbHelper.getAlumnoById(idAlumno);

        // Verifica si se encontró el alumno
        if (alumno != null) {
            // Muestra los detalles del alumno en los campos de entrada
            idEditText.setText(String.valueOf(alumno.getId()));
            nombreEditText.setText(alumno.getNombres());
            apellidoEditText.setText(alumno.getApellidos());
            correoEditText.setText(alumno.getCorreo());

            // Configura el Spinner para mostrar la carrera actual del alumno
            int carreraId = alumno.getCarreraId();
            // Encuentra la posición del ID de la carrera en la lista de carreras y selecciónala en el Spinner
            int posicionCarrera = encontrarPosicionCarreraEnSpinner(carreraId);
            carreraSpinner.setSelection(posicionCarrera);
        } else {
            // No se encontró el alumno con el ID proporcionado, muestra un mensaje de error
            Toast.makeText(this, "No se encontró el alumno con el ID especificado.", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para encontrar la posición de la carrera en el Spinner
    private int encontrarPosicionCarreraEnSpinner(int carreraId) {
        for (int i = 0; i < carreras.size(); i++) {
            if (carreras.get(i).getId() == carreraId) {
                return i;
            }
        }
        return 0; // Valor predeterminado si no se encuentra la carrera
    }


    private void actualizarAlumno() {
        // Obtén los valores actualizados de los campos de entrada
        int idAlumno = Integer.parseInt(idEditText.getText().toString());
        String nuevosNombres = nombreEditText.getText().toString();
        String nuevosApellidos = apellidoEditText.getText().toString();
        String nuevoCorreo = correoEditText.getText().toString();
        Carrera carreraSeleccionada = (Carrera) carreraSpinner.getSelectedItem();
        int nuevoIdCarrera = carreraSeleccionada.getId();

        // Crea un objeto Alumno con los nuevos valores
        Alumno alumnoActualizado = new Alumno(idAlumno, nuevosNombres, nuevosApellidos, nuevoCorreo, nuevoIdCarrera);

        // Actualiza el alumno en la base de datos
        int filasActualizadas = dbHelper.updateAlumno(alumnoActualizado);

        if (filasActualizadas > 0) {
            // La actualización fue exitosa, muestra un mensaje de éxito
            Toast.makeText(this, "Alumno actualizado con éxito.", Toast.LENGTH_SHORT).show();
        } else {
            // No se actualizó ningún registro, muestra un mensaje de error
            Toast.makeText(this, "No se pudo actualizar el alumno.", Toast.LENGTH_SHORT).show();
        }
    }



    // Cargar la lista de carreras desde la base de datos
    private void cargarCarreras() {
        // Limpia la lista de carreras antes de agregar nuevas
        carreras.clear();

        // Obtén las carreras desde la base de datos usando tu dbHelper
        List<Carrera> carrerasDesdeBD = dbHelper.getAllCarreras();

        // Agrega las carreras obtenidas a la lista
        carreras.addAll(carrerasDesdeBD);

        // Notifica al adaptador que los datos han cambiado
        carreraAdapter.notifyDataSetChanged();

        // Agrega logs de depuración para verificar si carrerasDesdeBD contiene datos
        for (Carrera carrera : carrerasDesdeBD) {
            Log.d("Carrera", "ID: " + carrera.getId() + ", Nombre: " + carrera.getNombre());
        }
    }

    private void registrarAlumno() {
        String nombres = nombreEditText.getText().toString();
        String apellidos = apellidoEditText.getText().toString();
        String correo = correoEditText.getText().toString(); // Obtén el valor del campo de correo

        // Obtener la carrera seleccionada del Spinner
        Carrera carreraSeleccionada = (Carrera) carreraSpinner.getSelectedItem();

        if (carreraSeleccionada != null) {
            int carreraId = carreraSeleccionada.getId();
            Alumno alumno = new Alumno(0, nombres, apellidos, correo, carreraId);

            long resultado = dbHelper.insertAlumno(alumno);

            if (resultado != -1) {
                Toast.makeText(this, "Alumno registrado con éxito.", Toast.LENGTH_SHORT).show();
                // Limpiar campos de entrada después de registrar
                nombreEditText.setText("");
                apellidoEditText.setText("");
                correoEditText.setText(""); // Limpiar el campo de correo
                // También puedes restablecer la selección en el Spinner
                carreraSpinner.setSelection(0);
            } else {
                Toast.makeText(this, "Error al registrar alumno.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Selecciona una carrera válida.", Toast.LENGTH_SHORT).show();
        }
    }

    private void eliminarAlumno() {
        int idAlumno = Integer.parseInt(idEditText.getText().toString());
        boolean eliminado = dbHelper.eliminarAlumno(idAlumno);

        if (eliminado) {
            Toast.makeText(this, "Alumno eliminado con éxito.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error al eliminar alumno.", Toast.LENGTH_SHORT).show();
        }
    }




    // Método para buscar un alumno por su ID
    private void buscarAlumnoPorId() {
        // Obtener el ID ingresado por el usuario
        String idString = idEditText.getText().toString();

        if (!idString.isEmpty()) {
            int id = Integer.parseInt(idString);

            // Consulta a la base de datos para obtener el alumno por ID
            Alumno alumno = dbHelper.getAlumnoById(id);

            if (alumno != null) {
                // Mostrar los datos en los campos EditText y el Spinner
                nombreEditText.setText(alumno.getNombres());
                apellidoEditText.setText(alumno.getApellidos());
                correoEditText.setText(alumno.getCorreo());

                // Seleccionar la carrera correspondiente en el Spinner
                for (int i = 0; i < carreras.size(); i++) {
                    if (carreras.get(i).getId() == alumno.getCarreraId()) {
                        carreraSpinner.setSelection(i);
                        break;
                    }
                }
            } else {
                // Limpiar los campos si no se encontró ningún alumno con ese ID
                nombreEditText.setText("");
                apellidoEditText.setText("");
                correoEditText.setText("");
                carreraSpinner.setSelection(0); // Puedes seleccionar la primera carrera o dejarla en blanco
                Toast.makeText(this, "No se encontró ningún alumno con ese ID.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ingresa un ID válido.", Toast.LENGTH_SHORT).show();
        }
    }
}