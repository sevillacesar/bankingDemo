package ec.com.banking.neo.account.mov.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author cesarsevilla
 * @project bankingDemoJava
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
public class ReporteDto {
    private String fecha;

    private String cliente;

    private String numeroCuenta;

    private String tipoCuenta;

    private String saldoInicial;

    private String estado;

    private String movimiento;

    private String saldo;
}
