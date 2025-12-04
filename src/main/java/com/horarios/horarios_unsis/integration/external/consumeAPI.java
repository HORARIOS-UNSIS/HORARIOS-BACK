package com.horarios.horarios_unsis.integration.external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Servicio para consumir datos de la API externa de aulas
 */
@Service
public class ConsumeAPI {
    
    private static final Logger logger = LoggerFactory.getLogger(ConsumeAPI.class);
    private static final String API_URL = "http://serv-horarios.unsis.lan/api/aulas/capacidad/32";
    private static final int TIMEOUT = 5000; // 5 segundos

    /**
     * Consume la API externa y obtiene la lista de aulas
     * @return Lista de aulas con sus propiedades
     */
    public List<Map<String, Object>> consumirApiAulas() {
        List<Map<String, Object>> aulasList = new ArrayList<>();
        
        logger.info("Iniciando consumo de API: {}", API_URL);

        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.setConnectTimeout(TIMEOUT);
            conn.setReadTimeout(TIMEOUT);

            int responseCode = conn.getResponseCode();
            logger.info("Código de respuesta HTTP: {}", responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                
                while ((line = br.readLine()) != null) {
                    response.append(line);
                }
                br.close();

                logger.debug("Respuesta de API: {}", response.toString());
                
                JSONArray jsonArray = new JSONArray(response.toString());
                logger.info("Se encontraron {} aulas", jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject aula = jsonArray.getJSONObject(i);

                    Map<String, Object> map = new HashMap<>();
                    map.put("clave", aula.optInt("clave", 0));
                    map.put("nombre", aula.optString("nombre", "N/A"));
                    map.put("capacidad", aula.optInt("capacidad", 0));
                    map.put("tipo", aula.optString("tipo", "N/A"));
                    map.put("proyector", aula.optString("statusProyector", "N/A"));
                    aulasList.add(map);
                    
                    logger.debug("Aula procesada: {}", map);
                }
            } else {
                logger.warn("La API retornó código de error: {}", responseCode);
            }
            
            conn.disconnect();

        } catch (java.net.ConnectException e) {
            logger.error("Error de conexión. Verifica que la API esté disponible en: {}", API_URL, e);
        } catch (java.net.SocketTimeoutException e) {
            logger.error("Timeout al conectar con la API", e);
        } catch (Exception e) {
            logger.error("Error al consumir la API de aulas", e);
        }
        
        logger.info("Consumo de API finalizado. Total de aulas: {}", aulasList.size());
        return aulasList;
    }
}
