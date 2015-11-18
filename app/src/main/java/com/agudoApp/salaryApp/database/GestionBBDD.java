package com.agudoApp.salaryApp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.agudoApp.salaryApp.R;
import com.agudoApp.salaryApp.model.Categoria;
import com.agudoApp.salaryApp.model.Cuenta;
import com.agudoApp.salaryApp.model.Movimiento;
import com.agudoApp.salaryApp.model.Recibo;
import com.agudoApp.salaryApp.model.Tarjeta;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GestionBBDD {

    private final String INFO = "INFO";
    String sqlCreateMovimientos = "CREATE TABLE IF NOT EXISTS [Movimientos] ( "
            + "[idMovimiento] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + "[tipo] VARCHAR(1)  NOT NULL, [cantidad] FLOAT NOT NULL,"
            + "[descripcion] TEXT NOT NULL, [idCategoria] INTEGER  NULL,"
            + "[idSubcategoria] INTEGER  NULL, [fecha] DATE NOT NULL, "
            + "[mes] INTEGER NULL, [anio] INTEGER NULL, "
            + "[recibo] BOOLEAN  NOT NULL, " + "[tarjeta] BOOLEAN NOT NULL, "
            + "[idTarjeta] INTEGER NULL, [idRecibo] INTEGER NULL,"
            + "[idCuenta] INTEGER NULL)";

    String sqlCreateCategorias = "CREATE TABLE IF NOT EXISTS [Categorias] ( "
            + "[idCategoria] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + "[descripcion] TEXT NOT NULL, " + "[idIcon] INTEGER NULL)";

    String sqlCreateSubcategorias = "CREATE TABLE IF NOT EXISTS [Subcategorias] ( "
            + "[idSubcategoria] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + "[descripcion] TEXT NOT NULL, " + "[idIcon] INTEGER NULL)";

    String sqlCreateTarjetas = "CREATE TABLE IF NOT EXISTS [Tarjetas] ( "
            + "[idTarjeta] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + "[nombre] TEXT NOT NULL, [limite] FLOAT NOT NULL, "
            + "[tipo] INTEGER  NOT NULL, [idIcon] INTEGER  NOT NULL)";

    String sqlCreateRecibos = "CREATE TABLE IF NOT EXISTS [Recibos] ( "
            + "[idRecibo] INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + "[cantidad] FLOAT NOT NULL, [descripcion] TEXT NOT NULL,"
            + "[idCategoria] INTEGER NULL, [idSubcategoria] INTEGER NULL,"
            + "[fechaIni] DATE NOT NULL, [fechaFin] DATE NOT NULL,"
            + "[tarjeta] BOOLEAN NOT NULL, [idTarjeta] INTEGER NULL, [idCuenta] INTEGER NULL, "
            + "[tipo] INTEGER NOT NULL, [nVeces] INTEGET NOT NULL)";

    String sqlCreateCuentas = "CREATE TABLE IF NOT EXISTS [Cuentas] ( "
            + "[idCuenta] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, "
            + "[nombre] TEXT NOT NULL, [idIcon] INTEGER NOT NULL)";

    Locale locale = Locale.getDefault();
    String languaje = locale.getLanguage();

    public void actualizarBD(SQLiteDatabase db) {
        db.execSQL(sqlCreateTarjetas);
        db.execSQL(sqlCreateRecibos);

        Cursor c3 = db.query("Tarjetas", new String[]{"idTarjeta"}, null,
                null, null, null, null);

        if (!c3.moveToFirst()) {
            Log.d(INFO, "Se van a insertar los campos de la "
                    + "tabla Tarjetas");

            if (languaje.equals("es") || languaje.equals("es-rUS")
                    || languaje.equals("ca")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Tarjeta 1')");
            } else if (languaje.equals("fr")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Carte de crédit 1')");
            } else if (languaje.equals("de")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Kreditkarte 1')");
            } else if (languaje.equals("en")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Credit card 1')");
            } else if (languaje.equals("it")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Carta di credito 1')");
            } else if (languaje.equals("pt")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Cartão de crédito 1')");
            } else {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Credit card 1')");
            }

            Log.d(INFO, "Los registros se han insertado correctamente");
        } else {
            if (c3 != null && !c3.isClosed()) {
                c3.close();
            }
        }

        db.execSQL("ALTER TABLE Movimientos ADD COLUMN idTarjeta INTEGER NULL");
        db.execSQL("ALTER TABLE Movimientos ADD COLUMN idRecibo INTEGER NULL");

        db.execSQL("UPDATE Movimientos	SET idTarjeta=0	WHERE tarjeta='true'");

    }

    public void actualizarBDCuentas(SQLiteDatabase db) {
        db.execSQL(sqlCreateCuentas);

        Cursor c1 = db.query("Cuentas", new String[]{"idCuenta"}, null,
                null, null, null, null);

        if (!c1.moveToFirst()) {
            Log.d(INFO, "Se van a insertar los campos de la " + "tabla Cuentas");
            if (languaje.equals("es") || languaje.equals("es-rUS")
                    || languaje.equals("ca")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Cuenta principal', 0)");
            } else if (languaje.equals("fr")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Compte principal', 0)");
            } else if (languaje.equals("de")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Master-Account', 0)");
            } else if (languaje.equals("en")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Master account', 0)");
            } else if (languaje.equals("it")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Account principale', 0)");
            } else if (languaje.equals("pt")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Conta principal', 0)");
            } else {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Master account', 0)");
            }

            Log.d(INFO, "Los registros se han insertado correctamente");
        } else {
            if (c1 != null && !c1.isClosed()) {
                c1.close();
            }
        }

        db.execSQL("ALTER TABLE Movimientos ADD COLUMN idCuenta INTEGER NULL");
        db.execSQL("ALTER TABLE Recibos ADD COLUMN idCuenta INTEGER NULL");

        db.execSQL("UPDATE Movimientos SET idCuenta=0");
        db.execSQL("UPDATE Recibos SET idCuenta=0");
    }

    public void actualizarVersion30(SQLiteDatabase db) {
        try {
            db.execSQL("ALTER TABLE Categorias ADD COLUMN idIcon INTEGER NULL");
            db.execSQL("ALTER TABLE Subcategorias ADD COLUMN idIcon INTEGER NULL");
            db.execSQL("ALTER TABLE Tarjetas ADD COLUMN limite FLOAT NULL");

            db.execSQL("UPDATE Categorias SET idIcon=0");
            db.execSQL("UPDATE Tarjetas SET limite=0");
        } catch (SQLException e) {
            Log.d("ERROR", "Error al actualizar la BD");
        }
    }

    public void actualizarVersion40(SQLiteDatabase db) {
        try {
            db.execSQL("ALTER TABLE Tarjetas ADD COLUMN tipo INTEGER NULL");
            db.execSQL("ALTER TABLE Tarjetas ADD COLUMN idIcon INTEGER NULL");
            db.execSQL("ALTER TABLE Recibos ADD COLUMN tipo INTEGER NULL");
            db.execSQL("ALTER TABLE Recibos ADD COLUMN nVeces INTEGER NULL");
            db.execSQL("ALTER TABLE Cuentas ADD COLUMN idIcon INTEGER NULL");

            db.execSQL("UPDATE Tarjetas SET tipo=0");
            db.execSQL("UPDATE Tarjetas SET idIcon=0");
            db.execSQL("UPDATE Recibos SET tipo=0");
            db.execSQL("UPDATE Recibos SET nVeces=0");
            db.execSQL("UPDATE Cuentas SET idIcon=0");

        } catch (SQLException e) {
            Log.d("ERROR", "Error al actualizar la BD");
        }
    }

    public void createTables(SQLiteDatabase db) {

        Log.d(INFO, "Se crear la estructura basica de base de"
                + " datos si no existe");

        // Creamos las tablas si no existen
        db.execSQL(sqlCreateMovimientos);
        db.execSQL(sqlCreateCategorias);
        db.execSQL(sqlCreateSubcategorias);
        db.execSQL(sqlCreateTarjetas);
        db.execSQL(sqlCreateRecibos);
        db.execSQL(sqlCreateCuentas);

        // Comprobamos si las tablas Categorias y Subcategorias estan vacias
        // para aadirle los campos por defecto
        Cursor c1 = db.query("Categorias", new String[]{"idCategoria"},
                null, null, null, null, null);

        if (!c1.moveToFirst()) {
            if (languaje.equals("es") || languaje.equals("es-rUS")
                    || languaje.equals("ca")) {
                db.execSQL("INSERT INTO Categorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Salario', 39)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Coche', 14)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Casa', 25)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Amor', 1)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Gimnasio', 24)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Compras', 17)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Familia', 19)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Bebé', 4)");
            } else if (languaje.equals("fr")) {
                db.execSQL("INSERT INTO Categorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Salaire', 39)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Voiture', 14)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Maison', 25)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Amour', 1)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Gym', 24)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Shopping', 17)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Famille', 19)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Bébé', 4)");
            } else if (languaje.equals("de")) {
                db.execSQL("INSERT INTO Categorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Gehalt', 39)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Auto', 14)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Zuhause', 25)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Liebe ', 1)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Fitnessstudio', 24)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Einkaufen', 17)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Familie', 19)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Baby', 4)");
            } else if (languaje.equals("en")) {
                db.execSQL("INSERT INTO Categorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Salary', 39)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Car', 14)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Home', 25)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Love', 1)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Gym', 24)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Shopping', 17)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Family', 19)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Baby', 4)");
            } else if (languaje.equals("it")) {
                db.execSQL("INSERT INTO Categorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Stipendio ', 39)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Auto', 14)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Casa', 25)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Amore', 1)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Palestra', 24)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Shopping', 17)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Famiglia', 19)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Bambino', 4)");
            } else if (languaje.equals("pt")) {
                db.execSQL("INSERT INTO Categorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Salário', 39)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Carro', 14)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Casa', 25)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Amor', 1)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Ginásio', 24)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Compras', 17)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Família', 19)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Bebê', 4)");
            } else {
                db.execSQL("INSERT INTO Categorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Salary', 39)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Car', 14)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Home', 25)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Love', 1)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Gym', 24)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Shopping', 17)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Family', 19)");
                db.execSQL("INSERT INTO Categorias VALUES(null, 'Baby', 4)");
            }

            Log.d(INFO, "Los registros se han insertado correctamente");
        } else {
            if (c1 != null && !c1.isClosed()) {
                c1.close();
            }
        }

        Cursor c2 = db
                .query("Subcategorias", new String[]{"idSubcategoria"},
                        null, null, null, null, null);

        if (!c2.moveToFirst()) {
            Log.d(INFO, "Se van a insertar los campos de la "
                    + "tabla Subcategorias");
            if (languaje.equals("es") || languaje.equals("es-rUS")
                    || languaje.equals("ca")) {
                db.execSQL("INSERT INTO Subcategorias VALUES(0, '-' ,0)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Médico', 18)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Colegio', 21)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Taller', 41)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Restaurante', 37)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Bebidas', 15)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Viaje', 45)");
            } else if (languaje.equals("fr")) {
                db.execSQL("INSERT INTO Subcategorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Médical', 18)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'École', 21)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Atelier', 41)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Restaurant', 37)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Boissons', 15)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Voyage', 45)");
            } else if (languaje.equals("de")) {
                db.execSQL("INSERT INTO Subcategorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Medizin', 18)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'College', 21)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Werkstatt', 41)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Restaurant', 37)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Getränke', 15)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Reise', 45)");
            } else if (languaje.equals("en")) {
                db.execSQL("INSERT INTO Subcategorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Doctor', 18)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'School', 21)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Body shop', 41)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Restaurant', 37)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Drinks', 15)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Travel', 45)");
            } else if (languaje.equals("it")) {
                db.execSQL("INSERT INTO Subcategorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Medico', 18)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'College', 21)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Officina', 41)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Ristorante', 37)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Bevande', 15)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Viaggi', 45)");
            } else if (languaje.equals("pt")) {
                db.execSQL("INSERT INTO Subcategorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Médico', 18)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Colégio', 21)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Oficina', 41)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Restaurante', 37)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Bebidas', 15)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Viagens', 45)");
            } else {
                db.execSQL("INSERT INTO Subcategorias VALUES(0, '-', 0)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Doctor', 18)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'School', 21)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Body shop', 41)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Restaurant', 37)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Drinks', 15)");
                db.execSQL("INSERT INTO Subcategorias VALUES(null, 'Travel', 45)");
            }

            Log.d(INFO, "Los registros se han insertado correctamente");
        } else {
            if (c2 != null && !c2.isClosed()) {
                c2.close();
            }
        }

        Cursor c3 = db.query("Tarjetas", new String[]{"idTarjeta"}, null,
                null, null, null, null);

        if (!c3.moveToFirst()) {
            Log.d(INFO, "Se van a insertar los campos de la "
                    + "tabla Tarjetas");
            if (languaje.equals("es") || languaje.equals("es-rUS")
                    || languaje.equals("ca")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Tarjeta 1', 2000, 0, 0)");
            } else if (languaje.equals("fr")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Carte de crédit 1', 2000, 0, 0)");
            } else if (languaje.equals("de")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Kreditkarte 1', 2000, 0, 0)");
            } else if (languaje.equals("en")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Credit card 1', 2000, 0, 0)");
            } else if (languaje.equals("it")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Carta di credito 1', 2000, 0, 0)");
            } else if (languaje.equals("pt")) {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Cartão de crédito 1', 2000, 0, 0)");
            } else {
                db.execSQL("INSERT INTO Tarjetas VALUES(0, 'Credit card 1', 2000, 0, 0)");
            }

            Log.d(INFO, "Los registros se han insertado correctamente");
        } else {
            if (c3 != null && !c3.isClosed()) {
                c3.close();
            }
        }

        Cursor c4 = db.query("Cuentas", new String[]{"idCuenta"}, null,
                null, null, null, null);

        if (!c4.moveToFirst()) {
            Log.d(INFO, "Se van a insertar los campos de la " + "tabla Cuentas");
            if (languaje.equals("es") || languaje.equals("es-rUS")
                    || languaje.equals("ca")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Cuenta principal', 0)");
            } else if (languaje.equals("fr")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Compte principal', 0)");
            } else if (languaje.equals("de")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Master-Account', 0)");
            } else if (languaje.equals("en")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Master account', 0)");
            } else if (languaje.equals("it")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Account principale', 0)");
            } else if (languaje.equals("pt")) {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Conta principal', 0)");
            } else {
                db.execSQL("INSERT INTO Cuentas VALUES(0, 'Master account', 0)");
            }

            Log.d(INFO, "Los registros se han insertado correctamente");
        } else {
            if (c4 != null && !c4.isClosed()) {
                c4.close();
            }
        }
    }

    public boolean comprobarTablas(SQLiteDatabase db) {
        // consultamos si existe una nomina del mes actual
        try {
            Cursor categorias = db.rawQuery("select * from Categorias", null);
            if (categorias.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public boolean comprobarTablasTarjeta(SQLiteDatabase db) {
        // consultamos si existe una nomina del mes actual
        try {
            Cursor tarjetas = db.rawQuery("select * from Tarjetas", null);
            if (tarjetas.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean comprobarTablasCuentas(SQLiteDatabase db) {
        // consultamos si existe una nomina del mes actual
        try {
            Cursor cuentas = db.rawQuery("select * from Cuentas", null);
            if (cuentas.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean comprobarVersion30(SQLiteDatabase db) {
        // consultamos si existe una nomina del mes actual
        try {
            Cursor cuentas = db.rawQuery(
                    "select * from Categorias where idIcon = 0", null);
            if (cuentas.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean comprobarVersion40(SQLiteDatabase db) {
        // consultamos si existe una nomina del mes actual
        try {
            Cursor cuentas = db.rawQuery(
                    "select * from Tarjetas where idIcon = 0", null);
            if (cuentas.moveToFirst()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public List<Categoria> getCategoriasFiltros(SQLiteDatabase db, String tabla,
                                                String id, Context context) {
        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        Cursor c1 = db.query(tabla,
                new String[]{id, "descripcion", "idIcon"}, null, null, null,
                null, null);
        Categoria categoriaTodas = new Categoria();
        categoriaTodas.setId("-1");
        categoriaTodas.setDescripcion(context.getResources().getString(R.string.todas));
        categoriaTodas.setIdIcon(-1);
        listaCategorias.add(categoriaTodas);
        if (c1.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();
                categoria.setId(c1.getString(0));
                if (categoria.getId().equals("0")) {
                    categoria.setDescripcion(context.getResources().getString(R.string.otros));
                } else {
                    categoria.setDescripcion(c1.getString(1));
                }
                categoria.setIdIcon(c1.getInt(2));
                listaCategorias.add(categoria);
            } while (c1.moveToNext());
        }
        return listaCategorias;
    }

    public List<Categoria> getCategorias(SQLiteDatabase db, String tabla,
                                         String id, Context context) {
        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        Cursor c1 = db.query(tabla,
                new String[]{id, "descripcion", "idIcon"}, null, null, null,
                null, null);
        if (c1.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();
                categoria.setId(c1.getString(0));
                if (categoria.getId().equals("0")) {
                    categoria.setDescripcion(context.getResources().getString(R.string.otros));
                } else {
                    categoria.setDescripcion(c1.getString(1));
                }
                categoria.setIdIcon(c1.getInt(2));
                listaCategorias.add(categoria);
            } while (c1.moveToNext());
        }
        return listaCategorias;
    }

    public List<Tarjeta> getTarjetas(SQLiteDatabase db) {
        List<Tarjeta> listaTarjetas = new ArrayList<Tarjeta>();
        Cursor c1 = db.query("Tarjetas",
                new String[]{"idTarjeta", "nombre", "limite", "tipo", "idIcon"}, null, null, null, null,
                "idTarjeta asc");
        if (c1.moveToFirst()) {
            do {
                Tarjeta tarjeta = new Tarjeta();
                tarjeta.setId(c1.getString(0));
                tarjeta.setNombre(c1.getString(1));
                tarjeta.setCantMax(c1.getFloat(2));
                tarjeta.setTipo(c1.getInt(3));
                tarjeta.setIdIcon(c1.getInt(4));
                listaTarjetas.add(tarjeta);
            } while (c1.moveToNext());
        }
        return listaTarjetas;
    }

    public List<Tarjeta> getTarjetasFiltro(SQLiteDatabase db, Context context) {
        List<Tarjeta> listaTarjetas = new ArrayList<Tarjeta>();
        Cursor c1 = db.query("Tarjetas",
                new String[]{"idTarjeta", "nombre", "limite", "tipo", "idIcon"}, null, null, null, null,
                "idTarjeta asc");
        Tarjeta tarjetaAux = new Tarjeta();
        tarjetaAux.setId("-1");

        tarjetaAux.setNombre(context.getResources().getString(R.string.todas));
        listaTarjetas.add(tarjetaAux);
        if (c1.moveToFirst()) {
            do {
                Tarjeta tarjeta = new Tarjeta();
                tarjeta.setId(c1.getString(0));
                tarjeta.setNombre(c1.getString(1));
                tarjeta.setCantMax(c1.getFloat(2));
                tarjeta.setTipo(c1.getInt(3));
                tarjeta.setIdIcon(c1.getInt(4));
                listaTarjetas.add(tarjeta);
            } while (c1.moveToNext());
        }
        return listaTarjetas;
    }

    public List<Cuenta> getCuentas(SQLiteDatabase db) {
        List<Cuenta> listaCuentas = new ArrayList<Cuenta>();
        Cursor c1 = db.rawQuery("select * from Cuentas order by idCuenta asc", null);
        if (c1.moveToFirst()) {
            do {
                Cuenta cuenta = new Cuenta();
                cuenta.setIdCuenta(c1.getString(0));
                cuenta.setDescCuenta(c1.getString(1));
                cuenta.setIdIcon(c1.getInt(2));
                listaCuentas.add(cuenta);
            } while (c1.moveToNext());
        }
        return listaCuentas;
    }

    public List<Recibo> getRecibos(SQLiteDatabase db, int idCuenta) {
        List<Recibo> listaRecibos = new ArrayList<Recibo>();

        try {
            Cursor c1 = db.rawQuery("select * from Recibos r, Categorias c, Subcategorias s where r.idCuenta='"
                    + idCuenta + "' and r.idCategoria = c.idCategoria and r.idSubcategoria = s.idSubcategoria", null);

            if (c1.moveToFirst()) {
                do {
                    Recibo recibo = new Recibo();
                    recibo.setId(c1.getString(0));
                    recibo.setCantidad(c1.getString(1));
                    recibo.setDescripcion(c1.getString(2));
                    recibo.setIdCategoria(c1.getString(3));
                    recibo.setIdSubcategoria(c1.getString(4));
                    recibo.setFechaIni(c1.getString(5));
                    recibo.setFechaFin(c1.getString(6));
                    recibo.setTarjeta(Boolean.parseBoolean(c1.getString(7)));
                    recibo.setIdTarjeta(c1.getString(8));
                    recibo.setTipo(c1.getInt(10));
                    recibo.setnVeces(c1.getInt(11));
                    recibo.setDescCategoria(c1.getString(13));
                    recibo.setIdIcon(c1.getInt(14));
                    recibo.setDescSubcategoria(c1.getString(16));
                    listaRecibos.add(recibo);
                } while (c1.moveToNext());
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return listaRecibos;
    }

    public List<Categoria> getCategoriasEditDelete(SQLiteDatabase db,
                                                   String tabla, String id) {
        List<Categoria> listaCategorias = new ArrayList<Categoria>();
        Cursor c1 = db.rawQuery("select " + id + ",descripcion, idIcon from "
                + tabla + " where " + id + "!='0' order by descripcion", null);
        if (c1.moveToFirst()) {
            do {
                Categoria categoria = new Categoria();
                categoria.setId(c1.getString(0));
                categoria.setDescripcion(c1.getString(1));
                categoria.setIdIcon(c1.getInt(2));
                listaCategorias.add(categoria);
            } while (c1.moveToNext());
        }
        return listaCategorias;
    }

    public boolean insertarMovimiento(SQLiteDatabase db, int tipo,
                                      Float cantidad, String desc, Date fecha, int idCat, int idSub,
                                      boolean recibo, boolean tarjeta, int mes, int anio, int idTarjeta,
                                      int idRecibo, int idCuenta) {

        try {
            String sql = "INSERT INTO Movimientos VALUES(" + null + "," + tipo
                    + ", " + cantidad + ", " + "'" + desc + "'," + idCat + ", "
                    + idSub + ", " + "'" + fecha + "',  " + "'" + mes + "',  "
                    + "'" + anio + "', " + "'" + recibo + "', " + "'" + tarjeta
                    + "', " + "'" + idTarjeta + "', '" + idRecibo + "' , '"
                    + idCuenta + "')";
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            Log.d("Error", "Error al insertar registro en BBDD");
            return false;
        }
    }

    public boolean insertarRecibo(SQLiteDatabase db, int tipo,
                                  Float cantidad, String desc, Date fechaD, Date fechaH, int idCat, int idSub,
                                  boolean tarjeta, int idTarjeta,
                                  int nVeces, int idCuenta) {

        try {
            String sql = "INSERT INTO Recibos VALUES(" + null + ", " + cantidad + ", " + "'"
                    + desc + "'," + idCat + ", " + idSub + ", " + "'" + fechaD + "', '" + fechaH + "', '"
                    + tarjeta + "', " + "'" + idTarjeta + "', '" + idCuenta + "', '" + tipo
                    + "', '" + nVeces + "')";
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            Log.d("Error", "Error al insertar registro en BBDD");
            return false;
        }
    }

    public boolean addCategoria(SQLiteDatabase db, String categoria, int idIcon) {
        try {
            db.execSQL("INSERT INTO Categorias VALUES(null, '" + categoria
                    + "', " + idIcon + ")");
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean addSubcategoria(SQLiteDatabase db, String subcategoria,
                                   int idIcon) {
        try {
            db.execSQL("INSERT INTO Subcategorias VALUES(null, '"
                    + subcategoria + "', " + idIcon + ")");
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean addTarjeta(SQLiteDatabase db, String tarjeta, float cant, int tipo, int idIcon) {
        try {
            db.execSQL("INSERT INTO Tarjetas VALUES(null, '" + tarjeta + "', '"
                    + cant + "', '" + tipo + "', '" + idIcon + "')");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addCuenta(SQLiteDatabase db, String cuenta, int idIcon) {
        try {
            db.execSQL("INSERT INTO Cuentas VALUES(null, '" + cuenta + "', " + "'" + idIcon + "')");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public ArrayList<Movimiento> getNominas(SQLiteDatabase db, int idCuenta) {
        Cursor c1 = db.rawQuery(
                "select * from Movimientos where tipo='1' and idCuenta='"
                        + idCuenta + "'", null);
        ArrayList<Movimiento> listaNominas = new ArrayList<Movimiento>();
        if (c1.moveToFirst()) {
            do {
                Movimiento mov = new Movimiento();
                mov.setId(c1.getString(0));
                mov.setDescripcion(c1.getString(3));
                mov.setMes(c1.getString(7));
                mov.setAnio(c1.getString(8));
                listaNominas.add(mov);
            } while (c1.moveToNext());
        }
        return listaNominas;
    }

    public ArrayList<Movimiento> getAnios(SQLiteDatabase db, int idCuenta) {
        Cursor c1 = db.rawQuery(
                "select fecha from Movimientos where idCuenta = '" + idCuenta
                        + "'", null);
        ArrayList<Movimiento> listaAnios = new ArrayList<Movimiento>();
        boolean anioEncontrado = false;
        if (c1.moveToFirst()) {
            do {
                anioEncontrado = false;
                Movimiento mov = new Movimiento();
                mov.setAnio(c1.getString(0).substring(0, 4));
                for (int i = 0; i < listaAnios.size(); i++) {
                    Movimiento mov2 = listaAnios.get(i);
                    if (mov.getAnio().equals(mov2.getAnio())) {
                        anioEncontrado = true;
                        break;
                    }
                }
                if (!anioEncontrado) {
                    listaAnios.add(mov);
                }
            } while (c1.moveToNext());
        }
        return listaAnios;
    }

    // Metodo que obtiene listado de movimientos al iniciar la actividad
    public ArrayList<Movimiento> getMovimientosFiltros(SQLiteDatabase db, boolean gasto, boolean ingreso, int tipoFecha,
                                                       int tipoFiltro, String idCategoria, String idSubcategoria, int tipoPago, String idTarjeta,
                                                       int month, int year, int diaDesde, int mesDesde, int anioDesde,
                                                       int diaHasta, int mesHasta, int anioHasta, int idCuenta) {
        ArrayList<Movimiento> listaMovimientos = new ArrayList<Movimiento>();

        Date fechaInicio = null;
        Date fechaFin = null;
        Date fechaDesde = null;
        Date fechaHasta = null;

        fechaInicio = new Date(year - 1900, month, 1);
        fechaFin = getFinMes(month + 1, year);
        fechaDesde = new Date(anioDesde - 1900, mesDesde, diaDesde);
        fechaHasta = new Date(anioHasta - 1900, mesHasta, diaHasta);

        // Obtenemos los movimientos del mes entero
        Cursor movimientos = consultarMovimientos(db, gasto, ingreso, tipoFecha, idCategoria, idSubcategoria,
                tipoPago, idTarjeta, fechaInicio, fechaFin, fechaDesde, fechaHasta, idCuenta);
        listaMovimientos = obtenerDatosMovimientos(movimientos);

        return listaMovimientos;
    }

    // Metodo encargado de obtener el listado de movimientos al cambiar los
    // spinners
    public ArrayList<Movimiento> getMovimientosExcel(SQLiteDatabase db,
                                                     int mes, int anio, int idCuenta) {
        ArrayList<Movimiento> listaMovimientos = new ArrayList<Movimiento>();
        Date fechaInicio = null;
        Date fechaFin = null;

        if (mes != 0) {
            // consultamos si existe una nomina del mes actual
            Cursor nominaMesActual = getNominaPorMes(db, mes, anio, idCuenta);
            Cursor nominaMesSiguiente;
            if (mes == 12) {
                nominaMesSiguiente = getNominaPorMes(db, 1, anio + 1, idCuenta);
            } else {
                nominaMesSiguiente = getNominaPorMes(db, mes + 1, anio,
                        idCuenta);
            }
            fechaFin = getFinMes(mes, anio);
            boolean incluirFechaFin = false;

            if (nominaMesActual.moveToFirst()) {
                Movimiento mov = new Movimiento();
                mov.setFecha(getFecha(nominaMesActual.getString(6)));
                fechaInicio = mov.getFecha();
                if (nominaMesSiguiente.moveToFirst()) {
                    Movimiento mov2 = new Movimiento();
                    mov2.setFecha(getFecha(nominaMesSiguiente.getString(6)));
                    fechaFin = mov2.getFecha();
                } else {
                    incluirFechaFin = true;
                    if (mes == 12) {
                        fechaFin = getFinMes(1, anio + 1);
                    } else {
                        fechaFin = getFinMes(mes + 1, anio);
                    }
                }
            } else {
                fechaInicio = new Date(anio - 1900, mes - 1, 1);
                if (nominaMesSiguiente.moveToFirst()) {
                    Movimiento mov = new Movimiento();
                    mov.setFecha(getFecha(nominaMesSiguiente.getString(6)));
                    fechaFin = mov.getFecha();
                } else {
                    incluirFechaFin = true;
                    fechaFin = getFinMes(mes, anio);
                }
            }

            if (fechaInicio != null && fechaFin != null) {
                Cursor movimientos;
                if (incluirFechaFin) {
                    movimientos = consultarMovimientosExcel(db, fechaInicio,
                            fechaFin, idCuenta);
                } else {
                    movimientos = consultarMovimientosDosNominasExcel(db,
                            fechaInicio, fechaFin, idCuenta);
                }
                listaMovimientos = obtenerDatosMovimientosExcel(movimientos);
            }
        } else {
            fechaInicio = new Date(anio - 1900, 0, 1);
            fechaFin = new Date(anio - 1900, 11, 31);

            if (fechaInicio != null && fechaFin != null) {
                Cursor movimientos = consultarMovimientosExcel(db, fechaInicio,
                        fechaFin, idCuenta);
                listaMovimientos = obtenerDatosMovimientosExcel(movimientos);
            }
        }
        return listaMovimientos;
    }

    // Metodo que obtiene listado de movimientos al iniciar la actividad
    // resumenTarjetas
    public ArrayList<Movimiento> getMovimientosExcelMes(SQLiteDatabase db,
                                                        int month, int year, int idCuenta) {
        ArrayList<Movimiento> listaMovimientos = new ArrayList<Movimiento>();

        Date fechaInicio = null;
        Date fechaFin = null;

        if (month != 0) {
            fechaInicio = new Date(year - 1900, month - 1, 1);
            fechaFin = getFinMes(month, year);
        } else {
            fechaInicio = new Date(year - 1900, 0, 1);
            fechaFin = new Date(year - 1900, 11, 31);
        }

        if (fechaInicio != null && fechaFin != null) {
            Cursor movimientos = consultarMovimientosExcel(db, fechaInicio,
                    fechaFin, idCuenta);
            listaMovimientos = obtenerDatosMovimientosExcel(movimientos);
        }
        return listaMovimientos;
    }

    // metodo encargado de pasar una fecha de String a Date
    public static Date getFecha(String fecha) {
        Date fecha2 = null;
        try {
            SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            fecha2 = new Date(sdf.parse(fecha).getTime());
        } catch (Exception e) {
            Log.d("error", "Se ha producido un error al parsear la fecha");
        }
        return fecha2;
    }

    public Cursor consultarMovimientos(SQLiteDatabase db, boolean gasto, boolean ingreso, int tipoFecha,
                                       String idCategoria, String idSubcategoria, int tipoPago, String idTarjeta,
                                       Date fechaInicio, Date fechaFin, Date fechaDesde, Date fechaHasta, int idCuenta) {

        Cursor c1 = null;
        try {
            StringBuffer sql = new StringBuffer(
                    "select * from Movimientos m, Categorias c, Subcategorias s where idCuenta = '" + idCuenta + "'");

            if(!(gasto && ingreso)){
                if (gasto) {
                    sql.append(" and m.cantidad < 0");
                }
                if (ingreso) {
                    sql.append(" and m.cantidad > 0");
                }
            }

            if (tipoFecha == 0) {
                sql.append(" and m.fecha>='" + fechaInicio + "' and m.fecha<='" + fechaFin + "'");
            } else {
                sql.append(" and m.fecha>='" + fechaDesde + "' and m.fecha<='" + fechaHasta + "'");
            }

            if (idCategoria.equals("-1")) {
                sql.append(" and m.idCategoria = c.idCategoria");
            } else {
                sql.append(" and m.idCategoria = c.idCategoria and c.idCategoria = '" + idCategoria + "'");
            }

            if (idSubcategoria.equals("-1")) {
                sql.append(" and m.idSubcategoria = s.idSubcategoria");
            } else {
                sql.append(" and m.idSubcategoria = s.idSubcategoria and s.idSubcategoria = '" + idSubcategoria + "'");
            }

            if (tipoPago == 1) {
                sql.append((" and m.tarjeta='false'"));
            } else if (tipoPago == 2) {
                sql.append(" and m.tarjeta='true'");
            }

            if(!idTarjeta.equals("-1")){
                sql.append(" and m.idTarjeta='" + idTarjeta + "'");
            }

            sql.append(" order by m.fecha desc, m.idMovimiento desc");

            c1 = db.rawQuery(sql.toString(), null);
        } catch (Exception e) {
            return c1;
        }
        return c1;
    }

    public Cursor consultarMovimientosExcel(SQLiteDatabase db,
                                            Date fechaInicio, Date fechaFin, int idCuenta) {
        Cursor nomina = db
                .rawQuery(
                        "select * from Movimientos m, Categorias c, Subcategorias s, Tarjetas t where m.fecha>='"
                                + fechaInicio
                                + "' and m.fecha<='"
                                + fechaFin
                                + "' and m.idCuenta='"
                                + idCuenta
                                + "' and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria and m.idTarjeta = t.idTarjeta order by m.fecha asc, m.tipo desc",
                        null);

        return nomina;
    }

    public Cursor consultarMovimientosTarjeta(SQLiteDatabase db,
                                              Date fechaInicio, Date fechaFin, int idTarjeta, int idCuenta) {
        Cursor nomina;
        if (idTarjeta == 99) {
            nomina = db
                    .rawQuery(
                            "select * from Movimientos m, Categorias c, Subcategorias s where m.fecha>='"
                                    + fechaInicio
                                    + "' and m.fecha<='"
                                    + fechaFin
                                    + "' and m.idCuenta='"
                                    + idCuenta
                                    + "' and (m.tarjeta='true' or m.tipo='1') and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria order by m.fecha asc , m.tipo desc",
                            null);
        } else {
            nomina = db
                    .rawQuery(
                            "select * from Movimientos m, Categorias c, Subcategorias s where m.fecha>='"
                                    + fechaInicio
                                    + "' and m.fecha<='"
                                    + fechaFin
                                    + "' and m.idCuenta='"
                                    + idCuenta
                                    + "' and (m.tarjeta='true' or m.tipo='1') and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria and m.idTarjeta='"
                                    + idTarjeta
                                    + "' order by m.fecha asc , m.tipo desc",
                            null);
        }
        return nomina;
    }

    public Cursor consultarMovimientosDosNominas(SQLiteDatabase db,
                                                 Date fechaInicio, Date fechaFin, int idCuenta) {
        Cursor nomina = db
                .rawQuery(
                        "select * from Movimientos m, Categorias c, Subcategorias s where m.fecha>='"
                                + fechaInicio
                                + "' and m.fecha<'"
                                + fechaFin
                                + "' and m.idCuenta='"
                                + idCuenta
                                + "' and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria order by m.fecha asc, m.tipo desc",
                        null);

        return nomina;
    }

    public Cursor consultarMovimientosDosNominasExcel(SQLiteDatabase db,
                                                      Date fechaInicio, Date fechaFin, int idCuenta) {
        Cursor nomina = db
                .rawQuery(
                        "select * from Movimientos m, Categorias c, Subcategorias s, Tarjetas t where m.fecha>='"
                                + fechaInicio
                                + "' and m.fecha<'"
                                + fechaFin
                                + "' and m.idCuenta='"
                                + idCuenta
                                + "' and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria and m.idTarjeta = t.idTarjeta order by m.fecha asc, m.tipo desc",
                        null);

        return nomina;
    }

    public Cursor consultarMovimientosDosNominasTarjetas(SQLiteDatabase db,
                                                         Date fechaInicio, Date fechaFin, int idTarjeta, int idCuenta) {
        Cursor nomina;
        if (idTarjeta == 99) {
            nomina = db
                    .rawQuery(
                            "select * from Movimientos m, Categorias c, Subcategorias s where m.fecha>='"
                                    + fechaInicio
                                    + "' and m.fecha<'"
                                    + fechaFin
                                    + "' and m.idCuenta='"
                                    + idCuenta
                                    + "' and (tarjeta='true' or m.tipo='1') and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria order by m.fecha asc, m.tipo desc",
                            null);
        } else {
            nomina = db
                    .rawQuery(
                            "select * from Movimientos m, Categorias c, Subcategorias s where m.fecha>='"
                                    + fechaInicio
                                    + "' and m.fecha<'"
                                    + fechaFin
                                    + "' and m.idCuenta='"
                                    + idCuenta
                                    + "' and (tarjeta='true' or m.tipo='1') and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria and m.idTarjeta='"
                                    + idTarjeta
                                    + "' order by m.fecha asc, m.tipo desc",
                            null);
        }
        return nomina;
    }

    public static ArrayList<Movimiento> obtenerDatosMovimientos(Cursor c) {
        ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
        if (c.moveToFirst()) {
            do {
                Movimiento mov = new Movimiento();
                mov.setId(c.getString(0));
                mov.setTipo(c.getString(1));
                mov.setCantidad(c.getString(2));
                DecimalFormat df = new DecimalFormat("0.00");
                mov.setCantidadAux(df.format(Float.parseFloat(c.getString(2))));
                mov.setDescripcion(c.getString(3));
                mov.setIdCategoria(c.getString(4));
                mov.setIdSubcategoria(c.getString(5));
                mov.setFecha(getFecha(c.getString(6)));
                mov.setMes(c.getString(7));
                mov.setAnio(c.getString(8));
                mov.setRecibo(Boolean.parseBoolean(c.getString(9)));
                mov.setTarjeta(Boolean.parseBoolean(c.getString(10)));
                mov.setIdTarjeta(c.getString(11));
                mov.setIdCuenta(c.getString(13));
                mov.setDescCategoria(c.getString(15));
                mov.setIdIconCat(c.getInt(16));
                mov.setDescSubcategoria(c.getString(18));
                mov.setIdIconSub(c.getInt(19));
                listMov.add(mov);
            } while (c.moveToNext());
        }
        return listMov;
    }

    public static ArrayList<Movimiento> obtenerDatosMovimientosExcel(Cursor c) {
        ArrayList<Movimiento> listMov = new ArrayList<Movimiento>();
        if (c.moveToFirst()) {
            do {
                Movimiento mov = new Movimiento();
                mov.setId(c.getString(0));
                mov.setTipo(c.getString(1));
                mov.setCantidad(c.getString(2));
                DecimalFormat df = new DecimalFormat("0.00");
                mov.setCantidadAux(df.format(Float.parseFloat(c.getString(2))));
                mov.setDescripcion(c.getString(3));
                mov.setIdCategoria(c.getString(4));
                mov.setIdSubcategoria(c.getString(5));
                mov.setFecha(getFecha(c.getString(6)));
                mov.setMes(c.getString(7));
                mov.setAnio(c.getString(8));
                mov.setRecibo(Boolean.parseBoolean(c.getString(9)));
                mov.setTarjeta(Boolean.parseBoolean(c.getString(10)));
                mov.setIdTarjeta(c.getString(11));
                mov.setDescCategoria(c.getString(15));
                mov.setIdIconCat(c.getInt(16));
                mov.setDescSubcategoria(c.getString(18));
                mov.setIdIconSub(c.getInt(19));
                mov.setDescTarjeta(c.getString(21));
                listMov.add(mov);
            } while (c.moveToNext());
        }
        return listMov;
    }

    public String getTotalIngresos(SQLiteDatabase db, int idCuenta) {
        Cursor c = db.rawQuery(
                "select cantidad from Movimientos where cantidad >'0' and idCuenta='"
                        + idCuenta + "'", null);
        float cantidad = 0;
        if (c.moveToFirst()) {
            do {
                cantidad = cantidad + Float.parseFloat(c.getString(0));
            } while (c.moveToNext());
        }
        return String.valueOf(cantidad);
    }

    public String getTotalGastos(SQLiteDatabase db, int idCuenta) {
        Cursor c = db.rawQuery(
                "select cantidad from Movimientos where cantidad <'0' and idCuenta='"
                        + idCuenta + "'", null);
        float cantidad = 0;
        if (c.moveToFirst()) {
            do {
                cantidad = cantidad + Float.parseFloat(c.getString(0));
            } while (c.moveToNext());
        }
        return String.valueOf(cantidad);
    }

    public String getNumRegistros(SQLiteDatabase db, int idCuenta) {
        Cursor c = db.rawQuery(
                "select count (*) from Movimientos where idCuenta='" + idCuenta
                        + "'", null);
        String registros = "";
        if (c.moveToFirst()) {
            registros = c.getString(0);
        }
        return registros;
    }

    public String getNumIngresos(SQLiteDatabase db, int idCuenta) {
        Cursor c = db.rawQuery(
                "select count (*) from Movimientos where cantidad > '0' and idCuenta='"
                        + idCuenta + "'", null);
        String numIngresos = "";
        if (c.moveToFirst()) {
            numIngresos = c.getString(0);
        }
        return numIngresos;
    }

    public String getNumGastos(SQLiteDatabase db, int idCuenta) {
        Cursor c = db.rawQuery(
                "select count (*) from Movimientos where cantidad < '0' and idCuenta='"
                        + idCuenta + "'", null);
        String numGatos = "";
        if (c.moveToFirst()) {
            numGatos = c.getString(0);
        }
        return numGatos;
    }

    public String getNumCategorias(SQLiteDatabase db) {
        Cursor c = db.rawQuery("select count (*) from Categorias", null);
        String numCat = "";
        if (c.moveToFirst()) {
            numCat = c.getString(0);
        }
        if (Integer.parseInt(numCat) > 0) {
            numCat = String.valueOf(Integer.parseInt(numCat) - 1);
        }
        return numCat;
    }

    public String getNumSubcategorias(SQLiteDatabase db) {
        Cursor c = db.rawQuery("select count (*) from Subcategorias", null);
        String numSubcat = "";
        if (c.moveToFirst()) {
            numSubcat = c.getString(0);
        }
        if (Integer.parseInt(numSubcat) > 0) {
            numSubcat = String.valueOf(Integer.parseInt(numSubcat) - 1);
        }
        return numSubcat;
    }

    public boolean eliminarRegistro(SQLiteDatabase db, String id) {
        try {
            db.delete("Movimientos", "idMovimiento=?", new String[]{id});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editCategoria(SQLiteDatabase db, String descripcion,
                                 String id, int idIcon) {
        try {
            ContentValues values = new ContentValues();
            values.put("descripcion", descripcion);
            values.put("idIcon", idIcon);
            db.update("Categorias", values, "idCategoria=?",
                    new String[]{id});

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editCategoriaCancel(SQLiteDatabase db, String id, int idIcon) {
        try {
            ContentValues values = new ContentValues();
            values.put("idIcon", idIcon);
            db.update("Categorias", values, "idCategoria=?",
                    new String[]{id});

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editSubcategoria(SQLiteDatabase db, String descripcion,
                                    String id, int idIcon) {
        try {
            ContentValues values = new ContentValues();
            values.put("descripcion", descripcion);
            values.put("idIcon", idIcon);
            db.update("Subcategorias", values, "idSubcategoria=?",
                    new String[]{id});

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editSubcategoriaCancel(SQLiteDatabase db, String id,
                                          int idIcon) {
        try {
            ContentValues values = new ContentValues();
            values.put("idIcon", idIcon);
            db.update("Subcategorias", values, "idSubcategoria=?",
                    new String[]{id});

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editTarjeta(SQLiteDatabase db, String descripcion, String id, int tipo, int idIcon, float limite) {
        try {
            ContentValues values = new ContentValues();
            values.put("nombre", descripcion);
            values.put("tipo", tipo);
            values.put("limite", limite);
            values.put("idIcon", idIcon);
            db.update("Tarjetas", values, "idTarjeta=?", new String[]{id});

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean editCuenta(SQLiteDatabase db, String descripcion,
                              String idCuenta, int idIcon) {
        try {
            ContentValues values = new ContentValues();
            values.put("nombre", descripcion);
            values.put("idIcon", idIcon);
            db.update("Cuentas", values, "idCuenta=?",
                    new String[]{idCuenta});

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteCategoria(SQLiteDatabase db, String id, String tipo) {
        try {
            if (tipo.equals("categoria")) {
                ContentValues values = new ContentValues();
                values.put("idCategoria", 0);
                db.delete("Categorias", "idCategoria=?", new String[]{id});
                db.update("Movimientos", values, "idCategoria=?",
                        new String[]{id});
            } else {
                ContentValues values = new ContentValues();
                values.put("idSubcategoria", 0);
                db.delete("Subcategorias", "idSubcategoria=?",
                        new String[]{id});
                db.update("Movimientos", values, "idSubcategoria=?",
                        new String[]{id});
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteTarjeta(SQLiteDatabase db, String id) {
        try {
            ContentValues values = new ContentValues();
            values.put("idTarjeta", 0);
            db.delete("Tarjetas", "idTarjeta=?", new String[]{id});
            db.update("Movimientos", values, "idTarjeta=?", new String[]{id});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteCuenta(SQLiteDatabase db, String idCuenta) {
        try {
            db.delete("Cuentas", "idCuenta=?", new String[]{idCuenta});
            db.delete("Movimientos", "idCuenta=?", new String[]{idCuenta});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean deleteRecibo(SQLiteDatabase db, String id) {
        try {
            ContentValues values = new ContentValues();
            values.put("idRecibo", 0);
            db.delete("Recibos", "idRecibo=?", new String[]{id});
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean eliminarTablas(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + "Movimientos");
            db.execSQL("DROP TABLE IF EXISTS " + "Categorias");
            db.execSQL("DROP TABLE IF EXISTS " + "Subcategorias");
            db.execSQL("DROP TABLE IF EXISTS " + "Tarjetas");
            db.execSQL("DROP TABLE IF EXISTS " + "Recibos");
            db.execSQL("DROP TABLE IF EXISTS " + "Cuentas");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Date getFinMes(int mes, int anio) {
        Date fechaFin = null;
        switch (mes) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                fechaFin = new Date(anio - 1900, mes - 1, 31);
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                fechaFin = new Date(anio - 1900, mes - 1, 30);
                break;
            case 2:
                if (((anio % 4 == 0) && !(anio % 100 == 0)) || (anio % 400 == 0))
                    fechaFin = new Date(anio - 1900, mes - 1, 29);
                else
                    fechaFin = new Date(anio - 1900, mes - 1, 28);
                break;
        }
        return fechaFin;
    }

    public Cursor getNominaPorMes(SQLiteDatabase db, int mes, int anio,
                                  int idCuenta) {
        Cursor c = db.rawQuery(
                "select * from Movimientos where tipo='1' and mes='" + (mes)
                        + "' and anio='" + anio + "' and idCuenta='" + idCuenta
                        + "'", null);
        return c;
    }

    public boolean getReciboId(SQLiteDatabase db, int idRecibo, Date fechaIni,
                               Date fechaFin) {
        Cursor c = db.rawQuery("select * from Movimientos where idRecibo='"
                + idRecibo + "' and " + "fecha>='" + fechaIni
                + "' and fecha<='" + fechaFin + "'", null);

        boolean hayRegistro = false;
        if (c.moveToFirst()) {
            hayRegistro = true;
        }
        return hayRegistro;
    }

    public Recibo getReciboId(SQLiteDatabase db, int idRecibo) {
        Cursor c1 = db.rawQuery("select * from Recibos where idRecibo='"
                + idRecibo + "'", null);

        Recibo recibo = new Recibo();
        if (c1.moveToFirst()) {
            recibo.setId(c1.getString(0));
            recibo.setCantidad(c1.getString(1));
            recibo.setDescripcion(c1.getString(2));
            recibo.setIdCategoria(c1.getString(3));
            recibo.setIdSubcategoria(c1.getString(4));
            recibo.setFechaIni(c1.getString(5));
            recibo.setFechaFin(c1.getString(6));
            recibo.setTarjeta(Boolean.parseBoolean(c1.getString(7)));
            recibo.setIdTarjeta(c1.getString(8));
            recibo.setTipo(c1.getInt(10));
            recibo.setnVeces(c1.getInt(11));
        }
        return recibo;
    }

    public Tarjeta getTarjetaId(SQLiteDatabase db, int idTarjeta) {
        Cursor c1 = db.rawQuery("select * from Tarjetas where idTarjeta='"
                + idTarjeta + "'", null);

        Tarjeta tarjeta = new Tarjeta();
        if (c1.moveToFirst()) {
            tarjeta.setId(c1.getString(0));
            tarjeta.setNombre(c1.getString(1));
            tarjeta.setCantMax(c1.getFloat(2));
            tarjeta.setTipo(c1.getInt(3));
            tarjeta.setIdIcon(c1.getInt(4));
        }
        return tarjeta;
    }


    public boolean editarMovimiento(SQLiteDatabase db, String id, int tipo,
                                    Float cantidad, String desc, Date fecha, int idCat, int idSub,
                                    boolean recibo, boolean tarjeta, int mes, int anio, int idTarjeta) {
        try {
            ContentValues values = new ContentValues();
            values.put("tipo", tipo);
            values.put("cantidad", cantidad);
            values.put("descripcion", desc);
            values.put("idCategoria", idCat);
            values.put("idSubcategoria", idSub);
            values.put("fecha", fecha.toString());
            values.put("mes", mes);
            values.put("anio", anio);
            values.put("recibo", String.valueOf(recibo));
            values.put("tarjeta", String.valueOf(tarjeta));
            values.put("idTarjeta", String.valueOf(idTarjeta));
            db.update("Movimientos", values, "idMovimiento=?",
                    new String[]{id});
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public boolean editarRecibo(SQLiteDatabase db, String id, int tipoRegistro, Float cantidad,
                                String desc, int idCat, int idSub, Date fechaIni, Date fechaFin,
                                boolean tarjeta, int idTarjeta, int nVeces) {
        try {
            ContentValues values = new ContentValues();
            values.put("cantidad", cantidad);
            values.put("descripcion", desc);
            values.put("idCategoria", idCat);
            values.put("idSubcategoria", idSub);
            values.put("fechaIni", fechaIni.toString());
            values.put("fechaFin", fechaFin.toString());
            values.put("tarjeta", String.valueOf(tarjeta));
            values.put("idTarjeta", String.valueOf(idTarjeta));
            values.put("tipo", String.valueOf(tipoRegistro));
            values.put("nVeces", nVeces);
            db.update("Recibos", values, "idRecibo=?", new String[]{id});
        } catch (Exception e) {
            return false;
        }
        return true;

    }

    public void closeDB(SQLiteDatabase db) {
        db.close();
    }

    public boolean exportarBBDD() {
        File dbFile = new File(Environment.getDataDirectory()
                + "/data/com.agudoApp.salaryApp/databases/BDGestionGastos");

        File exportDir = new File(Environment.getExternalStorageDirectory(),
                "/Finanfy");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        File file = new File(exportDir, dbFile.getName());

        try {
            file.createNewFile();
            this.copyFile(dbFile, file);
            return true;
        } catch (IOException e) {
            Log.e("Error al exportar", e.getMessage(), e);
            return false;
        }
    }

    public boolean importarBBDD(SQLiteDatabase db) {
        File importDir = new File(Environment.getDataDirectory()
                + "/data/com.agudoApp.salaryApp/databases/");

        File dbFile = new File(Environment.getExternalStorageDirectory(),
                "/Finanfy/BDGestionGastos");
        if (dbFile.exists()) {
            File file = new File(importDir, dbFile.getName());

            try {
                file.createNewFile();
                this.copyFile(dbFile, file);

                boolean tablasTarjeta = comprobarTablasTarjeta(db);
                if (!tablasTarjeta) {
                    actualizarBD(db);
                }

                boolean tablasCuentasCreadas = comprobarTablasCuentas(db);
                if (!tablasCuentasCreadas) {
                    actualizarBDCuentas(db);
                }

                boolean version30 = comprobarVersion30(db);
                if (!version30) {
                    actualizarVersion30(db);
                }

                boolean version40 = comprobarVersion40(db);
                if (!version40) {
                    actualizarVersion40(db);
                }

                return true;
            } catch (IOException e) {
                Log.e("Error al exportar", e.getMessage(), e);
                return false;
            }
        } else {
            Log.d("Error", "No se ha encontrado base de datos para importar");
            return false;
        }
    }

    public boolean importarBBDDPro(SQLiteDatabase db) {
        File importDir = new File(Environment.getDataDirectory()
                + "/data/com.agudoApp.salaryApp/databases/");

        File dbFile = new File(Environment.getDataDirectory()
                + "/data/com.agba.salaryControlPro/databases/BDGestionGastos");
        if (dbFile.exists()) {
            File file = new File(importDir, dbFile.getName());

            try {
                file.createNewFile();
                this.copyFile(dbFile, file);

                boolean tablasTarjeta = comprobarTablasTarjeta(db);
                if (!tablasTarjeta) {
                    actualizarBD(db);
                }

                boolean tablasCuentasCreadas = comprobarTablasCuentas(db);
                if (!tablasCuentasCreadas) {
                    actualizarBDCuentas(db);
                }

                boolean version30 = comprobarVersion30(db);
                if (!version30) {
                    actualizarVersion30(db);
                }

                return true;
            } catch (IOException e) {
                Log.e("Error al exportar", e.getMessage(), e);
                return false;
            }
        } else {
            Log.d("Error", "No se ha encontrado base de datos para importar");
            return false;
        }
    }

    public boolean existeBDLite() {
        File dbFile = new File(Environment.getDataDirectory()
                + "/data/com.agudoApp.salaryApp/databases/BDGestionGastos");
        if (dbFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean existeBDPro() {
        File dbFile = new File(Environment.getDataDirectory()
                + "/data/com.agba.salaryControlPro/databases/BDGestionGastos");
        if (dbFile.exists()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean importarBBDDLite(SQLiteDatabase db) {
        File importDir = new File(Environment.getDataDirectory()
                + "/data/com.agudoApp.salaryApp/databases/");

        File dbFile = new File(Environment.getDataDirectory()
                + "/data/com.agudoApp.salaryApp/databases/BDGestionGastos");
        if (dbFile.exists()) {
            File file = new File(importDir, dbFile.getName());

            try {
                file.createNewFile();
                this.copyFile(dbFile, file);

                boolean tablasTarjetaCreadas = comprobarTablasTarjeta(db);
                if (!tablasTarjetaCreadas) {
                    actualizarBD(db);
                }
                return true;
            } catch (IOException e) {
                Log.e("Error al exportar", e.getMessage(), e);
                return false;
            }
        } else {
            Log.d("Error", "No se ha encontrado base de datos para importar");
            return false;
        }
    }

    void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    public Cuenta getCuentaSeleccionada(SQLiteDatabase db, int cuen) {

        Cuenta cuenta = new Cuenta();
        int idCuenta = cuen;

        Cursor c1 = db.rawQuery("select * from Cuentas where idCuenta="
                + idCuenta, null);

        if (c1.moveToFirst()) {
            cuenta.setIdCuenta(c1.getString(0));
            cuenta.setDescCuenta(c1.getString(1));
            cuenta.setIdIcon(c1.getInt(2));
        }
        return cuenta;
    }

    public ArrayList<Movimiento> getTodosMovimientos(SQLiteDatabase db,
                                                     int idCuenta) {
        Cursor movimientos = db
                .rawQuery(
                        "select * from Movimientos m, Categorias c, Subcategorias s where m.idCuenta='"
                                + idCuenta
                                + "' and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria order by m.fecha asc, m.tipo desc",
                        null);

        ArrayList<Movimiento> listaMovimientos = obtenerDatosMovimientos(movimientos);

        return listaMovimientos;
    }

    public ArrayList<Movimiento> getTodosMovimientosTarjeta(SQLiteDatabase db,
                                                            int idTarjeta, int idCuenta) {

        Cursor movimientos;
        if (idTarjeta == 99) {
            movimientos = db
                    .rawQuery(
                            "select * from Movimientos m, Categorias c, Subcategorias s where m.idCuenta='"
                                    + idCuenta
                                    + "' and (tarjeta='true' or m.tipo='1') and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria order by m.fecha asc, m.tipo desc",
                            null);
        } else {
            movimientos = db
                    .rawQuery(
                            "select * from Movimientos m, Categorias c, Subcategorias s where m.idCuenta='"
                                    + idCuenta
                                    + "' and (tarjeta='true' or m.tipo='1') and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria and m.idTarjeta='"
                                    + idTarjeta
                                    + "' order by m.fecha asc, m.tipo desc",
                            null);
        }

        ArrayList<Movimiento> listaMovimientos = obtenerDatosMovimientos(movimientos);
        return listaMovimientos;
    }

    public Categoria getCategoriaId(SQLiteDatabase db, String idCat) {
        Cursor c = db.rawQuery("select * from Categorias where idCategoria='"
                + idCat + "'", null);

        Categoria cat = new Categoria();

        if (c.moveToFirst()) {
            cat.setId(c.getString(0));
            cat.setDescripcion(c.getString(1));
            cat.setIdIcon(c.getInt(2));
        }
        return cat;
    }

    public Categoria getSubcategoriaId(SQLiteDatabase db, String idSub) {
        Cursor c = db.rawQuery(
                "select * from Subcategorias where idSubcategoria='" + idSub
                        + "'", null);

        Categoria cat = new Categoria();

        if (c.moveToFirst()) {
            cat.setId(c.getString(0));
            cat.setDescripcion(c.getString(1));
            cat.setIdIcon(c.getInt(2));
        }
        return cat;
    }

    public Movimiento getMovimientoId(SQLiteDatabase db, String idMov) {
        Cursor c = db
                .rawQuery(
                        "select * from Movimientos m, Categorias c, Subcategorias s where m.idMovimiento='"
                                + idMov
                                + "' and m.idCategoria = c.idCategoria and m.idSubcategoria = s.idSubcategoria order by m.fecha asc, m.tipo desc",
                        null);

        Movimiento mov = new Movimiento();

        if (c.moveToFirst()) {
            mov.setId(c.getString(0));
            mov.setTipo(c.getString(1));
            mov.setCantidad(c.getString(2));
            DecimalFormat df = new DecimalFormat("0.00");
            mov.setCantidadAux(df.format(Float.parseFloat(c.getString(2))));
            mov.setDescripcion(c.getString(3));
            mov.setIdCategoria(c.getString(4));
            mov.setIdSubcategoria(c.getString(5));
            mov.setFecha(getFecha(c.getString(6)));
            mov.setMes(c.getString(7));
            mov.setAnio(c.getString(8));
            mov.setRecibo(Boolean.parseBoolean(c.getString(9)));
            mov.setTarjeta(Boolean.parseBoolean(c.getString(10)));
            mov.setIdTarjeta(c.getString(11));
            mov.setIdCuenta(c.getString(13));
            mov.setDescCategoria(c.getString(15));
            mov.setIdIconCat(c.getInt(16));
            mov.setDescSubcategoria(c.getString(18));
            mov.setIdIconSub(c.getInt(19));
        }
        return mov;
    }
}
