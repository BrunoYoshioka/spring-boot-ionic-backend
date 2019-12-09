package com.bruno.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.bruno.cursomc.dominio.PagamentoComBoleto;

@Service
public class BoletoService {
	
	// acrescentar uma data de pagamento com 7 dias depois da data pedido
	public void PreencherPagamentoComBoleto(/*Recebendo*/ PagamentoComBoleto pagto, Date instanteDoPedido) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(cal.getTime());
	}

}
