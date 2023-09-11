package com.example.trabajocrud.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.trabajocrud.R;
import com.example.trabajocrud.database.DatabaseHelper;

public class ListarActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar);

        dbHelper = new DatabaseHelper(this);
        tableLayout = findViewById(R.id.tableLayoutAlumnos);

        // Obtener todos los alumnos de la base de datos
        Cursor cursor = dbHelper.getAllAlumnos();

        // Configurar el encabezado de la tabla
        TableRow headerRow = new TableRow(this);
        TextView idHeader = new TextView(this);
        TextView nombresHeader = new TextView(this);
        TextView apellidosHeader = new TextView(this);
        TextView correoHeader = new TextView(this);
        TextView carreraHeader = new TextView(this);

        idHeader.setText("ID");
        nombresHeader.setText("Nombres");
        apellidosHeader.setText("Apellidos");
        correoHeader.setText("Correo");
        carreraHeader.setText("Carrera");

        headerRow.addView(idHeader);
        headerRow.addView(nombresHeader);
        headerRow.addView(apellidosHeader);
        headerRow.addView(correoHeader);
        headerRow.addView(carreraHeader);

        tableLayout.addView(headerRow);

        // Agregar filas de datos a la tabla
        if (cursor != null && cursor.moveToFirst()) {
            do {
                TableRow dataRow = new TableRow(this);
                TextView idTextView = new TextView(this);
                TextView nombresTextView = new TextView(this);
                TextView apellidosTextView = new TextView(this);
                TextView correoTextView = new TextView(this);
                TextView carreraTextView = new TextView(this);

                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombres = cursor.getString(cursor.getColumnIndexOrThrow("nombres"));
                String apellidos = cursor.getString(cursor.getColumnIndexOrThrow("apellidos"));
                String correo = cursor.getString(cursor.getColumnIndexOrThrow("correo"));
                int carreraId = cursor.getInt(cursor.getColumnIndexOrThrow("carrera_id"));

                // Obtener el nombre de la carrera correspondiente al ID
                String nombreCarrera = dbHelper.getCarreraNameById(carreraId);

                idTextView.setText(String.valueOf(id));
                nombresTextView.setText(nombres);
                apellidosTextView.setText(apellidos);
                correoTextView.setText(correo);
                carreraTextView.setText(nombreCarrera);

                dataRow.addView(idTextView);
                dataRow.addView(nombresTextView);
                dataRow.addView(apellidosTextView);
                dataRow.addView(correoTextView);
                dataRow.addView(carreraTextView);

                tableLayout.addView(dataRow);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }
    }
}