package ec.com.banking.neo.account.mov.controllers;

import ec.com.banking.neo.account.mov.dtos.MovementDto;
import ec.com.banking.neo.account.mov.errors.AccountError;
import ec.com.banking.neo.account.mov.models.Account;
import ec.com.banking.neo.account.mov.models.BackupMovement;
import ec.com.banking.neo.account.mov.models.Movement;
import ec.com.banking.neo.account.mov.responses.BaseResponse;
import ec.com.banking.neo.account.mov.services.AccountService;
import ec.com.banking.neo.account.mov.services.BackupMovementService;
import ec.com.banking.neo.account.mov.services.MovementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.Optional;

/**
 * @author cesarsevilla
 * @project bankingDemoJava
 */

@RestController
@RequestMapping("/movements")
public class MovementsController {

    @Autowired
    private MovementService movementService;

    @Autowired
    private BackupMovementService backupMovementService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountError accountError;

    @GetMapping
    public ResponseEntity<?> listMovements() {
        return ResponseEntity.ok(movementService.listMovements());
    }

    @PostMapping
    public ResponseEntity<?> createMovement(@Valid @RequestBody MovementDto command, BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(accountError.validationErrors(result));
        }

        try {
            Optional<Account> accountOptional = accountService.numberAccount(command.getNumeroCuenta());
            if (accountOptional.isEmpty()) {
                return new ResponseEntity<>(new BaseResponse("La cuenta no existe"), HttpStatus.NOT_FOUND);
            }

            Account account = accountOptional.get();
            Optional<Movement> movementFromDb = movementService.getMovement(account.getId());

            if (movementFromDb.isPresent()) {
                Optional<Movement> movement = movementService.updateMovement(account.getNumberAccount(), command);
                if (movement.isPresent())
                    return new ResponseEntity<>(new BaseResponse("Se actualizó exitosamente"), HttpStatus.OK);
                else
                    return new ResponseEntity<>(new BaseResponse("Datos incorrectos,saldo insuficiente."),
                            HttpStatus.NOT_FOUND);
            } else {
                float balance = account.getInitialBalance() + command.getValor();

                if (balance < 0) {
                    return new ResponseEntity<>(new BaseResponse("No existe saldo suficiente"), HttpStatus.NOT_FOUND);
                }

                Movement movement = new Movement();
                movement.setMovementType(command.getTipoMovimiento());
                movement.setBalance(balance);
                movement.setStatus(command.getEstado());
                movement.setAccountId(account);
                movementService.insertMovement(movement);

                BackupMovement backupMovement = new BackupMovement();
                backupMovement.setValue(command.getValor());
                backupMovement.setDate(new Date());
                backupMovement.setMovementId(movement);
                backupMovement.setBalance(balance);
                backupMovementService.insertBackupMovement(backupMovement);
                return new ResponseEntity<>(new BaseResponse("Datos creados exitósamente"), HttpStatus.CREATED);
            }
        } catch (IllegalArgumentException e) {
            return accountError.handleBadRequest(e);
        } catch (DataAccessException e) {
            return accountError.handleInternalError(e);
        }
    }

    @PutMapping({ "/{accountNumber}" })
    public ResponseEntity<?> updateMovement(@PathVariable("accountNumber") String accountNumber,
            @Valid @RequestBody MovementDto command, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(accountError.validationErrors(result));
        }

        try {
            Optional<Movement> movement = movementService.updateMovement(accountNumber, command);
            if (movement.isPresent())
                return new ResponseEntity<>(new BaseResponse("Se actualizo exitosamente"), HttpStatus.OK);
            else
                return new ResponseEntity<>(new BaseResponse("La cuenta no existe"), HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return accountError.handleBadRequest(e);
        } catch (DataAccessException e) {
            return accountError.handleInternalError(e);
        }
    }

}
