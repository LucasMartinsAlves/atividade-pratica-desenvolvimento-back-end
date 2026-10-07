package br.com.lucas.baozi.controller;

import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.converter.HttpMessageNotReadableException;

@RestControllerAdvice
public class ErrosController {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> validacao(MethodArgumentNotValidException erro) {
        var campos = erro.getBindingResult().getFieldErrors().stream()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage()).toList();
        return ResponseEntity.badRequest().body(Map.of("erro", "Dados invalidos", "campos", campos));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> jsonInvalido() {
        return ResponseEntity.badRequest().body(Map.of("erro", "JSON invalido. Confira os tipos e as datas (AAAA-MM-DD)."));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<?> vinculoExistente() {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("erro", "Registro vinculado a um pedido. Exclua o pedido primeiro."));
    }
}
