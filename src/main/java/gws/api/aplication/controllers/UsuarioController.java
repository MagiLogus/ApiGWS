package gws.api.aplication.controllers;

import gws.api.aplication.DTOs.UsuariosDTOs;
import gws.api.aplication.models.users.UsuarioModel;
import gws.api.aplication.repositories.usersrepositories.UsuariosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/usuarios")
public class UsuarioController {
    @Autowired
    UsuariosRepository usuarioRepository;

    @GetMapping
    public ResponseEntity<List<UsuarioModel>> listarUsuarios(){
        return ResponseEntity.status(HttpStatus.OK).body(usuarioRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarUsuario(@PathVariable(value = "id") UUID id){

        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        return ResponseEntity.status(HttpStatus.OK).body(usuarioBuscado.get());
    }

    @PostMapping
    public ResponseEntity<Object> criarUsuario(@RequestBody @Valid UsuariosDTOs usuariosDtos){
        if (usuarioRepository.findByEmail(usuariosDtos.email()) != null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuario já cadastrado");
        }

        UsuarioModel novoUsuario = new UsuarioModel();
        BeanUtils.copyProperties(usuariosDtos, novoUsuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(novoUsuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> editarUsuario(@PathVariable(value = "id") UUID id, @RequestBody @Valid UsuariosDTOs usuariosDTOs){
        Optional<UsuarioModel> buscandoUsuario = usuarioRepository.findById(id);

        if (buscandoUsuario.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado");
        }

        UsuarioModel usuarioEditado = new UsuarioModel();
        BeanUtils.copyProperties(usuariosDTOs, usuarioEditado);


        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRepository.save(usuarioEditado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarUsuario(@PathVariable(value = "id") UUID id){

        Optional<UsuarioModel> usuarioBuscado = usuarioRepository.findById(id);

        if (usuarioBuscado.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario não encontrado");
        }

        usuarioRepository.delete(usuarioBuscado.get());
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletado com sucesso!");

    }
}
