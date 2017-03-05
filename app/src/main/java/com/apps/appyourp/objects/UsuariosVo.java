package com.apps.appyourp.objects;

/**
 * Created by Rangel on 24/02/2017.
 */

public class UsuariosVo {

    /**
     * Clase Vo para realizar la insercion de los datos de los usuarios
     */
    private String id_user;
    private String name;
    private String email;
    private String photo;
    private String token;


    public UsuariosVo() {
    }

    public UsuariosVo(String id_user, String name, String email, String photo, String token) {
        this.id_user = id_user;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.token = token;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
