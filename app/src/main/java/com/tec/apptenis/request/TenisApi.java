package com.tec.apptenis.request;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tec.apptenis.model.Alumno;
import com.tec.apptenis.model.Profesor;
import com.tec.apptenis.model.Clase;
import com.tec.apptenis.model.Usuario;
import com.tec.apptenis.model.UsuarioRequest;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

// Clase renombrada a TenisApi
public class TenisApi {

    private static final String BASE_URL = "http://10.0.2.2:5190/";
    private static Retrofit retrofit = null; // Inicializado a null para el patrón Singleton

    // Método principal para obtener la instancia Singleton de Retrofit
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            // 1. Configuración del Logging Interceptor
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 2. Configuración del Cliente HTTP
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build();

            // 3. Creación de la instancia Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    // Añadir convertidores: Scalars para String (ej. token) y Gson para objetos JSON
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(getGsonInstance()))
                    .build();
        }
        return retrofit;
    }

    // Método auxiliar para obtener la instancia de Gson con leniency
    private static Gson getGsonInstance() {
        return new GsonBuilder().setLenient().create();
    }

    // Método unificado para obtener el servicio
    public static TenisApiService getTenisApiService() {
        // Usa la instancia Singleton de Retrofit para crear el servicio
        return getRetrofitInstance().create(TenisApiService.class);
    }


    public interface TenisApiService { // Interfaz renombrada de InmoServicio a TenisApiService

        @POST("api/Usuarios/Login")
        Call<String> login(
                // 🔑 CORRECCIÓN: Usar @Body para enviar el objeto como JSON
                @Body UsuarioRequest request
        );
        @GET("api/Profesores")
        Call<List<Profesor>> obtenerPerfil(@Header("Authorization") String token);

        @GET("api/Clases")
        Call<List<Profesor>> getClase(@Header("Authorization") String token);


        @GET("api/clase/alumno/{id}")
        Call<Alumno> obtenerClasesPorAlumno(@Header("Authorization") String token, @Path("id") int idAlumno);

        @GET("api/usuarios")
        Call<List<Usuario>> obtenerUsuarios(@Header("Authorization") String token);
        @PUT("api/profesores/{id}") // 👈 Usa {id}
        Call<Profesor> actualizarProfesor(
                @Header("Authorization") String token,
                @Path("id") int idProfesor, // 👈 Se requiere la anotación @Path
                @Body Profesor profesor
        );
        @PUT("api/alumnos/actualizar")
        Call<Alumno> actualizarAlumno(@Header("Authorization") String token, @Body Alumno alumno);

        @Multipart
        @POST("api/Clase/cargar")
        Call<Clase> CargarClase(@Header("Authorization") String token,
                                @Part MultipartBody.Part imagen,
                                @Part("clase") RequestBody alumnoBody);
    }


    public static void guardarToken(Context context, String token) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token", token);
        editor.apply();
    }

    public static String leerToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("token.xml", Context.MODE_PRIVATE);
        return sp.getString("token",  null);
    }
}