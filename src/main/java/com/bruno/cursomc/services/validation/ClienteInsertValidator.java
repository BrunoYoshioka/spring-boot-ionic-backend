package com.bruno.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.bruno.cursomc.dominio.Cliente;
import com.bruno.cursomc.dominio.enums.TipoCliente;
import com.bruno.cursomc.dto.ClienteNewDTO;
import com.bruno.cursomc.repositories.ClienteRepository;
import com.bruno.cursomc.resources.exception.FieldMessage;
import com.bruno.cursomc.services.validation.utils.BR;

public class ClienteInsertValidator implements ConstraintValidator</*Especificar o tipo de anotação*/ClienteInsert, /*Tipo da classe que aceita a anotação*/ClienteNewDTO> {

	@Autowired
	private ClienteRepository repo;
	
	@Override 
    public void initialize(ClienteInsert ann) {		
	}

	@Override     
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) { 
 
        List<FieldMessage> list = new ArrayList<>(); // criei uma lista vazia de FieldMessage
        
        // Teste se o tipo do meu DTO for igual pessoa física E não for válido o CPF
        if(objDto.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
        	list.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
        }
        
        // Teste se o tipo do meu DTO for igual pessoa jurídica E não for válido o CNPJ
        if(objDto.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
        	list.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
        }
        
        Cliente aux = repo.findByEmail(objDto.getEmail());
        if(aux != null /*Registro Encontrado no banco*/) {
        	list.add(new FieldMessage("email", "Email já existente")); // gerei erro de validação
        }
        
        for (FieldMessage e : list) {             
        	context.disableDefaultConstraintViolation();             
        	context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
        			.addConstraintViolation();         
        }        
        return list.isEmpty(); // se a minha lista de erros tiver vazia, significa que não teve nenhum erro, então o método IsValid retorna verdadeiro, 
        // porem se houver erro, a lista não estará vazia e o método retorna falso
	}
}