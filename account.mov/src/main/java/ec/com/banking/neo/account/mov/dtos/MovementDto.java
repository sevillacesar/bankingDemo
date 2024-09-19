package ec.com.banking.neo.account.mov.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @author cesarsevilla
 * @project bankingDemoJava
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class MovementDto {

    @NotNull
    @JsonProperty("tipo_movimiento")
    private String tipoMovimiento;

    @NotNull
    private Float valor;

    private Float saldo;

    @NotNull
    private String estado;

    @NotNull
    @JsonProperty("numero_cuenta")
    private String numeroCuenta;
}
