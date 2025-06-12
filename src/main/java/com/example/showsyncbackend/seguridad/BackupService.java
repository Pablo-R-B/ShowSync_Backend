package com.example.showsyncbackend.seguridad;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

@Component
public class BackupService {

    private static final Logger logger = Logger.getLogger(BackupService.class.getName());

    @Value("${spring.datasource.password}")
    private String dbPassword;

    // Puedes mover esta ruta a application.properties si quieres parametrizarla.
    private static final String PG_DUMP_PATH = "C:\\Program Files\\PostgreSQL\\17\\bin\\pg_dump.exe";
    private static final String BACKUP_DIR = "C:\\Users\\Usuario\\Documents";

    @Scheduled(cron = "0 0 * * * ?") // Todos los días a medianoche
    public void realizarBackup() {
        try {
            String backupFileName = "respaldo.sql";
            String backupFilePath = BACKUP_DIR + File.separator + backupFileName;


            logger.info("Iniciando backup a: " + backupFilePath);

            ProcessBuilder processBuilder = new ProcessBuilder(
                    PG_DUMP_PATH,
                    "-U", "postgres",
                    "-h", "localhost",
                    "-p", "5432",
                    "-d", "postgres",
                    "--schema=showsync",
                    "-f", backupFilePath
            );

            // Establece la contraseña vía variable de entorno
            processBuilder.environment().put("PGPASSWORD", dbPassword);

            // Hereda la salida de consola (opcional)
            processBuilder.inheritIO();

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                logger.info("Backup realizado exitosamente en: " + backupFilePath);
            } else {
                logger.severe("Error al realizar el backup. Código de salida: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            logger.severe("Error al ejecutar el backup: " + e.getMessage());
        }
    }
}
