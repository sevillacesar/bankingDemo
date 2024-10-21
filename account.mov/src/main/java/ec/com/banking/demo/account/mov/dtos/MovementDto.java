package ec.com.banking.demo.account.mov.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.validation.constraints.NotNull;

/**
 * @author cesarsevilla
 * bankingDemoJava
 */
@JsonSerialize
public record MovementDto(
        @NotNull @JsonProperty("tipo_movimiento") String tipoMovimiento,
        @NotNull Float valor,
        Float saldo,
        @NotNull String estado,
        @NotNull @JsonProperty("numero_cuenta") String numeroCuenta
) {}
