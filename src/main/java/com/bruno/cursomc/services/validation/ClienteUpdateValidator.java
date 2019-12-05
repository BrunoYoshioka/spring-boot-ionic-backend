package com.bruno.cursomc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.bruno.cursomc.dominio.Cliente;
import com.bruno.cursomc.dto.ClienteDTO;
import com.bruno.cursomc.repositories.ClienteRepository;
import com.bruno.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator</*Especificar o tipo de anotação*/ClienteUpdate, ClienteDTO> {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository repo;
	
	@Override 
    public void initialize(ClienteUpdate ann) {		
	}

	@Override     
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) { 
		
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
        List<FieldMessage> list = new ArrayList<>(); // criei uma lista vazia de FieldMessage
        
        Cliente aux = repo.findByEmail(objDto.getEmail());
        if(aux != null /*Registro Encontrado no banco*/ && !aux.getId().equals(uriId
        		)/*Verificar se id do cliente seja diferente do id que to chamando para atualizar*/) {
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