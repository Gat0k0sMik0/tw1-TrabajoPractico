package com.tallerwebi.presentacion;

public class DatosInicio {
        private String nombre;
        private String password;

        public DatosInicio(){

        }
            public DatosInicio(String nombre, String password) {
            this.nombre = nombre;
            this.password = password;
        }

        public String getNombre() {
            return nombre;
        }

        public void setNombre(String nombre) {
            this.nombre = nombre;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
