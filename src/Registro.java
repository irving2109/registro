import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro {
    private static final String LOG_FILE = "registro.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese las contraseñas separadas por comas: ");
        String input = scanner.nextLine();
        String[] contraseñas = input.split(",");

        // Crear un pool de hilos con la cantidad de contraseñas ingresadas
        ExecutorService executor = Executors.newFixedThreadPool(contraseñas.length);

        // Lanzar un hilo para validar cada contraseña y guardar el resultado en el archivo de registro
        for (String contraseña : contraseñas) {
            executor.execute(() -> {
                boolean esValida = validarContraseña(contraseña.trim());
                guardarRegistro(contraseña.trim(), esValida);
            });
        }

        // Apagar el pool de hilos después de que se completen todas las tareas
        executor.shutdown();
    }

    private static boolean validarContraseña(String contraseña) {
        // Expresión regular para validar la contraseña
        Pattern PATRON = Pattern.compile("^(?=.*[a-z].*[a-z].*[a-z])(?=.*[A-Z].*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
        Matcher matcher = PATRON.matcher(contraseña);
        return matcher.matches();
    }

    private static void guardarRegistro(String contraseña, boolean esValida) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            String resultado = esValida ? "VÁLIDA" : "INVÁLIDA";
            writer.write("Contraseña: " + contraseña + " - Estado: " + resultado);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de registro: " + e.getMessage());
        }
    }
}