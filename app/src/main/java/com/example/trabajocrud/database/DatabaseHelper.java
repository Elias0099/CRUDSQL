package com.example.trabajocrud.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.trabajocrud.model.Alumno;
import com.example.trabajocrud.model.Carrera;

import java.util.ArrayList;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "registro.db";
    private static final int DATABASE_VERSION = 1;

    // Tabla Carrera
    private static final String TABLE_CARRERA = "carrera";
    private static final String COLUMN_CARRERA_ID = "id";
    private static final String COLUMN_CARRERA_NOMBRE = "nombre";
    private static final String COLUMN_CARRERA_ESTADO = "estado";

    // Tabla Alumno
    private static final String TABLE_ALUMNO = "alumno";
    private static final String COLUMN_ALUMNO_ID = "id";
    private static final String COLUMN_ALUMNO_NOMBRES = "nombres";
    private static final String COLUMN_ALUMNO_APELLIDOS = "apellidos";
    private static final String COLUMN_ALUMNO_CORREO = "correo"; // Nuevo campo
    private static final String COLUMN_ALUMNO_CARRERA_ID = "carrera_id";

    // Sentencias SQL para crear tablas
    private static final String CREATE_TABLE_CARRERA = "CREATE TABLE " + TABLE_CARRERA + " ("
            + COLUMN_CARRERA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_CARRERA_NOMBRE + " TEXT,"
            + COLUMN_CARRERA_ESTADO + " TEXT);";

    private static final String CREATE_TABLE_ALUMNO = "CREATE TABLE " + TABLE_ALUMNO + " ("
            + COLUMN_ALUMNO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_ALUMNO_NOMBRES + " TEXT,"
            + COLUMN_ALUMNO_APELLIDOS + " TEXT,"
            + COLUMN_ALUMNO_CORREO + " TEXT," // Nuevo campo
            + COLUMN_ALUMNO_CARRERA_ID + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tablas
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARRERA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALUMNO);

        // Crear tablas nuevamente
        db.execSQL(CREATE_TABLE_CARRERA);
        db.execSQL(CREATE_TABLE_ALUMNO);

        // Insertar datos iniciales de carrera
        insertarDatosInicialesCarrera(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Si necesitas actualizar la estructura de la base de datos, aquí debes implementar la lógica.
        // Por ejemplo, puedes eliminar tablas existentes y volver a crearlas.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARRERA);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ALUMNO);
        onCreate(db);
    }

    // Métodos para operaciones CRUD de Carrera
    public long insertCarrera(Carrera carrera) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CARRERA_NOMBRE, carrera.getNombre());
        values.put(COLUMN_CARRERA_ESTADO, carrera.getEstado());
        long resultado = db.insert(TABLE_CARRERA, null, values);
        db.close();
        return resultado;
    }

    @SuppressLint("Range")
    public List<Carrera> getAllCarreras() {
        List<Carrera> carreras = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT * FROM " + TABLE_CARRERA, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Carrera carrera = new Carrera();
                    carrera.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_CARRERA_ID)));
                    carrera.setNombre(cursor.getString(cursor.getColumnIndex(COLUMN_CARRERA_NOMBRE)));
                    carrera.setEstado(cursor.getString(cursor.getColumnIndex(COLUMN_CARRERA_ESTADO)));
                    carreras.add(carrera);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return carreras;
    }

    // Métodos para operaciones CRUD de Alumno
    public long insertAlumno(Alumno alumno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALUMNO_NOMBRES, alumno.getNombres());
        values.put(COLUMN_ALUMNO_APELLIDOS, alumno.getApellidos());
        values.put(COLUMN_ALUMNO_CORREO, alumno.getCorreo()); // Nuevo campo
        values.put(COLUMN_ALUMNO_CARRERA_ID, alumno.getCarreraId());
        long resultado = db.insert(TABLE_ALUMNO, null, values);
        db.close();
        return resultado;
    }



    public Cursor getAllAlumnos() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ALUMNO, null);
    }

    private void insertarDatosInicialesCarrera(SQLiteDatabase db) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_CARRERA_NOMBRE, "Sistemas");
        values.put(COLUMN_CARRERA_ESTADO, "Activa");
        db.insert(TABLE_CARRERA, null, values);

        values.put(COLUMN_CARRERA_NOMBRE, "Arquitectura");
        values.put(COLUMN_CARRERA_ESTADO, "Inactiva");
        db.insert(TABLE_CARRERA, null, values);

        values.put(COLUMN_CARRERA_NOMBRE, "Ambiental");
        values.put(COLUMN_CARRERA_ESTADO, "Activa");
        db.insert(TABLE_CARRERA, null, values);

        values.put(COLUMN_CARRERA_NOMBRE, "Medicina");
        values.put(COLUMN_CARRERA_ESTADO, "Activa");
        db.insert(TABLE_CARRERA, null, values);

    }

    public String getCarreraNameById(int carreraId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {COLUMN_CARRERA_NOMBRE};
        String selection = COLUMN_CARRERA_ID + " = ?";
        String[] selectionArgs = {String.valueOf(carreraId)};

        Cursor cursor = db.query(TABLE_CARRERA, projection, selection, selectionArgs, null, null, null);

        String nombreCarrera = "";

        if (cursor != null && cursor.moveToFirst()) {
            nombreCarrera = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARRERA_NOMBRE));
            cursor.close();
        }

        db.close();

        return nombreCarrera;
    }

    public Alumno getAlumnoById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Alumno alumno = null;
        String[] projection = {
                COLUMN_ALUMNO_ID,
                COLUMN_ALUMNO_NOMBRES,
                COLUMN_ALUMNO_APELLIDOS,
                COLUMN_ALUMNO_CORREO,
                COLUMN_ALUMNO_CARRERA_ID
        };
        String selection = COLUMN_ALUMNO_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor cursor = db.query(
                TABLE_ALUMNO,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            int alumnoId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ALUMNO_ID));
            String nombres = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALUMNO_NOMBRES));
            String apellidos = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALUMNO_APELLIDOS));
            String correo = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ALUMNO_CORREO));
            int carreraId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ALUMNO_CARRERA_ID));

            alumno = new Alumno(alumnoId, nombres, apellidos, correo, carreraId);

            cursor.close();
        }

        db.close();

        return alumno;
    }

    // Método para actualizar un alumno por su ID
    public int updateAlumno(Alumno alumno) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ALUMNO_NOMBRES, alumno.getNombres());
        values.put(COLUMN_ALUMNO_APELLIDOS, alumno.getApellidos());
        values.put(COLUMN_ALUMNO_CORREO, alumno.getCorreo());
        values.put(COLUMN_ALUMNO_CARRERA_ID, alumno.getCarreraId());

        // Definir la cláusula WHERE para actualizar el registro específico por su ID
        String whereClause = COLUMN_ALUMNO_ID + " = ?";
        String[] whereArgs = {String.valueOf(alumno.getId())};

        // Realizar la actualización y devolver el número de filas afectadas
        int rowsUpdated = db.update(TABLE_ALUMNO, values, whereClause, whereArgs);
        db.close();
        return rowsUpdated;
    }

    public boolean eliminarAlumno(int idAlumno) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = COLUMN_ALUMNO_ID + " = ?";
        String[] whereArgs = {String.valueOf(idAlumno)};
        int result = db.delete(TABLE_ALUMNO, whereClause, whereArgs);
        db.close();
        return result > 0;
    }



}