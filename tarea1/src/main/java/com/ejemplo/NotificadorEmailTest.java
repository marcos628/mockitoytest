package com.ejemplo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)

public class NotificadorEmailTest {

    @Mock
    private EmailCliente emailClienteMock;

    @Test
    public void testNotificar() {
        NotificadorEmail notificador = new NotificadorEmail(emailClienteMock);
        notificador.notificar("ejemplo@test.com", "Hola Mundo");

        verify(emailClienteMock).enviarCorreo("ejemplo@test.com", "Hola Mundo");
    }

    @Test
    public void testNotificarConDireccionVacia() {
        NotificadorEmail notificador = new NotificadorEmail(emailClienteMock);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificador.notificar("", "Mensaje");
        });

        assertEquals("La dirección de correo no puede estar vacía", exception.getMessage());

        verify(emailClienteMock, times(0)).enviarCorreo(anyString(), anyString());
    }

    @Test
    public void testNotificarConMensajeNulo() {
        NotificadorEmail notificador = new NotificadorEmail(emailClienteMock);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificador.notificar("ejemplo@test.com", null);
        });

        assertEquals("El mensaje no puede ser nulo", exception.getMessage());

        verify(emailClienteMock, times(0)).enviarCorreo(anyString(), anyString());
    }

    @Test
    public void testNotificarConDireccionNula() {
        NotificadorEmail notificador = new NotificadorEmail(emailClienteMock);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            notificador.notificar(null, "Mensaje");
        });

        assertEquals("La dirección de correo no puede estar vacía", exception.getMessage());

        verify(emailClienteMock, times(0)).enviarCorreo(anyString(), anyString());
    }

    @Test
    public void testNotificarConMensajeVacio() {
        NotificadorEmail notificador = new NotificadorEmail(emailClienteMock);
        notificador.notificar("ejemplo@test.com", "");

        verify(emailClienteMock).enviarCorreo("ejemplo@test.com", "");
    }

    @Test
    public void testNotificarConDireccionYMensajeValidos() {
        NotificadorEmail notificador = new NotificadorEmail(emailClienteMock);
        notificador.notificar("ejemplo@test.com", "Mensaje de prueba");

        verify(emailClienteMock).enviarCorreo("ejemplo@test.com", "Mensaje de prueba");
    }

    @Test
    public void testNotificarCuandoEmailClienteLanzaExcepcion() {
        doThrow(new RuntimeException("Error enviando correo")).when(emailClienteMock).enviarCorreo(anyString(), anyString());

        NotificadorEmail notificador = new NotificadorEmail(emailClienteMock);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            notificador.notificar("ejemplo@test.com", "Mensaje");
        });

        assertEquals("Error enviando correo", exception.getMessage());
    }
}
