package com.tallerwebi.infraestructura;

import com.tallerwebi.dominio.*;
import com.tallerwebi.dominio.excepcion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service("servicioLogin")
@Transactional
public class ServicioLoginImpl implements ServicioLogin {

    private final RepositorioUsuario repositorioUsuario;
    private final RepositorioFotoPerfil repositorioFotoPerfil;

    @Autowired
    public ServicioLoginImpl(RepositorioUsuario repositorioUsuario, RepositorioFotoPerfil repositorioFotoPerfil){
        this.repositorioUsuario = repositorioUsuario;
        this.repositorioFotoPerfil = repositorioFotoPerfil;
    }

    @Override
    public Usuario consultarUsuario (String email, String password) {
        return repositorioUsuario.buscarUsuario(email, password);
    }

    @Override
    public void registrar(Usuario usuario) throws ActualizarUsuarioException, MailExistenteException, ContraseniasDiferentesException, ContraseniaInvalidaException, UsuarioExistenteException, UsuarioInvalidoException {
        Usuario usuarioEncontradoConMail = repositorioUsuario.buscarPorMail(usuario.getEmail());
        Usuario usuarioEncontradoConNombre = repositorioUsuario.buscarPorNombre(usuario.getNombreUsuario());

        // Validacion correo
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        if (!usuario.getEmail().matches(emailRegex) || !usuario.getEmail().endsWith(".com"))
            throw new ActualizarUsuarioException("El e-mail está mal escrito");
        // Que no exista usuario con mismo correo
        if (usuarioEncontradoConMail != null)
            throw new MailExistenteException();
        // Validacion largo de nombre de usuario
        if (usuario.getNombreUsuario().length() > 16 || usuario.getNombreUsuario().length() < 4)
            throw new UsuarioInvalidoException();
        // Que no exista usuario con el mismo nombre
        if (usuarioEncontradoConNombre != null)
            throw new UsuarioExistenteException();
        // Validacion repetir contraseña
        if (!usuario.getPassword().equals(usuario.getConfirmPassword()))
            throw new ContraseniasDiferentesException();
        // Validacion largo de contraseña
        if (usuario.getPassword().length() > 15 || usuario.getPassword().length() < 5)
            throw new ContraseniaInvalidaException();
    }

    @Override
    public void guardarUsuario(Usuario usuario) {
        // Guardar al usuario en la base de datos
        repositorioUsuario.guardar(usuario);
    }

    @Override
    public void agregarFotoPerfil(Usuario usuario, MultipartFile fotoPerfil) throws IOException {
        Path directorioFotos = Paths.get("C:/xampp/htdocs/tw1-TrabajoPractico/img/fotos-perfil");
        if (!Files.exists(directorioFotos)) {
            Files.createDirectories(directorioFotos); // Crear directorio si no existe
        }
        // Crear un nombre único para el archivo
        String nombreArchivo = UUID.randomUUID().toString() + "_" + fotoPerfil.getOriginalFilename();
        Path rutaArchivo = directorioFotos.resolve(nombreArchivo);

        // Guardar el archivo
        Files.copy(fotoPerfil.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);

        // Asociar la foto al usuario
        FotoPerfil foto = new FotoPerfil();
        foto.setImagen(nombreArchivo); // Guarda solo el nombre del archivo, no la ruta completa
        usuario.setFotoPerfil(foto);
        usuario.setUrlFotoPerfil(foto.getImagen());
        repositorioUsuario.modificar(usuario);
        repositorioFotoPerfil.guardarFotoPerfil(foto);
    }

    @Override
    public void asignarFotoDefault(Usuario usuario) {
        // Buscar la foto por defecto en la base de datos (asumiendo que tiene ID = 1)
        FotoPerfil fotoDefault = repositorioFotoPerfil.buscarFotoPorId(1L);

        if (fotoDefault == null) {
            throw new IllegalStateException("La foto por defecto no existe en la base de datos.");
        }

        // Asignar la foto por defecto al usuario
        usuario.setFotoPerfil(fotoDefault);
        usuario.setUrlFotoPerfil(fotoDefault.getImagen());

        // Guardar los cambios en el usuario
        repositorioUsuario.modificar(usuario);
    }

}

